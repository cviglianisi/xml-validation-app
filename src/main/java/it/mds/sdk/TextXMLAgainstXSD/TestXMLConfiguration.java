package it.mds.sdk.TextXMLAgainstXSD;

import it.mds.sdk.TextXMLAgainstXSD.Model.PercorsoFileXML;
import it.mds.sdk.TextXMLAgainstXSD.Model.PercorsoFileXSD;
import it.mds.sdk.TextXMLAgainstXSD.Model.ResponseWarningMessage;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.logging.Logger;


public class TestXMLConfiguration {
    private static final Logger log = Logger.getLogger("log");
    private static final String FILE_CONF = "application.properties";
    private static final String PROPERTIES_FILE_PATH = "/sdk/testXML/properties/";
    final PercorsoFileXML xmlPathFile;
    final PercorsoFileXSD xsdPathFile;
    final ResponseWarningMessage responseWarningMessage;

    public TestXMLConfiguration() {
        this(readExternalConfiguration());
    }

    public TestXMLConfiguration(final Properties conf) {
        this.xmlPathFile = PercorsoFileXML.builder()
                .withPercorso(conf.getProperty("path.xml", ""))
                .build();
        this.xsdPathFile = PercorsoFileXSD.builder()
                .withPercorso(conf.getProperty("path.xsd", ""))
                .build();
        this.responseWarningMessage = ResponseWarningMessage.builder()
                .withMessage(conf.getProperty("validation.warning.message", ""))
                .build();
    }


    private static Properties readConfiguration(final String nomeFile) {
        log.info("{}.leggiConfigurazione - nome file: " +  nomeFile);
        final Properties prop = new Properties();
        try (final InputStream is = TestXMLConfiguration.class.getClassLoader().getResourceAsStream(nomeFile)) {
            prop.load(is);
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
        return prop;
    }

    private static Properties readExternalConfiguration() {
        Properties properties = new Properties();
        try (InputStreamReader in = new InputStreamReader(new FileInputStream(PROPERTIES_FILE_PATH + FILE_CONF),
                StandardCharsets.UTF_8)) {
            properties.load(in);
        } catch (IOException e) {
            log.severe(e.getMessage());
            return readConfiguration(FILE_CONF);
        }
        return properties;
    }
}
