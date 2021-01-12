package io.microprofile

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString(includePackage = false, includeNames = true, includeFields = true)
class DtoProjectContributor {
    String login
    int contributions

    boolean equals(o) {
        if (this.is(o)) {
            return true
        }
        if (getClass() != o.class) {
            return false
        }
        DtoProjectContributor that = (DtoProjectContributor) o
        if (login != that.login) {
            return false
        }
        return true
    }

    int hashCode() {
        return login.hashCode()
    }
}
