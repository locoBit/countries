package com.countries.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CountriesControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void getAllCountries() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                        .get("/countries")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countries").exists())
                .andExpect(jsonPath("$.countries[*]").isNotEmpty());
    }

    @Test
    void getCountriesFilteredByName() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                        .get("/countries?name=ico")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countries").exists())
                .andExpect(jsonPath("$.countries[*]").isNotEmpty())
                .andExpect(jsonPath("$.countries[*].name", containsInAnyOrder("Mexico", "Puerto Rico", "Turks and Caicos Islands")));
    }

    @Test
    void getCountriesFilteredByNameOrderedByNameAsc() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                        .get("/countries?name=ico&name_sort=ascend")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countries").exists())
                .andExpect(jsonPath("$.countries[*]").isNotEmpty())
                .andExpect(jsonPath("$.countries[0].name", containsString("Mexico")))
                .andExpect(jsonPath("$.countries[1].name", containsString("Puerto Rico")))
                .andExpect(jsonPath("$.countries[2].name", containsString("Turks and Caicos Islands")));
    }

    @Test
    void getCountriesFilteredByNameOrderedByNameDesc() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                        .get("/countries?name=ico&name_sort=descend")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countries").exists())
                .andExpect(jsonPath("$.countries[*]").isNotEmpty())
                .andExpect(jsonPath("$.countries[2].name", containsString("Mexico")))
                .andExpect(jsonPath("$.countries[1].name", containsString("Puerto Rico")))
                .andExpect(jsonPath("$.countries[0].name", containsString("Turks and Caicos Islands")));
    }

    @Test
    void getCountriesFilteredByPopulationLowerThan1Million() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                        .get("/countries?population=1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countries").exists())
                .andExpect(jsonPath("$.countries[*]").isNotEmpty())
                .andExpect(jsonPath("$.countries[?(@.population > 1000000)]").isEmpty());
    }

    @Test
    void getCountriesFilteredByPopulationLowerThan10Million() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                        .get("/countries?population=10")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countries").exists())
                .andExpect(jsonPath("$.countries[*]").isNotEmpty())
                .andExpect(jsonPath("$.countries[?(@.population > 10000000)]").isEmpty());
    }

    @Test
    void getCountriesWithLimit1() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                        .get("/countries?offset=0&limit=1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countries").exists())
                .andExpect(jsonPath("$.countries[*]").isNotEmpty())
                .andExpect(jsonPath("$.countries.length()").value(1));
    }

    @Test
    void getCountriesWithLimit30() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                        .get("/countries?offset=0&limit=30")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countries").exists())
                .andExpect(jsonPath("$.countries[*]").isNotEmpty())
                .andExpect(jsonPath("$.countries.length()").value(30));
    }

    @Test
    void getCountriesPaginatedPage0() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                        .get("/countries?name=ico&offset=0&limit=1&name_sort=ascend")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countries").exists())
                .andExpect(jsonPath("$.countries[*]").isNotEmpty())
                .andExpect(jsonPath("$.countries[0].name", containsString("Mexico")))
                .andExpect(jsonPath("$.countries.length()").value(1));
    }

    @Test
    void getCountriesPaginatedPage1() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                        .get("/countries?name=ico&offset=1&limit=1&name_sort=ascend")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countries").exists())
                .andExpect(jsonPath("$.countries[*]").isNotEmpty())
                .andExpect(jsonPath("$.countries[0].name", containsString("Puerto Rico")))
                .andExpect(jsonPath("$.countries.length()").value(1));
    }

    @Test
    void getCountriesPaginatedPage2() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                        .get("/countries?name=ico&offset=2&limit=1&name_sort=ascend")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countries").exists())
                .andExpect(jsonPath("$.countries[*]").isNotEmpty())
                .andExpect(jsonPath("$.countries[0].name", containsString("Turks and Caicos Islands")))
                .andExpect(jsonPath("$.countries.length()").value(1));
    }
}
