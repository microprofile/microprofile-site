///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('javaeeio-guardians', [])

    .factory('eeioGuardiansService', [
        '$http',
        function ($http) {
            return {
                getGuardians: function () {
                    return $http.get('api/guardian');
                }
            };
        }
    ])

    .directive('eeioGuardiansList', [function () {
        return {
            restrict: 'E',
            scope: {},
            templateUrl: 'app/templates/dir_guardians_list.html',
            controller: ['$scope', '$timeout', 'eeioGuardiansService', function ($scope, $timeout, srv) {
                srv.getGuardians().then(function (response) {
                    $timeout(function () {
                        $scope.$apply(function () {
                            $scope.guardians = response.data;
                        });
                    });
                });
            }]
        };
    }])

    .run(function () {
        // placeholder
    });
