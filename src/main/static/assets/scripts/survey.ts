///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('microprofileio-survey', [])

    .directive('eeioCardSurvey', [function () {
        return {
            restrict: 'A',
            scope: {},
            templateUrl: 'app/templates/dir_card_survey.html'
        };
    }])

    .run(function () {
        // placeholder
    });
