package de.svws_nrw.core.types.reporting;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Enumeration definiert die möglichen Daten-Kontexte für das Reporting.
 * Sie dient der Identifikation und Steuerung der Datenquellen bei der Erstellung von Reports.
 */
public enum ReportingReportvorlageDatenContext {

	/** Daten-Context ist SCHUELER */
	SCHUELER("SCHUELER"),

	/** Daten-Context ist LEHRER */
	LEHRER("LEHRER"),

	/** Daten-Context ist KLASSEN */
	KLASSEN("KLASSEN"),

	/** Daten-Context ist KURSE */
	KURSE("KURSE"),

	/** Daten-Context ist GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG */
	GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG("GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG"),

	/** Daten-Context ist GOST_KURSPLANUNG */
	GOST_KURSPLANUNG("GOST_KURSPLANUNG"),

	/** Daten-Context ist GOST_KLAUSURPLANUNG */
	GOST_KLAUSURPLANUNG("GOST_KLAUSURPLANUNG"),

	/** Daten-Context ist STUNDENPLANUNG */
	STUNDENPLANUNG("STUNDENPLANUNG");


	/** Die Bezeichnung des Daten-Kontexts */
	private final @NotNull String bezeichnung;


	/**
	 * Erstellt eine neue DatenContextDefinition mit der angegebenen Bezeichnung.
	 *
	 * @param bezeichnung die Bezeichnung des Daten-Kontexts
	 */
	ReportingReportvorlageDatenContext(final @NotNull String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}


	/**
	 * Gibt die Bezeichnung des Daten-Kontexts zurück.
	 *
	 * @return die Bezeichnung
	 */
	public @NotNull String getBezeichnung() {
		return this.bezeichnung;
	}

	/**
	 * Gibt den Daten-Kontext anhand der Bezeichnung zurück.
	 *
	 * @param bezeichnung die Bezeichnung des Daten-Kontexts
	 *
	 * @return der Daten-Kontext oder null, wenn die Bezeichnung nicht gefunden wurde
	 */
	public ReportingReportvorlageDatenContext getByBezeichnung(final @NotNull String bezeichnung) {
		if (bezeichnung.isEmpty()) {
			return null;
		}
		for (final ReportingReportvorlageDatenContext rdc : ReportingReportvorlageDatenContext.values()) {
			if (rdc.bezeichnung.equals(bezeichnung)) {
				return rdc;
			}
		}
		return null;
	}

}
