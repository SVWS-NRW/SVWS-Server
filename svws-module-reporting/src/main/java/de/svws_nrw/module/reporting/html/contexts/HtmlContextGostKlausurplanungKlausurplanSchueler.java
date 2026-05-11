package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 * Ein Thymeleaf-html-Daten-Context zum Bereich "GostKlausurplanung", der die Aufteilung in Einzel-Contexts pro
 * Schüler unterstützt. Wird für Vorlagen wie "Klausurplan der Schülerinnen und Schüler" verwendet, bei denen die
 * Einzelausgabe pro Schüler erfolgen soll.
 */
public final class HtmlContextGostKlausurplanungKlausurplanSchueler extends HtmlContextGostKlausurplanungKlausurplan
		implements HtmlContextAufteilbar<HtmlContextGostKlausurplanungKlausurplanSchueler> {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingRepository	Repository mit Parametern, Logger und Daten zum Reporting.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public HtmlContextGostKlausurplanungKlausurplanSchueler(final ReportingRepository reportingRepository) throws ApiOperationException {
		super(reportingRepository);
	}

	private HtmlContextGostKlausurplanungKlausurplanSchueler(final ReportingRepository reportingRepository,
			final ReportingGostKlausurplanungKlausurplan gostKlausurplan,
			final Predicate<ReportingSchueler> filterSchueler, final Predicate<ReportingKurs> filterKurse,
			final Predicate<ReportingGostKlausurplanungKlausurtermin> filterKlausurtermine) {
		super(reportingRepository, gostKlausurplan, filterSchueler, filterKurse, filterKlausurtermine);
	}


	/**
	 * Teile diesen Context mit allen Schülern in eine Liste von Contexts auf, die jeweils einen Schüler enthalten. Damit können Ausgaben pro Schüler erzeugt
	 * werden.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	@Override
	public List<HtmlContextGostKlausurplanungKlausurplanSchueler> getEinzelContexts() {
		final List<HtmlContextGostKlausurplanungKlausurplanSchueler> result = new ArrayList<>();
		for (final ReportingSchueler s : this.gostKlausurplan.schueler()) {
			final long id = s.id();
			final Predicate<ReportingSchueler> einzelFilterSchueler = sch -> sch.id() == id;
			result.add(new HtmlContextGostKlausurplanungKlausurplanSchueler(
					this.reportingRepository, this.gostKlausurplan, einzelFilterSchueler, k -> true, kt -> true));
		}
		return result;
	}

	@Override
	public List<Long> getIds() {
		return this.gostKlausurplan.schueler().stream().map(ReportingSchueler::id).distinct().toList();
	}
}
