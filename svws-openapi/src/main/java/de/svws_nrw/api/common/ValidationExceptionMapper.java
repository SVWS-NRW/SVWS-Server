package de.svws_nrw.api.common;

import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Wandelt die Exception bei einer Constraint-Validation in eine Bad-Request-Response um.
 */
@Provider
public final class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

	@Override
	public Response toResponse(final ConstraintViolationException exception) {
		final String details = exception.getConstraintViolations().stream()
				.map(v -> v.getMessage())
				.collect(Collectors.joining(" "));
		return Response.status(Status.BAD_REQUEST).entity("Validierungsfehler: " + details).type("application/json").build();
	}

}
