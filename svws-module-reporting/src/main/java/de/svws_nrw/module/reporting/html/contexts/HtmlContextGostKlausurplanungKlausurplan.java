package de.svws_nrw.module.reporting.html.contexts;

import java.util.function.Predicate;

import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingAusgabeumfang;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryGostKlausurplanung;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ProxyReportingGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurplan;


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
	 * Repository und den vom Initializer ausgewählten Stufen aufgebaut; die Filterung der Schüler, Kurse und Klausurtermine erfolgt
	 * zentral im Repository anhand der konfigurierten FILTER-Companions.
	 *
	 * @param reportingContext	Context mit Parametern, Logger und Daten zum Reporting.
	 *
	 * @throws ApiOperationException	Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung.
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
	 * Erzeugt den Context zur GOSt-Klausurplanung. Die Stufen der Auswahl beschafft sich das Repository beim ersten Zugriff selbst.
	 *
	 * @throws ApiOperationException   	im Fehlerfall
	 */
	private void erzeugeContext() throws ApiOperationException {
		try {
			final ReportingRepositoryGostKlausurplanung repo = this.reportingContext.repositoryGostKlausurplanung();

			this.gostKlausurplan = new ProxyReportingGostKlausurplanungKlausurplan(this.reportingContext,
					repo.klausurtermine(), repo.kurse(), repo.kursklausuren(), repo.schueler(), repo.schuelerklausuren());

			// Erst der Manager kennt die Zähleinheiten dieses Datenaufbaus; die Sichtweise bestimmt, ob Schüler, Termine oder Kurse gezählt werden.
			this.reportingContext.meldeAusgabeumfang(ermittleAusgabeumfang(repo));

			final Context context = new Context();
			context.setVariable("GostKlausurplan", this.gostKlausurplan);

			super.setContext(context);
		} catch (final ApiOperationException e) {
			// Der Status der Datenschicht bleibt erhalten; ein pauschaler Status gäbe am API-Rand einen Serverfehler als fehlende Klausurdaten aus.
			// Die Meldung der Ursache reist als cause mit und erscheint im Fehlerblock unter den Fehlergründen.
			throw new ApiOperationException(e.getStatus(), e, "### FEHLER: Die Daten des Klausurplans konnten nicht geladen werden.");
		}
	}

	/**
	 * Ermittelt den Ausgabeumfang dieser Sichtweise aus dem Repository: die im Plan vorhandenen Einheiten gegen die nach Filterung
	 * ausgegebenen. Eine leere Ausgabe ist zulässig, wenn keine Einheit ausgegeben wird - sei es, weil keine Stufe übrig blieb, die Stufen keine Einheiten
	 * enthalten oder der Benutzerfilter alle ausschließt.
	 *
	 * @param repo Das Repository der GOSt-Klausurplanung.
	 *
	 * @return Der Ausgabeumfang dieser Sichtweise.
	 *
	 * @throws ApiOperationException Falls der Aufbau des Klausurplans scheitert.
	 */
	protected abstract ReportingAusgabeumfang ermittleAusgabeumfang(ReportingRepositoryGostKlausurplanung repo) throws ApiOperationException;
}
