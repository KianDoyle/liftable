package com.kd.liftable.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.kd.liftable.models.Lifter;
import com.kd.liftable.models.NameLink;
import com.kd.liftable.models.PowerliftingRecord;
import com.kd.liftable.models.RegionMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OpenPowerliftingService {

    private final ApiPowerliftingService apiPowerliftingService;

    public OpenPowerliftingService(ApiPowerliftingService apiPowerliftingService) {
        this.apiPowerliftingService = apiPowerliftingService;
    }

    // Used to create record objects for thymeleaf
    public ArrayList<PowerliftingRecord> getLifterRecords(String lifterName) throws Exception {
        String csvData = apiPowerliftingService.fetchLifterDataRaw(lifterName);
        String jsonString = ServiceUtils.convertCsvToJsonString(csvData);
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(jsonString, new TypeReference<ArrayList<PowerliftingRecord>>() {
        });
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
        return new Lifter(nameSex.get(0) + " " + nameSex.get(1), nameSex.get(2), stats.get(0), Float.parseFloat(stats.get(1)), Float.parseFloat(stats.get(2)), Float.parseFloat(stats.get(3)), Float.parseFloat(stats.get(4)), Float.parseFloat(stats.get(5)));
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

    public LinkedHashMap<String, ArrayList<NameLink>> fetchAllRegionalRankings() throws Exception {
        LinkedHashMap<String, ArrayList<NameLink>> nameLinksMap = new LinkedHashMap<>();

        for (RegionMapper region : RegionMapper.values()) {
            String regionName = region.getRegionName();
            JsonNode nameList = apiPowerliftingService.getRegionalRankingsJSON(regionName);
            JsonNode regionArray = nameList.path(regionName);
            ArrayList<NameLink> nameLinks = new ArrayList<>();

            for (JsonNode entry : regionArray) {
                String lifterName = entry.get(2).asText();
                String lifterLink = entry.get(3).asText();
                nameLinks.add(new NameLink(lifterName, "lifter/" + lifterLink));
            }
            nameLinksMap.put(regionName.toUpperCase(), nameLinks);
        }

        return nameLinksMap;
    }

}
