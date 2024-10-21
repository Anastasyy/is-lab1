package itmo.is.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CoordinatesDto(
        @JsonProperty("coordinate_x")
        Integer x,

        @JsonProperty("coordinate_y")
        int y
) {
}
