package com.directa24.main.challenge.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@Getter
@NoArgsConstructor
@ToString
public class MoviePageDto {
    @JsonProperty("page")
    private int currentPage; //page: The current page of the results
    @JsonProperty("per_page")
    private int perPage; //per_page: The maximum number of movies returned per page.
    @JsonProperty("total")
    private int totalItems; // total: The total number of movies on all pages of the result.
    @JsonProperty("total_pages")
    private int totalPages; // total_pages: The total number of pages with results.
    private List<MovieDto> data; // data: An array of objects containing movies returned on the requested page

    public boolean hasMorePages() {
        return currentPage < totalPages;
    }
}
