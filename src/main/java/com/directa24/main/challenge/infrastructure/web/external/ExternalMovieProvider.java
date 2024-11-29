package com.directa24.main.challenge.infrastructure.web.external;

import com.directa24.main.challenge.application.dto.MovieDto;
import com.directa24.main.challenge.application.dto.MoviePageDto;
import com.directa24.main.challenge.application.dto.PagingStatusDto;
import com.directa24.main.challenge.domain.service.MovieProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@AllArgsConstructor
public class ExternalMovieProvider implements MovieProvider {
    private static final Logger logger = LoggerFactory.getLogger(ExternalMovieProvider.class);

    private final WebClient.Builder clientBuilder;
    private final String url;

    private Flux<MovieDto> fetchMovies(int currentPage, boolean fetchAll) {
        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(10));
        return clientBuilder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build()
                .get()
                .uri(url+"?page={currentPage}", currentPage)
                .retrieve()
                .bodyToMono(byte[].class)
                .flatMapMany(byteArray -> {
                    try {
                        String jsonString = new String(byteArray, StandardCharsets.UTF_8);
                        MoviePageDto moviePage = new ObjectMapper().readValue(jsonString, MoviePageDto.class);
                        Flux<MovieDto> movieFlux = Flux.fromIterable(moviePage.getData());
                        if(!fetchAll) {
                            return movieFlux;
                        }

                        if (moviePage.hasMorePages()) {
                            return movieFlux.concatWith(fetchMovies(currentPage + 1, true));
                        }
                        return movieFlux;

                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public Flux<MovieDto> fallbackMethod(int currentPage, Exception ex) {
        logger.warn(
                "Fallback method was called for page {} given the following error was thrown: {}",
                currentPage,
                ex.getMessage()
        );
        return null;
    }

    @Retry(name = "movieRetry", fallbackMethod = "fallbackMethod")
    @Override
    public Flux<MovieDto> getAllMovies() {
        return fetchMovies(1, true);
    }

    @Retry(name = "movieRetry", fallbackMethod = "fallbackMethod")
    @Override
    public Flux<MovieDto> getMoviesForPage(int currentPage) {
        return fetchMovies(currentPage, false);
    }

    @Retry(name = "movieRetry", fallbackMethod = "fallbackMethod")
    public Mono<PagingStatusDto> getCurrentTotalItemCount() {
        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(10));
        return clientBuilder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build()
                .get()
                .uri(url+"?page=1")
                .retrieve()
                .bodyToMono(byte[].class)
                .flatMap(byteArray -> {
                    try {
                        String jsonString = new String(byteArray, StandardCharsets.UTF_8);
                        MoviePageDto moviePage = new ObjectMapper().readValue(jsonString, MoviePageDto.class);
                        return Mono.just(
                                new PagingStatusDto(
                                        moviePage.getTotalPages(),
                                        moviePage.getTotalItems(),
                                        moviePage.getPerPage()
                                )
                        );
                    } catch (JsonProcessingException e) {
                        return Mono.error(new RuntimeException(e));
                    }
                });
    }
}