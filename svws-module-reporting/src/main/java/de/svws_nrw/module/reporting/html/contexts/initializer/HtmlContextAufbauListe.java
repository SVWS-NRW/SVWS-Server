package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import de.svws_nrw.module.reporting.diagnose.ReportingAuswahlergebnis;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 * Die Konfiguration eines Datenaufbaus, der einer Liste von Hauptdaten-IDs folgt: Hauptdaten auswählen, die Auswahl gegebenenfalls fachlich einschränken,
 * Context aus den ausgewählten Objekten erzeugen.
 * <p>Der {@link ReportingContext} ist bei Auswahl und Einschränkung ein Parameter und nicht eingefangen: Nur so bleibt die Konfiguration request-unabhängig
 * und statisch, und nur so lassen sich die Prüfungen aus {@link HtmlContextValidierung} als Methodenreferenz eintragen. Der Typparameter bindet Auswahl,
 * Einschränkung und Context aneinander, sodass eine Auswahl von Klassen mit einem Context für Schüler beim Übersetzen auffällt.</p>
 * <p>Der Context entsteht aus den ausgewählten Objekten und nicht erneut aus den Request-IDs. Ein zweiter Ladeweg über die ursprünglichen IDs machte jedes
 * Auslassen und jede Einschränkung wirkungslos.</p>
 *
 * @param <T>               Der Reporting-Typ der ausgewählten Hauptdaten.
 * @param bezeichnungen     Die Beschriftungen für Log-Ausgaben und Fehlermeldungen.
 * @param contextSchluessel Der Schlüssel des Haupt-Contexts in der Context-Map (siehe {@link HtmlContextSchluessel}).
 * @param objektart         Die Objektart der Hauptdaten für den Schlüssel eines Ausgabeproblems.
 * @param auswahl           Wählt die Hauptdaten zu den übergebenen IDs aus, ohne bei fehlenden IDs abzubrechen.
 * @param contextErzeuger   Erzeugt den Haupt-Context aus den ausgewählten Objekten.
 * @param einschraenkung    Die fachliche Einschränkung dieses Datenaufbaus. Wo es keine gibt, reicht sie die Auswahl unverändert durch, nie {@code null}.
 */
record HtmlContextAufbauListe<T>(
		HtmlContextDatenbezeichnungen bezeichnungen,
		String contextSchluessel,
		Class<T> objektart,
		BiFunction<ReportingContext, List<Long>, ReportingAuswahlergebnis<T>> auswahl,
		BiFunction<ReportingContext, List<T>, HtmlContext<T>> contextErzeuger,
		HtmlContextEinschraenkung<T> einschraenkung) implements HtmlContextAufbau {

	/**
	 * Alle Datenaufbauten dieses Musters unterstützen die Einzelausgabe; ihre Contexts sind aufteilbar.
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
		return new HtmlContextInitializerListe<>(reportingContext, mapHtmlContexts, this);
	}

}
