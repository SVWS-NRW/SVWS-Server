package de.svws_nrw.module.reporting.html.contexts;

import java.util.function.Predicate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ProxyReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import jakarta.ws.rs.core.Response;
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
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
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
				filterSchueler, filterKurse, quelle.istSchuelerFilterAktiv(), quelle.istKurseFilterAktiv());

		final Context context = new Context();
		context.setVariable("GostBlockungsergebnis", this.blockungsergebnis);
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
			reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Die ID der Blockungsergebnisses wurde ermittelt: " + idBlockungsergebnis);
			final GostBlockungsergebnis ergebnis = DataGostBlockungsergebnisse.getErgebnisFromID(this.reportingContext.conn(), idBlockungsergebnis);
			reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Das Blockungsergebnis wurde ermittelt.");
			final GostBlockungsdatenManager datenManager =
					DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(this.reportingContext.conn(), ergebnis.blockungID);
			reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Der Datenmanager zum Blockungsergebnis wurde ermittelt.");

			this.blockungsergebnis = new ProxyReportingGostKursplanungBlockungsergebnis(this.reportingContext, ergebnis, datenManager);

			// Daten-Context für Thymeleaf erzeugen.
			final Context context = new Context();
			context.setVariable("GostBlockungsergebnis", this.blockungsergebnis);
			super.setContext(context);
		} catch (final ApiOperationException e) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, e,
					"FEHLER: Das Blockungsergebnis und der zugehörige Datenmanager konnten nicht ermittelt werden. " + e.getMessage());
		}
	}
}
