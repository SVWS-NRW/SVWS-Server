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

	/** Die Optionen für das Dateiformat, in dem der Report ausgegeben werden soll, angegeben als Wert gemäß CoreType {@link ReportingAusgabeformat} */
	@Schema(description = "Die Optionen für das Dateiformat, in dem der Report ausgegeben werden soll. Werte gemäß CoreType ReportingAusgabeformat, z. B. "
			+ "(HTML = 1, PDF = 2, EMAIL = 3).", example = "2")
	public @NotNull List<@NotNull Integer> ausgabeformatOptionen = new ArrayList<>();

	/** Das Dateiformat, in dem der Report ausgegeben werden soll, angegeben als Wert gemäß CoreType {@link ReportingAusgabeformat} */
	@Schema(description = "Das Dateiformat, in dem der Report ausgegeben werden soll. Werte gemäß CoreType ReportingAusgabeformat, z. B. (HTML = 1, PDF = 2, EMAIL = 3).",
			example = "2")
	public int ausgabeformat = ReportingAusgabeformat.PDF.getId();

	/** Die Bezeichnung des auszugebenden Reports gemäß Definition im CoreType {@link ReportingReportvorlage} */
	@Schema(description = "Die Bezeichnung der Vorlage des auszugebenden Reports gemäß Definition im Core Type ReportingReportvorlage",
			example = "GostKlausurplanung-SchuelerMitKlausuren")
	public @NotNull String reportvorlage = "";

	/** Eine ID zum Objekt, das die Hauptdaten-IDs enthält, wie z. B. die ID eines Blockungsergebnisses oder eines Stundenplans. Gibt es kein solches Objekt,
	 *  so ist der Wert kleiner 0. */
	@Schema(description = "Eine ID zum Objekt, das die Hauptdaten-IDs enthält, wie z. B. die ID eines Blockungsergebnisses oder eines Stundenplans. Gibt es "
			+ "kein solches Objekt, so ist der Wert kleiner 0.", example = "17")
	public long idHauptdatenObjekt = -1;

	/** Eine Liste von IDs für die Hauptdatenquelle des zu erstellenden PDFs. */
	@Schema(description = "Eine Liste von IDs für die Hauptdatenquelle des zu erstellenden PDF.", example = "[2019,5,2020,3,2021,1]")
	public @NotNull List<Long> idsHauptdaten = new ArrayList<>();

	/** Eine Liste von IDs für die Ausgabe von Detaildaten zu den Hauptdaten. */
	@Schema(description = "Eine Liste von IDs für die Ausgabe von Detaildaten zu den Hauptdaten.", example = "[]")
	public @NotNull List<Long> idsDetaildaten = new ArrayList<>();

	/** Eine Liste mit Gruppen von freien, typisierten Report-Parameter-Werten, die in Templates direkt über ihren Namen nutzbar sind. */
	@Schema(description = "Eine Liste mit Gruppen von freien, typisierten Report-Parameter-Werten, die in Templates direkt über ihren Namen nutzbar sind.")
	public @NotNull List<ReportingReportvorlageParameterGruppe> reportvorlageParameterGruppen = new ArrayList<>();

	/** Typenspezifische Sortierdefinitionen, die für die Sortierung von ProxyTyp-Objekten verwendet werden sollen. */
	@Schema(description = "Gruppen von typenspezifischen Sortierdefinitionen, die für die Sortierung von ProxyTyp-Objekten verwendet werden sollen.",
			example = "[]")
	public @NotNull List<ReportingSortierungDefinitionGruppe> sortierungDefinitionenGruppen = new ArrayList<>();

	/** Typenspezifische Filterdefinitionen, die für die Filterung von ProxyTyp-Objekten verwendet werden sollen. */
	@Schema(description = "Gruppen von typenspezifischen Filterdefinitionen, die für die Filterung von ProxyTyp-Objekten verwendet werden sollen.",
			example = "[]")
	public @NotNull List<ReportingFilterDefinitionGruppe> filterDefinitionenGruppen = new ArrayList<>();

	/** Parameter, der die Daten für den E-Mail-Versand enthält. */
	@Schema(description = "Parameter, der die Daten für den E-Mail-Versand enthält.",
			example = "{\"betreff\":\"Persönlicher Stundenplan\",\"text\":\"Sehr geehrte Damen und Herren,\n\nanbei erhalten Sie den persönlichen Stundenplan.\n\nMit freundlichen Grüßen\\nIhre Schule\"}")
	public ReportingEMailDaten eMailDaten = new ReportingEMailDaten();


	/**
	 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
	 * Sie enthält die Daten und Einstellungen, welche im Rahmen der serverseitigen Report-Generierung genutzt werden sollen.
	 */
	public ReportingParameter() {
		super();
	}
}
