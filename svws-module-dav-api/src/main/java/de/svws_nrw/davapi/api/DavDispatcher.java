package de.svws_nrw.davapi.api;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import de.svws_nrw.davapi.model.dav.All;
import de.svws_nrw.davapi.model.dav.CurrentUserPrivilegeSet;
import de.svws_nrw.davapi.model.dav.Error;
import de.svws_nrw.davapi.model.dav.Privilege;
import de.svws_nrw.davapi.model.dav.Prop;
import de.svws_nrw.davapi.model.dav.Propstat;
import de.svws_nrw.davapi.model.dav.Read;
import de.svws_nrw.davapi.model.dav.ReadAcl;
import de.svws_nrw.davapi.model.dav.ReadCurrentUserPrivilegeSet;
import de.svws_nrw.davapi.model.dav.Response;
import de.svws_nrw.davapi.model.dav.SimplePrivilege;
import de.svws_nrw.davapi.model.dav.Write;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.UriBuilder;

/**
 * Diese abstrakte Klasse ist die Grundlage für das einheitliche Verarbeiten von
 * Requests über das DAV-API des SVWSs.
 */
public abstract class DavDispatcher {

	/** Die allgemeine URL für DAV */
	public static final @NotNull String DAV_URL = "/dav/{schema}";

	/** Die allgemeine URL für einen DAV-Benutzer */
	public static final @NotNull String DAV_USER_URL = "/dav/{schema}/benutzer/{benutzerId}";

	/** Die allgemeine URL für CalDav */
	public static final @NotNull String CALDAV_URL = "/dav/{schema}/kalender";

	/** Die allgemeine URL für eine CalDav-Resource-Collection */
	public static final @NotNull String CALDAV_URL_RESOURCE_COLLECTION = "/dav/{schema}/kalender/{resourceCollectionId}";

	/** Die allgemeine URL für eine CalDav-Resource */
	public static final @NotNull String CALDAV_URL_RESOURCE = "/dav/{schema}/kalender/{resourceCollectionId}/{resourceId}.ics";

	/** Die allgemeine URL für CardDav */
	public static final @NotNull String CARDDAV_URL = "/dav/{schema}/adressbuecher";

	/** Die allgemeine URL für eine CardDav-Resource-Collection */
	public static final @NotNull String CARDDAV_URL_RESOURCE_COLLECTION = "/dav/{schema}/adressbuecher/{resourceCollectionId}";

	/** Die allgemeine URL für eine CardDav-Resource */
	public static final @NotNull String CARDDAV_URL_RESOURCE = "/dav/{schema}/adressbuecher/{resourceCollectionId}/{resourceId}.vcf";


	/** URI-Parameter für die Erzeugung von URIs des Ergebnisobjekts */
	protected final @NotNull Map<String, String> params = new HashMap<>();


	/**
	 * Setzt den URI-Parameter für key auf den Wert value.
	 *
	 * @param key     der Schlüssel
	 * @param value   der Wert
	 */
	private void setParameterString(final @NotNull String key, final String value) {
		if ((value == null) || (value.isBlank()))
			params.remove(key);
		else
			params.put(key, value);
	}


	/**
	 * getter für den Parameter 'schema', das Datenbankschema, gegen das Anfragen an
	 * das API ausgeführt werden
	 *
	 * @return der Parameter für 'schema'
	 */
	public String getParameterSchema() {
		return params.get("schema");
	}

	/**
	 * setter für den Parameter 'schema', das Datenbankschema, gegen das Anfragen an
	 * das API ausgeführt werden
	 *
	 * @param schema der Parameter für 'schema'
	 */
	public void setParameterSchema(final String schema) {
		setParameterString("schema", schema);
	}

	/**
	 * getter für den Parameter 'benutzerId', die Id der Ressource Benutzer
	 *
	 * @return der Parameter für 'benutzerId'
	 */
	public String getParameterBenutzerId() {
		return params.get("benutzerId");
	}

	/**
	 * setter für den Parameter 'benutzerId', die Id der Ressource Benutzer
	 *
	 * @param benutzerId der Parameter für 'benutzerId'
	 */
	public void setParameterBenutzerId(final String benutzerId) {
		setParameterString("benutzerId", benutzerId);
	}

	/**
	 * getter für den Parameter 'adressbuchId', die Id der Ressource Adressbuch
	 *
	 * @return der Parameter für 'adressbuchId'
	 */
	public String getParameterResourceCollectionId() {
		return params.get("resourceCollectionId");
	}

	/**
	 * setter für den Parameter 'adressbuchId', die Id der Ressource Adressbuch
	 *
	 * @param resourceCollectionId der Parameter für 'adressbuchId'
	 */
	public void setParameterResourceCollectionId(final String resourceCollectionId) {
		setParameterString("resourceCollectionId", resourceCollectionId);
	}

	/**
	 * getter für den Parameter 'adressbuchEintragId', die Id der Ressource
	 * AdressbuchEintrag
	 *
	 * @return der Parameter für 'adressbuchEintragId'
	 */
	public String getParameterResourceId() {
		return params.get("resourceId");
	}

	/**
	 * setter für den Parameter 'adressbuchEintragId', die Id der Ressource
	 * AdressbuchEintrag
	 *
	 * @param resourceId der Parameter für 'adressbuchEintragId'
	 */
	public void setParameterResourceId(final String resourceId) {
		setParameterString("resourceId", resourceId);
	}


	/**
	 * Generiert die URI zur für die Ressource durch einsetzen der übergebenen Parameter in den übergebenen Pfad.
	 *
	 * @param path     der Pfad
	 * @param params   die Parameter für den Aufbau der URI
	 *
	 * @return die URI zur Ressource als String.
	 */
	private static @NotNull String getDavUri(final @NotNull String path, final @NotNull Map<String, String> params) {
		final UriBuilder uriBuilder = UriBuilder.fromPath(path);
		final URI uri = uriBuilder.buildFromMap(params);
		return uri.toASCIIString();
	}


	/**
	 * Gibt die URI für DAV zurück.
	 *
	 * @return die URI
	 */
	public @NotNull String getRootUri() {
		return getDavUri(DAV_URL, params);
	}


	/**
	 * Gibt die URI für den Benutzer zurück.
	 *
	 * @return die URI
	 */
	public @NotNull String getBenutzerUri() {
		return getDavUri(DAV_USER_URL, params);
	}


	/**
	 * Gibt die URI für den Kalender zurück.
	 *
	 * @return die URI
	 */
	public @NotNull String getKalenderUri() {
		return getDavUri(CALDAV_URL, params);
	}


	/**
	 * Gibt die URI für eine Kalender-Resource-Collection zurück.
	 *
	 * @return die URI
	 */
	public @NotNull String getKalenderResourceCollectionUri() {
		return getDavUri(CALDAV_URL_RESOURCE_COLLECTION, params);
	}


	/**
	 * Gibt die URI für eine Kalender-Resource zurück.
	 *
	 * @return die URI
	 */
	public @NotNull String getKalenderResourceUri() {
		return getDavUri(CALDAV_URL_RESOURCE, params);
	}


	/**
	 * Gibt die URI für die Adressbücher zurück.
	 *
	 * @return die URI
	 */
	public @NotNull String getCardDavUri() {
		return getDavUri(CARDDAV_URL, params);
	}


	/**
	 * Gibt die URI für eine Kalender-Resource-Collection zurück.
	 *
	 * @return die URI
	 */
	public @NotNull String getCardDavResourceCollectionUri() {
		return getDavUri(CARDDAV_URL_RESOURCE_COLLECTION, params);
	}


	/**
	 * Gibt die URI für eine Kalender-Resource zurück.
	 *
	 * @return die URI
	 */
	public @NotNull String getCardDavResourceUri() {
		return getDavUri(CARDDAV_URL_RESOURCE, params);
	}


	/**
	 * Erzeugt ein Error-Rückgabeobjekt mit der angegebenen Message. Dieses Objekt
	 * wird von den Dispatchern zurückgegeben, wenn die angefragten Ressource nicht
	 * gefunden wurde.
	 *
	 * @param message Fehlermeldung
	 * @return Error-Objekt
	 */
	protected Error createResourceNotFoundError(final String message) {
		// korrektes Response für Fehler sieht wie folgt aus
		//CalDav Beispiel:
//		   <D:error>
//		     <C:supported-filter>
//		       <C:prop-filter name="X-ABC-GUID"/>
//		     </C:supported-filter>
//		   </D:error>

		// WebDav Beispiel
//		<d:multistatus xmlns:d="DAV:">
//	       <d:response>
//	         <d:href>http://www.example.com/container/resource3</d:href>
//	         <d:status>HTTP/1.1 423 Locked</d:status>
//	         <d:error><d:lock-token-submitted/></d:error>
//	       </d:response>
//	     </d:multistatus>
		return new Error();

	}

	/**
	 * Erzeugt ein Response-Rückgabeobjekt. Diese wird dynamisch aufgebaut auf der
	 * Basis der im Request angefragten Properties zu einer Ressource und den
	 * tatsächlich gefundenen/ermittelten Properties. Nicht-gefundene Properties
	 * oder Ressourcen werden in einem speziellen Block (404) im Response
	 * aufgelistet.
	 *
	 * @param propRequested Angeforderte Properties zu einer Ressource
	 * @param propResponded Gefundene/Ermittelte Properties zu einer Ressource
	 * @return Response-Objekt
	 */
	protected static final Response createResponse(final @NotNull Prop propRequested, final @NotNull Prop propResponded) {
		final Response response = new Response();
		final Propstat propStat200 = new Propstat();
		propStat200.setStatus(Propstat.PROP_STATUS_200_OK);
		propStat200.setProp(propResponded);
		response.getPropstat().add(propStat200);

		final DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);
		final Prop prop404 = dynamicPropUtil.getUnsupportedProps(propResponded);
		if (prop404 != null) {
			final Propstat propStat404 = new Propstat();
			propStat404.setStatus(Propstat.PROP_STATUS_404_NOT_FOUND);
			propStat404.setProp(prop404);
			response.getPropstat().add(propStat404);
		}
		return response;
	}

	/**
	 * Erzeugt ein DAV-Objekt vom Typ CurrentUserPrivilegeSet, das alle
	 * erforderlichen Rechte (Privileges) für einen lesenden Zugriff auf
	 * DAV-Ressourcen enthält.
	 *
	 *
	 *
	 * @return CurrentUserPrivilegeSet mit Privileges für einen lesenden Zugriff.
	 */
	protected static final CurrentUserPrivilegeSet getReadOnlyPrivilegeSet() {
		return getPrivilegeSet(true, false);
	}

	/**
	 * Erzeugt ein DAV-Objekt vom Typ CurrentUserPrivilegeSet, das alle
	 * erforderlichen Rechte (Privileges) für einen lesenden Zugriff auf
	 * DAV-Ressourcen enthält.
	 *
	 * @param darfSchreiben ob der angemeldete Nutzer auf dem Kalender schreiben
	 *                      darf
	 * @param darfLesen     ob der angemeldete Nutzer den Kalender Lesen darf
	 *
	 * @return CurrentUserPrivilegeSet mit Privileges für einen lesenden Zugriff.
	 */
	protected static final CurrentUserPrivilegeSet getPrivilegeSet(final boolean darfLesen, final boolean darfSchreiben) {
		final CurrentUserPrivilegeSet privilegeSet = new CurrentUserPrivilegeSet();
		final Privilege privilegeAll = new Privilege();
		privilegeAll.getContent().add(new All());

		if (darfLesen) {
			addPrivilege(privilegeSet, new ReadCurrentUserPrivilegeSet());
			addPrivilege(privilegeSet, new Read());
			addPrivilege(privilegeSet, new ReadAcl());
		}
		if (darfSchreiben) {
			addPrivilege(privilegeSet, new All());
			addPrivilege(privilegeSet, new Write());
		}
		return privilegeSet;
	}

	/**
	 * Fügt einer Sammlung an Rechten ein weiteres Recht (als Instanz des Interface
	 * {@link SimplePrivilege}) hinzu
	 *
	 * @param set             die Rechtesammlung
	 * @param simplePrivilege das zuzufügende Privilege
	 */
	private static void addPrivilege(final CurrentUserPrivilegeSet set, final SimplePrivilege simplePrivilege) {
		final Privilege privilege = new Privilege();
		privilege.getContent().add(simplePrivilege);
		set.getPrivilege().add(privilege);
	}

	/**
	 * Homogenisiert eTag-Werte von unterschiedlichen Quellsystemen.
	 *
	 * In Abhängigkeit der Quellsysteme (z.B. Thunderbird, Outlook)
	 * werden teilweise Anführungszeichen als Bestandteil der Werte
	 * von eTags oder ifMatch-Headers mitgesendet oder nicht.
	 * Die DAV-Dispatcher benötigen diese Methode, um eine einheitliche
	 * Auswertung dieser Werte sicherzustellen.
	 *
	 * @param value String, kann Anführungszeichen enthalten
	 * @return String, enthält keine Anführungszeichen mehr
	 */
	protected String adjustETags(final String value) {
		return value.replace("\"", "");
	}

}
