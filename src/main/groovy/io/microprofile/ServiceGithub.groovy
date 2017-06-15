package io.microprofile

import groovy.json.JsonSlurper
import org.tomitribe.sabot.Config
import org.yaml.snakeyaml.Yaml

import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Singleton
import javax.inject.Inject
import java.nio.charset.StandardCharsets
import java.util.logging.Level
import java.util.logging.Logger

@Singleton
@Lock(LockType.READ)
class ServiceGithub {
    private Logger logger = Logger.getLogger(this.class.name)

    @Inject
    @Config(value = 'microprofile_config_root')
    private String docRoot

    @Inject
    private ServiceApplication application

    private def removeBranch = { String projectName ->
        return projectName.split(':').with {
            return it[0]
        }
    }

    private def getBranch = { String projectName ->
        return projectName.split(':').with {
            if(it.length > 1) {
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
        def resourcePath = resourceName.split(':')
        return "https://api.github.com/repos/${removeBranch projectName}/contents/${resourcePath[0]}?ref=${getBranch projectName}".toURL().getBytes([
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
        def json = new JsonSlurper().parseText(
                "https://api.github.com/users/${login}".toURL().getText([
                        requestProperties: [
                                'Accept'       : 'application/vnd.github.v3+json',
                                'Authorization': "token ${application.githubAuthToken}"
                        ]
                ], StandardCharsets.UTF_8.name())
        )
        return new DtoContributorInfo(
                login: json.login as String,
                name: json.name as String,
                avatar: json.avatar_url as String,
                company: json.company as String,
                location: json.location as String
        )
    }

}
