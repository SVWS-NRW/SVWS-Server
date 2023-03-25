package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.core.types.Note;
import de.svws_nrw.db.converter.current.NoteConverterFromInteger;

/**
 * Diese Klasse ist ein Serialisierer für Noten. Sie serialisiert
 * einen Wert der Aufzählung {@link Note} in die Datenbankdarstellung als 
 * Integer, welche eine Note ohne Tendenz repräsentiert (1-6).
 */
public class NoteConverterFromIntegerSerializer extends StdSerializer<Note> {

	private static final long serialVersionUID = 7505421606549933149L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public NoteConverterFromIntegerSerializer() {
		super(Note.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	public NoteConverterFromIntegerSerializer(Class<Note> t) {
		super(t);
	}

	@Override
	public void serialize(Note value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		Integer note = NoteConverterFromInteger.instance.convertToDatabaseColumn(value);
		gen.writeString(note == null ? null : "" + note);
	}

}
