package de.svws_nrw.api.dav;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.davapi.api.CalDavRequestManager;
import de.svws_nrw.davapi.api.CardDavRequestManager;
import de.svws_nrw.davapi.api.DavRequestManager;
import de.svws_nrw.davapi.api.AbstractDavRequestManager;
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
 * Die Klasse spezifiziert die DAV-API-Schnittstelle des SVWS-Servers.
 * Diese enthält die CardDAV-Endpunkte (Adressbücher) und die CalDAV-Endpunkte (Kalender und Kalendereinträge).
 */
@Path("/dav/{schema}")
@Tag(name = "Server")
public class APIDav {

	// TODO Schuljahresabschnitt als URI param ergänzen und so auch Kalender für mehrere Halbjahre zur Verfügung stellen

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIDav() {
		// leer
	}


	/**
	 * Die CardDAV-API Methode zur Abfrage von Eigenschaften der Root-Ressource.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit einem Ergebnis vom Typ {@link Multistatus} oder {@link de.svws_nrw.davapi.model.dav.Error}
	 */
	@PROPFIND
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response davRoot(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return AbstractDavRequestManager.handle(
				conn -> new DavRequestManager(conn, request.getInputStream()).propfindRootCollection(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.CARDDAV_NUTZEN, BenutzerKompetenz.CALDAV_NUTZEN);
	}


	/**
	 * Die CardDAV-API Methode zur Abfrage von Eigenschaften eines Benutzer-Principals
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idUser    die ID des Benutzer-Principals
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit einem Ergebnis vom Typ {@link Multistatus} oder {@link de.svws_nrw.davapi.model.dav.Error}
	 */
	@PROPFIND
	@Path("/benutzer/{idUser}")
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response davPrincipal(@PathParam("schema") final String schema, @PathParam("idUser") final String idUser,
			@Context final HttpServletRequest request) {
		return AbstractDavRequestManager.handle(
				conn -> new DavRequestManager(conn, request.getInputStream()).propfindUserPrincipalCollection(idUser),
				request, ServerMode.STABLE,
				BenutzerKompetenz.CARDDAV_NUTZEN);
	}


	/**
	 * Die CardDAV-API Methode zur Abfrage von Eigenschaften zu der Kalendersammlung des
	 * Benutzers, der die Anfrage stellt.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit einem Ergebnis vom Typ {@link Multistatus} oder {@link Error}
	 */
	@PROPFIND
	@Path("/kalender")
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response caldavPropfindAllCalendars(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return AbstractDavRequestManager.handle(conn -> new CalDavRequestManager(conn, request.getInputStream()).propfindCollection(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.CALDAV_NUTZEN,
				BenutzerKompetenz.CALDAV_KALENDER_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.CALDAV_KALENDER_ANSEHEN_FUNKTIONSBEZOGEN);
	}


	/**
	 * Die CardDAV-API Methode zur Abfrage von Eigenschaften der Ressource Kalender
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idCal     die ID des Kalenders, für den die Eigenschaften abfragt werden
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit einem Ergebnis vom Typ {@link Multistatus} oder {@link Error}
	 */
	@PROPFIND
	@Path("/kalender/{idCal}")
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response caldavPropfindCalendar(@PathParam("schema") final String schema, @PathParam("idCal") final String idCal,
			@Context final HttpServletRequest request) {
		return AbstractDavRequestManager.handle(
				conn -> new CalDavRequestManager(conn, request.getInputStream()).propfindCalendar(idCal),
				request, ServerMode.STABLE,
				BenutzerKompetenz.CALDAV_NUTZEN,
				BenutzerKompetenz.CALDAV_KALENDER_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.CALDAV_KALENDER_ANSEHEN_FUNKTIONSBEZOGEN);
	}


	/**
	 * Die CardDAV-API Methode zur Abfrage von Informationen der Ressource Kalender
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idCal     die ID des Kalenders, für den die Informationen abfragt werden
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit einem Ergebnis vom Typ {@link Multistatus} oder {@link de.svws_nrw.davapi.model.dav.Error}
	 */
	@REPORT
	@Path("/kalender/{idCal}")
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response caldavReportCalendar(@PathParam("schema") final String schema, @PathParam("idCal") final String idCal,
			@Context final HttpServletRequest request) {
		return AbstractDavRequestManager.handle(
				conn -> new CalDavRequestManager(conn, request.getInputStream()).reportCalendar(idCal),
				request, ServerMode.STABLE,
				BenutzerKompetenz.CALDAV_NUTZEN,
				BenutzerKompetenz.CALDAV_KALENDER_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.CALDAV_KALENDER_ANSEHEN_FUNKTIONSBEZOGEN);
	}


	/**
	 * Die CardDAV-API Methode zur Anlage eines neuen Kalendereintrags oder zur
	 * Änderung eines bestehenden Kalendereintrags
	 *
	 * @param schema             das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idCal              die ID des Kalenders
	 * @param idEntry            die UID des Kalendereintrags, auf den geschrieben werden soll
	 * @param ifNonMatchHeader   der Header mit dem ETag für PUT einer neuen Ressource
	 * @param ifMatchHeader      der Header mit dem ETag für PUT auf vorhandene Ressource
	 * @param request            die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit einem Ergebnis vom Typ {@link Multistatus} oder {@link de.svws_nrw.davapi.model.dav.Error}
	 */
	@PUT
	@Path("/kalender/{idCal}/{idEntry}.ics")
	@Consumes({ "Text/Calendar", MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response caldavPutCalendarEntry(@PathParam("schema") final String schema, @PathParam("idCal") final String idCal,
			@PathParam("idEntry") final String idEntry, @HeaderParam("If-None-Match") final String ifNonMatchHeader,
			@HeaderParam("If-Match") final String ifMatchHeader, @Context final HttpServletRequest request) {
		return AbstractDavRequestManager.handle(conn -> new CalDavRequestManager(conn, request.getInputStream())
				.putEntry(idCal, idEntry, ifNonMatchHeader, ifMatchHeader),
				request, ServerMode.STABLE,
				BenutzerKompetenz.CALDAV_NUTZEN,
				BenutzerKompetenz.CALDAV_KALENDER_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.CALDAV_KALENDER_ANSEHEN_FUNKTIONSBEZOGEN);
	}


	/**
	 * API-Methode für Löschen von Kalendereinträgen
	 *
	 * @param schema          das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idCal           die ID des Kalenders
	 * @param idEntry         die UID der zu löschenden Ressource
	 * @param ifMatchHeader   der Header, welcher die dem CLient bekannte Version angibt
	 * @param request         die Informationen zur HTTP-Anfrage
	 *
	 * @return den Status 204 NO CONTENT bei erfolgreichem Löschen, ansonsten ein Multistatus mit Fehlermeldung
	 */
	@DELETE
	@Path("/kalender/{idCal}/{idEntry}.ics")
	@Produces(MediaType.TEXT_XML)
	public Response caldavDeleteCalendarEntry(@PathParam("schema") final String schema, @PathParam("idCal") final String idCal,
			@PathParam("idEntry") final String idEntry, @HeaderParam("If-Match") final String ifMatchHeader,
			@Context final HttpServletRequest request) {
		return AbstractDavRequestManager.handle(
				conn -> new CalDavRequestManager(conn, request.getInputStream()).deleteEntry(idCal, idEntry, ifMatchHeader),
				request, ServerMode.STABLE,
				BenutzerKompetenz.CALDAV_EIGENER_KALENDER);
	}


	/**
	 * API-Methode zum Löschen einer Kalender-Ressourcensammlung
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idCal     die ID des zu löschenden Kalenders
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return den Status 204 NO CONTENT bei erfolgtem Löschen, ansonsten ein Multistatus mit Fehlermeldungen
	 */
	@DELETE
	@Path("/kalender/{idCal}")
	@Produces(MediaType.TEXT_XML)
	public Response caldavDeleteCalendar(@PathParam("schema") final String schema, @PathParam("idCal") final String idCal,
			@Context final HttpServletRequest request) {
		return AbstractDavRequestManager.handle(
				conn -> new CalDavRequestManager(conn, request.getInputStream()).deleteCalendar(idCal),
				request, ServerMode.STABLE,
				BenutzerKompetenz.CALDAV_EIGENER_KALENDER);
	}


	/**
	 * Die CardDAV-API Methode zur Abfrage von Eigenschaften der Ressource-Colection
	 * Adressbuecher
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit einem Ergebnis vom Typ {@link Multistatus} oder {@link de.svws_nrw.davapi.model.dav.Error}
	 */
	@PROPFIND
	@Path("/adressbuecher")
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response carddavPropfindAllAddressbooks(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return AbstractDavRequestManager.handle(
				conn -> new CardDavRequestManager(conn, request.getInputStream()).propfindCollection(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.CARDDAV_NUTZEN);
	}


	/**
	 * Die CardDAV-API Methode zur Abfrage von Eigenschaften eines Adressbuches
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idBook    die ID des Adressbuches, für welches die Eigenschaften abfragt werden
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit einem Ergebnis vom Typ {@link Multistatus} oder {@link de.svws_nrw.davapi.model.dav.Error}
	 */
	@PROPFIND
	@Path("/adressbuecher/{idBook}")
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response carddavPropfindAddressbook(@PathParam("schema") final String schema, @PathParam("idBook") final String idBook,
			@Context final HttpServletRequest request) {
		return AbstractDavRequestManager.handle(
				conn -> new CardDavRequestManager(conn, request.getInputStream()).propfindAddressbook(idBook),
				request, ServerMode.STABLE,
				BenutzerKompetenz.CARDDAV_NUTZEN);
	}


	/**
	 * Die CardDAV-API Methode zur Abfrage von Informationen eines Adressbuches
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idBook    die ID des Adressbuches, für welches die Informationen abfragt werden
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit einem Ergebnis vom Typ {@link Multistatus} oder {@link de.svws_nrw.davapi.model.dav.Error}
	 */
	@REPORT
	@Path("/adressbuecher/{idBook}")
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response carddavReportAddressbook(@PathParam("schema") final String schema, @PathParam("idBook") final String idBook,
			@Context final HttpServletRequest request) {
		return AbstractDavRequestManager.handle(
				conn -> new CardDavRequestManager(conn, request.getInputStream()).reportAddressbook(idBook),
				request, ServerMode.STABLE,
				BenutzerKompetenz.CARDDAV_NUTZEN);
	}


	/**
	 * Die CardDAV-API Methode zur Abfrage von Informationen der Ressource Kontakt
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idBook    die ID des Adressbuches
	 * @param idEntry   die ID des Kontaktes, wessen Informationen angefragt werden sollen
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit einem Ergebnis vom Typ {@link Multistatus} oder {@link de.svws_nrw.davapi.model.dav.Error}
	 */
	@REPORT
	@Path("/adressbuecher/{idBook}/{idEntry}.vcf")
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response carddavReportContact(@PathParam("schema") final String schema, @PathParam("idBook") final String idBook,
			@PathParam("idEntry") final String idEntry, @Context final HttpServletRequest request) {
		return AbstractDavRequestManager.handle(
				conn -> new CardDavRequestManager(conn, request.getInputStream()).reportContact(idBook, idEntry),
				request, ServerMode.STABLE,
				BenutzerKompetenz.CARDDAV_NUTZEN);
	}

}
