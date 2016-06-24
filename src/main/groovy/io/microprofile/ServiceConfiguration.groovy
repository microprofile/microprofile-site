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
        setIfEmpty('google_forum_url', 'https://groups.google.com/forum/feed/microprofile/msgs/atom.xml?num=50')
        setIfEmpty('microprofile_config_root', {
            def result = System.getProperty('microprofile_config_root')
            if (!result) {
                return 'microprofile/microprofile.io'
            }
            return result
        })
        setIfEmpty('github_atoken', System.getProperty('microprofile_github_atoken'))
        setIfEmpty('microprofile_twitter_oauth_consumer_key', System.getProperty('microprofile_twitter_oauth_consumer_key'))
        setIfEmpty('microprofile_twitter_oauth_consumer_secret', System.getProperty('microprofile_twitter_oauth_consumer_secret'))
        setIfEmpty('microprofile_twitter_oauth_access_token', System.getProperty('microprofile_twitter_oauth_access_token'))
        setIfEmpty('microprofile_twitter_oauth_access_token_secret', System.getProperty('microprofile_twitter_oauth_access_token_secret'))
    }
}
