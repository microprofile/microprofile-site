///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('microprofileio-action', [])

    .directive('microprofileioCardAction', [function () {
        return {
            restrict: 'A',
            scope: {},
            templateUrl: 'app/templates/dir_card_action.html'
        };
    }])

    .run(function () {
        // placeholder
    });
