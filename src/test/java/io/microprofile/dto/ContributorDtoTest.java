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
import java.net.*;

public class ContributorDtoTest {


    @Test
    public void testContributorJson() throws JSONException {
        try {
            URL url = new URL("http://example.com");
            ContributorDto contributorDtoList = new ContributorDto("company", 100, "US", "microprofile", "mp", "image.png", url.toString());
            Jsonb jsonb = new JohnzonBuilder().build();
            String contributorJson = jsonb.toJson(contributorDtoList);

            final String expected = "{\"company\":\"company\",\"contributions\":100,\"location\":\"US\"," +
                    "\"login\":\"microprofile\",\"name\":\"mp\",\"avatar\":\"image.png\",\"profile\":\"http://example.com\"}";

            JSONAssert.assertEquals(expected, contributorJson, JSONCompareMode.LENIENT);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
