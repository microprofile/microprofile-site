angular.module('microprofileio-system', [])

    .factory('microprofileioSystemService', ['$http',
        ($http) => {
            return {
                getInfo: function () {
                    return $http.get('api/system');
                }
            };
        }
    ])

    .run(function () {
        // placeholder
    });
