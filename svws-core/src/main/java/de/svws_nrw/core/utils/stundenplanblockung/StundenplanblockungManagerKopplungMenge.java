package de.svws_nrw.core.utils.stundenplanblockung;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Benjamin A. Bartsch
 */
public class StundenplanblockungManagerKopplungMenge {

	private final @NotNull Vector<@NotNull StundenplanblockungManagerKopplung> _menge;
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanblockungManagerKopplung> _map;

	/**
	 * Erzeugt eine neues Objekt zur Verwaltung der Menge aller Kopplungen.
	 */
	public StundenplanblockungManagerKopplungMenge() {
		_menge = new Vector<>();
		_map = new HashMap<>();
	}

	/**
	 * Fügt die Kopplung hinzu. <br>
	 * Wirft eine NullPointerException, falls die Kopplung-ID bereits existiert.
	 *
	 * @param pKopplungID            Die Datenbank-ID der Kopplung.
	 * @param pKuerzel               Das Kürzel der Kopplung.
	 * @throws NullPointerException  Falls die Kopplung-ID bereits existiert.
	 */
	public void addOrException(final long pKopplungID, final @NotNull String pKuerzel) throws NullPointerException {
		if (_map.containsKey(pKopplungID))
			throw new NullPointerException("Die Kopplung-ID " + pKopplungID + " existiert bereits!");
		final StundenplanblockungManagerKopplung ko = new StundenplanblockungManagerKopplung(pKopplungID, pKuerzel);
		_map.put(pKopplungID, ko);
		_menge.add(ko);
	}

	/**
	 * Liefert das {@link StundenplanblockungManagerKopplung}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Kopplung-ID unbekannt ist.
	 *
	 * @param pKopplungID            Die Datenbank-ID der Kopplung.
	 * @throws NullPointerException  Falls die Kopplung-ID unbekannt ist.
	 * @return Das {@link StundenplanblockungManagerKopplung}-Objekt zur übergebenen ID.
	 */
	public @NotNull StundenplanblockungManagerKopplung getOrException(final long pKopplungID) throws NullPointerException {
		final StundenplanblockungManagerKopplung ko = _map.get(pKopplungID);
		if (ko == null)
			throw new NullPointerException("Kopplung-ID " + pKopplungID + " unbekannt!");
		return ko;
	}

	/**
	 * Liefert eine zufällige Kopplung. <br>
	 * Liefert eine Exception, falls die Menge der Kopplungen leer ist.
	 *
	 * @param pRandom  Das Random-Objekt zum Erzeugen von Zufallszahlen.
	 * @return         Liefert eine zufällige Kopplung.
	 */
	public @NotNull StundenplanblockungManagerKopplung getRandomOrException(final @NotNull Random pRandom) {
		final int size = _menge.size();
		if (size <= 0)
			throw new NullPointerException("Es gibt keine Kopplungen!");
		return _menge.get(pRandom.nextInt(size));
	}

	/**
	 * Löscht die übergebene Kopplung. <br>
	 * Wirft eine NullPointerException, falls die Kopplung-ID unbekannt ist.
	 *
	 * @param pKopplungID            Die Datenbank-ID der Kopplung.
	 * @throws NullPointerException  Falls die Kopplung-ID unbekannt ist.
	 */
	public void removeOrException(final long pKopplungID) throws NullPointerException {
		final @NotNull StundenplanblockungManagerKopplung ko = getOrException(pKopplungID);
		_map.remove(pKopplungID);
		_menge.remove(ko);
	}

	/**
	 * Liefert TRUE, falls die Kopplung-ID existiert.
	 *
	 * @param pKopplungID  Die Datenbank-ID der Kopplung.
	 * @return TRUE, falls die Kopplung-ID existiert.
	 */
	public boolean exists(final long pKopplungID) {
		return _map.containsKey(pKopplungID);
	}

	/**
	 * Liefert die Menge aller Kopplungen.
	 *
	 * @return Die Menge aller Kopplungen.
	 */
	public Vector<StundenplanblockungManagerKopplung> getMenge() {
		return _menge;
	}

	/**
	 * Liefert die Anzahl an Kopplungen.
	 *
	 * @return Die Anzahl an Kopplungen.
	 */
	public int size() {
		return _menge.size();
	}

}
