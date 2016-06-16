package io.microprofile

import org.tomitribe.sabot.Config

import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Singleton
import javax.inject.Inject

@Singleton
@Lock(LockType.READ)
class ServiceContributor {

    @Inject
    private ServiceGithub github

    @Inject
    private ServiceProject project

    @Inject
    @Config(value = 'microprofile_config_root')
    private String docRoot

    DtoContributorInfo getContributor(String login) {
        return github.getContributor(login)
    }

    @Cached
    Collection<DtoContributorInfo> getContributorDetails() {
        Map<String, DtoContributor> contributions = project.allContributors.collectEntries {
            [(it.login): it]
        }
        return project.allContributors.collect { contributor ->
            def info = getContributor(contributor.login)
            def userContributions = contributions.get(contributor.login)
            info.projects = userContributions.projects
            info.contributions = userContributions.contributions
            info
        }
    }

}

