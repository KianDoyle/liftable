package com.kd.liftable.services;

import com.fasterxml.jackson.databind.*;
import com.kd.liftable.models.NameLink;
import com.kd.liftable.models.PowerliftingRecord;
import com.kd.liftable.models.RegionMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.ArrayList;

@Service
public class OpenPowerliftingService {

    private final ApiPowerliftingService apiPowerliftingService;
    private final ServiceUtils serviceUtils;

    public OpenPowerliftingService(ApiPowerliftingService apiPowerliftingService, ServiceUtils serviceUtils) {
        this.apiPowerliftingService = apiPowerliftingService;
        this.serviceUtils = serviceUtils;
    }

    // Used to create record objects for thymeleaf
    public ArrayList<PowerliftingRecord> getLifterRecords(String lifterName) throws Exception {
        String csvData = apiPowerliftingService.fetchLifterDataRaw(lifterName);
        String jsonString = ServiceUtils.convertCsvToJsonString(csvData);
        return ServiceUtils.convertJsonStringToPlRecord(jsonString);
    }

    public PowerliftingRecord createLifterCard(String lifterName) throws Exception {
        ArrayList<PowerliftingRecord> records = getLifterRecords(lifterName);

        return new PowerliftingRecord(
                        "/web/lifter/" + lifterName,
                        records.getFirst().getName(),
                        records.getFirst().getSex(),
                        "Classic",
                        String.valueOf(serviceUtils.findLargestLift(records, "raw", "squat")),
                        String.valueOf(serviceUtils.findLargestLift(records, "raw", "bench")),
                        String.valueOf(serviceUtils.findLargestLift(records, "raw", "deadlift")),
                        String.valueOf(serviceUtils.findLargestLift(records, "raw", "total")),
                        String.valueOf(serviceUtils.findLargestLift(records, "raw", "glp"))
                        );
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

    public LinkedHashMap<String, ArrayList<PowerliftingRecord>> fetchAllRegionalRankings() throws Exception {
        LinkedHashMap<String, ArrayList<PowerliftingRecord>> lifterLinksMap = new LinkedHashMap<>();

        for (RegionMapper region : RegionMapper.values()) {
            String regionName = region.getRegionName();
            JsonNode nameList = apiPowerliftingService.getRegionalRankingsJSON(regionName);
            JsonNode regionArray = nameList.path(regionName);
            ArrayList<PowerliftingRecord> lifterLinks = new ArrayList<>();

            for (JsonNode entry : regionArray) {
                PowerliftingRecord lifterEntry = serviceUtils.mapJsonToPowerliftingRecord(entry);
                lifterLinks.add(lifterEntry);
            }
            lifterLinksMap.put(regionName.toUpperCase(), lifterLinks);
        }

        return lifterLinksMap;
    }

}
