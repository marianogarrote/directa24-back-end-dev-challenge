package com.directa24.main.challenge.infrastructure.config;

import com.directa24.main.challenge.application.usecase.SearchDirectorUseCase;
import com.directa24.main.challenge.domain.repository.DirectorStatisticsRepository;
import com.directa24.main.challenge.domain.service.MovieProvider;
import com.directa24.main.challenge.domain.service.MovieService;
import com.directa24.main.challenge.domain.service.MovieServiceImpl;
import com.directa24.main.challenge.domain.service.MovieStatusTracker;
import com.directa24.main.challenge.infrastructure.web.external.ExternalMovieProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {
    @Value("${external.movies.url}")
    private String url;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public MovieProvider movieProvider() {
        return new ExternalMovieProvider(webClientBuilder(), url);
    }

    @Bean
    public MovieService movieService(DirectorStatisticsRepository repository,  MovieStatusTracker statusTracker) {
        return new MovieServiceImpl(repository, statusTracker, movieProvider());
    }


    @Bean
    public SearchDirectorUseCase searchDirectorUseCase(MovieService movieService) {
        return new SearchDirectorUseCase(movieService);
    }
}
