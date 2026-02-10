package de.svws_nrw.core.abschluss.bk.d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafel;
import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit;
import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafelFach;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.bk.BKGymAbiturUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Manager-Klasse für eine die Stundentafeln eines Bildungsgangs in der Anlage D.
 * Aus den Stundentafeln des CoreTypes für die Anlage des Schülers werden alle möglichen
 * Stundentafeln erzeugt, die keine Wahlmöglichkeit in den einzelnen Positionen haben;
 * dabei wird schon berücksichtigt, ob ein Fach überhaupt belegt ist.
 */
public class BKGymStundentafelManager {
	// spezielle Fächer in den Stundentafel der Anlage D
	/** Die Bezeichnung für die zweite fortgeführte Fremdsprache */
	public static final @NotNull String ZWEITE_FREMDSPRACHE = "Zweite Fremdsprache";

	/** Die Bezeichnung für die neueinsetzende Fremdsprache */
	public static final @NotNull String NEUE_FREMDSPRACHE = "Neue Fremdsprache";

	/** Die Bezeichnung für das Fach Religion */
	public static final @NotNull String RELIGION = "Religionslehre";

	/** Das Wahlfach */
	public static final @NotNull String WAHLFACH = "Wahlfach";

	/** Die möglichen Ersatzfächer für Religion */
	public static final @NotNull List<String> ERSATZ_FUER_RELIGION = List.of(
			"Erziehungswissenschaften", "Geschichte", "Gesellschaftslehre mit Geschichte",
			"Philosophie", "Politik/Geschichte", "Psychologie", "Soziologie");

	/** Der Abiturdaten-Manager */
	private final @NotNull BKGymFachbelegungManager fachbelegungManager;

	/** Die Liste der Stundentafeln im Manager. */
	private final @NotNull List<BeruflichesGymnasiumStundentafel> stundentafeln = new ArrayList<>();


	// Datenstrukturen zum schnellen Zugriff auf Fachbelegungen

	/** Eine HashMap2D für den schnellen Zugriff auf die Fächer der Stundentafeln anhand der Tafel und der Fachbezeichnung */
	private final @NotNull HashMap2D<@NotNull BeruflichesGymnasiumStundentafel, @NotNull String, @NotNull BeruflichesGymnasiumStundentafelFach>
		mapStundentafelFachByTafelAndFachbezeichnung = new HashMap2D<>();


	/**
	 * Erstellt einen Manager mit den übergebenen Stundentafeln.
	 *
	 * @param belegungManager   der Fachbelegung-Manager
	 * @param tafeln            die Liste der Original-Stundentafeln eines Bildungsgangs aus dem CoreType
	 */
	public BKGymStundentafelManager(final @NotNull BKGymFachbelegungManager belegungManager, final @NotNull List<BeruflichesGymnasiumStundentafel> tafeln) {
		this.fachbelegungManager = belegungManager;
		this.stundentafeln.clear();
		for (final BeruflichesGymnasiumStundentafel t : tafeln) {
			this.stundentafeln.addAll(createStundentafelnOhneWahlmoeglichkeit(t));
		}
		for (final BeruflichesGymnasiumStundentafel t : this.stundentafeln)
			pruefeStundentafelAufDoppelte(t);

		//initialisiere Datenstruktur
		for (final @NotNull BeruflichesGymnasiumStundentafel tafel : this.stundentafeln)
			for (final @NotNull BeruflichesGymnasiumStundentafelFach fach : tafel.faecher)
				mapStundentafelFachByTafelAndFachbezeichnung.put(tafel, fach.fachbezeichnung, fach);
	}


	/**
	 * Getter für die Liste der Stundentafeln
	 *
	 * @return die Liste der Stundentafeln
	 */
	public @NotNull List<BeruflichesGymnasiumStundentafel> getStundentafeln() {
		return stundentafeln;
	}


	/**
	 * Liefert zu einer Fachbezeichnung und Stundentafel das entsprechende BeruflichesGymnasiumStundentafelFach-Objekt
	 *
	 * @param tafel             die Stundentafel, in der gesucht wird.
	 * @param fachbezeichnung   die Bezeichnung des Fachs
	 *
	 * @return das gefundene Stundentafelfach oder null, wenn es nicht existiert.
	 */
	public BeruflichesGymnasiumStundentafelFach getFachByTafelAndBezeichnung(final @NotNull BeruflichesGymnasiumStundentafel tafel,
			final @NotNull String fachbezeichnung) {
		return mapStundentafelFachByTafelAndFachbezeichnung.getOrNull(tafel, fachbezeichnung);
	}


	/**
	 * Erzeugt die die Liste der Stundentafeln entsprechend der übergebenen Stundentafel unter Berücksichtigung der Belegungen.
	 *
	 * @param t   die Stundentafel mit Wahlmöglichkeiten
	 *
	 * @return die Liste der Stundentafeln ohne Wahlmöglichkeiten entsprechend der belegten Fächer
	 */
	private @NotNull List<BeruflichesGymnasiumStundentafel> createStundentafelnOhneWahlmoeglichkeit(final @NotNull BeruflichesGymnasiumStundentafel t) {
		final @NotNull List<BeruflichesGymnasiumStundentafel> neueTafeln = new ArrayList<>();
		final @NotNull List<List<BeruflichesGymnasiumStundentafelFach>> alleMoeglichenFaecherlisten = getMoeglicheFaecherlisten(t);
		for (final List<BeruflichesGymnasiumStundentafelFach> fachListe : alleMoeglichenFaecherlisten) {
			final BeruflichesGymnasiumStundentafel neueTafel = new BeruflichesGymnasiumStundentafel();
			neueTafel.variante = t.variante;
			neueTafel.faecher = fachListe;
			neueTafel.wahlmoeglichkeiten = t.wahlmoeglichkeiten;
			neueTafeln.add(neueTafel);
		}
		return neueTafeln;
	}


	/**
	 * Erstellt eine Map der Fächer gruppiert nach der Sortierung.
	 * In dieser Map sind nicht belegte Wahlmöglichkeiten einer Fachtafelposition entfernt.
	 * Mindestens eine Position bleibt erhalten, falls kein Fach der Position belegt wurde.
	 *
	 * @param t   die Stundentafel
	 *
	 * @return die Map der Fächer gruppiert nach der Sortierung
	 */
	private @NotNull List<List<BeruflichesGymnasiumStundentafelFach>> getMoeglicheFaecherlisten(final @NotNull BeruflichesGymnasiumStundentafel t) {
		final @NotNull Map<Integer, List<BeruflichesGymnasiumStundentafelFach>> mapFachBySortierung = new HashMap<>();
		// erstelle Map der Fächer gruppiert nach Sortierung
		for (final BeruflichesGymnasiumStundentafelFach fach : t.faecher) {
			List<BeruflichesGymnasiumStundentafelFach> fachListe = mapFachBySortierung.get(fach.sortierung);
			if (fachListe == null) {
				fachListe = new ArrayList<>();
				mapFachBySortierung.put(fach.sortierung, fachListe);
			}
			fachListe.add(fach);
		}

		final @NotNull Map<Integer, List<BeruflichesGymnasiumStundentafelFach>> mapMultiFachBySortierung = new HashMap<>();
		final @NotNull List<BeruflichesGymnasiumStundentafelFach> eindeutigeFaecher = new ArrayList<>();
		for (final Entry<Integer, List<BeruflichesGymnasiumStundentafelFach>> entry : mapFachBySortierung.entrySet()) {
			final List<BeruflichesGymnasiumStundentafelFach> bereinigt = bereinigteFaecherliste(entry.getValue());
			if (bereinigt.isEmpty())
				eindeutigeFaecher.add(entry.getValue().getFirst());
			else if (bereinigt.size() == 1)
				eindeutigeFaecher.add(bereinigt.getFirst());
			else if (bereinigt.size() > 1)
				mapMultiFachBySortierung.put(entry.getKey(), bereinigt);
		}
		return createStundentafelPermutation(eindeutigeFaecher, mapMultiFachBySortierung);
	}


	/**
	 * Erstellt alle möglichen Permutationen der Stundentafeln basierend auf den eindeutigen Fächern
	 * und den Fächern mit mehreren Belegungsmöglichkeiten.
	 *
	 * @param eindeutigeFaecher          die Liste der eindeutigen Fächer
	 * @param mapMultiFachBySortierung   die Map der Fächer mit mehreren Belegungsmöglichkeiten gruppiert nach Sortierung
	 *
	 * @return die Liste der möglichen Stundentafel-Permutationen
	 */
	private static @NotNull List<List<BeruflichesGymnasiumStundentafelFach>> createStundentafelPermutation(
			final @NotNull List<BeruflichesGymnasiumStundentafelFach> eindeutigeFaecher,
			final @NotNull Map<Integer, List<BeruflichesGymnasiumStundentafelFach>> mapMultiFachBySortierung) {
		final @NotNull List<List<BeruflichesGymnasiumStundentafelFach>> result = new ArrayList<>();
		if (mapMultiFachBySortierung.isEmpty()) {
			result.add(eindeutigeFaecher);
			return result;
		}

		// Schlüssel als Liste
		final @NotNull List<Integer> keys = new ArrayList<>(mapMultiFachBySortierung.keySet());

		// Rekursive Methode für die Permutation
		permutiereFaecher(result, eindeutigeFaecher, mapMultiFachBySortierung, keys, 0, new ArrayList<>());
		return result;
	}


	/**
	 * Rekursive Methode zur Permutation der Fächer.
	 *
	 * @param result            die Liste der Ergebnisse
	 * @param eindeutigeFaecher die Liste der eindeutigen Fächer
	 * @param map               die Map der Fächer mit mehreren Belegungsmöglichkeiten
	 * @param keys              die sortierten Schlüssel der Map
	 * @param index             der aktuelle Index in den Schlüsseln
	 * @param current           die aktuelle Kombination von Fächern
	 */
	private static void permutiereFaecher(final @NotNull List<List<BeruflichesGymnasiumStundentafelFach>> result,
			final @NotNull List<BeruflichesGymnasiumStundentafelFach> eindeutigeFaecher,
			final @NotNull Map<Integer, List<BeruflichesGymnasiumStundentafelFach>> map,
			final @NotNull List<Integer> keys, final int index, final @NotNull List<BeruflichesGymnasiumStundentafelFach> current) {
		// Abbruchbedingung: Alle Schlüssel wurden verarbeitet
		if (index == keys.size()) {
			final List<BeruflichesGymnasiumStundentafelFach> kombi = new ArrayList<>(eindeutigeFaecher);
			kombi.addAll(current);
			kombi.sort(BKGymAbiturUtils.comparatorStundentafelFach);
			result.add(kombi);
			return;
		}

		// Rekursiver Fall: Iteriere über alle Fächer der aktuellen Fachtafelposition
		final List<BeruflichesGymnasiumStundentafelFach> currentFaecher = map.get(keys.get(index));
		if (currentFaecher == null)
			return;
		for (final BeruflichesGymnasiumStundentafelFach fach : currentFaecher) {
			current.add(fach);
			permutiereFaecher(result, eindeutigeFaecher, map, keys, index + 1, current);
			current.remove(current.size() - 1);
		}
	}


	/**
	 * Gibt die Liste mit den Fächern der übergebenen faecher zurück, die entweder belegt sind oder ein gültiges Spezialfach wie "Wahlfach" ist.
	 * Bei dem speziellen Stundentafeleintrag für die zweite bzw. neue Fremdsprache wird nur das Fach zurückgegeben, das auch tatsächlich zu verwenden ist.
	 *
	 * @param faecher   die Liste der Fächer einer Fachtafelposition
	 *
	 * @return das eindeutige Fach oder null, wenn kein eindeutiges Fach gefunden wurde
	 */
	private @NotNull List<BeruflichesGymnasiumStundentafelFach> bereinigteFaecherliste(final @NotNull List<BeruflichesGymnasiumStundentafelFach> faecher) {
		final @NotNull List<BeruflichesGymnasiumStundentafelFach> bereinigt = new ArrayList<>();
		for (final @NotNull BeruflichesGymnasiumStundentafelFach fach : faecher) {
			if (istGueltigesSpezialfach(fach.fachbezeichnung) || fachbelegungManager.getFachbelegungByBezeichnung(fach.fachbezeichnung) != null)
				bereinigt.add(fach);
		}
		return bereinigt;
	}


	/**
	 * Prüft, ob das Spezialfach zu übernehmen ist.
	 * Bei Wahlfach wird true zurück gegeben, da es nur einmal in einer Fachtafel vorkommt.
	 * Bei Zweiter Fremdsprache wird true geliefert, wenn keine zweite Fremdsprache erkannt wurde (zweiteFremdspracheID==null)
	 * oder die Fremdsprache erkannt wurde und die Fremdsprachenbelegung in der SI erfüllt wurde.
	 * Bei Neue Fremdsprache wird nur dann true geliefert, wenn eine zweite Fremdsprache erkannt wurde und die
	 * Fremdsprachenbelegung in der SI nicht erfüllt wurde.
	 *
	 * @param fachbezeichnung   die Bezeichnung des zu prüfenden Fachs,
	 *
	 * @return siehe oben
	 */
	private boolean istGueltigesSpezialfach(final @NotNull String fachbezeichnung) {
		if (istWahlfach(fachbezeichnung))
			return true;
		if (istZweiteFremdsprache(fachbezeichnung))
			return !fachbelegungManager.istZweiteFremdspracheNeuEinsetzend();
		if (istNeueFremdsprache(fachbezeichnung))
			return fachbelegungManager.istZweiteFremdspracheNeuEinsetzend();
		return false;
	}


	/**
	 * Prüft ob es sich um die Bezeichnung für das symbolische Wahlfach handelt.
	 * @param bezeichnung   eine Fachbezeichnung aus der Stundentafel
	 *
	 * @return true wenn es die Repräsentation für das Wahlfach ist, sonst false
	 */
	public static boolean istWahlfach(final @NotNull String bezeichnung) {
		return bezeichnung.equals(WAHLFACH);
	}


	/**
	 * Prüft ob es sich um die Bezeichnung für das symbolische Fach Zweite Fremdsprache handelt.
	 * @param bezeichnung   eine Fachbezeichnung aus der Stundentafel
	 *
	 * @return true wenn es die Repräsentation für die zweite Fremdsprache ist, sonst false
	 */
	public static boolean istZweiteFremdsprache(final @NotNull String bezeichnung) {
		return bezeichnung.equals(ZWEITE_FREMDSPRACHE);
	}


	/**
	 * Prüft ob es sich um die Bezeichnung für das symbolische Fach Neue Fremdsprache handelt.
	 *
	 * @param bezeichnung   eine Fachbezeichnung aus der Stundentafel
	 *
	 * @return true, wenn es die Repräsentation für die Neue Fremdsprache ist, sonst false
	 */
	public static boolean istNeueFremdsprache(final @NotNull String bezeichnung) {
		return bezeichnung.equals(NEUE_FREMDSPRACHE);
	}


	/**
	 * Prüft ob es sich um die Bezeichnung für das Fach Religion handelt
	 *
	 * @param bezeichnung   die Fachbezeichnung
	 *
	 * @return true, wenn es sich um die Fachbezeichnung für Religion handelt.
	 */
	public static boolean istReligion(final @NotNull String bezeichnung) {
		return bezeichnung.equals(RELIGION);
	}


	/**
	 * Prüft ob die Fachkombination für das dritte und vierte Abiturfach gültig ist.
	 *
	 * @param tafel   die zu prüfende Stundentafel mit ihren Wahlmöglichkeiten
	 * @param ab3Bezeichnung   die Bezeichnung des dritten Abiturfaches
	 * @param ab4Bezeichnung   die Bezeichnung vierten Abiturfaches
	 *
	 * @return true, wenn die Wahlmöglichkeit besteht, ansonsten false
	 */
	public boolean pruefeAbiGrundkurswahl(final @NotNull BeruflichesGymnasiumStundentafel tafel,
			final @NotNull String ab3Bezeichnung, final @NotNull String ab4Bezeichnung) {
		// Bestimme ob das dritte und vierte Abiturfach gültig gewählt wurden
		for (final @NotNull BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit wm : tafel.wahlmoeglichkeiten) {
			if (istGueltigeWahlmoeglichkeit(wm, ab3Bezeichnung, ab4Bezeichnung))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob die übergebene Kombination aus drittem und viertem Abiturfach gültig ist.
	 * Dabei werden die Spezialfälle für eine zweite Fremdsprache und ein mögliches Wahlfach (Zukunftstauglichkeit)
	 * berücksichtigt
	 *
	 * @param wm    die Wahlmöglichkeit
	 * @param ab3Bezeichnung   die Bezeichnung des dritten Abiturfaches
	 * @param ab4Bezeichnung   die Bezeichnung vierten Abiturfaches
	 *
	 * @return true, wenn sie gültig ist, und ansonsten false
	 */
	private boolean istGueltigeWahlmoeglichkeit(final @NotNull BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit wm,
			final @NotNull String ab3Bezeichnung, final @NotNull String ab4Bezeichnung) {

		// Prüfe zunächst, ob das dritte Fach der Wahlmöglichkeit entspricht
		String wm3 = null;
		for (final @NotNull String fachBez3 : wm.abifach3)
			if (fachBez3.equals(ab3Bezeichnung) || BKGymStundentafelManager.istWahlfach(fachBez3)
					|| (istZweiteFremdsprache(fachBez3) && fachbelegungManager.istZweiteFremdsprache(ab3Bezeichnung)))
				wm3 = fachBez3;
		if (wm3 == null)
			return false;

		// Prüfe danach, ob auch das vierte Fach der Wahlmöglichkeit entspricht
		for (final @NotNull String fachBez4 : wm.abifach4)
			if (fachBez4.equals(ab4Bezeichnung) || BKGymStundentafelManager.istWahlfach(fachBez4)
					|| (BKGymStundentafelManager.istZweiteFremdsprache(fachBez4) && fachbelegungManager.istZweiteFremdsprache(ab4Bezeichnung)))
				return true;
		return false;
	}


	/**
	 * Prüft, dass keine Sortierung doppelt vorkommt. Falls es vorkommt wird einer DeveloperNotificationException geworfen.
	 *
	 * @param t   die Stundentafel
	 */
	private static void pruefeStundentafelAufDoppelte(final @NotNull BeruflichesGymnasiumStundentafel t) {
		final @NotNull Set<Integer> single = new HashSet<>();
		for (final @NotNull BeruflichesGymnasiumStundentafelFach fach : t.faecher) {
			if (single.contains(fach.sortierung))
				throw new DeveloperNotificationException("In der Belegprüfung ist ein interner Fehler aufgetreten: In der Stundentafel sind noch Wahlmöglichkeiten enthalten.");
			single.add(fach.sortierung);
		}
	}


	/**
	 * Ermittelt den maximalen Wert des Attributes sortierung in der Stundentafel
	 *
	 * @param tafel   die Stundentafel
	 *
	 * @return der maximale Wert von sortierung
	 */
	public static int getMaximalSortierung(final @NotNull BeruflichesGymnasiumStundentafel tafel) {
		int max = Integer.MIN_VALUE;

		for (final @NotNull BeruflichesGymnasiumStundentafelFach fach : tafel.faecher)
			if (fach.sortierung > max)
				max = fach.sortierung;
		return max;
	}


	/**
	 * Prüft, ob das Fach in der Stundentafel eine Belegung benötigt.
	 *
	 * @param fach   das Fach der Stundentafel
	 *
	 * @return true, wenn das Fach eine Belegung benötigt, sonst false
	 */
	public static boolean brauchtBelegungInQPhase(@NotNull final BeruflichesGymnasiumStundentafelFach fach) {
		int summeStunden = 0;
		for (final GostHalbjahr hj : GostHalbjahr.getQualifikationsphase())
			summeStunden += fach.stundenumfang[hj.id];
		return summeStunden > 0;
	}
}
