
angular.module('microprofileio-text', [])

    .factory('microprofileioProjectsDocService', ['$location',
        function ($location) {
            return {
                normalizeResources: function (originalHtml, githubProject) {
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
                    content.find('img').each((idx, rawEl) => {
                        let el = angular.element(rawEl);
                        let oldHref = el.attr('src');
                        if (oldHref.startsWith('//') || oldHref.startsWith('http://') || oldHref.startsWith('https://')) {
                            return;
                        }
                        let imgPath = _.filter($location.path().split('/'), (item) => {
                            return item.trim() !== '';
                        });
                        imgPath.pop();
                        imgPath.shift();
                        oldHref = imgPath.join('/') + '/' + oldHref;
                        el.attr('src', '/api/project/raw/' + githubProject + '/' + oldHref);
                    });
                    return content.html();
                }
            };
        }
    ])

    .run(function () {
        // placeholder
    });
