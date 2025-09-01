package translation;

import com.openmock.OpenMockException;

public interface ITranslator {
    String translate(String text, String targetLang) throws OpenMockException;

    String translate(String text, String sourceLang, String targetLang) throws OpenMockException;
}
