package de.svws_nrw.asd.export.aggregation;

import static de.svws_nrw.asd.export.aggregation.AggregationUtils.auffuellenStellengerecht;
import static de.svws_nrw.asd.export.aggregation.AggregationUtils.istBK;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import de.svws_nrw.asd.data.kurse.KursLehrer;
import de.svws_nrw.asd.data.kurse.ZulaessigeKursartKatalogEintrag;
import de.svws_nrw.asd.data.statistik.FachStatistikGesamt;
import de.svws_nrw.asd.data.statistik.KlassenStatistikGesamt;
import de.svws_nrw.asd.data.statistik.KursStatistikGesamt;
import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerLeistungsdatenStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerLernabschnittStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.export.data.KlassenStatistikExport;
import de.svws_nrw.asd.export.data.StatistikExport;
import de.svws_nrw.asd.export.data.UnterrichtsverteilungStatistikExport;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schule.Schulform;

/*
 * AggregationStatistikExport.java
 *
 * Copyright (c) 2026 Projekt SVWS-Server - Schulverwaltungsserver
 *
 * Landesbetrieb Information und Technik Nordrhein-Westfalen (IT.NRW)
 * Alle Rechte vorbehalten.
 *
 * Versionshistorie
 * @version 1.00 - 11.03.2026 - Daniel Knittel (knitt01) - erste Version
 * @version 1.00 - 11.03.2026 - Mahmoud Guedda (guedd01) - erste Version
 */

/**
 * Die Klasse AggregationStatistikExport ist eine Klasse im Paket de.svws_nrw.asd.export.aggregation des Projekts SVWS-Server.
 *
 * @since 2026
 * @version 1.00 - 11.03.2026
 * @author Daniel Knittel (knitt01)
 * @author Mahmoud Guedda (guedd01)
 *
 */
public class AggregationUvdStatistikExport {

	/**
	 * Länge der Zeichenkette 'Bildungsgangkennzeichen'
	 */
	private static final int LAENGE_BILDUNGSGANGKENNZ = 1;
	/**
	 * Länge der Zeichenkette 'Fach'
	 */
	private static final int LAENGE_FACH = 2;
	/**
	 * Länge der Zeichenkette 'Jahrgang'
	 */
	private static final int LAENGE_JAHRGANG = 2;
	/**
	 * Länge der Zeichenkette 'Lehrerkürzel'
	 */
	private static final int LAENGE_LEHK = 4;
	/**
	 * Länge der Zeichenkette 'Parallelität'
	 */
	private static final int LAENGE_PARALLELITAET = 2;
	/**
	 * Länge der Zeichenkette 'Parallelität2'
	 */
	private static final int LAENGE_PARALLELITAET2 = 1;
	/**
	 * Länge der Zeichenkette 'schulinterne Bezeichnung'
	 */
	private static final int LAENGE_SCHULINTBEZ = 6;
	/**
	 * Länge der Zeichenkette 'Schulgliederung'
	 */
	private static final int LAENGE_SGL = 3;
	/**
	 * Länge der Zeichenkette 'Teilklasse'
	 */
	private static final int LAENGE_TEILKLASSE = 2;

	/**
	* Das aktuelle Schuljahr in vierstelliger Form.
	*/
	private final int aktuellesSchuljahr;

	/**
	 * Zuordnung der ID eines Fachs zum zugehörigen {@link FachStatistikGesamt}-Objekt.
	 */
	private final Map<Long, FachStatistikGesamt> fachIdMap;

	/**
	 * Eine Liste der Fehlermeldungen zu den aufgetretenen Fehlern.
	 */
	private final List<String> fehlermeldungen;

	private final Set<String> jahrgaengeSek2 = Set.of("11", "12", "13", "EF", "Q1", "Q2");

	/**
	 * Zuordnug der Jahrgang-IDs der Schule zu den idJahrgang des Katalogs.
	 */
	private final Map<Long, Long> jahrgangIdMap;


	/**
	 * Zuordnung der ID einer Klasse zum zugehörigen {@link KlassenStatistikGesamt}-Objekt.
	 */
	private final Map<Long, KlassenStatistikGesamt> klasseIdMap;

	/**
	 * Zuordnung der schulinternen Bezeichung einer Klasse zum zugehörigen {@link KlassenStatistikExport}-Objekt. <br>
	 * Wird nur für die B-Schulen (BK/SB) eingesetzt.
	 */
	private Map<String, List<KlassenStatistikExport>> klassenExportMap = null;

	/**
	 * Zuordnung der ID eines Kurses zum zugehörigen {@link kursStatistikGesamt}-Objekt.
	 */
	private final Map<Long, KursStatistikGesamt> kurseIdMap;

	/**
	 * Zuordnung der ID eines Lehrers zum zugehörigen {@link LehrerStatistikGesamt}-Objekt.
	 */
	private final Map<Long, LehrerStatistikGesamt> lehrerIdMap;

	/**
	 * Die Schulform der Schule als Enum {@link Schulform}.
	 */
	private final Schulform schulform;

	/**
	 * Die für den Export vorgesehenen Statistikdaten mit den Aggregaten.
	 */
	private final StatistikExport statistikExport;

	/**
	 * Die gesamten Statistikdaten der Schule, welche von einer Schule bei der Erfassung der amtlichen Schulstatistik übertragen werden.
	 */
	private final StatistikGesamt statistikGesamt;




	/**
	 * Konstruktor
	 *
	 * @param statistikGesamt
	 * @param statistikExport
	 * @param fehlermeldungen
	 * @param jahrgangIdMap
	 * @param fachIdMap
	 * @param klasseIdMap
	 * @param lehrerIdMap
	 * @param kurseIdMap
	 * @param aktuellesSchuljahr
	 */
	public AggregationUvdStatistikExport(final StatistikGesamt statistikGesamt, final StatistikExport statistikExport,
			final List<String> fehlermeldungen, final Map<Long, Long> jahrgangIdMap, final Map<Long, FachStatistikGesamt> fachIdMap,
			final Map<Long, KlassenStatistikGesamt> klasseIdMap, final Map<Long, LehrerStatistikGesamt> lehrerIdMap,
			final Map<Long, KursStatistikGesamt> kurseIdMap, final int aktuellesSchuljahr) {
		this.statistikGesamt = statistikGesamt;
		this.statistikExport = statistikExport;
		this.fehlermeldungen = fehlermeldungen;
		this.jahrgangIdMap = jahrgangIdMap;
		this.fachIdMap = fachIdMap;
		this.klasseIdMap = klasseIdMap;
		this.lehrerIdMap = lehrerIdMap;
		this.kurseIdMap = kurseIdMap;
		this.aktuellesSchuljahr = aktuellesSchuljahr;
		schulform = Schulform.data().getWertByBezeichner(statistikGesamt.schule.schulform);

		if (istBK(schulform)) {
			klassenExportMap =
					statistikExport.klassenStatistikExport.stream()
							.collect(Collectors.groupingBy(e -> auffuellenStellengerecht(e.schulinterneBezeichnung, LAENGE_SCHULINTBEZ)));
		}
	}

	/**
	 * Ermittelt die Anzahl der Schülerinnen in dieser Unterrichtseinheit.
	 *
	 * @param schuelerzahlenWeiblichProKursIdUndKey   - Map mit der Anzahl der Schülerinnen pro KursID und Key
	 * @param kursMapEntry                            - Eintrag zu einem Kurs
	 * @return Anzahl der weiblichen Schüler
	 */
	private static int ermittelnAnzahlSchuelerWeiblich(final HashMap<Long, HashMap<String, Long>> schuelerzahlenWeiblichProKursIdUndKey,
			final Entry<Long, HashMap<String, List<SchuelerLeistungsdatenStatistikGesamt>>> kursMapEntry) {
		final HashMap<String, Long> countMap = schuelerzahlenWeiblichProKursIdUndKey.get(kursMapEntry.getKey());

		if ((countMap != null) && (!countMap.isEmpty())) {
			final Long count = countMap.values().iterator().next();

			if (count != null) {
				return count.intValue();
			}
		}

		return 0;
	}

	/**
	 * Ermittelt den ersten Eintrag zu einer Unterrichtseinheit (Kurs). <br>
	 * Kann dieser nicht ermittelt werden oder ist leer wird null zurückgegeben.
	 *
	 * @param kursMapEntry   - Eintrag zu einem Kurs
	 * @return der erste Eintrag zu einer Unterrichtseinheit (Kurs) oder null
	 */
	private static Entry<String, List<SchuelerLeistungsdatenStatistikGesamt>> ermittelnErsterEintragZuKurs(
			final Entry<Long, HashMap<String, List<SchuelerLeistungsdatenStatistikGesamt>>> kursMapEntry) {
		Entry<String, List<SchuelerLeistungsdatenStatistikGesamt>> stammEntry = null;

		if ((kursMapEntry.getValue() != null) && (!kursMapEntry.getValue().isEmpty())) {
			stammEntry = kursMapEntry.getValue().entrySet().iterator().next();
		}

		if ((stammEntry == null) || (stammEntry.getValue() == null) || (stammEntry.getValue().isEmpty())) {
			return null;
		}

		return stammEntry;
	}



	/**
	 * @param stammEntry
	 * @param uvdExportStammsatz
	 */
	private static void fuellenSchluesselFelderSchulformA(final Entry<String, List<SchuelerLeistungsdatenStatistikGesamt>> stammEntry,
			final UnterrichtsverteilungStatistikExport uvdExportStammsatz) {
		uvdExportStammsatz.jahrgang = stammEntry.getKey().substring(0, LAENGE_JAHRGANG);
		uvdExportStammsatz.schulgliederung = stammEntry.getKey().substring(LAENGE_JAHRGANG, LAENGE_JAHRGANG + LAENGE_SGL).equals("***") ? ""
				: stammEntry.getKey().substring(LAENGE_JAHRGANG, LAENGE_JAHRGANG + LAENGE_SGL);
		uvdExportStammsatz.artDerGruppe = stammEntry.getKey().substring(LAENGE_JAHRGANG + LAENGE_SGL);
	}

	/**
	 * @param stammEntry
	 * @param uvdExportStammsatz
	 */
	private static void fuellenSchluesselFelderSchulformB(final Entry<String, List<SchuelerLeistungsdatenStatistikGesamt>> stammEntry,
			final UnterrichtsverteilungStatistikExport uvdExportStammsatz) {
		final String schulinterneBezeichnung = stammEntry.getKey().substring(0, LAENGE_SCHULINTBEZ);
		uvdExportStammsatz.jahrgang = stammEntry.getKey().substring(LAENGE_SCHULINTBEZ, LAENGE_SCHULINTBEZ + LAENGE_JAHRGANG);
		uvdExportStammsatz.bildungsgangkennzeichen =
				stammEntry.getKey().substring(LAENGE_SCHULINTBEZ + LAENGE_JAHRGANG, LAENGE_SCHULINTBEZ + LAENGE_JAHRGANG + LAENGE_BILDUNGSGANGKENNZ);
		uvdExportStammsatz.parallelitaet2 = stammEntry.getKey().substring(LAENGE_SCHULINTBEZ + LAENGE_JAHRGANG + LAENGE_BILDUNGSGANGKENNZ,
				LAENGE_SCHULINTBEZ + LAENGE_JAHRGANG + LAENGE_BILDUNGSGANGKENNZ + LAENGE_PARALLELITAET2);
		uvdExportStammsatz.teilklasse =
				stammEntry.getKey().substring(LAENGE_SCHULINTBEZ + LAENGE_JAHRGANG + LAENGE_BILDUNGSGANGKENNZ + LAENGE_PARALLELITAET2,
						LAENGE_SCHULINTBEZ + LAENGE_JAHRGANG + LAENGE_BILDUNGSGANGKENNZ + LAENGE_PARALLELITAET2 + LAENGE_TEILKLASSE);
		uvdExportStammsatz.schulinterneBezeichnung = schulinterneBezeichnung;
	}

	/**
	 * Hier werden Einträge zu einem Schlüssel (key) der inneren Maps initialisiert bzw. hinzugefügt.
	 *
	 * @param kursId
	 * @param key
	 * @param schueler
	 * @param schuelerzahlenWeiblichProKursIdUndKey
	 * @param leistungsdaten
	 * @param kurseOhneKlassenverband
	 */
	private static void initUndHinzufuegenEintraegeInnereMap(final Long kursId, final String key, final SchuelerStatistikGesamt schueler,
			final HashMap<Long, HashMap<String, Long>> schuelerzahlenWeiblichProKursIdUndKey, final SchuelerLeistungsdatenStatistikGesamt leistungsdaten,
			final HashMap<Long, HashMap<String, List<SchuelerLeistungsdatenStatistikGesamt>>> kurseOhneKlassenverband) {
		// Schlüssel der inneren Map schon vorhanden
		if (kurseOhneKlassenverband.get(kursId).containsKey(key)) {
			kurseOhneKlassenverband.get(kursId).get(key).add(leistungsdaten);

			// Hochzählen weibliche Schüler pro KursID und Key
			if (Geschlecht.W.id == schueler.geschlecht) {

				if (schuelerzahlenWeiblichProKursIdUndKey.get(kursId).containsKey(key)) {
					schuelerzahlenWeiblichProKursIdUndKey.get(kursId).put(key,
							schuelerzahlenWeiblichProKursIdUndKey.get(kursId).get(key) + 1);
				} else {
					schuelerzahlenWeiblichProKursIdUndKey.get(kursId).put(key, 1L);
				}
			}

		} else { // Initialisieren der Einträge zu einem neuen Schlüssel in der inneren Map
			kurseOhneKlassenverband.get(kursId).put(key, new LinkedList<>());
			kurseOhneKlassenverband.get(kursId).get(key).add(leistungsdaten);
			schuelerzahlenWeiblichProKursIdUndKey.get(kursId).put(key, 0L);

			if (Geschlecht.W.id == schueler.geschlecht) {
				schuelerzahlenWeiblichProKursIdUndKey.get(kursId).put(key, 1L);
			}
		}
	}

	/**
	 * @param fachId
	 * @return BilingualeSprache
	 */
	public String ermittelnBilingualeSprache(final long fachId) {

		if ((Schulform.H == schulform) || (Schulform.V == schulform) || (Schulform.S == schulform) || (Schulform.FW == schulform)) {
			return "";
		}

		final FachStatistikGesamt fach = fachIdMap.get(fachId);
		if ((fach != null) && (fach.bilingualeSprache != null)) {
			return fach.bilingualeSprache.equals("D") ? "" : fach.bilingualeSprache;
		}

		return "";
	}

	/**
	 * Führt die Aggregation der {@link StatistikGesamt}-Daten der UVD in das {@link StatistikExport}-Datenobjekt aus. <br>
	 * Fehlermeldungen zu gegebenenfalls aufgetretenen Fehlern werden in die Liste {@link #fehlermeldungen} geschrieben.
	 *
	 * @return - Ausführung erfolgreich und ohne schwere Fehler
	 */
	public boolean run() {

		if (statistikGesamt == null) {
			return false;
		}

		// Unterrichtverteilungsdaten
		erstellenUvdStatistikExport();

		return true;
	}

	/**
	 * Fügt der übergebenen Map kurseImKlassenverband einen Eintrag hinzu. <br>
	 * Hierfür wird ein Schlüssel bestehend aus jahrgang, parallelitaet, lehrerkuerzel und fach gebildet. <br>
	 * Gibt es unter diesem Schlüssel schon einen Eintrag in der Map, so wird der Satz an die bestehende List angehängt. <br>
	 * Gibt es noch keinen Eintrag, so wird eine neue Liste unter diesem erstellt und der Satz eingefügt.
	 *
	 * @param kurseImKlassenverband    - Die Map, in die die Sätze eingefügt werden
	 * @param schuelerLeistungsdaten   - Ein Satz
	 * @param jahrgangKurseImKlassenverband
	 * @param klasse
	 */
	private void bauenMapKurseImKlassenverband(final HashMap<String, List<SchuelerLeistungsdatenStatistikGesamt>> kurseImKlassenverband,
			final SchuelerLeistungsdatenStatistikGesamt schuelerLeistungsdaten,
			final String jahrgangKurseImKlassenverband, final KlassenStatistikGesamt klasse) {

		final FachStatistikGesamt fachObj = fachIdMap.get(schuelerLeistungsdaten.fachID);
		final String fach = (fachObj != null) ? auffuellenStellengerecht(fachObj.kuerzelStatistik, LAENGE_FACH) : auffuellenStellengerecht("", LAENGE_FACH);

		final LehrerStatistikGesamt lehrerObj = lehrerIdMap.get(schuelerLeistungsdaten.lehrerID);
		final String lehrerkuerzel = (lehrerObj != null) ? auffuellenStellengerecht(lehrerObj.kuerzel, LAENGE_LEHK) : auffuellenStellengerecht("", LAENGE_LEHK);
		// TODO: Nachfragen - Ist es richtig, dass Fach und Lehrerkürzel in den Schlüsseln für A- und B- Schulen die Stellen tauschen?
		final String key;
		// Schlüssel für A- und B-Schulen unterschiedlich zusammensetzen
		if (istBK(schulform)) {
			final String schulinterneBezeichnung = ((klasse != null) && (klasse.kuerzel != null)) ? auffuellenStellengerecht(klasse.kuerzel, LAENGE_SCHULINTBEZ)
					: auffuellenStellengerecht("", LAENGE_SCHULINTBEZ);
			key = schulinterneBezeichnung.concat(fach).concat(lehrerkuerzel);
		} else {
			final String parallelitaetStr =
					((klasse != null) && (klasse.parallelitaet != null)) ? auffuellenStellengerecht(klasse.parallelitaet, LAENGE_PARALLELITAET)
							: auffuellenStellengerecht("", LAENGE_PARALLELITAET);
			key = jahrgangKurseImKlassenverband.concat(parallelitaetStr)
					.concat(lehrerkuerzel).concat(fach);
		}

		kurseImKlassenverband.computeIfAbsent(key, k -> new LinkedList<>());
		kurseImKlassenverband.get(key).add(schuelerLeistungsdaten);
	}

	/**
	 * @param kurseOhneKlassenverband
	 * @param schuelerzahlenWeiblichProKursIdUndKey
	 * @param schuelerVonAndererSchuleProKursId
	 * @param leistungsdaten
	 * @param jahrgangSgl
	 * @param schueler
	 * @param klasse
	 */
	private void bauenMapsKurseOhneKlassenverband(final HashMap<Long, HashMap<String, List<SchuelerLeistungsdatenStatistikGesamt>>> kurseOhneKlassenverband,
			final HashMap<Long, HashMap<String, Long>> schuelerzahlenWeiblichProKursIdUndKey, final HashMap<Long, Integer> schuelerVonAndererSchuleProKursId,
			final SchuelerLeistungsdatenStatistikGesamt leistungsdaten, final String jahrgangSgl, final SchuelerStatistikGesamt schueler,
			final KlassenStatistikGesamt klasse) {
		final Long kursId = leistungsdaten.kursID;
		final KursStatistikGesamt kurs = kurseIdMap.get(kursId);
		// Nur Kurse in die Map aufnehmen, die nicht an einer anderen Schule unterrichtet werden.
		if ((kurs != null) && (kurs.schulnummer == null)) {
			String key = "";

			if (istBK(schulform)) {
				final String schulinterneBezeichnung =
						((klasse != null) && (klasse.kuerzel != null)) ? auffuellenStellengerecht(klasse.kuerzel, LAENGE_SCHULINTBEZ)
								: auffuellenStellengerecht("", LAENGE_SCHULINTBEZ);

				if (klassenExportMap.get(schulinterneBezeichnung) != null) {
					// Für jede Teiklasse wird ein Eintrag erzeugt
					durchlaufenTeilklassenSchulformB(kurseOhneKlassenverband, schuelerzahlenWeiblichProKursIdUndKey, schuelerVonAndererSchuleProKursId,
							leistungsdaten, schueler, kursId, schulinterneBezeichnung);
				} else {
					System.out.println("Schulinterne Bezeichnung nicht gefunden: " + schulinterneBezeichnung);
				}

			} else {
				key = jahrgangSgl.concat(ermittelnKursartNummer(leistungsdaten));
				hinzufuegenEintragMapsKurseOhneKlassenverband(kurseOhneKlassenverband, schuelerzahlenWeiblichProKursIdUndKey, schuelerVonAndererSchuleProKursId,
						leistungsdaten, schueler, kursId, key);
			}

		}
	}

	/**
	 * @param kurseOhneKlassenverband
	 * @param schuelerzahlenWeiblichProKursIdUndKey
	 * @param schuelerVonAndererSchuleProKursId
	 * @param leistungsdaten
	 * @param schueler
	 * @param kursId
	 * @param schulinterneBezeichnung
	 */
	private void durchlaufenTeilklassenSchulformB(final HashMap<Long, HashMap<String, List<SchuelerLeistungsdatenStatistikGesamt>>> kurseOhneKlassenverband,
			final HashMap<Long, HashMap<String, Long>> schuelerzahlenWeiblichProKursIdUndKey, final HashMap<Long, Integer> schuelerVonAndererSchuleProKursId,
			final SchuelerLeistungsdatenStatistikGesamt leistungsdaten, final SchuelerStatistikGesamt schueler, final Long kursId,
			final String schulinterneBezeichnung) {
		String key;
		for (final KlassenStatistikExport exportKlasse : klassenExportMap.get(schulinterneBezeichnung)) {
			key = schulinterneBezeichnung.concat(auffuellenStellengerecht(exportKlasse.jahrgang, LAENGE_JAHRGANG))
					.concat(auffuellenStellengerecht(exportKlasse.bildungsgangkennzeichen, LAENGE_BILDUNGSGANGKENNZ))
					.concat(auffuellenStellengerecht(exportKlasse.parallelitaet2, LAENGE_PARALLELITAET2))
					.concat(auffuellenStellengerecht(exportKlasse.teilklasse, LAENGE_TEILKLASSE));
			hinzufuegenEintragMapsKurseOhneKlassenverband(kurseOhneKlassenverband, schuelerzahlenWeiblichProKursIdUndKey,
					schuelerVonAndererSchuleProKursId, leistungsdaten, schueler, kursId, key);
		}
	}

	/**
	 * Ermitteln des Jahrgangs für Kurse die im Klassenverband unterrichtet werden.
	 *
	 * @param klasse   - die Klassendaten
	 * @return der Jahrgang
	 */
	private String ermittelnJahrgangKurseImKlassenverband(final KlassenStatistikGesamt klasse) {
		String jahrgangKurseImKlassenverband = "JU"; // Jahrgangsübergreifende Klassen 'JU' haben in idJahrgang 'NULL' stehen

		if (klasse != null) {
			final Long jgId = jahrgangIdMap.get(klasse.idJahrgang);

			if (jgId != null) {
				final String jgKuerzel = Jahrgaenge.data().getSchluesselByIDOrNull(jgId);

				if (jgKuerzel != null) {
					jahrgangKurseImKlassenverband = jgKuerzel;
				}
			}
		}
		// Jahrgänge "01" und "02" müssen in bestimmten Fällen in die Bezeichnung für die Schuleingangsphase umgesetzt werden
		if (Set.of("01", "02").contains(jahrgangKurseImKlassenverband)
				&& !(Schulform.BK.equals(schulform) || Schulform.SB.equals(schulform) || Schulform.WB.equals(schulform))) {

			if (jahrgangKurseImKlassenverband.equals("01")) {
				jahrgangKurseImKlassenverband = "1E";
			} else {
				jahrgangKurseImKlassenverband = "2E";
			}
		}

		return jahrgangKurseImKlassenverband;
	}

	/**
	 * Ermitteln des Jahrgangs für Kurse die ohne Klassenverband unterrichtet werden.
	 *
	 * @param schueler        - die Schülerdaten
	 * @param lernabschnitt   - der aktuelle Schüler-Lernabschnitt
	 * @return der Jahrgang
	 */
	private String ermittelnJahrgangKurseOhneKlassenverband(final SchuelerStatistikGesamt schueler, final SchuelerLernabschnittStatistikGesamt lernabschnitt) {
		String jahrgangKurseOhneKlassenverband = "JU";
		final Long jgIdLa = jahrgangIdMap.get(lernabschnitt.idJahrgang);

		if (jgIdLa != null) {
			final String jgKuerzelLa = Jahrgaenge.data().getSchluesselByIDOrNull(jgIdLa);

			if (jgKuerzelLa != null) {
				jahrgangKurseOhneKlassenverband = jgKuerzelLa;
			}
		}
		// Jahrgänge "01" und "02" müssen in bestimmten Fällen in die Bezeichnung für die Schuleingangsphase umgesetzt werden
		if (Set.of("01", "02").contains(jahrgangKurseOhneKlassenverband)
				&& !(Schulform.BK.equals(schulform) || Schulform.SB.equals(schulform) || Schulform.WB.equals(schulform))) {

			final Long epJahre =
					((schueler.lernabschnitte != null) && (!schueler.lernabschnitte.isEmpty()) && (schueler.lernabschnitte.getFirst().idEpJahre != null))
							? schueler.lernabschnitte.getFirst().idEpJahre
							: null;

			if (epJahre != null) {
				final String epSchluessel = PrimarstufeSchuleingangsphaseBesuchsjahre.data().getSchluesselByIDOrNull(epJahre);

				if (epSchluessel != null) {
					jahrgangKurseOhneKlassenverband = epSchluessel;
				}
			}
		}

		return jahrgangKurseOhneKlassenverband;
	}

	/**
	 * Ermittelt die Nummer der zulässigen Kursart über den zugehörigen Katalog und die spezielle Kursart des Schülers. <br>
	 * <b>Fachliche Exception:</b> Für die Kursart 'PUK' (Unterricht im Klassenverband) wird ein Leerstring zurück gegeben.
	 *
	 * @param leistungsdaten   - die Leistungsdaten des Schülers
	 * @return die Nummer der Kursart
	 */
	private String ermittelnKursartNummer(final SchuelerLeistungsdatenStatistikGesamt leistungsdaten) {
		String kursartNummer = "";
		final var zulKursart = ZulaessigeKursart.data().getWertByKuerzel(leistungsdaten.kursart);

		if ((zulKursart != null) && (ZulaessigeKursart.PUK != zulKursart)) {
			final ZulaessigeKursartKatalogEintrag kursartKatalogEintrag = zulKursart.daten(aktuellesSchuljahr);
			if (kursartKatalogEintrag != null) {
				kursartNummer = kursartKatalogEintrag.nummer;
			}
		}

		return kursartNummer;
	}

	/**
	 *
	 */
	private void erstellenUvdStatistikExport() {

		final HashMap<String, List<SchuelerLeistungsdatenStatistikGesamt>> kurseImKlassenverband = new HashMap<>();
		final HashMap<Long, HashMap<String, List<SchuelerLeistungsdatenStatistikGesamt>>> kurseOhneKlassenverband = new HashMap<>();
		final HashMap<Long, HashMap<String, Long>> schuelerzahlenWeiblichProKursIdUndKey = new HashMap<>();
		final HashMap<Long, Integer> schuelerVonAndererSchuleProKursId = new HashMap<>();

		// Bauen der Maps für die Kurszuordnung und Schüleranzahlen
		for (final SchuelerStatistikGesamt schueler : statistikGesamt.schueler) {
			final SchuelerLernabschnittStatistikGesamt lernabschnitt =
					AggregationUtils.ermittelnLernabschnitt(schueler, statistikGesamt.schule.idSchuljahresabschnitt);
			final KlassenStatistikGesamt klasse = klasseIdMap.get(lernabschnitt.idKlasse);
			final String jahrgangKurseImKlassenverband = ermittelnJahrgangKurseImKlassenverband(klasse);
			final String jahrgangKurseOhneKlassenverband = ermittelnJahrgangKurseOhneKlassenverband(schueler, lernabschnitt);

			final String sgl = AggregationUtils.auffuellenStellengerecht(AggregationUtils.getSchulgliederungById(lernabschnitt.idSchulgliederung), 3);
			final String jahrgangSgl = jahrgangKurseOhneKlassenverband.concat(sgl);

			for (final SchuelerLeistungsdatenStatistikGesamt leistungsdaten : lernabschnitt.leistungsdaten) {
				//Sätze ohne LehrerID oder Wochenstunden können ignoriert werden
				if ((leistungsdaten.lehrerID != null) && (leistungsdaten.wochenstunden > 0)) {

					//Kurse im Klassenverband
					if (leistungsdaten.kursID == null) {
						bauenMapKurseImKlassenverband(kurseImKlassenverband, leistungsdaten, jahrgangKurseImKlassenverband, klasse);
					} else { // Kurse ohne Klassenverband
						bauenMapsKurseOhneKlassenverband(kurseOhneKlassenverband,
								schuelerzahlenWeiblichProKursIdUndKey, schuelerVonAndererSchuleProKursId, leistungsdaten, jahrgangSgl, schueler, klasse);
					}
				}
			}
		}

		// Schreiben der Exportsätze
		final List<UnterrichtsverteilungStatistikExport> uvdExportList = new LinkedList<>();
		int uenr = 1;
		uenr = schreibenKurseImKlassenverband(kurseImKlassenverband, uvdExportList, uenr);
		schreibenKurseOhneKlassenverband(kurseOhneKlassenverband, schuelerzahlenWeiblichProKursIdUndKey, schuelerVonAndererSchuleProKursId, uvdExportList,
				uenr);
		statistikExport.unterrichtsverteilungStatistikExport.addAll(uvdExportList);
	}

	/**
	 * Füllt die Felder des UVD-Satzes, die abhängig davon ob es sich um eine A- oder B-Schulform handelt unterschiedlich gefüllt werden.
	 *
	 * @param entry       - Map-Entry zu einem Kurs
	 * @param firstItem   - Erster Eintrag der Schülerleistungsdaten
	 * @param uvdExport   - Liste der Export-Sätze
	 */
	private void fuellenFelderSchulformABImKlassenverband(final Entry<String, List<SchuelerLeistungsdatenStatistikGesamt>> entry,
			final SchuelerLeistungsdatenStatistikGesamt firstItem, final UnterrichtsverteilungStatistikExport uvdExport) {

		if (istBK(schulform)) { // B-Schulen
			KlassenStatistikExport exportKlasse = new KlassenStatistikExport();
			final String schulinterneBezeichnung = entry.getKey().substring(0, LAENGE_SCHULINTBEZ);
			try {
				exportKlasse = klassenExportMap.get(schulinterneBezeichnung).getFirst();
			} catch (final NullPointerException npe) {
				System.out.println("Schulinterne Bezeichnung nicht gefunden: " + schulinterneBezeichnung);
			}
			uvdExport.jahrgang = exportKlasse.jahrgang;
			uvdExport.bildungsgangkennzeichen = exportKlasse.bildungsgangkennzeichen;
			uvdExport.parallelitaet2 = exportKlasse.parallelitaet2;
			uvdExport.schulinterneBezeichnung = schulinterneBezeichnung;
			uvdExport.fach = entry.getKey().substring(LAENGE_SCHULINTBEZ, LAENGE_SCHULINTBEZ + LAENGE_FACH);
			uvdExport.kuerzel = entry.getKey().substring(LAENGE_SCHULINTBEZ + LAENGE_FACH, LAENGE_SCHULINTBEZ + LAENGE_FACH + LAENGE_LEHK);
		} else { // A-Schulen
			uvdExport.jahrgang = entry.getKey().substring(0, LAENGE_JAHRGANG);
			uvdExport.bildungsgangkennzeichen = entry.getKey().substring(2, 3);
			uvdExport.parallelitaet2 = entry.getKey().substring(3, 4);
			uvdExport.kuerzel = entry.getKey().substring(4, 4 + LAENGE_LEHK);
			uvdExport.fach = entry.getKey().substring(4 + LAENGE_LEHK, 4 + LAENGE_LEHK + LAENGE_FACH);
			uvdExport.bilingualSprache = ermittelnBilingualeSprache(firstItem.fachID);
		}
	}

	/**
	 * @param schuelerzahlenWeiblichProKursIdUndKey
	 * @param schuelerVonAndererSchuleProKursId
	 * @param kursMap
	 * @param stammEntry
	 * @param firstStammItem
	 * @param uvdExportStammsatz
	 * @param kursStatistikGesamt
	 * @return Anzahl der Schüler von einer anderen Schule, die in einer Schülerfolgezeile (3) zu berücksichtigen sind. Nur für B-Schulen relevant.
	 */
	private int fuellenFelderSchulformABOhneKlassenverband(final HashMap<Long, HashMap<String, Long>> schuelerzahlenWeiblichProKursIdUndKey,
			final HashMap<Long, Integer> schuelerVonAndererSchuleProKursId,
			final Entry<Long, HashMap<String, List<SchuelerLeistungsdatenStatistikGesamt>>> kursMap,
			final Entry<String, List<SchuelerLeistungsdatenStatistikGesamt>> stammEntry, final SchuelerLeistungsdatenStatistikGesamt firstStammItem,
			final UnterrichtsverteilungStatistikExport uvdExportStammsatz, final KursStatistikGesamt kursStatistikGesamt) {
		int zuBeruecksichtigendeFremdSchueler = 0;

		if (istBK(schulform)) { // B-Schulen
			fuellenSchluesselFelderSchulformB(stammEntry, uvdExportStammsatz);
			uvdExportStammsatz.artDerGruppe = ermittelnKursartNummer(firstStammItem);
			uvdExportStammsatz.wochenstunden = firstStammItem.wochenstunden;
			final LehrerStatistikGesamt lehrer = lehrerIdMap.get(firstStammItem.lehrerID);
			uvdExportStammsatz.kuerzel = (lehrer != null) ? lehrer.kuerzel : "";
			zuBeruecksichtigendeFremdSchueler =
					behandelnFremdeSchueler(schuelerVonAndererSchuleProKursId, kursMap, stammEntry, uvdExportStammsatz);
		} else { // A-Schulen
			fuellenSchluesselFelderSchulformA(stammEntry, uvdExportStammsatz);

			if (kursStatistikGesamt != null) {
				uvdExportStammsatz.wochenstunden = kursStatistikGesamt.wochenstundenLehrer;
				final LehrerStatistikGesamt lehrer = lehrerIdMap.get(kursStatistikGesamt.lehrer);
				uvdExportStammsatz.kuerzel = (lehrer != null) ? lehrer.kuerzel : "";
			} else {
				uvdExportStammsatz.wochenstunden = 0;
				uvdExportStammsatz.kuerzel = "";
			}
			uvdExportStammsatz.bilingualSprache = ermittelnBilingualeSprache(firstStammItem.fachID);
			uvdExportStammsatz.fremdschueler = schuelerVonAndererSchuleProKursId.get(kursMap.getKey()) != null;
			uvdExportStammsatz.schuelerInsgesamt = stammEntry.getValue().size();

			if (jahrgaengeSek2.contains(uvdExportStammsatz.jahrgang)) {
				uvdExportStammsatz.schuelerWeiblich = ermittelnAnzahlSchuelerWeiblich(schuelerzahlenWeiblichProKursIdUndKey, kursMap);
			}
		}

		return zuBeruecksichtigendeFremdSchueler;
	}

	/**
	 * @param schuelerVonAndererSchuleProKursId
	 * @param kursMap
	 * @param stammEntry
	 * @param uvdExportStammsatz
	 * @return Anzahl der Schüler von einer anderen Schule, die in einer Schülerfolgezeile (3) zu berücksichtigen sind. Nur für B-Schulen relevant.
	 */
	private static int behandelnFremdeSchueler(final HashMap<Long, Integer> schuelerVonAndererSchuleProKursId,
			final Entry<Long, HashMap<String, List<SchuelerLeistungsdatenStatistikGesamt>>> kursMap,
			final Entry<String, List<SchuelerLeistungsdatenStatistikGesamt>> stammEntry, final UnterrichtsverteilungStatistikExport uvdExportStammsatz) {
		int zuBeruecksichtigendeFremdSchueler = 0;
		// Schüler von anderer Schule dürfen nur im Stammsatz gezählt werden, wenn alle Schüler von einer anderen Schule stammen.
		if (schuelerVonAndererSchuleProKursId.get(kursMap.getKey()) != null) {
			final int schuelerVonAndererSchuleAnzahl = schuelerVonAndererSchuleProKursId.get(kursMap.getKey());

			if (schuelerVonAndererSchuleAnzahl == stammEntry.getValue().size()) {
				// Nur Schüler von anderer Schule im Hauptsatz
				uvdExportStammsatz.schuelerInsgesamt = stammEntry.getValue().size();
				uvdExportStammsatz.jahrgang = "FF";
				uvdExportStammsatz.bildungsgangkennzeichen = "";
				uvdExportStammsatz.parallelitaet2 = "";
				uvdExportStammsatz.teilklasse = "";
				uvdExportStammsatz.schulinterneBezeichnung = "ANDERE";
			} else {
				// Sind nicht alle Schüler von einer anderen Schule, werden diese gesondert in einer Schülerfolgezeile 3 berücksichtigt.
				uvdExportStammsatz.schuelerInsgesamt = stammEntry.getValue().size() - schuelerVonAndererSchuleAnzahl;
				zuBeruecksichtigendeFremdSchueler = schuelerVonAndererSchuleAnzahl;
			}
		}

		return zuBeruecksichtigendeFremdSchueler;
	}

	/**
	 * @param kurseOhneKlassenverband
	 * @param schuelerzahlenWeiblichProKursIdUndKey
	 * @param schuelerVonAndererSchuleProKursId
	 * @param leistungsdaten
	 * @param schueler
	 * @param kursId
	 * @param key
	 */
	private void hinzufuegenEintragMapsKurseOhneKlassenverband(
			final HashMap<Long, HashMap<String, List<SchuelerLeistungsdatenStatistikGesamt>>> kurseOhneKlassenverband,
			final HashMap<Long, HashMap<String, Long>> schuelerzahlenWeiblichProKursIdUndKey, final HashMap<Long, Integer> schuelerVonAndererSchuleProKursId,
			final SchuelerLeistungsdatenStatistikGesamt leistungsdaten, final SchuelerStatistikGesamt schueler, final Long kursId, final String key) {
		// KursID-Eintrag schon vorhanden
		if (kurseOhneKlassenverband.containsKey(kursId)) {
			// Zähler Schüler von anderer Schule hochzählen
			if (schueler.status == SchuelerStatus.data().getIDByWertAndSchuljahr(SchuelerStatus.EXTERN, aktuellesSchuljahr).intValue()) {
				schuelerVonAndererSchuleProKursId.put(kursId, (schuelerVonAndererSchuleProKursId.get(kursId) + 1));
			}
			initUndHinzufuegenEintraegeInnereMap(kursId, key, schueler, schuelerzahlenWeiblichProKursIdUndKey, leistungsdaten,
					kurseOhneKlassenverband);
		} else { // Initialisieren der Einträge zu einer neuen KursID
			initialisierenEintraegeNeueKursId(kursId, key, schueler, schuelerVonAndererSchuleProKursId, schuelerzahlenWeiblichProKursIdUndKey,
					leistungsdaten, kurseOhneKlassenverband);
		}
	}

	/**
	 * Hier wird der jeweils erste Eintrag zu einer neuen Kurs-ID in den Maps {@code schuelerVonAndererSchuleProKursId},
	 * {@code schuelerzahlenWeiblichProKursIdUndKey} und {@code kurseOhneKlassenverband} initialisiert bzw. gesetzt.
	 *
	 * @param kursId
	 * @param key
	 * @param schueler
	 * @param schuelerVonAndererSchuleProKursId
	 * @param schuelerzahlenWeiblichProKursIdUndKey
	 * @param leistungsdaten
	 * @param kurseOhneKlassenverband
	 */
	private void initialisierenEintraegeNeueKursId(final Long kursId, final String key, final SchuelerStatistikGesamt schueler,
			final HashMap<Long, Integer> schuelerVonAndererSchuleProKursId, final HashMap<Long, HashMap<String, Long>> schuelerzahlenWeiblichProKursIdUndKey,
			final SchuelerLeistungsdatenStatistikGesamt leistungsdaten,
			final HashMap<Long, HashMap<String, List<SchuelerLeistungsdatenStatistikGesamt>>> kurseOhneKlassenverband) {
		// Zähler Schüler von anderer Schule auf 1 setzen
		if (schueler.status == SchuelerStatus.data().getIDByWertAndSchuljahr(SchuelerStatus.EXTERN, aktuellesSchuljahr).intValue()) {
			schuelerVonAndererSchuleProKursId.put(kursId, 1);
		}
		// schuelerzahlenWeiblich müssen immer mit 0 initialisiert werden, da sie nicht bei jeder KursId vorhanden sind
		schuelerzahlenWeiblichProKursIdUndKey.put(kursId, new HashMap<>());
		schuelerzahlenWeiblichProKursIdUndKey.get(kursId).put(key, 0L);
		// Wenn weiblicher Schüler dann auf 1 setzen
		if (Geschlecht.W.id == schueler.geschlecht) {
			schuelerzahlenWeiblichProKursIdUndKey.get(kursId).put(key, 1L);
		}

		kurseOhneKlassenverband.put(kursId, new HashMap<>());
		kurseOhneKlassenverband.get(kursId).put(key, new LinkedList<>());
		kurseOhneKlassenverband.get(kursId).get(key).add(leistungsdaten);
	}

	/**
	 * Schreibt die Export-Sätze zu den Kursen im Klassenverband.
	 *
	 * @param kurseImKlassenverband   - Map der Kurse im Klassenverband pro KursID
	 * @param uvdExportList           - Liste der Export-Sätze
	 * @param uenr                    - Zähler der Unterrichtseinheitennummer
	 * @return die letzte Unterichtseinheitennummer
	 */
	private int schreibenKurseImKlassenverband(final HashMap<String, List<SchuelerLeistungsdatenStatistikGesamt>> kurseImKlassenverband,
			final List<UnterrichtsverteilungStatistikExport> uvdExportList, final int uenr) {
		int unterrichtsEinheitenNr = uenr;

		for (final Entry<String, List<SchuelerLeistungsdatenStatistikGesamt>> entry : kurseImKlassenverband.entrySet()) {
			if ((entry.getValue() != null) && (!entry.getValue().isEmpty())) {
				final SchuelerLeistungsdatenStatistikGesamt firstItem = entry.getValue().getFirst();

				final UnterrichtsverteilungStatistikExport uvdExport = new UnterrichtsverteilungStatistikExport();
				uvdExport.unterrichtseinheitennummer = String.format("%04d", unterrichtsEinheitenNr++);
				if (firstItem.zusatzkraftID != null) {
					uvdExport.folgezeilenmerkmal = "2";
					uvdExport.kopplungsnummer = "001";
				} else {
					uvdExport.folgezeilenmerkmal = "1";
					uvdExport.kopplungsnummer = "000";
				}

				fuellenFelderSchulformABImKlassenverband(entry, firstItem, uvdExport);
				uvdExport.wochenstunden = firstItem.wochenstunden;
				uvdExportList.add(uvdExport);

				if (firstItem.zusatzkraftID != null) {
					final UnterrichtsverteilungStatistikExport uvdexportZusatzkraft = new UnterrichtsverteilungStatistikExport();
					uvdexportZusatzkraft.unterrichtseinheitennummer = uvdExport.unterrichtseinheitennummer;
					uvdexportZusatzkraft.folgezeilenmerkmal = "6";
					uvdexportZusatzkraft.kopplungsnummer = "002";
					uvdexportZusatzkraft.wochenstunden = firstItem.zusatzkraftWochenstunden;

					final LehrerStatistikGesamt zusatzLehrer = lehrerIdMap.get(firstItem.zusatzkraftID);
					uvdexportZusatzkraft.kuerzel = (zusatzLehrer != null) ? zusatzLehrer.kuerzel : "";

					uvdExportList.add(uvdexportZusatzkraft);
				}
			}
		}
		return unterrichtsEinheitenNr;
	}

	/**
	 * Schreibt die Export-Sätze zu den Kursen ohne Klassenverband.
	 *
	 * @param kurseOhneKlassenverband                 - Map der Kurse ohne Klassenverband pro KursID
	 * @param schuelerzahlenWeiblichProKursIdUndKey   - Map mit der Anzahl der Schülerinnen pro KursID und Key
	 * @param schuelerVonAndererSchuleProKursId       - Map mit dem Marker Schüler von anderer Schule pro KursID
	 * @param uvdExportList                           - Liste der Export-Sätze
	 * @param uenr                                    - Zähler der Unterrichtseinheitennummer
	 */
	private void schreibenKurseOhneKlassenverband(final HashMap<Long, HashMap<String, List<SchuelerLeistungsdatenStatistikGesamt>>> kurseOhneKlassenverband,
			final HashMap<Long, HashMap<String, Long>> schuelerzahlenWeiblichProKursIdUndKey, final HashMap<Long, Integer> schuelerVonAndererSchuleProKursId,
			final List<UnterrichtsverteilungStatistikExport> uvdExportList, final int uenr) {
		int unterrichtsEinheitenNr = uenr;

		for (final Entry<Long, HashMap<String, List<SchuelerLeistungsdatenStatistikGesamt>>> kursMap : kurseOhneKlassenverband.entrySet()) {
			int kopplungsnr = 2; // Kopplungsnummern 0 und 1 werden fest vergeben; ab 2 werden diese hochgezählt
			final Entry<String, List<SchuelerLeistungsdatenStatistikGesamt>> stammEntry = ermittelnErsterEintragZuKurs(kursMap);
			// Gibt es zu dem Kurs keine Einträge wird er übersprungen.
			if (stammEntry == null) {
				continue;
			}
			final SchuelerLeistungsdatenStatistikGesamt firstStammItem = stammEntry.getValue().getFirst();
			// Schreiben des ersten Export-Satz zu einer Unterrichtseinheit (Kurs)
			final UnterrichtsverteilungStatistikExport uvdExportStammsatz = new UnterrichtsverteilungStatistikExport();
			uvdExportStammsatz.unterrichtseinheitennummer = String.format("%04d", unterrichtsEinheitenNr++);
			uvdExportStammsatz.folgezeilenmerkmal = "1"; // Initialwerte für den ersten Satz
			uvdExportStammsatz.kopplungsnummer = "000"; // Initialwerte für den ersten Satz
			final KursStatistikGesamt kursStatistikGesamt = kurseIdMap.get(kursMap.getKey());

			final int zuBeruecksichtigendeFremdSchueler =
					fuellenFelderSchulformABOhneKlassenverband(schuelerzahlenWeiblichProKursIdUndKey, schuelerVonAndererSchuleProKursId, kursMap, stammEntry,
							firstStammItem, uvdExportStammsatz, kursStatistikGesamt);
			final FachStatistikGesamt fach = fachIdMap.get(firstStammItem.fachID);
			uvdExportStammsatz.fach = (fach != null) ? fach.kuerzelStatistik : "";
			uvdExportList.add(uvdExportStammsatz);

			kopplungsnr = schreibenSchuelerFolgesaetze(schuelerzahlenWeiblichProKursIdUndKey, schuelerVonAndererSchuleProKursId, kursMap, kopplungsnr,
					firstStammItem, uvdExportStammsatz, uvdExportList);
			// Bei B-Schulen kann es Schüler von einer anderen Schule geben, für die eine eigene Schülerfolgezeile (3) geschrieben wird.
			if (zuBeruecksichtigendeFremdSchueler > 0) {
				kopplungsnr = schreibenFremdeSchuelerFolgesatz(kopplungsnr, uvdExportStammsatz, zuBeruecksichtigendeFremdSchueler, uvdExportList);
			}

			schreibenWeitereLehrer(kursStatistikGesamt, kopplungsnr, uvdExportStammsatz, uvdExportList);
		}
	}

	/**
	 * @param uvdExportStammsatz
	 * @param zuBeruecksichtigendeFremdSchueler
	 * @param kopplungsnummer
	 * @param uvdExportList
	 * @return die Kopplungsnummer
	 */
	private static int schreibenFremdeSchuelerFolgesatz(final int kopplungsnummer, final UnterrichtsverteilungStatistikExport uvdExportStammsatz,
			final int zuBeruecksichtigendeFremdSchueler, final List<UnterrichtsverteilungStatistikExport> uvdExportList) {
		int kopplungsnr = kopplungsnummer;
		final UnterrichtsverteilungStatistikExport uvdexportFremdeSchueler = new UnterrichtsverteilungStatistikExport();
		uvdexportFremdeSchueler.unterrichtseinheitennummer = uvdExportStammsatz.unterrichtseinheitennummer;
		uvdexportFremdeSchueler.folgezeilenmerkmal = "3"; // Schuelerfolgezeile
		uvdexportFremdeSchueler.kopplungsnummer = String.format("%03d", kopplungsnr++);
		uvdexportFremdeSchueler.schuelerInsgesamt = zuBeruecksichtigendeFremdSchueler;
		uvdexportFremdeSchueler.jahrgang = "FF";
		uvdexportFremdeSchueler.bildungsgangkennzeichen = "";
		uvdexportFremdeSchueler.parallelitaet2 = "";
		uvdexportFremdeSchueler.teilklasse = "";
		uvdexportFremdeSchueler.schulinterneBezeichnung = "ANDERE";
		uvdExportList.add(uvdexportFremdeSchueler);

		return kopplungsnr;
	}

	/**
	 * Schreiben der Schüler-Folgesätze.
	 *
	 * @param schuelerzahlenWeiblichProKursIdUndKey   - Map mit der Anzahl der Schülerinnen pro KursID und Key
	 * @param schuelerVonAndererSchuleProKursId
	 * @param kursMapEntry                            - Eintrag zu einem Kurs
	 * @param kopplungsnummer                         - die Kopplungsnummer verbindet den Stammsatz mit seinen Folgesätzen
	 * @param firstStammItem                          - Erster Satz aus den Schülerleistungsdaten zum Kurs
	 * @param uvdExportStammsatz                      - der erste Exportsatz eines Kurses; Folgezeilenmerkmal 1 oder 2
	 * @param uvdExportList                           - die Liste der Exportsätze
	 * @return die Kopplungsnummer
	 */
	private int schreibenSchuelerFolgesaetze(final HashMap<Long, HashMap<String, Long>> schuelerzahlenWeiblichProKursIdUndKey,
			final HashMap<Long, Integer> schuelerVonAndererSchuleProKursId,
			final Entry<Long, HashMap<String, List<SchuelerLeistungsdatenStatistikGesamt>>> kursMapEntry,
			final int kopplungsnummer, final SchuelerLeistungsdatenStatistikGesamt firstStammItem,
			final UnterrichtsverteilungStatistikExport uvdExportStammsatz,
			final List<UnterrichtsverteilungStatistikExport> uvdExportList) {
		int kopplungsnr = kopplungsnummer;
		int durchlaeufe = 0;

		for (final Entry<String, List<SchuelerLeistungsdatenStatistikGesamt>> kursEntry : kursMapEntry.getValue().entrySet()) {
			durchlaeufe++;
			// Der erste Durchlauf mit den Daten des Stammsatz wird übersprungen
			if (durchlaeufe > 1) {
				uvdExportStammsatz.folgezeilenmerkmal = "2"; // Stammsatz mit Schuelerfolgezeile
				uvdExportStammsatz.kopplungsnummer = "001"; // Folgezeilenmerkmal und Kopplungsnummer werden fest überschrieben.

				kopplungsnr = schreibenSchuelersatz(kursMapEntry.getKey(), kursEntry, kopplungsnr, schuelerzahlenWeiblichProKursIdUndKey, firstStammItem,
						uvdExportStammsatz, uvdExportList);
			}
		}

		return kopplungsnr;
	}

	/**
	 * Schreiben eines Schüler-Folgesatz.
	 *
	 * @param kursId                                  - ID des Kurs
	 * @param kursEntry                               - Kurs-Eintrag
	 * @param kopplungsnummer                         - die Kopplungsnummer verbindet den Stammsatz mit seinen Folgesätzen
	 * @param schuelerzahlenWeiblichProKursIdUndKey   - Map mit der Anzahl der Schülerinnen pro KursID und Key
	 * @param firstStammItem                          - Erster Satz aus den Schülerleistungsdaten zum Kurs
	 * @param uvdExportStammsatz                      - der erste Exportsatz eines Kurses; Folgezeilenmerkmal 1 oder 2
	 * @param uvdExportList                           - die Liste der Exportsätze
	 * @return die Kopplungsnummer
	 */
	private int schreibenSchuelersatz(final Long kursId, final Entry<String, List<SchuelerLeistungsdatenStatistikGesamt>> kursEntry, final int kopplungsnummer,
			final HashMap<Long, HashMap<String, Long>> schuelerzahlenWeiblichProKursIdUndKey, final SchuelerLeistungsdatenStatistikGesamt firstStammItem,
			final UnterrichtsverteilungStatistikExport uvdExportStammsatz, final List<UnterrichtsverteilungStatistikExport> uvdExportList) {
		int kopplungsnr = kopplungsnummer;
		final UnterrichtsverteilungStatistikExport uvdexportKursSchueler = new UnterrichtsverteilungStatistikExport();
		uvdexportKursSchueler.unterrichtseinheitennummer = uvdExportStammsatz.unterrichtseinheitennummer;
		uvdexportKursSchueler.folgezeilenmerkmal = "3"; // Schuelerfolgezeile
		uvdexportKursSchueler.kopplungsnummer = String.format("%03d", kopplungsnr++);
		uvdexportKursSchueler.schuelerInsgesamt = (kursEntry.getValue() != null) ? kursEntry.getValue().size() : 0;

		final HashMap<String, Long> keyMap = schuelerzahlenWeiblichProKursIdUndKey.get(kursId);
		if (keyMap != null) {
			final Long count = keyMap.get(kursEntry.getKey());
			uvdexportKursSchueler.schuelerWeiblich = (count != null) ? count.intValue() : 0;
		} else {
			uvdexportKursSchueler.schuelerWeiblich = 0;
		}

		if (istBK(schulform)) {
			fuellenSchluesselFelderSchulformB(kursEntry, uvdexportKursSchueler);
			uvdExportStammsatz.artDerGruppe = ermittelnKursartNummer(firstStammItem);
		} else {
			fuellenSchluesselFelderSchulformA(kursEntry, uvdExportStammsatz);
		}

		// Fach bei den Schülerfolgezeilen nur füllen wenn es sich um eine Fremdsprache mit Sprachenbeginn handelt.
		if ((uvdExportStammsatz.fach != null) && (uvdExportStammsatz.fach.toCharArray().length > 1)
				&& (!Set.of("S3", "S4").contains(uvdExportStammsatz.fach))
				&& StringUtils.isNumeric(uvdExportStammsatz.fach.substring(1))) {
			final FachStatistikGesamt firstFach = fachIdMap.get(firstStammItem.fachID);
			uvdexportKursSchueler.fach = (firstFach != null) ? firstFach.kuerzelStatistik : "";
		}

		uvdExportList.add(uvdexportKursSchueler);
		return kopplungsnr;
	}

	/**
	 * Schreiben der Sätze für weitere Lehrer, die im Rahmen von z.B. Teamteaching auch im Kurs unterrichten.
	 *
	 * @param kursStatistikGesamt   - Grundlegende Daten eines Kurses
	 * @param kopplungsnummer       - die Kopplungsnummer
	 * @param uvdExportStammsatz    - der erste Exportsatz eines Kurses; Folgezeilenmerkmal 1 oder 2
	 * @param uvdExportList         - die Liste der Exportsätze
	 */
	private void schreibenWeitereLehrer(final KursStatistikGesamt kursStatistikGesamt, final int kopplungsnummer,
			final UnterrichtsverteilungStatistikExport uvdExportStammsatz, final List<UnterrichtsverteilungStatistikExport> uvdExportList) {
		int kopplungsnr = kopplungsnummer;

		if ((kursStatistikGesamt != null) && (kursStatistikGesamt.weitereLehrer != null)) {

			for (final KursLehrer kursLehrer : kursStatistikGesamt.weitereLehrer) {
				// Sollte das Folgezeilenmerkmal noch auf 1 stehen weil es keine Schülerfolgezeilen gab, wird dieses hier auf 2 gesetzt
				// und die Kopplungsnummer auf 001.
				if (uvdExportStammsatz.folgezeilenmerkmal.equals("1")) {
					uvdExportStammsatz.folgezeilenmerkmal = "2";
					uvdExportStammsatz.kopplungsnummer = "001";
				}
				final UnterrichtsverteilungStatistikExport uvdexportZusatzkraft = new UnterrichtsverteilungStatistikExport();
				uvdexportZusatzkraft.unterrichtseinheitennummer = uvdExportStammsatz.unterrichtseinheitennummer;
				uvdexportZusatzkraft.folgezeilenmerkmal = "6"; // Teamteachingzeile
				uvdexportZusatzkraft.kopplungsnummer = String.format("%03d", kopplungsnr++);
				// weitererLehrer und kursLehrer sind unterschiedliche Datenmodelle zu demselben Lehrer
				final LehrerStatistikGesamt weitererLehrer = lehrerIdMap.get(kursLehrer.idLehrer);
				uvdexportZusatzkraft.kuerzel = (weitererLehrer != null) ? weitererLehrer.kuerzel : "";
				uvdexportZusatzkraft.wochenstunden = kursLehrer.wochenstundenLehrer;

				uvdExportList.add(uvdexportZusatzkraft);
			}
		}
	}

}
