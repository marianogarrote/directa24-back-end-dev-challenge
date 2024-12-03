package com.directa24.main.challenge.infrastructure.storage.db;

import com.directa24.main.challenge.application.dto.DirectorStatisticDto;
import com.directa24.main.challenge.domain.repository.DirectorStatisticsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
@AllArgsConstructor
public class DbDirectorStatisticsRepository implements DirectorStatisticsRepository {

    private final JpaDirectorStatisticsRepository jpaRepository;


    @Override
    public DirectorStatisticDto add(String directorName) {
        return null;
    }

    @Override
    public List<String> getDirectorNamesByMovieCount(int movieCount) {
        return List.of();
    }
}
