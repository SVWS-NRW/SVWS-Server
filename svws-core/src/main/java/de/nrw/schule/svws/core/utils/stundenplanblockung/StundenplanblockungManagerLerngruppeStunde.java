package de.nrw.schule.svws.core.utils.stundenplanblockung;

import jakarta.validation.constraints.NotNull;

/**
 * 
 * @author Benjamin A. Bartsch
 */
public class StundenplanblockungManagerLerngruppeStunde {

	/** Die Datenbank-ID des Stundenelementes. */
	long _id;

	/** Die Anzahl an Stunden in der Lerngruppe. */
	int wochenstunden = 1; // DEFAULT

	/** Die Anzahl an Stunden im Stundenplan. */
	int typ = 1; // 1 = jede Woche, 2 = alle zwei Wochen doppeln.

	/** Das Eltern-Objekt. */
	final @NotNull StundenplanblockungManagerLerngruppe _gr;

	/**
	 * Erzeugt ein neues Stundenelement.
	 * 
	 * @param pID     Die Datenbank-ID der Lerngruppe.
	 * @param pParent Das Eltern-Objekt.
	 */
	public StundenplanblockungManagerLerngruppeStunde(final long pID, final @NotNull StundenplanblockungManagerLerngruppe pParent) {
		_id = pID;
		_gr = pParent;
	}

	/**
	 * Liefert die Datenbank-ID des Objektes.
	 * 
	 * @return Die Datenbank-ID des Objektes.
	 */
	public long getID() {
		return _id;
	}

}
