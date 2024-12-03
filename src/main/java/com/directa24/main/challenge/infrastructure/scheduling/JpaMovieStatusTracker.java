package com.directa24.main.challenge.infrastructure.scheduling;

import com.directa24.main.challenge.application.dto.PagingStatusDto;
import com.directa24.main.challenge.domain.service.MovieStatusTracker;
import com.directa24.main.challenge.infrastructure.storage.db.PagingStatusJpaRepository;
import com.directa24.main.challenge.infrastructure.storage.db.entity.PagingStatusEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JpaMovieStatusTracker implements MovieStatusTracker {
    private final PagingStatusJpaRepository repository;


    @Override
    public PagingStatusDto find() {
        return repository.find().map(PagingStatusEntity::toDto).orElse(null);
    }

    @Override
    @Transactional
    public void update(PagingStatusDto newStatus) {
        repository.find().ifPresentOrElse(
                old -> old.update(newStatus),
                () -> PagingStatusEntity.fromDto(newStatus)
        );
    }
}
