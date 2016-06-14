package io.microprofile

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces

@Produces('application/json')
@Path('/contributor')
class ApiContributor {

    @Inject
    private ServiceProject project

    @Inject
    private ServiceContributor contributor

    @GET
    Collection<DtoContributorInfo> list() {
        return contributor.getContributorDetails()
    }

    @GET
    @Path('/{login}')
    DtoContributorInfo get(@PathParam("login") String login) {
        return contributor.getContributor(login)
    }

}
