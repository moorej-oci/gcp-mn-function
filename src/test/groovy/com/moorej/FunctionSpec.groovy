package com.moorej;
import spock.lang.*

class FunctionSpec extends Specification {

    @Shared @AutoCleanup
    Function function = new Function()

    void "test function"() {
        expect:"The function executes correctly"
        true // test logic here
    }
}
