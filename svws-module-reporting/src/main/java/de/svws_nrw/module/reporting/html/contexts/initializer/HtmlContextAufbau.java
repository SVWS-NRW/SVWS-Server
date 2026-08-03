package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.Map;

import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 * Die request-unabhängige Konfiguration eines Datenaufbaus. Jedes Ablaufmuster bringt seinen eigenen Konfigurationstyp mit; alle erfüllen diese
 * Schnittstelle, über die die {@link HtmlContextInitializerRegistry} sie einheitlich führt.
 * <p>Die Metadaten sind damit <b>ohne jeden Reporting-Context lesbar</b>. Der Initializer selbst entsteht erst im Request aus Konfiguration und Context.</p>
 */
public interface HtmlContextAufbau {

	/**
	 * Der Schlüssel des Haupt-Contexts in der Context-Map (siehe {@link HtmlContextSchluessel}).
	 *
	 * @return Der Schlüssel des Haupt-Contexts.
	 */
	String contextSchluessel();

	/**
	 * Gibt an, ob dieser Datenaufbau die Aufteilung in Einzeldokumente unterstützt.
	 *
	 * @return true, wenn die Einzelausgabe unterstützt wird, sonst false.
	 */
	boolean unterstuetztEinzelausgabe();

	/**
	 * Erzeugt den Initializer für einen konkreten Request.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param mapHtmlContexts  Die Map, in der die erzeugten HTML-Contexts gesammelt werden.
	 *
	 * @return Der Initializer für diesen Request.
	 */
	HtmlContextInitializer initializer(ReportingContext reportingContext, Map<String, HtmlContext<?>> mapHtmlContexts);

}
