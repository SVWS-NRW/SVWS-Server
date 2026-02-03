package de.svws_nrw.core.abschluss.bk.d;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafel;
import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafelFach;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturFachbelegung;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturdaten;
import de.svws_nrw.core.data.bk.abi.BKGymFach;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.bk.BKGymFaecherManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse verwaltet die Fachbelegungen eines Schülers im beruflichen Gymnasium
 */
public class BKGymFachbelegungManager {

	/** Der Abiturdaten-Manager */
	private final @NotNull BKGymAbiturdatenManager abidatenManager;

	/** Der Fächer-Manager */
	private final @NotNull BKGymFaecherManager faecherManager;

	/** FachID der zweiten Fremdsprache */
	private final Long zweiteFremdspracheID;

	/** Ob das Fach der Facharbeit ein LK ist */
	private final boolean istFacharbeitLK;

	// Datenstrukturen zum schnellen Zugriff auf Fachbelegungen

	/** Eine HashMap, welche den schnellen Zugriff auf die Fachbelegungen für ein Fach anhand der Bezeichnung ermöglicht */
	private final @NotNull Map<String, BKGymAbiturFachbelegung> mapFachbelegungenByFachbezeichnung = new HashMap<>();

	/** Eine Map, welche von der Nummer des Abiturfaches auf die Fachbelegung der Abiturdaten verweist.*/
	private final @NotNull Map<Integer, BKGymAbiturFachbelegung> mapAbiturfachbelegungen = new HashMap<>();

	/** Eine HashMap2D für den schnelle Zugriff auf die Halbjahresbelegungen anhand des Halbjahres und der Fachbezeichnung */
	private final @NotNull HashMap2D<@NotNull String, @NotNull Integer, @NotNull BKGymAbiturFachbelegungHalbjahr>
		mapBelegungByHalbjahrAndFachbezeichung = new HashMap2D<>();


	/**
	 * Erstellt einen neuen Fachbelegungs-Manager für die Fachbelegungen eines Schülers im beruflichen Gymnasium
	 *
	 * @param abidatenManager   der Manager für die Abiturdaten
	 */
	public BKGymFachbelegungManager(final @NotNull BKGymAbiturdatenManager abidatenManager) {
		this.abidatenManager = abidatenManager;
		this.faecherManager = abidatenManager.getFaecherManager();
		init();
		zweiteFremdspracheID = ermittleZweiteFremdspracheID();
		this.istFacharbeitLK = pruefeIstFacharbeitLK();
	}


	/**
	 * Initialisiert den Fachbelegungs-Manager.
	 */
	private void init() {
		final @NotNull BKGymAbiturdaten abidaten = abidatenManager.getAbidaten();
		// Leere die HashMaps und erstelle ggf. neue Listen für die Zuordnung von Abitur-Fachbelegungen
		mapAbiturfachbelegungen.clear();
		mapFachbelegungenByFachbezeichnung.clear();
		for (final @NotNull BKGymAbiturFachbelegung fb : abidaten.fachbelegungen) {
			final @NotNull String fachbezeichnung = faecherManager.getBezeichnungByFachID(fb.fachID);
			// Ordne die Fachbelegungen ihren Bezeichnungen zu.
			final BKGymFach fach = faecherManager.get(fb.fachID);
			if ((fach == null) || (fach.bezeichnung == null))
				continue;
			mapFachbelegungenByFachbezeichnung.put(fach.bezeichnung, fb);
			if (fb.abiturFach != null)
				mapAbiturfachbelegungen.put(fb.abiturFach, fb);
			for (final GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
				final BKGymAbiturFachbelegungHalbjahr belegungHalbjahr = fb.belegungen[halbjahr.id];
				if ((belegungHalbjahr != null) && (belegungHalbjahr.kursartKuerzel != null))
					mapBelegungByHalbjahrAndFachbezeichung.put(fachbezeichnung, halbjahr.id, belegungHalbjahr);
			}
		}
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
	 * Liefert die FachID der zweiten Fremdsprache oder null, falls nicht vorhanden
	 *
	 * @return die ID der zweiten Fremdsprache oder null
	 */
	public Long ermittleZweiteFremdspracheID() {
		// Durchwandere alle belegten Fächer und schaue nach Fremdsprache
		for (final Entry<String, BKGymAbiturFachbelegung> entry : mapFachbelegungenByFachbezeichnung.entrySet()) {
			final BKGymFach fach = faecherManager.get(entry.getValue().fachID);
			if ((fach != null) && fach.istFremdsprache && !fach.bezeichnung.equals("Englisch"))
				return entry.getValue().fachID;
		}
		return null;
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
	 * Prüft, ob die übergebene Fachbelegung als Fach in der Stundentafel vorkommt bzw. vorkommen kann.
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
				if (BKGymStundentafelManager.istZweiteFremdsprache(tafelFach.fachbezeichnung))
					return tafelFach;

		// Ggf. kann die Fachbelegung auch als Wahlfach gewertet werden.
		for (final BeruflichesGymnasiumStundentafelFach tafelFach : tafel.faecher)
			if (BKGymStundentafelManager.istWahlfach(tafelFach.fachbezeichnung))
				return tafelFach;

		return null;
	}


	/**
	 * Prüft, ob es sich bei der zweiten Fremdsprache um eine neu einsetzende Fremdsprache handelt.
	 *
	 * @return true, wenn es sich um eine neu einsetzende Fremdsprachenbelegung handelt, und ansonsten false
	 */
	public boolean istZweiteFremdspracheNeuEinsetzend() {
		// Wenn zweite Fremdsprache in SEK I erlernt wurde, dann ist diese nicht neu einsetzend
		if (abidatenManager.getZweiteFremdspracheInSekIErfuellt())
			return false;
		// Prüfe, ob das Fach in der Fächerliste des Abiturjahrgangs überhaupt existiert
		if (zweiteFremdspracheID == null)
			return false;
		final BKGymFach fbFach = faecherManager.get(zweiteFremdspracheID);
		if ((fbFach == null) || (fbFach.bezeichnung == null))
			return false;
		final BKGymAbiturFachbelegung fb = getFachbelegungByBezeichnung(fbFach.bezeichnung);
		return fbFach.istFremdSpracheNeuEinsetzend || (fb != null && fb.istFSNeu);
	}


	/**
	 * Prüft, ob die übergebene Fachbezeichnung der zweiten Fremdsprache entspricht.
	 *
	 * @param fachBezeichnung   die Fachbezeichnung
	 *
	 * @return true, wenn die Fachbezeichnung der zweiten Fremdsprache entspricht, sonst false
	 */
	public boolean istZweiteFremdsprache(@NotNull final String fachBezeichnung) {
		final String zweiteFremdspracheBezeichnung = getZweiteFremdspracheBezeichnung();
		if (zweiteFremdspracheBezeichnung == null)
			return false;
		return zweiteFremdspracheBezeichnung.equals(fachBezeichnung);
	}


	/**
	 * Ermittelt ob die Facharbeit einem LK-Fach zugeordnet ist.
	 * Wird dann auf false gesetzt, wenn eine Facharbeit vorhanden ist und die Fachbezeichnung
	 * für die Facharbeit nicht dem LK1 oder LK2 zugeordnet werden kann.
	 *
	 * @return false wenn Facharbeit vorhanden und nicht einem LK zugeordnet sonst true
	 */
	private boolean pruefeIstFacharbeitLK() {
		if (abidatenManager.getAbidaten().facharbeitFachbezeichnung == null)
			return true;
		final String fachbezeichnung = abidatenManager.getAbidaten().facharbeitFachbezeichnung;
		final Long facharbeitFachID = fachbezeichnung == null ? null : getFachIDByBezeichnung(fachbezeichnung);
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
	 * Getter für den Zugriff auf istFacharbeitLK
	 *
	 * @return ob ggfs. die Facharbeit einem LK-Fach zugeordnet ist
	 */
	public boolean getIstFacharbeitLK() {
		return istFacharbeitLK;
	}


	/**
	 * Prüft, ob das Fach in allen angegebenen Halbjahren belegt wurde.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung   die zu prüfende Fachbelegung
	 * @param leereHje       die ggfs.nicht gewerteten Halbjahre
	 * @param halbjahre      die zu prüfenden Halbjahre
	 *
	 * @return true, falls das Fach in den Halbjahren belegt wurde, sonst false
	 */
	public boolean pruefeBelegung(final BKGymAbiturFachbelegung fachbelegung, final @NotNull List<GostHalbjahr> leereHje, final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if (halbjahre.length == 0)
			return true;
		for (final GostHalbjahr halbjahr : halbjahre) {
			if (leereHje.contains(halbjahr))
				continue;
			final BKGymAbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr == null) || (belegungHalbjahr.kursartKuerzel == null))
				return false;
			if (istNullPunkteBelegungInQPhase(belegungHalbjahr))
				return false;
		}
		return true;
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
	 * Erstellt einen neuen Fachbelegung-zu-Stundentafelfach-Manager
	 *
	 * @param maxFachposition   die maximale Fachposition in der Stundentafel
	 *
	 * @return der neue Manager
	 */
	public @NotNull BKGymFachbelegungZuStundentafelfachManager newFachbelegungZuStundentafelfachManager(final int maxFachposition) {
		return new BKGymFachbelegungZuStundentafelfachManager(this, new HashMap2D<>(mapBelegungByHalbjahrAndFachbezeichung), abidatenManager, maxFachposition);
	}
}
