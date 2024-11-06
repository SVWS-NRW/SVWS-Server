package de.svws_nrw.api;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.BaseOpenApiResource;
import io.swagger.v3.oas.annotations.Operation;
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
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

/**
 * Stellt die OpenAPI-Schnittstelle für die Schema-Root-Applikation zur Verfügung
 */
@Path("")
public class OpenApiSchemaRoot extends BaseOpenApiResource {

	/** Die Servlet-Konfiguration */
	@Context
	ServletConfig config;

	/** Die Rest-Applikation */
	@Context
	Application app;

	/** Die Beschreibung der Open-API-Schnittstelle als HTML-Code */
	private static final String description =
			"""
			Die Open-API-Schnittstellenbeschreibungen des SVWS-Servers: <br>
			<ul>
			  <li> <a href="/debug/server/index.html"> API SVWS-Server </a> </li>
			  <li> <a href="/debug/privileged/index.html"> API SVWS-Server - Schemaverwaltung </a> </li>
			</ul>
			""";

	private final Info info = new Info()
			.title("SVWS Open-API Datenbank-Schema (root)")
			.version(SVWSVersion.version())
			.description(description)
			.license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0.html"));

	private final OpenAPI openApi = new OpenAPI()
			.info(info)
			.components(new Components()
					.addSecuritySchemes("basicAuth", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
			.addSecurityItem(new SecurityRequirement().addList("basicAuth"));

	private final SwaggerConfiguration openApiConfig = new SwaggerConfiguration()
			.openAPI(openApi)
			.prettyPrint(true)
			.resourcePackages(Stream.of("de.svws_nrw.api.schema").collect(Collectors.toSet()));

	/**
	 * Leerer Standardkonstruktor.
	 */
	public OpenApiSchemaRoot() {
		// leer
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private OpenApiContext getOpenApiContext() throws OpenApiConfigurationException {
		return new JaxrsOpenApiContextBuilder()
				.servletConfig(config)
				.application(app)
				.resourcePackages(resourcePackages)
				.configLocation(configLocation)
				.openApiConfiguration(openApiConfig)
				.ctxId(this.getClass().getCanonicalName())
				.buildContext(true);
	}


	/**
	 * Stellt die Open-API-Datei zur Verfügung.
	 *
	 * @param headers   die HTTP-Header
	 * @param uriInfo   die URI-Informationen
	 * @param type      der type der Datei (json oder yaml)
	 *
	 * @return die HTTP-Response mit der json oder yaml - Datei.
	 *
	 * @throws Exception im Fehlerfall
	 */
	@GET
	@Path("/openapi/privileged.{type:json|yaml}")
	@Produces({ MediaType.APPLICATION_JSON, "application/yaml" })
	@Operation(hidden = true)
	public Response getOpenApi(@Context final HttpHeaders headers,
			@Context final UriInfo uriInfo,
			@PathParam("type") final String type) throws Exception {
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
