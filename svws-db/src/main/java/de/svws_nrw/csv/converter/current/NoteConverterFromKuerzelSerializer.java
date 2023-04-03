package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.core.types.Note;
import de.svws_nrw.db.converter.current.NoteConverterFromKuerzel;

/**
 * Diese Klasse ist ein Serialisierer für Noten. Sie serialisiert
 * einen Wert der Aufzählung {@link Note} in die Datenbankdarstellung als
 * Notenkuerzel, welche als Zeichenkette dargestellt werden.
 */
public final class NoteConverterFromKuerzelSerializer extends StdSerializer<Note> {

	private static final long serialVersionUID = 7505421606549933149L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public NoteConverterFromKuerzelSerializer() {
		super(Note.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 *
	 * @param t   das Klassen-Objekt
	 */
	public NoteConverterFromKuerzelSerializer(final Class<Note> t) {
		super(t);
	}

	@Override
	public void serialize(final Note value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
		gen.writeString(NoteConverterFromKuerzel.instance.convertToDatabaseColumn(value));
	}

}
