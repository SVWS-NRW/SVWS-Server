package de.svws_nrw.module.reporting.types.gost.klausurplanung;


import java.util.List;

import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungRaum;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Raum in der GOSt-Klausurplanung.
 */
public class ReportingGostKlausurplanungKlausurraum {

	/** Die Klausuraufsichten in diesem Raum für den Klausurtermin. */
	protected List<ReportingGostKlausurplanungKlausuraufsicht> aufsichten;

	/** Die Bemerkung zum Raum für die Klausur. */
	protected String bemerkung;

	/** Die ID des Raumes in der Klausurplanung, genauer die ID für die Kombination aus Raum der Schule und Klausurtermin. */
	protected long id;

	/** Der Klausurtermin, dem dieser Raum mit seinen Aufsichten zugeordnet wurde. */
	protected ReportingGostKlausurplanungKlausurtermin klausurtermin;

	/** Die Daten des Raumes gemäß dem zum Klausurtermin gültigen Stundenplanes. */
	protected ReportingStundenplanungRaum raumdaten;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param aufsichten	Die Klausuraufsichten in diesem Raum für den Klausurtermin.
	 * @param bemerkung		Die Bemerkung zum Raum für die Klausur.
	 * @param id			Die ID des Raumes in der Klausurplanung, genauer die ID für die Kombination aus Raum der Schule und Klausurtermin.
	 * @param klausurtermin	Der Klausurtermin, dem dieser Raum mit seinen Aufsichten zugeordnet wurde.
	 * @param raumdaten		Die Daten des Raumes gemäß dem zum Klausurtermin gültigen Stundenplanes.
	 */
	public ReportingGostKlausurplanungKlausurraum(final List<ReportingGostKlausurplanungKlausuraufsicht> aufsichten, final String bemerkung, final long id,
			final ReportingGostKlausurplanungKlausurtermin klausurtermin, final ReportingStundenplanungRaum raumdaten) {
		this.aufsichten = aufsichten;
		this.bemerkung = bemerkung;
		this.id = id;
		this.klausurtermin = klausurtermin;
		this.raumdaten = raumdaten;
	}


	// ##### Getter #####

	/**
	 * Die Klausuraufsichten in diesem Raum für den Klausurtermin.
	 *
	 * @return Inhalt des Feldes aufsichten
	 */
	public List<ReportingGostKlausurplanungKlausuraufsicht> aufsichten() {
		return aufsichten;
	}

	/**
	 * Die Bemerkung zum Raum für die Klausur.
	 *
	 * @return Inhalt des Feldes bemerkung
	 */
	public String bemerkung() {
		return bemerkung;
	}

	/**
	 * Die ID des Raumes in der Klausurplanung, genauer die ID für die Kombination aus Raum der Schule und Klausurtermin.
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Der Klausurtermin, dem dieser Raum mit seinen Aufsichten zugeordnet wurde.
	 *
	 * @return Inhalt des Feldes klausurtermin
	 */
	public ReportingGostKlausurplanungKlausurtermin klausurtermin() {
		return klausurtermin;
	}

	/**
	 * Die Daten des Raumes gemäß dem zum Klausurtermin gültigen Stundenplanes.
	 *
	 * @return Inhalt des Feldes raumdaten
	 */
	public ReportingStundenplanungRaum raumdaten() {
		return raumdaten;
	}
}
