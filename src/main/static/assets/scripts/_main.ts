///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('microprofileio-main', [
    'ngRoute',
    'ngStorage',
    'microprofileio-menu',
    'microprofileio-header',
    'microprofileio-footer',
    'microprofileio-projects',
    'microprofileio-contributors',
    'microprofileio-survey',
    'microprofileio-action',
    'microprofileio-faq',
    'microprofileio-presentations'
])

    .config([
        '$locationProvider', '$routeProvider',
        function ($locationProvider, $routeProvider) {
            $locationProvider.html5Mode({
                enabled: true,
                requireBase: true
            });
            $routeProvider
                .when('/', {
                    templateUrl: 'app/templates/page_home.html'
                })
                .when('/page/:resourceName*', {
                    templateUrl: 'app/templates/page_home.html',
                    controller: 'HomeController'
                })
                .when('/projects', {
                    templateUrl: 'app/templates/page_documents.html',
                    controller: ['microprofileioMenuService', function (menu) {
                        menu.setSelected('docs');
                    }]
                })
                .when('/contributors', {
                    templateUrl: 'app/templates/page_contributors.html',
                    controller: ['microprofileioMenuService', function (menu) {
                        menu.setSelected('contributors');
                    }]
                })
                .when('/faq', {
                    templateUrl: 'app/templates/page_faq.html',
                    controller: ['microprofileioMenuService', function (menu) {
                        menu.setSelected('faq');
                    }]
                })
                .when('/presentations', {
                    templateUrl: 'app/templates/page_presentations.html',
                    controller: ['microprofileioMenuService', function (menu) {
                        menu.setSelected('presentations');
                    }]
                })
                .when('/project/:resourcePath*', {
                    templateUrl: 'app/templates/page_project.html',
                    controller: 'ProjectPageController'
                })
                .otherwise({
                    controller: ['$scope', '$location', 'microprofileioMenuService', function ($scope, $location, menu) {
                        $scope.path = $location.path();
                        menu.setSelected(null);
                    }],
                    templateUrl: 'app/templates/page_404.html'
                });
        }
    ])

    .filter('microprofileioTruncateText', () => {
        return (input, maxLength) => {
            let value = input || '';
            let overflow = false;
            if(value.length > maxLength) {
               value = value.substr(0, maxLength);
                overflow = true
            }
            return value + (overflow ? '...' : '');
        };
    })

    .controller('ProjectPageController', ['$route', '$scope', 'microprofileioMenuService', function ($route, $scope, menu) {
        let resourcePath = $route.current.params['resourcePath'];
        $scope.configFile = resourcePath.split('/').slice(0, 2).join('/');
        $scope.resource = resourcePath.split('/').slice(2).join('/');
        menu.setSelected('docs');
    }])

    .controller('HomeController', ['$route', '$scope', 'microprofileioMenuService', function ($route, $scope, menu) {
        $scope.resource = $route.current.params['resourceName'];
        if (!$scope.resource) {
            $scope.resource = 'frontpage.adoc';
        }
        menu.setSelected('home');
    }])

    .run(['$rootScope', function ($rootScope) {
        $rootScope.baseFullPath = angular.element('head base').first().attr('href');
    }])

    .run(function () {
        // placeholder
    });
