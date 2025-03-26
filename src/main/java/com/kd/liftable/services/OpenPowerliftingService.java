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
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        ArrayList<ArrayList<Object[]>> allStats = new ArrayList<>(List.of(
                new ArrayList<>(recordRepository.findBWGLByNameDistinctDateOrderByDateAsc(name, "Raw", "SBD")),
                new ArrayList<>(recordRepository.findSBDByNameDistinctDateOrderByDateAsc(name, "Raw")),
                new ArrayList<>(recordRepository.findTotalByNameDistinctDateOrderByDateAsc(name, "Raw", "SBD")),
                new ArrayList<>(recordRepository.findBWGLByNameDistinctDateOrderByDateAsc(name, "Single-Ply", "SBD")),
                new ArrayList<>(recordRepository.findSBDByNameDistinctDateOrderByDateAsc(name, "Single-Ply")),
                new ArrayList<>(recordRepository.findTotalByNameDistinctDateOrderByDateAsc(name, "Single-Ply", "SBD"))
        ));

        ArrayList<String> allStatsStrings = new ArrayList<>();

        for (ArrayList<Object[]> stats : allStats) {
            Map<String, Object> data = new LinkedHashMap<>();
            List<String> x = new ArrayList<>();
            data.put("xAxis", x);
            List<List<Float>> ys = new ArrayList<>();

            // Check if stats is empty
            if (stats.isEmpty()) {
                // Populate empty dataset for frontend handling
                data.put("xAxis", new ArrayList<>());
                data.put("lift1", new ArrayList<>()); // Add at least one lift key for parsing
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    String emptyDataString = mapper.writeValueAsString(data);
                    allStatsStrings.add(emptyDataString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                continue; // Skip further processing for this dataset
            }

            // Populate non-empty dataset
            int numY = stats.get(0).length - 1; // Updated from `getFirst()` to `get(0)`
            for (int i = 0; i < numY; i++) {
                ys.add(new ArrayList<>());
                data.put("lift" + (i + 1), ys.get(i));
            }

            for (Object[] row : stats) {
                x.add(((Date) row[0]).toLocalDate().toString()); // Convert Date to LocalDate and String
                for (int i = 1; i < row.length; i++) {
                    ys.get(i - 1).add(row[i] != null ? (Float) row[i] : null);
                }
            }

            // Serialize to JSON
            try {
                ObjectMapper mapper = new ObjectMapper();
                String chartDataString = mapper.writeValueAsString(data);
                allStatsStrings.add(chartDataString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return allStatsStrings;
    }

}
