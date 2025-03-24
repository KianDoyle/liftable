package com.kd.liftable.models;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

@Setter
@Getter
public class LifterCard {
    private String Link;
    private String Name;
    private String Sex;
    private String Equipment;
    private String Best3SquatKg;
    private String Best3BenchKg;
    private String Best3DeadliftKg;
    private String TotalKg;
    private String Goodlift;

    // Default constructor
    public LifterCard() {
    }


    public LifterCard(String link, String name, String sex, String equipment, String best3SquatKg, String best3BenchKg, String best3DeadliftKg, String totalKg, String goodlift) {
        Link = link;
        Name = name;
        Sex = sex;
        Equipment = equipment;
        Best3SquatKg = best3SquatKg;
        Best3BenchKg = best3BenchKg;
        Best3DeadliftKg = best3DeadliftKg;
        TotalKg = totalKg;
        Goodlift = goodlift;
    }

    public static float parseFloatSafe(String value) {
        try {
            return (value != null && !value.trim().isEmpty()) ? Float.parseFloat(value.trim()) : 0f;
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    public String getFieldValue(String fieldName) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName); // Get field by name
            field.setAccessible(true); // Allow access to private fields
            return (String) field.get(this); // Return field value
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return "Field not found or inaccessible: " + fieldName;
        }
    }

}


