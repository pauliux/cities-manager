package com.application.cities.converters;

import com.application.cities.entities.City;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PageableConverterTest {

    @Test
    void throwsNullExceptionWhenPageIsNull() {
        assertThrows(NullPointerException.class,
                () -> PageableConverter.convert(2, 4, null));
    }

    @Test
    void WorksFineWhenPageIsEmpty() {
        var pageAsMap = PageableConverter.convert(2, 4, Page.empty());

        if (pageAsMap.get("data") instanceof Map map) {
            if (map.get("items") instanceof List items) {
                assertThat(items).hasSize(0);
            } else {
                Assertions.fail("Expected instance of List for items");
            }
            assertThat((int) map.get("items_per_page")).isEqualTo(4);
            assertThat((int) map.get("page_index")).isEqualTo(2);
            assertThat((long) map.get("total_items")).isEqualTo(0);
        } else {
            Assertions.fail("Expected instance of Map for converted page");
        }
    }

    @Test
    void WorksFineWhenPageIsNotEmpty() {
        var city1 = new City("Test one", "https://test1.com");
        var city2 = new City("Test two", "https://test2.com");

        var page = new PageImpl<>(List.of(city1, city2));
        var pageAsMap = PageableConverter.convert(2, 1, page);

        if (pageAsMap.get("data") instanceof Map map) {
            if (map.get("items") instanceof List items) {
                assertThat(items).hasSize(2);
            } else {
                Assertions.fail("Expected instance of List for items");
            }
            assertThat((int) map.get("items_per_page")).isEqualTo(1);
            assertThat((int) map.get("page_index")).isEqualTo(2);
            assertThat((long) map.get("total_items")).isEqualTo(2);
        } else {
            Assertions.fail("Expected instance of Map for converted page");
        }
    }
}