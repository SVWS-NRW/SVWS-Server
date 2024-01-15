package de.svws_nrw.core.utils.gost.klausurplanung;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.adt.map.HashMap3D;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.core.utils.Map2DUtils;
import de.svws_nrw.core.utils.Map3DUtils;
import de.svws_nrw.core.utils.MapUtils;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link GostKursklausur}.
 * Hierbei werden auch Hilfsmethoden zur Interpretation der Daten erzeugt. Es
 * werden Kursklausuren eines Gost-Halbjahres eines Abiturjahrgangs verwaltet.
 */
public class GostKursklausurManager {

	private @NotNull GostKlausurvorgabenManager _vorgabenManager;

	private static final @NotNull Comparator<@NotNull GostKlausurtermin> _compTermin = (final @NotNull GostKlausurtermin a, final @NotNull GostKlausurtermin b) -> {
		if (a.datum == null && b.datum != null)
			return +1;
		if (b.datum == null && a.datum != null)
			return -1;
		if (a.datum != null && b.datum != null)
			return a.datum.compareTo(b.datum);
		if (a.quartal != b.quartal)
			return a.quartal - b.quartal;
		return Long.compare(a.id, b.id);
	};

	private final @NotNull Comparator<@NotNull GostKursklausur> _compKursklausur = (final @NotNull GostKursklausur a, final @NotNull GostKursklausur b) -> {
		GostFaecherManager faecherManager = _vorgabenManager.getFaecherManager();
		if (a.kursart.compareTo(b.kursart) < 0)
			return +1;
		if (a.kursart.compareTo(b.kursart) > 0)
			return -1;
		if (faecherManager != null) {
			final GostFach aFach = faecherManager.get(a.idFach);
			final GostFach bFach = faecherManager.get(b.idFach);
			if (aFach != null && bFach != null) {
				if (aFach.sortierung > bFach.sortierung)
					return +1;
				if (aFach.sortierung < bFach.sortierung)
					return -1;
			}
		}
		if (a.halbjahr != b.halbjahr)
			return a.halbjahr - b.halbjahr;
		if (a.quartal != b.quartal)
			return a.quartal - b.quartal;
		return Long.compare(a.id, b.id);
	};

	private final @NotNull Comparator<@NotNull GostSchuelerklausur> _compSchuelerklausur = (final @NotNull GostSchuelerklausur a, final @NotNull GostSchuelerklausur b) -> {
		GostFaecherManager faecherManager = _vorgabenManager.getFaecherManager();
		GostKlausurvorgabe aV = vorgabeBySchuelerklausur(a);
		GostKlausurvorgabe bV = vorgabeBySchuelerklausur(b);
		if (aV.quartal != bV.quartal)
			return aV.quartal - bV.quartal;
		if (aV.kursart.compareTo(bV.kursart) < 0)
			return +1;
		if (aV.kursart.compareTo(bV.kursart) > 0)
			return -1;
		if (faecherManager != null) {
			final GostFach aFach = faecherManager.get(aV.idFach);
			final GostFach bFach = faecherManager.get(bV.idFach);
			if (aFach != null && bFach != null) {
				if (aFach.sortierung > bFach.sortierung)
					return +1;
				if (aFach.sortierung < bFach.sortierung)
					return -1;
			}
		}
		return Long.compare(a.id, b.id);
	};

	private final @NotNull Comparator<@NotNull GostSchuelerklausurTermin> _compSchuelerklausurTermin = (final @NotNull GostSchuelerklausurTermin a, final @NotNull GostSchuelerklausurTermin b) -> {
		if (a.idSchuelerklausur == b.idSchuelerklausur) {
			return Integer.compare(a.folgeNr, b.folgeNr);
		}
		return Long.compare(a.id, b.id);
	};

	// GostKursklausur
	private final @NotNull Map<@NotNull Long, @NotNull GostKursklausur> _kursklausur_by_id = new HashMap<>();
	private final @NotNull List<@NotNull GostKursklausur> _kursklausurmenge = new ArrayList<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Integer, @NotNull List<@NotNull GostKursklausur>> _kursklausurmenge_by_halbjahr_and_quartal = new HashMap2D<>();
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostKursklausur>> _kursklausurmenge_by_idTermin = new HashMap<>();
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostKursklausur>> _kursklausurmenge_by_idVorgabe = new HashMap<>();
	private final @NotNull HashMap3D<@NotNull Integer, @NotNull Long, @NotNull Integer, @NotNull List<@NotNull GostKursklausur>> _kursklausurmenge_by_halbjahr_and_idTermin_and_quartal = new HashMap3D<>();
	private final @NotNull HashMap3D<@NotNull Long, @NotNull Integer, @NotNull Integer, @NotNull GostKursklausur> _kursklausur_by_idKurs_and_halbjahr_and_quartal = new HashMap3D<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Long, @NotNull List<@NotNull GostKursklausur>> _kursklausurmenge_by_kw_and_schuelerId = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull List<@NotNull GostKursklausur>> _kursklausurmenge_by_terminId_and_schuelerId = new HashMap2D<>();

	// GostKlausurtermin
	private final @NotNull Map<@NotNull Long, @NotNull GostKlausurtermin> _termin_by_id = new HashMap<>();
	private final @NotNull List<@NotNull GostKlausurtermin> _terminmenge = new ArrayList<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Integer, @NotNull List<@NotNull GostKlausurtermin>> _terminmenge_by_halbjahr_and_quartal = new HashMap2D<>();
	private final @NotNull Map<@NotNull String, @NotNull List<@NotNull GostKlausurtermin>> _terminmenge_by_datum = new HashMap<>();

	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull Long>> _schuelerIds_by_idTermin = new HashMap<>();

	// GostSchuelerklausur
	private final @NotNull Map<@NotNull Long, @NotNull GostSchuelerklausur> _schuelerklausur_by_id = new HashMap<>();
	private final @NotNull List<@NotNull GostSchuelerklausur> _schuelerklausurmenge = new ArrayList<>();

	// GostSchuelerklausurTermin
	private final @NotNull Map<@NotNull Long, @NotNull GostSchuelerklausurTermin> _schuelerklausurtermin_by_id = new HashMap<>();
	private final @NotNull List<@NotNull GostSchuelerklausurTermin> _schuelerklausurterminmenge = new ArrayList<>();
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostSchuelerklausurTermin>> _schuelerklausurterminmenge_by_idSchuelerklausur = new HashMap<>();
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostSchuelerklausurTermin>> _schuelerklausurterminmenge_by_idTermin = new HashMap<>();
	private final @NotNull HashMap3D<@NotNull Integer, @NotNull Long, @NotNull Integer, @NotNull List<@NotNull GostSchuelerklausurTermin>> _schuelerklausurterminntaktuellmenge_by_halbjahr_and_idTermin_and_quartal = new HashMap3D<>(); // ggf. neuer Manager

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param vorgabenManager der Klausurvorgaben-Manager
	 * @param listKlausuren   die Liste der GostKursklausuren eines Abiturjahrgangs
	 * @param listTermine     die Liste der GostKlausurtermine eines Abiturjahrgangs
	 * @param listSchuelerklausuren   die Liste der GostSchuelerklausuren eines Abiturjahrgangs
	 * @param listSchuelerklausurtermine   die Liste der GostSchuelerklausurTermine eines Abiturjahrgangs
	 */
	public GostKursklausurManager(final @NotNull GostKlausurvorgabenManager vorgabenManager, final @NotNull List<@NotNull GostKursklausur> listKlausuren,
			final List<@NotNull GostKlausurtermin> listTermine, final List<@NotNull GostSchuelerklausur> listSchuelerklausuren, final List<@NotNull GostSchuelerklausurTermin> listSchuelerklausurtermine) {
		_vorgabenManager = vorgabenManager;
		initAll(listKlausuren, listTermine, listSchuelerklausuren, listSchuelerklausurtermine);
	}

	private void initAll(final @NotNull List<@NotNull GostKursklausur> listKlausuren, final List<@NotNull GostKlausurtermin> listTermine, final List<@NotNull GostSchuelerklausur> listSchuelerklausuren, final List<@NotNull GostSchuelerklausurTermin> listSchuelerklausurtermine) {
		kursklausurAddAll(listKlausuren);
		if (listTermine != null)
			terminAddAll(listTermine);
		if (listSchuelerklausuren != null)
			schuelerklausurAddAll(listSchuelerklausuren);
		if (listSchuelerklausurtermine != null)
			schuelerklausurterminAddAll(listSchuelerklausurtermine);

		update_all();

	}

	private void update_all() {

		update_kursklausurmenge();
		update_terminmenge();
		update_schuelerklausurmenge();
		update_schuelerklausurterminmenge();

		update_kursklausurmenge_by_halbjahr_and_quartal();
		update_kursklausurmenge_by_idTermin();
		update_kursklausurmenge_by_idVorgabe();
		update_kursklausurmenge_by_halbjahr_and_quartal_and_idTermin();
		update_kursklausur_by_idKurs_and_halbjahr_and_quartal();
		update_terminmenge_by_halbjahr_and_quartal();
		update_terminmenge_by_datum();
		update_kursklausurmenge_by_terminId_and_schuelerId();

		update_schuelerIds_by_idTermin(); // benötigt _kursklausurmenge_by_idTermin
		update_kursklausurmenge_by_kw_and_schuelerId(); // benötigt _kursklausurmenge_by_idTermin

		update_schuelerklausurterminmenge_by_idSchuelerklausur();
		update_schuelerklausurterminmenge_by_idTermin();
		update_schuelerklausurterminntaktuellmenge_by_halbjahr_and_idTermin_and_quartal(); // benötigt _schuelerklausurterminmenge_by_idSchuelerklausur
	}

	private void update_kursklausurmenge_by_halbjahr_and_quartal() {
		_kursklausurmenge_by_halbjahr_and_quartal.clear();
		for (final @NotNull GostKursklausur kk : _kursklausurmenge) {
			Map2DUtils.getOrCreateArrayList(_kursklausurmenge_by_halbjahr_and_quartal, kk.halbjahr, kk.quartal).add(kk);
		}
	}

	private void update_kursklausurmenge_by_idTermin() {
		_kursklausurmenge_by_idTermin.clear();
		for (final @NotNull GostKursklausur kk : _kursklausurmenge)
			MapUtils.getOrCreateArrayList(_kursklausurmenge_by_idTermin, kk.idTermin != null ? kk.idTermin : -1).add(kk);
	}

	private void update_kursklausurmenge_by_idVorgabe() {
		_kursklausurmenge_by_idVorgabe.clear();
		for (final @NotNull GostKursklausur kk : _kursklausurmenge)
			MapUtils.getOrCreateArrayList(_kursklausurmenge_by_idVorgabe, kk.idVorgabe).add(kk);
	}

	private void update_kursklausurmenge_by_halbjahr_and_quartal_and_idTermin() {
		_kursklausurmenge_by_halbjahr_and_idTermin_and_quartal.clear();
		for (final @NotNull GostKursklausur kk : _kursklausurmenge) {
			Map3DUtils.getOrCreateArrayList(_kursklausurmenge_by_halbjahr_and_idTermin_and_quartal, kk.halbjahr, kk.idTermin != null ? kk.idTermin : -1, kk.quartal).add(kk);
		}
	}

	private void update_kursklausur_by_idKurs_and_halbjahr_and_quartal() {
		_kursklausur_by_idKurs_and_halbjahr_and_quartal.clear();
		for (final @NotNull GostKursklausur kk : _kursklausurmenge)
			_kursklausur_by_idKurs_and_halbjahr_and_quartal.put(kk.idKurs, kk.halbjahr, kk.quartal, kk);
	}

	private void update_terminmenge_by_halbjahr_and_quartal() {
		_terminmenge_by_halbjahr_and_quartal.clear();
		for (final @NotNull GostKlausurtermin t : _terminmenge)
			Map2DUtils.getOrCreateArrayList(_terminmenge_by_halbjahr_and_quartal, t.halbjahr, t.quartal).add(t);
	}

	private void update_terminmenge_by_datum() {
		_terminmenge_by_datum.clear();
		for (final @NotNull GostKlausurtermin t : _terminmenge)
			MapUtils.getOrCreateArrayList(_terminmenge_by_datum, t.datum).add(t);
	}

	private void update_schuelerIds_by_idTermin() {
		_schuelerIds_by_idTermin.clear();
		for (final @NotNull GostKlausurtermin t : _terminmenge) {
			final ArrayList<@NotNull Long> listSchuelerIds = new ArrayList<>();
			_schuelerIds_by_idTermin.put(t.id, listSchuelerIds);
			final List<@NotNull GostKursklausur> klausurenZuTermin = _kursklausurmenge_by_idTermin.get(t.id);
			if (klausurenZuTermin != null)
				for (final @NotNull GostKursklausur k : klausurenZuTermin)
					listSchuelerIds.addAll(k.schuelerIds);
		}
	}

	private void update_kursklausurmenge_by_kw_and_schuelerId() {
		_kursklausurmenge_by_kw_and_schuelerId.clear();
		for (final @NotNull GostKlausurtermin t : _terminmenge) {
			if (t.datum == null)
				continue;
			int kw = DateUtils.gibKwDesDatumsISO8601(t.datum);
			List<@NotNull GostKursklausur> klausuren = _kursklausurmenge_by_idTermin.get(t.id);
			if (klausuren != null)
				for (final @NotNull GostKursklausur kk : klausuren) {
					for (final @NotNull Long sId : kk.schuelerIds)
						Map2DUtils.getOrCreateArrayList(_kursklausurmenge_by_kw_and_schuelerId, kw, sId).add(kk);
				}
		}
	}

	private void update_kursklausurmenge_by_terminId_and_schuelerId() {
		_kursklausurmenge_by_terminId_and_schuelerId.clear();
		for (final @NotNull GostKursklausur kk : _kursklausurmenge) {
			for (final @NotNull Long sId : kk.schuelerIds)
				Map2DUtils.getOrCreateArrayList(_kursklausurmenge_by_terminId_and_schuelerId, kk.idTermin, sId).add(kk);
		}
	}

	private void update_schuelerklausurterminmenge_by_idSchuelerklausur() {
		_schuelerklausurterminmenge_by_idSchuelerklausur.clear();
		for (final @NotNull GostSchuelerklausurTermin skt : _schuelerklausurterminmenge)
			MapUtils.getOrCreateArrayList(_schuelerklausurterminmenge_by_idSchuelerklausur, skt.idSchuelerklausur).add(skt);
		for (@NotNull List<@NotNull GostSchuelerklausurTermin> sktList : _schuelerklausurterminmenge_by_idSchuelerklausur.values())
			sktList.sort(_compSchuelerklausurTermin);
	}

	private void update_schuelerklausurterminmenge_by_idTermin() {
		_schuelerklausurterminmenge_by_idTermin.clear();
		for (final @NotNull GostSchuelerklausurTermin skt : _schuelerklausurterminmenge)
			MapUtils.getOrCreateArrayList(_schuelerklausurterminmenge_by_idTermin, skt.idTermin).add(skt);
	}

	private void update_schuelerklausurterminntaktuellmenge_by_halbjahr_and_idTermin_and_quartal() { // ggf. neuer Manager
		_schuelerklausurterminntaktuellmenge_by_halbjahr_and_idTermin_and_quartal.clear();

		for (@NotNull List<@NotNull GostSchuelerklausurTermin> sktList : _schuelerklausurterminmenge_by_idSchuelerklausur.values()) {
			@NotNull GostSchuelerklausurTermin sktLast = sktList.get(sktList.size() - 1);
			@NotNull GostKlausurvorgabe v = vorgabeBySchuelerklausurTermin(sktLast);
			if (sktLast.folgeNr > 0)
				Map3DUtils.getOrCreateArrayList(_schuelerklausurterminntaktuellmenge_by_halbjahr_and_idTermin_and_quartal, v.halbjahr, sktLast.idTermin != null ? sktLast.idTermin : -1, v.quartal).add(sktLast);
		}
	}

	// #####################################################################
	// #################### GostKursklausur ################################
	// #####################################################################

	private void update_kursklausurmenge() {
		_kursklausurmenge.clear();
		_kursklausurmenge.addAll(_kursklausur_by_id.values());
		_kursklausurmenge.sort(_compKursklausur);
	}

	/**
	 * Fügt ein {@link GostKursklausur}-Objekt hinzu.
	 *
	 * @param kursklausur Das {@link GostKursklausur}-Objekt, welches hinzugefügt
	 *                    werden soll.
	 */
	public void kursklausurAdd(final @NotNull GostKursklausur kursklausur) {
		kursklausurAddAll(ListUtils.create1(kursklausur));
		update_all();
	}

	private void kursklausurAddAllOhneUpdate(final @NotNull List<@NotNull GostKursklausur> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostKursklausur klausur : list) {
			kursklausurCheck(klausur);
			DeveloperNotificationException.ifTrue("kursklausurAddAllOhneUpdate: ID=" + klausur.id + " existiert bereits!", _kursklausur_by_id.containsKey(klausur.id));
			DeveloperNotificationException.ifTrue("kursklausurAddAllOhneUpdate: ID=" + klausur.id + " doppelt in der Liste!", !setOfIDs.add(klausur.id));
		}

		// add all
		for (final @NotNull GostKursklausur klausur : list)
			DeveloperNotificationException.ifMapPutOverwrites(_kursklausur_by_id, klausur.id, klausur);
	}

	/**
	 * Fügt alle {@link GostKursklausur}-Objekte hinzu.
	 *
	 * @param listKursklausuren Die Menge der {@link GostKursklausur}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public void kursklausurAddAll(final @NotNull List<@NotNull GostKursklausur> listKursklausuren) {
		kursklausurAddAllOhneUpdate(listKursklausuren);
		update_all();
	}

	private static void kursklausurCheck(final @NotNull GostKursklausur kursklausur) {
		DeveloperNotificationException.ifInvalidID("kursklausur.id", kursklausur.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostKursklausur}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idKursklausur Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKursklausur}-Objekt.
	 */
	public @NotNull GostKursklausur kursklausurGetByIdOrException(final long idKursklausur) {
		return DeveloperNotificationException.ifMapGetIsNull(_kursklausur_by_id, idKursklausur);
	}

	/**
	 * Liefert eine Liste aller {@link GostKursklausur}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKursklausur}-Objekte.
	 */
	public @NotNull List<@NotNull GostKursklausur> kursklausurGetMengeAsList() {
		return _kursklausurmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKursklausur}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param kursklausur Das neue {@link GostKursklausur}-Objekt.
	 */
	public void kursklausurPatchAttributes(final @NotNull GostKursklausur kursklausur) {
		kursklausurCheck(kursklausur);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_kursklausur_by_id, kursklausur.id);
		DeveloperNotificationException.ifMapPutOverwrites(_kursklausur_by_id, kursklausur.id, kursklausur);

		update_all();
	}

	private void kursklausurRemoveOhneUpdateById(final long idKursklausur) {
		DeveloperNotificationException.ifMapRemoveFailes(_kursklausur_by_id, idKursklausur);
	}

	/**
	 * Entfernt ein existierendes {@link GostKursklausur}-Objekt.
	 *
	 * @param idKursklausur Die ID des {@link GostKursklausur}-Objekts.
	 */
	public void kursklausurRemoveById(final long idKursklausur) {
		kursklausurRemoveOhneUpdateById(idKursklausur);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostKursklausur}-Objekte.
	 *
	 * @param listKursklausuren Die Liste der zu entfernenden
	 *                          {@link GostKursklausur}-Objekte.
	 */
	public void kursklausurRemoveAll(final @NotNull List<@NotNull GostKursklausur> listKursklausuren) {
		for (final @NotNull GostKursklausur kursklausur : listKursklausuren)
			kursklausurRemoveOhneUpdateById(kursklausur.id);

		update_all();
	}

	// #####################################################################
	// #################### GostKlausurtermin ################################
	// #####################################################################

	private void update_terminmenge() {
		_terminmenge.clear();
		_terminmenge.addAll(_termin_by_id.values());
		_terminmenge.sort(_compTermin);
	}

	/**
	 * Fügt ein {@link GostKlausurtermin}-Objekt hinzu.
	 *
	 * @param termin Das {@link GostKlausurtermin}-Objekt, welches hinzugefügt
	 *               werden soll.
	 */
	public void terminAdd(final @NotNull GostKlausurtermin termin) {
		terminAddAll(ListUtils.create1(termin));
	}

	private void terminAddAllOhneUpdate(final @NotNull List<@NotNull GostKlausurtermin> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostKlausurtermin termin : list) {
			terminCheck(termin);
			DeveloperNotificationException.ifTrue("terminAddAllOhneUpdate: ID=" + termin.id + " existiert bereits!", _termin_by_id.containsKey(termin.id));
			DeveloperNotificationException.ifTrue("terminAddAllOhneUpdate: ID=" + termin.id + " doppelt in der Liste!", !setOfIDs.add(termin.id));
		}

		// add all
		for (final @NotNull GostKlausurtermin termin : list)
			DeveloperNotificationException.ifMapPutOverwrites(_termin_by_id, termin.id, termin);
	}

	/**
	 * Fügt alle {@link GostKlausurtermin}-Objekte hinzu.
	 *
	 * @param listTermine Die Menge der {@link GostKlausurtermin}-Objekte, welche
	 *                    hinzugefügt werden soll.
	 */
	public void terminAddAll(final @NotNull List<@NotNull GostKlausurtermin> listTermine) {
		terminAddAllOhneUpdate(listTermine);
		update_all();
	}

	private static void terminCheck(final @NotNull GostKlausurtermin termin) {
		DeveloperNotificationException.ifInvalidID("termin.id", termin.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostKlausurtermin}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idTermin Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKlausurtermin}-Objekt.
	 */
	public @NotNull GostKlausurtermin terminGetByIdOrException(final long idTermin) {
		return DeveloperNotificationException.ifMapGetIsNull(_termin_by_id, idTermin);
	}

	/**
	 * Liefert eine Liste aller {@link GostKlausurtermin}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKlausurtermin}-Objekte.
	 */
	public @NotNull List<@NotNull GostKlausurtermin> terminGetMengeAsList() {
		return _terminmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKlausurtermin}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param termin Das neue {@link GostKlausurtermin}-Objekt.
	 */
	public void terminPatchAttributes(final @NotNull GostKlausurtermin termin) {
		terminCheck(termin);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_termin_by_id, termin.id);
		DeveloperNotificationException.ifMapPutOverwrites(_termin_by_id, termin.id, termin);

		update_all();
	}

	private void terminRemoveOhneUpdateById(final long idTermin) {
		DeveloperNotificationException.ifMapRemoveFailes(_termin_by_id, idTermin);
		final List<@NotNull GostKursklausur> klausurenZuTermin = _kursklausurmenge_by_idTermin.get(idTermin);
		if (klausurenZuTermin != null)
			for (@NotNull final GostKursklausur k : klausurenZuTermin)
				k.idTermin = null;
	}

	/**
	 * Entfernt ein existierendes {@link GostKlausurtermin}-Objekt.
	 *
	 * @param idTermin Die ID des {@link GostKlausurtermin}-Objekts.
	 */
	public void terminRemoveById(final long idTermin) {
		terminRemoveOhneUpdateById(idTermin);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostKlausurtermin}-Objekte.
	 *
	 * @param listTermine Die Liste der zu entfernenden
	 *                    {@link GostKlausurtermin}-Objekte.
	 */
	public void terminRemoveAll(final @NotNull List<@NotNull GostKlausurtermin> listTermine) {
		for (final @NotNull GostKlausurtermin termin : listTermine)
			terminRemoveOhneUpdateById(termin.id);

		update_all();
	}

	// #####################################################################
	// #################### GostSchuelerklausur ################################
	// #####################################################################

	private void update_schuelerklausurmenge() {
		_schuelerklausurmenge.clear();
		_schuelerklausurmenge.addAll(_schuelerklausur_by_id.values());
		_schuelerklausurmenge.sort(_compSchuelerklausur);
	}

	/**
	 * Fügt ein {@link GostSchuelerklausur}-Objekt hinzu.
	 *
	 * @param kursklausur Das {@link GostSchuelerklausur}-Objekt, welches hinzugefügt
	 *                    werden soll.
	 */
	public void schuelerklausurAdd(final @NotNull GostSchuelerklausur kursklausur) {
		schuelerklausurAddAll(ListUtils.create1(kursklausur));
		update_all();
	}

	private void schuelerklausurAddAllOhneUpdate(final @NotNull List<@NotNull GostSchuelerklausur> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostSchuelerklausur klausur : list) {
			schuelerklausurCheck(klausur);
			DeveloperNotificationException.ifTrue("schuelerklausurAddAllOhneUpdate: ID=" + klausur.id + " existiert bereits!", _schuelerklausur_by_id.containsKey(klausur.id));
			DeveloperNotificationException.ifTrue("schuelerklausurAddAllOhneUpdate: ID=" + klausur.id + " doppelt in der Liste!", !setOfIDs.add(klausur.id));
		}

		// add all
		for (final @NotNull GostSchuelerklausur klausur : list)
			DeveloperNotificationException.ifMapPutOverwrites(_schuelerklausur_by_id, klausur.id, klausur);
	}

	/**
	 * Fügt alle {@link GostKursklausur}-Objekte hinzu.
	 *
	 * @param listKursklausuren Die Menge der {@link GostKursklausur}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public void schuelerklausurAddAll(final @NotNull List<@NotNull GostSchuelerklausur> listKursklausuren) {
		schuelerklausurAddAllOhneUpdate(listKursklausuren);
		update_all();
	}

	private static void schuelerklausurCheck(final @NotNull GostSchuelerklausur kursklausur) {
		DeveloperNotificationException.ifInvalidID("schuelerklausur.idSchuelerklausur", kursklausur.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostKursklausur}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idKursklausur Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKursklausur}-Objekt.
	 */
	public @NotNull GostSchuelerklausur schuelerklausurGetByIdOrException(final long idKursklausur) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerklausur_by_id, idKursklausur);
	}

	/**
	 * Liefert eine Liste aller {@link GostKursklausur}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKursklausur}-Objekte.
	 */
	public @NotNull List<@NotNull GostSchuelerklausur> schuelerklausurGetMengeAsList() {
		return new ArrayList<>(_schuelerklausurmenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKursklausur}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param kursklausur Das neue {@link GostKursklausur}-Objekt.
	 */
	public void schuelerklausurPatchAttributes(final @NotNull GostSchuelerklausur kursklausur) {
		schuelerklausurCheck(kursklausur);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_schuelerklausur_by_id, kursklausur.id);
		DeveloperNotificationException.ifMapPutOverwrites(_schuelerklausur_by_id, kursklausur.id, kursklausur);

		update_all();
	}

	private void schuelerklausurRemoveOhneUpdateById(final long idKursklausur) {
		DeveloperNotificationException.ifMapRemoveFailes(_schuelerklausur_by_id, idKursklausur);
	}

	/**
	 * Entfernt ein existierendes {@link GostKursklausur}-Objekt.
	 *
	 * @param idKursklausur Die ID des {@link GostKursklausur}-Objekts.
	 */
	public void schuelerklausurRemoveById(final long idKursklausur) {
		schuelerklausurRemoveOhneUpdateById(idKursklausur);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostKursklausur}-Objekte.
	 *
	 * @param listKursklausuren Die Liste der zu entfernenden
	 *                          {@link GostKursklausur}-Objekte.
	 */
	public void schuelerklausurRemoveAll(final @NotNull List<@NotNull GostSchuelerklausur> listKursklausuren) {
		for (final @NotNull GostSchuelerklausur kursklausur : listKursklausuren)
			schuelerklausurRemoveOhneUpdateById(kursklausur.id);

		update_all();
	}

	// #####################################################################
	// #################### GostSchuelerklausurTermin ################################
	// #####################################################################

	private void update_schuelerklausurterminmenge() {
		_schuelerklausurterminmenge.clear();
		_schuelerklausurterminmenge.addAll(_schuelerklausurtermin_by_id.values());
	}

	/**
	 * Fügt ein {@link GostSchuelerklausurTermin}-Objekt hinzu.
	 *
	 * @param schuelerklausurtermin Das {@link GostSchuelerklausurTermin}-Objekt, welches hinzugefügt
	 *                    werden soll.
	 */
	public void schuelerklausurterminAdd(final @NotNull GostSchuelerklausurTermin schuelerklausurtermin) {
		schuelerklausurterminAddAll(ListUtils.create1(schuelerklausurtermin));
		update_all();
	}

	private void schuelerklausurterminAddAllOhneUpdate(final @NotNull List<@NotNull GostSchuelerklausurTermin> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostSchuelerklausurTermin schuelerklausurtermin : list) {
			schuelerklausurterminCheck(schuelerklausurtermin);
			DeveloperNotificationException.ifTrue("schuelerklausurterminAddAllOhneUpdate: ID=" + schuelerklausurtermin.id + " existiert bereits!", _schuelerklausurtermin_by_id.containsKey(schuelerklausurtermin.id));
			DeveloperNotificationException.ifTrue("schuelerklausurterminAddAllOhneUpdate: ID=" + schuelerklausurtermin.id + " doppelt in der Liste!", !setOfIDs.add(schuelerklausurtermin.id));
		}

		// add all
		for (final @NotNull GostSchuelerklausurTermin schuelerklausurtermin : list)
			DeveloperNotificationException.ifMapPutOverwrites(_schuelerklausurtermin_by_id, schuelerklausurtermin.id, schuelerklausurtermin);
	}

	/**
	 * Fügt alle {@link GostSchuelerklausurTermin}-Objekte hinzu.
	 *
	 * @param listSchuelerklausurtermine Die Menge der {@link GostSchuelerklausurTermin}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public void schuelerklausurterminAddAll(final @NotNull List<@NotNull GostSchuelerklausurTermin> listSchuelerklausurtermine) {
		schuelerklausurterminAddAllOhneUpdate(listSchuelerklausurtermine);
		update_all();
	}

	private static void schuelerklausurterminCheck(final @NotNull GostSchuelerklausurTermin schuelerklausurtermin) {
		DeveloperNotificationException.ifInvalidID("schuelerschuelerklausurtermin.idSchuelerschuelerklausurtermin", schuelerklausurtermin.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostSchuelerklausurTermin}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idSchuelerklausurtermin Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostSchuelerklausurTermin}-Objekt.
	 */
	public @NotNull GostSchuelerklausurTermin schuelerklausurterminGetByIdOrException(final long idSchuelerklausurtermin) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerklausurtermin_by_id, idSchuelerklausurtermin);
	}

	/**
	 * Liefert eine Liste aller {@link GostSchuelerklausurTermin}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostSchuelerklausurTermin}-Objekte.
	 */
	public @NotNull List<@NotNull GostSchuelerklausurTermin> schuelerklausurterminGetMengeAsList() {
		return new ArrayList<>(_schuelerklausurterminmenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link GostSchuelerklausurTermin}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param schuelerklausurtermin Das neue {@link GostSchuelerklausurTermin}-Objekt.
	 */
	public void schuelerklausurterminPatchAttributes(final @NotNull GostSchuelerklausurTermin schuelerklausurtermin) {
		schuelerklausurterminCheck(schuelerklausurtermin);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_schuelerklausurtermin_by_id, schuelerklausurtermin.id);
		DeveloperNotificationException.ifMapPutOverwrites(_schuelerklausurtermin_by_id, schuelerklausurtermin.id, schuelerklausurtermin);

		update_all();
	}

	private void schuelerklausurterminRemoveOhneUpdateById(final long idSchuelerklausurtermin) {
		DeveloperNotificationException.ifMapRemoveFailes(_schuelerklausurtermin_by_id, idSchuelerklausurtermin);
	}

	/**
	 * Entfernt ein existierendes {@link GostSchuelerklausurTermin}-Objekt.
	 *
	 * @param idSchuelerklausurtermin Die ID des {@link GostSchuelerklausurTermin}-Objekts.
	 */
	public void schuelerklausurterminRemoveById(final long idSchuelerklausurtermin) {
		schuelerklausurterminRemoveOhneUpdateById(idSchuelerklausurtermin);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurTermin}-Objekte.
	 *
	 * @param listSchuelerklausurtermine Die Liste der zu entfernenden
	 *                          {@link GostSchuelerklausurTermin}-Objekte.
	 */
	public void schuelerklausurterminRemoveAll(final @NotNull List<@NotNull GostSchuelerklausurTermin> listSchuelerklausurtermine) {
		for (final @NotNull GostSchuelerklausurTermin schuelerklausurtermin : listSchuelerklausurtermine)
			schuelerklausurterminRemoveOhneUpdateById(schuelerklausurtermin.id);

		update_all();
	}


	// ################################################################################

	/**
	 * Liefert das GostKursklausur-Objekt zum übergebenen Termin und Kurs
	 *
	 * @param idTermin die ID des Klausurtermins
	 * @param idKurs   die ID des Kurses
	 *
	 * @return das GostKursklausur-Objekt
	 */
	public GostKursklausur kursklausurGetByTerminidAndKursid(final long idTermin, final long idKurs) {
		final List<@NotNull GostKursklausur> klausuren = kursklausurGetMengeByTerminid(idTermin);
		for (final @NotNull GostKursklausur klaus : klausuren) {
			if (klaus.idKurs == idKurs)
				return klaus;
		}
		return null;
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Datum
	 *
	 * @param datum das Datum der Klausurtermine im Format YYYY-MM-DD
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public @NotNull List<@NotNull GostKlausurtermin> terminGetMengeByDatum(final @NotNull String datum) {
		final List<@NotNull GostKlausurtermin> termine = _terminmenge_by_datum.get(datum);
		return termine != null ? termine : new ArrayList<>();
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Datum
	 *
	 * @param datum   das Datum der Klausurtermine
	 * @param zr      Zeitraster
	 * @param manager Manager
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public @NotNull List<@NotNull GostKlausurtermin> terminGetMengeByDatumAndZeitraster(final @NotNull String datum, final @NotNull StundenplanZeitraster zr,
			final @NotNull StundenplanManager manager) {
		final List<@NotNull GostKlausurtermin> termine = terminGetMengeByDatum(datum);
		final List<@NotNull GostKlausurtermin> retList = new ArrayList<>();
		for (final @NotNull GostKlausurtermin termin : termine) {
			final List<@NotNull StundenplanZeitraster> zrsTermin = manager.getZeitrasterByWochentagStartVerstrichen(Wochentag.fromIDorException(zr.wochentag),
					DeveloperNotificationException.ifNull("Startzeit des Klausurtermins", termin.startzeit), maxKlausurdauerGetByTerminid(termin.id));
			for (@NotNull final StundenplanZeitraster zrTermin : zrsTermin)
				if (zrTermin != null && zrTermin.id == zr.id)
					retList.add(termin);
		}
		return retList;
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Termin
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public @NotNull List<@NotNull GostKursklausur> kursklausurGetMengeByTerminid(final Long idTermin) {
		final List<@NotNull GostKursklausur> klausuren = _kursklausurmenge_by_idTermin.get(idTermin != null ? idTermin : -1);
		return klausuren != null ? klausuren : new ArrayList<>();
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Quartal
	 *
	 * @param halbjahr das Gosthalbjahr
	 * @param quartal  die Nummer des Quartals
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public List<@NotNull GostKursklausur> kursklausurGetMengeByHalbjahrAndQuartal(final @NotNull GostHalbjahr halbjahr, final int quartal) {
		return _kursklausurmenge_by_halbjahr_and_quartal.getOrNull(halbjahr.id, quartal);
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten für die noch kein Termin /
	 * Schiene gesetzt wurde
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public @NotNull List<@NotNull GostKursklausur> kursklausurOhneTerminGetMenge() {
		return kursklausurGetMengeByTerminid(-1L);
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Quartal für
	 * die noch kein Termin / Schiene gesetzt wurde
	 *
	 * @param halbjahr das Gosthalbjahr
	 * @param quartal  die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public @NotNull List<@NotNull GostKursklausur> kursklausurOhneTerminGetMengeByHalbjahrAndQuartal(final @NotNull GostHalbjahr halbjahr, final int quartal) {
		if (quartal > 0) {
			final List<@NotNull GostKursklausur> klausuren = _kursklausurmenge_by_halbjahr_and_idTermin_and_quartal.getOrNull(halbjahr.id, -1L, quartal);
			return klausuren != null ? klausuren : new ArrayList<>();
		}
		final List<@NotNull GostKursklausur> klausuren = new ArrayList<>();
		for (final @NotNull List<@NotNull GostKursklausur> kl : _kursklausurmenge_by_halbjahr_and_idTermin_and_quartal.getNonNullValuesOfMap3AsList(halbjahr.id, -1L)) {
			klausuren.addAll(kl);
		}
		return klausuren;
	}

	/**
	 * Gibt eine Liste von Schüler-IDs zurück, die vom übergebenen Termin betroffen
	 * sind.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Liste der betroffenen Schüler-IDs
	 */
	public @NotNull List<@NotNull Long> schueleridsGetMengeByTerminid(final long idTermin) {
		final List<@NotNull Long> schuelerIds = _schuelerIds_by_idTermin.get(idTermin);
		return schuelerIds != null ? schuelerIds : new ArrayList<>();
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Quartal
	 *
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal             die Nummer des Quartals, 0 für alle Quartale
	 * @param includeMultiquartal true, wenn auch für mehrere Quartale geöffnete
	 *                            Termine geliefert werden sollen, sonst false
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public @NotNull List<@NotNull GostKlausurtermin> terminGetMengeByHalbjahrAndQuartal(final @NotNull GostHalbjahr halbjahr, final int quartal, final boolean includeMultiquartal) {
		final List<@NotNull GostKlausurtermin> termine = new ArrayList<>();
		if (quartal > 0) {
			if (_terminmenge_by_halbjahr_and_quartal.getOrNull(halbjahr.id, quartal) != null)
				termine.addAll(_terminmenge_by_halbjahr_and_quartal.getOrNull(halbjahr.id, quartal));
			if (includeMultiquartal && _terminmenge_by_halbjahr_and_quartal.getOrNull(halbjahr.id, 0) != null)
				termine.addAll(_terminmenge_by_halbjahr_and_quartal.getOrNull(halbjahr.id, 0));
			return termine;
		}
		if (_terminmenge_by_halbjahr_and_quartal.containsKey1(halbjahr.id))
			for (@NotNull List<@NotNull GostKlausurtermin> qTermine : _terminmenge_by_halbjahr_and_quartal.getNonNullValuesOfKey1AsList(halbjahr.id)) {
				termine.addAll(qTermine);
			}
		return termine;
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten, die für Nachschreiber zugelassen sind zum übergebenen Quartal
	 *
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal             die Nummer des Quartals, 0 für alle Quartale
	 * @param includeMultiquartal true, wenn auch für mehrere Quartale geöffnete
	 *                            Termine geliefert werden sollen, sonst false
	 *
	 * @return die Liste von NT-GostKlausurtermin-Objekten
	 */
	public @NotNull List<@NotNull GostKlausurtermin> terminGetNTMengeByHalbjahrAndQuartal(final @NotNull GostHalbjahr halbjahr, final int quartal, final boolean includeMultiquartal) {
		final List<@NotNull GostKlausurtermin> termine = new ArrayList<>();
		for (@NotNull GostKlausurtermin t : terminGetMengeByHalbjahrAndQuartal(halbjahr, quartal, includeMultiquartal))
			if (!t.istHaupttermin || t.nachschreiberZugelassen)
				termine.add(t);
		return termine;
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten, die als Haupttermin angelegt wurden zum übergebenen Quartal
	 *
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal             die Nummer des Quartals, 0 für alle Quartale
	 * @param includeMultiquartal true, wenn auch für mehrere Quartale geöffnete
	 *                            Termine geliefert werden sollen, sonst false
	 *
	 * @return die Liste von HT-GostKlausurtermin-Objekten
	 */
	public @NotNull List<@NotNull GostKlausurtermin> terminGetHTMengeByHalbjahrAndQuartal(final @NotNull GostHalbjahr halbjahr, final int quartal, final boolean includeMultiquartal) {
		final List<@NotNull GostKlausurtermin> termine = new ArrayList<>();
		for (@NotNull GostKlausurtermin t : terminGetMengeByHalbjahrAndQuartal(halbjahr, quartal, includeMultiquartal))
			if (t.istHaupttermin)
				termine.add(t);
		return termine;
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten des Halbjahres, bei denen
	 * ein Datum gesetzt ist
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public @NotNull List<@NotNull GostKlausurtermin> terminMitDatumGetMenge() {
		final List<@NotNull GostKlausurtermin> termineMitDatum = new ArrayList<>();
		for (@NotNull final GostKlausurtermin termin : _terminmenge)
			if (termin.datum != null)
				termineMitDatum.add(termin);
		termineMitDatum.sort(_compTermin);
		return termineMitDatum;
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten des Quartals, bei denen ein
	 * Datum gesetzt ist
	 *
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal             die Nummer des Quartals
	 * @param includeMultiquartal true, wenn auch für mehrere Quartale geöffnete
	 *                            Termine geliefert werden sollen, sonst false
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public @NotNull List<@NotNull GostKlausurtermin> terminMitDatumGetMengeByHalbjahrAndQuartal(final @NotNull GostHalbjahr halbjahr, final int quartal, final boolean includeMultiquartal) {
		final List<@NotNull GostKlausurtermin> termineMitDatum = new ArrayList<>();
		for (@NotNull final GostKlausurtermin termin : terminGetMengeByHalbjahrAndQuartal(halbjahr, quartal, includeMultiquartal))
			if (termin.datum != null)
				termineMitDatum.add(termin);
		termineMitDatum.sort(_compTermin);
		return termineMitDatum;
	}

	/**
	 * Liefert das Quartal der Kursklausuren innerhalb des Klausurtermins, sofern
	 * alle identisch sind, sonst -1.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return das Quartal aller Klausuren, sofern identisch, sonst -1.
	 */
	public int quartalGetByTerminid(final long idTermin) {
		final List<@NotNull GostKursklausur> klausuren = _kursklausurmenge_by_idTermin.get(idTermin);
		if (klausuren == null)
			return DeveloperNotificationException.ifMapGetIsNull(_termin_by_id, idTermin).quartal;
		int quartal = -1;
		for (@NotNull final GostKursklausur k : klausuren) {
			if (quartal == -1)
				quartal = k.quartal;
			if (quartal != k.quartal)
				return -1;
		}
		return quartal;
	}

	/**
	 * Liefert die Anzahl der Schülerklausuren zu einem bestimmten Klausurtermin.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Anzahl der Schülerklausuren des Termins.
	 */
	public int schuelerklausurAnzahlGetByTerminid(final long idTermin) {
		return schueleridsGetMengeByTerminid(idTermin).size();
	}

	/**
	 * Liefert die minimale Startzeit des Klausurtermins in Minuten
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die minimale Startzeit
	 */
	public int minKursklausurstartzeitByTerminid(final long idTermin) {
		int minStart = 1440;
		final @NotNull GostKlausurtermin termin = DeveloperNotificationException.ifMapGetIsNull(_termin_by_id, idTermin);
		final List<@NotNull GostKursklausur> kks = _kursklausurmenge_by_idTermin.get(idTermin);
		if (kks == null || kks.isEmpty())
			return termin.startzeit;
		for (final GostKursklausur kk : kks) {
			int skStartzeit = -1;
			if (kk.startzeit != null)
				skStartzeit = kk.startzeit;
			else if (termin.startzeit != null)
				skStartzeit = termin.startzeit;
			else
				throw new DeveloperNotificationException("Startzeit des Termins nicht definiert, Termin-ID: " + idTermin);
			if (skStartzeit < minStart)
				minStart = skStartzeit;
		}
		return minStart;
	}

	/**
	 * Liefert die maximale Endzeit des Klausurtermins in Minuten
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die maximale Endzeit
	 */
	public int maxKursklausurendzeitByTerminid(final long idTermin) {
		int maxEnd = 1;
		final GostKlausurtermin termin = DeveloperNotificationException.ifMapGetIsNull(_termin_by_id, idTermin);
		final List<@NotNull GostKursklausur> kks = _kursklausurmenge_by_idTermin.get(idTermin);
		if (kks == null || kks.isEmpty())
			return maxEnd;
		for (final GostKursklausur kk : kks) {
			@NotNull GostKlausurvorgabe vorgabe = _vorgabenManager.vorgabeGetByIdOrException(kk.idVorgabe);
			int skStartzeit = -1;
			if (kk.startzeit != null)
				skStartzeit = kk.startzeit;
			else if (termin.startzeit != null)
				skStartzeit = termin.startzeit;
			else
				throw new DeveloperNotificationException("Startzeit des Termins nicht definiert, Termin-ID: " + idTermin);
			final int endzeit = skStartzeit + vorgabe.dauer + vorgabe.auswahlzeit;
			if (endzeit > maxEnd)
				maxEnd = endzeit;
		}
		return maxEnd;
	}

	/**
	 * Liefert die maximale Klausurdauer innerhalb eines Klausurtermins
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die maximale Klausurdauer innerhalb des Termins
	 */
	public int maxKlausurdauerGetByTerminid(final long idTermin) {
		final @NotNull List<@NotNull GostKursklausur> klausuren = DeveloperNotificationException.ifMapGetIsNull(_kursklausurmenge_by_idTermin, idTermin);
		int maxDauer = -1;
		for (@NotNull final GostKursklausur klausur : klausuren) {
			@NotNull GostKlausurvorgabe vorgabe = _vorgabenManager.vorgabeGetByIdOrException(klausur.idVorgabe);
			maxDauer = vorgabe.dauer > maxDauer ? vorgabe.dauer : maxDauer;
		}
		return maxDauer;
	}

	private static @NotNull Map<@NotNull GostKursklausur, @NotNull Set<@NotNull Long>> berechneKonflikte(@NotNull final List<@NotNull GostKursklausur> klausuren1,
			@NotNull final List<@NotNull GostKursklausur> klausuren2) {
		final Map<@NotNull GostKursklausur, @NotNull Set<@NotNull Long>> result = new HashMap<>();
		final List<@NotNull GostKursklausur> kursklausuren2Copy = new ArrayList<>(klausuren2);
		for (@NotNull final GostKursklausur kk1 : klausuren1) {
			kursklausuren2Copy.remove(kk1);
			for (@NotNull final GostKursklausur kk2 : kursklausuren2Copy) {
				final Set<@NotNull Long> konflikte = berechneKlausurKonflikte(kk1, kk2);
				if (!konflikte.isEmpty()) {
					MapUtils.getOrCreateHashSet(result, kk1).addAll(konflikte);
					MapUtils.getOrCreateHashSet(result, kk2).addAll(konflikte);
				}
			}
		}
		return result;
	}

	private static @NotNull Set<@NotNull Long> berechneKlausurKonflikte(@NotNull final GostKursklausur kk1, @NotNull final GostKursklausur kk2) {
		final HashSet<@NotNull Long> konflikte = new HashSet<>(kk1.schuelerIds);
		konflikte.retainAll(kk2.schuelerIds);
		return konflikte;
	}

	private static int countKonflikte(@NotNull final Map<@NotNull GostKursklausur, @NotNull Set<@NotNull Long>> konflikte) {
		final @NotNull HashSet<@NotNull Long> susIds = new HashSet<>();
		for (final @NotNull Set<@NotNull Long> klausurSids : konflikte.values())
			susIds.addAll(klausurSids);
		return susIds.size();
	}

	/**
	 * Liefert eine Map Kursklausur -> Schülerids, die die Konflikte in jeder
	 * Klausur der übergebenen Termin-ID enthält
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Map Kursklausur -> Schülerids
	 */
	public @NotNull Map<@NotNull GostKursklausur, @NotNull Set<@NotNull Long>> konflikteMapKursklausurSchueleridsByTerminid(final long idTermin) {
		final List<@NotNull GostKursklausur> klausuren = _kursklausurmenge_by_idTermin.get(idTermin);
		if (klausuren == null)
			return new HashMap<>();
		return berechneKonflikte(klausuren, klausuren);
	}

	/**
	 * Liefert eine Map Kursklausur -> Schülerids, die nur die Konflikte liefert,
	 * die die übergeben Klausur im übergebenen Termin verursacht
	 *
	 * @param idTermin      die ID des Klausurtermins
	 * @param idKursklausur die ID der Kursklausur
	 *
	 * @return die Map Kursklausur -> Schülerids
	 */
	public @NotNull Map<@NotNull GostKursklausur, @NotNull Set<@NotNull Long>> konflikteNeuMapKursklausurSchueleridsByTerminidAndKursklausurid(final long idTermin, final long idKursklausur) {
		final List<@NotNull GostKursklausur> klausuren1 = _kursklausurmenge_by_idTermin.get(idTermin);
		final List<@NotNull GostKursklausur> klausuren2 = new ArrayList<>();
		klausuren2.add(DeveloperNotificationException.ifMapGetIsNull(_kursklausur_by_id, idKursklausur));
		return berechneKonflikte(klausuren1 != null ? klausuren1 : new ArrayList<>(), klausuren2);
	}

	/**
	 * Prüft, ob eine Kursklausur konfliktfrei zu einem bestehenden Klausurtermin
	 * hinzugefügt werden kann. Es werden die Schüler-IDs, die den Konflikt
	 * verursachen, als Liste zurückgegeben. Wenn die zurückgegebene Liste leer ist,
	 * gibt es keinen Konflikt.
	 *
	 * @param klausur die zu prüfende Kursklausur
	 *
	 * @return die Anzahl der Schüler-IDs, die einen Konflikt verursachen.
	 */
	public int konflikteAnzahlZuEigenemTerminGetByKursklausur(final @NotNull GostKursklausur klausur) {
		final @NotNull List<@NotNull GostKursklausur> klausuren1 = new ArrayList<>(DeveloperNotificationException.ifMapGetIsNull(_kursklausurmenge_by_idTermin, klausur.idTermin));
		klausuren1.remove(klausur);
		final List<@NotNull GostKursklausur> klausuren2 = new ArrayList<>();
		klausuren2.add(klausur);
		return countKonflikte(berechneKonflikte(klausuren1, klausuren2));
	}

	/**
	 * Prüft, ob eine Kursklausur konfliktfrei zu einem bestehenden Klausurtermin
	 * hinzugefügt werden kann. Es werden die Schüler-IDs, die den Konflikt
	 * verursachen, als Liste zurückgegeben. Wenn die zurückgegebene Liste leer ist,
	 * gibt es keinen Konflikt.
	 *
	 * @param termin  der zu prüfende Klausurtermin
	 * @param klausur die zu prüfende Kursklausur
	 *
	 * @return die Liste der Schüler-IDs, die einen Konflikt verursachen.
	 */
	public int konflikteAnzahlZuTerminGetByTerminAndKursklausur(final @NotNull GostKlausurtermin termin, final @NotNull GostKursklausur klausur) {
		return countKonflikte(konflikteNeuMapKursklausurSchueleridsByTerminidAndKursklausurid(termin.id, klausur.id));
	}

	/**
	 * Prüft, ob es innerhalb eines bestehenden Klausurtermins Konflikte gibt. Es
	 * wird die Anzahl der Konflikte zurückgegeben.
	 *
	 * @param idTermin die ID des zu prüfenden Klausurtermins
	 *
	 * @return die Anzahl der Konflikte innerhalb des Termins.
	 */
	public int konflikteAnzahlGetByTerminid(final long idTermin) {
		return countKonflikte(konflikteMapKursklausurSchueleridsByTerminid(idTermin));
	}

	/**
	 * Liefert für einen Schwellwert und einen Klausurtermin eine Map, die alle
	 * Schülerids mit einer Kursklausur-Liste enthält, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreibt, als der Schwellwert
	 * definiert
	 *
	 * @param termin    der Klausurtermin, dessen Kalenderwoche geprüft wird
	 * @param threshold der Schwellwert (z.B. 3), der erreicht sein muss, damit die
	 *                  Klausuren in die Map aufgenommen werden
	 *
	 * @return die Map (Schülerid -> GostKursklausur)
	 */
	public @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostKursklausur>> klausurenProSchueleridExceedingKWThresholdByTerminAndThreshold(final @NotNull GostKlausurtermin termin,
			final int threshold) {
		return klausurenProSchueleridExceedingKWThresholdByTerminAndKursklausurAndThreshold(termin, null, threshold);
	}

	/**
	 * Liefert für einen Schwellwert, einen Klausurtermin und eine Kursklausur eine
	 * Map, die alle Schülerids mit einer Kursklausur-Liste enthält, die in der den
	 * Termin enthaltenen Kalenderwoche mehr (>=) Klausuren schreibt, als der
	 * Schwellwert definiert, wenn die übergebene Kursklausur in den Termin
	 * integriert würde
	 *
	 * @param termin    der Klausurtermin, dessen Kalenderwoche geprüft wird
	 * @param klausur   die Klausur, deren Integration in den Termin angenommen wird
	 * @param threshold der Schwellwert (z.B. 3), der erreicht sein muss, damit die
	 *                  Klausuren berücksichtigt werden
	 *
	 * @return die Map (Schülerid -> GostKursklausur)
	 */
	public @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostKursklausur>> klausurenProSchueleridExceedingKWThresholdByTerminAndKursklausurAndThreshold(final @NotNull GostKlausurtermin termin,
			final GostKursklausur klausur, final int threshold) {
		Map<@NotNull Long, @NotNull List<@NotNull GostKursklausur>> ergebnis = new HashMap<>();
		if (termin.datum == null)
			return ergebnis;
		int kw = DateUtils.gibKwDesDatumsISO8601(termin.datum);
		Map<@NotNull Long, List<@NotNull GostKursklausur>> kursklausurmenge_by_schuelerId = _kursklausurmenge_by_kw_and_schuelerId.getSubMapOrNull(kw);
		if (kursklausurmenge_by_schuelerId == null)
			return ergebnis;
		for (@NotNull Entry<@NotNull Long, List<@NotNull GostKursklausur>> entry : kursklausurmenge_by_schuelerId.entrySet()) {
			List<@NotNull GostKursklausur> temp = entry.getValue();
			List<@NotNull GostKursklausur> klausuren = temp != null ? new ArrayList<>(temp) : new ArrayList<>();
			if (klausur != null && klausur.idTermin != termin.id && klausur.schuelerIds.contains(entry.getKey()))
				klausuren.add(klausur);
			if (klausuren.size() >= threshold)
				ergebnis.put(entry.getKey(), klausuren);
		}
		return ergebnis;
	}

	/**
	 * Liefert für einen Schwellwert und einen Klausurtermin eine Map, die alle
	 * Schülerids mit einer Kursklausur-Liste enthält, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreibt, als der Schwellwert
	 * definiert
	 *
	 * @param termin        der Klausurtermin, dessen Kalenderwoche geprüft wird
	 * @param datum         das Datum, auf
	 * @param threshold     der Schwellwert (z.B. 3), der erreicht sein muss, damit
	 *                      die Klausuren in die Map aufgenommen werden
	 * @param thresholdOnly nur die exakte Anzahl an Klausurkonflikten wird in die
	 *                      Ergebnismap übernommen
	 *
	 * @return die Map (Schülerid -> GostKursklausur)
	 */
	public @NotNull Map<@NotNull Long, @NotNull HashSet<@NotNull GostKursklausur>> klausurenProSchueleridExceedingKWThresholdByTerminAndDatumAndThreshold(final @NotNull GostKlausurtermin termin,
			final @NotNull String datum, final int threshold, final boolean thresholdOnly) {
		int kwDatum = DateUtils.gibKwDesDatumsISO8601(datum);
		return klausurenProSchueleridExceedingKWThresholdByKwAndTerminAndThreshold(kwDatum, termin, threshold, thresholdOnly);
	}

	private @NotNull Map<@NotNull Long, @NotNull HashSet<@NotNull GostKursklausur>> klausurenProSchueleridExceedingKWThresholdByKwAndTerminAndThreshold(final int kw, final GostKlausurtermin termin,
			final int threshold, final boolean thresholdOnly) {
		Map<@NotNull Long, @NotNull HashSet<@NotNull GostKursklausur>> ergebnis = new HashMap<>();

		Map<@NotNull Long, List<@NotNull GostKursklausur>> kursklausurmenge_by_schuelerId = _kursklausurmenge_by_kw_and_schuelerId.getSubMapOrNull(kw);
		if (kursklausurmenge_by_schuelerId == null)
			return ergebnis;

		for (@NotNull Entry<@NotNull Long, List<@NotNull GostKursklausur>> entry : kursklausurmenge_by_schuelerId.entrySet()) {
			List<@NotNull GostKursklausur> temp = entry.getValue();
			HashSet<@NotNull GostKursklausur> klausuren = temp != null ? new HashSet<>(temp) : new HashSet<>();
			if (termin != null) {
				List<@NotNull GostKursklausur> klausurenInTermin = _kursklausurmenge_by_terminId_and_schuelerId.getOrNull(termin.id, entry.getKey());
				if (klausurenInTermin != null)
					klausuren.addAll(klausurenInTermin);
			}
			if (klausuren.size() == threshold || (klausuren.size() > threshold && !thresholdOnly))
				ergebnis.put(entry.getKey(), klausuren);
		}
		return ergebnis;
	}

	/**
	 * Liefert für einen Schwellwert und einen Klausurtermin eine Map, die alle
	 * Schülerids mit einer Kursklausur-Liste enthält, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreibt, als der Schwellwert
	 * definiert
	 *
	 * @param kw            der Klausurtermin, dessen Kalenderwoche geprüft wird
	 * @param threshold     der Schwellwert (z.B. 3), der erreicht sein muss, damit
	 *                      die Klausuren in die Map aufgenommen werden
	 * @param thresholdOnly nur die exakte Anzahl an Klausurkonflikten wird in die
	 *                      Ergebnismap übernommen
	 *
	 * @return die Map (Schülerid -> GostKursklausur)
	 */
	public @NotNull Map<@NotNull Long, @NotNull HashSet<@NotNull GostKursklausur>> klausurenProSchueleridExceedingKWThresholdByKwAndThreshold(final int kw, final int threshold,
			final boolean thresholdOnly) {
		return klausurenProSchueleridExceedingKWThresholdByKwAndTerminAndThreshold(kw, null, threshold, thresholdOnly);
	}

	/**
	 * Liefert den Klausurtermin zu einer Kursklausur, sonst NULL.
	 *
	 * @param klausur die Kursklausur, zu der der Termin gesucht wird.
	 *
	 * @return den Klausurtermin
	 */
	public GostKlausurtermin terminByKursklausur(final @NotNull GostKursklausur klausur) {
		return _termin_by_id.get(klausur.idTermin);
	}

	/**
	 * Liefert den Klausurtermin zu einem Schülerklausurtermin oder NULL.
	 *
	 * @param termin der Schülerklausurtermin, zu dem der Termin gesucht wird.
	 *
	 * @return den Klausurtermin
	 */
	public GostKlausurtermin terminBySchuelerklausurTermin(final @NotNull GostSchuelerklausurTermin termin) {
		if (termin.folgeNr > 0)
			return termin.idTermin == null ? null : terminGetByIdOrException(termin.idTermin);
		return terminByKursklausur(kursklausurBySchuelerklausurTermin(termin));
	}

	/**
	 * Liefert den Klausurtermin zu einer Schülerklausur oder NULL.
	 *
	 * @param sk die Schülerklausur
	 *
	 * @return den Klausurtermin
	 */
	public GostKlausurtermin terminKursklausurBySchuelerklausur(final @NotNull GostSchuelerklausur sk) {
		return terminByKursklausur(kursklausurBySchuelerklausur(sk));
	}

	/**
	 * Liefert die Klausurvorgabe zu einer Kursklausur.
	 *
	 * @param klausur die Kursklausur, zu der die Vorgabe gesucht wird.
	 *
	 * @return die Klausurvorgabe
	 */
	public @NotNull GostKlausurvorgabe vorgabeByKursklausur(final @NotNull GostKursklausur klausur) {
		return _vorgabenManager.vorgabeGetByIdOrException(klausur.idVorgabe);
	}

	/**
	 * Liefert die Klausurvorgabe zu einer Schuelerklausur.
	 *
	 * @param klausur die Schuelerklausur, zu der die Vorgabe gesucht wird.
	 *
	 * @return die Klausurvorgabe
	 */
	public @NotNull GostKlausurvorgabe vorgabeBySchuelerklausur(final @NotNull GostSchuelerklausur klausur) {
		@NotNull GostKursklausur kk = kursklausurGetByIdOrException(klausur.idKursklausur);
		return _vorgabenManager.vorgabeGetByIdOrException(kk.idVorgabe);
	}

	/**
	 * Liefert die Klausurvorgabe zu einem Schuelerklausurtermin.
	 *
	 * @param klausur der Schuelerklausurtermin, zu der die Vorgabe gesucht wird.
	 *
	 * @return die Klausurvorgabe
	 */
	public @NotNull GostKlausurvorgabe vorgabeBySchuelerklausurTermin(final @NotNull GostSchuelerklausurTermin klausur) {
		return vorgabeBySchuelerklausur(schuelerklausurGetByIdOrException(klausur.idSchuelerklausur));
	}

	/**
	 * Liefert die GostKursklausur zu einer Schuelerklausur.
	 *
	 * @param klausur die Schuelerklausur, zu der die GostKursklausur gesucht wird.
	 *
	 * @return die GostKursklausur
	 */
	public @NotNull GostKursklausur kursklausurBySchuelerklausur(final @NotNull GostSchuelerklausur klausur) {
		return kursklausurGetByIdOrException(klausur.idKursklausur);
	}

	/**
	 * Liefert die GostKursklausur zu einem Schuelerklausurtermin.
	 *
	 * @param termin der Schuelerklausurtermin, zu der die GostKursklausur gesucht wird.
	 *
	 * @return die GostKursklausur
	 */
	public @NotNull GostKursklausur kursklausurBySchuelerklausurTermin(final @NotNull GostSchuelerklausurTermin termin) {
		return kursklausurBySchuelerklausur(schuelerklausurGetByIdOrException(termin.idSchuelerklausur));
	}

	/**
	 * Liefert zurück, ob die übergebene Klausurvorgabe von einer Kursklausur
	 * verwendet wird.
	 *
	 * @param vorgabe die Klausurvorgabe, die auf Verwendung geprüft werden soll.
	 *
	 * @return true oder false
	 */
	public boolean istVorgabeVerwendetByVorgabe(final @NotNull GostKlausurvorgabe vorgabe) {
		List<@NotNull GostKursklausur> klausuren = _kursklausurmenge_by_idVorgabe.get(vorgabe.idVorgabe);
		return klausuren != null && !klausuren.isEmpty();
	}

	/**
	 * Liefert das GostKursklausur-Objekt zu den übergebenen Parametern.
	 *
	 * @param idKurs  die ID des Kurses
	 * @param halbjahr das Gosthalbjahr
	 * @param quartal das Quartal der Klausur
	 *
	 * @return die Kursklausur
	 */
	public GostKursklausur kursklausurByKursidAndHalbjahrAndQuartal(final long idKurs, final @NotNull GostHalbjahr halbjahr, final int quartal) {
		return _kursklausur_by_idKurs_and_halbjahr_and_quartal.getOrNull(idKurs, halbjahr.id, quartal);
	}

	/**
	 * Liefert die Vorgänger-GostKursklausur aus dem letzten Quartal, soweit
	 * vorhanden.
	 *
	 * @param klausur die Kursklausur, deren Vorgänger gesucht wird
	 *
	 * @return die Kursklausur
	 */
	public GostKursklausur kursklausurVorterminByKursklausur(final @NotNull GostKursklausur klausur) {
		GostKlausurvorgabe previousVorgabe = _vorgabenManager.getPrevious(_vorgabenManager.vorgabeGetByIdOrException(klausur.idVorgabe));
		if (previousVorgabe == null)
			return null;
		List<@NotNull GostKursklausur> klausuren = _kursklausurmenge_by_idVorgabe.get(previousVorgabe.idVorgabe);
		if (klausuren == null)
			return null;
		for (@NotNull GostKursklausur k : klausuren) {
			if (k.kursKurzbezeichnung.equals(klausur.kursKurzbezeichnung)) // TODO unsauber, aber KursId geht nicht, weil ggf. in Schuljahresabschnitten unterschiedlich
				return k;
		}
		return null;
	}

	/**
	 * Gibt die Startzeit der übergebenen Klausur aus. Falls keine individuelle gesetzt ist, wird die des Termins zurückgegeben.
	 * Sollte kein Termin gesetzt sein oder der Termin keine Startzeit definiert haben, wird null zurückgegeben.
	 *
	 * @param klausur die Kursklausur, deren Startzeit gesucht wird.
	 *
	 * @return die Startzeit der Klausur
	 */
	public Integer startzeitByKursklausur(final @NotNull GostKursklausur klausur) {
		GostKlausurtermin termin = terminByKursklausur(klausur);
		if (klausur.startzeit != null)
			return klausur.startzeit;
		return termin == null ? null : termin.startzeit;
	}

	/**
	 * Gibt die Startzeit der übergebenen Klausur aus. Falls keine individuelle gesetzt ist, wird die des Termins zurückgegeben.
	 * Sollte kein Termin gesetzt sein oder der Termin keine Startzeit definiert haben, wird null zurückgegeben.
	 *
	 * @param klausur die Kursklausur, deren Startzeit gesucht wird.
	 *
	 * @return die Startzeit der Klausur
	 */
	public boolean hatAbweichendeStartzeitByKursklausur(final @NotNull GostKursklausur klausur) {
		GostKlausurtermin termin = terminByKursklausur(klausur);
		return !(klausur.startzeit == null || termin == null || termin.startzeit == null || termin.startzeit.equals(klausur.startzeit));
	}

	/**
	 * Gibt die Liste von Schülerklausur-Terminen zu einer Schülerklausur zurück.
	 *
	 * @param sk die Schülerklausur, zu der die Termine gesucht werden.
	 *
	 * @return die Liste von Schülerklausur-Terminen
	 */
	public @NotNull List<@NotNull GostSchuelerklausurTermin> schuelerklausurterminGetMengeBySchuelerklausur(@NotNull final GostSchuelerklausur sk) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerklausurterminmenge_by_idSchuelerklausur, sk.id);
	}

	/**
	 * Gibt die Liste von Schülerklausur-Terminen zu einem Klausurtermin zurück.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Liste von Schülerklausur-Terminen
	 */
	public @NotNull List<@NotNull GostSchuelerklausurTermin> schuelerklausurterminGetMengeByTerminid(@NotNull final long idTermin) {
		List<@NotNull GostSchuelerklausurTermin> list = _schuelerklausurterminmenge_by_idTermin.get(idTermin);
		return list != null ? list : new ArrayList<>();
	}

	/**
	 * Prüft, ob der übergebene Schülerklausurtermin der aktuellste Termin der Schülerklausur ist.
	 *
	 * @param skt der Schülerklausurtermin, der geprüft werden soll
	 *
	 * @return true, wenn es sich um den aktuellen Termin handelt, sonst false
	 */
	public boolean istAktuellerSchuelerklausurtermin(final @NotNull GostSchuelerklausurTermin skt) {
		@NotNull List<GostSchuelerklausurTermin> skts = DeveloperNotificationException.ifMapGetIsNull(_schuelerklausurterminmenge_by_idSchuelerklausur, skt.idSchuelerklausur);
		return skts.get(skts.size() - 1) == skt;
	}

	/**
	 * Liefert eine Liste von aktuellen Nachschreib-GostSchuelerklausurTermin-Objekten zum übergebenen Quartal für
	 * die noch kein Termin gesetzt wurde
	 *
	 * @param halbjahr das Gosthalbjahr
	 * @param quartal  die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von GostSchuelerklausurTermin-Objekten
	 */
	public @NotNull List<@NotNull GostSchuelerklausurTermin> schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(final @NotNull GostHalbjahr halbjahr, final int quartal) {
		if (quartal > 0) {
			final List<@NotNull GostSchuelerklausurTermin> skts = _schuelerklausurterminntaktuellmenge_by_halbjahr_and_idTermin_and_quartal.getOrNull(halbjahr.id, -1L, quartal);
			return skts != null ? skts : new ArrayList<>();
		}
		final List<@NotNull GostSchuelerklausurTermin> skts = new ArrayList<>();
		for (final @NotNull List<@NotNull GostSchuelerklausurTermin> sktList : _schuelerklausurterminntaktuellmenge_by_halbjahr_and_idTermin_and_quartal.getNonNullValuesOfMap3AsList(halbjahr.id, -1L)) {
			skts.addAll(sktList);
		}
		return skts;
	}

	/**
	 * Liefert den GostSchuelerklausurTermin, sofern vorhanden, zu einer Klausurtermin-ID und einer Schüler-ID
	 *
	 * @param idTermin die ID des Klausurtermins
	 * @param idSchueler  die ID des Schülers
	 *
	 * @return das GostSchuelerklausurTermin-Objekt, sofern vorhanden
	 */
	public GostSchuelerklausurTermin schuelerklausurterminByTerminidAndSchuelerid(final long idTermin, final long idSchueler) {
		List<@NotNull GostSchuelerklausurTermin> skts = _schuelerklausurterminmenge_by_idTermin.get(idTermin);
		if (skts != null)
			for (@NotNull GostSchuelerklausurTermin skt : skts)
				if (schuelerklausurGetByIdOrException(skt.idSchuelerklausur).idSchueler == idSchueler)
					return skt;
		return null;
	}

}
