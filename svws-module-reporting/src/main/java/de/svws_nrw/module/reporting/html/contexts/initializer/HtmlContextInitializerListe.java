package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.List;
import java.util.Map;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 * Initializer für alle Datenaufbauten, die einer Liste von Hauptdaten-IDs folgen: IDs prüfen, gegebenenfalls zusätzlich prüfen, Haupt-Context erzeugen.
 * <p>Der Ablauf ist für alle diese Datenaufbauten derselbe; sie unterscheiden sich allein in ihrer {@link HtmlContextAufbauListe}-Konfiguration.</p>
 *
 * @param <T> Der Reporting-Typ der geladenen Hauptdaten.
 */
final class HtmlContextInitializerListe<T> extends HtmlContextInitializerBasis {

	/** Die Konfiguration des Datenaufbaus, den dieser Initializer ausführt. */
	private final HtmlContextAufbauListe<T> aufbau;


	/**
	 * Erzeugt den Initializer für einen konkreten Request.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param mapHtmlContexts  Die Map, in der die erzeugten HTML-Contexts gesammelt werden.
	 * @param aufbau           Die Konfiguration des auszuführenden Datenaufbaus.
	 */
	HtmlContextInitializerListe(final ReportingContext reportingContext, final Map<String, HtmlContext<?>> mapHtmlContexts,
			final HtmlContextAufbauListe<T> aufbau) {
		super(reportingContext, mapHtmlContexts);
		this.aufbau = aufbau;
	}


	/**
	 * Prüft die übergebenen Hauptdaten-IDs, führt die Zusatzprüfungen des Datenaufbaus aus und legt den erzeugten Haupt-Context in der Context-Map ab.
	 *
	 * @throws ApiOperationException Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	@Override
	public void init() throws ApiOperationException {
		final HtmlContextDatenbezeichnungen bezeichnungen = aufbau.bezeichnungen();

		reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				"Validiere die Daten für %s für die HTML-Generierung.".formatted(bezeichnungen.nominativ()));

		final List<Long> ids = reportingParameter.idsHauptdaten();
		HtmlContextValidierung.validiereIds(reportingContext.logger(), ids, aufbau.lader().apply(reportingContext, ids), aufbau.idExtractor(),
				bezeichnungen.idTyp(), "FEHLER: Es wurden ungültige %s übergeben.".formatted(bezeichnungen.idTyp()));
		aufbau.zusatzpruefung().accept(reportingContext, ids);

		reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext %s für die HTML-Generierung - %d IDs von %s wurden übergeben für Template %s."
						.formatted(bezeichnungen.nominativ(), ids.size(), bezeichnungen.dativ(), reportingReportvorlage.name()));
		mapHtmlContexts.put(aufbau.contextSchluessel(), aufbau.contextErzeuger().apply(reportingContext));
	}


	/**
	 * Der Schlüssel des Haupt-Contexts in der Context-Map. Die Datenaufbauten dieses Musters unterstützen die Einzelausgabe.
	 *
	 * @return Der Schlüssel des Haupt-Contexts in der Context-Map.
	 */
	@Override
	public String einzelContextBezeichnung() {
		return aufbau.contextSchluessel();
	}

}
