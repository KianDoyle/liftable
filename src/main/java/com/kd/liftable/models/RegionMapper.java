package com.kd.liftable.models;

import lombok.Getter;

@Getter
public enum RegionMapper {
    EUROPE("europe", "epf"),
    NORTH_AMERICA("north america", "napf"),
    SOUTH_AMERICA("south america", "fesupo"),
    ASIA("asia", "asianpf"),
    AFRICA("africa", "africanpf"),
    OCEANIA("oceania", "orpf");

    // Getter for region name
    private final String regionName;
    // Getter for affiliated federation
    private final String affiliatedFederation;

    // Constructor to initialize the enum with region and federation
    RegionMapper(String regionName, String affiliatedFederation) {
        this.regionName = regionName;
        this.affiliatedFederation = affiliatedFederation;
    }

    // Method to get federation by region name
    public static String getFederationByRegion(String regionName) {
        for (RegionMapper region : values()) {
            if (region.getRegionName().equalsIgnoreCase(regionName)) {
                return region.getAffiliatedFederation();
            }
        }
        return "No federation found for this region";  // Default message if region is not found
    }

}

