package de.svws_nrw.api.common;

import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Ein Exception-Mapper für die ApiOperationException, so dass Try-Catch-Blöcke vermieden werden können.
 */
@Provider
public final class ApiOperationExceptionMapper implements ExceptionMapper<ApiOperationException> {

	@Override
	public Response toResponse(final ApiOperationException exception) {
		return exception.getResponse();
	}

}
