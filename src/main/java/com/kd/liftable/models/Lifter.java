package com.kd.liftable.models;

public record Lifter(
    String name,
    String sex,
    String equip,
    Float squat,
    Float bench,
    Float deadlift,
    Float total,
    Float glp) {
}
