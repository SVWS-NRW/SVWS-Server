package de.svws_nrw.core.abschluss.bk.d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag;
import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafel;
import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit;
import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafelFach;
import de.svws_nrw.asd.types.schule.BeruflichesGymnasiumPruefungsordnungAnlage;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.data.bk.abi.BKGymFach;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.bk.BKGymFaecherManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Methoden zur Verfügung um die angegebenen Abiturdaten zu bearbeiten und Auswertungen durchzuführen.
 */
public class BKGymAbiturdatenManager {

	/** Die Abiturdaten des Schülers */
	private final @NotNull BKGymAbiturdaten abidaten;

	/** Die Schulgliederung des Bildungsgangs des Schülers */
	private final @NotNull Schulgliederung gliederung;

	/** Der Fachklassen-Schlüssel des Bildungsgangs des Schülers */
	private final @NotNull String fks;

	/** Der Manager für die Fächer des beruflichen Gymnasiums */
	private final @NotNull BKGymFaecherManager faecherManager;

	/** Das Halbjahr, bis zu welchem die Belegprüfung durchgeführt werden soll */
	private final @NotNull GostHalbjahr bisHalbjahr;

	/** Der Belegprüfungsalgorithmus */
	private final @NotNull BKGymBelegpruefung belegpruefung;

	/** Eine HashMap, welche den schnellen Zugriff auf die Fachbelegungen für ein Fach anhand der Bezeichnung ermöglicht */
	private final @NotNull Map<String, BKGymAbiturFachbelegung> mapFachbelegungenByFachbezeichnung = new HashMap<>();

	/** Die Menge der Belegprüfungsfehler, die bei den durchgeführten Belegprüfungen aufgetreten sind. */
	private @NotNull List<BKGymBelegungsfehler> belegpruefungsfehler = new ArrayList<>();

	/** Gibt an, ob die Belegprüfung insgesamt erfolgreich war oder nicht. */
	private boolean belegpruefungErfolgreich = false;

	// Datenstrukturen zum schnellen Zugriff auf Fachbelegungen

	/** Eine Map, welche von der Nummer des Abiturfaches auf die Fachbelegung der Abiturdaten verweist.*/
	private final @NotNull HashMap<Integer, BKGymAbiturFachbelegung> mapAbiturfachbelegungen = new HashMap<>();



	/**
	 * Erstellt ein neues Manager-Objekt, welches mit den übergebenen Abiturdaten verknüpft wird.
	 *
	 * @param abidaten         die Abiturdaten des Schülers
	 * @param gliederung       die Schulgliederung des Bildungsgangs des Schülers
	 * @param fks              der fünfstellige Fachklassenschlüssel des Bildungsgangs des Schülers
	 * @param faecherManager   der Manager für die Fächer
	 * @param bisHalbjahr      die Art der Belegpruefung - bis zu welchem Halbjahr geprüft werden soll
	 */
	public BKGymAbiturdatenManager(final @NotNull BKGymAbiturdaten abidaten, final @NotNull Schulgliederung gliederung, final @NotNull String fks,
			final @NotNull BKGymFaecherManager faecherManager, final @NotNull GostHalbjahr bisHalbjahr) {
		this.abidaten = abidaten;
		this.gliederung = gliederung;
		this.fks = fks;
		this.faecherManager = faecherManager;
		this.bisHalbjahr = bisHalbjahr;
		this.belegpruefung = getBelegpruefung();
		init();
		this.belegpruefung.pruefe();
		belegpruefungsfehler = this.belegpruefung.getBelegungsfehler();
		belegpruefungErfolgreich = this.belegpruefung.istErfolgreich();
	}


	/**
	 * Initialisiert bzw. reinitialisert die Datenstrukturen, die für den schnellen Zugriff auf die Daten
	 * eingerichtet werden.
	 */
	public void init() {
		// Leere die HashMaps und erstelle ggf. neue Listen für die Zuordnung von Abitur-Fachbelegungen
		mapFachbelegungenByFachbezeichnung.clear();
		mapAbiturfachbelegungen.clear();

		// Durchwandere alle belegten Fächer und weise diese den Fachbereichen und den Abiturfächern zu
		final @NotNull List<BKGymAbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (final BKGymAbiturFachbelegung fachbelegung : fachbelegungen) {
			// Ordne den Abiturfächern die Fachbelegungen zu
			if (fachbelegung.abiturFach != null)
				mapAbiturfachbelegungen.put(fachbelegung.abiturFach, fachbelegung);

			// Ordne die Fachbelegungen ihren Bezeichnungen zu.
			final BKGymFach fach = faecherManager.get(fachbelegung.fachID);
			if ((fach == null) || (fach.bezeichnung == null))
				continue;
			mapFachbelegungenByFachbezeichnung.put(fach.bezeichnung, fachbelegung);
		}
	}


	/**
	 * Erstellt eine Belegprüfung zu einer Fachklasse in der Schulgliederung D01.
	 *
	 * @return der Belegprüfungsalgorithmus
	 */
	private @NotNull BKGymBelegpruefung createBelegpruefungD01() {
		return switch (fks) {
			case "10600" -> new BKGymBelegpruefungD3(this);
			default -> throw new DeveloperNotificationException("Die Belegprüfung für die Schulgliederung " + gliederung.name()
					+ " und den Fachklassenschlüssel " + fks + " wird noch nicht unterstützt.");
		};
	}


	/**
	 * Erstellt die zugehörige Belegprüfung mit den Abiturdaten anhand des übergebenen Bildungsganges.
	 *
	 * @return der Belegprüfungsalgorithmus
	 */
	private @NotNull BKGymBelegpruefung getBelegpruefung() {
		final @NotNull BKGymBelegpruefung pruefung = switch (gliederung) {
			case D01 -> createBelegpruefungD01();
			default ->
				throw new DeveloperNotificationException("Die Belegprüfung für die Schulgliederung " + gliederung.name() + " wird noch nicht unterstützt.");
		};
		return pruefung;
	}


	/**
	 * Getter für den Zugriff auf die Abiturdaten
	 *
	 * @return die Abiturdaten
	 */
	public @NotNull BKGymAbiturdaten getAbidaten() {
		return abidaten;
	}


	/**
	 * Getter für den Zugriff auf das Halbjahr, bis zu welchem geprüft werde soll
	 *
	 * @return das Halbjahr
	 */
	public @NotNull GostHalbjahr getBisHalbjahr() {
		return bisHalbjahr;
	}


	/**
	 * Getter für den Zugriff auf die Schulgliederung des Bildungsganges
	 *
	 * @return die Schulgliederung des Bildungsganges
	 */
	public @NotNull Schulgliederung getGliederung() {
		return gliederung;
	}


	/**
	 * Getter für den Zugriff auf den Fachklassenschlüssel des Bildungsganges
	 *
	 * @return der Fachklassenschlüssel des Bildungsganges
	 */
	public @NotNull String getFachklassenschluessel() {
		return fks;
	}

	/**
	 * Getter für den Zugriff auf das Schuljahr in dem das Abitur stattfindet
	 *
	 * @return das Schuljahr des Abiturs
	 */
	public int getSchuljahrAbitur() {
		return this.abidaten.schuljahrAbitur;
	}

	/**
	 * Gibt das Ergebnis der Belegprüfung zurück. Dieses enthält eine Liste der Fehler, die bei der Belegprüfung
	 * festgestellt wurden und ob diese erfolgreich gewesen ist oder nicht.
	 *
	 * @return das Ergebnis der Belegprüfung
	 */
	public @NotNull BKGymBelegpruefungErgebnis getBelegpruefungErgebnis() {
		final @NotNull BKGymBelegpruefungErgebnis ergebnis = new BKGymBelegpruefungErgebnis();
		ergebnis.erfolgreich = belegpruefungErfolgreich;
		for (int i = 0; i < belegpruefungsfehler.size(); i++) {
			final @NotNull BKGymBelegungsfehler fehler = belegpruefungsfehler.get(i);
			ergebnis.fehlercodes.add(new BKGymBelegpruefungErgebnisFehler(fehler));
		}
		return ergebnis;
	}


	/**
	 * Gibt das Abiturfachdaten für das geforderte Abiturfach zurück.
	 *
	 * @param abiFach Das n. Abiturfach, das gewünscht ist
	 *
	 * @return die entsprechende Fachbelegung des Abiturfachs
	 */
	public BKGymAbiturFachbelegung getAbiFachbelegung(@NotNull final GostAbiturFach abiFach) {
		return mapAbiturfachbelegungen.get(abiFach.id);
	}

	/**
	 * Prüft, ob die übergebene Fachbelgung als Fach in der Stundentafel vorkommt bzw. vorkommen kann.
	 *
	 * @param tafel   die Stundentafel
	 * @param fb      die Fachbelegung
	 *
	 * @return der Eintrag der Stundentafel, bei welchem die Fachbelegung vorkommt, oder null, wenn keine Zuordnung zur Stundentafel möglich ist
	 */
	private BeruflichesGymnasiumStundentafelFach getFachByBelegung(final @NotNull BeruflichesGymnasiumStundentafel tafel,
			final @NotNull BKGymAbiturFachbelegung fb) {
		// Prüfe, ob das Fach in der Fächerliste des Abiturjahrgangs überhaupt existiert
		final BKGymFach fbFach = faecherManager.get(fb.fachID);
		if ((fbFach == null) || (fbFach.bezeichnung == null))
			return null;

		// Wenn die Bezeichnungen gleich sind, dann wurde ein Fach gefunden
		for (final BeruflichesGymnasiumStundentafelFach tafelFach : tafel.faecher)
			if (tafelFach.fachbezeichnung.equals(fbFach.bezeichnung))
				return tafelFach;

		// Wenn es sich um eine Fremdsprache handelt, dann kann diese ggf. als zweite Fremdsprache genommen werden
		if (fbFach.istFremdsprache)
			for (final BeruflichesGymnasiumStundentafelFach tafelFach : tafel.faecher)
				if (tafelFach.fachbezeichnung.equals("Zweite Fremdsprache"))
					return tafelFach;

		// Ggf. kann die Fachbelegung auch als Wahlfach gewertet werden.
		for (final BeruflichesGymnasiumStundentafelFach tafelFach : tafel.faecher)
			if (tafelFach.fachbezeichnung.equals("Wahlfach"))
				return tafelFach;

		return null;
	}


	/**
	 * Gibt TRUE zurück falls die übergebene Fachbelegung in der Stundentafel in der durch Abifach gegebenen Rolle gültig ist.
	 *
	 * @param tafel     die Stundentafel
	 * @param fb        die zu prüfende Fachbelegung
	 * @param abifach   die Rolle als Abiturfach oder null als Nicht-Abiturfach
	 *
	 * @return true, wenn die Belegung mit der angegebenen Kursart gültig ist und ansonsten FALSE
	 */
	private boolean isValidKursartFachbelegung(final @NotNull BeruflichesGymnasiumStundentafel tafel, final @NotNull BKGymAbiturFachbelegung fb,
			final @NotNull GostAbiturFach abifach) {
		// Prüfe zunächst, ob die Fachbelegung der Stundentafel zugeordnet werden kann
		final BeruflichesGymnasiumStundentafelFach tafelFach = getFachByBelegung(tafel, fb);
		if (tafelFach == null)
			return false;

		// Prüfe ggf. ob die Kursart LK1 bzw. LK2 ist, sofern es sich um eine Abiturfachbelegung im LK-Bereich handeln soll
		if ((abifach == GostAbiturFach.LK1) || (abifach == GostAbiturFach.LK2))
			return tafelFach.kursart.equals(abifach.kuerzel);

		// Prüfe ggf. ob die Kursart AB3, AB4 oder AB5 ist, sofern es sich um eine Abiturfachbelegung im GK-Bereich handeln soll (nur grobe Prüfung!)
		if ((abifach == GostAbiturFach.AB3) || (abifach == GostAbiturFach.AB4) || (abifach == GostAbiturFach.AB5)) {
			for (final @NotNull BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit wm : tafel.wahlmoeglichkeiten) {
				final @NotNull List<String> abifaecher =
						(abifach == GostAbiturFach.AB3) ? wm.abifach3 : ((abifach == GostAbiturFach.AB4) ? wm.abifach4 : wm.abifach5);
				for (final @NotNull String bezeichnung : abifaecher)
					if (bezeichnung.equals(tafelFach.fachbezeichnung))
						return true;
			}
			return false;
		}

		// Bei einem Nicht-Abiturfach genügt es, wenn das Fach in der Stundentafel existiert
		return true;
	}


	/**
	 * Bestimmt zu der übergebenen Anlage der Prüfungsordnung die möglichen Stundentafel-Variante anhand der Leistungskurs-Kombinationen.
	 *
	 * @param anlage   die Anlage der Prüfungsordnung, deren Stundentafel bestimmt werden soll
	 *
	 * @return die Liste der möglichen Variante anhand der belegten Leistungskurse
	 */
	public @NotNull List<BeruflichesGymnasiumStundentafel> getStundentafelByLeistungskurse(final @NotNull BeruflichesGymnasiumPruefungsordnungAnlage anlage) {
		// Bestimme die Liste der für die Prüfungsordnung möglichen Stundentafeln - Gebe im Fehlerfall eine leere Liste zurück
		final @NotNull List<BeruflichesGymnasiumStundentafel> result = new ArrayList<>();
		final int schuljahr = getSchuljahrAbitur();
		final BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag poke = anlage.daten(schuljahr);
		if (poke == null)
			return result;

		// Bestimme die Leistungskursbelegungen. Fehlt eine, so gebe eine leere Liste an Stundentafeln zurück
		final BKGymAbiturFachbelegung lk1 = getAbiFachbelegung(GostAbiturFach.LK1);
		final BKGymAbiturFachbelegung lk2 = getAbiFachbelegung(GostAbiturFach.LK2);
		if ((lk1 == null) || (lk2 == null))
			return result;

		// Bestimme die Stundentafeln, die zu der LK-Kombination passen
		for (final BeruflichesGymnasiumStundentafel tafel : poke.stundentafeln)
			if (isValidKursartFachbelegung(tafel, lk1, GostAbiturFach.LK1) && isValidKursartFachbelegung(tafel, lk2, GostAbiturFach.LK2))
				result.add(tafel);

		return result;
	}

	/**
	 * Prüft, ob die übergebene Kombination aus drittem und viertem Abiturfach gültig ist.
	 * Dabei werden die Spezialfälle für eine zweite Fremdsprache und ein mögliches Wahlfach (Zukunftstauglichkeit)
	 * berücksichtigt
	 *
	 * @param wm    die Wahlmöglichkeit
	 * @param ab3   die Belegung des dritten Abiturfaches
	 * @param ab4   die Belegung des vierten Abiturfaches
	 *
	 * @return true, wenn sie gültig ist, und ansonsten false
	 */
	private boolean isValidWahlmoeglichkeit(final @NotNull BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit wm,
			final @NotNull BKGymAbiturFachbelegung ab3, final @NotNull BKGymAbiturFachbelegung ab4) {
		final BKGymFach ab3Fach = faecherManager.get(ab3.fachID);
		final BKGymFach ab4Fach = faecherManager.get(ab4.fachID);
		if ((ab3Fach == null) || (ab4Fach == null))
			return false;

		// Prüfe zunächst, ob das dritte Fach der Wahlmöglichkeit entspricht
		String wm3 = null;
		for (final @NotNull String fachBez3 : wm.abifach3)
			if (fachBez3.equals(ab3Fach.bezeichnung) || ("Zweite Fremdsprache".equals(fachBez3) && ab3Fach.istFremdsprache) || "Wahlfach".equals(fachBez3))
				wm3 = fachBez3;
		if (wm3 == null)
			return false;

		// Prüfe danach, ob auch das vierte Fach der Wahlmöglichkeit entspricht
		for (final @NotNull String fachBez4 : wm.abifach4)
			if (fachBez4.equals(ab4Fach.bezeichnung) || ("Zweite Fremdsprache".equals(fachBez4) && ab4Fach.istFremdsprache) || "Wahlfach".equals(fachBez4))
				return true;
		return false;
	}


	/**
	 * Bestimmt die Wahlmöglichkeit, welche zu der Belelgung des dritten und vierten Abiturfaches passt.
	 *
	 * @param tafel   die Stundentafel
	 *
	 * @return die Wahlmöglichkeit oder null, wenn keine passt
	 */
	public BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit getWahlmoeglichkeitAusStundentafel(
			final @NotNull BeruflichesGymnasiumStundentafel tafel) {
		// 3. und 4. Abiturfach werden benötigt...
		final BKGymAbiturFachbelegung ab3 = getAbiFachbelegung(GostAbiturFach.AB3);
		final BKGymAbiturFachbelegung ab4 = getAbiFachbelegung(GostAbiturFach.AB4);
		if ((ab3 == null) || (ab4 == null))
			return null;

		// Bestimme die eindeutige Wahlmöglichekti anhand des dritten und vierten Faches
		BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit found = null;
		for (final @NotNull BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit wm : tafel.wahlmoeglichkeiten) {
			if (!isValidWahlmoeglichkeit(wm, ab3, ab4))
				continue;
			if (found != null)
				throw new DeveloperNotificationException(
						"In der Definition der Prüfungsordnung ist ein Fehler aufgetreten: Eine Abiturfachkombination darf in der Definition zur Prüfungsordnung der Anlage bei einer Variante nicht mehrfach auftreten.");
			found = wm;
		}
		return found;
	}

	/**
	 * Liefert eine Zuordnung der gefundenen Wahlmöglichkeiten zu deren Stundentafeln als Map.
	 *
	 * @param tafeln   die zu prüfenden Stundetafeln mit ihren Wahlmöglichkeiten
	 *
	 * @return die Map
	 */
	public @NotNull Map<BeruflichesGymnasiumStundentafel, BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit> getWahlmoeglichekiten(final @NotNull List<BeruflichesGymnasiumStundentafel> tafeln) {
		final @NotNull Map<BeruflichesGymnasiumStundentafel, BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit> result = new HashMap<>();
		for (final BeruflichesGymnasiumStundentafel tafel : tafeln) {
			final BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit wm = getWahlmoeglichkeitAusStundentafel(tafel);
			if (wm != null)
				result.put(tafel, wm);
		}
		return result;
	}

}
