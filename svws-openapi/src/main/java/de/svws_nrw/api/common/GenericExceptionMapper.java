package de.svws_nrw.api.common;

import java.io.PrintWriter;
import java.io.StringWriter;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Ein Exception-Mapper für Unerwartetet Exceptions
 */
@Provider
public final class GenericExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Logger logger = Logger.global();

	@Override
	public Response toResponse(final Throwable e) {
		// Logge den unerwarteten Fehler im Server-Log
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		logger.logLn(LogLevel.ERROR, "Unerwarteter Serverfehler: " + e.getMessage() + System.lineSeparator() + sw.toString());

		// Erstelle eine allgemeine 500er-Response ohne Detail-Informationen, die ggf. sensible Daten beinhalten könnten
		return Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity("Es ist ein interner Serverfehler aufgetreten. Weitere Informationen finden sie im Log des SVWS-Servers.")
				.type("text/plain")
				.build();
	}

}
