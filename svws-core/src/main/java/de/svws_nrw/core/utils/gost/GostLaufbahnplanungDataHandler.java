package de.svws_nrw.core.utils.gost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.asd.data.schueler.Sprachpruefung;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostBeratungslehrer;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostJahrgang;
import de.svws_nrw.core.data.gost.GostJahrgangFachkombination;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.data.gost.GostSchuelerGKLWahl;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Beratungslehrer;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Fach;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Fachbelegung;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Fachkombination;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Schueler;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Sprachbelegung;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Sprachpruefung;
import de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2;
import de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2Beratungslehrer;
import de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2Fach;
import de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2Fachkombination;
import de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2GKL;
import de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2Schueler;
import de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2SchuelerFachbelegung;
import de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2SchuelerSprachbelegung;
import de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2SchuelerSprachpruefung;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse handhabt den Umgang mit den Laufbahnplanungsdaten aus einer Laufbahnplanungsdatei und stellt Methoden
 * für den Import und Export in die Dateiformat-Versionen für die Dateien bereit.
 */
public final class GostLaufbahnplanungDataHandler {

	private final @NotNull SchuleStammdaten schuleStammdaten;
	private final @NotNull SchuelerListeEintrag schueler;
	private final @NotNull String idSchuelerEncrpyted;
	private final @NotNull GostJahrgang gostJahrgang;
	private final @NotNull GostJahrgangsdaten gostJahrgangsdaten;
	private final @NotNull GostFaecherManager faecherManager;
	private @NotNull Abiturdaten abiturdaten;

	private final @NotNull HashMap2D<Long, GostHalbjahr, List<GostLaufbahnplanungGKLKlausurvorgabe>> gklMoeglich;
	private final @NotNull Map<Long, GostLaufbahnplanungGKLKlausurvorgabe> mapKlausurvorgaben;
	private @NotNull GostSchuelerGKLWahl gklWahlen;


	private GostLaufbahnplanungDataHandler(final @NotNull SchuleStammdaten schuleStammdaten,
			final @NotNull SchuelerListeEintrag schueler,
			final @NotNull String idSchuelerEncrpyted,
			final @NotNull GostJahrgang gostJahrgang,
			final @NotNull GostJahrgangsdaten gostJahrgangsdaten,
			final @NotNull GostFaecherManager faecherManager,
			final @NotNull Abiturdaten abiturdaten,
			final @NotNull HashMap2D<Long, GostHalbjahr, List<GostLaufbahnplanungGKLKlausurvorgabe>> gklMoeglich,
			final @NotNull Map<Long, GostLaufbahnplanungGKLKlausurvorgabe> mapKlausurvorgaben,
			final @NotNull GostSchuelerGKLWahl gklWahlen) {
		this.schuleStammdaten = schuleStammdaten;
		this.schueler = schueler;
		this.idSchuelerEncrpyted = idSchuelerEncrpyted;
		this.gostJahrgang = gostJahrgang;
		this.gostJahrgangsdaten = gostJahrgangsdaten;
		this.faecherManager = faecherManager;
		this.abiturdaten = abiturdaten;
		this.gklMoeglich = gklMoeglich;
		this.mapKlausurvorgaben = mapKlausurvorgaben;
		this.gklWahlen = gklWahlen;
	}


	private GostLaufbahnplanungDataHandler(final @NotNull GostLaufbahnplanungExportV1 daten) {
		this.schuleStammdaten = importV1SchuleStammdaten(daten);
		this.faecherManager = importV1GostFaecherManager(daten);

		final @NotNull GostLaufbahnplanungExportV1Schueler planungsdaten = daten.schueler.get(0);
		this.schueler = importV1Schueler(daten, planungsdaten);
		this.abiturdaten = importV1Abiturdaten(daten, planungsdaten, this.faecherManager);

		this.gostJahrgang = importV1GostJahrgang(daten);
		this.gostJahrgangsdaten = importV1GostJahrgangsdaten(daten, this.gostJahrgang);
		this.gostJahrgangsdaten.beratungslehrer.addAll(importV1Beratungslehrer(daten.beratungslehrer));
		this.gostJahrgangsdaten.istBlockungFestgelegt = abiturdaten.bewertetesHalbjahr;

		this.idSchuelerEncrpyted = planungsdaten.idEnc;

		this.gklMoeglich = new HashMap2D<>();
		this.mapKlausurvorgaben = new HashMap<>();
		this.gklWahlen = new GostSchuelerGKLWahl();
	}


	private GostLaufbahnplanungDataHandler(final @NotNull GostLaufbahnplanungExportV2 daten) {
		this.schuleStammdaten = importV2SchuleStammdaten(daten);
		this.faecherManager = importV2GostFaecherManager(daten);

		final @NotNull GostLaufbahnplanungExportV2Schueler planungsdaten = daten.schueler.get(0);
		this.schueler = importV2Schueler(daten, planungsdaten);
		this.abiturdaten = importV2Abiturdaten(daten, planungsdaten, this.faecherManager);

		this.gostJahrgang = importV2GostJahrgang(daten);
		this.gostJahrgangsdaten = importV2GostJahrgangsdaten(daten, this.gostJahrgang);
		this.gostJahrgangsdaten.beratungslehrer.addAll(importV2Beratungslehrer(daten.beratungslehrer));
		this.gostJahrgangsdaten.istBlockungFestgelegt = abiturdaten.bewertetesHalbjahr;

		this.idSchuelerEncrpyted = planungsdaten.idEnc;

		this.gklMoeglich = new HashMap2D<>();
		this.mapKlausurvorgaben = new HashMap<>();
		this.gklWahlen = new GostSchuelerGKLWahl();
		this.importV2GKL(planungsdaten, daten.gkl, faecherManager);
	}


	/**
	 * Gibt die Stammdaten der Schule, welche für die Laufbahnplanung relevant sind, zurück.
	 *
	 * @return die für die Laufbahnplanung relevanten Stammdaten der Schule
	 */
	public @NotNull SchuleStammdaten getSchuleStammdaten() {
		return schuleStammdaten;
	}


	/**
	 * Gibt den Schüler-Listen-Eintrag für den Schüler zurück, dessen Laufbahndaten angezeigt werden.
	 *
	 * @return der Schüler-Listen-Eintrag
	 */
	public @NotNull SchuelerListeEintrag getSchueler() {
		return schueler;
	}


	/**
	 * Gibt die verschlüsselte ID des Schülers für die Laufbahnplanungsdatei zurück.
	 *
	 * @return die verschlüsselte ID des Schülers für die Laufbahnplanungsdatei
	 */
	public @NotNull String getIdSchuelerEncrpyted() {
		return idSchuelerEncrpyted;
	}


	/**
	 * Grundlegende Informationen zum Jahrgang der Gymnasialen Oberstufe
	 *
	 * @return die grundlegenden Informationen zum Jahrgang der Gymnasialen Oberstufe
	 */
	public @NotNull GostJahrgang getGostJahrgang() {
		return gostJahrgang;
	}


	/**
	 * Ausführlichere Informationen zum Jahrgang der Gymnasialen Oberstufe
	 *
	 * @return die ausführlicheren Informationen zum Jahrgang der Gymnasialen Oberstufe
	 */
	public @NotNull GostJahrgangsdaten getGostJahrgangsdaten() {
		return gostJahrgangsdaten;
	}


	/**
	 * Der Manager für die Fächer der Gymnasialen Oberstufe
	 *
	 * @return der Fächer-Manager
	 */
	public @NotNull GostFaecherManager getFaecherManager() {
		return faecherManager;
	}


	/**
	 * Gibt die Abiturdaten des Schülers der Laufbahnplanungsdatei zurück.
	 *
	 * @return die Abiturdaten des Schülers
	 */
	public @NotNull Abiturdaten getAbiturdaten() {
		return abiturdaten;
	}


	/**
	 * Gibt eine Map mit der Zuordnung von Klausurvorgaben mit Festlegungen zu GKLs zu der ID des zugehörigen Faches und dem Halbjahr der Vorgabeinformationen zurück.
	 *
	 * @return die zweidimensionale Map mit den Klausurvorgaben mit Festlegungen zu GKLs
	 */
	public @NotNull HashMap2D<Long, GostHalbjahr, List<GostLaufbahnplanungGKLKlausurvorgabe>> getGklMoeglich() {
		return gklMoeglich;
	}


	/**
	 * Gibt eine Map mit der Zuordnung von Klausurvorgaben mit Festlegungen zu GKLs zu der ID der Klausurvorgabe zurück.
	 *
	 * @return die Map mit den Klausurvorgaben zugeordnet zu der Vorgabe-ID
	 */
	public @NotNull Map<Long, GostLaufbahnplanungGKLKlausurvorgabe> getMapKlausurvorgaben() {
		return mapKlausurvorgaben;
	}


	/**
	 * Gibt die Informationen zu den GKL-Wahlen des Schülers zurück.
	 *
	 * @return die Informationen zu den GKL-Wahlen des Schülers
	 */
	public @NotNull GostSchuelerGKLWahl getGklWahlen() {
		return gklWahlen;
	}


	/**
	 * Ersetzt die internen Abiturdaten mit den neuen übergebenen Abiturdaten
	 *
	 * @param abiturdaten   die neuen Abiturdaten
	 */
	public void replaceAbiturdaten(final @NotNull Abiturdaten abiturdaten) {
		this.abiturdaten = abiturdaten;
	}

	/**
	 * Ersetzt die internen GKL-Wahlen mit den neuen übergebenen GKL-Wahlen
	 *
	 * @param gklWahlen   die neuen GKL-Wahlen
	 */
	public void replaceGKLWahlen(final @NotNull GostSchuelerGKLWahl gklWahlen) {
		this.gklWahlen = gklWahlen;
	}

	private static @NotNull SchuleStammdaten importV1SchuleStammdaten(final @NotNull GostLaufbahnplanungExportV1 daten) {
		final @NotNull SchuleStammdaten schuleStammdaten = new SchuleStammdaten();
		schuleStammdaten.schulNr = daten.schulNr;
		schuleStammdaten.bezeichnung1 = daten.schulBezeichnung1;
		schuleStammdaten.bezeichnung2 = daten.schulBezeichnung2;
		schuleStammdaten.bezeichnung3 = daten.schulBezeichnung3;
		return schuleStammdaten;
	}


	private static @NotNull GostJahrgang importV1GostJahrgang(final @NotNull GostLaufbahnplanungExportV1 daten) {
		final @NotNull GostJahrgang gostJahrgang = new GostJahrgang();
		gostJahrgang.abiturjahr = daten.abiturjahr;
		gostJahrgang.jahrgang = daten.jahrgang;
		gostJahrgang.bezeichnung = "Abiturjahr " + daten.abiturjahr;
		gostJahrgang.istAbgeschlossen = false;
		return gostJahrgang;
	}


	private static @NotNull GostJahrgangsdaten importV1GostJahrgangsdaten(final @NotNull GostLaufbahnplanungExportV1 daten,
			final @NotNull GostJahrgang gostJahrgang) {
		final @NotNull GostJahrgangsdaten gostJahrgangsdaten = new GostJahrgangsdaten();
		gostJahrgangsdaten.abiturjahr = gostJahrgang.abiturjahr;
		gostJahrgangsdaten.jahrgang = gostJahrgang.jahrgang;
		gostJahrgangsdaten.bezeichnung = gostJahrgang.bezeichnung;
		gostJahrgangsdaten.istAbgeschlossen = gostJahrgang.istAbgeschlossen;
		gostJahrgangsdaten.hatZusatzkursGE = daten.hatZusatzkursGE;
		gostJahrgangsdaten.beginnZusatzkursGE = daten.beginnZusatzkursGE;
		gostJahrgangsdaten.hatZusatzkursSW = daten.hatZusatzkursSW;
		gostJahrgangsdaten.beginnZusatzkursSW = daten.beginnZusatzkursSW;
		gostJahrgangsdaten.textBeratungsbogen = daten.textBeratungsbogen;
		gostJahrgangsdaten.textMailversand = null;
		return gostJahrgangsdaten;
	}

	private static @NotNull List<GostBeratungslehrer> importV1Beratungslehrer(final @NotNull List<GostLaufbahnplanungExportV1Beratungslehrer> listBeratungslehrer) {
		final @NotNull List<GostBeratungslehrer> result = new ArrayList<>();
		for (final @NotNull GostLaufbahnplanungExportV1Beratungslehrer beratungslehrer : listBeratungslehrer) {
			final @NotNull GostBeratungslehrer l = new GostBeratungslehrer();
			l.id = beratungslehrer.id;
			l.kuerzel = beratungslehrer.kuerzel;
			l.nachname = beratungslehrer.nachname;
			l.vorname = beratungslehrer.vorname;
			result.add(l);
		}
		return result;
	}

	private static @NotNull List<GostFach> importV1GostFaecher(final @NotNull List<GostLaufbahnplanungExportV1Fach> faecher) {
		final @NotNull List<GostFach> result = new ArrayList<>();
		for (final @NotNull GostLaufbahnplanungExportV1Fach fach : faecher) {
			final @NotNull GostFach f = new GostFach();
			f.id = fach.id;
			f.kuerzel = fach.kuerzel;
			f.kuerzelAnzeige = fach.kuerzelAnzeige;
			f.bezeichnung = fach.bezeichnung;
			f.sortierung = fach.sortierung;
			f.istPruefungsordnungsRelevant = fach.istPruefungsordnungsRelevant;
			f.istFremdsprache = fach.istFremdsprache;
			f.istFremdSpracheNeuEinsetzend = fach.istFremdSpracheNeuEinsetzend;
			f.biliSprache = fach.biliSprache;
			f.istMoeglichAbiLK = fach.istMoeglichAbiLK;
			f.istMoeglichAbiGK = fach.istMoeglichAbiGK;
			f.istMoeglichEF1 = fach.istMoeglichEF1;
			f.istMoeglichEF2 = fach.istMoeglichEF2;
			f.istMoeglichQ11 = fach.istMoeglichQ11;
			f.istMoeglichQ12 = fach.istMoeglichQ12;
			f.istMoeglichQ21 = fach.istMoeglichQ21;
			f.istMoeglichQ22 = fach.istMoeglichQ22;
			f.wochenstundenQualifikationsphase = fach.wochenstundenQualifikationsphase;
			f.projektKursLeitfach1ID = fach.projektKursLeitfach1ID;
			f.projektKursLeitfach1Kuerzel = fach.projektKursLeitfach1Kuerzel;
			f.projektKursLeitfach2ID = fach.projektKursLeitfach2ID;
			f.projektKursLeitfach2Kuerzel = fach.projektKursLeitfach2Kuerzel;
			result.add(f);
		}
		return result;
	}


	private static @NotNull List<GostJahrgangFachkombination> importV1Fachkombinationen(
			final @NotNull List<GostLaufbahnplanungExportV1Fachkombination> fachkombis) {
		final @NotNull List<GostJahrgangFachkombination> result = new ArrayList<>();
		for (final @NotNull GostLaufbahnplanungExportV1Fachkombination fachkombi : fachkombis) {
			final @NotNull GostJahrgangFachkombination k = new GostJahrgangFachkombination();
			k.id = fachkombi.id;
			k.abiturjahr = fachkombi.abiturjahr;
			k.fachID1 = fachkombi.fachID1;
			k.kursart1 = fachkombi.kursart1;
			k.fachID2 = fachkombi.fachID2;
			k.kursart2 = fachkombi.kursart2;
			k.gueltigInHalbjahr[0] = fachkombi.gueltigInHalbjahr[0];
			k.gueltigInHalbjahr[1] = fachkombi.gueltigInHalbjahr[1];
			k.gueltigInHalbjahr[2] = fachkombi.gueltigInHalbjahr[2];
			k.gueltigInHalbjahr[3] = fachkombi.gueltigInHalbjahr[3];
			k.gueltigInHalbjahr[4] = fachkombi.gueltigInHalbjahr[4];
			k.gueltigInHalbjahr[5] = fachkombi.gueltigInHalbjahr[5];
			k.typ = fachkombi.typ;
			k.hinweistext = fachkombi.hinweistext;
			result.add(k);
		}
		return result;
	}

	private static @NotNull GostFaecherManager importV1GostFaecherManager(final @NotNull GostLaufbahnplanungExportV1 daten) {
		final GostFaecherManager faecherManager = new GostFaecherManager(daten.abiturjahr - 1, importV1GostFaecher(daten.faecher));
		faecherManager.addFachkombinationenAll(importV1Fachkombinationen(daten.fachkombinationen));
		return faecherManager;
	}

	private static @NotNull SchuelerListeEintrag importV1Schueler(final @NotNull GostLaufbahnplanungExportV1 daten,
			final @NotNull GostLaufbahnplanungExportV1Schueler planungsdaten) {
		final @NotNull SchuelerListeEintrag schueler = new SchuelerListeEintrag();
		schueler.id = planungsdaten.id;
		schueler.vorname = planungsdaten.vorname;
		schueler.nachname = planungsdaten.nachname;
		schueler.geschlecht = planungsdaten.geschlecht;
		schueler.abiturjahrgang = daten.abiturjahr;
		return schueler;
	}

	private static void importV1Sprachdaten(final @NotNull GostLaufbahnplanungExportV1Schueler planungsdaten, final @NotNull Abiturdaten abiturdaten) {
		for (final @NotNull GostLaufbahnplanungExportV1Sprachbelegung bel : planungsdaten.sprachendaten.belegungen) {
			final @NotNull Sprachbelegung mappedBel = new Sprachbelegung();
			mappedBel.sprache = bel.sprache;
			mappedBel.istNachweis = (bel.istNachweis != null) && bel.istNachweis;
			mappedBel.reihenfolge = bel.reihenfolge;
			mappedBel.belegungVonJahrgang = bel.belegungVonJahrgang;
			mappedBel.belegungVonAbschnitt = bel.belegungVonAbschnitt;
			mappedBel.belegungBisJahrgang = bel.belegungBisJahrgang;
			mappedBel.belegungBisAbschnitt = bel.belegungBisAbschnitt;
			mappedBel.referenzniveau = bel.referenzniveau;
			mappedBel.hatKleinesLatinum = bel.hatKleinesLatinum;
			mappedBel.hatLatinum = bel.hatLatinum;
			mappedBel.hatGraecum = bel.hatGraecum;
			mappedBel.hatHebraicum = bel.hatHebraicum;
			abiturdaten.sprachendaten.belegungen.add(mappedBel);
		}
		for (final @NotNull GostLaufbahnplanungExportV1Sprachpruefung pruef : planungsdaten.sprachendaten.pruefungen) {
			final @NotNull Sprachpruefung mappedPruef = new Sprachpruefung();
			mappedPruef.sprache = pruef.sprache;
			mappedPruef.jahrgang = pruef.jahrgang;
			mappedPruef.anspruchsniveauId = pruef.anspruchsniveauId;
			mappedPruef.pruefungsdatum = pruef.pruefungsdatum;
			mappedPruef.ersetzteSprache = pruef.ersetzteSprache;
			mappedPruef.istHSUPruefung = pruef.istHSUPruefung;
			mappedPruef.istFeststellungspruefung = pruef.istFeststellungspruefung;
			mappedPruef.kannErstePflichtfremdspracheErsetzen = pruef.kannErstePflichtfremdspracheErsetzen;
			mappedPruef.kannZweitePflichtfremdspracheErsetzen = pruef.kannZweitePflichtfremdspracheErsetzen;
			mappedPruef.kannWahlpflichtfremdspracheErsetzen = pruef.kannWahlpflichtfremdspracheErsetzen;
			mappedPruef.kannBelegungAlsFortgefuehrteSpracheErlauben = pruef.kannBelegungAlsFortgefuehrteSpracheErlauben;
			mappedPruef.referenzniveau = pruef.referenzniveau;
			mappedPruef.note = pruef.note;
			mappedPruef.zeugnisbezeichnung = pruef.zeugnisbezeichnung;
			abiturdaten.sprachendaten.pruefungen.add(mappedPruef);
		}
		abiturdaten.bilingualeSprache = planungsdaten.bilingualeSprache;
	}

	private static void importV1Belegungen(final @NotNull GostLaufbahnplanungExportV1Schueler planungsdaten, final @NotNull Abiturdaten abiturdaten,
			final @NotNull GostFaecherManager faecherManager) {
		for (final @NotNull GostHalbjahr hj : GostHalbjahr.values()) {
			abiturdaten.bewertetesHalbjahr[hj.id] = planungsdaten.bewertetesHalbjahr[hj.id];
		}
		for (int i = 0; i < planungsdaten.fachbelegungen.size(); i++) {
			final @NotNull AbiturFachbelegung belegung = new AbiturFachbelegung();
			final @NotNull GostLaufbahnplanungExportV1Fachbelegung fb = planungsdaten.fachbelegungen.get(i);
			final GostFach fach = faecherManager.get(fb.fachID);
			if (fach == null) {
				continue;
			}
			belegung.fachID = fb.fachID;
			belegung.abiturFach = fb.abiturFach;
			belegung.istFSNeu = fach.istFremdSpracheNeuEinsetzend;
			for (final @NotNull GostHalbjahr hj : GostHalbjahr.values()) {
				final String kursart = fb.kursart[hj.id];
				if (kursart == null) {
					continue;
				}
				final @NotNull AbiturFachbelegungHalbjahr hjBelegung = new AbiturFachbelegungHalbjahr();
				hjBelegung.halbjahrKuerzel = hj.kuerzel;
				hjBelegung.kursartKuerzel = kursart;
				hjBelegung.schriftlich = fb.schriftlich[hj.id];
				hjBelegung.biliSprache = fach.biliSprache;
				if ("PX".equals(fach.kuerzel)) {
					hjBelegung.wochenstunden = fach.wochenstundenQualifikationsphase;
				}
				if ("AT".equals(kursart)) {
					hjBelegung.notenkuerzel = "AT";
				}
				belegung.belegungen[hj.id] = hjBelegung;
				belegung.letzteKursart = kursart;
			}
			abiturdaten.fachbelegungen.add(belegung);
		}
	}

	private static @NotNull Abiturdaten importV1Abiturdaten(final @NotNull GostLaufbahnplanungExportV1 daten,
			final @NotNull GostLaufbahnplanungExportV1Schueler planungsdaten, final @NotNull GostFaecherManager faecherManager) {
		final @NotNull Abiturdaten abiturdaten = new Abiturdaten();
		abiturdaten.abiturjahr = daten.abiturjahr;
		importV1Sprachdaten(planungsdaten, abiturdaten);
		importV1Belegungen(planungsdaten, abiturdaten, faecherManager);
		return abiturdaten;
	}

	/**
	 * Import die Laufbahnplanungsdaten in der Version 1 des Datenformates
	 *
	 * @param daten   die Laufbahnplanungsdaten in der Version 1
	 *
	 * @return die geladenen Laufbahnplanungsdaten
	 */
	public static @NotNull GostLaufbahnplanungDataHandler importV1(final @NotNull GostLaufbahnplanungExportV1 daten) {
		return new GostLaufbahnplanungDataHandler(daten);
	}


	private static @NotNull SchuleStammdaten importV2SchuleStammdaten(final @NotNull GostLaufbahnplanungExportV2 daten) {
		final @NotNull SchuleStammdaten schuleStammdaten = new SchuleStammdaten();
		schuleStammdaten.schulNr = daten.schulNr;
		schuleStammdaten.bezeichnung1 = daten.schulBezeichnung1;
		schuleStammdaten.bezeichnung2 = daten.schulBezeichnung2;
		schuleStammdaten.bezeichnung3 = daten.schulBezeichnung3;
		return schuleStammdaten;
	}


	private static @NotNull GostJahrgang importV2GostJahrgang(final @NotNull GostLaufbahnplanungExportV2 daten) {
		final @NotNull GostJahrgang gostJahrgang = new GostJahrgang();
		gostJahrgang.abiturjahr = daten.abiturjahr;
		gostJahrgang.jahrgang = daten.jahrgang;
		gostJahrgang.bezeichnung = "Abiturjahr " + daten.abiturjahr;
		gostJahrgang.istAbgeschlossen = false;
		return gostJahrgang;
	}


	private static @NotNull GostJahrgangsdaten importV2GostJahrgangsdaten(final @NotNull GostLaufbahnplanungExportV2 daten,
			final @NotNull GostJahrgang gostJahrgang) {
		final @NotNull GostJahrgangsdaten gostJahrgangsdaten = new GostJahrgangsdaten();
		gostJahrgangsdaten.abiturjahr = gostJahrgang.abiturjahr;
		gostJahrgangsdaten.jahrgang = gostJahrgang.jahrgang;
		gostJahrgangsdaten.bezeichnung = gostJahrgang.bezeichnung;
		gostJahrgangsdaten.istAbgeschlossen = gostJahrgang.istAbgeschlossen;
		gostJahrgangsdaten.hatZusatzkursGE = daten.hatZusatzkursGE;
		gostJahrgangsdaten.beginnZusatzkursGE = daten.beginnZusatzkursGE;
		gostJahrgangsdaten.hatZusatzkursSW = daten.hatZusatzkursSW;
		gostJahrgangsdaten.beginnZusatzkursSW = daten.beginnZusatzkursSW;
		gostJahrgangsdaten.textBeratungsbogen = daten.textBeratungsbogen;
		gostJahrgangsdaten.textMailversand = null;
		return gostJahrgangsdaten;
	}

	private static @NotNull List<GostBeratungslehrer> importV2Beratungslehrer(final @NotNull List<GostLaufbahnplanungExportV2Beratungslehrer> listBeratungslehrer) {
		final @NotNull List<GostBeratungslehrer> result = new ArrayList<>();
		for (final @NotNull GostLaufbahnplanungExportV2Beratungslehrer beratungslehrer : listBeratungslehrer) {
			final @NotNull GostBeratungslehrer l = new GostBeratungslehrer();
			l.id = beratungslehrer.id;
			l.kuerzel = beratungslehrer.kuerzel;
			l.nachname = beratungslehrer.nachname;
			l.vorname = beratungslehrer.vorname;
			result.add(l);
		}
		return result;
	}

	private static @NotNull List<GostFach> importV2GostFaecher(final @NotNull List<GostLaufbahnplanungExportV2Fach> faecher) {
		final @NotNull List<GostFach> result = new ArrayList<>();
		for (final @NotNull GostLaufbahnplanungExportV2Fach fach : faecher) {
			final @NotNull GostFach f = new GostFach();
			f.id = fach.id;
			f.kuerzel = fach.kuerzel;
			f.kuerzelAnzeige = fach.kuerzelAnzeige;
			f.bezeichnung = fach.bezeichnung;
			f.sortierung = fach.sortierung;
			f.istPruefungsordnungsRelevant = fach.istPruefungsordnungsRelevant;
			f.istFremdsprache = fach.istFremdsprache;
			f.istFremdSpracheNeuEinsetzend = fach.istFremdSpracheNeuEinsetzend;
			f.biliSprache = fach.biliSprache;
			f.istMoeglichAbiLK = fach.istMoeglichAbiLK;
			f.istMoeglichAbiGK = fach.istMoeglichAbiGK;
			f.istMoeglichEF1 = fach.istMoeglich[0];
			f.istMoeglichEF2 = fach.istMoeglich[1];
			f.istMoeglichQ11 = fach.istMoeglich[2];
			f.istMoeglichQ12 = fach.istMoeglich[3];
			f.istMoeglichQ21 = fach.istMoeglich[4];
			f.istMoeglichQ22 = fach.istMoeglich[5];
			f.wochenstundenQualifikationsphase = fach.wochenstundenQualifikationsphase;
			f.projektKursLeitfach1ID = fach.referenzfach1ID;
			f.projektKursLeitfach2ID = fach.referenzfach2ID;
			result.add(f);
		}
		return result;
	}


	private static @NotNull List<GostJahrgangFachkombination> importV2Fachkombinationen(
			final @NotNull List<GostLaufbahnplanungExportV2Fachkombination> fachkombis) {
		final @NotNull List<GostJahrgangFachkombination> result = new ArrayList<>();
		for (final @NotNull GostLaufbahnplanungExportV2Fachkombination fachkombi : fachkombis) {
			final @NotNull GostJahrgangFachkombination k = new GostJahrgangFachkombination();
			k.id = fachkombi.id;
			k.abiturjahr = fachkombi.abiturjahr;
			k.fachID1 = fachkombi.fachID1;
			k.kursart1 = fachkombi.kursart1;
			k.fachID2 = fachkombi.fachID2;
			k.kursart2 = fachkombi.kursart2;
			k.gueltigInHalbjahr[0] = fachkombi.gueltigInHalbjahr[0];
			k.gueltigInHalbjahr[1] = fachkombi.gueltigInHalbjahr[1];
			k.gueltigInHalbjahr[2] = fachkombi.gueltigInHalbjahr[2];
			k.gueltigInHalbjahr[3] = fachkombi.gueltigInHalbjahr[3];
			k.gueltigInHalbjahr[4] = fachkombi.gueltigInHalbjahr[4];
			k.gueltigInHalbjahr[5] = fachkombi.gueltigInHalbjahr[5];
			k.typ = fachkombi.typ;
			k.hinweistext = fachkombi.hinweistext;
			result.add(k);
		}
		return result;
	}


	private static @NotNull GostFaecherManager importV2GostFaecherManager(final @NotNull GostLaufbahnplanungExportV2 daten) {
		final GostFaecherManager faecherManager = new GostFaecherManager(daten.abiturjahr - 1, importV2GostFaecher(daten.faecher));
		faecherManager.addFachkombinationenAll(importV2Fachkombinationen(daten.fachkombinationen));
		return faecherManager;
	}


	private static @NotNull SchuelerListeEintrag importV2Schueler(final @NotNull GostLaufbahnplanungExportV2 daten,
			final @NotNull GostLaufbahnplanungExportV2Schueler planungsdaten) {
		final @NotNull SchuelerListeEintrag schueler = new SchuelerListeEintrag();
		schueler.id = planungsdaten.id;
		schueler.vorname = planungsdaten.vorname;
		schueler.nachname = planungsdaten.nachname;
		schueler.geschlecht = planungsdaten.geschlecht;
		schueler.abiturjahrgang = daten.abiturjahr;
		schueler.idSchulgliederung = planungsdaten.istG8
				? Schulgliederung.GY8.historie().getLast().id
				: Schulgliederung.GY9.historie().getLast().id;
		return schueler;
	}

	private static void importV2Sprachdaten(final @NotNull GostLaufbahnplanungExportV2Schueler planungsdaten, final @NotNull Abiturdaten abiturdaten) {
		for (final @NotNull GostLaufbahnplanungExportV2SchuelerSprachbelegung bel : planungsdaten.sprachbelegungen) {
			final @NotNull Sprachbelegung mappedBel = new Sprachbelegung();
			mappedBel.sprache = bel.sprache;
			mappedBel.istNachweis = bel.istNachweis;
			mappedBel.reihenfolge = bel.reihenfolge;
			mappedBel.belegungVonJahrgang = bel.belegungVonJahrgang;
			mappedBel.belegungVonAbschnitt = bel.belegungVonAbschnitt;
			mappedBel.belegungBisJahrgang = bel.belegungBisJahrgang;
			mappedBel.belegungBisAbschnitt = bel.belegungBisAbschnitt;
			mappedBel.referenzniveau = bel.referenzniveau;
			mappedBel.hatKleinesLatinum = bel.hatKleinesLatinum;
			mappedBel.hatLatinum = bel.hatLatinum;
			mappedBel.hatGraecum = bel.hatGraecum;
			mappedBel.hatHebraicum = bel.hatHebraicum;
			abiturdaten.sprachendaten.belegungen.add(mappedBel);
		}
		for (final @NotNull GostLaufbahnplanungExportV2SchuelerSprachpruefung pruef : planungsdaten.sprachpruefungen) {
			final @NotNull Sprachpruefung mappedPruef = new Sprachpruefung();
			mappedPruef.sprache = pruef.sprache;
			mappedPruef.jahrgang = pruef.jahrgang;
			mappedPruef.anspruchsniveauId = pruef.anspruchsniveauId;
			mappedPruef.pruefungsdatum = pruef.pruefungsdatum;
			mappedPruef.ersetzteSprache = pruef.ersetzteSprache;
			mappedPruef.istHSUPruefung = pruef.istHSUPruefung;
			mappedPruef.istFeststellungspruefung = pruef.istFeststellungspruefung;
			mappedPruef.kannErstePflichtfremdspracheErsetzen = pruef.kannErstePflichtfremdspracheErsetzen;
			mappedPruef.kannZweitePflichtfremdspracheErsetzen = pruef.kannZweitePflichtfremdspracheErsetzen;
			mappedPruef.kannWahlpflichtfremdspracheErsetzen = pruef.kannWahlpflichtfremdspracheErsetzen;
			mappedPruef.kannBelegungAlsFortgefuehrteSpracheErlauben = pruef.kannBelegungAlsFortgefuehrteSpracheErlauben;
			mappedPruef.referenzniveau = pruef.referenzniveau;
			mappedPruef.note = pruef.note;
			mappedPruef.zeugnisbezeichnung = pruef.zeugnisbezeichnung;
			abiturdaten.sprachendaten.pruefungen.add(mappedPruef);
		}
		abiturdaten.bilingualeSprache = planungsdaten.bilingualeSprache;
	}

	private static void importV2Belegungen(final @NotNull GostLaufbahnplanungExportV2Schueler planungsdaten, final @NotNull Abiturdaten abiturdaten,
			final @NotNull GostFaecherManager faecherManager) {
		for (final @NotNull GostHalbjahr hj : GostHalbjahr.values()) {
			abiturdaten.bewertetesHalbjahr[hj.id] = planungsdaten.bewertetesHalbjahr[hj.id];
		}
		for (int i = 0; i < planungsdaten.fachbelegungen.size(); i++) {
			final @NotNull AbiturFachbelegung belegung = new AbiturFachbelegung();
			final @NotNull GostLaufbahnplanungExportV2SchuelerFachbelegung fb = planungsdaten.fachbelegungen.get(i);
			final GostFach fach = faecherManager.get(fb.fachID);
			if (fach == null) {
				continue;
			}
			belegung.fachID = fb.fachID;
			belegung.abiturFach = fb.abiturFach;
			belegung.idReferenzfach = fb.idReferenzfach;
			belegung.istFSNeu = fach.istFremdSpracheNeuEinsetzend;
			for (final @NotNull GostHalbjahr hj : GostHalbjahr.values()) {
				final String kursart = fb.kursart[hj.id];
				if (kursart == null) {
					continue;
				}
				final @NotNull AbiturFachbelegungHalbjahr hjBelegung = new AbiturFachbelegungHalbjahr();
				hjBelegung.halbjahrKuerzel = hj.kuerzel;
				hjBelegung.kursartKuerzel = kursart;
				hjBelegung.schriftlich = fb.schriftlich[hj.id];
				hjBelegung.biliSprache = fach.biliSprache;
				if ("PX".equals(fach.kuerzel)) {
					hjBelegung.wochenstunden = fach.wochenstundenQualifikationsphase;
				}
				if ("AT".equals(kursart)) {
					hjBelegung.notenkuerzel = "AT";
				}
				belegung.belegungen[hj.id] = hjBelegung;
				belegung.letzteKursart = kursart;
			}
			abiturdaten.fachbelegungen.add(belegung);
		}
	}

	private static @NotNull Abiturdaten importV2Abiturdaten(final @NotNull GostLaufbahnplanungExportV2 daten,
			final @NotNull GostLaufbahnplanungExportV2Schueler planungsdaten, final @NotNull GostFaecherManager faecherManager) {
		final @NotNull Abiturdaten abiturdaten = new Abiturdaten();
		abiturdaten.abiturjahr = daten.abiturjahr;
		importV2Sprachdaten(planungsdaten, abiturdaten);
		importV2Belegungen(planungsdaten, abiturdaten, faecherManager);
		return abiturdaten;
	}


	private void importV2GKL(final @NotNull GostLaufbahnplanungExportV2Schueler planungsdaten,
			final @NotNull List<GostLaufbahnplanungExportV2GKL> listMoeglich, final @NotNull GostFaecherManager faecherManager) {
		for (final @NotNull GostFach fach : faecherManager.faecher()) {
			for (final @NotNull GostHalbjahr halbjahr : GostHalbjahr.values()) {
				gklMoeglich.put(fach.id, halbjahr, new ArrayList<>());
			}
		}

		for (final @NotNull GostLaufbahnplanungExportV2GKL moeglich : listMoeglich) {
			final @NotNull GostKlausurvorgabe vorgabe = new GostKlausurvorgabe();
			vorgabe.id = moeglich.id;
			vorgabe.idFach = moeglich.idFach;
			vorgabe.halbjahr = moeglich.idHalbjahr;
			vorgabe.quartal = moeglich.quartal;
			final @NotNull GostHalbjahr halbjahr = GostHalbjahr.fromIDorException(vorgabe.halbjahr);
			final GostFach fach = faecherManager.get(vorgabe.idFach);
			if (fach == null) {
				continue;
			}
			final @NotNull GostLaufbahnplanungGKLKlausurvorgabe eintrag = new GostLaufbahnplanungGKLKlausurvorgabe(fach, halbjahr, vorgabe);
			mapKlausurvorgaben.put(vorgabe.id, eintrag);
			gklMoeglich.getOrException(vorgabe.idFach, halbjahr).add(eintrag);
		}

		gklWahlen.idKlausurvorgabeEF_Sprachen = planungsdaten.gkl[0];
		gklWahlen.idKlausurvorgabeEF_GW = planungsdaten.gkl[1];
		gklWahlen.idKlausurvorgabeEF_NW = planungsdaten.gkl[2];
		gklWahlen.idKlausurvorgabeQ_Sprachen = planungsdaten.gkl[3];
		gklWahlen.idKlausurvorgabeQ_GW = planungsdaten.gkl[4];
		gklWahlen.idKlausurvorgabeQ_NW = planungsdaten.gkl[5];
	}



	/**
	 * Import die Laufbahnplanungsdaten in der Version 2 des Datenformates
	 *
	 * @param daten   die Laufbahnplanungsdaten in der Version 2
	 *
	 * @return die geladenen Laufbahnplanungsdaten
	 */
	public static @NotNull GostLaufbahnplanungDataHandler importV2(final @NotNull GostLaufbahnplanungExportV2 daten) {
		return new GostLaufbahnplanungDataHandler(daten);
	}


	private static @NotNull List<GostLaufbahnplanungExportV1Beratungslehrer> exportV1Beratungslehrer(final @NotNull List<GostBeratungslehrer> listBeratungslehrer) {
		final @NotNull List<GostLaufbahnplanungExportV1Beratungslehrer> result = new ArrayList<>();
		for (final @NotNull GostBeratungslehrer beratungslehrer : listBeratungslehrer) {
			final @NotNull GostLaufbahnplanungExportV1Beratungslehrer l = new GostLaufbahnplanungExportV1Beratungslehrer();
			l.id = beratungslehrer.id;
			l.kuerzel = beratungslehrer.kuerzel;
			l.nachname = beratungslehrer.nachname;
			l.vorname = beratungslehrer.vorname;
			result.add(l);
		}
		return result;
	}

	private static @NotNull List<GostLaufbahnplanungExportV1Fach> exportV1GostFaecher(final @NotNull List<GostFach> faecher) {
		final @NotNull List<GostLaufbahnplanungExportV1Fach> result = new ArrayList<>();
		for (final @NotNull GostFach fach : faecher) {
			final @NotNull GostLaufbahnplanungExportV1Fach f = new GostLaufbahnplanungExportV1Fach();
			f.id = fach.id;
			f.kuerzel = fach.kuerzel;
			f.kuerzelAnzeige = fach.kuerzelAnzeige;
			f.bezeichnung = fach.bezeichnung;
			f.sortierung = fach.sortierung;
			f.istPruefungsordnungsRelevant = fach.istPruefungsordnungsRelevant;
			f.istFremdsprache = fach.istFremdsprache;
			f.istFremdSpracheNeuEinsetzend = fach.istFremdSpracheNeuEinsetzend;
			f.biliSprache = fach.biliSprache;
			f.istMoeglichAbiLK = fach.istMoeglichAbiLK;
			f.istMoeglichAbiGK = fach.istMoeglichAbiGK;
			f.istMoeglichEF1 = fach.istMoeglichEF1;
			f.istMoeglichEF2 = fach.istMoeglichEF2;
			f.istMoeglichQ11 = fach.istMoeglichQ11;
			f.istMoeglichQ12 = fach.istMoeglichQ12;
			f.istMoeglichQ21 = fach.istMoeglichQ21;
			f.istMoeglichQ22 = fach.istMoeglichQ22;
			f.wochenstundenQualifikationsphase = fach.wochenstundenQualifikationsphase;
			f.projektKursLeitfach1ID = fach.projektKursLeitfach1ID;
			f.projektKursLeitfach1Kuerzel = fach.projektKursLeitfach1Kuerzel;
			f.projektKursLeitfach2ID = fach.projektKursLeitfach2ID;
			f.projektKursLeitfach2Kuerzel = fach.projektKursLeitfach2Kuerzel;
			result.add(f);
		}
		return result;
	}

	private static @NotNull List<GostLaufbahnplanungExportV1Fachkombination> exportV1Fachkombinationen(final @NotNull List<GostJahrgangFachkombination> listKombis) {
		final @NotNull List<GostLaufbahnplanungExportV1Fachkombination> result = new ArrayList<>();
		for (final @NotNull GostJahrgangFachkombination kombi : listKombis) {
			final @NotNull GostLaufbahnplanungExportV1Fachkombination k = new GostLaufbahnplanungExportV1Fachkombination();
			k.id = kombi.id;
			k.abiturjahr = kombi.abiturjahr;
			k.fachID1 = kombi.fachID1;
			k.kursart1 = kombi.kursart1;
			k.fachID2 = kombi.fachID2;
			k.kursart2 = kombi.kursart2;
			k.gueltigInHalbjahr[0] = kombi.gueltigInHalbjahr[0];
			k.gueltigInHalbjahr[1] = kombi.gueltigInHalbjahr[1];
			k.gueltigInHalbjahr[2] = kombi.gueltigInHalbjahr[2];
			k.gueltigInHalbjahr[3] = kombi.gueltigInHalbjahr[3];
			k.gueltigInHalbjahr[4] = kombi.gueltigInHalbjahr[4];
			k.gueltigInHalbjahr[5] = kombi.gueltigInHalbjahr[5];
			k.typ = kombi.typ;
			k.hinweistext = kombi.hinweistext;
			result.add(k);
		}
		return result;
	}

	private static void exportV1Sprachdaten(final @NotNull GostLaufbahnplanungExportV1Schueler planungsdaten, final @NotNull Abiturdaten abiturdaten) {
		for (final @NotNull Sprachbelegung bel : abiturdaten.sprachendaten.belegungen) {
			final @NotNull GostLaufbahnplanungExportV1Sprachbelegung mappedBel = new GostLaufbahnplanungExportV1Sprachbelegung();
			mappedBel.sprache = bel.sprache;
			mappedBel.istNachweis = bel.istNachweis;
			mappedBel.reihenfolge = bel.reihenfolge;
			mappedBel.belegungVonJahrgang = bel.belegungVonJahrgang;
			mappedBel.belegungVonAbschnitt = bel.belegungVonAbschnitt;
			mappedBel.belegungBisJahrgang = bel.belegungBisJahrgang;
			mappedBel.belegungBisAbschnitt = bel.belegungBisAbschnitt;
			mappedBel.referenzniveau = bel.referenzniveau;
			mappedBel.hatKleinesLatinum = bel.hatKleinesLatinum;
			mappedBel.hatLatinum = bel.hatLatinum;
			mappedBel.hatGraecum = bel.hatGraecum;
			mappedBel.hatHebraicum = bel.hatHebraicum;
			planungsdaten.sprachendaten.belegungen.add(mappedBel);
		}
		for (final @NotNull Sprachpruefung pruef : abiturdaten.sprachendaten.pruefungen) {
			final @NotNull GostLaufbahnplanungExportV1Sprachpruefung mappedPruef = new GostLaufbahnplanungExportV1Sprachpruefung();
			mappedPruef.sprache = pruef.sprache;
			mappedPruef.jahrgang = pruef.jahrgang;
			mappedPruef.anspruchsniveauId = pruef.anspruchsniveauId;
			mappedPruef.pruefungsdatum = pruef.pruefungsdatum;
			mappedPruef.ersetzteSprache = pruef.ersetzteSprache;
			mappedPruef.istHSUPruefung = pruef.istHSUPruefung;
			mappedPruef.istFeststellungspruefung = pruef.istFeststellungspruefung;
			mappedPruef.kannErstePflichtfremdspracheErsetzen = pruef.kannErstePflichtfremdspracheErsetzen;
			mappedPruef.kannZweitePflichtfremdspracheErsetzen = pruef.kannZweitePflichtfremdspracheErsetzen;
			mappedPruef.kannWahlpflichtfremdspracheErsetzen = pruef.kannWahlpflichtfremdspracheErsetzen;
			mappedPruef.kannBelegungAlsFortgefuehrteSpracheErlauben = pruef.kannBelegungAlsFortgefuehrteSpracheErlauben;
			mappedPruef.referenzniveau = pruef.referenzniveau;
			mappedPruef.note = pruef.note;
			mappedPruef.zeugnisbezeichnung = pruef.zeugnisbezeichnung;
			planungsdaten.sprachendaten.pruefungen.add(mappedPruef);
		}
		planungsdaten.bilingualeSprache = abiturdaten.bilingualeSprache;
	}


	/**
	 * Exportiert die Informationen zu der Laufbahnplanung des Schüler in das Export-Format in Version 1
	 *
	 * @param dateNow   das aktuelle Datum in der Form DD.MM.YYYY
	 *
	 * @return der Laufbahnplanungs-Export in Version 1
	 */
	public @NotNull GostLaufbahnplanungExportV1 exportV1(final @NotNull String dateNow) {
		final @NotNull GostLaufbahnplanungExportV1 daten = new GostLaufbahnplanungExportV1();
		daten.schulNr = this.schuleStammdaten.schulNr;
		daten.schulBezeichnung1 = this.schuleStammdaten.bezeichnung1;
		daten.schulBezeichnung2 = (this.schuleStammdaten.bezeichnung2 == null) ? "" : this.schuleStammdaten.bezeichnung2;
		daten.schulBezeichnung3 = (this.schuleStammdaten.bezeichnung3 == null) ? "" : this.schuleStammdaten.bezeichnung3;
		daten.anmerkungen = "Letzte Änderung am " + dateNow;
		daten.abiturjahr = this.abiturdaten.abiturjahr;
		daten.jahrgang = this.gostJahrgang.jahrgang;
		daten.hatZusatzkursGE = this.gostJahrgangsdaten.hatZusatzkursGE;
		daten.beginnZusatzkursGE = this.gostJahrgangsdaten.beginnZusatzkursGE;
		daten.hatZusatzkursSW = this.gostJahrgangsdaten.hatZusatzkursSW;
		daten.beginnZusatzkursSW = this.gostJahrgangsdaten.beginnZusatzkursSW;
		daten.beratungslehrer.addAll(exportV1Beratungslehrer(this.gostJahrgangsdaten.beratungslehrer));
		daten.textBeratungsbogen = this.gostJahrgangsdaten.textBeratungsbogen;
		daten.fachkombinationen.addAll(exportV1Fachkombinationen(this.faecherManager.getFachkombinationen()));
		daten.faecher.addAll(exportV1GostFaecher(this.faecherManager.faecher()));
		final GostLaufbahnplanungExportV1Schueler s = new GostLaufbahnplanungExportV1Schueler();
		s.id = this.schueler.id;
		s.idEnc = this.idSchuelerEncrpyted;
		s.vorname = this.schueler.vorname;
		s.nachname = this.schueler.nachname;
		s.geschlecht = this.schueler.geschlecht;
		s.bilingualeSprache = this.abiturdaten.bilingualeSprache;
		exportV1Sprachdaten(s, this.abiturdaten);
		for (final @NotNull GostHalbjahr hj : GostHalbjahr.values()) {
			s.bewertetesHalbjahr[hj.id] = this.abiturdaten.bewertetesHalbjahr[hj.id];
		}
		for (int i = 0; i < this.abiturdaten.fachbelegungen.size(); i++) {
			final AbiturFachbelegung belegung = this.abiturdaten.fachbelegungen.get(i);
			final @NotNull GostLaufbahnplanungExportV1Fachbelegung fb = new GostLaufbahnplanungExportV1Fachbelegung();
			fb.fachID = belegung.fachID;
			fb.abiturFach = belegung.abiturFach;
			for (final @NotNull GostHalbjahr hj : GostHalbjahr.values()) {
				final AbiturFachbelegungHalbjahr hjBelegung = belegung.belegungen[hj.id];
				if (hjBelegung == null) {
					continue;
				}
				fb.kursart[hj.id] = hjBelegung.kursartKuerzel;
				fb.schriftlich[hj.id] = hjBelegung.schriftlich;
			}
			s.fachbelegungen.add(fb);
		}
		daten.schueler.add(s);
		return daten;
	}


	private static @NotNull List<GostLaufbahnplanungExportV2Beratungslehrer> exportV2Beratungslehrer(final @NotNull List<GostBeratungslehrer> listBeratungslehrer) {
		final @NotNull List<GostLaufbahnplanungExportV2Beratungslehrer> result = new ArrayList<>();
		for (final @NotNull GostBeratungslehrer beratungslehrer : listBeratungslehrer) {
			final @NotNull GostLaufbahnplanungExportV2Beratungslehrer l = new GostLaufbahnplanungExportV2Beratungslehrer();
			l.id = beratungslehrer.id;
			l.kuerzel = beratungslehrer.kuerzel;
			l.nachname = beratungslehrer.nachname;
			l.vorname = beratungslehrer.vorname;
			result.add(l);
		}
		return result;
	}

	private static @NotNull List<GostLaufbahnplanungExportV2Fach> exportV2GostFaecher(final @NotNull List<GostFach> faecher) {
		final @NotNull List<GostLaufbahnplanungExportV2Fach> result = new ArrayList<>();
		for (final @NotNull GostFach fach : faecher) {
			final @NotNull GostLaufbahnplanungExportV2Fach f = new GostLaufbahnplanungExportV2Fach();
			f.id = fach.id;
			f.kuerzel = fach.kuerzel;
			f.kuerzelAnzeige = fach.kuerzelAnzeige;
			f.bezeichnung = fach.bezeichnung;
			f.sortierung = fach.sortierung;
			f.istPruefungsordnungsRelevant = fach.istPruefungsordnungsRelevant;
			f.istFremdsprache = fach.istFremdsprache;
			f.istFremdSpracheNeuEinsetzend = fach.istFremdSpracheNeuEinsetzend;
			f.biliSprache = fach.biliSprache;
			f.istMoeglichAbiLK = fach.istMoeglichAbiLK;
			f.istMoeglichAbiGK = fach.istMoeglichAbiGK;
			f.istMoeglich[0] = fach.istMoeglichEF1;
			f.istMoeglich[1] = fach.istMoeglichEF2;
			f.istMoeglich[2] = fach.istMoeglichQ11;
			f.istMoeglich[3] = fach.istMoeglichQ12;
			f.istMoeglich[4] = fach.istMoeglichQ21;
			f.istMoeglich[5] = fach.istMoeglichQ22;
			f.wochenstundenQualifikationsphase = fach.wochenstundenQualifikationsphase;
			f.referenzfach1ID = fach.projektKursLeitfach1ID;
			f.referenzfach2ID = fach.projektKursLeitfach2ID;
			result.add(f);
		}
		return result;
	}

	private static @NotNull List<GostLaufbahnplanungExportV2Fachkombination> exportV2Fachkombinationen(final @NotNull List<GostJahrgangFachkombination> listKombis) {
		final @NotNull List<GostLaufbahnplanungExportV2Fachkombination> result = new ArrayList<>();
		for (final @NotNull GostJahrgangFachkombination kombi : listKombis) {
			final @NotNull GostLaufbahnplanungExportV2Fachkombination k = new GostLaufbahnplanungExportV2Fachkombination();
			k.id = kombi.id;
			k.abiturjahr = kombi.abiturjahr;
			k.fachID1 = kombi.fachID1;
			k.kursart1 = kombi.kursart1;
			k.fachID2 = kombi.fachID2;
			k.kursart2 = kombi.kursart2;
			k.gueltigInHalbjahr[0] = kombi.gueltigInHalbjahr[0];
			k.gueltigInHalbjahr[1] = kombi.gueltigInHalbjahr[1];
			k.gueltigInHalbjahr[2] = kombi.gueltigInHalbjahr[2];
			k.gueltigInHalbjahr[3] = kombi.gueltigInHalbjahr[3];
			k.gueltigInHalbjahr[4] = kombi.gueltigInHalbjahr[4];
			k.gueltigInHalbjahr[5] = kombi.gueltigInHalbjahr[5];
			k.typ = kombi.typ;
			k.hinweistext = kombi.hinweistext;
			result.add(k);
		}
		return result;
	}

	private static void exportV2Sprachdaten(final @NotNull GostLaufbahnplanungExportV2Schueler planungsdaten, final @NotNull Abiturdaten abiturdaten) {
		for (final @NotNull Sprachbelegung bel : abiturdaten.sprachendaten.belegungen) {
			final @NotNull GostLaufbahnplanungExportV2SchuelerSprachbelegung mappedBel = new GostLaufbahnplanungExportV2SchuelerSprachbelegung();
			mappedBel.sprache = bel.sprache;
			mappedBel.istNachweis = bel.istNachweis;
			mappedBel.reihenfolge = bel.reihenfolge;
			mappedBel.belegungVonJahrgang = bel.belegungVonJahrgang;
			mappedBel.belegungVonAbschnitt = bel.belegungVonAbschnitt;
			mappedBel.belegungBisJahrgang = bel.belegungBisJahrgang;
			mappedBel.belegungBisAbschnitt = bel.belegungBisAbschnitt;
			mappedBel.referenzniveau = bel.referenzniveau;
			mappedBel.hatKleinesLatinum = bel.hatKleinesLatinum;
			mappedBel.hatLatinum = bel.hatLatinum;
			mappedBel.hatGraecum = bel.hatGraecum;
			mappedBel.hatHebraicum = bel.hatHebraicum;
			planungsdaten.sprachbelegungen.add(mappedBel);
		}
		for (final @NotNull Sprachpruefung pruef : abiturdaten.sprachendaten.pruefungen) {
			final @NotNull GostLaufbahnplanungExportV2SchuelerSprachpruefung mappedPruef = new GostLaufbahnplanungExportV2SchuelerSprachpruefung();
			mappedPruef.sprache = pruef.sprache;
			mappedPruef.jahrgang = pruef.jahrgang;
			mappedPruef.anspruchsniveauId = pruef.anspruchsniveauId;
			mappedPruef.pruefungsdatum = pruef.pruefungsdatum;
			mappedPruef.ersetzteSprache = pruef.ersetzteSprache;
			mappedPruef.istHSUPruefung = pruef.istHSUPruefung;
			mappedPruef.istFeststellungspruefung = pruef.istFeststellungspruefung;
			mappedPruef.kannErstePflichtfremdspracheErsetzen = pruef.kannErstePflichtfremdspracheErsetzen;
			mappedPruef.kannZweitePflichtfremdspracheErsetzen = pruef.kannZweitePflichtfremdspracheErsetzen;
			mappedPruef.kannWahlpflichtfremdspracheErsetzen = pruef.kannWahlpflichtfremdspracheErsetzen;
			mappedPruef.kannBelegungAlsFortgefuehrteSpracheErlauben = pruef.kannBelegungAlsFortgefuehrteSpracheErlauben;
			mappedPruef.referenzniveau = pruef.referenzniveau;
			mappedPruef.note = pruef.note;
			mappedPruef.zeugnisbezeichnung = pruef.zeugnisbezeichnung;
			planungsdaten.sprachpruefungen.add(mappedPruef);
		}
		planungsdaten.bilingualeSprache = abiturdaten.bilingualeSprache;
	}

	private void exportV2GKL(final @NotNull GostLaufbahnplanungExportV2 daten, final @NotNull GostLaufbahnplanungExportV2Schueler schueler) {
		for (final @NotNull GostLaufbahnplanungGKLKlausurvorgabe eintrag : this.mapKlausurvorgaben.values()) {
			final @NotNull GostLaufbahnplanungExportV2GKL gkl = new GostLaufbahnplanungExportV2GKL();
			final @NotNull GostKlausurvorgabe vorgabe = eintrag.getVorgabe();
			gkl.id = vorgabe.id;
			gkl.idFach = vorgabe.idFach;
			gkl.idHalbjahr = vorgabe.halbjahr;
			gkl.quartal = vorgabe.halbjahr;
			daten.gkl.add(gkl);
		}
		schueler.gkl[0] = this.gklWahlen.idKlausurvorgabeEF_Sprachen;
		schueler.gkl[1] = this.gklWahlen.idKlausurvorgabeEF_GW;
		schueler.gkl[2] = this.gklWahlen.idKlausurvorgabeEF_NW;
		schueler.gkl[3] = this.gklWahlen.idKlausurvorgabeQ_Sprachen;
		schueler.gkl[4] = this.gklWahlen.idKlausurvorgabeQ_GW;
		schueler.gkl[5] = this.gklWahlen.idKlausurvorgabeQ_NW;
	}

	/**
	 * Exportiert die Informationen zu der Laufbahnplanung des Schüler in das Export-Format in Version 2
	 *
	 * @param dateNow   das aktuelle Datum in der Form DD.MM.YYYY
	 *
	 * @return der Laufbahnplanungs-Export in Version 2
	 */
	public @NotNull GostLaufbahnplanungExportV2 exportV2(final @NotNull String dateNow) {
		final @NotNull GostLaufbahnplanungExportV2 daten = new GostLaufbahnplanungExportV2();
		daten.schulNr = this.schuleStammdaten.schulNr;
		daten.schulBezeichnung1 = this.schuleStammdaten.bezeichnung1;
		daten.schulBezeichnung2 = (this.schuleStammdaten.bezeichnung2 == null) ? "" : this.schuleStammdaten.bezeichnung2;
		daten.schulBezeichnung3 = (this.schuleStammdaten.bezeichnung3 == null) ? "" : this.schuleStammdaten.bezeichnung3;
		daten.anmerkungen = "Letzte Änderung am " + dateNow;
		daten.abiturjahr = abiturdaten.abiturjahr;
		daten.jahrgang = this.gostJahrgang.jahrgang;
		daten.hatZusatzkursGE = this.gostJahrgangsdaten.hatZusatzkursGE;
		daten.beginnZusatzkursGE = this.gostJahrgangsdaten.beginnZusatzkursGE;
		daten.hatZusatzkursSW = this.gostJahrgangsdaten.hatZusatzkursSW;
		daten.beginnZusatzkursSW = this.gostJahrgangsdaten.beginnZusatzkursSW;
		daten.beratungslehrer.addAll(exportV2Beratungslehrer(this.gostJahrgangsdaten.beratungslehrer));
		daten.textBeratungsbogen = this.gostJahrgangsdaten.textBeratungsbogen;
		daten.fachkombinationen.addAll(exportV2Fachkombinationen(this.faecherManager.getFachkombinationen()));
		daten.faecher.addAll(exportV2GostFaecher(this.faecherManager.faecher()));

		final GostLaufbahnplanungExportV2Schueler s = new GostLaufbahnplanungExportV2Schueler();
		s.id = this.schueler.id;
		s.idEnc = this.idSchuelerEncrpyted;
		s.vorname = this.schueler.vorname;
		s.nachname = this.schueler.nachname;
		s.geschlecht = this.schueler.geschlecht;
		exportV2Sprachdaten(s, this.abiturdaten);
		for (final @NotNull GostHalbjahr hj : GostHalbjahr.values()) {
			s.bewertetesHalbjahr[hj.id] = abiturdaten.bewertetesHalbjahr[hj.id];
		}
		s.istG8 = (Schulgliederung.data().getWertByIDOrNull(schueler.idSchulgliederung) == Schulgliederung.GY8);
		for (int i = 0; i < abiturdaten.fachbelegungen.size(); i++) {
			final AbiturFachbelegung belegung = this.abiturdaten.fachbelegungen.get(i);
			final GostLaufbahnplanungExportV2SchuelerFachbelegung fb = new GostLaufbahnplanungExportV2SchuelerFachbelegung();
			fb.fachID = belegung.fachID;
			fb.abiturFach = belegung.abiturFach;
			fb.idReferenzfach = belegung.idReferenzfach;
			for (final @NotNull GostHalbjahr hj : GostHalbjahr.values()) {
				final AbiturFachbelegungHalbjahr hjBelegung = belegung.belegungen[hj.id];
				if ((hjBelegung == null) || ("".equals(hjBelegung.kursartKuerzel))) {
					continue;
				}
				fb.kursart[hj.id] = hjBelegung.kursartKuerzel;
				fb.schriftlich[hj.id] = hjBelegung.schriftlich;
			}
			s.fachbelegungen.add(fb);
		}
		daten.schueler.add(s);
		this.exportV2GKL(daten, s);
		return daten;
	}

}
