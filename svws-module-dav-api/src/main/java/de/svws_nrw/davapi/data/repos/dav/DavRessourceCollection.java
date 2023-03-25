package de.svws_nrw.davapi.data.repos.dav;

import de.svws_nrw.core.types.dav.DavRessourceCollectionTyp;

/**
 * Diese Klasse bildet eine Ressourcensammlung ab.
 *
 */
public class DavRessourceCollection {

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
	/** die ID der Ressourcensammlung */
	public Long id;
	/** die Berechtigungen der Ressourcensammlung */
	public DavRessourceCollectionACLPermissions permissions;

	/**
	 * leerer DefaultKonstruktor
	 */
	public DavRessourceCollection() {
		//empty default constructor
	}

}
