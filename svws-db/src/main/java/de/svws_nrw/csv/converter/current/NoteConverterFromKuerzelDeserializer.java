package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.Note;
import de.svws_nrw.db.converter.current.NoteConverterFromKuerzel;

/**
 * Diese Klasse ist ein Deserialisierer für Noten. Sie deserialisiert
 * die Datenbankdarstellung als Notenkuerzel, welche als Zeichenkette 
 * dargestellt werden, in einen Wert der Aufzählung {@link Note}.
 */
public class NoteConverterFromKuerzelDeserializer extends StdDeserializer<Note> {

	private static final long serialVersionUID = 7505421606549933149L;

	/**
	 * Erzeugt einen neuen Deerialisierer
	 */
	public NoteConverterFromKuerzelDeserializer() {
		super(Note.class);
	}
	
	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	protected NoteConverterFromKuerzelDeserializer(Class<Note> t) {
		super(t);
	}

	@Override
	public Note deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return NoteConverterFromKuerzel.instance.convertToEntityAttribute(p.getText());
	}
	
}
