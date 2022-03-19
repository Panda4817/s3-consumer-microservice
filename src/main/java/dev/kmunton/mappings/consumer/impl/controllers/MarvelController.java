package dev.kmunton.mappings.consumer.impl.controllers;

import dev.kmunton.mappings.consumer.api.ConsumerContract;
import dev.kmunton.mappings.consumer.impl.services.MarvelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MarvelController implements ConsumerContract {

    @Autowired
    private MarvelService marvelService;

    @Override
    @GetMapping(value = "marvel", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getMarvelHero(String attribute1, String attribute2) {
        return new ResponseEntity<>(marvelService.getMarvelHero(attribute1, attribute2), HttpStatus.OK);
    }

    @Override
    @GetMapping(value = "mappings")
    public ResponseEntity<Void> getNewMappings(String key) throws IOException {
        marvelService.updateMappings(key);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
