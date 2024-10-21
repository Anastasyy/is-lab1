package itmo.is.dto.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import itmo.is.dto.domain.ColorDto;
import itmo.is.dto.domain.CoordinatesDto;
import itmo.is.dto.domain.CountryDto;
import itmo.is.dto.domain.LocationDto;

import java.time.LocalDate;

public record UpdatePersonRequest(
        @JsonProperty(value = "id", required = true)
        int id,

        @JsonProperty(value = "name", required = true)
        String name,

        @JsonProperty(value = "coordinates", required = true)
        CoordinatesDto coordinates,

        @JsonProperty(value = "eye_color", required = false)
        ColorDto eyeColor,

        @JsonProperty(value = "hair_color", required = true)
        ColorDto hairColor,

        @JsonProperty(value = "location", required = true)
        LocationDto location,

        @JsonProperty(value = "height", required = true)
        Long height,

        @JsonProperty(value = "birthday", required = true)
        LocalDate birthday,

        @JsonProperty(value = "weight", required = false)
        Integer weight,

        @JsonProperty(value = "nationality", required = false)
        CountryDto nationality
) {
}
