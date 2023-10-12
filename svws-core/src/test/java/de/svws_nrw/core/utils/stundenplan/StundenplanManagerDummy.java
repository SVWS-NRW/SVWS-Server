package de.svws_nrw.core.utils.stundenplan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.svws_nrw.core.data.stundenplan.StundenplanFach;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse ahmt den {@link StundenplanManager} nach, um randomisierte Tests zu simulieren.
 *
 * @author Benjamin A. Bartsch
 */
public final class StundenplanManagerDummy {

	/** Größtmögliche Fach-ID */
	public static final long FACH_MAX_ID = 60;

	private static final @NotNull Comparator<@NotNull StundenplanFach> _compFach = (final @NotNull StundenplanFach a, final @NotNull StundenplanFach b) -> {
		if (a.sortierung < b.sortierung) return -1;
		if (a.sortierung > b.sortierung) return +1;
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};


	private final @NotNull Map<@NotNull Long, @NotNull StundenplanFach> _fachmap = new HashMap<>();
	private final @NotNull List<@NotNull StundenplanFach> _fachmenge = new ArrayList<>();

	/**
	 * Der Manager ist anfangs leer.
	 */
	public StundenplanManagerDummy() {
		// nichts
	}

	/**
	 * Erzeugt ein zufälliges Dummy-Fach.
	 *
	 * @param rnd  Dient zur Erzeugung von Zufallswerten ausgehen von einem bekannten Seed.
	 *
	 * @return ein zufälliges Dummy-Fach.
	 */
	public static @NotNull StundenplanFach fachCreateRandom(final Random rnd) {
		final @NotNull StundenplanFach fach = new StundenplanFach();
		fach.id = rnd.nextLong(FACH_MAX_ID);
		fach.kuerzel = "Fach Nr. " + fach.id;
		fach.kuerzelStatistik = "Fach Nr. " + fach.id;
		fach.bezeichnung = "Das Fach mit Nr. " + fach.id;
		fach.sortierung = rnd.nextInt(60000);
		fach.farbe = null;
		return fach;
	}

	/**
	 * Liefert eine Liste mit erzeugten Fächern.
	 *
	 * @param rnd  Dient zur Erzeugung von Zufallswerten ausgehen von einem bekannten Seed.
	 *
	 * @return eine Liste mit erzeugten Fächern.
	 */
	public static List<@NotNull StundenplanFach> fachListCreateRandom(final @NotNull Random rnd) {
		final List<@NotNull StundenplanFach> fachList = new ArrayList<>();

		final int size = rnd.nextInt(5);
		for (int i = 0; i < size; i++)
			fachList.add(fachCreateRandom(rnd));

		return fachList;
	}

	/**
	 * Fügt ein neues Fach hinzu.
	 *
	 * @param fach  Das neue Fach, welches hinzugefügt werden soll.
	 *
	 * @throws DeveloperNotificationException  falls das Fach bereits
	 */
	public void fachAdd(final @NotNull StundenplanFach fach) throws DeveloperNotificationException {
		DeveloperNotificationException.ifMapPutOverwrites(_fachmap, fach.id, fach);
		_fachmenge.add(fach);
		_fachmenge.sort(_compFach);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanFach}-Objekte, sortiert nach {@link StundenplanFach#sortierung}.
	 *
	 * @return eine Liste aller {@link StundenplanFach}-Objekte, sortiert nach {@link StundenplanFach#sortierung}.
	 */
	public @NotNull List<@NotNull StundenplanFach> fachGetMengeAsList() {
		return _fachmenge;
	}

	/**
	 * Liefert das Fach mit der übergebenen ID.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return  das Fach mit der übergebenen ID.
	 * @throws DeveloperNotificationException falls das Fach mit der ID nicht existiert.
	 */
	public StundenplanFach fachGetByIdOrException(final long idFach) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_fachmap, idFach);
	}

	/**
	 * Fügt alle Fächer hinzu.
	 *
	 * @param listFach  Die Menge der Fächer, welche hinzugefügt werden soll.
	 */
	public void fachAddAll(final @NotNull List<@NotNull StundenplanFach> listFach) {
		// check
		final @NotNull HashSet<@NotNull Long> fachIDs = new HashSet<>();
		for (final @NotNull StundenplanFach fach : listFach) {
			if (_fachmap.containsKey(fach.id))
				throw new DeveloperNotificationException("fachAddAllOhneUpdate: Fach-ID existiert bereits!");
			if (!fachIDs.add(fach.id))
				throw new DeveloperNotificationException("fachAddAllOhneUpdate: Doppelte Fach-ID in der Liste!");
		}

		// add
		for (final @NotNull StundenplanFach fach : listFach)
			fachAdd(fach);
	}

}
