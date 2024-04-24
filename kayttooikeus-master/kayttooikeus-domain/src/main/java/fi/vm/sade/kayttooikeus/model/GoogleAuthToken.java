package fi.vm.sade.kayttooikeus.model;

import lombok.*;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "google_auth_token", schema = "public")
public class GoogleAuthToken {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue
    private Integer id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "henkilo_id", nullable = false, unique = true)
    private Henkilo henkilo;

    @Column(name = "secret_key", nullable = false)
    private String secretKey;

    @Column(name = "salt", nullable = false)
    private String salt;

    @Column(name = "iv", nullable = false)
    private String iv;

    @Column(name = "registration_date", nullable = true)
    private LocalDateTime registrationDate;
}
