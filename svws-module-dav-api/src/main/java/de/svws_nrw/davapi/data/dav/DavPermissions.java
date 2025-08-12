package de.svws_nrw.davapi.data.dav;

import de.svws_nrw.db.dto.current.svws.dav.DTODavRessourceCollection;

/**
 * Diese Klasse beinhaltet die Informationen zu den Lese- und Schreibberechtigungen eines Nutzers zu einer DAV-Ressource-Collection.
 */
public class DavPermissions {

	/** Die ID der {@link DTODavRessourceCollection} */
	private final Long idCollection;

	/** Die ID des Benutzers */
	private final Long idUser;

	/** Gibt an, ob eine Berechtigung zum Lesen vorliegt oder nicht */
	private boolean allowRead = false;

	/** Gibt an, ob eine Berechtigung zum Schreiben vorliegt oder nicht */
	private boolean allowWrite = false;


	/**
	 * Erzeugt eine neue Berechtigung für eine DAV-Ressource-Collection.
	 *
	 * @param allowRead      gibt an, ob eine Berechtigung zum Lesen vorliegt oder nicht
	 * @param allowWrite     gibt an, ob eine Berechtigung zum Schreiben vorliegt oder nicht
	 * @param idCollection   die ID der {@link DTODavRessourceCollection}
	 * @param idUser         die ID des Benutzers
	 */
	public DavPermissions(final boolean allowRead, final boolean allowWrite, final long idCollection, final long idUser) {
		this.allowRead = allowRead;
		this.allowWrite = allowWrite;
		this.idCollection = idCollection;
		this.idUser = idUser;
	}


	/**
	 * Erzeugt eine neue Berechtigung für eine DAV-Ressource-Collection.
	 *
	 * @param permissions    die Berechtigungen als String
	 * @param idCollection   die ID der {@link DTODavRessourceCollection}
	 * @param idUser         die ID des Benutzers
	 */
	public DavPermissions(final String permissions, final long idCollection, final long idUser) {
		final char[] charArray = permissions.toCharArray();
		this.allowRead = (charArray[0] == 'r');
		this.allowWrite = (charArray[1] == 'w');
		this.idCollection = idCollection;
		this.idUser = idUser;
	}


	/**
	 * Gibt zurück, ob eine Berechtigung zum Lesen vorliegt oder nicht.
	 *
	 * @return true, wenn eine Berechtigung vorliegt, und ansonsten false
	 */
	public boolean readable() {
		return allowRead;
	}


	/**
	 * Gibt zurück, ob eine Berechtigung zum Schreiben vorliegt oder nicht.
	 *
	 * @return true, wenn eine Berechtigung vorliegt, und ansonsten false
	 */
	public boolean writable() {
		return allowWrite;
	}


	/**
	 * Gibt die ID für die DAV-Ressource-Collection zurück.
	 *
	 * @return die ID der {@link DTODavRessourceCollection}
	 */
	public Long getCollectionID() {
		return idCollection;
	}


	/**
	 * Gibt die ID des Benutzes zurück.
	 *
	 * @return die ID des Benutzers
	 */
	public Long getUserID() {
		return idUser;
	}


	/**
	 * Gibt die Lese- und Schreibberechtigungen für das Speichern in der Datenbank als String zurück.
	 *
	 * @return eine Zeichenkette mit der Information, ob eine Lese- bzw. Schreibberechtigung vorliegt oder nicht.
	 */
	public String toPermissionString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(allowRead ? 'r' : '-');
		sb.append(allowWrite ? 'w' : '-');
		return sb.toString();
	}

}
