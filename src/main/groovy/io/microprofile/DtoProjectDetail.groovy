package io.microprofile

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString(includePackage = false, includeNames = true, includeFields = true)
class DtoProjectDetail {
    DtoProjectInfo info
    Collection<DtoProjectContributor> contributors
}
