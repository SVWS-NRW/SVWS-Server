package de.svws_nrw.module.reporting.types.gost.kursplanung;

import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungFachwahlstatistik.
 */
public class ReportingGostKursplanungFachwahlstatistik extends ReportingBaseType {

	/** Die Kursgrößendifferenz des Faches für die Kursart LK. */
	protected int differenzKursgroessenLK;

	/** Die Kursgrößendifferenz des Faches für die Kursart GK. */
	protected int differenzKursgroessenGK;

	/** Die Kursgrößendifferenz des Faches für die Kursart ZK. */
	protected int differenzKursgroessenZK;

	/** Die Kursgrößendifferenz des Faches für die Kursart PJK. */
	protected int differenzKursgroessenPJK;

	/** Die Kursgrößendifferenz des Faches für die Kursart VTF. */
	protected int differenzKursgroessenVTF;

	/** Das Fach, für welches die Statistikdaten der Fachwahlen ermittelt wurden. */
	protected ReportingFach fach;

	/** Die Gesamtzahl der Wahlen als Leistungskurs. */
	protected int wahlenLK;

	/** Die Gesamtzahl der Wahlen als Grundkurs, Projektkurs oder Vertiefungskurs. */
	protected int wahlenGK;

	/** Die Anzahl der Wahlen als mündlicher Grundkurs, Projektkurs oder Vertiefungskurs. */
	protected int wahlenGKMuendlich;

	/** Die Anzahl der Wahlen als schriftlicher Grundkurs. */
	protected int wahlenGKSchriftlich;

	/** Die Anzahl der Wahlen als drittes Abiturfach. */
	protected int wahlenAB3;

	/** Die Anzahl der Wahlen als viertes Abiturfach. */
	protected int wahlenAB4;

	/** Die Anzahl der Wahlen als Zusatzkurs. */
	protected int wahlenZK;

	/** Die Anzahl der Wahlen als Projektkurs. */
	protected int wahlenPJK;

	/** Die Anzahl der Wahlen als Vertiefungskurs. */
	protected int wahlenVTF;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
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



	// ##### Getter #####

	/**
	 * Die Kursgrößendifferenz des Faches für die Kursart LK.
	 *
	 * @return Inhalt des Feldes differenzKursgroessenLK
	 */
	public int differenzKursgroessenLK() {
		return differenzKursgroessenLK;
	}

	/**
	 * Die Kursgrößendifferenz des Faches für die Kursart GK.
	 *
	 * @return Inhalt des Feldes differenzKursgroessenGK
	 */
	public int differenzKursgroessenGK() {
		return differenzKursgroessenGK;
	}

	/**
	 * Die Kursgrößendifferenz des Faches für die Kursart ZK.
	 *
	 * @return Inhalt des Feldes differenzKursgroessenZK
	 */
	public int differenzKursgroessenZK() {
		return differenzKursgroessenZK;
	}

	/**
	 * Die Kursgrößendifferenz des Faches für die Kursart PJK.
	 *
	 * @return Inhalt des Feldes differenzKursgroessenPJK
	 */
	public int differenzKursgroessenPJK() {
		return differenzKursgroessenPJK;
	}

	/**
	 * Die Kursgrößendifferenz des Faches für die Kursart VTF.
	 *
	 * @return Inhalt des Feldes differenzKursgroessenVTF
	 */
	public int differenzKursgroessenVTF() {
		return differenzKursgroessenVTF;
	}


	/**
	 * Das Fach, für welches die Statistikdaten der Fachwahlen ermittelt wurden.
	 *
	 * @return Inhalt des Feldes fach
	 */
	public ReportingFach fach() {
		return fach;
	}

	/**
	 * Die Gesamtzahl der Wahlen als Leistungskurs.
	 *
	 * @return Inhalt des Feldes wahlenLK
	 */
	public int wahlenLK() {
		return wahlenLK;
	}

	/**
	 * Die Gesamtzahl der Wahlen als Grundkurs, Projektkurs oder Vertiefungskurs.
	 *
	 * @return Inhalt des Feldes wahlenGK
	 */
	public int wahlenGK() {
		return wahlenGK;
	}

	/**
	 * Die Anzahl der Wahlen als mündlicher Grundkurs, Projektkurs oder Vertiefungskurs.
	 *
	 * @return Inhalt des Feldes wahlenGKMuendlich
	 */
	public int wahlenGKMuendlich() {
		return wahlenGKMuendlich;
	}

	/**
	 * Die Anzahl der Wahlen als schriftlicher Grundkurs.
	 *
	 * @return Inhalt des Feldes wahlenGKSchriftlich
	 */
	public int wahlenGKSchriftlich() {
		return wahlenGKSchriftlich;
	}

	/**
	 * Die Anzahl der Wahlen als drittes Abiturfach.
	 *
	 * @return Inhalt des Feldes wahlenAB3
	 */
	public int wahlenAB3() {
		return wahlenAB3;
	}

	/**
	 * Die Anzahl der Wahlen als viertes Abiturfach.
	 *
	 * @return Inhalt des Feldes wahlenAB4
	 */
	public int wahlenAB4() {
		return wahlenAB4;
	}

	/**
	 * Die Anzahl der Wahlen als Zusatzkurs.
	 *
	 * @return Inhalt des Feldes wahlenZK
	 */
	public int wahlenZK() {
		return wahlenZK;
	}

	/**
	 * Die Anzahl der Wahlen als Zusatzkurs.
	 *
	 * @return Inhalt des Feldes wahlenPJK
	 */
	public int wahlenPJK() {
		return wahlenPJK;
	}

	/**
	 * Die Anzahl der Wahlen als Zusatzkurs.
	 *
	 * @return Inhalt des Feldes wahlenVTF
	 */
	public int wahlenVTF() {
		return wahlenVTF;
	}

}
