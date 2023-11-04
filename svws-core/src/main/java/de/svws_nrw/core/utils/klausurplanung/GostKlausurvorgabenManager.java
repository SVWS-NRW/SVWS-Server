package de.svws_nrw.core.utils.klausurplanung;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.adt.map.HashMap3D;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.Map2DUtils;
import de.svws_nrw.core.utils.MapUtils;
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
		if (a.kursart.compareTo(b.kursart) < 0)
			return +1;
		if (a.kursart.compareTo(b.kursart) > 0)
			return -1;
		return Integer.compare(a.quartal, b.quartal);
	};

	// GostKlausurvorgabe
	private final @NotNull Map<@NotNull Long, @NotNull GostKlausurvorgabe> _vorgabe_by_id = new HashMap<>();
	private final @NotNull List<@NotNull GostKlausurvorgabe> _vorgabenmenge = new ArrayList<>();
	private final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull GostKlausurvorgabe>> _vorgabenmenge_by_quartal = new HashMap<>();
	private final @NotNull HashMap3D<@NotNull Integer, @NotNull String, @NotNull Long, @NotNull GostKlausurvorgabe> _vorgabe_by_quartal_and_kursartAllg_and_idFach = new HashMap3D<>();
	private final @NotNull HashMap2D<@NotNull String, @NotNull Long, @NotNull List<@NotNull GostKlausurvorgabe>> _vorgabenmenge_by_kursartAllg_and_idFach = new HashMap2D<>();


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

		update_vorgabenmenge_by_quartal();
		update_vorgabe_by_quartal_and_kursartAllg_and_idFach();
		update_vorgabenmenge_by_kursartAllg_and_idFach();

	}


	private void update_vorgabenmenge_by_quartal() {
		_vorgabenmenge_by_quartal.clear();
		for (final @NotNull GostKlausurvorgabe v : _vorgabenmenge)
			MapUtils.getOrCreateArrayList(_vorgabenmenge_by_quartal, v.quartal).add(v);
	}

	private void update_vorgabe_by_quartal_and_kursartAllg_and_idFach() {
		_vorgabe_by_quartal_and_kursartAllg_and_idFach.clear();
		for (final @NotNull GostKlausurvorgabe v : _vorgabenmenge)
			_vorgabe_by_quartal_and_kursartAllg_and_idFach.put(v.quartal, v.kursart, v.idFach, v);
	}

	private void update_vorgabenmenge_by_kursartAllg_and_idFach() {
		_vorgabenmenge_by_kursartAllg_and_idFach.clear();
		for (final @NotNull GostKlausurvorgabe v : _vorgabenmenge)
			Map2DUtils.getOrCreateArrayList(_vorgabenmenge_by_kursartAllg_and_idFach, v.kursart, v.idFach).add(v);
	}


	// #####################################################################
	// #################### GostKlausurvorgabe ################################
	// #####################################################################

	private void update_vorgabemenge() {
		_vorgabenmenge.clear();
		_vorgabenmenge.addAll(_vorgabe_by_id.values());
		_vorgabenmenge.sort(_compVorgabe);
	}

	private void vorgabeAddOhneUpdate(final @NotNull GostKlausurvorgabe vorgabe) {
		vorgabeCheck(vorgabe);
		DeveloperNotificationException.ifMapPutOverwrites(_vorgabe_by_id, vorgabe.idVorgabe, vorgabe);
	}

	/**
	 * Fügt ein {@link GostKlausurvorgabe}-Objekt hinzu.
	 *
	 * @param vorgabe Das {@link GostKlausurvorgabe}-Objekt, welches hinzugefügt
	 *                    werden soll.
	 */
	public void vorgabeAdd(final @NotNull GostKlausurvorgabe vorgabe) {
		vorgabeAddOhneUpdate(vorgabe);

		update_all();
	}

	private void vorgabeAddAllOhneUpdate(final @NotNull List<@NotNull GostKlausurvorgabe> listVorgaben) {
		for (final @NotNull GostKlausurvorgabe vorgabe : listVorgaben)
			vorgabeAddOhneUpdate(vorgabe);
	}

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
		DeveloperNotificationException.ifInvalidID("kursklausur.id", vorgabe.idVorgabe);
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
	 * Liefert eine Liste von GostKlausurvorgabe-Objekten zum übergebenen Quartal
	 *
	 * @param quartal die Nummer des Quartals
	 *
	 * @return die Liste von GostKlausurvorgabe-Objekten
	 */
	public @NotNull List<@NotNull GostKlausurvorgabe> vorgabeGetMengeByQuartal(final int quartal) {
		if (quartal == 0)
			return new ArrayList<>(vorgabeGetMengeAsList()); // new ArrayList als Workaround für DataTable, der sonst nicht aktualisiert
		List<@NotNull GostKlausurvorgabe> vorgaben = _vorgabenmenge_by_quartal.get(quartal);
		return vorgaben != null ? vorgaben : new ArrayList<>();
	}

	/**
	 * Gibt das GostKlausurvorgabe-Objekt zu den übergebenen Parametern zurück.
	 *
	 * @param quartal     das Quartal
	 * @param kursartAllg die Kursart
	 * @param idFach      die ID des Fachs
	 *
	 * @return das GostKlausurvorgabe-Objekt
	 */
	public GostKlausurvorgabe vorgabeGetByQuartalAndKursartallgAndFachid(final int quartal, final @NotNull GostKursart kursartAllg, final long idFach) {
		return _vorgabe_by_quartal_and_kursartAllg_and_idFach.getOrNull(quartal, kursartAllg.kuerzel, idFach);
	}

	/**
	 * Gibt die Liste der GostKlausurvorgabe-Objekte zu den übergebenen Parametern
	 * zurück.
	 *
	 * @param quartal     das Quartal, wenn 0, Vorgaben für alle Quartale
	 * @param kursartAllg die Kursart
	 * @param idFach      die ID des Fachs
	 *
	 * @return die Liste der GostKlausurvorgabe-Objekte
	 */
	public @NotNull List<@NotNull GostKlausurvorgabe> vorgabeGetMengeByQuartalAndKursartallgAndFachid(final int quartal, final @NotNull GostKursart kursartAllg, final long idFach) {
		if (quartal > 0) {
			final List<@NotNull GostKlausurvorgabe> retList = new ArrayList<>();
			final GostKlausurvorgabe vorgabe = vorgabeGetByQuartalAndKursartallgAndFachid(quartal, kursartAllg, idFach);
			if (vorgabe != null)
				retList.add(vorgabe);
			return retList;
		}
		return vorgabeGetMengeByKursartallgAndFachid(kursartAllg, idFach);
	}

	/**
	 * Gibt die Liste der GostKlausurvorgabe-Objekte zu den übergebenen Parametern
	 * zurück.
	 *
	 * @param kursartAllg die Kursart
	 * @param idFach      die ID des Fachs
	 *
	 * @return die Liste der GostKlausurvorgabe-Objekte
	 */
	public @NotNull List<@NotNull GostKlausurvorgabe> vorgabeGetMengeByKursartallgAndFachid(final @NotNull GostKursart kursartAllg, final long idFach) {
		final List<@NotNull GostKlausurvorgabe> list = _vorgabenmenge_by_kursartAllg_and_idFach.getOrNull(kursartAllg.kuerzel, idFach);
		return list != null ? list : new ArrayList<>();
	}

}
