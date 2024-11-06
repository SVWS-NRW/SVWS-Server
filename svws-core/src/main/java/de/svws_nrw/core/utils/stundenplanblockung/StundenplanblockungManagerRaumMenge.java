package de.svws_nrw.core.utils.stundenplanblockung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jakarta.validation.constraints.NotNull;

/**
 * Ein Objekt zur Speicherung mehrerer Räume während der Stundenplanberechnung.
 *
 * @author Benjamin A. Bartsch
 */
public class StundenplanblockungManagerRaumMenge {

	private final @NotNull List<StundenplanblockungManagerRaum> _menge = new ArrayList<>();
	private final @NotNull Map<Long, StundenplanblockungManagerRaum> _map = new HashMap<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public StundenplanblockungManagerRaumMenge() {
		// leer
	}

	/**
	 * Fügt den Raum hinzu. <br>
	 * Wirft eine NullPointerException, falls die Raum-ID bereits existiert.
	 *
	 * @param pRaumID                Die Datenbank-ID des Raumes.
	 * @param pKuerzel               Das Kürzel des Raumes.
	 *
	 * @throws NullPointerException  Falls die Raum-ID bereits existiert.
	 */
	public void addOrException(final long pRaumID, final @NotNull String pKuerzel) throws NullPointerException {
		if (_map.containsKey(pRaumID))
			throw new NullPointerException("Die Raum-ID " + pRaumID + " existiert bereits!");
		final @NotNull StundenplanblockungManagerRaum ra = new StundenplanblockungManagerRaum(pRaumID, pKuerzel);
		_map.put(pRaumID, ra);
		_menge.add(ra);
	}

	/**
	 * Liefert das {@link StundenplanblockungManagerRaum}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Raum-ID unbekannt ist.
	 *
	 * @param pRaumID                Die Datenbank-ID des Raumes.
	 * @throws NullPointerException  Falls die Raum-ID unbekannt ist.
	 * @return Das {@link StundenplanblockungManagerRaum}-Objekt zur übergebenen ID.
	 */
	public @NotNull StundenplanblockungManagerRaum getOrException(final long pRaumID) throws NullPointerException {
		final StundenplanblockungManagerRaum ra = _map.get(pRaumID);
		if (ra == null)
			throw new NullPointerException("Raum-ID " + pRaumID + " unbekannt!");
		return ra;
	}

	/**
	 * Liefert einen zufälligen Raum. <br>
	 * Liefert eine Exception, falls die Menge der Räume leer ist.
	 *
	 * @param pRandom  Das Random-Objekt zum Erzeugen von Zufallszahlen.
	 *
	 * @return         Liefert einen zufälligen Raum.
	 */
	public @NotNull StundenplanblockungManagerRaum getRandomOrException(final @NotNull Random pRandom) {
		final int size = _menge.size();
		if (size <= 0)
			throw new NullPointerException("Es gibt keine Räume!");
		return _menge.get(pRandom.nextInt(size));
	}

	/**
	 * Löscht den übergebenen Raum. <br>
	 * Wirft eine NullPointerException, falls die Raum-ID unbekannt ist.
	 *
	 * @param pRaumID                Die Datenbank-ID des Raumes.
	 *
	 * @throws NullPointerException  Falls die Raum-ID unbekannt ist.
	 */
	public void removeOrException(final long pRaumID) throws NullPointerException {
		final @NotNull StundenplanblockungManagerRaum ra = getOrException(pRaumID);
		_map.remove(pRaumID);
		_menge.remove(ra);
	}

	/**
	 * Liefert die Menge aller Räume.
	 *
	 * @return Die Menge aller Räume.
	 */
	public List<StundenplanblockungManagerRaum> getMenge() {
		return _menge;
	}

	/**
	 * Liefert TRUE, falls die Raum-ID existiert.
	 *
	 * @param pRaumID Die Datenbank-ID des Raumes.
	 * @return TRUE, falls die Raum-ID existiert.
	 */
	public boolean exists(final long pRaumID) {
		return _map.containsKey(pRaumID);
	}

	/**
	 * Liefert die Anzahl an Räume.
	 *
	 * @return Die Anzahl an Räume.
	 */
	public int size() {
		return _menge.size();
	}

}
