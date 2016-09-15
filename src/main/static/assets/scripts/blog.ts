///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('microprofileio-blog', [])

    .factory('microprofileioBlogService', ['$http', ($http) => {
        return {
            getHtml: (resource) => $http.get(`api/blog/html/${resource}.adoc`)
        };
    }])

    .directive('microprofileioBlogEntry', [() => {
        return {
            restrict: 'A',
            scope: {
                resource: '='
            },
            templateUrl: 'app/templates/dir_blog_entry.html',
            controller: ['$scope', '$timeout', 'microprofileioBlogService', '$sce', ($scope, $timeout, srv, $sce) => {
                srv.getHtml($scope.resource).then((response) => $timeout(() => $scope.$apply(() => {
                    $scope.content = $sce.trustAsHtml(response.data.content);
                })));
            }]
        };
    }])

    .run(function () {
        // placeholder
    });
