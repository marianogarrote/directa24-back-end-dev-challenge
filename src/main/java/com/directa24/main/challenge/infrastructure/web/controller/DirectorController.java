package com.directa24.main.challenge.infrastructure.web.controller;

import com.directa24.main.challenge.application.usecase.SearchDirectorUseCase;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/directors")
@AllArgsConstructor
public class DirectorController implements DirectorApi {

    private SearchDirectorUseCase searchDirectorUseCase;

    @GetMapping
    @Cacheable(value = "DirectorsByCountCache", key="{#threshold}", condition = "#threshold != null && #threshold > 0" )
    public Mono<List<String>> getDirectors(Integer threshold) {
        return searchDirectorUseCase.getDirectors(threshold);
    }
}