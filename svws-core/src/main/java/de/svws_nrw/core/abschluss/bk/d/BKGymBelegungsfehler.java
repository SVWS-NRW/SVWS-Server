package de.svws_nrw.core.abschluss.bk.d;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung beinhaltet die Fehlercodes, die bei einer Belegprüfung auftreten können.
 * Die Aufzählungsobjekte enthalten zusätzlich die Textnachrichten.
 */
public enum BKGymBelegungsfehler {

	/** BelegungsfehlerArt ABI_11 */
	ABI_11("ABI_11", BKGymBelegungsfehlerArt.BELEGUNG, "Religionslehre und Sport dürfen nicht gleichzeitig Abiturfächer sein."),

	/** BelegungsfehlerArt ABI_12 */
	ABI_12("ABI_12", BKGymBelegungsfehlerArt.SCHRIFTLICHKEIT, "In Q2.2 muss das 3. Abiturfach schriftlich belegt sein."),

	/** BelegungsfehlerArt 	ABI_13 */
	ABI_13("ABI_13", BKGymBelegungsfehlerArt.SCHRIFTLICHKEIT, "In Q2.2 muss das 4. Abiturfach mündlich belegt sein.");


	/** Der eindeutige Code des Belegungsfehlers */
	public final @NotNull String code;

	/** Die Art des Fehlers */
	public final @NotNull BKGymBelegungsfehlerArt art;

	/** Der Text des Fehlers, der ausgegeben wird */
	public final @NotNull String text;


	/**
	 * Erstellt einen neuen Belegungsfehler für die Aufzählung (s.o.). Dabei wird ein
	 * Text für die Prüfung angegeben.
	 *
	 * @param code   der eindeutige Code des Belegungsfehlers
	 * @param art    die Fehlerart (Belegungsfehler, Schriftlichkeit oder Information)
	 * @param text   der zugeordnete Text für die Gesamtbelegprüfung oder null
	 */
	BKGymBelegungsfehler(final @NotNull String code, final @NotNull BKGymBelegungsfehlerArt art, final @NotNull String text) {
		this.code = code;
		this.art = art;
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

}
