package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.core.types.Note;
import de.svws_nrw.db.converter.current.NoteConverterFromNotenpunkteString;

/**
 * Diese Klasse ist ein Serialisierer für Noten. Sie serialisiert
 * einen Wert der Aufzählung {@link Note} in die Datenbankdarstellung
 * als Notenpunkte, welche in einer Zeichenkette dargestellt werden.
 */
public final class NoteConverterFromNotenpunkteStringSerializer extends StdSerializer<Note> {

	private static final long serialVersionUID = 5891746067532824732L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public NoteConverterFromNotenpunkteStringSerializer() {
		super(Note.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	public NoteConverterFromNotenpunkteStringSerializer(final Class<Note> t) {
		super(t);
	}

	@Override
	public void serialize(final Note value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
		gen.writeString(NoteConverterFromNotenpunkteString.instance.convertToDatabaseColumn(value));
	}

}
