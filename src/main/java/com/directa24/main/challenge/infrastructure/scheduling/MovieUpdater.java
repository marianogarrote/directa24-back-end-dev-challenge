package com.directa24.main.challenge.infrastructure.scheduling;

import com.directa24.main.challenge.domain.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class MovieUpdater {
    private final MovieService movieService;

    @Async("threadPoolTaskExecutor")
    @Scheduled(fixedRateString = "${scheduler.movie.external.fix.in.minutes}", timeUnit = TimeUnit.MINUTES)
    public void updateMovies() {
        movieService.update();
    }
}