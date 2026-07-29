package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 * Ein Thymeleaf-html-Daten-Context zum Bereich "GostKlausurplanung", der die Aufteilung in Einzel-Contexts pro
 * Kurs unterstützt. Wird für Vorlagen verwendet, bei denen die Einzelausgabe pro Kurs erfolgen soll.
 *
 * <p><b>Hinweis:</b> Diese Klasse wird derzeit von der {@code HtmlFactory} nicht erzeugt, weil die zugehörige
 * Reportvorlage (Klausurplan je Kurs, analog zu {@code GostKlausurplanung-SchuelerMitKlausuren}) noch aussteht.
 * Sie ist damit <b>kein toter Code, sondern Vorbereitung</b> und bleibt erhalten; sobald die Vorlage im Enum
 * {@code ReportingReportvorlage} ergänzt ist, genügt ein weiterer Zweig in der Context-Auswahl der
 * {@code HtmlFactory}. Der Hinweis steht hier, damit die Klasse bei einer Durchsicht nicht erneut als
 * Löschkandidat geprüft werden muss.</p>
 */
public final class HtmlContextGostKlausurplanungKlausurplanKurse extends HtmlContextGostKlausurplanungKlausurplan
		implements HtmlContextAufteilbar<HtmlContextGostKlausurplanungKlausurplanKurse> {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingContext	Context mit Parametern, Logger und Daten zum Reporting.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public HtmlContextGostKlausurplanungKlausurplanKurse(final ReportingContext reportingContext) throws ApiOperationException {
		super(reportingContext);
	}

	private HtmlContextGostKlausurplanungKlausurplanKurse(final ReportingContext reportingContext,
			final ReportingGostKlausurplanungKlausurplan gostKlausurplan,
			final Predicate<ReportingSchueler> filterSchueler, final Predicate<ReportingKurs> filterKurse,
			final Predicate<ReportingGostKlausurplanungKlausurtermin> filterKlausurtermine) {
		super(reportingContext, gostKlausurplan, filterSchueler, filterKurse, filterKlausurtermine);
	}


	/**
	 * Teile diesen Context mit allen Kursen in eine Liste von Contexts auf, die jeweils einen Kurs enthalten. Damit können Ausgaben pro Kurs erzeugt werden.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	@Override
	public List<HtmlContextGostKlausurplanungKlausurplanKurse> getEinzelContexts() {
		final List<HtmlContextGostKlausurplanungKlausurplanKurse> result = new ArrayList<>();
		for (final ReportingKurs k : this.gostKlausurplan.kurse()) {
			final long id = k.id();
			final Predicate<ReportingKurs> einzelFilterKurs = kurs -> kurs.id() == id;
			result.add(new HtmlContextGostKlausurplanungKlausurplanKurse(
					this.reportingContext, this.gostKlausurplan, s -> true, einzelFilterKurs, kt -> true));
		}
		return result;
	}

	@Override
	public List<Long> getIds() {
		return this.gostKlausurplan.kurse().stream().map(ReportingKurs::id).distinct().toList();
	}
}
