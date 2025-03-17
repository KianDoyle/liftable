package com.kd.liftable.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.List;

@Service
public class ServiceUtils {

    public static Document fetchResponseDocument(String url) throws Exception {
        return Jsoup.connect(url)
                .header("User-Agent", "Googlebot") // Spoof as a bot
                .header("Accept", "text/html")
                .header("Referer", "https://www.google.com/")
                .method(Connection.Method.GET)
                .execute()
                .parse();
    }

    public static String fetchResponseString(String url) throws Exception {
        return Jsoup.connect(url)
                .header("User-Agent", "Googlebot")
                .header("Accept", "text/html")
                .header("Referer", "https://www.google.com/")
                .method(Connection.Method.GET)
                .execute()
                .body();
    }

    public static String convertCsvToJsonString(String csvData) throws Exception {
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
}
