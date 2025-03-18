package com.kd.liftable.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kd.liftable.models.PowerliftingRecord;
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

    public PowerliftingRecord mapJsonToPowerliftingRecord(JsonNode row) {

        return new PowerliftingRecord(
                "/web/lifter/" + row.get(3).asText(),
                row.get(2).asText(),  // Name
                row.get(13).asText(), // Sex
                null, // Event (assuming Classic from provided data)
                row.get(14).asText(), // Equipment
                row.get(15).asText(), // Age
                row.get(16).asText(), // AgeClass
                null, // BirthYearClass (not provided)
                row.get(16).asText(), // Division (Same as AgeClass)
                row.get(17).asText(), // BodyweightKg
                row.get(18).asText(), // WeightClassKg
                null, // Squat1Kg
                null, // Squat2Kg
                null, // Squat3Kg
                null, // Squat4Kg (not provided)
                row.get(19).asText(), // Best3SquatKg (same as Squat3Kg)
                null, // Bench1Kg (not provided)
                null, // Bench2Kg (not provided)
                null, // Bench3Kg (not provided)
                null, // Bench4Kg (not provided)
                row.get(20).asText(), // Best3BenchKg (same as Squat2Kg)
                null, // Deadlift1Kg (not provided)
                null, // Deadlift2Kg (not provided)
                null, // Deadlift3Kg (not provided)
                null, // Deadlift4Kg (not provided)
                row.get(21).asText(), // Best3DeadliftKg
                row.get(22).asText(), // TotalKg
                null, // Place (not provided)
                null, // Dots (same as TotalKg)
                null, // Wilks (not provided)
                null, // Glossbrenner (not provided)
                row.get(23).asText(), // Goodlift (not provided)
                "Yes", // Tested (assuming tested since it's a ranking)
                row.get(6).asText(), // Country
                null, // State (not provided)
                row.get(8).asText(), // Federation
                null, // ParentFederation (not provided)
                row.get(9).asText(), // Date
                row.get(10).asText(), // MeetCountry
                null, // MeetState (not provided)
                null, // MeetTown (not provided)
                "European Powerlifting Championships", // MeetName
                "Yes" // Sanctioned (assuming all records are sanctioned)
        );
    }
}
