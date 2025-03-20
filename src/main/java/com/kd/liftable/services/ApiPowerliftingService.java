package com.kd.liftable.services;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kd.liftable.models.LiftMapper;
import com.kd.liftable.models.PowerliftingRecord;
import com.kd.liftable.models.RecordData;
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

    private final ServiceUtils serviceUtils;

    public ApiPowerliftingService(ServiceUtils serviceUtils) {
        this.serviceUtils = serviceUtils;
    }

    public JsonNode getLifterJson(String lifterName) throws Exception {
        String csvData = fetchLifterDataRaw(lifterName);
        String jsonString = serviceUtils.convertCsvToJsonString(csvData);
        return serviceUtils.convertJsonStringToJsonNode(jsonString);
    }

    public String fetchLifterDataRaw(String lifterName) throws Exception {
        String url = "https://www.openipf.org/api/liftercsv/";
        String formattedName = lifterName.strip().toLowerCase();
        String apiUrl = url + formattedName;

        return serviceUtils.fetchResponseString(apiUrl);
    }

    public JsonNode getRegionalRankingsJSON(String region) throws Exception {

        String regionalFed = RegionMapper.getFederationByRegion(region);

        String url = "https://www.openipf.org/rankings/" + regionalFed;

        Document doc = serviceUtils.fetchResponseDocument(url);

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

        return serviceUtils.convertJsonStringToJsonNode(jsonRowsString);
    }

    //API
    public JsonNode getScatterChartData(String name) throws Exception {
        String csvData = fetchLifterDataRaw(name);
        String jsonString = serviceUtils.convertCsvToJsonString(csvData);
        ArrayList<PowerliftingRecord> records = serviceUtils.convertJsonStringToPlRecord(jsonString);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode chartData = objectMapper.createObjectNode();
        chartData.set("labels", buildLabels("BW", "GLP"));
        chartData.set("data", buildScatterSet(records, LiftMapper.BODYWEIGHT.getColName(), LiftMapper.GOODLIFT.getColName()));
        return chartData;
    }

    //API PASS TO WEB
    public JsonNode getScatterChartFromData(ArrayList<PowerliftingRecord> records, String xl, String yl, String xd, String yd) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode chartData = objectMapper.createObjectNode();
        chartData.set("labels", buildLabels(xl, yl));
        chartData.set("data", buildScatterSet(records, xd, yd));
        return chartData;
    }

    public ObjectNode buildLabels(String x, String y) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode labelsNode = objectMapper.createObjectNode();
        labelsNode.put("x", x);
        labelsNode.put("y", y);

        return labelsNode;
    }

    public ArrayNode buildScatterSet(ArrayList<PowerliftingRecord> records, String x, String y) throws Exception {
        ArrayNode xData = serviceUtils.convertArrayListToArrNode(
                serviceUtils.isolateColumn(records, x, "raw", "sbd"));

        ArrayNode yData = serviceUtils.convertArrayListToArrNode(
                serviceUtils.isolateColumn(records, y, "raw", "sbd"));

        return serviceUtils.zipDataArrays(xData, yData);
    }
}
