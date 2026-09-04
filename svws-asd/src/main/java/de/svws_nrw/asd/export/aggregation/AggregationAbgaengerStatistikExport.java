/**
 *
 */
package de.svws_nrw.asd.export.aggregation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.data.statistik.KlassenStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerLernabschnittStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.export.data.AbgaengerDetailStatistikExport;
import de.svws_nrw.asd.export.data.AbgaengerStatistikExport;
import de.svws_nrw.asd.export.data.StatistikExport;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.InvalidDateException;

/*
 * AggregationAbgaengerStatistikExport.java
 *
 * Copyright (c) 2026 Projekt SVWS-Server - Schulverwaltungsserver
 *
 * Landesbetrieb Information und Technik Nordrhein-Westfalen (IT.NRW)
 * Alle Rechte vorbehalten.
 *
 * Versionshistorie
 * @version 1.00 - 19.08.2026 - Daniel Knittel (knitt01) - erste Version
 */

/**
 * Die Klasse AggregationAbgaengerStatistikExport ist eine Klasse im Paket de.svws_nrw.asd.export.aggregation des Projekts SVWS-Server.
 *
 * @since 2026
 * @version 1.00 - 19.08.2026
 * @author Daniel Knittel (knitt01)
 *
 */
public class AggregationAbgaengerStatistikExport {

	/**
	 * Eine Liste der Fehlermeldungen zu den aufgetretenen Fehlern.
	 */
	private final List<String> fehlermeldungen;

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
	 * Die Schulform der Schule als Enum {@link Schulform}.
	 */
	private final Schulform schulform;

	/**
	 * Zuordnug der Jahrgang-IDs der Schule zu den idJahrgang des Katalogs.
	 */
	private final Map<Long, Long> jahrgangIdMap;

	/**
	 * Zuordnung der ID einer Klasse zum zugehörigen {@link KlassenStatistikGesamt}-Objekt.
	 */
	private final Map<Long, KlassenStatistikGesamt> klasseIdMap;

	/**
	 * Zuordnug der Foerderschwerpunkt-IDs der Schule zu den idFoerderschwerpunkt des Katalogs.
	 */
	private final Map<Long, Long> foerderschwerpunktIdMap;


	/**
	 * Konstruktor
	 * @param statistikGesamt
	 * @param statistikExport
	 * @param aktuellesSchuljahr
	 * @param fehlermeldungen
	 * @param jahrgangIdMap
	 * @param klasseIdMap
	 * @param foerderschwerpunktIdMap
	 */
	public AggregationAbgaengerStatistikExport(final StatistikGesamt statistikGesamt, final StatistikExport statistikExport, final int aktuellesSchuljahr,
			final List<String> fehlermeldungen, final Map<Long, Long> jahrgangIdMap, final Map<Long, KlassenStatistikGesamt> klasseIdMap,
			final Map<Long, Long> foerderschwerpunktIdMap) {
		this.statistikGesamt = statistikGesamt;
		this.statistikExport = statistikExport;
		this.aktuellesSchuljahr = aktuellesSchuljahr;
		this.fehlermeldungen = fehlermeldungen;
		schulform = Schulform.data().getWertByBezeichner(statistikGesamt.schule.schulform);
		this.jahrgangIdMap = jahrgangIdMap;
		this.klasseIdMap = klasseIdMap;
		this.foerderschwerpunktIdMap = foerderschwerpunktIdMap;

	}

	/**
	 * Stellt den kombinierten Schlüsselwert aus den Schlüsselfeldern des Abgänger-Satzes zusammen.
	 *
	 * @param abgaengerExport
	 * @return der kombinierte Schlüsselwert des Satzes
	 */
	private static String bauenSchluessel(final AbgaengerStatistikExport abgaengerExport) {
		final StringBuilder schluesselBuilder = new StringBuilder();
		schluesselBuilder.append(abgaengerExport.jahrgang);
		schluesselBuilder.append(abgaengerExport.bildungsgangkennzeichen);
		schluesselBuilder.append(abgaengerExport.schulgliederung);
		schluesselBuilder.append(abgaengerExport.fachklasse);
		schluesselBuilder.append(abgaengerExport.klassenart);
		schluesselBuilder.append(abgaengerExport.jahrgangBK);
		schluesselBuilder.append(abgaengerExport.foerderschwerpunkt1);
		schluesselBuilder.append(abgaengerExport.foerderschwerpunkt2);
		schluesselBuilder.append(abgaengerExport.hatSchwerbehinderungsNachweis);

		return schluesselBuilder.toString();
	}

	/**
	 * Es werden die Bedingungen für die Anzahl-Felder Insgesamt und Weiblich geprüft
	 * und diese gegebenenfalls um eins erhöht.
	 *
	 * @param abgaengerDetail   der AbgaengerDetail-Satz mit den zu erhöhenden Feldern
	 * @param schueler          der Schüler-Satz zum prüfen der Bedingungen
	 */
	private static void erhoehenSchuelerAnzahlen(final AbgaengerDetailStatistikExport abgaengerDetail, final SchuelerStatistikGesamt schueler) {
		abgaengerDetail.abschluesseInsgesamtZusammen++;
		final boolean istWeiblich = Geschlecht.W.id == schueler.geschlecht;

		if (istWeiblich) {
			abgaengerDetail.abschluesseInsgesamtWeiblich++;
		}
	}

	/**
	 * Führt die Aggregation der {@link StatistikGesamt}-Daten der Schule in das {@link StatistikExport}-Datenobjekt aus. <br>
	 * Fehlermeldungen zu gegebenenfalls aufgetretenen Fehlern werden in die Liste {@link #fehlermeldungen} geschrieben. <br>
	 * <br>
	 * Hier werden die Export-Daten für die Abgänger (V51) und die abhängigen Abgänger-Details (V54) erstellt.
	 *
	 * @return - Ausführung erfolgreich und ohne schwere Fehler
	 */
	public boolean run() {

		if (statistikGesamt == null) {
			return false;
		}

		// Für die Schulform 02 (Grundschulen) wird die Satzart V51-Abgänger nicht angelegt.
		// Damit entfällt natürlich auch die Satzart V54-Abgänger-Detail.
		if (Schulform.G != schulform) {
			// V51 - Vorjahresschüler/-innen und Abgänge
			erstellenAbgaengerStatistikExport();
		}

		return true;
	}

	/**
	 * Es werden die Bedingungen für die Anzahl-Felder Insgesamt, Weiblich, Ausländer und AusländerWeiblich geprüft
	 * und diese gegebenenfalls um eins erhöht.
	 *
	 * @param abgaenger   der Abgaenger-Satz mit den zu erhöhenden Feldern
	 * @param schueler    der Schüler-Satz zum prüfen der Bedingungen
	 */
	private void erhoehenSchuelerAnzahlen(final AbgaengerStatistikExport abgaenger, final SchuelerStatistikGesamt schueler) {
		abgaenger.abgaengeInsgesamtZusammen++;
		final boolean istWeiblich = Geschlecht.W.id == schueler.geschlecht;

		if (istWeiblich) {
			abgaenger.abgaengeInsgesamtWeiblich++;
		}

		if (AggregationUtils.istAuslaender(schueler, aktuellesSchuljahr)) {
			abgaenger.abgaengeAuslaenderZusammen++;

			if (istWeiblich) {
				abgaenger.abgaengeAuslaenderWeiblich++;
			}
		}
	}

	/**
	 * Erstellen der AbgaengerDetailsStatistikExport-Daten (V54)
	 *
	 * @param schuelerMap    - Zuordnung der Satzschlüssel zu den Schüler-Sätzen
	 * @param abgaengerMap   - Zuordnung der Satzschlüssel zu den erstellten Export-Daten
	 */
	private void erstellenAbgaengerDetailsStatistikExport(final Map<String, List<SchuelerStatistikGesamt>> schuelerMap,
			final SortedMap<String, AbgaengerStatistikExport> abgaengerMap) {

		for (final Entry<String, List<SchuelerStatistikGesamt>> entry : schuelerMap.entrySet()) {
			final SortedMap<String, AbgaengerDetailStatistikExport> abgaengerDetailsMap = new TreeMap<>();

			for (final SchuelerStatistikGesamt schueler : entry.getValue()) {
				final AbgaengerDetailStatistikExport abgaengerDetail = new AbgaengerDetailStatistikExport();
				abgaengerDetail.abgangsart = schueler.idEntlassungAbschlussart;
				try {
					abgaengerDetail.geburtsjahr = String.valueOf(DateManager.from(schueler.geburtsdatum).getJahr());
				} catch (final InvalidDateException e) {
					abgaengerDetail.geburtsjahr = "";
					fehlermeldungen.add(
							"Geburtsjahr zu folgender Schüler-ID konnte nicht ermittelt werden: " + schueler.id + " Fehlermeldung: " + e.getLocalizedMessage());
				}

				abgaengerDetail.staatsangehoerigkeit = AggregationUtils.ermittleStaatsangehoerigkeitSchluessel(schueler.idStaatsangehoerigkeit,
						schueler.idStaatsangehoerigkeit2, aktuellesSchuljahr);

				final StringBuilder schluesselBuilder = new StringBuilder();
				schluesselBuilder.append(abgaengerDetail.abgangsart);
				schluesselBuilder.append(abgaengerDetail.geburtsjahr);
				schluesselBuilder.append(abgaengerDetail.staatsangehoerigkeit);
				final String schluessel = schluesselBuilder.toString();

				// Summenbildung
				if (abgaengerDetailsMap.containsKey(schluessel)) {
					final AbgaengerDetailStatistikExport gefundenerSatz = abgaengerDetailsMap.get(schluessel);
					erhoehenSchuelerAnzahlen(gefundenerSatz, schueler);
					abgaengerDetailsMap.put(schluessel, gefundenerSatz);
				} else {
					erhoehenSchuelerAnzahlen(abgaengerDetail, schueler);
					abgaengerDetailsMap.put(schluessel, abgaengerDetail);
				}

			}
			abgaengerMap.get(entry.getKey()).abgaengerDetailStatistikExport.addAll(abgaengerDetailsMap.values());
		}

	}

	/**
	 * Erstellen der AbgaengerStatistikExport-Daten (V51)
	 */
	private void erstellenAbgaengerStatistikExport() {
		final SortedMap<String, AbgaengerStatistikExport> abgaengerMap = new TreeMap<>();
		final Map<String, List<SchuelerStatistikGesamt>> schuelerMap = new HashMap<>();
		final List<Schuljahresabschnitt> vorjahresAbschnitte =
				statistikGesamt.schule.abschnitte.stream().filter(e -> (aktuellesSchuljahr - 1) == e.schuljahr).toList();

		if (vorjahresAbschnitte.isEmpty()) {
			fehlermeldungen.add("An dieser Schule existieren keine Schuljahresabschnitte zum Vorjahr");
			return;
		}

		for (final SchuelerStatistikGesamt schueler : statistikGesamt.schueler) {
			final SchuelerStatus schuelerStatus = SchuelerStatus.data().getWertByIDOrNull((long) schueler.status);
			//Aufsummierung: nur Schüler mit dem Status 8 oder 9 und ...
			if ((SchuelerStatus.ABSCHLUSS == schuelerStatus) || (SchuelerStatus.ABGANG == schuelerStatus)) {
				// wenn bei allgemeinbildenden Schulformen die Schulpflicht erfüllt ist und ...
				if (((!Schulform.WB.equals(schulform)) && (!AggregationUtils.istBK(schulform))) && (!schueler.istSchulpflichtErfuellt)) {
					continue;
				}
				final SchuelerLernabschnittStatistikGesamt vorjahresLernabschnitt =
						AggregationUtils.ermittelnLernabschnitt(schueler, vorjahresAbschnitte);
				// ...die einen Lernabschnitt aus dem Vorjahr aufweisen.
				if (vorjahresLernabschnitt.id > 0) {
					final String schluessel = schreibenAbgaengerExportSatz(abgaengerMap, schueler, vorjahresLernabschnitt);

					// Schueler-Map für die V54 aufbauen
					schuelerMap.computeIfAbsent(schluessel, s -> new LinkedList<>());
					schuelerMap.get(schluessel).add(schueler);
				}
			}
		}

		// V54 - Abgänger/innen nach Abgangsarten
		erstellenAbgaengerDetailsStatistikExport(schuelerMap, abgaengerMap);
		statistikExport.abgaengerStatistikExport.addAll(abgaengerMap.values());
	}

	/**
	 * Erstellt einen AbaengerExport-Satz, befüllt dessen Felder und schreibt ihn in die {@code abgaengerMap}. <br>
	 * Dabei wird aus den Schlüsselfeldern des Satzes ein kombinierter Schlüsselwert gebildet. <br>
	 * Abgänger mit demselben Schlüsselwert werden in der Map aufsummiert.
	 *
	 * @param abgaengerMap             - Zuordnung der Satzschlüssel zu den erstellten Export-Daten
	 * @param schueler                 - Der Schüler-Datensatz eines Abgängers
	 * @param vorjahresLernabschnitt   - Der letzte vorliegende Vorjahres-Lernabschnitt des Schülers
	 * @return der kombinierte Schlüsselwert des Satzes
	 */
	private String schreibenAbgaengerExportSatz(final SortedMap<String, AbgaengerStatistikExport> abgaengerMap, final SchuelerStatistikGesamt schueler,
			final SchuelerLernabschnittStatistikGesamt vorjahresLernabschnitt) {
		final AbgaengerStatistikExport abgaengerExport = new AbgaengerStatistikExport();

		// Schlüssel-Felder
		String jahrgang = "";
		final Long jgId = jahrgangIdMap.get(vorjahresLernabschnitt.idJahrgang);

		if (jgId != null) {
			final String jgKuerzel = Jahrgaenge.data().getSchluesselByIDOrNull(jgId);

			if (jgKuerzel != null) {
				jahrgang = jgKuerzel;
			}
		} else {
			System.out.println("Keinen Vorjahres-Jahrgang zur ID gefunden: " + vorjahresLernabschnitt.idJahrgang);
		}

		final KlassenStatistikGesamt klasse = klasseIdMap.get(vorjahresLernabschnitt.idKlasse);
		abgaengerExport.bildungsgangkennzeichen = AggregationStatistikExport.EIN_LEERZEICHEN;

		if (AggregationUtils.istBK(schulform)) {
			abgaengerExport.jahrgangBK = jahrgang;
		} else {
			abgaengerExport.jahrgang = jahrgang;

			if (klasse != null) {
				abgaengerExport.bildungsgangkennzeichen =
						((klasse.parallelitaet != null) && (!klasse.parallelitaet.isEmpty())) ? klasse.parallelitaet.substring(0, 1) : "";
			} else {
				System.out.println("Keine Vorjahres-Klasse zur ID gefunden: " + vorjahresLernabschnitt.idKlasse);
			}
		}

		abgaengerExport.schulgliederung = AggregationUtils.getSchulgliederungById(vorjahresLernabschnitt.idSchulgliederung);
		abgaengerExport.fachklasse = AggregationUtils.getFachklasseById(vorjahresLernabschnitt.idFachklasse);
		abgaengerExport.klassenart = AggregationUtils.getKlassenartById(vorjahresLernabschnitt.idKlassenart);
		abgaengerExport.foerderschwerpunkt1 =
				AggregationUtils.getFoerderschwerpunktById(vorjahresLernabschnitt.idFoerderschwerpunkt1, foerderschwerpunktIdMap);
		abgaengerExport.foerderschwerpunkt2 =
				AggregationUtils.getFoerderschwerpunktById(vorjahresLernabschnitt.idFoerderschwerpunkt2, foerderschwerpunktIdMap);
		abgaengerExport.hatSchwerbehinderungsNachweis = vorjahresLernabschnitt.hatSchwerbehinderungsNachweis;

		// weitere Felder
		abgaengerExport.datumStempelVorjahresSchueler = String.valueOf(aktuellesSchuljahr - 1);
		//TODO: Woher kommen diese Werte? - Noch in Klärung
		abgaengerExport.bestaetigungKeineAbgaenger = false;
		abgaengerExport.istVorgabedatensatz = false;

		final String schluessel = bauenSchluessel(abgaengerExport);

		// Summenbildung
		if (abgaengerMap.containsKey(schluessel)) {
			final AbgaengerStatistikExport gefundenerSatz = abgaengerMap.get(schluessel);
			erhoehenSchuelerAnzahlen(gefundenerSatz, schueler);
			abgaengerMap.put(schluessel, gefundenerSatz);
		} else {
			erhoehenSchuelerAnzahlen(abgaengerExport, schueler);
			abgaengerMap.put(schluessel, abgaengerExport);
		}

		return schluessel;
	}

}
