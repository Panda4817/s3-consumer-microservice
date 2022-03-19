package dev.kmunton.mappings.consumer.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Tag(name = "Mappings Consumer")
public interface ConsumerContract {

    @Operation(summary = "Retrieve a marvel hero based on attributes provided")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The request completed successfully"),
            @ApiResponse(responseCode = "400", description = "The request was malformed"),
            @ApiResponse(responseCode = "500", description = "Marvel hero could not be retrieved due to an unknown error")})
    ResponseEntity<String> getMarvelHero(@Parameter(name = "attribute1", in= QUERY, required = true, example = "red") String attribute1,
                                         @Parameter(name = "attribute2", in= QUERY, required = false) String attribute2);

    @Operation(summary = "Refresh Mappings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The request completed successfully"),
            @ApiResponse(responseCode = "400", description = "The request was malformed"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
    ResponseEntity<Void> getNewMappings(@Parameter(name = "key", in= QUERY, required = true, example = "test.csv") String key) throws IOException;
}

