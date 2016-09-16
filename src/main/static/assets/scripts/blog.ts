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
                resource: '=',
                title: '='
            },
            templateUrl: 'app/templates/dir_blog_entry.html',
            controller: ['$scope', '$timeout', 'microprofileioBlogService', '$sce', ($scope, $timeout, srv, $sce) => {
                srv.getHtml($scope.resource).then((response) => $timeout(() => $scope.$apply(() => {
                    let content = angular.element(response.data.content);
                    let title = content.find('h1').first();
                    title.find('> *').remove();
                    $scope.title = title.html();
                    title.remove();
                    content.find('.anchor').remove();
                    $scope.content = $sce.trustAsHtml(content.html());
                })));
            }]
        };
    }])

    .run(function () {
        // placeholder
    });
