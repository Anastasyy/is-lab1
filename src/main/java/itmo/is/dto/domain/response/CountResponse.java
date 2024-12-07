package itmo.is.dto.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CountResponse(
        @JsonProperty("count")
        long count
) {
}
