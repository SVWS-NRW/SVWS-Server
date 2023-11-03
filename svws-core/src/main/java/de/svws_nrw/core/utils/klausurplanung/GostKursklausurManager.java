package de.svws_nrw.core.utils.klausurplanung;

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
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.core.utils.Map2DUtils;
import de.svws_nrw.core.utils.Map3DUtils;
import de.svws_nrw.core.utils.MapUtils;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link GostKursklausur}.
 * Hierbei werden auch Hilfsmethoden zur Interpretation der Daten erzeugt. Es
 * werden Kursklausuren eines Gost-Halbjahres eines Abiturjahrgangs verwaltet.
 */
public class GostKursklausurManager {

	private final @NotNull GostKlausurvorgabenManager _vorgabenManager;

	private static final @NotNull Comparator<@NotNull GostKlausurtermin> _compTermin = (final @NotNull GostKlausurtermin a, final @NotNull GostKlausurtermin b) -> {
		if (a.datum == null && b.datum != null)
			return +1;
		if (b.datum == null && a.datum != null)
			return -1;
		if (a.datum != null && b.datum != null)
			return a.datum.compareTo(b.datum);
		if (a.quartal != b.quartal)
			return a.quartal - b.quartal;
		return a.id > b.id ? +1 : -1;
	};

	private static final @NotNull Comparator<@NotNull GostKursklausur> _compKursklausur = (final @NotNull GostKursklausur a, final @NotNull GostKursklausur b) -> {
		if (a.halbjahr != b.halbjahr)
			return a.halbjahr - b.halbjahr;
		if (a.quartal != b.quartal)
			return a.quartal - b.quartal;
		if (a.idFach != b.idFach)
			return a.idFach > b.idFach ? +1 : -1;
		if (!a.kursart.equals(b.kursart))
			return GostKursart.fromKuerzelOrException(a.kursart).compareTo(GostKursart.fromKuerzelOrException(b.kursart));
		return a.id > b.id ? +1 : -1;
	};

	// GostKursklausur
	private final @NotNull Map<@NotNull Long, @NotNull GostKursklausur> _kursklausur_by_id = new HashMap<>();
	private final @NotNull List<@NotNull GostKursklausur> _kursklausurmenge = new ArrayList<>();
	private final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull GostKursklausur>> _kursklausurmenge_by_quartal = new HashMap<>();
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostKursklausur>> _kursklausurmenge_by_idTermin = new HashMap<>();
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostKursklausur>> _kursklausurmenge_by_idVorgabe = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Long, @NotNull List<@NotNull GostKursklausur>> _kursklausurmenge_by_quartal_and_idTermin = new HashMap2D<>();
	private final @NotNull HashMap3D<@NotNull Integer, @NotNull String, @NotNull Long, @NotNull List<@NotNull GostKursklausur>> _kursklausurmenge_by_quartal_and_kursart_and_idTermin = new HashMap3D<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Long, @NotNull List<@NotNull GostKursklausur>> _kursklausurmenge_by_kw_and_schuelerId = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull List<@NotNull GostKursklausur>> _kursklausurmenge_by_terminId_and_schuelerId = new HashMap2D<>();

	// GostKlausurtermin
	private final @NotNull Map<@NotNull Long, @NotNull GostKlausurtermin> _termin_by_id = new HashMap<>();
	private final @NotNull List<@NotNull GostKlausurtermin> _terminmenge = new ArrayList<>();
	private final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull GostKlausurtermin>> _terminmenge_by_quartal = new HashMap<>();
	private final @NotNull Map<@NotNull String, @NotNull List<@NotNull GostKlausurtermin>> _terminmenge_by_datum = new HashMap<>();

	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull Long>> _schuelerIds_by_idTermin = new HashMap<>();

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param vorgabenManager der Klausurvorgaben-Manager
	 * @param listKlausuren   die Liste der GostKursklausuren eines Abiturjahrgangs
	 *                        und Gost-Halbjahres
	 * @param listTermine     die Liste der GostKlausurtermine eines Abiturjahrgangs
	 *                        und Gost-Halbjahres
	 */
	public GostKursklausurManager(final @NotNull GostKlausurvorgabenManager vorgabenManager, final @NotNull List<@NotNull GostKursklausur> listKlausuren,
			final List<@NotNull GostKlausurtermin> listTermine) {
		_vorgabenManager = vorgabenManager;
		initAll(listKlausuren, listTermine);
	}

//	/**
//	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
//	 * und erzeugt die privaten Attribute.
//	 *
//	 * @param vorgabenManager der Klausurvorgaben-Manager
//	 * @param listKlausuren die Liste der GostKursklausuren eines Abiturjahrgangs
//	 *                      und Gost-Halbjahres
//	 */
//	public GostKursklausurManager(final @NotNull GostKlausurvorgabenManager vorgabenManager, final @NotNull List<@NotNull GostKursklausur> listKlausuren) {
//		_vorgabenManager = vorgabenManager;
//		initAll(listKlausuren, new ArrayList<>());
//	}

	private void initAll(final @NotNull List<@NotNull GostKursklausur> listKlausuren, final List<@NotNull GostKlausurtermin> listTermine) {

		kursklausurAddAll(listKlausuren);
		terminAddAll(listTermine != null ? listTermine : new ArrayList<>());

		update_all();

	}

	private void update_all() {

		update_kursklausurmenge();
		update_terminmenge();

		update_kursklausurmenge_by_quartal();
		update_kursklausurmenge_by_idTermin();
		update_kursklausurmenge_by_idVorgabe();
		update_kursklausurmenge_by_quartal_and_idTermin();
		update_kursklausurmenge_by_quartal_and_kursart_and_idTermin();
		update_terminmenge_by_quartal();
		update_terminmenge_by_datum();
		update_kursklausurmenge_by_terminId_and_schuelerId();

		update_schuelerIds_by_idTermin(); // benötigt _kursklausurmenge_by_idTermin
		update_kursklausurmenge_by_kw_and_schuelerId(); // benötigt _kursklausurmenge_by_idTermin
	}

	private void update_kursklausurmenge_by_quartal() {
		_kursklausurmenge_by_quartal.clear();
		for (final @NotNull GostKursklausur kk : _kursklausurmenge)
			MapUtils.getOrCreateArrayList(_kursklausurmenge_by_quartal, kk.quartal).add(kk);
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

	private void update_kursklausurmenge_by_quartal_and_idTermin() {
		_kursklausurmenge_by_quartal_and_idTermin.clear();
		for (final @NotNull GostKursklausur kk : _kursklausurmenge)
			Map2DUtils.getOrCreateArrayList(_kursklausurmenge_by_quartal_and_idTermin, kk.quartal, kk.idTermin != null ? kk.idTermin : -1).add(kk);
	}

	private void update_kursklausurmenge_by_quartal_and_kursart_and_idTermin() {
		_kursklausurmenge_by_quartal_and_kursart_and_idTermin.clear();
		for (final @NotNull GostKursklausur kk : _kursklausurmenge)
			Map3DUtils.getOrCreateArrayList(_kursklausurmenge_by_quartal_and_kursart_and_idTermin, kk.quartal, kk.kursart, kk.idTermin != null ? kk.idTermin : -1).add(kk);
	}

	private void update_terminmenge_by_quartal() {
		_terminmenge_by_quartal.clear();
		for (final @NotNull GostKlausurtermin t : _terminmenge)
			MapUtils.getOrCreateArrayList(_terminmenge_by_quartal, t.quartal).add(t);
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

	// #####################################################################
	// #################### GostKursklausur ################################
	// #####################################################################

	private void update_kursklausurmenge() {
		_kursklausurmenge.clear();
		_kursklausurmenge.addAll(_kursklausur_by_id.values());
		_kursklausurmenge.sort(_compKursklausur);
	}

	private void kursklausurAddOhneUpdate(final @NotNull GostKursklausur kursklausur) {
		kursklausurCheck(kursklausur);
		DeveloperNotificationException.ifMapPutOverwrites(_kursklausur_by_id, kursklausur.id, kursklausur);
	}

	/**
	 * Fügt ein {@link GostKursklausur}-Objekt hinzu.
	 *
	 * @param kursklausur Das {@link GostKursklausur}-Objekt, welches hinzugefügt
	 *                    werden soll.
	 */
	public void kursklausurAdd(final @NotNull GostKursklausur kursklausur) {
		kursklausurAddOhneUpdate(kursklausur);

		update_all();
	}

	private void kursklausurAddAllOhneUpdate(final @NotNull List<@NotNull GostKursklausur> listKursklausuren) {
		for (final @NotNull GostKursklausur kursklausur : listKursklausuren)
			kursklausurAddOhneUpdate(kursklausur);
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

	private void terminAddOhneUpdate(final @NotNull GostKlausurtermin termin) {
		terminCheck(termin);
		DeveloperNotificationException.ifMapPutOverwrites(_termin_by_id, termin.id, termin);
	}

	/**
	 * Fügt ein {@link GostKlausurtermin}-Objekt hinzu.
	 *
	 * @param termin Das {@link GostKlausurtermin}-Objekt, welches hinzugefügt
	 *               werden soll.
	 */
	public void terminAdd(final @NotNull GostKlausurtermin termin) {
		terminAddOhneUpdate(termin);

		update_all();
	}

	private void terminAddAllOhneUpdate(final @NotNull List<@NotNull GostKlausurtermin> listTermine) {
		for (final @NotNull GostKlausurtermin termin : listTermine)
			terminAddOhneUpdate(termin);
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
		DeveloperNotificationException.ifInvalidID("kursklausur.id", termin.id);
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
	 * @param quartal die Nummer des Quartals
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public List<@NotNull GostKursklausur> kursklausurGetMengeByQuartal(final int quartal) {
		return _kursklausurmenge_by_quartal.get(quartal);
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
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public @NotNull List<@NotNull GostKursklausur> kursklausurOhneTerminGetMengeByQuartal(final int quartal) {
		if (quartal > 0) {
			final List<@NotNull GostKursklausur> klausuren = _kursklausurmenge_by_quartal_and_idTermin.getOrNull(quartal, -1L);
			return klausuren != null ? klausuren : new ArrayList<>();
		}
//		final List<@NotNull GostKursklausur> klausuren = new ArrayList<>();
//		final List<@NotNull List<@NotNull GostKursklausur>> klausurListen = _kursklausurmenge_by_quartal_and_idTermin.getNonNullValuesAsList();
//		for (final @NotNull List<@NotNull GostKursklausur> kl : klausurListen) {
//			klausuren.addAll(kl);
//		}
//		return klausuren;
		return kursklausurOhneTerminGetMenge();
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
	 * @param quartal             die Nummer des Quartals, 0 für alle Quartale
	 * @param includeMultiquartal true, wenn auch für mehrere Quartale geöffnete
	 *                            Termine geliefert werden sollen, sonst false
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public @NotNull List<@NotNull GostKlausurtermin> terminGetMengeByQuartal(final int quartal, final boolean includeMultiquartal) {
		final List<@NotNull GostKlausurtermin> termine = new ArrayList<>();
		if (quartal > 0) {
			if (_terminmenge_by_quartal.get(quartal) != null)
				termine.addAll(_terminmenge_by_quartal.get(quartal));
			if (includeMultiquartal && _terminmenge_by_quartal.get(0) != null)
				termine.addAll(_terminmenge_by_quartal.get(0));
			return termine;
		}
		return terminGetMengeAsList();
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
	 * @param quartal             die Nummer des Quartals
	 * @param includeMultiquartal true, wenn auch für mehrere Quartale geöffnete
	 *                            Termine geliefert werden sollen, sonst false
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public @NotNull List<@NotNull GostKlausurtermin> terminMitDatumGetMengeByQuartal(final int quartal, final boolean includeMultiquartal) {
		final List<@NotNull GostKlausurtermin> termineMitDatum = new ArrayList<>();
		for (@NotNull final GostKlausurtermin termin : terminGetMengeByQuartal(quartal, includeMultiquartal))
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
		final GostKlausurtermin termin = DeveloperNotificationException.ifMapGetIsNull(_termin_by_id, idTermin);
		for (final GostKursklausur kk : DeveloperNotificationException.ifMapGetIsNull(_kursklausurmenge_by_idTermin, idTermin)) {
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
		int maxEnd = 0;
		final GostKlausurtermin termin = DeveloperNotificationException.ifMapGetIsNull(_termin_by_id, idTermin);
		for (final GostKursklausur kk : DeveloperNotificationException.ifMapGetIsNull(_kursklausurmenge_by_idTermin, idTermin)) {
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
		@NotNull final List<@NotNull GostKursklausur> klausuren = DeveloperNotificationException.ifMapGetIsNull(_kursklausurmenge_by_idTermin, idTermin);
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
		final List<@NotNull GostKursklausur> klausuren1 = DeveloperNotificationException.ifMapGetIsNull(_kursklausurmenge_by_idTermin, idTermin);
		final List<@NotNull GostKursklausur> klausuren2 = new ArrayList<>();
		klausuren2.add(DeveloperNotificationException.ifMapGetIsNull(_kursklausur_by_id, idKursklausur));
		return berechneKonflikte(klausuren1, klausuren2);
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
	 * @param termin    der Klausurtermin, dessen Kalenderwoche geprüft wird
	 * @param datum     das Datum, auf
	 * @param threshold der Schwellwert (z.B. 3), der erreicht sein muss, damit die
	 *                  Klausuren in die Map aufgenommen werden
	 * @param thresholdOnly nur die exakte Anzahl an Klausurkonflikten wird in die Ergebnismap übernommen
	 *
	 * @return die Map (Schülerid -> GostKursklausur)
	 */
	public @NotNull Map<@NotNull Long, @NotNull HashSet<@NotNull GostKursklausur>> klausurenProSchueleridExceedingKWThresholdByTerminAndDatumAndThreshold(final @NotNull GostKlausurtermin termin,
			final @NotNull String datum, final int threshold, final boolean thresholdOnly) {
		int kwDatum = DateUtils.gibKwDesDatumsISO8601(datum);
		return klausurenProSchueleridExceedingKWThresholdByKwAndTerminAndThreshold(kwDatum, termin, threshold, thresholdOnly);
	}

	private @NotNull Map<@NotNull Long, @NotNull HashSet<@NotNull GostKursklausur>> klausurenProSchueleridExceedingKWThresholdByKwAndTerminAndThreshold(final int kw, final GostKlausurtermin termin, final int threshold, final boolean thresholdOnly) {
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
	 * @param kw        der Klausurtermin, dessen Kalenderwoche geprüft wird
	 * @param threshold der Schwellwert (z.B. 3), der erreicht sein muss, damit die
	 *                  Klausuren in die Map aufgenommen werden
	 * @param thresholdOnly nur die exakte Anzahl an Klausurkonflikten wird in die Ergebnismap übernommen
	 *
	 * @return die Map (Schülerid -> GostKursklausur)
	 */
	public @NotNull Map<@NotNull Long, @NotNull HashSet<@NotNull GostKursklausur>> klausurenProSchueleridExceedingKWThresholdByKwAndThreshold(final int kw, final int threshold, final boolean thresholdOnly) {
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

}
