package com.directa24.main.challenge.infrastructure.storage.db;

import com.directa24.main.challenge.domain.repository.DirectorStatisticsRepository;
import com.directa24.main.challenge.infrastructure.storage.db.entity.DirectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaDirectorStatisticsRepository extends DirectorStatisticsRepository, JpaRepository<DirectorEntity, Long> {
    Optional<DirectorEntity> findByDirectorName(String directorName);

    /**
     * See Also
     * <a href="https://www.baeldung.com/spring-data-jpa-modifying-annotation">Spring Data JPA @Modifying Annotation</a>
     */
    @Modifying(
            clearAutomatically = true, // The persistence context is cleared after query execution
            flushAutomatically = true // The EntityManager is flushed before our query is executed
    )
    @Query("update DirectorEntity director set director.count = director.count + 1 where director.directorName = :directorName")
    void updateCount(@Param("directorName") String directorName);
}