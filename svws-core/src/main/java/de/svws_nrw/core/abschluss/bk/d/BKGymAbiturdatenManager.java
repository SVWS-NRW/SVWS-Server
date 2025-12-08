package de.svws_nrw.core.abschluss.bk.d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	// spezielle Fächer in den Stundentafel der Anlage D
	/** Die Zweite Fremdsprache */
	public final @NotNull String zweiteFremdsprache = "Zweite Fremdsprache";
	/** Die Neueinsetzende Fremdsprache */
	public final @NotNull String neueFremdsprache = "Neue Fremdsprache";
	/** Das Wahlfach */
	public final @NotNull String wahlfach = "Wahlfach";

	/** Die Abiturdaten des Schülers */
	private final @NotNull BKGymAbiturdaten abidaten;

	/** Die Schulgliederung des Bildungsgangs des Schülers */
	private final @NotNull Schulgliederung gliederung;

	/** Der Fachklassen-Schlüssel des Bildungsgangs des Schülers */
	private final @NotNull String fks;

	/** Die Anlage, die zur Schulgliederung und Fachklasse gehört */
	private final @NotNull BeruflichesGymnasiumPruefungsordnungAnlage anlage;

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
	 * @param bisHalbjahr      die Art der Belegprüfung - bis zu welchem Halbjahr geprüft werden soll
	 */
	public BKGymAbiturdatenManager(final @NotNull BKGymAbiturdaten abidaten, final @NotNull Schulgliederung gliederung, final @NotNull String fks,
			final @NotNull BKGymFaecherManager faecherManager, final @NotNull GostHalbjahr bisHalbjahr) {
		this.abidaten = abidaten;
		this.gliederung = gliederung;
		this.fks = fks;
		this.faecherManager = faecherManager;
		this.bisHalbjahr = bisHalbjahr;
		this.anlage = bestimmeAnlage();
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
	 * Ermittelt die Anlage zu einer Fachklasse in der Schulgliederung D01.
	 *
	 * @return die Anlage
	 */
	private @NotNull BeruflichesGymnasiumPruefungsordnungAnlage bestimmeAnlage() {
		return switch (gliederung) {
			case D01 -> getAnlageD01();
			case D02 -> getAnlageD02();
			default ->
				throw new DeveloperNotificationException("Die Belegprüfung für die Schulgliederung " + gliederung.name() + " wird noch nicht unterstützt.");
		};
	}


	private @NotNull BeruflichesGymnasiumPruefungsordnungAnlage getAnlageD01() {
		return switch (fks) {
			case "10100" -> BeruflichesGymnasiumPruefungsordnungAnlage.D6;
			case "10200" -> BeruflichesGymnasiumPruefungsordnungAnlage.D1;
			case "10300" -> BeruflichesGymnasiumPruefungsordnungAnlage.D7;
			case "10400" -> BeruflichesGymnasiumPruefungsordnungAnlage.D8;
			case "10500" -> BeruflichesGymnasiumPruefungsordnungAnlage.D2;
			case "10600" -> BeruflichesGymnasiumPruefungsordnungAnlage.D3;
			case "10700" -> BeruflichesGymnasiumPruefungsordnungAnlage.D4;
			// case "10800" -> BeruflichesGymnasiumPruefungsordnungAnlage.D;
			case "10900" -> BeruflichesGymnasiumPruefungsordnungAnlage.D12;
			// case "11000" -> BeruflichesGymnasiumPruefungsordnungAnlage.D;
			case "11100" -> BeruflichesGymnasiumPruefungsordnungAnlage.D9;
			case "11200" -> BeruflichesGymnasiumPruefungsordnungAnlage.D13;
			// case "11300" -> BeruflichesGymnasiumPruefungsordnungAnlage.D;
			case "11400" -> BeruflichesGymnasiumPruefungsordnungAnlage.D10;
			case "11500" -> BeruflichesGymnasiumPruefungsordnungAnlage.D3a;
			default -> throw new DeveloperNotificationException("Die Belegprüfung für die Schulgliederung " + gliederung.name()
					+ " und den Fachklassenschlüssel " + fks + " wird noch nicht unterstützt.");
		};
	}


	private @NotNull BeruflichesGymnasiumPruefungsordnungAnlage getAnlageD02() {
		return switch (fks) {
			case "10100" -> BeruflichesGymnasiumPruefungsordnungAnlage.D14;
			case "10200" -> BeruflichesGymnasiumPruefungsordnungAnlage.D27;
			case "10300" -> BeruflichesGymnasiumPruefungsordnungAnlage.D22;
			case "10400" -> BeruflichesGymnasiumPruefungsordnungAnlage.D23;
			// case "10500" -> BeruflichesGymnasiumPruefungsordnungAnlage.D;
			case "10600" -> BeruflichesGymnasiumPruefungsordnungAnlage.D25;
			case "10700" -> BeruflichesGymnasiumPruefungsordnungAnlage.D15;
			// case "10800" -> BeruflichesGymnasiumPruefungsordnungAnlage.D;
			case "10900" -> BeruflichesGymnasiumPruefungsordnungAnlage.D19;
			case "11000" -> BeruflichesGymnasiumPruefungsordnungAnlage.D16;
			case "11100" -> BeruflichesGymnasiumPruefungsordnungAnlage.D17;
			// case "11200" -> BeruflichesGymnasiumPruefungsordnungAnlage.D;
			case "11300" -> BeruflichesGymnasiumPruefungsordnungAnlage.D18;
			case "11400" -> BeruflichesGymnasiumPruefungsordnungAnlage.D20;
			case "11500" -> BeruflichesGymnasiumPruefungsordnungAnlage.D21;
			// case "11600" -> BeruflichesGymnasiumPruefungsordnungAnlage.D;
			// case "11700" -> BeruflichesGymnasiumPruefungsordnungAnlage.D;
			// case "11800" -> BeruflichesGymnasiumPruefungsordnungAnlage.D;
			// case "11900" -> BeruflichesGymnasiumPruefungsordnungAnlage.D;
			case "12000" -> BeruflichesGymnasiumPruefungsordnungAnlage.D17a;
			case "12100" -> BeruflichesGymnasiumPruefungsordnungAnlage.D15a;
			case "12200" -> BeruflichesGymnasiumPruefungsordnungAnlage.D28;
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
		return new BKGymBelegpruefung(this);
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
	 * Getter für den Zugriff auf die Anlage
	 *
	 * @return die Anlage
	 */
	public @NotNull BeruflichesGymnasiumPruefungsordnungAnlage getAnlage() {
		return anlage;
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
	 * Prüft ob es sich um die Bezeichnung für das symbolische Wahlfach handelt.
	 * @param bezeichnung   eine Fachbezeichnung aus der Stundentafel
	 *
	 * @return true wenn es die Repräsentation für das Wahlfach ist, sonst false
	 */
	public boolean istWahlfach(final @NotNull String bezeichnung) {
		return bezeichnung.equals(wahlfach);
	}


	/**
	 * Prüft ob es sich um die Bezeichnung für das symbolische Fach Zweite Fremdsprache handelt.
	 * @param bezeichnung   eine Fachbezeichnung aus der Stundentafel
	 *
	 * @return true wenn es die Repräsentation für die zweite Fremdsprache ist, sonst false
	 */
	public boolean istZweiteFremdsprache(final @NotNull String bezeichnung) {
		return bezeichnung.equals(zweiteFremdsprache);
	}


	/**
	 * Prüft ob es sich um die Bezeichnung für das symbolische Fach Neue Fremdsprache handelt.
	 * @param bezeichnung   eine Fachbezeichnung aus der Stundentafel
	 *
	 * @return true wenn es die Repräsentation für das Wahlfach ist, sonst false
	 */
	public boolean istNeueFremdsprache(final @NotNull String bezeichnung) {
		return bezeichnung.equals(neueFremdsprache);
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
	 * Liefert eine Belegung anhand der Fachbezeichnung zurück
	 *
	 * @param bezeichnung   das Fach
	 *
	 * @return die Fachbelegung
	 */
	public BKGymAbiturFachbelegung getFachbelegungByBezeichnung(@NotNull final String bezeichnung) {
		return mapFachbelegungenByFachbezeichnung.get(bezeichnung);
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
	 * Prüft, ob es sich bei der Fachbelegung um eine Belegung einer Fremdsprache handelt.
	 *
	 * @param fb   die Fachbelegung
	 *
	 * @return true, wenn es sich um eine Fremdsprachenbelegung handelt, und ansonsten false
	 */
	public boolean istFremdsprachenbelegung(final @NotNull BKGymAbiturFachbelegung fb) {
		// Prüfe, ob das Fach in der Fächerliste des Abiturjahrgangs überhaupt existiert
		final BKGymFach fbFach = faecherManager.get(fb.fachID);
		if ((fbFach == null) || (fbFach.bezeichnung == null))
			return false;
		return fbFach.istFremdsprache;
	}

	/**
	 * Prüft, ob es sich bei der Fachbelegung um eine Belegung einer neu einsetzenden Fremdsprache handelt.
	 *
	 * @param fb   die Fachbelegung
	 *
	 * @return true, wenn es sich um ein neu einsetzende Fremdsprachenbelegung handelt, und ansonsten false
	 */
	public boolean istNeueFremdsprachenbelegung(final @NotNull BKGymAbiturFachbelegung fb) {
		// Prüfe, ob das Fach in der Fächerliste des Abiturjahrgangs überhaupt existiert
		final BKGymFach fbFach = faecherManager.get(fb.fachID);
		if ((fbFach == null) || (fbFach.bezeichnung == null))
			return false;
		return fbFach.istFremdSpracheNeuEinsetzend;
	}


	/**
	 * liefert die Fachbezeichnung einer Belegung
	 *
	 * @param fb   die Fachbelegung
	 *
	 * @return die Fachbezeichnung
	 */
	public @NotNull String getFachbezeichnungFromFachbelegung(final @NotNull BKGymAbiturFachbelegung fb) {
		// Prüfe, ob das Fach in der Fächerliste des Abiturjahrgangs überhaupt existiert
		final BKGymFach fbFach = faecherManager.get(fb.fachID);
		if ((fbFach == null) || (fbFach.bezeichnung == null))
			return new String();
		return fbFach.bezeichnung;
	}


	/**
	 * liefert die Fachbezeichnung einer Belegung
	 *
	 * @param fb   die Fachbelegung
	 *
	 * @return die Fachbezeichnung
	 */
	public @NotNull String getFachkuerzelFromFachbelegung(final @NotNull BKGymAbiturFachbelegung fb) {
		// Prüfe, ob das Fach in der Fächerliste des Abiturjahrgangs überhaupt existiert
		final BKGymFach fbFach = faecherManager.get(fb.fachID);
		if ((fbFach == null) || (fbFach.kuerzelAnzeige == null))
			return new String();
		return fbFach.kuerzelAnzeige;
	}


	/**
	 * Prüft, ob die übergebene Fachbelgung als Fach in der Stundentafel vorkommt bzw. vorkommen kann.
	 *
	 * @param tafel   die Stundentafel
	 * @param fb      die Fachbelegung
	 *
	 * @return der Eintrag der Stundentafel, bei welchem die Fachbelegung vorkommt, oder null, wenn keine Zuordnung zur Stundentafel möglich ist
	 */
	public BeruflichesGymnasiumStundentafelFach getFachByBelegung(final @NotNull BeruflichesGymnasiumStundentafel tafel,
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
	public boolean isValidKursartFachbelegung(final @NotNull BeruflichesGymnasiumStundentafel tafel, final @NotNull BKGymAbiturFachbelegung fb,
			final @NotNull GostAbiturFach abifach) {
		// Prüfe zunächst, ob die Fachbelegung der Stundentafel zugeordnet werden kann
		final BeruflichesGymnasiumStundentafelFach tafelFach = getFachByBelegung(tafel, fb);
		if (tafelFach == null)
			return false;

		// Prüfe ggf. ob die Kursart LK1 bzw. LK2 ist, sofern es sich um eine Abiturfachbelegung im LK-Bereich handeln soll
		if (((abifach == GostAbiturFach.LK1) && (tafelFach.abifach != null) && (tafelFach.abifach == 1))
				|| ((abifach == GostAbiturFach.LK2) && (tafelFach.abifach != null) && (tafelFach.abifach == 2)))
			return tafelFach.kursart.equals("LK");

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
	 * Prüft ob die Fachkombination für das dritte und vierte Abiturfach gültig ist.
	 *
	 * @param tafel   die zu prüfende Stundentafel mit ihren Wahlmöglichkeiten
	 * @param ab3     die Belegung des dritten Abiturfaches
	 * @param ab4     die Belegung des vierten Abiturfaches
	 *
	 * @return true, wenn die Wahlmöglichkeit besteht, ansonsten false
	 */
	public boolean pruefeAbiGrundkurswahl(final @NotNull BeruflichesGymnasiumStundentafel tafel, final @NotNull BKGymAbiturFachbelegung ab3,
			final @NotNull BKGymAbiturFachbelegung ab4) {
		// Bestimme ob das dritte und vierte Abiturfach gültig gewählt wurden
		for (final @NotNull BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit wm : tafel.wahlmoeglichkeiten) {
			if (isValidWahlmoeglichkeit(wm, ab3, ab4))
				return true;
		}
		return false;
	}


	/**
	 * Liefert eine Map, die zu jedem Index der Fachtafel die zugehörigen Fächer liefert.
	 * Hier sind die Wahlmöglichkeiten enthalten, die eine Stundentafelvariante erlaubt.
	 *
	 * @param tafel   die Stundentafel mit der Liste der Fächer
	 *
	 * @return die Map
	 */
	public @NotNull Map<Integer, List<BeruflichesGymnasiumStundentafelFach>> getMapFaecherFromTafelByIndex(final @NotNull BeruflichesGymnasiumStundentafel tafel) {
		final @NotNull Map<Integer, List<BeruflichesGymnasiumStundentafelFach>> mapFaecher = new HashMap<>();
		for (final @NotNull BeruflichesGymnasiumStundentafelFach fach : tafel.faecher) {
			// Nur Fächer, die mindestens in einem Halbjahr mit mehr als 0 Stunden belegt werden müssen, aufnehmen
			if ((fach.stundenumfang[0] > 0) || (fach.stundenumfang[1] > 0) || (fach.stundenumfang[2] > 0)
						|| (fach.stundenumfang[3] > 0) || (fach.stundenumfang[4] > 0) || (fach.stundenumfang[5] > 0)) {
				List<BeruflichesGymnasiumStundentafelFach> faecher = mapFaecher.get(fach.sortierung);
				if (faecher == null) {
					faecher = new ArrayList<>();
					mapFaecher.put(fach.sortierung, faecher);
				}
				faecher.add(fach);
			}
		}
		return mapFaecher;
	}


	/**
	 * Liefert eine Map, die zu jedem Fach der Stundentafel die zugehörigen Belegungen zuordnet.
	 *
	 * @param tafel   die Stundentafel aus der APO-BK Anlage D
	 *
	 * @return die Map
	 */
	public @NotNull Map<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>> getMapBelegungenForTafelByFach(final @NotNull BeruflichesGymnasiumStundentafel tafel) {
		final @NotNull Map<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>> mapBelegungenByFach = new HashMap<>();
		final @NotNull Set<BKGymAbiturFachbelegung> zugeordnet = new HashSet<>();
		@NotNull List<BKGymAbiturFachbelegung> zweiteFremdspracheBelegungen = new ArrayList<>();
		@NotNull List<BKGymAbiturFachbelegung> neueFremdspracheBelegungen = new ArrayList<>();
		@NotNull List<BKGymAbiturFachbelegung> wahlfachBelegungen = new ArrayList<>();
		// Zuordnung der Belegungen für alle Fächer außer den Platzhalterfächern Zweite Fremdsprache und Wahlfach
		for (final @NotNull BeruflichesGymnasiumStundentafelFach fach : tafel.faecher) {
			List<BKGymAbiturFachbelegung> belegungen = mapBelegungenByFach.get(fach);
			if (belegungen == null) {
				belegungen = new ArrayList<>();
				mapBelegungenByFach.put(fach, belegungen);
			}
			final BKGymAbiturFachbelegung belegung = getFachbelegungByBezeichnung(fach.fachbezeichnung);
			if (belegung != null) {
				belegungen.add(belegung);
				zugeordnet.add(belegung);
			} else if (istZweiteFremdsprache(fach.fachbezeichnung))
				zweiteFremdspracheBelegungen = belegungen;
			else if (istNeueFremdsprache(fach.fachbezeichnung))
				neueFremdspracheBelegungen = belegungen;
			else if (istWahlfach(fach.fachbezeichnung))
				wahlfachBelegungen = belegungen;
		}
		// Zuordnung der Fremdsprachen zur zweiten Fremdsprache und der nicht genutzten Fächern zu den Wahlfächern
		final @NotNull List<BKGymAbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (final BKGymAbiturFachbelegung fachbelegung : fachbelegungen) {
			if (!zugeordnet.contains(fachbelegung)) {
				if (istNeueFremdsprachenbelegung(fachbelegung))
					neueFremdspracheBelegungen.add(fachbelegung);
				else if (istFremdsprachenbelegung(fachbelegung))
					zweiteFremdspracheBelegungen.add(fachbelegung);
				else
					wahlfachBelegungen.add(fachbelegung);
			}
		}

		return mapBelegungenByFach;
	}


	/**
	 * Liefert die Stundentafeln, die zur APO-BK-Anlage dieses Managers gehören
	 *
	 * @return die Liste der Stundentafeln
	 */
	public @NotNull List<BeruflichesGymnasiumStundentafel> getStundentafeln() {
		// Bestimme die Liste der für die Prüfungsordnung möglichen Stundentafeln - Gebe im Fehlerfall eine leere Liste zurück
		final @NotNull List<BeruflichesGymnasiumStundentafel> result = new ArrayList<>();
		final int schuljahr = getSchuljahrAbitur();
		final BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag poke = anlage.daten(schuljahr);
		if (poke == null)
			return result;
		return poke.stundentafeln;
	}

}
