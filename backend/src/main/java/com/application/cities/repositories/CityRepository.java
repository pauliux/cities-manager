package com.application.cities.repositories;

import com.application.cities.entities.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Page<City> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<City> findAll(Pageable pageable);
}