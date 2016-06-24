///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('microprofileio-main', [
    'ngRoute',
    'ngStorage',
    'microprofileio-menu',
    'microprofileio-header',
    'microprofileio-footer',
    'microprofileio-projects',
    'microprofileio-contributors',
    'microprofileio-googlegroups',
    'microprofileio-twitter',
    'microprofileio-survey',
    'microprofileio-action'
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
                    controller: ['$timeout', function ($timeout) {
                        $timeout(function () {
                            //particlesJS("home-particles", {
                            //    "particles": {
                            //        "number": {
                            //            "value": 80,
                            //            "density": {
                            //                "enable": true,
                            //                "value_area": 800
                            //            }
                            //        },
                            //        "color": {
                            //            "value": "#ffffff"
                            //        },
                            //        "shape": {
                            //            "type": "circle",
                            //            "stroke": {
                            //                "width": 0,
                            //                "color": "#000000"
                            //            },
                            //            "polygon": {
                            //                "nb_sides": 5
                            //            },
                            //            "image": {
                            //                "src": "img/github.svg",
                            //                "width": 100,
                            //                "height": 100
                            //            }
                            //        },
                            //        "opacity": {
                            //            "value": 0.5,
                            //            "random": false,
                            //            "anim": {
                            //                "enable": false,
                            //                "speed": 1,
                            //                "opacity_min": 0.1,
                            //                "sync": false
                            //            }
                            //        },
                            //        "size": {
                            //            "value": 3,
                            //            "random": true,
                            //            "anim": {
                            //                "enable": false,
                            //                "speed": 40,
                            //                "size_min": 0.1,
                            //                "sync": false
                            //            }
                            //        },
                            //        "line_linked": {
                            //            "enable": true,
                            //            "distance": 150,
                            //            "color": "#ffffff",
                            //            "opacity": 0.4,
                            //            "width": 1
                            //        },
                            //        "move": {
                            //            "enable": true,
                            //            "speed": 6,
                            //            "direction": "none",
                            //            "random": false,
                            //            "straight": false,
                            //            "out_mode": "out",
                            //            "bounce": false,
                            //            "attract": {
                            //                "enable": false,
                            //                "rotateX": 600,
                            //                "rotateY": 1200
                            //            }
                            //        }
                            //    },
                            //    "interactivity": {
                            //        "detect_on": "canvas",
                            //        "events": {
                            //            "onhover": {
                            //                "enable": true,
                            //                "mode": "grab"
                            //            },
                            //            "onclick": {
                            //                "enable": true,
                            //                "mode": "push"
                            //            },
                            //            "resize": true
                            //        },
                            //        "modes": {
                            //            "grab": {
                            //                "distance": 140,
                            //                "line_linked": {
                            //                    "opacity": 1
                            //                }
                            //            },
                            //            "bubble": {
                            //                "distance": 400,
                            //                "size": 40,
                            //                "duration": 2,
                            //                "opacity": 8,
                            //                "speed": 3
                            //            },
                            //            "repulse": {
                            //                "distance": 200,
                            //                "duration": 0.4
                            //            },
                            //            "push": {
                            //                "particles_nb": 4
                            //            },
                            //            "remove": {
                            //                "particles_nb": 2
                            //            }
                            //        }
                            //    },
                            //    "retina_detect": true
                            //});
                        });
                    }]
                })
                .when('/page', {
                    templateUrl: 'app/templates/page_pages.html',
                    controller: ['$location', function ($location) {
                        $location.path('/page/frontpage.adoc');
                    }]
                })
                .when('/page/:resourceName*', {
                    templateUrl: 'app/templates/page_home.html',
                    controller: 'HomeController'
                })
                .when('/projects', {
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
                .when('/contributors', {
                    templateUrl: 'app/templates/page_contributors.html',
                    controller: ['eeioMenuService', function (menu) {
                        menu.setSelected('contributors');
                    }]
                })
                .when('/faq', {
                    templateUrl: 'app/templates/page_faq.html',
                    controller: ['eeioMenuService', function (menu) {
                        menu.setSelected('faq');
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

    .controller('HomeController', ['$route', '$scope', 'eeioMenuService', function ($route, $scope, menu) {
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
