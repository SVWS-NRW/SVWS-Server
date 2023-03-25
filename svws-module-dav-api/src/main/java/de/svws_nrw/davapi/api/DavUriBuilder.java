package de.svws_nrw.davapi.api;

import jakarta.ws.rs.core.UriBuilder;
import java.net.URI;

import jakarta.validation.constraints.NotNull;

/**
 * Klasse dient der Generierung von Ressourcen-URIs des DAV-APIs. Diese wird bei
 * der Erstellung der Ergebnisobjekte benötigt. Die Ergebnisobjekte enthalten
 * tw. URIs zu angefragten Ressourcen.
 */
public class DavUriBuilder {
//TODO Schuljahresabschintt als URI param ergänzen
	/** URI-Pattern der Basis URI für das DAV-API */
	public static final String DAV_BASE_URI_PATTERN = "/db/{schema}/dav";
	/**
	 * URI-Pattern für die Resource Princial. Muss der Basis-URI angehängt werden.
	 */
	public static final String DAV_REL_URI_PATTERN_PRINCIPAL = "/benutzer/{benutzerId}";
	/**
	 * URI-Pattern für die Resource Adressbuch-Liste. Muss der Basis-URI angehängt
	 * werden.
	 */
	public static final String CARD_DAV_REL_URI_PATTERN_ADDRESSBOOK_COLLECTION = "/adressbuecher";
	/**
	 * URI-Pattern für die Resource Adressbuch. Muss der Basis-URI angehängt werden.
	 */
	public static final String CARD_DAV_REL_URI_PATTERN_ADDRESSBOOK = "/adressbuecher/{resourceCollectionId}";
	/**
	 * URI-Pattern für die Resource AdressbuchEintrag. Muss der Basis-URI angehängt
	 * werden.
	 */
	public static final String CARD_DAV_REL_URI_PATTERN_ADDRESS_ENTRY = "/adressbuecher/{resourceCollectionId}/{resourceId}.vcf";
	/**
	 * URI-Pattern für die Resource Kalender-Liste. Muss der Basis-URI angehängt
	 * werden.
	 */
	public static final String CARD_DAV_REL_URI_PATTERN_CALENDAR_COLLECTION = "/kalender";
	/**
	 * URI-Pattern für die Resource Adressbuch. Muss der Basis-URI angehängt werden.
	 */
	public static final String CARD_DAV_REL_URI_PATTERN_CALENDAR = "/kalender/{resourceCollectionId}";
	/**
	 * URI-Pattern für die Resource KalenderEintrag. Muss der Basis-URI angehängt
	 * werden.
	 */
	public static final String CARD_DAV_REL_URI_PATTERN_CALENDAR_ENTRY = "/kalender/{resourceCollectionId}/{resourceId}.ics";

	/**
	 * Statische Klasse, privater Konstruktor
	 */
	private DavUriBuilder() {
		throw new IllegalStateException("Utility class");
	}

    /**
     * Generiert die URI zur CardDAV Root-Ressource
     * @param parameter Parameter für den Aufbau der URI
     * @return URI zur Ressource als String.
     */
    static String getCardDavRootUri(@NotNull DavUriParameter parameter){
        UriBuilder uriBuilder = UriBuilder.fromPath(DAV_BASE_URI_PATTERN);
        URI uri = uriBuilder.buildFromMap(parameter.toMap());
        return uri.toASCIIString();
    }

	/**
	 * Generiert die URI zu einer Adressbuch-Ressource
	 *
	 * @param parameter Parameter für den Aufbau der URI
	 * @return URI zur Ressource als String.
	 */
	static String getAddressbookUri(@NotNull DavUriParameter parameter) {
		UriBuilder uriBuilder = UriBuilder
				.fromPath(DAV_BASE_URI_PATTERN.concat(CARD_DAV_REL_URI_PATTERN_ADDRESSBOOK));
		URI uri = uriBuilder.buildFromMap(parameter.toMap());
		return uri.toASCIIString();
	}

	/**
	 * Generiert die URI zu einer Adressbuch-Liste
	 *
	 * @param parameter Parameter für den Aufbau der URI
	 * @return URI zur Ressource als String.
	 */
	static String getAddressbookCollectionUri(@NotNull DavUriParameter parameter) {
		UriBuilder uriBuilder = UriBuilder
				.fromPath(DAV_BASE_URI_PATTERN.concat(CARD_DAV_REL_URI_PATTERN_ADDRESSBOOK_COLLECTION));
		URI uri = uriBuilder.buildFromMap(parameter.toMap());
		return uri.toASCIIString();
	}

	/**
	 * Generiert die URI zu einer AdressbuchEintrag-Ressource
	 *
	 * @param parameter Parameter für den Aufbau der URI
	 * @return URI zur Ressource als String.
	 */
	static String getAddressEntryUri(@NotNull DavUriParameter parameter) {
		UriBuilder uriBuilder = UriBuilder
				.fromPath(DAV_BASE_URI_PATTERN.concat(CARD_DAV_REL_URI_PATTERN_ADDRESS_ENTRY));
		URI uri = uriBuilder.buildFromMap(parameter.toMap());
		return uri.toASCIIString();
	}

	/**
	 * Generiert die URI zu einer Principal-Ressource (Benutzer)
	 *
	 * @param parameter Parameter für den Aufbau der URI
	 * @return URI zur Ressource als String.
	 */
	static String getPrincipalUri(@NotNull DavUriParameter parameter) {
		UriBuilder uriBuilder = UriBuilder
				.fromPath(DAV_BASE_URI_PATTERN.concat(DAV_REL_URI_PATTERN_PRINCIPAL));
		URI uri = uriBuilder.buildFromMap(parameter.toMap());
		return uri.toASCIIString();
	}

	/**
	 * Generiert die URI zu einer Kalender-Liste
	 *
	 * @param parameter Parameter für den Aufbau der URI
	 * @return URI zur Ressource als String.
	 */
	static String getCalendarCollectionUri(@NotNull DavUriParameter parameter) {
		UriBuilder uriBuilder = UriBuilder
			.fromPath(DAV_BASE_URI_PATTERN.concat(CARD_DAV_REL_URI_PATTERN_CALENDAR_COLLECTION));
		URI uri = uriBuilder.buildFromMap(parameter.toMap());
		return uri.toASCIIString();
	}

	/**
	 * Generiert die URI zu einer Kalender-Ressource
	 *
	 * @param parameter Parameter für den Aufbau der URI
	 * @return URI zur Ressource als String.
	 */
	static String getCalendarUri(@NotNull DavUriParameter parameter) {
		UriBuilder uriBuilder = UriBuilder
			.fromPath(DAV_BASE_URI_PATTERN.concat(CARD_DAV_REL_URI_PATTERN_CALENDAR));
		URI uri = uriBuilder.buildFromMap(parameter.toMap());
		return uri.toASCIIString();
	}

	/**
	 * Generiert die URI zu einer KalenderEintrag-Ressource
	 *
	 * @param parameter Parameter für den Aufbau der URI
	 * @return URI zur Ressource als String.
	 */
	static String getCalendarEntryUri(@NotNull DavUriParameter parameter) {
		UriBuilder uriBuilder = UriBuilder
			.fromPath(DAV_BASE_URI_PATTERN.concat(CARD_DAV_REL_URI_PATTERN_CALENDAR_ENTRY));
		URI uri = uriBuilder.buildFromMap(parameter.toMap());
		return uri.toASCIIString();
	}

}
