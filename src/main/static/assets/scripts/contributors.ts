///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('microprofileio-contributors', [])

    .factory('microprofileioContributorsService', [
        '$http',
        function ($http) {
            return {
                getContributors: function () {
                    return $http.get('api/contributor');
                },
                getContributor: function (login) {
                    return $http.get('api/contributor/' + login);
                }
            };
        }
    ])

    .directive('microprofileioContributorSummary', [function () {
        return {
            restrict: 'E',
            scope: {
                login: '='
            },
            templateUrl: 'app/templates/dir_contributors_contributor_summary.html',
            controller: ['$scope', '$timeout', 'microprofileioContributorsService', function ($scope, $timeout, contributorsService) {
                contributorsService.getContributor($scope.login).then(function (response) {
                    $timeout(function () {
                        $scope.$apply(function () {
                            $scope.contributor = response.data;
                        });
                    });
                });
            }]
        };
    }])

    .directive('microprofileioContributorsList', [function () {
        return {
            restrict: 'E',
            scope: {
                login: '='
            },
            templateUrl: 'app/templates/dir_contributors_contributors_list.html',
            controller: ['$scope', '$timeout', 'microprofileioContributorsService', function ($scope, $timeout, contributorsService) {
                contributorsService.getContributors().then(function (response) {
                    $timeout(function () {
                        $scope.$apply(function () {
                            $scope.contributors = response.data;
                        });
                    });
                });
            }]
        };
    }])

    .run(function () {
        // placeholder
    });
