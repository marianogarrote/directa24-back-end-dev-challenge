package com.directa24.main.challenge.application.usecase;

import com.directa24.main.challenge.domain.service.MovieService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
public class SearchDirectorUseCase {
    private final MovieService movieService;
    public Mono<List<String>> getDirectors(Integer threshold) {
        return movieService.getDirectors(threshold);
    }
}
