///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('microprofileio-twitter', [])

    .directive('eeioCardTwitter', [function () {
        return {
            restrict: 'A',
            scope: {},
            templateUrl: 'app/templates/dir_card_twitter.html',
            controller: ['$scope', '$timeout', 'eeioTwitterService', function ($scope, $timeout, eeioTwitterService) {
                eeioTwitterService.getTweets(function (data) {
                    $timeout(function () {
                        $scope.$apply(function () {
                            // author, authorName, date, id, image, message
                            $scope.tweets = data.data;
                        });
                    });
                });
            }]
        };
    }])

    .factory('eeioTwitterService', ['$http', function ($http) {
        return {
            getTweets: function (success) {
                $http.get('api/twitter').then(success);
            }
        };
    }])

    .run(function () {
        // placeholder
    });
