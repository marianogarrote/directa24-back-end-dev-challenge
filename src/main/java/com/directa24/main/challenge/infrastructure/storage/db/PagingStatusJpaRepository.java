package com.directa24.main.challenge.infrastructure.storage.db;

import com.directa24.main.challenge.application.dto.PagingStatusDto;
import com.directa24.main.challenge.infrastructure.storage.db.entity.PagingStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagingStatusJpaRepository extends JpaRepository<PagingStatusEntity, Long> {
    default Optional<PagingStatusEntity> find() {
        return findById(1L);
    }

    void update(PagingStatusEntity newStatus);
}
