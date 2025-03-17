package com.kd.liftable.services;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kd.liftable.models.RegionMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ApiPowerliftingService {

    public JsonNode getLifterJson(String lifterName) throws Exception {
        String csvData = fetchLifterDataRaw(lifterName);
        String jsonString = ServiceUtils.convertCsvToJsonString(csvData);
        return ServiceUtils.convertJsonStringToJsonNode(jsonString);
    }

    public String fetchLifterDataRaw(String lifterName) throws Exception {
        String url = "https://www.openipf.org/api/liftercsv/";
        String formattedName = lifterName.strip().toLowerCase();
        String apiUrl = url + formattedName;

        return ServiceUtils.fetchResponseString(apiUrl);
    }

    public JsonNode getRegionalRankingsJSON(String region) throws Exception {

        String regionalFed = RegionMapper.getFederationByRegion(region);

        String url = "https://www.openipf.org/rankings/" + regionalFed;

        Document doc = ServiceUtils.fetchResponseDocument(url);

        // Extract the script tag containing `initial_data`
        Element scriptTag = doc.select("script:containsData(initial_data)").first();

        if (scriptTag == null) {
            return null;
        }

        String scriptContent = scriptTag.html();

        // Regex pattern to extract only the first 10 rows from "rows": [...]
        Pattern pattern = Pattern.compile("\"rows\":\\[(\\[[^]]*](?:,\\[[^]]*]){0,9})");
        Matcher matcher = pattern.matcher(scriptContent);

        if (!matcher.find()) {
            return null;
        }

        String jsonRowsString = "{\"" + region + "\":" + "[" + matcher.group(1) + "]" + "}";

        return ServiceUtils.convertJsonStringToJsonNode(jsonRowsString);
    }

    public JsonNode getRegionalRankingsJSONNameOnly(String region) throws Exception {
        // Get the full JSON data for the region
        JsonNode fullData = getRegionalRankingsJSON(region);
        if (fullData == null) {
            return null;
        }

        // Get the "europe" array from the root node
        JsonNode regionArray = fullData.path(region);

        // Create an ArrayNode to store the usernames
        ArrayNode usernamesArray = new ArrayNode(JsonNodeFactory.instance);

        // Iterate over the array in the "europe" node
        for (JsonNode entry : regionArray) {
            // Extract the 4th element (index 3) from each entry (the username)
            String username = entry.get(3).asText();
            usernamesArray.add(username);
        }

        // Create the final result in JSON format
        ObjectNode resultNode = new ObjectNode(JsonNodeFactory.instance);
        resultNode.set(region, usernamesArray);

        return resultNode;
    }
}
