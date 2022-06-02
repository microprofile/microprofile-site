/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.microprofile.main;

import io.microprofile.dto.ContributorDto;
import javax.json.bind.Jsonb;
import org.apache.johnzon.jsonb.JohnzonBuilder;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.PagedIterable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;


public class ContributorsProcessing {
    Logger log = java.util.logging.Logger.getLogger(this.getClass().getName());


    public void process() throws IOException {

        //obtain list of repositories
        final Config config = ConfigProvider.getConfig();
        final ArrayList<String> repositoriesNames = new ArrayList<>(Arrays.asList(config.getValue("repositories",
                String[].class)));

        final String githubToken = config.getValue("MP_BOT_TOKEN", String.class);
        final String filePath = config.getValue("filepath", String.class);

        log.info("Repositories to process: " + repositoriesNames.toString());

        final GitHub github = new GitHubBuilder().withOAuthToken(githubToken).build();

        // Obtain Map containing the total list of contributors and their total contributions from the list of
        // repositories.
        final HashMap<String, ContributorDto> contributorDtoHashMap = getContributorsDtoMap(github, repositoriesNames);

        //As of May 5th 2022, this returns 203 and matches current count int https://microprofile.io/contributors/
        log.info("Total contributors: " + contributorDtoHashMap.size());


        //write contributors list to a file
        List<ContributorDto> contributorDtoList = new ArrayList<>(contributorDtoHashMap.values());

        //List in descending order based on the number of contributions.
        Collections.sort(contributorDtoList, Comparator.comparing(ContributorDto::getContributions).reversed());

        Jsonb jsonb = new JohnzonBuilder().build();
        String myData = jsonb.toJson(contributorDtoList);
        log.info(myData);
        Path fileName = Path.of(filePath);
        String content  = myData;
        Files.writeString(fileName, content);

    }

    private HashMap<String, ContributorDto> getContributorsDtoMap(GitHub github, List<String> repositoriesList) throws
            IOException {
        HashMap<String, ContributorDto> contributorDtoMap = new HashMap<>();

        for (String repoName : repositoriesList) {

            log.info("Processing repository: " + repoName);

            //Connect to Repository
            GHRepository repo = github.getRepository(repoName);

            //Get Contributors for the repository
            PagedIterable<GHRepository.Contributor> contributors = repo.listContributors();

            for (GHRepository.Contributor ghContributor : contributors) {

                //ToDo: Add ignore-list to ignore github accounts like: eclipse-microprofile-bot

                //Increase existing contributions for existing contributor
                if (contributorDtoMap.containsKey(ghContributor.getLogin())) {
                    contributorDtoMap.get(ghContributor.getLogin())
                                     .increaseContributions(ghContributor.getContributions());
                } else { //New contributor to be added in the Map.
                    contributorDtoMap.put(ghContributor.getLogin(),
                            new ContributorDto(ghContributor.getCompany(), ghContributor.getContributions(),
<<<<<<< HEAD
                                    ghContributor.getLocation(), ghContributor.getLogin(), ghContributor.getName())
=======
                                    ghContributor.getLocation(), ghContributor.getLogin(), ghContributor.getName(), ghContributor.getAvatarUrl(), ghContributor.getHtmlUrl().toString())
>>>>>>> boot/main
                    );
                }
            }
        }
        return contributorDtoMap;
    }

}
