package io.microprofile

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Produces('application/json')
@Path('/project')
class ApiProject {

    @Inject
    private ServiceProject srv

    @GET
    Collection<DtoProjectInfo> list() {
        // listing the available specs
        return srv.availableProjects
    }

    @GET
    @Path('/{configFile}')
    DtoProjectDetail get(@PathParam("configFile") String configFile) {
        return srv.getDetails(configFile)
    }

    @GET
    @Path('/page/{configFile}/{projectResource : .+}')
    DtoProjectPage getPage(@PathParam("configFile") String configFile,
                           @PathParam("projectResource") String projectResource) {
        return new DtoProjectPage(
                content: srv.getProjectPage(configFile, projectResource)
        )
    }

    @GET
    @Path('/page/{configFile}/')
    DtoProjectPage getPage(@PathParam("configFile") String configFile) {
        return new DtoProjectPage(
                content: srv.getProjectPage(configFile, null)
        )
    }

    @GET
    @Path('/raw/{configFile}/{projectResource : .+}')
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    Response getFile(@PathParam("configFile") String configFile,
                     @PathParam("projectResource") String projectResource) {
        byte[] data = srv.getRaw(configFile, projectResource)
        return Response.ok(data).build()
    }

}
