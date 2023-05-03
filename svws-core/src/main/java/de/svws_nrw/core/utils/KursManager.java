package de.svws_nrw.core.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.kurse.KursListeEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klassen bietet Hilfsmethoden für den Zugriff auf Daten des Typs
 * {@link GostFach}.
 */
public class KursManager {

	/** Die Kurse, die im Manager vorhanden sind */
	private final @NotNull List<@NotNull KursListeEintrag> _kurse = new Vector<>();

	/** Eine HashMap für den schnellen Zugriff auf ein Fach anhand der ID */
	private final @NotNull HashMap<@NotNull Long, @NotNull KursListeEintrag> _map = new HashMap<>();

	/**
	 * Erstelle einen neuen Manager mit einer leeren Fächerliste
	 */
	public KursManager() {
		// do nothing...
	}

	/**
	 * Erstellt einen neuen Manager mit den übergebenen Kursen.
	 *
	 * @param kurse die Liste mit den Kursen
	 */
	public KursManager(final @NotNull List<@NotNull KursListeEintrag> kurse) {
		addAll(kurse);
	}

	/**
	 * Fügt den übergebenen Kurs zu diesem Manager hinzu. Die interne Sortierung
	 * wird nicht korrigiert
	 *
	 * @param kurs der hinzuzufügende Kurs
	 *
	 * @return true, falls der Kurs hinzugefügt wurde
	 * @throws DeveloperNotificationException Falls die ID des Kurses nagativ ist.
	 */
	private boolean addInternal(final @NotNull KursListeEintrag kurs) throws DeveloperNotificationException {
		if (kurs.id < 0)
			throw new DeveloperNotificationException("Die Kurs-ID darf nicht negativ sein!");
		final KursListeEintrag old = _map.put(kurs.id, kurs);
		if (old != null)
			return false;
		return _kurse.add(kurs);
	}

	/**
	 * Führt eine Sortierung der Kurse anhand des Sortierungsfeldes durch.
	 */
	private void sort() {
//		_faecher.sort(comp);
	}

	/**
	 * Fügt den übergebenen Kurs zu diesem Manager hinzu und passt intern die
	 * Sortierung der Kurse an.
	 *
	 * @param kurs der hinzuzufügende Kurs
	 *
	 * @return true, falls der Kurs hinzugefügt wurde
	 */
	public boolean add(final @NotNull KursListeEintrag kurs) {
		final boolean result = addInternal(kurs);
		sort();
		return result;
	}

	/**
	 * Fügt die Kurse in der übergeben Liste zu diesem Manager hinzu.
	 *
	 * @param kurse die einzufügenden Kurse
	 *
	 * @return true, falls <i>alle</i> Kurse eingefügt wurden, sonst false
	 */
	public boolean addAll(final @NotNull Collection<@NotNull KursListeEintrag> kurse) {
		boolean result = true;
		for (final @NotNull KursListeEintrag kurs : kurse)
			if (!addInternal(kurs))
				result = false;
		sort();
		return result;
	}

	/**
	 * Gibt den Kurs mit der angegebenen ID zurück oder null, falls es den Kurs
	 * nicht gibt.
	 *
	 * @param id die ID des gesuchten Kurses
	 * @return Der Kurs mit der angegebenen ID oder null, falls es den Kurs nicht
	 *         gibt.
	 */
	public KursListeEintrag get(final long id) {
		return _map.get(id);
	}

	/**
	 * Liefert den Kurs mit der angegebenen ID zurück.
	 *
	 * @param pKursID die ID des gesuchten Kurses
	 * @return den Kurs mit der angegebenen ID
	 * @throws DeveloperNotificationException falls ein Kurs mit der ID nicht
	 *                                        bekannt ist
	 */
	public @NotNull KursListeEintrag getOrException(final long pKursID) throws DeveloperNotificationException {
		final KursListeEintrag kurs = _map.get(pKursID);
		if (kurs == null)
			throw new DeveloperNotificationException("KursListeEintrag mit id=" + pKursID + " gibt es nicht.");
		return kurs;
	}

	/**
	 * Gibt zurück, ob die Liste der Kurse leer ist
	 *
	 * @return true, wenn die Liste der Kurse leer ist.
	 */
	public boolean isEmpty() {
		return _kurse.isEmpty();
	}

	/**
	 * Liefert die interne Liste der Kurse. Diese sollte nicht verändert werden.
	 *
	 * @return die interne Liste der Kurse
	 */
	public @NotNull List<@NotNull KursListeEintrag> kurse() {
		return _kurse;
	}

	/**
	 * Erstellt aus der internen Liste der Kurse ein Array
	 *
	 * @return ein Array mit den Kursen
	 */
	public @NotNull KursListeEintrag @NotNull [] values() {
		return _kurse.toArray(new KursListeEintrag[0]);
	}

	/**
	 * Erstellt aus der internen Liste einen Vector mit den Daten
	 *
	 * @return ein Vector mit den Kursen
	 */
	public @NotNull Vector<@NotNull KursListeEintrag> toVector() {
		final @NotNull Vector<@NotNull KursListeEintrag> result = new Vector<>();
		for (final @NotNull KursListeEintrag kurs : _kurse)
			result.add(kurs);
		return result;
	}

}
