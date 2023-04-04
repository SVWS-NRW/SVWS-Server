package de.svws_nrw.davapi.api;

import jakarta.ws.rs.core.UriBuilder;
import java.net.URI;

import jakarta.validation.constraints.NotNull;

/**
 * Klasse dient der Generierung von Ressourcen-URIs des DAV-APIs. Diese wird bei
 * der Erstellung der Ergebnisobjekte benötigt. Die Ergebnisobjekte enthalten
 * tw. URIs zu angefragten Ressourcen.
 */
public final class CardDavUriBuilder {

//TODO Schuljahresabschintt als URI param ergänzen

	/** URI-Pattern der Basis URI für das CardDAV-API */
	public static final String CARD_DAV_BASE_URI_PATTERN = "/db/{schema}/carddav";
	/**
	 * URI-Pattern für die Resource Princial. Muss der Basis-URI angehängt werden.
	 */
	public static final String CARD_DAV_REL_URI_PATTERN_PRINCIPAL = "/benutzer/{benutzerId}";
	/**
	 * URI-Pattern für die Resource Adressbuch-Liste. Muss der Basis-URI angehängt
	 * werden.
	 */
	public static final String CARD_DAV_REL_URI_PATTERN_ADDRESSBOOK_COLLECTION = "/adressbuecher";
	/**
	 * URI-Pattern für die Resource Adressbuch. Muss der Basis-URI angehängt werden.
	 */
	public static final String CARD_DAV_REL_URI_PATTERN_ADDRESSBOOK = "/adressbuecher/{adressbuchId}";
	/**
	 * URI-Pattern für die Resource AdressbuchEintrag. Muss der Basis-URI angehängt
	 * werden.
	 */
	public static final String CARD_DAV_REL_URI_PATTERN_ADRESS_ENTRY = "/adressbuecher/{adressbuchId}/{adressbuchEintragId}.vcf";


	/**
	 * privater Konstruktor für statische Utility Klasse.
	 */
	private CardDavUriBuilder() {

	}

    /**
     * Generiert die URI zur CardDAV Root-Ressource
     * @param parameter Parameter für den Aufbau der URI
     * @return URI zur Ressource als String.
     */
    static String getCardDavRootUri(@NotNull final CardDavUriParameter parameter) {
        final UriBuilder uriBuilder = UriBuilder.fromPath(CARD_DAV_BASE_URI_PATTERN);
        final URI uri = uriBuilder.buildFromMap(parameter.toMap());
        return uri.toASCIIString();
    }

	/**
	 * Generiert die URI zu einer Adressbuch-Ressource
	 *
	 * @param parameter Parameter für den Aufbau der URI
	 * @return URI zur Ressource als String.
	 */
	static String getAddressbookUri(@NotNull final CardDavUriParameter parameter) {
		final UriBuilder uriBuilder = UriBuilder
				.fromPath(CARD_DAV_BASE_URI_PATTERN.concat(CARD_DAV_REL_URI_PATTERN_ADDRESSBOOK));
		final URI uri = uriBuilder.buildFromMap(parameter.toMap());
		return uri.toASCIIString();
	}

	/**
	 * Generiert die URI zu einer Adressbuch-Liste
	 *
	 * @param parameter Parameter für den Aufbau der URI
	 * @return URI zur Ressource als String.
	 */
	static String getAddressbookCollectionUri(@NotNull final CardDavUriParameter parameter) {
		final UriBuilder uriBuilder = UriBuilder
				.fromPath(CARD_DAV_BASE_URI_PATTERN.concat(CARD_DAV_REL_URI_PATTERN_ADDRESSBOOK_COLLECTION));
		final URI uri = uriBuilder.buildFromMap(parameter.toMap());
		return uri.toASCIIString();
	}

	/**
	 * Generiert die URI zu einer AdressbuchEintrag-Ressource
	 *
	 * @param parameter Parameter für den Aufbau der URI
	 * @return URI zur Ressource als String.
	 */
	static String getAdressEntryUri(@NotNull final CardDavUriParameter parameter) {
		final UriBuilder uriBuilder = UriBuilder
				.fromPath(CARD_DAV_BASE_URI_PATTERN.concat(CARD_DAV_REL_URI_PATTERN_ADRESS_ENTRY));
		final URI uri = uriBuilder.buildFromMap(parameter.toMap());
		return uri.toASCIIString();
	}

	/**
	 * Generiert die URI zu einer Principal-Ressource (Benutzer)
	 *
	 * @param parameter Parameter für den Aufbau der URI
	 * @return URI zur Ressource als String.
	 */
	static String getPrincipalUri(@NotNull final CardDavUriParameter parameter) {
		final UriBuilder uriBuilder = UriBuilder
				.fromPath(CARD_DAV_BASE_URI_PATTERN.concat(CARD_DAV_REL_URI_PATTERN_PRINCIPAL));
		final URI uri = uriBuilder.buildFromMap(parameter.toMap());
		return uri.toASCIIString();
	}

}
