package de.svws_nrw.davapi.data.carddav;

/**
 * Enum für die verschiedenen Adressbuecher, die dieses Repository abbildet
 */
public enum AdressbuchContactTypes {

	/** ein Adressbuch mit Schuelerdaten */
	SCHUELER("Schüler", "Ein generiertes Adressbuch mit allen Schülern"),

	/** ein Adressbuch mit Lehrerdaten */
	LEHRER("Lehrer", "Ein generiertes Adressbuch mit allen Lehrern"),

	/** ein Adressbuch mit Erzieherdaten */
	ERZIEHER("Erzieher", "Ein generiertes Adressbuch mit allen Erziehungsberechtigten"),

	/** ein persönliches Adressbuch */
	PERSOENLICH("Persönlich", "Ihr persönliches Adressbuch"),

	/** ein Adressbuch mit Daten von Personengruppen */
	PERSONENGRUPPEN("Personengruppen", "Ein generiertes Adressbuch mit Personengruppen"),

	/** ein öffentliches Adressbuch */
	OEFFENTLICH("Öffentlich", "Das öffentliche Adressbuch der Schule");


	/** der Anzeigename des Adressbuchs */
	public final String displayname;

	/** die Beschreibung des Adressbuchs */
	public final String beschreibung;


	/**
	 * erstellt eine neue Adressbuchkonstakte mit Anzeigename und Beschreibung
	 *
	 * @param displayname    der Anzeigename
	 * @param beschreibung   die Beschreibung
	 */
	AdressbuchContactTypes(final String displayname, final String beschreibung) {
		this.displayname = displayname;
		this.beschreibung = beschreibung;
	}

}
