package de.svws_nrw.db.converter.current;

import java.util.Locale;

import jakarta.persistence.Converter;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.db.converter.DBAttributeConverter;



/**
 * Diese Klasse dient dem Konvertieren Notentext zur Note.
 */
@Converter(autoApply = true)
public class NoteConverterFromNotenpunkteString extends DBAttributeConverter<Note, String> {

	/** Die Instanz des Konverters */
	public final static NoteConverterFromNotenpunkteString instance = new NoteConverterFromNotenpunkteString();
	
	@Override
	public String convertToDatabaseColumn(Note note) {
		if (note == null)
			return null;
		if (note == Note.KEINE)
			return null;
		if (note == Note.ATTEST)
			return "AT";
		if (note.notenpunkte == null)
			return note.kuerzel;
		return String.format((Locale) null, "%02d", note.notenpunkte);
	}

	@Override
	public Note convertToEntityAttribute(String dbData) {
		if (dbData == null)
			return Note.KEINE;
		try {			
			return Note.fromNotenpunkte(Integer.parseInt(dbData));
		} catch(@SuppressWarnings("unused") NumberFormatException e) {
			return Note.fromKuerzel(dbData);
		}
	}

	@Override
	public Class<Note> getResultType() {
		return Note.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}