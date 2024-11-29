package com.directa24.main.challenge.domain.service;

import com.directa24.main.challenge.application.dto.PagingStatusDto;

public interface MovieStatusTracker {
    PagingStatusDto find();
    void update(PagingStatusDto newStatus);
}
