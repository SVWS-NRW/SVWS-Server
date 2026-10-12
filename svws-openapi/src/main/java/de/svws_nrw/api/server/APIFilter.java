package de.svws_nrw.api.server;

import de.svws_nrw.controller.filter.FilterControllerFactory;
import de.svws_nrw.core.data.filter.FilterCategoryResponse;
import de.svws_nrw.core.types.filter.FilterDomain;
import de.svws_nrw.db.utils.ApiOperationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für die Filter-Metadaten (Filterkategorien und Filterattribute).
 */
@Path("/db/{schema}/v1/filter")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIFilter {


	/**
	 * Die OpenAPI-Methode für die Abfrage einer Liste mit allen verfügbaren Filterkategorien einer Domäne.
	 *
	 * @param schema Das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param domain Der Schlüssel der Domäne, z. B. {@code schueler}
	 * @param request Der HTTP-Request
	 *
	 * @return Liste mit Filterkategorien
	 */
	@GET
	@Path("{domain}/categories")
	@Operation(
			summary = "Liefert eine Liste mit allen verfügbaren Filterkategorien einer Domäne.",
			description = """
					Liefert eine Liste mit allen verfügbaren Filterkategorien der angegebenen Domäne, z. B. schueler.
					Einer Kategorie können beliebig viele Filterattribute zugeordnet sein.
					"""
	)
	@ApiResponse(
			responseCode = "200",
			description = "Liste mit Filterkategorien der Domäne",
			content = @Content(
					mediaType = "application/json",
					array = @ArraySchema(
							schema = @Schema(
									implementation = FilterCategoryResponse.class
							)
					)
			)
	)
	@ApiResponse(
			responseCode = "403",
			description = "Der Benutzer hat keine Rechte, um die Filterkategorien der Domäne abzurufen."
	)
	@ApiResponse(
			responseCode = "404",
			description = "Es gibt keine Filterdomäne mit dem angegebenen Schlüssel."
	)
	public Response getFilterCategories(@PathParam("schema") final String schema, @PathParam("domain") final String domain,
			@Context final HttpServletRequest request) {
		final FilterDomain filterDomain = getDomain(domain);
		return FilterControllerFactory.withReadAccessByDomain(request, filterDomain)
				.getFilterCategoryController()
				.getByDomain(filterDomain);
	}

	/**
	 * Ermittelt die {@link FilterDomain} zu dem übergebenen Schlüssel. Die Groß- und Kleinschreibung wird dabei nicht beachtet.
	 *
	 * @param domainKey der Schlüssel der Domäne, z. B. {@code schueler}
	 *
	 * @return die {@link FilterDomain}
	 *
	 * @throws ApiOperationException mit dem Status NOT_FOUND, wenn es zu dem Schlüssel keine Domäne gibt
	 */
	private static FilterDomain getDomain(final String domainKey) throws ApiOperationException {
		final FilterDomain domain = FilterDomain.getByKey(domainKey);
		if (domain == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Die Filterdomäne '%s' ist unbekannt.".formatted(domainKey));
		}
		return domain;
	}
}
