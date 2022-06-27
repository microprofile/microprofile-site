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
  // projects list source Url
  const projectsUrl = "https://microprofile.github.io/microprofile-site/projects.json";

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

  // check if element with id `microprofileio-projects-div` exists
  const bootstrapEl = document.getElementById("microprofileio-projects-div");
  if (!bootstrapEl)
    return console.warn("No element with id `microprofileio-projects-div`");

  arrayWaterfall(scripts, promiseGetScript).then(async () => {
    //wait for document to load
    angular
      .module("microprofileio-projects", [])
      .filter("truncateEllipsis", function () {
        return (input, maxLength = 130) =>
          input?.length > maxLength
            ? `${input.substring(0, maxLength)}...`
            : input;
      })
      .directive("microprofileioProjectsShortlist", [
        function () {
          return {
            restrict: "E",
            template: `
            <div><div>
            <div ng-repeat="project in projects">
            <div class="microprofileio-project-card">
                <div>
                    <a class="project-card-body" ng-href="/project/{{project.name}}">
                        <h3>{{project.name}}</h3>
                        <p>{{project.description | truncateEllipsis:130}}</p>
                    </a>
                    <div class="project-card-footer">
                        <div class="microprofileio-project-card-contributors">
                            <div>
                                <div ng-repeat="contributor in project.contributors | limitTo: 6" class="contributor">
                                    <div>
                                        <a ng-href="{{contributor.profile}}" title="{{contributor.name}}">
                                            <img ng-src="{{contributor.avatar}}"/>
                                        </a>
                                    </div>
                                </div>
                                <div class="has-more" ng-show="project.contributors.length > 6">
                                    <a ng-href="//github.com/{{project.name}}" ng-title="{{project.name}}">
                                        <i class="fa fa-plus"></i>
                                    </a>
                                </div>
                            </div>
                        </div>
                        <div>
                            <a target="_blank" ng-href="https://twitter.com/intent/tweet?text=Check out ${location.origin}/projects/{{project.name}}"><i class="fa fa-twitter"></i></a>
                            <a target="_blank" ng-href="http://www.facebook.com/sharer/sharer.php?u=${location.origin}/projects/{{project.name}}"><i class="fa fa-facebook"></i></a>
                        </div>
                    </div>
                </div>
            </div>
            </div>
            </div></div>`,
            controller: [
              "$scope",
              "$http",
              function ($scope, $http) {
                console.log('test');;
                $http.get(projectsUrl).then(function (response) {
                    console.log(response);
                  if (!response?.data?.length)
                    return console.warn("No projects fetched");
                  $scope.projects = response.data;
                });
              },
            ],
          };
        },
      ]);
    angular.bootstrap(bootstrapEl, ["microprofileio-projects"]);
  });
})(jQuery);

