///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('microprofileio-twitter', [])

    .directive('eeioCardTwitter', ['$window', function ($window) {
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
                            $scope.selected = null;
                            if ($scope.tweets && $scope.tweets.length) {
                                $scope.selected = $scope.tweets[0];
                            }
                        });
                    });
                });
                $scope.setSelected = function (tweet) {
                    $timeout(function () {
                        $scope.$apply(function () {
                            $scope.selected = tweet;
                        });
                    });
                };
            }],
            link: function (scope, el) {
                var winEl = angular.element($window);
                var adjust = function () {
                    if (!scope.selected) {
                        return;
                    }
                    var index = scope.tweets.indexOf(scope.selected);
                    var tweetsEl = el.find('.tweets');
                    var width = tweetsEl.width();
                    tweetsEl.css('transform', 'translateX(-' + (index * width) + 'px)')
                };
                scope.$watch('selected', adjust);
                winEl.bind('resize', adjust);
            }
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
