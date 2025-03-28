package de.svws_nrw.core.utils.stundenplanblockung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungLehrkraft;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Objekt zur Speicherung mehrerer Lehrkraefte während der Stundenplanberechnung.
 *
 * @author Benjamin A. Bartsch
 */
public class StundenplanblockungManagerLehrkraftMenge {

	private final @NotNull List<StundenplanblockungManagerLehrkraft> _menge = new ArrayList<>();
	private final @NotNull Map<Long, StundenplanblockungManagerLehrkraft> _map = new HashMap<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public StundenplanblockungManagerLehrkraftMenge() {
		// leer
	}

	/**
	 * Fügt die Lehrkraft hinzu. <br>
	 * Wirft eine NullPointerException, falls die Lehrkraft-ID bereits existiert.
	 *
	 * @param pLehrkraftID           Die Datenbank-ID der Lehrkraft.
	 * @param pKuerzel               Das Kürzel der Lehrkraft.
	 * @throws NullPointerException  Falls die Lehrkraft-ID bereits existiert.
	 */
	public void addOrException(final long pLehrkraftID, final @NotNull String pKuerzel) throws NullPointerException {
		if (_map.containsKey(pLehrkraftID))
			throw new NullPointerException("Die Lehrkraft-ID " + pLehrkraftID + " existiert bereits!");
		final StundenplanblockungManagerLehrkraft le = new StundenplanblockungManagerLehrkraft(pLehrkraftID, pKuerzel);
		_map.put(pLehrkraftID, le);
		_menge.add(le);
	}

	/**
	 * Liefert das {@link StundenplanblockungLehrkraft}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Lehrkraft-ID unbekannt ist.
	 *
	 * @param pLehrkraftID           Die Datenbank-ID der Lehrkraft.
	 * @return                       Das {@link StundenplanblockungLehrkraft}-Objekt zur übergebenen ID.
	 * @throws NullPointerException  Falls die Lehrkraft-ID unbekannt ist.
	 */
	public @NotNull StundenplanblockungManagerLehrkraft getOrException(final long pLehrkraftID) throws NullPointerException {
		final StundenplanblockungManagerLehrkraft lehrkraft = _map.get(pLehrkraftID);
		if (lehrkraft == null)
			throw new NullPointerException("Lehrkraft-ID " + pLehrkraftID + " unbekannt!");
		return lehrkraft;
	}

	/**
	 * Liefert eine zufällige Lehrkraft. <br>
	 * Liefert eine Exception, falls die Menge der Lehrkräfte leer ist.
	 *
	 * @param pRandom  Das Random-Objekt zum Erzeugen von Zufallszahlen.
	 * @return         Liefert eine zufällige Lehrkraft.
	 */
	public @NotNull StundenplanblockungManagerLehrkraft getRandomOrException(final @NotNull Random pRandom) {
		final int size = _menge.size();
		if (size <= 0)
			throw new NullPointerException("Es gibt keine Lehrkräfte!");
		return _menge.get(pRandom.nextInt(size));
	}

	/**
	 * Löscht die übergebene Lehrkraft. <br>
	 * Wirft eine NullPointerException, falls die Lehrkraft-ID unbekannt ist.
	 *
	 * @param pLehrkraftID           Die Datenbank-ID der Lehrkraft.
	 * @throws NullPointerException  Falls die Lehrkraft-ID unbekannt ist.
	 */
	public void removeOrException(final long pLehrkraftID) throws NullPointerException {
		final @NotNull StundenplanblockungManagerLehrkraft lehrkraft = getOrException(pLehrkraftID);
		_map.remove(pLehrkraftID);
		_menge.remove(lehrkraft);
	}

	/**
	 * Liefert TRUE, falls die Lehrkraft-ID existiert.
	 *
	 * @param pLehrkraftID  Die Datenbank-ID der Lehrkraft.
	 * @return              TRUE, falls die Lehrkraft-ID existiert.
	 */
	public boolean exists(final long pLehrkraftID) {
		return _map.containsKey(pLehrkraftID);
	}

	/**
	 * Liefert die Menge aller Lehrkräfte.
	 *
	 * @return Die Menge aller Lehrkräfte.
	 */
	public List<StundenplanblockungManagerLehrkraft> getMenge() {
		return _menge;
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
