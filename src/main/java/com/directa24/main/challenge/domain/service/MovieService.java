package com.directa24.main.challenge.domain.service;

import reactor.core.publisher.Mono;

import java.util.List;

public interface MovieService {
    void update();
    Mono<List<String>> getDirectors(Integer threshold);
}
