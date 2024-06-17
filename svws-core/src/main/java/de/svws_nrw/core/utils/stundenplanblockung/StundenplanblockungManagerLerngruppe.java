package de.svws_nrw.core.utils.stundenplanblockung;

import java.util.ArrayList;
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
	@NotNull
	ArrayList<StundenplanblockungManagerLehrkraft> _menge_le = new ArrayList<>();

	/** Die Klassen der Lerngruppe. */
	@NotNull
	ArrayList<StundenplanblockungManagerKlasse> _menge_kl = new ArrayList<>();

	/** Die Fächer Lerngruppe. */
	@NotNull
	ArrayList<StundenplanblockungManagerFach> _menge_fa = new ArrayList<>();

	/** Die Räume Lerngruppe. */
	@NotNull
	ArrayList<StundenplanblockungManagerRaum> _menge_ra = new ArrayList<>();

	/** Die Kopplungen Lerngruppe. */
	@NotNull
	ArrayList<StundenplanblockungManagerKopplung> _menge_ko = new ArrayList<>();

	/**
	 * Erzeugt eine neue Lerngruppe mit der übergebenen ID.
	 *
	 * @param pID  Die Datenbank-ID der Lerngruppe.
	 */
	public StundenplanblockungManagerLerngruppe(final long pID) {
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
	public void addLehrkraftOrException(final @NotNull StundenplanblockungManagerLehrkraft pLe) {
		if (_menge_le.contains(pLe))
			throw new NullPointerException("Lerngruppe " + _id + " hat bereits Lehrkraft " + pLe._kuerzel + " (" + pLe._id + ")!");
		_menge_le.add(pLe);
		pLe._menge_gr.add(this);
	}

	/**
	 * Fügt der Lerngruppe die Klasse hinzu (und umgekehrt).
	 *
	 * @param pKl Das Objekt, welches hinzugefügt werden soll.
	 */
	public void addKlasseOrException(final @NotNull StundenplanblockungManagerKlasse pKl) {
		if (_menge_kl.contains(pKl))
			throw new NullPointerException("Lerngruppe " + _id + " hat bereits Klasse " + pKl._kuerzel + " (" + pKl._id + ")!");
		_menge_kl.add(pKl);
		pKl._menge_gr.add(this);
	}

	/**
	 * Fügt der Lerngruppe das Fach hinzu (und umgekehrt).
	 *
	 * @param pFa Das Objekt, welches hinzugefügt werden soll.
	 */
	public void addFachOrException(final @NotNull StundenplanblockungManagerFach pFa) {
		if (_menge_fa.contains(pFa))
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
	public boolean hasLehrkraft(final @NotNull StundenplanblockungManagerLehrkraft pLe) {
		return _menge_le.contains(pLe);
	}

	/**
	 * Liefert TRUE, falls der Lerngruppe der Klasse zugeordnet ist.
	 *
	 * @param pKl  Das Objekt, nach dem gesucht wird.
	 * @return TRUE, falls der Lerngruppe der Klasse zugeordnet ist.
	 */
	public boolean hasKlasse(final @NotNull StundenplanblockungManagerKlasse pKl) {
		return _menge_kl.contains(pKl);
	}

	/**
	 * Liefert TRUE, falls der Lerngruppe das Fach zugeordnet ist.
	 *
	 * @param pFa  Das Objekt, nach dem gesucht wird..
	 * @return TRUE, falls der Lerngruppe das Fach zugeordnet ist.
	 */
	public boolean hasFach(final @NotNull StundenplanblockungManagerFach pFa) {
		return _menge_fa.contains(pFa);
	}

	/**
	 * Entfernt aus der Lerngruppe die Lehrkraft (und umgekehrt).
	 *
	 * @param pLe  Das Objekt, welches entfernt werden soll.
	 */
	public void removeLehrkraftOrException(final @NotNull StundenplanblockungManagerLehrkraft pLe) {
		if (!_menge_le.contains(pLe))
			throw new NullPointerException("Lerngruppe " + _id + " hat nicht Lehrkraft " + pLe._kuerzel + " (" + pLe._id + ")!");
		_menge_le.remove(pLe);
		pLe._menge_gr.remove(this);
	}

	/**
	 * Entfernt aus der Lerngruppe die Lehrkraft (und umgekehrt).
	 *
	 * @param pKl  Das Objekt, welches entfernt werden soll.
	 */
	public void removeKlasseOrException(final @NotNull StundenplanblockungManagerKlasse pKl) {
		if (!_menge_kl.contains(pKl))
			throw new NullPointerException("Lerngruppe " + _id + " hat nicht Klasse " + pKl._kuerzel + " (" + pKl._id + ")!");
		_menge_kl.remove(pKl);
		pKl._menge_gr.remove(this);
	}

	/**
	 * Entfernt aus der Lerngruppe das Fach (und umgekehrt).
	 *
	 * @param pFa  Das Objekt, welches entfernt werden soll.
	 */
	public void removeFachOrException(final @NotNull StundenplanblockungManagerFach pFa) {
		if (!_menge_fa.contains(pFa))
			throw new NullPointerException("Lerngruppe " + _id + " hat nicht Fach " + pFa._kuerzel + " (" + pFa._id + ")!");
		_menge_fa.remove(pFa);
		pFa._menge_gr.remove(this);
	}

}
