package com.kd.liftable.services;

import com.kd.liftable.models.*;
import com.kd.liftable.models.Record;
import com.kd.liftable.repositories.NameRepository;
import com.kd.liftable.repositories.RecordRepository;
import com.kd.liftable.repositories.RegionalRecordRepository;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.ArrayList;

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
}
