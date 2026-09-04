package de.svws_nrw.asd.export.aggregation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
	 * Zuordnung der ID eines Fachs zum zugehörigen {@link FachStatistikGesamt}-Objekt.
	 */
	private final Map<Long, FachStatistikGesamt> fachIdMap;

	/**
	 * Eine Liste der Fehlermeldungen zu den aufgetretenen Fehlern.
	 */
	private final LinkedList<String> fehlermeldungen;

	/**
	 * Zuordnug der Jahrgang-IDs der Schule zu den idJahrgang des Katalogs.
	 */
	private final Map<Long, Long> jahrgangIdMap;

	/**
	 * Zuordnung der ID einer Klasse zum zugehörigen {@link KlassenStatistikGesamt}-Objekt.
	 */
	private final Map<Long, KlassenStatistikGesamt> klasseIdMap;

	/**
	 * Zuordnung der ID eines Lehrers zum zugehörigen {@link LehrerStatistikGesamt}-Objekt.
	 */
	private final Map<Long, LehrerStatistikGesamt> lehrerIdMap;


	/**
	 * Zuordnung der ID eines Kurses zum zugehörigen {@link kursStatistikGesamt}-Objekt.
	 */
	private final Map<Long, KursStatistikGesamt> kurseIdMap;

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
	* Das aktuelle Schuljahr in vierstelliger Form.
	*/
	private final int aktuellesSchuljahr;

	private final Set<String> jahrgaengeSek2 = Set.of("11", "12", "13", "EF", "Q1", "Q2");




	/**
	 * Konstruktor
	 * @param statistikGesamt
	 * @param fehlermeldungen
	 * @param statistikExport
	 * @param lehrerIdMap
	 * @param klasseIdMap
	 * @param fachIdMap
	 * @param jahrgangIdMap
	 * @param kurseIdMap
	 * @param aktuellesSchuljahr
	 */
	public AggregationUvdStatistikExport(final StatistikGesamt statistikGesamt, final StatistikExport statistikExport,
			final LinkedList<String> fehlermeldungen, final Map<Long, Long> jahrgangIdMap, final Map<Long, FachStatistikGesamt> fachIdMap,
			final Map<Long, KlassenStatistikGesamt> klasseIdMap, final Map<Long, LehrerStatistikGesamt> lehrerIdMap,
			final Map<Long, KursStatistikGesamt> kurseIdMap, final int aktuellesSchuljahr) {
		this.statistikGesamt = statistikGesamt;
		this.statistikExport = statistikExport;
		this.fehlermeldungen = fehlermeldungen;
		schulform = Schulform.data().getWertByBezeichner(statistikGesamt.schule.schulform);
		this.jahrgangIdMap = jahrgangIdMap;
		this.fachIdMap = fachIdMap;
		this.klasseIdMap = klasseIdMap;
		this.lehrerIdMap = lehrerIdMap;
		this.kurseIdMap = kurseIdMap;
		this.aktuellesSchuljahr = aktuellesSchuljahr;
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
	 *
	 */
	private void erstellenUvdStatistikExport() {

		final HashMap<String, List<SchuelerLeistungsdatenStatistikGesamt>> kurseImKlassenverband = new HashMap<>();
		final HashMap<Long, HashMap<String, List<SchuelerLeistungsdatenStatistikGesamt>>> kurseOhneKlassenverband = new HashMap<>();
		final HashMap<Long, Long> schuelerzahlenProKursId = new HashMap<>();
		final HashMap<Long, Long> schuelerzahlenWeiblichProKursId = new HashMap<>();
		final HashMap<Long, HashMap<String, Long>> schuelerzahlenWeiblichProKursIdUndKey = new HashMap<>();
		final HashMap<Long, Boolean> schuelerVonAndererSchuleProKursId = new HashMap<>(); //TODO siehe unten

		//
		for (final SchuelerStatistikGesamt schueler : statistikGesamt.schueler) {
			final SchuelerLernabschnittStatistikGesamt lernabschnitt =
					AggregationUtils.ermittelnLernabschnitt(schueler, statistikGesamt.schule.idSchuljahresabschnitt);
			final KlassenStatistikGesamt klasse = klasseIdMap.get(lernabschnitt.idKlasse);

			// TODO: Welchen wert bekommen wir für JU (Jahrgangsübergreifende Klassen)? NULL oder leer?
			String jahrgangKurseImKlassenverband = "JU";
			if (klasse != null) {
				final Long jgId = jahrgangIdMap.get(klasse.idJahrgang);
				if (jgId != null) {
					final String jgKuerzel = Jahrgaenge.data().getSchluesselByIDOrNull(jgId);
					if (jgKuerzel != null) {
						jahrgangKurseImKlassenverband = jgKuerzel;
					}
				}
			}

			String jahrgangKurseOhneKlassenverband = "JU";
			final Long jgIdLa = jahrgangIdMap.get(lernabschnitt.idJahrgang);
			if (jgIdLa != null) {
				final String jgKuerzelLa = Jahrgaenge.data().getSchluesselByIDOrNull(jgIdLa);
				if (jgKuerzelLa != null) {
					jahrgangKurseOhneKlassenverband = jgKuerzelLa;
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

			final String sgl = AggregationUtils.auffuellenStellengerecht(AggregationUtils.getSchulgliederungById(lernabschnitt.idSchulgliederung), 3);
			final String jahrgangSgl = jahrgangKurseOhneKlassenverband.concat(sgl);

			for (final SchuelerLeistungsdatenStatistikGesamt l : lernabschnitt.leistungsdaten) {
				//Sätze ohne LehrerID oder Wochenstunden können ignoriert werden
				if ((l.lehrerID != null) && (l.wochenstunden > 0)) {

					//Kurse im Klassenverband
					if (l.kursID == null) {

						bauenMapKurseImKlassenverband(kurseImKlassenverband, l, jahrgangKurseImKlassenverband, klasse);

					} else { // Kurse ohne Klassenverband

						// Nur Kurse in die Map aufnehmen, die nicht an einer anderen Schule unterrichtet werden.
						final KursStatistikGesamt kurs = kurseIdMap.get(l.kursID);
						if ((kurs != null) && (kurs.schulnummer == null)) {
							String kursartNummer = "";
							final var zulKursart = ZulaessigeKursart.data().getWertByKuerzel(l.kursart);
							if (zulKursart != null) {
								final ZulaessigeKursartKatalogEintrag kursartKatalogEintrag = zulKursart.daten(aktuellesSchuljahr);
								if (kursartKatalogEintrag != null) {
									kursartNummer = kursartKatalogEintrag.nummer;
								}
							}

							final String key = jahrgangSgl.concat(kursartNummer);

							// KursID-Eintrag schon vorhanden
							if (kurseOhneKlassenverband.containsKey(l.kursID)) {
								schuelerzahlenProKursId.put(l.kursID, schuelerzahlenProKursId.get(l.kursID) + 1);
								if (schueler.status == SchuelerStatus.data().getIDByWertAndSchuljahr(SchuelerStatus.EXTERN, aktuellesSchuljahr).intValue()) {
									schuelerVonAndererSchuleProKursId.put(l.kursID, true);
								}
								// Hochzählen weibliche Schüler pro KursID
								if (Geschlecht.W.id == schueler.geschlecht) {

									if (schuelerzahlenWeiblichProKursId.containsKey(l.kursID)) {
										schuelerzahlenWeiblichProKursId.put(l.kursID, schuelerzahlenWeiblichProKursId.get(l.kursID) + 1);
									} else {
										schuelerzahlenWeiblichProKursId.put(l.kursID, 1L);
									}
								}

								// Schlüssel der inneren Map schon vorhanden
								if (kurseOhneKlassenverband.get(l.kursID).containsKey(key)) {
									kurseOhneKlassenverband.get(l.kursID).get(key).add(l);

									// Hochzählen weibliche Schüler pro KursID und Key
									if (Geschlecht.W.id == schueler.geschlecht) {

										if (schuelerzahlenWeiblichProKursIdUndKey.get(l.kursID).containsKey(key)) {
											schuelerzahlenWeiblichProKursIdUndKey.get(l.kursID).put(key,
													schuelerzahlenWeiblichProKursIdUndKey.get(l.kursID).get(key) + 1);
										} else {
											schuelerzahlenWeiblichProKursIdUndKey.get(l.kursID).put(key, 1L);
										}
									}

								} else { // Initialisieren der Einträge zu einem neuen Schlüssel in der inneren Map
									kurseOhneKlassenverband.get(l.kursID).put(key, new LinkedList<>());
									kurseOhneKlassenverband.get(l.kursID).get(key).add(l);
									schuelerzahlenWeiblichProKursIdUndKey.get(l.kursID).put(key, 0L);

									if (Geschlecht.W.id == schueler.geschlecht) {
										schuelerzahlenWeiblichProKursIdUndKey.get(l.kursID).put(key, 1L);
									}
								}

							} else { // Initialisieren der Einträge zu einer neuen KursId
								schuelerzahlenProKursId.put(l.kursID, 1L);
								if (schueler.status == SchuelerStatus.data().getIDByWertAndSchuljahr(SchuelerStatus.EXTERN, aktuellesSchuljahr).intValue()) {
									schuelerVonAndererSchuleProKursId.put(l.kursID, true);
								}

								// schuelerzahlenWeiblich müssen immer mit 0 initialisiert werden, da sie nicht bei jeder KursId vorhanden sind
								schuelerzahlenWeiblichProKursId.put(l.kursID, 0L);
								schuelerzahlenWeiblichProKursIdUndKey.put(l.kursID, new HashMap<>());
								schuelerzahlenWeiblichProKursIdUndKey.get(l.kursID).put(key, 0L);

								// Wenn weiblicher Schüler dann auf 1 setzen
								if (Geschlecht.W.id == schueler.geschlecht) {
									schuelerzahlenWeiblichProKursId.put(l.kursID, 1L);
									schuelerzahlenWeiblichProKursIdUndKey.get(l.kursID).put(key, 1L);
								}

								kurseOhneKlassenverband.put(l.kursID, new HashMap<>());
								kurseOhneKlassenverband.get(l.kursID).put(key, new LinkedList<>());
								kurseOhneKlassenverband.get(l.kursID).get(key).add(l);
							}
						}
					}
				}
			}
		}

		final List<UnterrichtsverteilungStatistikExport> uvdExportList = new LinkedList<>();
		int uenr = 1;

		// Kurse im Klassenverband zuerst.
		for (final Entry<String, List<SchuelerLeistungsdatenStatistikGesamt>> entry : kurseImKlassenverband.entrySet()) {
			if ((entry.getValue() != null) && (!entry.getValue().isEmpty())) {
				final SchuelerLeistungsdatenStatistikGesamt firstItem = entry.getValue().getFirst();

				final UnterrichtsverteilungStatistikExport uvdExport = new UnterrichtsverteilungStatistikExport();
				uvdExport.unterrichtseinheitennummer = String.format("%04d", uenr++);
				if (firstItem.zusatzkraftID != null) {
					uvdExport.folgezeilenmerkmal = "2";
					uvdExport.kopplungsnummer = "001";
				} else {
					uvdExport.folgezeilenmerkmal = "1";
					uvdExport.kopplungsnummer = "000";
				}

				uvdExport.jahrgang = entry.getKey().substring(0, 2);
				uvdExport.bildungsgangkennzeichen = entry.getKey().substring(2, 3);
				uvdExport.parallelitaet2 = entry.getKey().substring(3, 4);
				uvdExport.wochenstunden = firstItem.wochenstunden;
				uvdExport.fach = entry.getKey().substring(8);
				uvdExport.kuerzel = entry.getKey().substring(4, 8);
				uvdExport.bilingualSprache = ermittelnBilingualeSprache(firstItem.fachID);
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

		// Kurse ohne Klassenverband
		for (final Entry<Long, HashMap<String, List<SchuelerLeistungsdatenStatistikGesamt>>> kursMap : kurseOhneKlassenverband.entrySet()) {
			//TODO: uenr=169 sollte eine Schülerfolgezeile haben; Hauptsatz Q1 Schülerzahlen 11und6w, Schülerfolgezeile Q2 szahlen 1und1w
			int durchlaeufe = 0;
			int kopplungsnr = 2;

			if ((kursMap.getValue() == null) || (kursMap.getValue().isEmpty())) {
				continue;
			}

			final Entry<String, List<SchuelerLeistungsdatenStatistikGesamt>> stammEntry = kursMap.getValue().entrySet().iterator().next();

			if ((stammEntry.getValue() == null) || (stammEntry.getValue().isEmpty())) {
				continue;
			}

			final SchuelerLeistungsdatenStatistikGesamt firstStammItem = stammEntry.getValue().getFirst();
			final UnterrichtsverteilungStatistikExport uvdExport = new UnterrichtsverteilungStatistikExport();
			uvdExport.unterrichtseinheitennummer = String.format("%04d", uenr++);
			uvdExport.folgezeilenmerkmal = "1";
			uvdExport.kopplungsnummer = "000";
			uvdExport.jahrgang = stammEntry.getKey().substring(0, 2);
			uvdExport.schulgliederung = stammEntry.getKey().substring(2, 5).equals("***") ? "" : stammEntry.getKey().substring(2, 5);
			uvdExport.artDerGruppe = stammEntry.getKey().substring(5);

			final KursStatistikGesamt kursStatistikGesamt = kurseIdMap.get(kursMap.getKey());
			if (kursStatistikGesamt != null) {
				uvdExport.wochenstunden = kursStatistikGesamt.wochenstundenLehrer;
				final LehrerStatistikGesamt lehrer = lehrerIdMap.get(kursStatistikGesamt.lehrer);
				uvdExport.kuerzel = (lehrer != null) ? lehrer.kuerzel : "";
			} else {
				uvdExport.wochenstunden = 0;
				uvdExport.kuerzel = "";
			}

			final FachStatistikGesamt fach = fachIdMap.get(firstStammItem.fachID);
			uvdExport.fach = (fach != null) ? fach.kuerzelStatistik : "";
			uvdExport.bilingualSprache = ermittelnBilingualeSprache(firstStammItem.fachID);
			uvdExport.schuelerInsgesamt = stammEntry.getValue().size();
			uvdExport.fremdschueler = schuelerVonAndererSchuleProKursId.get(kursMap.getKey()) != null;

			if (jahrgaengeSek2.contains(uvdExport.jahrgang)) {
				final HashMap<String, Long> countMap = schuelerzahlenWeiblichProKursIdUndKey.get(kursMap.getKey());
				if ((countMap != null) && (!countMap.isEmpty())) {
					final Long count = countMap.values().iterator().next();
					uvdExport.schuelerWeiblich = (count != null) ? count.intValue() : 0;
				} else {
					uvdExport.schuelerWeiblich = 0;
				}
			}

			uvdExportList.add(uvdExport);

			for (final Entry<String, List<SchuelerLeistungsdatenStatistikGesamt>> entry : kursMap.getValue().entrySet()) {
				durchlaeufe++;

				if (durchlaeufe > 1) {
					uvdExport.folgezeilenmerkmal = "2";
					uvdExport.kopplungsnummer = "001";

					final UnterrichtsverteilungStatistikExport uvdexportKursSchueler = new UnterrichtsverteilungStatistikExport();
					uvdexportKursSchueler.unterrichtseinheitennummer = uvdExport.unterrichtseinheitennummer;
					uvdexportKursSchueler.folgezeilenmerkmal = "3";
					uvdexportKursSchueler.kopplungsnummer = String.format("%03d", kopplungsnr++);
					uvdexportKursSchueler.schuelerInsgesamt = (entry.getValue() != null) ? entry.getValue().size() : 0;

					final HashMap<String, Long> keyMap = schuelerzahlenWeiblichProKursIdUndKey.get(kursMap.getKey());
					if (keyMap != null) {
						final Long count = keyMap.get(entry.getKey());
						uvdexportKursSchueler.schuelerWeiblich = (count != null) ? count.intValue() : 0;
					} else {
						uvdexportKursSchueler.schuelerWeiblich = 0;
					}

					uvdexportKursSchueler.jahrgang = entry.getKey().substring(0, 2);
					uvdexportKursSchueler.schulgliederung = entry.getKey().substring(2, 5).equals("***") ? "" : entry.getKey().substring(2, 5);
					uvdexportKursSchueler.artDerGruppe = entry.getKey().substring(5);

					// Fach bei den Schülerfolgezeilen nur füllen wenn es sich um eine Fremdsprache mit Sprachenbeginn handelt.
					if ((uvdExport.fach != null) && (uvdExport.fach.toCharArray().length > 1) && (!Set.of("S3", "S4").contains(uvdExport.fach))
							&& StringUtils.isNumeric(uvdExport.fach.substring(1))) {
						final FachStatistikGesamt firstFach = fachIdMap.get(firstStammItem.fachID);
						uvdexportKursSchueler.fach = (firstFach != null) ? firstFach.kuerzelStatistik : "";
					}

					uvdExportList.add(uvdexportKursSchueler);
				}
			}


			if ((kursStatistikGesamt != null) && (kursStatistikGesamt.weitereLehrer != null)) {

				for (final KursLehrer kursLehrer : kursStatistikGesamt.weitereLehrer) {

					if (uvdExport.folgezeilenmerkmal.equals("1")) {
						uvdExport.folgezeilenmerkmal = "2";
						uvdExport.kopplungsnummer = "001";
					}
					final UnterrichtsverteilungStatistikExport uvdexportZusatzkraft = new UnterrichtsverteilungStatistikExport();
					uvdexportZusatzkraft.unterrichtseinheitennummer = uvdExport.unterrichtseinheitennummer;
					uvdexportZusatzkraft.folgezeilenmerkmal = "6";
					uvdexportZusatzkraft.kopplungsnummer = String.format("%03d", kopplungsnr++);

					final LehrerStatistikGesamt addLehrer = lehrerIdMap.get(kursLehrer.idLehrer);
					uvdexportZusatzkraft.kuerzel = (addLehrer != null) ? addLehrer.kuerzel : "";
					uvdexportZusatzkraft.wochenstunden = kursLehrer.wochenstundenLehrer;

					uvdExportList.add(uvdexportZusatzkraft);
				}
			}

		}

		statistikExport.unterrichtsverteilungStatistikExport.addAll(uvdExportList);
	}

	/**
	 * Fügt der übergebenen Map kurseImKlassenverband einen Eintrag hinzu. <br>
	 * Hierfür wird ein Schlüssel bestehend aus jahrgang, parallelitaet, lehrerkuerzel und fach gebildet. <br>
	 * Gibt es unter diesem Schlüssel schon einen Eintrag in der Map, so wird der Satz an die bestehende List angehängt. <br>
	 * Gibt es noch keinen Eintrag, so wird eine neue Liste unter diesem erstellt und der Satz eingefügt.
	 *
	 * @param kurseImKlassenverband - Die Map, in die die Sätze eingefügt werden
	 * @param schuelerLeistungsdaten - Ein Satz
	 * @param jahrgangKurseImKlassenverband
	 * @param klasse
	 */
	private void bauenMapKurseImKlassenverband(final HashMap<String, List<SchuelerLeistungsdatenStatistikGesamt>> kurseImKlassenverband,
			final SchuelerLeistungsdatenStatistikGesamt schuelerLeistungsdaten,
			final String jahrgangKurseImKlassenverband, final KlassenStatistikGesamt klasse) {

		final FachStatistikGesamt fachObj = fachIdMap.get(schuelerLeistungsdaten.fachID);
		final String fach = (fachObj != null) ? AggregationUtils.auffuellenStellengerecht(fachObj.kuerzelStatistik, 2) : "  ";

		final LehrerStatistikGesamt lehrerObj = lehrerIdMap.get(schuelerLeistungsdaten.lehrerID);
		final String lehrerkuerzel = (lehrerObj != null) ? AggregationUtils.auffuellenStellengerecht(lehrerObj.kuerzel, 4) : "    ";

		final String parallelitaetStr =
				((klasse != null) && (klasse.parallelitaet != null)) ? AggregationUtils.auffuellenStellengerecht(klasse.parallelitaet, 2) : "  ";

		final String key = jahrgangKurseImKlassenverband.concat(parallelitaetStr)
				.concat(lehrerkuerzel).concat(fach);

		if (!kurseImKlassenverband.containsKey(key)) {
			kurseImKlassenverband.put(key, new LinkedList<>());
		}
		kurseImKlassenverband.get(key).add(schuelerLeistungsdaten);
	}

}
