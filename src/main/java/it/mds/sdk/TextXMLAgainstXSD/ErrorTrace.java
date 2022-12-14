package it.mds.sdk.TextXMLAgainstXSD;

import java.util.ArrayList;
import java.util.List;


public final class ErrorTrace {
	
	private List<Object> oErrors;
	
	public ErrorTrace()
	{
		oErrors = new ArrayList<>();
	}
	
	public void addErrorBuffer(StringBuffer oError)
	{
		oErrors.add(oError);
	}
	
	public StringBuffer getErrors()
	{
		StringBuffer oBuffer = new StringBuffer(500);
		TestXMLConfiguration configurazioneTestXML = new TestXMLConfiguration();
		for (Object oError : this.oErrors) {
			if (oBuffer.length() > 10000000) {
				StringBuffer messaggioDimensioneElevata = new StringBuffer(configurazioneTestXML.responseWarningMessage.getMessage());
				messaggioDimensioneElevata.append(oBuffer);
				oBuffer = messaggioDimensioneElevata;
				break; //limito a 10 MByte la dimensione del testo degli errori XSD
			}
			oBuffer.append(oError).append("\n");
		}
		
		return oBuffer;
	}
	
//	public boolean areThereErrors()
//	{
//		return !Objects.isNull(this.oErrors) && this.oErrors.size() != 0;
//	}
}
