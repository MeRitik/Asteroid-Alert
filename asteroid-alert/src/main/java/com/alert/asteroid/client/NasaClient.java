package com.alert.asteroid.client;

import com.alert.asteroid.dto.Asteroid;
import com.alert.asteroid.dto.NasaNeoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class NasaClient {

    @Value("${nasa.neo.api.url}")
    private String nasaApiUrl;
    @Value("${nasa.api.key}")
    private String nasaApiKey;

    public List<Asteroid> getNeoAsteroids(final LocalDate fromDate, final LocalDate toDate) {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final NasaNeoResponse nasaNeoResponse =
                restTemplate.getForObject(getUrl(fromDate, toDate), NasaNeoResponse.class);

        List<Asteroid> asteroidList = null;
        if (nasaNeoResponse != null) {
            asteroidList = new ArrayList<>(nasaNeoResponse.getNearEarthObjects().values().stream().flatMap(List::stream).toList());
        }

        return asteroidList;
    }

    public String getUrl(final LocalDate fromDate, final LocalDate toDate) {

        return UriComponentsBuilder.fromUriString(nasaApiUrl)
                .queryParam("start_date", fromDate)
                .queryParam("end_date", toDate)
                .queryParam("api_key", nasaApiKey)
                .toUriString();
    }
}
