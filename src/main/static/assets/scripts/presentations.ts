///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('microprofileio-presentations', [])

    .directive('microprofileioPresentations', [function () {
        return {
            restrict: 'A',
            scope: {},
            templateUrl: 'app/templates/dir_presentations.html'
        };
    }])

    .run(function () {
        // placeholder
    });
