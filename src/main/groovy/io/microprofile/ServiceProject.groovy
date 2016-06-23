package io.microprofile

import org.yaml.snakeyaml.Yaml

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import java.util.logging.Level
import java.util.logging.Logger

@ApplicationScoped
class ServiceProject {
    private Logger logger = Logger.getLogger(this.class.name)

    @Inject
    private ServiceGithub github

    private def loadYaml(DtoConfigFile configFile) {
        try {
            return new Yaml().load(configFile.content)
        } catch (e) {
            logger.log(Level.WARNING, "Invalid yaml file: '${configFile.name}'", e)
        }
        return null
    }

    private DtoProjectInfo getDtoProjectInfo(String configFile) {
        DtoProjectInfo info = getAvailableProjects().find { it.configFile == configFile }
        if (!info) {
            throw new ExceptionApplication("Project not found: '${configFile}'")
        }
        return info
    }

    Collection<DtoProjectInfo> getAvailableProjects() {
        Set<DtoProjectInfo> result = []
        github.getConfigurationFiles().each {
            def conf = loadYaml(it)
            if (!conf) {
                return
            }
            result << new DtoProjectInfo(
                    configFile: it.name,
                    name: conf.name as String,
                    friendlyName: conf.friendly_name as String,
                    description: conf.name ? github.getRepoDescription(conf.name as String) : '',
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
                    spec: conf.spec != null ? conf.spec : false,
                    related: conf.related
            )
        }
        return result
    }

    DtoProjectDetail getDetails(String configFile) {
        DtoProjectInfo info = getDtoProjectInfo(configFile)
        def conf = loadYaml(github.getConfigurationFiles().find {
            it.name == configFile
        })
        Set<DtoProjectContributor> contributors = github.getRepoContributors(conf.name as String)
        info.related.each { relatedIt ->
            def relatedConf = loadYaml(github.getConfigurationFiles().find {
                it.name == relatedIt
            })
            contributors.addAll(github.getRepoContributors(relatedConf.name))
        }
        return new DtoProjectDetail(
                info: info,
                contributors: contributors
        )
    }

    String getApplicationPage(String resourceName) {
        return github.getAppPage(resourceName)
    }

    DtoPageHeader getApplicationPageHeader(String resourceName) {
        def name = resourceName + ".yaml"
        DtoConfigFile cfgFile
        try {
            cfgFile = github.getConfigurationFile(name, "pages/${name}")
        } catch (FileNotFoundException e) {
            logger.log(Level.CONFIG, "Configuration yaml file '${name}' for page ${resourceName} not found, "
                    + "using defaults", e)
            return new DtoPageHeader(
                    h1: "DESIGN BETTER. FASTER. TOGETHER.",
                    h2: "The world's leading prototyping, collaboration & workflow platform"
            )
        }
        def conf = loadYaml(cfgFile)
        if (!conf) {
            return null
        } else {
            return new DtoPageHeader(
                    h1: conf.h1 as String,
                    h2: conf.h2 as String
            )
        }
    }

    String getProjectPage(String configFile, String resourceName) {
        def conf = loadYaml(github.getConfigurationFiles().find {
            it.name == configFile
        })
        def computedResourceName = resourceName
        if (!computedResourceName) {
            computedResourceName = conf.home as String
        }
        if (!computedResourceName) {
            computedResourceName = 'README.adoc'
        }
        return github.getRepoPage(conf.name as String, computedResourceName)
    }


    byte[] getApplicationRaw(String resourceName) {
        return github.getAppRaw(resourceName)
    }

    byte[] getRaw(String configFile, String resourceName) {
        DtoProjectInfo info = getDtoProjectInfo(configFile)
        return github.getRepoRaw(info.name, resourceName)
    }

    Collection<DtoContributor> getAllContributors() {
        Map<String, DtoContributor> contributors = [:]
        getAvailableProjects().each { project ->
            def details = getDetails(project.configFile)
            details.contributors.each { projContributor ->
                DtoContributor contributor = contributors.get(projContributor.login)
                if (!contributor) {
                    contributor = new DtoContributor(
                            login: projContributor.login
                    )
                    contributors.put(projContributor.login, contributor)
                }
                contributor.projects << project.name
                contributor.contributions += projContributor.contributions
            }
        }
        return contributors.values() as List<DtoContributor>
    }

}

