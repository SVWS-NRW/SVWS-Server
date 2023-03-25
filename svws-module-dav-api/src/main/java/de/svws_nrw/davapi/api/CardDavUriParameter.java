package de.svws_nrw.davapi.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasse beschreibt die Ressourcen-Namen zu Aufbau einer CardDAV-URI. Über den
 * {@link CardDavUriBuilder} können mit Instanzen dieser Klasse URIs für das
 * CarDAV-API generiert werden.
 */
public class CardDavUriParameter {

	/** Datenbankschema, gegen das Anfragen an das API ausgeführt werden */
	private String schema;
	/** Id der Ressource Benutzer */
	private String benutzerId;
	/** Id der Ressource Adressbuch */
	private String adressbuchId;
	/** Id der Ressource AdressbuchEintrag */
	private String adressbuchEintragId;

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
	public void setSchema(String schema) {
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
	public void setBenutzerId(String benutzerId) {
		this.benutzerId = benutzerId;
	}

	/**
	 * getter für den Parameter 'adressbuchId', die Id der Ressource Adressbuch
	 * 
	 * @return der Parameter für 'adressbuchId'
	 */
	public String getAdressbuchId() {
		return adressbuchId;
	}

	/**
	 * setter für den Parameter 'adressbuchId', die Id der Ressource Adressbuch
	 * 
	 * @param adressbuchId der Parameter für 'adressbuchId'
	 */
	public void setAdressbuchId(String adressbuchId) {
		this.adressbuchId = adressbuchId;
	}

	/**
	 * getter für den Parameter 'adressbuchEintragId', die Id der Ressource
	 * AdressbuchEintrag
	 * 
	 * @return der Parameter für 'adressbuchEintragId'
	 */
	public String getAdressbuchEintragId() {
		return adressbuchEintragId;
	}

	/**
	 * setter für den Parameter 'adressbuchEintragId', die Id der Ressource
	 * AdressbuchEintrag
	 * 
	 * @param adressbuchEintragId der Parameter für 'adressbuchEintragId'
	 */
	public void setAdressbuchEintragId(String adressbuchEintragId) {
		this.adressbuchEintragId = adressbuchEintragId;
	}

	/**
	 * generiert eine Map aus diesen URI-Parametern zur weiteren Verwendung
	 * 
	 * @return eine Map, bei denen die URIParameter zu ihren Parameterbezeichnungen
	 *         gemappt sind
	 */
	public Map<String, String> toMap() {
		Map<String, String> parameters = new HashMap<>();

		if (this.schema != null && !this.schema.isBlank()) {
			parameters.put("schema", this.schema);
		}
		if (this.benutzerId != null && !this.benutzerId.isBlank()) {
			parameters.put("benutzerId", this.benutzerId);
		}
		if (this.adressbuchId != null && !this.adressbuchId.isBlank()) {
			parameters.put("adressbuchId", this.adressbuchId);
		}
		if (this.adressbuchEintragId != null && !this.adressbuchEintragId.isBlank()) {
			parameters.put("adressbuchEintragId", this.adressbuchEintragId);
		}
		return parameters;
	}
}
