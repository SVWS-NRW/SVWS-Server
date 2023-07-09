package de.svws_nrw.core.utils.gost;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostJahrgangFachkombination;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.fach.Fachgruppe;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostLaufbahnplanungFachkombinationTyp;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klassen bietet Hilfsmethoden für den Zugriff auf Daten des
 * Typs {@link GostFach}.
 */
public class GostFaecherManager {

	/** Sortiert die Fächer anhand ihrer konfigurierten Sortierung */
	public static final @NotNull Comparator<@NotNull GostFach> comp = (a, b) -> {
		final int va = (a == null) ? Integer.MIN_VALUE : a.sortierung;
		final int vb = (b == null) ? Integer.MIN_VALUE : b.sortierung;
		return Integer.compare(va, vb);
	};

	/** Die Liste der Fächer, die im Manager vorhanden sind. */
	private final @NotNull List<@NotNull GostFach> _faecher = new ArrayList<>();

	/** Eine HashMap für den schnellen Zugriff auf ein Fach anhand der ID */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostFach> _map = new HashMap<>();

	/** Eine HashMap für den schnellen Zugriff auf die Fächer anhand des Statistik-Kürzels des Faches */
	private final @NotNull HashMap<@NotNull String, @NotNull List<@NotNull GostFach>> _mapByKuerzel = new HashMap<>();

	/** Eine HashMap für den schnellen Zugriff auf die Fremdsprachen-Fächer anhand des Sprachenkürzels */
	private final @NotNull HashMap<@NotNull String, @NotNull List<@NotNull GostFach>> _mapBySprachkuerzel = new HashMap<>();

	/** Eine Map für den schnellen Zugriff auf die Fächer, welche als Leitfächer zur Verfügung stehen. */
	private final @NotNull List<@NotNull GostFach> _leitfaecher = new ArrayList<>();

	/** Die Liste der erforderlichen oder nicht erlaubten Fachkombinationen */
	private final @NotNull List<@NotNull GostJahrgangFachkombination> _fachkombis = new ArrayList<>();

	/** Die Liste mit den geforderten Fachkombinationen */
	private final @NotNull List<@NotNull GostJahrgangFachkombination> _fachkombisErforderlich = new ArrayList<>();

	/** Die Liste mit den nicht erlaubten Fachkombinationen */
	private final @NotNull List<@NotNull GostJahrgangFachkombination> _fachkombisVerboten = new ArrayList<>();


	/**
	 * Erstelle einen neuen Manager mit einer leeren Fächerliste
	 */
	public GostFaecherManager() {
		// do nothing...
	}


	/**
	 * Erstellt einen neuen Manager mit den übergebenen Fächern.
	 *
	 * @param faecher   die Liste mit den Fächern
	 */
	public GostFaecherManager(final @NotNull List<@NotNull GostFach> faecher) {
		addAll(faecher);
	}


	/**
	 * Erstellt einen neuen Manager mit den übergebenen Fächern und den
	 * übergebenen geforderten und nicht erlaubten Fächerkombinationen.
	 *
	 * @param faecher      die Liste mit den Fächern
	 * @param fachkombis   die Liste mit den Fächerkombinationen
	 */
	public GostFaecherManager(final @NotNull List<@NotNull GostFach> faecher, final @NotNull List<@NotNull GostJahrgangFachkombination> fachkombis) {
		addAll(faecher);
		addFachkombinationenAll(fachkombis);
	}


	/**
	 * Fügt das übergebene Fach zu diesem Manager hinzu. Die interne Sortierung wird nicht korrigiert.
	 *
	 * @param fach   das hinzuzufügende Fach
	 *
	 * @return true, falls das Fach hinzugefügt wurde
	 *
	 * @throws DeveloperNotificationException Falls die ID des Faches negativ ist.
	 */
	private boolean addFachInternal(final @NotNull GostFach fach) throws DeveloperNotificationException {
		// Füge das Fach hinzu, wenn es nicht bereits vorhanden ist...
		DeveloperNotificationException.ifSmaller("fach.id", fach.id, 0);
		if (_map.containsKey(fach.id))
			return false;
		_map.put(fach.id, fach);
		final @NotNull ZulaessigesFach zf = ZulaessigesFach.getByKuerzelASD(fach.kuerzel);
		List<@NotNull GostFach> listForKuerzel = _mapByKuerzel.get(fach.kuerzel);
		if (listForKuerzel == null) {
			listForKuerzel = new ArrayList<>();
			_mapByKuerzel.put(fach.kuerzel, listForKuerzel);
		}
		listForKuerzel.add(fach);
		if (fach.istFremdsprache) {
			List<@NotNull GostFach> listForSprachkuerzel = _mapBySprachkuerzel.get(zf.daten.kuerzel);
			if (listForSprachkuerzel == null) {
				listForSprachkuerzel = new ArrayList<>();
				_mapBySprachkuerzel.put(zf.daten.kuerzel, listForSprachkuerzel);
			}
			listForSprachkuerzel.add(fach);
		}
		final boolean added = _faecher.add(fach);
		// Prüfe, ob das Fach als Leitfach geeignet ist, d.h. kein Vertiefungs-, Projekt- oder Ersatzfach ist
		if (!GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach)) {
			final Fachgruppe fg = ZulaessigesFach.getByKuerzelASD(fach.kuerzel).getFachgruppe();
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
	 * @param fachkombi   die hinzuzufügende Fachkombinationen
	 *
	 * @return true, falls die Fachkombination hinzugefügt wurde
	 *
	 * @throws DeveloperNotificationException Falls die Fachkombination nicht zu den Fächern des Managers passt.
	 */
	private boolean addFachkombinationInternal(final @NotNull GostJahrgangFachkombination fachkombi) throws DeveloperNotificationException {
		// Füge ddie Fachkombination hinzu, sie noch nicht enthalten ist...
		DeveloperNotificationException.ifSmaller("fachkombi.fachID1", fachkombi.fachID1, 0);
		DeveloperNotificationException.ifSmaller("fachkombi.fachID2", fachkombi.fachID2, 0);
		DeveloperNotificationException.ifMapNotContains("_map", _map, fachkombi.fachID1);
		DeveloperNotificationException.ifMapNotContains("_map", _map, fachkombi.fachID2);
		DeveloperNotificationException.ifNotInRange("fachkombi.typ", fachkombi.typ, 0, 1);
		final @NotNull GostLaufbahnplanungFachkombinationTyp typ = GostLaufbahnplanungFachkombinationTyp.fromValue(fachkombi.typ);
		if (fachkombi.hinweistext.isBlank()) {
			final @NotNull GostFach fach1 = getOrException(fachkombi.fachID1);
			final @NotNull GostFach fach2 = getOrException(fachkombi.fachID2);
			final @NotNull String kursart1 = ((fachkombi.kursart1 == null) || fachkombi.kursart1.isBlank()) ? " als " + fachkombi.kursart1 : "";
			final @NotNull String kursart2 = ((fachkombi.kursart2 == null) || fachkombi.kursart2.isBlank()) ? " als " + fachkombi.kursart2 : "";
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
	public boolean addAll(final @NotNull Collection<@NotNull GostFach> faecher) {
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
	public boolean addFachkombinationenAll(final @NotNull List<@NotNull GostJahrgangFachkombination> fachkombis) {
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
	public @NotNull List<@NotNull GostFach> getByKuerzel(final @NotNull String kuerzel) {
		final List<@NotNull GostFach> faecher = _mapByKuerzel.get(kuerzel);
		return (faecher == null) ? new ArrayList<>() : faecher;
	}


	/**
	 * Liefert die Liste der Fächer für das angegebene Sprachkürzel zurück.
	 *
	 * @param sprache   das Sprachkürzel der gesuchten Sprache
	 *
	 * @return eine Liste der Fächer, welche das angegebene Sprachkürzel haben
	 */
	public @NotNull List<@NotNull GostFach> getBySprachkuerzel(final @NotNull String sprache) {
		final List<@NotNull GostFach> faecher = _mapBySprachkuerzel.get(sprache);
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
	public @NotNull List<@NotNull GostFach> faecher() {
		return _faecher;
	}


	/**
	 * Liefert die interne Liste mit den Leitfächern zurück.
	 *
	 * @return die interne Liste mit den Leitfächern
	 */
	public @NotNull List<@NotNull GostFach> getLeitfaecher() {
		return _leitfaecher;
	}


	/**
	 * Liefert die interne Liste mit den Fachkombinationen zurück.
	 *
	 * @return die interne Liste mit den Fachkombinationen
	 */
	public @NotNull List<@NotNull GostJahrgangFachkombination> getFachkombinationen() {
		return this._fachkombis;
	}

	/**
	 * Liefert die interne Liste mit den geforderten Fachkombinationen zurück.
	 *
	 * @return die interne Liste mit den geforderten Fachkombinationen
	 */
	public @NotNull List<@NotNull GostJahrgangFachkombination> getFachkombinationenErforderlich() {
		return this._fachkombisErforderlich;
	}

	/**
	 * Liefert die interne Liste mit den nicht erlaubten Fachkombinationen zurück.
	 *
	 * @return die interne Liste mit den nicht erlaubten Fachkombinationen
	 */
	public @NotNull List<@NotNull GostJahrgangFachkombination> getFachkombinationenVerboten() {
		return this._fachkombisVerboten;
	}

}
