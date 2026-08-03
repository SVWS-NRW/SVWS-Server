package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToLongFunction;

import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 * Die Konfiguration eines Datenaufbaus, der einer Liste von Hauptdaten-IDs folgt: IDs prüfen, gegebenenfalls zusätzlich prüfen, Context erzeugen.
 * <p>Der {@link ReportingContext} ist bei Lader und Zusatzprüfung ein Parameter und nicht eingefangen. Nur so bleibt die Konfiguration request-unabhängig
 * und statisch, und nur so lassen sich die Prüfungen aus {@link HtmlContextValidierung} als Methodenreferenz eintragen.</p>
 * <p>Über den Typparameter sind Lader, ID-Extraktor und Context aneinander gebunden — ein Lader für Klassen mit einem Context für Schüler fällt damit
 * bereits beim Übersetzen auf.</p>
 *
 * @param <T>               Der Reporting-Typ der geladenen Hauptdaten.
 * @param bezeichnungen     Die Beschriftungen für Log-Ausgaben und Fehlermeldungen.
 * @param contextSchluessel Der Schlüssel des Haupt-Contexts in der Context-Map (siehe {@link HtmlContextSchluessel}).
 * @param lader             Lädt die Hauptdaten zu den übergebenen IDs, ohne bei fehlenden IDs selbst abzubrechen.
 * @param idExtractor       Bestimmt die ID eines geladenen Objekts.
 * @param contextErzeuger   Erzeugt den Haupt-Context.
 * @param zusatzpruefung    Zusätzliche Prüfungen dieses Datenaufbaus. Ohne Zusatzprüfungen ist das eine leere Aktion, nie {@code null}.
 */
record HtmlContextAufbauListe<T>(
		HtmlContextDatenbezeichnungen bezeichnungen,
		String contextSchluessel,
		BiFunction<ReportingContext, List<Long>, List<T>> lader,
		ToLongFunction<T> idExtractor,
		Function<ReportingContext, HtmlContext<T>> contextErzeuger,
		BiConsumer<ReportingContext, List<Long>> zusatzpruefung) implements HtmlContextAufbau {

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
