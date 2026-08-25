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
	 * @throws ApiOperationException Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung.
	 */
	void init() throws ApiOperationException;

	/**
	 * Der Schlüssel des Haupt-Contexts in der Context-Map — nur für die Einzelausgabe. Unter diesem Schlüssel wird der Haupt-Context für jedes Einzeldokument
	 * ersetzt. Ob ein Datenaufbau die Einzelausgabe zusagt, sagt {@link HtmlContextAufbau#unterstuetztEinzelausgabe()}; die Basisklasse liest die Zusage und
	 * wirft ohne sie den passenden Client-Fehler.
	 *
	 * @return Der Schlüssel des Haupt-Contexts in der Context-Map.
	 *
	 * @throws ApiOperationException Falls der Datenaufbau keine Einzelausgabe unterstützt.
	 */
	String einzelContextBezeichnung() throws ApiOperationException;

	/**
	 * Gibt an, wo dieser Datenaufbau seinen Ausgabeumfang meldet: {@code false}, wenn der Initializer selbst in {@link #init()} meldet, weil er die
	 * Zählwerte kennt (ID-Auswahl); {@code true}, wenn erst der Context-Aufbau sie kennt und während {@link #init()} meldet (Manager-Aufbauten,
	 * Fachwahlstatistik). Die Methode hat bewusst keine Standard-Implementierung: Jeder neue Datenaufbau muss die Entscheidung treffen, statt still ohne
	 * Umfang zu bleiben - die Ausgabefactory bricht ohne Meldung mit einem Serverfehler ab.
	 *
	 * @return true, wenn der Context-Aufbau den Ausgabeumfang meldet, sonst false.
	 */
	boolean meldetAusgabeumfangImContextAufbau();

}
