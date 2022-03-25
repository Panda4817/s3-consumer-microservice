package dev.kmunton.mappings.consumer.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.io.IOException;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Tag(name = "Mappings Consumer")
@Validated
public interface ConsumerContract {

    @Operation(summary = "Retrieve a marvel hero based on attributes provided")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The request completed successfully"),
            @ApiResponse(responseCode = "400", description = "The request was malformed"),
            @ApiResponse(responseCode = "500", description = "Marvel hero could not be retrieved due to an unknown error")})
    ResponseEntity<String> getMarvelHero(@Parameter(name = "attribute1", in= QUERY, required = true, example = "red") @NotNull String attribute1,
                                         @Parameter(name = "attribute2", in= QUERY, required = false) String attribute2,
                                         @Parameter(name = "attribute3", in= QUERY, required = false ) String attribute3);

    @Operation(summary = "Refresh Mappings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The request completed successfully"),
            @ApiResponse(responseCode = "400", description = "The request was malformed"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
    ResponseEntity<Void> getNewMappings(@Parameter(name = "key", in= QUERY, required = true)
                                        @Pattern(regexp = "^([a-zA-Z_]+)_v([0-9])_([0-9])\\.csv$") String key) throws IOException;
}

