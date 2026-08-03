package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 * Die Konfiguration eines Datenaufbaus der Stundenplanung: Stundenplan laden, die Hauptdaten-IDs je nach Sichtweise prüfen, Context erzeugen.
 * <p>Das Laden des Stundenplans samt Fehlerbehandlung steht einmal im Initializer; die fünf Sichtweisen unterscheiden sich allein in ihrer Prüfung und im
 * erzeugten Context.</p>
 *
 * @param <T>               Der Reporting-Typ der Context-Daten dieser Sichtweise.
 * @param contextSchluessel Der Schlüssel des Haupt-Contexts in der Context-Map (siehe {@link HtmlContextSchluessel}).
 * @param contextErzeuger   Erzeugt den Haupt-Context aus Stundenplan und Hauptdaten-IDs.
 * @param pruefung          Die Prüfung der Hauptdaten-IDs dieser Sichtweise. Wo die Sichtweise keine Prüfung kennt, ist das eine leere Aktion, nie
 *                          {@code null}.
 */
record HtmlContextAufbauStundenplan<T>(
		String contextSchluessel,
		HtmlContextStundenplanErzeuger<T> contextErzeuger,
		BiConsumer<ReportingContext, List<Long>> pruefung) implements HtmlContextAufbau {

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
