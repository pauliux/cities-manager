package com.application.cities.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Getter
@NoArgsConstructor
public final class City {
    @Id
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String photo;
}