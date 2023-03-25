package de.svws_nrw.csv.converter.current;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.core.types.dav.DavRessourceCollectionTyp;
import de.svws_nrw.db.converter.current.DavRessourceCollectionTypConverter;

import java.io.IOException;

/**
 * Diese Klasse ist einen Serialisierer von Benutzer-Typ-Objekten.
 */
public class DavRessourceCollectionTypConverterSerializer extends StdSerializer<DavRessourceCollectionTyp> {

	private static final long serialVersionUID = -1745427357127293925L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public DavRessourceCollectionTypConverterSerializer() {
		super(DavRessourceCollectionTyp.class);
	}
	
	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 * 
	 * @param t   ein Klassenobjekt für die Benutzer-Typ-Klasse
	 */
	public DavRessourceCollectionTypConverterSerializer(Class<DavRessourceCollectionTyp> t) {
		super(t);
	}

	@Override
	public void serialize(DavRessourceCollectionTyp value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(DavRessourceCollectionTypConverter.instance.convertToDatabaseColumn(value).toString());
	}

}
