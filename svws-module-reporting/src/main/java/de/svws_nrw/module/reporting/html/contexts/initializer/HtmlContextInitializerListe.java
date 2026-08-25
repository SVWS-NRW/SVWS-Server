package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.List;
import java.util.Map;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingAusgabeumfang;
import de.svws_nrw.module.reporting.diagnose.ReportingAuswahlergebnis;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 * Initializer für alle Datenaufbauten, die einer Liste von Hauptdaten-IDs folgen: Hauptdaten auswählen, Auswahl prüfen und einschränken, Haupt-Context aus den
 * ausgewählten Objekten erzeugen.
 * <p>Der Ablauf ist für alle diese Datenaufbauten derselbe; sie unterscheiden sich allein in ihrer {@link HtmlContextAufbauListe}-Konfiguration.</p>
 * <p>Der Ausgabeumfang wird direkt aus der Auswahl gemeldet: Die Zählwerte entstehen hier, und die Ausgabefactory braucht sie, um eine gewollt leere Ausgabe
 * von einem Programmierfehler zu unterscheiden.</p>
 *
 * @param <T> Der Reporting-Typ der ausgewählten Hauptdaten.
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
		super(reportingContext, mapHtmlContexts, aufbau);
		this.aufbau = aufbau;
	}


	/**
	 * Wählt die Hauptdaten zu den übergebenen IDs aus, meldet die dabei ausgelassenen, schränkt die Auswahl fachlich ein und legt den daraus erzeugten
	 * Haupt-Context in der Context-Map ab.
	 *
	 * @throws ApiOperationException Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung.
	 */
	@Override
	public void init() throws ApiOperationException {
		final HtmlContextDatenbezeichnungen bezeichnungen = aufbau.bezeichnungen();

		reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Wähle die Daten für %s für die HTML-Generierung aus.".formatted(bezeichnungen.nominativ()));

		final List<Long> ids = reportingParameter.idsHauptdaten();
		ReportingAuswahlergebnis<T> auswahl = aufbau.auswahl().apply(reportingContext, ids);
		HtmlContextValidierung.pruefeUndMeldeAuswahl(reportingContext, auswahl, aufbau.objektart(), bezeichnungen);
		auswahl = aufbau.einschraenkung().schraenkeEin(reportingContext, auswahl);
		// Die Einschränkung lässt weitere Datensätze aus; sie werden erst danach gemeldet, damit kein Datensatz zweimal in der Meldung erscheint.
		HtmlContextValidierung.pruefeUndMeldeAuswahl(reportingContext, auswahl, aufbau.objektart(), bezeichnungen);
		reportingContext.meldeAusgabeumfang(ReportingAusgabeumfang.ausAuswahl(auswahl));

		reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext %s für die HTML-Generierung - %d von %d IDs aus %s stehen für Template %s zur Verfügung."
						.formatted(bezeichnungen.nominativ(), auswahl.objekte().size(), auswahl.idsAngefordert().size(), bezeichnungen.dativ(),
								reportingReportvorlage.name()));
		mapHtmlContexts.put(aufbau.contextSchluessel(), aufbau.contextErzeuger().apply(reportingContext, auswahl.objekte()));
	}


	/**
	 * Der Ausgabeumfang wird direkt in {@link #init()} aus der Auswahl gemeldet.
	 *
	 * @return false, denn dieser Initializer meldet selbst.
	 */
	@Override
	public boolean meldetAusgabeumfangImContextAufbau() {
		return false;
	}

}
