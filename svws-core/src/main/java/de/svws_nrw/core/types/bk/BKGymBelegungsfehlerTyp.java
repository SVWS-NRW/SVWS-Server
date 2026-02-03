package de.svws_nrw.core.types.bk;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung beinhaltet die Fehlercodes, die bei einer Belegprüfung auftreten können.
 * Die Aufzählungsobjekte enthalten zusätzlich die Textnachrichten.
 */
public enum BKGymBelegungsfehlerTyp {

	/** BelegungsfehlerArt LK_1 | Parameter: Fachbezeichnung */
	LK_1("LK_1", BKGymBelegungsfehlerArt.BELEGUNG, 10, "Das Fach %s ist nicht der erste Leistungskurs."),

	/** BelegungsfehlerArt LK_2 | Parameter: Fachbezeichnung */
	LK_2("LK_2", BKGymBelegungsfehlerArt.BELEGUNG, 10, "Das Fach %s ist nicht der zweite Leistungskurs."),

	/** BelegungsfehlerArt LK_3 | Parameter: Fachbezeichnung */
	LK_3("LK_3", BKGymBelegungsfehlerArt.BELEGUNG, 10, "Das Grundkursfach %s ist als Leistungskurs markiert."),

	/** BelegungsfehlerArt AB_3 */
	AB_3("AB_3", BKGymBelegungsfehlerArt.BELEGUNG, 10, "Es muss ein drittes Abiturfach gewählt werden."),

	/** BelegungsfehlerArt AB_4 */
	AB_4("AB_4", BKGymBelegungsfehlerArt.BELEGUNG, 10, "Es muss ein viertes Abiturfach gewählt werden."),

	/** BelegungsfehlerArt AB_5 | Parameter: Fachkürzel von AB3, Fachkürzel von AB4, Gliederung, Fachklassenschlüssel*/
	AB_5("AB_5", BKGymBelegungsfehlerArt.BELEGUNG, 5, "Die gewählte Kombination aus 3. Abiturfach (%s) und 4. Abiturfach (%s) ist in dem Bildungsgang %s%s nicht zulässig."),

	/** BelegungsfehlerArt ST_1 | Parameter: Fachbezeichnung */
	ST_1("ST_1", BKGymBelegungsfehlerArt.BELEGUNG, 5, "Das Fach %s der Stundentafel wurde mehrfach belegt."),

	/** BelegungsfehlerArt ST_2 | Parameter: Fachbezeichnung, Halbjahr */
	ST_2("ST_2", BKGymBelegungsfehlerArt.BELEGUNG, 5, "Das Fach %s weist im Halbjahr %s keine Note auf, obwohl es belegt werden muss."),

	/** BelegungsfehlerArt ST_3 | Parameter: Fachbezeichnung */
	ST_3("ST_3", BKGymBelegungsfehlerArt.BELEGUNG, 5, "Das Fach %s der Stundentafel wurde nicht im nötigen Stundenumfang belegt."),

	/** BelegungsfehlerArt ST_4 | Parameter: Fachbezeichnung */
	ST_4("ST_4", BKGymBelegungsfehlerArt.BELEGUNG, 10, "Das Fach %s der Stundentafel wurde überhaupt nicht belegt."),

	/** BelegungsfehlerArt ST_5_INFO | Parameter: Fachbezeichnung */
	ST_5_INFO("ST_5_INFO", BKGymBelegungsfehlerArt.HINWEIS, 0, "Der Stundenumfang des Fachs %s ist in der Summe erfüllt, aber nicht in allen Halbjahren."),

	/** BelegungsfehlerArt ST_6 | Parameter: Fachbezeichnung, Halbjahr */
	ST_6("ST_6", BKGymBelegungsfehlerArt.BELEGUNG, 5, "Das Fach %s der Stundentafel wurde im Halbjahr %s nicht belegt."),

	/** BelegungsfehlerArt KL_1 Klausur | Parameter: Fachbezeichnung, Halbjahr */
	KL_1("KL_1", BKGymBelegungsfehlerArt.SCHRIFTLICHKEIT, 2, "Im Fach %s fehlt die Klausurbelegung im Halbjahr %s."),

	/** BelegungsfehlerArt KL_2 Klausur | Parameter: Fachbezeichnung, Halbjahr */
	KL_2("KL_2", BKGymBelegungsfehlerArt.SCHRIFTLICHKEIT, 2, "Im LK-Fach %s fehlt die Klausurbelegung im Halbjahr %s."),

	/** BelegungsfehlerArt KL_3 Klausur | Parameter: Fachbezeichnung, Halbjahr */
	KL_3("KL_3", BKGymBelegungsfehlerArt.SCHRIFTLICHKEIT, 2, "In der Fremdsprache %s fehlt die Klausurbelegung im Halbjahr %s."),

	/** BelegungsfehlerArt KL_4 Klausur | Parameter: Fachbezeichnung, Halbjahr */
	KL_4("KL_4", BKGymBelegungsfehlerArt.SCHRIFTLICHKEIT, 2, "Im Abiturfach %s fehlt die Klausurbelegung im Halbjahr %s."),

	/** BelegungsfehlerArt KL_1_INFO Klausur | Parameter: Fachbezeichnung, Halbjahr */
	KL_1_INFO("KL_1_INFO", BKGymBelegungsfehlerArt.SCHRIFTLICHKEIT, 0, "Hinweis: Im Fach %s fehlt die Klausurbelegung im Halbjahr %s."),

	/** BelegungsfehlerArt KL_3_INFO Klausur | Parameter: Fachbezeichnung, Halbjahr */
	KL_3_INFO("KL_3_INFO", BKGymBelegungsfehlerArt.SCHRIFTLICHKEIT, 0, "Hinweis: In der Fremdsprache %s fehlt die Klausurbelegung im Halbjahr %s."),

	/** BelegungsfehlerArt HJ_1_INFO Belegung | Parameter: Halbjahr */
	HJ_1_INFO("KL_3_INFO", BKGymBelegungsfehlerArt.BELEGUNG, 0, "Hinweis: Das Halbjahr %s ist nicht bewertet. Bedingungen müssen manuell geprüft werden.");


	/** Der eindeutige Code des Belegungsfehlers */
	public final @NotNull String code;

	/** Die Art des Fehlers */
	public final @NotNull BKGymBelegungsfehlerArt art;

	/** Der Wert des Fehlers höhere Werte gleich schwerwiegenderer Fehler */
	public final @NotNull Integer wert;

	/** Der Text des Fehlers, der ausgegeben wird */
	public final @NotNull String text;


	/**
	 * Erstellt einen neuen Belegungsfehler für die Aufzählung (s.o.). Dabei wird ein
	 * Text für die Prüfung angegeben.
	 *
	 * @param code   der eindeutige Code des Belegungsfehlers
	 * @param art    die Fehlerart (Belegungsfehler, Schriftlichkeit oder Information)
	 * @param wert   die Härte des Fehlers zur Beurteilung der Fehlerschwere
	 * @param text   der zugeordnete Text für die Gesamtbelegprüfung oder null
	 */
	BKGymBelegungsfehlerTyp(final @NotNull String code, final @NotNull BKGymBelegungsfehlerArt art, final @NotNull Integer wert, final @NotNull String text) {
		this.code = code;
		this.art = art;
		this.wert = wert;
		this.text = text;
	}


	/**
	 * Gibt zurück, ob es sich bei dem Belegungsfehler nur um eine Information
	 * und nicht um einen "echten" Fehler handelt.
	 *
	 * @return true, falls es sich nur um eine Information handelt, sonst false
	 */
	public boolean istInfo() {
		return (this.art == BKGymBelegungsfehlerArt.HINWEIS);
	}


	/**
	 * Gibt zurück, ob es sich bei dem Belegungsfehler um einen "echten" Fehler handelt
	 * und nicht nur um eine Information.
	 *
	 * @return true, falls es sich um einen "echten" Fehler handelt, sonst false
	 */
	public boolean istFehler() {
		return (this.art != BKGymBelegungsfehlerArt.HINWEIS);
	}


	/**
	 * Gibt die Art des Belegungsfehlers zurück.
	 *
	 * @return die Art des Belegungsfehlers
	 */
	public @NotNull BKGymBelegungsfehlerArt getArt() {
		return art;
	}


	/**
	 * Gibt den zugehörigen Wert für den Belegungsfehler zurück.
	 *
	 * @return der zugehörige Wert des Belegungsfehlers
	 */
	public @NotNull Integer getWert() {
		return wert;
	}


	/**
	 * Gibt den zugehörigen Text für den Belegungsfehler zurück.
	 *
	 * @return der zugehörige Text des Belegungsfehlers
	 */
	public @NotNull String getText() {
		return text;
	}


	@Override
	public @NotNull String toString() {
		return code;
	}


	/**
	 * Gibt den formatierten Text mit den angegebenen Parametern zurück.
	 *
	 * @param args   die Parameter für den String
	 *
	 * @return der Fehlertext mit den eingesetzten Parametern
	 */
	public @NotNull String format(final Object... args) {
		return String.format(this.text, args);
	}
}
