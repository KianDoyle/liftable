package com.kd.liftable.services;

import com.fasterxml.jackson.databind.*;
import com.kd.liftable.models.*;
import com.kd.liftable.models.Record;
import com.kd.liftable.repositories.LifterDataRepository;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpenPowerliftingService {

    private final LifterDataRepository lifterDataRepository;
    private final ApiPowerliftingService apiPowerliftingService;
    private final ServiceUtils serviceUtils;

    public OpenPowerliftingService(LifterDataRepository lifterDataRepository, ApiPowerliftingService apiPowerliftingService, ServiceUtils serviceUtils) {
        this.lifterDataRepository = lifterDataRepository;
        this.apiPowerliftingService = apiPowerliftingService;
        this.serviceUtils = serviceUtils;
    }

    public LifterData fetchLifter(String name) {
        ArrayList<Record> records = new ArrayList<>(lifterDataRepository.findAllByName(name));
        return new LifterData(generateLifterCard(name, "raw", "a"), records, null);
    }

    public LifterCard generateLifterCard(String name, String equip, String event) {
        ArrayList<Record> records = new ArrayList<>(lifterDataRepository.findAllByName(name));
        ArrayList<String> colNames = new ArrayList<>(List.of(LiftMapper.BEST_SQUAT.getColName(), LiftMapper.BEST_BENCH.getColName(), LiftMapper.BEST_DEADLIFT.getColName(), LiftMapper.TOTAL.getColName(), LiftMapper.GOODLIFT.getColName()));
        ArrayList<Float> largestLifts = new ArrayList<>();
        for (String colName : colNames) {
            largestLifts.add(records.stream()
                    .filter(r -> r.getEquipment().trim().equalsIgnoreCase(equip) && (r.getEvent().trim().equalsIgnoreCase(event) || event.equalsIgnoreCase("a")))
                    .map(r -> r.getFloatFieldValue(colName))
                    .max(Float::compare).orElse(0F));
        }
        return new LifterCard(
                "/web/lifter/" + name.replace(" ", "").replace("#", "").toLowerCase(),
                name,
                records.getFirst().getSex(),
                "Classic",
                String.valueOf(largestLifts.get(0)),
                String.valueOf(largestLifts.get(1)),
                String.valueOf(largestLifts.get(2)),
                String.valueOf(largestLifts.get(3)),
                String.valueOf(largestLifts.get(4))
        );
    }

    public LinkedHashMap<String, ArrayList<Record>> fetchTop10LiftersInEachRegion() {
        LinkedHashMap<String, ArrayList<Record>> regionalRecords = new LinkedHashMap<>();
        for (RegionMapper region : RegionMapper.values()) {
            PageRequest pageable = PageRequest.of(0, 10);
            ArrayList<Record> records = new ArrayList<>(lifterDataRepository.findTop10ByFederationOrderByGoodliftDesc(region.getRegionName(), pageable));
            regionalRecords.put(region.getRegionName(), records);
        }
        return regionalRecords;
    }

    // Used to create record objects for thymeleaf
    public LifterData getLifterRecords(String lifterName) throws Exception {
        String csvData = apiPowerliftingService.fetchLifterDataRaw(lifterName);
        String jsonString = serviceUtils.convertCsvToJsonString(csvData);
        ArrayList<PowerliftingRecord> records = serviceUtils.convertJsonStringToPlRecord(jsonString);
        PowerliftingRecord lifterCard = createLifterCard(records, lifterName);

        return new LifterData(apiPowerliftingService.getScatterChartFromData(records, "BW", "GLP", LiftMapper.BODYWEIGHT.getColName(), LiftMapper.GOODLIFT.getColName()), lifterCard, records);
    }

    public PowerliftingRecord createLifterCard(ArrayList<PowerliftingRecord> records, String lifterName) throws Exception {
        ArrayList<String> colNames = new ArrayList<>(List.of(LiftMapper.BEST_SQUAT.getColName(), LiftMapper.BEST_BENCH.getColName(), LiftMapper.BEST_DEADLIFT.getColName(), LiftMapper.TOTAL.getColName(), LiftMapper.GOODLIFT.getColName()));

        ArrayList<Float> largestLifts = serviceUtils.findLargestLifts(records, "raw", "a", colNames);

        return new PowerliftingRecord(
                        "/web/lifter/" + lifterName,
                        records.getFirst().getName(),
                        records.getFirst().getSex(),
                        "Classic",
                        String.valueOf(largestLifts.get(0)),
                        String.valueOf(largestLifts.get(1)),
                        String.valueOf(largestLifts.get(2)),
                        String.valueOf(largestLifts.get(3)),
                        String.valueOf(largestLifts.get(4))
                        );
    }

    public ArrayList<NameLink> fetchLiftersDisambiguationList(String lifterName) throws Exception {
        String baseUrl = "https://www.openipf.org/u/";
        ArrayList<NameLink> lifterList = new ArrayList<>();

        String url = baseUrl + lifterName.strip().replaceAll("[^a-zA-Z]", "");

        Document doc = serviceUtils.fetchResponseDocument(url);

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
