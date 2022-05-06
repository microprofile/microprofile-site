package io.microprofile.dto;

import org.apache.johnzon.jsonb.JohnzonBuilder;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import javax.json.bind.Jsonb;

public class ContributorDtoTest {


    @Test
    public void testContributorJson() throws JSONException {
        ContributorDto contributorDtoList = new ContributorDto("company", 100, "US", "microprofile", "mp");
        Jsonb jsonb = new JohnzonBuilder().build();
        String contributorJson = jsonb.toJson(contributorDtoList);

        final String expected = "{\"company\":\"company\",\"contributions\":100,\"location\":\"US\"," +
                "\"login\":\"microprofile\",\"name\":\"mp\"}";

        JSONAssert.assertEquals(expected, contributorJson, JSONCompareMode.LENIENT);
    }
}
