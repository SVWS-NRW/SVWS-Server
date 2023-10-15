package de.svws_nrw.core.utils.stundenplan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.svws_nrw.core.data.stundenplan.StundenplanAufsichtsbereich;
import de.svws_nrw.core.data.stundenplan.StundenplanFach;
import de.svws_nrw.core.data.stundenplan.StundenplanJahrgang;
import de.svws_nrw.core.data.stundenplan.StundenplanKlasse;
import de.svws_nrw.core.data.stundenplan.StundenplanLehrer;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanSchueler;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse ahmt den {@link StundenplanManager} nach, um randomisierte Tests zu simulieren.
 *
 * @author Benjamin A. Bartsch
 */
public final class StundenplanManagerDummy {

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
	private static final @NotNull Comparator<@NotNull StundenplanLehrer> _compLehrer = (final @NotNull StundenplanLehrer a, final @NotNull StundenplanLehrer b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};
	private static final @NotNull Comparator<@NotNull StundenplanKlasse> _compKlasse = (final @NotNull StundenplanKlasse a, final @NotNull StundenplanKlasse b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};
	private static final @NotNull Comparator<@NotNull StundenplanSchueler> _compSchueler = (final @NotNull StundenplanSchueler a, final @NotNull StundenplanSchueler b) -> {
		if (a.idKlasse < b.idKlasse) return -1;
		if (a.idKlasse > b.idKlasse) return +1;
		final int cmpNachname = a.nachname.compareTo(b.nachname);
		if (cmpNachname != 0) return cmpNachname;
		final int cmpVorname = a.vorname.compareTo(b.vorname);
		if (cmpVorname != 0) return cmpVorname;
		return Long.compare(a.id, b.id);
	};
	private static final @NotNull Comparator<@NotNull StundenplanJahrgang> _compJahrgang = (final @NotNull StundenplanJahrgang a, final @NotNull StundenplanJahrgang b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};
	private static final @NotNull Comparator<@NotNull StundenplanAufsichtsbereich> _compAufsichtsbereich = (final @NotNull StundenplanAufsichtsbereich a, final @NotNull StundenplanAufsichtsbereich b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};

	/** Größtmögliche Fach-ID */
	public static final long FACH_MAX_ID = 60;

	/** Größtmögliche Raum-ID */
	public static final long RAUM_MAX_ID = 50;

	/** Größtmögliche Lehrer-ID */
	public static final long LEHRER_MAX_ID = 50;

	/** Größtmögliche Schüler-ID */
	public static final long SCHUELER_MAX_ID = 700;

	/** Größtmögliche Klasse-ID */
	public static final long KLASSE_MAX_ID = 40;

	/** Größtmögliche Jahrgang-ID */
	public static final long JAHRGANG_MAX_ID = 13;

	/** Größtmögliche Aufsichtsbereich-ID */
	public static final long AUFSICHTSBEREICH_MAX_ID = 30;

	private final @NotNull Map<@NotNull Long, @NotNull StundenplanFach> _fachmap = new HashMap<>();
	private final @NotNull Map<@NotNull Long, @NotNull StundenplanRaum> _raummap = new HashMap<>();
	private final @NotNull Map<@NotNull Long, @NotNull StundenplanLehrer> _lehrermap = new HashMap<>();
	private final @NotNull Map<@NotNull Long, @NotNull StundenplanKlasse> _klassemap = new HashMap<>();
	private final @NotNull Map<@NotNull Long, @NotNull StundenplanSchueler> _schuelermap = new HashMap<>();
	private final @NotNull Map<@NotNull Long, @NotNull StundenplanJahrgang> _jahrgangmap = new HashMap<>();
	private final @NotNull Map<@NotNull Long, @NotNull StundenplanAufsichtsbereich> _aufsichtsbereichmap = new HashMap<>();

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
	public static @NotNull StundenplanFach fachCreateRandom(final @NotNull Random rnd) {
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
	public static @NotNull List<@NotNull StundenplanFach> fachListCreateRandom(final @NotNull Random rnd) {
		final @NotNull List<@NotNull StundenplanFach> fachList = new ArrayList<>();

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
		final @NotNull List<@NotNull StundenplanFach> list = new ArrayList<>(_fachmap.values());
		list.sort(_compFach);
		return list;
	}

	/**
	 * Liefert einen zufällig erzeugten Dummy-Raum.
	 *
	 * @param rnd  Dient zur Erzeugung von Zufallswerten ausgehen von einem bekannten Seed.
	 *
	 * @return einen zufällig erzeugten Dummy-Raum.
	 */
	public static @NotNull StundenplanRaum raumCreateRandom(final @NotNull Random rnd) {
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
		// Check nicht nötig.

		// Add
		DeveloperNotificationException.ifMapPutOverwrites(_raummap, raum.id, raum);
	}

	/**
	 * Liefert eine Liste mit erzeugten Räumen.
	 *
	 * @param rnd  Dient zur Erzeugung von Zufallswerten ausgehen von einem bekannten Seed.
	 *
	 * @return eine Liste mit erzeugten Räumen.
	 */
	public static @NotNull List<@NotNull StundenplanRaum> raumListCreateRandom(final @NotNull Random rnd) {
		final @NotNull List<@NotNull @NotNull StundenplanRaum> raumList = new ArrayList<>();

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
		final @NotNull List<@NotNull StundenplanRaum> list = new ArrayList<>(_raummap.values());
		list.sort(_compRaum);
		return list;
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

	/**
	 * Liefert einen zufällig erzeugten Dummy-Schueler.
	 * <br>Hinweis: Ein neuer Schüler hat zunächst keine Klasse.
	 *
	 * @param rnd  Dient zur Erzeugung von Zufallswerten ausgehen von einem bekannten Seed.
	 *
	 * @return einen zufällig erzeugten Dummy-Schueler.
	 */
	public static @NotNull StundenplanSchueler schuelerCreateRandom(final @NotNull Random rnd) {
		final @NotNull StundenplanSchueler schueler = new StundenplanSchueler();
		schueler.id = rnd.nextLong(SCHUELER_MAX_ID);
		schueler.nachname = "Nachname " + schueler.id;
		schueler.vorname = "Vorname " + schueler.id;
		schueler.idKlasse = -1;
		return schueler;
	}

	/**
	 * Liefert eine Liste mit erzeugten Schülern.
	 *
	 * @param rnd  Dient zur Erzeugung von Zufallswerten ausgehen von einem bekannten Seed.
	 *
	 * @return eine Liste mit erzeugten Schülern.
	 */
	public static @NotNull List<@NotNull StundenplanSchueler> schuelerListCreateRandom(final @NotNull Random rnd) {
		final @NotNull List<@NotNull @NotNull StundenplanSchueler> schuelerList = new ArrayList<>();

		final int size = rnd.nextInt(5);
		for (int i = 0; i < size; i++)
			schuelerList.add(schuelerCreateRandom(rnd));

		return schuelerList;
	}

	/**
	 * Fügt einen neuen Schüler hinzu.
	 *
	 * @param schueler  Der neue Schüler, welcher hinzugefügt werden soll.
	 *
	 * @throws DeveloperNotificationException  falls der Schüler bereits existiert.
	 */
	public void schuelerAdd(final @NotNull StundenplanSchueler schueler) throws DeveloperNotificationException {
		// Check: Klassenreferenz nicht prüfen, da Prüfung umgekehrt stattfindet.

		// add
		DeveloperNotificationException.ifMapPutOverwrites(_schuelermap, schueler.id, schueler);
	}

	/**
	 * Fügt alle Schüler hinzu.
	 *
	 * @param list  Die Menge der Schüler, welche hinzugefügt werden soll.
	 */
	public void schuelerAddAll(final @NotNull List<@NotNull StundenplanSchueler> list) {
		// check
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanSchueler lehrer : list) {
			if (_schuelermap.containsKey(lehrer.id))
				throw new DeveloperNotificationException("schuelerAddAll: Schüler-ID existiert bereits!");
			if (!setOfIDs.add(lehrer.id))
				throw new DeveloperNotificationException("schuelerAddAll: Doppelte Schüler-ID in 'list'!");
			// Hinweis: Kein check der Klassenreferenz, der Check wird umgekehrt gemacht.
		}

		// add
		for (final @NotNull StundenplanSchueler schueler : list)
			schuelerAdd(schueler);
	}

	/**
	 * Liefert die Anzahl der SuS der Klasse.
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 *
	 * @return die Anzahl der SuS der Klasse.
	 */
	public int schuelerGetAnzahlByKlasseIdOrException(final long idKlasse) {
		return DeveloperNotificationException.ifMapGetIsNull(_klassemap, idKlasse).schueler.size();
	}

	/**
	 * Liefert die Menge der Schüler der Klasse, sortiert nach {@link #_compSchueler}.
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 *
	 * @return die Menge der Schüler der Klasse, sortiert nach {@link #_compSchueler}.
	 */
	public @NotNull List<@NotNull StundenplanSchueler> schuelerGetMengeByKlasseIdAsListOrException(final long idKlasse) {
		final @NotNull List<@NotNull StundenplanSchueler> listSchueler = new ArrayList<>();

		for (final @NotNull Long idSchueler : DeveloperNotificationException.ifMapGetIsNull(_klassemap, idKlasse).schueler)
			listSchueler.add(DeveloperNotificationException.ifMapGetIsNull(_schuelermap, idSchueler));

		listSchueler.sort(_compSchueler);
		return listSchueler;
	}

	/**
	 * Liefert den Schüler mit der übergebenen ID.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return den Schüler mit der übergebenen ID.
	 * @throws DeveloperNotificationException falls der Schüler mit der ID nicht existiert.
	 */
	public StundenplanSchueler schuelerGetByIdOrException(final long idSchueler) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelermap, idSchueler);
	}

	/**
	 * Liefert eine Liste aller Schüler-Objekte, sortiert nach dem {@link #_compSchueler}.
	 *
	 * @return eine Liste aller Schüler-Objekte, sortiert nach dem {@link #_compSchueler}.
	 */
	public @NotNull List<@NotNull StundenplanSchueler> schuelerGetMengeAsList() {
		final @NotNull List<@NotNull StundenplanSchueler> list = new ArrayList<>(_schuelermap.values());
		list.sort(_compSchueler);
		return list;
	}

	/**
	 * Liefert einen zufällig erzeugten Dummy-Jahrgang.
	 *
	 * @param rnd  Dient zur Erzeugung von Zufallswerten ausgehen von einem bekannten Seed.
	 *
	 * @return einen zufällig erzeugten Dummy-Jahrgang.
	 */
	public static @NotNull StundenplanJahrgang jahrgangCreateRandom(final @NotNull Random rnd) {
		final @NotNull StundenplanJahrgang jahrgang = new StundenplanJahrgang();
		jahrgang.id = rnd.nextLong(JAHRGANG_MAX_ID);
		jahrgang.kuerzel = "" + (jahrgang.id + 1);
		if (jahrgang.id == JAHRGANG_MAX_ID - 1)
			jahrgang.kuerzel = "Q2";
		if (jahrgang.id == JAHRGANG_MAX_ID - 2)
			jahrgang.kuerzel = "Q1";
		if (jahrgang.id == JAHRGANG_MAX_ID - 3)
			jahrgang.kuerzel = "EF";
		jahrgang.bezeichnung = "Bezeichung von " + jahrgang.kuerzel;
		return jahrgang;
	}

	/**
	 * Liefert eine Liste mit erzeugten Jahrgängen.
	 *
	 * @param rnd  Dient zur Erzeugung von Zufallswerten ausgehen von einem bekannten Seed.
	 *
	 * @return eine Liste mit erzeugten Jahrgängen.
	 */
	public static @NotNull List<@NotNull StundenplanJahrgang> jahrgangListCreateRandom(final @NotNull Random rnd) {
		final @NotNull List<@NotNull @NotNull StundenplanJahrgang> jahrgangList = new ArrayList<>();

		final int size = rnd.nextInt(5);
		for (int i = 0; i < size; i++)
			jahrgangList.add(jahrgangCreateRandom(rnd));

		return jahrgangList;
	}

	/**
	 * Fügt einen neuen Jahrgang hinzu.
	 *
	 * @param jahrgang  Der neue Jahrgang, welcher hinzugefügt werden soll.
	 *
	 * @throws DeveloperNotificationException  falls der Jahrgang bereits existiert.
	 */
	public void jahrgangAdd(final @NotNull StundenplanJahrgang jahrgang) throws DeveloperNotificationException {
		// Check nicht nötig, da keine Referenzen vorhanden.

		// add
		DeveloperNotificationException.ifMapPutOverwrites(_jahrgangmap, jahrgang.id, jahrgang);
	}

	/**
	 * Fügt alle Jahrgänge hinzu.
	 *
	 * @param list  Die Menge der Jahrgänge, welche hinzugefügt werden soll.
	 */
	public void jahrgangAddAll(final @NotNull List<@NotNull StundenplanJahrgang> list) {
		// check
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanJahrgang jahrgang : list) {
			if (_jahrgangmap.containsKey(jahrgang.id))
				throw new DeveloperNotificationException("jahrgangAddAll: Jahrgang-ID existiert bereits!");
			if (!setOfIDs.add(jahrgang.id))
				throw new DeveloperNotificationException("jahrgangAddAll: Doppelte Jahrgang-ID in 'list'!");
		}

		// add
		for (final @NotNull StundenplanJahrgang jahrgang : list)
			jahrgangAdd(jahrgang);
	}

	/**
	 * Liefert den Jahrgang mit der übergebenen ID.
	 *
	 * @param idJahrgang  Die Datenbank-ID des Jahrgangs.
	 *
	 * @return den Jahrgang mit der übergebenen ID.
	 * @throws DeveloperNotificationException falls der Jahrgang mit der ID nicht existiert.
	 */
	public StundenplanJahrgang jahrgangGetByIdOrException(final long idJahrgang) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_jahrgangmap, idJahrgang);
	}

	/**
	 * Aktualisiert das vorhandene Jahrgang-Objekt durch das neue Objekt.
	 * <br>Hinweis: Die ID kann nicht gepatched werden.
	 *
	 * @param jahrgang  Das neue Objekt, welches das alte Objekt ersetzt.
	 */
	public void jahrgangPatchAttributes(final @NotNull StundenplanJahrgang jahrgang) {
		// check
		if (!_jahrgangmap.containsKey(jahrgang.id))
			throw new DeveloperNotificationException("jahrgangPatchAttributes: Jahrgang-ID existiert nicht!");

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_jahrgangmap, jahrgang.id);
		DeveloperNotificationException.ifMapPutOverwrites(_jahrgangmap, jahrgang.id, jahrgang);
	}

	/**
	 * Liefert eine Liste aller Jahrgang-Objekte, sortiert nach dem {@link #_compJahrgang}.
	 *
	 * @return eine Liste aller Jahrgang-Objekte, sortiert nach dem {@link #_compJahrgang}.
	 */
	public @NotNull List<@NotNull StundenplanJahrgang> jahrgangGetMengeAsList() {
		final @NotNull List<@NotNull StundenplanJahrgang> list = new ArrayList<>(_jahrgangmap.values());
		list.sort(_compJahrgang);
		return list;
	}

	/**
	 * Fügt einen neuen Aufsichtsbereich hinzu.
	 *
	 * @param aufsichtsbereich  Der neue Aufsichtsbereich, welcher hinzugefügt werden soll.
	 *
	 * @throws DeveloperNotificationException  falls der Aufsichtsbereich bereits existiert.
	 */
	public void aufsichtsbereichAdd(final @NotNull StundenplanAufsichtsbereich aufsichtsbereich) throws DeveloperNotificationException {
		// Check nicht nötig, da keine Referenzen vorhanden.

		// add
		DeveloperNotificationException.ifMapPutOverwrites(_aufsichtsbereichmap, aufsichtsbereich.id, aufsichtsbereich);
	}

	/**
	 * Fügt alle Aufsichtsbereiche hinzu.
	 *
	 * @param list  Die Menge der Aufsichtsbereiche, welche hinzugefügt werden soll.
	 */
	public void aufsichtsbereichAddAll(final @NotNull List<@NotNull StundenplanAufsichtsbereich> list) {
		// check
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanAufsichtsbereich aufsichtsbereich : list) {
			if (_aufsichtsbereichmap.containsKey(aufsichtsbereich.id))
				throw new DeveloperNotificationException("aufsichtsbereichAddAll: Aufsichtsbereich-ID existiert bereits!");
			if (!setOfIDs.add(aufsichtsbereich.id))
				throw new DeveloperNotificationException("aufsichtsbereichAddAll: Doppelte Aufsichtsbereich-ID in 'list'!");
		}

		// add
		for (final @NotNull StundenplanAufsichtsbereich aufsichtsbereich : list)
			aufsichtsbereichAdd(aufsichtsbereich);
	}

	/**
	 * Liefert einen zufällig erzeugten Dummy-Aufsichtsbereich.
	 *
	 * @param rnd  Dient zur Erzeugung von Zufallswerten ausgehen von einem bekannten Seed.
	 *
	 * @return einen zufällig erzeugten Dummy-Aufsichtsbereich.
	 */
	public static @NotNull StundenplanAufsichtsbereich aufsichtsbereichCreateRandom(final @NotNull Random rnd) {
		final @NotNull StundenplanAufsichtsbereich aufsichtsbereich = new StundenplanAufsichtsbereich();
		aufsichtsbereich.id = rnd.nextLong(AUFSICHTSBEREICH_MAX_ID);
		aufsichtsbereich.kuerzel = "aufsichtsbereich.kuerzel " + aufsichtsbereich.id;
		aufsichtsbereich.beschreibung = "aufsichtsbereich.beschreibung " + aufsichtsbereich.beschreibung;
		return aufsichtsbereich;
	}

	/**
	 * Liefert eine Liste mit erzeugten Aufsichtsbereichen.
	 *
	 * @param rnd  Dient zur Erzeugung von Zufallswerten ausgehen von einem bekannten Seed.
	 *
	 * @return eine Liste mit erzeugten Aufsichtsbereichen.
	 */
	public static @NotNull List<@NotNull StundenplanAufsichtsbereich> aufsichtsbereichListCreateRandom(final @NotNull Random rnd) {
		final @NotNull List<@NotNull @NotNull StundenplanAufsichtsbereich> aufsichtsbereichList = new ArrayList<>();

		final int size = rnd.nextInt(5);
		for (int i = 0; i < size; i++)
			aufsichtsbereichList.add(aufsichtsbereichCreateRandom(rnd));

		return aufsichtsbereichList;
	}

	/**
	 * Liefert den Aufsichtsbereich mit der übergebenen ID.
	 *
	 * @param idAufsichtsbereich  Die Datenbank-ID des Aufsichtsbereichs.
	 *
	 * @return den Aufsichtsbereich mit der übergebenen ID.
	 * @throws DeveloperNotificationException falls der Aufsichtsbereich mit der ID nicht existiert.
	 */
	public StundenplanAufsichtsbereich aufsichtsbereichGetByIdOrException(final long idAufsichtsbereich) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_aufsichtsbereichmap, idAufsichtsbereich);
	}

	/**
	 * Aktualisiert das vorhandene Aufsichtsbereich-Objekt durch das neue Objekt.
	 * <br>Hinweis: Die ID kann nicht gepatched werden.
	 *
	 * @param aufsichtsbereich  Das neue Objekt, welches das alte Objekt ersetzt.
	 */
	public void aufsichtsbereichPatchAttributes(final @NotNull StundenplanAufsichtsbereich aufsichtsbereich) {
		// check
		if (!_aufsichtsbereichmap.containsKey(aufsichtsbereich.id))
			throw new DeveloperNotificationException("aufsichtsbereichPatchAttributes: Aufsichtsbereich-ID existiert nicht!");

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_aufsichtsbereichmap, aufsichtsbereich.id);
		DeveloperNotificationException.ifMapPutOverwrites(_aufsichtsbereichmap, aufsichtsbereich.id, aufsichtsbereich);
	}

	/**
	 * Entfernt das existierende Aufsichtsbereich-Objekt.
	 *
	 * @param id  Die ID des Aufsichtsbereichs.
	 */
	public void aufsichtsbereichRemoveById(final long id) {
		DeveloperNotificationException.ifMapRemoveFailes(_aufsichtsbereichmap, id);
	}

	/**
	 * Entfernt alle Aufsichtsbereiche.
	 *
	 * @param list  Die Menge der Aufsichtsbereiche, welche entfernt werden soll.
	 */
	public void aufsichtsbereichRemoveAll(final @NotNull List<@NotNull StundenplanAufsichtsbereich> list) {
		// check
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanAufsichtsbereich aufsichtsbereich : list) {
			if (!_aufsichtsbereichmap.containsKey(aufsichtsbereich.id))
				throw new DeveloperNotificationException("aufsichtsbereichRemoveAll: Aufsichtsbereich-ID existiert nicht!");
			if (!setOfIDs.add(aufsichtsbereich.id))
				throw new DeveloperNotificationException("aufsichtsbereichRemoveAll: Doppelte Aufsichtsbereich-ID in der Liste!");
		}

		// add
		for (final @NotNull StundenplanAufsichtsbereich aufsichtsbereich : list)
			aufsichtsbereichRemoveById(aufsichtsbereich.id);
	}

	/**
	 * Liefert eine Liste aller Aufsichtsbereich-Objekte, sortiert nach dem {@link #_compAufsichtsbereich}.
	 *
	 * @return eine Liste aller Aufsichtsbereich-Objekte, sortiert nach dem {@link #_compAufsichtsbereich}.
	 */
	public @NotNull List<@NotNull StundenplanAufsichtsbereich> aufsichtsbereichGetMengeAsList() {
		final @NotNull List<@NotNull StundenplanAufsichtsbereich> list = new ArrayList<>(_aufsichtsbereichmap.values());
		list.sort(_compAufsichtsbereich);
		return list;
	}

	/**
	 * Liefert einen zufällig erzeugten Dummy-Lehrer.
	 *
	 * @param rnd  Dient zur Erzeugung von Zufallswerten ausgehen von einem bekannten Seed.
	 *
	 * @return einen zufällig erzeugten Dummy-Lehrer.
	 */
	public static @NotNull StundenplanLehrer lehrerCreateRandom(final @NotNull Random rnd) {
		final @NotNull StundenplanLehrer lehrer = new StundenplanLehrer();
		lehrer.id = rnd.nextLong(LEHRER_MAX_ID);
		lehrer.kuerzel = "Kürzel " + lehrer.id;
		lehrer.nachname = "Nachname " + lehrer.id;
		lehrer.vorname = "Vorname " + lehrer.id;

		final int nFaecher = rnd.nextInt(3);
		for (int i = 0; i < nFaecher; i++)
			lehrer.faecher.add(rnd.nextLong(FACH_MAX_ID));

		return lehrer;
	}

	/**
	 * Liefert eine Liste mit erzeugten Lehrern.
	 *
	 * @param rnd  Dient zur Erzeugung von Zufallswerten ausgehen von einem bekannten Seed.
	 *
	 * @return eine Liste mit erzeugten Lehrern.
	 */
	public static @NotNull List<@NotNull StundenplanLehrer> lehrerListCreateRandom(final @NotNull Random rnd) {
		final @NotNull List<@NotNull @NotNull StundenplanLehrer> lehrerList = new ArrayList<>();

		final int size = rnd.nextInt(5);
		for (int i = 0; i < size; i++)
			lehrerList.add(lehrerCreateRandom(rnd));

		return lehrerList;
	}

	/**
	 * Fügt einen neuen Lehrer hinzu.
	 *
	 * @param lehrer  Der neue Lehrer, welcher hinzugefügt werden soll.
	 *
	 * @throws DeveloperNotificationException  falls der Lehrer bereits existiert.
	 */
	public void lehrerAdd(final @NotNull StundenplanLehrer lehrer) throws DeveloperNotificationException {
		// check
		for (final @NotNull Long idFach : lehrer.faecher)
			DeveloperNotificationException.ifMapNotContains("_fachmap", _fachmap, idFach);

		// add
		DeveloperNotificationException.ifMapPutOverwrites(_lehrermap, lehrer.id, lehrer);
	}

	/**
	 * Fügt alle Lehrer hinzu.
	 *
	 * @param list  Die Menge der Lehrer, welche hinzugefügt werden soll.
	 */
	public void lehrerAddAll(final @NotNull List<@NotNull StundenplanLehrer> list) {
		// check
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanLehrer lehrer : list) {
			if (_lehrermap.containsKey(lehrer.id))
				throw new DeveloperNotificationException("lehrerAddAll: Lehrer-ID existiert bereits!");
			if (!setOfIDs.add(lehrer.id))
				throw new DeveloperNotificationException("lehrerAddAll: Doppelte Lehrer-ID in 'list'!");
			for (final @NotNull Long idFach : lehrer.faecher)
				if (!_fachmap.containsKey(idFach))
					throw new DeveloperNotificationException("lehrerAddAll: Fach-ID der Lehrkraft existiert nicht!");
		}

		// add
		for (final @NotNull StundenplanLehrer lehrer : list)
			lehrerAdd(lehrer);
	}

	/**
	 * Liefert die Lehrkraft mit der übergebenen ID.
	 *
	 * @param idLehrer  Die Datenbank-ID der Lehrkraft.
	 *
	 * @return die Lehrkraft mit der übergebenen ID.
	 * @throws DeveloperNotificationException falls die Lehrkraft mit der ID nicht existiert.
	 */
	public StundenplanLehrer lehrerGetByIdOrException(final long idLehrer) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_lehrermap, idLehrer);
	}

	/**
	 * Aktualisiert das vorhandene Lehrer-Objekt durch das neue Objekt.
	 * <br>Hinweis: Die ID kann nicht gepatched werden.
	 *
	 * @param lehrer  Das neue Objekt, welches das alte Objekt ersetzt.
	 */
	public void lehrerPatchAttributes(final @NotNull StundenplanLehrer lehrer) {
		// check
		if (!_lehrermap.containsKey(lehrer.id))
			throw new DeveloperNotificationException("lehrerPatchAttributes: Lehrer-ID existiert nicht!");
		for (final @NotNull Long idFach : lehrer.faecher)
			if (!_fachmap.containsKey(idFach))
				throw new DeveloperNotificationException("lehrerPatchAttributes: Fach-ID der Lehrkraft existiert nicht!");

		lehrerRemoveById(lehrer.id);
		lehrerAdd(lehrer);
	}

	/**
	 * Entfernt das existierende Lehrer-Objekt.
	 *
	 * @param id  Die ID der Lehrkraft.
	 */
	public void lehrerRemoveById(final long id) {
		DeveloperNotificationException.ifMapRemoveFailes(_lehrermap, id);
	}

	/**
	 * Entfernt alle Lehrkräfte.
	 *
	 * @param list  Die Menge der Lehrkräfte, welche entfernt werden soll.
	 */
	public void lehrerRemoveAll(final @NotNull List<@NotNull StundenplanLehrer> list) {
		// check
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanLehrer lehrer : list) {
			if (!_lehrermap.containsKey(lehrer.id))
				throw new DeveloperNotificationException("lehrerRemoveAll: Lehrer-ID existiert nicht!");
			if (!setOfIDs.add(lehrer.id))
				throw new DeveloperNotificationException("lehrerRemoveAll: Doppelte Lehrer-ID in der Liste!");
		}

		// add
		for (final @NotNull StundenplanLehrer lehrer : list)
			lehrerRemoveById(lehrer.id);
	}

	/**
	 * Liefert eine Liste aller Lehrer-Objekte, sortiert nach dem {@link #_compLehrer}.
	 *
	 * @return eine Liste aller Lehrer-Objekte, sortiert nach dem {@link #_compLehrer}.
	 */
	public @NotNull List<@NotNull StundenplanLehrer> lehrerGetMengeAsList() {
		final @NotNull List<@NotNull StundenplanLehrer> list = new ArrayList<>(_lehrermap.values());
		list.sort(_compLehrer);
		return list;
	}

	/**
	 * Liefert eine zufällig erzeugte Dummy-Klasse.
	 *
	 * @param rnd  Dient zur Erzeugung von Zufallswerten ausgehen von einem bekannten Seed.
	 *
	 * @return eine zufällig erzeugte Dummy-Klasse.
	 */
	public static @NotNull StundenplanKlasse klasseCreateRandom(final @NotNull Random rnd) {
		final int stufe = rnd.nextInt(13) + 1;

		final @NotNull StundenplanKlasse klasse = new StundenplanKlasse();
		klasse.id = rnd.nextLong(KLASSE_MAX_ID);

		if (stufe < 11) {
			klasse.kuerzel = stufe + "" + ((char) (rnd.nextInt(6) + 'a'));
		}
		if (stufe == 11) {
			klasse.kuerzel = "EF";
		}
		if (stufe == 12) {
			klasse.kuerzel = "Q1";
		}
		if (stufe == 13) {
			klasse.kuerzel = "Q2";
		}

		klasse.bezeichnung = "Klasse mit ID " + klasse.id + " und Kürzel " + klasse.kuerzel;
		// klasse.jahrgaenge // TODO
		// klasse.schueler // TODO
		return klasse;
	}

	/**
	 * Fügt eine neue Klasse hinzu.
	 *
	 * @param klasse  Die neue Klasse, welche hinzugefügt werden soll.
	 *
	 * @throws DeveloperNotificationException  falls die Klasse bereits existiert.
	 */
	public void klasseAdd(final @NotNull StundenplanKlasse klasse) throws DeveloperNotificationException {
		// check // TODO
//		for (final @NotNull Long idSchueler : klasse.schueler)
//			DeveloperNotificationException.ifMapNotContains("_fachmap", _fachmap, idFach);

		// add
		DeveloperNotificationException.ifMapPutOverwrites(_klassemap, klasse.id, klasse);
	}

	/**
	 * Liefert eine Liste aller Klasse-Objekte, sortiert nach dem {@link #_compKlasse}.
	 *
	 * @return eine Liste aller Klasse-Objekte, sortiert nach dem {@link #_compKlasse}.
	 */
	public @NotNull List<@NotNull StundenplanKlasse> klasseGetMengeAsList() {
		final @NotNull List<@NotNull StundenplanKlasse> list = new ArrayList<>(_klassemap.values());
		list.sort(_compKlasse);
		return list;
	}

}
