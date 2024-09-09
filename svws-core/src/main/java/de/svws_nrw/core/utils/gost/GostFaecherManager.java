package de.svws_nrw.core.utils.gost;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostJahrgangFachkombination;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.asd.types.fach.Fachgruppe;
import de.svws_nrw.asd.data.fach.FachKatalogEintrag;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostLaufbahnplanungFachkombinationTyp;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klassen bietet Hilfsmethoden für den Zugriff auf Daten des
 * Typs {@link GostFach}.
 */
public class GostFaecherManager {

	/** Sortiert die Fächer anhand ihrer konfigurierten Sortierung */
	public static final @NotNull Comparator<@AllowNull GostFach> comp = (a, b) -> GostFachbereich.compareGostFach(a, b);

	/** das Schuljahr, für welches der Fächer-Manager die Fächer verwaltet */
	private final int schuljahr;

	/** Die Liste der Fächer, die im Manager vorhanden sind. */
	private final @NotNull List<GostFach> _faecher = new ArrayList<>();

	/** Eine HashMap für den schnellen Zugriff auf ein Fach anhand der ID */
	private final @NotNull HashMap<Long, GostFach> _map = new HashMap<>();

	/** Eine HashMap für den schnellen Zugriff auf die Fächer anhand des Statistik-Kürzels des Faches */
	private final @NotNull HashMap<String, List<GostFach>> _mapByKuerzel = new HashMap<>();

	/** Eine HashMap für den schnellen Zugriff auf die Fremdsprachen-Fächer anhand des Sprachenkürzels */
	private final @NotNull HashMap<String, List<GostFach>> _mapBySprachkuerzel = new HashMap<>();

	/** Eine Map für den schnellen Zugriff auf die Fächer, welche als Leitfächer zur Verfügung stehen. */
	private final @NotNull List<GostFach> _leitfaecher = new ArrayList<>();

	/** Die Liste der erforderlichen oder nicht erlaubten Fachkombinationen */
	private final @NotNull List<GostJahrgangFachkombination> _fachkombis = new ArrayList<>();

	/** Die Liste mit den geforderten Fachkombinationen */
	private final @NotNull List<GostJahrgangFachkombination> _fachkombisErforderlich = new ArrayList<>();

	/** Die Liste mit den nicht erlaubten Fachkombinationen */
	private final @NotNull List<GostJahrgangFachkombination> _fachkombisVerboten = new ArrayList<>();


	/**
	 * Erstelle einen neuen Manager mit einer leeren Fächerliste
	 *
	 * @param schuljahr    das Schuljahr, für welches der Fächer-Manager die Fächer verwaltet
	 */
	public GostFaecherManager(final int schuljahr) {
		this.schuljahr = schuljahr;
	}


	/**
	 * Erstellt einen neuen Manager mit den übergebenen Fächern.
	 *
	 * @param schuljahr    das Schuljahr, für welches der Fächer-Manager die Fächer verwaltet
	 * @param faecher   die Liste mit den Fächern
	 */
	public GostFaecherManager(final int schuljahr, final @NotNull List<GostFach> faecher) {
		this.schuljahr = schuljahr;
		addAll(faecher);
	}


	/**
	 * Erstellt einen neuen Manager mit den übergebenen Fächern und den
	 * übergebenen geforderten und nicht erlaubten Fächerkombinationen.
	 *
	 * @param schuljahr    das Schuljahr, für welches der Fächer-Manager die Fächer verwaltet
	 * @param faecher      die Liste mit den Fächern
	 * @param fachkombis   die Liste mit den Fächerkombinationen
	 */
	public GostFaecherManager(final int schuljahr, final @NotNull List<GostFach> faecher, final @NotNull List<GostJahrgangFachkombination> fachkombis) {
		this.schuljahr = schuljahr;
		addAll(faecher);
		addFachkombinationenAll(fachkombis);
	}


	/**
	 * Gibt das Schuljahr des Managers zurück, d.h. das Schuljahr, für welches die Fächer der Oberstufe verwaltet werden.
	 *
	 * @return das Schuljahr
	 */
	public int getSchuljahr() {
		return schuljahr;
	}


	/**
	 * Fügt das übergebene Fach zu diesem Manager hinzu. Die interne Sortierung wird nicht korrigiert.
	 *
	 * @param fach        das hinzuzufügende Fach
	 *
	 * @return true, falls das Fach hinzugefügt wurde
	 *
	 * @throws DeveloperNotificationException Falls die ID des Faches negativ ist.
	 */
	private boolean addFachInternal(final @NotNull GostFach fach) throws DeveloperNotificationException {
		// Füge das Fach hinzu, wenn es nicht bereits vorhanden ist und gültig ist...
		DeveloperNotificationException.ifSmaller("fach.id", fach.id, 0);
		if (_map.containsKey(fach.id))
			return false;
		final Fach zf = Fach.data().getWertBySchluessel(fach.kuerzel);
		if (zf == null)
			return false;
		final FachKatalogEintrag fke = zf.daten(schuljahr);
		if (fke == null)
			return false;
		_map.put(fach.id, fach);
		List<GostFach> listForKuerzel = _mapByKuerzel.get(fach.kuerzel);
		if (listForKuerzel == null) {
			listForKuerzel = new ArrayList<>();
			_mapByKuerzel.put(fach.kuerzel, listForKuerzel);
		}
		listForKuerzel.add(fach);
		if (fach.istFremdsprache && fke.istFremdsprache) {
			List<GostFach> listForSprachkuerzel = _mapBySprachkuerzel.get(fke.kuerzel);
			if (listForSprachkuerzel == null) {
				listForSprachkuerzel = new ArrayList<>();
				_mapBySprachkuerzel.put(fke.kuerzel, listForSprachkuerzel);
			}
			listForSprachkuerzel.add(fach);
		}
		final boolean added = _faecher.add(fach);
		// Prüfe, ob das Fach als Leitfach geeignet ist, d.h. kein Vertiefungs-, Projekt- oder Ersatzfach ist
		if (!GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach)) {
			final Fachgruppe fg = Fach.data().getWertBySchluesselOrException(fach.kuerzel).getFachgruppe(schuljahr);
			if ((fg != Fachgruppe.FG_VX) && (fg != Fachgruppe.FG_PX))
				_leitfaecher.add(fach);
		}
		return added;
	}


	/**
	 * Führt eine Sortierung der Fächer anhand des Sortierungsfeldes durch.
	 */
	private void sort() {
		_faecher.sort(comp);
		_leitfaecher.sort(comp);
	}


	/**
	 * Fügt die übergebene Fachkombination zu diesem Manager hinzu.
	 *
	 * @param fachkombi   die hinzuzufügende Fachkombination
	 *
	 * @return true, falls die Fachkombination hinzugefügt wurde
	 *
	 * @throws DeveloperNotificationException Falls die Fachkombination nicht zu den Fächern des Managers passt.
	 */
	private boolean addFachkombinationInternal(final @NotNull GostJahrgangFachkombination fachkombi) throws DeveloperNotificationException {
		// Füge die Fachkombination hinzu, falls sie noch nicht enthalten ist...
		DeveloperNotificationException.ifSmaller("fachkombi.fachID1", fachkombi.fachID1, 0);
		DeveloperNotificationException.ifSmaller("fachkombi.fachID2", fachkombi.fachID2, 0);
		DeveloperNotificationException.ifMapNotContains("_map", _map, fachkombi.fachID1);
		DeveloperNotificationException.ifMapNotContains("_map", _map, fachkombi.fachID2);
		DeveloperNotificationException.ifNotInRange("fachkombi.typ", fachkombi.typ, 0, 1);
		final @NotNull GostLaufbahnplanungFachkombinationTyp typ = GostLaufbahnplanungFachkombinationTyp.fromValue(fachkombi.typ);
		if (fachkombi.hinweistext.isBlank()) {
			final @NotNull GostFach fach1 = getOrException(fachkombi.fachID1);
			final @NotNull GostFach fach2 = getOrException(fachkombi.fachID2);
			final @NotNull String kursart1 = ((fachkombi.kursart1 == null) || fachkombi.kursart1.isBlank()) ? "" : (" als " + fachkombi.kursart1);
			final @NotNull String kursart2 = ((fachkombi.kursart2 == null) || fachkombi.kursart2.isBlank()) ? "" : (" als " + fachkombi.kursart2);
			fachkombi.hinweistext = fach1.kuerzelAnzeige + kursart1
					+ ((typ == GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH) ? " erfordert " : " erlaubt kein ")
					+ fach2.kuerzelAnzeige + kursart2;
		}
		if (typ == GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH) {
			_fachkombisErforderlich.add(fachkombi);
		} else if (typ == GostLaufbahnplanungFachkombinationTyp.VERBOTEN) {
			_fachkombisVerboten.add(fachkombi);
		}
		return _fachkombis.add(fachkombi);
	}


	/**
	 * Fügt das übergebene Fach zu diesem Manager hinzu und
	 * passt intern die Sortierung der Fächer an.
	 *
	 * @param fach   das hinzuzufügende Fach
	 *
	 * @return true, falls das Fach hinzugefügt wurde
	 */
	public boolean add(final @NotNull GostFach fach) {
		final boolean result = addFachInternal(fach);
		sort();
		return result;
	}


	/**
	 * Fügt die Fächer in der übergeben Liste zu diesem Manager hinzu.
	 *
	 * @param faecher   die hinzuzufügenden Fächer
	 *
	 * @return true, falls <i>alle</i> Fächer eingefügt wurden, sonst false
	 */
	public boolean addAll(final @NotNull Collection<GostFach> faecher) {
		boolean result = true;
		for (final @NotNull GostFach fach : faecher)
			if (!addFachInternal(fach))
				result = false;
		sort();
		return result;
	}


	/**
	 * Fügt die geforderten oder nicht erlaubte Fächerkombination zu diesem
	 * Manager hinzu.
	 *
	 * @param fachkombi   das hinzuzufügende Fachkombination
	 *
	 * @return true, falls die Fachkombination hinzugefügt wurde
	 */
	public boolean add(final @NotNull GostJahrgangFachkombination fachkombi) {
		return addFachkombinationInternal(fachkombi);
	}


	/**
	 * Fügt die geforderten und nicht erlaubten Fächerkombinationen in der übergebenen
	 * Liste zu diesem Manager hinzu.
	 *
	 * @param fachkombis   die hinzuzufügenden Fachkombinationen
	 *
	 * @return true, falls <i>alle</i> Fachkombinationen eingefügt wurden, sonst false
	 */
	public boolean addFachkombinationenAll(final @NotNull List<GostJahrgangFachkombination> fachkombis) {
		boolean result = true;
		for (final @NotNull GostJahrgangFachkombination fachkombi : fachkombis)
			if (!addFachkombinationInternal(fachkombi))
				result = false;
		return result;
	}


	/**
	 * Gibt das Fach mit der angegebenen ID zurück oder null, falls es das Fach nicht gibt.
	 *
	 * @param id   die ID des gesuchten Faches
	 *
	 * @return Das fach mit der angegebenen ID oder null, falls es das Fach nicht gibt.
	 */
	public GostFach get(final long id) {
		return _map.get(id);
	}


	/**
	 * Liefert das Fach mit der angegebenen ID zurück.
	 *
	 * @param idFach   die ID des gesuchten Faches.
	 *
	 * @return Das Fach mit der angegebenen ID zurück.
	 *
	 * @throws DeveloperNotificationException Falls ein Fach mit der ID nicht bekannt ist.
	 */
	public @NotNull GostFach getOrException(final long idFach) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_map, idFach);
	}


	/**
	 * Liefert die Liste der Fächer für das angegebene Statistik-Kürzel zurück.
	 *
	 * @param kuerzel   das Statistik-Kürzel des gesuchten Faches
	 *
	 * @return eine Liste der Fächer, welche das angegebene Statistik-Kürzel haben
	 */
	public @NotNull List<GostFach> getByKuerzel(final @NotNull String kuerzel) {
		final List<GostFach> faecher = _mapByKuerzel.get(kuerzel);
		return (faecher == null) ? new ArrayList<>() : faecher;
	}


	/**
	 * Liefert die Liste der Fächer für das angegebene Sprachkürzel zurück.
	 *
	 * @param sprache   das Sprachkürzel der gesuchten Sprache
	 *
	 * @return eine Liste der Fächer, welche das angegebene Sprachkürzel haben
	 */
	public @NotNull List<GostFach> getBySprachkuerzel(final @NotNull String sprache) {
		final List<GostFach> faecher = _mapBySprachkuerzel.get(sprache);
		return (faecher == null) ? new ArrayList<>() : faecher;
	}


	/**
	 * Gibt zurück, ob die Liste der Fächer leer ist
	 *
	 * @return true, wenn die Liste der Fächer leer ist.
	 */
	public boolean isEmpty() {
		return _faecher.isEmpty();
	}


	/**
	 * Liefert die interne Liste der Fächer. Diese sollte nicht
	 * verändert werden.
	 *
	 * @return die interne Liste der Fächer
	 */
	public @NotNull List<GostFach> faecher() {
		return new ArrayList<>(_faecher);
	}

	/**
	 * Liefert die Liste der Fächer, die nur die schriftlich möglichen Fächer enthält.
	 *
	 * @return die Liste der schriftlich möglichen Fächer
	 */
	public @NotNull List<GostFach> getFaecherSchriftlichMoeglich() {
		final @NotNull List<GostFach> faecherSchriftlichMoeglich = new ArrayList<>();
		for (final @NotNull GostFach f : _faecher) {
			final Fach zf = Fach.data().getWertBySchluesselOrException(f.kuerzel);
			if ((zf == Fach.PX) || (zf == Fach.VX) || (zf == Fach.VO) || (zf == Fach.IN))
				continue;
			faecherSchriftlichMoeglich.add(f);
		}
		return faecherSchriftlichMoeglich;
	}

	/**
	 * Liefert die interne Liste mit den Leitfächern zurück.
	 *
	 * @return die interne Liste mit den Leitfächern
	 */
	public @NotNull List<GostFach> getLeitfaecher() {
		return _leitfaecher;
	}


	/**
	 * Gibt eine Liste aller Fremdsprachen-Kürzel zurück, welche bei
	 * den im Manager enthaltenen Fächer definiert sind.
	 *
	 * @return die Liste der Fremdsprachen-Kürzel
	 */
	public @NotNull List<String> getFremdsprachenkuerzel() {
		final @NotNull List<String> result = new ArrayList<>();
		result.addAll(_mapBySprachkuerzel.keySet());
		result.sort((final @NotNull String a, final @NotNull String b) -> a.compareToIgnoreCase(b));
		return result;
	}


	/**
	 * Liefert die interne Liste mit den Fachkombinationen zurück.
	 *
	 * @return die interne Liste mit den Fachkombinationen
	 */
	public @NotNull List<GostJahrgangFachkombination> getFachkombinationen() {
		return this._fachkombis;
	}

	/**
	 * Liefert die interne Liste mit den geforderten Fachkombinationen zurück.
	 *
	 * @return die interne Liste mit den geforderten Fachkombinationen
	 */
	public @NotNull List<GostJahrgangFachkombination> getFachkombinationenErforderlich() {
		return this._fachkombisErforderlich;
	}

	/**
	 * Liefert die interne Liste mit den nicht erlaubten Fachkombinationen zurück.
	 *
	 * @return die interne Liste mit den nicht erlaubten Fachkombinationen
	 */
	public @NotNull List<GostJahrgangFachkombination> getFachkombinationenVerboten() {
		return this._fachkombisVerboten;
	}


	/**
	 * Gibt an, ob es sich bei dem Fach mit der übergebenen ID um ein Projektkursfach handelt oder nicht.
	 *
	 * @param id   die ID des Faches
	 *
	 * @return true, wenn es sich um ein Projektkurs-Fach handelt und ansonsten false.
	 */
	public boolean fachIstProjektkurs(final long id) {
		final GostFach fach = _map.get(id);
		if (fach == null)
			return false;
		return "PX".equals(fach.kuerzel);
	}


	/**
	 * Gibt an, ob es sich bei dem Fach mit der übergebenen ID um einen Vertiefungskurs handelt oder nicht.
	 *
	 * @param id   die ID des Faches
	 *
	 * @return true, wenn es sich um einen Vertiefungskurs handelt und ansonsten false.
	 */
	public boolean fachIstVertiefungskurs(final long id) {
		final GostFach fach = _map.get(id);
		if (fach == null)
			return false;
		return "VX".equals(fach.kuerzel);
	}

}
