package translation;

import com.deepl.api.DeepLClient;
import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.openmock.OpenMockException;
import com.openmock.util.FileUtil;
import lombok.extern.log4j.Log4j2;

import java.util.Properties;

///
/// See: [Your first API request](https://developers.deepl.com/docs/getting-started/your-first-api-request#java)
///
@Log4j2
public class DepLTranslator implements ITranslator{
    private final DeepLClient translator;

    public DepLTranslator() throws OpenMockException {
        Properties prop = FileUtil.loadProperties("deepl.properties");
        if(prop == null ) {
            throw new OpenMockException("deepl.properties file not found");
        }
        else {
            String authKey = prop.getProperty("api.key");
            if (authKey == null || authKey.compareTo("") == 0) {
                throw new OpenMockException("api.key not found in deepl.properties file");
            } else {
                translator = new DeepLClient(prop.getProperty("api.key"));
            }
        }
    }

    @Override
    public String translate(String text, String targetLang) throws OpenMockException {
        return translate(text, null, targetLang);
    }

    @Override
    public String translate(String text, String sourceLang, String targetLang) throws OpenMockException {
        TextResult result;
        try {
            result = translator.translateText(text, sourceLang, targetLang);
        } catch (DeepLException | InterruptedException e) {
            log.error("Error call DeepL API: ", e);
            throw new OpenMockException(e);
        }
        log.debug(result.getText()); // "Bonjour, le monde !"    }

        return result.getText();
    }
}
