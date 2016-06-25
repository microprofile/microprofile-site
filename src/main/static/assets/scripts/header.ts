///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('microprofileio-header', [])

    .directive('eeioHeader', ['$window', function ($window) {
        return {
            restrict: 'E',
            transclude: true,
            scope: {
                login: '='
            },
            templateUrl: 'app/templates/dir_header.html',
            link: function (scope, el) {
                var transcludeEl = el.find('div[ng-transclude]');
                if (transcludeEl.html() === '') {
                    el.find('.title').remove();
                }
                var page = angular.element($window);
                var background = el.find('.background');
                var title = el.find('.title > div');
                var header = el.find('.head');
                var adjustHeader = function() {
                    var step = page.scrollTop();
                    background.css({
                        'transform': 'translateY(' + (step / 3) + 'px)'
                    });
                    title.css({
                        'transform': 'translateY(' + (step / 2.3) + 'px)',
                        'opacity': (title.offset().top - $(window).scrollTop()) / 100
                    });
                    // header out
                    var menu = header.find('eeio-menu');
                    if (el.outerHeight() < step) {
                        if (!header.hasClass('floating')) {
                            header.addClass('floating');
                            menu.addClass('floating');
                        }
                    } else {
                        header.removeClass('floating');
                        menu.removeClass('floating');
                        menu.removeClass('expanded');
                    }
                };
                page.scroll(adjustHeader);
                adjustHeader();
            }
        };
    }])

    .run(function () {
        // placeholder
    });
