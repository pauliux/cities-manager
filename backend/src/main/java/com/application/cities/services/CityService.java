package com.application.cities.services;

import com.application.cities.entities.City;
import com.application.cities.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository repository;

    public Page<City> findByName(String name, Pageable pageable) {
        return repository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<City> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
