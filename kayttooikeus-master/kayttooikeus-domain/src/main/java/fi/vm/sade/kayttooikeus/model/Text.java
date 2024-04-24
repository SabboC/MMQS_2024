package fi.vm.sade.kayttooikeus.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "text")
@Getter @Setter
public class Text extends IdentifiableAndVersionedEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "textgroup_id")
    private TextGroup textGroup;
    private String text;
    private String lang;

    public Text() {
    }

    public Text(TextGroup textGroup, String lang, String text) {
        this.textGroup = textGroup;
        this.lang = lang;
        this.text = text;
    }
}
