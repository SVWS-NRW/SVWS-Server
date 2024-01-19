package de.svws_nrw.core.utils.gost.klausurplanung;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.adt.map.HashMap3D;
import de.svws_nrw.core.adt.map.HashMap4D;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.core.utils.Map2DUtils;
import de.svws_nrw.core.utils.Map3DUtils;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link GostKlausurvorgabe}.
 * Hierbei werden auch Hilfsmethoden zur Interpretation der Daten erzeugt. Es
 * werden Klausurvorgaben eines Gost-Halbjahres eines Abiturjahrgangs verwaltet.
 */
public class GostKlausurvorgabenManager {

	private GostFaecherManager _faecherManager;

	private final @NotNull Comparator<@NotNull GostKlausurvorgabe> _compVorgabe = (final @NotNull GostKlausurvorgabe a, final @NotNull GostKlausurvorgabe b) -> {
		if (a.kursart.compareTo(b.kursart) < 0)
			return +1;
		if (a.kursart.compareTo(b.kursart) > 0)
			return -1;
		if (_faecherManager != null) {
			final GostFach aFach = _faecherManager.get(a.idFach);
			final GostFach bFach = _faecherManager.get(b.idFach);
			if (aFach != null && bFach != null) {
				if (aFach.sortierung > bFach.sortierung)
					return +1;
				if (aFach.sortierung < bFach.sortierung)
					return -1;
			}
		}
		if (a.halbjahr != b.halbjahr)
			return Integer.compare(a.halbjahr, b.halbjahr);
		return Integer.compare(a.quartal, b.quartal);
	};

	// GostKlausurvorgabe
	private final @NotNull Map<@NotNull Long, @NotNull GostKlausurvorgabe> _vorgabe_by_id = new HashMap<>();
	private final @NotNull List<@NotNull GostKlausurvorgabe> _vorgabenmenge = new ArrayList<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Integer, @NotNull List<@NotNull GostKlausurvorgabe>> _vorgabenmenge_by_halbjahr_and_quartal = new HashMap2D<>();
	private final @NotNull HashMap4D<@NotNull Integer, @NotNull Integer, @NotNull String, @NotNull Long, @NotNull GostKlausurvorgabe> _vorgabe_by_halbjahr_and_quartal_and_kursartAllg_and_idFach = new HashMap4D<>();
	private final @NotNull HashMap3D<@NotNull Integer, @NotNull String, @NotNull Long, @NotNull List<@NotNull GostKlausurvorgabe>> _vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach = new HashMap3D<>();


	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param listVorgaben die Liste der GostKlausurvorgaben eines Abiturjahrgangs
	 *                      und Gost-Halbjahres
	 * @param faecherManager   der Fächermanager
	 */
	public GostKlausurvorgabenManager(final @NotNull List<@NotNull GostKlausurvorgabe> listVorgaben, final GostFaecherManager faecherManager) {
		_faecherManager = faecherManager;
		initAll(listVorgaben);
	}

	private void initAll(final @NotNull List<@NotNull GostKlausurvorgabe> listVorgaben) {

		vorgabeAddAll(listVorgaben);

		update_all();

	}

	/**
	 * Liefert den Fächermanager
	 *
	 * @return den Fächermanager
	 */
	public GostFaecherManager getFaecherManager() {
		return _faecherManager;
	}

	private void update_all() {

		update_vorgabemenge();

		update_vorgabenmenge_by_halbjahr_and_quartal();
		update_vorgabe_by_halbjahr_and_quartal_and_kursartAllg_and_idFach();
		update_vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach();

	}


	private void update_vorgabenmenge_by_halbjahr_and_quartal() {
		_vorgabenmenge_by_halbjahr_and_quartal.clear();
		for (final @NotNull GostKlausurvorgabe v : _vorgabenmenge) {
			Map2DUtils.getOrCreateArrayList(_vorgabenmenge_by_halbjahr_and_quartal, v.halbjahr, v.quartal).add(v);
		}
	}

	private void update_vorgabe_by_halbjahr_and_quartal_and_kursartAllg_and_idFach() {
		_vorgabe_by_halbjahr_and_quartal_and_kursartAllg_and_idFach.clear();
		for (final @NotNull GostKlausurvorgabe v : _vorgabenmenge)
			_vorgabe_by_halbjahr_and_quartal_and_kursartAllg_and_idFach.put(v.halbjahr, v.quartal, v.kursart, v.idFach, v);
	}

	private void update_vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach() {
		_vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach.clear();
		for (final @NotNull GostKlausurvorgabe v : _vorgabenmenge)
			Map3DUtils.getOrCreateArrayList(_vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach, v.halbjahr, v.kursart, v.idFach).add(v);
	}


	// #####################################################################
	// #################### GostKlausurvorgabe ################################
	// #####################################################################

	private void update_vorgabemenge() {
		_vorgabenmenge.clear();
		_vorgabenmenge.addAll(_vorgabe_by_id.values());
		_vorgabenmenge.sort(_compVorgabe);
	}

	/**
	 * Fügt ein {@link GostKlausurvorgabe}-Objekt hinzu.
	 *
	 * @param vorgabe Das {@link GostKlausurvorgabe}-Objekt, welches hinzugefügt
	 *                    werden soll.
	 */
	public void vorgabeAdd(final @NotNull GostKlausurvorgabe vorgabe) {
		vorgabeAddAll(ListUtils.create1(vorgabe));
	}

	private void vorgabeAddAllOhneUpdate(final @NotNull List<@NotNull GostKlausurvorgabe> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostKlausurvorgabe vorgabe : list) {
			vorgabeCheck(vorgabe);
			DeveloperNotificationException.ifTrue("vorgabeAddAllOhneUpdate: ID=" + vorgabe.idVorgabe + " existiert bereits!", _vorgabe_by_id.containsKey(vorgabe.idVorgabe));
			DeveloperNotificationException.ifTrue("vorgabeAddAllOhneUpdate: ID=" + vorgabe.idVorgabe + " doppelt in der Liste!", !setOfIDs.add(vorgabe.idVorgabe));
		}

		// add all
		for (final @NotNull GostKlausurvorgabe vorgabe : list)
			DeveloperNotificationException.ifMapPutOverwrites(_vorgabe_by_id, vorgabe.idVorgabe, vorgabe);	}

	/**
	 * Fügt alle {@link GostKlausurvorgabe}-Objekte hinzu.
	 *
	 * @param listVorgaben Die Menge der {@link GostKlausurvorgabe}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public void vorgabeAddAll(final @NotNull List<@NotNull GostKlausurvorgabe> listVorgaben) {
		vorgabeAddAllOhneUpdate(listVorgaben);
		update_all();
	}

	private static void vorgabeCheck(final @NotNull GostKlausurvorgabe vorgabe) {
		DeveloperNotificationException.ifInvalidID("vorgabe.idVorgabe", vorgabe.idVorgabe);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostKlausurvorgabe}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idVorgabe Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKlausurvorgabe}-Objekt.
	 */
	public @NotNull GostKlausurvorgabe vorgabeGetByIdOrException(final long idVorgabe) {
		return DeveloperNotificationException.ifMapGetIsNull(_vorgabe_by_id, idVorgabe);
	}

	/**
	 * Liefert eine Liste aller {@link GostKlausurvorgabe}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKlausurvorgabe}-Objekte.
	 */
	public @NotNull List<@NotNull GostKlausurvorgabe> vorgabeGetMengeAsList() {
		return _vorgabenmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKlausurvorgabe}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param vorgabe Das neue {@link GostKlausurvorgabe}-Objekt.
	 */
	public void vorgabePatchAttributes(final @NotNull GostKlausurvorgabe vorgabe) {
		vorgabeCheck(vorgabe);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_vorgabe_by_id, vorgabe.idVorgabe);
		DeveloperNotificationException.ifMapPutOverwrites(_vorgabe_by_id, vorgabe.idVorgabe, vorgabe);

		update_all();
	}

	private void vorgabeRemoveOhneUpdateById(final long idVorgabe) {
		DeveloperNotificationException.ifMapRemoveFailes(_vorgabe_by_id, idVorgabe);
	}

	/**
	 * Entfernt ein existierendes {@link GostKlausurvorgabe}-Objekt.
	 *
	 * @param idVorgabe Die ID des {@link GostKlausurvorgabe}-Objekts.
	 */
	public void vorgabeRemoveById(final long idVorgabe) {
		vorgabeRemoveOhneUpdateById(idVorgabe);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostKlausurvorgabe}-Objekte.
	 *
	 * @param listVorgaben Die Liste der zu entfernenden
	 *                          {@link GostKlausurvorgabe}-Objekte.
	 */
	public void vorgabeRemoveAll(final @NotNull List<@NotNull GostKlausurvorgabe> listVorgaben) {
		for (final @NotNull GostKlausurvorgabe vorgabe : listVorgaben)
			vorgabeRemoveOhneUpdateById(vorgabe.idVorgabe);

		update_all();
	}


	// ################################################################################


	/**
	 * Liefert eine Liste von GostKlausurvorgabe-Objekten zum übergebenen Gost-Halkbjahr und Quartal
	 *
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal die Nummer des Quartals
	 *
	 * @return die Liste von GostKlausurvorgabe-Objekten
	 */
	public @NotNull List<@NotNull GostKlausurvorgabe> vorgabeGetMengeByHalbjahrAndQuartal(final @NotNull GostHalbjahr halbjahr, final int quartal) {
		if (quartal == 0) {
			final List<@NotNull GostKlausurvorgabe> vorgaben = new ArrayList<>();
			if (_vorgabenmenge_by_halbjahr_and_quartal.containsKey1(halbjahr.id))
				for (final @NotNull List<@NotNull GostKlausurvorgabe> vQuartal : _vorgabenmenge_by_halbjahr_and_quartal.getNonNullValuesOfKey1AsList(halbjahr.id)) {
					vorgaben.addAll(vQuartal);
				}
			return vorgaben;
		}
		final List<@NotNull GostKlausurvorgabe> vorgaben = _vorgabenmenge_by_halbjahr_and_quartal.getOrNull(halbjahr.id, quartal);
		return vorgaben != null ? vorgaben : new ArrayList<>();
	}

	/**
	 * Gibt das GostKlausurvorgabe-Objekt zu den übergebenen Parametern zurück.
	 *
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal     das Quartal
	 * @param kursartAllg die Kursart
	 * @param idFach      die ID des Fachs
	 *
	 * @return das GostKlausurvorgabe-Objekt
	 */
	public GostKlausurvorgabe vorgabeGetByHalbjahrAndQuartalAndKursartallgAndFachid(final @NotNull GostHalbjahr halbjahr, final int quartal, final @NotNull GostKursart kursartAllg, final long idFach) {
		return _vorgabe_by_halbjahr_and_quartal_and_kursartAllg_and_idFach.getOrNull(halbjahr.id, quartal, kursartAllg.kuerzel, idFach);
	}

	/**
	 * Gibt die Liste der GostKlausurvorgabe-Objekte zu den übergebenen Parametern
	 * zurück.
	 *
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal     das Quartal, wenn 0, Vorgaben für alle Quartale
	 * @param kursartAllg die Kursart
	 * @param idFach      die ID des Fachs
	 *
	 * @return die Liste der GostKlausurvorgabe-Objekte
	 */
	public @NotNull List<@NotNull GostKlausurvorgabe> vorgabeGetMengeByHalbjahrAndQuartalAndKursartallgAndFachid(final @NotNull GostHalbjahr halbjahr, final int quartal, final @NotNull GostKursart kursartAllg, final long idFach) {
		if (quartal > 0) {
			final List<@NotNull GostKlausurvorgabe> retList = new ArrayList<>();
			final GostKlausurvorgabe vorgabe = vorgabeGetByHalbjahrAndQuartalAndKursartallgAndFachid(halbjahr, quartal, kursartAllg, idFach);
			if (vorgabe != null)
				retList.add(vorgabe);
			return retList;
		}
		return vorgabeGetMengeByHalbjahrAndKursartallgAndFachid(halbjahr, kursartAllg, idFach);
	}

	/**
	 * Gibt die Liste der GostKlausurvorgabe-Objekte zu den übergebenen Parametern
	 * zurück.
	 *
	 * @param halbjahr das Gost-Halbjahr
	 * @param kursartAllg die Kursart
	 * @param idFach      die ID des Fachs
	 *
	 * @return die Liste der GostKlausurvorgabe-Objekte
	 */
	public @NotNull List<@NotNull GostKlausurvorgabe> vorgabeGetMengeByHalbjahrAndKursartallgAndFachid(final @NotNull GostHalbjahr halbjahr, final @NotNull GostKursart kursartAllg, final long idFach) {
		final List<@NotNull GostKlausurvorgabe> list = _vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach.getOrNull(halbjahr.id, kursartAllg.kuerzel, idFach);
		return list != null ? list : new ArrayList<>();
	}

	/**
	 * Gibt die das Klausurvorgabe-Objekt zum übergebenen Parameter zurück (vorhergehendes Quartal des aktuellen Schuljahres).
	 *
	 * @param vorgabe das Klausurvorgabe-Objekt, dessen Vorgänger gesucht ist.
	 *
	 * @return das GostKlausurvorgabe-Objekt
	 */
	public GostKlausurvorgabe getPrevious(final @NotNull GostKlausurvorgabe vorgabe) {
		final List<@NotNull GostKlausurvorgabe> vorgabenSchuljahr = _vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach.getNonNullOrException(vorgabe.halbjahr, vorgabe.kursart, vorgabe.idFach);
		if (vorgabe.halbjahr % 2 == 1) {
			final List<@NotNull GostKlausurvorgabe> vorgabenVorhalbjahr = _vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach.getOrNull(vorgabe.halbjahr - 1, vorgabe.kursart, vorgabe.idFach);
			if (vorgabenVorhalbjahr != null)
				vorgabenSchuljahr.addAll(vorgabenVorhalbjahr);
		}
		vorgabenSchuljahr.sort(_compVorgabe);
		int listIndex = vorgabenSchuljahr.indexOf(vorgabe);
		if (listIndex == 0)
			return null;
		return vorgabenSchuljahr.get(listIndex - 1);
	}

}
