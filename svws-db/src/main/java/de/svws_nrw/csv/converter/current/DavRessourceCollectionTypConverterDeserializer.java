package de.svws_nrw.csv.converter.current;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.dav.DavRessourceCollectionTyp;
import de.svws_nrw.db.converter.current.DavRessourceCollectionTypConverter;

import java.io.IOException;

/**
 * Diese Klasse ist einen Deserialisierer von Benutzer-Typ-Objekten.
 */
public final class DavRessourceCollectionTypConverterDeserializer extends StdDeserializer<DavRessourceCollectionTyp> {

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
	protected DavRessourceCollectionTypConverterDeserializer(final Class<DavRessourceCollectionTyp> t) {
		super(t);
	}

	@Override
	public DavRessourceCollectionTyp deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		Integer i;
		try {
			i = Integer.parseInt(p.getText());
		} catch (@SuppressWarnings("unused") final NumberFormatException e) {
			i = null;
		}
		return DavRessourceCollectionTypConverter.instance.convertToEntityAttribute(i);
	}

}
