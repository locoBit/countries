package com.countries.controllers;

import com.countries.dtos.GetCountriesRequest;
import com.countries.services.CountriesService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@AllArgsConstructor
@RestController
@Slf4j
public class CountriesController {

    @Autowired
    private CountriesService countriesService;

    @GetMapping("/countries")
    public JsonNode getCountries(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "population", required = false) String population,
            @RequestParam(value = "name_sort", required = false) String nameSort,
            @RequestParam(value = "offset", required = false) String offset,
            @RequestParam(value = "limit", required = false) String limit
    ) {

        ObjectNode response = null;

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
            log.error(e.getMessage());
        }

        return response;
    }
}
