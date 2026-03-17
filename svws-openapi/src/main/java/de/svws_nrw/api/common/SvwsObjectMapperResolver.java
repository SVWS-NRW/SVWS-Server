package de.svws_nrw.api.common;

import org.openapitools.jackson.nullable.JsonNullableModule;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

/**
 * Ein Resolver, um einen Object-Mapper für die API bereitzustellen, der mit JsonNullable zurecht kommt.
 */
@Provider
public final class SvwsObjectMapperResolver implements ContextResolver<ObjectMapper> {

	private final ObjectMapper mapper;

	/**
	 * Erzeugt einen neuen ObjectMapper mit den registrierten Modulen und JsonNullable
	 */
	public SvwsObjectMapperResolver() {
		this.mapper = new ObjectMapper();
		this.mapper.findAndRegisterModules();
		this.mapper.registerModule(new JsonNullableModule());
	}

	@Override
	public ObjectMapper getContext(final Class<?> type) {
		return mapper;
	}

}
