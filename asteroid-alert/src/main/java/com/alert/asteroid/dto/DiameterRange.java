package com.alert.asteroid.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiameterRange {
    @JsonProperty("estimated_diameter_min")
    private Double minDiameter;

    @JsonProperty("estimated_diameter_max")
    private Double maxDiameter;
}
