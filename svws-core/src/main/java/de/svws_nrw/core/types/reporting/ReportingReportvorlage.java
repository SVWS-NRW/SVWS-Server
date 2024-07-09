package de.svws_nrw.core.types.reporting;

import jakarta.validation.constraints.NotNull;

/**
 * Eine ENUM der integrierten Report-Vorlagen des SVWS-Servers.
 * Im Rahmen des Reportings werden auf Basis dieses CoreTyps Template-Definitionen vorgenommen.
 * --------
 * Hinweis:
 * --------
 * Es ist NICHT ausreichend eine neue Vorlage nur hier einzubinden. Es muss in jedem Fall auch eine neue
 * Html-Template-Definition im Reporting-Modul erstellt werden.
 * ----------
 * Anmerkung:
 * ----------
 * Die Benennung der Vorlagen erfolgt nach dem Schema Hauptdaten_v_Detaildaten. Bei der Report-Generierung erfolgt in
 * Teilen ein entsprechendes Füllen der Datenkontexte anhand der Benennung.
 */
public enum ReportingReportvorlage {

	/** Report-Vorlage: GOSt - Kursplanung - Kurs-Kurschüler */
	GOST_KURSPLANUNG_v_KURS_MIT_KURSSCHUELERN("GostKursplanung-KursMitKursschuelern"),

	/** Report-Vorlage: GOSt - Kursplanung - Kurse-Statistikwerte */
	GOST_KURSPLANUNG_v_KURSE_MIT_STATISTIKWERTEN("GostKursplanung-KurseMitStatistikwerten"),

	/** Report-Vorlage: GOSt - Kursplanung - Schüler-Kurse */
	GOST_KURSPLANUNG_v_SCHUELER_MIT_KURSEN("GostKursplanung-SchuelerMitKursen"),

	/** Report-Vorlage: GOSt - Kursplanung - Schüler-Schienen-Kurse */
	GOST_KURSPLANUNG_v_SCHUELER_MIT_SCHIENEN_KURSEN("GostKursplanung-SchuelerMitSchienenKursen"),

	/** Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) */
	SCHUELER_v_GOST_ABITUR_APO_ANLAGE_12("Schueler-GostAbiturApoAnlage12"),

	/** Report-Vorlage: GOSt - Laufbahnplanung - Ergebnisübersicht */
	SCHUELER_v_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT("Schueler-GostLaufbahnplanungErgebnisuebersicht"),

	/** Report-Vorlage: GOSt - Laufbahnplanung - Wahlbogen */
	SCHUELER_v_GOST_LAUFBAHNPLANUNG_WAHLBOGEN("Schueler-GostLaufbahnplanungWahlbogen");



	/** Die Bezeichnung der Report-Vorlage */
	private final String bezeichnung;

	/**
	 * Erstellt eine neue Report-Vorlage
	 * @param bezeichnung Der Name der Report-Vorlage
	 */
	ReportingReportvorlage(final @NotNull String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Gibt die Bezeichnung dieser Report-Vorlage zurück
	 * @return Die Bezeichnung dieser Report-Vorlage
	 */
	public @NotNull String getBezeichnung() {
		return (this.bezeichnung != null) ? this.bezeichnung : "";
	}

	/**
	 * Diese Methode ermittelt die Report-Vorlage anhand der übergebenen Bezeichnung.
	 * @param bezeichnung Die Bezeichnung der Report-Vorlage
	 * @return Die Report-Vorlage
	 */
	public static ReportingReportvorlage getByBezeichnung(final @NotNull String bezeichnung) {
		for (final ReportingReportvorlage rv : ReportingReportvorlage.values())
			if (!bezeichnung.isEmpty() && rv.bezeichnung.equals(bezeichnung))
				return rv;
		return null;
	}

}
