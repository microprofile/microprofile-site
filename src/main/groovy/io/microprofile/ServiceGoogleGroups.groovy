package io.microprofile

import org.tomitribe.sabot.Config

import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Singleton
import javax.inject.Inject
import java.nio.charset.StandardCharsets

@Singleton
@Lock(LockType.READ)
class ServiceGoogleGroups {

    @Inject
    @Config(value = 'google_forum_url')
    private String url

    @Cached
    Collection<DtoGroupMessage> listMessages() {
        String xmlText = url.toURL().getText([:], StandardCharsets.UTF_8.name())
        def xml = new XmlSlurper().parseText(xmlText)
        return xml.entry.collect {
            return new DtoGroupMessage(
                    id: it.id.text(),
                    author: it.author.text(),
                    updated: it.updated.text(),
                    title: it.title.text(),
                    summary: it.summary.text()
            )
        }
    }
}

