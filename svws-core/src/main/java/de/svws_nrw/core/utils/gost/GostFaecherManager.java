package de.svws_nrw.core.utils.gost;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.fach.Fachgruppe;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.types.gost.GostFachbereich;
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

	/** Eine Map für den schnellen Zugriff auf die Fächer, welche als Leitfächer zur Verfügung stehen. */
	private final @NotNull List<@NotNull GostFach> _leitfaecher = new ArrayList<>();


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
	 * @param faecher   die einzufügenden Fächer
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

}
