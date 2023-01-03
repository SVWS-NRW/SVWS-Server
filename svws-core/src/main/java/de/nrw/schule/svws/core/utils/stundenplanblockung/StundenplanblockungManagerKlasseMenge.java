package de.nrw.schule.svws.core.utils.stundenplanblockung;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungKlasse;
import jakarta.validation.constraints.NotNull;

/**
 * 
 * @author Benjamin A. Bartsch
 */
public class StundenplanblockungManagerKlasseMenge {

	private final @NotNull Vector<@NotNull StundenplanblockungManagerKlasse> _menge;
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanblockungManagerKlasse> _map;

	/**
	 * Erzeugt eine neues Objekt zur Verwaltung der Menge aller Klassen.
	 */
	public StundenplanblockungManagerKlasseMenge() {
		_menge = new Vector<>();
		_map = new HashMap<>();
	}

	/**
	 * Fügt die Klasse hinzu. <br>
	 * Wirft eine NullPointerException, falls die Klasse-ID bereits existiert.
	 * 
	 * @param pKlasseID              Die Datenbank-ID der Klasse. 
	 * @param pKuerzel               Das Kürzel der Klasse.
	 * @throws NullPointerException  Falls die Klasse-ID bereits existiert.
	 */
	public void addOrException(long pKlasseID, @NotNull String pKuerzel) throws NullPointerException {
		if (_map.containsKey(pKlasseID) == true)
			throw new NullPointerException("Die Klasse-ID " + pKlasseID + " existiert bereits!");
		StundenplanblockungManagerKlasse kl = new StundenplanblockungManagerKlasse(pKlasseID, pKuerzel);
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
	public @NotNull StundenplanblockungManagerKlasse getOrException(long pKlasseID) throws NullPointerException {
		StundenplanblockungManagerKlasse klasse = _map.get(pKlasseID);
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
	public @NotNull StundenplanblockungManagerKlasse getRandomOrException(@NotNull Random pRandom) {
		int size = _menge.size();
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
	public void removeOrException(long pKlasseID) throws NullPointerException {
		@NotNull StundenplanblockungManagerKlasse klasse = getOrException(pKlasseID);
		_map.remove(pKlasseID);
		_menge.remove(klasse);
	}

	/**
	 * Liefert TRUE, falls die Klasse-ID existiert. 
	 * 
	 * @param pKlasseID  Die Datenbank-ID der Klasse.
	 * @return           TRUE, falls die Klasse-ID existiert.
	 */
	public boolean exists(long pKlasseID) {
		return _map.containsKey(pKlasseID);
	}

	/**
	 * Liefert die Menge aller Klassen.
	 * 
	 * @return Die Menge aller Klassen.
	 */
	public Vector<StundenplanblockungManagerKlasse> getMenge() {
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
