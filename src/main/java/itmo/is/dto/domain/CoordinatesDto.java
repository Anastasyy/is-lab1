package itmo.is.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record CoordinatesDto(
        @Schema(example = "1")
        @JsonProperty("coordinate_x")
        Integer x,

        @Schema(example = "2")
        @JsonProperty("coordinate_y")
        int y
) {
}
