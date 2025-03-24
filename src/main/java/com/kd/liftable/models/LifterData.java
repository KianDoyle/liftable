package com.kd.liftable.models;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
public class LifterData {
    private JsonNode chartData;
    private PowerliftingRecord lifterRecord;
    private LifterCard lifterCard;
    private ArrayList<PowerliftingRecord> plRecords;
    private ArrayList<Record> records;

    public LifterData() {

    }

    public LifterData(JsonNode chartData, PowerliftingRecord lifterRecord, ArrayList<PowerliftingRecord> records) {
        this.chartData = chartData;
        this.lifterRecord = lifterRecord;
        this.plRecords = records;
    }

    public LifterData(LifterCard lifterCard, ArrayList<Record> records, JsonNode chartData) {
        this.lifterCard = lifterCard;
        this.records = records;
        this.chartData = chartData;
    }

}
