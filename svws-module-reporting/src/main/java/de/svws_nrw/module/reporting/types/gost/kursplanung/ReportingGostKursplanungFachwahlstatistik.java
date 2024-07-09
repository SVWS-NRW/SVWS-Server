package de.svws_nrw.module.reporting.types.gost.kursplanung;

import de.svws_nrw.module.reporting.types.fach.ReportingFach;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungFachwahlstatistik.</p>
 * <p>Sie enthält die Daten zu den Fachwahlen eines Halbjahres der Kursplanung der gymnasialen Oberstufe.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostKursplanungFachwahlstatistik {


	/** Die Kursgrößendifferenz des Faches für die Kursart LK. */
	private int differenzKursgroessenLK = 0;

	/** Die Kursgrößendifferenz des Faches für die Kursart GK. */
	private int differenzKursgroessenGK = 0;

	/** Die Kursgrößendifferenz des Faches für die Kursart ZK. */
	private int differenzKursgroessenZK = 0;

	/** Die Kursgrößendifferenz des Faches für die Kursart PJK. */
	private int differenzKursgroessenPJK = 0;

	/** Die Kursgrößendifferenz des Faches für die Kursart VTF. */
	private int differenzKursgroessenVTF = 0;

	/** Das Fach, für welches die Statistikdaten der Fachwahlen ermittelt wurden. */
	private ReportingFach fach;

	/** Die Gesamtzahl der Wahlen als Leistungskurs. */
	private int wahlenLK = 0;

	/** Die Gesamtzahl der Wahlen als Grundkurs, Projektkurs oder Vertiefungskurs. */
	private int wahlenGK = 0;

	/** Die Anzahl der Wahlen als mündlicher Grundkurs, Projektkurs oder Vertiefungskurs. */
	private int wahlenGKMuendlich = 0;

	/** Die Anzahl der Wahlen als schriftlicher Grundkurs. */
	private int wahlenGKSchriftlich = 0;

	/** Die Anzahl der Wahlen als drittes Abiturfach. */
	private int wahlenAB3 = 0;

	/** Die Anzahl der Wahlen als viertes Abiturfach. */
	private int wahlenAB4 = 0;

	/** Die Anzahl der Wahlen als Zusatzkurs. */
	private int wahlenZK = 0;

	/** Die Anzahl der Wahlen als Projektkurs. */
	private int wahlenPJK = 0;

	/** Die Anzahl der Wahlen als Vertiefungskurs. */
	private int wahlenVTF = 0;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param differenzKursgroessenLK Die Kursgrößendifferenz des Faches für die Kursart LK.
	 * @param differenzKursgroessenGK Die Kursgrößendifferenz des Faches für die Kursart GK.
	 * @param differenzKursgroessenZK Die Kursgrößendifferenz des Faches für die Kursart ZK.
	 * @param differenzKursgroessenPJK Die Kursgrößendifferenz des Faches für die Kursart PJK.
	 * @param differenzKursgroessenVTF Die Kursgrößendifferenz des Faches für die Kursart VTF.
	 * @param fach Das Fach, für welches die Statistikdaten der Fachwahlen ermittelt wurden.
	 * @param wahlenLK Die Gesamtzahl der Wahlen als Leistungskurs.
	 * @param wahlenGK Die Gesamtzahl der Wahlen als Grundkurs, Projektkurs oder Vertiefungskurs.
	 * @param wahlenGKMuendlich Die Anzahl der Wahlen als mündlicher Grundkurs, Projektkurs oder Vertiefungskurs.
	 * @param wahlenGKSchriftlich Die Anzahl der Wahlen als schriftlicher Grundkurs.
	 * @param wahlenAB3 Die Anzahl der Wahlen als drittes Abiturfach.
	 * @param wahlenAB4 Die Anzahl der Wahlen als viertes Abiturfach.
	 * @param wahlenZK Die Anzahl der Wahlen als Zusatzkurs.
	 * @param wahlenPJK Die Anzahl der Wahlen als Zusatzkurs.
	 * @param wahlenVTF Die Anzahl der Wahlen als Zusatzkurs.
	 */
	public ReportingGostKursplanungFachwahlstatistik(final int differenzKursgroessenLK, final int differenzKursgroessenGK, final int differenzKursgroessenZK,
			final int differenzKursgroessenPJK, final int differenzKursgroessenVTF, final ReportingFach fach, final int wahlenLK, final int wahlenGK,
			final int wahlenGKMuendlich, final int wahlenGKSchriftlich, final int wahlenAB3, final int wahlenAB4, final int wahlenZK, final int wahlenPJK,
			final int wahlenVTF) {
		this.differenzKursgroessenLK = differenzKursgroessenLK;
		this.differenzKursgroessenGK = differenzKursgroessenGK;
		this.differenzKursgroessenZK = differenzKursgroessenZK;
		this.differenzKursgroessenPJK = differenzKursgroessenPJK;
		this.differenzKursgroessenVTF = differenzKursgroessenVTF;
		this.fach = fach;
		this.wahlenLK = wahlenLK;
		this.wahlenGK = wahlenGK;
		this.wahlenGKMuendlich = wahlenGKMuendlich;
		this.wahlenGKSchriftlich = wahlenGKSchriftlich;
		this.wahlenAB3 = wahlenAB3;
		this.wahlenAB4 = wahlenAB4;
		this.wahlenZK = wahlenZK;
		this.wahlenPJK = wahlenPJK;
		this.wahlenVTF = wahlenVTF;
	}



	// ##### Getter und Setter #####

	/**
	 * Die Kursgrößendifferenz des Faches für die Kursart LK.
	 * @return Inhalt des Feldes differenzKursgroessenLK
	 */
	public int differenzKursgroessenLK() {
		return differenzKursgroessenLK;
	}

	/**
	 * Die Kursgrößendifferenz des Faches für die Kursart LK wird gesetzt.
	 * @param differenzKursgroessenLK Wert für das Feld differenzKursgroessenLK
	 */
	public void setDifferenzKursgroessenLK(final int differenzKursgroessenLK) {
		this.differenzKursgroessenLK = differenzKursgroessenLK;
	}

	/**
	 * Die Kursgrößendifferenz des Faches für die Kursart GK.
	 * @return Inhalt des Feldes differenzKursgroessenGK
	 */
	public int differenzKursgroessenGK() {
		return differenzKursgroessenGK;
	}

	/**
	 * Die Kursgrößendifferenz des Faches für die Kursart GK wird gesetzt.
	 * @param differenzKursgroessenGK Wert für das Feld differenzKursgroessenGK
	 */
	public void setDifferenzKursgroessenGK(final int differenzKursgroessenGK) {
		this.differenzKursgroessenGK = differenzKursgroessenGK;
	}

	/**
	 * Die Kursgrößendifferenz des Faches für die Kursart ZK.
	 * @return Inhalt des Feldes differenzKursgroessenZK
	 */
	public int differenzKursgroessenZK() {
		return differenzKursgroessenZK;
	}

	/**
	 * Die Kursgrößendifferenz des Faches für die Kursart ZK wird gesetzt.
	 * @param differenzKursgroessenZK Wert für das Feld differenzKursgroessenZK
	 */
	public void setDifferenzKursgroessenZK(final int differenzKursgroessenZK) {
		this.differenzKursgroessenZK = differenzKursgroessenZK;
	}

	/**
	 * Die Kursgrößendifferenz des Faches für die Kursart PJK.
	 * @return Inhalt des Feldes differenzKursgroessenPJK
	 */
	public int differenzKursgroessenPJK() {
		return differenzKursgroessenPJK;
	}

	/**
	 * Die Kursgrößendifferenz des Faches für die Kursart PJK wird gesetzt.
	 * @param differenzKursgroessenPJK Wert für das Feld differenzKursgroessenPJK
	 */
	public void setDifferenzKursgroessenPJK(final int differenzKursgroessenPJK) {
		this.differenzKursgroessenPJK = differenzKursgroessenPJK;
	}

	/**
	 * Die Kursgrößendifferenz des Faches für die Kursart VTF.
	 * @return Inhalt des Feldes differenzKursgroessenVTF
	 */
	public int differenzKursgroessenVTF() {
		return differenzKursgroessenVTF;
	}

	/**
	 * Die Kursgrößendifferenz des Faches für die Kursart VTF wird gesetzt.
	 * @param differenzKursgroessenVTF Wert für das Feld differenzKursgroessenVTF
	 */
	public void setDifferenzKursgroessenVTF(final int differenzKursgroessenVTF) {
		this.differenzKursgroessenVTF = differenzKursgroessenVTF;
	}


	/**
	 * Das Fach, für welches die Statistikdaten der Fachwahlen ermittelt wurden.
	 * @return Inhalt des Feldes fach
	 */
	public ReportingFach fach() {
		return fach;
	}

	/**
	 * Das Fach, für welches die Statistikdaten der Fachwahlen ermittelt wurden, wird gesetzt.
	 * @param fach Neuer Wert für das Feld fach
	 */
	public void setFach(final ReportingFach fach) {
		this.fach = fach;
	}

	/**
	 * Die Gesamtzahl der Wahlen als Leistungskurs.
	 * @return Inhalt des Feldes wahlenLK
	 */
	public int wahlenLK() {
		return wahlenLK;
	}

	/**
	 * Die Gesamtzahl der Wahlen als Leistungskurs wird gesetzt.
	 * @param wahlenLK Neuer Wert für das Feld wahlenLK
	 */
	public void setWahlenLK(final int wahlenLK) {
		this.wahlenLK = wahlenLK;
	}

	/**
	 * Die Gesamtzahl der Wahlen als Grundkurs, Projektkurs oder Vertiefungskurs.
	 * @return Inhalt des Feldes wahlenGK
	 */
	public int wahlenGK() {
		return wahlenGK;
	}

	/**
	 * Die Gesamtzahl der Wahlen als Grundkurs, Projektkurs oder Vertiefungskurs wird gesetzt.
	 * @param wahlenGK Neuer Wert für das Feld wahlenGK
	 */
	public void setWahlenGK(final int wahlenGK) {
		this.wahlenGK = wahlenGK;
	}

	/**
	 * Die Anzahl der Wahlen als mündlicher Grundkurs, Projektkurs oder Vertiefungskurs.
	 * @return Inhalt des Feldes wahlenGKMuendlich
	 */
	public int wahlenGKMuendlich() {
		return wahlenGKMuendlich;
	}

	/**
	 * Die Anzahl der Wahlen als mündlicher Grundkurs, Projektkurs oder Vertiefungskurs wird gesetzt.
	 * @param wahlenGKMuendlich Neuer Wert für das Feld wahlenGKMuendlich
	 */
	public void setWahlenGKMuendlich(final int wahlenGKMuendlich) {
		this.wahlenGKMuendlich = wahlenGKMuendlich;
	}

	/**
	 * Die Anzahl der Wahlen als schriftlicher Grundkurs.
	 * @return Inhalt des Feldes wahlenGKSchriftlich
	 */
	public int wahlenGKSchriftlich() {
		return wahlenGKSchriftlich;
	}

	/**
	 * Die Anzahl der Wahlen als schriftlicher Grundkurs wird gesetzt.
	 * @param wahlenGKSchriftlich Neuer Wert für das Feld wahlenGKSchriftlich
	 */
	public void setWahlenGKSchriftlich(final int wahlenGKSchriftlich) {
		this.wahlenGKSchriftlich = wahlenGKSchriftlich;
	}

	/**
	 * Die Anzahl der Wahlen als drittes Abiturfach.
	 * @return Inhalt des Feldes wahlenAB3
	 */
	public int wahlenAB3() {
		return wahlenAB3;
	}

	/**
	 * Die Anzahl der Wahlen als drittes Abiturfach wird gesetzt.
	 * @param wahlenAB3 Neuer Wert für das Feld wahlenAB3
	 */
	public void setWahlenAB3(final int wahlenAB3) {
		this.wahlenAB3 = wahlenAB3;
	}

	/**
	 * Die Anzahl der Wahlen als viertes Abiturfach.
	 * @return Inhalt des Feldes wahlenAB4
	 */
	public int wahlenAB4() {
		return wahlenAB4;
	}

	/**
	 * Die Anzahl der Wahlen als viertes Abiturfach wird gesetzt.
	 * @param wahlenAB4 Neuer Wert für das Feld wahlenAB4
	 */
	public void setWahlenAB4(final int wahlenAB4) {
		this.wahlenAB4 = wahlenAB4;
	}

	/**
	 * Die Anzahl der Wahlen als Zusatzkurs.
	 * @return Inhalt des Feldes wahlenZK
	 */
	public int wahlenZK() {
		return wahlenZK;
	}

	/**
	 * Die Anzahl der Wahlen als Zusatzkurs wird gesetzt.
	 * @param wahlenZK Neuer Wert für das Feld wahlenZK
	 */
	public void setWahlenZK(final int wahlenZK) {
		this.wahlenZK = wahlenZK;
	}

	/**
	 * Die Anzahl der Wahlen als Zusatzkurs.
	 * @return Inhalt des Feldes wahlenPJK
	 */
	public int wahlenPJK() {
		return wahlenPJK;
	}

	/**
	 * Die Anzahl der Wahlen als Zusatzkurs wird gesetzt.
	 * @param wahlenPJK Neuer Wert für das Feld wahlenPJK
	 */
	public void setWahlenPJK(final int wahlenPJK) {
		this.wahlenPJK = wahlenPJK;
	}

	/**
	 * Die Anzahl der Wahlen als Zusatzkurs.
	 * @return Inhalt des Feldes wahlenVTF
	 */
	public int wahlenVTF() {
		return wahlenVTF;
	}

	/**
	 * Die Anzahl der Wahlen als Zusatzkurs wird gesetzt.
	 * @param wahlenVTF Neuer Wert für das Feld wahlenVTF
	 */
	public void setWahlenVTF(final int wahlenVTF) {
		this.wahlenVTF = wahlenVTF;
	}
}
