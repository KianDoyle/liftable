package com.kd.liftable.services;

import com.fasterxml.jackson.databind.*;
import org.springframework.stereotype.Service;

@Service
public class ApiPowerliftingService {

    public ApiPowerliftingService() {
    }

    public JsonNode getLifterJson(String lifterName) {
        return null;
    }

    public JsonNode getRegionalRankingsJSON(String region) {
        return null;
    }

    //API

//    public ObjectNode buildLabels(String x, String y) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectNode labelsNode = objectMapper.createObjectNode();
//        labelsNode.put("x", x);
//        labelsNode.put("y", y);
//
//        return labelsNode;
//    }
//
//    public ArrayNode buildScatterSet(ArrayList<PowerliftingRecord> records, String x, String y) throws Exception {
//        ArrayNode xData = serviceUtils.convertArrayListToArrNode(
//                serviceUtils.isolateColumn(records, x, "raw", "sbd"));
//
//        ArrayNode yData = serviceUtils.convertArrayListToArrNode(
//                serviceUtils.isolateColumn(records, y, "raw", "sbd"));
//
//        return serviceUtils.zipDataArrays(xData, yData);
//    }
//
//    public JsonNode getScatterChartData(String name) throws Exception {
//        String csvData = fetchLifterDataRaw(name);
//        String jsonString = serviceUtils.convertCsvToJsonString(csvData);
//        ArrayList<PowerliftingRecord> records = serviceUtils.convertJsonStringToPlRecord(jsonString);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectNode chartData = objectMapper.createObjectNode();
//        chartData.set("labels", buildLabels("BW", "GLP"));
//        chartData.set("data", buildScatterSet(records, LiftMapper.BODYWEIGHT.getColName(), LiftMapper.GOODLIFT.getColName()));
//        return chartData;
//    }
//
//    //API PASS TO WEB
//    public JsonNode getScatterChartFromData(ArrayList<PowerliftingRecord> records, String xl, String yl, String xd, String yd) throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectNode chartData = objectMapper.createObjectNode();
//        chartData.set("labels", buildLabels(xl, yl));
//        chartData.set("data", buildScatterSet(records, xd, yd));
//        return chartData;
//    }
}
