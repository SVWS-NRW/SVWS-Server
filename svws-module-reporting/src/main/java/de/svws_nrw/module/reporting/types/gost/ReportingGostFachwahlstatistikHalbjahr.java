package de.svws_nrw.module.reporting.types.gost;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostFachwahlstatistikHalbjahr.
 */
public class ReportingGostFachwahlstatistikHalbjahr extends ReportingBaseType {

	/** Der Abiturjahrgang in Form des Abiturjahres, dessen Fachwahlstatistik betrachtet werden soll. */
	protected int abiturjahr;

	/** Das Fach, für welches die Statistikdaten der Fachwahlen ermittelt wurden. */
	protected ReportingFach fach;

	/** Das Halbjahr der GOSt, dem diese Fachwahlstatistik zugeordnet ist. */
	protected GostHalbjahr gostHalbjahr;

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
	 * @param abiturjahr Der Abiturjahrgang in Form des Abiturjahres, dessen Fachwahlstatistik betrachtet werden soll.
	 * @param fach Das Fach, für welches die Statistikdaten der Fachwahlen ermittelt wurden.
	 * @param gostHalbjahr Das Halbjahr der GOSt, dem diese Fachwahlstatistik zugeordnet ist.
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
	public ReportingGostFachwahlstatistikHalbjahr(final int abiturjahr, final ReportingFach fach, final GostHalbjahr gostHalbjahr, final int wahlenLK,
			final int wahlenGK, final int wahlenGKMuendlich, final int wahlenGKSchriftlich, final int wahlenAB3, final int wahlenAB4, final int wahlenZK,
			final int wahlenPJK, final int wahlenVTF) {
		this.abiturjahr = abiturjahr;
		this.fach = fach;
		this.gostHalbjahr = gostHalbjahr;
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
	 * Gibt das Abiturjahr zurück, dem diese Fachwahlstatistik zugeordnet ist.
	 *
	 * @return Das Abiturjahr zur Fachwahlstatistik.
	 */
	public int abiturjahr() {
		return abiturjahr;
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
	 * Gibt das Gost-Halbjahr zurück, dem diese Fachwahlstatistik zugeordnet ist.
	 *
	 * @return Das Gost-Halbjahr zur Fachwahlstatistik.
	 */
	public GostHalbjahr gostHalbjahr() {
		return gostHalbjahr;
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
