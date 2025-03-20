package com.kd.liftable.models;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
public class RecordData {
    private JsonNode chartData;
    private ArrayList<PowerliftingRecord> records;

    public RecordData() {

    }

    public RecordData(JsonNode chartData, ArrayList<PowerliftingRecord> records) {
        this.chartData = chartData;
        this.records = records;
    }

}
