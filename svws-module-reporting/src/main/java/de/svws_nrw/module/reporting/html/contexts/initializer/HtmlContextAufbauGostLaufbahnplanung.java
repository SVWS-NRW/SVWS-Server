package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.Map;

import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 * Die Konfiguration des Datenaufbaus der GOSt-Laufbahnplanung eines Abiturjahrgangs.
 * <p>Anders als die übrigen Muster hat dieser Datenaufbau nichts zu konfigurieren: Er kennt nur eine Reportvorlage, einen Context-Typ und einen festen
 * Ablauf. Die Konfiguration bleibt dennoch eigenständig, damit die Registry die Metadaten aller Datenaufbauten einheitlich und ohne Reporting-Context
 * lesen kann.</p>
 */
final class HtmlContextAufbauGostLaufbahnplanung implements HtmlContextAufbau {

	/**
	 * Der Schlüssel des Haupt-Contexts in der Context-Map.
	 *
	 * @return Der Schlüssel des Haupt-Contexts.
	 */
	@Override
	public String contextSchluessel() {
		return HtmlContextSchluessel.GOST_LAUFBAHNPLANUNG_FACHWAHLSTATISTIKEN;
	}

	/**
	 * Dieser Datenaufbau unterstützt als einziger keine Einzelausgabe; die Fachwahlstatistiken eines Abiturjahrgangs sind nicht aufteilbar.
	 *
	 * @return stets false.
	 */
	@Override
	public boolean unterstuetztEinzelausgabe() {
		return false;
	}

	/**
	 * Erzeugt den Initializer für einen konkreten Request.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param mapHtmlContexts  Die Map, in der die erzeugten HTML-Contexts gesammelt werden.
	 *
	 * @return Der Initializer für diesen Request.
	 */
	@Override
	public HtmlContextInitializer initializer(final ReportingContext reportingContext, final Map<String, HtmlContext<?>> mapHtmlContexts) {
		return new HtmlContextInitializerGostLaufbahnplanung(reportingContext, mapHtmlContexts, this);
	}

}
