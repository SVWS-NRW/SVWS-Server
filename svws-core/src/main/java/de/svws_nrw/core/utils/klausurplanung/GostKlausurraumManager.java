package de.svws_nrw.core.utils.klausurplanung;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkrsKrs;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurraumstunde;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.core.utils.Map2DUtils;
import de.svws_nrw.core.utils.MapUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link GostKlausurraum}.
 * Hierbei werden auch Hilfsmethoden zur Interpretation der Daten erzeugt. Es
 * werden Klausurräume eines Gost-Klausurtermins verwaltet.
 */
public class GostKlausurraumManager {

	private final @NotNull GostKursklausurManager _kursklausurManager;
	private final @NotNull GostKlausurtermin _termin;

	/** Ein Comparator für die GostKlausurräume. */
	private static final @NotNull Comparator<@NotNull GostKlausurraum> _compRaum = (final @NotNull GostKlausurraum a, final @NotNull GostKlausurraum b) -> Long.compare(a.id, b.id);

	// GostKlausurraum
	private final @NotNull Map<@NotNull Long, @NotNull GostKlausurraum> _raum_by_id = new HashMap<>();
	private final @NotNull List<@NotNull GostKlausurraum> _raummenge = new ArrayList<>();
	private final @NotNull Map<@NotNull Long, @NotNull GostKlausurraum> _klausurraum_by_idStundenplanraum = new HashMap<>();
	private final @NotNull Map<@NotNull Long, @NotNull GostKlausurraum> _klausurraum_by_idSchuelerklausur = new HashMap<>();

	// GostKlausurraumstunde
	private final @NotNull Map<@NotNull Long, @NotNull GostKlausurraumstunde> _raumstunde_by_id = new HashMap<>();
	private final @NotNull List<@NotNull GostKlausurraumstunde> _raumstundenmenge = new ArrayList<>();
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostKlausurraumstunde>> _raumstundenmenge_by_idRaum = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull GostKlausurraumstunde> _raumstunde_by_idRaum_and_idZeitraster = new HashMap2D<>();
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostKlausurraumstunde>> _raumstundenmenge_by_idSchuelerklausur = new HashMap<>();

	// GostSchuelerklausur
	private final @NotNull Map<@NotNull Long, @NotNull GostSchuelerklausur> _schuelerklausur_by_id = new HashMap<>();
	private final @NotNull List<@NotNull GostSchuelerklausur> _schuelerklausurmenge = new ArrayList<>();
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostSchuelerklausur>> _schuelerklausurmenge_by_idRaum = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull List<@NotNull GostSchuelerklausur>> _schuelerklausurmenge_by_idRaum_and_idKursklausur = new HashMap2D<>();
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostSchuelerklausur>> _schuelerklausurmenge_by_idKursklausur = new HashMap<>();

	// GostSchuelerklausurraumstunde
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull GostSchuelerklausurraumstunde> _schuelerklausurraumstunde_by_idSchuelerklausur_and_idRaumstunde = new HashMap2D<>();
	private final @NotNull List<@NotNull GostSchuelerklausurraumstunde> _schuelerklausurraumstundenmenge = new ArrayList<>();
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostSchuelerklausurraumstunde>> _schuelerklausurraumstundenmenge_by_idRaumstunde = new HashMap<>();
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostSchuelerklausurraumstunde>> _schuelerklausurraumstundenmenge_by_idSchuelerklausur = new HashMap<>();

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param raum              der Gost-Klausurraum
	 * @param stunden           die Liste der GostKlausurraumstunden eines
	 *                          Gost-Klausurtermins
	 * @param schuelerklausuren die Liste der GostSchuelerklausuren des
	 *                          Gost-Klausurtermins
	 * @param kursklausurmanager der Kursklausur-Manager
	 * @param termin              der Gost-Klausurtermin
	 */
	public GostKlausurraumManager(final @NotNull GostKlausurraum raum, final @NotNull List<@NotNull GostKlausurraumstunde> stunden,
			final @NotNull List<@NotNull GostSchuelerklausur> schuelerklausuren, final @NotNull GostKursklausurManager kursklausurmanager, final @NotNull GostKlausurtermin termin) {
		_kursklausurManager = kursklausurmanager;
		_termin = termin;
		final List<@NotNull GostKlausurraum> raeume = new ArrayList<>();
		raeume.add(raum);
		initAll(raeume, stunden, new ArrayList<>(), schuelerklausuren);
	}

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param raeume            die Liste der GostKlausurräume eines
	 *                          Gost-Klausurtermins
	 * @param listRs            die Liste der GostKlausurraumstunden eines
	 *                          Gost-Klausurtermins
	 * @param listSkrs          die Liste der Schülerklausurraumstunden
	 * @param schuelerklausuren die Liste der GostSchuelerklausuren des
	 *                          Gost-Klausurtermins
	 * @param kursklausurmanager der Kursklausur-Manager
	 * @param termin              der Gost-Klausurtermin
	 */
	public GostKlausurraumManager(final @NotNull List<@NotNull GostKlausurraum> raeume, final @NotNull List<@NotNull GostKlausurraumstunde> listRs,
			final @NotNull List<@NotNull GostSchuelerklausurraumstunde> listSkrs, final @NotNull List<@NotNull GostSchuelerklausur> schuelerklausuren, final @NotNull GostKursklausurManager kursklausurmanager, final @NotNull GostKlausurtermin termin) {
		_kursklausurManager = kursklausurmanager;
		_termin = termin;
		initAll(raeume, listRs, listSkrs, schuelerklausuren);
	}

	private void initAll(final @NotNull List<@NotNull GostKlausurraum> listRaum, final @NotNull List<@NotNull GostKlausurraumstunde> listRaumstunde,
			final @NotNull List<@NotNull GostSchuelerklausurraumstunde> listSchuelerklausurraumstunde, final @NotNull List<@NotNull GostSchuelerklausur> listSchuelerklausur) {

		raumAddAll(listRaum);
		raumstundeAddAll(listRaumstunde);
		schuelerklausurAddAll(listSchuelerklausur);
		schuelerklausurraumstundeAddAll(listSchuelerklausurraumstunde);

		update_all();

	}

	private void update_all() {

		update_raummenge();
		update_raumstundenmenge();
		update_schuelerklausurmenge();
		update_schuelerklausurraumstundenmenge();

		update_klausurraum_by_idStundenplanraum();
		update_raumstundenmenge_by_idRaum();
		update_raumstunde_by_idRaum_and_idZeitraster();
		update_raumstundenmenge_by_idSchuelerklausur(); // benötigt _raumstunde_by_id
		update_schuelerklausurmenge_by_idRaum(); // benötigt _raumstundenmenge_by_idSchuelerklausur
		update_schuelerklausurmenge_by_idRaum_and_idKursklausur(); // benötigt _raumstundenmenge_by_idSchuelerklausur
		update_schuelerklausurmenge_by_idKursklausur();
		update_schuelerklausurraumstundenmenge_by_idRaumstunde();
		update_schuelerklausurraumstundenmenge_by_idSchuelerklausur();
		update_klausurraum_by_idSchuelerklausur(); // benötigt _raumstundenmenge_by_idSchuelerklausur,

	}

	private void update_klausurraum_by_idStundenplanraum() {
		_klausurraum_by_idStundenplanraum.clear();
		for (final @NotNull GostKlausurraum raum : _raummenge)
			if (raum.idStundenplanRaum != null)
				DeveloperNotificationException.ifMapPutOverwrites(_klausurraum_by_idStundenplanraum, raum.idStundenplanRaum, raum);
	}

	private void update_raumstundenmenge_by_idRaum() {
		_raumstundenmenge_by_idRaum.clear();
		for (final @NotNull GostKlausurraumstunde krs : _raumstundenmenge)
			MapUtils.getOrCreateArrayList(_raumstundenmenge_by_idRaum, krs.idRaum).add(krs);
	}

	private void update_raumstunde_by_idRaum_and_idZeitraster() {
		_raumstunde_by_idRaum_and_idZeitraster.clear();
		for (final @NotNull GostKlausurraumstunde rs : _raumstundenmenge)
			DeveloperNotificationException.ifMap2DPutOverwrites(_raumstunde_by_idRaum_and_idZeitraster, rs.idRaum, rs.idZeitraster, rs);
	}

	private void update_raumstundenmenge_by_idSchuelerklausur() {
		_raumstundenmenge_by_idSchuelerklausur.clear();
		for (final @NotNull GostSchuelerklausurraumstunde skrs : _schuelerklausurraumstundenmenge)
			MapUtils.getOrCreateArrayList(_raumstundenmenge_by_idSchuelerklausur, skrs.idSchuelerklausur).add(DeveloperNotificationException.ifMapGetIsNull(_raumstunde_by_id, skrs.idRaumstunde));
	}

	private void update_schuelerklausurmenge_by_idRaum() {
		_schuelerklausurmenge_by_idRaum.clear();
		for (final @NotNull GostSchuelerklausur k : _schuelerklausurmenge) {
			final List<@NotNull GostKlausurraumstunde> raumstunden = _raumstundenmenge_by_idSchuelerklausur.get(k.idSchuelerklausur);
			MapUtils.getOrCreateArrayList(_schuelerklausurmenge_by_idRaum, raumstunden == null || raumstunden.isEmpty() ? -1L : raumstunden.get(0).idRaum).add(k);
		}
	}

	private void update_schuelerklausurmenge_by_idRaum_and_idKursklausur() {
		_schuelerklausurmenge_by_idRaum_and_idKursklausur.clear();
		for (final @NotNull GostSchuelerklausur k : _schuelerklausurmenge) {
			final List<@NotNull GostKlausurraumstunde> raumstunden = _raumstundenmenge_by_idSchuelerklausur.get(k.idSchuelerklausur);
			Map2DUtils.getOrCreateArrayList(_schuelerklausurmenge_by_idRaum_and_idKursklausur, raumstunden == null || raumstunden.isEmpty() ? -1L : raumstunden.get(0).idRaum, k.idKursklausur).add(k);
		}
	}

	private void update_schuelerklausurmenge_by_idKursklausur() {
		_schuelerklausurmenge_by_idKursklausur.clear();
		for (final @NotNull GostSchuelerklausur k : _schuelerklausurmenge)
			MapUtils.getOrCreateArrayList(_schuelerklausurmenge_by_idKursklausur, k.idKursklausur).add(k);
	}

	private void update_schuelerklausurraumstundenmenge_by_idRaumstunde() {
		_schuelerklausurraumstundenmenge_by_idRaumstunde.clear();
		for (final @NotNull GostSchuelerklausurraumstunde skrs : _schuelerklausurraumstundenmenge)
			MapUtils.getOrCreateArrayList(_schuelerklausurraumstundenmenge_by_idRaumstunde, skrs.idRaumstunde).add(skrs);
	}

	private void update_schuelerklausurraumstundenmenge_by_idSchuelerklausur() {
		_schuelerklausurraumstundenmenge_by_idSchuelerklausur.clear();
		for (final @NotNull GostSchuelerklausurraumstunde skrs : _schuelerklausurraumstundenmenge)
			MapUtils.getOrCreateArrayList(_schuelerklausurraumstundenmenge_by_idSchuelerklausur, skrs.idSchuelerklausur).add(skrs);
	}

	private void update_klausurraum_by_idSchuelerklausur() {
		_klausurraum_by_idSchuelerklausur.clear();
		for (final @NotNull GostSchuelerklausurraumstunde skrs : _schuelerklausurraumstundenmenge) {
			@NotNull
			final List<@NotNull GostKlausurraumstunde> krsList = DeveloperNotificationException.ifMapGetIsNull(_raumstundenmenge_by_idSchuelerklausur, skrs.idSchuelerklausur);
			for (@NotNull final GostKlausurraumstunde krs : krsList) {
				@NotNull
				final GostKlausurraum kr = DeveloperNotificationException.ifMapGetIsNull(_raum_by_id, krs.idRaum);
				final GostKlausurraum krAlt = _klausurraum_by_idSchuelerklausur.put(skrs.idSchuelerklausur, kr);
				if (krAlt != null && krAlt != kr)
					throw new DeveloperNotificationException("Schülerklausur " + skrs.idSchuelerklausur + " ist zwei Klausurräumen zugeordnet.");
			}
		}
	}

	// #####################################################################
	// #################### Klausurraum ################################
	// #####################################################################

	private void update_raummenge() {
		_raummenge.clear();
		_raummenge.addAll(_raum_by_id.values());
		_raummenge.sort(_compRaum);
	}

	/**
	 * Fügt ein {@link GostKlausurraum}-Objekt hinzu.
	 *
	 * @param raum Das {@link GostKlausurraum}-Objekt, welches hinzugefügt werden
	 *             soll.
	 */
	public void raumAdd(final @NotNull GostKlausurraum raum) {
		raumAddAll(ListUtils.create1(raum));
	}

	private void raumAddAllOhneUpdate(final @NotNull List<@NotNull GostKlausurraum> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostKlausurraum raum : list) {
			raumCheck(raum);
			DeveloperNotificationException.ifTrue("raumAddAllOhneUpdate: ID=" + raum.id + " existiert bereits!", _raum_by_id.containsKey(raum.id));
			DeveloperNotificationException.ifTrue("raumAddAllOhneUpdate: ID=" + raum.id + " doppelt in der Liste!", !setOfIDs.add(raum.id));
		}

		// add all
		for (final @NotNull GostKlausurraum raum : list)
			DeveloperNotificationException.ifMapPutOverwrites(_raum_by_id, raum.id, raum);
	}

	/**
	 * Fügt alle {@link GostKlausurraum}-Objekte hinzu.
	 *
	 * @param listRaum Die Menge der {@link GostKlausurraum}-Objekte, welche
	 *                 hinzugefügt werden soll.
	 */
	public void raumAddAll(final @NotNull List<@NotNull GostKlausurraum> listRaum) {
		raumAddAllOhneUpdate(listRaum);
		update_all();
	}

	private static void raumCheck(final @NotNull GostKlausurraum raum) {
		DeveloperNotificationException.ifInvalidID("raum.id", raum.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostKlausurraum}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idRaum Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKlausurraum}-Objekt.
	 */
	public @NotNull GostKlausurraum raumGetByIdOrException(final long idRaum) {
		return DeveloperNotificationException.ifMapGetIsNull(_raum_by_id, idRaum);
	}

	/**
	 * Liefert eine Liste aller {@link GostKlausurraum}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKlausurraum}-Objekte.
	 */
	public @NotNull List<@NotNull GostKlausurraum> raumGetMengeAsList() {
		return _raummenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKlausurraum}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param raum Das neue {@link GostKlausurraum}-Objekt.
	 */
	public void raumPatchAttributes(final @NotNull GostKlausurraum raum) {
		raumCheck(raum);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_raum_by_id, raum.id);
		DeveloperNotificationException.ifMapPutOverwrites(_raum_by_id, raum.id, raum);

		update_all();
	}

	private void raumRemoveOhneUpdateById(final long idRaum) {
		DeveloperNotificationException.ifMapRemoveFailes(_raum_by_id, idRaum);
		final List<@NotNull GostKlausurraumstunde> rsList = _raumstundenmenge_by_idRaum.get(idRaum);
		if (rsList != null)
			for (@NotNull final GostKlausurraumstunde rs : rsList)
				raumstundeRemoveOhneUpdateById(rs.id);
	}

	/**
	 * Entfernt ein existierendes {@link GostKlausurraum}-Objekt.
	 *
	 * @param idRaum Die ID des {@link GostKlausurraum}-Objekts.
	 */
	public void raumRemoveById(final long idRaum) {
		raumRemoveOhneUpdateById(idRaum);

		update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanRaum}-Objekte.
	 *
	 * @param listRaum Die Liste der zu entfernenden
	 *                 {@link StundenplanRaum}-Objekte.
	 */
	public void raumRemoveAll(final @NotNull List<@NotNull GostKlausurraum> listRaum) {
		for (final @NotNull GostKlausurraum raum : listRaum)
			raumRemoveOhneUpdateById(raum.id);

		update_all();
	}

	// #####################################################################
	// #################### Klausurraumstunde ################################
	// #####################################################################

	private void update_raumstundenmenge() {
		_raumstundenmenge.clear();
		_raumstundenmenge.addAll(_raumstunde_by_id.values());
//		_raumstundenmenge.sort(_compRaum);
	}

	/**
	 * Fügt ein {@link GostKlausurraumstunde}-Objekt hinzu.
	 *
	 * @param raumstunde Das {@link GostKlausurraumstunde}-Objekt, welches
	 *                   hinzugefügt werden soll.
	 */
	public void raumstundeAdd(final @NotNull GostKlausurraumstunde raumstunde) {
		raumstundeAddAll(ListUtils.create1(raumstunde));
	}

	private void raumstundeAddAllOhneUpdate(final @NotNull List<@NotNull GostKlausurraumstunde> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostKlausurraumstunde raumstunde : list) {
			raumstundeCheck(raumstunde);
			DeveloperNotificationException.ifTrue("raumstundeAddAllOhneUpdate: ID=" + raumstunde.id + " existiert bereits!", _raumstunde_by_id.containsKey(raumstunde.id));
			DeveloperNotificationException.ifTrue("raumstundeAddAllOhneUpdate: ID=" + raumstunde.id + " doppelt in der Liste!", !setOfIDs.add(raumstunde.id));
		}

		// add all
		for (final @NotNull GostKlausurraumstunde raumstunde : list)
			DeveloperNotificationException.ifMapPutOverwrites(_raumstunde_by_id, raumstunde.id, raumstunde);
	}

	/**
	 * Fügt alle {@link GostKlausurraumstunde}-Objekte hinzu.
	 *
	 * @param listRaumstunde Die Menge der {@link GostKlausurraumstunde}-Objekte,
	 *                       welche hinzugefügt werden soll.
	 */
	public void raumstundeAddAll(final @NotNull List<@NotNull GostKlausurraumstunde> listRaumstunde) {
		raumstundeAddAllOhneUpdate(listRaumstunde);
		update_all();
	}

	private static void raumstundeCheck(final @NotNull GostKlausurraumstunde raumstunde) {
		DeveloperNotificationException.ifInvalidID("raumstunde.id", raumstunde.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostKlausurraumstunde}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idRaumstunde Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKlausurraumstunde}-Objekt.
	 */
	public @NotNull GostKlausurraumstunde raumstundeGetByIdOrException(final long idRaumstunde) {
		return DeveloperNotificationException.ifMapGetIsNull(_raumstunde_by_id, idRaumstunde);
	}

	/**
	 * Liefert eine Liste aller {@link GostKlausurraumstunde}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKlausurraumstunde}-Objekte.
	 */
	public @NotNull List<@NotNull GostKlausurraumstunde> raumstundeGetMengeAsList() {
		return _raumstundenmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKlausurraumstunde}-Objekt durch das
	 * neue Objekt.
	 *
	 * @param raumstunde Das neue {@link GostKlausurraumstunde}-Objekt.
	 */
	public void raumstundePatchAttributes(final @NotNull GostKlausurraumstunde raumstunde) {
		raumstundeCheck(raumstunde);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_raumstunde_by_id, raumstunde.id);
		DeveloperNotificationException.ifMapPutOverwrites(_raumstunde_by_id, raumstunde.id, raumstunde);

		update_all();
	}

	private void raumstundeRemoveOhneUpdateById(final long idRaumstunde) {
		DeveloperNotificationException.ifMapRemoveFailes(_raumstunde_by_id, idRaumstunde);
		final List<@NotNull GostSchuelerklausurraumstunde> skrsList = _schuelerklausurraumstundenmenge_by_idRaumstunde.get(idRaumstunde);
		if (skrsList != null)
			for (@NotNull final GostSchuelerklausurraumstunde skrs : skrsList)
				schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurAndIdRaumstunde(skrs.idSchuelerklausur, skrs.idRaumstunde);
	}

	/**
	 * Entfernt ein existierendes {@link GostKlausurraumstunde}-Objekt.
	 *
	 * @param idRaumstunde Die ID des {@link GostKlausurraumstunde}-Objekts.
	 */
	public void raumstundeRemoveById(final long idRaumstunde) {
		raumstundeRemoveOhneUpdateById(idRaumstunde);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostKlausurraumstunde}-Objekte.
	 *
	 * @param listRaumstunde Die Liste der zu entfernenden
	 *                       {@link GostKlausurraumstunde}-Objekte.
	 */
	public void raumstundeRemoveAll(final @NotNull List<@NotNull GostKlausurraumstunde> listRaumstunde) {
		for (final @NotNull GostKlausurraumstunde raumstunde : listRaumstunde)
			raumstundeRemoveOhneUpdateById(raumstunde.id);

		update_all();
	}

	// #####################################################################
	// #################### Schülerklausur ################################
	// #####################################################################

	private void update_schuelerklausurmenge() {
		_schuelerklausurmenge.clear();
		_schuelerklausurmenge.addAll(_schuelerklausur_by_id.values());
//		_schuelerklausurmenge.sort(_compRaum);
	}

	/**
	 * Fügt ein {@link GostSchuelerklausur}-Objekt hinzu.
	 *
	 * @param schuelerklausur Das {@link GostSchuelerklausur}-Objekt, welches
	 *                        hinzugefügt werden soll.
	 */
	public void schuelerklausurAdd(final @NotNull GostSchuelerklausur schuelerklausur) {
		schuelerklausurAddAll(ListUtils.create1(schuelerklausur));
	}

	private void schuelerklausurAddAllOhneUpdate(final @NotNull List<@NotNull GostSchuelerklausur> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostSchuelerklausur schuelerklausur : list) {
			schuelerklausurCheck(schuelerklausur);
			DeveloperNotificationException.ifTrue("schuelerklausurAddAllOhneUpdate: ID=" + schuelerklausur.idSchuelerklausur + " existiert bereits!", _schuelerklausur_by_id.containsKey(schuelerklausur.idSchuelerklausur));
			DeveloperNotificationException.ifTrue("schuelerklausurAddAllOhneUpdate: ID=" + schuelerklausur.idSchuelerklausur + " doppelt in der Liste!", !setOfIDs.add(schuelerklausur.idSchuelerklausur));
		}

		// add all
		for (final @NotNull GostSchuelerklausur schuelerklausur : list)
			DeveloperNotificationException.ifMapPutOverwrites(_schuelerklausur_by_id, schuelerklausur.idSchuelerklausur, schuelerklausur);
	}

	/**
	 * Fügt alle {@link GostSchuelerklausur}-Objekte hinzu.
	 *
	 * @param listSchuelerklausur Die Menge der {@link GostSchuelerklausur}-Objekte,
	 *                            welche hinzugefügt werden soll.
	 */
	public void schuelerklausurAddAll(final @NotNull List<@NotNull GostSchuelerklausur> listSchuelerklausur) {
		schuelerklausurAddAllOhneUpdate(listSchuelerklausur);
		update_all();
	}

	private static void schuelerklausurCheck(final @NotNull GostSchuelerklausur schuelerklausur) {
		DeveloperNotificationException.ifInvalidID("schuelerklausur.idSchuelerklausur", schuelerklausur.idSchuelerklausur);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostSchuelerklausur}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idSchuelerklausur Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostSchuelerklausur}-Objekt.
	 */
	public @NotNull GostSchuelerklausur schuelerklausurGetByIdOrException(final long idSchuelerklausur) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerklausur_by_id, idSchuelerklausur);
	}

	/**
	 * Liefert eine Liste aller {@link GostSchuelerklausur}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostSchuelerklausur}-Objekte.
	 */
	public @NotNull List<@NotNull GostSchuelerklausur> schuelerklausurGetMengeAsList() {
		return _schuelerklausurmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostSchuelerklausur}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param schuelerklausur Das neue {@link GostSchuelerklausur}-Objekt.
	 */
	public void schuelerklausurPatchAttributes(final @NotNull GostSchuelerklausur schuelerklausur) {
		schuelerklausurCheck(schuelerklausur);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_schuelerklausur_by_id, schuelerklausur.idSchuelerklausur);
		DeveloperNotificationException.ifMapPutOverwrites(_schuelerklausur_by_id, schuelerklausur.idSchuelerklausur, schuelerklausur);

		update_all();
	}

	private void schuelerklausurRemoveOhneUpdateById(final long idSchuelerklausur) {
		DeveloperNotificationException.ifMapRemoveFailes(_schuelerklausur_by_id, idSchuelerklausur);
		schuelerklausurraumstundenmengeRemoveOhneUpdateByIdSchuelerklausur(idSchuelerklausur);
	}

	/**
	 * Entfernt ein existierendes {@link GostSchuelerklausur}-Objekt.
	 *
	 * @param idSchuelerklausur Die ID des {@link GostSchuelerklausur}-Objekts.
	 */
	public void schuelerklausurRemoveById(final long idSchuelerklausur) {
		schuelerklausurRemoveOhneUpdateById(idSchuelerklausur);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausur}-Objekte.
	 *
	 * @param listSchuelerklausur Die Liste der zu entfernenden
	 *                            {@link GostSchuelerklausur}-Objekte.
	 */
	public void schuelerklausurRemoveAll(final @NotNull List<@NotNull GostSchuelerklausur> listSchuelerklausur) {
		for (final @NotNull GostSchuelerklausur schuelerklausur : listSchuelerklausur)
			schuelerklausurRemoveOhneUpdateById(schuelerklausur.idSchuelerklausur);

		update_all();
	}

	// #####################################################################
	// #################### Schuelerklausurraumstunde
	// ################################
	// #####################################################################

	private void update_schuelerklausurraumstundenmenge() {
		_schuelerklausurraumstundenmenge.clear();
		_schuelerklausurraumstundenmenge.addAll(_schuelerklausurraumstunde_by_idSchuelerklausur_and_idRaumstunde.getNonNullValuesAsList());
//		_schuelerklausurraumstundenmenge.sort(_compRaum);
	}

	/**
	 * Fügt ein {@link GostSchuelerklausurraumstunde}-Objekt hinzu.
	 *
	 * @param schuelerklausurraumstunde Das
	 *                                  {@link GostSchuelerklausurraumstunde}-Objekt,
	 *                                  welches hinzugefügt werden soll.
	 */
	public void schuelerklausurraumstundeAdd(final @NotNull GostSchuelerklausurraumstunde schuelerklausurraumstunde) {
		schuelerklausurraumstundeAddAll(ListUtils.create1(schuelerklausurraumstunde));
	}

	private void schuelerklausurraumstundeAddAllOhneUpdate(final @NotNull List<@NotNull GostSchuelerklausurraumstunde> list) {
		// check all
		final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull GostSchuelerklausurraumstunde> setOfIDs = new HashMap2D<>();
		for (final @NotNull GostSchuelerklausurraumstunde schuelerklausurraumstunde : list) {
			schuelerklausurraumstundeCheck(schuelerklausurraumstunde);
			DeveloperNotificationException.ifTrue("schuelerklausurraumstundeAddAllOhneUpdate: ID=(" + schuelerklausurraumstunde.idSchuelerklausur + "," + schuelerklausurraumstunde.idRaumstunde + ") existiert bereits!", _schuelerklausurraumstunde_by_idSchuelerklausur_and_idRaumstunde.contains(schuelerklausurraumstunde.idSchuelerklausur, schuelerklausurraumstunde.idRaumstunde));
			DeveloperNotificationException.ifTrue("schuelerklausurraumstundeAddAllOhneUpdate: ID=" + schuelerklausurraumstunde.idSchuelerklausur + "," + schuelerklausurraumstunde.idRaumstunde + ") doppelt in der Liste!", setOfIDs.contains(schuelerklausurraumstunde.idSchuelerklausur, schuelerklausurraumstunde.idRaumstunde));
			setOfIDs.put(schuelerklausurraumstunde.idSchuelerklausur, schuelerklausurraumstunde.idRaumstunde, schuelerklausurraumstunde);
		}

		// add all
		for (final @NotNull GostSchuelerklausurraumstunde schuelerklausurraumstunde : list)
			DeveloperNotificationException.ifMap2DPutOverwrites(_schuelerklausurraumstunde_by_idSchuelerklausur_and_idRaumstunde, schuelerklausurraumstunde.idSchuelerklausur, schuelerklausurraumstunde.idRaumstunde, schuelerklausurraumstunde);
	}

	/**
	 * Fügt alle {@link GostSchuelerklausurraumstunde}-Objekte hinzu.
	 *
	 * @param listSchuelerklausurraumstunde Die Menge der
	 *                                      {@link GostSchuelerklausurraumstunde}-Objekte,
	 *                                      welche hinzugefügt werden soll.
	 */
	public void schuelerklausurraumstundeAddAll(final @NotNull List<@NotNull GostSchuelerklausurraumstunde> listSchuelerklausurraumstunde) {
		schuelerklausurraumstundeAddAllOhneUpdate(listSchuelerklausurraumstunde);
		update_all();
	}

	private static void schuelerklausurraumstundeCheck(final @NotNull GostSchuelerklausurraumstunde schuelerklausurraumstunde) {
		DeveloperNotificationException.ifInvalidID("schuelerklausurraumstunde.idSchuelerklausur", schuelerklausurraumstunde.idSchuelerklausur);
		DeveloperNotificationException.ifInvalidID("schuelerklausurraumstunde.idRaumstunde", schuelerklausurraumstunde.idRaumstunde);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostSchuelerklausurraumstunde}-Objekt.
	 * <br>
	 * Laufzeit: O(1)
	 *
	 * @param idSchuelerklausur Die ID des {@link GostSchuelerklausur}-Objekts.
	 * @param idRaumstunde      Die ID des {@link GostKlausurraumstunde}-Objekts.
	 *
	 * @return das zur ID zugehörige {@link GostSchuelerklausurraumstunde}-Objekt.
	 */
	public @NotNull GostSchuelerklausurraumstunde schuelerklausurraumstundeGetByIdSchuelerklausurAndIdRaumstundeOrException(final long idSchuelerklausur, final long idRaumstunde) {
		return DeveloperNotificationException.ifMap2DGetIsNull(_schuelerklausurraumstunde_by_idSchuelerklausur_and_idRaumstunde, idSchuelerklausur, idRaumstunde);
	}

	/**
	 * Liefert eine Liste aller {@link GostSchuelerklausurraumstunde}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostSchuelerklausurraumstunde}-Objekte.
	 */
	public @NotNull List<@NotNull GostSchuelerklausurraumstunde> schuelerklausurraumstundeGetMengeAsList() {
		return _schuelerklausurraumstundenmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostSchuelerklausurraumstunde}-Objekt
	 * durch das neue Objekt.
	 *
	 * @param schuelerklausurraumstunde Das neue
	 *                                  {@link GostSchuelerklausurraumstunde}-Objekt.
	 */
	public void schuelerklausurraumstundePatchAttributes(final @NotNull GostSchuelerklausurraumstunde schuelerklausurraumstunde) {
		schuelerklausurraumstundeCheck(schuelerklausurraumstunde);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMap2DRemoveFailes(_schuelerklausurraumstunde_by_idSchuelerklausur_and_idRaumstunde, schuelerklausurraumstunde.idSchuelerklausur,
				schuelerklausurraumstunde.idRaumstunde);
		DeveloperNotificationException.ifMap2DPutOverwrites(_schuelerklausurraumstunde_by_idSchuelerklausur_and_idRaumstunde, schuelerklausurraumstunde.idSchuelerklausur,
				schuelerklausurraumstunde.idRaumstunde, schuelerklausurraumstunde);

		update_all();
	}

	private void schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurAndIdRaumstunde(final long idSchuelerklausur, final long idRaumstunde) {
		DeveloperNotificationException.ifMap2DRemoveFailes(_schuelerklausurraumstunde_by_idSchuelerklausur_and_idRaumstunde, idSchuelerklausur, idRaumstunde);
	}

	private void schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausur(final long idSchuelerklausur) {
		_schuelerklausurraumstunde_by_idSchuelerklausur_and_idRaumstunde.removeSubMap(idSchuelerklausur);
	}

	private void schuelerklausurraumstundenmengeRemoveOhneUpdateByIdSchuelerklausur(final long idSchuelerklausur) {
		final List<@NotNull GostSchuelerklausurraumstunde> skrsList = _schuelerklausurraumstundenmenge_by_idSchuelerklausur.get(idSchuelerklausur);
		if (skrsList != null)
			for (@NotNull final GostSchuelerklausurraumstunde skrs : skrsList)
				DeveloperNotificationException.ifMap2DRemoveFailes(_schuelerklausurraumstunde_by_idSchuelerklausur_and_idRaumstunde, skrs.idSchuelerklausur, skrs.idRaumstunde);
	}

	/**
	 * Entfernt ein existierendes {@link GostSchuelerklausurraumstunde}-Objekt.
	 *
	 * @param idSchuelerklausur Die ID des {@link GostSchuelerklausur}-Objekts.
	 * @param idRaumstunde      Die ID des {@link GostKlausurraumstunde}-Objekts.
	 */
	public void schuelerklausurraumstundeRemoveByIdSchuelerklausurAndIdRaumstunde(final long idSchuelerklausur, final long idRaumstunde) {
		schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurAndIdRaumstunde(idSchuelerklausur, idRaumstunde);

		update_all();
	}

	/**
	 * Entfernt ein existierendes {@link GostSchuelerklausurraumstunde}-Objekt.
	 *
	 * @param idSchuelerklausur Die ID des {@link GostSchuelerklausur}-Objekts.
	 */
	public void schuelerklausurraumstundeRemoveByIdSchuelerklausur(final long idSchuelerklausur) {
		schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausur(idSchuelerklausur);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurraumstunde}-Objekte, deren Schülerklausur-ID in der übergebenen Liste enthalten ist.
	 *
	 * @param idsSchuelerklausuren die Liste der Schülerklausur-IDs.
	 */
	public void schuelerklausurraumstundeRemoveAllByIdSchuelerklausur(final @NotNull List<@NotNull Long> idsSchuelerklausuren) {
		for (final long idSchuelerklausur : idsSchuelerklausuren)
			schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausur(idSchuelerklausur);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurraumstunde}-Objekte.
	 *
	 * @param listSchuelerklausurRaumstunde Die Liste der zu entfernenden
	 *                                      {@link GostSchuelerklausurraumstunde}-Objekte.
	 */
	public void schuelerklausurraumstundeRemoveAll(final @NotNull List<@NotNull GostSchuelerklausurraumstunde> listSchuelerklausurRaumstunde) {
		for (final @NotNull GostSchuelerklausurraumstunde schuelerklausurraumstunde : listSchuelerklausurRaumstunde)
			schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurAndIdRaumstunde(schuelerklausurraumstunde.idSchuelerklausur, schuelerklausurraumstunde.idRaumstunde);

		update_all();
	}

	// ################################################################################

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param idRaum       die ID des Klausurraums
	 * @param idZeitraster die ID des Zeitrasters
	 *
	 * @return die Klausurraumstunde
	 */
	public GostKlausurraumstunde klausurraumstundeGetByRaumidAndZeitrasterid(final long idRaum, final long idZeitraster) {
		return _raumstunde_by_idRaum_and_idZeitraster.getOrNull(idRaum, idZeitraster);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param idRaum       die ID des Klausurraums
	 *
	 * @return die Klausurraumstunde
	 */
	public @NotNull List<@NotNull GostKlausurraumstunde> klausurraumstundeGetMengeByRaumid(final long idRaum) {
		final List<@NotNull GostKlausurraumstunde> stunden = _raumstundenmenge_by_idRaum.get(idRaum);
		return stunden != null ? stunden : new ArrayList<>();
	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich der Klausurraum geändert
	 * hat.
	 *
	 * @param collectionSkrsKrs das GostKlausurraum-Objekt
	 */
	public void setzeRaumZuSchuelerklausuren(final @NotNull GostKlausurenCollectionSkrsKrs collectionSkrsKrs) {
		raumstundeRemoveAll(collectionSkrsKrs.raumstundenGeloescht);
		raumstundeAddAll(collectionSkrsKrs.raumstunden);
		schuelerklausurraumstundeRemoveAllByIdSchuelerklausur(collectionSkrsKrs.idsSchuelerklausuren);
		schuelerklausurraumstundeAddAll(collectionSkrsKrs.skRaumstunden);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public @NotNull List<@NotNull GostKursklausur> kursklausurGetMenge() {
		final List<@NotNull GostKursklausur> kursklausuren = new ArrayList<>();
		for (final long kkId : _schuelerklausurmenge_by_idKursklausur.keySet()) {
			kursklausuren.add(_kursklausurManager.kursklausurGetByIdOrException(kkId));
		}
		return kursklausuren;
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param idKursklausur die Id der Kursklausur
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public @NotNull List<@NotNull GostSchuelerklausur> schuelerklausurGetMengeByKursklausurid(final long idKursklausur) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerklausurmenge_by_idKursklausur, idKursklausur);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param idRaum  die Id des Klausurraums
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public @NotNull List<@NotNull GostKursklausur> kursklausurGetMengeByRaumid(final long idRaum) {
		final List<@NotNull GostKursklausur> kursklausuren = new ArrayList<>();
		if (!_schuelerklausurmenge_by_idRaum_and_idKursklausur.containsKey1(idRaum))
			return kursklausuren;
		for (final long idKK : _schuelerklausurmenge_by_idRaum_and_idKursklausur.getKeySetOf(idRaum)) {
			if (!_schuelerklausurmenge_by_idRaum_and_idKursklausur.getNonNullOrException(idRaum, idKK).isEmpty())
				kursklausuren.add(_kursklausurManager.kursklausurGetByIdOrException(idKK));
		}
		return kursklausuren;
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param idRaum  die Id des Klausurraums
	 * @param idKursklausur die Id der Kursklausur
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public @NotNull List<@NotNull GostSchuelerklausur> schuelerklausurGetMengeByRaumidAndKursklausurid(final long idRaum, final long idKursklausur) {
		return DeveloperNotificationException.ifMap2DGetIsNull(_schuelerklausurmenge_by_idRaum_and_idKursklausur, idRaum, idKursklausur);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param idRaum  die Id des Klausurraums
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public @NotNull List<@NotNull GostSchuelerklausur> schuelerklausurGetMengeByRaumid(final long idRaum) {
		final List<@NotNull GostSchuelerklausur> schuelerklausuren = new ArrayList<>();
		if (!_schuelerklausurmenge_by_idRaum_and_idKursklausur.containsKey1(idRaum))
			return schuelerklausuren;
		for (final long idKK : _schuelerklausurmenge_by_idRaum_and_idKursklausur.getKeySetOf(idRaum))
			schuelerklausuren.addAll(_schuelerklausurmenge_by_idRaum_and_idKursklausur.getNonNullOrException(idRaum, idKK));
		return schuelerklausuren;
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public @NotNull List<@NotNull GostSchuelerklausur> schuelerklausurOhneRaumGetMenge() {
		return schuelerklausurGetMengeByRaumid(-1L);
	}

	/**
	 * Liefert eine Liste von Stundenplanräumen, die nicht für diesen Klausurtermin
	 * verplant sind.
	 *
	 * @param alleRaeume die Liste aller Stundenplanräume
	 *
	 * @return die Liste der nicht verplanten StundenplanRäume
	 */
	public @NotNull List<@NotNull StundenplanRaum> stundenplanraumVerfuegbarGetMenge(@NotNull final List<@NotNull StundenplanRaum> alleRaeume) {
		final List<@NotNull StundenplanRaum> raeume = new ArrayList<>();
		for (@NotNull final StundenplanRaum raum : alleRaeume)
			if (!_klausurraum_by_idStundenplanraum.containsKey(raum.id))
				raeume.add(raum);
		return raeume;
	}

	/**
	 * Prüft, ob alle zu einer Kursklausur gehörenden Schülerklausuren einem Raum
	 * zugeordnet sind.
	 *
	 * @param kk die zu prüfende Kursklausur
	 *
	 * @return true, wenn alle Schülerklausuren verplant sind, sonst false
	 */
	public boolean isAlleSchuelerklausurenVerplant(final @NotNull GostKursklausur kk) {
		for (@NotNull final GostSchuelerklausur sk : DeveloperNotificationException.ifMapGetIsNull(_schuelerklausurmenge_by_idKursklausur, kk.id)) {
			if (!_raumstundenmenge_by_idSchuelerklausur.containsKey(sk.idSchuelerklausur))
				return false;
		}
		return true;
	}

	/**
	 * Prüft, ob eine Kursklausur im übergebenen Klausurraum enthalten ist.
	 *
	 * @param idRaum der Raum, in dem die Kursklausur geprüft wird
	 * @param idKursklausur die zu prüfende Kursklausur
	 *
	 * @return true, wenn enthalten, sonst false
	 */
	public boolean containsKlausurraumKursklausur(final long idRaum, final long idKursklausur) {
		return _schuelerklausurmenge_by_idRaum_and_idKursklausur.contains(idRaum, idKursklausur);
	}

	/**
	 * Liefert den enthaltenen Gost-KursklausurManager zurück
	 *
	 * @return den KursklausurManager
	 */
	public @NotNull GostKursklausurManager getKursklausurManager() {
		return _kursklausurManager;
	}

	/**
	 * Liefert die gemeinsame Klausurdauer aller Kursklausuren, die im übergebenen Raum geschrieben werden.
	 * Falls die Dauern sich unterscheiden, wird null zurückgegeben.
	 *
	 * @param raum der Klausurraum, dessen Klausurdauern überprüft werden.
	 *
	 * @return die gemeinsame Klausurdauer aller Kursklausuren, falls keine solche existiert, null
	 */
	public Integer getGemeinsameKursklausurdauerByKlausurraum(final @NotNull GostKlausurraum raum) {
		int dauer = -1;
		for (@NotNull GostKursklausur klausur : kursklausurGetMengeByRaumid(raum.id)) {
			@NotNull GostKlausurvorgabe vorgabe = _kursklausurManager.vorgabeByKursklausur(klausur);
			if (dauer == -1)
				dauer = vorgabe.dauer;
			if (dauer != vorgabe.dauer)
				return null;
		}
		return dauer;
	}

	/**
	 * Liefert die gemeinsame Klausurdauer aller Kursklausuren, die im übergebenen Raum geschrieben werden.
	 * Falls die Dauern sich unterscheiden, wird null zurückgegeben.
	 *
	 * @param raum der Klausurraum, dessen Klausurdauern überprüft werden.
	 *
	 * @return die gemeinsame Klausurdauer aller Kursklausuren, falls keine solche existiert, null
	 */
	public Integer getGemeinsamerKursklausurstartByKlausurraum(final @NotNull GostKlausurraum raum) {
		Integer start = -1;
		for (@NotNull GostKursklausur klausur : kursklausurGetMengeByRaumid(raum.id)) {
			if (start != null && start == -1)
				start = klausur.startzeit;
			if (_kursklausurManager.hatAbweichendeStartzeitByKursklausur(klausur))
				return null;
		}
		return start == null ? _termin.startzeit : start;
	}

}
