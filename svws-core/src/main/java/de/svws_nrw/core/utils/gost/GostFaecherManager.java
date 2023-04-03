package de.svws_nrw.core.utils.gost;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.adt.collection.LinkedCollection;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klassen bietet Hilfsmethoden für den Zugriff auf Daten des
 * Typs {@link GostFach}.
 */
public class GostFaecherManager {

	// TODO create a class for a LinkedCollection with HashMap functionalities and refactor this class

	/** Sortiert die Fächer anhand ihrer konfigurierten Sortierung */
	public static @NotNull Comparator<@NotNull GostFach> comp = (a, b) -> {
		final int va = (a == null) ? Integer.MIN_VALUE : a.sortierung;
		final int vb = (b == null) ? Integer.MIN_VALUE : b.sortierung;
		return Integer.compare(va, vb);
	};

	/** Die Liste der Fächer, die im Manager vorhanden sind. */
	private final @NotNull LinkedCollection<@NotNull GostFach> _faecher = new LinkedCollection<>();

	/** Eine HashMap für den schnellen Zugriff auf ein Fach anhand der ID */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostFach> _map = new HashMap<>();


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
	 * Fügt das übergebene Fach zu diesem Manager hinzu.
	 * Die interne Sortierung wird nicht korrigiert
	 *
	 * @param fach   das hinzuzufügende Fach
	 *
	 * @return true, falls das Fach hinzugefügt wurde
	 * @throws DeveloperNotificationException Falls die ID des Faches nagativ ist.
	 */
	private boolean addInternal(final @NotNull GostFach fach) throws DeveloperNotificationException {
		if (fach.id < 0)
			throw new DeveloperNotificationException("Die Fach-ID darf nicht negativ sein!");
		final GostFach old = _map.put(fach.id, fach);
		if (old != null)
			return false;
		return _faecher.add(fach);
	}


	/**
	 * Führt eine Sortierung der Fächer anhand des Sortierungsfeldes durch.
	 */
	private void sort() {
		_faecher.sort(comp);
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
		final boolean result = addInternal(fach);
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
			if (!addInternal(fach))
				result = false;
		sort();
		return result;
	}


	/**
	 * Gibt das Fach mit der angegebenen ID zurück oder null, falls es das Fach nicht gibt.
	 *
	 * @param id   die ID des gesuchten Faches
	 * @return Das fach mit der angegebenen ID oder null, falls es das Fach nicht gibt.
	 */
	public GostFach get(final long id) {
		return _map.get(id);
	}

	/**
	 * Liefert das Fach mit der angegebenen ID zurück.
	 *
	 * @param pFachID die ID des gesuchten Faches.
	 * @return Das Fach mit der angegebenen ID zurück.
	 * @throws DeveloperNotificationException Falls ein Fach mit der ID nicht bekannt ist.
	 */
	public @NotNull GostFach getOrException(final long pFachID) throws DeveloperNotificationException {
		final GostFach fach = _map.get(pFachID);
		if (fach == null)
			throw new DeveloperNotificationException("GostFach mit id=" + pFachID + " gibt es nicht.");
		return fach;
	}


	/**
	 * Gibt zurück, ob die Liste der Fächer leer ist
	 *
	 * @return true, wenn die Liste der Fächer leer ist.
	 */
	public boolean isEmpty() {
		return _faecher.size() <= 0;
	}


	/**
	 * Liefert die interne Liste der Fächer. Diese sollte nicht
	 * verändert werden.
	 *
	 * @return die interne Liste der Fächer
	 */
	public @NotNull LinkedCollection<@NotNull GostFach> faecher() {
		return _faecher;
	}


	/**
	 * Erstellt aus der internen Liste der Fächer ein Array
	 *
	 * @return ein Array mit den Fächern
	 */
	public @NotNull GostFach@NotNull[] values() {
		return _faecher.toArray(new GostFach[0]);
	}


	/**
	 * Erstellt aus der internen Liste einen Vector mit den Daten
	 *
	 * @return ein Vector mit den Fächern
	 */
	public @NotNull Vector<@NotNull GostFach> toVector() {
		final @NotNull Vector<@NotNull GostFach> result = new Vector<>();
		for (final @NotNull GostFach fach : _faecher)
			result.add(fach);
		return result;
	}

}
