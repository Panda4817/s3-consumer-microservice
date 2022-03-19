package dev.kmunton.mappings.consumer.impl.utils;

import dev.kmunton.mappings.consumer.impl.models.MarvelKey;
import dev.kmunton.mappings.consumer.impl.services.MarvelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
public class MappingsUtilsTest {

    @Mock
    MappingsUtils mappingsUtils;

    private MarvelService marvelService;

    @BeforeEach
    public void setUp() {
        marvelService = new MarvelService(mappingsUtils);
    }


    @Test
    public void givenAttributeAndMappings_whenGetMarvelHero_returnValidResponse() {
        // Given
        Map<MarvelKey, String> mappings = new HashMap<>();
        mappings.put(new MarvelKey("green", "angry", ""), "hulk");
        setField(marvelService, "marvelMappings", mappings);

        // When
        String res = marvelService.getMarvelHero("green", "");

        // Then
        assertThat(res, is("hulk"));
    }

    @Test
    public void givenAttributeAndNotMappings_whenGetMarvelHero_returnInValidResponse() {
        // Given
        Map<MarvelKey, String> mappings = new HashMap<>();
        setField(marvelService, "marvelMappings", mappings);

        // When
        String res = marvelService.getMarvelHero("green", "");

        // Then
        assertThat(res, is("No marvel hero found for those attributes"));
    }

    @Test
    public void givenNewMappings_whenUpdateMappings_returnNull() throws IOException {
        // Given
        Map<MarvelKey, String> mappings = new HashMap<>();
        setField(marvelService, "marvelMappings", mappings);
        when(mappingsUtils.getMarvelMappings()).thenReturn(mappings);

        // When
        marvelService.updateMappings("marvel_mappings");

        // Then
        verify(mappingsUtils, times(1)).getMarvelMappings();
    }

    @Test
    public void givenNoNewMappings_whenUpdateMappings_returnNull() throws IOException {
        // Given
        Map<MarvelKey, String> mappings = new HashMap<>();
        setField(marvelService, "marvelMappings", mappings);

        // When
        marvelService.updateMappings("other");

        // Then
        verify(mappingsUtils, times(0)).getMarvelMappings();
    }

}