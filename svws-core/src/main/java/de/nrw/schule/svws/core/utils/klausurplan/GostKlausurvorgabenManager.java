package de.nrw.schule.svws.core.utils.klausurplan;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.data.gost.klausuren.GostKlausurtermin;
import de.nrw.schule.svws.core.data.gost.klausuren.GostKlausurvorgabe;
import de.nrw.schule.svws.core.data.gost.klausuren.GostKursklausur;
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

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen
	 * GostKlausurvorgaben und erzeugt die privaten Attribute.
	 * 
	 * @param vorgaben die Liste der GostKlausurvorgaben eines Abiturjahrgangs und
	 *                 Gost-Halbjahres
	 */
	public GostKlausurvorgabenManager(@NotNull List<@NotNull GostKlausurvorgabe> vorgaben) {
		_vorgaben = vorgaben;

		for (@NotNull
		GostKlausurvorgabe v : _vorgaben) {
			_mapIdKlausurvorgabe.put(v.idVorgabe, v);
			addVorgabeToInternalMaps(v);
		}

	}

	private void addVorgabeToInternalMaps(@NotNull GostKlausurvorgabe v) {
		// Füllen von _mapQuartalKlausurvorgaben
		Vector<@NotNull GostKlausurvorgabe> listKlausurvorgabenMapQuartalKlausurvorgaben = _mapQuartalKlausurvorgaben.get(v.quartal);
		if (listKlausurvorgabenMapQuartalKlausurvorgaben == null) {
			listKlausurvorgabenMapQuartalKlausurvorgaben = new Vector<>();
			_mapQuartalKlausurvorgaben.put(v.quartal, listKlausurvorgabenMapQuartalKlausurvorgaben);
		}
		listKlausurvorgabenMapQuartalKlausurvorgaben.add(v);
	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich Informationen einer
	 * Klausurvorgabe geändert hat.
	 * 
	 * @param vorgabe das GostKlausurvorgabe-Objekt
	 */
	public void updateKlausurvorgabe(@NotNull GostKlausurvorgabe vorgabe) {
		if (!_vorgaben.contains(vorgabe)) {
			_vorgaben.add(vorgabe);
		}
		
		_mapIdKlausurvorgabe.put(vorgabe.idVorgabe, vorgabe);

		// aus _mapQuartalKlausurvorgaben löschen
		Vector<@NotNull GostKlausurvorgabe> quartalList = _mapQuartalKlausurvorgaben.get(vorgabe.quartal);
		if (quartalList != null) {
			quartalList.remove(vorgabe);
		} else {
			// TODO Fehler, denn kann eigentlich nicht sein.
		}

		addVorgabeToInternalMaps(vorgabe);

	}
	
	/**
	 * Löscht eine Klausurvorgabe aus den internen Strukturen
	 * 
	 * @param vorgabe das GostKlausurvorgabe-Objekt
	 */
	public void removeVorgabe(@NotNull GostKlausurvorgabe vorgabe) {
		Vector<@NotNull GostKlausurvorgabe> listKlausurvorgabenMapQuartalKlausurvorgaben = _mapQuartalKlausurvorgaben.get(vorgabe.quartal);
		if (listKlausurvorgabenMapQuartalKlausurvorgaben == null) {
			// TODO Fehlerbehandlung
			return;			
		}
		listKlausurvorgabenMapQuartalKlausurvorgaben.remove(vorgabe);

		_vorgaben.remove(vorgabe);
		_mapIdKlausurvorgabe.remove(vorgabe.idVorgabe);

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
	public List<@NotNull GostKlausurvorgabe> getKlausurvorgaben(int quartal) {
		return _mapQuartalKlausurvorgaben.get(quartal);
	}

	/**
	 * Gibt das GostKlausurvorgabe-Objekt zur übergebenen id zurück.
	 * 
	 * @param idVorgabe die ID der Klausurvorgabe
	 * 
	 * @return das GostKlausurvorgabe-Objekt
	 */
	public GostKlausurvorgabe gibGostKlausurvorgabe(long idVorgabe) {
		return _mapIdKlausurvorgabe.get(idVorgabe);
	}

}
