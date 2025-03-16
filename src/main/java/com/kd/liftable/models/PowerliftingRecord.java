package com.kd.liftable.models;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getAgeClass() {
        return AgeClass;
    }

    public void setAgeClass(String ageClass) {
        AgeClass = ageClass;
    }

    public String getBirthYearClass() {
        return BirthYearClass;
    }

    public void setBirthYearClass(String birthYearClass) {
        BirthYearClass = birthYearClass;
    }

    public String getDivision() {
        return Division;
    }

    public void setDivision(String division) {
        Division = division;
    }

    public String getBodyweightKg() {
        return BodyweightKg;
    }

    public void setBodyweightKg(String bodyweightKg) {
        BodyweightKg = bodyweightKg;
    }

    public String getWeightClassKg() {
        return WeightClassKg;
    }

    public void setWeightClassKg(String weightClassKg) {
        WeightClassKg = weightClassKg;
    }

    public String getSquat1Kg() {
        return Squat1Kg;
    }

    public void setSquat1Kg(String squat1Kg) {
        Squat1Kg = squat1Kg;
    }

    public String getSquat2Kg() {
        return Squat2Kg;
    }

    public void setSquat2Kg(String squat2Kg) {
        Squat2Kg = squat2Kg;
    }

    public String getSquat3Kg() {
        return Squat3Kg;
    }

    public void setSquat3Kg(String squat3Kg) {
        Squat3Kg = squat3Kg;
    }

    public String getSquat4Kg() {
        return Squat4Kg;
    }

    public void setSquat4Kg(String squat4Kg) {
        Squat4Kg = squat4Kg;
    }

    public String getBest3SquatKg() {
        return Best3SquatKg;
    }

    public void setBest3SquatKg(String best3SquatKg) {
        Best3SquatKg = best3SquatKg;
    }

    public String getBench1Kg() {
        return Bench1Kg;
    }

    public void setBench1Kg(String bench1Kg) {
        Bench1Kg = bench1Kg;
    }

    public String getBench2Kg() {
        return Bench2Kg;
    }

    public void setBench2Kg(String bench2Kg) {
        Bench2Kg = bench2Kg;
    }

    public String getBench3Kg() {
        return Bench3Kg;
    }

    public void setBench3Kg(String bench3Kg) {
        Bench3Kg = bench3Kg;
    }

    public String getBench4Kg() {
        return Bench4Kg;
    }

    public void setBench4Kg(String bench4Kg) {
        Bench4Kg = bench4Kg;
    }

    public String getBest3BenchKg() {
        return Best3BenchKg;
    }

    public void setBest3BenchKg(String best3BenchKg) {
        Best3BenchKg = best3BenchKg;
    }

    public String getDeadlift1Kg() {
        return Deadlift1Kg;
    }

    public void setDeadlift1Kg(String deadlift1Kg) {
        Deadlift1Kg = deadlift1Kg;
    }

    public String getDeadlift2Kg() {
        return Deadlift2Kg;
    }

    public void setDeadlift2Kg(String deadlift2Kg) {
        Deadlift2Kg = deadlift2Kg;
    }

    public String getDeadlift3Kg() {
        return Deadlift3Kg;
    }

    public void setDeadlift3Kg(String deadlift3Kg) {
        Deadlift3Kg = deadlift3Kg;
    }

    public String getDeadlift4Kg() {
        return Deadlift4Kg;
    }

    public void setDeadlift4Kg(String deadlift4Kg) {
        Deadlift4Kg = deadlift4Kg;
    }

    public String getBest3DeadliftKg() {
        return Best3DeadliftKg;
    }

    public void setBest3DeadliftKg(String best3DeadliftKg) {
        Best3DeadliftKg = best3DeadliftKg;
    }

    public String getTotalKg() {
        return TotalKg;
    }

    public void setTotalKg(String totalKg) {
        TotalKg = totalKg;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getDots() {
        return Dots;
    }

    public void setDots(String dots) {
        Dots = dots;
    }

    public String getWilks() {
        return Wilks;
    }

    public void setWilks(String wilks) {
        Wilks = wilks;
    }

    public String getGlossbrenner() {
        return Glossbrenner;
    }

    public void setGlossbrenner(String glossbrenner) {
        Glossbrenner = glossbrenner;
    }

    public String getGoodlift() {
        return Goodlift;
    }

    public void setGoodlift(String goodlift) {
        Goodlift = goodlift;
    }

    public String getTested() {
        return Tested;
    }

    public void setTested(String tested) {
        Tested = tested;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getFederation() {
        return Federation;
    }

    public void setFederation(String federation) {
        Federation = federation;
    }

    public String getParentFederation() {
        return ParentFederation;
    }

    public void setParentFederation(String parentFederation) {
        ParentFederation = parentFederation;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getMeetCountry() {
        return MeetCountry;
    }

    public void setMeetCountry(String meetCountry) {
        MeetCountry = meetCountry;
    }

    public String getMeetState() {
        return MeetState;
    }

    public void setMeetState(String meetState) {
        MeetState = meetState;
    }

    public String getMeetTown() {
        return MeetTown;
    }

    public void setMeetTown(String meetTown) {
        MeetTown = meetTown;
    }

    public String getMeetName() {
        return MeetName;
    }

    public void setMeetName(String meetName) {
        MeetName = meetName;
    }

    public String getSanctioned() {
        return Sanctioned;
    }

    public void setSanctioned(String sanctioned) {
        Sanctioned = sanctioned;
    }
}


