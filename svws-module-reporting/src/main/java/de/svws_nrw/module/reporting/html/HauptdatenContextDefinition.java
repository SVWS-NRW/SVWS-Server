package de.svws_nrw.module.reporting.html;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Enumeration definiert die möglichen Hauptdaten-Kontexte für das Reporting.
 * Sie dient der Identifikation und Steuerung der Datenquellen bei der Erstellung von Reports.
 */
public enum HauptdatenContextDefinition {

	/** Hauptdaten-Context ist SCHUELER */
	SCHUELER("SCHUELER"),

	/** Hauptdaten-Context ist LEHRER */
	LEHRER("LEHRER"),

	/** Hauptdaten-Context ist KLASSEN */
	KLASSEN("KLASSEN"),

	/** Hauptdaten-Context ist KURSE */
	KURSE("KURSE"),

	/** Hauptdaten-Context ist GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG */
	GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG("GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG"),

	/** Hauptdaten-Context ist GOST_KURSPLANUNG */
	GOST_KURSPLANUNG("GOST_KURSPLANUNG"),

	/** Hauptdaten-Context ist GOST_KLAUSURPLANUNG */
	GOST_KLAUSURPLANUNG("GOST_KLAUSURPLANUNG"),

	/** Hauptdaten-Context ist STUNDENPLANUNG */
	STUNDENPLANUNG("STUNDENPLANUNG");


	/** Die Bezeichnung des Hauptdaten-Kontexts */
	private final @NotNull String bezeichnung;


	/**
	 * Erstellt eine neue HauptdatenContextDefinition mit der angegebenen Bezeichnung.
	 *
	 * @param bezeichnung die Bezeichnung des Hauptdaten-Kontexts
	 */
	HauptdatenContextDefinition(final @NotNull String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}


	/**
	 * Gibt die Bezeichnung des Hauptdaten-Kontexts zurück.
	 *
	 * @return die Bezeichnung
	 */
	public @NotNull String getBezeichnung() {
		return this.bezeichnung;
	}

	@Override
	public String toString() {
		return this.bezeichnung;
	}

}
