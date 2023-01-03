package de.nrw.schule.svws.core.utils.stundenplanblockung;

/**
 * Dieses Objekt speichert die globale Konfiguration der Schule.
 * 
 * @author Benjamin A. Bartsch
 */
public class StundenplanblockungManagerSchule {

	private int _tage_pro_woche = 5; // DEFAULT
	private int _stunden_pro_tag = 9; // DEFAULT

	/**
	 * Erzeugt ein neues Objekt.
	 */
	public StundenplanblockungManagerSchule() {
	}

	/**
	 * Liefert die Anzahl an Unterrichtstagen in der Woche.
	 * 
	 * @return Die Anzahl an Unterrichtstagen in der Woche.
	 */
	public int get_tage_pro_woche() {
		return _tage_pro_woche;
	}

	/**
	 * Liefert die maximale Anzahl an Stunden pro Tag.
	 * @return Die maximale Anzahl an Stunden pro Tag.
	 */
	public int get_stunden_pro_tag() {
		return _stunden_pro_tag;
	}

}
