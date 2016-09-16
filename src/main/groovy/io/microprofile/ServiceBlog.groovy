package io.microprofile

import org.tomitribe.sabot.Config

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class ServiceBlog {

    @Inject
    @Config(value = 'microprofile_blog_root')
    private String blobProject

    @Inject
    private ServiceGithub srv

    String getPage(String resource) {
        return srv.getRepoPage(blobProject, resource)
    }

    byte[] getFile(String resource) {
        return srv.getRepoRaw(blobProject, resource)
    }

}

