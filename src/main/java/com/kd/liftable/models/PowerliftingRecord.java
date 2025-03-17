package com.kd.liftable.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PowerliftingRecord {

    @JsonProperty("Name")
    private String Name;

    @JsonProperty("Sex")
    private String Sex;

    @JsonProperty("Event")
    private String Event;

    @JsonProperty("Equipment")
    private String Equipment;

    @JsonProperty("Age")
    private String Age;

    @JsonProperty("AgeClass")
    private String AgeClass;

    @JsonProperty("BirthYearClass")
    private String BirthYearClass;

    @JsonProperty("Division")
    private String Division;

    @JsonProperty("BodyweightKg")
    private String BodyweightKg;

    @JsonProperty("WeightClassKg")
    private String WeightClassKg;

    @JsonProperty("Squat1Kg")
    private String Squat1Kg;

    @JsonProperty("Squat2Kg")
    private String Squat2Kg;

    @JsonProperty("Squat3Kg")
    private String Squat3Kg;

    @JsonProperty("Squat4Kg")
    private String Squat4Kg;

    @JsonProperty("Best3SquatKg")
    private String Best3SquatKg;

    @JsonProperty("Bench1Kg")
    private String Bench1Kg;

    @JsonProperty("Bench2Kg")
    private String Bench2Kg;

    @JsonProperty("Bench3Kg")
    private String Bench3Kg;

    @JsonProperty("Bench4Kg")
    private String Bench4Kg;

    @JsonProperty("Best3BenchKg")
    private String Best3BenchKg;

    @JsonProperty("Deadlift1Kg")
    private String Deadlift1Kg;

    @JsonProperty("Deadlift2Kg")
    private String Deadlift2Kg;

    @JsonProperty("Deadlift3Kg")
    private String Deadlift3Kg;

    @JsonProperty("Deadlift4Kg")
    private String Deadlift4Kg;

    @JsonProperty("Best3DeadliftKg")
    private String Best3DeadliftKg;

    @JsonProperty("TotalKg")
    private String TotalKg;

    @JsonProperty("Place")
    private String Place;

    @JsonProperty("Dots")
    private String Dots;

    @JsonProperty("Wilks")
    private String Wilks;

    @JsonProperty("Glossbrenner")
    private String Glossbrenner;

    @JsonProperty("Goodlift")
    private String Goodlift;

    @JsonProperty("Tested")
    private String Tested;

    @JsonProperty("Country")
    private String Country;

    @JsonProperty("State")
    private String State;

    @JsonProperty("Federation")
    private String Federation;

    @JsonProperty("ParentFederation")
    private String ParentFederation;

    @JsonProperty("Date")
    private String Date;

    @JsonProperty("MeetCountry")
    private String MeetCountry;

    @JsonProperty("MeetState")
    private String MeetState;

    @JsonProperty("MeetTown")
    private String MeetTown;

    @JsonProperty("MeetName")
    private String MeetName;

    @JsonProperty("Sanctioned")
    private String Sanctioned;

    // Default constructor
    public PowerliftingRecord() {
    }

    // Parameterized constructor
    public PowerliftingRecord(String Name, String Sex, String Event, String Equipment, String Age, String AgeClass,
                              String BirthYearClass, String Division, String BodyweightKg, String WeightClassKg,
                              String Squat1Kg, String Squat2Kg, String Squat3Kg, String Squat4Kg, String Best3SquatKg,
                              String Bench1Kg, String Bench2Kg, String Bench3Kg, String Bench4Kg, String Best3BenchKg,
                              String Deadlift1Kg, String Deadlift2Kg, String Deadlift3Kg, String Deadlift4Kg,
                              String Best3DeadliftKg, String TotalKg, String Place, String Dots, String Wilks,
                              String Glossbrenner, String Goodlift, String Tested, String Country, String State,
                              String Federation, String ParentFederation, String Date, String MeetCountry, String MeetState,
                              String MeetTown, String MeetName, String Sanctioned) {
        this.Name = Name;
        this.Sex = Sex;
        this.Event = Event;
        this.Equipment = Equipment;
        this.Age = Age;
        this.AgeClass = AgeClass;
        this.BirthYearClass = BirthYearClass;
        this.Division = Division;
        this.BodyweightKg = BodyweightKg;
        this.WeightClassKg = WeightClassKg;
        this.Squat1Kg = Squat1Kg;
        this.Squat2Kg = Squat2Kg;
        this.Squat3Kg = Squat3Kg;
        this.Squat4Kg = Squat4Kg;
        this.Best3SquatKg = Best3SquatKg;
        this.Bench1Kg = Bench1Kg;
        this.Bench2Kg = Bench2Kg;
        this.Bench3Kg = Bench3Kg;
        this.Bench4Kg = Bench4Kg;
        this.Best3BenchKg = Best3BenchKg;
        this.Deadlift1Kg = Deadlift1Kg;
        this.Deadlift2Kg = Deadlift2Kg;
        this.Deadlift3Kg = Deadlift3Kg;
        this.Deadlift4Kg = Deadlift4Kg;
        this.Best3DeadliftKg = Best3DeadliftKg;
        this.TotalKg = TotalKg;
        this.Place = Place;
        this.Dots = Dots;
        this.Wilks = Wilks;
        this.Glossbrenner = Glossbrenner;
        this.Goodlift = Goodlift;
        this.Tested = Tested;
        this.Country = Country;
        this.State = State;
        this.Federation = Federation;
        this.ParentFederation = ParentFederation;
        this.Date = Date;
        this.MeetCountry = MeetCountry;
        this.MeetState = MeetState;
        this.MeetTown = MeetTown;
        this.MeetName = MeetName;
        this.Sanctioned = Sanctioned;
    }

}


