package com.kd.liftable.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kd.liftable.models.*;
import com.kd.liftable.models.Record;
import com.kd.liftable.repositories.NameRepository;
import com.kd.liftable.repositories.RecordRepository;
import com.kd.liftable.repositories.RegionalRecordRepository;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OpenPowerliftingService {

    private final RecordRepository recordRepository;
    private final RegionalRecordRepository regionalRecordRepository;
    private final NameRepository nameRepository;

    public OpenPowerliftingService(RecordRepository recordRepository, RegionalRecordRepository regionalRecordRepository, NameRepository nameRepository) {
        this.recordRepository = recordRepository;
        this.regionalRecordRepository = regionalRecordRepository;
        this.nameRepository = nameRepository;
    }

    public ArrayList<Name> fetchPossibleLifters(String lifterName, String origin) {
        ArrayList<Name> names = new ArrayList<>(nameRepository.searchByNameOnlyEfficient(lifterName));
        for (Name name : names) {
            if (origin.equalsIgnoreCase("showdown")){
                name.setLink("/web/showdown/" + name.getName());
            } else {
                name.setLink("/web/lifter/" + name.getName());
            }
        }
        return names;
    }

    public LifterData fetchLifter(String name) {
        ArrayList<Record> records = new ArrayList<>(recordRepository.findAllByNameOrderedByDateDesc(name));
        return new LifterData(generateLifterCard("/web/lifter/" + name, name, records.getFirst().getSex(), "raw"), records, null);
    }

    public LifterData fetchLifterShowdown(String name) {
        ArrayList<Record> records = new ArrayList<>(recordRepository.findAllByNameOrderedByDateDesc(name));
        return new LifterData(generateLifterCard("/web/showdown/" + name, name, records.getFirst().getSex(), "raw"), records, null);
    }

    public LifterCard generateLifterCard(String link, String name, String sex, String equip) {
        ArrayList<Float[]> largestStatsList = new ArrayList<>(recordRepository.findLargestStats(name, equip));
        Float[] largestStats = largestStatsList.getFirst();

        return new LifterCard(
                link,
                name,
                sex,
                "Classic",
                String.valueOf(largestStats[0]),
                String.valueOf(largestStats[1]),
                String.valueOf(largestStats[2]),
                String.valueOf(largestStats[3]),
                String.valueOf(largestStats[4])
        );
    }

    public LinkedHashMap<String, ArrayList<RegionRecord>> fetchTop10LiftersInEachRegion() {
        LinkedHashMap<String, ArrayList<RegionRecord>> regionalRecords = new LinkedHashMap<>();
        for (RegionMapper region : RegionMapper.values()) {
            ArrayList<RegionRecord> records = new ArrayList<>(new ArrayList<>(regionalRecordRepository.findTop10UniqueByFederationOrderedByGoodliftDescRawSbd(region.getAffiliatedFederation().toUpperCase())));
            for (RegionRecord record : records) {
                record.setLink("/web/lifter/" + record.getName());
            }
            regionalRecords.put(region.getRegionName(), records);
        }
        return regionalRecords;
    }

    public Map<String, Object> getChartData(Map<String, List<String>> filters) {
        String name = filters.get("name").getFirst();
        System.out.println("Name: " + name);
        // Get all records for the person (already implemented)
        List<Record> records = recordRepository.findAllByNameOrderedByDateAsc(name);

        System.out.println(records.size() + " records found.");

        // Order: bw, gl, squat, bench, deadlift, total, event, equipment
        List<String> filterList = filters.get("filters").stream().filter(s -> !s.equals("n/a")).toList();
        System.out.println("Filters: " + filterList);

        String event = filterList.get(filterList.size() - 2);
        String equipment = filterList.getLast();

        System.out.println("Event: " + event);
        System.out.println("Equipment: " + equipment);

        // Filter the records based on user-selected filters
        List<Record> filteredRecords = records.stream()
                .filter(record -> record.getStringFieldValue("event").equals(event)
                            && record.getStringFieldValue("equipment").equals(equipment))
                .toList();

        System.out.println(filteredRecords.size() + " filtered records found.");

        // Build the data model for the chart
        Map<String, Object> chartData = new HashMap<>();
        List<String> xAxis = new ArrayList<>();
        List<List<Float>> yAxisDatapoints = new ArrayList<>();
        List<String> yAxisLabels = new ArrayList<>();

        for (int i = 0; i < filterList.size() - 2; i++) {
            yAxisLabels.add(filterList.get(i).toUpperCase());
            yAxisDatapoints.add(new ArrayList<>());
        }

        // For example, iterate through filtered records to build arrays
        for (Record rec : filteredRecords) {
            xAxis.add(rec.getDate().toString());
            for (int i = 0; i < filterList.size() - 2; i++) {
                String filter = filterList.get(i);
                List<Float> currDp = yAxisDatapoints.get(i);
                currDp.add(rec.getFloatFieldValue(filter));
                yAxisDatapoints.set(i, currDp);
            }
            System.out.println(yAxisDatapoints);
            System.out.println(yAxisLabels);
        }

        for (int i = 0; i < yAxisDatapoints.size(); i++) {
            chartData.put(yAxisLabels.get(i), yAxisDatapoints.get(i));
        }

        // Determine a chart title and axis labels based on the filters
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < yAxisLabels.size(); i++) {
            if (i == yAxisLabels.size() - 1) {
                sb.append(yAxisLabels.get(i)).append(" Progression Over Time");
            } else {
                sb.append(yAxisLabels.get(i)).append(", ");
            }
        }
        String chartTitle = sb.toString();
        String xAxisLabel = "Date";
        chartData.put("chartTitle", chartTitle);
        chartData.put("xAxis", xAxis);
        chartData.put("xAxisLabel", xAxisLabel);
        chartData.put("yAxisLabels", yAxisLabels);

        return chartData;
    }

    public List<String> getFilters() {
        List<String> filters = new ArrayList<>();
        filters.add("bodyweightKg");
        filters.add("goodlift");
        filters.add("best3SquatKg");
        filters.add("best3BenchKg");
        filters.add("best3DeadliftKg");
        filters.add("totalKg");
        filters.add("event");
        filters.add("equipment");
        return filters;
    }
}
