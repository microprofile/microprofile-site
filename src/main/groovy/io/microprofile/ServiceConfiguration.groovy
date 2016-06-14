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
        setIfEmpty('google_forum_url', 'https://groups.google.com/forum/feed/javaee-guardians/topics/atom.xml?num=50')
        setIfEmpty('javaeeio_config_root', {
            def result = System.getProperty('javaeeio_config_root')
            if (!result) {
                return 'jcpevangelists/javaee.io.config'
            }
            return result
        })
        setIfEmpty('github_atoken', System.getProperty('javaeeio_github_atoken'))
        setIfEmpty('javaeeio_twitter_oauth_consumer_key', System.getProperty('javaeeio_twitter_oauth_consumer_key'))
        setIfEmpty('javaeeio_twitter_oauth_consumer_secret', System.getProperty('javaeeio_twitter_oauth_consumer_secret'))
        setIfEmpty('javaeeio_twitter_oauth_access_token', System.getProperty('javaeeio_twitter_oauth_access_token'))
        setIfEmpty('javaeeio_twitter_oauth_access_token_secret', System.getProperty('javaeeio_twitter_oauth_access_token_secret'))
    }
}
