package io.microprofile

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Produces('application/json')
@Path('/blog')
class ApiBlog {

    @Inject
    private ServiceBlog srv

    @GET
    @Path('/html/{resource : .+}')
    DtoProjectPage getPage(@PathParam("resource") String resource) {
        return new DtoProjectPage(
                content: srv.getPage(resource)
        )
    }

    @GET
    @Path('/raw/{resource : .+}')
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    Response getFile(@PathParam("resource") String resource) {
        byte[] data = srv.getFile(resource)
        return Response.ok(data).build()
    }


    @GET
    Collection<DtoBlogEntry> list() {
        return srv.list()
    }

}
