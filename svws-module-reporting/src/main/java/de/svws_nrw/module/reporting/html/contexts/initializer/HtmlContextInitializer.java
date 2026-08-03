package de.svws_nrw.module.reporting.html.contexts.initializer;

import de.svws_nrw.db.utils.ApiOperationException;

/**
 * Ein Initializer baut die Daten-Contexts genau eines Datenaufbaus auf.
 * <p>Welcher Initializer für eine Reportvorlage zuständig ist, ergibt sich aus deren
 * {@link de.svws_nrw.core.types.reporting.ReportingReportvorlageDatenContext} über die {@link HtmlContextInitializerRegistry}. Ein Initializer entsteht
 * immer für einen konkreten Request; die request-unabhängige Konfiguration hält der zugehörige {@link HtmlContextAufbau}.</p>
 */
public interface HtmlContextInitializer {

	/**
	 * Baut die Daten-Contexts für diesen Datenaufbau auf und legt sie in der Context-Map ab.
	 *
	 * @throws ApiOperationException Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	void init() throws ApiOperationException;

	/**
	 * Der Schlüssel des Haupt-Contexts in der Context-Map — nur für die Einzelausgabe. Unter diesem Schlüssel wird der Haupt-Context für jedes Einzeldokument
	 * ersetzt. Datenaufbauten ohne Einzelausgabe erben die Standard-Implementierung der Basisklasse, die den entsprechenden Fehler wirft.
	 *
	 * @return Der Schlüssel des Haupt-Contexts in der Context-Map.
	 *
	 * @throws ApiOperationException Falls der Datenaufbau keine Einzelausgabe unterstützt.
	 */
	String einzelContextBezeichnung() throws ApiOperationException;

}
