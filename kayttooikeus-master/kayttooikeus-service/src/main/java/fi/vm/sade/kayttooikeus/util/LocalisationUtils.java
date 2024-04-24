package fi.vm.sade.kayttooikeus.util;

import fi.vm.sade.kayttooikeus.model.Text;
import fi.vm.sade.kayttooikeus.model.TextGroup;

import java.util.function.Supplier;

public class LocalisationUtils {
    /**
     * Returns text from text group with given preferred language code
     * defaulting to given supplier.
     *
     * @param preferredLanguageCode preferred language code
     * @param textGroup text group
     * @param defaultValue default value supplier
     * @return text
     */
    public static String getText(String preferredLanguageCode, TextGroup textGroup, Supplier<String> defaultValue) {
        String name = null;
        if (textGroup != null) {
            for (Text text : textGroup.getTexts()) {
                if (text.getText() != null) {
                    name = text.getText();
                    if (preferredLanguageCode.equalsIgnoreCase(text.getLang())) {
                        break;
                    }
                }
            }
        }
        if (name != null) {
            return name;
        }
        return defaultValue.get();
    }
}
