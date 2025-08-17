package dev.lqwd.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;

import java.time.LocalDateTime;
import java.util.UUID;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "sessions", schema = "weather_viewer")
public class Session {

    @Id
    @GeneratedValue
    @JdbcType(UUIDJdbcType.class)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, name = "user_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User user;

    @Column(nullable = false, name = "expires_at")
    private final LocalDateTime expiresAt = LocalDateTime.now();

}
