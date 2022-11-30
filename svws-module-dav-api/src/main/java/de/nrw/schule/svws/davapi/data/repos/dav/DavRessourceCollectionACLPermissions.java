package de.nrw.schule.svws.davapi.data.repos.dav;

import de.nrw.schule.svws.db.dto.current.svws.dav.DTODavRessourceCollection;

/**
 * Diese Klasse dient der Repräsentation eines ACL-Eintrags. Ein ACL-Eintrag
 * besteht aus Nutzer, Id der Collection, der Id des Nutzers und den Rechten für
 * Lesen und Schreiben.
 *
 */
public class DavRessourceCollectionACLPermissions {
	/** permission-character für 'Keine Berechtigung' */
	private static final char NO_PERMISSION = '-';
	/** permission-character für 'Leseberechtigung' */
	private static final char READ_PERMISSION = 'r';
	/** permission-character für 'Schreibberechtigung' */
	private static final char WRITE_PERMISSION = 'w';
	/** ob dieser eintrag das Leserecht enthält */
	private boolean darfLesen = false;
	/** ob dieser Eintrag das Schreibrecht enthält */
	private boolean darfSchreiben = false;
	/** die ID der {@link DTODavRessourceCollection} */
	private Long ressourceCollectionId;
	/** die Id des Benutzers */
	private Long benutzerId;

	/**
	 * öffentlicher Konstruktor
	 * 
	 * @param darfLesen             soll das Leserecht enthalten sein
	 * @param darfSchreiben         soll das Schreibrecht enthalten sein
	 * @param ressourceCollectionId die ID der {@link DTODavRessourceCollection}
	 * @param benutzerId            die BenutzerID
	 */
	public DavRessourceCollectionACLPermissions(boolean darfLesen, boolean darfSchreiben, long ressourceCollectionId,
			long benutzerId) {
		this.darfLesen = darfLesen;
		this.darfSchreiben = darfSchreiben;
		this.ressourceCollectionId = ressourceCollectionId;
		this.benutzerId = benutzerId;
	}

	/**
	 * öffentlicher Konstruktor
	 * 
	 * @param perms                 die Berechtigungen für diesen ACL-Eintrag als
	 *                              String
	 * @param ressourceCollectionId die ID der {@link DTODavRessourceCollection}
	 * @param benutzerId            die BenutzerID
	 */
	public DavRessourceCollectionACLPermissions(String perms, long ressourceCollectionId, long benutzerId) {
		char[] charArray = perms.toCharArray();
		this.darfLesen = READ_PERMISSION == charArray[0];
		this.darfSchreiben = WRITE_PERMISSION == charArray[1];
		this.ressourceCollectionId = ressourceCollectionId;
		this.benutzerId = benutzerId;
	}

	/**
	 * getter für Leserecht
	 * 
	 * @return das Leserecht
	 */
	public boolean darfLesen() {
		return darfLesen;
	}

	/**
	 * getter für Schreibrecht
	 * 
	 * @return das Schreibrecht
	 */
	public boolean darfSchreiben() {
		return darfSchreiben;
	}

	/**
	 * getter für die ID der {@link DTODavRessourceCollection}
	 * 
	 * @return die ID der RessourceCollection
	 */
	public Long getRessourceCollectionId() {
		return ressourceCollectionId;
	}

	/**
	 * getter für die BenutzerID
	 * 
	 * @return die BenutzerID
	 */
	public Long getBenutzerId() {
		return benutzerId;
	}

	/**
	 * Wandelt diesen Rechte in diesem ACL-Eintrag in eine Zeichenkette zum
	 * speichern in der Datenbank um
	 * 
	 * @return eine Zeichenkette, die die Rechte dieses ACL-Eintrags repräsentiert
	 */
	public String toPermissionString() {
		StringBuilder sb = new StringBuilder();
		sb.append(darfLesen ? READ_PERMISSION : NO_PERMISSION);
		sb.append(darfSchreiben ? WRITE_PERMISSION : NO_PERMISSION);
		return sb.toString();
	}
}
