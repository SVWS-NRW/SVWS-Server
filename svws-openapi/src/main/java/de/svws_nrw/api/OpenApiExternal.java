package de.svws_nrw.api;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;

import de.svws_nrw.api.common.SVWSVersion;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.BaseOpenApiResource;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.servlet.ServletConfig;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

/**
 * Stellt die OpenAPI-Schnittstelle für die Extern angebotene API zur Verfügung
 */
@Path("")
public class OpenApiExternal extends BaseOpenApiResource implements OpenApiDefinition {

	/** Die Servlet-Konfiguration */
	@Context
	ServletConfig config;

	/** Die Rest-Applikation */
	@Context
	Application app;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public OpenApiExternal() {
		// leer
	}

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
	@GET
	@Path("/openapi/external.{type:json|yaml}")
	@Override
	public Response getOpenApiFile(@PathParam("type") final String type) throws OpenApiConfigurationException, JsonProcessingException {
		return OpenApiDefinition.super.getOpenApiFile(type);
	}

	/**
	 * Die Methode liefert einen Titel zur API zurück.
	 *
	 * @return Titel der API
	 */
	@Override
	public String getTitle() {
		return "SVWS Open-API Extern";
	}

	/**
	 * Die Methode liefert eine Beschreibung zur API zurück.
	 *
	 * @return Beschreibungstext der API
	 */
	@Override
	public String getDescription() {
		return "Die Open-API-Schnittstellenbeschreibungen für extern bereitgestellte API Endpunkte des SVWS-Servers.";
	}

	/**
	 * Die Methode gibt das {@link Info} Objekt zur API zurück.
	 *
	 * @return {@link Info} Objekt
	 */
	@Override
	public Info getInfo() {
		return new Info()
				.title(getTitle())
				.version(SVWSVersion.version())
				.description(getDescription())
				.license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html"));
	}

	/**
	 * Die Methode gibt das {@link OpenAPI} Objekt zur API zurück.
	 *
	 * @return {@link OpenAPI} Objekt
	 */
	@Override
	public OpenAPI getOpenAPI() {
		return new OpenAPI()
				.info(getInfo())
				.components(new Components()
						.addSecuritySchemes("basicAuth", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
				.addSecurityItem(new SecurityRequirement().addList("basicAuth"));
	}

	/**
	 * Die Methode gibt das {@link SwaggerConfiguration} Objekt zur API zurück.
	 *
	 * @return {@link SwaggerConfiguration} Objekt
	 */
	@Override
	public SwaggerConfiguration getSwaggerConfiguration() {
		return new SwaggerConfiguration()
				.openAPI(getOpenAPI())
				.prettyPrint(true)
				.resourcePackages(Stream.of("de.svws_nrw.api.external").collect(Collectors.toSet()));
	}

	/**
	 * Die Methode gibt das {@link OpenApiContext} Objekt der API zurück.
	 *
	 * @return {@link OpenApiContext} Objekt
	 *
	 * @throws OpenApiConfigurationException im Fehlerfall
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public OpenApiContext getOpenApiContext() throws OpenApiConfigurationException {
		return new JaxrsOpenApiContextBuilder()
				.servletConfig(config)
				.application(app)
				.resourcePackages(resourcePackages)
				.configLocation(configLocation)
				.openApiConfiguration(getSwaggerConfiguration())
				.ctxId(this.getClass().getCanonicalName())
				.buildContext(true);
	}

}
