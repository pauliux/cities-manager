package com.application.cities.controllers;

import com.application.cities.entities.City;
import com.application.cities.services.CityService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    private static final City DEFAULT_CITY1 = new City("Test one", "https://test1.com");
    private static final City DEFAULT_CITY2 = new City("Test two", "https://test2.com");
    private static final List<City> DEFAULT_CITIES = List.of(DEFAULT_CITY1, DEFAULT_CITY2);

    @BeforeAll
    public void setup() {
        this.mvc = MockMvcBuilders.standaloneSetup(cityController).build();
        Pageable pageable = PageRequest.of(0, 20);
        when(service.findAll(pageable)).thenReturn(new PageImpl<>(DEFAULT_CITIES));
        when(service.findByName("one", pageable)).thenReturn(new PageImpl<>(List.of(DEFAULT_CITY1)));
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
}