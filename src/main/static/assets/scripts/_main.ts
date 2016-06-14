///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('javaeeio-main', [
    'ngRoute',
    'ngStorage',
    'javaeeio-menu',
    'javaeeio-header',
    'javaeeio-footer',
    'javaeeio-projects',
    'javaeeio-contributors',
    'javaeeio-guardians',
    'javaeeio-googlegroups',
    'javaeeio-twitter'
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
                    templateUrl: 'app/templates/page_home.html',
                    controller: 'RootPageController'
                })
                .when('/page', {
                    templateUrl: 'app/templates/page_home.html',
                    controller: 'RootPageController'
                })
                .when('/page/:resourceName*', {
                    templateUrl: 'app/templates/page_home.html',
                    controller: 'HomeController'
                })
                .when('/docs', {
                    templateUrl: 'app/templates/page_documents.html',
                    controller: ['eeioMenuService', function (menu) {
                        menu.setSelected('docs');
                    }]
                })
                .when('/forum', {
                    templateUrl: 'app/templates/page_forum.html',
                    controller: ['eeioMenuService', function (menu) {
                        menu.setSelected('forum');
                    }]
                })
                .when('/social', {
                    templateUrl: 'app/templates/page_social.html',
                    controller: ['eeioMenuService', function (menu) {
                        menu.setSelected('social');
                    }]
                })
                .when('/contributors', {
                    templateUrl: 'app/templates/page_contributors.html',
                    controller: ['eeioMenuService', function (menu) {
                        menu.setSelected('contributors');
                    }]
                })
                .when('/guardians', {
                    templateUrl: 'app/templates/page_guardians.html',
                    controller: ['eeioMenuService', function (menu) {
                        menu.setSelected('guardians');
                    }]
                })
                .when('/project/:configFile/:resourceName*', {
                    templateUrl: 'app/templates/page_project.html',
                    controller: 'ProjectPageController'
                })
                .when('/project/:configFile', {
                    templateUrl: 'app/templates/page_project.html',
                    controller: 'ProjectPageController'
                })
                .otherwise({
                    controller: ['$scope', '$location', 'eeioMenuService', function ($scope, $location, menu) {
                        $scope.path = $location.path();
                        menu.setSelected(null);
                    }],
                    templateUrl: 'app/templates/page_404.html'
                });
        }
    ])

    .controller('ProjectPageController', ['$route', '$scope', 'eeioMenuService', function ($route, $scope, menu) {
        $scope.configFile = $route.current.params['configFile'];
        $scope.resource = $route.current.params['resourceName'];
        menu.setSelected('docs');
    }])

    .controller('RootPageController', ['$location', function ($location) {
        $location.path('/page/frontpage.adoc');
    }])

    .controller('HomeController', ['$route', '$scope', 'eeioMenuService', function ($route, $scope, menu) {
        $scope.resource = $route.current.params['resourceName'];
        if (!$scope.resource) {
            //$scope.resource = 'javaee_guardians.adoc';
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
