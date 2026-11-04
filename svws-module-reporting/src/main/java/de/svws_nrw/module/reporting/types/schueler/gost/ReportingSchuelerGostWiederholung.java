package de.svws_nrw.module.reporting.types.schueler.gost;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.module.reporting.types.ReportingBaseType;

/**
 * Basis-Klasse im Rahmen des Reportings für die Wiederholung zweier GOSt-Halbjahre im folgenden Abiturjahrgang. Vorlagen können damit
 * Wiederholer kennzeichnen, etwa mit den beiden Abiturjahrgängen. Nach dem vollzogenen Wechsel gilt die Laufbahn wie jede andere, dann
 * liefert der Schüler kein Objekt dieser Klasse mehr.
 */
public class ReportingSchuelerGostWiederholung extends ReportingBaseType {

	/** Der Abiturjahrgang laut aktuellem Lernabschnitt. */
	protected Integer abiturjahrgangBisher;

	/** Der Abiturjahrgang der Wiederholung. */
	protected int abiturjahrgangNeu;

	/** Das Halbjahr, mit dem der Schüler im neuen Abiturjahrgang einsteigt. */
	protected GostHalbjahr einstiegshalbjahr;

	/** Gibt an, ob die Schule das Einstiegshalbjahr erreicht hat, der Lernabschnitt aber nicht zum neuen Abiturjahrgang passt. */
	protected boolean wechselNichtVollzogen;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param abiturjahrgangBisher    der Abiturjahrgang laut aktuellem Lernabschnitt oder null, wenn er nicht bestimmt werden kann
	 * @param abiturjahrgangNeu       der Abiturjahrgang der Wiederholung
	 * @param einstiegshalbjahr       das Halbjahr, mit dem der Schüler im neuen Abiturjahrgang einsteigt
	 * @param wechselNichtVollzogen   true, wenn die Schule das Einstiegshalbjahr erreicht hat, der Lernabschnitt aber nicht passt
	 */
	public ReportingSchuelerGostWiederholung(final Integer abiturjahrgangBisher, final int abiturjahrgangNeu, final GostHalbjahr einstiegshalbjahr,
			final boolean wechselNichtVollzogen) {
		this.abiturjahrgangBisher = abiturjahrgangBisher;
		this.abiturjahrgangNeu = abiturjahrgangNeu;
		this.einstiegshalbjahr = einstiegshalbjahr;
		this.wechselNichtVollzogen = wechselNichtVollzogen;
	}


	// ##### Getter #####

	/**
	 * Der Abiturjahrgang laut aktuellem Lernabschnitt.
	 *
	 * @return Inhalt des Feldes abiturjahrgangBisher; kann {@code null} sein, wenn der Jahrgang aus dem Lernabschnitt nicht bestimmt werden kann.
	 */
	public Integer abiturjahrgangBisher() {
		return abiturjahrgangBisher;
	}

	/**
	 * Der Abiturjahrgang der Wiederholung.
	 *
	 * @return Inhalt des Feldes abiturjahrgangNeu
	 */
	public int abiturjahrgangNeu() {
		return abiturjahrgangNeu;
	}

	/**
	 * Das Halbjahr, mit dem der Schüler im neuen Abiturjahrgang einsteigt.
	 *
	 * @return Inhalt des Feldes einstiegshalbjahr
	 */
	public GostHalbjahr einstiegshalbjahr() {
		return einstiegshalbjahr;
	}

	/**
	 * Gibt an, ob die Schule das Einstiegshalbjahr erreicht hat, der Lernabschnitt aber nicht zum neuen Abiturjahrgang passt.
	 *
	 * @return Inhalt des Feldes wechselNichtVollzogen
	 */
	public boolean wechselNichtVollzogen() {
		return wechselNichtVollzogen;
	}

}
