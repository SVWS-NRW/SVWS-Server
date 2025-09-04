package de.svws_nrw.api.dav;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.davapi.api.CalDavRequestManager;
import de.svws_nrw.davapi.api.DavRequestManager;
import de.svws_nrw.davapi.api.PROPFIND;
import de.svws_nrw.davapi.api.REPORT;
import de.svws_nrw.davapi.model.dav.Error;
import de.svws_nrw.davapi.model.dav.Multistatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Die Klasse spezifiziert die CardDAV-API-Schnittstelle für den Zugriff auf
 * Kalender und Kalendereinträge.
 */
@Path("/dav/{schema}")
@Tag(name = "Server")
public class APIKalender {

	// TODO Schuljahresabschnitt als URI param ergänzen und so auch Kalender für mehrere Halbjahre zur Verfügung stellen

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIKalender() {
		// leer
	}

	/**
	 * Die CardDAV-API Methode zur Abfrage von Eigenschaften der
	 * Ressource-Collection Kalender
	 *
	 * @param schema  Das Datenbankschema, auf welches die Abfrage ausgeführt werden
	 *                soll
	 * @param request Die Informationen zur HTTP-Anfrage
	 * @return Ergebnisobjekt vom Typ {@link Multistatus} oder {@link Error}
	 */
	@PROPFIND
	@Path("/kalender")
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response caldavPropfindAllCalendars(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DavRequestManager.handle(conn -> new CalDavRequestManager(conn, request.getInputStream()).propfindCollection(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.CALDAV_NUTZEN,
				BenutzerKompetenz.CALDAV_KALENDER_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.CALDAV_KALENDER_ANSEHEN_FUNKTIONSBEZOGEN);
	}

	/**
	 * Die CardDAV-API Methode zur Abfrage von Eigenschaften der Ressource Kalender
	 *
	 * @param schema     Das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param kalenderId Id des Kalenders, für den die Eigenschaften abfragt werden
	 * @param request    Die Informationen zur HTTP-Anfrage
	 * @return Ergebnisobjekt vom Typ {@link Multistatus} oder {@link Error}
	 */
	@PROPFIND
	@Path("/kalender/{resourceCollectionId}")
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response caldavPropfindCalendar(@PathParam("schema") final String schema, @PathParam("resourceCollectionId") final String kalenderId,
			@Context final HttpServletRequest request) {
		return DavRequestManager.handle(
				conn -> new CalDavRequestManager(conn, request.getInputStream()).propfindCalendar(kalenderId),
				request, ServerMode.STABLE,
				BenutzerKompetenz.CALDAV_NUTZEN,
				BenutzerKompetenz.CALDAV_KALENDER_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.CALDAV_KALENDER_ANSEHEN_FUNKTIONSBEZOGEN);
	}

	/**
	 * Die CardDAV-API Methode zur Abfrage von Informationen der Ressource Kalender
	 *
	 * @param schema     Das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param kalenderId Id des Kalenders, für den die Informationen abfragt werden
	 * @param request    Die Informationen zur HTTP-Anfrage
	 * @return Ergebnisobjekt vom Typ {@link Multistatus} oder
	 *         {@link de.svws_nrw.davapi.model.dav.Error}
	 */
	@REPORT
	@Path("/kalender/{resourceCollectionId}")
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response caldavReportCalendar(@PathParam("schema") final String schema, @PathParam("resourceCollectionId") final String kalenderId,
			@Context final HttpServletRequest request) {
		return DavRequestManager.handle(
				conn -> new CalDavRequestManager(conn, request.getInputStream()).reportCalendar(kalenderId),
				request, ServerMode.STABLE,
				BenutzerKompetenz.CALDAV_NUTZEN,
				BenutzerKompetenz.CALDAV_KALENDER_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.CALDAV_KALENDER_ANSEHEN_FUNKTIONSBEZOGEN);
	}

	/**
	 * Die CardDAV-API Methode zur Anlage eines neuen Kalendereintrags oder zur
	 * Änderung eines bestehenden Kalendereintrags
	 *
	 * @param schema             Das Datenbankschema, auf welches die Abfrage
	 *                           ausgeführt werden soll
	 * @param kalenderId         Id des Kalenders, für den die Informationen abfragt
	 *                           werden
	 * @param request            Die Informationen zur HTTP-Anfrage
	 * @param kalenderEintragUId die UID des Kalendereintrags, auf den geschrieben
	 *                           werden soll
	 * @param ifNonMatchHeader   Header mit ETag für PUT einer neuen Ressource
	 * @param ifMatchHeader      Header mit ETag für PUT auf vorhandene Ressource
	 * @return Ergebnisobjekt vom Typ {@link Multistatus} oder
	 *         {@link de.svws_nrw.davapi.model.dav.Error}
	 */
	@PUT
	@Path("/kalender/{resourceCollectionId}/{resourceId}.ics")
	@Consumes({ "Text/Calendar", MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response caldavPutCalendarEntry(@PathParam("schema") final String schema,
			@PathParam("resourceCollectionId") final String kalenderId, @PathParam("resourceId") final String kalenderEintragUId,
			@HeaderParam("If-None-Match") final String ifNonMatchHeader, @HeaderParam("If-Match") final String ifMatchHeader,
			@Context final HttpServletRequest request) {
		return DavRequestManager.handle(conn -> new CalDavRequestManager(conn, request.getInputStream())
				.putEntry(kalenderId, kalenderEintragUId, ifNonMatchHeader, ifMatchHeader),
				request, ServerMode.STABLE,
				BenutzerKompetenz.CALDAV_NUTZEN,
				BenutzerKompetenz.CALDAV_KALENDER_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.CALDAV_KALENDER_ANSEHEN_FUNKTIONSBEZOGEN);
	}

	/**
	 * API-Methode für Löschen von Kalendereinträgen
	 *
	 * @param schema             das Schema der Datenbank
	 * @param kalenderId         die ID des Kalenders
	 * @param kalenderEintragUID die UID der zu löschenden Ressource
	 * @param ifMatchHeader      der ifMatchHeader, welcher die dem CLient bekannte
	 *                           Version angibt
	 * @param request            die Informationen zur HTTP-Anfrage
	 * @return den Status 204 NO CONTENT bei erfolgreichem Löschen, ansonsten ein
	 *         Multistatus mit Fehlermeldung
	 */
	@DELETE
	@Path("/kalender/{resourceCollectionId}/{resourceId}.ics")
	@Produces(MediaType.TEXT_XML)
	public Response caldavDeleteCalendarEntry(@PathParam("schema") final String schema,
			@PathParam("resourceCollectionId") final String kalenderId, @PathParam("resourceId") final String kalenderEintragUID,
			@HeaderParam("If-Match") final String ifMatchHeader, @Context final HttpServletRequest request) {
		return DavRequestManager.handle(
				conn -> new CalDavRequestManager(conn, request.getInputStream()).deleteEntry(kalenderId, kalenderEintragUID, ifMatchHeader),
				request, ServerMode.STABLE,
				BenutzerKompetenz.CALDAV_EIGENER_KALENDER);
	}

	/**
	 * API-Methode zum Löschen einer Kalender-Ressourcensammlung
	 *
	 * @param schema     das Schema der Datenbank
	 * @param kalenderId die ID des zu löschenden Kalenders
	 * @param request    Informationen zum HTTP-Request
	 * @return den Status 204 NO CONTENT bei erfolgtem Löschen, ansonsten ein
	 *         Multistatus mit Fehlermeldungen
	 */
	@DELETE
	@Path("/kalender/{resourceCollectionId}")
	@Produces(MediaType.TEXT_XML)
	public Response caldavDeleteCalendar(@PathParam("schema") final String schema,
			@PathParam("resourceCollectionId") final String kalenderId, @Context final HttpServletRequest request) {
		return DavRequestManager.handle(
				conn -> new CalDavRequestManager(conn, request.getInputStream()).deleteCalendar(kalenderId),
				request, ServerMode.STABLE,
				BenutzerKompetenz.CALDAV_EIGENER_KALENDER);
	}

}
