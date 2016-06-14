///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('javaeeio-projects', [])

    .factory('eeioProjectsDocService', ['$location',
        function ($location) {
            return {
                normalizeResources: function (pRoot, sRoot, originalHtml) {
                    var content = angular.element(originalHtml);
                    content.find('[href]').each(function (index, el) {
                        var ael = angular.element(el);
                        var currentHref = ael.attr('href');
                        if (s.startsWith(currentHref, '../')) {
                            ael.attr('href', currentHref.substring('../'.length));
                        } else if (!s.startsWith(currentHref, 'http://') && !s.startsWith(currentHref, 'https://')) {
                            var images = ael.find('> img');
                            if (images.length) {
                                var pathRoot = '/' + pRoot;
                                var currentHrefSplit = currentHref.split('/');
                                var resourceNamePath = $location.url().substring(pathRoot.length).split('/');
                                resourceNamePath.pop();
                                var resourceName = resourceNamePath.join('/') + '/' + currentHref;
                                var href = sRoot + resourceName;
                                ael.attr('href', href);
                                images.each(function (indexImg, elImg) {
                                    var aelImg = angular.element(elImg);
                                    aelImg.attr('src', href);
                                });
                            } else {
                                if (!s.startsWith(currentHref, '#')) {
                                    var href = pRoot + currentHref;
                                    ael.attr('href', href);
                                }
                            }
                        }
                    });
                    return content.html();
                }
            };
        }
    ])

    .factory('eeioProjectsService', [
        '$http',
        '$q',
        function ($http, $q) {
            return {
                getSpecs: function () {
                    return $http.get('api/specs');
                },
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
                },
                getAppPageHeader: function (resource) {
                    return $http.get('api/application/page-header/' + resource);
                }
            };
        }
    ])

    .directive('eeioProjectsShortlist', [function () {
        return {
            restrict: 'E',
            scope: {},
            templateUrl: 'app/templates/dir_projects_projects_shortlist.html',
            controller: ['$scope', '$timeout', 'eeioProjectsService', function ($scope, $timeout, projectsService) {
                projectsService.getSpecs().then(function (response) {
                    $timeout(function () {
                        $scope.$apply(function () {
                            $scope.specs = response.data;
                        });
                    });
                });
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

    .directive('eeioProjectNameDescription', [function () {
        return {
            restrict: 'E',
            scope: {
                configFile: '='
            },
            templateUrl: 'app/templates/dir_projects_project_name_description.html',
            controller: ['$scope', '$timeout', '$sce', 'eeioProjectsService',
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

    .directive('eeioProjectDoc', [function () {
        return {
            restrict: 'E',
            scope: {
                configFile: '=',
                resource: '='
            },
            templateUrl: 'app/templates/dir_projects_project_doc.html',
            controller: ['$scope', '$timeout', '$sce', 'eeioProjectsService', 'eeioProjectsDocService',
                function ($scope, $timeout, $sce, projectsService, docService) {
                    $scope.project = {};
                    projectsService.getProject($scope.configFile).then(function (response) {
                        $timeout(function () {
                            $scope.$apply(function () {
                                $scope.project.detail = response.data;
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

    .directive('eeioProjectDocRelated', [function () {
        return {
            restrict: 'E',
            scope: {
                configFile: '='
            },
            templateUrl: 'app/templates/dir_projects_project_doc_related.html',
            controller: ['$scope', '$timeout', 'eeioProjectsService',
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

    .directive('eeioApplicationPage', [function () {
        return {
            restrict: 'E',
            scope: {
                resource: '='
            },
            templateUrl: 'app/templates/dir_application_page.html',
            controller: ['$scope', '$timeout', '$sce', 'eeioProjectsService', 'eeioProjectsDocService', '$location',
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

    .directive('eeioApplicationPageHeader', [function () {
        return {
            restrict: 'E',
            scope: {
                resource: '='
            },
            templateUrl: 'app/templates/dir_application_page_header.html',
            controller: ['$scope', '$timeout', '$sce', 'eeioProjectsService', 'eeioProjectsDocService', '$location',
                function ($scope, $timeout, $sce, projectsService, docService, $location) {
                    projectsService.getAppPageHeader($scope.resource).then(function (response) {
                        $timeout(function () {
                            $scope.$apply(function () {
                                var dataObject = response.data;
                                var newHtml;
                                if (dataObject.h1 !== undefined) {
                                    newHtml = docService.normalizeResources(
                                        'page/',
                                        'api/application/raw',
                                        "<span>" + dataObject.h1 + "</span>"
                                    );
                                    $scope.h1 = $sce.trustAsHtml(newHtml);
                                } else {
                                    $scope.h1 = "";
                                }
                                if (dataObject.h2 !== undefined) {
                                    newHtml = docService.normalizeResources(
                                        'page/',
                                        'api/application/raw',
                                        "<span>" + dataObject.h2 + "</span>"
                                    );
                                    $scope.h2 = $sce.trustAsHtml(newHtml);
                                } else {
                                    $scope.h2 = "";
                                }
                            });
                        });
                    });
                }
            ]
        };
    }])

    .run(function () {
        // placeholder
    });
