package io.microprofile

import groovy.transform.ToString

@ToString(includePackage = false, includeNames = true, includeFields = true, excludes = ['metaClass'])
class DtoBlogEntry {
    String url
    String title
    String extract
    String author
    Long date
    List<String> tags
}
