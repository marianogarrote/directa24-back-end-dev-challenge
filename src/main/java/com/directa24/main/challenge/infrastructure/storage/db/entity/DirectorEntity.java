package com.directa24.main.challenge.infrastructure.storage.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Table(
        name = "directors",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "director_name_unique_constraint",
                        columnNames = "directorName"
                )
        },
        indexes = {
                @Index(name = "director_name_idx", columnList = "directorName")
        }
)
@AllArgsConstructor
@Getter
public class DirectorEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String directorName;
    private Long count = 0L;

    protected DirectorEntity() {

    }

    public void newOccurrency() {
        count++;
    }
}
