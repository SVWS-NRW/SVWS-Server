package de.svws_nrw.core.abschluss.bk.d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag;
import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafel;
import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit;
import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafelFach;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.schule.BeruflichesGymnasiumPruefungsordnungAnlage;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsalgorithmus;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturFachbelegung;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusErgebnis;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturdaten;
import de.svws_nrw.core.data.bk.abi.BKGymBelegpruefungErgebnis;
import de.svws_nrw.core.data.bk.abi.BKGymFach;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.bk.BKGymFaecherManager;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Methoden zur Verfügung um die angegebenen Abiturdaten zu bearbeiten und Auswertungen durchzuführen.
 */
public class BKGymAbiturdatenManager {
	// spezielle Fächer in den Stundentafel der Anlage D
	/** Die Zweite Fremdsprache */
	public static final @NotNull String ZWEITE_FREMDSPRACHE = "Zweite Fremdsprache";

	/** Die Neueinsetzende Fremdsprache */
	public static final @NotNull String NEUE_FREMDSPRACHE = "Neue Fremdsprache";

	/** Das Wahlfach */
	public static final @NotNull String WAHLFACH = "Wahlfach";

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

	/** FachID der zweiten Fremdsprache */
	private final Long zweiteFremdspracheID;

	/** Ob eine zweite Fremdsprache in der SekI vier Jahre lang belegt wurde */
	private final boolean zweiteFremdspracheInSekIErfuellt;

	/** Ob das Fach der Facharbeit ein LK ist */
	private final boolean istFacharbeitLK;

	/** Der Belegprüfungsalgorithmus */
	private final @NotNull BKGymBelegpruefung belegpruefung;

	/** Der Markierungsalgorithmus */
	private final @NotNull BKGymAbiturMarkierungsalgorithmus markieren;

	/** Eine HashMap, welche den schnellen Zugriff auf die Fachbelegungen für ein Fach anhand der Bezeichnung ermöglicht */
	private final @NotNull Map<String, BKGymAbiturFachbelegung> mapFachbelegungenByFachbezeichnung = new HashMap<>();

	/** Die Menge der Belegprüfungsfehler, die bei den durchgeführten Belegprüfungen aufgetreten sind. */
	private @NotNull List<BKGymBelegungsfehler> belegpruefungsfehler = new ArrayList<>();

	/** Gibt an, ob die Belegprüfung insgesamt erfolgreich war oder nicht. */
	private boolean belegpruefungErfolgreich = false;

	/** Das Ergebnis des Markierungsalgorithmus */
	private BKGymAbiturMarkierungsalgorithmusErgebnis ergebnisMarkierungsalgorithmus = null;

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
		this.zweiteFremdspracheInSekIErfuellt = istZweiteFremdspracheInSekIErfuellt();
		this.anlage = bestimmeAnlage();
		this.istFacharbeitLK = pruefeIstFacharbeitLK();
		this.belegpruefung = getBelegpruefung();
		this.markieren = new BKGymAbiturMarkierungsalgorithmus(this);
		init();
		this.zweiteFremdspracheID = ermittleZweiteFremdspracheID();
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
	 * Ermittelt ob die Facharbeit einem LK-Fach zugeordnet ist.
	 * Wird dann auf false gesetzt, wenn eine Facharbeit vorhanden ist und die Fachbezeichnung
	 * für die Facharbeit nicht dem LK1 oder LK2 zugeordnet werden kann.
	 *
	 * @return false wenn Facharbeit vorhanden und nicht einem LK zugeordnet sonst true
	 */
	private boolean pruefeIstFacharbeitLK() {
		if (abidaten.facharbeitFachbezeichnung == null)
			return true;
		final Long facharbeitFachID = faecherManager.getFachIDByBezeichnung(abidaten.facharbeitFachbezeichnung);
		if (facharbeitFachID == null)
			return false;
		final Long fachIDLK1 = getAbiFachID(GostAbiturFach.LK1);
		if (fachIDLK1 != null && facharbeitFachID.equals(fachIDLK1))
			return true;
		final Long fachIDLK2 = getAbiFachID(GostAbiturFach.LK2);
		if (fachIDLK2 == null)
			return false;
		return facharbeitFachID.equals(fachIDLK2);
	}


		/**
	 * Führte die Schritte zur Belegprüfung aus
	 */
	private void belegPruefung() {
		this.belegpruefung.pruefe();
		this.belegpruefungsfehler = this.belegpruefung.getBelegungsfehler();
		this.belegpruefungErfolgreich = this.belegpruefung.istErfolgreich();
	}


	/**
	 * Markiert zuerst die Kurse und führt dann eine Prüfung der Zulassung durch
	 */
	private void zulassungsPruefung() {
		if (istBewertetQualifikationsPhase()) {
			this.ergebnisMarkierungsalgorithmus = markieren.berechne();
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
			case "10900" -> BeruflichesGymnasiumPruefungsordnungAnlage.D12;
			case "11100" -> BeruflichesGymnasiumPruefungsordnungAnlage.D9;
			case "11200" -> BeruflichesGymnasiumPruefungsordnungAnlage.D13;
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
			case "10600" -> BeruflichesGymnasiumPruefungsordnungAnlage.D25;
			case "10700" -> BeruflichesGymnasiumPruefungsordnungAnlage.D15;
			case "10900" -> BeruflichesGymnasiumPruefungsordnungAnlage.D19;
			case "11000" -> BeruflichesGymnasiumPruefungsordnungAnlage.D16;
			case "11100" -> BeruflichesGymnasiumPruefungsordnungAnlage.D17;
			case "11300" -> BeruflichesGymnasiumPruefungsordnungAnlage.D18;
			case "11400" -> BeruflichesGymnasiumPruefungsordnungAnlage.D20;
			case "11500" -> BeruflichesGymnasiumPruefungsordnungAnlage.D21;
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
	 * Getter für den Zugriff auf die FachID der zweiten Fremdsprache
	 *
	 * @return die FachID
	 */
	public Long getZweiteFremdspracheID() {
		return zweiteFremdspracheID;
	}


	/**
	 * liefert die Bezeichnung der zweiten Fremdsprache
	 *
	 * @return die Bezeichnung der zweiten Fremdsprache
	 */
	public String getZweiteFremdspracheBezeichnung() {
		return zweiteFremdspracheID == null ? null : faecherManager.getBezeichnungByFachID(zweiteFremdspracheID);
	}



	/**
	 * Getter für den Zugriff auf den Status der zweiten Fremdsprache
	 *
	 * @return ob die zweite Fremdsprache in der SI ausreichend belegt war.
	 */
	public boolean getZweiteFremdspracheInSekIErfuellt() {
		return zweiteFremdspracheInSekIErfuellt;
	}


	/**
	 * Getter für den Zugriff auf istFacharbeitLK
	 *
	 * @return ob ggfs. die Facharbeit einem LK-Fach zugeordnet ist
	 */
	public boolean getIstFacharbeitLK() {
		return istFacharbeitLK;
	}


	/**
	 * Prüft ob eine Facharbeit vorhanden ist
	 * Das Fach wird hier nicht einbezogen, sondern beim Markieren geprüft
	 *
	 * @return true, wenn Facharbeit vorhanden ist, sonst false
	 */
	public boolean istFacharbeitVorhanden() {
		final Integer notenpunkte = getAbidaten().facharbeitNotenpunkte;
		return (notenpunkte != null) && (notenpunkte > 0);
	}


	/**
	 * Prüft ob es sich um die Bezeichnung für das symbolische Wahlfach handelt.
	 * @param bezeichnung   eine Fachbezeichnung aus der Stundentafel
	 *
	 * @return true wenn es die Repräsentation für das Wahlfach ist, sonst false
	 */
	public boolean istWahlfach(final @NotNull String bezeichnung) {
		return bezeichnung.equals(WAHLFACH);
	}


	/**
	 * Prüft ob es sich um die Bezeichnung für das symbolische Fach Zweite Fremdsprache handelt.
	 * @param bezeichnung   eine Fachbezeichnung aus der Stundentafel
	 *
	 * @return true wenn es die Repräsentation für die zweite Fremdsprache ist, sonst false
	 */
	public boolean istZweiteFremdsprache(final @NotNull String bezeichnung) {
		return bezeichnung.equals(ZWEITE_FREMDSPRACHE);
	}


	/**
	 * Prüft ob es sich um die Bezeichnung für das symbolische Fach Neue Fremdsprache handelt.
	 * @param bezeichnung   eine Fachbezeichnung aus der Stundentafel
	 *
	 * @return true wenn es die Repräsentation für das Wahlfach ist, sonst false
	 */
	public boolean istNeueFremdsprache(final @NotNull String bezeichnung) {
		return bezeichnung.equals(NEUE_FREMDSPRACHE);
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
		belegPruefung();
		final @NotNull BKGymBelegpruefungErgebnis ergebnis = new BKGymBelegpruefungErgebnis();
		ergebnis.erfolgreich = belegpruefungErfolgreich;
		for (int i = 0; i < belegpruefungsfehler.size(); i++) {
			final @NotNull BKGymBelegungsfehler fehler = belegpruefungsfehler.get(i);
			ergebnis.fehlercodes.add(new BKGymBelegpruefungErgebnisFehler(fehler));
		}
		return ergebnis;
	}


	/**
	 * Gibt das Ergebnis des Markierungsalgorithmus zurück. Dieses enthält, ob der Algorithmus erfolgreich gewesen ist
	 * und im Fehlerfall den Log des Ergebnisses.
	 *
	 * @return das Ergebnis der Markierungsalgorithmus
	 */
	public @NotNull BKGymAbiturMarkierungsalgorithmusErgebnis getErgebnisMarkierungsalgorithmus() {
		zulassungsPruefung();
		if (this.ergebnisMarkierungsalgorithmus == null)
			return new BKGymAbiturMarkierungsalgorithmusErgebnis();
		return this.ergebnisMarkierungsalgorithmus;
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
	 * Liefert die FachID anhand der Fachbezeichnung zurück
	 *
	 * @param bezeichnung   das Fach
	 *
	 * @return die FachID oder null, wenn die Bezeichnung nicht existiert.
	 */
	public Long getFachIDByBezeichnung(@NotNull final String bezeichnung) {
		final BKGymAbiturFachbelegung fach = mapFachbelegungenByFachbezeichnung.get(bezeichnung);
		if (fach == null)
			return null;
		return fach.fachID;
	}


	/**
	 * liefert zu einer fachID die Fachbezeichnung
	 *
	 * @param id   die ID des Fachs
	 *
	 * @return die Fachbezeichnung
	 */
	public @NotNull String getBezeichnungByFachID(final long id) {
		return faecherManager.getBezeichnungByFachID(id);
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
	 * Gibt die FachID für das geforderte Abiturfach zurück.
	 *
	 * @param abiFach Das n. Abiturfach, das gewünscht ist
	 *
	 * @return die entsprechende FachID des Abiturfachs oder null wenn es nicht gefunden wird.
	 */
	public Long getAbiFachID(@NotNull final GostAbiturFach abiFach) {
		final BKGymAbiturFachbelegung abifach = getAbiFachbelegung(abiFach);
		if (abifach == null)
			return null;
		return abifach.fachID;
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
	public @NotNull String getFachkuerzelFromFachbelegung(final @NotNull BKGymAbiturFachbelegung fb) {
		// Prüfe, ob das Fach in der Fächerliste des Abiturjahrgangs überhaupt existiert
		final BKGymFach fbFach = faecherManager.get(fb.fachID);
		if ((fbFach == null) || (fbFach.kuerzelAnzeige == null))
			return "";
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
				if (tafelFach.fachbezeichnung.equals(ZWEITE_FREMDSPRACHE))
					return tafelFach;

		// Ggf. kann die Fachbelegung auch als Wahlfach gewertet werden.
		for (final BeruflichesGymnasiumStundentafelFach tafelFach : tafel.faecher)
			if (tafelFach.fachbezeichnung.equals(WAHLFACH))
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
	public boolean istGueltigeKursartFachbelegung(final @NotNull BeruflichesGymnasiumStundentafel tafel, final @NotNull BKGymAbiturFachbelegung fb,
			final @NotNull GostAbiturFach abifach) {
		// Prüfe zunächst, ob die Fachbelegung der Stundentafel zugeordnet werden kann
		final BeruflichesGymnasiumStundentafelFach tafelFach = getFachByBelegung(tafel, fb);
		if (tafelFach == null)
			return false;
		if (tafelFach.abifach == null)
			return true;
		return switch (abifach) {
			case LK1 -> (tafelFach.abifach == 1) && tafelFach.kursart.equals("LK");
			case LK2 -> (tafelFach.abifach == 2) && tafelFach.kursart.equals("LK");
			case AB3, AB4, AB5 -> istNtesAbifach(tafel.wahlmoeglichkeiten, abifach, tafelFach);
		};
	}


	/**
	 * Prüft ob das Fach in der Liste der Wahlmöglichkeiten für ein bestimmtes Abiturfach ist
	 *
	 * @param wahlmoeglichkeiten   die Wahlmöglichkeiten aus der Stundentafel
	 * @param abifach              welches Abiturfach
	 * @param tafelFach            das belegte Fach
	 *
	 * @return true, wenn es eine gültige Belegung ist.
	 */
	private static boolean istNtesAbifach(final @NotNull List<BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit> wahlmoeglichkeiten,
			final @NotNull GostAbiturFach abifach, final @NotNull BeruflichesGymnasiumStundentafelFach tafelFach) {
		for (final @NotNull BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit wm : wahlmoeglichkeiten) {
			final @NotNull List<String> abifaecher = switch (abifach) {
				case AB3 -> wm.abifach3;
				case AB4 -> wm.abifach4;
				case AB5 -> wm.abifach5;
				default -> new ArrayList<>();
			};
			for (final @NotNull String bezeichnung : abifaecher)
				if (bezeichnung.equals(tafelFach.fachbezeichnung))
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
	 * @param ab3   die Belegung des dritten Abiturfaches
	 * @param ab4   die Belegung des vierten Abiturfaches
	 *
	 * @return true, wenn sie gültig ist, und ansonsten false
	 */
	private boolean istGueltigeWahlmoeglichkeit(final @NotNull BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit wm,
			final @NotNull BKGymAbiturFachbelegung ab3, final @NotNull BKGymAbiturFachbelegung ab4) {
		final BKGymFach ab3Fach = faecherManager.get(ab3.fachID);
		final BKGymFach ab4Fach = faecherManager.get(ab4.fachID);
		if ((ab3Fach == null) || (ab4Fach == null))
			return false;

		// Prüfe zunächst, ob das dritte Fach der Wahlmöglichkeit entspricht
		String wm3 = null;
		for (final @NotNull String fachBez3 : wm.abifach3)
			if (fachBez3.equals(ab3Fach.bezeichnung) || (ZWEITE_FREMDSPRACHE.equals(fachBez3) && ab3Fach.istFremdsprache) || WAHLFACH.equals(fachBez3))
				wm3 = fachBez3;
		if (wm3 == null)
			return false;

		// Prüfe danach, ob auch das vierte Fach der Wahlmöglichkeit entspricht
		for (final @NotNull String fachBez4 : wm.abifach4)
			if (fachBez4.equals(ab4Fach.bezeichnung) || (ZWEITE_FREMDSPRACHE.equals(fachBez4) && ab4Fach.istFremdsprache) || WAHLFACH.equals(fachBez4))
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
			if (istGueltigeWahlmoeglichkeit(wm, ab3, ab4))
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
	public @NotNull Map<Integer, List<BeruflichesGymnasiumStundentafelFach>> getMapFaecherFromTafelByIndex(
			final @NotNull BeruflichesGymnasiumStundentafel tafel) {
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
	public @NotNull Map<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>> getMapBelegungenForTafelByFach(
			final @NotNull BeruflichesGymnasiumStundentafel tafel) {
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


	/**
	 * Gibt zurück, ob das angegebene Halbjahr bereits bewertet ist oder nicht.
	 *
	 * @param halbjahr   das Halbjahr
	 *
	 * @return true, falls es bereits bewertet ist
	 */
	public boolean istBewertet(final @NotNull GostHalbjahr halbjahr) {
		return abidaten.bewertetesHalbjahr[halbjahr.id];
	}


	/**
	 * Gibt zurück, ob alle Halbjahr der Qualifikationsphase bewertet sind oder nicht.
	 *
	 * @return true, falls alle Halbjahre bewertet sind, und ansonsten false
	 */
	public boolean istBewertetQualifikationsPhase() {
		for (final @NotNull GostHalbjahr hj : GostHalbjahr.getQualifikationsphase())
			if (!istBewertet(hj))
				return false;
		return true;
	}


	/**
	 * Liefert die FachID der zweiten Fremdsprache oder null, falls nicht vorhanden
	 *
	 * @return die ID der zweiten Fremdsprache oder null
	 */
	private Long ermittleZweiteFremdspracheID() {
		// Durchwandere alle belegten Fächer und schaue nach Fremdsprache
		for (final Entry<String, BKGymAbiturFachbelegung> entry : mapFachbelegungenByFachbezeichnung.entrySet()) {
			final BKGymFach fach = faecherManager.get(entry.getValue().fachID);
			if ((fach != null) && fach.istFremdsprache && !fach.bezeichnung.equals("Englisch"))
				return fach.id;
		}
		return null;
	}


	/**
	 * Ermittelt, ob in der SekI eine zweite Fremdsprache über vier Jahre belegt wurde anhand der Sprachdaten in
	 * den AbiDaten.
	 *
	 * @return true, wenn die Belegung einer zweiten Fremdsprache nicht ununterbrochen über vier Jahre belegt war.
	 */
	private boolean istZweiteFremdspracheInSekIErfuellt() {
		for (final @NotNull Sprachbelegung belegung : abidaten.sprachendaten.belegungen) {
			if ((belegung.reihenfolge == null) || (belegung.belegungVonJahrgang == null) || (belegung.belegungBisJahrgang == null)
					|| (belegung.belegungVonAbschnitt == null) || (belegung.belegungBisAbschnitt == null))
				continue;
			if (!belegung.sprache.equals("E")) {
				int anzHalbjahre = (SprachendatenUtils.getJahrgangNumerisch(belegung.belegungBisJahrgang)
						- SprachendatenUtils.getJahrgangNumerisch(belegung.belegungBisJahrgang) + 1) * 2;
				anzHalbjahre += belegung.belegungBisAbschnitt - belegung.belegungVonAbschnitt - 1;
				if (anzHalbjahre >= 8)
					return true;
			}
		}
		return false;
	}


	/**
	 * Delegation für die doppelten Fächer als List
	 *
	 * @return die Liste der doppelten Fächer
	 */
	public @NotNull List<String> getDoppelteFaecher() {
		return faecherManager.getDoppelteFaecher();
	}


	/**
	 * Gibt zurück, ob es sich bei der Halbjahresbelegung um eine Belegung handelt, welche mit
	 * null Punkten abgeschlossen wurde und welche daher als nicht belegter Kurs zu werten ist.
	 *
	 * @param halbjahresbelegung   die Halbjahresbelegung eines Kurses
	 *
	 * @return true, fall es sich um einen Null-Punkte-Kurs in der Qualifikationsphase handelt.
	 */
	public static boolean istNullPunkteBelegungInQPhase(final @NotNull BKGymAbiturFachbelegungHalbjahr halbjahresbelegung) {
		final GostHalbjahr hj = GostHalbjahr.fromKuerzel(halbjahresbelegung.halbjahrKuerzel);
		if ((hj == null) || (hj.istEinfuehrungsphase()))
			return false;
		return Note.fromKuerzel(halbjahresbelegung.notenkuerzel) == Note.UNGENUEGEND;
	}


	/**
	 * Prüft, ob das Fach in allen angegebenen Halbjahren belegt wurde.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachbelegung
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls das Fach in den Halbjahren belegt wurde, sonst false
	 */
	public boolean pruefeBelegung(final BKGymAbiturFachbelegung fachbelegung, final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if (halbjahre.length == 0)
			return true;
		for (final GostHalbjahr halbjahr : halbjahre) {
			final BKGymAbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr == null) || (belegungHalbjahr.kursartKuerzel == null))
				return false;
			if (istNullPunkteBelegungInQPhase(belegungHalbjahr))
				return false;
		}
		return true;
	}
}
