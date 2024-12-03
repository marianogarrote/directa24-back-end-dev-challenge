package com.directa24.main.challenge.infrastructure.storage.db.entity;

import com.directa24.main.challenge.application.dto.PagingStatusDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Table(name = "paging_status")
@Getter
@AllArgsConstructor

public class PagingStatusEntity {
    private static final long UNIQUE_INSTANCE_ID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int pageCount;
    private int itemCounts;
    private int pageSize;

    protected PagingStatusEntity() {

    }

    public PagingStatusDto toDto() {
        return new PagingStatusDto(
                pageCount,
                itemCounts,
                pageSize
        );
    }

    public static PagingStatusEntity fromDto(PagingStatusDto dto) {
        return new PagingStatusEntity(
                UNIQUE_INSTANCE_ID,
                dto.getPageSize(),
                dto.getItemCounts(),
                dto.getPageSize()
        );
    }

    public void update(PagingStatusDto dto) {
        this.pageCount = dto.getPageCount();
        this.itemCounts = dto.getItemCounts();
        this.pageSize = dto.getPageSize();
    }
}
