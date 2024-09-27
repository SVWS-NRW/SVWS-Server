package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungZeitrasterstunde;


/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausuraufsicht.
 */
public class ReportingGostKlausurplanungKlausuraufsicht {

	/** Die Uhrzeit in Minuten seit 0 Uhr, wann die Aufsicht beginnt. NULL bedeutet "noch nicht definiert". */
	protected Integer aufsichtsbeginn;

	/** Die Uhrzeit in Minuten seit 0 Uhr, wann die Aufsicht endet. NULL bedeutet "noch nicht definiert". */
	protected Integer aufsichtsende;

	/** Die textuelle Bemerkung zur Klausuraufsicht. */
	protected String bemerkung;

	/** Der aufsichtsführende Lehrer. */
	protected ReportingLehrer lehrer;

	/** Die Unterrichtsstunde (Eintrag im Zeitraster des Stundenplans), die der Aufsicht zugeordnet ist. */
	protected ReportingStundenplanungZeitrasterstunde unterrichtsstunde;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param aufsichtsbeginn	Die Uhrzeit in Minuten seit 0 Uhr, wann die Aufsicht beginnt. NULL bedeutet "noch nicht definiert".
	 * @param aufsichtsende		Die Uhrzeit in Minuten seit 0 Uhr, wann die Aufsicht endet. NULL bedeutet "noch nicht definiert".
	 * @param bemerkung			Die textuelle Bemerkung zur Klausuraufsicht.
	 * @param lehrer			Der aufsichtsführende Lehrer.
	 * @param unterrichtsstunde	Die Unterrichtsstunde (Eintrag im Zeitraster des Stundenplans), die der Aufsicht zugeordnet ist.
	 */
	public ReportingGostKlausurplanungKlausuraufsicht(final Integer aufsichtsbeginn, final Integer aufsichtsende, final String bemerkung,
			final ReportingLehrer lehrer, final ReportingStundenplanungZeitrasterstunde unterrichtsstunde) {
		this.aufsichtsbeginn = aufsichtsbeginn;
		this.aufsichtsende = aufsichtsende;
		this.bemerkung = bemerkung;
		this.lehrer = lehrer;
		this.unterrichtsstunde = unterrichtsstunde;
	}


	// ##### Getter #####

	/**
	 * Die Uhrzeit in Minuten seit 0 Uhr, wann die Aufsicht beginnt. NULL bedeutet "noch nicht definiert".
	 * @return Inhalt des Feldes aufsichtsbeginn
	 */
	public Integer aufsichtsbeginn() {
		return aufsichtsbeginn;
	}

	/**
	 * Die Uhrzeit in Minuten seit 0 Uhr, wann die Aufsicht endet. NULL bedeutet "noch nicht definiert".
	 * @return Inhalt des Feldes aufsichtsende
	 */
	public Integer aufsichtsende() {
		return aufsichtsende;
	}

	/**
	 * Die textuelle Bemerkung zur Klausuraufsicht.
	 * @return Inhalt des Feldes bemerkung
	 */
	public String bemerkung() {
		return bemerkung;
	}

	/**
	 * Der aufsichtsführende Lehrer.
	 * @return Inhalt des Feldes lehrer
	 */
	public ReportingLehrer lehrer() {
		return lehrer;
	}

	/**
	 * Die Unterrichtsstunde (Eintrag im Zeitraster des Stundenplans), die der Aufsicht zugeordnet ist.
	 * @return Inhalt des Feldes unterrichtsstunde
	 */
	public ReportingStundenplanungZeitrasterstunde unterrichtsstunde() {
		return unterrichtsstunde;
	}
}
