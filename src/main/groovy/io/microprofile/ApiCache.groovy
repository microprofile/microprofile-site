package io.microprofile

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

@Produces('application/json')
@Path('/cache')
class ApiCache {

    @Inject
    private CachedHolder cache

    @GET
    @Path('/clear')
    Response clear() {
        cache.clearCache()
        return Response.ok().build()
    }

}
