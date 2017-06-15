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
        setIfEmpty('microprofile_config_root', {
            def result = System.getProperty('microprofile_config_root')
            if (!result) {
                return 'microprofile/microprofile.io'
            }
            return result
        })
        setIfEmpty('microprofile_github_atoken', System.getProperty('microprofile_github_atoken'))
    }
}
