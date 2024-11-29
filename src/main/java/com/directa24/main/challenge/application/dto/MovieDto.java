package com.directa24.main.challenge.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class MovieDto {
    @JsonProperty("Title")
    public String title;
    @JsonProperty("Year")
    public String year;
    @JsonProperty("Rated")
    public String rated;
    @JsonProperty("Released")
    public String released;
    @JsonProperty("Runtime")
    public String runtime;
    @JsonProperty("Genre")
    public String genre;
    @JsonProperty("Director")
    public String director;
    @JsonProperty("Writer")
    public String writer;
    @JsonProperty("Actors")
    public String actors;
}
