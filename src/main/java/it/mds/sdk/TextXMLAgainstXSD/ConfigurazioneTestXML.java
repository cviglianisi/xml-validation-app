package it.mds.sdk.TextXMLAgainstXSD;

import it.mds.sdk.TextXMLAgainstXSD.Model.PercorsoFileXML;
import it.mds.sdk.TextXMLAgainstXSD.Model.PercorsoFileXSD;
import it.mds.sdk.TextXMLAgainstXSD.Model.ResponseWarningMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.logging.Logger;


public class ConfigurazioneTestXML {
    private static final Logger log = Logger.getLogger("log");
    private static final String FILE_CONF = "application.properties";
    final PercorsoFileXML percorsoFileXML;
    final PercorsoFileXSD percorsoFileXSD;
    final ResponseWarningMessage responseWarningMessage;

    public ConfigurazioneTestXML() {
        this(leggiConfigurazioneEsterna());
    }

    public ConfigurazioneTestXML(final Properties conf) {
        this.percorsoFileXML = PercorsoFileXML.builder()
                .withPercorso(conf.getProperty("percorso.xml", ""))
                .build();
        this.percorsoFileXSD = PercorsoFileXSD.builder()
                .withPercorso(conf.getProperty("percorso.xsd", ""))
                .build();
        this.responseWarningMessage = ResponseWarningMessage.builder()
                .withMessage(conf.getProperty("validation.warning.message", ""))
                .build();
    }


    private static Properties leggiConfigurazione(final String nomeFile) {
        log.info("{}.leggiConfigurazione - nome file: " +  nomeFile);
        final Properties prop = new Properties();
        try (final InputStream is = ConfigurazioneTestXML.class.getClassLoader().getResourceAsStream(nomeFile)) {
            prop.load(is);
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
        return prop;
    }

    private static Properties leggiConfigurazioneEsterna() {
        Properties properties = new Properties();

        try (InputStreamReader in = new InputStreamReader(new FileInputStream("/sdk/testXML/properties/" + FILE_CONF),
                StandardCharsets.UTF_8)) {
            properties.load(in);
        } catch (IOException e) {
            log.severe(e.getMessage());
            return leggiConfigurazione(FILE_CONF);
        }
        return properties;
    }
}
