package io.microprofile

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Produces('application/json')
@Path('/guardian')
class ApiGuardian {

    @Inject
    private ServiceContributor srv

    @GET
    Collection<DtoGuardian> list() {
        return srv.getGuardians()
    }

}
