package com.countries.controllers;

import com.countries.dtos.GetCountriesRequest;
import com.countries.services.CountriesService;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@AllArgsConstructor
@RestController
public class CountriesController {

    private final CountriesService countriesService;

    @GetMapping("/countries")
    public ArrayNode getCountries(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "population", required = false) String population,
            @RequestParam(value = "name_sort", required = false) String nameSort,
            @RequestParam(value = "offset", required = false) String offset,
            @RequestParam(value = "limit", required = false) String limit
    ) {

        ArrayNode response = null;

        try {
            val request = GetCountriesRequest.builder()
                    .name(name)
                    .population(population)
                    .nameSort(nameSort)
                    .offset(offset)
                    .limit(limit)
                    .build();

            response = countriesService.getCountries(request);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response;
    }
}
