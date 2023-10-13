package de.svws_nrw.core.utils.stundenplan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.svws_nrw.core.data.stundenplan.StundenplanFach;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
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

	/** Größtmögliche Raum-ID */
	public static final long RAUM_MAX_ID = 50;

	private static final @NotNull Comparator<@NotNull StundenplanFach> _compFach = (final @NotNull StundenplanFach a, final @NotNull StundenplanFach b) -> {
		if (a.sortierung < b.sortierung) return -1;
		if (a.sortierung > b.sortierung) return +1;
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};
	private static final @NotNull Comparator<@NotNull StundenplanRaum> _compRaum = (final @NotNull StundenplanRaum a, final @NotNull StundenplanRaum b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};


	private final @NotNull Map<@NotNull Long, @NotNull StundenplanFach> _fachmap = new HashMap<>();
	private final @NotNull List<@NotNull StundenplanFach> _fachmenge = new ArrayList<>();

	private final @NotNull Map<@NotNull Long, @NotNull StundenplanRaum> _raummap = new HashMap<>();
	private final @NotNull List<@NotNull StundenplanRaum> _raummenge = new ArrayList<>();

	/**
	 * Der Manager ist anfangs leer.
	 */
	public StundenplanManagerDummy() {
		// nichts
	}

	/**
	 * Liefert ein zufällig erzeugtes Dummy-Fach.
	 *
	 * @param rnd  Dient zur Erzeugung von Zufallswerten ausgehen von einem bekannten Seed.
	 *
	 * @return ein zufällig erzeugtes Dummy-Fach.
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
	 * @throws DeveloperNotificationException  falls das Fach bereits existiert.
	 */
	public void fachAdd(final @NotNull StundenplanFach fach) throws DeveloperNotificationException {
		DeveloperNotificationException.ifMapPutOverwrites(_fachmap, fach.id, fach);
		_fachmenge.add(fach);
		_fachmenge.sort(_compFach);
	}

	/**
	 * Fügt alle Fächer hinzu.
	 *
	 * @param list  Die Menge der Fächer, welche hinzugefügt werden soll.
	 */
	public void fachAddAll(final @NotNull List<@NotNull StundenplanFach> list) {
		// check
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanFach fach : list) {
			if (_fachmap.containsKey(fach.id))
				throw new DeveloperNotificationException("fachAddAll: Fach-ID existiert bereits!");
			if (!setOfIDs.add(fach.id))
				throw new DeveloperNotificationException("fachAddAll: Doppelte Fach-ID in der Liste!");
		}

		// add
		for (final @NotNull StundenplanFach fach : list)
			fachAdd(fach);
	}

	/**
	 * Liefert das Fach mit der übergebenen ID.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return das Fach mit der übergebenen ID.
	 * @throws DeveloperNotificationException falls das Fach mit der ID nicht existiert.
	 */
	public StundenplanFach fachGetByIdOrException(final long idFach) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_fachmap, idFach);
	}

	/**
	 * Liefert eine Liste aller Fach-Objekte, sortiert nach {@link #_compFach}.
	 *
	 * @return eine Liste aller Fach-Objekte, sortiert nach {@link #_compFach}.
	 */
	public @NotNull List<@NotNull StundenplanFach> fachGetMengeAsList() {
		return _fachmenge;
	}

	/**
	 * Liefert einen zufällig erzeugten Dummy-Raum.
	 *
	 * @param rnd  Dient zur Erzeugung von Zufallswerten ausgehen von einem bekannten Seed.
	 *
	 * @return einen zufällig erzeugten Dummy-Raum.
	 */
	public static @NotNull StundenplanRaum raumCreateRandom(final Random rnd) {
		final @NotNull StundenplanRaum raum = new StundenplanRaum();
		raum.id = rnd.nextLong(RAUM_MAX_ID);
		raum.kuerzel = "Raum Nr. " + raum.id;
		raum.beschreibung = "Raum Nr. " + raum.id;
		raum.groesse = rnd.nextInt(35);
		return raum;
	}

	/**
	 * Fügt einen neuen Raum hinzu.
	 *
	 * @param raum  Der neue Raum, welcher hinzugefügt werden soll.
	 *
	 * @throws DeveloperNotificationException  falls der Raum bereits existiert.
	 */
	public void raumAdd(final @NotNull StundenplanRaum raum) throws DeveloperNotificationException {
		DeveloperNotificationException.ifMapPutOverwrites(_raummap, raum.id, raum);
		_raummenge.add(raum);
		_raummenge.sort(_compRaum);
	}

	/**
	 * Liefert eine Liste mit erzeugten Räumen.
	 *
	 * @param rnd  Dient zur Erzeugung von Zufallswerten ausgehen von einem bekannten Seed.
	 *
	 * @return eine Liste mit erzeugten Räumen.
	 */
	public static List<@NotNull StundenplanRaum> raumListCreateRandom(final @NotNull Random rnd) {
		final List<@NotNull @NotNull StundenplanRaum> raumList = new ArrayList<>();

		final int size = rnd.nextInt(5);
		for (int i = 0; i < size; i++)
			raumList.add(raumCreateRandom(rnd));

		return raumList;
	}

	/**
	 * Fügt alle Räume hinzu.
	 *
	 * @param list  Die Menge der Räume, welche hinzugefügt werden soll.
	 */
	public void raumAddAll(final @NotNull List<@NotNull StundenplanRaum> list) {
		// check
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanRaum raum : list) {
			if (_raummap.containsKey(raum.id))
				throw new DeveloperNotificationException("raumAddAll: Raum-ID existiert bereits!");
			if (!setOfIDs.add(raum.id))
				throw new DeveloperNotificationException("raumAddAll: Doppelte Raum-ID in 'list'!");
		}

		// add
		for (final @NotNull StundenplanRaum raum : list)
			raumAdd(raum);
	}

	/**
	 * Liefert den Raum Fach mit der übergebenen ID.
	 *
	 * @param idRaum  Die Datenbank-ID des Raumes.
	 *
	 * @return den Raum mit der übergebenen ID.
	 * @throws DeveloperNotificationException falls der Raum mit der ID nicht existiert.
	 */
	public StundenplanRaum raumGetByIdOrException(final long idRaum) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_raummap, idRaum);
	}

	/**
	 * Liefert eine Liste aller Raum-Objekte, sortiert nach dem {@link #_compRaum}.
	 *
	 * @return eine Liste aller Raum-Objekte, sortiert nach dem {@link #_compRaum}.
	 */
	public @NotNull List<@NotNull StundenplanRaum> raumGetMengeAsList() {
		return _raummenge;
	}

	/**
	 * Aktualisiert das vorhandene Raum-Objekt durch das neue Objekt.
	 * <br>Hinweis: Die ID kann nicht gepatched werden.
	 *
	 * @param raum  Das neue Objekt, welches das alte Objekt ersetzt.
	 */
	public void raumPatchAttributes(final @NotNull StundenplanRaum raum) {
		raumRemoveById(raum.id);
		raumAdd(raum);
	}

	/**
	 * Entfernt das existierende Raum-Objekt.
	 *
	 * @param id  Die ID des Raumes.
	 */
	public void raumRemoveById(final long id) {
		DeveloperNotificationException.ifMapRemoveFailes(_raummap, id);

		for (final @NotNull Iterator<@NotNull StundenplanRaum> i = _raummenge.iterator(); i.hasNext();)
			if (i.next().id == id)
				i.remove();
	}

	/**
	 * Entfernt alle Räume.
	 *
	 * @param list  Die Menge der Räume, welche entfernt werden soll.
	 */
	public void raumRemoveAll(final @NotNull List<@NotNull StundenplanRaum> list) {
		// check
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanRaum raum : list) {
			if (!_raummap.containsKey(raum.id))
				throw new DeveloperNotificationException("raumRemoveAll: Raum-ID existiert nicht!");
			if (!setOfIDs.add(raum.id))
				throw new DeveloperNotificationException("raumRemoveAll: Doppelte Raum-ID in der Liste!");
		}

		// add
		for (final @NotNull StundenplanRaum raum : list)
			raumRemoveById(raum.id);
	}

}
