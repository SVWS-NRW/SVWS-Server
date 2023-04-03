package de.svws_nrw.db.converter.current;

import jakarta.persistence.Converter;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.db.converter.DBAttributeConverter;



/**
 * Diese Klasse dient dem Konvertieren vom Notenkürzel zu der Aufzählung {@link Note}.
 */
@Converter(autoApply = true)
public final class NoteConverterFromKuerzel extends DBAttributeConverter<Note, String> {

	/** Die Instanz des Konverters */
	public static final NoteConverterFromKuerzel instance = new NoteConverterFromKuerzel();

	@Override
	public String convertToDatabaseColumn(final Note note) {
		if (note == null)
			return null;
		final String s = note.kuerzel;
		if ((s != null) && "".contentEquals(s))
			return null;
		return note.kuerzel;
	}

	@Override
	public Note convertToEntityAttribute(final String dbData) {
		return Note.fromKuerzel(dbData);
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
