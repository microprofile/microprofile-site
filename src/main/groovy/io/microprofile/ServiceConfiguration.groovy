package io.microprofile

import org.tomitribe.sabot.ConfigurationObserver
import org.tomitribe.sabot.ConfigurationResolver

import javax.ejb.Singleton

@Singleton
class ServiceConfiguration implements ConfigurationObserver {

    static {
        // https://github.com/tomitribe/sabot
        ConfigurationResolver.registerConfigurationObserver(new ServiceConfiguration());
    }

    @Override
    void mergeConfiguration(Properties properties) {
        def setIfEmpty = { String key, def value ->
            if (!properties.getProperty(key) && value) {
                String propVal
                if (String.class.isInstance(value)) {
                    propVal = value as String
                } else {
                    propVal = value()
                }
                properties.setProperty(key, propVal)
            }
        }
        setIfEmpty('google_forum_url', 'https://groups.google.com/forum/feed/microprofile-guardians/topics/atom.xml?num=50')
        setIfEmpty('microprofileio_config_root', {
            def result = System.getProperty('microprofileio_config_root')
            if (!result) {
                return 'jcpevangelists/microprofile.io.config'
            }
            return result
        })
        setIfEmpty('github_atoken', System.getProperty('microprofileio_github_atoken'))
        setIfEmpty('microprofileio_twitter_oauth_consumer_key', System.getProperty('microprofileio_twitter_oauth_consumer_key'))
        setIfEmpty('microprofileio_twitter_oauth_consumer_secret', System.getProperty('microprofileio_twitter_oauth_consumer_secret'))
        setIfEmpty('microprofileio_twitter_oauth_access_token', System.getProperty('microprofileio_twitter_oauth_access_token'))
        setIfEmpty('microprofileio_twitter_oauth_access_token_secret', System.getProperty('microprofileio_twitter_oauth_access_token_secret'))
    }
}
