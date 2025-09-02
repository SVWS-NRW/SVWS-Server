package de.svws_nrw.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

public interface OpenApiDefinition {

	/**
	 * Die Methode liefert einen Titel zur API zurück.
	 *
	 * @return Titel der API
	 */
	String getTitle();

	/**
	 * Die Methode liefert eine Beschreibung zur API zurück.
	 *
	 * @return Beschreibungstext der API
	 */
	String getDescription();

	/**
	 * Die Methode gibt das {@link Info} Objekt zur API zurück.
	 *
	 * @return {@link Info} Objekt
	 */
	Info getInfo();

	/**
	 * Die Methode gibt das {@link OpenAPI} Objekt zur API zurück.
	 *
	 * @return {@link OpenAPI} Objekt
	 */
	OpenAPI getOpenAPI();

	/**
	 * Die Methode gibt das {@link SwaggerConfiguration} Objekt zur API zurück.
	 *
	 * @return {@link SwaggerConfiguration} Objekt
	 */
	SwaggerConfiguration getSwaggerConfiguration();


	/**
	 * Die Methode gibt das {@link OpenApiContext} Objekt der API zurück.
	 *
	 * @return {@link OpenApiContext} Objekt
	 *
	 * @throws OpenApiConfigurationException im Fehlerfall
	 */
	OpenApiContext getOpenApiContext() throws OpenApiConfigurationException;

	/**
	 * Die Methode liefert die OpenAPI-Datei.
	 *
	 * @param type      der Typ der Datei (json oder yaml)
	 *
	 * @return die HTTP-Response mit der json oder yaml - OpenAPI-Datei.
	 *
	 * @throws OpenApiConfigurationException im Fehlerfall
	 * @throws JsonProcessingException im Fehlerfall
	 */
	@Produces({ MediaType.APPLICATION_JSON, "application/yaml" })
	@Operation(hidden = true)
	default Response getOpenApiFile(@PathParam("type") final String type) throws OpenApiConfigurationException, JsonProcessingException {
		final OpenApiContext ctx = getOpenApiContext();
		final OpenAPI oas = ctx.read();
		if (oas == null)
			return Response.status(404).build();

		if (StringUtils.isNotBlank(type) && type.trim().equalsIgnoreCase("yaml")) {
			return Response.status(Response.Status.OK)
					.entity(ctx.getOutputYamlMapper().writer(new DefaultPrettyPrinter()).writeValueAsString(oas))
					.type("application/yaml")
					.build();
		}
		return Response.status(Response.Status.OK)
				.entity(ctx.getOutputJsonMapper().writer(new DefaultPrettyPrinter()).writeValueAsString(oas))
				.type(MediaType.APPLICATION_JSON_TYPE)
				.build();
	}
}
