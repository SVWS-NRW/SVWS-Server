package de.svws_nrw.db.utils;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse stellt Hilfsmethoden zum Erzeugen von HTTP-Responses zur Verfügung
 */
public final class ApiUtils {

	private ApiUtils() {
		// Die Klasse darf nicht instantiiert werden
	}

	/**
	 * Erzeugt für die übergebenen Daten eine HTTP-Response mit den übergebenen Daten
	 * als JSON-Objekt.
	 *
	 * @param <T>     der Typ der Daten
	 * @param daten   die zu serialisierenden Daten
	 *
	 * @return die HTTP-Response
	 */
	public static <T> Response getResponse(final T daten) {
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Erzeugt aus der übergenenen Exception eine HTTP-Response mit einer SimpleResponse,
	 * welche im Log den Stack-Trace der Exception beinhaltet.
	 *
	 * @param e   die Exception
	 *
	 * @return die Response mit der SimpleResponse
	 */
	public static Response getSimpleResponseWithStacktrace(final Exception e) {
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		final SimpleOperationResponse simpleOperationResponse = new SimpleOperationResponse();
		simpleOperationResponse.success = false;
		Status code = Status.INTERNAL_SERVER_ERROR;
		logger.logLn(e.getMessage());
		if (e instanceof final ApiOperationException aoe)
			code = aoe.getStatus();
		final StackTraceElement[] stack = e.getStackTrace();
		for (final StackTraceElement s : stack)
			logger.logLn(2, s.toString());
		simpleOperationResponse.log = log.getStrings();
		return Response.status(code).type(MediaType.APPLICATION_JSON).entity(simpleOperationResponse).build();
	}

}
