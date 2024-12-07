package itmo.is.dto.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record CountResponse(
        @Schema(example = "15")
        @JsonProperty("count")
        long count
) {
}
