package de.svws_nrw.core.utils.stundenplanblockung;

import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;

import de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungLerngruppe;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Benjamin A. Bartsch
 */
public class StundenplanblockungManagerLerngruppeMenge {

	/** Alle Lerngruppen. */
	private final @NotNull ArrayList<@NotNull StundenplanblockungManagerLerngruppe> _menge;
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanblockungManagerLerngruppe> _map;

	/**
	 * Erzeugt eine neue Menge an Lerngruppen.
	 */
	public StundenplanblockungManagerLerngruppeMenge() {
		_menge = new ArrayList<>();
		_map = new HashMap<>();
	}

	/**
	 * Liefert die zuvor erzeugte Lerngruppe. <br>
	 * Wirft eine NullPointerException, falls die Lerngruppe-ID bereits existiert.
	 *
	 * @param pLerngruppeID          Die Datenbank-ID der Lerngruppe.
	 * @throws NullPointerException  Falls die Lerngruppe-ID bereits existiert.
	 * @return Die zuvor erzeugte Lerngruppe.
	 */
	public @NotNull StundenplanblockungManagerLerngruppe createOrException(final long pLerngruppeID) throws NullPointerException {
		if (_map.containsKey(pLerngruppeID))
			throw new NullPointerException("Die Lerngruppe-ID " + pLerngruppeID + " existiert bereits!");
		final StundenplanblockungManagerLerngruppe gr = new StundenplanblockungManagerLerngruppe(pLerngruppeID);
		_map.put(pLerngruppeID, gr);
		_menge.add(gr);
		return gr;
	}

	/**
	 * Liefert das {@link StundenplanblockungLerngruppe}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Lerngruppe-ID unbekannt ist.
	 *
	 * @param pLerngruppeID          Die Datenbank-ID der Lerngruppe.
	 * @throws NullPointerException  Falls die Lerngruppe-ID unbekannt ist.
	 * @return Das {@link StundenplanblockungLerngruppe}-Objekt zur übergebenen ID.
	 */
	public @NotNull StundenplanblockungManagerLerngruppe getOrException(final long pLerngruppeID) throws NullPointerException {
		final StundenplanblockungManagerLerngruppe gr = _map.get(pLerngruppeID);
		if (gr == null)
			throw new NullPointerException("Lerngruppe-ID " + pLerngruppeID + " unbekannt!");
		return gr;
	}

	/**
	 * Liefert eine zufällige Lerngruppe. <br>
	 * Liefert eine Exception, falls die Menge der Lerngruppen leer ist.
	 *
	 * @param pRandom  Das Random-Objekt zum Erzeugen von Zufallszahlen.
	 * @return         Liefert eine zufällige Lerngruppe.
	 */
	public @NotNull StundenplanblockungManagerLerngruppe getRandomOrException(final @NotNull Random pRandom) {
		final int size = _menge.size();
		if (size <= 0)
			throw new NullPointerException("Es gibt keine Lerngruppen!");
		return _menge.get(pRandom.nextInt(size));
	}

	/**
	 * Löscht die übergebene Lerngruppe. <br>
	 * Wirft eine NullPointerException, falls die Lerngruppe-ID unbekannt ist.
	 *
	 * @param pLerngruppeID          Die Datenbank-ID der Lerngruppe.
	 * @throws NullPointerException  Falls die Lerngruppe-ID unbekannt ist.
	 */
	public void removeOrException(final long pLerngruppeID) throws NullPointerException {
		final @NotNull StundenplanblockungManagerLerngruppe gr = getOrException(pLerngruppeID);
		_map.remove(pLerngruppeID);
		_menge.remove(gr);
	}

	/**
	 * Liefert die Menge aller Lerngruppen.
	 *
	 * @return Die Menge aller Lerngruppen.
	 */
	public ArrayList<StundenplanblockungManagerLerngruppe> getMenge() {
		return _menge;
	}

	/**
	 * Liefert TRUE, falls die Lerngruppe-ID existiert.
	 *
	 * @param pRaumID Die Datenbank-ID des Raumes.
	 * @return TRUE, falls die Lerngruppe-ID existiert.
	 */
	public boolean exists(final long pRaumID) {
		return _map.containsKey(pRaumID);
	}

	/**
	 * Liefert die Anzahl an Lerngruppen.
	 *
	 * @return Die Anzahl an Lerngruppen.
	 */
	public int size() {
		return _menge.size();
	}

}
