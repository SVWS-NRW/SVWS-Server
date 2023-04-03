package de.svws_nrw.core.utils.stundenplanblockung;

import java.util.Vector;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Benjamin A. Bartsch
 */
public class StundenplanblockungManagerLehrkraft {

	/** Die Datenbank-ID der Lehrkraft. */
	final long _id;

	/** Das Kürzel der Lehrkraft. Beispielsweise 'BAR'. */
	@NotNull String _kuerzel = "";

	/** TRUE, falls die Lehrkraft zur Vertretung in Springstunden herangezogen werden kann. */
	boolean _darf_vertreten = false;

	/** Alle Lerngruppen der Lehrkraft. */
	final @NotNull Vector<StundenplanblockungManagerLerngruppe> _menge_gr;

	/**
	 * @param pLehrkraftID  Die Datenbank-ID der Lehrkraft.
	 * @param pKuerzel      Das Kürzel der Lehrkraft.
	 */
	public StundenplanblockungManagerLehrkraft(final long pLehrkraftID, final @NotNull String pKuerzel) {
		_id = pLehrkraftID;
		_kuerzel = pKuerzel;
		_menge_gr = new Vector<>();
	}

	/**
	 * Liefert die Datenbank-ID der Lehrkraft.
	 *
	 * @return Die Datenbank-ID der Lehrkraft.
	 */
	public long getID() {
		return _id;
	}

	/**
	 * Liefert TRUE, falls die Lehrkraft zur Vertretung in Springstunden herangezogen werden kann.
	 *
	 * @return TRUE, falls die Lehrkraft zur Vertretung in Springstunden herangezogen werden kann.
	 */
	public boolean getDarfVertreten() {
		return _darf_vertreten;
	}

	/**
	 * Setzt das Kürzel der Lehrkraft.
	 *
	 * @param pKuerzel  Das neue Kürzel der Lehrkraft.
	 */
	public void setKuerzel(final @NotNull String pKuerzel) {
		_kuerzel = pKuerzel;
	}

}
