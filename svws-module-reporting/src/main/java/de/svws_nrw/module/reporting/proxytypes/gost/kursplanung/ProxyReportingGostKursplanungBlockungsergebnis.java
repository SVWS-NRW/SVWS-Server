package de.svws_nrw.module.reporting.proxytypes.gost.kursplanung;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.data.gost.DataGostAbiturjahrgangFachwahlen;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import de.svws_nrw.module.reporting.proxytypes.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.reporting.proxytypes.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.proxytypes.schueler.gost.kursplanung.ProxyReportingSchuelerGostKursplanungKursbelegung;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungFachwahlstatistik;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungKurs;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungSchiene;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungBlockungsergebnis und erweitert die Klasse
 *  {@link ReportingGostKursplanungBlockungsergebnis}.
 */
public class ProxyReportingGostKursplanungBlockungsergebnis extends ReportingGostKursplanungBlockungsergebnis {

	/** Repository für das Reporting. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/** Ergebnismanager des Blockungsergebnisses. */
	@JsonIgnore
	private final GostBlockungsergebnisManager ergebnisManager;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostKursplanungBlockungsergebnis}.
	 *
	 * @param reportingRepository	Repository für das Reporting.
	 * @param blockungsergebnis 	Das GOSt-Blockungsergebnis, welches für das Reporting genutzt werden soll.
	 * @param datenManager 			Der zum Blockungsergebnis gehörige Datenmanager der Blockung.
	 */
	public ProxyReportingGostKursplanungBlockungsergebnis(final ReportingRepository reportingRepository, final GostBlockungsergebnis blockungsergebnis,
			final GostBlockungsdatenManager datenManager) {
		super(0, 0, 0, 0, 0, 0, "", null, null, blockungsergebnis.id, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		this.reportingRepository = reportingRepository;

		// Initialisiere den Blockungsergebnis-Manager.
		ergebnisManager = new GostBlockungsergebnisManager(datenManager, blockungsergebnis);

		// Sortierungen definieren.
		ergebnisManager.kursSetSortierungKursartFachNummer();
		final Collator colGerman = java.text.Collator.getInstance(Locale.GERMAN);

		// Grundwerte des Blockungsergebnisses setzen.
		super.abiturjahr = datenManager.daten().abijahrgang;
		super.anzahlDummy = ergebnisManager.getAnzahlSchuelerDummy();
		super.anzahlExterne = ergebnisManager.getAnzahlSchuelerExterne();
		super.anzahlMaxKurseProSchiene = ergebnisManager.getOfSchieneMaxKursanzahl();
		super.anzahlSchienen = super.schienen().size();
		super.anzahlSchueler = datenManager.schuelerGetAnzahl();
		super.bezeichnung = ersetzeNullBlankTrim(datenManager.daten().name);
		super.gostHalbjahr = GostHalbjahr.fromID(datenManager.daten().gostHalbjahr);

		// Schülerstammdaten ermitteln und in Listen und Maps einfügen
		final List<SchuelerStammdaten> schuelerStammdaten = new ArrayList<>();
		final List<Long> fehlendeSchueler = new ArrayList<>();
		for (final Long idSchueler : datenManager.schuelerGetListe().stream().map(s -> s.id).toList()) {
			if (reportingRepository.mapSchuelerStammdaten().containsKey(idSchueler))
				schuelerStammdaten.add(reportingRepository.mapSchuelerStammdaten().get(idSchueler));
			else
				fehlendeSchueler.add(idSchueler);
		}
		if (!fehlendeSchueler.isEmpty()) {
			final List<SchuelerStammdaten> fehlendeSchuelerStammdaten = DataSchuelerStammdaten.getListStammdaten(reportingRepository.conn(), fehlendeSchueler);
			schuelerStammdaten.addAll(fehlendeSchuelerStammdaten);
			fehlendeSchuelerStammdaten.forEach(s -> this.reportingRepository.mapSchuelerStammdaten().putIfAbsent(s.id, s));
		}

		// Füge die neu erzeugten Reporting-Objekte der Schüler der internen Map hinzu, um auf die Schüler im Folgenden direkt zugreifen zu können.
		final HashMap<Long, ReportingSchueler> mapBlockungsergebnisSchuelermenge = new HashMap<>();
		mapBlockungsergebnisSchuelermenge.putAll(schuelerStammdaten
				.stream()
				.map(s -> (ReportingSchueler) new ProxyReportingSchueler(
						reportingRepository,
						s))
				.toList()
				.stream()
				.collect(Collectors.toMap(ReportingSchueler::id, s -> s)));

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
			List<ReportingLehrer> kursLehrer = new ArrayList<>();
			if (!datenManager.kursGetLehrkraefteSortiert(kurs.id).isEmpty())
				kursLehrer = datenManager.kursGetLehrkraefteSortiert(kurs.id)
						.stream()
						.map(l -> (ReportingLehrer) new ProxyReportingLehrer(
								reportingRepository,
								reportingRepository.mapLehrerStammdaten().computeIfAbsent(l.id, ls -> {
									try {
										return new DataLehrerStammdaten(reportingRepository.conn()).getById(l.id);
									} catch (final ApiOperationException e) {
										ReportingExceptionUtils.putStacktraceInLog(
												"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der Stammdaten eines Lehrers.", e,
												reportingRepository.logger(), LogLevel.INFO, 0);
										return new LehrerStammdaten();
									}
								})))
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
					this.reportingRepository.schuljahresabschnitt(schuljahr, (datenManager.daten().gostHalbjahr % 2) + 1)
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
				String fachwahlAbiturfach = "";
				boolean fachwahlGueltig = false;
				boolean fachwahlSchriftlich = false;
				try {
					final GostFachwahl gostFachwahl = ergebnisManager.getOfSchuelerOfKursFachwahl(idKursschueler, kurs.id);
					if (gostFachwahl != null) {
						fachwahlAbiturfach = "" + (gostFachwahl.abiturfach != null ? gostFachwahl.abiturfach : "");
						fachwahlGueltig = true;
						fachwahlSchriftlich = gostFachwahl.istSchriftlich;
					}
				} catch (final Exception e) {
					ReportingExceptionUtils.putStacktraceInLog(
							"INFO: Fehler mit definiertem Rückgabewert abgefangen aufgrund fehlender Fachwahl eines Schülers bei dessen Kursplanungskursbelegung.",
							e, reportingRepository.logger(), LogLevel.INFO, 0);
				}
				mapBlockungsergebnisSchuelermenge.get(idKursschueler).gostKursplanungKursbelegungen()
						.add(new ProxyReportingSchuelerGostKursplanungKursbelegung(fachwahlAbiturfach, fachwahlGueltig, fachwahlSchriftlich,
								reportingGostKursplanungKurs));
			}

			// Füge den neuen Kurs in die Liste der Kurse der entsprechenden Schienen ein.
			reportingGostKursplanungKurs.schienen().forEach(s -> mapBlockungsergebnisSchienenmenge.get(s.id()).kurse().add(reportingGostKursplanungKurs));

			// Füge den neuen Kurs in die Liste der Kurse ein und initialisiere damit schrittweise die Liste der Super-Klasse.
			super.kurse().add(reportingGostKursplanungKurs);
		}

		// Erstelle eine Liste von Schienen aus dem Blockungsergebnis und initialisiere damit die Liste der Super-Klasse.
		datenManager.schieneGetListe()
				.stream()
				.filter(s -> !ergebnisManager.getOfSchieneKursmengeSortiert(s.id).isEmpty())
				.toList()
				.forEach(s -> super.schienen().add(mapBlockungsergebnisSchienenmenge.get(s.id)));

		// Erstelle eine alphabetisch sortierte Liste der Schülermenge aus dem Blockungsergebnis und initialisiere damit die Liste der Super-Klasse.
		super.schueler().addAll(mapBlockungsergebnisSchuelermenge.values()
				.stream()
				.sorted(Comparator.comparing(ReportingSchueler::nachname, colGerman)
						.thenComparing(ReportingSchueler::vorname, colGerman)
						.thenComparing(ReportingSchueler::vornamen, colGerman)
						.thenComparing(ReportingSchueler::id))
				.toList());
		this.reportingRepository.mapSchueler().putAll(super.schueler.stream().collect(Collectors.toMap(ReportingSchueler::id, s -> s)));
	}


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	@JsonIgnore
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}


	/**
	 * Map mit den Fachwahlstatistiken des GOSt-Halbjahres des Blockungsergebnisses zur Fach-ID
	 *
	 * @return Map mit den Fachwahlstatistiken zu den Fächern.
	 */
	@Override
	public Map<Long, ReportingGostKursplanungFachwahlstatistik> fachwahlstatistik() {
		if ((super.fachwahlstatistik() == null) || super.fachwahlstatistik().isEmpty()) {
			final Map<Long, ReportingGostKursplanungFachwahlstatistik> mapFachwahlStatistik = new HashMap<>();
			final DataGostAbiturjahrgangFachwahlen gostAbiturjahrgangFachwahlen =
					new DataGostAbiturjahrgangFachwahlen(reportingRepository.conn(), super.abiturjahr());
			try {
				final List<GostStatistikFachwahl> gostFachwahlenStatistik = gostAbiturjahrgangFachwahlen.getFachwahlen();
				if ((gostFachwahlenStatistik != null) && (!gostFachwahlenStatistik.isEmpty())) {
					mapFachwahlStatistik.putAll(
							gostFachwahlenStatistik.stream().collect(
									Collectors.toMap(
											f -> f.id,
											f -> (ReportingGostKursplanungFachwahlstatistik) new ProxyReportingGostKursplanungFachwahlstatistik(
													this.reportingRepository, this.gostHalbjahr(), f, this.ergebnisManager))));
				}
			} catch (final ApiOperationException e) {
				ReportingExceptionUtils.putStacktraceInLog(
						"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der GOSt-Fachwahlstatistik.", e,
						reportingRepository.logger(), LogLevel.INFO, 0);
			}
			super.fachwahlstatistik = mapFachwahlStatistik;
		}
		return super.fachwahlstatistik();
	}

}
