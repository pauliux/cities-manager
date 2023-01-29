package com.application.cities.controllers;

import com.application.cities.converters.PageableConverter;
import com.application.cities.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cities")
@RequiredArgsConstructor
public class CityController {
    private final CityService service;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCities(@RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "20", name = "per_page") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var cities = service.findAll(pageable);
        return ResponseEntity.ok().body(PageableConverter.convert(page, size, cities));
    }

    @GetMapping("{name}")
    public ResponseEntity<Map<String, Object>> getCities(@PathVariable String name,
                                                         @RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "20", name = "per_page") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var cities = service.findByName(name, pageable);
        return ResponseEntity.ok().body(PageableConverter.convert(page, size, cities));
    }
}
