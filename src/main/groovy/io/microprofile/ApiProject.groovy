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
    Collection<String> list() {
        // listing the available specs
        return srv.getAvailableProjects()
    }

    @GET
    @Path('/{projectName : .+}')
    DtoProjectDetail get(@PathParam("projectName") String projectName) {
        return srv.getDetails(projectName)
    }

    @GET
    @Path('/page/{resourcePath : .+}')
    DtoProjectPage getPage(@PathParam("resourcePath") String resourcePath) {
        def projectName = resourcePath.split('/').take(2).join('/')
        def projectResource = resourcePath.split('/').drop(2).join('/') ?: null
        return new DtoProjectPage(
                content: srv.getProjectPage(projectName, projectResource)
        )
    }

    @GET
    @Path('/raw/{resourcePath : .+}')
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    Response getFile(@PathParam("resourcePath") String resourcePath) {
        def projectName = resourcePath.split('/').take(2).join('/')
        def projectResource = resourcePath.split('/').drop(2).join('/') ?: null
        byte[] data = srv.getRaw(projectName, projectResource)
        return Response.ok(data).build()
    }

}
