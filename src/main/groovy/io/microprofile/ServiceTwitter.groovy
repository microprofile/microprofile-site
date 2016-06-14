package io.microprofile

import org.tomitribe.sabot.Config
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder

import javax.annotation.PostConstruct
import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Singleton
import javax.inject.Inject

@Singleton
@Lock(LockType.READ)
class ServiceTwitter {

    private Twitter twitter

    @Inject
    @Config(value = 'javaeeio_twitter_oauth_consumer_key')
    private String oAuthConsumerKey

    @Inject
    @Config(value = 'javaeeio_twitter_oauth_consumer_secret')
    private String oAuthConsumerSecret

    @Inject
    @Config(value = 'javaeeio_twitter_oauth_access_token')
    private String oAuthAccessToken

    @Inject
    @Config(value = 'javaeeio_twitter_oauth_access_token_secret')
    private String oAuthAccessTokenSecret

    @PostConstruct
    void init() {
        def cb = new ConfigurationBuilder()
        cb.setDebugEnabled(false)
                .setOAuthConsumerKey(oAuthConsumerKey)
                .setOAuthConsumerSecret(oAuthConsumerSecret)
                .setOAuthAccessToken(oAuthAccessToken)
                .setOAuthAccessTokenSecret(oAuthAccessTokenSecret)
        def tf = new TwitterFactory(cb.build())
        this.twitter = tf.getInstance()
    }

    @Cached
    DtoTwitterUser getUser(String name) {
        def raw = this.twitter.showUser(name)
        return new DtoTwitterUser(
                screenName: raw.screenName,
                name: raw.name,
                pic: raw.biggerProfileImageURLHttps
        )
    }

    @Cached
    Collection<DtoTweet> getTweets() {
        return this.twitter.getUserTimeline('javaee_guardian').collect {
            def tweet = it.retweetedStatus ?: it
            return new DtoTweet(
                    id: tweet.id,
                    authorName: tweet.user.name,
                    author: tweet.user.screenName,
                    message: tweet.text,
                    date: tweet.createdAt.format("yyyy-MM-dd'T'HH:mm:ssZ"),
                    image: tweet.user.profileImageURLHttps
            )
        }
    }

}
