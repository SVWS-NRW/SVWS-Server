package de.svws_nrw.core.utils.stundenplanblockung;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;

/**
 * Ein Objekt zur Speicherung eines Raumes während der Stundenplanberechnung.
 *
 * @author Benjamin A. Bartsch
 */
public class StundenplanblockungManagerRaum {

	/** Die Datenbank-ID des Raumes. */
	private final long _id;

	/** Das Kürzel des Raumes. Beispielsweise 'E01'. */
	private @NotNull String _kuerzel = "";

	/** Alle Lerngruppen der Räume. */
	private @NotNull List<StundenplanblockungManagerLerngruppe> _lerngruppen = new ArrayList<>();

	/**
	 * Konstruktor zur Erstellung eines {@link StundenplanblockungManagerRaum}-Objektes.
	 *
	 * @param pRaumID   Die Datenbank-ID des Raumes.
	 * @param pKuerzel  Das Kürzel des Raumes.
	 */
	public StundenplanblockungManagerRaum(final long pRaumID, final @NotNull String pKuerzel) {
		_id = pRaumID;
		_kuerzel = pKuerzel;
		_lerngruppen = new ArrayList<>();
	}

	/**
	 * Liefert die Datenbank-ID des Raumes.
	 *
	 * @return Die Datenbank-ID des Raumes.
	 */
	public long getID() {
		return _id;
	}

	/**
	 * Setzt das Kürzel des Raumes.
	 *
	 * @param pKuerzel  Das neue Kürzel des Raumes.
	 */
	public void setKuerzel(final @NotNull String pKuerzel) {
		_kuerzel = pKuerzel;
	}
}
