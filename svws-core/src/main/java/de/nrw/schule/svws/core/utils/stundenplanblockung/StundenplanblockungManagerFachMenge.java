package de.nrw.schule.svws.core.utils.stundenplanblockung;

import java.util.HashMap;
import java.util.Vector;

import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungFach;
import jakarta.validation.constraints.NotNull;

/**
 * 
 * @author Benjamin A. Bartsch
 */
public class StundenplanblockungManagerFachMenge {

	private final @NotNull Vector<@NotNull StundenplanblockungManagerFach> _menge;
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanblockungManagerFach> _map;

	/**
	 * Erzeugt eine neues Objekt zur Verwaltung der Menge aller Fächer.
	 */
	public StundenplanblockungManagerFachMenge() {
		_menge = new Vector<>();
		_map = new HashMap<>();
	}
	
	
	/**
	 * Fügt das Fach hinzu. <br>
	 * Wirft eine NullPointerException, falls die Fach-ID bereits existiert.
	 * 
	 * @param pFachID                Die Datenbank-ID des Fach. 
	 * @param pKuerzel               Das Kürzel des Faches.
	 * @throws NullPointerException  Falls die Fach-ID bereits existiert.
	 */
	public void addOrException(long pFachID, @NotNull String pKuerzel) throws NullPointerException {
		if (_map.containsKey(pFachID) == true)
			throw new NullPointerException("Die Fach-ID " + pFachID + " existiert bereits!");
		@NotNull StundenplanblockungManagerFach fa = new StundenplanblockungManagerFach(pFachID, pKuerzel);
		_map.put(pFachID, fa);
		_menge.add(fa);
	}
	
	/**
	 * Liefert das {@link StundenplanblockungFach}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Fach-ID unbekannt ist.
	 * 
	 * @param pFachID                Die Datenbank-ID des Faches.
	 * @return                       Das {@link StundenplanblockungFach}-Objekt zur übergebenen ID.
	 * @throws NullPointerException  Falls die Fach-ID unbekannt ist.
	 */
	public @NotNull StundenplanblockungManagerFach getOrException(long pFachID) throws NullPointerException {
		StundenplanblockungManagerFach fa = _map.get(pFachID);
		if (fa == null)
			throw new NullPointerException("Fach-ID " + pFachID + " unbekannt!");
		return fa;
	}
	
	/**
	 * Löscht das übergebene Fach. <br>
	 * Wirft eine NullPointerException, falls die Fach-ID unbekannt ist.
	 * 
	 * @param pFachID                Die Datenbank-ID des Faches.
	 * @throws NullPointerException  Falls die Fach-ID unbekannt ist.
	 */
	public void removeOrException(long pFachID) throws NullPointerException {
		@NotNull StundenplanblockungManagerFach fa = getOrException(pFachID);
		_map.remove(pFachID);
		_menge.remove(fa);
	}
	
	/**
	 * Liefert TRUE, falls die Fach-ID existiert. 
	 * 
	 * @param pFachID Die Datenbank-ID des Faches.
	 * @return TRUE, falls die Fach-ID existiert.
	 */
	public boolean exists(long pFachID) {
		return _map.containsKey(pFachID);
	}


	/**
	 * Liefert die Anzahl an Lehrkräften.
	 * 
	 * @return Die Anzahl an Lehrkräften.
	 */
	public int size() {
		return _menge.size();
	}
	
}
