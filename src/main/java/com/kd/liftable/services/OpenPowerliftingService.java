package com.kd.liftable.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.kd.liftable.models.Lifter;
import com.kd.liftable.models.NameLink;
import com.kd.liftable.models.PowerliftingRecord;
import com.kd.liftable.models.RegionMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OpenPowerliftingService {

    // Used to get records in JSON format for API
    public JsonNode getLifterJson(String lifterName) throws Exception {
        String csvData = fetchLifterDataRaw(lifterName);
        String jsonString = ServiceUtils.convertCsvToJsonString(csvData);
        return ServiceUtils.convertJsonStringToJsonNode(jsonString);
    }

    // Used to create record objects for thymeleaf
    public ArrayList<PowerliftingRecord> getLifterRecords(String lifterName) throws Exception {
        String csvData = fetchLifterDataRaw(lifterName);
        String jsonString = ServiceUtils.convertCsvToJsonString(csvData);
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(jsonString, new TypeReference<ArrayList<PowerliftingRecord>>() {
        });
    }

    private String fetchLifterDataRaw(String lifterName) throws Exception {
        String url = "https://www.openipf.org/api/liftercsv/";
        String formattedName = lifterName.strip().toLowerCase();
        String apiUrl = url + formattedName;

        return ServiceUtils.fetchResponseString(apiUrl);
    }

    public Lifter fetchLifterData(String lifterName) throws Exception {
        // Format the URL to match OpenIPF's URL structure
        String formattedName = lifterName.strip().toLowerCase();
        String url = "https://www.openipf.org/u/" + formattedName;

        // Fetch and parse the HTML document
        Document doc = ServiceUtils.fetchResponseDocument(url);

        // Extract lifter data
        Element nameElement = !doc.select("h1").isEmpty() ? doc.select("h1").getFirst() : null;  // Assuming the lifter's name is in an <h1> tag
        Element statsHeadingsElement = !doc.select("tr").isEmpty() ? doc.select("tr").getFirst() : null;  // Adjust selector based on the site's structure
        Element statsElement = doc.select("tr").size() > 1 ? doc.select("tr").get(1) : null;

        if (nameElement == null || statsHeadingsElement == null || statsElement == null) {
            throw new Exception("Failed to fetch lifter data");
        }

        List<String> nameSex = Arrays.stream(nameElement.text().split(" ")).toList();
        List<String> stats = Arrays.stream(statsElement.text().split(" ")).toList();

        // Create and return the lifter object
        Lifter lifter = new Lifter(nameSex.get(0) + " " + nameSex.get(1), nameSex.get(2), stats.get(0), Float.parseFloat(stats.get(1)), Float.parseFloat(stats.get(2)), Float.parseFloat(stats.get(3)), Float.parseFloat(stats.get(4)), Float.parseFloat(stats.get(5)));
        return lifter;
    }

    public ArrayList<NameLink> fetchLiftersDisambiguationList(String lifterName) throws Exception {
        String baseUrl = "https://www.openipf.org/u/";
        ArrayList<NameLink> lifterList = new ArrayList<>();

        String url = baseUrl + lifterName.strip().replaceAll("[^a-zA-Z]", "");

        Document doc = ServiceUtils.fetchResponseDocument(url);

        if (!doc.body().text().isEmpty() || !doc.body().text().equals("404")) {
            if (doc.select("title").text().equals("Disambiguation")) {
                ArrayList<Element> elementList = doc.select("h2");
                for (Element element : elementList) {
                    lifterList.add(new NameLink(element.text(), "lifter/" + element.text().replaceAll("[^a-zA-Z0-9]", "").replaceFirst(".$", "").toLowerCase()));
                }
            } else {
                lifterList.add(new NameLink(doc.select("h1").text(), "lifter/" + doc.select("title").text().replaceAll("[^a-zA-Z0-9]", "").toLowerCase()));
            }
        }

        return lifterList;
    }

    public JsonNode getRegionalRankingsJSON(String region) throws Exception {

        String regionalFed = RegionMapper.getFederationByRegion(region);

        String url = "https://www.openipf.org/rankings/" + regionalFed;

        Document doc = ServiceUtils.fetchResponseDocument(url);

        // Extract the script tag containing `initial_data`
        Element scriptTag = doc.select("script:containsData(initial_data)").first();

        if (scriptTag == null) {
            System.out.println("No matching script tag found");
            return null;
        }

        String scriptContent = scriptTag.html();

        // Regex pattern to extract only the first 10 rows from "rows": [...]
        Pattern pattern = Pattern.compile("\"rows\":\\[(\\[.*?])(?:,\\[.*?]){0,9}");
        Matcher matcher = pattern.matcher(scriptContent);

        if (!matcher.find()) {
            System.out.println("Failed to extract the first 10 entries");
            return null;
        }

        String jsonRowsString = "[" + matcher.group(1) + "]";

        return ServiceUtils.convertJsonStringToJsonNode(jsonRowsString);
    }

}
