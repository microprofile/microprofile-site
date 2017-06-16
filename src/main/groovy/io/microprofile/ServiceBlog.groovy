package io.microprofile

import org.tomitribe.sabot.Config

import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Singleton
import javax.inject.Inject

@Singleton
@Lock(LockType.READ)
class ServiceBlog {

    @Inject
    @Config(value = 'microprofile_blog_root')
    private String blobProject

    @Inject
    private ServiceGithub srv

    @Cached
    String getPage(String resource) {
        return srv.getRepoPage(blobProject, resource)
    }

    @Cached
    byte[] getFile(String resource) {
        return srv.getRepoRaw(blobProject, resource)
    }

}

