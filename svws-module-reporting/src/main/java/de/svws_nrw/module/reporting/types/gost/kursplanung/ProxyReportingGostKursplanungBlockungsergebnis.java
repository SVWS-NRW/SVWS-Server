package de.svws_nrw.module.reporting.types.gost.kursplanung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.gost.fachwahlstatistik.ProxyReportingGostFachwahlstatistikHalbjahr;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungBlockungsergebnis und erweitert die Klasse
 * {@link ReportingGostKursplanungBlockungsergebnis}.
 */
public class ProxyReportingGostKursplanungBlockungsergebnis extends ReportingGostKursplanungBlockungsergebnis {

	/** Repository für das Reporting. */
	@JsonIgnore
	private final ReportingContext reportingContext;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostKursplanungBlockungsergebnis}. Wird vom
	 * Domänen-Repository {@code ReportingRepositoryGostKursplanung} verwendet, um das aufgebaute Blockungsergebnis
	 * mit bereits gefilterten Schüler-/Kurs-/Schienen-Listen zu kapseln.
	 *
	 * @param reportingContext         Context mit Parametern, Logger und Daten zum Reporting.
	 * @param abiturjahr               Das Kalenderjahr, in dem die Abiturprüfung des Blockungsergebnisses stattfindet.
	 * @param anzahlDummy              Anzahl der Dummy-Schüler im Ergebnis.
	 * @param anzahlExterne            Anzahl der externen Schüler im Ergebnis.
	 * @param anzahlMaxKurseProSchiene Maximale Anzahl an Kursen pro Schiene über alle Schienen.
	 * @param anzahlSchienen           Anzahl der Schienen (nur jene mit Kursen).
	 * @param anzahlSchueler           Anzahl der Schüler im Ergebnis.
	 * @param bezeichnung              Bezeichnung des Blockungsergebnisses (wird intern auf Null/Blank getrimmt).
	 * @param gostHalbjahr             Das Halbjahr der gymnasialen Oberstufe des Blockungsergebnisses.
	 * @param id                       ID des Blockungsergebnisses.
	 * @param kurse                    Mutable Liste der Kurse — wird vom Repository nach der Erzeugung gefüllt.
	 * @param schienen                 Mutable Liste der Schienen — wird vom Repository nach der Erzeugung gefüllt.
	 * @param schueler                 Liste der Schüler des Blockungsergebnisses.
	 */
	@SuppressWarnings("java:S107") // Konstruktoren mit zu vielen Parametern (gemäß SonarQube) werden aktuell toleriert und nicht refacored (Stand 2026-04).
	public ProxyReportingGostKursplanungBlockungsergebnis(final ReportingContext reportingContext, final int abiturjahr, final int anzahlDummy,
			final int anzahlExterne, final int anzahlMaxKurseProSchiene, final int anzahlSchienen, final int anzahlSchueler, final String bezeichnung,
			final GostHalbjahr gostHalbjahr, final long id, final List<ReportingGostKursplanungKurs> kurse,
			final List<ReportingGostKursplanungSchiene> schienen, final List<ReportingSchueler> schueler) {
		super(abiturjahr, anzahlDummy, anzahlExterne, anzahlMaxKurseProSchiene, anzahlSchienen, anzahlSchueler,
				ersetzeNullBlankTrim(bezeichnung), null, gostHalbjahr, id, kurse, schienen, schueler,
				ReportingSchueler.FILTER.bedingung(
						reportingContext.filterService().getFilter(ReportingSchueler.class.getSimpleName()), null),
				ReportingGostKursplanungKurs.FILTER.bedingung(
						reportingContext.filterService().getFilter(ReportingGostKursplanungKurs.class.getSimpleName()), null));
		this.reportingContext = reportingContext;
	}


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostKursplanungBlockungsergebnis} mit explizit
	 * übergebenen Schüler- und Kurs-Prädikaten. Wird verwendet, um Sub-Kontexte (z. B. für Einzelausgaben) auf einen
	 * einzelnen Schüler oder Kurs einzuschränken. Die zugrunde liegenden Listen werden aus einem bereits aufgebauten
	 * Reporting-Objekt übernommen.
	 *
	 * @param reportingContext       Context mit Parametern, Logger und Daten zum Reporting.
	 * @param quelle                 Ein bereits aufgebautes Reporting-Objekt, dessen Listen wiederverwendet werden.
	 * @param filterSchueler         Ein Prädikat, das bestimmt, welche Schüler in der Ausgabe der Hauptliste enthalten sind.
	 * @param filterKurse            Ein Prädikat, das bestimmt, welche Kurse in der Ausgabe enthalten sind.
	 */
	public ProxyReportingGostKursplanungBlockungsergebnis(final ReportingContext reportingContext,
			final ReportingGostKursplanungBlockungsergebnis quelle, final Predicate<ReportingSchueler> filterSchueler,
			final Predicate<ReportingGostKursplanungKurs> filterKurse) {
		super(quelle.abiturjahr(), quelle.anzahlDummy(), quelle.anzahlExterne(), quelle.anzahlMaxKurseProSchiene(),
				quelle.anzahlSchienen(), quelle.anzahlSchueler(), quelle.bezeichnung(), quelle.fachwahlstatistik(),
				quelle.gostHalbjahr(), quelle.id(),
				new ArrayList<>(quelle.kurse), new ArrayList<>(quelle.schienen()), new ArrayList<>(quelle.schueler),
				filterSchueler, filterKurse);
		this.reportingContext = reportingContext;
	}


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	@JsonIgnore
	public ReportingContext reportingContext() {
		return reportingContext;
	}


	/**
	 * Map mit den Fachwahlstatistiken des GOSt-Halbjahres des Blockungsergebnisses zur Fach-ID
	 *
	 * @return Map mit den Fachwahlstatistiken zur Fach-ID.
	 */
	@Override
	public Map<Long, ReportingGostKursplanungFachwahlstatistik> fachwahlstatistik() {
		if ((super.fachwahlstatistik() == null) || super.fachwahlstatistik().isEmpty()) {
			final Map<Long, ReportingGostKursplanungFachwahlstatistik> mapFachwahlStatistik = new HashMap<>();
			// Die Fachwahl-Anzahlen aus der Laufbahnplanung reichern die Kursausgabe an: Ein Ladefehler wird gemeldet, die Kurse erscheinen weiterhin und die
			// Vorlage zeigt an den Statistik-Stellen ihren Platzhalter.
			final List<GostStatistikFachwahl> gostFachwahlenStatistik = this.reportingContext.repositoryGost().fachwahlenOptional(super.abiturjahr());
			if (!gostFachwahlenStatistik.isEmpty()) {
				mapFachwahlStatistik.putAll(
						gostFachwahlenStatistik.stream().collect(
								Collectors.toMap(
										f -> f.id,
										f -> (ReportingGostKursplanungFachwahlstatistik) new ProxyReportingGostKursplanungFachwahlstatistik(
												new ProxyReportingGostFachwahlstatistikHalbjahr(this.reportingContext, this.gostHalbjahr(), f),
												this.reportingContext.repositoryGostKursplanung().manager()))));
			}
			super.fachwahlstatistik = mapFachwahlStatistik;
		}
		return super.fachwahlstatistik();
	}

}
