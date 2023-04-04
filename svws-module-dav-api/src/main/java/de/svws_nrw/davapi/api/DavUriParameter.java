package de.svws_nrw.davapi.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasse beschreibt die Ressourcen-Namen zu Aufbau einer CardDAV-URI. Über den
 * {@link DavUriBuilder} können mit Instanzen dieser Klasse URIs für das
 * CarDAV-API generiert werden.
 */
public class DavUriParameter {

	/** Datenbankschema, gegen das Anfragen an das API ausgeführt werden */
	private String schema;
	/** Id der Ressource Benutzer */
	private String benutzerId;
	/** Id der Ressource-Collection, z.B. Adressbuch, Kalender */
	private String resourceCollectionId;
	/** Id der Ressource, z.B. Adressbuch-Eintrag, Kalender-Eintrag */
	private String resourceId;

	/**
	 * getter für den Parameter 'schema', das Datenbankschema, gegen das Anfragen an
	 * das API ausgeführt werden
	 *
	 * @return der Parameter für 'schema'
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * setter für den Parameter 'schema', das Datenbankschema, gegen das Anfragen an
	 * das API ausgeführt werden
	 *
	 * @param schema der Parameter für 'schema'
	 */
	public void setSchema(final String schema) {
		this.schema = schema;
	}

	/**
	 * getter für den Parameter 'benutzerId', die Id der Ressource Benutzer
	 *
	 * @return der Parameter für 'benutzerId'
	 */
	public String getBenutzerId() {
		return benutzerId;
	}

	/**
	 * setter für den Parameter 'benutzerId', die Id der Ressource Benutzer
	 *
	 * @param benutzerId der Parameter für 'benutzerId'
	 */
	public void setBenutzerId(final String benutzerId) {
		this.benutzerId = benutzerId;
	}

	/**
	 * getter für den Parameter 'adressbuchId', die Id der Ressource Adressbuch
	 *
	 * @return der Parameter für 'adressbuchId'
	 */
	public String getResourceCollectionId() {
		return resourceCollectionId;
	}

	/**
	 * setter für den Parameter 'adressbuchId', die Id der Ressource Adressbuch
	 *
	 * @param resourceCollectionId der Parameter für 'adressbuchId'
	 */
	public void setResourceCollectionId(final String resourceCollectionId) {
		this.resourceCollectionId = resourceCollectionId;
	}

	/**
	 * getter für den Parameter 'adressbuchEintragId', die Id der Ressource
	 * AdressbuchEintrag
	 *
	 * @return der Parameter für 'adressbuchEintragId'
	 */
	public String getResourceId() {
		return resourceId;
	}

	/**
	 * setter für den Parameter 'adressbuchEintragId', die Id der Ressource
	 * AdressbuchEintrag
	 *
	 * @param resourceId der Parameter für 'adressbuchEintragId'
	 */
	public void setResourceId(final String resourceId) {
		this.resourceId = resourceId;
	}

	/**
	 * generiert eine Map aus diesen URI-Parametern zur weiteren Verwendung
	 *
	 * @return eine Map, bei denen die URIParameter zu ihren Parameterbezeichnungen
	 *         gemappt sind
	 */
	public Map<String, String> toMap() {
		final Map<String, String> parameters = new HashMap<>();

		if (this.schema != null && !this.schema.isBlank()) {
			parameters.put("schema", this.schema);
		}
		if (this.benutzerId != null && !this.benutzerId.isBlank()) {
			parameters.put("benutzerId", this.benutzerId);
		}
		if (this.resourceCollectionId != null && !this.resourceCollectionId.isBlank()) {
			parameters.put("resourceCollectionId", this.resourceCollectionId);
		}
		if (this.resourceId != null && !this.resourceId.isBlank()) {
			parameters.put("resourceId", this.resourceId);
		}
		return parameters;
	}
}
