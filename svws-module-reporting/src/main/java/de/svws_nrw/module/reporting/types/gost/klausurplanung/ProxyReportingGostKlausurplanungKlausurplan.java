package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import java.util.List;
import java.util.function.Predicate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausurplan und erweitert die Klasse
 *  {@link ReportingGostKlausurplanungKlausurplan}.
 */
public class ProxyReportingGostKlausurplanungKlausurplan extends ReportingGostKlausurplanungKlausurplan {

	/** Repository für das Reporting. */
	@JsonIgnore
	private final ReportingContext reportingContext;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostKlausurplanungKlausurplan} für den Default-Pfad
	 * ohne Schüler-/Kurs-/Klausurtermin-Prädikate. Die zentrale Filterung erfolgt bereits im Repository; die Basisklasse
	 * setzt für die Prädikate intern Allpass-Defaults.
	 *
	 * @param reportingContext	Context mit Parametern, Logger und Daten zum Reporting.
	 * @param klausurtermine		Eine Liste, die alle Termine des Klausurplanes beinhaltet.
	 * @param kurse 				Eine Liste, die alle Kurse des Klausurplanes beinhaltet.
	 * @param kursklausuren 		Eine Liste, die alle Kursklausuren des Klausurplanes beinhaltet.
	 * @param schueler 				Eine Liste, die alle Schüler des Klausurplanes beinhaltet.
	 * @param schuelerklausuren 	Eine Liste, die alle Schülerklausuren des Klausurplanes beinhaltet.
	 */
	public ProxyReportingGostKlausurplanungKlausurplan(final ReportingContext reportingContext,
			final List<ReportingGostKlausurplanungKlausurtermin> klausurtermine, final List<ReportingKurs> kurse,
			final List<ReportingGostKlausurplanungKursklausur> kursklausuren, final List<ReportingSchueler> schueler,
			final List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren) {
		this(reportingContext, klausurtermine, kurse, kursklausuren, schueler, schuelerklausuren, null, null, null);
	}

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostKlausurplanungKlausurplan} mit explizit übergebenen
	 * Schüler-, Kurs- und Klausurtermin-Prädikaten. Wird für Sub-Kontexte (Einzelausgabe pro Schüler/Kurs/Klausurtermin)
	 * verwendet, um die Sicht auf einzelne Entitäten einzuschränken.
	 *
	 * @param reportingContext	Context mit Parametern, Logger und Daten zum Reporting.
	 * @param klausurtermine		Eine Liste, die alle Termine des Klausurplanes beinhaltet.
	 * @param kurse 				Eine Liste, die alle Kurse des Klausurplanes beinhaltet.
	 * @param kursklausuren 		Eine Liste, die alle Kursklausuren des Klausurplanes beinhaltet.
	 * @param schueler 				Eine Liste, die alle Schüler des Klausurplanes beinhaltet.
	 * @param schuelerklausuren 	Eine Liste, die alle Schülerklausuren des Klausurplanes beinhaltet.
	 * @param filterSchueler		Ein Prädikat, das bestimmt, welche Schüler in der Ausgabe enthalten sind.
	 * @param filterKurse			Ein Prädikat, das bestimmt, welche Kurse in der Ausgabe enthalten sind.
	 * @param filterKlausurtermine	Ein Prädikat, das bestimmt, welche Klausurtermine in der Ausgabe enthalten sind.
	 */
	@SuppressWarnings("java:S107") // Konstruktoren mit zu vielen Parametern (gemäß SonarQube) werden aktuell toleriert und nicht refacored (Stand 2026-04).
	public ProxyReportingGostKlausurplanungKlausurplan(final ReportingContext reportingContext,
			final List<ReportingGostKlausurplanungKlausurtermin> klausurtermine, final List<ReportingKurs> kurse,
			final List<ReportingGostKlausurplanungKursklausur> kursklausuren, final List<ReportingSchueler> schueler,
			final List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren,
			final Predicate<ReportingSchueler> filterSchueler, final Predicate<ReportingKurs> filterKurse,
			final Predicate<ReportingGostKlausurplanungKlausurtermin> filterKlausurtermine) {
		super(klausurtermine, kurse, kursklausuren, schueler, schuelerklausuren,
				filterSchueler, filterKurse, filterKlausurtermine);
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

}
