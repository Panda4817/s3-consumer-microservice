package dev.kmunton.mappings.consumer.impl.utils;

import dev.kmunton.mappings.consumer.impl.models.MarvelKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dev.kmunton.mappings.consumer.impl.models.MappingFileNames.MARVEL;
import static dev.kmunton.mappings.consumer.impl.utils.StringHandlerUtils.handleEmpty;

@Component
public class MappingsUtils {


    private final static String BUCKET_NAME = "mappings-demo";

    private final static S3Client s3Client = S3Client.builder().region(Region.EU_WEST_2)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();

    public Map<MarvelKey, String> getMarvelMappings() throws IOException {
        try {

            return readLines(new InputStreamReader(getLatestObject(MARVEL.getName()))).stream()
                    .collect(Collectors.toMap(s -> new MarvelKey(handleEmpty(s[0]), handleEmpty(s[1]), handleEmpty(s[2])), s -> s[3]));

        } catch (S3Exception | IOException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    private List<String[]> readLines(final InputStreamReader data) throws IOException {
        BufferedReader reader = new BufferedReader(data);
        return reader.lines()
                .map(s -> StringUtils.stripAll(s.split(",")))
                .collect(Collectors.toList());

    }

    private ResponseInputStream<GetObjectResponse> getLatestObject(final String filter) {

        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request
                .builder()
                .bucket(BUCKET_NAME)
                .prefix(filter)
                .build();

        ListObjectsV2Response res = s3Client.listObjectsV2(listObjectsRequest);
        String key = res.contents().stream().max(Comparator.comparing(S3Object::lastModified)).get().key();
        System.out.println(key);
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        return s3Client.getObject(getObjectRequest);
    }
}
