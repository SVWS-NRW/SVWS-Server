package de.svws_nrw.module.reporting.parameter;

import java.util.List;

import de.svws_nrw.core.data.reporting.ReportingEMailDaten;
import de.svws_nrw.core.data.reporting.ReportingFilterDefinition;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.data.reporting.ReportingSortierungDefinition;
import de.svws_nrw.core.data.reporting.ReportingVorlageParameter;
import de.svws_nrw.core.types.reporting.ReportingAusgabeformat;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.module.reporting.html.HtmlTemplateDefinition;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

/**
 * Diese Klasse kapselt ein {@link ReportingParameter}-Objekt und stellt dessen Attribute
 * sowie zusätzliche typisierte Informationen (Schuljahresabschnitt, Ausgabeformat, Vorlage) bereit.
 */
public class ReportingParameterTypisiert {

	/** Das Repository für den Zugriff auf weitere Datenobjekte */
	private final ReportingRepository reportingRepository;

	/** Das gekapselte ReportingParameter-Objekt */
	private final ReportingParameter reportingParameter;


	/**
	 * Erstellt ein neues Objekt der Klasse ReportingParameterTypisiert.
	 *
	 * @param reportingRepository  das Repository für den Datenzugriff
	 * @param reportingParameter   das ReportingParameter-Objekt
	 */
	public ReportingParameterTypisiert(final ReportingRepository reportingRepository, final ReportingParameter reportingParameter) {
		this.reportingRepository = reportingRepository;
		this.reportingParameter = reportingParameter;
	}


	/**
	 * Ermittelt den Schuljahresabschnitt anhand der ID aus den Parametern.
	 *
	 * @return der ReportingSchuljahresabschnitt
	 */
	public ReportingSchuljahresabschnitt schuljahresabschnitt() {
		return reportingRepository.schuljahresabschnitt(reportingParameter.idSchuljahresabschnitt);
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
	 * Ermittelt das Objekt der ReportingReportvorlage.
	 *
	 * @return die ReportingReportvorlage oder null.
	 */
	public ReportingReportvorlage reportVorlage() {
		return ReportingReportvorlage.getByBezeichnung(reportingParameter.reportvorlage);
	}

	/**
	 * Ermittelt die HtmlTemplateDefinition passend zur Vorlage.
	 *
	 * @return die HtmlTemplateDefinition oder null.
	 */
	public HtmlTemplateDefinition htmlTemplateDefinition() {
		final ReportingReportvorlage vorlage = this.reportVorlage();
		return (vorlage != null) ? HtmlTemplateDefinition.getByReportvorlage(vorlage) : null;
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


	// ##### Getter für die Attribute aus ReportingParameter. #####

	/**
	 * Gibt die Liste von IDs für die Hauptdatenquelle zurück.
	 *
	 * @return die Liste von IDs für die Hauptdatenquelle
	 */
	public List<Long> idsHauptdaten() {
		return reportingParameter.idsHauptdaten;
	}

	/**
	 * Gibt zurück, ob eine Einzelausgabe für Hauptdaten gewünscht ist.
	 *
	 * @return true, wenn Einzelausgabe für Hauptdaten gewünscht
	 */
	public boolean einzelausgabeHauptdaten() {
		return reportingParameter.einzelausgabeHauptdaten;
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
	 * Gibt zurück, ob eine Einzelausgabe für Detaildaten gewünscht ist.
	 *
	 * @return true, wenn Einzelausgabe für Detaildaten gewünscht
	 */
	public boolean einzelausgabeDetaildaten() {
		return reportingParameter.einzelausgabeDetaildaten;
	}

	/**
	 * Gibt die Sortierung für die Hauptdaten zurück.
	 *
	 * @return die Sortierung für die Hauptdaten
	 */
	public ReportingSortierungDefinition sortierungHauptdaten() {
		return reportingParameter.sortierungHauptdaten;
	}

	/**
	 * Gibt die Sortierung für die Detaildaten zurück.
	 *
	 * @return die Sortierung für die Detaildaten
	 */
	public ReportingSortierungDefinition sortierungDetaildaten() {
		return reportingParameter.sortierungDetaildaten;
	}

	/**
	 * Gibt die Liste der Sortierdefinitionen zurück.
	 *
	 * @return die Liste der Sortierdefinitionen
	 */
	public List<ReportingSortierungDefinition> sortierungDefinitionen() {
		return reportingParameter.sortierungDefinitionen;
	}

	/**
	 * Gibt die Liste der Filterdefinitionen zurück.
	 *
	 * @return die Liste der Filterdefinitionen
	 */
	public List<ReportingFilterDefinition> filterDefinitionen() {
		return reportingParameter.filterDefinitionen;
	}

	/**
	 * Gibt die E-Mail-Daten zurück.
	 *
	 * @return die E-Mail-Daten
	 */
	public ReportingEMailDaten eMailDaten() {
		return reportingParameter.eMailDaten;
	}

	/**
	 * Gibt zurück, ob der Duplexdruck aktiviert ist.
	 *
	 * @return true bei Duplexdruck
	 */
	public boolean duplexdruck() {
		return reportingParameter.duplexdruck;
	}

	/**
	 * Gibt die Liste der Vorlage-Parameter zurück.
	 *
	 * @return die Liste der Vorlage-Parameter
	 */
	public List<ReportingVorlageParameter> vorlageParameter() {
		return reportingParameter.vorlageParameter;
	}
}
