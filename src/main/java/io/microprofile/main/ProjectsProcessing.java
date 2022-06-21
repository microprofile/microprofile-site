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
import io.microprofile.dto.ProjectDto;
import org.apache.johnzon.jsonb.JohnzonBuilder;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.PagedIterable;

import javax.json.bind.Jsonb;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class ProjectsProcessing {
    Logger log = java.util.logging.Logger.getLogger(this.getClass().getName());


    public void process() throws IOException {

        //obtain list of repositories
        final Config config = ConfigProvider.getConfig();
        final ArrayList<String> repositoriesNames = new ArrayList<>(Arrays.asList(config.getValue("repositories",
                String[].class)));

        final String githubToken = config.getValue("MP_BOT_TOKEN", String.class);
        final String projectsFilePath = config.getValue("projects-filepath", String.class);

        log.info("Repositories to process: " + repositoriesNames.toString());

        final GitHub github = new GitHubBuilder().withOAuthToken(githubToken).build();

        //Obtaining the total list of repositories
        final ArrayList<ProjectDto> projectDtoArrayList = getProjects(github, repositoriesNames);

        log.info("Total projects found: " + projectDtoArrayList.size());

        final Jsonb jsonb = new JohnzonBuilder().build();
        final String myData = jsonb.toJson(projectDtoArrayList);
        log.info(myData);
        Path fileName = Path.of(projectsFilePath);
        String content  = myData;
        Files.writeString(fileName, content);
    }

    private ArrayList<ProjectDto> getProjects(GitHub github, List<String> repositoriesList) throws
            IOException  {

        ArrayList<ProjectDto> projectsList = new ArrayList<>();

        for(String repoName : repositoriesList){
            ProjectDto projectDto = null;

            log.info("Processing repository: " + repoName);

            //Connect to Repository
            GHRepository repo = github.getRepository(repoName);

            //Get Contributors for the repository
            PagedIterable<GHRepository.Contributor> contributors = repo.listContributors();

            //Obtain the contributors from the repository
            PagedIterable<GHRepository.Contributor> contributorsGibHub = repo.listContributors();
            List<ContributorDto> repoContributors = new ArrayList<>();

            for (GHRepository.Contributor ghContributor : contributorsGibHub) {
                repoContributors.add(new ContributorDto(ghContributor.getCompany(), ghContributor.getContributions(),
                        ghContributor.getLocation(), ghContributor.getLogin(), ghContributor.getName(),
                        ghContributor.getAvatarUrl(), ghContributor.getHtmlUrl().toString()));
            }

            //List in descending order based on the number of contributions.
            Collections.sort(repoContributors, Comparator.comparing(ContributorDto::getContributions).reversed());

            //Add all the date need it for a new Project node
            projectDto = new ProjectDto(repo.getFullName(),repo.getDescription());
            projectDto.setContributors(repoContributors);
            projectsList.add(projectDto);
        }
        return projectsList;
    }

}
