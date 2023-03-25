package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.svws_nrw.core.types.Note;
import de.svws_nrw.db.converter.current.NoteConverterFromNotenpunkte;

/**
 * Diese Klasse ist ein Serialisierer für Noten. Sie serialisiert
 * einen Wert der Aufzählung {@link Note} in die Datenbankdarstellung als 
 * Notenpunkte, welche als Zahl dargestellt werden.
 */
public class NoteConverterFromNotenpunkteSerializer extends StdSerializer<Note> {

	private static final long serialVersionUID = -2517057436836075856L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public NoteConverterFromNotenpunkteSerializer() {
		super(Note.class);
	}

	/**
	 * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	public NoteConverterFromNotenpunkteSerializer(Class<Note> t) {
		super(t);
	}

	@Override
	public void serialize(Note value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(NoteConverterFromNotenpunkte.instance.convertToDatabaseColumn(value).toString());
	}

}
