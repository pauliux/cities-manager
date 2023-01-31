package com.application.cities.services.city;

import com.application.cities.entities.City;
import com.application.cities.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class CityService implements CityRetriever, CityUpdater {
    private final CityRepository repository;

    public Page<City> findByName(String name, Pageable pageable) {
        return repository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<City> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    private boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    @Override
    public City updateCity(City city, long id) {
        if (city.getId() != id) {
            throw new IllegalArgumentException("IDs of the city in body and path do not match");
        } else if (!isValidUrl(city.getPhoto())) {
            throw new IllegalArgumentException("Invalid photo URL");
        } else if (city.getName() == null || city.getName().isBlank()) {
            throw new IllegalArgumentException("City name cannot be empty");
        } else if (repository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("City does not exist");
        }
        return repository.save(city);
    }
}
