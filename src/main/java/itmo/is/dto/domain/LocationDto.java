package itmo.is.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LocationDto(
        @JsonProperty("location_x")
        double x,

        @JsonProperty("location_y")
        Double y,

        @JsonProperty("location_z")
        double z
) {
}
