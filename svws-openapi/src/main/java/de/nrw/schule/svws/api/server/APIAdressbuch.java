package de.nrw.schule.svws.api.server;

import java.io.IOException;

import de.nrw.schule.svws.api.OpenAPIApplication;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.davapi.api.DavExtendedHttpStatus;
import de.nrw.schule.svws.davapi.api.DavUriBuilder;
import de.nrw.schule.svws.davapi.api.DavUriParameter;
import de.nrw.schule.svws.davapi.api.PROPFIND;
import de.nrw.schule.svws.davapi.api.PropfindAddressbookDispatcher;
import de.nrw.schule.svws.davapi.api.PropfindDavRootDispatcher;
import de.nrw.schule.svws.davapi.api.PropfindPrincipalDispatcher;
import de.nrw.schule.svws.davapi.api.REPORT;
import de.nrw.schule.svws.davapi.api.ReportAddressbookDispatcher;
import de.nrw.schule.svws.davapi.data.IAdressbuchRepository;
import de.nrw.schule.svws.davapi.data.repos.bycategory.AdressbuchWithCategoriesRepository;
import de.nrw.schule.svws.davapi.model.dav.Error;
import de.nrw.schule.svws.davapi.model.dav.Multistatus;
import de.nrw.schule.svws.db.DBEntityManager;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Die Klasse spezifiziert die CardDAV-API-Schnittstelle für den Zugriff auf
 * Adressbücher und Kontakte.
 */
@Path(DavUriBuilder.DAV_BASE_URI_PATTERN)
@Tag(name = "Server")
public class APIAdressbuch {

	/**
	 * Die CardDAV-API Methode zur Abfrage von Eigenschaften der Root-Ressource.
	 * 
	 * @param schema  Das Datenbankschema, auf welches die Abfrage ausgeführt werden
	 *                soll
	 * @param request Die Informationen zur HTTP-Anfrage
	 * @return Ergebnisobjekt vom Typ {@link Multistatus} oder
	 *         {@link de.nrw.schule.svws.davapi.model.dav.Error}
	 */
	@PROPFIND
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response propfindOnRoot(@PathParam("schema") String schema, @Context HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADRESSDATEN_ANSEHEN);
				ServletInputStream inputStream = request.getInputStream()) {
			PropfindDavRootDispatcher dispatcher = createPropfindDavRootDispatcher(conn);
			Object result = dispatcher.dispatchCollection(inputStream);
			return buildResponse(result);
		} catch (IOException e) {
			e.printStackTrace();
			return buildBadRequest(e);
		}
	}

	/**
	 * Die CardDAV-API Methode zur Abfrage von Eigenschaften der Ressource Principal
	 * (Benutzer)
	 *
	 * @param schema     Das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param benutzerId Id des Principals (Benutzer), für den die Eigenschaften
	 *                   abfragt werden
	 * @param request    Die Informationen zur HTTP-Anfrage
	 * @return Ergebnisobjekt vom Typ {@link Multistatus} oder
	 *         {@link de.nrw.schule.svws.davapi.model.dav.Error}
	 */
	@PROPFIND
	@Path(DavUriBuilder.DAV_REL_URI_PATTERN_PRINCIPAL)
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response propfindOnPrincipal(@PathParam("schema") String schema, @PathParam("benutzerId") String benutzerId,
			@Context HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADRESSDATEN_ANSEHEN);
				ServletInputStream inputStream = request.getInputStream()) {
			PropfindPrincipalDispatcher dispatcher = createPropfindPrincipalDispatcher(conn);
			Object result = dispatcher.dispatch(inputStream, benutzerId);
			return buildResponse(result);
		} catch (IOException e) {
			e.printStackTrace();
			return buildBadRequest(e);
		}
	}

	/**
	 * Die CardDAV-API Methode zur Abfrage von Eigenschaften der Ressource-Colection
	 * Adressbuecher
	 *
	 * @param schema  Das Datenbankschema, auf welches die Abfrage ausgeführt werden
	 *                soll
	 * @param request Die Informationen zur HTTP-Anfrage
	 * @return Ergebnisobjekt vom Typ {@link Multistatus} oder
	 *         {@link de.nrw.schule.svws.davapi.model.dav.Error}
	 */
	@PROPFIND
	@Path(DavUriBuilder.CARD_DAV_REL_URI_PATTERN_ADDRESSBOOK_COLLECTION)
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response propfindOnAddressbooks(@PathParam("schema") String schema, @Context HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADRESSDATEN_ANSEHEN);
				ServletInputStream inputStream = request.getInputStream()) {
			PropfindAddressbookDispatcher dispatcher = createPropfindAddressbookDispatcher(conn);
			Object result = dispatcher.dispatch(inputStream, "");
			return buildResponse(result);
		} catch (IOException e) {
			e.printStackTrace();
			return buildBadRequest(e);
		}
	}

	/**
	 * Die CardDAV-API Methode zur Abfrage von Eigenschaften der Ressource
	 * Adressbuch
	 *
	 * @param schema       Das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                     werden soll
	 * @param adressbuchId Id des Adressbuchs, für den die Eigenschaften abfragt
	 *                     werden
	 * @param request      Die Informationen zur HTTP-Anfrage
	 * @return Ergebnisobjekt vom Typ {@link Multistatus} oder
	 *         {@link de.nrw.schule.svws.davapi.model.dav.Error}
	 */
	@PROPFIND
	@Path(DavUriBuilder.CARD_DAV_REL_URI_PATTERN_ADDRESSBOOK)
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response propfindOnAddressbook(@PathParam("schema") String schema,
			@PathParam("resourceCollectionId") String adressbuchId, @Context HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADRESSDATEN_ANSEHEN);
				ServletInputStream inputStream = request.getInputStream()) {
			PropfindAddressbookDispatcher dispatcher = createPropfindAddressbookDispatcher(conn);
			Object result = dispatcher.dispatch(inputStream, adressbuchId);
			return buildResponse(result);
		} catch (IOException e) {
			e.printStackTrace();
			return buildBadRequest(e);
		}
	}

	/**
	 * Die CardDAV-API Methode zur Abfrage von Informationen der Ressource
	 * Adressbuch
	 *
	 * @param schema       Das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                     werden soll
	 * @param adressbuchId Id des Adressbuchs, für den die Informationen abfragt
	 *                     werden
	 * @param request      Die Informationen zur HTTP-Anfrage
	 * @return Ergebnisobjekt vom Typ {@link Multistatus} oder
	 *         {@link de.nrw.schule.svws.davapi.model.dav.Error}
	 */
	@REPORT
	@Path(DavUriBuilder.CARD_DAV_REL_URI_PATTERN_ADDRESSBOOK)
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response reportOnAddressbook(@PathParam("schema") String schema,
			@PathParam("resourceCollectionId") String adressbuchId, @Context HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADRESSDATEN_ANSEHEN);
				ServletInputStream inputStream = request.getInputStream()) {
			ReportAddressbookDispatcher dispatcher = createReportAddressbookDispatcher(conn);
			Object result = dispatcher.dispatch(inputStream, adressbuchId);
			return buildResponse(result);
		} catch (IOException e) {
			e.printStackTrace();
			return buildBadRequest(e);
		}
	}

	/**
	 * Die CardDAV-API Methode zur Abfrage von Informationen der Ressource Kontakt
	 *
	 * @param schema       Das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                     werden soll
	 * @param adressbuchId Id des Adressbuchs, für den die Informationen abfragt
	 *                     werden
	 * @param kontaktId    die ID des angefragten Kontakts
	 * @param request      Die Informationen zur HTTP-Anfrage
	 * @return Ergebnisobjekt vom Typ {@link Multistatus} oder
	 *         {@link de.nrw.schule.svws.davapi.model.dav.Error}
	 */
	@REPORT
	@Path(DavUriBuilder.CARD_DAV_REL_URI_PATTERN_ADDRESS_ENTRY)
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response reportOnContact(@PathParam("schema") String schema,
			@PathParam("resourceCollectionId") String adressbuchId, @PathParam("resourceId") String kontaktId,
			@Context HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADRESSDATEN_ANSEHEN);
				ServletInputStream inputStream = request.getInputStream()) {
			ReportAddressbookDispatcher dispatcher = createReportAddressbookDispatcher(conn);
			Object result = dispatcher.dispatch(inputStream, adressbuchId);
			return buildResponse(result);
		} catch (IOException e) {
			e.printStackTrace();
			return buildBadRequest(e);
		}
	}

	/**
	 * Generiert ein Response-Objekt in Abhängigkeit des Typs der zurückzugebenden
	 * Objektklasse.
	 *
	 * @param result Objektklasse, entspricht dem Ergebnis der Anfrage. In
	 *               Abhängigkeit des Typs (z.B. {@link Multistatus}, {@link Error})
	 *               wird das Response-Objekt inkl. passendem HTTP-Status-Code
	 *               generiert.
	 * @return Response Objekt
	 */
	private static Response buildResponse(Object result) {
		if (result instanceof Multistatus) {
			return Response.status(DavExtendedHttpStatus.MULTISTATUS).type(MediaType.TEXT_XML).entity(result).build();
		} else if (result instanceof Error) {
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_XML).entity(result).build();
		} else {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).build();
		}
	}

	/**
	 * Generiert ein Response-Objekt mit dem Status BAD_REQUEST und der gegebenen
	 * ErrorMessage
	 *
	 * @param e the Exception
	 * @return
	 */
	private static Response buildBadRequest(IOException e) {
		return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
	}

	/**
	 * Erzeugt einen PropfindDavRootDispatcher mit der gegebenen
	 * Datenbankverbindung.
	 *
	 * @param conn die Datenbankverbindung
	 * @return ein parametrierter Dispatcher
	 */
	private static PropfindDavRootDispatcher createPropfindDavRootDispatcher(DBEntityManager conn) {
		DavUriParameter uriParameter = new DavUriParameter();
		uriParameter.setSchema(conn.getDBSchema());
		uriParameter.setBenutzerId(String.valueOf(conn.getUser().getId()));
		return new PropfindDavRootDispatcher(uriParameter);
	}

	/**
	 * Erzeugt einen ReportAddressbookDispatcher mit der gegebenen
	 * Datenbankverbindung
	 *
	 * @param conn die Datenbankverbindung
	 * @return ein parametrierter Dispatcher
	 */
	private static ReportAddressbookDispatcher createReportAddressbookDispatcher(DBEntityManager conn) {
		IAdressbuchRepository adressbuchRepository = createAdressbuchRepository(conn);
		DavUriParameter uriParameter = new DavUriParameter();
		uriParameter.setSchema(conn.getDBSchema());
		uriParameter.setBenutzerId(String.valueOf(conn.getUser().getId()));
		return new ReportAddressbookDispatcher(adressbuchRepository, uriParameter);
	}

	/**
	 * Erzeugt einen PropfindAddressbookDispatcher mit der gegebenen
	 * Datenbankverbindung.
	 *
	 * @param conn die Datenbankverbindung
	 * @return ein parametrierter Dispatcher
	 */
	private static PropfindAddressbookDispatcher createPropfindAddressbookDispatcher(DBEntityManager conn) {
		IAdressbuchRepository repository = createAdressbuchRepository(conn);
		DavUriParameter uriParameter = new DavUriParameter();
		uriParameter.setSchema(conn.getDBSchema());
		uriParameter.setBenutzerId(String.valueOf(conn.getUser().getId()));
		return new PropfindAddressbookDispatcher(repository, uriParameter);
	}

	/**
	 * Erzeugt einen PropfindPrincipalDispatcher mit der gegebenen
	 * Datenbankverbindung.
	 *
	 * @param conn die Datenbankverbindung
	 * @return ein parametrierter Dispatcher
	 */
	private static PropfindPrincipalDispatcher createPropfindPrincipalDispatcher(DBEntityManager conn) {
		DavUriParameter uriParameter = new DavUriParameter();
		uriParameter.setSchema(conn.getDBSchema());
		uriParameter.setBenutzerId(String.valueOf(conn.getUser().getId()));
		return new PropfindPrincipalDispatcher(uriParameter);
	}

	/**
	 * Erzeugt ein neues {@link IAdressbuchRepository} mit der gegebenen
	 * Datenbankverbindung.
	 *
	 * @param conn die Datenbankverbindung
	 * @return eine Implementierung von {@link IAdressbuchRepository} für den
	 *         Zugriff auf Adressbuecher.
	 */
	private static IAdressbuchRepository createAdressbuchRepository(DBEntityManager conn) {
		return new AdressbuchWithCategoriesRepository(conn);
	}
}
