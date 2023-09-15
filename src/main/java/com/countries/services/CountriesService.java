package com.countries.services;

import com.countries.dtos.GetCountriesRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.val;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class CountriesService {

    private final ObjectMapper mapper = new ObjectMapper();

    public ArrayNode getCountries(GetCountriesRequest getCountriesRequest) throws IOException, InterruptedException {

        val response = this.makeCountriesExternalRequest();
        val countries = this.parseCountriesResponse(response);
        ArrayNode result = countries;

        if (getCountriesRequest.getName() != null) {
            result = this.filterCountriesByName(countries, getCountriesRequest.getName());
        }

        if (getCountriesRequest.getPopulation() != null) {
            result = this.filterCountriesByPopulation(result, getCountriesRequest.getIntPopulation());
        }

        if (getCountriesRequest.getNameSort() != null) {
            result = this.sortCountriesByName(result, getCountriesRequest.getNameSort());
        }

        if (getCountriesRequest.getOffset() != null && getCountriesRequest.getLimit() != null) {
            result = this.paginateCountries(result, Integer.parseInt(getCountriesRequest.getOffset()), Integer.parseInt(getCountriesRequest.getLimit()));
        }

        return result;
    }

    public String makeCountriesExternalRequest() throws IOException, InterruptedException {
        val client = HttpClient.newHttpClient();
        val countriesUrl = "https://restcountries.com/v3.1/all?fields=name,population,flag";
        val request = HttpRequest.newBuilder()
                .uri(URI.create(countriesUrl))
                .build();

        val response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public ArrayNode parseCountriesResponse(String response) throws IOException {
        val arrayNode = (ArrayNode) mapper.readTree(response);
        val countries = new ArrayNode(mapper.getNodeFactory());

        arrayNode.forEach(c -> {
            val country = new ObjectNode(mapper.getNodeFactory());
            country.set("name", c.get("name").get("common"));
            country.set("population", c.get("population"));
            country.set("flag", c.get("flag"));
            
            countries.add(country);
        });

        return countries;
    }

    public ArrayNode filterCountriesByName(ArrayNode countries, String name) {
        val result = new ArrayNode(mapper.getNodeFactory());

        getStreamFromIterator(countries.iterator())
                .filter(country -> country.get("name").asText().contains(name))
                .forEach(result::add);

        return result;
    }

    public ArrayNode filterCountriesByPopulation(ArrayNode countries, int population) {
        val result = new ArrayNode(mapper.getNodeFactory());

        getStreamFromIterator(countries.iterator())
                .filter(country -> {
                    int countryPopulation = country.get("population").asInt();

                    return countryPopulation <= population;
                })
                .forEach(result::add);

        return result;
    }

    public ArrayNode sortCountriesByName(ArrayNode countries, String nameSort) {
        val result = new ArrayNode(mapper.getNodeFactory());

        getStreamFromIterator(countries.iterator())
                .sorted((country1, country2) -> {
                    String countryName1 = country1.get("name").asText();
                    String countryName2 = country2.get("name").asText();

                    if (nameSort.equals("ascend")) {
                        return countryName1.compareTo(countryName2);
                    } else {
                        return countryName2.compareTo(countryName1);
                    }
                })
                .forEach(result::add);

        return result;
    }

    public ArrayNode paginateCountries(ArrayNode countries, int offset, int limit) {
        val result = new ArrayNode(mapper.getNodeFactory());

        getStreamFromIterator(countries.iterator())
                .skip(offset)
                .limit(limit)
                .forEach(result::add);

        return result;
    }

    public static <T> Stream<T>
    getStreamFromIterator(Iterator<T> iterator)
    {
        Spliterator<T>
                spliterator = Spliterators
                .spliteratorUnknownSize(iterator, 0);

        return StreamSupport.stream(spliterator, false);
    }
}
