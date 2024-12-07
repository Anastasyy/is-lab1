package itmo.is.dto.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record PercentageResponse(
        @Schema(example = "25.0")
        @JsonProperty("percentage")
        double percentage
) {
}
