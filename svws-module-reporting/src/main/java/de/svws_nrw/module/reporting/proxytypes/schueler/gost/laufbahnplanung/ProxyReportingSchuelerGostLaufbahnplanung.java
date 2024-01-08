package de.svws_nrw.module.reporting.proxytypes.schueler.gost.laufbahnplanung;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import de.svws_nrw.core.data.schueler.Sprachbelegung;
import de.svws_nrw.core.data.schueler.Sprachpruefung;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.data.gost.DBUtilsGostLaufbahn;
import de.svws_nrw.data.gost.DataGostJahrgangFachkombinationen;
import de.svws_nrw.data.gost.DataGostJahrgangsdaten;
import de.svws_nrw.data.gost.DataGostSchuelerLaufbahnplanungBeratungsdaten;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.module.reporting.proxytypes.gost.laufbahnplanung.ProxyReportingGostLaufbahnplanungErgebnismeldung;
import de.svws_nrw.module.reporting.proxytypes.gost.laufbahnplanung.ProxyReportingGostLaufbahnplanungFachwahl;
import de.svws_nrw.module.reporting.proxytypes.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.laufbahnplanung.ReportingGostLaufbahnplanungErgebnismeldung;
import de.svws_nrw.module.reporting.types.gost.laufbahnplanung.ReportingGostLaufbahnplanungErgebnismeldungKategorie;
import de.svws_nrw.module.reporting.types.gost.laufbahnplanung.ReportingGostLaufbahnplanungFachwahl;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.gost.laufbahnplanung.ReportingSchuelerGostLaufbahnplanung;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ SchuelerGostLaufbahnplanung und erweitert die Klasse {@link ReportingSchuelerGostLaufbahnplanung}.</p>
 *
 *  <p>In diesem Kontext besitzt die Proxy-Klasse ausschließlich die gleichen Methoden wie die zugehörige Reporting-Super-Klasse.
 *  Während die Super-Klasse aber als reiner Datentyp konzipiert ist, d. h. ohne Anbindung an die Datenbank,
 *  greift die Proxy-Klassen an verschiedenen Stellen auf die Datenbank zu.</p>
 *
 *  <ul>
 *      <li>Die Proxy-Klasse stellt in der Regel einen zusätzlichen Constructor zur Verfügung, um Reporting-Objekte
 *  		aus Stammdatenobjekten (aus dem Package core.data) erstellen zu können. Darin werden Felder, die Reporting-Objekte
 *  		zurückgegeben und nicht im Stammdatenobjekt enthalten sind, mit null initialisiert.</li>
 * 		<li>Die Proxy-Klasse überschreibt einzelne Getter der Super-Klasse (beispielsweise bei Felder, die mit null initialisiert wurden)
 *  		und lädt dort dann aus der Datenbank die Daten bei Bedarf nach (lazy-loading), um den Umfang der Datenstrukturen gering zu
 *  		halten.</li>
 * 		<li>Die Proxy-Klasse können zudem auf das Repository {@link ReportingRepository} zugreifen. Dieses
 * 			enthält neben den Stammdaten der Schule einige Maps, in der zur jeweiligen ID bereits ausgelesene Stammdaten anderer Objekte
 * 			wie Kataloge, Jahrgänge, Klassen, Lehrer, Schüler usw. gespeichert werden. So sollen Datenbankzugriffe minimiert werden. Werden in der
 * 			Proxy-Klasse Daten nachgeladen, so werden sie dabei auch in der entsprechenden Map des Repository ergänzt.</li>
 *  </ul>
 */
public class ProxyReportingSchuelerGostLaufbahnplanung extends ReportingSchuelerGostLaufbahnplanung {

	/** Repository für die Reporting */
	@JsonIgnore
	private final ReportingRepository reportingRepository;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis eines Stammdaten-Objektes.
	 * @param reportingRepository Repository für die Reporting.
	 * @param reportingSchueler	Schüler, dessen GOSt-Laufbahnplanung gelesen werden soll.
	 */
	public ProxyReportingSchuelerGostLaufbahnplanung(final ReportingRepository reportingRepository, final ReportingSchueler reportingSchueler) {
		super(0, "", "", "", new ArrayList<>(), "", new ArrayList<>(), new ArrayList<>(), "", new ArrayList<>(), "", null, "", "", 0, 0, 0, 0, 0, 0, 0, "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		this.reportingRepository = reportingRepository;

		// Abiturdaten zum Schüler holen. Wenn zum Schüler kein Abiturjahr gefunden wird, dann wird er übergangen. Die Daten sind an die aus initSchuelerGostLaufbahnplanung.
		final Abiturdaten abiturdaten = DBUtilsGostLaufbahn.get(this.reportingRepository.conn(), reportingSchueler.id());
		if (abiturdaten.abiturjahr <= 0) {
			return;
		}
		super.setAbiturjahr(abiturdaten.abiturjahr);

		// Erstelle die Maps und Manager, welche zum Abiturjahr die notwendigen Informationen liefern, und ergänze sie jeweils bei Bedarf.
		final GostLaufbahnplanungBeratungsdaten schuelerBeratungsdaten = this.reportingRepository.mapGostBeratungsdaten().computeIfAbsent(reportingSchueler.id(), s -> new DataGostSchuelerLaufbahnplanungBeratungsdaten(this.reportingRepository.conn()).getFromID(reportingSchueler.id()));
		final GostJahrgangsdaten gostJahrgangsdaten = this.reportingRepository.mapGostAbiturjahrgangDaten().computeIfAbsent(super.abiturjahr(), a -> DataGostJahrgangsdaten.getJahrgangsdaten(this.reportingRepository.conn(), super.abiturjahr()));
		if (!this.reportingRepository.mapGostAbiturjahrgangFaecher().containsKey(super.abiturjahr())) {
			final GostFaecherManager tempGostFaecherManager = DBUtilsFaecherGost.getNurWaehlbareFaecherListeGost(this.reportingRepository.conn(), super.abiturjahr());
			tempGostFaecherManager.addFachkombinationenAll(DataGostJahrgangFachkombinationen.getFachkombinationen(this.reportingRepository.conn(), super.abiturjahr()));
			this.reportingRepository.mapGostAbiturjahrgangFaecher().put(super.abiturjahr(), tempGostFaecherManager);
		}
		final GostFaecherManager gostFaecherManager = this.reportingRepository.mapGostAbiturjahrgangFaecher().get(super.abiturjahr());
		final AbiturdatenManager abiturdatenManager = new AbiturdatenManager(abiturdaten, gostJahrgangsdaten, gostFaecherManager, GostBelegpruefungsArt.GESAMT);

		// ##### Grunddaten und Summen setzen ###############
		var aktuellerLernabschnitt = reportingSchueler.aktuellerLernabschnitt();
		super.setPruefungsordnung(aktuellerLernabschnitt.pruefungsOrdnung());
		if (!super.pruefungsordnung().toLowerCase().contains("gost"))
			super.setPruefungsordnung("APO-GOSt");
		super.setBeratungsbogenText(gostJahrgangsdaten.textBeratungsbogen);
		super.setEmailText(gostJahrgangsdaten.textMailversand);
		super.setAktuellesGOStHalbjahr(reportingSchueler.aktuellerLernabschnitt().jahrgang().kuerzelStatistik() + '.' + this.reportingRepository.aktuellerSchuljahresabschnitt().abschnitt);
		super.setAktuelleKlasse(reportingSchueler.aktuellerLernabschnitt().klasse().kuerzel());
		eintragBeratungGostHalbjahrErgaenzen(reportingSchueler.aktuellerLernabschnitt().jahrgang());
		eintragBeratungslehrkraefteErgaenzen(schuelerBeratungsdaten, gostJahrgangsdaten);
		super.setLetzterRuecklaufDatum(schuelerBeratungsdaten.beratungsdatum);
		super.setLetzteBeratungDatum(schuelerBeratungsdaten.beratungsdatum);
		super.setKommentar(schuelerBeratungsdaten.kommentar);

		final int[] kurse = abiturdatenManager.getAnrechenbareKurse();
		final int[] wochenstunden = abiturdatenManager.getWochenstunden();

		super.setKursanzahlEF1(kurse[0]);
		super.setKursanzahlEF2(kurse[1]);
		super.setKursanzahlQ11(kurse[2]);
		super.setKursanzahlQ12(kurse[3]);
		super.setKursanzahlQ21(kurse[4]);
		super.setKursanzahlQ22(kurse[5]);

		super.setKursanzahlQPh(super.kursanzahlQ11() + super.kursanzahlQ12() + super.kursanzahlQ21() + super.kursanzahlQ22());

		super.setWochenstundenEF1(wochenstunden[0]);
		super.setWochenstundenEF2(wochenstunden[1]);
		super.setWochenstundenQ11(wochenstunden[2]);
		super.setWochenstundenQ12(wochenstunden[3]);
		super.setWochenstundenQ21(wochenstunden[4]);
		super.setWochenstundenQ22(wochenstunden[5]);

		super.setWochenstundenDurchschnittEF((super.wochenstundenEF1() + super.wochenstundenEF2()) / 2.0);
		super.setWochenstundenDurchschnittQ1((super.wochenstundenQ11() + super.wochenstundenQ12()) / 2.0);
		super.setWochenstundenDurchschnittQ2((super.wochenstundenQ21() + super.wochenstundenQ22()) / 2.0);
		super.setWochenstundenDurchschnittQPh((super.wochenstundenQ11() + super.wochenstundenQ12() + super.wochenstundenQ21() + super.wochenstundenQ22()) / 4.00);
		super.setWochenstundenGesamt((super.wochenstundenEF1() + super.wochenstundenEF2() + super.wochenstundenQ11() + super.wochenstundenQ12() + super.wochenstundenQ21() + super.wochenstundenQ22()) / 2.0);

		// ##### Fachwahlliste erstellen ###############
		super.setFachwahlen(getListFachwahlen(abiturdaten, this.reportingRepository.mapGostAbiturjahrgangFaecher().get(abiturdaten.abiturjahr)));

		// ##### Fehlerliste und Hinweisliste erstellen ###############
		final GostBelegpruefungErgebnis ergebnis = abiturdatenManager.getBelegpruefungErgebnis();
		for (final GostBelegpruefungErgebnisFehler f : ergebnis.fehlercodes) {
			final GostBelegungsfehlerArt art = GostBelegungsfehlerArt.fromKuerzel(f.art);
			if (art == GostBelegungsfehlerArt.HINWEIS) {
				final ReportingGostLaufbahnplanungErgebnismeldung laufbahnplanungHinweis = new ProxyReportingGostLaufbahnplanungErgebnismeldung(f.code, ReportingGostLaufbahnplanungErgebnismeldungKategorie.HINWEIS, f.beschreibung);
				super.hinweise().add(laufbahnplanungHinweis);
			} else {
				final ReportingGostLaufbahnplanungErgebnismeldung laufbahnplanungFehler = new ProxyReportingGostLaufbahnplanungErgebnismeldung(f.code, ReportingGostLaufbahnplanungErgebnismeldungKategorie.FEHLER, f.beschreibung);
				super.fehler().add(laufbahnplanungFehler);
			}
		}
	}



	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 * @return Repository für die Reporting
	 */
	public ReportingRepository reportingRepositorySchule() {
		return reportingRepository;
	}



	/**
	 * Setzt das Beratungshalbjahr für den Schüler
	 * @param aktuellerJahrgang 	Der aktuelle Jahrgang des Schülers
	 */
	private void eintragBeratungGostHalbjahrErgaenzen(final ReportingJahrgang aktuellerJahrgang) {
		final int folgeHalbjahr = (this.reportingRepository.aktuellerSchuljahresabschnitt().abschnitt % 2) + 1;
		if (folgeHalbjahr == 2) {
			super.setFolgeGOStHalbjahr(aktuellerJahrgang.kuerzelStatistik() + ".2");
		} else if (folgeHalbjahr == 1) {
			if (aktuellerJahrgang.folgejahrgang() != null)
				super.setFolgeGOStHalbjahr(aktuellerJahrgang.folgejahrgang().kuerzelStatistik() + ".1");
			else
				super.setFolgeGOStHalbjahr(aktuellerJahrgang.kuerzelStatistik() + ".2");

		} else
			super.setFolgeGOStHalbjahr("");
		// Frühestes Beratungshalbjahr kann die EF.1 sein.
		if (super.folgeGOStHalbjahr().compareTo("EF.1") < 0)
			super.setFolgeGOStHalbjahr("EF.1");
	}


	/**
	 * Setzt die Daten der Lehrkraft der letzten Beratung und der Beratungslehrkräfte der Jahrgangsstufe
	 * @param gostBeratungsdaten	Oberstufenschüler, dessen Beratungslehrkräfte ermitteln werden sollen.
	 * @param gostJahrgangsdaten	Die Daten des GOSt-Jahrgangs.
	 */
	private void eintragBeratungslehrkraefteErgaenzen(final GostLaufbahnplanungBeratungsdaten gostBeratungsdaten, final GostJahrgangsdaten gostJahrgangsdaten) {
		// Letzte Beratungslehrkraft bestimmen aus den GOSt-Daten des Schülers
		if (gostBeratungsdaten.beratungslehrerID != null) {
			super.setLetzteBeratungLehrkraft(new ProxyReportingLehrer(this.reportingRepository, new DataLehrerStammdaten(this.reportingRepository.conn()).getFromID(gostBeratungsdaten.beratungslehrerID)));
		}
		// Beratungslehrkräfte der Stufe bestimmen aus den GOSt-Daten der Jahrgangsstufe
		final List<GostBeratungslehrer> beratungslehrer = gostJahrgangsdaten.beratungslehrer;
		if (!beratungslehrer.isEmpty()) {
			for (final GostBeratungslehrer lehrkraft : beratungslehrer) {
				super.beratungslehrkraefte().add(new ProxyReportingLehrer(this.reportingRepository, new DataLehrerStammdaten(this.reportingRepository.conn()).getFromID(lehrkraft.id)));
			}
		}
	}

	/**
	 * Ergänzt im übergebenen LaufbahnplanungFachwahl-Objekt den Fremdspracheneintrag, wenn es sich um eine Fremdsprache handelt.
	 * @param laufbahnplanungFach 	Das Laufbahnplanungsfach, bei dem die Fremdspracheninformationen ergänzt werden sollen.
	 * @param fach 					GOST-Fach der Fremdsprache
	 * @param abiturdaten 			Abiturdaten des Schülers
	 * @param sprachbelegungen 		Sprachbelegungen des Schülers aus der Sprachenfolge
	 * @param sprachpruefungen 		Sprachprüfungen des Schülers
	 */
	private void eintragFremdspracheInLaufbahnplanungFachErgaenzen(final ProxyReportingGostLaufbahnplanungFachwahl laufbahnplanungFach, final GostFach fach, final Abiturdaten abiturdaten, final Map<String, Sprachbelegung> sprachbelegungen, final Map<String, Sprachpruefung> sprachpruefungen) {

		if (!fach.istFremdsprache)
			return;

		final ZulaessigesFach zfach = ZulaessigesFach.getByKuerzelASD(fach.kuerzel);

		// Verhindern, dass Pseudofächer der Statistik hier als zulässiges Fach verwendet werden.
		if (!(zfach.daten.kuerzelASD.equals("PX") || zfach.daten.kuerzelASD.equals("VX"))) {
			final Sprachbelegung sprachbelegung = sprachbelegungen.get(zfach.daten.kuerzel);
			final Sprachpruefung sprachpruefung = sprachpruefungen.get(zfach.daten.kuerzel);

			if (sprachbelegung != null) {
				if (((sprachbelegung.belegungVonJahrgang != null) && !sprachbelegung.belegungVonJahrgang.isEmpty())
					&& ((zfach.daten.abJahrgang == null)
					|| zfach.daten.abJahrgang.isEmpty()
					|| ((zfach.daten.abJahrgang.compareToIgnoreCase("EF") >= 0) && fach.istFremdSpracheNeuEinsetzend && (sprachbelegung.belegungVonJahrgang.compareToIgnoreCase("EF") >= 0))
					|| ((zfach.daten.abJahrgang.compareToIgnoreCase("EF") < 0) && !fach.istFremdSpracheNeuEinsetzend && (sprachbelegung.belegungVonJahrgang.compareToIgnoreCase("EF") < 0)))) {
					// Nur Sprachen heranziehen, die auch vor oder mit der eigenen Belegung hätten starten können. So wird bspw. die neue Fremdsprache ab EF nicht durch die Belegung der gleichen Sprache in der Sek-I als belegt markiert.
					laufbahnplanungFach.setFachIstFortfuehrbareFremdspracheInGOSt(true);
					laufbahnplanungFach.setJahrgangFremdsprachenbeginn(sprachbelegung.belegungVonJahrgang);
					laufbahnplanungFach.setPositionFremdsprachenfolge(sprachbelegung.reihenfolge.toString());
				}
			} else if ((sprachpruefung != null) && (SprachendatenUtils.istFortfuehrbareSpracheInGOSt(abiturdaten.sprachendaten, zfach.daten.kuerzel))) {
				laufbahnplanungFach.setFachIstFortfuehrbareFremdspracheInGOSt(true);
				if (sprachpruefung.istFeststellungspruefung) {
					laufbahnplanungFach.setJahrgangFremdsprachenbeginn("SFP");
				} else if (sprachpruefung.istHSUPruefung) {
					laufbahnplanungFach.setJahrgangFremdsprachenbeginn("HSU");
				}
				if (sprachpruefung.kannErstePflichtfremdspracheErsetzen)
					laufbahnplanungFach.setPositionFremdsprachenfolge("1");
				else if (sprachpruefung.kannZweitePflichtfremdspracheErsetzen)
					laufbahnplanungFach.setPositionFremdsprachenfolge("2");
				else if (sprachpruefung.kannWahlpflichtfremdspracheErsetzen)
					laufbahnplanungFach.setPositionFremdsprachenfolge("2");
				else {
					laufbahnplanungFach.setPositionFremdsprachenfolge("");
				}
			}
		}
	}

	/**
	 * Erstellt eine Liste von Fachwahlen aus der Laufbahnplanung eines Schülers für die GOSt.
	 * @param abiturdaten			Abiturdaten des Schülers.
	 * @param gostFaecherManager	Die Fächer des Abiturjahrgangs und im Fächermanager.
	 * @return						Eine Liste der Fachwahlen.
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
			final ZulaessigesFach zulaessigesFach = ZulaessigesFach.getByKuerzelASD(fach.kuerzel);
			final AbiturFachbelegung belegung = belegungen.get(fach.id);

			final ProxyReportingGostLaufbahnplanungFachwahl fachwahl = new ProxyReportingGostLaufbahnplanungFachwahl(
				"",
				Objects.requireNonNullElse(zulaessigesFach.daten.aufgabenfeld, 0),
				"", "",
				"", "", "", "",
				fach.bezeichnung,
				zulaessigesFach.daten.fachgruppe,
				false,
				false,
				zulaessigesFach.getHMTLFarbeRGB().replace("rgba", "rgb"),
				"",
				fach.kuerzelAnzeige,
				""
			);

			eintragFremdspracheInLaufbahnplanungFachErgaenzen(fachwahl, fach, abiturdaten, sprachbelegungen, sprachpruefungen);

			if (belegung != null) {
				fachwahl.setBelegungEF1(eintragFachbelegung(belegung.belegungen[0]));
				fachwahl.setBelegungEF2(eintragFachbelegung(belegung.belegungen[1]));
				fachwahl.setBelegungQ11(eintragFachbelegung(belegung.belegungen[2]));
				fachwahl.setBelegungQ12(eintragFachbelegung(belegung.belegungen[3]));
				fachwahl.setBelegungQ21(eintragFachbelegung(belegung.belegungen[4]));
				fachwahl.setBelegungQ22(eintragFachbelegung(belegung.belegungen[5]));
				fachwahl.setFachIstBelegtInGOSt(true);

				if (belegung.abiturFach != null) {
					fachwahl.setAbiturfach(belegung.abiturFach.toString());
				}
			}

			fachwahlen.add(fachwahl);
		}

		return fachwahlen;
	}


	/** Gibt den Belegungseintrag eines Faches für die Halbjahres-Belegung zurück.
	 * @param belegungHj 	Halbjahresbelegung des Faches
	 * @return 				String mit dem Belegungskürzel des Faches gemäß dessen Halbjahresbelegung
	 */
	private String eintragFachbelegung(final AbiturFachbelegungHalbjahr belegungHj) {
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
