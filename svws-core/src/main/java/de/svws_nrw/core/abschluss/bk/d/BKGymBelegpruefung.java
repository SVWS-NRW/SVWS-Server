package de.svws_nrw.core.abschluss.bk.d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafel;
import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafelFach;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturFachbelegung;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturFachbelegungHalbjahr;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.bk.BKGymBelegungsfehlerTyp;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import jakarta.validation.constraints.NotNull;

/**
 * Die abstrakte Klasse für die Belegprüfungen bei Bildungsgängen.
 */
public class BKGymBelegpruefung {
	/** Die Abiturdaten-Manager */
	protected final @NotNull BKGymAbiturdatenManager manager;

	/** Die Stundentafeln laut APO-BK der Anlage */
	final @NotNull List<BeruflichesGymnasiumStundentafel> stundentafeln;

	/** Eine HashMap2D für den schnellen Zugriff auf die Fächer der Stundentafeln anhand der Tafel und der Fachbezeichnung */
	private final @NotNull HashMap2D<@NotNull BeruflichesGymnasiumStundentafel, @NotNull String, @NotNull BeruflichesGymnasiumStundentafelFach>
		mapStundentafelFachByTafelAndFachbezeichnung = new HashMap2D<>();

	/** Die Belegungsfehler, die für jede Stundentafel bei der Prüfung festgehalten werden. */
	private final @NotNull HashMap<BeruflichesGymnasiumStundentafel, List<BKGymBelegungsfehler>> mapBelegungsfehler = new HashMap<>();

	/** Die Liste von Belegungsfehlern der am besten passenden Stundentafel */
	private @NotNull List<BKGymBelegungsfehler> besteFehlerliste = new ArrayList<>();

	/** Flag ob neue Fehler hinzugekommen sind */
	private boolean dirty = false;


	/**
	 * Erzeugt eine neue Belegprüfung mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Abiturdaten
	 */
	public BKGymBelegpruefung(final @NotNull BKGymAbiturdatenManager manager) {
		this.manager = manager;
		stundentafeln = manager.getStundentafeln();
		init();
	}


	/**
	 * Initialisiert die Maps für schnelle Zugriffe
	 */
	private void init() {
		for (final @NotNull BeruflichesGymnasiumStundentafel tafel : stundentafeln) {
			for (final @NotNull BeruflichesGymnasiumStundentafelFach fach : tafel.faecher) {
				mapStundentafelFachByTafelAndFachbezeichnung.put(tafel, fach.fachbezeichnung, fach);
			}
		}
	}


	/**
	 * Fügt einen Belegungsfehler zu der Belegprüfung hinzu. Diese Methode wird von den Sub-Klassen
	 * aufgerufen, wenn dort ein Belegungsfehler erkannt wird.
	 *
	 * @param tafel    die Stundentafel
	 * @param fehler   der hinzuzufügende Belegungsfehler
	 *
	 * @return true, falls ein Fehler vorliegt false, wenn nur ein Hinweis ausgegeben wurde.
	 */
	protected boolean addFehler(final BeruflichesGymnasiumStundentafel tafel, final @NotNull BKGymBelegungsfehler fehler) {
		if (tafel != null) {
			final List<BKGymBelegungsfehler> fehlerliste = mapBelegungsfehler.get(tafel);
			if (fehlerliste != null && !fehlerliste.contains(fehler)) {
				fehlerliste.add(fehler);
				dirty = true;
			}
		}
		return fehler.istFehler();
	}


	/**
	 * Ermittelt die Stundentafel mit den wenigsten Fehlern und gibt die zugehörigen Belegungsfehler aus
	 */
	private void ermittleBesteTafel() {
		if (dirty) {
			int minFehlerZahl = Integer.MAX_VALUE;
			for (final @NotNull List<BKGymBelegungsfehler> fehlerliste : mapBelegungsfehler.values()) {
				int fehlerZahl = 0;
				for (final @NotNull BKGymBelegungsfehler fehler : fehlerliste)
					fehlerZahl += fehler.wert;
				if (fehlerZahl < minFehlerZahl) {
					minFehlerZahl = fehlerZahl;
					besteFehlerliste = fehlerliste;
				}
			}
		}
		dirty = false;
	}


	/**
	 * Ermittelt die Stundentafel mit den wenigsten Fehlern und gibt die zugehörigen Belegungsfehler aus
	 *
	 * @return die Belegungsfehler der Stundentafel
	 */
	public @NotNull List<BKGymBelegungsfehler> getBelegungsfehler() {
		ermittleBesteTafel();
		return besteFehlerliste;
	}


	/**
	 * Gibt zurück, ob mindestens eine Stundentafel existiert, die keine "echten" Belegungsfehler hat. Warnungen und Hinweise werden toleriert.
	 *
	 * @return true, wenn kein "echter" Belegungsfehler vorliegt, und ansonsten false.
	 */
	public boolean istErfolgreich() {
		for (final @NotNull BKGymBelegungsfehler fehler : getBelegungsfehler())
			if (!fehler.istInfo())
				return false;
		return true;
	}


	/**
	 * Die Methode wird zur Durchführung der Belegprüfung aufgerufen.
	 *
	 * Sie führt zuerst die allgemeinen Prüfungen aus, die für alle Anlagen des beruflichen Gymnasiums identisch sind.
	 */
	public void pruefe() {
		for (final @NotNull BeruflichesGymnasiumStundentafel tafel : stundentafeln) {
			mapBelegungsfehler.put(tafel, new ArrayList<>());
			final @NotNull Map<Integer, List<BeruflichesGymnasiumStundentafelFach>> mapFaecherByIndex = manager.getMapFaecherFromTafelByIndex(tafel);
			final @NotNull Map<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>> mapBelegungByFach =
					manager.getMapBelegungenForTafelByFach(tafel);
			bewegeBelegungZuWahlfach(tafel, mapFaecherByIndex, mapBelegungByFach);
			allgemeinePruefungen(tafel, mapFaecherByIndex, mapBelegungByFach);
		}
	}


	/**
	 * Diese Methode identifiziert für Positionen einer Stundentafel, die Wahlmöglichkeiten vorsieht,
	 * ob mehrere Belegungen vorliegen, von denen nur eine der Position zugewiesen wird und die anderen
	 * dem Wahlfach zugeschlagen wird.
	 * Es sollte nur ein Fach pro Position vorhanden sein, dass in der Gruppe Berufsbezogen bzw. Berufsübergreifend ist.
	 * Die anderen Belegungen müssten Differenzierung zugewiesen sein.
	 * Dafür wird ermittelt welche Belegung den Vorgaben der Position entspricht. Die erste wird behalten, die
	 * anderen werden nach Wahlfach verschoben.
	 *
	 * @param tafel               die zu überprüfende Stundentafel
	 * @param mapFaecherByIndex   die Map mit den Fächern der Stundentafel
	 * @param mapBelegungByFach   die Map mit den Fächern der Belegungen
	 */
	private void bewegeBelegungZuWahlfach(@NotNull final BeruflichesGymnasiumStundentafel tafel,
			@NotNull final Map<Integer, List<BeruflichesGymnasiumStundentafelFach>> mapFaecherByIndex,
			@NotNull final Map<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>> mapBelegungByFach) {
		final @NotNull List<BKGymAbiturFachbelegung> wahlBelegungen = getBelegungenWahlfach(tafel, mapBelegungByFach);

		// Prüfe Einträge der Stundentafel mit Wahlmöglichkeit auf mehrfache Belegung
		for (final List<BeruflichesGymnasiumStundentafelFach> faecher : mapFaecherByIndex.values()) {
			if (faecher == null)
				continue;
			if (hatStundentafelpositionWahloption(faecher, mapBelegungByFach))
				bewegeWahlfach(faecher, mapBelegungByFach, wahlBelegungen);
		}
	}


	/**
	 * Prüft, ob die Stundentafel eine Wahloption hat anhand der gegebenen Fächerliste der Position
	 *
	 * @param faecher             die Liste der Fächer einer Position der Stundentafel
	 * @param mapBelegungByFach   die Map mit den Fächern der Belegungen
	 *
	 * @return true, wenn ein Wahlfach in der Position der Stundentafel vorhanden ist, sonst false
	 */
	private static boolean hatStundentafelpositionWahloption(final @NotNull List<BeruflichesGymnasiumStundentafelFach> faecher,
			@NotNull final Map<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>> mapBelegungByFach) {
		if (faecher.size() == 1)
			return false;

		final Iterator<BeruflichesGymnasiumStundentafelFach> iter = faecher.iterator();
		// schaue nach nicht belegten Fächern (und Differenzierungsfächern)
		while (iter.hasNext()) {
			final BeruflichesGymnasiumStundentafelFach tafelFach = iter.next();
			final List<BKGymAbiturFachbelegung> belegungen = mapBelegungByFach.get(tafelFach);
			if ((belegungen == null) || (belegungen.isEmpty())) //|| manager.istDifferenzierung(belegung))
				iter.remove();
		}

		return faecher.size() >= 2;
	}


	/**
	 * Bei Wahlmöglichkeiten für ein Pflichtfach wird bei Belegung mehrerer Fächer das erstbeste behalten und
	 * die anderen Belegungen zum Wahlfach übertragen
	 *
	 * @param faecher             die Liste der Fächer einer Position der Stundentafel
	 * @param mapBelegungByFach   die Map mit den Fächern der Belegungen
	 * @param wahlBelegungen      die Liste der Belegungen des Wahlfachs
	 */
	private void bewegeWahlfach(final @NotNull List<BeruflichesGymnasiumStundentafelFach> faecher,
			@NotNull final Map<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>> mapBelegungByFach, @NotNull final List<BKGymAbiturFachbelegung> wahlBelegungen) {
		if (!bewegeFortgefuehrteFS(faecher, mapBelegungByFach, wahlBelegungen)) {
			final Iterator<BeruflichesGymnasiumStundentafelFach> iter = faecher.iterator();
			boolean gefunden = false;
			while (iter.hasNext()) {
				final BeruflichesGymnasiumStundentafelFach tafelFach = iter.next();
				if (gefunden) {
					wahlBelegungen.addAll(mapBelegungByFach.get(tafelFach));
					iter.remove();
				} else {
					gefunden = istPflichtFach(tafelFach, mapBelegungByFach, !iter.hasNext());
					if (!gefunden) {
						wahlBelegungen.addAll(mapBelegungByFach.get(tafelFach));
						iter.remove();
					}
				}
			}
		}
	}


	/**
	 * Wird nur aufgerufen, wenn fortgeführte und neu einsetzende Fremdsprachen belegt sind.
	 * Die fortgeführte Fremdsprache wird zum Wahlfach verschoben
	 *
	 * @param faecher             die Liste der Fächer einer Position der Stundentafel
	 * @param mapBelegungByFach   die Map mit den Fächern der Belegungen
	 * @param wahlBelegungen      die Liste der Belegungen für das Wahlfach
	 *
	 * @return true, wenn die Fremdsprachen behandelt wurden, sonst false
	 */
	private boolean bewegeFortgefuehrteFS(@NotNull final List<BeruflichesGymnasiumStundentafelFach> faecher,
			@NotNull final Map<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>> mapBelegungByFach,
			@NotNull final List<BKGymAbiturFachbelegung> wahlBelegungen) {
		final BeruflichesGymnasiumStundentafelFach tafelFach = faecher.getFirst();
		if ((tafelFach != null) && (manager.istZweiteFremdsprache(tafelFach.fachbezeichnung) || manager.istNeueFremdsprache(tafelFach.fachbezeichnung))) {
			final Iterator<BeruflichesGymnasiumStundentafelFach> iter = faecher.iterator();
			while (iter.hasNext()) {
				final BeruflichesGymnasiumStundentafelFach tf = iter.next();
				if (manager.istZweiteFremdsprache(tf.fachbezeichnung)) {
					wahlBelegungen.addAll(mapBelegungByFach.get(tf));
					iter.remove();
				}
			}
			return true;
		}
		return false;
	}


	/**
	 * Gilt für die Positionen der Stundentafel, die Wahlmöglichkeiten vorsehen wie Physik/Chemie/Biologie.
	 * Hier wird geprüft, ob die Belegung in Ordnung ist.
	 *
	 * @param tafelFach           ein Fach der Stundentafel
	 * @param mapBelegungByFach   die Map mit den Fächern der Belegungen
	 * @param letztesFach         ob es das letzte Fach der Position in der Stundentafel ist.
	 *
	 * @return true, wenn als Pflichtfach erkannt, sonst false
	 */
	private boolean istPflichtFach(final BeruflichesGymnasiumStundentafelFach tafelFach,
			@NotNull final Map<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>> mapBelegungByFach, final boolean letztesFach) {
		if (tafelFach == null)
			return false;

		final List<BKGymAbiturFachbelegung> belegungen = mapBelegungByFach.get(tafelFach);

		if ((belegungen == null) || (belegungen.size() != 1))
			return letztesFach;

		final BKGymAbiturFachbelegung belegung = belegungen.getFirst();
		if (belegung != null)
			return pruefeBelegung(null, tafelFach, belegung);

		throw new DeveloperNotificationException(
					"In der Belegungsprüfung ist ein interner Fehler aufgetreten: Die Belegung für ein Fach ist nicht vorhanden.");
	}


	/**
	 * allgemeinePruefungen führt die Belegprüfungen durch, die für alle Anlagen des beruflichen Gymnasiums identisch ausgeführt werden können.
	 * Aufgetretene Fehler werden in die Map mapBelegungsfehler eingetragen.
	 *
	 * @param tafel                    die zu überprüfende Stundentafel
	 * @param mapFaecherByIndex        die Map mit den Fächern der Stundentafel
	 * @param mapBelegungByTafelfach   die Map mit den Fächern der Belegungen
	 */
	private void allgemeinePruefungen(final @NotNull BeruflichesGymnasiumStundentafel tafel,
			final @NotNull Map<Integer, List<BeruflichesGymnasiumStundentafelFach>> mapFaecherByIndex,
			final @NotNull Map<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>> mapBelegungByTafelfach) {
		pruefeLKs(tafel);
		pruefeAbiGrundkurse(tafel);

		// Eigentliche Belegprüfung: Durchwandern der Zuordnung der Fächer in der Stundentafel
		for (final List<BeruflichesGymnasiumStundentafelFach> faecher : mapFaecherByIndex.values()) {
			if (faecher != null)
				allgemeinePruefungenTafelfach(tafel, faecher, mapBelegungByTafelfach);
		}
	}


	/**
	 * Führt für eine Position der Stundentafel die Prüfung der Belegung durch.
	 *
	 * @param tafel                    die zu überprüfende Stundentafel
	 * @param faecher                  die Liste der Fächer einer Position der Stundentafel
	 * @param mapBelegungByTafelfach   die Map mit den Fächern der Belegungen
	 */
	private void allgemeinePruefungenTafelfach(@NotNull final BeruflichesGymnasiumStundentafel tafel, @NotNull final List<BeruflichesGymnasiumStundentafelFach> faecher,
			@NotNull final Map<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>> mapBelegungByTafelfach) {
		if (faecher.size() > 1)
			throw new DeveloperNotificationException(
					"In der Belegungsprüfung ist ein interner Fehler aufgetreten: Für eine Position der Stundentafel gibt es mehr als einen Eintrag.");
		// Wenn ein Fach keine Belegung braucht.
		if (faecher.isEmpty())
			throw new DeveloperNotificationException(
					"In der Belegungsprüfung ist ein interner Fehler aufgetreten: Für eine Position der Stundentafel gibt keinen Eintrag.");

		final BeruflichesGymnasiumStundentafelFach tafelFach = faecher.getFirst();
		if (tafelFach == null)
			return;

		final List<BKGymAbiturFachbelegung> belegungen = mapBelegungByTafelfach.get(tafelFach);
		if ((belegungen == null) || belegungen.isEmpty()) {
			addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.ST_4, tafelFach.fachbezeichnung));
			return;
		}

		if (manager.istWahlfach(tafelFach.fachbezeichnung))
			pruefeBelegungWahlfach(tafel, tafelFach, belegungen);
		else {
			if (belegungen.size() > 1)
				addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.ST_1, tafelFach.fachbezeichnung));
			else
				pruefeBelegung(tafel, tafelFach, belegungen.getFirst());
		}
	}


	/**
	 * Führt die Prüfungen für eine Belegung durch
	 *
	 * @param tafel          die Stundentafel der Anlage
	 * @param fachTafel      das zu prüfende Fach aus der Stundentafel
	 * @param fachBelegung   die zugeordnete Fachbelegung
	 *
	 * @return true, wenn die Prüfung keinen Fehler entdeckt, sonst false
	 */
	private boolean pruefeBelegung(final BeruflichesGymnasiumStundentafel tafel, final @NotNull BeruflichesGymnasiumStundentafelFach fachTafel,
			final @NotNull BKGymAbiturFachbelegung fachBelegung) {
		final boolean successStundenumfang = pruefeStundenumfang(tafel, fachTafel, fachBelegung);
		final boolean successSchriftlichkeit = pruefeSchriftlich(tafel, fachTafel, fachBelegung);
		return successStundenumfang && successSchriftlichkeit;
	}


	/**
	 * Schlägt die Belegungen für das Wahlfach nach
	 *
	 * @param tafel                    die betreffende Stundentafel
	 * @param mapBelegungByTafelfach   das zugehörige Mapping der Belegungen auf die Fächer der Stundentafel
	 *
	 * @return Die Liste der Belegungen für das Wahlfach
	 */
	private @NotNull List<BKGymAbiturFachbelegung> getBelegungenWahlfach(final @NotNull BeruflichesGymnasiumStundentafel tafel,
			final @NotNull Map<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>> mapBelegungByTafelfach) {
		final BeruflichesGymnasiumStundentafelFach tafelWahlfach = mapStundentafelFachByTafelAndFachbezeichnung.getOrNull(tafel, BKGymAbiturdatenManager.WAHLFACH);
		List<BKGymAbiturFachbelegung> wahlfachBelegungen = null;
		if (tafelWahlfach != null)
			wahlfachBelegungen = mapBelegungByTafelfach.computeIfAbsent(tafelWahlfach,  k -> new ArrayList<>());
		if (wahlfachBelegungen != null)
			return wahlfachBelegungen;
		return new ArrayList<>();
	}


	/**
	 * Führt die Belegungsprüfung für das Wahlfach aus.
	 * Hier muss nur die Summe der Wochenstunden pro Halbjahr erfüllt sein.
	 *
	 * @param tafel        die Stundentafel der Anlage
	 * @param fachTafel    das zu prüfende Fach aus der Stundentafel
	 * @param belegungen   die zugeordnete Fachbelegung
	 *
	 * @return true, wenn die Prüfung keinen Fehler entdeckt, sonst false
	 */
	private boolean pruefeBelegungWahlfach(final BeruflichesGymnasiumStundentafel tafel,
			final @NotNull BeruflichesGymnasiumStundentafelFach fachTafel, final @NotNull List<BKGymAbiturFachbelegung> belegungen) {
		boolean success = true;
		final @NotNull int[] summeWS = new int[GostHalbjahr.maxHalbjahre];
		// Summe der Wochenstunden der Wahlbelegungen ermitteln.
		for (final BKGymAbiturFachbelegung fachBelegung : belegungen)
			for (final @NotNull GostHalbjahr hj : GostHalbjahr.getQualifikationsphase()) {
				final BKGymAbiturFachbelegungHalbjahr belegung = fachBelegung.belegungen[hj.id];
				if (belegung != null)
					summeWS[hj.id] += belegung.wochenstunden;
			}

		// Prüfen ob die Wochenstunden in den Halbjahren erreicht wurden
		for (final @NotNull GostHalbjahr hj : GostHalbjahr.getQualifikationsphase())
			if ((summeWS[hj.id] < fachTafel.stundenumfang[hj.id])
					&& addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.ST_3, fachTafel.fachbezeichnung)))
				success = false;
		return success;
	}


	/**
	 * Prüft, ob Unterricht im erforderlichen Umfang erteilt wurde.
	 *
	 * @param tafel          die Stundentafel der Anlage
	 * @param fachTafel      das zu prüfende Fach aus der Stundentafel
	 * @param fachBelegung   die zugeordnete Fachbelegung
	 *
	 * @return true bei fehlerloser Prüfung sonst false
	 */
	private boolean pruefeStundenumfang(final BeruflichesGymnasiumStundentafel tafel,
			final @NotNull BeruflichesGymnasiumStundentafelFach fachTafel, final @NotNull BKGymAbiturFachbelegung fachBelegung) {
		boolean success = true;
		int summeTafel = 0;
		int summeBelegung = 0;
		boolean unterbelegung = false;
		for (final @NotNull GostHalbjahr hj : GostHalbjahr.getQualifikationsphase()) {
			summeTafel += fachTafel.stundenumfang[hj.id];
			final BKGymAbiturFachbelegungHalbjahr belegung = fachBelegung.belegungen[hj.id];

			if (belegung == null) {
				if (fachTafel.stundenumfang[hj.id] > 0)
					success = !addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.ST_6, fachTafel.fachbezeichnung, hj.kuerzel)) && success;
				continue;
			}

			if ((belegung.notenkuerzel != null) && (!belegung.notenkuerzel.isEmpty())) {
				summeBelegung += belegung.wochenstunden;
				unterbelegung |= (fachTafel.stundenumfang[hj.id] > belegung.wochenstunden);
			} else if (fachTafel.stundenumfang[hj.id] > 0) {
				success = !(addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.ST_2, fachTafel.fachbezeichnung, hj.kuerzel))) && success;
				unterbelegung = true;
			}
		}


		if (summeTafel > summeBelegung) {
			success = !addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.ST_3, fachTafel.fachbezeichnung)) && success;
			return success;
		}

		success = !(unterbelegung && addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.ST_5_INFO, fachTafel.fachbezeichnung)))  && success;
		return success;
	}



	/**
	 * Prüft, ob die Schriftlichkeit der Fächer korrekt erfüllt ist.
	 *
	 * @param tafel          die Stundentafel der Anlage
	 * @param fachTafel      das zu prüfende Fach aus der Stundentafel
	 * @param fachBelegung   die zugeordnete Fachbelegung
	 *
	 * @return true, wenn die Prüfung keinen Fehler entdeckt, sonst false
	 */
	private boolean pruefeSchriftlich(final BeruflichesGymnasiumStundentafel tafel, final @NotNull BeruflichesGymnasiumStundentafelFach fachTafel,
			final @NotNull BKGymAbiturFachbelegung fachBelegung) {
		boolean success;
		success = pruefeSchriftlichEF(GostHalbjahr.EF1, tafel, fachTafel, fachBelegung);
		success = pruefeSchriftlichEF(GostHalbjahr.EF2, tafel, fachTafel, fachBelegung) && success;
		success = pruefeSchriftlichQ1(GostHalbjahr.Q11, tafel, fachTafel, fachBelegung) && success;
		success = pruefeSchriftlichQ1(GostHalbjahr.Q12, tafel, fachTafel, fachBelegung) && success;
		success = pruefeSchriftlichQ2(GostHalbjahr.Q21, tafel, fachTafel, fachBelegung) && success;
		success = pruefeSchriftlichQ2(GostHalbjahr.Q22, tafel, fachTafel, fachBelegung) && success;
		return success;
	}


	/**
	 * In der EF muss in mindestens vier Fächern, in den LK-Fächern, Deutsch, Mathematik und Fremdsprachen in jedem Fall
	 *
	 * @param hj             das Oberstufenhalbjahr
	 * @param tafel          die Stundentafel der Anlage
	 * @param fachTafel      das zu prüfende Fach aus der Stundentafel
	 * @param fachBelegung   die zugeordnete Fachbelegung
	 *
	 * @return true, wenn die Prüfung keinen Fehler entdeckt, sonst false
	 */
	private boolean pruefeSchriftlichEF(final @NotNull GostHalbjahr hj, final BeruflichesGymnasiumStundentafel tafel, @NotNull final BeruflichesGymnasiumStundentafelFach fachTafel,
			@NotNull final BKGymAbiturFachbelegung fachBelegung) {
		final BKGymAbiturFachbelegungHalbjahr belegungHj = fachBelegung.belegungen[hj.id];

		if ((belegungHj == null) || (belegungHj.schriftlich))
			return true;
		if ("Deutsch".equals(fachTafel.fachbezeichnung) || "Mathematik".equals(fachTafel.fachbezeichnung))
			return !addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.KL_1, fachTafel.fachbezeichnung, hj.kuerzel));
		if ((fachBelegung.abiturFach != null) && (fachBelegung.abiturFach <= 2))
			return !addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.KL_2, fachTafel.fachbezeichnung, hj.kuerzel));
		if (manager.istFremdsprachenbelegung(fachBelegung))
			return !addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.KL_3, fachTafel.fachbezeichnung, hj.kuerzel));
		return true;
	}


	/**
	 * In der Q1 müssen allen Abiturfächer schriftlich belegt sein. Deutsch, Mathematik,
	 * Fremdsprachen und die Fächer der Berufsabschlussprüfung in jedem Fall
	 *
	 * @param hj             das Oberstufenhalbjahr
	 * @param tafel          die Stundentafel der Anlage
	 * @param fachTafel      das zu prüfende Fach aus der Stundentafel
	 * @param fachBelegung   die zugeordnete Fachbelegung
	 *
	 * @return true, wenn die Prüfung keinen Fehler entdeckt, sonst false
	 */
	private boolean pruefeSchriftlichQ1(final @NotNull GostHalbjahr hj, final BeruflichesGymnasiumStundentafel tafel, @NotNull final BeruflichesGymnasiumStundentafelFach fachTafel,
			@NotNull final BKGymAbiturFachbelegung fachBelegung) {
		final BKGymAbiturFachbelegungHalbjahr belegungHj = fachBelegung.belegungen[hj.id];

		if ((belegungHj == null) || (belegungHj.schriftlich))
			return true;
		if ("Deutsch".equals(fachTafel.fachbezeichnung) || "Mathematik".equals(fachTafel.fachbezeichnung))
			return !addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.KL_1, fachTafel.fachbezeichnung, hj.kuerzel));
		if ((fachBelegung.abiturFach != null) && (fachBelegung.abiturFach <= 4))
			return !addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.KL_4, fachTafel.fachbezeichnung, hj.kuerzel));
		if (manager.istFremdsprachenbelegung(fachBelegung))
			return !addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.KL_3, fachTafel.fachbezeichnung, hj.kuerzel));
		return true;
	}


	/**
	 * In der Q2 müssen das erste bis dritte Abiturfach schriftlich belegt sein. Deutsch, Mathematik,
	 * Nur in der Q21 auch die Fremdsprachen
	 *
	 * @param hj             das Oberstufenhalbjahr
	 * @param tafel          die Stundentafel der Anlage
	 * @param fachTafel      das zu prüfende Fach aus der Stundentafel
	 * @param fachBelegung   die zugeordnete Fachbelegung
	 *
	 * @return true, wenn die Prüfung keinen Fehler entdeckt, sonst false
	 */
	private boolean pruefeSchriftlichQ2(final @NotNull GostHalbjahr hj, final BeruflichesGymnasiumStundentafel tafel, @NotNull final BeruflichesGymnasiumStundentafelFach fachTafel,
			@NotNull final BKGymAbiturFachbelegung fachBelegung) {
		final BKGymAbiturFachbelegungHalbjahr belegungHj = fachBelegung.belegungen[hj.id];

		if ((belegungHj == null) || (belegungHj.schriftlich))
			return true;
		if ((fachBelegung.abiturFach != null) && (fachBelegung.abiturFach <= 3))
			return !addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.KL_4, fachTafel.fachbezeichnung, hj.kuerzel));
		if ((GostHalbjahr.Q21 == hj) && manager.istFremdsprachenbelegung(fachBelegung))
			return !addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.KL_3, fachTafel.fachbezeichnung, hj.kuerzel));
		return true;
	}


	/**
	 * Diese Methode prüft die LK-Belegung in der Stundentafel.
	 *
	 * @param tafel   die zu prüfende Stundentafel
	 */
	private void pruefeLKs(final @NotNull BeruflichesGymnasiumStundentafel tafel) {
		// Bestimme die Leistungskursbelegungen. Diese müssen für die weitere Prüfung vorhanden sein.
		final BKGymAbiturFachbelegung lk1 = manager.getAbiFachbelegung(GostAbiturFach.LK1);
		if (lk1 == null)
			addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.LK_1));
		final BKGymAbiturFachbelegung lk2 = manager.getAbiFachbelegung(GostAbiturFach.LK2);
		if (lk2 == null)
			addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.LK_2));
		if (lk1 != null && lk2 != null && !(manager.istGueltigeKursartFachbelegung(tafel, lk1, GostAbiturFach.LK1)
				&& manager.istGueltigeKursartFachbelegung(tafel, lk2, GostAbiturFach.LK2)))
			addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.LK_3, manager.getFachkuerzelFromFachbelegung(lk1),
					manager.getFachkuerzelFromFachbelegung(lk2), manager.getGliederung().name(), manager.getFachklassenschluessel()));
	}


	/**
	 * pruefe die Belegung des 3. und 4. Abiturfachs
	 *
	 * @param tafel
	 */
	private void pruefeAbiGrundkurse(final @NotNull BeruflichesGymnasiumStundentafel tafel) {
		// Prüfe, ob das dritte und vierte Abiturfach korrekt gewählt sind.
		// Bestimme das dritte Abiturfach
		final BKGymAbiturFachbelegung ab3 = manager.getAbiFachbelegung(GostAbiturFach.AB3);
		if (ab3 == null)
			addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.AB_3));

		// Bestimme das vierte Abiturfach
		final BKGymAbiturFachbelegung ab4 = manager.getAbiFachbelegung(GostAbiturFach.AB4);
		if (ab4 == null)
			addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.AB_4));

		// Prüfe die Wahlmöglichkeiten bei den Varianten in Bezug auf AB3 und AB4, verlasse ggf. die Prüfung, wenn keine Wahlmöglichkeit für AB3 und AB4 gefunden wurde
		if ((ab3 != null) && (ab4 != null) && !manager.pruefeAbiGrundkurswahl(tafel, ab3, ab4))
			addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.AB_5, manager.getFachkuerzelFromFachbelegung(ab3),
					manager.getFachkuerzelFromFachbelegung(ab4), manager.getGliederung().name(), manager.getFachklassenschluessel()));
	}
}
