package de.svws_nrw.asd.export.aggregation;

import static de.svws_nrw.asd.export.aggregation.AggregationStatistikExport.EIN_LEERZEICHEN;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.data.statistik.FachStatistikGesamt;
import de.svws_nrw.asd.data.statistik.KlassenStatistikGesamt;
import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.OrteStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerLernabschnittStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.export.data.KlassenAltersstrukturStatistikExport;
import de.svws_nrw.asd.export.data.KlassenHerkunftStatistikExport;
import de.svws_nrw.asd.export.data.KlassenNationalitaetenStatistikExport;
import de.svws_nrw.asd.export.data.KlassenStatistikExport;
import de.svws_nrw.asd.export.data.KlassenWohnorteStatistikExport;
import de.svws_nrw.asd.export.data.KlassenZuwanderungsgeschichteStatistikExport;
import de.svws_nrw.asd.export.data.StatistikExport;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.types.schueler.Einschulungsart;
import de.svws_nrw.asd.types.schueler.HerkunftSonstige;
import de.svws_nrw.asd.types.schueler.Herkunftsarten;
import de.svws_nrw.asd.types.schueler.Hochschulabschluss;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schueler.Uebergangsempfehlung;
import de.svws_nrw.asd.types.schueler.Versetzungsvermerk;
import de.svws_nrw.asd.types.schule.Laender;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.types.schule.Orte;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Verkehrssprache;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.InvalidDateException;

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
public class AggregationKlassenStatistikExport {


	/**
	 * Das Schuljahr.
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

	/**
	 * Zuordnung der Foerderschwerpunkt-IDs der Schule zu den idFoerderschwerpunkt des Katalogs.
	 */
	private final Map<Long, Long> foerderschwerpunktIdMap;

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
	 * Zuordnung der ID eines Ortes zum zugehörigen {@link OrteStatistikGesamt}-Objekt.
	 */
	private final Map<Long, OrteStatistikGesamt> orteIdMap;

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
	 * @param statistikGesamt
	 * @param statistikExport
	 * @param fehlermeldungen
	 * @param jahrgangIdMap
	 * @param foerderschwerpunktIdMap
	 * @param fachIdMap
	 * @param klasseIdMap
	 * @param lehrerIdMap
	 * @param orteIdMap
	 * @param aktuellesSchuljahr
	 */
	@SuppressWarnings("all")
	public AggregationKlassenStatistikExport(final StatistikGesamt statistikGesamt, final StatistikExport statistikExport,
			final List<String> fehlermeldungen, final Map<Long, Long> jahrgangIdMap, final Map<Long, Long> foerderschwerpunktIdMap,
			final Map<Long, FachStatistikGesamt> fachIdMap,
			final Map<Long, KlassenStatistikGesamt> klasseIdMap, final Map<Long, LehrerStatistikGesamt> lehrerIdMap,
			final Map<Long, OrteStatistikGesamt> orteIdMap, final int aktuellesSchuljahr) {
		this.statistikGesamt = statistikGesamt;
		this.statistikExport = statistikExport;
		this.fehlermeldungen = fehlermeldungen;
		schulform = Schulform.data().getWertByBezeichner(statistikGesamt.schule.schulform);
		this.jahrgangIdMap = jahrgangIdMap;
		this.foerderschwerpunktIdMap = foerderschwerpunktIdMap;
		this.fachIdMap = fachIdMap;
		this.klasseIdMap = klasseIdMap;
		this.lehrerIdMap = lehrerIdMap;
		this.orteIdMap = orteIdMap;
		this.aktuellesSchuljahr = aktuellesSchuljahr;
	}

	/**
	 * @param schulform - die Schulform
	 * @return der Comparator für die übergebene Schulform
	 */
	private static Comparator<Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>>> baueComparatorFuerSchulform(final Schulform schulform) {

		if (Schulform.BK.equals(schulform) || Schulform.SB.equals(schulform)) {
			return Comparator.comparing((final Map.Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>> e) -> e.getKey().klassenKuerzel)
					.thenComparing(e -> e.getKey().gliederung)
					.thenComparing(e -> e.getKey().fachklasse)
					.thenComparing(e -> e.getKey().orgForm)
					.thenComparing(e -> e.getKey().aktJahrgang)
					.thenComparing(e -> e.getKey().foerderschwerp)
					.thenComparing(e -> e.getKey().foerderschwerp2)
					.thenComparing(e -> e.getKey().schwerstbeh)
					.thenComparing(e -> e.getKey().istJva)
					.thenComparing(e -> e.getKey().adressmerkmal);
		}

		return Comparator.comparing((final Map.Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>> e) -> e.getKey().klassenKuerzel)
				.thenComparing(e -> e.getKey().gliederung)
				.thenComparing(e -> e.getKey().klassenart)
				.thenComparing(e -> e.getKey().orgForm)
				.thenComparing(e -> e.getKey().aktJahrgang)
				.thenComparing(e -> e.getKey().foerderschwerp)
				.thenComparing(e -> e.getKey().schwerstbeh)
				.thenComparing(e -> e.getKey().labk)
				.thenComparing(e -> e.getKey().reformpdg)
				.thenComparing(e -> e.getKey().foerderschwerp2)
				.thenComparing(e -> e.getKey().adressmerkmal);
	}


	/**
	 * @param fachId
	 * @return BilingualeSprache
	 */
	public String ermittelnBilingualeSprache(final long fachId) {

		if ((Schulform.H == schulform) || (Schulform.V == schulform) || (Schulform.S == schulform) || (Schulform.FW == schulform)) {
			return "";
		}

		return fachIdMap.get(fachId).bilingualeSprache.equals("D") ? ""
				: fachIdMap.get(fachId).bilingualeSprache;
	}

	/**
	 * Führt die Aggregation der {@link StatistikGesamt}-Daten der Klassen in das {@link StatistikExport}-Datenobjekt aus. <br>
	 * Fehlermeldungen zu gegebenenfalls aufgetretenen Fehlern werden in die Liste {@link #fehlermeldungen} geschrieben.
	 *
	 * @return - Ausführung erfolgreich und ohne schwere Fehler
	 */
	public boolean run() {

		if (statistikGesamt == null) {
			return false;
		}

		// Klassendaten
		erstellenKlassenStatistikExport();

		return true;
	}

	private String bauenBildungsbereich(final TeilKlassenKey klassenKey) {
		String bildungsbereich = "";
		if (Schulform.FW.equals(schulform) || Schulform.WF.equals(schulform) || Schulform.HI.equals(schulform)
				|| Schulform.SG.equals(schulform)) {
			if (!klassenKey.foerderschwerp.isBlank()) {
				bildungsbereich = "S";
			} else {
				bildungsbereich = "A";
			}

			if (Schulform.HI.equals(schulform)) {
				if ("K02".equals(klassenKey.gliederung)) {
					bildungsbereich = "K";
				}
				if (!"K02".equals(klassenKey.gliederung) && !klassenKey.gliederung.isBlank()) {
					bildungsbereich = "B";
				}
			}
		}

		return bildungsbereich;
	}


	/**
	 * Ermitteln und füllen der zur KLAK gehörenden Teilfelder <i>jahrgang</i>, <i>bildungsgangkennzeichen</i> und <i>parallelitaet2</i>
	 * für die allgemeinbildenden Schulen. <br>
	 *
	 * @param klassenStatistikExport
	 * @param lernabschnitt
	 */
	private void bauenKlakFuerASchulen(final KlassenStatistikExport klassenStatistikExport, final SchuelerLernabschnittStatistikGesamt lernabschnitt) {
		final KlassenStatistikGesamt klasse = klasseIdMap.get(lernabschnitt.idKlasse);

		if (klasse.idJahrgang == null) {
			klassenStatistikExport.jahrgang = "";
			// Jahrgangsübergreifende Klasse
			if (!(Schulform.WB.equals(schulform))) {
				klassenStatistikExport.jahrgang = "JU";
			}
		} else {
			klassenStatistikExport.jahrgang = Jahrgaenge.data().getSchluesselByIDOrNull(jahrgangIdMap.get(klasse.idJahrgang));
		}

		if (klassenStatistikExport.jahrgang == null) {
			klassenStatistikExport.jahrgang = "";
			fehlermeldungen
					.add(this.getClass().getSimpleName() + ": Über die Klasse mit folgender ID konnte kein Jahrgang ermittelt werden: " + klasse.id
							+ " idJahrgang: " + klasse.idJahrgang);
		}

		// Jahrgänge "01" und "02" müssen in bestimmten Fällen in die Bezeichnung für die Schuleingangsphase umgesetzt werden
		if (Set.of("01", "02").contains(klassenStatistikExport.jahrgang)
				&& !(Schulform.WB.equals(schulform))) {

			if (klassenStatistikExport.jahrgang.equals("01")) {
				klassenStatistikExport.jahrgang = "1E";
			} else {
				klassenStatistikExport.jahrgang = "2E";
			}
		}

		if (schulform.istAllgemeinbildend()) {
			final String parallelitaet = klasseIdMap.get(lernabschnitt.idKlasse).parallelitaet;
			klassenStatistikExport.bildungsgangkennzeichen =
					parallelitaet == null ? EIN_LEERZEICHEN : parallelitaet.trim();
			klassenStatistikExport.parallelitaet2 = EIN_LEERZEICHEN;
		}
	}

	/**
	 * Ermitteln und füllen der zur KLAKX gehörenden Teilfelder <i>jahrgang</i>, <i>bildungsgangkennzeichen</i>, <i>parallelitaet2</i>
	 * und <i>teilklasse</i>. <br>
	 * Handelt es sich bei der Schulform um eine berufsbildende Schule ist das <i>klassenKuerzel</i> schon mit einem fortlaufenden
	 * Zähler belegt, der hier nur noch übernommen werden muss.
	 *
	 * @param klassenStatistikExport
	 * @param lernabschnitt
	 * @param entry
	 */
	private void bauenKlakx(final KlassenStatistikExport klassenStatistikExport, final SchuelerLernabschnittStatistikGesamt lernabschnitt,
			final Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>> entry) {
		final String klassenKuerzel = entry.getKey().klassenKuerzel;

		if (AggregationUtils.istBK(schulform)) {
			klassenStatistikExport.jahrgang = klassenKuerzel.substring(0, 2);
			klassenStatistikExport.bildungsgangkennzeichen = klassenKuerzel.substring(2, 3);
			klassenStatistikExport.parallelitaet2 = klassenKuerzel.substring(3, 4);
		} else {
			bauenKlakFuerASchulen(klassenStatistikExport, lernabschnitt);
		}

		klassenStatistikExport.teilklasse = klassenKuerzel.substring(4);
	}

	private List<Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>>> bauenTeilklassenSchueler() {

		//Gruppieren
		final Map<TeilKlassenKey, List<SchuelerStatistikGesamt>> gruppiert =
				statistikGesamt.schueler.stream()
						.filter(e -> SchuelerStatus.AKTIV == SchuelerStatus.data().getWertByIDOrNull((long) e.status)) // nur aktive Schüler
						.collect(Collectors.groupingBy(
								e -> new TeilKlassenKey(e, klasseIdMap, lehrerIdMap, statistikGesamt.schule.idSchuljahresabschnitt,
										jahrgangIdMap, schulform, foerderschwerpunktIdMap, fehlermeldungen)));

		//Sortieren
		final Comparator<Map.Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>>> comparator = baueComparatorFuerSchulform(schulform);
		final List<Map.Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>>> sortiert =
				gruppiert.entrySet().stream()
						.sorted(comparator)
						.toList();


		//Teilklassen erstellen
		final Map<String, Long> anzahlProKlasse =
				sortiert.stream()
						.collect(Collectors.groupingBy(
								e -> e.getKey().klassenKuerzel.toUpperCase(),
								Collectors.counting()
						));
		final Map<String, Integer> counterProKlasse = new HashMap<>();
		final List<Map.Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>>> sortiertTeilklassen = new ArrayList<>();
		String letzteKlasse = "";
		int nummer = 0;
		char teilklassenZaehler = 0;

		for (final Map.Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>> entry : sortiert) {

			final TeilKlassenKey key = entry.getKey();
			final String klassenkuerzel = key.klassenKuerzel.toUpperCase();

			final long anzahl = anzahlProKlasse.getOrDefault(klassenkuerzel, 0L);
			String teilklassenkuerzel = null;

			if ((Schulform.BK == schulform) || (Schulform.SB == schulform)) {

				if (!klassenkuerzel.equals(letzteKlasse)) {
					letzteKlasse = klassenkuerzel;
					teilklassenZaehler = 'A';
					nummer++;
				} else {
					teilklassenZaehler++;
				}

				teilklassenkuerzel = String.format("%04d%c", nummer, teilklassenZaehler);
				teilklassenkuerzel = AggregationUtils.auffuellenStellengerecht(teilklassenkuerzel, 6);
			} else {


				if (anzahl == 1) {
					// nur eine Klasse → keine Nummer
					teilklassenkuerzel = AggregationUtils.auffuellenStellengerecht(klassenkuerzel, 6);
				} else {
					// mehrere Teilklassen → nummerieren
					nummer = counterProKlasse.getOrDefault(klassenkuerzel, 0) + 1;
					counterProKlasse.put(klassenkuerzel, nummer);

					teilklassenkuerzel = AggregationUtils.auffuellenStellengerecht(klassenkuerzel, 6);
					teilklassenkuerzel = teilklassenkuerzel.substring(0, teilklassenkuerzel.length() - 2) + String.format("%02d", nummer);

				}
			}
			final Map.Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>> entryTeilklasse = new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue());
			entryTeilklasse.getKey().klassenKuerzel = teilklassenkuerzel;
			sortiertTeilklassen.add(entryTeilklasse);
		}

		return sortiertTeilklassen;
	}

	private void erstellenKlassenAltersstrukturStatistikExport(final List<SchuelerStatistikGesamt> schuelerStatistikGesamt,
			final KlassenStatistikExport klassenStatistikExport) {
		final Map<AltersstrukturKey, List<SchuelerStatistikGesamt>> map = schuelerStatistikGesamt.stream()
				.collect(Collectors
						.groupingBy(s -> {
							final String staatsangehoerigkeitSchluessel =
									AggregationUtils.ermittleStaatsangehoerigkeitSchluessel(s.idStaatsangehoerigkeit, s.idStaatsangehoerigkeit2,
											aktuellesSchuljahr);
							try {
								return new AltersstrukturKey(staatsangehoerigkeitSchluessel, String.valueOf(DateManager.from(s.geburtsdatum).getJahr()));
							} catch (@SuppressWarnings("unused") final InvalidDateException e) {
								fehlermeldungen
										.add(this.getClass().getSimpleName() + ": Folgendes Geburtsdatum konnte nicht geparst werden: " + s.geburtsdatum
												+ " Beim Schüler mit der ID: " + s.id);
								return new AltersstrukturKey(staatsangehoerigkeitSchluessel, "");
							}
						}));

		map.entrySet().stream().forEach(t -> {
			final KlassenAltersstrukturStatistikExport klassenAltersstrukturStatistikExport = new KlassenAltersstrukturStatistikExport();
			klassenAltersstrukturStatistikExport.nationalitaet = t.getKey().nationalitaet;
			if ("DEU".equalsIgnoreCase(klassenAltersstrukturStatistikExport.nationalitaet)) {
				klassenAltersstrukturStatistikExport.nationalitaet = "";
			}
			klassenAltersstrukturStatistikExport.geburtsjahr = t.getKey().geburtsjahr;
			klassenAltersstrukturStatistikExport.schuelerInsgesamt = t.getValue().size();
			klassenAltersstrukturStatistikExport.schuelerWeiblich = (int) t.getValue().stream().filter(f -> Geschlecht.W.id == f.geschlecht).count();
			klassenStatistikExport.klassenAltersstrukturStatistikExport.add(klassenAltersstrukturStatistikExport);
		});

	}

	private void erstellenKlassenNationalitaetenStatistikExport(final List<SchuelerStatistikGesamt> schuelerStatistikGesamt,
			final KlassenStatistikExport klassenStatistikExport) {
		final Map<String, List<SchuelerStatistikGesamt>> map =
				schuelerStatistikGesamt.stream().collect(Collectors.groupingBy(s -> {
					if (s.idStaatsangehoerigkeit == null) {
						fehlermeldungen.add(this.getClass().getSimpleName()
								+ ": Der SchuelerStatistikGesamt-Satz mit folgender ID hat eine StaatsangehoerigkeitID von Null: " + s.id);
						return "";
					}
					final long gueltigeIdStaatsangehoerigkeit =
							AggregationUtils.ermittleStaatsangehoerigkeit(s.idStaatsangehoerigkeit, s.idStaatsangehoerigkeit2, aktuellesSchuljahr);

					return Nationalitaeten.data().getWertByID(gueltigeIdStaatsangehoerigkeit).daten(aktuellesSchuljahr).schluessel;
				}));

		map.entrySet().stream().filter(f -> !f.getKey().equalsIgnoreCase(Nationalitaeten.getDEU().daten(aktuellesSchuljahr).schluessel)).forEach(t -> {
			final KlassenNationalitaetenStatistikExport klassenNationalitaetenStatistikExport = new KlassenNationalitaetenStatistikExport();
			klassenNationalitaetenStatistikExport.nationalitaet = t.getKey();
			klassenNationalitaetenStatistikExport.insgesamtZusammen = t.getValue().size();
			klassenNationalitaetenStatistikExport.insgesamtWeiblich = (int) t.getValue().stream().filter(f -> Geschlecht.W.id == f.geschlecht).count();
			klassenStatistikExport.klassenNationalitaetenStatistikExport.add(klassenNationalitaetenStatistikExport);
		});

	}

	private void erstellenKlassenStatistikExport() {

		final List<Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>>> teilklassenSchueler = bauenTeilklassenSchueler();


		teilklassenSchueler.forEach(e -> {
			final KlassenStatistikExport klassenStatistikExport = new KlassenStatistikExport();
			SchuelerLernabschnittStatistikGesamt lernabschnitt = new SchuelerLernabschnittStatistikGesamt();
			final Optional<SchuelerLernabschnittStatistikGesamt> optional =
					e.getValue().getFirst().lernabschnitte.stream().filter(k -> k.idSchuljahresabschnitt == statistikGesamt.schule.idSchuljahresabschnitt)
							.findFirst();

			if (optional.isPresent()) {
				lernabschnitt = optional.get();
			}
			bauenKlakx(klassenStatistikExport, lernabschnitt, e);
			// jahrgangTeilklasse nur füllen, wenn Teilklasse vorhanden ist
			if (!klassenStatistikExport.teilklasse.isBlank()) {
				klassenStatistikExport.jahrgangTeilklasse = e.getKey().aktJahrgang;
			}
			// Jahrgänge "01" und "02" müssen in bestimmten Fällen in die Bezeichnung für die Schuleingangsphase umgesetzt werden
			if (Set.of("01", "02").contains(e.getKey().aktJahrgang)
					&& !(Schulform.BK.equals(schulform) || Schulform.SB.equals(schulform) || Schulform.WB.equals(schulform))) {
				klassenStatistikExport.jahrgangTeilklasse = PrimarstufeSchuleingangsphaseBesuchsjahre.data()
						.getSchluesselByIDOrNull(e.getValue().getFirst().lernabschnitte.getFirst().idEpJahre);
			}
			klassenStatistikExport.adresskennzeichen = e.getKey().adressmerkmal;
			klassenStatistikExport.bildungsbereich = bauenBildungsbereich(e.getKey());
			klassenStatistikExport.fachklasse = AggregationUtils.getFachklasseById(lernabschnitt.idFachklasse);
			klassenStatistikExport.foerderschwerpunkt1 = e.getKey().foerderschwerp;
			klassenStatistikExport.foerderschwerpunkt2 = e.getKey().foerderschwerp2;
			klassenStatistikExport.hatSchwerbehinderungsNachweis = e.getKey().schwerstbeh;
			klassenStatistikExport.jvaKlasse = e.getValue().getFirst().istJvaSchueler ? "1" : "0";
			klassenStatistikExport.klassenart = e.getKey().klassenart;
			klassenStatistikExport.kuerzelKlassenlehrer = e.getKey().labk;
			klassenStatistikExport.organisationsform = e.getKey().orgForm;
			klassenStatistikExport.reformpaedagogik = e.getKey().reformpdg;
			klassenStatistikExport.schuelerAuslaendischWeiblich = (int) e.getValue().stream()
					.filter(w -> (Geschlecht.W.id == w.geschlecht)
							&& AggregationUtils.istAuslaender(w, aktuellesSchuljahr)
					).count();
			klassenStatistikExport.schuelerAuslaendischZusammen =
					(int) e.getValue().stream().filter(w -> AggregationUtils.istAuslaender(w, aktuellesSchuljahr)
					).count();
			klassenStatistikExport.schuelerInsgesamt = e.getValue().size();
			klassenStatistikExport.schuelerWeiblich = (int) e.getValue().stream().filter(w -> Geschlecht.W.id == w.geschlecht
			).count();
			klassenStatistikExport.schulgliederung = e.getKey().gliederung;

			if ((schulform == Schulform.BK) || (schulform == Schulform.SB)) {
				klassenStatistikExport.schulinterneBezeichnung = klasseIdMap.get(lernabschnitt.idKlasse).kuerzel;
			} else {
				klassenStatistikExport.schulinterneBezeichnung = "";
			}
			//TODO muss geklärt werden, ersatzweise wird false verwendet
			klassenStatistikExport.verkuerzungHalbjaehrlich = false;
			// **K82 - Herkunft der Schüler**
			erstellenKlassenHerkunftStatistikExport(e.getValue(), klassenStatistikExport);
			// **K83 - Ausländer**
			erstellenKlassenNationalitaetenStatistikExport(e.getValue(), klassenStatistikExport);
			// **K85 - Ausbildungsort**
			//TODO KlassenAusbildungsortsartStatistikExport - BK-Thema
			// **K87 - Betreuung**
			//TODO KlassenBetreuungStatistikExport - muss noch vorbereitet werden
			// **X94 - Regionale Herkunft der Schüler (Wohnort)
			erstellenKlassenWohnorteStatistikExport(e.getValue(), klassenStatistikExport);
			// **X95 - Altersstruktur der Schüler**
			erstellenKlassenAltersstrukturStatistikExport(e.getValue(), klassenStatistikExport);


			// **X96 - Regionale Herkunft der Schüler (Ausbildungsort)
			//TODO KlassenAusbildungsorteStatistikExport
			// **X98 - Zuwanderungsgeschichte
			erstellenKlassenZuwanderungsgeschichte(e.getValue(), klassenStatistikExport);

			statistikExport.klassenStatistikExport.add(klassenStatistikExport);
		});




	}

	private void erstellenKlassenHerkunftStatistikExport(final List<SchuelerStatistikGesamt> value, final KlassenStatistikExport klassenStatistikExport) {

		final Map<String, KlassenHerkunftStatistikExport> exportsBySchluessel = new LinkedHashMap<>();

		final List<Schuljahresabschnitt> vorjahresAbschnitte =
				statistikGesamt.schule.abschnitte.stream().filter(e -> (aktuellesSchuljahr - 1) == e.schuljahr).toList();

		if (vorjahresAbschnitte.isEmpty()) {
			fehlermeldungen.add("An dieser Schule existieren keine Schuljahresabschnitte zum Vorjahr");
			return;
		}
		final String[] sonstigeHerkunftList = { "AS", "HU", "UN", "WZ", "XB", "XS" };
		final String[] vorigeAllgHerkunftBezeichner_1 = { "HU", "WZ", "XB", "XS" };
		final String[] vorigeAllgHerkunftBezeichner_2 = { "HU", "UN", "WZ", "XB", "XS" };
		final boolean istWB_BK_SB = Schulform.WB.equals(schulform) || Schulform.BK.equals(schulform) || Schulform.SB.equals(schulform);

		value.forEach(schueler -> {
			String herkunftsart = "";
			String herkunftsschulform = "";
			String herkunftsSchulNr = "";
			String kuerzelGrundschuleUebergangsempfehlung = "";

			final SchuelerLernabschnittStatistikGesamt lernabschnitt = schueler.lernabschnitte.getLast();
			final SchuelerStatus schuelerStatus = SchuelerStatus.data().getWertByIDOrNull((long) schueler.status);
			if (schueler.idHerkunftSonstigeVorherigeSchule == null) {
				fehlermeldungen.add(this.getClass().getSimpleName()
						+ ": Der SchuelerStatistikGesamt-Satz mit folgender ID hat eine idHerkunftSonstigeVorherigeSchule von Null: " + schueler.id);
				return;
			}
			final String vorigeAllgHerkunft = HerkunftSonstige.data().getWertByIDOrNull(schueler.idHerkunftSonstigeVorherigeSchule).name();
			final SchuelerLernabschnittStatistikGesamt vorjahresLernabschnitt =
					AggregationUtils.ermittelnLernabschnitt(schueler, vorjahresAbschnitte);

			if (SchuelerStatus.AKTIV == schuelerStatus) {


				//es darf nur ein Lernabschnitt vorliegen, dabei muss es sich um den aktuellen Lernabschnitt handeln
				if ((schueler.lernabschnitte.size() == 1)
						&& (AggregationUtils.ermittelnLernabschnitt(schueler, statistikGesamt.schule.idSchuljahresabschnitt).id != 0)) {

					/*
					* ============================================================
					* STRANG 1 -EINSCHULUNG
					* ============================================================
					*/
					if (einschulung(lernabschnitt)
							&& ("ES".equals(vorigeAllgHerkunft) || "AS".equals(vorigeAllgHerkunft) || "XS".equals(vorigeAllgHerkunft))) {

						herkunftsSchulNr = String.valueOf(statistikGesamt.schule.schulNr);
						herkunftsschulform = "ES";

						final String[] einschulungsartBezeincher1 = { "E51", "E52", "E53", "E54" };
						final String[] einschulungsartBezeincher2 = { "E18", "E19" };

						if (Arrays.asList(einschulungsartBezeincher1)
								.contains(Einschulungsart.data().getWertByID(schueler.idGrundschuleEinschulungsart).name())) {
							herkunftsart = Einschulungsart.data().getSchluesselByIDOrNull(schueler.idGrundschuleEinschulungsart);
						} else if (Arrays.asList(einschulungsartBezeincher2)
								.contains(Einschulungsart.data().getWertByID(schueler.idGrundschuleEinschulungsart).name())) {
							herkunftsart = "51";
						} else {
							herkunftsart = "";
						}

						// bleibt leer
						kuerzelGrundschuleUebergangsempfehlung = "";
						/*
						 * ============================================================
						 * STRANG 2 - ZUZUG AUSLAND / SONSTIGE HERKUNFT
						 * ============================================================
						 *
						 */
					} else if (Arrays.asList(sonstigeHerkunftList).contains(vorigeAllgHerkunft)) {
						/*
						 * herkunftsSchulNr
						 */
						if (Schulform.WB.equals(schulform)) {
							// bleibt leer
							herkunftsSchulNr = "";
						} else if ("AS".equals(vorigeAllgHerkunft)) {
							herkunftsSchulNr = "999000";
						} else if (Arrays.asList(vorigeAllgHerkunftBezeichner_1).contains(vorigeAllgHerkunft)) {
							herkunftsSchulNr = "980500";
						} else if ("UN".equals(vorigeAllgHerkunft)) {
							herkunftsSchulNr = "999500";
						}

						/*
						 * herkunftsschulform
						 */
						if ("AS".equals(vorigeAllgHerkunft)) {

							final String staatsangehoerigkeit = Nationalitaeten.data().getSchluesselByIDOrNull(schueler.idStaatsangehoerigkeit);
							final String staatsangehoerigkeit2 = Nationalitaeten.data().getSchluesselByIDOrNull(schueler.idStaatsangehoerigkeit2);

							if (!"DEU".equals(staatsangehoerigkeit) && !"DEU".equals(staatsangehoerigkeit2)) {
								herkunftsschulform = "AS";
							} else {
								herkunftsschulform = "XS";
							}

						} else if (Arrays.asList(vorigeAllgHerkunftBezeichner_2).contains(vorigeAllgHerkunft)) {

							herkunftsschulform = HerkunftSonstige.data().getWertByBezeichnerOrNull(vorigeAllgHerkunft).name();

						}

						/*
						 * herkunftsart
						 */
						if ("AS".equals(vorigeAllgHerkunft)) {

							if (Schulform.WB.equals(schulform) || Schulform.BK.equals(schulform) || Schulform.SB.equals(schulform)) {
								herkunftsart = "X";
							} else {
								herkunftsart = "99";
							}

						} else if ("XS".equals(vorigeAllgHerkunft)) {

							if (!Schulform.WB.equals(schulform) && !Schulform.BK.equals(schulform) && !Schulform.SB.equals(schulform)) {
								herkunftsart = "11";
							}

						} else if ("UN".equals(vorigeAllgHerkunft)) {

							herkunftsart = "U";

						} else if (Arrays.asList(vorigeAllgHerkunftBezeichner_1).contains(vorigeAllgHerkunft)) {

							if (istWB_BK_SB) {
								final String herkunftsart_1_Stelle = schueler.idVorigeAbschlussart == null ? "" : schueler.idVorigeAbschlussart;
								final String herkunftsart_2_Stelle =
										schueler.berufsabschlussVorhandenVorherigeSchule || (Hochschulabschluss.OHNE_HOCHSCHULABSCHLUSS != Hochschulabschluss
												.data().getWertByIDOrNull(schueler.idHochschulabschluss)) ? "Y" : "";
								herkunftsart = herkunftsart_1_Stelle + herkunftsart_2_Stelle;
							}
						}

						// bleibt leer
						kuerzelGrundschuleUebergangsempfehlung = "";
					} else {
						/*
						 * ============================================================
						 * STRANG 3 - WECHSEL VON ANDERER SCHULE
						 * ============================================================
						 */
						/*
						 * herkunftsSchulNr
						 */
						if (Schulform.WB.equals(schulform)) {
							herkunftsSchulNr = "";
						} else {
							herkunftsSchulNr = schueler.vorherigeSchuleSchulnummerStatistik;
						}

						/*
						 * herkunftsschulform
						 */
						if ("SK".equals(vorigeAllgHerkunft)) {

							herkunftsschulform = "SE";

						} else if ("V".equals(vorigeAllgHerkunft)) {

							final boolean jahrgangGrundschule =
									Jahrgaenge.data().getWertByID(jahrgangIdMap.get(lernabschnitt.idJahrgang)).equals(Jahrgaenge.JAHRGANG_01)
											|| Jahrgaenge.data().getWertByID(jahrgangIdMap.get(lernabschnitt.idJahrgang)).equals(Jahrgaenge.JAHRGANG_02)
											|| Jahrgaenge.data().getWertByID(jahrgangIdMap.get(lernabschnitt.idJahrgang)).equals(Jahrgaenge.JAHRGANG_03)
											|| Jahrgaenge.data().getWertByID(jahrgangIdMap.get(lernabschnitt.idJahrgang)).equals(Jahrgaenge.JAHRGANG_04);

							if (jahrgangGrundschule) {
								herkunftsschulform = "G";
							} else {
								herkunftsschulform = "H";
							}

						} else {
							herkunftsschulform = vorigeAllgHerkunft;
						}

						/*
						 * herkunftsart
						 *
						 * schluessel aus Herkunftsarten.json
						 * anhand von vorigeArtLetzteVersetzung
						 */
						//TODO ID vorigeArtLetzteVersetzung ist momentan als String
						herkunftsart = Herkunftsarten.data()
								.getSchluesselByID(Long.valueOf(schueler.vorigeArtLetzteVersetzung == null ? "0" : schueler.vorigeArtLetzteVersetzung));

						/*
						 * kuerzelGrundschuleUebergangsempfehlung
						 */
						final String[] schulformen = { "H", "V", "R", "PS", "SK", "GE", "FW", "GY" };

						if (Arrays.asList(schulformen).contains(schulform.name())) {
							final String jahrgangBezeichner = Jahrgaenge.data().getWertByID(jahrgangIdMap.get(lernabschnitt.idJahrgang)).name();
							final boolean jahrgang05 = Jahrgaenge.JAHRGANG_05.name().equals(jahrgangBezeichner);
							final boolean jahrgang06 = Jahrgaenge.JAHRGANG_06.name().equals(jahrgangBezeichner);
							final boolean relevanteHerkunft =
									"G".equals(vorigeAllgHerkunft) || "V".equals(vorigeAllgHerkunft) || "PS".equals(vorigeAllgHerkunft);
							final String vorigeArtLetzteVersetzung = schueler.vorigeArtLetzteVersetzung;
							final boolean versetzt = "VERSETZT".equals(vorigeArtLetzteVersetzung);
							final boolean vorversetzt = "VORVERSETZT".equals(vorigeArtLetzteVersetzung);

							if (relevanteHerkunft && jahrgang05 && (versetzt || vorversetzt)) {

								kuerzelGrundschuleUebergangsempfehlung =
										Uebergangsempfehlung.data()
												.getSchluesselByIDOrNull(schueler.idKuerzelGrundschuleUebergangsempfehlung);

							} else if (relevanteHerkunft && jahrgang06 && vorversetzt) {

								kuerzelGrundschuleUebergangsempfehlung =
										Uebergangsempfehlung.data()
												.getSchluesselByIDOrNull(schueler.idKuerzelGrundschuleUebergangsempfehlung).equals("****") ? ""
														: Uebergangsempfehlung.data()
																.getSchluesselByIDOrNull(schueler.idKuerzelGrundschuleUebergangsempfehlung);

							}

						}
					}
				} else if (vorjahresLernabschnitt.id > 0) {
					/*
					 * ===============================================================================
					 * Der Schüler war am Ende des letzten Schuljahres bereits an der Schule.
					 * ===============================================================================
					 */

					final Klassenart vorjahresKlassenart = Klassenart.data().getWertByIDOrNull(vorjahresLernabschnitt.idKlassenart);
					final Klassenart aktuelleKlassenart = Klassenart.data().getWertByIDOrNull(lernabschnitt.idKlassenart);
					final Jahrgaenge vorjahresJahrgang = Jahrgaenge.data().getWertByIDOrNull(jahrgangIdMap.get(vorjahresLernabschnitt.idJahrgang));

					/*
					 * ============================================================
					 * herkunftsart / herkunftsschulform
					 * ============================================================
					 */

					if (!istWB_BK_SB && Jahrgaenge.HAUSFRUEHERZIEHUNG.equals(vorjahresJahrgang)) {
						// Förderschulkindergarten / Hausfrüherziehung
						herkunftsschulform = "FE";
						herkunftsart = "18";

					} else if (!istWB_BK_SB && Jahrgaenge.JAHRGANG_00.equals(vorjahresJahrgang)) {
						// Jahrgang 00
						herkunftsschulform = "SK";
						herkunftsart = "19";

					} else if (!istWB_BK_SB && Klassenart.DF.equals(vorjahresKlassenart)) {
						// Deutschförderung
						herkunftsschulform = Schulform.SK.equals(schulform) ? "SE" : schulform.name();
						if (Klassenart.DF.equals(aktuelleKlassenart)) {
							herkunftsart = "91";
						} else {
							herkunftsart = "92";
						}

					} else {

						// Normalfall
						herkunftsschulform = Schulform.SK.equals(schulform) ? "SE" : schulform.name();
						herkunftsart = ermittelnHerkunftsart(Versetzungsvermerk.data().getSchluesselByIDOrNull(vorjahresLernabschnitt.idVersetzungsvermerk));
					}

					/*
					 * ============================================================
					 * herkunftsSchulNr
					 * ============================================================
					 */
					if (Schulform.WB.equals(schulform)) {
						herkunftsSchulNr = "";
					} else {
						herkunftsSchulNr = String.valueOf(statistikGesamt.schule.schulNr);
					}

					/*
					 * ============================================================
					 * Übergangsempfehlung
					 * ============================================================
					 */
					kuerzelGrundschuleUebergangsempfehlung =
							Uebergangsempfehlung.data().getSchluesselByIDOrNull(schueler.idKuerzelGrundschuleUebergangsempfehlung);
				}

			}

			final String schluessel = herkunftsSchulNr + herkunftsschulform + herkunftsart + kuerzelGrundschuleUebergangsempfehlung;

			KlassenHerkunftStatistikExport export = exportsBySchluessel.get(schluessel);

			if (export == null) {
				export = new KlassenHerkunftStatistikExport();
				export.herkunftsart = herkunftsart;
				export.herkunftsschulform = herkunftsschulform;
				export.herkunftsSchulNr = herkunftsSchulNr;
				export.kuerzelGrundschuleUebergangsempfehlung =
						kuerzelGrundschuleUebergangsempfehlung;

				exportsBySchluessel.put(schluessel, export);
			}

			export.schuelerInsgesamt++;
			if (Geschlecht.W.id == schueler.geschlecht) {
				export.schuelerWeiblich++;
			}
			if (AggregationUtils.istAuslaender(schueler, aktuellesSchuljahr)) {
				export.schuelerAuslaendischZusammen++;
				if (Geschlecht.W.id == schueler.geschlecht) {
					export.schuelerAuslaendischWeiblich++;
				}
			}
		});

		klassenStatistikExport.klassenHerkunftStatistikExport.addAll(exportsBySchluessel.values());

	}

	private boolean einschulung(final SchuelerLernabschnittStatistikGesamt lernabschnitt) {

		final boolean ersteJahrgangsstufe =
				Jahrgaenge.data().getWertByID(jahrgangIdMap.get(lernabschnitt.idJahrgang)).equals(Jahrgaenge.JAHRGANG_01)
						&& (PrimarstufeSchuleingangsphaseBesuchsjahre.E1 == PrimarstufeSchuleingangsphaseBesuchsjahre.data()
								.getWertByIDOrNull(lernabschnitt.idEpJahre));

		final boolean hausfrueherziehung =
				Jahrgaenge.data().getWertByID(jahrgangIdMap.get(lernabschnitt.idJahrgang)).equals(Jahrgaenge.HAUSFRUEHERZIEHUNG);
		final boolean jahrgang00 = Jahrgaenge.data().getWertByID(jahrgangIdMap.get(lernabschnitt.idJahrgang)).equals(Jahrgaenge.JAHRGANG_00);
		final boolean einschlung = ersteJahrgangsstufe || hausfrueherziehung || jahrgang00;
		return einschlung;
	}



	private String ermittelnHerkunftsart(final String schluessel) {

		if (schluessel == null) {
			return "";
		}

		if (Schulform.WB.equals(schulform)
				|| Schulform.BK.equals(schulform)
				|| Schulform.SB.equals(schulform)) {

			return switch (schluessel) {
				case "A", "V", "VN", "VV" -> "V";
				case "FR", "N", "NP" -> "W";
				default -> "";
			};
		}

		return schluessel;
	}

	private void erstellenKlassenWohnorteStatistikExport(final List<SchuelerStatistikGesamt> value,
			final KlassenStatistikExport klassenStatistikExport) {


		/*
		* Schlüssel = PLZ + "_" + Gemeindeschlüssel
		* Wert = Anzahl Schüler
		*/
		final Map<String, Integer> schuelerAnzahlProWohnort = new HashMap<>();

		value.forEach(s -> {

			String postleitzahl = "0";
			String gemeindeschluessel = "0";

			/*
			 * Wohnort zur Wohnort-ID ermitteln
			 */
			final OrteStatistikGesamt ort = orteIdMap.get(s.wohnortID);

			if (ort != null) {

				/*
				 * Nordrhein-Westfalen
				 */
				if (Laender.NW.id(aktuellesSchuljahr).equals(ort.idLand)) {

					postleitzahl = ort.plz;
					final String ortsname = ort.ortsname;
					final String schluessel = ortsname.toUpperCase() + "_" + postleitzahl;

					final Orte ortData = Orte.data().getWertBySchluessel(schluessel);

					if (ortData != null) {
						gemeindeschluessel = ortData.daten(aktuellesSchuljahr).ags;
					}

					/*
					 * Ausland / anderes Bundesland
					 */
				} else {

					final Laender land = Laender.data().getWertByIDOrNull(ort.idLand);

					if (land != null) {

						postleitzahl = land.daten(aktuellesSchuljahr).plz;
						gemeindeschluessel = land.daten(aktuellesSchuljahr).ags;
					}
				}
			}

			/*
			 * Nach dem tatsächlichen Export-Schlüssel gruppieren.
			 *
			 */
			final String schluessel = postleitzahl + "_" + gemeindeschluessel;

			schuelerAnzahlProWohnort.merge(schluessel, 1, Integer::sum);
		});

		/*
		 * Exportobjekte erzeugen
		 */
		schuelerAnzahlProWohnort.forEach((schluessel, anzahl) -> {

			final String[] teile = schluessel.split("_");

			final KlassenWohnorteStatistikExport export = new KlassenWohnorteStatistikExport();

			export.postleitzahl = teile[0];
			export.gemeindeschluessel = teile[1];
			export.schuelerInsgesamt = anzahl;

			klassenStatistikExport.klassenWohnorteStatistikExport.add(export);
		});

	}

	private void erstellenKlassenZuwanderungsgeschichte(final List<SchuelerStatistikGesamt> teilKlassenSchueler,
			final KlassenStatistikExport klassenStatistikExport) {

		final KlassenZuwanderungsgeschichteStatistikExport klassenZuwanderungsgeschichteStatistikExport = new KlassenZuwanderungsgeschichteStatistikExport();
		teilKlassenSchueler.stream().forEach(e -> {

			if (e.hatMigrationshintergrund) {
				klassenZuwanderungsgeschichteStatistikExport.zuwanderungsgeschichteInsgesamt++;

				if (!Long.valueOf(Nationalitaeten.getDEU().daten(aktuellesSchuljahr).id).equals(e.idGeburtsland)) {
					klassenZuwanderungsgeschichteStatistikExport.zuwanderungsgeschichteEigenerZuzug++;
				}

				if ((!Long.valueOf(Nationalitaeten.getDEU().daten(aktuellesSchuljahr).id).equals(e.idGeburtslandMutter))
						|| (!Long.valueOf(Nationalitaeten.getDEU().daten(aktuellesSchuljahr).id).equals(e.idGeburtslandVater))) {
					klassenZuwanderungsgeschichteStatistikExport.zuwanderungsgeschichteElternteilZugezogen++;
				}

				if (!Long.valueOf(Verkehrssprache.getDEU().daten(aktuellesSchuljahr).id).equals(e.idVerkehrspracheFamilie)) {
					klassenZuwanderungsgeschichteStatistikExport.zuwanderungsgeschichteNichtDeutscheVerkehrssprache++;
				}
			}
		});

		klassenStatistikExport.klassenZuwanderungsgeschichteStatistikExport = klassenZuwanderungsgeschichteStatistikExport;
	}


}
