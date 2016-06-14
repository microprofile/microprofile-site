package io.microprofile

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Produces('application/json')
@Path('/specs')
class ApiSpecs {

    @Inject
    private ServiceProject srv

    @GET
    Collection<DtoProjectInfo> list() {
        // listing the available specs
        return srv.availableProjects.findAll { it.spec || it.related != null }
    }

}
