package io.microprofile

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString(includePackage = false, includeNames = true, includeFields = true)
class DtoContributor {
    String login
    Collection<String> projects = new HashSet<>()
    int contributions = 0
}
