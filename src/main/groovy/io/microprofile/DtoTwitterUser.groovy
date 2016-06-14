package io.microprofile

import groovy.transform.ToString

@ToString(includePackage = false, includeNames = true, includeFields = true, excludes = ['metaClass'])
class DtoTwitterUser {
    String screenName
    String name
    String pic
}
