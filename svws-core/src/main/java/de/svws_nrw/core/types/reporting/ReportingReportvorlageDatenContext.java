package de.svws_nrw.core.types.reporting;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Enumeration definiert die möglichen Daten-Kontexte für das Reporting.
 * Sie dient der Identifikation und Steuerung der Datenquellen bei der Erstellung von Reports.
 * Jeder Wert bezeichnet genau einen Ablauf des Datenaufbaus, so dass zu einer Reportvorlage über ihren Daten-Kontext
 * eindeutig feststeht, welche Daten geladen und welche Prüfungen dabei durchgeführt werden.
 */
public enum ReportingReportvorlageDatenContext {

	/** Daten-Context ist SCHUELER - Schülerdaten ohne weitere Zusatzprüfungen */
	SCHUELER("SCHUELER"),

	/** Daten-Context ist SCHUELER_GOST_LAUFBAHNPLANUNG - Schülerdaten mit den Beratungs- und Abiturdaten der GOSt-Laufbahnplanung */
	SCHUELER_GOST_LAUFBAHNPLANUNG("SCHUELER_GOST_LAUFBAHNPLANUNG"),

	/** Daten-Context ist SCHUELER_GOST_ABITUR - Schülerdaten mit den Abiturdaten der GOSt */
	SCHUELER_GOST_ABITUR("SCHUELER_GOST_ABITUR"),

	/** Daten-Context ist LEHRER - Daten der Lehrkräfte */
	LEHRER("LEHRER"),

	/** Daten-Context ist KLASSEN - Daten der Klassen */
	KLASSEN("KLASSEN"),

	/** Daten-Context ist KURSE - Daten der Kurse */
	KURSE("KURSE"),

	/** Daten-Context ist GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG - Fachwahlstatistiken der GOSt-Laufbahnplanung eines Abiturjahrgangs */
	GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG("GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG"),

	/** Daten-Context ist GOST_KURSPLANUNG_KURSE - Blockungsergebnis der GOSt-Kursplanung aus Sicht der Kurse */
	GOST_KURSPLANUNG_KURSE("GOST_KURSPLANUNG_KURSE"),

	/** Daten-Context ist GOST_KURSPLANUNG_SCHUELER - Blockungsergebnis der GOSt-Kursplanung aus Sicht der Schüler */
	GOST_KURSPLANUNG_SCHUELER("GOST_KURSPLANUNG_SCHUELER"),

	/** Daten-Context ist GOST_KLAUSURPLANUNG_SCHUELER - Klausurplan der GOSt aus Sicht der Schüler */
	GOST_KLAUSURPLANUNG_SCHUELER("GOST_KLAUSURPLANUNG_SCHUELER"),

	/** Daten-Context ist GOST_KLAUSURPLANUNG_TERMINE - Klausurplan der GOSt aus Sicht der Klausurtermine */
	GOST_KLAUSURPLANUNG_TERMINE("GOST_KLAUSURPLANUNG_TERMINE"),

	/** Daten-Context ist STUNDENPLANUNG_FACH - Stundenplan aus Sicht der Fächer */
	STUNDENPLANUNG_FACH("STUNDENPLANUNG_FACH"),

	/** Daten-Context ist STUNDENPLANUNG_KLASSEN - Stundenplan aus Sicht der Klassen */
	STUNDENPLANUNG_KLASSEN("STUNDENPLANUNG_KLASSEN"),

	/** Daten-Context ist STUNDENPLANUNG_LEHRER - Stundenplan aus Sicht der Lehrkräfte */
	STUNDENPLANUNG_LEHRER("STUNDENPLANUNG_LEHRER"),

	/** Daten-Context ist STUNDENPLANUNG_RAUM - Stundenplan aus Sicht der Räume */
	STUNDENPLANUNG_RAUM("STUNDENPLANUNG_RAUM"),

	/** Daten-Context ist STUNDENPLANUNG_SCHUELER - Stundenplan aus Sicht der Schüler */
	STUNDENPLANUNG_SCHUELER("STUNDENPLANUNG_SCHUELER");


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
