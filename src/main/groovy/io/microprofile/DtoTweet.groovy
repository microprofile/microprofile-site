package io.microprofile

import groovy.transform.ToString

@ToString(includePackage = false, includeNames = true, includeFields = true, excludes = ['metaClass'])
class DtoTweet {
    String id
    String authorName
    String author
    String message
    String date
    String image
}