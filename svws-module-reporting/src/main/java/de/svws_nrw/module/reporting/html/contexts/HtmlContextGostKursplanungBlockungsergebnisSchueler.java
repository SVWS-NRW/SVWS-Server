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
 * Schüler unterstützt. Wird für Vorlagen wie "Schüler mit Schienen-/Kurszuordnung" verwendet, bei denen die
 * Einzelausgabe pro Schüler erfolgen soll.
 */
public final class HtmlContextGostKursplanungBlockungsergebnisSchueler extends HtmlContextGostKursplanungBlockungsergebnis
		implements HtmlContextAufteilbar<HtmlContextGostKursplanungBlockungsergebnisSchueler> {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingContext	Context mit Parametern, Logger und Daten zum Reporting.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public HtmlContextGostKursplanungBlockungsergebnisSchueler(final ReportingContext reportingContext) throws ApiOperationException {
		super(reportingContext);
	}

	/**
	 * Erzeugt einen Einzel-Sub-Context, der auf einen einzelnen Schüler eingeschränkt ist und das bereits aufgebaute
	 * Blockungsergebnis aus dem aufrufenden Context wiederverwendet. Wird ausschließlich intern von {@link #getEinzelContexts()}
	 * für die Einzelausgabe pro Schüler verwendet.
	 *
	 * @param reportingContext	Context mit Parametern, Logger und Daten zum Reporting.
	 * @param quelle				Das bereits aufgebaute Blockungsergebnis, das als Datenquelle wiederverwendet wird.
	 * @param filterSchueler		Ein Prädikat, das den einen Schüler der Einzelausgabe selektiert.
	 * @param filterKurse			Ein Prädikat, das bestimmt, welche Kurse in der Ausgabe enthalten sind.
	 */
	private HtmlContextGostKursplanungBlockungsergebnisSchueler(final ReportingContext reportingContext,
			final ReportingGostKursplanungBlockungsergebnis quelle,
			final Predicate<ReportingSchueler> filterSchueler, final Predicate<ReportingGostKursplanungKurs> filterKurse) {
		super(reportingContext, quelle, filterSchueler, filterKurse);
		getContext().setVariable("zeigeProSchueler", true);
	}


	/**
	 * Teile diesen Context mit allen Schülern in eine Liste von Contexts auf, die jeweils einen Schüler enthalten. Damit können Ausgaben pro Schüler erzeugt
	 * werden.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	@Override
	public List<HtmlContextGostKursplanungBlockungsergebnisSchueler> getEinzelContexts() {
		final List<HtmlContextGostKursplanungBlockungsergebnisSchueler> result = new ArrayList<>();
		for (final ReportingSchueler s : this.blockungsergebnis.schueler()) {
			final long id = s.id();
			final Predicate<ReportingSchueler> einzelFilterSchueler = sch -> sch.id() == id;
			result.add(new HtmlContextGostKursplanungBlockungsergebnisSchueler(
					this.reportingContext, this.blockungsergebnis, einzelFilterSchueler, k -> true));
		}
		return result;
	}

	@Override
	public List<Long> getIds() {
		return this.blockungsergebnis.schueler().stream().map(ReportingSchueler::id).distinct().toList();
	}

	/**
	 * Die Zähleinheit dieser Sichtweise sind die Schüler des Blockungsergebnisses: die vorhandenen gegen die nach Filterung ausgegebenen.
	 * <p>Die angeforderte Zahl stammt aus {@code anzahlSchueler()} und damit aus den Blockungsdaten selbst. Die Schülerliste des Ergebnisses taugt dafür
	 * nicht: Sie entsteht über das zentrale Schüler-Repository und ist bereits um ausgefilterte und nicht ladbare Schüler verkürzt. Aus ihr gebildet, meldete
	 * der Header eine vollständige Ausgabe, obwohl Datensätze fehlen.</p>
	 *
	 * @param ergebnis Das aufgebaute Blockungsergebnis dieses Reports.
	 *
	 * @return Der Ausgabeumfang dieser Sichtweise.
	 */
	@Override
	protected ReportingAusgabeumfang ermittleAusgabeumfang(final ReportingGostKursplanungBlockungsergebnis ergebnis) {
		final int ausgegeben = ergebnis.schueler().size();
		return new ReportingAusgabeumfang(ergebnis.anzahlSchueler(), ausgegeben, ausgegeben == 0);
	}
}
