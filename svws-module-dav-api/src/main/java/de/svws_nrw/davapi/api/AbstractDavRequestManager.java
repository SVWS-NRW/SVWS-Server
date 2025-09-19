package de.svws_nrw.davapi.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationModule;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.ThrowingFunction;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.davapi.model.dav.All;
import de.svws_nrw.davapi.model.dav.CurrentUserPrivilegeSet;
import de.svws_nrw.davapi.model.dav.Error;
import de.svws_nrw.davapi.model.dav.Multistatus;
import de.svws_nrw.davapi.model.dav.Privilege;
import de.svws_nrw.davapi.model.dav.Prop;
import de.svws_nrw.davapi.model.dav.Propstat;
import de.svws_nrw.davapi.model.dav.Read;
import de.svws_nrw.davapi.model.dav.ReadAcl;
import de.svws_nrw.davapi.model.dav.ReadCurrentUserPrivilegeSet;
import de.svws_nrw.davapi.model.dav.SimplePrivilege;
import de.svws_nrw.davapi.model.dav.Write;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese abstrakte Klasse dient als Grundlage für das einheitliche Verarbeiten von HTTP-Requests an
 * die DAV-API des SVWS-Servers.
 */
public abstract class AbstractDavRequestManager {

	/** Debug Option, damit Requests nach Insomnia übertragen werden können */
	protected static final boolean LOG_REQUESTS = false;

	/** Der ObjectMapper für das Deserialisieren der XML-Daten */
	protected final ObjectMapper mapper = getMapper();

	/** Die Datenbank-Verbindung, die von dem Request-Manager verwendet wird */
	protected final @NotNull DBEntityManager conn;

	/** Der Input-Stream mit dem Body der Anfrage*/
	protected final byte[] requestBody;

	/** URI-Parameter für die Erzeugung von URIs des Ergebnisobjekts */
	protected final @NotNull Map<String, String> params = new HashMap<>();


	/**
	 * Erstellt einen neuen Request-Manager, welcher die übergebene Datenbank-Verbindung nutzt.
	 *
	 * @param conn   die Datenbankverbindung
	 * @param is     der Input-Stream mit dem Body der Anfrage
	 *
	 * @throws IOException   wenn der Request-Body nicht gelesen werden kann
	 */
	protected AbstractDavRequestManager(final @NotNull DBEntityManager conn, final InputStream is) throws IOException {
		this.conn = conn;
		this.requestBody = is.readAllBytes();
		setParameterSchema(conn.getDBSchema());
		setParameterBenutzerId(String.valueOf(conn.getUser().getId()));
	}


	/**
	 * Erstellt einen neuen ObjectMapper zum Deserialisieren von XML-Daten
	 *
	 * @return der ObjectMapper
	 */
	private static ObjectMapper getMapper() {
		final JacksonXmlModule module = new JacksonXmlModule();
		module.setDefaultUseWrapper(false);
		final ObjectMapper mapper = new XmlMapper(module);
		mapper.registerModule(new JakartaXmlBindAnnotationModule());
		mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		final DeserializationConfig deserializationConfig = mapper.getDeserializationConfig();
		deserializationConfig.with(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
		mapper.setConfig(deserializationConfig);
		return mapper;
	}


	/**
	 * Führt den übergebenen Task für den übergebenen HTTP-Request im angegebenen Server-Mode und mit den
	 * übergebenen benötigten Benutzerkompetenzen aus, sofern dies erlaubt ist.
	 *
	 * @param task          der auszuführende Task
	 * @param request       die HTTP-Anfrage
	 * @param mode          der Server-Mode
	 * @param kompetenzen   die benötigten Kompetenzen
	 *
	 * @return die HTTP-Response
	 */
	public static Response handle(final ThrowingFunction<DBEntityManager, Response> task, final HttpServletRequest request,
			final ServerMode mode, final BenutzerKompetenz... kompetenzen) {
		try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, mode, kompetenzen)) {
				return task.applyThrows(conn);
		} catch (final ApiOperationException e) {
			return e.getResponse();
		} catch (final IOException e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
		} catch (final Exception e) {
			if (e instanceof final ApiOperationException apiOperationException)
				return apiOperationException.getResponse();
			return new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e).getResponse();
		}
	}


	/**
	 * Generiert eine HTTP-Response für das übergebene Antwort-Objekt. Dabei wird
	 * zwischen den Fällen unterschieden, dass es sich um eine {@link Multistatus}-Antwort,
	 * eine {@link Error}-Antwort oder um eine fehlerhafte Anfrage handelt (alle anderen Fälle).
	 *
	 * @param o   das Objekt mit den Antwortdaten ({@link Multistatus} oder {@link Error})
	 *
	 * @return die HTTP-Response für das übergebene Objekt
	 */
	public static Response buildResponse(final Object o) {
		if (o instanceof Multistatus)
			return Response.status(DavExtendedHttpStatus.MULTISTATUS).type(MediaType.TEXT_XML).entity(o).build();
		if (o instanceof Error)
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_XML).entity(o).build();
		return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).build();
	}


	/**
	 * Generiert ein HTTP-Response mit dem Statuscode 201 (Created) und einem EntityTag-Header.
	 *
	 * @param eTag   das {@link EntityTag}
	 *
	 * @return die HTTP-Response
	 */
	protected static Response buildCreatedResponse(final EntityTag eTag) {
		return Response.status(Response.Status.CREATED).header("ETag", eTag.getValue()).build();
	}

	/**
	 * Generiert ein HTTP-Response mit dem Statuscode 204 (No Content).
	 *
	 * @return die HTTP-Response
	 */
	protected static Response buildNoContentResponse() {
		return Response.status(Status.NO_CONTENT).type(MediaType.TEXT_PLAIN).build();
	}

	/**
	 * Generiert ein HTTP-Response mit dem Statuscode 204 (No Content) und einem EntityTag-Header.
	 *
	 * @param eTag   das {@link EntityTag}
	 *
	 * @return die HTTP-Response
	 */
	protected static Response buildNoContentResponse(final EntityTag eTag) {
		return Response.status(Response.Status.NO_CONTENT).header("ETag", eTag.getValue()).build();
	}

	/**
	 * Generiert ein HTTP-Response mit dem Statuscode 400 (Bad Request) und der Message aus der übergebenen Exception
	 * als Fehlermeldung.
	 *
	 * @param e   die Exception
	 *
	 * @return die HTTP-Response
	 */
	protected static Response buildBadRequest(final Exception e) {
		return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
	}


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
		return getDavUri("/dav/{schema}", params);
	}


	/**
	 * Gibt die URI für den Benutzer zurück.
	 *
	 * @return die URI
	 */
	public @NotNull String getBenutzerUri() {
		return getDavUri("/dav/{schema}/benutzer/{benutzerId}", params);
	}


	/**
	 * Gibt die URI für den Kalender zurück.
	 *
	 * @return die URI
	 */
	public @NotNull String getKalenderUri() {
		return getDavUri("/dav/{schema}/kalender", params);
	}


	/**
	 * Gibt die URI für eine Kalender-Resource-Collection zurück.
	 *
	 * @return die URI
	 */
	public @NotNull String getKalenderResourceCollectionUri() {
		return getDavUri("/dav/{schema}/kalender/{resourceCollectionId}", params);
	}


	/**
	 * Gibt die URI für eine Kalender-Resource zurück.
	 *
	 * @return die URI
	 */
	public @NotNull String getKalenderResourceUri() {
		return getDavUri("/dav/{schema}/kalender/{resourceCollectionId}/{resourceId}.ics", params);
	}


	/**
	 * Gibt die URI für die Adressbücher zurück.
	 *
	 * @return die URI
	 */
	public @NotNull String getCardDavUri() {
		return getDavUri("/dav/{schema}/adressbuecher", params);
	}


	/**
	 * Gibt die URI für eine Kalender-Resource-Collection zurück.
	 *
	 * @return die URI
	 */
	public @NotNull String getCardDavResourceCollectionUri() {
		return getDavUri("/dav/{schema}/adressbuecher/{resourceCollectionId}", params);
	}


	/**
	 * Gibt die URI für eine Kalender-Resource zurück.
	 *
	 * @return die URI
	 */
	public @NotNull String getCardDavResourceUri() {
		return getDavUri("/dav/{schema}/adressbuecher/{resourceCollectionId}/{resourceId}.vcf", params);
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
	 * @param uri   die Uri für das href-Feld der XML-Response
	 *
	 * @return Response-Objekt
	 */
	protected static final de.svws_nrw.davapi.model.dav.Response createResponse(final @NotNull Prop propRequested,
			final @NotNull Prop propResponded, final @NotNull String uri) {
		final de.svws_nrw.davapi.model.dav.Response response = new de.svws_nrw.davapi.model.dav.Response();
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
		response.getHref().add(uri);
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
