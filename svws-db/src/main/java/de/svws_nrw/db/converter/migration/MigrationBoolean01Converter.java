package de.svws_nrw.db.converter.migration;

import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;

/**
 * Diese Klasse dient dem Konvertieren von Wahrheitswerten in Java (Boolean)
 * zu einer Zahldarstellung in der Datenbank, bei der die 0 false repräsentiert
 * und die  1 true.
 * Sie ist abgeleitet von der Basisklasse {@link DBAttributeConverter}, welche
 * die grundlegende Funktionalität von Konvertern zur Verfügung stellt. Dort muss
 * der Konverter auch in der Methode {@link DBAttributeConverter#getByClass}
 * registriert werden.
 */
@Converter
public final class MigrationBoolean01Converter extends DBAttributeConverter<Boolean, Integer> {

	/** Die Instanz des Konverters */
	public static final MigrationBoolean01Converter instance = new MigrationBoolean01Converter();

	@Override
	public Integer convertToDatabaseColumn(final Boolean value) {
		return ((value != null) && value) ? 1 : 0;
	}

	@Override
	public Boolean convertToEntityAttribute(final Integer dbData) {
		return (dbData != null) && (dbData != 0);
	}

	@Override
	public Class<Boolean> getResultType() {
		return Boolean.class;
	}

	@Override
	public Class<Integer> getDBType() {
		return Integer.class;
	}

}
