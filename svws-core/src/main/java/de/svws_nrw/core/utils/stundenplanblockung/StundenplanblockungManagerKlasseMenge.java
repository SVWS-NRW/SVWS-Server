package de.svws_nrw.core.utils.stundenplanblockung;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;

import de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungKlasse;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Benjamin A. Bartsch
 */
public class StundenplanblockungManagerKlasseMenge {

	private final @NotNull List<StundenplanblockungManagerKlasse> _menge = new ArrayList<>();
	private final @NotNull Map<Long, StundenplanblockungManagerKlasse> _map = new HashMap<>();

	/**
	 * Fügt die Klasse hinzu. <br>
	 * Wirft eine NullPointerException, falls die Klasse-ID bereits existiert.
	 *
	 * @param pKlasseID              Die Datenbank-ID der Klasse.
	 * @param pKuerzel               Das Kürzel der Klasse.
	 * @throws NullPointerException  Falls die Klasse-ID bereits existiert.
	 */
	public void addOrException(final long pKlasseID, final @NotNull String pKuerzel) throws NullPointerException {
		if (_map.containsKey(pKlasseID))
			throw new NullPointerException("Die Klasse-ID " + pKlasseID + " existiert bereits!");
		final StundenplanblockungManagerKlasse kl = new StundenplanblockungManagerKlasse(pKlasseID, pKuerzel);
		_map.put(pKlasseID, kl);
		_menge.add(kl);
	}

	/**
	 * Liefert das {@link StundenplanblockungKlasse}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Klasse-ID unbekannt ist.
	 *
	 * @param pKlasseID              Die Datenbank-ID der Klasse.
	 * @return                       Das {@link StundenplanblockungKlasse}-Objekt zur übergebenen ID.
	 * @throws NullPointerException  Falls die Klasse-ID unbekannt ist.
	 */
	public @NotNull StundenplanblockungManagerKlasse getOrException(final long pKlasseID) throws NullPointerException {
		final StundenplanblockungManagerKlasse klasse = _map.get(pKlasseID);
		if (klasse == null)
			throw new NullPointerException("Klasse-ID " + pKlasseID + " unbekannt!");
		return klasse;
	}

	/**
	 * Liefert eine zufällige Klasse oder null, falls es gar keine Lehrkräfte gibt.
	 *
	 * @param pRandom  Das Random-Objekt zum Erzeugen von Zufallszahlen.
	 * @return         Eine zufällige Lehrkraft oder null, falls es gar keine Lehrkräfte gibt.
	 */
	public @NotNull StundenplanblockungManagerKlasse getRandomOrException(final @NotNull Random pRandom) {
		final int size = _menge.size();
		if (size <= 0)
			throw new NullPointerException("Es gibt keine Klassen!");
		return _menge.get(pRandom.nextInt(size));
	}

	/**
	 * Löscht die übergebene Klasse. <br>
	 * Wirft eine NullPointerException, falls die Klasse-ID unbekannt ist.
	 *
	 * @param pKlasseID              Die Datenbank-ID der Klasse.
	 * @throws NullPointerException  Falls die Klasse-ID unbekannt ist.
	 */
	public void removeOrException(final long pKlasseID) throws NullPointerException {
		final @NotNull StundenplanblockungManagerKlasse klasse = getOrException(pKlasseID);
		_map.remove(pKlasseID);
		_menge.remove(klasse);
	}

	/**
	 * Liefert TRUE, falls die Klasse-ID existiert.
	 *
	 * @param pKlasseID  Die Datenbank-ID der Klasse.
	 * @return           TRUE, falls die Klasse-ID existiert.
	 */
	public boolean exists(final long pKlasseID) {
		return _map.containsKey(pKlasseID);
	}

	/**
	 * Liefert die Menge aller Klassen.
	 *
	 * @return Die Menge aller Klassen.
	 */
	public List<StundenplanblockungManagerKlasse> getMenge() {
		return _menge;
	}

	/**
	 * Liefert die Anzahl an Klassen.
	 *
	 * @return Die Anzahl an Klassen.
	 */
	public int size() {
		return _menge.size();
	}

}
