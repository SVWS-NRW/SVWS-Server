package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionHjData;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ProxyReportingGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurplan;
import jakarta.ws.rs.core.Response;


/**
 * Abstrakte Basisklasse für Thymeleaf-html-Daten-Contexts zum Bereich "GostKlausurplanung". Sie bündelt den Aufbau
 * des Klausurplans und die Übergabe an Thymeleaf. Die Aufteilung in Einzel-Contexts erfolgt in den konkreten Subklassen
 * {@link HtmlContextGostKlausurplanungKlausurplanSchueler}, {@link HtmlContextGostKlausurplanungKlausurplanKurse} und
 * {@link HtmlContextGostKlausurplanungKlausurplanTermine}, die jeweils das Interface {@link HtmlContextAufteilbar}
 * implementieren.
 */
public abstract class HtmlContextGostKlausurplanungKlausurplan extends HtmlContext<Object> {

	/** Klausurplan dieses Contexts. */
	@JsonIgnore
	protected ReportingGostKlausurplanungKlausurplan gostKlausurplan;

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten. Der Klausurplan wird vollständig aus dem
	 * Repository und den Reporting-Parametern aufgebaut; die Filterung der Schüler, Kurse und Klausurtermine erfolgt
	 * über den FilterService anhand der konfigurierten Filterdefinitionen.
	 *
	 * @param reportingContext	Context mit Parametern, Logger und Daten zum Reporting.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	protected HtmlContextGostKlausurplanungKlausurplan(final ReportingContext reportingContext) throws ApiOperationException {
		super(reportingContext);
		erzeugeContext();
	}

	/**
	 * Initialisiert einen neuen HtmlContext für einen Einzel-Sub-Context, der ein bereits vorhandenes Klausurplan-Objekt
	 * wiederverwendet und die Sicht über Schüler-, Kurs- und Klausurtermin-Prädikate auf einzelne Entitäten einschränkt.
	 * Wird ausschließlich von Subklassen für die Erzeugung der Einzel-Contexts verwendet.
	 *
	 * @param reportingContext	Context mit Parametern, Logger und Daten zum Reporting.
	 * @param gostKlausurplan		Ein bereits aufgebauter GOSt-Klausurplan, der als Datenquelle wiederverwendet wird.
	 * @param filterSchueler		Ein Prädikat, das bestimmt, welche Schüler in der Ausgabe enthalten sind.
	 * @param filterKurse			Ein Prädikat, das bestimmt, welche Kurse in der Ausgabe enthalten sind.
	 * @param filterKlausurtermine	Ein Prädikat, das bestimmt, welche Klausurtermine in der Ausgabe enthalten sind.
	 */
	protected HtmlContextGostKlausurplanungKlausurplan(final ReportingContext reportingContext,
			final ReportingGostKlausurplanungKlausurplan gostKlausurplan,
			final Predicate<ReportingSchueler> filterSchueler, final Predicate<ReportingKurs> filterKurse,
			final Predicate<ReportingGostKlausurplanungKlausurtermin> filterKlausurtermine) {
		super(reportingContext);
		this.gostKlausurplan = new ProxyReportingGostKlausurplanungKlausurplan(reportingContext, gostKlausurplan.klausurtermine(), gostKlausurplan.kurse(),
				gostKlausurplan.kursklausuren(), gostKlausurplan.schueler(), gostKlausurplan.schuelerklausuren(),
				filterSchueler, filterKurse, filterKlausurtermine);

		final Context context = new Context();
		context.setVariable("GostKlausurplan", this.gostKlausurplan);
		super.setContext(context);
	}


	/**
	 * Erzeugt den Context zur GOSt-Klausurplanung.
	 *
	 * @throws ApiOperationException   	im Fehlerfall
	 */
	private void erzeugeContext() throws ApiOperationException {

		// In den idsHauptdaten der Reporting-Parameter werden im Wechsel das Abiturjahr und das GostHalbjahr (0 = EF.1 bis 5 = Q2.2) übergeben.
		// Hier werden die Daten NICHT validiert. Die Daten aus den Parametern müssen vorab validiert worden sein (HtmlFactory).
		final List<Long> parameterDaten = reportingContext.reportingParameter().idsHauptdaten().stream().filter(Objects::nonNull).toList();
		final List<GostKlausurenCollectionHjData> selection = new ArrayList<>();

		if (!parameterDaten.isEmpty()) {
			// Stelle die übergebenen Stufen und Halbjahre zusammen.
			for (int i = 0; i < parameterDaten.size(); i = i + 2) {
				selection.add(new GostKlausurenCollectionHjData(Math.toIntExact(parameterDaten.get(i)), Math.toIntExact(parameterDaten.get(i + 1))));
			}
		} else {
			// Es wurden keine Stufen übergeben. Erzeuge die Ausgabe für alle Stufen gemäß Schuljahresabschnitt im Client.
			// EF:
			selection.add(new GostKlausurenCollectionHjData(reportingContext.repositorySchule().auswahlSchuljahresabschnitt().schuljahr() + 3,
					reportingContext.repositorySchule().auswahlSchuljahresabschnitt().abschnitt() - 1));
			// Q1:
			selection.add(new GostKlausurenCollectionHjData(reportingContext.repositorySchule().auswahlSchuljahresabschnitt().schuljahr() + 2,
					reportingContext.repositorySchule().auswahlSchuljahresabschnitt().abschnitt() + 1));
			// Q2:
			selection.add(new GostKlausurenCollectionHjData(reportingContext.repositorySchule().auswahlSchuljahresabschnitt().schuljahr() + 1,
					reportingContext.repositorySchule().auswahlSchuljahresabschnitt().abschnitt() + 3));
		}

		try {
			final GostKlausurenCollectionAllData allData = this.reportingContext.repositoryGost().klausurplanDaten(selection);
			final GostKlausurplanManager gostKlausurManager =
					new GostKlausurplanManager(allData);

			this.gostKlausurplan = new ProxyReportingGostKlausurplanungKlausurplan(this.reportingContext, gostKlausurManager);

			// Daten-Context für Thymeleaf erzeugen.
			final Context context = new Context();
			context.setVariable("GostKlausurplan", this.gostKlausurplan);

			super.setContext(context);
		} catch (final ApiOperationException e) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, e,
					"FEHLER: Zu mindestens einer Stufe konnten keine Klausurplanungsdaten ermittelt werden. Es konnte kein html-Klausuren-Kontext erstellt werden.");
		}
	}
}
