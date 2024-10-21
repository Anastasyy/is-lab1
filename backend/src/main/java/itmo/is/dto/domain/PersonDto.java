package itmo.is.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PersonDto(
        @JsonProperty("id")
        int id,

        @JsonProperty("name")
        String name,

        @JsonProperty("coordinates")
        CoordinatesDto coordinates,

        @JsonProperty("creation_date")
        LocalDateTime creationDate,

        @JsonProperty("eye_color")
        ColorDto eyeColor,

        @JsonProperty("hair_color")
        ColorDto hairColor,

        @JsonProperty("location")
        LocationDto location,

        @JsonProperty("height")
        Long height,

        @JsonProperty("birthday")
        LocalDate birthday,

        @JsonProperty("weight")
        Integer weight,

        @JsonProperty("nationality")
        CountryDto nationality
) {
}
