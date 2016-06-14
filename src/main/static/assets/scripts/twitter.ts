///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('javaeeio-twitter', [])

    .factory('eeioTwitterService', [
        '$http',
        function ($http) {
            return {
                getMessages: function () {
                    return $http.get('api/twitter');
                }
            };
        }
    ])

    .directive('eeioTwitterShortlist', [function () {
        return {
            restrict: 'E',
            scope: {},
            templateUrl: 'app/templates/dir_twitter_shortlist.html',
            controller: ['$scope', '$timeout', 'eeioTwitterService', function ($scope, $timeout, twitterService) {
                twitterService.getMessages().then(function (response) {
                    $timeout(function () {
                        $scope.$apply(function () {
                            $scope.tweets = response.data;
                        });
                    });
                });
            }]
        };
    }])

    .run(function () {
        // placeholder
    });
