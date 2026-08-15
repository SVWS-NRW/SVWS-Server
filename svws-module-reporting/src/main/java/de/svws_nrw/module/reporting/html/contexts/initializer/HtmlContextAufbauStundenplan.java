package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.Map;

import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 * Die Konfiguration eines Datenaufbaus der Stundenplanung: Stundenplan laden, die Hauptdaten der Sichtweise auswählen, Context erzeugen.
 * <p>Das Laden des Stundenplans samt Fehlerbehandlung steht einmal im Initializer; die fünf Sichtweisen unterscheiden sich allein in ihrer Auswahl und im
 * erzeugten Context. Eine ID, die die Auswahl nicht auflösen kann, wird ausgelassen und gemeldet - sie bricht den Report nicht mehr ab.</p>
 *
 * @param <T>               Der Reporting-Typ der Context-Daten dieser Sichtweise.
 * @param <H>               Der Reporting-Typ der ausgewählten Hauptdaten dieser Sichtweise.
 * @param bezeichnungen     Die Beschriftungen für Log-Ausgaben und Fehlermeldungen.
 * @param contextSchluessel Der Schlüssel des Haupt-Contexts in der Context-Map (siehe {@link HtmlContextSchluessel}).
 * @param objektart         Die Objektart der Hauptdaten für den Schlüssel eines Ausgabeproblems.
 * @param auswahl           Wählt die Hauptdaten zu den übergebenen IDs aus, ohne bei fehlenden IDs abzubrechen.
 * @param contextErzeuger   Erzeugt den Haupt-Context aus Stundenplan und den ausgewählten Hauptdaten-IDs.
 */
record HtmlContextAufbauStundenplan<T, H>(
		HtmlContextDatenbezeichnungen bezeichnungen,
		String contextSchluessel,
		Class<H> objektart,
		HtmlContextStundenplanAuswahl<H> auswahl,
		HtmlContextStundenplanErzeuger<T> contextErzeuger) implements HtmlContextAufbau {

	/**
	 * Alle Sichtweisen der Stundenplanung unterstützen die Einzelausgabe; ihre Contexts sind aufteilbar.
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
		return new HtmlContextInitializerStundenplan<>(reportingContext, mapHtmlContexts, this);
	}

}
