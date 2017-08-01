package io.microprofile

import groovy.transform.ToString

@ToString(includePackage = false, includeNames = true, includeFields = true, excludes = ['metaClass'])
class DtoSystemInfo {
    String configProject
    String blogProject
}
