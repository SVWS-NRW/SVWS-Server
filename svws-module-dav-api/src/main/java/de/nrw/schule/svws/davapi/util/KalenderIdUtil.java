package de.nrw.schule.svws.davapi.util;

import de.nrw.schule.svws.db.dto.current.svws.dav.DTODavRessourceCollection;

/**
 * Utility-Klasse für Verwalten und Unterscheiden von Kalender-IDs.
 *
 */
public class KalenderIdUtil {

	/**
	 * private default constructor for utility clas
	 */
	private KalenderIdUtil() {
		// private default constructor for utiltiy
	}

	/**
	 * Gibt wieder, ob eine gegebene KalenderID einen generierten kalender
	 * repräsentiert oder eine {@link DTODavRessourceCollection}
	 * 
	 * @param kalenderId die KalenderID
	 * @return ob der Kalender mit dieser ID generiert wurde
	 */
	public static boolean isGenerated(String kalenderId) {
		// für generierte Kalender hier die ID Strings parsen
		try {
			Long.parseLong(kalenderId);
			return false;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	/**
	 * Versucht eine Kalender-ID zu parsen. Für generierte Kalender muss hier noch
	 * weiter implementiert werden.
	 * 
	 * @param kalenderId die gegebene Kalenderid als String
	 * @return die geparste ID des Kalenders
	 */
	public static Long parseId(String kalenderId) {
		try {
			return Long.parseLong(kalenderId);
		} catch (NumberFormatException nfe) {
			// für generierte Kalender hier parsen
		}
		throw new IllegalArgumentException("Kalender Id <" + kalenderId + "> konnte nicht geparst werden.");
	}

}
