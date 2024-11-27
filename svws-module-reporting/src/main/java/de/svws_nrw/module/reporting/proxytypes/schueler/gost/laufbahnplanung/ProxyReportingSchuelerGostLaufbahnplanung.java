package de.svws_nrw.module.reporting.proxytypes.schueler.gost.laufbahnplanung;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnis;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnisFehler;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehlerArt;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostBeratungslehrer;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.core.data.schueler.Sprachbelegung;
import de.svws_nrw.core.data.schueler.Sprachpruefung;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.data.gost.DBUtilsGostLaufbahn;
import de.svws_nrw.data.gost.DataGostJahrgangFachkombinationen;
import de.svws_nrw.data.gost.DataGostJahrgangsdaten;
import de.svws_nrw.data.gost.DataGostSchuelerLaufbahnplanungBeratungsdaten;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import jakarta.validation.constraints.NotNull;
import de.svws_nrw.module.reporting.proxytypes.gost.laufbahnplanung.ProxyReportingGostLaufbahnplanungErgebnismeldung;
import de.svws_nrw.module.reporting.proxytypes.gost.laufbahnplanung.ProxyReportingGostLaufbahnplanungFachwahl;
import de.svws_nrw.module.reporting.proxytypes.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.laufbahnplanung.ReportingGostLaufbahnplanungErgebnismeldung;
import de.svws_nrw.module.reporting.types.gost.laufbahnplanung.ReportingGostLaufbahnplanungErgebnismeldungKategorie;
import de.svws_nrw.module.reporting.types.gost.laufbahnplanung.ReportingGostLaufbahnplanungFachwahl;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.gost.laufbahnplanung.ReportingSchuelerGostLaufbahnplanung;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ SchuelerGostLaufbahnplanung und erweitert die Klasse {@link ReportingSchuelerGostLaufbahnplanung}.
 */
public class ProxyReportingSchuelerGostLaufbahnplanung extends ReportingSchuelerGostLaufbahnplanung {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/** Das im Repository ausgewählte Schuljahr. Auf dessen Basis werden die Statistikdaten geladen. */
	@JsonIgnore
	private final int auswahlSchuljahr;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchuelerGostLaufbahnplanung}.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 * @param reportingSchueler	Schüler, dessen GOSt-Laufbahnplanung gelesen werden soll.
	 */
	public ProxyReportingSchuelerGostLaufbahnplanung(final ReportingRepository reportingRepository, final ReportingSchueler reportingSchueler) {
		super(0, "", "", "", "", "", new ArrayList<>(), "", new ArrayList<>(), new ArrayList<>(), "", "", new ArrayList<>(), "", null, "", "", 0, 0, 0, 0, 0, 0,
				0, "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

		this.reportingRepository = reportingRepository;
		this.auswahlSchuljahr = this.reportingRepository.auswahlSchuljahresabschnitt().schuljahr();

		// Abiturdaten zum Schüler holen. Wenn zum Schüler kein Abiturjahr gefunden wird, dann wird er übergangen. Die Daten sind dann die aus der
		// Initialisierung.
		final Abiturdaten abiturdaten;
		try {
			abiturdaten = DBUtilsGostLaufbahn.get(this.reportingRepository.conn(), reportingSchueler.id());
		} catch (final ApiOperationException e) {
			ReportingExceptionUtils.putStacktraceInLog(
					"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der GOSt-Laufbahnplanung eines Schülers (Abitur).", e,
					reportingRepository.logger(), LogLevel.INFO, 0);
			return;
		}
		if (abiturdaten.abiturjahr <= 0) {
			return;
		}
		super.abiturjahr = abiturdaten.abiturjahr;

		// Weitere Datenobjekte erstellen. Prüfe zunächst, ob alle notwendigen Daten ermittelt werden können und speichere sie zwischen.
		// Im Fehlerfall wird nur das initialisierte Reporting-Objekt zurückgegeben.
		final GostLaufbahnplanungBeratungsdaten tempGostLaufbahnplanungBeratungsdaten;
		final GostJahrgangsdaten tempGostJahrgangsdaten;
		final GostFaecherManager tempGostFaecherManager;
		try {
			tempGostLaufbahnplanungBeratungsdaten =
					new DataGostSchuelerLaufbahnplanungBeratungsdaten(this.reportingRepository.conn()).getFromID(reportingSchueler.id());
			tempGostJahrgangsdaten = DataGostJahrgangsdaten.getJahrgangsdaten(this.reportingRepository.conn(), super.abiturjahr());
			tempGostFaecherManager =
					DBUtilsFaecherGost.getFaecherManager(auswahlSchuljahr, this.reportingRepository.conn(),
							super.abiturjahr());
		} catch (final ApiOperationException e) {
			ReportingExceptionUtils.putStacktraceInLog(
					"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der GOSt-Laufbahnplanung eines Schülers (Manager).", e,
					reportingRepository.logger(), LogLevel.INFO, 0);
			return;
		}

		// Abfragen erfolgreich. Erstelle die Maps und Manager, welche zum Abiturjahr die notwendigen Informationen liefern, und ergänze sie jeweils bei Bedarf.
		final GostLaufbahnplanungBeratungsdaten schuelerBeratungsdaten =
				this.reportingRepository.mapGostBeratungsdaten().computeIfAbsent(reportingSchueler.id(), s -> tempGostLaufbahnplanungBeratungsdaten);
		final GostJahrgangsdaten gostJahrgangsdaten =
				this.reportingRepository.mapGostAbiturjahrgangDaten().computeIfAbsent(super.abiturjahr(), a -> tempGostJahrgangsdaten);
		if (!this.reportingRepository.mapGostAbiturjahrgangFaecher().containsKey(super.abiturjahr())) {
			tempGostFaecherManager.addFachkombinationenAll(DataGostJahrgangFachkombinationen.getFachkombinationen(this.reportingRepository.conn(),
					super.abiturjahr()));
			this.reportingRepository.mapGostAbiturjahrgangFaecher().put(super.abiturjahr(), tempGostFaecherManager);
		}
		final GostFaecherManager gostFaecherManager = this.reportingRepository.mapGostAbiturjahrgangFaecher().get(super.abiturjahr());
		final AbiturdatenManager abiturdatenManager = new AbiturdatenManager(abiturdaten, gostJahrgangsdaten, gostFaecherManager, GostBelegpruefungsArt.GESAMT);

		// ##### Grunddaten und Summen setzen ###############
		super.beratungsbogenText = gostJahrgangsdaten.textBeratungsbogen;
		super.emailText = gostJahrgangsdaten.textMailversand;

		// Halbjahre gemäß Abiturjahrgang und Schuljahresabschnitte setzen.
		eintragBeratungGostHalbjahreErzeugen();

		// Aktuelle Prüfungsordnung und Klasse ergänzen, wenn Lernabschnitt vorhanden.
		if (reportingSchueler.aktuellerLernabschnitt() != null) {
			super.pruefungsordnung = reportingSchueler.aktuellerLernabschnitt().pruefungsOrdnung();
			if (!super.pruefungsordnung().toLowerCase().contains("gost"))
				super.pruefungsordnung = "APO-GOSt";
			super.aktuelleKlasse = reportingSchueler.aktuellerLernabschnitt().klasse().kuerzel();
		} else {
			super.pruefungsordnung = "APO-GOSt";
			super.aktuelleKlasse = "";
		}

		eintragBeratungslehrkraefteErzeugen(schuelerBeratungsdaten, gostJahrgangsdaten);
		super.letzterRuecklaufDatum = schuelerBeratungsdaten.ruecklaufdatum;
		super.letzteBeratungDatum = schuelerBeratungsdaten.beratungsdatum;
		super.kommentar = schuelerBeratungsdaten.kommentar;

		final int[] kurse = abiturdatenManager.getAnrechenbareKurse();
		final int[] wochenstunden = abiturdatenManager.getWochenstunden();

		super.kursanzahlEF1 = kurse[0];
		super.kursanzahlEF2 = kurse[1];
		super.kursanzahlQ11 = kurse[2];
		super.kursanzahlQ12 = kurse[3];
		super.kursanzahlQ21 = kurse[4];
		super.kursanzahlQ22 = kurse[5];

		super.kursanzahlQPh = super.kursanzahlQ11() + super.kursanzahlQ12() + super.kursanzahlQ21() + super.kursanzahlQ22();
		super.kursanzahlAnrechenbarBlockI = (abiturdatenManager.getAnrechenbareKurseBlockI());

		super.wochenstundenEF1 = wochenstunden[0];
		super.wochenstundenEF2 = wochenstunden[1];
		super.wochenstundenQ11 = wochenstunden[2];
		super.wochenstundenQ12 = wochenstunden[3];
		super.wochenstundenQ21 = wochenstunden[4];
		super.wochenstundenQ22 = wochenstunden[5];

		super.wochenstundenDurchschnittEF = (super.wochenstundenEF1() + super.wochenstundenEF2()) / 2.0;
		super.wochenstundenDurchschnittQ1 = (super.wochenstundenQ11() + super.wochenstundenQ12()) / 2.0;
		super.wochenstundenDurchschnittQ2 = (super.wochenstundenQ21() + super.wochenstundenQ22()) / 2.0;
		super.wochenstundenDurchschnittQPh = (super.wochenstundenQ11() + super.wochenstundenQ12() + super.wochenstundenQ21() + super.wochenstundenQ22()) / 4.00;
		super.wochenstundenGesamt = (super.wochenstundenEF1() + super.wochenstundenEF2() + super.wochenstundenQ11() + super.wochenstundenQ12()
				+ super.wochenstundenQ21() + super.wochenstundenQ22()) / 2.0;

		// ##### Fachwahlliste erstellen ###############
		super.fachwahlen = getListFachwahlen(abiturdaten, this.reportingRepository.mapGostAbiturjahrgangFaecher().get(abiturdaten.abiturjahr));

		// ##### Fehlerliste und Hinweisliste erstellen ###############
		final GostBelegpruefungErgebnis ergebnis = abiturdatenManager.getBelegpruefungErgebnis();
		for (final GostBelegpruefungErgebnisFehler f : ergebnis.fehlercodes) {
			final GostBelegungsfehlerArt art = GostBelegungsfehlerArt.fromKuerzel(f.art);
			if (art == GostBelegungsfehlerArt.HINWEIS) {
				final ReportingGostLaufbahnplanungErgebnismeldung laufbahnplanungHinweis = new ProxyReportingGostLaufbahnplanungErgebnismeldung(f.code,
						ReportingGostLaufbahnplanungErgebnismeldungKategorie.HINWEIS, f.beschreibung);
				super.hinweise().add(laufbahnplanungHinweis);
			} else {
				final ReportingGostLaufbahnplanungErgebnismeldung laufbahnplanungFehler = new ProxyReportingGostLaufbahnplanungErgebnismeldung(f.code,
						ReportingGostLaufbahnplanungErgebnismeldungKategorie.FEHLER, f.beschreibung);
				super.fehler().add(laufbahnplanungFehler);
			}
		}

		ersetzeStringNullDurchEmpty(this, false);
	}


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}



	/**
	 * Setzt die Beratungshalbjahre für den Schüler gemäß der ausgewählten Schuljahresabschnitte und des Abiturjahrgangs.
	 */
	private void eintragBeratungGostHalbjahreErzeugen() {
		final GostHalbjahr aktuellesGostHalbjahrAbiturjahrgang = GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(abiturjahr,
				this.reportingRepository.aktuellerSchuljahresabschnitt().schuljahr(), this.reportingRepository.aktuellerSchuljahresabschnitt().abschnitt());
		final GostHalbjahr auswahlGostHalbjahrAbiturjahrgang = GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(abiturjahr,
				this.reportingRepository.auswahlSchuljahresabschnitt().schuljahr(), this.reportingRepository.auswahlSchuljahresabschnitt().abschnitt());

		if (aktuellesGostHalbjahrAbiturjahrgang != null) {
			super.aktuellesGOStHalbjahr = aktuellesGostHalbjahrAbiturjahrgang.kuerzel;
			if (aktuellesGostHalbjahrAbiturjahrgang.next() != null)
				super.folgeAktuellesGOStHalbjahr = aktuellesGostHalbjahrAbiturjahrgang.next().kuerzel;
			else
				super.folgeAktuellesGOStHalbjahr = GostHalbjahr.Q22.kuerzel;
		} else {
			// Hier muss entweder ein Jahr vor EF.1 oder nach Q2.2 vorliegen. Prüfe mittels Abiturjahr.
			super.aktuellesGOStHalbjahr = "";
			if (this.reportingRepository.aktuellerSchuljahresabschnitt().schuljahr() >= super.abiturjahr)
				super.folgeAktuellesGOStHalbjahr = GostHalbjahr.Q22.kuerzel;
			else
				super.folgeAktuellesGOStHalbjahr = GostHalbjahr.EF1.kuerzel;
		}

		if (auswahlGostHalbjahrAbiturjahrgang != null) {
			super.auswahlGOStHalbjahr = auswahlGostHalbjahrAbiturjahrgang.kuerzel;
			if (auswahlGostHalbjahrAbiturjahrgang.next() != null)
				super.folgeAuswahlGOStHalbjahr = auswahlGostHalbjahrAbiturjahrgang.next().kuerzel;
			else
				super.folgeAuswahlGOStHalbjahr = GostHalbjahr.Q22.kuerzel;
		} else {
			// Hier muss entweder ein Jahr vor EF.1 oder nach Q2.2 vorliegen. Prüfe mittels Abiturjahr.
			super.auswahlGOStHalbjahr = "";
			if (this.reportingRepository.auswahlSchuljahresabschnitt().schuljahr() >= super.abiturjahr)
				super.folgeAuswahlGOStHalbjahr = GostHalbjahr.Q22.kuerzel;
			else
				super.folgeAuswahlGOStHalbjahr = GostHalbjahr.EF1.kuerzel;
		}
	}


	/**
	 * Setzt die Daten der Lehrkraft der letzten Beratung und der Beratungslehrkräfte der Jahrgangsstufe
	 *
	 * @param gostBeratungsdaten	Oberstufenschüler, dessen Beratungslehrkräfte ermitteln werden sollen.
	 * @param gostJahrgangsdaten	Die Daten des GOSt-Jahrgangs.
	 */
	private void eintragBeratungslehrkraefteErzeugen(final GostLaufbahnplanungBeratungsdaten gostBeratungsdaten, final GostJahrgangsdaten gostJahrgangsdaten) {
		// Letzte Beratungslehrkraft bestimmen aus den GOSt-Daten des Schülers
		if (gostBeratungsdaten.beratungslehrerID != null) {
			super.letzteBeratungLehrkraft = new ProxyReportingLehrer(
					this.reportingRepository,
					this.reportingRepository.mapLehrerStammdaten().computeIfAbsent(gostBeratungsdaten.beratungslehrerID, l -> {
						try {
							return new DataLehrerStammdaten(this.reportingRepository.conn()).getFromID(gostBeratungsdaten.beratungslehrerID);
						} catch (final ApiOperationException e) {
							ReportingExceptionUtils.putStacktraceInLog(
									"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der Stammdaten eines Lehrers.", e,
									reportingRepository.logger(), LogLevel.INFO, 0);
							return new LehrerStammdaten();
						}
					}));
		}
		// Beratungslehrkräfte der Stufe bestimmen aus den GOSt-Daten der Jahrgangsstufe
		final List<GostBeratungslehrer> beratungslehrer = gostJahrgangsdaten.beratungslehrer;
		if (!beratungslehrer.isEmpty()) {
			for (final GostBeratungslehrer lehrkraft : beratungslehrer) {
				super.beratungslehrkraefte().add(new ProxyReportingLehrer(
						this.reportingRepository,
						this.reportingRepository.mapLehrerStammdaten().computeIfAbsent(lehrkraft.id, l -> {
							try {
								return new DataLehrerStammdaten(this.reportingRepository.conn()).getFromID(lehrkraft.id);
							} catch (final ApiOperationException e) {
								ReportingExceptionUtils.putStacktraceInLog(
										"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der Stammdaten eines Lehrers.", e,
										reportingRepository.logger(), LogLevel.INFO, 0);
								return new LehrerStammdaten();
							}
						})));
			}
			super.beratungslehrkraefte().sort(Comparator.comparing(ReportingLehrer::nachname).thenComparing(ReportingLehrer::vorname));
		}
	}


	/**
	 * Erstellt eine Liste von Fachwahlen aus der Laufbahnplanung eines Schülers für die GOSt.
	 *
	 * @param abiturdaten			Abiturdaten des Schülers.
	 * @param gostFaecherManager	Die Fächer des Abiturjahrgangs und im Fächermanager.
	 *
	 * @return Eine Liste der Fachwahlen.
	 */
	private ArrayList<ReportingGostLaufbahnplanungFachwahl> getListFachwahlen(final Abiturdaten abiturdaten, final GostFaecherManager gostFaecherManager) {

		final ArrayList<ReportingGostLaufbahnplanungFachwahl> fachwahlen = new ArrayList<>();

		// Erzeuge eine Map Fach-ID → AbiturFachbelegung aus den AbiturDaten
		final Map<Long, AbiturFachbelegung> belegungen = abiturdaten.fachbelegungen.stream().collect(Collectors.toMap(b -> b.fachID, b -> b));
		// Erzeuge eine Map einstelliges Sprachkürzel → Sprachbelegung aus den AbiturDaten
		final Map<String, Sprachbelegung> sprachbelegungen = abiturdaten.sprachendaten.belegungen.stream().collect(Collectors.toMap(b -> b.sprache, b -> b));
		// Erzeuge eine Map einstelliges Sprachkürzel → Sprachprüfung aus den AbiturDaten
		final Map<String, Sprachpruefung> sprachpruefungen = abiturdaten.sprachendaten.pruefungen.stream().collect(Collectors.toMap(b -> b.sprache, b -> b));

		// Erzeuge für jedes Fach des Abiturjahrgangs eine Zeile, wobei ggf. die Belegungen aus der Map verwendet werden
		for (final GostFach fach : gostFaecherManager.faecher()) {
			if (!checkBelegungInGostMoeglich(fach))
				continue;

			final ProxyReportingGostLaufbahnplanungFachwahl fachwahl;

			// Variablen initialisieren
			String abiturfach = "";
			String belegungEF1 = "";
			String belegungEF2 = "";
			String belegungQ11 = "";
			String belegungQ12 = "";
			String belegungQ21 = "";
			String belegungQ22 = "";
			boolean fachBelegtInGost = false;
			boolean istFortfuehrbareFremdspracheInGOSt = false;
			String jahrgangFremdsprachenbeginn = "";
			String positionFremdsprachenfolge = "";

			final AbiturFachbelegung belegung = belegungen.get(fach.id);
			if (belegung != null) {
				abiturfach = (belegung.abiturFach != null) ? belegung.abiturFach.toString() : "";
				belegungEF1 = eintragFachbelegung(belegung.belegungen[0]);
				belegungEF2 = eintragFachbelegung(belegung.belegungen[1]);
				belegungQ11 = eintragFachbelegung(belegung.belegungen[2]);
				belegungQ12 = eintragFachbelegung(belegung.belegungen[3]);
				belegungQ21 = eintragFachbelegung(belegung.belegungen[4]);
				belegungQ22 = eintragFachbelegung(belegung.belegungen[5]);
				fachBelegtInGost = true;
			}

			// Bestimme noch Einträge zu den Sprachdaten, wenn das Fach eine Sprache ist.
			final @NotNull Fach zfach = Fach.getBySchluesselOrDefault(fach.kuerzel);
			Sprachbelegung sprachbelegung = null;
			Sprachpruefung sprachpruefung = null;

			if (checkIstFremdsprachenfach(fach, zfach)) {
				sprachbelegung = sprachbelegungen.get(zfach.daten(auswahlSchuljahr).kuerzel);
				sprachpruefung = sprachpruefungen.get(zfach.daten(auswahlSchuljahr).kuerzel);
			}

			if (sprachbelegung != null) {
				if (checkSprachbelegungsbeginn(fach, sprachbelegung, zfach)) {
					// Nur Sprachen heranziehen, die auch vor oder mit der eigenen Belegung hätten starten können. So wird bspw. die neue Fremdsprache ab EF nicht durch die Belegung der gleichen Sprache in der Sek-I als belegt markiert.
					istFortfuehrbareFremdspracheInGOSt = true;
					jahrgangFremdsprachenbeginn = sprachbelegung.belegungVonJahrgang;
					positionFremdsprachenfolge = (sprachbelegung.reihenfolge != null) ? sprachbelegung.reihenfolge.toString() : "";
				}
			} else if ((sprachpruefung != null) && (SprachendatenUtils.istFortfuehrbareSpracheInGOSt(abiturdaten.sprachendaten,
					zfach.daten(auswahlSchuljahr).kuerzel))) {
				istFortfuehrbareFremdspracheInGOSt = true;
				if (sprachpruefung.istFeststellungspruefung) {
					jahrgangFremdsprachenbeginn = "SFP";
				} else if (sprachpruefung.istHSUPruefung) {
					jahrgangFremdsprachenbeginn = "HSU";
				}
				if (sprachpruefung.kannErstePflichtfremdspracheErsetzen)
					positionFremdsprachenfolge = "1";
				else if (sprachpruefung.kannZweitePflichtfremdspracheErsetzen || sprachpruefung.kannWahlpflichtfremdspracheErsetzen)
					positionFremdsprachenfolge = "2";
			}

			fachwahl = new ProxyReportingGostLaufbahnplanungFachwahl(
					abiturfach,
					belegungEF1, belegungEF2, belegungQ11, belegungQ12, belegungQ21, belegungQ22,
					reportingRepository.auswahlSchuljahresabschnitt().fach(fach.id),
					fachBelegtInGost,
					istFortfuehrbareFremdspracheInGOSt,
					jahrgangFremdsprachenbeginn,
					positionFremdsprachenfolge);

			fachwahlen.add(fachwahl);
		}

		return fachwahlen;
	}


	/**
	 * Prüft, ob ein Fach in der Oberstufe in mindestens einem Halbjahr belegt werden konnte.
	 *
	 * @param fach	Das zu prüfende Fach
	 *
	 * @return true, wenn eine Belegung in mindestens einem Halbjahr möglich war, sonst false
	 */
	private static boolean checkBelegungInGostMoeglich(final GostFach fach) {
		return (fach.istMoeglichEF1 || fach.istMoeglichEF2 || fach.istMoeglichQ11 || fach.istMoeglichQ12 || fach.istMoeglichQ21 || fach.istMoeglichQ22);
	}


	/**
	 * Prüft, ob das Fach eine Fremdsprache ist und kein Pseudofach der Statistik
	 *
	 * @param fach	Das zu prüfende Fach
	 * @param zfach	Das zu prüfende zugehörige Fach
	 *
	 * @return true, wenn es eine reguläre Fremdsprache ist, sonst false.
	 */
	private static boolean checkIstFremdsprachenfach(final GostFach fach, final Fach zfach) {
		return fach.istFremdsprache && (zfach != null) && !((Fach.PX == zfach) || (Fach.VX == zfach));
	}


	/**
	 * Prüft, ob eine Sprachbelegung vor oder mit der eigenen Belegung hätten starten können. So wird bspw. die neue Fremdsprache ab EF nicht durch die Belegung der gleichen Sprache in der Sek-I als belegt markiert.
	 *
	 * @param fach				Das zu prüfende Fach
	 * @param sprachbelegung 	Die Sprachbelegung aus der Sprachenfolge
	 * @param zfach 			Das zulässige Fach, gegen das der Sprachbeginn geprüft wird.
	 *
	 * @return true, wenn eine Belegung möglich war, sonst false
	 */
	private boolean checkSprachbelegungsbeginn(final GostFach fach, final Sprachbelegung sprachbelegung, final Fach zfach) {
		String abJahrgang = zfach.daten(auswahlSchuljahr).abJahrgang;
		if (abJahrgang == null)
			return true;
		if (Jahrgaenge.data().getWertByBezeichner(abJahrgang).daten(auswahlSchuljahr) == null)
			return false;
		abJahrgang = Jahrgaenge.data().getWertByBezeichner(abJahrgang).daten(auswahlSchuljahr).schluessel;
		return ((sprachbelegung.belegungVonJahrgang != null) && !sprachbelegung.belegungVonJahrgang.isEmpty()) && ((abJahrgang == null) || abJahrgang.isEmpty()
				|| ((abJahrgang.compareToIgnoreCase("EF") >= 0) && fach.istFremdSpracheNeuEinsetzend
						&& (sprachbelegung.belegungVonJahrgang.compareToIgnoreCase("EF") >= 0))
				|| ((abJahrgang.compareToIgnoreCase("EF") < 0) && !fach.istFremdSpracheNeuEinsetzend
						&& (sprachbelegung.belegungVonJahrgang.compareToIgnoreCase("EF") < 0)));
	}


	/**
	 * Gibt den Belegungseintrag eines Faches für die Halbjahres-Belegung zurück.
	 *
	 * @param belegungHj 	Halbjahresbelegung des Faches
	 *
	 * @return 				String mit dem Belegungskürzel des Faches gemäß dessen Halbjahresbelegung
	 */
	private static String eintragFachbelegung(final AbiturFachbelegungHalbjahr belegungHj) {
		if (belegungHj == null)
			return "";

		final GostKursart kursart = GostKursart.fromKuerzel(belegungHj.kursartKuerzel);
		if (kursart == GostKursart.GK)
			return belegungHj.schriftlich ? "S" : "M";
		if (kursart == GostKursart.LK)
			return "LK";
		if ((kursart == GostKursart.PJK) || (kursart == GostKursart.VTF))
			return "M";
		if (kursart == GostKursart.ZK)
			return "ZK";
		if ("AT".equals(belegungHj.kursartKuerzel))
			return "AT";
		return "";
	}

}
