package de.svws_nrw.core.utils.gost.klausurplanung;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkrsKrs;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumRich;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTerminRich;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurterminraumstunde;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.core.utils.Map2DUtils;
import de.svws_nrw.core.utils.MapUtils;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link GostKlausurraum}.
 * Hierbei werden auch Hilfsmethoden zur Interpretation der Daten erzeugt. Es
 * werden Klausurräume eines Gost-Klausurtermins verwaltet.
 */
public class GostKlausurraumManager {

	private final @NotNull GostKursklausurManager _kursklausurManager;
	private StundenplanManager _stundenplanManager;
	private final @NotNull GostKlausurtermin _termin;

	/** Ein Comparator für die GostKlausurräume. */
	private static final @NotNull Comparator<GostKlausurraum> _compRaum =
			(final @NotNull GostKlausurraum a, final @NotNull GostKlausurraum b) -> Long.compare(a.id, b.id);

	// GostKlausurraum
	private final @NotNull Map<Long, GostKlausurraum> _raum_by_id = new HashMap<>();
	private final @NotNull List<GostKlausurraum> _raummenge = new ArrayList<>();
	private final @NotNull Map<Long, List<GostKlausurraum>> _raummenge_by_idTermin = new HashMap<>();
	private final @NotNull Map<Long, GostKlausurraum> _klausurraum_by_idStundenplanraum = new HashMap<>();
	private final @NotNull Map<Long, GostKlausurraum> _klausurraum_by_idSchuelerklausurtermin = new HashMap<>();

	// GostKlausurraumstunde
	private final @NotNull Map<Long, GostKlausurraumstunde> _raumstunde_by_id = new HashMap<>();
	private final @NotNull List<GostKlausurraumstunde> _raumstundenmenge = new ArrayList<>();
	private final @NotNull Map<Long, List<GostKlausurraumstunde>> _raumstundenmenge_by_idRaum = new HashMap<>();
	private final @NotNull HashMap2D<Long, Long, GostKlausurraumstunde> _raumstunde_by_idRaum_and_idZeitraster = new HashMap2D<>();
	private final @NotNull Map<Long, List<GostKlausurraumstunde>> _raumstundenmenge_by_idSchuelerklausurtermin = new HashMap<>();

	// GostSchuelerklausurTermin
	private final @NotNull Map<Long, GostSchuelerklausurTermin> _schuelerklausurtermin_by_id = new HashMap<>();
	private final @NotNull List<GostSchuelerklausurTermin> _schuelerklausurterminmenge = new ArrayList<>();
	private final @NotNull Map<Long, List<GostSchuelerklausurTermin>> _schuelerklausurterminmenge_by_idRaum = new HashMap<>();
	private final @NotNull HashMap2D<Long, Long, List<GostSchuelerklausurTermin>> _schuelerklausurterminmenge_by_idRaum_and_idTermin = new HashMap2D<>();
	private final @NotNull HashMap2D<Long, Long, List<GostSchuelerklausurTermin>> _schuelerklausurterminmenge_by_idRaum_and_idKursklausur = new HashMap2D<>();
	private final @NotNull Map<Long, List<GostSchuelerklausurTermin>> _schuelerklausurterminmenge_by_idKursklausur = new HashMap<>();
	private final @NotNull Map<Long, List<GostSchuelerklausurTermin>> _schuelerklausurterminmenge_by_idTermin = new HashMap<>();

	// GostSchuelerklausurraumstunde
	private final @NotNull HashMap2D<Long, Long, GostSchuelerklausurterminraumstunde> _schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde =
			new HashMap2D<>();
	private final @NotNull List<GostSchuelerklausurterminraumstunde> _schuelerklausurterminraumstundenmenge = new ArrayList<>();
	private final @NotNull Map<Long, List<GostSchuelerklausurterminraumstunde>> _schuelerklausurterminraumstundenmenge_by_idRaumstunde = new HashMap<>();
	private final @NotNull Map<Long, List<GostSchuelerklausurterminraumstunde>> _schuelerklausurraumstundenmenge_by_idSchuelerklausurtermin = new HashMap<>();

	/**
	 * Erstellt einen leeren Manager.
	 *
	 */
	public GostKlausurraumManager() {
		_termin = new GostKlausurtermin();
		_kursklausurManager = new GostKursklausurManager();
		_stundenplanManager = null;
	}

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param raum              der Gost-Klausurraum
	 * @param stunden           die Liste der GostKlausurraumstunden eines
	 *                          Gost-Klausurtermins
	 * @param listSchuelerklausurterminIds die Liste der GostSchuelerklausuren des
	 *                          Gost-Klausurtermins
	 * @param kursklausurmanager der Kursklausur-Manager
	 * @param termin              der Gost-Klausurtermin
	 */
	public GostKlausurraumManager(final @NotNull GostKlausurraum raum, final @NotNull List<GostKlausurraumstunde> stunden,
			final @NotNull List<Long> listSchuelerklausurterminIds, final @NotNull GostKursklausurManager kursklausurmanager,
			final @NotNull GostKlausurtermin termin) {
		_kursklausurManager = kursklausurmanager;
		_termin = termin;
		final List<GostKlausurraum> raeume = new ArrayList<>();
		raeume.add(raum);
		initAll(raeume, stunden, new ArrayList<>(), listSchuelerklausurterminIds);
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
	 * @param listSchuelerklausurterminIds die Liste der GostSchuelerklausuren des
	 *                          Gost-Klausurtermins
	 * @param kursklausurmanager der Kursklausur-Manager
	 * @param termin              der Gost-Klausurtermin
	 */
	public GostKlausurraumManager(final @NotNull List<GostKlausurraum> raeume, final @NotNull List<GostKlausurraumstunde> listRs,
			final @NotNull List<GostSchuelerklausurterminraumstunde> listSkrs, final @NotNull List<Long> listSchuelerklausurterminIds,
			final @NotNull GostKursklausurManager kursklausurmanager, final @NotNull GostKlausurtermin termin) {
		_kursklausurManager = kursklausurmanager;
		_termin = termin;
		initAll(raeume, listRs, listSkrs, listSchuelerklausurterminIds);
	}

	private void initAll(final @NotNull List<GostKlausurraum> listRaum, final @NotNull List<GostKlausurraumstunde> listRaumstunde,
			final @NotNull List<GostSchuelerklausurterminraumstunde> listSchuelerklausurraumstunde,
			final @NotNull List<Long> listSchuelerklausurterminIds) {

		raumAddAllOhneUpdate(listRaum);
		raumstundeAddAllOhneUpdate(listRaumstunde);
		for (final @NotNull Long skt : listSchuelerklausurterminIds)
			schuelerklausurAddOhneUpdate(_kursklausurManager.schuelerklausurterminGetByIdOrException(skt));
		schuelerklausurraumstundeAddAllOhneUpdate(listSchuelerklausurraumstunde);

		update_all();

	}

	/**
	 * Setzt den StundenplanManager
	 *
	 * @param stundenplanManager der StundenplanManager
	 */
	public void setStundenplanManager(final @NotNull StundenplanManager stundenplanManager) {
		_stundenplanManager = stundenplanManager;
	}

	/**
	 * Liefert den StundenplanManager, falls dieser gesetzt ist, sonst wird eine DeveloperNotificationException geworfen.
	 *
	 * @return den StundenplanManager
	 */
	public @NotNull StundenplanManager getStundenplanManager() {
		if (_stundenplanManager == null)
			throw new DeveloperNotificationException("StundenplanManager not set.");
		return _stundenplanManager;
	}

	/**
	 * Liefert den zu diesem Manager gehörenden Haupt-Klausurtermin.
	 *
	 * @return das GostKlausurtermin-Objekt
	 */
	public @NotNull GostKlausurtermin getHauptTermin() {
		return _termin;
	}

	private void update_all() {

		update_raummenge();
		update_raumstundenmenge();
		update_schuelerklausurmenge();
		update_schuelerklausurraumstundenmenge();

		update_klausurraum_by_idStundenplanraum();
		update_raummenge_by_idTermin();
		update_raumstundenmenge_by_idRaum();
		update_raumstunde_by_idRaum_and_idZeitraster();
		update_raumstundenmenge_by_idSchuelerklausurtermin(); // benötigt _raumstunde_by_id
		update_schuelerklausurterminmenge_by_idTermin();
		update_schuelerklausurterminmenge_by_idRaum(); // benötigt _raumstundenmenge_by_idSchuelerklausur
		update_schuelerklausurterminmenge_by_idRaum_and_idTermin(); // benötigt _raumstundenmenge_by_idSchuelerklausur
		update_schuelerklausurterminmenge_by_idRaum_and_idKursklausur(); // benötigt _raumstundenmenge_by_idSchuelerklausur
		update_schuelerklausurterminmenge_by_idKursklausur();
		update_schuelerklausurterminraumstundenmenge_by_idRaumstunde();
		update_schuelerklausurraumstundenmenge_by_idSchuelerklausur();
		update_klausurraum_by_idSchuelerklausurtermin(); // benötigt _raumstundenmenge_by_idSchuelerklausur,

	}

	private void update_klausurraum_by_idStundenplanraum() {
		_klausurraum_by_idStundenplanraum.clear();
		for (final @NotNull GostKlausurraum raum : _raummenge)
			if (raum.idStundenplanRaum != null)
				DeveloperNotificationException.ifMapPutOverwrites(_klausurraum_by_idStundenplanraum, raum.idStundenplanRaum, raum);
	}

	private void update_raummenge_by_idTermin() {
		_raummenge_by_idTermin.clear();
		for (final @NotNull GostKlausurraum raum : _raummenge)
			MapUtils.getOrCreateArrayList(_raummenge_by_idTermin, raum.idTermin).add(raum);
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

	private void update_raumstundenmenge_by_idSchuelerklausurtermin() {
		_raumstundenmenge_by_idSchuelerklausurtermin.clear();
		for (final @NotNull GostSchuelerklausurterminraumstunde skrs : _schuelerklausurterminraumstundenmenge)
			MapUtils.getOrCreateArrayList(_raumstundenmenge_by_idSchuelerklausurtermin, skrs.idSchuelerklausurtermin)
					.add(DeveloperNotificationException.ifMapGetIsNull(_raumstunde_by_id, skrs.idRaumstunde));
	}

	private void update_schuelerklausurterminmenge_by_idRaum() {
		_schuelerklausurterminmenge_by_idRaum.clear();
		for (final @NotNull GostSchuelerklausurTermin k : _schuelerklausurterminmenge) {
			final List<GostKlausurraumstunde> raumstunden = _raumstundenmenge_by_idSchuelerklausurtermin.get(k.id);
			MapUtils.getOrCreateArrayList(_schuelerklausurterminmenge_by_idRaum,
					((raumstunden == null) || raumstunden.isEmpty()) ? -1L : raumstunden.get(0).idRaum)
					.add(k);
		}
	}

	private void update_schuelerklausurterminmenge_by_idTermin() {
		_schuelerklausurterminmenge_by_idTermin.clear();
		for (final @NotNull GostSchuelerklausurTermin k : _schuelerklausurterminmenge)
			MapUtils.addToList(_schuelerklausurterminmenge_by_idTermin, _kursklausurManager.terminOrExceptionBySchuelerklausurTermin(k).id, k);
	}

	private void update_schuelerklausurterminmenge_by_idRaum_and_idTermin() {
		_schuelerklausurterminmenge_by_idRaum_and_idTermin.clear();
		for (final @NotNull GostSchuelerklausurTermin k : _schuelerklausurterminmenge) {
			final List<GostKlausurraumstunde> raumstunden = _raumstundenmenge_by_idSchuelerklausurtermin.get(k.id);
			Map2DUtils.getOrCreateArrayList(_schuelerklausurterminmenge_by_idRaum_and_idTermin,
					((raumstunden == null) || raumstunden.isEmpty()) ? -1L : raumstunden.get(0).idRaum,
					_kursklausurManager.terminOrExceptionBySchuelerklausurTermin(k).id).add(k);
		}
	}

	private void update_schuelerklausurterminmenge_by_idRaum_and_idKursklausur() {
		_schuelerklausurterminmenge_by_idRaum_and_idKursklausur.clear();
		for (final @NotNull GostSchuelerklausurTermin k : _schuelerklausurterminmenge) {
			final List<GostKlausurraumstunde> raumstunden = _raumstundenmenge_by_idSchuelerklausurtermin.get(k.id);
			Map2DUtils.getOrCreateArrayList(_schuelerklausurterminmenge_by_idRaum_and_idKursklausur,
					((raumstunden == null) || raumstunden.isEmpty()) ? -1L : raumstunden.get(0).idRaum,
					_kursklausurManager.kursklausurBySchuelerklausurTermin(k).id).add(k);
		}
	}

	private void update_schuelerklausurterminmenge_by_idKursklausur() {
		_schuelerklausurterminmenge_by_idKursklausur.clear();
		for (final @NotNull GostSchuelerklausurTermin k : _schuelerklausurterminmenge)
			MapUtils.getOrCreateArrayList(_schuelerklausurterminmenge_by_idKursklausur, _kursklausurManager.kursklausurBySchuelerklausurTermin(k).id).add(k);
	}

	private void update_schuelerklausurterminraumstundenmenge_by_idRaumstunde() {
		_schuelerklausurterminraumstundenmenge_by_idRaumstunde.clear();
		for (final @NotNull GostSchuelerklausurterminraumstunde skrs : _schuelerklausurterminraumstundenmenge)
			MapUtils.getOrCreateArrayList(_schuelerklausurterminraumstundenmenge_by_idRaumstunde, skrs.idRaumstunde).add(skrs);
	}

	private void update_schuelerklausurraumstundenmenge_by_idSchuelerklausur() {
		_schuelerklausurraumstundenmenge_by_idSchuelerklausurtermin.clear();
		for (final @NotNull GostSchuelerklausurterminraumstunde skrs : _schuelerklausurterminraumstundenmenge)
			MapUtils.getOrCreateArrayList(_schuelerklausurraumstundenmenge_by_idSchuelerklausurtermin, skrs.idSchuelerklausurtermin).add(skrs);
	}

	private void update_klausurraum_by_idSchuelerklausurtermin() {
		_klausurraum_by_idSchuelerklausurtermin.clear();
		for (final @NotNull GostSchuelerklausurterminraumstunde skrs : _schuelerklausurterminraumstundenmenge) {
			final @NotNull List<GostKlausurraumstunde> krsList =
					DeveloperNotificationException.ifMapGetIsNull(_raumstundenmenge_by_idSchuelerklausurtermin, skrs.idSchuelerklausurtermin);
			for (final @NotNull GostKlausurraumstunde krs : krsList) {
				final @NotNull GostKlausurraum kr = DeveloperNotificationException.ifMapGetIsNull(_raum_by_id, krs.idRaum);
				final GostKlausurraum krAlt = _klausurraum_by_idSchuelerklausurtermin.put(skrs.idSchuelerklausurtermin, kr);
				if ((krAlt != null) && (krAlt != kr))
					throw new DeveloperNotificationException("Schülerklausur " + skrs.idSchuelerklausurtermin + " ist zwei Klausurräumen zugeordnet.");
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

	private void raumAddAllOhneUpdate(final @NotNull List<GostKlausurraum> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
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
	public void raumAddAll(final @NotNull List<GostKlausurraum> listRaum) {
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
	public @NotNull List<GostKlausurraum> raumGetMengeAsList() {
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
		final List<GostKlausurraumstunde> rsList = _raumstundenmenge_by_idRaum.get(idRaum);
		if (rsList != null)
			for (final @NotNull GostKlausurraumstunde rs : rsList)
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
	public void raumRemoveAll(final @NotNull List<GostKlausurraum> listRaum) {
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

	private void raumstundeAddAllOhneUpdate(final @NotNull List<GostKlausurraumstunde> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostKlausurraumstunde raumstunde : list) {
			raumstundeCheck(raumstunde);
			DeveloperNotificationException.ifTrue("raumstundeAddAllOhneUpdate: ID=" + raumstunde.id + " existiert bereits!",
					_raumstunde_by_id.containsKey(raumstunde.id));
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
	public void raumstundeAddAll(final @NotNull List<GostKlausurraumstunde> listRaumstunde) {
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
	public @NotNull List<GostKlausurraumstunde> raumstundeGetMengeAsList() {
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
		final List<GostSchuelerklausurterminraumstunde> skrsList = _schuelerklausurterminraumstundenmenge_by_idRaumstunde.get(idRaumstunde);
		if (skrsList != null)
			for (final @NotNull GostSchuelerklausurterminraumstunde skrs : skrsList)
				schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurAndIdRaumstunde(skrs.idSchuelerklausurtermin, skrs.idRaumstunde);
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
	public void raumstundeRemoveAllOhneUpdate(final @NotNull List<GostKlausurraumstunde> listRaumstunde) {
		for (final @NotNull GostKlausurraumstunde raumstunde : listRaumstunde)
			raumstundeRemoveOhneUpdateById(raumstunde.id);
	}

	/**
	 * Entfernt alle {@link GostKlausurraumstunde}-Objekte.
	 *
	 * @param listRaumstunde Die Liste der zu entfernenden
	 *                       {@link GostKlausurraumstunde}-Objekte.
	 */
	public void raumstundeRemoveAll(final @NotNull List<GostKlausurraumstunde> listRaumstunde) {
		raumstundeRemoveAllOhneUpdate(listRaumstunde);
		update_all();
	}

	// #####################################################################
	// #################### Schülerklausur ################################
	// #####################################################################

	private void update_schuelerklausurmenge() {
		_schuelerklausurterminmenge.clear();
		_schuelerklausurterminmenge.addAll(_schuelerklausurtermin_by_id.values());
//		_schuelerklausurmenge.sort(_compRaum);
	}

	/**
	 * Fügt ein {@link GostSchuelerklausurTermin}-Objekt hinzu.
	 *
	 * @param schuelerklausur Das {@link GostSchuelerklausurTermin}-Objekt, welches
	 *                        hinzugefügt werden soll.
	 */
	public void schuelerklausurAdd(final @NotNull GostSchuelerklausurTermin schuelerklausur) {
		schuelerklausurAddAll(ListUtils.create1(schuelerklausur));
	}

	/**
	 * Fügt ein {@link GostSchuelerklausurTermin}-Objekt hinzu.
	 *
	 * @param schuelerklausur Das {@link GostSchuelerklausurTermin}-Objekt, welches
	 *                        hinzugefügt werden soll.
	 */
	public void schuelerklausurAddOhneUpdate(final @NotNull GostSchuelerklausurTermin schuelerklausur) {
		schuelerklausurAddAllOhneUpdate(ListUtils.create1(schuelerklausur));
	}

	private void schuelerklausurAddAllOhneUpdate(final @NotNull List<GostSchuelerklausurTermin> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostSchuelerklausurTermin schuelerklausur : list) {
			schuelerklausurCheck(schuelerklausur);
			DeveloperNotificationException.ifTrue("schuelerklausurAddAllOhneUpdate: ID=" + schuelerklausur.idSchuelerklausur + " existiert bereits!",
					_schuelerklausurtermin_by_id.containsKey(schuelerklausur.idSchuelerklausur));
			DeveloperNotificationException.ifTrue("schuelerklausurAddAllOhneUpdate: ID=" + schuelerklausur.idSchuelerklausur + " doppelt in der Liste!",
					!setOfIDs.add(schuelerklausur.idSchuelerklausur));
		}

		// add all
		for (final @NotNull GostSchuelerklausurTermin schuelerklausur : list)
			DeveloperNotificationException.ifMapPutOverwrites(_schuelerklausurtermin_by_id, schuelerklausur.idSchuelerklausur, schuelerklausur);
	}

	/**
	 * Fügt alle {@link GostSchuelerklausurTermin}-Objekte hinzu.
	 *
	 * @param listSchuelerklausur Die Menge der {@link GostSchuelerklausurTermin}-Objekte,
	 *                            welche hinzugefügt werden soll.
	 */
	public void schuelerklausurAddAll(final @NotNull List<GostSchuelerklausurTermin> listSchuelerklausur) {
		schuelerklausurAddAllOhneUpdate(listSchuelerklausur);
		update_all();
	}

	private static void schuelerklausurCheck(final @NotNull GostSchuelerklausurTermin schuelerklausur) {
		DeveloperNotificationException.ifInvalidID("schuelerklausur.idSchuelerklausur", schuelerklausur.idSchuelerklausur);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostSchuelerklausurTermin}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idSchuelerklausur Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostSchuelerklausurTermin}-Objekt.
	 */
	public @NotNull GostSchuelerklausurTermin schuelerklausurGetByIdOrException(final long idSchuelerklausur) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerklausurtermin_by_id, idSchuelerklausur);
	}

	/**
	 * Liefert eine Liste aller {@link GostSchuelerklausurTermin}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostSchuelerklausurTermin}-Objekte.
	 */
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurGetMengeAsList() {
		return _schuelerklausurterminmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostSchuelerklausurTermin}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param schuelerklausur Das neue {@link GostSchuelerklausurTermin}-Objekt.
	 */
	public void schuelerklausurPatchAttributes(final @NotNull GostSchuelerklausurTermin schuelerklausur) {
		schuelerklausurCheck(schuelerklausur);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_schuelerklausurtermin_by_id, schuelerklausur.idSchuelerklausur);
		DeveloperNotificationException.ifMapPutOverwrites(_schuelerklausurtermin_by_id, schuelerklausur.idSchuelerklausur, schuelerklausur);

		update_all();
	}

	private void schuelerklausurRemoveOhneUpdateById(final long idSchuelerklausur) {
		DeveloperNotificationException.ifMapRemoveFailes(_schuelerklausurtermin_by_id, idSchuelerklausur);
		schuelerklausurraumstundenmengeRemoveOhneUpdateByIdSchuelerklausur(idSchuelerklausur);
	}

	/**
	 * Entfernt ein existierendes {@link GostSchuelerklausurTermin}-Objekt.
	 *
	 * @param idSchuelerklausur Die ID des {@link GostSchuelerklausurTermin}-Objekts.
	 */
	public void schuelerklausurRemoveById(final long idSchuelerklausur) {
		schuelerklausurRemoveOhneUpdateById(idSchuelerklausur);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurTermin}-Objekte.
	 *
	 * @param listSchuelerklausur Die Liste der zu entfernenden
	 *                            {@link GostSchuelerklausurTermin}-Objekte.
	 */
	public void schuelerklausurRemoveAll(final @NotNull List<GostSchuelerklausurTermin> listSchuelerklausur) {
		for (final @NotNull GostSchuelerklausurTermin schuelerklausur : listSchuelerklausur)
			schuelerklausurRemoveOhneUpdateById(schuelerklausur.idSchuelerklausur);

		update_all();
	}

	// #####################################################################
	// #################### Schuelerklausurraumstunde
	// ################################
	// #####################################################################

	private void update_schuelerklausurraumstundenmenge() {
		_schuelerklausurterminraumstundenmenge.clear();
		_schuelerklausurterminraumstundenmenge.addAll(_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.getNonNullValuesAsList());
	}

	/**
	 * Fügt ein {@link GostSchuelerklausurterminraumstunde}-Objekt hinzu.
	 *
	 * @param schuelerklausurraumstunde Das
	 *                                  {@link GostSchuelerklausurterminraumstunde}-Objekt,
	 *                                  welches hinzugefügt werden soll.
	 */
	public void schuelerklausurraumstundeAdd(final @NotNull GostSchuelerklausurterminraumstunde schuelerklausurraumstunde) {
		schuelerklausurraumstundeAddAll(ListUtils.create1(schuelerklausurraumstunde));
	}

	private void schuelerklausurraumstundeAddAllOhneUpdate(final @NotNull List<GostSchuelerklausurterminraumstunde> list) {
		// check all
		final @NotNull HashMap2D<Long, Long, GostSchuelerklausurterminraumstunde> setOfIDs = new HashMap2D<>();
		for (final @NotNull GostSchuelerklausurterminraumstunde schuelerklausurraumstunde : list) {
			schuelerklausurraumstundeCheck(schuelerklausurraumstunde);
			DeveloperNotificationException.ifTrue(
					"schuelerklausurraumstundeAddAllOhneUpdate: ID=(" + schuelerklausurraumstunde.idSchuelerklausurtermin + ","
							+ schuelerklausurraumstunde.idRaumstunde + ") existiert bereits!",
					_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.contains(schuelerklausurraumstunde.idSchuelerklausurtermin,
							schuelerklausurraumstunde.idRaumstunde));
			DeveloperNotificationException.ifTrue(
					"schuelerklausurraumstundeAddAllOhneUpdate: ID=" + schuelerklausurraumstunde.idSchuelerklausurtermin + ","
							+ schuelerklausurraumstunde.idRaumstunde + ") doppelt in der Liste!",
					setOfIDs.contains(schuelerklausurraumstunde.idSchuelerklausurtermin, schuelerklausurraumstunde.idRaumstunde));
			setOfIDs.put(schuelerklausurraumstunde.idSchuelerklausurtermin, schuelerklausurraumstunde.idRaumstunde, schuelerklausurraumstunde);
		}

		// add all
		for (final @NotNull GostSchuelerklausurterminraumstunde schuelerklausurraumstunde : list)
			DeveloperNotificationException.ifMap2DPutOverwrites(_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde,
					schuelerklausurraumstunde.idSchuelerklausurtermin, schuelerklausurraumstunde.idRaumstunde, schuelerklausurraumstunde);
	}

	/**
	 * Fügt alle {@link GostSchuelerklausurterminraumstunde}-Objekte hinzu.
	 *
	 * @param listSchuelerklausurraumstunde Die Menge der
	 *                                      {@link GostSchuelerklausurterminraumstunde}-Objekte,
	 *                                      welche hinzugefügt werden soll.
	 */
	public void schuelerklausurraumstundeAddAll(final @NotNull List<GostSchuelerklausurterminraumstunde> listSchuelerklausurraumstunde) {
		schuelerklausurraumstundeAddAllOhneUpdate(listSchuelerklausurraumstunde);
		update_all();
	}

	private static void schuelerklausurraumstundeCheck(final @NotNull GostSchuelerklausurterminraumstunde schuelerklausurraumstunde) {
		DeveloperNotificationException.ifInvalidID("schuelerklausurraumstunde.idSchuelerklausur", schuelerklausurraumstunde.idSchuelerklausurtermin);
		DeveloperNotificationException.ifInvalidID("schuelerklausurraumstunde.idRaumstunde", schuelerklausurraumstunde.idRaumstunde);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 * <br>
	 * Laufzeit: O(1)
	 *
	 * @param idSchuelerklausur Die ID des {@link GostSchuelerklausurTermin}-Objekts.
	 * @param idRaumstunde      Die ID des {@link GostKlausurraumstunde}-Objekts.
	 *
	 * @return das zur ID zugehörige {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 */
	public @NotNull GostSchuelerklausurterminraumstunde schuelerklausurraumstundeGetByIdSchuelerklausurAndIdRaumstundeOrException(final long idSchuelerklausur,
			final long idRaumstunde) {
		return DeveloperNotificationException.ifMap2DGetIsNull(_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde, idSchuelerklausur,
				idRaumstunde);
	}

	/**
	 * Liefert eine Liste aller {@link GostSchuelerklausurterminraumstunde}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostSchuelerklausurterminraumstunde}-Objekte.
	 */
	public @NotNull List<GostSchuelerklausurterminraumstunde> schuelerklausurraumstundeGetMengeAsList() {
		return _schuelerklausurterminraumstundenmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostSchuelerklausurterminraumstunde}-Objekt
	 * durch das neue Objekt.
	 *
	 * @param schuelerklausurraumstunde Das neue
	 *                                  {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 */
	public void schuelerklausurraumstundePatchAttributes(final @NotNull GostSchuelerklausurterminraumstunde schuelerklausurraumstunde) {
		schuelerklausurraumstundeCheck(schuelerklausurraumstunde);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMap2DRemoveFailes(_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde,
				schuelerklausurraumstunde.idSchuelerklausurtermin, schuelerklausurraumstunde.idRaumstunde);
		DeveloperNotificationException.ifMap2DPutOverwrites(_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde,
				schuelerklausurraumstunde.idSchuelerklausurtermin, schuelerklausurraumstunde.idRaumstunde, schuelerklausurraumstunde);

		update_all();
	}

	private void schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurAndIdRaumstunde(final long idSchuelerklausur, final long idRaumstunde) {
		DeveloperNotificationException.ifMap2DRemoveFailes(_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde, idSchuelerklausur,
				idRaumstunde);
	}

	private void schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurtermin(final long idSchuelerklausurtermin) {
		_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.removeSubMap(idSchuelerklausurtermin);
	}

	private void schuelerklausurraumstundenmengeRemoveOhneUpdateByIdSchuelerklausur(final long idSchuelerklausur) {
		final List<GostSchuelerklausurterminraumstunde> skrsList = _schuelerklausurraumstundenmenge_by_idSchuelerklausurtermin.get(idSchuelerklausur);
		if (skrsList != null)
			for (final @NotNull GostSchuelerklausurterminraumstunde skrs : skrsList)
				DeveloperNotificationException.ifMap2DRemoveFailes(_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde,
						skrs.idSchuelerklausurtermin, skrs.idRaumstunde);
	}

	/**
	 * Entfernt ein existierendes {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 *
	 * @param idSchuelerklausur Die ID des {@link GostSchuelerklausurTermin}-Objekts.
	 * @param idRaumstunde      Die ID des {@link GostKlausurraumstunde}-Objekts.
	 */
	public void schuelerklausurraumstundeRemoveByIdSchuelerklausurAndIdRaumstunde(final long idSchuelerklausur, final long idRaumstunde) {
		schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurAndIdRaumstunde(idSchuelerklausur, idRaumstunde);

		update_all();
	}

	/**
	 * Entfernt ein existierendes {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 *
	 * @param idSchuelerklausur Die ID des {@link GostSchuelerklausurTermin}-Objekts.
	 */
	public void schuelerklausurraumstundeRemoveByIdSchuelerklausur(final long idSchuelerklausur) {
		schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurtermin(idSchuelerklausur);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurterminraumstunde}-Objekte, deren Schülerklausur-ID in der übergebenen Liste enthalten ist.
	 *
	 * @param idsSchuelerklausurtermine die Liste der Schülerklausur-IDs.
	 */
	public void schuelerklausurraumstundeRemoveAllByIdSchuelerklausurterminOhneUpdate(final @NotNull List<Long> idsSchuelerklausurtermine) {
		for (final long idSchuelerklausurtermin : idsSchuelerklausurtermine)
			schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurtermin(idSchuelerklausurtermin);
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurterminraumstunde}-Objekte, deren Schülerklausur-ID in der übergebenen Liste enthalten ist.
	 *
	 * @param idsSchuelerklausurtermine die Liste der Schülerklausur-IDs.
	 */
	public void schuelerklausurraumstundeRemoveAllByIdSchuelerklausurtermin(final @NotNull List<Long> idsSchuelerklausurtermine) {
		schuelerklausurraumstundeRemoveAllByIdSchuelerklausurterminOhneUpdate(idsSchuelerklausurtermine);
		update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurterminraumstunde}-Objekte.
	 *
	 * @param listSchuelerklausurRaumstunde Die Liste der zu entfernenden
	 *                                      {@link GostSchuelerklausurterminraumstunde}-Objekte.
	 */
	public void schuelerklausurraumstundeRemoveAll(final @NotNull List<GostSchuelerklausurterminraumstunde> listSchuelerklausurRaumstunde) {
		for (final @NotNull GostSchuelerklausurterminraumstunde schuelerklausurraumstunde : listSchuelerklausurRaumstunde)
			schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurAndIdRaumstunde(schuelerklausurraumstunde.idSchuelerklausurtermin,
					schuelerklausurraumstunde.idRaumstunde);

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
	public @NotNull List<GostKlausurraumstunde> klausurraumstundeGetMengeByRaumid(final long idRaum) {
		final List<GostKlausurraumstunde> stunden = _raumstundenmenge_by_idRaum.get(idRaum);
		return (stunden != null) ? stunden : new ArrayList<>();
	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich der Klausurraum geändert
	 * hat.
	 *
	 * @param collectionSkrsKrs das GostKlausurraum-Objekt
	 */
	public void setzeRaumZuSchuelerklausuren(final @NotNull GostKlausurenCollectionSkrsKrs collectionSkrsKrs) {
		raumstundeAddAllOhneUpdate(collectionSkrsKrs.raumstunden);
		raumstundeRemoveAllOhneUpdate(collectionSkrsKrs.raumstundenGeloescht);
		schuelerklausurraumstundeRemoveAllByIdSchuelerklausurterminOhneUpdate(collectionSkrsKrs.idsSchuelerklausurtermine);
		schuelerklausurraumstundeAddAllOhneUpdate(collectionSkrsKrs.sktRaumstunden);

		update_all();
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public @NotNull List<GostKursklausur> kursklausurGetMenge() {
		final List<GostKursklausur> kursklausuren = new ArrayList<>();
		for (final long kkId : _schuelerklausurterminmenge_by_idKursklausur.keySet()) {
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
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurGetMengeByKursklausurid(final long idKursklausur) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerklausurterminmenge_by_idKursklausur, idKursklausur);
	}

	/**
	 * Liefert die Menge aller Kursklausuren zurück, die in einem bestimmten Raum geschrieben werden, auch wenn die Kursklausur nur nachgeschrieben wird.
	 *
	 * @param idRaum  die Id des Klausurraums
	 *
	 * @return die Liste der GostKursklausuren im übergebenen Klausurraum
	 */
	public @NotNull List<GostKursklausur> kursklausurGetMengeByRaumid(final long idRaum) {
		final List<GostKursklausur> kursklausuren = new ArrayList<>();
		if (!_schuelerklausurterminmenge_by_idRaum_and_idKursklausur.containsKey1(idRaum))
			return kursklausuren;
		for (final long idKK : _schuelerklausurterminmenge_by_idRaum_and_idKursklausur.getKeySetOf(idRaum)) {
			if (!_schuelerklausurterminmenge_by_idRaum_and_idKursklausur.getOrException(idRaum, idKK).isEmpty())
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
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurGetMengeByRaumidAndKursklausurid(final long idRaum, final long idKursklausur) {
		return DeveloperNotificationException.ifMap2DGetIsNull(_schuelerklausurterminmenge_by_idRaum_and_idKursklausur, idRaum, idKursklausur);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param idRaum  die Id des Klausurraums
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurGetMengeByRaumid(final long idRaum) {
		final List<GostSchuelerklausurTermin> schuelerklausuren = new ArrayList<>();
		if (!_schuelerklausurterminmenge_by_idRaum_and_idKursklausur.containsKey1(idRaum))
			return schuelerklausuren;
		for (final long idKK : _schuelerklausurterminmenge_by_idRaum_and_idKursklausur.getKeySetOf(idRaum))
			schuelerklausuren.addAll(_schuelerklausurterminmenge_by_idRaum_and_idKursklausur.getOrException(idRaum, idKK));
		return schuelerklausuren;
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurHauptterminOhneRaumGetMenge() {
		final List<GostSchuelerklausurTermin> schuelerklausuren =
				_schuelerklausurterminmenge_by_idRaum_and_idTermin.getOrNull(-1L, getHauptTermin().id);
		return (schuelerklausuren == null) ? new ArrayList<>() : schuelerklausuren;
	}

	/**
	 * Liefert eine Liste von Stundenplanräumen, die nicht für diesen Klausurtermin
	 * verplant sind.
	 *
	 * @param alleRaeume die Liste aller Stundenplanräume
	 *
	 * @return die Liste der nicht verplanten StundenplanRäume
	 */
	public @NotNull List<StundenplanRaum> stundenplanraumVerfuegbarGetMenge(final @NotNull List<StundenplanRaum> alleRaeume) {
		final List<StundenplanRaum> raeume = new ArrayList<>();
		for (final @NotNull StundenplanRaum raum : alleRaeume)
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
	public boolean isKursklausurAlleSchuelerklausurenVerplant(final @NotNull GostKursklausur kk) {
		for (final @NotNull GostSchuelerklausurTermin sk : DeveloperNotificationException.ifMapGetIsNull(_schuelerklausurterminmenge_by_idKursklausur, kk.id)) {
			if (!_raumstundenmenge_by_idSchuelerklausurtermin.containsKey(sk.id))
				return false;
		}
		return true;
	}

	/**
	 * Prüft, ob alle zu einem Klausurtermin gehörenden Schülerklausuren einem Raum
	 * zugeordnet sind.
	 *
	 * @param t der zu prüfende Klausurtermin
	 *
	 * @return true, wenn alle Schülerklausuren verplant sind, sonst false
	 */
	public boolean isTerminAlleSchuelerklausurenVerplant(final @NotNull GostKlausurtermin t) {
		for (final @NotNull GostSchuelerklausurTermin sk : DeveloperNotificationException.ifMapGetIsNull(_schuelerklausurterminmenge_by_idTermin, t.id)) {
			if (!_raumstundenmenge_by_idSchuelerklausurtermin.containsKey(sk.id))
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
		return _schuelerklausurterminmenge_by_idRaum_and_idKursklausur.contains(idRaum, idKursklausur);
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
		for (final @NotNull GostKursklausur klausur : kursklausurGetMengeByRaumid(raum.id)) {
			final @NotNull GostKlausurvorgabe vorgabe = _kursklausurManager.vorgabeByKursklausur(klausur);
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
		for (final @NotNull GostKursklausur klausur : kursklausurGetMengeByRaumid(raum.id)) {
			if ((start != null) && (start == -1))
				start = klausur.startzeit;
			if (_kursklausurManager.hatAbweichendeStartzeitByKursklausur(klausur))
				return null;
		}
		return (start == null) ? _termin.startzeit : start;
	}

//	/**
//	 * Liefert eine Liste von {@link GostKlausurraum}-Objekten. <br>
//	 * Laufzeit: O(1)
//	 *
//	 * @param terminOnly falls true, werden nur die Räume, die speziell zu diesem Termin gehören angezeigt, falls false werden alle angezeigt
//	 *
//	 * @return eine Liste der {@link GostKlausurraum}-Objekte.
//	 */
//	public @NotNull List<GostKlausurraum> raumGetMengeTerminOnlyAsList(final boolean terminOnly) {
//		return terminOnly ? MapUtils.getOrDefault(_raummenge_by_idTermin, _termin.id, new ArrayList<>()) : raumGetMengeAsList();
//	}

	/**
	 * Liefert true zurück, falls Klausuren in terminfremden Räumen zugeordnet sind, sonst false
	 *
	 * @return true, falls Klausuren in terminfremden Räumen zugeordnet sind, sonst false
	 */
	public boolean isKlausurenInFremdraeumen() {
		for (final @NotNull GostSchuelerklausurTermin skt : _schuelerklausurterminmenge) {
			final GostKlausurraum raum = _klausurraum_by_idSchuelerklausurtermin.get(skt.id);
			if ((raum != null) && (raum.idTermin != getKursklausurManager().terminOrExceptionBySchuelerklausurTermin(skt).id))
				return true;
		}
		return false;
	}

	/**
	 * Liefert den zu einem Schülerklausurtermin zugehörigen Klausurraum zurück.
	 *
	 * @param skt der Schülerklausurtermin, zu dem der Klausurraum gesucht wird.
	 *
	 * @return den Klausurraum, falls einer zugewiesen ist, sonst null
	 */
	public GostKlausurraum klausurraumGetBySchuelerklausurtermin(final @NotNull GostSchuelerklausurTermin skt) {
		return _klausurraum_by_idSchuelerklausurtermin.get(skt.id);
	}

	/**
	 * Liefert den zu einem Schülerklausurtermin zugehörigen Stundenplanraum zurück.
	 *
	 * @param skt der Schülerklausurtermin, zu dem der Klausurraum gesucht wird.
	 *
	 * @return den Stundenplanraum, falls einer zugewiesen ist, sonst null
	 */
	public StundenplanRaum stundenplanraumGetBySchuelerklausurtermin(final @NotNull GostSchuelerklausurTermin skt) {
		final GostKlausurraum raum = klausurraumGetBySchuelerklausurtermin(skt);
		return ((raum == null) || (raum.idStundenplanRaum == null)) ? null : getStundenplanManager().raumGetByIdOrException(raum.idStundenplanRaum);
	}

	/**
	 * Liefert die Anzahl der Klausurtermine, deren Räume in diesem Manager verwaltet werden. <br>
	 *
	 * @return die Anzahl
	 */
	public int anzahlTermine() {
		return _schuelerklausurterminmenge_by_idTermin.containsKey(_termin.id) ? _schuelerklausurterminmenge_by_idTermin.size()
				: (_schuelerklausurterminmenge_by_idTermin.size() + 1);
	}

	/**
	 * Liefert die Anzahl der Klausurtermine, deren Räume in diesem Manager verwaltet werden. <br>
	 *
	 * @return die Anzahl
	 */
	public @NotNull List<Pair<GostKlausurtermin, List<GostSchuelerklausurTermin>>> getFremdTermine() {
		final @NotNull List<Pair<GostKlausurtermin, List<GostSchuelerklausurTermin>>> ergebnis = new ArrayList<>();
		for (final @NotNull Entry<Long, List<GostSchuelerklausurTermin>> entry : _schuelerklausurterminmenge_by_idTermin.entrySet()) {
			if (_termin.id != entry.getKey()) {
				ergebnis.add(new Pair<>(getKursklausurManager().terminGetByIdOrException(entry.getKey()), entry.getValue()));
			}
		}
		return ergebnis;
	}

	/**
	 * Prüft, ob Schülerklausuren bereits Klausurräumen zugeordnet sind. <br>
	 *
	 * @param fremdTermine wenn true, werden Fremdtermine (jahrgangsübergreifend) auch berücksichtigt.
	 *
	 * @return Wahrheitswert
	 */
	public boolean isSchuelerklausurenInRaum(final boolean fremdTermine) {
		for (final @NotNull GostSchuelerklausurTermin termin : schuelerklausurtermineZuVerteilenGetMenge(fremdTermine))
			if (_raumstundenmenge_by_idSchuelerklausurtermin.containsKey(termin.id))
				return true;
		return false;
	}

	/**
	 * Gibt die Liste der zu verteilenden Schülerklausurtermine zurück. <br>
	 *
	 * @param fremdTermine wenn true, werden Fremdtermine (jahrgangsübergreifend) auch berücksichtigt.
	 *
	 * @return die Liste der Schülerklausurtermine
	 */
	public @NotNull List<GostKlausurraum> raeumeVerfuegbarGetMenge(final boolean fremdTermine) {
		final List<GostKlausurraum> raeume = fremdTermine ? _raummenge : _raummenge_by_idTermin.get(getHauptTermin().id);
		return (raeume == null) ? new ArrayList<>() : raeume;
	}

	/**
	 * Prüft, ob Schülerklausuren bereits Klausurräumen zugeordnet sind. <br>
	 *
	 * @param fremdTermine wenn true, werden Fremdtermine (jahrgangsübergreifend) auch berücksichtigt.
	 *
	 * @return Wahrheitswert
	 */
	public int anzahlPlaetzeAlleRaeume(final boolean fremdTermine) {
		int kapazitaet = 0;
		for (final @NotNull GostKlausurraum raum : raeumeVerfuegbarGetMenge(fremdTermine)) {
			if (raum.idStundenplanRaum != null)
				kapazitaet += getStundenplanManager().raumGetByIdOrException(raum.idStundenplanRaum).groesse;
		}
		return kapazitaet;
	}

	/**
	 * Prüft, ob Schülerklausuren bereits Klausurräumen zugeordnet sind. <br>
	 *
	 * @param fremdTermine wenn true, werden Fremdtermine (jahrgangsübergreifend) auch berücksichtigt.
	 *
	 * @return Wahrheitswert
	 */
	public int anzahlBenoetigtePlaetzeAlleKlausuren(final boolean fremdTermine) {
		return schuelerklausurtermineZuVerteilenGetMenge(fremdTermine).size();
	}

	/**
	 * Gibt die Liste der zu verteilenden Schülerklausurtermine zurück. <br>
	 *
	 * @param fremdTermine wenn true, werden Fremdtermine (jahrgangsübergreifend) auch berücksichtigt.
	 *
	 * @return die Liste der Schülerklausurtermine
	 */
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurtermineZuVerteilenGetMenge(final boolean fremdTermine) {
		final List<GostSchuelerklausurTermin> skts =
				fremdTermine ? _schuelerklausurterminmenge : _schuelerklausurterminmenge_by_idTermin.get(getHauptTermin().id);
		return (skts == null) ? new ArrayList<>() : skts;
	}

	/**
	 * Prüft, ob Schülerklausuren bereits Klausurräumen zugeordnet sind. <br>
	 *
	 * @param fremdTermine wenn true, werden Fremdtermine (jahrgangsübergreifend) auch berücksichtigt.
	 *
	 * @return Wahrheitswert
	 */
	public boolean isPlatzkapazitaetAusreichend(final boolean fremdTermine) {
		return anzahlBenoetigtePlaetzeAlleKlausuren(fremdTermine) <= anzahlPlaetzeAlleRaeume(fremdTermine);
	}

	/**
	 * Erzeugt eine um relevante Informationen angereicherte Schülerklausurtermin-Objekte Liste, z.B. für Blockungs-Algorithmen. <br>
	 *
	 * @param termine die Liste der Schülerklausurtermin-Objekte.
	 *
	 * @return die Liste von angereicherten Objekten
	 */
	public @NotNull List<GostSchuelerklausurTerminRich> enrichSchuelerklausurtermine(final @NotNull List<GostSchuelerklausurTermin> termine) {
		final @NotNull List<GostSchuelerklausurTerminRich> ergebnis = new ArrayList<>();
		for (final @NotNull GostSchuelerklausurTermin termin : termine)
			ergebnis.add(new GostSchuelerklausurTerminRich(termin, getKursklausurManager()));
		return ergebnis;
	}

	/**
	 * Erzeugt eine um relevante Informationen angereicherte Klausurraum-Objekte Liste, z.B. für Blockungs-Algorithmen. <br>
	 *
	 * @param raeume die Liste der Klausurraum-Objekte.
	 *
	 * @return die Liste von angereicherten Objekten
	 */
	public @NotNull List<GostKlausurraumRich> enrichKlausurraeume(final @NotNull List<GostKlausurraum> raeume) {
		final @NotNull List<GostKlausurraumRich> ergebnis = new ArrayList<>();
		for (final @NotNull GostKlausurraum raum : raeume)
			if (raum.idStundenplanRaum != null) // TODO in andere Methode verlagern
				ergebnis.add(new GostKlausurraumRich(raum, getStundenplanManager().raumGetByIdOrException(raum.idStundenplanRaum)));
		return ergebnis;
	}

	/**
	 * Prüft, ob allen Räumen ein Stundenplanraum zugewiesen ist. <br>
	 *
	 * @param fremdTermine wenn true, werden Fremdtermine (jahrgangsübergreifend) auch berücksichtigt.
	 *
	 * @return true oder false
	 */
	public boolean alleRaeumeHabenStundenplanRaum(final boolean fremdTermine) {
		for (final @NotNull GostKlausurraum raum : raeumeVerfuegbarGetMenge(fremdTermine))
			if (raum.idStundenplanRaum == null)
				return false;
		return true;
	}



}
