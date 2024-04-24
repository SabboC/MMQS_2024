package fi.vm.sade.kayttooikeus.repositories.populate;

import fi.vm.sade.kayttooikeus.model.Text;
import fi.vm.sade.kayttooikeus.model.TextGroup;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

public class TextGroupPopulator implements Populator<TextGroup> {
    private Map<String,String> texts = new HashMap<>();

    public TextGroupPopulator() {
    }

    public TextGroupPopulator(Map<String, String> texts) {
        this.texts = texts;
    }

    public TextGroupPopulator(String lang, String value) {
        this.texts.put(lang, value);
    }
    
    public TextGroupPopulator put(String lang, String value) {
        this.texts.put(lang, value);
        return this;
    }
    
    public static TextGroupPopulator text() {
        return new TextGroupPopulator();
    }
    public static TextGroupPopulator text(String lang, String value) {
        return new TextGroupPopulator(lang, value);
    }

    @Override
    public TextGroup apply(EntityManager entityManager) {
        TextGroup group = new TextGroup();
        this.texts.entrySet().forEach(e -> group.addText(new Text(group, e.getKey(), e.getValue())));
        entityManager.persist(group);
        return group;
    }
}
