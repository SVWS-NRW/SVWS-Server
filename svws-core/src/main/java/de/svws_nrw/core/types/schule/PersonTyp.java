package de.svws_nrw.core.types.schule;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse ist eine Aufzählung der zulässigen Personentypen für Einwilligungsarten.
 */
public enum PersonTyp {

	/** Ein Personentyp für Lehrer und weiteres Personal */
	LEHRER(1, "L", "Lehrer/Personal"),

	/** Ein Personentyp für Schüler */
	SCHUELER(2, "S", "Schüler"),

	/** Ein Personentyp für Erzieher */
	ERZIEHER(3, "E", "Erzieher");



	/** Die ID des Personentyps */
	public final int id;

	/** Das Kürzel des Personentyps. */
	public final @NotNull String kuerzel;

	/** Die textuelle Bezeichnung des Personentyps. */
	public final @NotNull String bezeichnung;


	/**
	 * Erzeugt einen neuen Personentyp für die Aufzählung.
	 *
	 * @param id                  die ID des Personentyps
	 * @param kuerzel             das Kürzel des Personentyps
	 * @param bezeichnung         die Bezeichnung des Personentyps
	 */
	PersonTyp(final int id, final @NotNull String kuerzel, final @NotNull String bezeichnung) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.bezeichnung = bezeichnung;
	}


	/**
	 * Gibt den Personentyp anhand der übergebenen ID zurück.
	 *
	 * @param id    die ID des Personentyps
	 *
	 * @return den Personentyp oder null, falls die ID fehlerhaft ist
	 */
	public static PersonTyp getByID(final int id) {
		switch (id) {
			case 1:
				return PersonTyp.LEHRER;
			case 2:
				return PersonTyp.SCHUELER;
			case 3:
				return PersonTyp.ERZIEHER;
			default:
				return null;
		}
	}

	/**
	 * Gibt den Personentyp anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel    das Kürzel des Personentyps
	 *
	 * @return den Personentyp oder null, falls das Kürzel fehlerhaft ist
	 */
	public static PersonTyp getByKuerzel(final String kuerzel) {
		switch (kuerzel) {
			case "L":
				return PersonTyp.LEHRER;
			case "S":
				return PersonTyp.SCHUELER;
			case "E":
				return PersonTyp.ERZIEHER;
			default:
				return null;
		}
	}
}
