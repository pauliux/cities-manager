package com.application.cities.controllers;

import com.application.cities.entities.City;
import com.application.cities.exceptions.ErrorHandlingAdvice;
import com.application.cities.services.city.CityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CityControllerIntegrationTest {

    MockMvc mvc;

    @InjectMocks
    private CityController cityController;

    @Mock
    private CityService service;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final City DEFAULT_CITY1 = new City("Test one", "https://test1.com");
    private static final City DEFAULT_CITY2 = new City("Test two", "https://test2.com");
    private static final List<City> DEFAULT_CITIES = List.of(DEFAULT_CITY1, DEFAULT_CITY2);

    private static final City VALID_CITY = new City(1L, "Valid city", "https://valid.com");
    private static final City INVALID_CITY = new City(2L, "Invalid city", "https://invalid.com");

    @BeforeAll
    public void setup() {
        this.mvc = MockMvcBuilders.standaloneSetup(cityController)
                .setControllerAdvice(new ErrorHandlingAdvice())
                .build();

        Pageable pageable = PageRequest.of(0, 20);
        when(service.findAll(pageable)).thenReturn(new PageImpl<>(DEFAULT_CITIES));
        when(service.findByName("one", pageable)).thenReturn(new PageImpl<>(List.of(DEFAULT_CITY1)));
        when(service.updateCity(eq(VALID_CITY), eq(VALID_CITY.getId()))).thenReturn(VALID_CITY);
        when(service.updateCity(eq(INVALID_CITY), eq(INVALID_CITY.getId()))).thenThrow(new IllegalArgumentException("Test exception"));
    }

    @Test
    void getAllCities() throws Exception {
        mvc.perform(get("/api/v1/cities")
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.items", hasSize(2)))
                .andExpect(jsonPath("$.data.items_per_page", equalTo(20)))
                .andExpect(jsonPath("$.data.page_index", equalTo(1)))
                .andExpect(jsonPath("$.data.total_items", equalTo(2)));
    }

    @Test
    void getCitiesByName() throws Exception {
        mvc.perform(get("/api/v1/cities/one")
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.items", hasSize(1)))
                .andExpect(jsonPath("$.data.items_per_page", equalTo(20)))
                .andExpect(jsonPath("$.data.page_index", equalTo(1)))
                .andExpect(jsonPath("$.data.total_items", equalTo(1)));
    }

    @Test
    void whenUpdatingCityWithValidDataReturnsUpdatedCity() throws Exception {
        mvc.perform(put("/api/v1/cities/" + VALID_CITY.getId())
                        .content(MAPPER.writeValueAsString(VALID_CITY))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.id", equalTo(VALID_CITY.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(VALID_CITY.getName())))
                .andExpect(jsonPath("$.photo", equalTo(VALID_CITY.getPhoto())));
    }

    @Test
    void whenUpdatingCityWithInvalidThenReturnsError() throws Exception {
        mvc.perform(put("/api/v1/cities/" + INVALID_CITY.getId())
                        .content(MAPPER.writeValueAsString(INVALID_CITY))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", equalTo("Test exception")))
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.name").doesNotExist())
                .andExpect(jsonPath("$.photo").doesNotExist());
    }
}