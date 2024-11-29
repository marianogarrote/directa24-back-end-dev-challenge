package com.directa24.main.challenge.domain.service;

import com.directa24.main.challenge.application.dto.MovieDto;
import com.directa24.main.challenge.application.dto.PagingStatusDto;
import com.directa24.main.challenge.domain.exception.DomainException;
import com.directa24.main.challenge.domain.repository.DirectorStatisticsRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MovieServiceImpl implements MovieService {
    private static final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);

    private final DirectorStatisticsRepository repository;
    private final MovieStatusTracker statusTracker;
    private final MovieProvider movieProvider;

    @Override
    public void update() {
        PagingStatusDto currentStatus = statusTracker.find();
        logger.info("Current status: {}", currentStatus);
        movieProvider.getCurrentTotalItemCount().subscribe(paging -> {
            if(currentStatus.getItemCounts() < paging.getItemCounts()) {
                logger.info("Updating status to {}", paging);
                int firstPageIndex = Math.max(currentStatus.getPageCount(), 1);
                for (int pageIndex = firstPageIndex; pageIndex <= paging.getPageCount(); pageIndex++) {
                    int itemsToBeSkipped = pageIndex == firstPageIndex? calculateItemsToBeSkipped(currentStatus, paging): 0;
                    addToRepo(movieProvider.getMoviesForPage(pageIndex), itemsToBeSkipped);
                }
                statusTracker.update(paging);
            }
        });
    }

    /**
     * Calculate the items that should be skipped because they have been already processed in a previous update
     * @return
     */
    private int calculateItemsToBeSkipped(PagingStatusDto previous, PagingStatusDto current) {
        if(previous.getItemCounts() != 0 && previous.getItemCounts() < current.getItemCounts()) {
            return current.getItemCounts() - previous.getItemCounts();
        }
        return 0;
    }


    private void addToRepo(Flux<MovieDto> flux, int itemsToBeSkipped) {
        flux.collectList().subscribe(items -> {
            List<MovieDto> toBeProcessed = itemsToBeSkipped == 0? items: items.subList(items.size() - itemsToBeSkipped, items.size());
            logger.info(
                    "Items = {} (skipped items = {}, to be processed = {}))",
                    items.size(),
                    itemsToBeSkipped,
                    toBeProcessed.size()
            );
            toBeProcessed.forEach(movie -> repository.add(movie.director));
        });
    }

    @Override
    public Mono<List<String>> getDirectors(Integer threshold) {
        if(threshold == null || threshold < 0) {
            throw new DomainException(
                    "INVALID_DATA:threshold",
                    "Threshold must be not null and greater than or equal to 0"
            );
        }
        return Mono.fromSupplier(() -> repository.getDirectorNamesByMovieCount(threshold)
                .stream()
                .sorted()
                .collect(Collectors.toList()));
    }

}
