package io.microprofile

import org.tomitribe.sabot.Config

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Produces('application/json')
@Path('/system')
class ApiSystem {

    @Inject
    @Config(value = 'microprofile_config_root')
    private String docRoot

    @Inject
    @Config(value = 'microprofile_blog_root')
    private String blogRoot

    @GET
    DtoSystemInfo getInfo() {
        return new DtoSystemInfo(
                configProject: docRoot,
                blogProject: blogRoot
        )
    }

}
