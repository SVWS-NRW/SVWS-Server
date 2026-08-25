package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingAusgabeumfang;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 * Ein Thymeleaf-html-Daten-Context zum Bereich "GostKursplanung", der die Aufteilung in Einzel-Contexts pro
 * Kurs unterstützt. Wird für Vorlagen wie "Kurs mit Kursschülern" verwendet, bei denen die Einzelausgabe pro
 * Kurs erfolgen soll.
 */
public final class HtmlContextGostKursplanungBlockungsergebnisKurse extends HtmlContextGostKursplanungBlockungsergebnis
		implements HtmlContextAufteilbar<HtmlContextGostKursplanungBlockungsergebnisKurse> {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingContext	Context mit Parametern, Logger und Daten zum Reporting.
	 *
	 * @throws ApiOperationException	Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung.
	 */
	public HtmlContextGostKursplanungBlockungsergebnisKurse(final ReportingContext reportingContext) throws ApiOperationException {
		super(reportingContext);
	}

	/**
	 * Erzeugt einen Einzel-Sub-Context, der auf einen einzelnen Kurs eingeschränkt ist und das bereits aufgebaute
	 * Blockungsergebnis aus dem aufrufenden Context wiederverwendet. Wird ausschließlich intern von {@link #getEinzelContexts()}
	 * für die Einzelausgabe pro Kurs verwendet.
	 *
	 * @param reportingContext	Context mit Parametern, Logger und Daten zum Reporting.
	 * @param quelle				Das bereits aufgebaute Blockungsergebnis, das als Datenquelle wiederverwendet wird.
	 * @param filterSchueler		Ein Prädikat, das bestimmt, welche Schüler in der Ausgabe enthalten sind.
	 * @param filterKurse			Ein Prädikat, das den einen Kurs der Einzelausgabe selektiert.
	 */
	private HtmlContextGostKursplanungBlockungsergebnisKurse(final ReportingContext reportingContext,
			final ReportingGostKursplanungBlockungsergebnis quelle,
			final Predicate<ReportingSchueler> filterSchueler, final Predicate<ReportingGostKursplanungKurs> filterKurse) {
		super(reportingContext, quelle, filterSchueler, filterKurse);
	}


	/**
	 * Teile diesen Context mit allen Kursen in eine Liste von Contexts auf, die jeweils einen Kurs enthalten. Damit können Ausgaben pro Kurs erzeugt
	 * werden.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	@Override
	public List<HtmlContextGostKursplanungBlockungsergebnisKurse> getEinzelContexts() {
		final List<HtmlContextGostKursplanungBlockungsergebnisKurse> result = new ArrayList<>();
		for (final ReportingGostKursplanungKurs k : this.blockungsergebnis.kurse()) {
			final long id = k.id();
			final Predicate<ReportingGostKursplanungKurs> einzelFilterKurs = kurs -> kurs.id() == id;
			result.add(new HtmlContextGostKursplanungBlockungsergebnisKurse(
					this.reportingContext, this.blockungsergebnis, s -> true, einzelFilterKurs));
		}
		return result;
	}

	@Override
	public List<Long> getIds() {
		return this.blockungsergebnis.kurse().stream().map(ReportingGostKursplanungKurs::id).distinct().toList();
	}

	/**
	 * Die Zähleinheit dieser Sichtweise sind die Kurse des Blockungsergebnisses: die vorhandenen gegen die nach Filterung ausgegebenen.
	 *
	 * @param ergebnis Das aufgebaute Blockungsergebnis dieses Reports.
	 *
	 * @return Der Ausgabeumfang dieser Sichtweise.
	 */
	@Override
	protected ReportingAusgabeumfang ermittleAusgabeumfang(final ReportingGostKursplanungBlockungsergebnis ergebnis) {
		final int ausgegeben = ergebnis.kurse().size();
		return new ReportingAusgabeumfang(ergebnis.anzahlKurseVorhanden(), ausgegeben, ausgegeben == 0);
	}
}
