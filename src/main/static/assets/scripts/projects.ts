///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('microprofileio-projects', ['microprofileio-contributors'])

    .factory('microprofileioProjectsDocService', ['$location',
        function ($location) {
            return {
                normalizeResources: function (pRoot, sRoot, originalHtml) {
                    var content = angular.element(originalHtml);
                    content.find('a.anchor').remove();
                    content.find('#user-content-toc').remove();
                    content.find('a').each((idx, rawEl) => {
                        let el = angular.element(rawEl);
                        let oldHref = el.attr('href');
                        let newHref = window.location.pathname.split('/').filter((entry) => {
                            return entry !== '';
                        });
                        if (oldHref.startsWith('#')) {
                            el.attr('href', newHref.join('/') + oldHref);
                        } else {
                            newHref.pop();
                            newHref.push(oldHref);
                            el.attr('href', newHref.join('/'));
                        }
                    });
                    return content.html();
                }
            };
        }
    ])

    .factory('microprofileioProjectsService', [
        '$http',
        '$q',
        function ($http, $q) {
            return {
                getProjects: function () {
                    return $http.get('api/project');
                },
                getProject: function (configFile) {
                    if (!configFile) {
                        return {
                            then: function () {
                            }
                        };
                    } else {
                        return $http.get('api/project/' + configFile);
                    }
                },
                getProjectPage: function (configFile, resource) {
                    if (resource === null || resource === undefined) {
                        return $http.get('api/project/page/' + configFile + '/');
                    } else {
                        return $http.get('api/project/page/' + configFile + '/' + resource);
                    }
                },
                getAppPage: function (resource) {
                    return $http.get('api/application/page/' + resource);
                }
            };
        }
    ])

    .directive('microprofileioProjectsShortlist', [function () {
        return {
            restrict: 'E',
            scope: {},
            templateUrl: 'app/templates/dir_projects_projects_shortlist.html',
            controller: ['$scope', '$timeout', 'microprofileioProjectsService', function ($scope, $timeout, projectsService) {
                projectsService.getProjects().then(function (response) {
                    $timeout(function () {
                        $scope.$apply(function () {
                            $scope.projects = response.data;
                        });
                    });
                });
            }]
        };
    }])

    .directive('microprofileioProjectCardContributors', [function () {
        return {
            restrict: 'A',
            scope: {
                project: '='
            },
            templateUrl: 'app/templates/dir_projects_project_card_contributors.html',
            controller: ['$scope', '$timeout', function ($scope, $timeout) {
                $scope.$watch('project', () => {
                    if ($scope.project && $scope.project.contributors) {
                        $timeout(() => {
                            $scope.$apply(() => {
                                $scope.hasMore = $scope.project.contributors.length > 6;
                                $scope.contributors = $scope.project.contributors.slice(0, 6);
                            });
                        });
                    }
                });
            }]
        };
    }])

    .directive('microprofileioProjectCardContributor', [function () {
        return {
            restrict: 'A',
            scope: {
                contributor: '='
            },
            templateUrl: 'app/templates/dir_projects_project_card_contributor.html',
            controller: ['$scope', '$timeout', 'microprofileioContributorsService', function ($scope, $timeout, contributorService) {
                contributorService.getContributor($scope.contributor.login).then(function (response) {
                    $timeout(function () {
                        $scope.$apply(function () {
                            $scope.contributor = response.data;
                        });
                    });
                });
            }]
        };
    }])

    .directive('microprofileioProjectCard', [function () {
        return {
            restrict: 'A',
            scope: {
                projectName: '='
            },
            templateUrl: 'app/templates/dir_projects_project_card.html',
            controller: ['$scope', '$timeout', 'microprofileioProjectsService', function ($scope, $timeout, projectsService) {
                projectsService.getProject($scope.projectName).then(function (response) {
                    $timeout(function () {
                        $scope.$apply(function () {
                            let project = response.data;
                            $scope.project = project;
                            let normalizeName = (name: String) => {
                                return name.split(':')[0];
                            };
                            $scope.name = project.info.friendlyName ? project.info.friendlyName : normalizeName(project.info.name);
                        });
                    });
                });
            }]
        };
    }])

    .directive('microprofileioProjectNameDescription', [function () {
        return {
            restrict: 'E',
            scope: {
                configFile: '='
            },
            templateUrl: 'app/templates/dir_projects_project_name_description.html',
            controller: ['$scope', '$timeout', '$sce', 'microprofileioProjectsService',
                function ($scope, $timeout, $sce, projectsService) {
                    $scope.project = {};
                    projectsService.getProject($scope.configFile).then(function (response) {
                        $timeout(function () {
                            $scope.$apply(function () {
                                $scope.project.detail = response.data;
                            });
                        });
                    });
                }
            ]
        };
    }])

    .directive('microprofileioProjectDoc', [function () {
        return {
            restrict: 'E',
            scope: {
                configFile: '=',
                resource: '='
            },
            templateUrl: 'app/templates/dir_projects_project_doc.html',
            controller: ['$scope', '$timeout', '$sce', 'microprofileioProjectsService', 'microprofileioProjectsDocService',
                function ($scope, $timeout, $sce, projectsService, docService) {
                    $scope.project = {};
                    let normalizeName = (name: String) => {
                        return name.split(':')[0];
                    };
                    let normalizeVersion = (name: String) => {
                        let values = name.split(':');
                        if (values.length > 1) {
                            return values[1];
                        }
                        return '';
                    };
                    projectsService.getProject($scope.configFile).then(function (response) {
                        $timeout(function () {
                            $scope.$apply(function () {
                                $scope.project.detail = response.data;
                                $scope.name = normalizeName(response.data.info.name);
                                $scope.version = normalizeVersion(response.data.info.name);
                            });
                        });
                    });
                    projectsService.getProjectPage($scope.configFile, $scope.resource).then(function (response) {
                        $timeout(function () {
                            $scope.$apply(function () {
                                $scope.project.doc = $sce.trustAsHtml(docService.normalizeResources(
                                    'project/' + $scope.configFile + '/',
                                    'api/project/raw/' + $scope.configFile + '/',
                                    response.data.content
                                ));
                            });
                        });
                    });
                }
            ]
        };
    }])

    .directive('microprofileioProjectDocRelated', [function () {
        return {
            restrict: 'E',
            scope: {
                configFile: '='
            },
            templateUrl: 'app/templates/dir_projects_project_doc_related.html',
            controller: ['$scope', '$timeout', 'microprofileioProjectsService',
                function ($scope, $timeout, projectsService) {
                    $scope.related = {};
                    projectsService.getProject($scope.configFile).then(function (response) {
                        $timeout(function () {
                            $scope.$apply(function () {
                                $scope.related = response.data;
                            });
                        });
                    });
                }
            ]
        };
    }])

    .directive('microprofileioApplicationPage', [function () {
        return {
            restrict: 'E',
            scope: {
                resource: '='
            },
            templateUrl: 'app/templates/dir_application_page.html',
            controller: ['$scope', '$timeout', '$sce', 'microprofileioProjectsService', 'microprofileioProjectsDocService', '$location',
                function ($scope, $timeout, $sce, projectsService, docService, $location) {
                    projectsService.getAppPage($scope.resource).then(function (response) {
                        $timeout(function () {
                            $scope.$apply(function () {
                                var newHtml = docService.normalizeResources(
                                    'page/',
                                    'api/application/raw',
                                    response.data
                                );
                                $scope.page = $sce.trustAsHtml(newHtml);
                            });
                        });
                    });
                }
            ]
        };
    }])

    .directive('microprofileioApplicationPageHeader', [function () {
        return {
            restrict: 'E',
            scope: {
                resource: '='
            },
            templateUrl: 'app/templates/dir_application_page_header.html'
        };
    }])

    .directive('microprofileioShareProject', ['$window', function ($window) {
        return {
            restrict: 'A',
            scope: {
                project: "=",
                media: "@"
            },
            link: function ($scope, $element) {
                $element.bind('click', function () {
                    var url;
                    if ('twitter' === $scope.media) {
                        url = 'https://twitter.com/intent/tweet?text=Check out '
                            + window.location.origin + '/projects/' + $scope.project.info.name;
                    } else if ('facebook' === $scope.media) {
                        url = 'http://www.facebook.com/sharer/sharer.php?u='
                            + window.location.origin + '/projects/' + $scope.project.info.name;
                    }
                    if (url) {
                        $window.open(url, 'name', 'width=600,height=400');
                    }
                });
            }
        };
    }])

    .run(function () {
        // placeholder
    });
