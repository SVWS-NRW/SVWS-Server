package de.svws_nrw.davapi.data.dav;


/**
 * Diese Enumaration gibt an, welche Informationen zu einer Collection abgefragt werden sollen. Sie stellt
 * für die einzelnen Datenbankanfragen dann boolean-Flags zur Verfügung, die angeben welche Informationen
 * abgefragt werden sollen und welche nicht.
 */
public enum CollectionQueryParameters {

	/** Gibt an, dass nur die Versionsinformationen der Collection abgefragt werden sollen, aber icht die Ressourcen des Collection */
	NO_RESSOURCES(false, false, false, false, true),

	/** Gibt an, dass die Collection und die Ressourcen ohne Payload abgefragt werden sollen */
	NO_PAYLOAD(true, true, true, false, true),

	/** Gibt an, dass die Collection und die Ressourcen vollständig abgefragt werden sollen */
	ALL(true, true, true, true, true);


	/** Parameter bestimmt, ob die angefragten Adressbuecher die Adressbucheinträge enthalten sollen. */
	public final boolean includeRessources;

	/** Parameter bestimmt, ob die angefragten Adressbucheinträge ihre IDs enthalten sollen */
	public final boolean includeEintragIDs;

	/** Parameter bestimmt, ob die angefragten Ressourcen ihre UIDs enthalten sollen */
	public final boolean includeEintragUIDs;

	/** Parameter bestimmt, ob die angefragten Ressourcen ihren Payload, also den Ressourceninhalt enthalten sollen */
	public final boolean includeEintragPayload;

	/** Parameter bestimmt, ob die angefragte Ressourcen oder Resosurcensammlungen ihr Versionskennzeichen enthalten sollen. */
	public final boolean includeVersion;


	/**
	 * Erstellt einen Eintrag für die Konfiguration der Abfrage-Parameter
	 *
	 * @param includeRessources
	 * @param includeEintragIDs
	 * @param includeEintragUIDs
	 * @param includeEintragPayload
	 * @param includeVersion
	 */
	CollectionQueryParameters(final boolean includeRessources, final boolean includeEintragIDs,
			final boolean includeEintragUIDs, final boolean includeEintragPayload, final boolean includeVersion) {
		this.includeRessources = includeRessources;
		this.includeEintragIDs = includeEintragIDs;
		this.includeEintragUIDs = includeEintragUIDs;
		this.includeEintragPayload = includeEintragPayload;
		this.includeVersion = includeVersion;
	}

}
