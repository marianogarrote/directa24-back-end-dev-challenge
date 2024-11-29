package com.directa24.main.challenge.infrastructure.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "Director Provider API")
public interface DirectorApi {

    @Operation(summary = "A list of alphabetically sorted director names whose number of movies directed is strictly greater than the given threshold.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Alphabetically sorted director names whose number of movies directed is strictly greater than the given threshold.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = List.class,
                                            example = "[\n" +
                                                    "  \"Martin Scorsese\",\n" +
                                                    "  \"Woody Allen\"\n" +
                                                    "]"
                                    )
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "The request was not correctly formed (missing required parameters, wrong types...)",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ProblemDetail.class,
                                            example = "{\n" +
                                                    "  \"type\": \"/api/directors\",\n" +
                                                    "  \"title\": \"Bad Request\",\n" +
                                                    "  \"status\": 400,\n" +
                                                    "  \"detail\": \"Data Error: Threshold must be not null and greater than or equal to 0\",\n" +
                                                    "  \"instance\": \"/api/directors\"\n" +
                                                    "}"
                                    )
                            )
                    }
            )
    })
    Mono<List<String>> getDirectors(
            @Parameter(
                    description = "threshold: integer denoting the threshold value for the number movies a person has directed",
                    required = true
            )
            Integer threshold
    );
}