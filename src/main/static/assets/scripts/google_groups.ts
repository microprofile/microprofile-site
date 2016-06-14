///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('javaeeio-googlegroups', [])

    .factory('eeioGooglegroupsService', [
        '$http',
        function ($http) {
            return {
                getMessages: function () {
                    return $http.get('api/googlegroups/messages');
                }
            };
        }
    ])

    .directive('eeioGooglegroupsShortlist', [function () {
        return {
            restrict: 'E',
            scope: {},
            templateUrl: 'app/templates/dir_googlegroups_shortlist.html',
            controller: ['$scope', '$timeout', 'eeioGooglegroupsService', function ($scope, $timeout, googlegroups) {
                googlegroups.getMessages().then(function (response) {
                    $timeout(function () {
                        $scope.$apply(function () {
                            $scope.messages = response.data;
                        });
                    });
                });
            }]
        };
    }])

    .run(function () {
        // placeholder
    });
