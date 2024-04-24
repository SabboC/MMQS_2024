package fi.vm.sade.kayttooikeus.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "login_counter")
public class LoginCounter {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false, unique = true)
    private Kayttajatiedot kayttajatiedot;

    @Column(name = "login_count")
    private long count;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    public void increment() {
        count++;
    }

    @PrePersist
    @PreUpdate
    public void updateLoginTimestamp() {
        lastLogin = LocalDateTime.now();
    }
}
