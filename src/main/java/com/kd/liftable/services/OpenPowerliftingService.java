package com.kd.liftable.services;

import com.fasterxml.jackson.databind.*;
import com.kd.liftable.models.Lifter;
import com.kd.liftable.models.NameLink;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

@Service
public class OpenPowerliftingService {

    // gpt code start ------

    private static final String BASE_URL = "https://www.openipf.org/api/liftercsv/";

    public JsonNode getLifterJson(String lifterName) throws Exception {
        String csvData = fetchLifterDataRaw(lifterName);
        String jsonString = convertCsvToJson(csvData);
        return convertJsonStringToJsonNode(jsonString);
    }

    private String fetchLifterDataRaw(String lifterName) throws Exception {
        String formattedName = lifterName.strip().toLowerCase();
        String apiUrl = BASE_URL + formattedName;

        return Jsoup.connect(apiUrl)
                .header("User-Agent", "Googlebot") // Spoof as a bot
                .header("Accept", "text/html")
                .header("Referer", "https://www.google.com/")
                .method(Connection.Method.GET)
                .execute()
                .body();
    }

    public static String convertCsvToJson(String csvData) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try (CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build())) {
            List<String> headers = parser.getHeaderNames();
            ArrayNode jsonArray = objectMapper.createArrayNode();

            for (CSVRecord record : parser) {
                ObjectNode jsonObject = objectMapper.createObjectNode();
                for (String header : headers) {
                    jsonObject.put(header, record.get(header));
                }
                jsonArray.add(jsonObject);
            }
            return objectMapper.writeValueAsString(jsonArray);
        }
    }

    // Method to convert a JSON string to a JsonNode object
    public static JsonNode convertJsonStringToJsonNode(String jsonString) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(jsonString);  // Converts the JSON string into a JsonNode
    }

    // gpt code end ------

    public Lifter fetchLifterData(String lifterName) throws Exception{
        // Format the URL to match OpenIPF's URL structure
        String formattedName = lifterName.strip().toLowerCase();
        String url = "https://www.openipf.org/u/" + formattedName;

        // Fetch and parse the HTML document
        Document doc = Jsoup.connect(url).get();

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

//    public String fetchLifterDataRaw(String lifterName) throws Exception {
//        // Format the URL to match OpenIPF's URL structure
//        String formattedName = lifterName.strip().toLowerCase();
//        String apiUrl = "https://www.openipf.org/api/liftercsv/" + formattedName;
//
//        // Simulate a browser requesting "view-source"
//        Document doc2 = Jsoup.connect(apiUrl)
//                .header("User-Agent", "Googlebot") // Spoof as a bot
//                .header("Accept", "text/html")
//                .header("Referer", "https://www.google.com/")
//                .method(Connection.Method.GET)
//                .execute()
//                .parse();
//
//        // Extract content;
//        return doc2.body().text();
//    }

    public ArrayList<NameLink> fetchLiftersDisambiguationList(String lifterName) {
        String baseUrl = "https://www.openipf.org/u/";
        ArrayList<NameLink> lifterList = new ArrayList<>();

        String url = baseUrl + lifterName.strip().replaceAll("[^a-zA-Z]", "");
        try {
            Document doc = Jsoup.connect(url)
                    .header("User-Agent", "Googlebot") // Spoof as a bot
                    .header("Accept", "text/html")
                    .header("Referer", "https://www.google.com/")
                    .method(Connection.Method.GET)
                    .execute()
                    .parse();

            if (!doc.body().text().isEmpty() || !doc.body().text().equals("404")) {
                if (doc.select("title").text().equals("Disambiguation")) {
                    ArrayList<Element> elementList = doc.select("h2");
                    for (Element element : elementList) {
                        lifterList.add(new NameLink(element.text(), "lifter/" + element.text().replaceAll("[^a-zA-Z0-9]", "").replaceFirst(".$","").toLowerCase()));
                    }
                } else {
                    lifterList.add(new NameLink(doc.select("h1").text(), "lifter/" + doc.select("title").text().replaceAll("[^a-zA-Z0-9]", "").toLowerCase()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lifterList;
    }

}
