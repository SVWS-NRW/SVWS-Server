package de.svws_nrw.db.schema;

/**
 * Diese Klasse beschreibt die unterschiedlichen Aktionen
 * für Fremdschlüssel-Aktionen bei ON DELETE und ON UPDATE,
 * welche in einem SVWS-Datenbankschema unterstützt werden.
 */
public enum SchemaFremdschluesselAktionen {

	/**
	 * Wenn der Schlüssel des Eltern-Datensatzes entfernt oder verändert
	 * wird, dann wird keine Aktion vorgenommen.
	 */
	NO_ACTION("NO ACTION"),

	/**
	 * Wenn der Schlüssel des Eltern-Datensatzes entfernt oder verändert
	 * wird, dann wird der Wert bei allen Datensätzen, die auf den Eltern-Datensatz
	 * verweisen auf NULL gesetzt.
	 */
	SET_NULL("SET NULL"),

	/**
	 * Wenn der Schlüssel des Eltern-Datensatzes entfernt oder verändert
	 * wird, dann wird der Wert bei allen Datensätzen, die auf den Eltern-Datensatz
	 * verweisen auf den Default-Wert gesetzt.
	 */
	SET_DEFAULT("SET DEFAULT"),

	/**
	 * Wenn der Schlüssel des Eltern-Datensatzes entfernt wird,
	 * dann werden alle zugehörigen Datensätze ebenfalls entfernt.
	 * Wenn der Schlüssel des Eltern-Datensatzes verändert wird,
	 * dann werden alle zugehörigen Datensätze entsprechend angepasst.
	 */
	CASCADE("CASCADE"),

	/**
	 * Wenn der Schlüssel des Eltern-Datensatzes entfernt werden soll,
	 * wird dies verhindert, solange es noch zugehörige Datensätze gibt.
	 */
	RESTRICT("RESTRICT");

	/** Der SQL-String für die Fremdschlüssel-Aktion */
	private final String _sql;

	/**
	 * Erstellt ein neues Element in der Aufzählung der Fremdschlüssel-Aktionen
	 *
	 * @param sql   der SQL-String für die Fremdschlüssel-Aktion
	 */
	SchemaFremdschluesselAktionen(final String sql) {
		this._sql = sql;
	}

	/**
	 * gibt den SQL-String für die Fremdschlüssel-Aktion zurück.
	 *
	 * @return der SQL-String für die Fremdschlüssel-Aktion
	 */
	public final String sql() {
		return this._sql;
	}

	/**
	 * Gibt die Fremdschlüssel-Aktion anhand des übergebenen SQL-Strings zurück.
	 *
	 * @param sql   der SQL-String
	 *
	 * @return die Fremdschlüssel-Aktion
	 *
	 * @throws IllegalArgumentException falls der String keine unterstützte
	 *                                  Fremdschlüssel-Aktion enthält
	 */
	public static SchemaFremdschluesselAktionen getBySQL(final String sql) throws IllegalArgumentException {
		return switch (sql.toUpperCase()) {
			case "NO ACTION" -> NO_ACTION;
			case "SET NULL" -> SET_NULL;
			case "SET DEFAULT" -> SET_DEFAULT;
			case "CASCADE" -> CASCADE;
			case "RESTRICT" -> RESTRICT;
			default -> throw new IllegalArgumentException("Unexpected value: " + sql.toUpperCase());
		};
	}

}
