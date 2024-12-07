package itmo.is.dto.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PercentageResponse(
        @JsonProperty("percentage")
        double percentage
) {
}
