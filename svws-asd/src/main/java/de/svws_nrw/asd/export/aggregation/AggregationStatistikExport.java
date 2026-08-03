package de.svws_nrw.asd.export.aggregation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.data.statistik.FachStatistikGesamt;
import de.svws_nrw.asd.data.statistik.KlassenStatistikGesamt;
import de.svws_nrw.asd.data.statistik.KursStatistikGesamt;
import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.export.data.StatistikExport;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;

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
public class AggregationStatistikExport {


	/**
	 * Ein String mit drei Leerzeichen.
	 */
	public static final String DREI_LEERZEICHEN = "   ";
	/**
	 * Ein String mit einem Leerzeichen.
	 */
	public static final String EIN_LEERZEICHEN = " ";

	/**
	 * Ein String mit zwei Leerzeichen.
	 */
	public static final String ZWEI_LEERZEICHEN = "  ";

	/**
	* Das aktuelle Schuljahr in vierstelliger Form.
	*/
	private final int aktuellesSchuljahr;

	/**
	 * Zuordnung der ID eines Fachs zum zugehörigen {@link FachStatistikGesamt}-Objekt.
	 */
	private Map<Long, FachStatistikGesamt> fachIdMap = new HashMap<>();

	/**
	 * Eine Liste der Fehlermeldungen zu den aufgetretenen Fehlern.
	 */
	private final LinkedList<String> fehlermeldungen = new LinkedList<>();


	/**
	 * Zuordnung der Foerderschwerpunkt-IDs der Schule zu den idFoerderschwerpunkt des Katalogs.
	 */
	private final Map<Long, Long> foerderschwerpunktIdMap;

	/**
	 * Zuordnung der Jahrgang-IDs der Schule zu den idJahrgang des Katalogs.
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
	 * Zuordnung der Religion-IDs der Schule zu den idReligion des Katalogs.
	 */
	private final Map<Long, Long> religionIdMap;

	/**
	 * Zuordnung der ID eines Kurses zum zugehörigen {@link kursStatistikGesamt}-Objekt.
	 */
	private final Map<Long, KursStatistikGesamt> kurseIdMap;

	/**
	 * Die für den Export vorgesehenen Statistikdaten mit den Aggregaten.
	 */
	private StatistikExport statistikExport;

	/**
	 * Die gesamten Statistikdaten der Schule, welche von einer Schule bei der Erfassung der amtlichen Schulstatistik übertragen werden.
	 */
	private final StatistikGesamt statistikGesamt;



	/**
	 * Konstruktor
	 * @param statistikGesamt
	 */
	public AggregationStatistikExport(final StatistikGesamt statistikGesamt) {
		this.statistikGesamt = statistikGesamt;
		jahrgangIdMap = statistikGesamt.jahrgaenge.stream().collect(Collectors.toMap(e -> e.id, e -> e.idKatalog));
		foerderschwerpunktIdMap = statistikGesamt.foederschwerpunkte.stream().collect(Collectors.toMap(e -> e.id, e -> e.idKatalog));
		religionIdMap = statistikGesamt.religionen.stream().collect(Collectors.toMap(e -> e.id, e -> e.idKatalog));
		fachIdMap = statistikGesamt.faecher.stream().collect(Collectors.toMap(e -> e.id, e -> e));
		klasseIdMap = statistikGesamt.klassen.stream().collect(Collectors.toMap(e -> e.id, e -> e));
		lehrerIdMap = statistikGesamt.lehrer.stream().collect(Collectors.toMap(e -> e.id, e -> e));
		kurseIdMap = statistikGesamt.kurse.stream().collect(Collectors.toMap(e -> e.id, e -> e));
		final Optional<Schuljahresabschnitt> optional =
				statistikGesamt.schule.abschnitte.stream().filter(e -> e.id == statistikGesamt.schule.idSchuljahresabschnitt).findFirst();
		aktuellesSchuljahr = optional.isPresent() ? optional.get().schuljahr : 0;
	}

	/**
	 * Auffüllen eines Feldes auf eine bestimmte Anzahl an Stellen mit Leerzeichen.
	 *
	 * @param feld
	 * @param anzahlStellen
	 * @return mit Leerzeichen aufgefülltes Feld
	 */
	public static String auffuellenStellengerecht(final String feld, final int anzahlStellen) {
		String feldFormatiert = feld;

		while (feldFormatiert.toCharArray().length < anzahlStellen) {
			feldFormatiert = feldFormatiert.concat(EIN_LEERZEICHEN);
		}

		return feldFormatiert;
	}

	/**
	 * @param idFoerderschwerpunkt1
	 * @param idSchulgliederung
	 * @param fehlermeldungen
	 * @return der Bildungsbereich
	 */
	public static String bauenBildungsbereich(final Long idSchulgliederung, final Long idFoerderschwerpunkt1, final List<String> fehlermeldungen) {
		final Schulgliederung schulgliederung = Schulgliederung.data().getWertByIDOrNull(idSchulgliederung);

		if (Schulgliederung.DEFAULT.equals(schulgliederung) && (idFoerderschwerpunkt1 == null)) { // If unnötig! Kann weg?!
			return "A";
		}

		if (idFoerderschwerpunkt1 != null) {
			return "S";
		}

		if (Schulgliederung.K02.equals(schulgliederung)) {
			return "K";
		}

		if (Schulgliederung.H01.equals(schulgliederung) || Schulgliederung.H02.equals(schulgliederung)) {
			return "B";
		}

		fehlermeldungen.add(
				"Bildungsbereich konnnte nicht ermittelt werden - Schulgliederung: " + idSchulgliederung + " idFörderschwerpunkt1: " + idFoerderschwerpunkt1);
		return null;
	}

	/**
	 * Gibt zurück, ob es sich um eine Schulform handelt, bei der der Jahrgang zum Erstellen der SatzkennungReligion
	 * erforderlich ist.
	 *
	 *@param schulform - {@link Schulform}
	 * @return true/false
	 */
	public static boolean istJahrgangErforderlich(final Schulform schulform) {
		return switch (schulform) {
			case G, H, V, R, PS, SK, GE, GY -> true;
			default -> false;
		};
	}

	/**
	 * Gibt das Feld {@link #fehlermeldungen} zurück.
	 *
	 * @return das Feld {@link #fehlermeldungen}
	 */
	public List<String> getFehlermeldungen() {
		return fehlermeldungen;
	}

	/**
	 * Gibt das Feld {@link #statistikExport} zurück.
	 *
	 * @return das Feld {@link #statistikExport}
	 */
	public StatistikExport getStatistikExport() {
		return statistikExport;
	}


	/**
	 * Führt die Aggregation der {@link StatistikGesamt}-Daten in das {@link StatistikExport}-Datenobjekt aus. <br>
	 * Fehlermeldungen zu gegebenenfalls aufgetretenen Fehlern können über {@link #getFehlermeldungen()} hinterher abgerufen werden.
	 *
	 * @return - Ausführung erfolgreich und ohne Fehler
	 */
	public boolean run() {

		if (statistikGesamt == null) {
			return false;
		}

		statistikExport = new StatistikExport();
		boolean erfolg = true;

		// B01 und B02
		final AggregationSchuleStatistikExport aggregationSchuleStatistikExport =
				new AggregationSchuleStatistikExport(statistikGesamt, statistikExport, fehlermeldungen);
		erfolg &= aggregationSchuleStatistikExport.run();

		// S42
		final AggregationReligionStatistikExport aggregationReligionStatistikExport =
				new AggregationReligionStatistikExport(statistikGesamt, statistikExport, fehlermeldungen, jahrgangIdMap, foerderschwerpunktIdMap, religionIdMap,
						aktuellesSchuljahr);
		erfolg &= aggregationReligionStatistikExport.run();

		// L61-L68
		final AggregationLehrerStatistikExport aggregationLehrerStatistikExport =
				new AggregationLehrerStatistikExport(statistikGesamt, statistikExport, fehlermeldungen);
		erfolg &= aggregationLehrerStatistikExport.run();

		// Klassendaten
		final AggregationKlassenStatistikExport aggregationKlassenStatistikExport =
				new AggregationKlassenStatistikExport(statistikGesamt, statistikExport, fehlermeldungen, jahrgangIdMap, fachIdMap, klasseIdMap, lehrerIdMap,
						aktuellesSchuljahr);
		erfolg &= aggregationKlassenStatistikExport.run();

		// Unterrichtverteilungsdaten
		final AggregationUvdStatistikExport aggregationUvdStatistikExport =
				new AggregationUvdStatistikExport(statistikGesamt, statistikExport, fehlermeldungen, jahrgangIdMap, fachIdMap, klasseIdMap, lehrerIdMap,
						kurseIdMap, aktuellesSchuljahr);
		erfolg &= aggregationUvdStatistikExport.run();

		return erfolg;
	}


}
