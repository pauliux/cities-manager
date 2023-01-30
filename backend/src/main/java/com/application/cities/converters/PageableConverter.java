package com.application.cities.converters;

import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

public class PageableConverter {
    public static <T> Map<String, Object> convert(@NonNull int pageNumber, @NonNull int size, @NonNull Page<T> page) {
        Map<String, Object> response = new HashMap<>();
        response.put("items", page.getContent());
        response.put("items_per_page", size);
        response.put("page_index", pageNumber);
        response.put("total_items", page.getTotalElements());
        return Map.of("data", response);
    }
}
