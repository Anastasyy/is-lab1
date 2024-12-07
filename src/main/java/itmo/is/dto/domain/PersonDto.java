package itmo.is.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import itmo.is.dto.authentication.UserDto;
import itmo.is.model.domain.Color;
import itmo.is.model.domain.Country;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PersonDto(
        @Schema(example = "1")
        @JsonProperty("id")
        int id,

        @Schema(example = "Kirill Kravtsov")
        @JsonProperty("name")
        String name,

        @JsonProperty("coordinates")
        CoordinatesDto coordinates,

        @Schema(example = "2024-02-10T16:24:23.760031")
        @JsonProperty("creation_date")
        LocalDateTime creationDate,

        @Schema(example = "BLUE")
        @JsonProperty("eye_color")
        Color eyeColor,

        @Schema(example = "YELLOW")
        @JsonProperty("hair_color")
        Color hairColor,

        @JsonProperty("location")
        LocationDto location,

        @Schema(example = "175")
        @JsonProperty("height")
        Long height,

        @Schema(example = "2003-08-27")
        @JsonProperty("birthday")
        LocalDate birthday,

        @Schema(example = "55")
        @JsonProperty("weight")
        Integer weight,

        @Schema(example = "UNITED_KINGDOM")
        @JsonProperty("nationality")
        Country nationality,

        @JsonProperty("owner")
        UserDto owner,

        @Schema(example = "false")
        @JsonProperty("admin_edit_allowed")
        boolean adminEditAllowed
) {
}
