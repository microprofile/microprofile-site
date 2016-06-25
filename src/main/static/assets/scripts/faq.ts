///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('microprofileio-faq', [])

    .directive('eeioFaqQuestions', [function () {
        return {
            restrict: 'A',
            scope: {},
            templateUrl: 'app/templates/dir_faq_questions.html'
        };
    }])

    .run(function () {
        // placeholder
    });
