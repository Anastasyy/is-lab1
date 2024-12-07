package itmo.is.dto.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import itmo.is.dto.domain.CoordinatesDto;
import itmo.is.dto.domain.LocationDto;
import itmo.is.model.domain.Color;
import itmo.is.model.domain.Country;

import java.time.LocalDate;

public record UpdatePersonRequest(
        @Schema(example = "Kirill Kravtsov")
        @JsonProperty(value = "name", required = true)
        String name,

        @JsonProperty(value = "coordinates", required = true)
        CoordinatesDto coordinates,

        @Schema(example = "BLUE")
        @JsonProperty(value = "eye_color", required = false)
        Color eyeColor,

        @Schema(example = "YELLOW")
        @JsonProperty(value = "hair_color", required = true)
        Color hairColor,

        @JsonProperty(value = "location", required = true)
        LocationDto location,

        @Schema(example = "175")
        @JsonProperty(value = "height", required = true)
        Long height,

        @Schema(example = "2003-08-27")
        @JsonProperty(value = "birthday", required = true)
        LocalDate birthday,

        @Schema(example = "55")
        @JsonProperty(value = "weight", required = false)
        Integer weight,

        @Schema(example = "FRANCE")
        @JsonProperty(value = "nationality", required = false)
        Country nationality
) {
}
