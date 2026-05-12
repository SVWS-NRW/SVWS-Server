package de.svws_nrw.module.reporting.types.gost.kursplanung;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.module.reporting.types.gost.fachwahlstatistik.ProxyReportingGostFachwahlstatistikHalbjahr;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import de.svws_nrw.module.reporting.types.schueler.gost.kursplanung.ProxyReportingSchuelerGostKursplanungKursbelegung;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungBlockungsergebnis und erweitert die Klasse
 *  {@link ReportingGostKursplanungBlockungsergebnis}.
 */
public class ProxyReportingGostKursplanungBlockungsergebnis extends ReportingGostKursplanungBlockungsergebnis {

	/** Repository für das Reporting. */
	@JsonIgnore
	private final ReportingContext reportingContext;

	/** Ergebnismanager des Blockungsergebnisses. */
	@JsonIgnore
	private final GostBlockungsergebnisManager ergebnisManager;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostKursplanungBlockungsergebnis}. Die Filter-Prädikate für
	 * Schüler und Kurse werden über den FilterService aus den Reporting-Parametern abgeleitet.
	 *
	 * @param reportingContext	Repository für das Reporting.
	 * @param blockungsergebnis 	Das GOSt-Blockungsergebnis, welches für das Reporting genutzt werden soll.
	 * @param datenManager 			Der zum Blockungsergebnis gehörige Datenmanager der Blockung.
	 */
	public ProxyReportingGostKursplanungBlockungsergebnis(final ReportingContext reportingContext, final GostBlockungsergebnis blockungsergebnis,
			final GostBlockungsdatenManager datenManager) {
		super(0, 0, 0, 0, 0, 0, "", null, null, blockungsergebnis.id, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
				setFilterSchueler(reportingContext), setFilterKurse(reportingContext), istSchuelerFilterAktiv(reportingContext),
				istKurseFilterAktiv(reportingContext));
		this.reportingContext = reportingContext;

		// Initialisiere den Blockungsergebnis-Manager.
		ergebnisManager = new GostBlockungsergebnisManager(datenManager, blockungsergebnis);

		// Grundwerte des Blockungsergebnisses setzen.
		super.abiturjahr = datenManager.daten().abijahrgang;
		super.anzahlDummy = ergebnisManager.getAnzahlSchuelerDummy();
		super.anzahlExterne = ergebnisManager.getAnzahlSchuelerExterne();
		super.anzahlMaxKurseProSchiene = ergebnisManager.getOfSchieneMaxKursanzahl();
		super.anzahlSchienen = super.schienen.size();
		super.anzahlSchueler = datenManager.schuelerGetAnzahl();
		super.bezeichnung = ersetzeNullBlankTrim(datenManager.daten().name);
		super.gostHalbjahr = GostHalbjahr.fromID(datenManager.daten().gostHalbjahr);

		// Füge die Schüler der Liste der Schüler dieses Blockungsergebnisses hinzu und lege eine interne Map an, um auf die Schüler im Folgenden direkt zugreifen zu können.
		super.schueler.addAll(this.reportingContext.repositorySchueler().schueler(datenManager.schuelerGetListe().stream().map(s -> s.id).toList()));
		final HashMap<Long, ReportingSchueler> mapBlockungsergebnisSchuelermenge = new HashMap<>();
		mapBlockungsergebnisSchuelermenge.putAll(super.schueler.stream().collect(Collectors.toMap(ReportingSchueler::id, s -> s)));

		// Liste der Schienen aus der Blockung einlesen und diese einer internen Map hinzufügen. Dabei werden Schienen ohne Kurse nicht berücksichtigt.
		final HashMap<Long, ReportingGostKursplanungSchiene> mapBlockungsergebnisSchienenmenge = new HashMap<>();
		mapBlockungsergebnisSchienenmenge.putAll(datenManager.schieneGetListe()
				.stream()
				.filter(s -> !ergebnisManager.getOfSchieneKursmengeSortiert(s.id).isEmpty())
				.map(s -> (ReportingGostKursplanungSchiene) new ProxyReportingGostKursplanungSchiene(
						this,
						ergebnisManager.getOfSchieneAnzahlSchuelerDummy(s.id),
						ergebnisManager.getOfSchieneAnzahlSchuelerExterne(s.id),
						ergebnisManager.getOfSchieneAnzahlSchueler(s.id),
						s.bezeichnung,
						ergebnisManager.getOfSchieneHatKollision(s.id),
						s.id,
						ergebnisManager.getOfSchieneKursmengeMitKollisionen(s.id).stream().map(k -> k.id).toList(),
						ergebnisManager.getOfSchieneSchuelermengeMitKollisionen(s.id).stream().toList(),
						new ArrayList<>(),
						s.nummer))
				.toList()
				.stream()
				.collect(Collectors.toMap(ReportingGostKursplanungSchiene::id, s -> s)));

		// Liste der Kurse aus der Blockung einlesen.
		// Dabei werden auch die Kursbelegungen der Schüler und die Kurse bei den Schienen ergänzt.
		for (final GostBlockungKurs kurs : datenManager.kursGetListeSortiertNachKursartFachNummer()) {
			// Liste der Kurslehrer erzeugen.
			final List<ReportingLehrer> kursLehrer = datenManager.kursGetLehrkraefteSortiert(kurs.id)
					.stream()
					.map(l -> reportingContext.repositoryLehrer().lehrer(l.id))
					.toList();

			// Den Kurs der Gost-Kurplanung erzeugen.
			// Darin fehlen die Kurschüler. Diese werden später durch das ProxyKursobjekt nachgeladen (lazy-loading), in dem dort
			// alle Schüler durchlaufen werden und deren Kursbelegung geprüft wird.
			// Zudem fehlen in den Schienen der Schienenliste dieses Kurses die anderen Kurse der Schiene. Auch diese
			// werden später nachgeladen, in dem das ProxySchienenobjekt alle Kurse durchläuft und deren Schienenzugehörigkeit auswertet.
			final int schuljahr = (datenManager.daten().abijahrgang - 3) + (datenManager.daten().gostHalbjahr / 2);
			final ReportingGostKursplanungKurs reportingGostKursplanungKurs = new ProxyReportingGostKursplanungKurs(
					this,
					ergebnisManager.getOfKursAnzahlSchuelerAbiturLK(kurs.id),
					ergebnisManager.getOfKursAnzahlSchuelerAbitur3(kurs.id),
					ergebnisManager.getOfKursAnzahlSchuelerAbitur4(kurs.id),
					ergebnisManager.getOfKursAnzahlSchuelerDummy(kurs.id),
					ergebnisManager.getOfKursAnzahlSchuelerExterne(kurs.id),
					ergebnisManager.getOfKursAnzahlSchueler(kurs.id),
					ergebnisManager.getOfKursAnzahlSchuelerSchriftlich(kurs.id),
					datenManager.kursGetName(kurs.id),
					this.reportingContext.repositorySchule().schuljahresabschnitt(schuljahr, (datenManager.daten().gostHalbjahr % 2) + 1)
							.fach(datenManager.kursGet(kurs.id).fach_id),
					null,
					GostHalbjahr.fromID(datenManager.daten().gostHalbjahr),
					GostKursart.fromID(ergebnisManager.getKursE(kurs.id).kursart),
					kurs.id,
					kursLehrer,
					ergebnisManager.getOfKursSchienenmenge(kurs.id).stream().map(s -> mapBlockungsergebnisSchienenmenge.get(s.id)).toList(),
					new ArrayList<>());

			// Ergänze bei den Schülern die Kursbelegung mit dem neuen Kurs (ohne die Mitschüler des Kurses).
			// Es kann der Fall auftreten, dass z. B. durch Wiederholung Kursbelegungen ohne Fachwahlen auftreten. Diese werden mit Standardwerten gefüllt.
			for (final long idKursschueler : ergebnisManager.getOfKursSchuelermenge(kurs.id).stream().map(s -> s.id).toList()) {
				ergaenzeKursbelegung(idKursschueler, kurs.id, reportingGostKursplanungKurs, mapBlockungsergebnisSchuelermenge);
			}

			// Füge den neuen Kurs in die Liste der Kurse der entsprechenden Schienen ein.
			reportingGostKursplanungKurs.schienen().forEach(s -> mapBlockungsergebnisSchienenmenge.get(s.id()).kurse().add(reportingGostKursplanungKurs));

			// Füge den neuen Kurs in die Liste der Kurse ein und initialisiere damit schrittweise die Liste der Super-Klasse.
			super.kurse.add(reportingGostKursplanungKurs);

			// Aktualisiere die Map der Kursplanungskurse im Repository.
			this.reportingContext.repositoryGost().kursplanungKurse().clear();
			this.reportingContext.repositoryGost().kursplanungKurse()
					.putAll(super.kurse.stream().collect(Collectors.toMap(ReportingGostKursplanungKurs::id,
							k -> k)));
		}

		// Erstelle eine Liste von Schienen aus dem Blockungsergebnis und initialisiere damit die Liste der Super-Klasse.
		datenManager.schieneGetListe()
				.stream()
				.filter(s -> !ergebnisManager.getOfSchieneKursmengeSortiert(s.id).isEmpty())
				.toList()
				.forEach(s -> super.schienen.add(mapBlockungsergebnisSchienenmenge.get(s.id)));
	}


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostKursplanungBlockungsergebnis} mit explizit
	 * übergebenen Schüler- und Kurs-Prädikaten. Wird verwendet, um Sub-Kontexte (z. B. für Einzelausgaben) auf einen
	 * einzelnen Schüler oder Kurs einzuschränken. Die zugrunde liegenden Listen werden aus einem bereits aufgebauten
	 * Reporting-Objekt übernommen.
	 *
	 * @param reportingContext    Repository für das Reporting.
	 * @param quelle                 Ein bereits aufgebautes Reporting-Objekt, dessen Listen wiederverwendet werden.
	 * @param filterSchueler         Ein Prädikat, das bestimmt, welche Schüler in der Ausgabe der Hauptliste enthalten sind.
	 * @param filterKurse            Ein Prädikat, das bestimmt, welche Kurse in der Ausgabe enthalten sind.
	 * @param istSchuelerFilterAktiv Gibt an, ob auf der Hauptliste der Schüler ein Filter angewendet wird.
	 * @param istKurseFilterAktiv    Gibt an, ob auf der Liste der Kurse ein Filter angewendet wird.
	 */
	public ProxyReportingGostKursplanungBlockungsergebnis(final ReportingContext reportingContext,
			final ReportingGostKursplanungBlockungsergebnis quelle,
			final Predicate<ReportingSchueler> filterSchueler, final Predicate<ReportingGostKursplanungKurs> filterKurse,
			final boolean istSchuelerFilterAktiv, final boolean istKurseFilterAktiv) {
		super(quelle.abiturjahr(), quelle.anzahlDummy(), quelle.anzahlExterne(), quelle.anzahlMaxKurseProSchiene(),
				quelle.anzahlSchienen(), quelle.anzahlSchueler(), quelle.bezeichnung(), quelle.fachwahlstatistik(),
				quelle.gostHalbjahr(), quelle.id(),
				new ArrayList<>(quelle.kurse), new ArrayList<>(quelle.schienen()), new ArrayList<>(quelle.schueler),
				filterSchueler, filterKurse, istSchuelerFilterAktiv, istKurseFilterAktiv);
		this.reportingContext = reportingContext;
		this.ergebnisManager = null;
	}


	private static Predicate<ReportingSchueler> setFilterSchueler(final ReportingContext reportingContext) {
		return (reportingContext == null)
				? s -> true
				: reportingContext.filterService().getFilter(ReportingSchueler.class.getSimpleName(), null);
	}

	private static Predicate<ReportingGostKursplanungKurs> setFilterKurse(final ReportingContext reportingContext) {
		return (reportingContext == null)
				? k -> true
				: reportingContext.filterService().getFilter(ReportingGostKursplanungKurs.class.getSimpleName(), null);
	}

	private static boolean istSchuelerFilterAktiv(final ReportingContext reportingContext) {
		return (reportingContext != null)
				&& reportingContext.filterService().hatFilter(ReportingSchueler.class.getSimpleName());
	}

	private static boolean istKurseFilterAktiv(final ReportingContext reportingContext) {
		return (reportingContext != null)
				&& reportingContext.filterService().hatFilter(ReportingGostKursplanungKurs.class.getSimpleName());
	}


	private void ergaenzeKursbelegung(final long idKursschueler, final long kursId, final ReportingGostKursplanungKurs reportingGostKursplanungKurs,
			final HashMap<Long, ReportingSchueler> mapBlockungsergebnisSchuelermenge) {
		String fachwahlAbiturfach = "";
		boolean fachwahlGueltig = false;
		boolean fachwahlSchriftlich = false;
		try {
			final GostFachwahl gostFachwahl = ergebnisManager.getOfSchuelerOfKursFachwahl(idKursschueler, kursId);
			fachwahlAbiturfach = (gostFachwahl.abiturfach != null) ? String.valueOf(gostFachwahl.abiturfach) : "";
			fachwahlGueltig = true;
			fachwahlSchriftlich = gostFachwahl.istSchriftlich;
		} catch (final Exception e) {
			ReportingExceptionUtils.logException(
					"INFO: Fehler mit definiertem Rückgabewert abgefangen aufgrund fehlender Fachwahl eines Schülers bei dessen Kursplanungskursbelegung.",
					e, reportingContext.logger(), LogLevel.INFO, 0);
		}
		mapBlockungsergebnisSchuelermenge.get(idKursschueler).gostKursplanungKursbelegungen()
				.add(new ProxyReportingSchuelerGostKursplanungKursbelegung(fachwahlAbiturfach, fachwahlGueltig, fachwahlSchriftlich,
						reportingGostKursplanungKurs));
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
			final List<GostStatistikFachwahl> gostFachwahlenStatistik = this.reportingContext.repositoryGost().fachwahlen(super.abiturjahr());
			if (!gostFachwahlenStatistik.isEmpty()) {
				mapFachwahlStatistik.putAll(
						gostFachwahlenStatistik.stream().collect(
								Collectors.toMap(
										f -> f.id,
										f -> (ReportingGostKursplanungFachwahlstatistik) new ProxyReportingGostKursplanungFachwahlstatistik(
												new ProxyReportingGostFachwahlstatistikHalbjahr(this.reportingContext, this.gostHalbjahr(), f),
												this.ergebnisManager))));
			}
			super.fachwahlstatistik = mapFachwahlStatistik;
		}
		return super.fachwahlstatistik();
	}

}
