package com.application.cities.services;

import com.application.cities.entities.City;
import com.application.cities.repositories.CityRepository;
import com.application.cities.services.city.CityService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CityServiceIntegrationTest {
    @Autowired
    private CityRepository cityRepository;

    private CityService service;

    @BeforeAll
    public void setup() {
        service = new CityService(cityRepository);
    }

    @AfterEach
    public void clearDB() {
        cityRepository.deleteAll();
    }

    private void fillDBWithDumpData() {
        var city1 = new City("Test one", "https://test1.com");
        var city11 = new City("Test One", "https://test1-1.com");
        var city2 = new City("Test two", "https://test2.com");
        var city22 = new City("Test Two", "https://test2-2.com");
        var city3 = new City("Test three", "https://test3.com");

        cityRepository.save(city1);
        cityRepository.save(city11);
        cityRepository.save(city2);
        cityRepository.save(city22);
        cityRepository.save(city3);
    }

    private Page<City> findCitiesByName(String name) {
        Pageable pageable = PageRequest.of(0, 10);
        return service.findByName(name, pageable);
    }

    private Page<City> findAllCities() {
        Pageable pageable = PageRequest.of(0, 10);
        return service.findAll(pageable);
    }

    @Test
    void returnsNoCitiesWhenNoData() {
        var cities = findAllCities();
        assertThat(cities.getTotalElements()).isEqualTo(0);
    }

    @Test
    void returnsNoCitiesByNameWhenNoData() {
        var cities = findCitiesByName("one");
        assertThat(cities.getTotalElements()).isEqualTo(0);
    }

    @Test
    void returnsAllCities() {
        fillDBWithDumpData();

        var cities = findAllCities();
        assertThat(cities.getTotalElements()).isEqualTo(5);
    }


    @Test
    void returnsCitiesByNames() {
        fillDBWithDumpData();

        var citiesSearchedLowercaseName = findCitiesByName("one");
        var citiesSearchedUppercaseName = findCitiesByName("One");
        var citiesSearchedByFullName = findCitiesByName("Test three");
        var citiesSearchedByImage = findCitiesByName("test1");
        var citiesSearchedByNotExistingName = findCitiesByName("four");

        assertAll(() -> {
            assertThat(citiesSearchedLowercaseName.getTotalElements()).isEqualTo(2);
            assertThat(citiesSearchedUppercaseName.getTotalElements()).isEqualTo(2);
            assertThat(citiesSearchedByFullName.getTotalElements()).isEqualTo(1);
            assertThat(citiesSearchedByImage.getTotalElements()).isEqualTo(0);
            assertThat(citiesSearchedByNotExistingName.getTotalElements()).isEqualTo(0);
        });
    }

    @Test
    void WhenUpdatingCityWithBlankNameThenThrowsException() {
        var city = new City(1L, " ", "https://test1.com");
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> service.updateCity(city, 1L));
        assertEquals("City name cannot be empty", exception.getMessage());
    }

    @Test
    void WhenUpdatingCityWithInvalidPhotoThenThrowsException() {
        var city = new City(1L, "Test", "test1");
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> service.updateCity(city, 1L));
        assertEquals("Invalid photo URL", exception.getMessage());
    }

    @Test
    void WhenUpdatingCityWithNotMatchingIDThenThrowsException() {
        var city = new City(1L, "Test", "https://test1.com");
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> service.updateCity(city, 2L));
        assertEquals("IDs of the city in body and path do not match", exception.getMessage());
    }

    @Test
    void WhenUpdatingNotExistingCityThenThrowsException() {
        var city = new City(100L, "Test", "https://test1.com");
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> service.updateCity(city, 100L));
        assertEquals("City does not exist", exception.getMessage());
    }

    private void validateCityFromDB(City city) {
        var cityFromDB = cityRepository.findById(city.getId());
        assertThat(cityFromDB).isPresent();
        assertAll((() -> {
            assertThat(cityFromDB.get().getName()).isEqualTo(city.getName());
            assertThat(cityFromDB.get().getPhoto()).isEqualTo(city.getPhoto());
        }));
    }

    @Test
    void WhenUpdatingCityWithValidDataThenUpdatesCity() {
        var savedCity = cityRepository.save(new City("Test one", "https://test1.com"));
        City updatedCity = new City(savedCity.getId(), "Updated Test", "https://updated-test.com");

        service.updateCity(updatedCity, savedCity.getId());
        validateCityFromDB(updatedCity);
    }
}