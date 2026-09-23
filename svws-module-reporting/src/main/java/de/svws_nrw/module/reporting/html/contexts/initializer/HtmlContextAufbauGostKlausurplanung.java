package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.Map;
import java.util.function.Function;

import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 * Die Konfiguration eines Datenaufbaus der GOSt-Klausurplanung.
 * <p>Beide Sichtweisen — die der Schüler und die der Klausurtermine — prüfen dasselbe und legen ihren Context unter demselben Schlüssel ab; sie
 * unterscheiden sich allein im Context-Typ. Der Rückgabetyp des Erzeugers bindet diesen auf die Klausurplan-Contexts.</p>
 *
 * @param contextErzeuger Erzeugt den Haupt-Context dieser Sichtweise.
 */
record HtmlContextAufbauGostKlausurplanung(
		Function<ReportingContext, HtmlContextGostKlausurplanungKlausurplan> contextErzeuger) implements HtmlContextAufbau {

	/**
	 * Der Schlüssel des Haupt-Contexts in der Context-Map; für beide Sichtweisen derselbe.
	 *
	 * @return Der Schlüssel des Haupt-Contexts.
	 */
	@Override
	public String contextSchluessel() {
		return HtmlContextSchluessel.GOST_KLAUSURPLAN;
	}

	/**
	 * Beide Sichtweisen der GOSt-Klausurplanung unterstützen die Einzelausgabe.
	 *
	 * @return stets true.
	 */
	@Override
	public boolean unterstuetztEinzelausgabe() {
		return true;
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
		return new HtmlContextInitializerGostKlausurplanung(reportingContext, mapHtmlContexts, this);
	}

}
