package dev.lqwd.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Service
@Table(name = "locations", schema = "weather_viewer")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
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
            columnDefinition = "NUMERIC(9,6) CHECK (latitude BETWEEN -180 AND 180)")
    private BigDecimal longitude;

}
