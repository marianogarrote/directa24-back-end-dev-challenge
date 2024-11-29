package com.directa24.main.challenge.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class PagingStatusDto {
    private int pageCount;
    private int itemCounts;
    private int pageSize;
}
