package de.svws_nrw.asd.export.aggregation;

import static de.svws_nrw.asd.export.aggregation.AggregationStatistikExport.EIN_LEERZEICHEN;
import static de.svws_nrw.asd.export.aggregation.AggregationStatistikExport.auffuellenStellengerecht;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.statistik.FachStatistikGesamt;
import de.svws_nrw.asd.data.statistik.KlassenStatistikGesamt;
import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.OrteStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerLernabschnittStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.export.data.KlassenAltersstrukturStatistikExport;
import de.svws_nrw.asd.export.data.KlassenNationalitaetenStatistikExport;
import de.svws_nrw.asd.export.data.KlassenStatistikExport;
import de.svws_nrw.asd.export.data.KlassenWohnorteStatistikExport;
import de.svws_nrw.asd.export.data.KlassenZuwanderungsgeschichteStatistikExport;
import de.svws_nrw.asd.export.data.StatistikExport;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
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
	 * Zuordnung der ID eines Fachs zum zugehörigen {@link FachStatistikGesamt}-Objekt.
	 */
	private Map<Long, FachStatistikGesamt> fachIdMap = new HashMap<>();

	/**
	 * Eine Liste der Fehlermeldungen zu den aufgetretenen Fehlern.
	 */
	private final LinkedList<String> fehlermeldungen;

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
	private Map<Long, KlassenStatistikGesamt> klasseIdMap = new HashMap<>();

	/**
	 * Zuordnung der ID eines Lehrers zum zugehörigen {@link LehrerStatistikGesamt}-Objekt.
	 */
	private Map<Long, LehrerStatistikGesamt> lehrerIdMap = new HashMap<>();

	/**
	 * Zuordnung der ID eines Ortes zum zugehörigen {@link OrteStatistikGesamt}-Objekt.
	 */
	private Map<Long, OrteStatistikGesamt> orteIdMap = new HashMap<>();


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
	 * Das Schuljahr.
	 */
	private final int aktuellesSchuljahr;



	/**
	 * Konstruktor
	 * @param statistikGesamt
	 * @param fehlermeldungen
	 * @param statistikExport
	 * @param lehrerIdMap
	 * @param klasseIdMap
	 * @param fachIdMap
	 * @param jahrgangIdMap
	 * @param foerderschwerpunktIdMap
	 * @param orteIdMap
	 * @param aktuellesSchuljahr
	 */
	public AggregationKlassenStatistikExport(final StatistikGesamt statistikGesamt, final StatistikExport statistikExport,
			final LinkedList<String> fehlermeldungen, final Map<Long, Long> jahrgangIdMap, final Map<Long, Long> foerderschwerpunktIdMap,
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

	private void erstellenKlassenZuwanderungsgeschichte(final List<SchuelerStatistikGesamt> teilKlassenSchueler,
			final KlassenStatistikExport klassenStatistikExport) {

		final KlassenZuwanderungsgeschichteStatistikExport klassenZuwanderungsgeschichteStatistikExport = new KlassenZuwanderungsgeschichteStatistikExport();
		teilKlassenSchueler.stream().forEach(e -> {

			if (e.hatMigrationshintergrund) {
				klassenZuwanderungsgeschichteStatistikExport.zuwanderungsgeschichteInsgesamt++;

				if ((e.idGeburtsland != null) && !Long.valueOf(Nationalitaeten.getDEU().daten(aktuellesSchuljahr).id).equals(e.idGeburtsland)) {
					klassenZuwanderungsgeschichteStatistikExport.zuwanderungsgeschichteEigenerZuzug++;
				}
				if (((e.idGeburtslandMutter != null) && !Long.valueOf(Nationalitaeten.getDEU().daten(aktuellesSchuljahr).id).equals(e.idGeburtslandMutter))
						|| ((e.idGeburtslandVater != null)
								&& !Long.valueOf(Nationalitaeten.getDEU().daten(aktuellesSchuljahr).id).equals(e.idGeburtslandVater))) {
					klassenZuwanderungsgeschichteStatistikExport.zuwanderungsgeschichteElternteilZugezogen++;
				}
				if ((e.idVerkehrspracheFamilie != null)
						&& !Long.valueOf(Verkehrssprache.getDEU().daten(aktuellesSchuljahr).id).equals(e.idVerkehrspracheFamilie)) {
					klassenZuwanderungsgeschichteStatistikExport.zuwanderungsgeschichteNichtDeutscheVerkehrssprache++;
				}
			}



		});

		klassenStatistikExport.klassenZuwanderungsgeschichteStatistikExport = klassenZuwanderungsgeschichteStatistikExport;
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
				teilklassenkuerzel = auffuellenStellengerecht(teilklassenkuerzel, 6);
			} else {


				if (anzahl == 1) {
					// nur eine Klasse → keine Nummer
					teilklassenkuerzel = auffuellenStellengerecht(klassenkuerzel, 6);
				} else {
					// mehrere Teilklassen → nummerieren
					nummer = counterProKlasse.getOrDefault(klassenkuerzel, 0) + 1;
					counterProKlasse.put(klassenkuerzel, nummer);

					teilklassenkuerzel = auffuellenStellengerecht(klassenkuerzel, 6);
					teilklassenkuerzel = teilklassenkuerzel.substring(0, teilklassenkuerzel.length() - 2) + String.format("%02d", nummer);

				}
			}
			final Map.Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>> entryTeilklasse = new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue());
			entryTeilklasse.getKey().klassenKuerzel = teilklassenkuerzel;
			sortiertTeilklassen.add(entryTeilklasse);
		}

		return sortiertTeilklassen;
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

	private void erstellenKlassenAltersstrukturStatistikExport(final List<SchuelerStatistikGesamt> schuelerStatistikGesamt,
			final KlassenStatistikExport klassenStatistikExport) {
		final Map<AltersstrukturKey, List<SchuelerStatistikGesamt>> map = schuelerStatistikGesamt.stream()
				.collect(Collectors
						.groupingBy(s -> {
							final String iso3 = getNationalitaetIso3(ermittleStaatsangehoerigkeit(s.idStaatsangehoerigkeit, s.idStaatsangehoerigkeit2));
							try {
								return new AltersstrukturKey(iso3, String.valueOf(DateManager.from(s.geburtsdatum).getJahr()));
							} catch (final InvalidDateException e) {
								fehlermeldungen.add("Folgendes Geburtsdatum konnte nicht geparst werden: " + s.geburtsdatum);
								return new AltersstrukturKey(iso3, "");
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

	/**
	 * @param idStaatsangehoerigkeit
	 * @param idStaatsangehoerigkeit2
	 * @return die gültige ID zu den übergebenen Staatsangehörigkeiten des Schülers
	 *
	 * Wenn die 2.Staatsangehörigkeit Deutsch ist, ist der Schüler als Deutscher zu werten
	 *
	 */
	private Long ermittleStaatsangehoerigkeit(final Long idStaatsangehoerigkeit, final Long idStaatsangehoerigkeit2) {

		if ((idStaatsangehoerigkeit2 != null) && "DEU".equalsIgnoreCase(getNationalitaetIso3(idStaatsangehoerigkeit2))) {
			return idStaatsangehoerigkeit2;
		}

		return idStaatsangehoerigkeit;

	}

	/**
	 * @param idStaatsangehoerigkeit
	 * @return der ISO3-Wert zur übergebenen ID
	 */
	private String getNationalitaetIso3(final Long idStaatsangehoerigkeit) {
		// TODO: Nachfragen bei Methodik: Wie soll mit idStaatsangehörigkeit = null umgegangen werden?
		return Nationalitaeten.data().getWertByIDOrNull(idStaatsangehoerigkeit) == null ? ""
				: Nationalitaeten.data().getWertByID(idStaatsangehoerigkeit).daten(aktuellesSchuljahr).iso3;
	}

	private void erstellenKlassenNationalitaetenStatistikExport(final List<SchuelerStatistikGesamt> schuelerStatistikGesamt,
			final KlassenStatistikExport klassenStatistikExport) {
		final Map<String, List<SchuelerStatistikGesamt>> map =
				schuelerStatistikGesamt.stream().collect(Collectors.groupingBy(s -> {
					if (s.idStaatsangehoerigkeit == null) {
						fehlermeldungen.add("Der SchuelerStatistikGesamt-Satz mit folgender ID hat eine StaatsangehoerigkeitID von Null: " + s.id);
						return "";
					}
					final long gueltigeIdStaatsangehoerigkeit = ermittleStaatsangehoerigkeit(s.idStaatsangehoerigkeit, s.idStaatsangehoerigkeit2);

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
			final KlassenStatistikGesamt klasse = klasseIdMap.get(lernabschnitt.idKlasse);
			// TODO: Frage an Methodik, ob das so in Ordnung ist.
			if (klasse.idJahrgang == null) {
				// B-Schule
				klassenStatistikExport.jahrgang = "";
				// Jahrgangsübergreifende Klasse
				if (!(Schulform.BK.equals(schulform) || Schulform.SB.equals(schulform) || Schulform.WB.equals(schulform))) {
					klassenStatistikExport.jahrgang = "JU";
				}
			} else {
				klassenStatistikExport.jahrgang = Jahrgaenge.data().getSchluesselByIDOrNull(jahrgangIdMap.get(klasse.idJahrgang));
			}

			if (klassenStatistikExport.jahrgang == null) {
				klassenStatistikExport.jahrgang = "";
				fehlermeldungen
						.add("Über die Klasse mit folgender ID konnte kein Jahrgang ermittelt werden: " + klasse.id + " idJahrgang: " + klasse.idJahrgang);
			}

			// Jahrgänge "01" und "02" müssen in bestimmten Fällen in die Bezeichnung für die Schuleingangsphase umgesetzt werden
			if (Set.of("01", "02").contains(klassenStatistikExport.jahrgang)
					&& !(Schulform.BK.equals(schulform) || Schulform.SB.equals(schulform) || Schulform.WB.equals(schulform))) {

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
			// jahrgangTeilklasse nur füllen, wenn Teilklasse vorhanden ist
			if (!e.getKey().klassenKuerzel.substring(4).isBlank()) {
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
			//TODO hier mus noch ein Json-katalog ink. Cortype vorgesehen weredn, erstazweise wird die id verwendet
			klassenStatistikExport.fachklasse = lernabschnitt.idFachklasse == null ? null : String.valueOf(lernabschnitt.idFachklasse);
			klassenStatistikExport.foerderschwerpunkt1 = e.getKey().foerderschwerp;
			klassenStatistikExport.foerderschwerpunkt2 = e.getKey().foerderschwerp2;
			klassenStatistikExport.hatSchwerbehinderungsNachweis = e.getKey().schwerstbeh;
			klassenStatistikExport.jvaKlasse = e.getValue().getFirst().istJvaSchueler ? "1" : "0";
			klassenStatistikExport.klassenart = e.getKey().klassenart;
			klassenStatistikExport.kuerzelKlassenlehrer = e.getKey().labk;
			klassenStatistikExport.organisationsform = e.getKey().orgForm;
			klassenStatistikExport.reformpaedagogik = e.getKey().reformpdg;
			//TODO: Nachfragen bei Methodik: Wie soll mit idStaatsangehörigkeit = null umgegangen werden?
			klassenStatistikExport.schuelerAuslaendischWeiblich = (int) e.getValue().stream()
					.filter(w -> (Geschlecht.W.id == w.geschlecht)
							&& (Nationalitaeten.getDEU().daten(aktuellesSchuljahr).id != (w.idStaatsangehoerigkeit == null ? 0L
									: w.idStaatsangehoerigkeit.longValue()))
					).count();
			klassenStatistikExport.schuelerAuslaendischZusammen =
					(int) e.getValue().stream().filter(w -> Nationalitaeten.getDEU().daten(aktuellesSchuljahr).id != (w.idStaatsangehoerigkeit == null ? 0L
							: w.idStaatsangehoerigkeit.longValue())
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
			klassenStatistikExport.teilklasse = e.getKey().klassenKuerzel.substring(4);
			//TODO muss geklärt werden, ersatzweise wird false verwendet
			klassenStatistikExport.verkuerzungHalbjaehrlich = false;
			// **K82 - Herkunft der Schüler**
			// TODO KlassenHerkunftStatistikExport - muss noch vorbereitet werden
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

	private void erstellenKlassenWohnorteStatistikExport(final List<SchuelerStatistikGesamt> value,
			final KlassenStatistikExport klassenStatistikExport) {

		final Map<Long, Integer> schuelerAnzahlProWohnId = new HashMap<>();
		value.forEach(s -> {
			if (schuelerAnzahlProWohnId.containsKey(s.wohnortID)) {
				schuelerAnzahlProWohnId.put(s.wohnortID, schuelerAnzahlProWohnId.get(s.wohnortID) + 1);
			} else {
				schuelerAnzahlProWohnId.put(s.wohnortID, 1);
			}
		});

		schuelerAnzahlProWohnId.entrySet().forEach(e -> {
			final KlassenWohnorteStatistikExport klassenWohnorteStatistikExport = new KlassenWohnorteStatistikExport();
			klassenWohnorteStatistikExport.jahrgang = klassenStatistikExport.jahrgang;
			klassenWohnorteStatistikExport.bildungsgangkennzeichen = klassenStatistikExport.bildungsgangkennzeichen;
			klassenWohnorteStatistikExport.parallelitaet2 = klassenStatistikExport.parallelitaet2;
			klassenWohnorteStatistikExport.teilklasse = klassenStatistikExport.teilklasse;

			final OrteStatistikGesamt orteStatistikGesamt = orteIdMap.get(e.getKey());
			if ((orteStatistikGesamt != null) && (Laender.NW.id(aktuellesSchuljahr).equals(orteStatistikGesamt.idLand))) {
				klassenWohnorteStatistikExport.postleitzahl = orteStatistikGesamt.plz;
				final String ortsname = orteStatistikGesamt.ortsname;
				final String schluessel = ortsname.toUpperCase() + "_" + klassenWohnorteStatistikExport.postleitzahl;
				klassenWohnorteStatistikExport.gemeindeschluessel =
						(Orte.data().getWertBySchluessel(schluessel) == null ? "0" : Orte.data().getWertBySchluessel(schluessel).daten(aktuellesSchuljahr).ags);
			} else if (orteStatistikGesamt != null) {
				klassenWohnorteStatistikExport.postleitzahl = (Laender.data().getWertByIDOrNull(orteStatistikGesamt.idLand) == null) ? "0"
						: Laender.data().getWertByID(orteStatistikGesamt.idLand).daten(aktuellesSchuljahr).plz;

				klassenWohnorteStatistikExport.gemeindeschluessel = (Laender.data().getWertByIDOrNull(orteStatistikGesamt.idLand) == null) ? "0"
						: Laender.data().getWertByID(orteStatistikGesamt.idLand).daten(aktuellesSchuljahr).ags;
			} else {
				klassenWohnorteStatistikExport.postleitzahl = "0";
				klassenWohnorteStatistikExport.gemeindeschluessel = "0";
			}


			klassenWohnorteStatistikExport.schuelerInsgesamt = e.getValue();

			klassenStatistikExport.klassenWohnorteStatistikExport.add(klassenWohnorteStatistikExport);
		});

	}


}
