package com.openmock.translation;

import com.openmock.OpenMockException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import translation.DepLTranslator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DepLTranslatorTest {
    static DepLTranslator translator;

    @BeforeAll
    public static void setup() throws OpenMockException {
        translator = new DepLTranslator();
    }

    @Test
    public void translate(){
        try {
            assertEquals("Hello", translator.translate("Hola", "es", "en-GB"));
        } catch (OpenMockException e) {
            fail(e);
        }
    }

    @Test
    public void translateDetectingSourceLand(){
        try {
            assertEquals("Hola", translator.translate("Hello", "es"));
        } catch (OpenMockException e) {
            fail(e);
        }
    }
}
