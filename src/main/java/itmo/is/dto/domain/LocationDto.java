package itmo.is.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record LocationDto(
        @Schema(example = "3")
        @JsonProperty("location_x")
        double x,

        @Schema(example = "4")
        @JsonProperty("location_y")
        Double y,

        @Schema(example = "5")
        @JsonProperty("location_z")
        double z
) {
}
