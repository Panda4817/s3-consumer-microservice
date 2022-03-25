package dev.kmunton.mappings.consumer.impl.services;

import dev.kmunton.mappings.consumer.impl.models.MarvelKey;
import dev.kmunton.mappings.consumer.impl.utils.MappingsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static dev.kmunton.mappings.consumer.impl.utils.StringHandlerUtils.handleEmpty;

@Service
public class MarvelService {


    private MappingsUtils mappingsUtils;

    private Map<MarvelKey, String> marvelMappings;

    @Autowired
    public MarvelService(MappingsUtils mappingsUtils) {
        this.mappingsUtils = mappingsUtils;
    }

    @PostConstruct
    private void init() throws IOException {
        marvelMappings = mappingsUtils.getMarvelMappings();
    }

    public String getMarvelHero(String attribute1, String attribute2, String attribute3) {

        MarvelKey key = new MarvelKey(handleEmpty(attribute1), handleEmpty(attribute2), handleEmpty(attribute3));

        System.out.println(key);

        Optional<Map.Entry<MarvelKey, String>> res = marvelMappings.entrySet().stream()
                .filter(mapping ->
                        Objects.equals(mapping.getKey().attribute1(), key.attribute1()))
                .filter(mapping ->
                        Objects.equals(mapping.getKey().attribute2(), key.attribute2())
                                || Objects.equals(key.attribute2(), ""))
                .filter(mapping ->
                        Objects.equals(mapping.getKey().attribute3(), key.attribute3())
                                || Objects.equals(key.attribute3(), ""))
                .findFirst();

        if (res.isPresent()) return res.get().getValue();

        return "No marvel hero found for those attributes";

    }

    public void updateMappings(final String key) throws IOException {

        if (key.contains("marvel_mappings")) {
            marvelMappings = mappingsUtils.getMarvelMappings(key);
            System.out.println("New mapping file being used, with key=" + key);
        }

        System.out.println("Key is incorrect or this mapping file is not used in this microservice, key=" + key);

    }

}
