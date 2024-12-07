package itmo.is.model.domain;

import itmo.is.model.security.OwnedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "people")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person extends OwnedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @NotEmpty
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Embedded
    private Coordinates coordinates;

    @CreationTimestamp
    @Column(name = "creation_date", updatable = false, nullable = false)
    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "eye_color", nullable = true)
    private Color eyeColor;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "hair_color", nullable = false)
    private Color hairColor;

    @NotNull
    @Embedded
    private Location location;

    @NotNull
    @Min(1)
    @Column(name = "height", nullable = false)
    private Long height;

    @NotNull
    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Min(1)
    @Column(name = "weight", nullable = true)
    private Integer weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "nationality", nullable = true)
    private Country nationality;
}
