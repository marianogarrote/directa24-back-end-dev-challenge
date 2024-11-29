package com.directa24.main.challenge.infrastructure.scheduling;

import com.directa24.main.challenge.application.dto.PagingStatusDto;
import com.directa24.main.challenge.domain.service.MovieStatusTracker;
import java.util.concurrent.atomic.AtomicReference;

public class InMemoryMovieStatusTracker implements MovieStatusTracker {
    private final AtomicReference<PagingStatusDto> status =
            new AtomicReference<>(new PagingStatusDto(0,0,0));

    @Override
    public PagingStatusDto find() {
        return status.get();
    }

    @Override
    public void update(PagingStatusDto newStatus) {
        status.compareAndSet(status.get(), newStatus);
    }
}
