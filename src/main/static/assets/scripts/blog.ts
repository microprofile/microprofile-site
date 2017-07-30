angular.module('microprofileio-blog', ['microprofileio-text'])

    .factory('microprofileioBlogService', ['$http', ($http) => {
        let archive = null;
        let tag = null;
        return {
            getHtml: (resource) => $http.get(`api/blog/html/${resource}.adoc`),
            listEntries: () => {
                return $http.get(`api/blog`);
            },
            setArchive: (value) => {
                if (value === archive) {
                    archive = null;
                } else {
                    archive = value;
                }
            },
            getArchive: () => {
                return archive;
            },
            setTag: (value) => {
                if (value === tag) {
                    tag = null;
                } else {
                    tag = value;
                }
            },
            getTag: () => {
                return tag;
            }
        };
    }])

    .directive('microprofileioBlogEntry', [() => {
        return {
            restrict: 'A',
            scope: {
                resource: '='
            },
            templateUrl: 'app/templates/dir_blog_entry.html',
            controller: ['$scope', '$timeout', 'microprofileioBlogService', '$sce', 'microprofileioProjectsDocService', 'microprofileioSystemService', ($scope, $timeout, srv, $sce, docService, systemSrv) => {
                systemSrv.getInfo().then((inforesponse) => {
                    srv.getHtml($scope.resource).then((response) => $timeout(() => $scope.$apply(() => {
                        let content = angular.element(response.data.content);
                        content.find('a.anchor').remove();
                        var newHtml = docService.normalizeResources(
                            content.html(),
                            inforesponse.data.blogProject
                        );
                        $scope.content = $sce.trustAsHtml(newHtml);
                    })));
                    srv.listEntries().then((entries) => $timeout(() => $scope.$apply(() => {
                        $scope.entry = _.find(entries.data, (entry) => {
                            return entry.url === $scope.resource;
                        });

                    })));
                });
            }]
        };
    }])

    .directive('microprofileioBlogList', [() => {
        return {
            restrict: 'A',
            scope: {},
            templateUrl: 'app/templates/dir_blog_list.html',
            controller: ['$scope', '$timeout', 'microprofileioBlogService', 'microprofileioSystemService', ($scope, $timeout, srv, sysSrv) => {
                sysSrv.getInfo().then((inforesponse) => {
                    $scope.dto = {
                        entries: [],
                        archive: null
                    };
                    srv.listEntries().then((entries) => $timeout(() => $scope.$apply(() => {
                        $scope.dto.entries = _.sortBy(entries.data, (entry) => {
                            return -1 * entry.date;
                        });
                        $scope.entries = $scope.dto.entries;
                    })));
                    $scope.normalizeUrl = (url: string) => {
                        if (url.startsWith('//') || url.startsWith('http://') || url.startsWith('https://')) {
                            return url;
                        }
                        return `blog/${url.replace(/\.adoc$/, '')}`;
                    };
                    $scope.normalizeImageUrl = (url: string) => {
                        if (url.startsWith('//') || url.startsWith('http://') || url.startsWith('https://')) {
                            return url;
                        }
                        return '/api/project/raw/' + inforesponse.data.blogProject + '/' + url;
                    };
                    $scope.$watchGroup(['dto.tag', 'dto.archive', 'dto.entries'], () => {
                        $timeout(() => {
                            $scope.$apply(() => {
                                $scope.entries = $scope.dto.entries;
                                if ($scope.dto.archive) {
                                    $scope.entries = _.filter($scope.entries, (entry) => {
                                        return moment(new Date(entry.date)).format('MMMM YYYY') === $scope.dto.archive;
                                    });
                                }
                                if ($scope.dto.tag) {
                                    $scope.entries = _.filter($scope.entries, (entry) => {
                                        return entry.tags && _.find(entry.tags, (tag) => {
                                            return tag === $scope.dto.tag
                                        });
                                    });
                                }
                            });
                        });
                    });
                });

            }]
        };
    }])

    .directive('microprofileioBlogListArchive', [() => {
        return {
            restrict: 'A',
            scope: {
                dto: '='
            },
            templateUrl: 'app/templates/dir_blog_list_archive.html',
            controller: ['$scope', '$timeout', 'microprofileioBlogService', ($scope, $timeout, srv) => {
                $scope.setArchive = (value) => {
                    srv.setArchive(value);
                    $scope.dto.archive = srv.getArchive();
                };
                $scope.$watch('dto.entries', () => {
                    $timeout(function () {
                        $scope.$apply(function () {
                            $scope.months = [];
                            if ($scope.dto.entries) {
                                _.each($scope.dto.entries, (entry) => {
                                    $scope.months.push(moment(new Date(entry.date)).format('MMMM YYYY'));
                                });
                                $scope.months = _.uniq($scope.months);
                            }
                        });
                    });
                });
            }]
        };
    }])

    .directive('microprofileioBlogListTags', [() => {
        return {
            restrict: 'A',
            scope: {
                dto: '='
            },
            templateUrl: 'app/templates/dir_blog_list_tags.html',
            controller: ['$scope', '$timeout', 'microprofileioBlogService', ($scope, $timeout, srv) => {
                $scope.setTag = (value) => {
                    srv.setTag(value);
                    $scope.dto.tag = srv.getTag();
                };
                $scope.$watch('dto.entries', () => {
                    $timeout(function () {
                        $scope.$apply(function () {
                            $scope.tags = [];
                            if ($scope.dto.entries) {
                                _.each($scope.dto.entries, (entry) => {
                                    if (entry.tags) {
                                        $scope.tags = _.union($scope.tags, entry.tags);
                                    }
                                });
                                $scope.tags = _.uniq($scope.tags);
                            }
                        });
                    });
                });
            }]
        };
    }])

    .directive('microprofileioBlogUser', [() => {
        return {
            restrict: 'A',
            scope: {
                entry: '='
            },
            templateUrl: 'app/templates/dir_blog_user.html',
            controller: ['$scope', '$timeout', 'microprofileioContributorsService', ($scope, $timeout, contributorsService) => {
                $scope.$watch('entry', () => {
                    if (!$scope.entry) {
                        return;
                    }
                    contributorsService.getContributor($scope.entry.author).then(function (response) {
                        $timeout(function () {
                            $scope.$apply(function () {
                                $scope.contributor = response.data;
                            });
                        });
                    });
                });
            }]
        };
    }])


    .run(function () {
        // placeholder
    });
