package io.microprofile

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Produces('application/json')
@Path('/application')
class ApiPage {

    @Inject
    private ServiceProject srv

    @GET
    @Path('/page/{projectResource : .+}')
    @Produces('text/html')
    String getPage(@PathParam("projectResource") String projectResource) {
        return srv.getApplicationPage(projectResource)
    }

    @GET
    @Path('/raw/{projectResource : .+}')
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    Response getFile(@PathParam("projectResource") String projectResource) {
        byte[] data = srv.getApplicationRaw(projectResource)
        return Response.ok(data).build()
    }

    @GET
    @Path('/page-header/{projectResource : .+}')
    @Produces(MediaType.APPLICATION_JSON)
    DtoPageHeader getPageHeader(@PathParam("projectResource") String projectResource) {
        return srv.getApplicationPageHeader(projectResource)
    }

}
