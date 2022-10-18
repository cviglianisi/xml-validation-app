package it.mds.sdk.TextXMLAgainstXSD;

import java.io.*;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;


public class MainTester {
    private static final Logger log = Logger.getLogger("log");
    private static final String VALIDATION_ERROR_MESSAGE = "ERROR during the validation process ";

    public static void main(String[] args) throws Exception {
        MainTester mt = new MainTester();
        TestXMLConfiguration conf = new TestXMLConfiguration();

        FileInputStream fis = new FileInputStream(conf.xsdPathFile.getPercorso());
        mt.validate(new File(conf.xmlPathFile.getPercorso()), new InputSource(fis));


    }

    public void validate(File oXML, InputSource schema) throws Exception {
        log.info("{}.validate() - filename: " + oXML.getName() + " schema: " + schema);
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
                sb.append(VALIDATION_ERROR_MESSAGE + e.getMessage());
                oCallbackManager.oErrorTrace.addErrorBuffer(sb);
            } catch (IOException e1) {
                throw new Exception(VALIDATION_ERROR_MESSAGE, e1);
            }
        } catch (ParserConfigurationException e) {
            throw new Exception(VALIDATION_ERROR_MESSAGE, e);
        } catch (SAXParseException e) {

        } catch (SAXException e) {
            throw new Exception(VALIDATION_ERROR_MESSAGE, e);
        } catch (Throwable e) {
            throw new Exception(VALIDATION_ERROR_MESSAGE, e);
        }

        StringBuffer sb = oCallbackManager.getErrorTrace().getErrors();
        System.out.println(sb.toString());
    }


    private static class CallbackManager extends DefaultHandler {

        private static final String ERROR_HEADER = "*** VALIDATION ERROR ***\n" ;
        private static final String ERROR_BODY_MESSAGE = "ERRORE ALLA RIGA/COLONNA: ";
        private static final String EXCEPTION_MESSAGE = "MESSAGGIO ERRORE: ";
        private ErrorTrace oErrorTrace = new ErrorTrace();

        public ErrorTrace getErrorTrace() {
            return this.oErrorTrace;
        }

        public void error(SAXParseException exception) throws SAXParseException {
            log.severe("ERROR");
            StringBuffer oBuffer = new StringBuffer(300);
            oBuffer.append(ERROR_HEADER);
            oBuffer.append(ERROR_BODY_MESSAGE + exception.getLineNumber() + "/"
                    + exception.getColumnNumber() + "\n");
            oBuffer.append(EXCEPTION_MESSAGE + exception.getMessage()+"\n");
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
            oBuffer.append(ERROR_HEADER);
            oBuffer.append(ERROR_BODY_MESSAGE + exception.getLineNumber() + "/"
                    + exception.getColumnNumber() + "\n");
            oBuffer.append(EXCEPTION_MESSAGE + exception.getMessage() + "\n");
            oBuffer.append("******\n");
            this.oErrorTrace.addErrorBuffer(oBuffer);
        }

    }

}
