package com.countries.dtos;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetCountriesRequest {

    private String name;
    private String population;
    private String nameSort;
    private String offset;
    private String limit;

    public int getIntPopulation() {
        return Integer.parseInt(population) * 1_000_000;
    }
}
