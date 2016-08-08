///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('microprofileio-menu', [])

    .factory('microprofileioMenuService', [
        function () {
            var selectedMenu = null;
            return {
                setSelected: function (value) {
                    selectedMenu = value;
                },
                getSelected: function () {
                    return selectedMenu;
                }
            };
        }
    ])

    .directive('microprofileioMenu', [function () {
        return {
            restrict: 'E',
            scope: {},
            templateUrl: 'app/templates/dir_menu.html',
            controller: ['$element', '$scope', '$timeout', 'microprofileioMenuService', function ($element, $scope, $timeout, srv) {
                $scope.setSelected = function (value) {
                    $timeout(function () {
                        $scope.$apply(function () {
                            $scope.selectedItem = value;
                            srv.setSelected(value);
                        });
                    });
                };
                $scope.setSelected(srv.getSelected());
                $scope.toggleExpanded = function() {
                    $element.toggleClass('expanded');
                };
            }]
        };
    }])

    .run(function () {
        // placeholder
    });
