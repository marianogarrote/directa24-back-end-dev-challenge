package com.directa24.main.challenge.domain.service;


import com.directa24.main.challenge.application.dto.MovieDto;
import com.directa24.main.challenge.application.dto.MoviePageDto;
import com.directa24.main.challenge.application.dto.PagingStatusDto;
import com.directa24.main.challenge.domain.exception.DomainException;
import com.directa24.main.challenge.domain.repository.DirectorStatisticsRepository;
import com.directa24.main.challenge.infrastructure.storage.inmemory.InMemoryDirectoryStatisticsRepository;
import com.directa24.main.challenge.infrastructure.scheduling.InMemoryMovieStatusTracker;
import com.directa24.main.challenge.utils.JsonReader;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.IntStream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {
    private static final String RESPONSE_NAME_PATTERN = "json/page0%s.json";
    private static final String UPDATE_RESPONSE_NAME_PATTERN = "json/updatePage0%s.json";
    private static final Map<String, Integer> directorsByMovieCount = Map.of(
            "Woody Allen", 8,
            "Pedro Almodóvar", 4,
            "Juan José Campanella", 1,
            "Martin Scorsese",4,
            "M. Night Shyamalan", 4,
            "Quentin Tarantino", 2,
            "Clint Eastwood",3
    ) ;

    private static final int pageCount = 3;
    private static final int itemCount = 26;
    private static final int pageSize = 10 ;

    private static final int newItemCount = itemCount + 3;


    @Mock(strictness = Mock.Strictness.LENIENT)
    private MovieProvider movieProvider;

    private MovieStatusTracker statusTracker;
    private DirectorStatisticsRepository repository;
    private MovieService service;

    @Captor
    ArgumentCaptor<PagingStatusDto> pageCaptor;

    @BeforeEach
    public void setup() {
        statusTracker = Mockito.spy(new InMemoryMovieStatusTracker());
        repository = Mockito.spy(new InMemoryDirectoryStatisticsRepository());
        service = new MovieServiceImpl(repository, statusTracker, movieProvider);
    }

    @Test
    public void updateTest() {
        // Given the provider result
        PagingStatusDto currentStatus = new PagingStatusDto(pageCount, itemCount, pageSize);
        Mono<PagingStatusDto> pagingStatus = Mono.just(currentStatus);
        Mockito.when(movieProvider.getCurrentTotalItemCount()).thenReturn(pagingStatus);

        Flux<MovieDto> firstPage = getPageByNumber(1, false);
        Mockito.when(movieProvider.getMoviesForPage(1)).thenReturn(firstPage);
        Mockito.when(movieProvider.getMoviesForPage(2)).thenReturn(getPageByNumber(2, false));
        Mockito.when(movieProvider.getMoviesForPage(3)).thenReturn(getPageByNumber(3, false));

        // Then (first update)
        service.update();
        verify(statusTracker, times(1)).update(pageCaptor.capture());
        verify(repository, times(currentStatus.getItemCounts())).add(Mockito.anyString());

        // Assertions
        assertThat(currentStatus).isEqualTo(pageCaptor.getValue());

        countByDirectors().forEach(this::assertDirectorsByCount);


        // ================     Check status once request items were updated
        PagingStatusDto newStatus = new PagingStatusDto(pageCount, newItemCount, pageSize);
        Mono<PagingStatusDto> newPagingStatus = Mono.just(newStatus);
        Mockito.when(movieProvider.getCurrentTotalItemCount()).thenReturn(newPagingStatus);

        Flux<MovieDto> firstPageForUpdate = getPageByNumber(1, true);
        Mockito.when(movieProvider.getMoviesForPage(1)).thenReturn(firstPageForUpdate);
        Mockito.when(movieProvider.getMoviesForPage(2)).thenReturn(getPageByNumber(2, true));
        Mockito.when(movieProvider.getMoviesForPage(3)).thenReturn(getPageByNumber(3, true));

        // Then (after second Update)
        service.update();
        verify(statusTracker, times(2)).update(pageCaptor.capture());
        verify(repository, times(newStatus.getItemCounts())).add(Mockito.anyString());

        // Assertions
        assertThat(newStatus).isEqualTo(pageCaptor.getValue());
    }


    private void assertDirectorsByCount(int count, List<String> directors) {
        assertThat(directors)
                .containsExactlyInAnyOrderElementsOf(repository.getDirectorNamesByMovieCount(count));
    }

    @Test
    public void invalidInput() {
        Assertions.assertThatThrownBy(() -> service.getDirectors(null).subscribe())
                .isInstanceOf(DomainException.class)
                .hasMessage("Threshold must be not null and greater than or equal to 0");

        Assertions.assertThatThrownBy(() -> service.getDirectors(-12).subscribe())
                .isInstanceOf(DomainException.class)
                .hasMessage("Threshold must be not null and greater than or equal to 0");



    }


    private Map<Integer, List<String>> countByDirectors() {
        Map<String, Integer> directorsByMovieCount = Map.of(
                "Woody Allen", 8,
                "Pedro Almodóvar", 4,
                "Juan José Campanella", 1,
                "Martin Scorsese",4,
                "M. Night Shyamalan", 4,
                "Quentin Tarantino", 2,
                "Clint Eastwood",3
        ) ;

        Map<Integer, List<String>> countByDirectors = new HashMap<>();
        int maxMovieCount = Collections.max(directorsByMovieCount.values());
        IntStream.range(1, maxMovieCount +1).forEach(index -> {
            countByDirectors.put(index, new ArrayList<>());
        });

        directorsByMovieCount.forEach((director, movies) -> {
            for (int key = movies - 1; key > 0 ; key--) {
                try {
                    countByDirectors.get(key).add(director);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
        });
        return countByDirectors;
    }

    private Flux<MovieDto> getPageByNumber(int number, boolean isUpdate) {
        String path = String.format(isUpdate? UPDATE_RESPONSE_NAME_PATTERN: RESPONSE_NAME_PATTERN, number);
        return Flux.fromIterable(JsonReader.readJson(path, MoviePageDto.class).getData());
    }
}
