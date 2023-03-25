package de.svws_nrw.davapi.data.repos.dav;

/**
 * Diese Klasse repräsentiert eine WebDav-Ressource
 *
 */
public class DavRessource {

	/** die ID der Ressourcensammlung */
	public Long ressourceCollectionId;
	/** die ID der Ressource */
	public Long id;
	/** der Timestamp-String für das Ende eines Kalendereintrags */
	public String kalenderEnde;
	/** der Timestamp-String für den Start eines Kalendereintrags */
	public String kalenderStart;
	/** der Typ eines Kalendereintrags */
	public String kalenderTyp;
	/** das Synctoken der Ressource */
	public long syncToken;
	/** die serialisierten Daten dieser Ressource */
	public String data;
	/** die UID dieser Ressource */
	public String uid;
	/** die Berechtigungen des angemeldeten Nutzers auf dieser Ressource */
	public DavRessourceCollectionACLPermissions permissions;

	/**
	 * leerer Default-Konstruktor
	 */
	public DavRessource() {
		//empty default constructor
	}

}
