package de.svws_nrw.module.reporting.types.schueler.gost.laufbahnplanung;

import java.util.ArrayList;
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
import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.asd.data.schueler.Sprachpruefung;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import jakarta.validation.constraints.NotNull;
import de.svws_nrw.module.reporting.types.gost.laufbahnplanung.ProxyReportingGostLaufbahnplanungErgebnismeldung;
import de.svws_nrw.module.reporting.types.gost.laufbahnplanung.ProxyReportingGostLaufbahnplanungFachwahl;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.gost.laufbahnplanung.ReportingGostLaufbahnplanungErgebnismeldung;
import de.svws_nrw.module.reporting.types.gost.laufbahnplanung.ReportingGostLaufbahnplanungErgebnismeldungKategorie;
import de.svws_nrw.module.reporting.types.gost.laufbahnplanung.ReportingGostLaufbahnplanungFachwahl;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ SchuelerGostLaufbahnplanung und erweitert die Klasse {@link ReportingSchuelerGostLaufbahnplanung}.
 */
public class ProxyReportingSchuelerGostLaufbahnplanung extends ReportingSchuelerGostLaufbahnplanung {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;

	/** Das im Repository ausgewählte Schuljahr. Auf dessen Basis werden die Statistikdaten geladen. */
	@JsonIgnore
	private final int auswahlSchuljahr;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchuelerGostLaufbahnplanung}.
	 *
	 * @param reportingContext Repository für das Reporting.
	 * @param reportingSchueler	Schüler, dessen GOSt-Laufbahnplanung gelesen werden soll.
	 */
	public ProxyReportingSchuelerGostLaufbahnplanung(final ReportingContext reportingContext, final ReportingSchueler reportingSchueler) {
		super(0, "", "", "", "", "", new ArrayList<>(), "", new ArrayList<>(), new ArrayList<>(), "", "", new ArrayList<>(), "", null, "", "", 0, 0, 0, 0, 0, 0,
				0, "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

		this.reportingContext = reportingContext;
		this.auswahlSchuljahr = this.reportingContext.repositorySchule().auswahlSchuljahresabschnitt().schuljahr();

		// Im Folgenden werden die GOSt-Laufbahnplanungsdaten geladen. Sollten dabei Fehler auftreten, so wird der Vorgang abgebrochen und die Daten des
		// Objekts sind die Daten aus der Initialisierung.

		// ##### Abiturdaten laden
		final Abiturdaten abiturdaten = this.reportingContext.repositoryGost().beratungsdatenAbiturdaten(reportingSchueler.id());
		if ((abiturdaten == null) || (abiturdaten.abiturjahr <= 0)) {
			return;
		}
		super.abiturjahr = abiturdaten.abiturjahr;

		// ##### Jahrgangsdaten und Fächer laden und Manager initialisieren
		final GostJahrgangsdaten gostJahrgangsdaten;
		final GostFaecherManager gostFaecherManager;
		try {
			gostJahrgangsdaten = this.reportingContext.repositoryGost().jahrgangsdaten(super.abiturjahr());
			gostFaecherManager = this.reportingContext.repositoryGost().faecherManager(super.abiturjahr());
		} catch (final ApiOperationException e) {
			ReportingExceptionUtils.logException("INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der GOSt-Laufbahnplanung "
					+ "eines Schülers (Fächer und Jahrgänge).", e, reportingContext.logger(), LogLevel.INFO, 0);
			return;
		}
		final AbiturdatenManager abiturdatenManager = new AbiturdatenManager(abiturdaten, gostJahrgangsdaten,
				gostFaecherManager, GostBelegpruefungsArt.GESAMT);

		// ##### Beratungsdaten laden
		final GostLaufbahnplanungBeratungsdaten schuelerBeratungsdaten =
				this.reportingContext.repositoryGost().beratungsdaten(reportingSchueler.id());
		if (schuelerBeratungsdaten == null) {
			return;
		}

		// ##### Grunddaten setzen
		setzeGrunddaten(gostJahrgangsdaten, schuelerBeratungsdaten, reportingSchueler);

		// ##### Kurse und Wochenstunden setzen
		setzeKurseUndWochenstunden(abiturdatenManager);

		// ##### Fachwahlliste erstellen
		super.fachwahlen = getListFachwahlen(abiturdaten, gostFaecherManager);

		// ##### Fehlerliste und Hinweisliste erstellen
		erstelleFehlerUndHinweisliste(abiturdatenManager);
	}

	private void setzeGrunddaten(final GostJahrgangsdaten gostJahrgangsdaten, final GostLaufbahnplanungBeratungsdaten schuelerBeratungsdaten,
			final ReportingSchueler reportingSchueler) {
		super.beratungsbogenText = ersetzeNullBlankTrim(gostJahrgangsdaten.textBeratungsbogen);
		super.emailText = ersetzeNullBlankTrim(gostJahrgangsdaten.textMailversand);

		eintragBeratungGostHalbjahreErzeugen();

		if (reportingSchueler.aktuellerLernabschnitt() != null) {
			super.pruefungsordnung = reportingSchueler.aktuellerLernabschnitt().pruefungsOrdnung();
			if (!super.pruefungsordnung().toLowerCase().contains("gost")) {
				super.pruefungsordnung = "APO-GOSt";
			}
			super.aktuelleKlasse = (reportingSchueler.aktuellerLernabschnitt().klasse() != null) ? reportingSchueler.aktuellerLernabschnitt().klasse().kuerzel() : "";
		} else {
			super.pruefungsordnung = "APO-GOSt";
			super.aktuelleKlasse = "";
		}

		eintragBeratungslehrkraefteErzeugen(schuelerBeratungsdaten, gostJahrgangsdaten);
		super.letzterRuecklaufDatum = ersetzeNullBlankTrim(schuelerBeratungsdaten.ruecklaufdatum);
		super.letzteBeratungDatum = ersetzeNullBlankTrim(schuelerBeratungsdaten.beratungsdatum);
		super.kommentar = ersetzeNullBlankTrim(schuelerBeratungsdaten.kommentar);
	}

	private void setzeKurseUndWochenstunden(final AbiturdatenManager abiturdatenManager) {
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
	}

	private void erstelleFehlerUndHinweisliste(final AbiturdatenManager abiturdatenManager) {
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
	}



	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingContext reportingContext() {
		return reportingContext;
	}



	/**
	 * Setzt die Beratungshalbjahre für den Schüler gemäß dem ausgewählten Schuljahresabschnitt und dem Abiturjahrgang.
	 */
	private void eintragBeratungGostHalbjahreErzeugen() {
		final GostHalbjahr aktuellesGostHalbjahrAbiturjahrgang = GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(abiturjahr,
				this.reportingContext.repositorySchule().aktuellerSchuljahresabschnitt().schuljahr(),
				this.reportingContext.repositorySchule().aktuellerSchuljahresabschnitt().abschnitt());
		final GostHalbjahr auswahlGostHalbjahrAbiturjahrgang = GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(abiturjahr,
				this.reportingContext.repositorySchule().auswahlSchuljahresabschnitt().schuljahr(),
				this.reportingContext.repositorySchule().auswahlSchuljahresabschnitt().abschnitt());

		super.aktuellesGOStHalbjahr = bestimmeAktuellesHalbjahr(aktuellesGostHalbjahrAbiturjahrgang);
		super.folgeAktuellesGOStHalbjahr =
				bestimmeFolgeHalbjahr(aktuellesGostHalbjahrAbiturjahrgang,
						this.reportingContext.repositorySchule().aktuellerSchuljahresabschnitt().schuljahr(), abiturjahr);

		super.auswahlGOStHalbjahr = bestimmeAktuellesHalbjahr(auswahlGostHalbjahrAbiturjahrgang);
		super.folgeAuswahlGOStHalbjahr =
				bestimmeFolgeHalbjahr(auswahlGostHalbjahrAbiturjahrgang, this.reportingContext.repositorySchule().auswahlSchuljahresabschnitt().schuljahr(),
						abiturjahr);
	}

	/**
	 * Bestimmt das Kürzel für das aktuelle Halbjahr.
	 *
	 * @param gostHalbjahr Das zu prüfende GOSt-Halbjahr.
	 *
	 * @return Das Kürzel oder ein leerer String, wenn das Halbjahr null ist.
	 */
	private String bestimmeAktuellesHalbjahr(final GostHalbjahr gostHalbjahr) {
		return (gostHalbjahr != null) ? gostHalbjahr.kuerzel : "";
	}

	/**
	 * Bestimmt das Kürzel für das Folge-Halbjahr basierend auf dem GOSt-Halbjahr und dem Schuljahr.
	 *
	 * @param gostHalbjahr Das zu prüfende GOSt-Halbjahr.
	 * @param schuljahr    Das zugrundeliegende Schuljahr.
	 * @param abiturjahr   Das Abiturjahr des Schülers.
	 *
	 * @return Das Kürzel für das Folge-Halbjahr.
	 */
	private String bestimmeFolgeHalbjahr(final GostHalbjahr gostHalbjahr, final int schuljahr, final int abiturjahr) {
		if (gostHalbjahr != null) {
			return (gostHalbjahr.next() != null) ? gostHalbjahr.next().kuerzel : GostHalbjahr.Q22.kuerzel;
		}
		// Hier muss entweder ein Jahr vor EF.1 oder nach Q2.2 vorliegen. Prüfe mittels Abiturjahr.
		return (schuljahr >= abiturjahr) ? GostHalbjahr.Q22.kuerzel : GostHalbjahr.EF1.kuerzel;
	}


	/**
	 * Setzt die Daten der Lehrkraft der letzten Beratung und der Beratungslehrkräfte der Jahrgangsstufe
	 *
	 * @param gostBeratungsdaten	Oberstufenschüler, dessen Beratungslehrkräfte ermitteln werden sollen.
	 * @param gostJahrgangsdaten	Die Daten des GOSt-Jahrgangs.
	 */
	private void eintragBeratungslehrkraefteErzeugen(final GostLaufbahnplanungBeratungsdaten gostBeratungsdaten, final GostJahrgangsdaten gostJahrgangsdaten) {
		// Letzte Beratungslehrkraft aus den GOSt-Daten des Schülers bestimmen.
		if (gostBeratungsdaten.beratungslehrerID != null) {
			super.letzteBeratungLehrkraft = this.reportingContext.repositoryLehrer().lehrer(gostBeratungsdaten.beratungslehrerID);
		}
		// Beratungslehrkräfte der Stufe bestimmen aus den GOSt-Daten der Jahrgangsstufe
		final List<GostBeratungslehrer> beratungslehrer = gostJahrgangsdaten.beratungslehrer;
		if (!beratungslehrer.isEmpty()) {
			for (final GostBeratungslehrer lehrkraft : beratungslehrer) {
				final ReportingLehrer reportingLehrer = this.reportingContext.repositoryLehrer().lehrer(lehrkraft.id);
				if (reportingLehrer != null) {
					super.beratungslehrkraefte().add(reportingLehrer);
				}
			}
			super.beratungslehrkraefte().sort(ReportingLehrer.SORTIERUNG.comparatorStandard());
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
			if (!checkBelegungInGostMoeglich(fach)) {
				continue;
			}

			fachwahlen.add(erstelleFachwahl(fach, belegungen, sprachbelegungen, sprachpruefungen, abiturdaten));
		}

		return fachwahlen;
	}

	/**
	 * Eine Hilfsmethode, um eine einzelne Fachwahl für die Laufbahnplanung zu erstellen.
	 *
	 * @param fach Das zu verarbeitende GOSt-Fach.
	 * @param belegungen Map mit Fachbelegungen.
	 * @param sprachbelegungen Map mit Sprachbelegungen.
	 * @param sprachpruefungen Map mit Sprachprüfungen.
	 * @param abiturdaten Abiturdaten des Schülers.
	 *
	 * @return Ein fertig befülltes Objekt vom Typ ProxyReportingGostLaufbahnplanungFachwahl.
	 */
	private ProxyReportingGostLaufbahnplanungFachwahl erstelleFachwahl(final GostFach fach, final Map<Long, AbiturFachbelegung> belegungen,
			final Map<String, Sprachbelegung> sprachbelegungen, final Map<String, Sprachpruefung> sprachpruefungen, final Abiturdaten abiturdaten) {

		// Variablen initialisieren
		String abiturfach = "";
		String belegungEF1 = "";
		String belegungEF2 = "";
		String belegungQ11 = "";
		String belegungQ12 = "";
		String belegungQ21 = "";
		String belegungQ22 = "";
		boolean fachBelegtInGost = false;

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

		final SprachdatenErgebnis sprachdaten = bestimmeSprachdaten(fach, sprachbelegungen, sprachpruefungen, abiturdaten);

		return new ProxyReportingGostLaufbahnplanungFachwahl(
				abiturfach,
				belegungEF1, belegungEF2, belegungQ11, belegungQ12, belegungQ21, belegungQ22,
				reportingContext.repositorySchule().auswahlSchuljahresabschnitt().fach(fach.id),
				fachBelegtInGost,
				sprachdaten.istFortfuehrbareFremdspracheInGOSt(),
				sprachdaten.jahrgangFremdsprachenbeginn(),
				sprachdaten.positionFremdsprachenfolge());
	}

	/**
	 * Eine Hilfsklasse zur Speicherung von Sprachdaten für die gymnasiale Oberstufe (GOSt) eines Schülers.
	 *
	 * @param istFortfuehrbareFremdspracheInGOSt Gibt an, ob es sich um eine fortführbare Fremdsprache in der gymnasialen Oberstufe handelt.
	 * @param jahrgangFremdsprachenbeginn        Der Jahrgang, in dem die Fremdsprache erstmals begonnen wurde.
	 * @param positionFremdsprachenfolge         Die Position der Fremdsprache in der Sprachenfolge (z. B. erste, zweite Fremdsprache etc.).
	 */
	private record SprachdatenErgebnis(boolean istFortfuehrbareFremdspracheInGOSt, String jahrgangFremdsprachenbeginn, String positionFremdsprachenfolge) {
	}

	/**
	 * Eine Hilfsmethode zur Bestimmung der Sprachdaten für ein bestimmtes Fach.
	 *
	 * @param fach Das zu prüfende GOSt-Fach.
	 * @param sprachbelegungen Map mit Sprachbelegungen.
	 * @param sprachpruefungen Map mit Sprachprüfungen.
	 * @param abiturdaten Abiturdaten des Schülers.
	 *
	 * @return Ein Record mit den ermittelten Sprachinformationen.
	 */
	private SprachdatenErgebnis bestimmeSprachdaten(final GostFach fach, final Map<String, Sprachbelegung> sprachbelegungen,
			final Map<String, Sprachpruefung> sprachpruefungen, final Abiturdaten abiturdaten) {

		final @NotNull Fach zfach = Fach.getBySchluesselOrDefault(fach.kuerzel);

		if (!checkIstFremdsprachenfach(fach, zfach)) {
			return new SprachdatenErgebnis(false, "", "");
		}

		final String sprachKuerzel = zfach.daten(auswahlSchuljahr).kuerzel;
		final Sprachbelegung sprachbelegung = sprachbelegungen.get(sprachKuerzel);
		final Sprachpruefung sprachpruefung = sprachpruefungen.get(sprachKuerzel);

		if ((sprachbelegung != null) && checkSprachbelegungsbeginn(fach, sprachbelegung, zfach)) {
			// Nur Sprachen heranziehen, die auch vor oder mit der eigenen Belegung hätten starten können.
			final String beginn = sprachbelegung.belegungVonJahrgang;
			final String reihenfolge = (sprachbelegung.reihenfolge != null) ? sprachbelegung.reihenfolge.toString() : "";
			return new SprachdatenErgebnis(true, beginn, reihenfolge);
		}

		if ((sprachpruefung != null) && SprachendatenUtils.istFortfuehrbareSpracheInGOSt(abiturdaten.sprachendaten, sprachKuerzel)) {
			final String beginn = getSprachpruefungBeginn(sprachpruefung);
			final String reihenfolge = getSprachpruefungReihenfolge(sprachpruefung);
			return new SprachdatenErgebnis(true, beginn, reihenfolge);
		}

		return new SprachdatenErgebnis(false, "", "");
	}

	/**
	 * Gibt den Beginn der Sprachprüfung anhand der Art der Prüfung zurück.
	 *
	 * @param sprachpruefung Die Sprachprüfung, deren Beginn bestimmt werden soll.
	 *
	 * @return Ein Kürzel für die Art der Sprachprüfung ("SFP" für Sprachfeststellungsprüfung,
	 *         "HSU" für herkunftssprachlichen Unterricht) oder ein leerer String, wenn keine der Bedingungen zutrifft.
	 */
	private String getSprachpruefungBeginn(final Sprachpruefung sprachpruefung) {
		if (sprachpruefung.istFeststellungspruefung) {
			return "SFP";
		}
		if (sprachpruefung.istHSUPruefung) {
			return "HSU";
		}
		return "";
	}

	/**
	 * Bestimmt die Reihenfolge, in der eine Sprachprüfung eine Pflicht- oder Wahlpflichtfremdsprache ersetzen kann.
	 *
	 * @param sprachpruefung Die zu prüfende Sprachprüfung, deren Ersetzungsreihenfolge ermittelt werden soll.
	 *
	 * @return "1", wenn die Sprachprüfung die erste Pflichtfremdsprache ersetzen kann,
	 *         "2", wenn sie die zweite Pflichtfremdsprache oder eine Wahlpflichtfremdsprache ersetzen kann,
	 *         oder ein leerer String, wenn keine der Bedingungen zutrifft.
	 */
	private String getSprachpruefungReihenfolge(final Sprachpruefung sprachpruefung) {
		if (sprachpruefung.kannErstePflichtfremdspracheErsetzen) {
			return "1";
		}
		if (sprachpruefung.kannZweitePflichtfremdspracheErsetzen || sprachpruefung.kannWahlpflichtfremdspracheErsetzen) {
			return "2";
		}
		return "";
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
		if (abJahrgang == null) {
			return true;
		}
		if (Jahrgaenge.data().getWertByBezeichner(abJahrgang).daten(auswahlSchuljahr) == null) {
			return false;
		}
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
		if (belegungHj == null) {
			return "";
		}

		final GostKursart kursart = GostKursart.fromKuerzel(belegungHj.kursartKuerzel);
		if (kursart == GostKursart.GK) {
			return belegungHj.schriftlich ? "S" : "M";
		}
		if (kursart == GostKursart.LK) {
			return "LK";
		}
		if ((kursart == GostKursart.PJK) || (kursart == GostKursart.VTF)) {
			return "M";
		}
		if (kursart == GostKursart.ZK) {
			return "ZK";
		}
		if ("AT".equals(belegungHj.kursartKuerzel)) {
			return "AT";
		}
		return "";
	}

}
