package de.nrw.schule.svws.core.utils.klausurplan;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.data.gost.klausuren.GostKlausurvorgabe;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link GostKlausurvorgabe}.
 * Hierbei werden auch Hilfsmethoden zur Interpretation der Daten erzeugt. Es
 * werden Klausurvorgaben eines Gost-Halbjahres eines Abiturjahrgangs verwaltet.
 */
public class GostKlausurvorgabenManager {

	/** Die GostKlausurvorgaben, die im Manager vorhanden sind */
	private final @NotNull List<@NotNull GostKlausurvorgabe> _vorgaben;

	/** Eine Map quartal -> Liste von GostKlausurvorgaben */
	private final @NotNull HashMap<@NotNull Integer, @NotNull Vector<@NotNull GostKlausurvorgabe>> _mapQuartalKlausurvorgaben = new HashMap<>();

	/** Eine Map id -> GostKlausurvorgabe */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostKlausurvorgabe> _mapIdKlausurvorgabe = new HashMap<>();

	/** Eine Map quartal -> kursartAllg -> fachId -> GostKlausurvorgabe */
	private final @NotNull HashMap<@NotNull Integer, @NotNull HashMap<@NotNull String, @NotNull HashMap<@NotNull Long, @NotNull GostKlausurvorgabe>>> _mapQuartalKursartFachKlausurvorgabe = new HashMap<>();

	/** Eine Map kursartAllg -> fachId -> Liste von GostKlausurvorgabe */
	private final @NotNull HashMap<@NotNull String, @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull GostKlausurvorgabe>>> _mapKursartFachKlausurvorgaben = new HashMap<>();

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen
	 * GostKlausurvorgaben und erzeugt die privaten Attribute.
	 * 
	 * @param vorgaben die Liste der GostKlausurvorgaben eines Abiturjahrgangs und
	 *                 Gost-Halbjahres
	 */
	public GostKlausurvorgabenManager(final @NotNull List<@NotNull GostKlausurvorgabe> vorgaben) {
		_vorgaben = vorgaben;

		for (final @NotNull GostKlausurvorgabe v : _vorgaben) {
			_mapIdKlausurvorgabe.put(v.idVorgabe, v);
			addVorgabeToInternalMaps(v);
		}

	}

	private void addVorgabeToInternalMaps(@NotNull GostKlausurvorgabe v) {
		// Füllen von _mapQuartalKlausurvorgaben
		Vector<@NotNull GostKlausurvorgabe> listKlausurvorgabenMapQuartalKlausurvorgaben = _mapQuartalKlausurvorgaben
				.get(v.quartal);
		if (listKlausurvorgabenMapQuartalKlausurvorgaben == null) {
			listKlausurvorgabenMapQuartalKlausurvorgaben = new Vector<>();
			_mapQuartalKlausurvorgaben.put(v.quartal, listKlausurvorgabenMapQuartalKlausurvorgaben);
		}
		listKlausurvorgabenMapQuartalKlausurvorgaben.add(v);

		// Füllen von _mapQuartalKursartFachKlausurvorgabe
		HashMap<@NotNull String, @NotNull HashMap<@NotNull Long, @NotNull GostKlausurvorgabe>> mapKursartFachKlausurvorgabe = _mapQuartalKursartFachKlausurvorgabe
				.get(v.quartal);
		if (mapKursartFachKlausurvorgabe == null)
			_mapQuartalKursartFachKlausurvorgabe.put(v.quartal, mapKursartFachKlausurvorgabe = new HashMap<>());
		HashMap<@NotNull Long, @NotNull GostKlausurvorgabe> mapFachKlausurvorgabe = mapKursartFachKlausurvorgabe
				.get(v.kursart);
		if (mapFachKlausurvorgabe == null)
			mapKursartFachKlausurvorgabe.put(v.kursart, mapFachKlausurvorgabe = new HashMap<>());
		mapFachKlausurvorgabe.put(v.idFach, v);

		// Füllen von _mapKursartFachKlausurvorgaben
		HashMap<@NotNull Long, @NotNull List<@NotNull GostKlausurvorgabe>> mapFachKlausurvorgaben = _mapKursartFachKlausurvorgaben
				.get(v.kursart);
		if (mapFachKlausurvorgaben == null)
			_mapKursartFachKlausurvorgaben.put(v.kursart, mapFachKlausurvorgaben = new HashMap<>());
		List<@NotNull GostKlausurvorgabe> listKlausurvorgaben = mapFachKlausurvorgaben.get(v.idFach);
		if (listKlausurvorgaben == null)
			mapFachKlausurvorgaben.put(v.idFach, listKlausurvorgaben = new Vector<>());
		listKlausurvorgaben.add(v);
	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich Informationen einer
	 * Klausurvorgabe geändert hat.
	 * 
	 * @param vorgabe das GostKlausurvorgabe-Objekt
	 */
	public void updateKlausurvorgabe(@NotNull GostKlausurvorgabe vorgabe) {
		if (!_vorgaben.contains(vorgabe)) {
			// TODO Error Klausurvorgabe nicht da
		}

		removeUpdateKlausurvorgabeCommons(vorgabe);
		addVorgabeToInternalMaps(vorgabe);

	}
	
	/**
	 * Fügt die Klausurvorgabe den internen Strukturen hinzu.
	 * 
	 * @param vorgabe das GostKlausurvorgabe-Objekt
	 */
	public void addKlausurvorgabe(@NotNull GostKlausurvorgabe vorgabe) {
		_vorgaben.add(vorgabe);
		_mapIdKlausurvorgabe.put(vorgabe.idVorgabe, vorgabe);
		removeUpdateKlausurvorgabeCommons(vorgabe);
		addVorgabeToInternalMaps(vorgabe);
	}

	private void removeUpdateKlausurvorgabeCommons(@NotNull GostKlausurvorgabe vorgabe) {
		// aus _mapQuartalKlausurvorgaben löschen
		final Vector<@NotNull GostKlausurvorgabe> listKlausurvorgabenMapQuartalKlausurvorgaben = _mapQuartalKlausurvorgaben
				.get(vorgabe.quartal);
		if (listKlausurvorgabenMapQuartalKlausurvorgaben != null) {
			listKlausurvorgabenMapQuartalKlausurvorgaben.remove(vorgabe);
		}
		// aus _mapQuartalKursartFachKlausurvorgabe löschen
		final HashMap<@NotNull String, @NotNull HashMap<@NotNull Long, @NotNull GostKlausurvorgabe>> map1 = _mapQuartalKursartFachKlausurvorgabe
				.get(vorgabe.quartal);
		if (map1 != null) {
			final HashMap<@NotNull Long, @NotNull GostKlausurvorgabe> map2 = map1.get(vorgabe.kursart);
			if (map2 != null) {
				final GostKlausurvorgabe kv = map2.get(vorgabe.idFach);
				if (kv == vorgabe)
					map2.remove(vorgabe.idFach);
			}
		}
		// aus _mapKursartFachKlausurvorgaben löschen
		final HashMap<@NotNull Long, @NotNull List<@NotNull GostKlausurvorgabe>> map3 = _mapKursartFachKlausurvorgaben
				.get(vorgabe.kursart);
		if (map3 != null) {
			final List<@NotNull GostKlausurvorgabe> list = map3.get(vorgabe.idFach);
			if (list != null) {
				list.remove(vorgabe);
			}
		}
	}

	/**
	 * Löscht eine Klausurvorgabe aus den internen Strukturen
	 * 
	 * @param vorgabe das GostKlausurvorgabe-Objekt
	 */
	public void removeVorgabe(@NotNull GostKlausurvorgabe vorgabe) {
		_vorgaben.remove(vorgabe);
		_mapIdKlausurvorgabe.remove(vorgabe.idVorgabe);
		removeUpdateKlausurvorgabeCommons(vorgabe);
	}

	/**
	 * Liefert eine Liste von GostKlausurvorgabe-Objekten des Halbjahres
	 * 
	 * @return die Liste von GostKlausurvorgabe-Objekten
	 */
	public @NotNull List<@NotNull GostKlausurvorgabe> getKlausurvorgaben() {
		return _vorgaben;
	}

	/**
	 * Liefert eine Liste von GostKlausurvorgabe-Objekten zum übergebenen Quartal
	 * 
	 * @param quartal die Nummer des Quartals
	 * 
	 * @return die Liste von GostKlausurvorgabe-Objekten
	 */
	public List<@NotNull GostKlausurvorgabe> getKlausurvorgaben(final int quartal) {
		return _mapQuartalKlausurvorgaben.get(quartal);
	}

	/**
	 * Gibt das GostKlausurvorgabe-Objekt zur übergebenen id zurück.
	 * 
	 * @param idVorgabe die ID der Klausurvorgabe
	 * 
	 * @return das GostKlausurvorgabe-Objekt
	 */
	public GostKlausurvorgabe gibGostKlausurvorgabe(final long idVorgabe) {
		return _mapIdKlausurvorgabe.get(idVorgabe);
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
	public GostKlausurvorgabe gibGostKlausurvorgabe(final int quartal, final String kursartAllg, final long idFach) {
		final HashMap<@NotNull String, @NotNull HashMap<@NotNull Long, @NotNull GostKlausurvorgabe>> map1 = _mapQuartalKursartFachKlausurvorgabe
				.get(quartal);
		if (map1 == null)
			return null;
			final HashMap<@NotNull Long, @NotNull GostKlausurvorgabe> map2 = map1.get(kursartAllg);
		if (map2 != null)
			return map2.get(idFach);
		return null;
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
	public List<@NotNull GostKlausurvorgabe> gibGostKlausurvorgaben(final int quartal, final String kursartAllg, final long idFach) {
		if (quartal > 0) {
			final List<@NotNull GostKlausurvorgabe> retList = new Vector<>();
			final GostKlausurvorgabe vorgabe = gibGostKlausurvorgabe(quartal, kursartAllg, idFach);
			if (vorgabe != null)
				retList.add(vorgabe);
			return retList;
		}
		return gibGostKlausurvorgaben(kursartAllg, idFach);
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
	public List<@NotNull GostKlausurvorgabe> gibGostKlausurvorgaben(final String kursartAllg, final long idFach) {
		final HashMap<@NotNull Long, @NotNull List<@NotNull GostKlausurvorgabe>> map1 = _mapKursartFachKlausurvorgaben
			.get(kursartAllg);
		if (map1 == null)
			return new Vector<>();
		final List<@NotNull GostKlausurvorgabe> list = map1.get(idFach);
		if (list == null)
			return new Vector<>();
		return list;
	}

}
