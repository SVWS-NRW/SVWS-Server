package de.svws_nrw.module.reporting.types.gost.kursplanung;

import de.svws_nrw.module.reporting.types.fach.ReportingFach;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungFachwahlstatistik.</p>
 * <p>Sie enthält die Daten zu den Fachwahlen eines Halbjahres der Kursplanung der gymnasialen Oberstufe.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostKursplanungFachwahlstatistik {

	/** Das Fach, für welches die Statistikdaten der Fachwahlen ermittelt wurden. */
	private ReportingFach fach;

	/** Die Anzahl der Wahlen als drittes Abiturfach. */
	private int wahlenAB3 = 0;

	/** Die Anzahl der Wahlen als viertes Abiturfach. */
	private int wahlenAB4 = 0;

	/** Die Gesamtzahl der Wahlen als Grundkurs, Projektkurs oder Vertiefungskurs. */
	private int wahlenGK = 0;

	/** Die Anzahl der Wahlen als mündlicher Grundkurs, Projektkurs oder Vertiefungskurs. */
	private int wahlenGKMuendlich = 0;

	/** Die Anzahl der Wahlen als schriftlicher Grundkurs. */
	private int wahlenGKSchriftlich = 0;

	/** Die Gesamtzahl der Wahlen als Leistungskurs. */
	private int wahlenLK = 0;

	/** Die Anzahl der Wahlen als Zusatzkurs. */
	private int wahlenZK = 0;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param fach Das Fach, für welches die Statistikdaten der Fachwahlen ermittelt wurden.
	 * @param wahlenAB3 Die Anzahl der Wahlen als drittes Abiturfach.
	 * @param wahlenAB4 Die Anzahl der Wahlen als viertes Abiturfach.
	 * @param wahlenGK Die Gesamtzahl der Wahlen als Grundkurs, Projektkurs oder Vertiefungskurs.
	 * @param wahlenGKMuendlich Die Anzahl der Wahlen als mündlicher Grundkurs, Projektkurs oder Vertiefungskurs.
	 * @param wahlenGKSchriftlich Die Anzahl der Wahlen als schriftlicher Grundkurs.
	 * @param wahlenLK Die Gesamtzahl der Wahlen als Leistungskurs.
	 * @param wahlenZK Die Anzahl der Wahlen als Zusatzkurs.
	 */
	public ReportingGostKursplanungFachwahlstatistik(final ReportingFach fach, final int wahlenAB3, final int wahlenAB4, final int wahlenGK, final int wahlenGKMuendlich, final int wahlenGKSchriftlich, final int wahlenLK, final int wahlenZK) {
		this.fach = fach;
		this.wahlenAB3 = wahlenAB3;
		this.wahlenAB4 = wahlenAB4;
		this.wahlenGK = wahlenGK;
		this.wahlenGKMuendlich = wahlenGKMuendlich;
		this.wahlenGKSchriftlich = wahlenGKSchriftlich;
		this.wahlenLK = wahlenLK;
		this.wahlenZK = wahlenZK;
	}



	// ##### Getter und Setter #####

	/**
	 * Das Fach, für welches die Statistikdaten der Fachwahlen ermittelt wurden.
	 * @return Inhalt des Feldes fach
	 */
	public  ReportingFach fach() {
		return fach;
	}

	/**
	 * Das Fach, für welches die Statistikdaten der Fachwahlen ermittelt wurden, wird gesetzt.
	 * @param fach Neuer Wert für das Feld fach
	 */
	public void setFach(final  ReportingFach fach) {
		this.fach = fach;
	}

	/**
	 * Die Anzahl der Wahlen als drittes Abiturfach.
	 * @return Inhalt des Feldes wahlenAB3
	 */
	public  int wahlenAB3() {
		return wahlenAB3;
	}

	/**
	 * Die Anzahl der Wahlen als drittes Abiturfach wird gesetzt.
	 * @param wahlenAB3 Neuer Wert für das Feld wahlenAB3
	 */
	public void setWahlenAB3(final  int wahlenAB3) {
		this.wahlenAB3 = wahlenAB3;
	}

	/**
	 * Die Anzahl der Wahlen als viertes Abiturfach.
	 * @return Inhalt des Feldes wahlenAB4
	 */
	public  int wahlenAB4() {
		return wahlenAB4;
	}

	/**
	 * Die Anzahl der Wahlen als viertes Abiturfach wird gesetzt.
	 * @param wahlenAB4 Neuer Wert für das Feld wahlenAB4
	 */
	public void setWahlenAB4(final  int wahlenAB4) {
		this.wahlenAB4 = wahlenAB4;
	}

	/**
	 * Die Gesamtzahl der Wahlen als Grundkurs, Projektkurs oder Vertiefungskurs.
	 * @return Inhalt des Feldes wahlenGK
	 */
	public  int wahlenGK() {
		return wahlenGK;
	}

	/**
	 * Die Gesamtzahl der Wahlen als Grundkurs, Projektkurs oder Vertiefungskurs wird gesetzt.
	 * @param wahlenGK Neuer Wert für das Feld wahlenGK
	 */
	public void setWahlenGK(final  int wahlenGK) {
		this.wahlenGK = wahlenGK;
	}

	/**
	 * Die Anzahl der Wahlen als mündlicher Grundkurs, Projektkurs oder Vertiefungskurs.
	 * @return Inhalt des Feldes wahlenGKMuendlich
	 */
	public  int wahlenGKMuendlich() {
		return wahlenGKMuendlich;
	}

	/**
	 * Die Anzahl der Wahlen als mündlicher Grundkurs, Projektkurs oder Vertiefungskurs wird gesetzt.
	 * @param wahlenGKMuendlich Neuer Wert für das Feld wahlenGKMuendlich
	 */
	public void setWahlenGKMuendlich(final  int wahlenGKMuendlich) {
		this.wahlenGKMuendlich = wahlenGKMuendlich;
	}

	/**
	 * Die Anzahl der Wahlen als schriftlicher Grundkurs.
	 * @return Inhalt des Feldes wahlenGKSchriftlich
	 */
	public  int wahlenGKSchriftlich() {
		return wahlenGKSchriftlich;
	}

	/**
	 * Die Anzahl der Wahlen als schriftlicher Grundkurs wird gesetzt.
	 * @param wahlenGKSchriftlich Neuer Wert für das Feld wahlenGKSchriftlich
	 */
	public void setWahlenGKSchriftlich(final  int wahlenGKSchriftlich) {
		this.wahlenGKSchriftlich = wahlenGKSchriftlich;
	}

	/**
	 * Die Gesamtzahl der Wahlen als Leistungskurs.
	 * @return Inhalt des Feldes wahlenLK
	 */
	public  int wahlenLK() {
		return wahlenLK;
	}

	/**
	 * Die Gesamtzahl der Wahlen als Leistungskurs wird gesetzt.
	 * @param wahlenLK Neuer Wert für das Feld wahlenLK
	 */
	public void setWahlenLK(final  int wahlenLK) {
		this.wahlenLK = wahlenLK;
	}

	/**
	 * Die Anzahl der Wahlen als Zusatzkurs.
	 * @return Inhalt des Feldes wahlenZK
	 */
	public  int wahlenZK() {
		return wahlenZK;
	}

	/**
	 * Die Anzahl der Wahlen als Zusatzkurs wird gesetzt.
	 * @param wahlenZK Neuer Wert für das Feld wahlenZK
	 */
	public void setWahlenZK(final  int wahlenZK) {
		this.wahlenZK = wahlenZK;
	}
}
