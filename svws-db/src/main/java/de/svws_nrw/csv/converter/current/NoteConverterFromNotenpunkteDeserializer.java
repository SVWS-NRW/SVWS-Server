package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.Note;
import de.svws_nrw.db.converter.current.NoteConverterFromNotenpunkte;

/**
 * Diese Klasse ist ein Deserialisierer für Noten. Sie deserialisiert
 * die Datenbankdarstellung als Notenpunkte, welche als Zahl dargestellt
 * werden, in einen Wert der Aufzählung {@link Note}.
 */
public final class NoteConverterFromNotenpunkteDeserializer extends StdDeserializer<Note> {

	private static final long serialVersionUID = -2517057436836075856L;

	/**
	 * Erzeugt einen neuen Deerialisierer
	 */
	public NoteConverterFromNotenpunkteDeserializer() {
		super(Note.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	protected NoteConverterFromNotenpunkteDeserializer(final Class<Note> t) {
		super(t);
	}

	@Override
	public Note deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Integer i;
		try {
			i = Integer.parseInt(p.getText());
		} catch (@SuppressWarnings("unused") final NumberFormatException e) {
			i = null;
		}
		return NoteConverterFromNotenpunkte.instance.convertToEntityAttribute(i);
	}

}
