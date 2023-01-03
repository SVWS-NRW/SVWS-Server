package de.nrw.schule.svws.core.utils.stundenplanblockung;

import java.util.Vector;
import jakarta.validation.constraints.NotNull;

/**
 * 
 * @author Benjamin A. Bartsch
 */
public class StundenplanblockungManagerLerngruppe {
	
	/** Die Datenbank-ID der Lerngruppe. */
	long _id;
	
	/** Die Anzahl an Stunden im Stundenplan. */
	int stunden = 0;
	
	/** Die Lehrkräfte der Lerngruppe. */
	@NotNull Vector<StundenplanblockungManagerLehrkraft> _menge_le = new Vector<>();

	/** Die Klassen der Lerngruppe. */
	@NotNull Vector<StundenplanblockungManagerKlasse> _menge_kl = new Vector<>();

	/** Die Fächer Lerngruppe. */
	@NotNull Vector<StundenplanblockungManagerFach> _menge_fa = new Vector<>();

	/** Die Räume Lerngruppe. */
	@NotNull Vector<StundenplanblockungManagerRaum> _menge_ra = new Vector<>();

	/** Die Kopplungen Lerngruppe. */
	@NotNull Vector<StundenplanblockungManagerKopplung> _menge_ko = new Vector<>();
	
	/**
	 * Erzeugt eine neue Lerngruppe mit der übergebenen ID.
	 * 
	 * @param pID  Die Datenbank-ID der Lerngruppe.
	 */
	public StundenplanblockungManagerLerngruppe(long pID) {
		_id = pID;
	}
	
	/**
	 * Liefert die Datenbank-ID der Lerngruppe.
	 * 
	 * @return Die Datenbank-ID der Lerngruppe.
	 */
	public long getID() {
		return _id;
	}
	
	/**
	 * Fügt der Lerngruppe die Lehrkraft hinzu (und umgekehrt).
	 * 
	 * @param pLe Das Objekt, welches hinzugefügt werden soll.
	 */
	public void addLehrkraftOrException(@NotNull StundenplanblockungManagerLehrkraft pLe) {
		if (_menge_le.contains(pLe) == true)
			throw new NullPointerException("Lerngruppe " + _id + " hat bereits Lehrkraft " + pLe._kuerzel + " (" + pLe._id + ")!");
		_menge_le.add(pLe);
		pLe._menge_gr.add(this);
	}

	/**
	 * Fügt der Lerngruppe die Klasse hinzu (und umgekehrt).
	 * 
	 * @param pKl Das Objekt, welches hinzugefügt werden soll.
	 */
	public void addKlasseOrException(@NotNull StundenplanblockungManagerKlasse pKl) {
		if (_menge_kl.contains(pKl) == true)
			throw new NullPointerException("Lerngruppe " + _id + " hat bereits Klasse " + pKl._kuerzel + " (" + pKl._id + ")!");
		_menge_kl.add(pKl);
		pKl._menge_gr.add(this);
	}
	
	/**
	 * Fügt der Lerngruppe das Fach hinzu (und umgekehrt).
	 * 
	 * @param pFa Das Objekt, welches hinzugefügt werden soll.
	 */
	public void addFachOrException(@NotNull StundenplanblockungManagerFach pFa) {
		if (_menge_fa.contains(pFa) == true)
			throw new NullPointerException("Lerngruppe " + _id + " hat bereits Fach " + pFa._kuerzel + " (" + pFa._id + ")!");
		_menge_fa.add(pFa);
		pFa._menge_gr.add(this);
	}
	
	/**
	 * Liefert TRUE, falls der Lerngruppe die Lehrkraft zugeordnet ist.
	 * 
	 * @param pLe  Das Objekt, nach dem gesucht wird.
	 * @return TRUE, falls der Lerngruppe die Lehrkraft zugeordnet ist.
	 */
	public boolean hasLehrkraft(@NotNull StundenplanblockungManagerLehrkraft pLe) {
		return _menge_le.contains(pLe);
	}

	/**
	 * Liefert TRUE, falls der Lerngruppe der Klasse zugeordnet ist.
	 * 
	 * @param pKl  Das Objekt, nach dem gesucht wird.
	 * @return TRUE, falls der Lerngruppe der Klasse zugeordnet ist.
	 */
	public boolean hasKlasse(@NotNull StundenplanblockungManagerKlasse pKl) {
		return _menge_kl.contains(pKl);
	}

	/**
	 * Liefert TRUE, falls der Lerngruppe das Fach zugeordnet ist.
	 * 
	 * @param pFa  Das Objekt, nach dem gesucht wird..
	 * @return TRUE, falls der Lerngruppe das Fach zugeordnet ist.
	 */
	public boolean hasFach(@NotNull StundenplanblockungManagerFach pFa) {
		return _menge_fa.contains(pFa);
	}
	/**
	 * Entfernt aus der Lerngruppe die Lehrkraft (und umgekehrt).
	 * 
	 * @param pLe  Das Objekt, welches entfernt werden soll.
	 */
	public void removeLehrkraftOrException(@NotNull StundenplanblockungManagerLehrkraft pLe) {
		if (_menge_le.contains(pLe) == false)
			throw new NullPointerException("Lerngruppe " + _id + " hat nicht Lehrkraft " + pLe._kuerzel + " (" + pLe._id + ")!");
		_menge_le.remove(pLe);
		pLe._menge_gr.remove(this);
	}

	/**
	 * Entfernt aus der Lerngruppe die Lehrkraft (und umgekehrt).
	 * 
	 * @param pKl  Das Objekt, welches entfernt werden soll.
	 */
	public void removeKlasseOrException(@NotNull StundenplanblockungManagerKlasse pKl) {
		if (_menge_kl.contains(pKl) == false)
			throw new NullPointerException("Lerngruppe " + _id + " hat nicht Klasse " + pKl._kuerzel + " (" + pKl._id + ")!");
		_menge_kl.remove(pKl);
		pKl._menge_gr.remove(this);
	}

	/**
	 * Entfernt aus der Lerngruppe das Fach (und umgekehrt).
	 * 
	 * @param pFa  Das Objekt, welches entfernt werden soll.
	 */
	public void removeFachOrException(@NotNull StundenplanblockungManagerFach pFa) {
		if (_menge_fa.contains(pFa) == false)
			throw new NullPointerException("Lerngruppe " + _id + " hat nicht Fach " + pFa._kuerzel + " (" + pFa._id + ")!");
		_menge_fa.remove(pFa);
		pFa._menge_gr.remove(this);
	}	
	
}
