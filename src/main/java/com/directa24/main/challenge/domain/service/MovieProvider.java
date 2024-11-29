package com.directa24.main.challenge.domain.service;

import com.directa24.main.challenge.application.dto.MovieDto;
import com.directa24.main.challenge.application.dto.PagingStatusDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieProvider {
    Flux<MovieDto> getAllMovies();
    Flux<MovieDto> getMoviesForPage(int currentPage);
    Mono<PagingStatusDto> getCurrentTotalItemCount();
}
