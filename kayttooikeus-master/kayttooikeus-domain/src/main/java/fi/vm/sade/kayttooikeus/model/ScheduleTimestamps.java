package fi.vm.sade.kayttooikeus.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "schedule_timestamps", schema = "public")
public class ScheduleTimestamps extends IdentifiableAndVersionedEntity {

    private LocalDateTime modified = LocalDateTime.now();

    private String identifier;

}
