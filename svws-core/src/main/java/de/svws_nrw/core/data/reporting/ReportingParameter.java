package de.svws_nrw.core.data.reporting;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.types.reporting.ReportingAusgabeformat;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie enthält die Daten und Einstellungen, welche im Rahmen der serverseitigen Report-Generierung genutzt werden sollen.
 */
@XmlRootElement
@Schema(description = "Die Daten und Einstellungen für die Report-Generierung.")
@TranspilerDTO
public class ReportingParameter {

	/** Die ID des Schuljahres, auf den sich die Ausgabe des Reports beziehen soll. */
	@Schema(description = "Der Schuljahresabschnitt, für den der Report erstellt werden soll.", example = "0")
	public long idSchuljahresabschnitt = -1;

	/** Das Dateiformat, in dem der Report ausgegeben werden soll, als Wert gemäß CoreType {@link ReportingAusgabeformat} */
	@Schema(description = "Das Dateiformat, in dem der Report ausgegeben werden soll. Werte gemäß CoreType ReportingAusgabeformat, z. B. (HTML = 1, PDF = 2).",
			example = "2")
	public int ausgabeformat = ReportingAusgabeformat.PDF.getId();

	/** Die Bezeichnung des auszugebenden Reports gemäß Definition im CoreType {@link ReportingReportvorlage} */
	@Schema(description = "Die Bezeichnung der Vorlage des auszugebenden Reports gemäß Definition im Core Type ReportingReportvorlage",
			example = "GostKlausurplanung-SchuelerMitKlausuren")
	public @NotNull String reportvorlage = "";

	/** Eine Liste von IDs für die Hauptdatenquelle des zu erstellenden PDF. */
	@Schema(description = "Eine Liste von IDs für die Hauptdatenquelle des zu erstellenden PDF.", example = "[2019,5,2020,3,2021,1]")
	public @NotNull List<Long> idsHauptdaten = new ArrayList<>();

	/** Legt fest, ob pro Datensatz der Hauptdaten eine einzelne PDF-Datei erzeugt werden soll. */
	@Schema(description = "Legt fest, ob pro Datensatz der Hauptdaten eine einzelne PDF-Datei erzeugt werden soll.", example = "false")
	public boolean einzelausgabeHauptdaten = false;

	/** Eine Liste von IDs für die Ausgabe von Detaildaten zu den Hauptdaten. */
	@Schema(description = "Eine Liste von IDs für die Ausgabe von Detaildaten zu den Hauptdaten.", example = "[]")
	public @NotNull List<Long> idsDetaildaten = new ArrayList<>();

	/** Legt fest, ob pro Datensatz der Detaildaten eine einzelne PDF-Datei erzeugt werden soll. */
	@Schema(description = "Legt fest, ob pro Datensatz der Detaildaten eine einzelne PDF-Datei erzeugt werden soll.", example = "false")
	public boolean einzelausgabeDetaildaten = false;

	/** Legt fest, ob die in den Daten-Contexts definierte Standardsortierung verwendet werden soll, oder die in diesen Parametern definierten Sortierungen. */
	@Schema(description = "Legt fest, ob die in den Daten-Contexts definierte Standardsortierung verwendet werden soll, oder die in diesen Parametern definierten Sortierungen.",
			example = "true")
	public Boolean verwendeStandardsortierung = true;

	/** Liste von Attributnamen, nach denen die Hauptdaten sortiert werden sollen. */
	@Schema(description = "Liste von Attributnamen, nach denen die Hauptdaten sortiert werden sollen.", example = "Nachname, Vorname")
	public List<String> sortierungHauptdaten = new ArrayList<>();

	/** Liste von Attributnamen, nach denen die Detaildaten sortiert werden sollen. */
	@Schema(description = "Liste von Attributnamen, nach denen die Detaildaten sortiert werden sollen.", example = "Nachname, Vorname")
	public List<String> sortierungDetaildaten = new ArrayList<>();

	/** Legt fest, ob die Seiteneinstellungen für einen Duplexdruck verwendet werden sollen. */
	@Schema(description = "Legt fest, ob die Seiteneinstellungen für einen Duplexdruck verwendet werden sollen.", example = "false")
	public boolean duplexdruck = false;

	/** Parameter, der in Templates verwendet werden kann, um den Detailgrad der Darstellung zu steuern. */
	@Schema(description = "Parameter, der in Templates verwendet werden kann, um den Detailgrad der Darstellung zu steuern.", example = "0")
	public int detailLevel = 0;


	/**
	 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
	 * Sie enthält die Daten und Einstellungen, welche im Rahmen der serverseitigen Report-Generierung genutzt werden sollen.
	 */
	public ReportingParameter() {
		super();
	}
}
