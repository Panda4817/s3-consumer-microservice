package dev.kmunton.mappings.consumer.impl.models;

public record MarvelKey(String attribute1, String attribute2, String attribute3) {
    public MarvelKey(String attribute1, String attribute2, String attribute3) {
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
    }
}
