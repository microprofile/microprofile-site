describe("testing the home page", function () {
    var $compile;
    var $rootScope;
    var $httpBackend;

    beforeEach(module('javaeeio-main'));

    beforeEach(inject(function (_$compile_, _$rootScope_) {
        $compile = _$compile_;
        $rootScope = _$rootScope_;
    }));

    beforeEach(inject(function ($injector) {
        // Set up the mock http service responses
        $httpBackend = $injector.get('$httpBackend');
    }));

    afterEach(function () {
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.verifyNoOutstandingRequest();
    });

    it('should have work', function () {
        expect('testing').toContain('testing');
    });
});