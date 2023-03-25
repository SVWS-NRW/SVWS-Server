package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.Note;
import de.svws_nrw.db.converter.current.NoteConverterFromNotenpunkteString;

/**
 * Diese Klasse ist ein Deserialisierer für Noten. Sie deserialisiert
 * die Datenbankdarstellung als Notenpunkte, welche in einer Zeichenkette dargestellt 
 * werden, in einen Wert der Aufzählung {@link Note}.
 */
public class NoteConverterFromNotenpunkteStringDeserializer extends StdDeserializer<Note> {

	private static final long serialVersionUID = 5891746067532824732L;

	/**
	 * Erzeugt einen neuen Deerialisierer
	 */
	public NoteConverterFromNotenpunkteStringDeserializer() {
		super(Note.class);
	}
	
	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 * 
	 * @param t   das Klassen-Objekt
	 */
	protected NoteConverterFromNotenpunkteStringDeserializer(Class<Note> t) {
		super(t);
	}

	@Override
	public Note deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return NoteConverterFromNotenpunkteString.instance.convertToEntityAttribute(p.getText());
	}
	
}
