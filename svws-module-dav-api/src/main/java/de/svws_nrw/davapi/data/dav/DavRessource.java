package de.svws_nrw.davapi.data.dav;

import de.svws_nrw.db.dto.current.svws.dav.DTODavRessourceCollection;

/**
 * Diese Klasse ist ein DTO für eine DAV-Ressourcen.
 */
public class DavRessource {

	/** Die ID der zugehörigen ID der {@link DTODavRessourceCollection} */
	public Long idCollection;

	/** Die ID der Ressource */
	public Long id;

	/** Der Timestamp-String für das Ende eines Kalendereintrags */
	public String kalenderEnde;

	/** Der Timestamp-String für den Start eines Kalendereintrags */
	public String kalenderStart;

	/** Der Typ eines Kalendereintrags */
	public String kalenderTyp;

	/** Das Synctoken der Ressource */
	public long syncToken;

	/** Die serialisierten Daten dieser Ressource */
	public String data;

	/** Die UID dieser Ressource */
	public String uid;

	/** Die Berechtigungen des angemeldeten Nutzers auf dieser Ressource */
	public DavPermissions permissions;



	/**
	 * Leerer Default-Konstruktor
	 */
	public DavRessource() {
		// empty default constructor
	}

}
