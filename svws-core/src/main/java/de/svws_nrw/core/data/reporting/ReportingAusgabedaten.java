package de.svws_nrw.core.data.reporting;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie enthält die Daten und Einstellungen, welche im Rahmen der serverseitigen Report-Generierung genutzt werden sollen.
 */
@XmlRootElement
@Schema(description = "Die Daten und Einstellungen für die Report-Generierung.")
@TranspilerDTO
public class ReportingAusgabedaten {

	/** Die ID des Schuljahres, auf den sich die Ausgabe des Reports beziehen soll. */
	@Schema(description = "Parameter, der in Templates verwendet werden kann, um den Detailgrad der Darstellung zu steuern.", example = "0")
	public long idSchuljahresabschnitt = -1;

//	/** Das Dateiformat, in dem der Report ausgegeben werden soll. */
//	@Schema(description = "Das Dateiformat, in dem der Report ausgegeben werden soll.", example = "2")
//	public ReportingAusgabeformat ausgabeformat = ReportingAusgabeformat.PDF;

	/** Pfad und Dateiname mit der Thymeleaf-html-Dokumentvorlage, aus der später die PDF-Datei erzeugt wird. */
	@Schema(description = "Pfad und Dateiname mit der Thymeleaf-html-Dokumentvorlage, aus der später die PDF-Datei erzeugt wird", example = "de/svws_nrw/module/reporting/gost/laufbahnplanung/GostLaufbahnplanungWahlbogen.html")
	public @NotNull String dateipfadHtmlTemplate = "";

	/** Pfad zur css-Datei, die in der html-Dokumentvorlage verlinkt wurde. Er wird vom PDF-Builder benötigt, um als baseURI für nachladbare Dateien zu fungieren. */
	@Schema(description = "Pfad zur css-Datei, die in der html-Dokumentvorlage verlinkt wurde. Er wird vom PDF-Builder benötigt, um als baseURI für nachladbare Dateien zu fungieren", example = "de/svws_nrw/module/reporting/gost/laufbahnplanung/GostLaufbahnplanungWahlbogen.css")
	public @NotNull String dateipfadCss = "";

	/** Eine Liste von IDs für die Hauptdatenquelle des zu erstellenden PDF. */
	@Schema(description = "Eine Liste von IDs für die Hauptdatenquelle des zu erstellenden PDF.", example = "[12,54,123]")
	public @NotNull List<@NotNull Long> idsHauptdaten = new ArrayList<>();

	/** Legt fest, ob pro Datensatz der Hauptdaten eine einzelne PDF-Datei erzeugt werden soll. */
	@Schema(description = "Legt fest, ob pro Datensatz der Hauptdaten eine einzelne PDF-Datei erzeugt werden soll.", example = "false")
	public boolean einzelausgabeHauptdaten = false;

	/** Eine Liste von IDs für die Ausgabe von Detaildaten zu den Hauptdaten. */
	@Schema(description = "Eine Liste von IDs für die Ausgabe von Detaildaten zu den Hauptdaten.", example = "[12,54,123]")
	public @NotNull List<@NotNull Long> idsDetaildaten = new ArrayList<>();

	/** Legt fest, ob pro Datensatz der Detaildaten eine einzelne PDF-Datei erzeugt werden soll. */
	@Schema(description = "Legt fest, ob pro Datensatz der Detaildaten eine einzelne PDF-Datei erzeugt werden soll.", example = "false")
	public boolean einzelausgabeDetaildaten = false;

	/** Parameter, der in Templates verwendet werden kann, um den Detailgrad der Darstellung zu steuern. */
	@Schema(description = "Parameter, der in Templates verwendet werden kann, um den Detailgrad der Darstellung zu steuern.", example = "0")
	public int detailLevel = 0;

}