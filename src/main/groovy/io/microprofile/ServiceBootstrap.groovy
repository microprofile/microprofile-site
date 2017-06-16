package io.microprofile

import javax.annotation.PostConstruct
import javax.ejb.Singleton
import javax.ejb.Startup
import javax.inject.Inject

@Singleton
@Startup
class ServiceBootstrap {

    @Inject
    private ServiceContributor contributor

    @PostConstruct
    void init() {
        contributor.getContributorDetails()
    }
}

