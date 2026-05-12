package de.svws_nrw.module.reporting.parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.reporting.ReportingEMailDaten;
import de.svws_nrw.core.data.reporting.ReportingFilterDefinitionGruppe;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameter;
import de.svws_nrw.core.data.reporting.ReportingSortierungDefinitionGruppe;
import de.svws_nrw.core.types.reporting.ReportingAusgabeformat;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

/**
 * Diese Klasse kapselt ein {@link ReportingParameter}-Objekt und stellt dessen Attribute
 * sowie zusätzliche typisierte Informationen (Schuljahresabschnitt, Ausgabeformat, Vorlage) bereit.
 */
public class ReportingParameterTypisiert {

	/** Das Repository für den Zugriff auf weitere Datenobjekte */
	private final ReportingContext reportingContext;

	/** Das gekapselte ReportingParameter-Objekt */
	private final ReportingParameter reportingParameter;

	/** Map mit allen ReportingVorlageParametern */
	private final Map<String, ReportingReportvorlageParameter> mapReportVorlageParameter = new HashMap<>();

	/** Liste mit allen ReportingVorlageParametern */
	private final List<ReportingReportvorlageParameter> listReportVorlageParameter = new ArrayList<>();


	/**
	 * Erstellt ein neues Objekt der Klasse ReportingParameterTypisiert.
	 *
	 * @param reportingContext  das Repository für den Datenzugriff
	 * @param reportingParameter   das ReportingParameter-Objekt
	 */
	public ReportingParameterTypisiert(final ReportingContext reportingContext, final ReportingParameter reportingParameter) {
		this.reportingContext = reportingContext;
		this.reportingParameter = reportingParameter;
		this.reportingParameter.reportvorlageParameterGruppen
				.forEach(g -> g.reportvorlageParameter
						.forEach(p -> mapReportVorlageParameter.put(p.name, p)));
		this.reportingParameter.reportvorlageParameterGruppen
				.forEach(g -> listReportVorlageParameter.addAll(g.reportvorlageParameter));
	}


	/**
	 * Ermittelt den Schuljahresabschnitt anhand der ID aus den Parametern.
	 *
	 * @return der ReportingSchuljahresabschnitt
	 */
	public ReportingSchuljahresabschnitt schuljahresabschnitt() {
		return reportingContext.repositorySchule().schuljahresabschnitt(reportingParameter.idSchuljahresabschnitt);
	}

	/**
	 * Gibt das Report-Ausgabeformat als Enum zurück.
	 *
	 * @return das ReportingAusgabeformat
	 */
	public List<ReportingAusgabeformat> ausgabeformatOptionen() {
		return reportingParameter.ausgabeformatOptionen.stream().map(ReportingAusgabeformat::getByID).toList();
	}

	/**
	 * Gibt das Report-Ausgabeformat als Enum zurück.
	 *
	 * @return das ReportingAusgabeformat
	 */
	public ReportingAusgabeformat ausgabeformat() {
		return ReportingAusgabeformat.getByID(reportingParameter.ausgabeformat);
	}

	/**
	 * Gibt zurück, ob eine Ausgabe für einen Ausdruck (auf Papier) erfolgen soll (PDF oder E-Mail).
	 *
	 * @return true, wenn die Ausgabe für einen Ausdruck erfolgen soll.
	 */
	public boolean istDruckausgabe() {
		final ReportingAusgabeformat ausgabeformat = this.ausgabeformat();
		return (ausgabeformat == ReportingAusgabeformat.PDF) || (ausgabeformat == ReportingAusgabeformat.EMAIL);
	}

	/**
	 * Ermittelt das Objekt der ReportingReportvorlage.
	 *
	 * @return die ReportingReportvorlage oder null.
	 */
	public ReportingReportvorlage reportVorlage() {
		return ReportingReportvorlage.getByBezeichnung(reportingParameter.reportvorlage);
	}

	/**
	 * Eine ID zum Objekt, das die Hauptdaten-IDs enthält, wie z. B. die ID eines Blockungsergebnisses oder eines Stundenplans.
	 *
	 * @return die ID des Hauptdatenobjektes.
	 */
	public long idHauptdatenObjekt() {
		return reportingParameter.idHauptdatenObjekt;
	}

	/**
	 * Gibt die Liste von IDs für die Hauptdatenquelle zurück.
	 *
	 * @return die Liste von IDs für die Hauptdatenquelle
	 */
	public List<Long> idsHauptdaten() {
		return reportingParameter.idsHauptdaten;
	}

	/**
	 * Gibt die Liste von IDs für die Detaildaten zurück.
	 *
	 * @return die Liste von IDs für die Detaildaten
	 */
	public List<Long> idsDetaildaten() {
		return reportingParameter.idsDetaildaten;
	}

	/**
	 * Gibt die Liste der Vorlage-Parameter zurück.
	 *
	 * @return die Liste der Vorlage-Parameter
	 */
	public List<ReportingReportvorlageParameter> reportvorlageParameter() {
		return listReportVorlageParameter;
	}

	/**
	 * Gibt die Liste der Sortierdefinitionen zurück.
	 *
	 * @return die Liste der Sortierdefinitionen
	 */
	public List<ReportingSortierungDefinitionGruppe> sortierungDefinitionenGruppen() {
		return reportingParameter.sortierungDefinitionenGruppen;
	}

	/**
	 * Gibt die Liste der Filterdefinitionen zurück.
	 *
	 * @return die Liste der Filterdefinitionen
	 */
	public List<ReportingFilterDefinitionGruppe> filterDefinitionenGruppen() {
		return reportingParameter.filterDefinitionenGruppen;
	}

	/**
	 * Gibt die E-Mail-Daten zurück.
	 *
	 * @return die E-Mail-Daten
	 */
	public ReportingEMailDaten eMailDaten() {
		return reportingParameter.eMailDaten;
	}



	// ##### Getter für spezielle bzw. allgemeine ReportvorlageParameter. #####

	/**
	 * Gibt zurück, ob eine Einzelausgabe für Daten gewünscht ist.
	 *
	 * @return true, wenn Einzelausgabe für Daten gewünscht
	 */
	public boolean einzelausgabeDaten() {
		final ReportingReportvorlageParameter einzelausgabeDaten = mapReportVorlageParameter.get("einzelausgabeDaten");
		return (einzelausgabeDaten != null) && einzelausgabeDaten.wert.equalsIgnoreCase("true");
	}

	/**
	 * Gibt zurück, ob der Duplexdruck aktiviert ist.
	 *
	 * @return true bei Duplexdruck
	 */
	public boolean duplexdruck() {
		final ReportingReportvorlageParameter duplexdruck = mapReportVorlageParameter.get("duplexdruck");
		return (duplexdruck != null) && duplexdruck.wert.equalsIgnoreCase("true");
	}

}
