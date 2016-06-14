package io.microprofile

import groovy.transform.ToString

@ToString(includePackage = false, includeNames = true, includeFields = true, excludes = ['metaClass'])
class DtoGroupMessage {
    String id
    String author
    String updated
    String title
    String summary
}
