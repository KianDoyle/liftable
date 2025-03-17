package com.kd.liftable.models;

public enum RegionMapper {
    EUROPE("europe", "epf"),
    NORTH_AMERICA("na", "napf"),
    SOUTH_AMERICA("sa", "fesupo"),
    ASIA("asia", "asianpf"),
    AFRICA("africa", "africanpf"),
    OCEANIA("oceania", "orpf");

    private final String regionName;
    private final String affiliatedFederation;

    // Constructor to initialize the enum with region and federation
    RegionMapper(String regionName, String affiliatedFederation) {
        this.regionName = regionName;
        this.affiliatedFederation = affiliatedFederation;
    }

    // Getter for region name
    public String getRegionName() {
        return regionName;
    }

    // Getter for affiliated federation
    public String getAffiliatedFederation() {
        return affiliatedFederation;
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

