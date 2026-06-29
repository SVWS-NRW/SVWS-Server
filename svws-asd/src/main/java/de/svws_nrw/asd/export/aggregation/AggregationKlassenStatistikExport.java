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
import de.svws_nrw.asd.data.statistik.SchuelerLernabschnittStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.export.data.KlassenAltersstrukturStatistikExport;
import de.svws_nrw.asd.export.data.KlassenNationalitaetenStatistikExport;
import de.svws_nrw.asd.export.data.KlassenStatistikExport;
import de.svws_nrw.asd.export.data.KlassenZuwanderungsgeschichteStatistikExport;
import de.svws_nrw.asd.export.data.StatistikExport;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
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
	 * @param aktuellesSchuljahr
	 */
	public AggregationKlassenStatistikExport(final StatistikGesamt statistikGesamt, final StatistikExport statistikExport,
			final LinkedList<String> fehlermeldungen, final Map<Long, Long> jahrgangIdMap, final Map<Long, FachStatistikGesamt> fachIdMap,
			final Map<Long, KlassenStatistikGesamt> klasseIdMap, final Map<Long, LehrerStatistikGesamt> lehrerIdMap, final int aktuellesSchuljahr) {
		this.statistikGesamt = statistikGesamt;
		this.statistikExport = statistikExport;
		this.fehlermeldungen = fehlermeldungen;
		this.schulform = Schulform.data().getWertByBezeichner(statistikGesamt.schule.schulform);
		this.jahrgangIdMap = jahrgangIdMap;
		this.fachIdMap = fachIdMap;
		this.klasseIdMap = klasseIdMap;
		this.lehrerIdMap = lehrerIdMap;
		this.aktuellesSchuljahr = aktuellesSchuljahr;

	}

	private void erstellenKlassenZuwanderungsgeschichte(final List<SchuelerStatistikGesamt> teilKlassenSchueler,
			final KlassenStatistikExport klassenStatistikExport) {

		final KlassenZuwanderungsgeschichteStatistikExport klassenZuwanderungsgeschichteStatistikExport = new KlassenZuwanderungsgeschichteStatistikExport();
		teilKlassenSchueler.stream().forEach(e -> {

			if (e.hatMigrationshintergrund) {
				klassenZuwanderungsgeschichteStatistikExport.zuwanderungsgeschichteInsgesamt++;

				if (e.idGeburtsland != null && !Long.valueOf(Nationalitaeten.getDEU().daten(this.aktuellesSchuljahr).id).equals(e.idGeburtsland)) {
					klassenZuwanderungsgeschichteStatistikExport.zuwanderungsgeschichteEigenerZuzug++;
				}
				if (e.idGeburtslandMutter != null && !Long.valueOf(Nationalitaeten.getDEU().daten(this.aktuellesSchuljahr).id).equals(e.idGeburtslandMutter)
						|| e.idGeburtslandVater != null && !Long.valueOf(Nationalitaeten.getDEU().daten(this.aktuellesSchuljahr).id).equals(e.idGeburtslandVater)) {
					klassenZuwanderungsgeschichteStatistikExport.zuwanderungsgeschichteElternteilZugezogen++;
				}
				if (e.idVerkehrspracheFamilie != null && !Long.valueOf(Verkehrssprache.getDEU().daten(this.aktuellesSchuljahr).id).equals(e.idVerkehrspracheFamilie)) {
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

		if ((Schulform.H == this.schulform) || (Schulform.V == this.schulform) || (Schulform.S == this.schulform) || (Schulform.FW == this.schulform)) {
			return "";
		}

		return this.fachIdMap.get(fachId).bilingualeSprache.equals("D") ? ""
				: this.fachIdMap.get(fachId).bilingualeSprache;
	}

	/**
	 * Führt die Aggregation der {@link StatistikGesamt}-Daten der Klassen in das {@link StatistikExport}-Datenobjekt aus. <br>
	 * Fehlermeldungen zu gegebenenfalls aufgetretenen Fehlern werden in die Liste {@link #fehlermeldungen} geschrieben.
	 *
	 * @return - Ausführung erfolgreich und ohne schwere Fehler
	 */
	public boolean run() {

		if (this.statistikGesamt == null) {
			return false;
		}

		// Klassendaten
		erstellenKlassenStatistikExport();

		return true;
	}

	private String bauenBildungsbereich(final TeilKlassenKey klassenKey) {
		String bildungsbereich = "";
		if (Schulform.FW.equals(this.schulform) || Schulform.WF.equals(this.schulform) || Schulform.HI.equals(this.schulform)
				|| Schulform.SG.equals(this.schulform)) {
			if (!klassenKey.foerderschwerp.isBlank()) {
				bildungsbereich = "S";
			} else {
				bildungsbereich = "A";
			}

			if (Schulform.HI.equals(this.schulform)) {
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
				this.statistikGesamt.schueler.stream()
						.filter(e -> SchuelerStatus.data().getSchluesselByIDOrNull((long) e.status).equals("2")) // nur aktive Schüler; Status = 2
						.collect(Collectors.groupingBy(
								e -> new TeilKlassenKey(e, this.klasseIdMap, this.lehrerIdMap, this.statistikGesamt.schule.idSchuljahresabschnitt,
										this.jahrgangIdMap, this.schulform, this.fehlermeldungen)));

		//Sortieren
		Comparator<Map.Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>>> comparator;

		if (Schulform.BK.equals(this.schulform) || Schulform.SB.equals(this.schulform)) {
			comparator =
					Comparator.comparing((final Map.Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>> e) -> e.getKey().klassenKuerzel)
							.thenComparing(e -> e.getKey().gliederung)
							.thenComparing(e -> e.getKey().fachklasse)
							.thenComparing(e -> e.getKey().orgForm)
							.thenComparing(e -> e.getKey().aktJahrgang)
							.thenComparing(e -> e.getKey().foerderschwerp)
							.thenComparing(e -> e.getKey().foerderschwerp2)
							.thenComparing(e -> e.getKey().schwerstbeh)
							.thenComparing(e -> e.getKey().istJva)
							.thenComparing(e -> e.getKey().adressmerkmal);
		} else {
			comparator =
					Comparator.comparing((final Map.Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>> e) -> e.getKey().klassenKuerzel)
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

			if ((Schulform.BK == this.schulform) || (Schulform.SB == this.schulform)) {

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

	private void erstellenKlassenAltersstrukturStatistikExport(final List<SchuelerStatistikGesamt> schuelerStatistikGesamt,
			final KlassenStatistikExport klassenStatistikExport) {
		final Map<AltersstrukturKey, List<SchuelerStatistikGesamt>> map = schuelerStatistikGesamt.stream()
				.collect(Collectors
						.groupingBy(s -> {
							final String iso3 = getNationalitaetIso3(s.idStaatsangehoerigkeit);
							try {
								return new AltersstrukturKey(iso3, String.valueOf(DateManager.from(s.geburtsdatum).getJahr()));
							} catch (final InvalidDateException e) {
								this.fehlermeldungen.add("Folgendes Geburtsdatum konnte nicht geparst werden: " + s.geburtsdatum);
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
	 * @return der ISO3-Wert zur übergebenen ID
	 */
	private String getNationalitaetIso3(final Long idStaatsangehoerigkeit) {
		return Nationalitaeten.data().getWertByID(idStaatsangehoerigkeit).daten(this.aktuellesSchuljahr).iso3;
	}

	private void erstellenKlassenNationalitaetenStatistikExport(final List<SchuelerStatistikGesamt> schuelerStatistikGesamt,
			final KlassenStatistikExport klassenStatistikExport) {
		final Map<String, List<SchuelerStatistikGesamt>> map =
				schuelerStatistikGesamt.stream().collect(Collectors.groupingBy(s -> {
					if (s.idStaatsangehoerigkeit == null) {
						this.fehlermeldungen.add("Der SchuelerStatistikGesamt-Satz mit folgender ID hat eine StaatsangehoerigkeitID von Null: " + s.id);
						return "";
					}
					return Nationalitaeten.data().getWertByID(s.idStaatsangehoerigkeit).daten(this.aktuellesSchuljahr).schluessel;
				}));

		map.entrySet().stream().filter(f -> !f.getKey().equalsIgnoreCase(Nationalitaeten.getDEU().daten(this.aktuellesSchuljahr).schluessel)).forEach(t -> {
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
					e.getValue().getFirst().lernabschnitte.stream().filter(k -> k.idSchuljahresabschnitt == this.statistikGesamt.schule.idSchuljahresabschnitt)
							.findFirst();

			if (optional.isPresent()) {
				lernabschnitt = optional.get();
			}
			final KlassenStatistikGesamt klasse = this.klasseIdMap.get(lernabschnitt.idKlasse);
			// TODO: Frage an Methodik, ob das so in Ordnung ist.
			if (klasse.idJahrgang == null) {
				// B-Schule
				klassenStatistikExport.jahrgang = "";
				// Jahrgangsübergreifende Klasse
				if (!(Schulform.BK.equals(this.schulform) || Schulform.SB.equals(this.schulform) || Schulform.WB.equals(this.schulform))) {
					klassenStatistikExport.jahrgang = "JU";
				}
			} else {
				klassenStatistikExport.jahrgang = Jahrgaenge.data().getSchluesselByIDOrNull(this.jahrgangIdMap.get(klasse.idJahrgang));
			}

			if (klassenStatistikExport.jahrgang == null) {
				klassenStatistikExport.jahrgang = "";
				this.fehlermeldungen
						.add("Über die Klasse mit folgender ID konnte kein Jahrgang ermittelt werden: " + klasse.id + " idJahrgang: " + klasse.idJahrgang);
			}

			// Jahrgänge "01" und "02" müssen in bestimmten Fällen in die Bezeichnung für die Schuleingangsphase umgesetzt werden
			if (Set.of("01", "02").contains(klassenStatistikExport.jahrgang)
					&& !(Schulform.BK.equals(this.schulform) || Schulform.SB.equals(this.schulform) || Schulform.WB.equals(this.schulform))) {

				if (klassenStatistikExport.jahrgang.equals("01")) {
					klassenStatistikExport.jahrgang = "1E";
				} else {
					klassenStatistikExport.jahrgang = "2E";
				}
			}

			if (this.schulform.istAllgemeinbildend()) {
				final String parallelitaet = this.klasseIdMap.get(lernabschnitt.idKlasse).parallelitaet;
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
					&& !(Schulform.BK.equals(this.schulform) || Schulform.SB.equals(this.schulform) || Schulform.WB.equals(this.schulform))) {
				klassenStatistikExport.jahrgangTeilklasse = PrimarstufeSchuleingangsphaseBesuchsjahre.data()
						.getSchluesselByIDOrNull(e.getValue().getFirst().lernabschnitte.getFirst().epJahre.longValue());
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
			klassenStatistikExport.schuelerAuslaendischWeiblich = (int) e.getValue().stream()
					.filter(w -> (Geschlecht.W.id == w.geschlecht)
							&& (Nationalitaeten.getDEU().daten(this.aktuellesSchuljahr).id != w.idStaatsangehoerigkeit.longValue())
					).count();
			klassenStatistikExport.schuelerAuslaendischZusammen =
					(int) e.getValue().stream().filter(w -> Nationalitaeten.getDEU().daten(this.aktuellesSchuljahr).id != w.idStaatsangehoerigkeit.longValue()
					).count();
			klassenStatistikExport.schuelerInsgesamt = e.getValue().size();
			klassenStatistikExport.schuelerWeiblich = (int) e.getValue().stream().filter(w -> Geschlecht.W.id == w.geschlecht
			).count();
			klassenStatistikExport.schulgliederung = e.getKey().gliederung;

			if ((this.schulform == Schulform.BK) || (this.schulform == Schulform.SB)) {
				klassenStatistikExport.schulinterneBezeichnung = this.klasseIdMap.get(lernabschnitt.idKlasse).kuerzel;
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
			//TODO KlassenWohnorteStatistikExport - Gemeindeschlüssel muss noch geklärt werden
			// **X95 - Altersstruktur der Schüler**
			erstellenKlassenAltersstrukturStatistikExport(e.getValue(), klassenStatistikExport);


			// **X96 - Regionale Herkunft der Schüler (Ausbildungsort)
			//TODO KlassenAusbildungsorteStatistikExport
			// **X98 - Zuwanderungsgeschichte
			erstellenKlassenZuwanderungsgeschichte(e.getValue(), klassenStatistikExport);

			this.statistikExport.klassenStatistikExport.add(klassenStatistikExport);
		});




	}


}
