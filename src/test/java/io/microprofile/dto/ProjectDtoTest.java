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
package io.microprofile.dto;

import org.apache.johnzon.jsonb.JohnzonBuilder;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import javax.json.bind.Jsonb;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProjectDtoTest {
    @Test
    public void testProjectJson() throws JSONException {
        try {
            URL url = new URL("http://example.com");
            ContributorDto contributorDto = new ContributorDto("company", 100, "US", "microprofile", "mp", "image" +
                    ".png", url.toString());

            List<ContributorDto> contributorList = new ArrayList<ContributorDto>();
            contributorList.add(contributorDto);

            ProjectDto projectDto = new ProjectDto("Test Project","This is a project for testing.");
            projectDto.setContributors(contributorList);

            Jsonb jsonb = new JohnzonBuilder().build();
            String projectJson = jsonb.toJson(projectDto);

            System.out.println(projectJson);

            final String expected = "{\"contributors\":[{\"avatar\":\"image.png\",\"company\":\"company\"," +
                    "\"contributions\":100,\"location\":\"US\",\"login\":\"microprofile\",\"name\":\"mp\"," +
                    "\"profile\":\"http://example.com\"}],\"description\":\"This is a project for testing.\",\"name\":\"Test Project\"}";

            JSONAssert.assertEquals(expected, projectJson, JSONCompareMode.LENIENT);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
