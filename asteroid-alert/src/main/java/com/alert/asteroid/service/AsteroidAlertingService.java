package com.alert.asteroid.service;

import com.alert.asteroid.client.NasaClient;
import com.alert.asteroid.dto.Asteroid;
import com.alert.asteroid.event.AsteroidCollisionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class AsteroidAlertingService {

    private final NasaClient nasaClient;
    private final KafkaTemplate<String, AsteroidCollisionEvent> kafkaTemplate;

    public AsteroidAlertingService(NasaClient nasaClient, KafkaTemplate<String, AsteroidCollisionEvent> kafkaTemplate) {
        this.nasaClient = nasaClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void alert() {
        log.info("Alert service called");

        // Start and End date
        final LocalDate fromDate = LocalDate.now();
        final LocalDate toDate = LocalDate.now().plusDays(7);

        // Call NASA API to get the asteroid data
        log.info("Getting asteroid list from dates: {} to {}", fromDate, toDate);
        final List<Asteroid> asteroidList = nasaClient.getNeoAsteroids(fromDate, toDate);
        log.info("Retrieved Asteroid list of size: {}", asteroidList.size());

        // If there are any hazardous asteroid, send an alert
        final List<Asteroid> dangerousAsteroids = asteroidList.stream()
                .filter(Asteroid::isPotentiallyHazardous)
                .toList();

        log.info("Found {} hazardous asteroids", dangerousAsteroids.size());

        // Create an alert and put on Kafka topic
        final List<AsteroidCollisionEvent> asteroidCollisionEvents = createEventListOfDangerousAsteroids(
                dangerousAsteroids);
        log.info("Sending {} asteroid collision alerts to Kafka", asteroidCollisionEvents.size());

        asteroidCollisionEvents.forEach(event -> {
            kafkaTemplate.send("asteroid-alert", event);
            log.info("Sent {} asteroid collision alert to Kafka", event);
        });

    }

    private List<AsteroidCollisionEvent> createEventListOfDangerousAsteroids(List<Asteroid> dangerousAsteroids) {
        return dangerousAsteroids.stream().map(
                asteroid -> {
                    return AsteroidCollisionEvent.builder()
                            .asteroidName(asteroid.getName())
                            .closeApproachDate(
                                    asteroid.getCloseApproachData().getFirst().getCloseApproachDate().toString())
                            .missDistanceKilometers(
                                    asteroid.getCloseApproachData().getFirst().getMissDistance().getKilometers())
                            .estimatedDiameterAvgMeters((asteroid.getEstimatedDiameter().getMeters().getMinDiameter()
                                    + asteroid.getEstimatedDiameter().getMeters().getMaxDiameter()) / 2)
                            .build();
                }).toList();
    }
}
