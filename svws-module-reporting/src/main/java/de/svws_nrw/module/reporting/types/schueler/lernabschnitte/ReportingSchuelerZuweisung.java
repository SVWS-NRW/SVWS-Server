package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ Zuweisung.</p>
 * <p>Diese Daten liegen typischerweise nur an Berufskollegs vor.</p>
 */
public class ReportingSchuelerZuweisung extends ReportingBaseType {

	/** Das Fach, auf das sich die Zuweisung bezieht. */
	protected ReportingFach fach;

	/** Die Kursart der Zuweisung. */
	protected String kursart;

	/** Der Lernabschnitt, zu dem diese Zuweisung gehört. */
	protected ReportingSchuelerLernabschnitt lernabschnitt;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param fach          Das Fach, auf das sich die Zuweisung bezieht.
	 * @param kursart       Die Kursart der Zuweisung.
	 * @param lernabschnitt Der Lernabschnitt, zu dem diese Zuweisung gehört.
	 */
	public ReportingSchuelerZuweisung(final ReportingFach fach, final String kursart, final ReportingSchuelerLernabschnitt lernabschnitt) {
		this.fach = fach;
		this.kursart = kursart;
		this.lernabschnitt = lernabschnitt;
	}


	// ##### Getter #####

	/**
	 * Das Fach, auf das sich die Zuweisung bezieht.
	 *
	 * @return Inhalt des Feldes fach
	 */
	public ReportingFach fach() {
		return fach;
	}

	/**
	 * Die Kursart der Zuweisung.
	 *
	 * @return Inhalt des Feldes kursart
	 */
	public String kursart() {
		return kursart;
	}

	/**
	 * Der Lernabschnitt, zu dem diese Zuweisung gehört.
	 *
	 * @return Inhalt des Feldes lernabschnitt
	 */
	public ReportingSchuelerLernabschnitt lernabschnitt() {
		return lernabschnitt;
	}
}
