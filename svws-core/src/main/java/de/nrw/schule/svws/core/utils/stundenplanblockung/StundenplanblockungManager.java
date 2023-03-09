package de.nrw.schule.svws.core.utils.stundenplanblockung;

import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Manipulation der Stundenplandaten. Der Manager dient: <br>
 *     (1) Zur Berechnung eines Stundenplanes. <br>
 *     (2) Zur Manipulation von der GUI. <br>
 * 
 * @author Benjamin A. Bartsch 
 */
public class StundenplanblockungManager {

	// Sub-Manager
	
	private final @NotNull StundenplanblockungManagerSchule _manager_sc;
	private final @NotNull StundenplanblockungManagerFachMenge _manager_fa;
	private final @NotNull StundenplanblockungManagerKlasseMenge _manager_kl;
	private final @NotNull StundenplanblockungManagerKopplungMenge _manager_ko;
	private final @NotNull StundenplanblockungManagerLehrkraftMenge _manager_le;
	private final @NotNull StundenplanblockungManagerLerngruppeMenge _manager_gr;
	private final @NotNull StundenplanblockungManagerRaumMenge _manager_ra;
	private final @NotNull StundenplanblockungManagerStatistik _manager_st;

	/**
	 * Erzeugt einen neuen, leeren Manager.
	 */
	public StundenplanblockungManager() {
		_manager_sc = new StundenplanblockungManagerSchule();
		_manager_fa = new StundenplanblockungManagerFachMenge();
		_manager_kl = new StundenplanblockungManagerKlasseMenge();
		_manager_ko = new StundenplanblockungManagerKopplungMenge();
		_manager_le = new StundenplanblockungManagerLehrkraftMenge();
		_manager_gr = new StundenplanblockungManagerLerngruppeMenge();
		_manager_ra = new StundenplanblockungManagerRaumMenge();
		_manager_st = new StundenplanblockungManagerStatistik();
	}

	/**
	 * Liefert den Manager zur Verwaltung der globalen Schulkonfiguration.
	 * 
	 * @return Den Manager zur Verwaltung der globalen Schulkonfiguration.
	 */
	public @NotNull StundenplanblockungManagerSchule getSchule() {
		return _manager_sc;
	}

	/**
	 * Liefert den Manager zur Verwaltung der Lehrkräfte.
	 * 
	 * @return Den Manager zur Verwaltung der Lehrkräfte.
	 */
	public @NotNull StundenplanblockungManagerLehrkraftMenge getLehrkraefte() {
		return _manager_le;
	}

	/**
	 * Liefert den Manager zur Verwaltung der Lerngruppen.
	 * 
	 * @return Den Manager zur Verwaltung der Lerngruppen.
	 */
	public @NotNull StundenplanblockungManagerLerngruppeMenge getLerngruppen() {
		return _manager_gr;
	}

	/**
	 * Liefert den Manager zur Verwaltung der Klassen.
	 * 
	 * @return Den Manager zur Verwaltung der Klassen.
	 */
	public @NotNull StundenplanblockungManagerKlasseMenge getKlassen() {
		return _manager_kl;
	}

	/**
	 * Liefert den Manager zur Verwaltung der Fächer.
	 * 
	 * @return Den Manager zur Verwaltung der Fächer.
	 */
	public @NotNull StundenplanblockungManagerFachMenge getFaecher() {
		return _manager_fa;
	}

	/**
	 * Liefert den Manager zur Verwaltung der Räume.
	 * 
	 * @return Den Manager zur Verwaltung der Räume.
	 */
	public @NotNull StundenplanblockungManagerRaumMenge getRaeume() {
		return _manager_ra;
	}

	/**
	 * Liefert den Manager zur Verwaltung der Kopplungen.
	 * 
	 * @return Den Manager zur Verwaltung der Kopplungen.
	 */
	public @NotNull StundenplanblockungManagerKopplungMenge getKopplungen() {
		return _manager_ko;
	}

	/**
	 * Überprüft alle Daten auf ihre Konsistenz. <br>
	 * Wirft eine Exception, falls die Daten nicht konsistent sind.  
	 */
	public void miscCheckConsistencyOrException() {
		// TODO BAR miscCheckConsistencyOrException
	}

}
