package io.microprofile

import groovy.json.JsonSlurper
import org.tomitribe.sabot.Config
import org.yaml.snakeyaml.Yaml

import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Singleton
import javax.inject.Inject
import java.nio.charset.StandardCharsets
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.logging.Level
import java.util.logging.Logger

@Singleton
@Lock(LockType.READ)
class ServiceGithub {
    private Logger logger = Logger.getLogger(this.class.name)

    private static final DateFormat DT_FORM = new SimpleDateFormat('yyyy-MM-dd')

    @Inject
    @Config(value = 'microprofile_config_root')
    private String docRoot

    @Inject
    @Config(value = 'microprofile_blog_root')
    private String blogRoot

    @Inject
    private ServiceApplication application

    private def removeBranch = { String projectName ->
        return projectName.split(':').with {
            return it[0]
        }
    }

    private def getBranch = { String projectName ->
        return projectName.split(':').with {
            if (it.length > 1) {
                return it[1]
            }
            return 'master'
        }
    }

    @Cached
    List<String> getPublishedProjects() {
        return new Yaml().load(new String(
                getRepoRaw(docRoot, 'site.yaml'), StandardCharsets.UTF_8.name()
        )).projects
    }

    @Cached
    List<DtoBlogEntry> getBlogEntries() {
        return new Yaml().load(new String(
                getRepoRaw(blogRoot, 'config.yaml'), StandardCharsets.UTF_8.name()
        )).blog.collect({
            new DtoBlogEntry(
                    url: it.url as String,
                    title: it.title as String,
                    extract: it.extract as String,
                    author: it.author as String,
                    date: DT_FORM.parse(it.date as String).time,
                    tags: it.tags,
                    image: it.image
            )
        })
    }

    @Cached
    String getRepoDescription(String projectName) {
        if (!projectName) {
            return null
        }
        def json = new JsonSlurper().parseText(
                "https://api.github.com/repos/${removeBranch projectName}".toURL().getText([
                        requestProperties: [
                                'Accept'       : 'application/vnd.github.v3+json',
                                'Authorization': "token ${application.githubAuthToken}"
                        ]
                ], StandardCharsets.UTF_8.name())
        )
        return json.description as String
    }

    @Cached
    Collection<DtoProjectContributor> getRepoContributors(String projectName) {
        if (!projectName) {
            return []
        }
        def json = new JsonSlurper().parseText(
                "https://api.github.com/repos/${removeBranch projectName}/contributors".toURL().getText([
                        requestProperties: [
                                'Accept'       : 'application/vnd.github.v3+json',
                                'Authorization': "token ${application.githubAuthToken}"
                        ]
                ], StandardCharsets.UTF_8.name())
        )
        return json.collect {
            new DtoProjectContributor(
                    login: it.login as String,
                    contributions: it.contributions as Integer
            )
        }
    }

    @Cached
    String getAppPage(String resourceName) {
        return getRepoPage(docRoot, 'pages/' + resourceName)
    }

    @Cached
    String getRepoPage(String projectName, String resourceName) {
        try {
            return "https://api.github.com/repos/${removeBranch projectName}/contents/${resourceName}?ref=${getBranch projectName}".toURL().getText([
                    requestProperties: [
                            'Accept'       : 'application/vnd.github.v3.html',
                            'Authorization': "token ${application.githubAuthToken}"
                    ]
            ], StandardCharsets.UTF_8.name())
        } catch (FileNotFoundException fnf) {
            logger.log(Level.FINE, "The project ${projectName} has no document named ${resourceName}", fnf)
            return ''
        }
    }

    @Cached
    byte[] getRepoRaw(String projectName, String resourceName) {
        def resourcePath = resourceName.split(':')[0].split('/') as List<String>
        def resourceDir = resourcePath.subList(0, resourcePath.size() - 1).join('/')
        def dirInfo = "https://api.github.com/repos/${removeBranch projectName}/contents/${resourceDir}".toURL().getText([
                requestProperties: [
                        'Accept'       : 'application/vnd.github.v3+json',
                        'Authorization': "token ${application.githubAuthToken}"
                ]
        ], StandardCharsets.UTF_8.name())
        def fileInfo = new JsonSlurper().parseText(dirInfo).find({
            it.path == resourcePath.join('/')
        })
        if (!fileInfo) {
            throw new FileNotFoundException("$resourceName not found")
        }
        return "https://api.github.com/repos/${removeBranch projectName}/git/blobs/${fileInfo.sha}".toURL().getBytes([
                requestProperties: [
                        'Accept'       : 'application/vnd.github.v3.raw',
                        'Authorization': "token ${application.githubAuthToken}"
                ]
        ])
    }

    @Cached
    byte[] getAppRaw(String resourceName) {
        return getRepoRaw(docRoot, 'pages/' + resourceName)
    }

    @Cached
    DtoContributorInfo getContributor(String login) {
        def json
        try {
            json = new JsonSlurper().parseText(
                    "https://api.github.com/users/${login}".toURL().getText([
                            requestProperties: [
                                    'Accept'       : 'application/vnd.github.v3+json',
                                    'Authorization': "token ${application.githubAuthToken}"
                            ]
                    ], StandardCharsets.UTF_8.name())
            )
        } catch (IOException ignore) {
            json = [
                    login: login,
                    name : login
            ]
        }
        return new DtoContributorInfo(
                login: json.login as String,
                name: json.name as String,
                avatar: json.avatar_url as String,
                company: json.company as String,
                location: json.location as String
        )
    }

}
