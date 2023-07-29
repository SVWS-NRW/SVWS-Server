package de.svws_nrw.api.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import de.svws_nrw.api.OpenAPIApplication;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.davapi.api.DavExtendedHttpStatus;
import de.svws_nrw.davapi.api.DavUriBuilder;
import de.svws_nrw.davapi.api.DavUriParameter;
import de.svws_nrw.davapi.api.PROPFIND;
import de.svws_nrw.davapi.api.PropfindAddressbookDispatcher;
import de.svws_nrw.davapi.api.PropfindDavRootDispatcher;
import de.svws_nrw.davapi.api.PropfindPrincipalDispatcher;
import de.svws_nrw.davapi.api.REPORT;
import de.svws_nrw.davapi.api.ReportAddressbookDispatcher;
import de.svws_nrw.davapi.data.IAdressbuchRepository;
import de.svws_nrw.davapi.data.repos.bycategory.AdressbuchWithCategoriesRepository;
import de.svws_nrw.davapi.model.dav.Error;
import de.svws_nrw.davapi.model.dav.Multistatus;
import de.svws_nrw.db.DBEntityManager;
import io.swagger.v3.oas.annotations.tags.Tag;
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
	 * Logger für diese Klasse
	 */
	private static final Logger logger = createLogger();

	/**
	 * Die CardDAV-API Methode zur Abfrage von Eigenschaften der Root-Ressource.
	 *
	 * @param schema  Das Datenbankschema, auf welches die Abfrage ausgeführt werden
	 *                soll
	 * @param request Die Informationen zur HTTP-Anfrage
	 * @return Ergebnisobjekt vom Typ {@link Multistatus} oder
	 *         {@link de.svws_nrw.davapi.model.dav.Error}
	 */
	@PROPFIND
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response propfindOnRoot(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.ADRESSDATEN_ANSEHEN);
				InputStream inputStream = getInputStream(request)) {
			final PropfindDavRootDispatcher dispatcher = createPropfindDavRootDispatcher(conn);
			final Object result = dispatcher.dispatchCollection(inputStream);
			return buildResponse(result);
		} catch (final IOException e) {
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
	 *         {@link de.svws_nrw.davapi.model.dav.Error}
	 */
	@PROPFIND
	@Path(DavUriBuilder.DAV_REL_URI_PATTERN_PRINCIPAL)
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response propfindOnPrincipal(@PathParam("schema") final String schema, @PathParam("benutzerId") final String benutzerId,
			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.ADRESSDATEN_ANSEHEN);
				InputStream inputStream = getInputStream(request, "benutzerId=+benutzerId")) {
			final PropfindPrincipalDispatcher dispatcher = createPropfindPrincipalDispatcher(conn);
			final Object result = dispatcher.dispatch(inputStream, benutzerId);
			return buildResponse(result);
		} catch (final IOException e) {
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
	 *         {@link de.svws_nrw.davapi.model.dav.Error}
	 */
	@PROPFIND
	@Path(DavUriBuilder.CARD_DAV_REL_URI_PATTERN_ADDRESSBOOK_COLLECTION)
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response propfindOnAddressbooks(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.ADRESSDATEN_ANSEHEN);
				InputStream inputStream = getInputStream(request)) {
			final PropfindAddressbookDispatcher dispatcher = createPropfindAddressbookDispatcher(conn);
			final Object result = dispatcher.dispatch(inputStream, "");
			return buildResponse(result);
		} catch (final IOException e) {
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
	 *         {@link de.svws_nrw.davapi.model.dav.Error}
	 */
	@PROPFIND
	@Path(DavUriBuilder.CARD_DAV_REL_URI_PATTERN_ADDRESSBOOK)
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response propfindOnAddressbook(@PathParam("schema") final String schema,
			@PathParam("resourceCollectionId") final String adressbuchId, @Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.ADRESSDATEN_ANSEHEN);
				InputStream inputStream = getInputStream(request, "adressbuchId=" + adressbuchId)) {
			final PropfindAddressbookDispatcher dispatcher = createPropfindAddressbookDispatcher(conn);
			final Object result = dispatcher.dispatch(inputStream, adressbuchId);
			return buildResponse(result);
		} catch (final IOException e) {
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
	 *         {@link de.svws_nrw.davapi.model.dav.Error}
	 */
	@REPORT
	@Path(DavUriBuilder.CARD_DAV_REL_URI_PATTERN_ADDRESSBOOK)
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response reportOnAddressbook(@PathParam("schema") final String schema,
			@PathParam("resourceCollectionId") final String adressbuchId, @Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.ADRESSDATEN_ANSEHEN);
				InputStream inputStream = getInputStream(request, "adressbuchId=" + adressbuchId)) {
			final ReportAddressbookDispatcher dispatcher = createReportAddressbookDispatcher(conn);
			final Object result = dispatcher.dispatch(inputStream, adressbuchId);
			return buildResponse(result);
		} catch (final IOException e) {
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
	 *         {@link de.svws_nrw.davapi.model.dav.Error}
	 */
	@REPORT
	@Path(DavUriBuilder.CARD_DAV_REL_URI_PATTERN_ADDRESS_ENTRY)
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_XML)
	public Response reportOnContact(@PathParam("schema") final String schema,
			@PathParam("resourceCollectionId") final String adressbuchId, @PathParam("resourceId") final String kontaktId,
			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.ADRESSDATEN_ANSEHEN);
				InputStream inputStream = getInputStream(request, "adressbuchId=" + adressbuchId, "kontaktId=" + kontaktId)) {
			final ReportAddressbookDispatcher dispatcher = createReportAddressbookDispatcher(conn);
			final Object result = dispatcher.dispatch(inputStream, adressbuchId);
			return buildResponse(result);
		} catch (final IOException e) {
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
	private static Response buildResponse(final Object result) {
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
	 *
	 * @return die HTTP-Response
	 */
	private static Response buildBadRequest(final IOException e) {
		return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
	}

	/**
	 * Erzeugt einen PropfindDavRootDispatcher mit der gegebenen
	 * Datenbankverbindung.
	 *
	 * @param conn die Datenbankverbindung
	 * @return ein parametrierter Dispatcher
	 */
	private static PropfindDavRootDispatcher createPropfindDavRootDispatcher(final DBEntityManager conn) {
		final DavUriParameter uriParameter = new DavUriParameter();
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
	private static ReportAddressbookDispatcher createReportAddressbookDispatcher(final DBEntityManager conn) {
		final IAdressbuchRepository adressbuchRepository = createAdressbuchRepository(conn);
		final DavUriParameter uriParameter = new DavUriParameter();
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
	private static PropfindAddressbookDispatcher createPropfindAddressbookDispatcher(final DBEntityManager conn) {
		final IAdressbuchRepository repository = createAdressbuchRepository(conn);
		final DavUriParameter uriParameter = new DavUriParameter();
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
	private static PropfindPrincipalDispatcher createPropfindPrincipalDispatcher(final DBEntityManager conn) {
		final DavUriParameter uriParameter = new DavUriParameter();
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
	private static IAdressbuchRepository createAdressbuchRepository(final DBEntityManager conn) {
		return new AdressbuchWithCategoriesRepository(conn);
	}

	/**
	 * Debug Option, damit Requests nach Insomnia übertragen werden können
	 */
	private static final boolean LOG_INPUTSTREAM = false;

	/**
	 * Loggt abhängig von {@link #LOG_INPUTSTREAM} den Informationen sowie
	 * Inputstream des Requests
	 *
	 * @param request   der request
	 * @param params    string
	 *
	 * @return ein ungelesener Inputstream des Requests
	 *
	 * @throws IOException
	 */
	private static InputStream getInputStream(final HttpServletRequest request, final String... params) throws IOException {
		InputStream result = request.getInputStream();
		if (LOG_INPUTSTREAM) {
			final String methodName = getApiMethodName(2);
			logger.log(LogLevel.WARNING, methodName);
			for (final String s: params)
				logger.log(LogLevel.WARNING, s);
			final String input = new String(result.readAllBytes(), StandardCharsets.UTF_8);
			logger.log(methodName + "\n");
			logger.log(LogLevel.WARNING, request.toString());
			logger.log(LogLevel.WARNING, input);
			result = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
		}
		return result;
	}

	private static String getApiMethodName(final long n) {
		final StackWalker walker = StackWalker.getInstance();
	    final Optional<String> methodName = walker.walk(frames -> frames
	    	      .skip(n).findFirst()
	    	      .map(StackWalker.StackFrame::getMethodName));
	    return methodName.get();
	}

	private static Logger createLogger() {
		final Logger logger = new Logger();
		logger.addConsumer(new LogConsumerConsole(true, false));
		return logger;
	}
}
