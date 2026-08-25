package de.svws_nrw.module.reporting.html.contexts;

import java.util.function.Predicate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingAusgabeumfang;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryGostKursplanung;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ProxyReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import org.thymeleaf.context.Context;


/**
 * Abstrakte Basisklasse für Thymeleaf-html-Daten-Contexts zum Bereich "GostKursplanung". Sie bündelt den Aufbau
 * des Blockungsergebnisses und die Übergabe an Thymeleaf. Die Aufteilung in Einzel-Contexts erfolgt in den konkreten Subklassen
 * {@link HtmlContextGostKursplanungBlockungsergebnisSchueler} und {@link HtmlContextGostKursplanungBlockungsergebnisKurse},
 * die jeweils das Interface {@link HtmlContextAufteilbar} implementieren.
 */
public abstract class HtmlContextGostKursplanungBlockungsergebnis extends HtmlContext<Object> {

	/** Das Reporting-Objekt zum Blockungsergebnis. */
	@JsonIgnore
	protected ReportingGostKursplanungBlockungsergebnis blockungsergebnis;

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten. Das Blockungsergebnis wird vollständig aus dem
	 * Repository und den Reporting-Parametern aufgebaut; die Filterung der Schüler und Kurse erfolgt über den FilterService
	 * anhand der konfigurierten Filterdefinitionen.
	 *
	 * @param reportingContext	Context mit Parametern, Logger und Daten zum Reporting.
	 *
	 * @throws ApiOperationException	Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung.
	 */
	protected HtmlContextGostKursplanungBlockungsergebnis(final ReportingContext reportingContext) throws ApiOperationException {
		super(reportingContext);
		erzeugeContext();
	}

	/**
	 * Initialisiert einen neuen HtmlContext für einen Einzel-Sub-Context, der ein bereits vorhandenes Blockungsergebnis-Objekt
	 * wiederverwendet und die Sicht über Schüler- und Kurs-Prädikate auf einzelne Entitäten einschränkt. Wird ausschließlich
	 * von Subklassen für die Erzeugung der Einzel-Contexts verwendet.
	 *
	 * @param reportingContext	Context mit Parametern, Logger und Daten zum Reporting.
	 * @param quelle				Ein bereits aufgebautes Blockungsergebnis, das als Datenquelle wiederverwendet wird.
	 * @param filterSchueler		Ein Prädikat, das bestimmt, welche Schüler in der Ausgabe enthalten sind.
	 * @param filterKurse			Ein Prädikat, das bestimmt, welche Kurse in der Ausgabe enthalten sind.
	 */
	protected HtmlContextGostKursplanungBlockungsergebnis(final ReportingContext reportingContext,
			final ReportingGostKursplanungBlockungsergebnis quelle,
			final Predicate<ReportingSchueler> filterSchueler, final Predicate<ReportingGostKursplanungKurs> filterKurse) {
		super(reportingContext);
		this.blockungsergebnis = new ProxyReportingGostKursplanungBlockungsergebnis(reportingContext, quelle,
				filterSchueler, filterKurse);

		final Context context = new Context();
		context.setVariable("GostBlockungsergebnis", this.blockungsergebnis);
		context.setVariable("zeigeProSchueler",
				reportingContext.filterService().hatFilter(ReportingSchueler.class.getSimpleName()));
		super.setContext(context);
	}


	/**
	 * Erzeugt den Context zur GOSt-Kursplanung.
	 *
	 * @throws ApiOperationException   	im Fehlerfall
	 */
	private void erzeugeContext() throws ApiOperationException {

		reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Erzeuge Context zu einem GostKursplanungBlockungsergebnis.");

		try {
			final long idBlockungsergebnis = this.reportingContext.reportingParameter().idHauptdatenObjekt();

			final ReportingRepositoryGostKursplanung repo = this.reportingContext.repositoryGostKursplanung();
			repo.initManager(idBlockungsergebnis);
			this.blockungsergebnis = repo.blockungsergebnis();

			// Erst das aufgebaute Blockungsergebnis kennt die Zähleinheiten dieses Datenaufbaus; die Sichtweise bestimmt, ob Kurse oder Schüler gezählt werden.
			this.reportingContext.meldeAusgabeumfang(ermittleAusgabeumfang(this.blockungsergebnis));

			final Context context = new Context();
			context.setVariable("GostBlockungsergebnis", this.blockungsergebnis);
			context.setVariable("zeigeProSchueler",
					reportingContext.filterService().hatFilter(ReportingSchueler.class.getSimpleName()));
			super.setContext(context);
		} catch (final ApiOperationException e) {
			// Der Status der Datenschicht bleibt erhalten; ein pauschaler Status gäbe am API-Rand einen Serverfehler als "Blockungsergebnis nicht
			// gefunden" aus. Die Meldung der Ursache reist als cause mit und erscheint im Fehlerblock unter den Fehlergründen.
			throw new ApiOperationException(e.getStatus(), e, "### FEHLER: Das Blockungsergebnis konnte nicht geladen werden.");
		}
	}

	/**
	 * Ermittelt den Ausgabeumfang dieser Sichtweise aus dem aufgebauten Blockungsergebnis: die vorhandenen Einheiten gegen die nach Filterung ausgegebenen.
	 * Eine leere Ausgabe ist zulässig, wenn keine Einheit ausgegeben wird - sei es, weil das Ergebnis keine enthält oder der Benutzerfilter alle ausschließt.
	 *
	 * @param ergebnis Das aufgebaute Blockungsergebnis dieses Reports.
	 *
	 * @return Der Ausgabeumfang dieser Sichtweise.
	 */
	protected abstract ReportingAusgabeumfang ermittleAusgabeumfang(ReportingGostKursplanungBlockungsergebnis ergebnis);
}
