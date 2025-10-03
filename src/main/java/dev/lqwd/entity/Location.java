package dev.lqwd.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "locations", schema = "weather_viewer",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_user_location",
                columnNames = {"user_id", "latitude", "longitude"}
        ))
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, name = "user_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User user;

    @Column(precision = 9,
            scale = 6,
            columnDefinition = "NUMERIC(9,6) CHECK (latitude BETWEEN -90 AND 90)")
    private BigDecimal latitude;

    @Column(precision = 10,
            scale = 6,
            columnDefinition = "NUMERIC(10,6) CHECK (longitude BETWEEN -180 AND 180)")
    private BigDecimal longitude;

}
