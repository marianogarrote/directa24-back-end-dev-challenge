package com.directa24.main.challenge.infrastructure.web.controller;

import com.directa24.main.challenge.application.usecase.SearchDirectorUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

@WebFluxTest(controllers = DirectorController.class)
public class DirectorControllerTest {
    @Autowired
    private WebTestClient webClient;

    @MockBean
    private SearchDirectorUseCase searchDirectorUseCase;

    @Captor
    private ArgumentCaptor<Integer> thresholdCaptor;

    @Test
    public void test() {
        List<String> directors = List.of("Martin Scorsese","Woody Allen");
        int threshold = 4;
        when(searchDirectorUseCase.getDirectors(threshold)).thenReturn(Mono.just(directors));

        List<String> response = webClient.get().uri(uriBuilder ->
                        uriBuilder
                                .path("/api/directors")
                                .queryParam("threshold", threshold)
                                .build())
                .header(HttpHeaders.ACCEPT, "application/json")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<String>>() {})
                        .returnResult()
                .getResponseBody();

        verify(searchDirectorUseCase, times(1)).getDirectors(thresholdCaptor.capture());
        Assertions.assertThat(response).hasSize(directors.size());
        Assertions.assertThat(response).containsAll(directors);
    }
}
