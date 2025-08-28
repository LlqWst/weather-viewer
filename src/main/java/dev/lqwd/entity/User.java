package dev.lqwd.entity;

import jakarta.persistence.*;
import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@NamedQuery(
        name = "User.findByLogin",
        query = "SELECT u FROM User u WHERE u.login = :login"
)
@Table(name = "users", schema = "weather_viewer")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

}
