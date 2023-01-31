package com.application.cities.services.city;

import com.application.cities.entities.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CityRetriever {
    Page<City> findByName(String name, Pageable pageable);

    Page<City> findAll(Pageable pageable);
}
