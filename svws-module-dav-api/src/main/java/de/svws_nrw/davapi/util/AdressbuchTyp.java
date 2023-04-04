package de.svws_nrw.davapi.util;

/**
 * Aufzählung für die verschiedenen Adressbuchtypen.
 *
 */
public enum AdressbuchTyp {

	/** Persönliches Adressbuch eines Benutzers */
	PERSOENLICH(0, "Persönlich"),

	/** Ein generiertes (read-only) Adressbuch für berechtigte Personen */
	GENERIERT(1, "Generiert"),

	/** Ein globales Adressbuchtyp für berechtige Personen */
	OEFFENTLICH(2, "Öffentlich");

	/** Die ID des Adressbuchtyps */
	public final int id;

	/** Die textuelle Bezeichnung des Adressbuchtyps. */
	public final String bezeichnung;

	/**
	 * Erzeugt einen neuen Adressbuchtyp für die Aufzählung.
	 *
	 * @param id          die ID des Adressbuchtyps
	 * @param bezeichnung die Bezeichnung des Adressbuchtyps
	 */
	AdressbuchTyp(final int id, final String bezeichnung) {
		this.id = id;
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Gibt den Adressbuchtyp anhand der übergebenen ID zurück.
	 *
	 * @param id die ID der Adressbuchtyp
	 *
	 * @return die Adressbuchtyp oder null, falls die ID fehlerhaft ist
	 */
	public static AdressbuchTyp getByID(final int id) {
		switch (id) {
			case 0:
				return AdressbuchTyp.PERSOENLICH;
			case 1:
				return AdressbuchTyp.GENERIERT;
			case 2:
				return AdressbuchTyp.OEFFENTLICH;
			default:
				return null;
		}
	}
}
