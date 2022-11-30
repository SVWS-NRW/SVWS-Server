package de.nrw.schule.svws.davapi.data;

/**
 * Diese Klasse abstrahiert verschiedene Parameter für die Suche nach
 * Adressbuechern oder Kalendern aus einem Repository Die Klasse soll nicht an
 * anderen Orten instantiiert werden, statt dessen sollen die vorhandenen
 * Kombinationen aus Parametern genutzt werden.
 *
 */
public class CollectionRessourceQueryParameters {

	/**
	 * Parameter bestimmt, ob die angefragten Adressbuecher die Adressbucheinträge
	 * enthalten sollen.
	 */
	public final boolean includeRessources;
	/**
	 * Parameter bestimmt, ob die angefragten Adressbucheinträge ihre IDs enthalten
	 * sollen
	 */
	public final boolean includeEintragIDs;

	/**
	 * Parameter bestimmt, ob die angefragten Ressourcen ihre UIDs enthalten sollen
	 */
	public final boolean includeEintragUIDs;

	/**
	 * Parameter bestimmt, b die angefragten Ressourcen ihren Payload, also den
	 * Ressourceninhalt enthalten sollen
	 */
	public final boolean includeEintragPayload;

	/**
	 * Parameter bestimmt, ob die angefragte Ressourcen oder Resosurcensammlungen
	 * ihr Versionskennzeichen enthalten sollen.
	 */
	public final boolean includeVersion;

	/**
	 * Privater Konstruktor
	 * 
	 * @param includeEintraege
	 * @param includeEintragIDs
	 * @param includeEintragUIDs
	 * @param includeEintragPayload
	 * @param includeVersion
	 */
	private CollectionRessourceQueryParameters(boolean includeRessources, boolean includeEintragIDs,
			boolean includeEintragUIDs, boolean includeEintragPayload, boolean includeVersion) {
		super();
		this.includeRessources = includeRessources;
		this.includeEintragIDs = includeEintragIDs;
		this.includeEintragUIDs = includeEintragUIDs;
		this.includeEintragPayload = includeEintragPayload;
		this.includeVersion = includeVersion;
	}

	/**
	 * QueryParameter für Ressourcensammlungen, welche ihr Versionskennzeichen, aber
	 * nicht ihre Ressourcen enthalten sollen
	 */
	public static final CollectionRessourceQueryParameters EXCLUDE_RESSOURCES = new CollectionRessourceQueryParameters(
			false, false, false, false, true);

	/**
	 * QueryParameter für Ressourcensammlungen und Ressourcen, welche nur IDs, UIDs
	 * und Versionskennzeichen enthalten sollen
	 */
	public static final CollectionRessourceQueryParameters INCLUDE_RESSOURCES_EXCLUDE_PAYLOAD = new CollectionRessourceQueryParameters(
			true, true, true, false, true);

	/**
	 * QueryParameter für Ressourcensammlungen und Ressourcen, welche ihren
	 * Kompletten Inhalt enthalten sollen.
	 */
	public static final CollectionRessourceQueryParameters INCLUDE_RESSOURCES_INCLUDE_PAYLOAD = new CollectionRessourceQueryParameters(
			true, true, true, true, true);
}
