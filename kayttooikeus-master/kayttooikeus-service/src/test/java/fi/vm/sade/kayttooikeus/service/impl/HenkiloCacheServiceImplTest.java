package fi.vm.sade.kayttooikeus.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class HenkiloCacheServiceImplTest {

    private final String desc;
    private final String input;
    private final String expected;

    public HenkiloCacheServiceImplTest(String desc, String input, String expected) {
        this.desc = desc;
        this.input = input;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<String[]> parameters() {
        return Arrays.asList(
                new String[]{"Handles null:s", null, null},
                new String[]{"Handles empty strings", "", ""},
                new String[]{"Trims prefix", " foo", "foo"},
                new String[]{"Trims suffix", "foo ", "foo"},
                new String[]{"Trims both", " foo ", "foo"}
        );
    }

    @Test
    public void trim() {
        assertEquals(desc, expected, HenkiloCacheServiceImpl.trim(input));
    }
}
