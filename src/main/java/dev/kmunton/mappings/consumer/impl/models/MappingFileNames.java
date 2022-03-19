package dev.kmunton.mappings.consumer.impl.models;

public enum MappingFileNames {
    MARVEL("marvel_mappings");

    private String name;

    MappingFileNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
