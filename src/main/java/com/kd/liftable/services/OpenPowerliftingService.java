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

    public ArrayList<String> getLineChartData(String name) {
        // Prepare your list of query results in a known order.
        // (0) Raw BWGL, (1) Raw SBD, (2) Raw Total, (3) Single-Ply BWGL, (4) Single-Ply SBD, (5) Single-Ply Total
        ArrayList<ArrayList<Object[]>> allStats = new ArrayList<>(List.of(
                new ArrayList<>(recordRepository.findBWGLByNameDistinctDateOrderByDateAsc(name, "Raw", "SBD")),
                new ArrayList<>(recordRepository.findSBDByNameDistinctDateOrderByDateAsc(name, "Raw")),
                new ArrayList<>(recordRepository.findTotalByNameDistinctDateOrderByDateAsc(name, "Raw", "SBD")),
                new ArrayList<>(recordRepository.findBWGLByNameDistinctDateOrderByDateAsc(name, "Single-Ply", "SBD")),
                new ArrayList<>(recordRepository.findSBDByNameDistinctDateOrderByDateAsc(name, "Single-Ply")),
                new ArrayList<>(recordRepository.findTotalByNameDistinctDateOrderByDateAsc(name, "Single-Ply", "SBD"))
        ));

        ArrayList<String> allStatsStrings = new ArrayList<>();

        // Define mappings for descriptive keys and titles
        // The keys in these maps correspond to the positions in the result row (after the date column)
        List<Map<Integer, String>> labelMappings = new ArrayList<>();
        labelMappings.add(Map.of(
                0, "Bodyweight (kg)",
                1, "Goodlift"  // You can append units if needed, e.g., "Goodlift (kg)"
        ));
        labelMappings.add(Map.of(
                0, "Squat (kg)",
                1, "Bench (kg)",
                2, "Deadlift (kg)"
        ));
        labelMappings.add(Map.of(
                0, "Total (kg)"
        ));
        labelMappings.add(Map.of(
                0, "Bodyweight (kg)",
                1, "Goodlift"
        ));
        labelMappings.add(Map.of(
                0, "Squat (kg)",
                1, "Bench (kg)",
                2, "Deadlift (kg)"
        ));
        labelMappings.add(Map.of(
                0, "Total (kg)"
        ));

        // Optionally, set chart titles for each dataset.
        List<String> chartTitles = List.of(
                "Bodyweight & GL Progression (Raw, SBD)",
                "SBD Lifts Progression (Raw, Inclusive)",
                "Total Lift Progression (Raw, SBD)",
                "Bodyweight & GL Progression (Single-Ply, SBD)",
                "SBD Lifts Progression (Single-Ply, Inclusive)",
                "Total Lift Progression (Single-Ply, SBD)"
        );

        for (int datasetIndex = 0; datasetIndex < allStats.size(); datasetIndex++) {
            ArrayList<Object[]> stats = allStats.get(datasetIndex);
            Map<String, Object> data = new LinkedHashMap<>();
            List<String> xAxis = new ArrayList<>();
            data.put("xAxis", xAxis);
            // Add the chart title
            data.put("chartTitle", chartTitles.get(datasetIndex));

            // If no data is found, return an object with empty arrays (and title) for proper front-end handling.
            if (stats.isEmpty()) {
                // Provide empty keys (use at least one default key if needed)
                data.put("noData", new ArrayList<>());
                try {
                    String emptyDataString = new ObjectMapper().writeValueAsString(data);
                    allStatsStrings.add(emptyDataString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                continue;
            }

            // Get the mapping for this dataset - note that result rows have 1 date column + n lift columns.
            Map<Integer, String> mapping = labelMappings.get(datasetIndex);
            int numY = stats.get(0).length - 1; // number of data columns

            // Create series lists with descriptive keys as specified in the mapping.
            for (int i = 0; i < numY; i++) {
                // Use the mapping to get a descriptive key. If a key is missing, you can use a fallback.
                String label = mapping.getOrDefault(i, "Lift " + (i + 1));
                data.put(label, new ArrayList<Float>());
            }

            // Process each row and fill the x-axis dates and y-series arrays.
            for (Object[] row : stats) {
                // Assume the first column is a Date; convert it to a String in ISO format.
                xAxis.add(((Date) row[0]).toLocalDate().toString());
                // Loop through the remaining columns
                int keyIndex = 0;
                for (int i = 1; i < row.length; i++) {
                    String key = mapping.getOrDefault(keyIndex, "Lift " + (keyIndex + 1));
                    // Retrieve the list that was assigned to this key and add the value.
                    @SuppressWarnings("unchecked")
                    List<Float> seriesData = (List<Float>) data.get(key);
                    // Note: you might want to handle type conversion and null values as needed.
                    seriesData.add(row[i] != null ? (Float) row[i] : null);
                    keyIndex++;
                }
            }

            // Serialize the data map to JSON
            try {
                String chartDataString = new ObjectMapper().writeValueAsString(data);
                allStatsStrings.add(chartDataString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return allStatsStrings;
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
