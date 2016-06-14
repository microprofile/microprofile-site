///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('microprofileio-footer', [])

    .directive('eeioFooter', [function () {
        return {
            restrict: 'E',
            scope: {
                login: '='
            },
            templateUrl: 'app/templates/dir_footer.html'
        };
    }])

    .run(function () {
        // placeholder
    });
