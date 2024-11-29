package com.directa24.main.challenge.infrastructure.storage.inmemory;

import com.directa24.main.challenge.application.dto.DirectorStatisticDto;
import com.directa24.main.challenge.domain.repository.DirectorStatisticsRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryDirectoryStatisticsRepository implements DirectorStatisticsRepository {
    private final Map<String, Integer> cache = new ConcurrentHashMap<>();

    @Override
    public DirectorStatisticDto add(String directorName) {
        int currentCount = cache.getOrDefault(directorName, 0);
        cache.put(directorName, ++currentCount);
        return new DirectorStatisticDto(directorName, currentCount);
    }

    @Override
    public List<String> getDirectorNamesByMovieCount(int movieCount) {
        return cache.entrySet().stream()
                .filter(x -> x.getValue() > movieCount)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());
    }
}
