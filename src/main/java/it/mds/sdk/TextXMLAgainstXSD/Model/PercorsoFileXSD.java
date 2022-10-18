package it.mds.sdk.TextXMLAgainstXSD.Model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(setterPrefix = "with")
public class PercorsoFileXSD {
    String percorso;
}