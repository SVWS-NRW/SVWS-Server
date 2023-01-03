package de.nrw.schule.svws.core.utils.stundenplanblockung;

import java.util.Vector;
import jakarta.validation.constraints.NotNull;

/**
 * 
 * @author Benjamin A. Bartsch
 */
public class StundenplanblockungManagerKopplung {

	/** Die Datenbank-ID der Kopplung. */
	private long _id;

	/** Das Kürzel der Kopplung. Beispielsweise '7RE'. */
	private @NotNull String _kuerzel = "";

	/** Alle Lerngruppen der Kopplungen. */
	private @NotNull Vector<StundenplanblockungManagerLerngruppe> _lerngruppen = new Vector<>();

	/**
	 * Erzeugt eine neue Kopplung.
	 * 
	 * @param pKopplungID  Die Datenbank-ID der Kopplung.
	 * @param pKuerzel     Das Kürzel der Kopplung.
	 */
	public StundenplanblockungManagerKopplung(long pKopplungID, @NotNull String pKuerzel) {
		_id = pKopplungID;
		_kuerzel = pKuerzel;
	}
	
	/**
	 * Liefert die Datenbank-ID der Kopplung.
	 * 
	 * @return Die Datenbank-ID der Kopplung.
	 */
	public long getID() {
		return _id;
	}
	
	/**
	 * Setzt das Kürzel der Kopplung.
	 * 
	 * @param pKuerzel  Das neue Kürzel der Kopplung.
	 */
	public void setKuerzel(@NotNull String pKuerzel) {
		_kuerzel = pKuerzel;
	}
}
