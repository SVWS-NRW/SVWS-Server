package de.nrw.schule.svws.csv.converter.current;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.core.types.dav.DavRessourceCollectionTyp;
import de.nrw.schule.svws.db.converter.current.DavRessourceCollectionTypConverter;

import java.io.IOException;

/**
 * Diese Klasse ist einen Deserialisierer von Benutzer-Typ-Objekten.
 */
public class DavRessourceCollectionTypConverterDeserializer extends StdDeserializer<DavRessourceCollectionTyp> {

	private static final long serialVersionUID = -1745427357127293925L;

	/**
	 * Erzeugt ein neues Objekt zur Deserialisierung
	 */
	public DavRessourceCollectionTypConverterDeserializer() {
		super(BenutzerKompetenz.class);
	}
	
	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	protected DavRessourceCollectionTypConverterDeserializer(Class<DavRessourceCollectionTyp> t) {
		super(t);
	}

	@Override
	public DavRessourceCollectionTyp deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Integer i;
		try {
			i = Integer.parseInt(p.getText());
		} catch (@SuppressWarnings("unused") NumberFormatException e) {
			i = null;
		}
		return DavRessourceCollectionTypConverter.instance.convertToEntityAttribute(i);
	}
	
}
