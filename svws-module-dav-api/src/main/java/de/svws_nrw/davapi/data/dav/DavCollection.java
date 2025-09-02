package de.svws_nrw.davapi.data.dav;

import de.svws_nrw.core.types.dav.DavRessourceCollectionTyp;

/**
 * Diese Klasse ist ein DTO für eine DAV-Ressource-Collection.
 */
public class DavCollection {

	/** die ID der Ressourcensammlung */
	public Long id;

	/** der Typ der Ressourcensammlung */
	public DavRessourceCollectionTyp typ;

	/** der Besitzer der Ressourcensammlung */
	public Long besitzer;

	/** der Anzeigename der Ressourcensammlung */
	public String anzeigename;

	/** die Beschreibung der Ressourcensammlung */
	public String beschreibung;

	/** das Synctoken der Ressourcensammlung */
	public long syncToken;

	/** die Berechtigungen der Ressourcensammlung */
	public DavPermissions permissions;


	/**
	 * Leerer DefaultKonstruktor
	 */
	public DavCollection() {
		// Empty default constructor
	}

}
