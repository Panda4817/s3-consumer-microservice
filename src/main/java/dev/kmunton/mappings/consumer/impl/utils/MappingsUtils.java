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
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static dev.kmunton.mappings.consumer.impl.utils.StringHandlerUtils.handleEmpty;

@Component
public class MappingsUtils {

    
    private final static String MARVEL_BUCKET_NAME = "mappings-demo";
    private final static String MARVEL_FILE_NAME_PREFIX = "marvel_mappings";
    private final static Pattern FILE_NAME_REGEX = Pattern.compile("^([a-zA-Z_]+)_v([0-9])_([0-9])\\.csv$");

    private final static S3Client s3Client = S3Client.builder().region(Region.EU_WEST_2)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();

    public Map<MarvelKey, String> getMarvelMappings() throws IOException {
        return transformToMap(findAndGetLatestObjectFromBucket(MARVEL_BUCKET_NAME, MARVEL_FILE_NAME_PREFIX));
    }

    public Map<MarvelKey, String> getMarvelMappings(final String fileName) throws IOException {
        return transformToMap(getLatestObject(MARVEL_BUCKET_NAME, fileName));
    }

    private Map<MarvelKey, String> transformToMap(final  ResponseInputStream<GetObjectResponse> responseStream) throws IOException {
        try {
            return readLines(new InputStreamReader(responseStream)).stream()
                    .collect(Collectors.toMap(s -> new MarvelKey(handleEmpty(s[0]), handleEmpty(s[1]),handleEmpty(s[2])), s -> s[3]));

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

    private ResponseInputStream<GetObjectResponse> findAndGetLatestObjectFromBucket(final String bucketName, final String fileNamePrefix) {

        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request
                .builder()
                .bucket(bucketName)
                .prefix(fileNamePrefix)
                .build();

        ListObjectsV2Response res = s3Client.listObjectsV2(listObjectsRequest);
        List<S3Object> s3Objects = res.contents();
        String latestVersion = getLatestVersion(s3Objects);
        System.out.println("Looking for latest version " + latestVersion);
        Optional<S3Object> latestObject = s3Objects.stream().filter(object -> object.key().equals(fileNamePrefix+"_v"+latestVersion)).findFirst();
        if(latestObject.isEmpty()) {
            System.out.println("No file found with latest version");
            throw new RuntimeException("No file found with latest version");
        }

        return getLatestObject(bucketName, latestObject.get().key());
    }

    private boolean isFollowingFileNameStandard(final String fileName) {
        return FILE_NAME_REGEX.matcher(fileName).matches();
    }


    private String getLatestVersion(final List<S3Object> s3Objects) {
        OptionalDouble version = s3Objects.stream()
                .filter(object -> isFollowingFileNameStandard(object.key()))
                .map(object -> object.key().split("_v")[1].split("\\.")[0])
                .map(versionStr -> versionStr.replace("_", "."))
                .mapToDouble(Double::parseDouble)
                .max();

        if (version.isPresent()) {
            return String.valueOf(version.getAsDouble()).replace(".", "_") + ".csv";
        }

        System.out.println("Something went wrong, no Version found");
        throw new RuntimeException("Something went wrong, no Version found");
    }



    private ResponseInputStream<GetObjectResponse> getLatestObject(final String bucketName, final String fileName) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        return s3Client.getObject(getObjectRequest);
    }
}
