/*
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at
      http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
(function (jQuery) {
    // contributors list source Url
  const contributorsUrl =
    "https://microprofile.github.io/microprofile-site/contributors.json";

  const scripts = [
    "https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.8.3/angular.min.js",
  ];

  // function to Promisify jQuery.getScript
  const promiseGetScript = async (url) =>
    await new Promise((res) => jQuery.getScript(url, res));

  // function to load scripts one by one,
  // to load in parallel use Promise.all(scripts.map(promiseGetScript))
  const arrayWaterfall = (array, fn) =>
    array.reduce((p, script) => p.then(() => fn(script)), Promise.resolve());
  
  // check if element with id `microprofileio-contributors-div` exists
  const bootstrapEl = document.getElementById(
    "microprofileio-contributors-div"
  );
  if (!bootstrapEl)
    return console.warn(
      "No element with id `microprofileio-contributors-div`"
    );

  arrayWaterfall(scripts, promiseGetScript).then(async () => {
    //wait for document to load
    //await new Promise((res) => angular.element(document).ready(res));

    angular
      .module("microprofileio-contributors", [])
      .directive("microprofileioContributorsList", [
        function () {
          return {
            restrict: "E",
            template: `<div>
                <a ng-repeat="contributor in contributors | orderBy:'contributions':true" ng-href="{{contributor.profile}}" target="_blank">
                    <img ng-src="{{contributor.avatar}}" title="{{contributor.name}}"/>
                    <div>
                        <h1>{{contributor.name ? contributor.name : contributor.login}}</h1>
                        <h2 class="company">{{contributor.company ? contributor.company : '-'}}</h2>
                        <h2>{{contributor.location ? contributor.location : '-'}}</h2>
                    </div>
                </a>
              </div>`,
            controller: [
              "$scope",
              "$http",
              function ($scope, $http) {
                $http.get(contributorsUrl).then(function (response) {
                  if (!response?.data?.length)
                    return console.warn("No contributors fetched");
                  $scope.contributors = response.data;
                });
              },
            ],
          };
        },
      ]);
    angular.bootstrap(bootstrapEl, ["microprofileio-contributors"]);
  });
})(jQuery);
