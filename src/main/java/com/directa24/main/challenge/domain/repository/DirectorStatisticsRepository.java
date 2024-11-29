package com.directa24.main.challenge.domain.repository;

import com.directa24.main.challenge.application.dto.DirectorStatisticDto;
import java.util.List;

public interface DirectorStatisticsRepository {
    DirectorStatisticDto add(String directorName);
    List<String> getDirectorNamesByMovieCount(int movieCount);
}
