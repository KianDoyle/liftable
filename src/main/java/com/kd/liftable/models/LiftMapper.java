package com.kd.liftable.models;

import lombok.Getter;

@Getter
public enum LiftMapper {
    LINK("Link"),
    NAME("Name"),
    SEX("Sex"),
    EVENT("Event"),
    EQUIPMENT("Equipment"),
    AGE("Age"),
    AGE_CLASS("AgeClass"),
    BIRTH_YEAR_CLASS("BirthYearClass"),
    DIVISION("Division"),
    BODYWEIGHT("BodyweightKg"),
    WEIGHT_CLASS("WeightClassKg"),
    SQUAT1("Squat1Kg"),
    SQUAT2("Squat2Kg"),
    SQUAT3("Squat3Kg"),
    SQUAT4("Squat4Kg"),
    BEST_SQUAT("Best3SquatKg"),
    BENCH1("Bench1Kg"),
    BENCH2("Bench2Kg"),
    BENCH3("Bench3Kg"),
    BENCH4("Bench4Kg"),
    BEST_BENCH("Best3BenchKg"),
    DEADLIFT1("Deadlift1Kg"),
    DEADLIFT2("Deadlift2Kg"),
    DEADLIFT3("Deadlift3Kg"),
    DEADLIFT4("Deadlift4Kg"),
    BEST_DEADLIFT("Best3DeadliftKg"),
    TOTAL("TotalKg"),
    PLACE("Place"),
    DOTS("Dots"),
    WILKS("Wilks"),
    GLOSSBRENNER("Glossbrenner"),
    GOODLIFT("Goodlift"),
    TESTED("Tested"),
    COUNTRY("Country"),
    STATE("State"),
    FEDERATION("Federation"),
    PARENT_FEDERATION("ParentFederation"),
    DATE("Date"),
    MEET_COUNTRY("MeetCountry"),
    MEET_STATE("MeetState"),
    MEET_TOWN("MeetTown"),
    MEET_NAME("MeetName"),
    SANCTIONED("Sanctioned");

    private final String colName;

    LiftMapper(String colName) {
        this.colName = colName;
    }

    public static String getMappedField(String key) {
        try {
            return LiftMapper.valueOf(key.toUpperCase()).getColName();
        } catch (IllegalArgumentException e) {
            return "Invalid field name: " + key;
        }
    }
}
