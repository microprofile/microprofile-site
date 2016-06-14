package io.microprofile

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString(includePackage = false, includeNames = true, includeFields = true, excludes = ['metaClass'])
class DtoContributorInfo {
    String login
    String name
    String avatar
    String company
    String location
    Collection<String> projects
    int contributions
}
