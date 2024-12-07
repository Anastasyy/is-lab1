package itmo.is.model.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates {
    @NotNull
    @Max(384)
    @Column(name = "coordinate_x", nullable = false)
    private Integer x;

    @Max(663)
    @Column(name = "coordinate_y", nullable = true)
    private int y;
}
