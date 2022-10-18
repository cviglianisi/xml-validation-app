package it.mds.sdk.TextXMLAgainstXSD;

import java.io.*;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import lombok.extern.slf4j.Slf4j;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;


public class MainTester {
    private static final Logger log = Logger.getLogger("log");

    public static void main(String[] args) throws Exception {
        MainTester mt = new MainTester();
        ConfigurazioneTestXML conf = new ConfigurazioneTestXML();

        FileInputStream fis = new FileInputStream(conf.percorsoFileXSD.getPercorso());
        mt.validate(new File(conf.percorsoFileXML.getPercorso()), new InputSource(fis));


    }

    public void validate(File oXML, InputSource schema) throws Exception {
        log.info("{}.validate() - nome file: " + oXML.getName());
        System.out.println(schema);
        SAXParserFactory sp = SAXParserFactory.newInstance();
        sp.setNamespaceAware(true);
        sp.setValidating(true);
        // sp.setValidating(false);
        CallbackManager oCallbackManager = new CallbackManager();

        try {
            SAXParser oSAXParser = sp.newSAXParser();
            oSAXParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                    "http://www.w3.org/2001/XMLSchema");
            oSAXParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", schema);
            try {
                oSAXParser.parse(oXML, oCallbackManager);
            } catch (UTFDataFormatException e) {
                StringBuffer sb = new StringBuffer();
                sb.append("ERRORE NEL PROCESSO DI VALIDAZIONE " + e.getMessage());
                oCallbackManager.oErrorTrace.addErrorBuffer(sb);
            } catch (IOException e1) {
                throw new Exception("ERRORE NEL PROCESSO DI VALIDAZIONE", e1);
            }
        } catch (ParserConfigurationException e) {
            throw new Exception("ERRORE NEL PROCESSO DI VALIDAZIONE", e);
        } catch (SAXParseException e) {
            /*
             * MODIFICA DEL 07/12/2006 - autore: Andrea La Terra aggiunto il catch
             * dell'eccezione SAXParse - Viene gestita come se si trattasse di un semplice
             * errore di validazione (restituisce comunque la riga a cui l'errore si è
             * verificato... per cui NON rilancio l'eccezione). Questa eccezione riporta il
             * dettaglio sulla riga XML in cui si trova l'errore (Già memorizzato dal
             * callback manager)
             */
        } catch (SAXException e) {
            throw new Exception("ERRORE NEL PROCESSO DI VALIDAZIONE", e);
        } catch (Throwable e) {
            throw new Exception("ERRORE NEL PROCESSO DI VALIDAZIONE", e);
        }

        StringBuffer sb = oCallbackManager.getErrorTrace().getErrors();
        System.out.println(sb.toString());
    }


    private static class CallbackManager extends DefaultHandler {

        private ErrorTrace oErrorTrace = new ErrorTrace();

        public ErrorTrace getErrorTrace() {
            return this.oErrorTrace;
        }

        public void error(SAXParseException exception) throws SAXParseException {
            log.severe("ERROR");
            StringBuffer oBuffer = new StringBuffer(300);
            oBuffer.append("*** VALIDATION ERROR ***\n");
            oBuffer.append("ERRORE VERIFICATO ALLA RIGA/COLONNA:" + exception.getLineNumber() + "/"
                    + exception.getColumnNumber() + "\n");
            oBuffer.append("MESSAGGIO ERRORE::" + exception.getMessage()+"\n");
            oBuffer.append("******\n");
            // ****************************************************/
//			if (exception.getMessage().indexOf("cvc-complex-type.3.2.2") == -1) 

            this.oErrorTrace.addErrorBuffer(oBuffer);

            if (exception.getMessage().contains("cvc-elt.1")) {
                throw new SAXParseException(exception.getMessage(), "", "", exception.getLineNumber(),
                        exception.getColumnNumber(), exception);
            }
        }

        public void fatalError(SAXParseException exception) {
            log.severe("FATAL ERROR");
            StringBuffer oBuffer = new StringBuffer(300);
            oBuffer.append("*** VALIDATION ERROR ***\n");
            oBuffer.append("ERRORE VERIFICATO ALLA RIGA/COLONNA:" + exception.getLineNumber() + "/"
                    + exception.getColumnNumber() + "\n");
            oBuffer.append("MESSAGGIO ERRORE::" + exception.getMessage() + "\n");
            oBuffer.append("******\n");
            this.oErrorTrace.addErrorBuffer(oBuffer);
        }

    }

}
