package de.svws_nrw.module.reporting.types.gost.kursplanung;

import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.gost.ReportingGostFachwahlstatistikHalbjahr;

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

	/** Die allgemeine Fachwahlstatistik der gymnasialen Oberstufe. */
	protected ReportingGostFachwahlstatistikHalbjahr reportingGostFachwahlstatistik;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param differenzKursgroessenLK Die Kursgrößendifferenz des Faches für die Kursart LK.
	 * @param differenzKursgroessenGK Die Kursgrößendifferenz des Faches für die Kursart GK.
	 * @param differenzKursgroessenZK Die Kursgrößendifferenz des Faches für die Kursart ZK.
	 * @param differenzKursgroessenPJK Die Kursgrößendifferenz des Faches für die Kursart PJK.
	 * @param differenzKursgroessenVTF Die Kursgrößendifferenz des Faches für die Kursart VTF.
	 * @param reportingGostFachwahlstatistik Die allgemeine Fachwahlstatistik der gymnasialen Oberstufe.
	 */
	public ReportingGostKursplanungFachwahlstatistik(final int differenzKursgroessenLK, final int differenzKursgroessenGK, final int differenzKursgroessenZK,
			final int differenzKursgroessenPJK, final int differenzKursgroessenVTF, final ReportingGostFachwahlstatistikHalbjahr reportingGostFachwahlstatistik) {
		this.differenzKursgroessenLK = differenzKursgroessenLK;
		this.differenzKursgroessenGK = differenzKursgroessenGK;
		this.differenzKursgroessenZK = differenzKursgroessenZK;
		this.differenzKursgroessenPJK = differenzKursgroessenPJK;
		this.differenzKursgroessenVTF = differenzKursgroessenVTF;
		this.reportingGostFachwahlstatistik = reportingGostFachwahlstatistik;
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
	 * Die allgemeine Fachwahlstatistik der gymnasialen Oberstufe.
	 *
	 * @return Inhalt des Feldes reportingGostFachwahlstatistik
	 */
	public ReportingGostFachwahlstatistikHalbjahr reportingGostFachwahlstatistik() {
		return reportingGostFachwahlstatistik;
	}

}
