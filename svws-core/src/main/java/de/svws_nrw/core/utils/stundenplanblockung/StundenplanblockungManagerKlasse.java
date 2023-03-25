package de.svws_nrw.core.utils.stundenplanblockung;

import java.util.Vector;
import jakarta.validation.constraints.NotNull;

/**
 * 
 * @author Benjamin A. Bartsch
 */
public class StundenplanblockungManagerKlasse {

	/** Die Datenbank-ID der Klasse. */
	long _id;

	/** Das Kürzel der Klasse. Beispielsweise '05a' oder 'Q1'. */
	@NotNull String _kuerzel = "";

	/** Alle Lerngruppen der Klasse. */
	@NotNull Vector<StundenplanblockungManagerLerngruppe> _menge_gr;

	/**
	 * Erzeugt eine neue Klasse.
	 * 
	 * @param pKlasseID  Die Datenbank-ID der Klasse.
	 * @param pKuerzel   Das Kürzel der Klasse.
	 */
	public StundenplanblockungManagerKlasse(final long pKlasseID, final @NotNull String pKuerzel) {
		_id = pKlasseID;
		_kuerzel = pKuerzel;
		_menge_gr = new Vector<>();
	}

	/**
	 * Liefert die Datenbank-ID der Klasse.
	 * 
	 * @return Die Datenbank-ID der Klasse.
	 */
	public long getID() {
		return _id;
	}

	/**
	 * Setzt das Kürzel der Klasse.
	 * 
	 * @param pKuerzel  Das neue Kürzel der Klasse.
	 */
	public void setKuerzel(final @NotNull String pKuerzel) {
		_kuerzel = pKuerzel;
	}

}
