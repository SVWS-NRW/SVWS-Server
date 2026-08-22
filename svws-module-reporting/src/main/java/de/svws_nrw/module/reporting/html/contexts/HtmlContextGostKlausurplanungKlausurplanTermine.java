package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurenHalbjahresdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingAusgabeumfang;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryGostKlausurplanung;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 * Ein Thymeleaf-html-Daten-Context zum Bereich "GostKlausurplanung", der die Aufteilung in Einzel-Contexts pro
 * Klausurtermin unterstützt. Wird für Vorlagen verwendet, bei denen die Einzelausgabe pro Klausurtermin erfolgen soll.
 */
public final class HtmlContextGostKlausurplanungKlausurplanTermine extends HtmlContextGostKlausurplanungKlausurplan
		implements HtmlContextAufteilbar<HtmlContextGostKlausurplanungKlausurplanTermine> {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingContext	Context mit Parametern, Logger und Daten zum Reporting.
	 * @param selection			Die vom Initializer ausgewählten Stufen (Abiturjahrgang und GOSt-Halbjahr).
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public HtmlContextGostKlausurplanungKlausurplanTermine(final ReportingContext reportingContext,
			final List<GostKlausurenHalbjahresdaten> selection) throws ApiOperationException {
		super(reportingContext, selection);
	}

	private HtmlContextGostKlausurplanungKlausurplanTermine(final ReportingContext reportingContext,
			final ReportingGostKlausurplanungKlausurplan gostKlausurplan,
			final Predicate<ReportingSchueler> filterSchueler, final Predicate<ReportingKurs> filterKurse,
			final Predicate<ReportingGostKlausurplanungKlausurtermin> filterKlausurtermine) {
		super(reportingContext, gostKlausurplan, filterSchueler, filterKurse, filterKlausurtermine);
	}


	/**
	 * Teile diesen Context mit allen Klausurterminen in eine Liste von Contexts auf, die jeweils einen Klausurtermin enthalten.
	 * Damit können Ausgaben pro Klausurtermin erzeugt werden.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	@Override
	public List<HtmlContextGostKlausurplanungKlausurplanTermine> getEinzelContexts() {
		final List<HtmlContextGostKlausurplanungKlausurplanTermine> result = new ArrayList<>();
		for (final ReportingGostKlausurplanungKlausurtermin t : this.gostKlausurplan.klausurtermine()) {
			final long id = t.id();
			final Predicate<ReportingGostKlausurplanungKlausurtermin> einzelFilterTermin = termin -> termin.id() == id;
			result.add(new HtmlContextGostKlausurplanungKlausurplanTermine(
					this.reportingContext, this.gostKlausurplan, s -> true, k -> true, einzelFilterTermin));
		}
		return result;
	}

	@Override
	public List<Long> getIds() {
		return this.gostKlausurplan.klausurtermine().stream().map(ReportingGostKlausurplanungKlausurtermin::id).distinct().toList();
	}

	/**
	 * Die Zähleinheit dieser Sichtweise sind die Klausurtermine des Klausurplans: die im Plan vorhandenen gegen die nach Filterung ausgegebenen.
	 *
	 * @param repo Das initialisierte Repository der GOSt-Klausurplanung.
	 *
	 * @return Der Ausgabeumfang dieser Sichtweise.
	 */
	@Override
	protected ReportingAusgabeumfang ermittleAusgabeumfang(final ReportingRepositoryGostKlausurplanung repo) {
		final int ausgegeben = repo.klausurtermine().size();
		return new ReportingAusgabeumfang(repo.anzahlKlausurtermineVorhanden(), ausgegeben, ausgegeben == 0);
	}
}
