package de.svws_nrw.core.types.gost;

import jakarta.validation.constraints.NotNull;

/*
 * Bei dieser Implementierung handelt es sich um eine Umsetzung in Bezug auf möglichen zukünftigen
 * Änderungen in der APO-GOSt. Diese basiert auf der aktuellen Implementierung und integriert Aspekte
 * aus dem Eckpunktepapier und auf in den Schulleiterdienstbesprechungen erläuterten Vorhaben.
 * Sie dient der Evaluierung von möglichen Umsetzungsvarianten und als Vorbereitung einer späteren
 * Implementierung der Belegprüfung. Insbesondere sollen erste Versuche mit Laufbahnen mit einem
 * 5. Abiturfach und Projektkursen erprobt werden. Detailaspekte können erst nach Erscheinen der APO-GOSt
 * umgesetzt werden.
 * Es handelt sich also um experimentellen Code, der keine Rückschlüsse auf Details einer zukünftigen APO-GOSt
 * erlaubt.
 */
/**
 * Diese Klasse stellt die Core-Types als Aufzählung für die Abiturfacharten zur Verfügung.
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum GostAbiturFach {

	/** 1. Leistungskurs = LK1 */
	LK1(1, "LK1", "1. Leistungskurs"),

	/** 2. Leistungskurs = LK2 */
	LK2(2, "LK2", "2. Leistungskurs"),

	/** 3. Abiturfach (GK, schriftlich in der Abiturprüfung) = AB3 */
	AB3(3, "AB3", "3. Abiturfach (GK, schriftlich)"),

	/** 4. Abiturfach (GK, mündlich in der Abiturprüfung) = AB4 */
	AB4(4, "AB4", "4. Abiturfach (GK, mündlich)"),

	/** 5. Abiturfach (GK/PJK/BLL)*/
	AB5(5, "AB5", "5. Abiturfach (GK/PJK/BLL)");


	/** Die ID bzw. Nummer der Abiturfachart (1-5) */
	public final int id;

	/** Das Kürzel der Abiturfachart, welches auch in speziellen Kursarten genutzt wird. */
	public final @NotNull String kuerzel;

	/** Die textuelle Beschreibung der Abiturfachart. */
	public final @NotNull String beschreibung;


	/**
	 * Erstellt eine Abiturfachart für diese Aufzählung der Abiturfacharten.
	 *
	 * @param id             die ID bzw. Nummer der Abiturfachart
	 * @param kuerzel        das Kürzel der Abiturfachart, welches auch in speziellen Kursarten genutzt wird
	 * @param beschreibung   die textuelle Beschreibung der Abiturfachart
	 */
	GostAbiturFach(final int id, final @NotNull String kuerzel, final @NotNull String beschreibung) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
	}


	/**
	 * Gibt die Abiturfachart anhand der ID zurück.
	 *
	 * @param id    die ID der Abiturfachart
	 *
	 * @return die Abiturfachart oder null falls die ID ungültig ist
	 * */
	public static GostAbiturFach fromID(final Integer id) {
		if (id == null)
			return null;
		switch (id) {
			case 1:
				return LK1;
			case 2:
				return LK2;
			case 3:
				return AB3;
			case 4:
				return AB4;
			case 5:
				return AB5;
			default:
				return null;
		}
	}


	/**
	 * Gibt die Abiturfachart anhand der ID (als String) zurück.
	 *
	 * @param strID    die ID der Abiturfachart (als String)
	 *
	 * @return die Abiturfachart oder null, falls die ID ungültig ist
	 * */
	public static GostAbiturFach fromIDString(final String strID) {
		if (strID == null)
			return null;
		switch (strID) {
			case "1":
				return LK1;
			case "2":
				return LK2;
			case "3":
				return AB3;
			case "4":
				return AB4;
			case "5":
				return AB5;
			default:
				return null;
		}
	}


	/**
	 * Gibt die Abiturfachart anhand des Kürzels zurück.
	 *
	 * @param kuerzel    das Kürzel der Abiturfachart
	 *
	 * @return die Abiturfachart oder null, falls das Kürzel ungültig ist
	 * */
	public static GostAbiturFach fromKuerzel(final String kuerzel) {
		switch (kuerzel) {
			case "LK1":
				return LK1;
			case "LK2":
				return LK2;
			case "AB3":
				return AB3;
			case "AB4":
				return AB4;
			case "AB5":
				return AB5;
			default:
				return null;
		}
	}


	@Override
	public @NotNull String toString() {
		return kuerzel;
	}

}
