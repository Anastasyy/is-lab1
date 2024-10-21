package itmo.is.model.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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
public class Location {
    @Column(name = "location_x", nullable = true)
    private double x;

    @NotNull
    @Column(name = "location_y", nullable = false)
    private Double y;

    @Column(name = "location_z", nullable = true)
    private double z;
}
