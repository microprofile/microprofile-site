package io.microprofile

import org.yaml.snakeyaml.Yaml

import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Singleton
import javax.inject.Inject
import java.nio.charset.StandardCharsets

@Singleton
@Lock(LockType.READ)
class ServiceProject {

    @Inject
    private ServiceGithub github

    private def loadYaml(String projectName) {
        try {
            return new Yaml().load(new String(
                    github.getRepoRaw(projectName, 'site.yaml'), StandardCharsets.UTF_8.name()
            ))
        } catch (FileNotFoundException ignore) {
            //
        }
        return null
    }

    private DtoProjectInfo getDtoProjectInfo(String projectName) {
        def conf = loadYaml(projectName)
        if (!conf) {
            return new DtoProjectInfo(
                    name: projectName,
                    description: github.getRepoDescription(projectName) ?: ''
            )
        }
        return new DtoProjectInfo(
                name: projectName,
                friendlyName: conf.friendly_name as String,
                description: github.getRepoDescription(projectName) ?: '',
                home: conf.home as String,
                resources: conf.resources?.collect { resource ->
                    def dto = new DtoProjectResource()
                    if (String.class.isInstance(resource)) {
                        dto.url = resource
                    } else {
                        dto.url = resource.url
                        dto.title = resource.title
                    }
                    return dto
                },
                related: conf.related
        )
    }

    @Cached
    Collection<String> getAvailableProjects() {
        return github.getPublishedProjects()
    }

    @Cached
    DtoProjectDetail getDetails(String projectName) {
        DtoProjectInfo info = getDtoProjectInfo(projectName)
        Set<DtoProjectContributor> contributors = github.getRepoContributors(projectName)
        info.related.each { relatedIt ->
            contributors.addAll(github.getRepoContributors(relatedIt))
        }
        return new DtoProjectDetail(
                info: info,
                contributors: contributors
        )
    }

    String getApplicationPage(String resourceName) {
        return github.getAppPage(resourceName)
    }

    String getProjectPage(String projectName, String resourceName) {
        def conf = loadYaml(projectName)
        def computedResourceName = resourceName
        if (!computedResourceName) {
            computedResourceName = conf?.home as String
        }
        if (!computedResourceName) {
            computedResourceName = 'README.adoc'
        }
        return github.getRepoPage(projectName, computedResourceName)
    }

    byte[] getApplicationRaw(String resourceName) {
        return github.getAppRaw(resourceName)
    }

    byte[] getRaw(String projectName, String resourceName) {
        return github.getRepoRaw(projectName, resourceName)
    }

    Collection<DtoContributor> getAllContributors() {
        Map<String, DtoContributor> contributors = [:]
        github.getPublishedProjects().each { project ->
            def details = getDetails(project)
            details.contributors.each { projContributor ->
                DtoContributor contributor = contributors.get(projContributor.login)
                if (!contributor) {
                    contributor = new DtoContributor(
                            login: projContributor.login
                    )
                    contributors.put(projContributor.login, contributor)
                }
                contributor.projects << project
                contributor.contributions += projContributor.contributions
            }
        }
        return contributors.values() as List<DtoContributor>
    }

}

