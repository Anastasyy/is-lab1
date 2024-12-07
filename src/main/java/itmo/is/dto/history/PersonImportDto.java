package itmo.is.dto.history;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import itmo.is.dto.authentication.UserDto;

import java.time.LocalDateTime;

public record PersonImportDto(
        @Schema(example = "1")
        @JsonProperty(value = "id", required = true)
        Long id,

        @JsonProperty(value = "user", required = true)
        UserDto user,

        @Schema(example = "true")
        @JsonProperty(value = "success", required = true)
        boolean success,

        @Schema(example = "5")
        @JsonProperty(value = "objects_added", required = true)
        Integer objectsAdded,

        @Schema(example = "2024-02-10 16:24:23")
        @JsonProperty(value = "started_at", required = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime startedAt,

        @Schema(example = "2024-11-09 22:47:24")
        @JsonProperty(value = "finished_at", required = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime finishedAt
) {
}
