package io.microprofile

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString(includePackage = false, includeNames = true, includeFields = true)
class DtoProjectResource {
    String url
    String title
}
