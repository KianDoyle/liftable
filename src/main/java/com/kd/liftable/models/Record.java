package com.kd.liftable.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

@Setter
@Getter
@Entity
@Table(name = "LifterData", schema = "open_ipf_db")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Sex")
    private String sex;

    @Column(name = "Event")
    private String event;

    @Column(name = "Equipment")
    private String equipment;

    @Column(name = "Age")
    private Integer age;

    @Column(name = "AgeClass")
    private String ageClass;

    @Column(name = "BirthYearClass")
    private String birthYearClass;

    @Column(name = "Division")
    private String division;

    @Column(name = "BodyweightKg")
    private Float bodyweightKg;

    @Column(name = "WeightClassKg")
    private String weightClassKg;

    @Column(name = "Squat1Kg")
    private Float squat1Kg;

    @Column(name = "Squat2Kg")
    private Float squat2Kg;

    @Column(name = "Squat3Kg")
    private Float squat3Kg;

    @Column(name = "Squat4Kg")
    private Float squat4Kg;

    @Column(name = "Best3SquatKg")
    private Float best3SquatKg;

    @Column(name = "Bench1Kg")
    private Float bench1Kg;

    @Column(name = "Bench2Kg")
    private Float bench2Kg;

    @Column(name = "Bench3Kg")
    private Float bench3Kg;

    @Column(name = "Bench4Kg")
    private Float bench4Kg;

    @Column(name = "Best3BenchKg")
    private Float best3BenchKg;

    @Column(name = "Deadlift1Kg")
    private Float deadlift1Kg;

    @Column(name = "Deadlift2Kg")
    private Float deadlift2Kg;

    @Column(name = "Deadlift3Kg")
    private Float deadlift3Kg;

    @Column(name = "Deadlift4Kg")
    private Float deadlift4Kg;

    @Column(name = "Best3DeadliftKg")
    private Float best3DeadliftKg;

    @Column(name = "TotalKg")
    private Float totalKg;

    @Column(name = "Place")
    private String place;

    @Column(name = "Dots")
    private Float dots;

    @Column(name = "Wilks")
    private Float wilks;

    @Column(name = "Glossbrenner")
    private Float glossbrenner;

    @Column(name = "Goodlift")
    private Float goodlift;

    @Column(name = "Tested")
    private String tested;

    @Column(name = "Country")
    private String country;

    @Column(name = "State")
    private String state;

    @Column(name = "Federation")
    private String federation;

    @Column(name = "ParentFederation")
    private String parentFederation;

    @Column(name = "Date")
    private String date;

    @Column(name = "MeetCountry")
    private String meetCountry;

    @Column(name = "MeetState")
    private String meetState;

    @Column(name = "MeetTown")
    private String meetTown;

    @Column(name = "MeetName")
    private String meetName;

    @Column(name = "Sanctioned")
    private String sanctioned;

    // Default constructor
    public Record() {
    }

    public float getFloatFieldValue(String fieldName) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName); // Get field by name
            field.setAccessible(true); // Allow access to private fields
            return (float) field.get(this); // Return field value
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return 0f;
        }
    }
}
