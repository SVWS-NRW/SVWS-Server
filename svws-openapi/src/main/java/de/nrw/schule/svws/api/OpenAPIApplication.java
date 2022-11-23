package de.nrw.schule.svws.api;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.nrw.schule.svws.api.client.APISVWSClient;
import de.nrw.schule.svws.api.debug.APIDebug;
import de.nrw.schule.svws.api.schema.APISchemaRoot;
import de.nrw.schule.svws.api.server.APIAdressbuch;
import de.nrw.schule.svws.api.server.APIAlgoGesamtschuleAbschluss;
import de.nrw.schule.svws.api.server.APIAlgoGostAbschluss;
import de.nrw.schule.svws.api.server.APIBenutzer;
import de.nrw.schule.svws.api.server.APIBetrieb;
import de.nrw.schule.svws.api.server.APIClientConfig;
import de.nrw.schule.svws.api.server.APIConfig;
import de.nrw.schule.svws.api.server.APIENM;
import de.nrw.schule.svws.api.server.APIErzieher;
import de.nrw.schule.svws.api.server.APIFaecher;
import de.nrw.schule.svws.api.server.APIGesamtschule;
import de.nrw.schule.svws.api.server.APIGost;
import de.nrw.schule.svws.api.server.APIJahrgaenge;
import de.nrw.schule.svws.api.server.APIKAOA;
import de.nrw.schule.svws.api.server.APIKataloge;
import de.nrw.schule.svws.api.server.APIKlassen;
import de.nrw.schule.svws.api.server.APIKurse;
import de.nrw.schule.svws.api.server.APILehrer;
import de.nrw.schule.svws.api.server.APILupo;
import de.nrw.schule.svws.api.server.APISchema;
import de.nrw.schule.svws.api.server.APISchild;
import de.nrw.schule.svws.api.server.APISchueler;
import de.nrw.schule.svws.api.server.APISchule;
import de.nrw.schule.svws.api.server.APIStundenplan;
import de.nrw.schule.svws.config.SVWSKonfiguration;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.db.Benutzer;
import de.nrw.schule.svws.db.DBConfig;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.utils.OperationError;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Context;


/**
 * Diese Klasse dient als Grundlage für einen OpenAPI-Server und dient der Initialisierung
 * der OpenAPI-Schnittstelle auf Basis der zugeordneten OpenAPI-Klassen. Zunächst können 
 * OpenAPI-Klassen über die Methode addAPI hinzugefügt werden, bevor das
 * Servlet später mit dem zugehörigen Init-Parameter (jakarta.ws.rs.Application)
 * und dieser Klasse initialisiert wird.
 */
@ApplicationPath("/")
public class OpenAPIApplication extends Application {

	/// Enthält alle Klassen, die für die OpenAPI eingebunden werden
    private Set<Class<?>> classes = new HashSet<>();
    
    
    /**
     * Erstellt die OpenAPI-Definition für den server und levered diese zurück.
     * 
     * @return die OpenAPI-Spezifikation ohne die Definition der Schnittstellen-Klassen.
     */
    public static OpenAPI getBasicOpenAPI() {
    	String adminEmail= "admin@localhost";   // TODO Servlet-Config 
    	String baseUrl = "https://localhost";   // TODO automatisch bestimmen
    	
        OpenAPI oas = new OpenAPI();
        Info info = new Info()
                .title("SVWSOpenAPI")
                .version("1.0.0")
                .description("Ein Server für die Bereitstellung der Open-API eines SVWS-Servers und Anwendungen zum Zugriff auf die Daten dieses Servers " +
                        "auf [" + baseUrl + "](" + baseUrl + ").")
                .termsOfService(baseUrl + "/terms/")
                .contact(new Contact()
                        .email(adminEmail))
                .license(new License()
                        .name("Apache 2.0")
                        .url("http://www.apache.org/licenses/LICENSE-2.0.html"));

        oas.info(info);
        oas.components(new Components()
                .addSecuritySchemes("basicAuth", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"));
        oas.addServersItem(new Server().url("/").description("The server where the openapi.json is located."));
        oas.addServersItem(new Server().url("https://localhost").description("Lokaler Server"));
        return oas;
    }
    
    
	/**
     * Erstellt eine neue OpenAPI-Applikation anhand der zuvor spezifizierten API-Klassen
     * (@see addAPI). 
	 * 
	 * @param servletConfig die Konfiguration für das Servlet
	 */
	@SuppressWarnings("rawtypes")
	public OpenAPIApplication(@Context ServletConfig servletConfig) {
		super();

		OpenAPI oas = getBasicOpenAPI();

        SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                .openAPI(oas)
                .prettyPrint(true)
				.resourcePackages(Stream.of("de.nrw.schule.svws.openapi").collect(Collectors.toSet()));

		try {
            new JaxrsOpenApiContextBuilder()
                    .servletConfig(servletConfig)
                    .application(this)
                    .openApiConfiguration(oasConfig)
                    .buildContext(true);
		} catch (OpenApiConfigurationException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		this.classes.add(OpenAPICorsFilter.class);

		this.classes.add(APIConfig.class);
		this.classes.add(APIDebug.class);
		this.classes.add(APISVWSClient.class);
		this.classes.add(APIAlgoGesamtschuleAbschluss.class);
		this.classes.add(APIAlgoGostAbschluss.class);
		this.classes.add(APIBenutzer.class);
		this.classes.add(APIClientConfig.class);
		this.classes.add(APISchule.class);
		this.classes.add(APIJahrgaenge.class);
		this.classes.add(APIKlassen.class);
		this.classes.add(APIKurse.class);
		this.classes.add(APIFaecher.class);
		this.classes.add(APISchueler.class);
		this.classes.add(APIGesamtschule.class);
		this.classes.add(APILupo.class);
		this.classes.add(APIGost.class);
		this.classes.add(APILehrer.class);
		this.classes.add(APIErzieher.class);
		this.classes.add(APIBetrieb.class);
		this.classes.add(APIKataloge.class);
		this.classes.add(APISchema.class);
		this.classes.add(APIStundenplan.class);
        this.classes.add(APISchild.class);
        this.classes.add(APIKAOA.class);
		this.classes.add(APIENM.class);
		this.classes.add(APIAdressbuch.class);
		if (!SVWSKonfiguration.get().isDBRootAccessDisabled())
			this.classes.add(APISchemaRoot.class);

		this.classes.add(OpenApiResource.class);
	}

	@Override
	public Set<Class<?>> getClasses() {
		return this.classes;
	}

	/**
	 * Ermittelt den aktuellen SVWS-Benutzer anhand des HTTP-Requests.
	 * 
	 * @param request das HTTP-Request-Objekt
	 * 
	 * @return der aktuelle SVWS-Benutzer
	 */
	private static Benutzer getSVWSUser(HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		if (principal instanceof OpenAPIPrincipal) {
			Benutzer user = ((OpenAPIPrincipal) principal).getUser();
			if (user == null)
				return null;
			DBConfig config = user.connectionManager.getConfig();
			if ((config == null) || (config.getDBSchema() == null))
				return user;
			if (SVWSKonfiguration.get().isLockedSchema(config.getDBSchema()))
				throw OperationError.SERVICE_UNAVAILABLE.exception("Datenbank-Schema ist zur Zeit aufgrund von internen Operationen gesperrt. Der Zugriff kann später nochmals versucht werden.");
			return user;
		}
		return null;
	}

	/**
	 * Ermittelt den aktuellen SVWS-Benutzer anhand des HTTP-Requests und überprüft, ob der Benutzer
	 * entweder Admin-Rechte oder die übergebene Kompetenz besitzt.  
	 * 
	 * @param request   das HTTP-Request-Objekt
	 * @param kompetenz die zu prüfende Kompetenz
	 * 
	 * @return der aktuelle SVWS-Benutzer, falls ein Benutzer mit der Kompetenz angemeldet ist
	 * 
	 * @throws WebApplicationException   Ist kein Benutzer angemeldet oder besitzt nicht die erforderliche Kompetenz,
	 *                                   so wird eine WebApplicationException mit dem HTTP Status Code FORBIDDEN (403) generiert
	 */
	public static Benutzer getSVWSUser(HttpServletRequest request, BenutzerKompetenz kompetenz) throws WebApplicationException {
		Benutzer user = getSVWSUser(request);
		if ((user == null) || (kompetenz != BenutzerKompetenz.KEINE) && (!user.pruefeKompetenz(kompetenz)))
			throw OperationError.FORBIDDEN.exception();
		return user;
	}

	/**
	 * Ermittelt den aktuellen SVWS-Benutzer anhand des HTTP-Requests und überprüft, ob der Benutzer
	 * entweder Admin-Rechte oder die übergebene Kompetenz besitzt. Erlaubt wird auch der Zugriff von 
	 * dem Benutzer mit der übergebenen Benutzer-ID.  
	 * 
	 * @param request     das HTTP-Request-Objekt
	 * @param kompetenz   die zu prüfende Kompetenz
	 * @param user_id     die zu prüfende Benutzer-ID (ist dies die ID des angemeldeten Benutzers?)
	 * 
	 * @return der aktuelle SVWS-Benutzer, falls ein Benutzer mit der Kompetenz angemeldet ist
	 * 
	 * @throws WebApplicationException   Ist kein Benutzer angemeldet oder besitzt nicht die erforderliche Kompetenz,
	 *                                   so wird eine WebApplicationException mit dem HTTP Status Code FORBIDDEN (403) generiert
	 */
	public static Benutzer getSVWSUserAllowSelf(HttpServletRequest request, BenutzerKompetenz kompetenz, long user_id) throws WebApplicationException {
		Benutzer user = getSVWSUser(request);
		if ((user == null) || (kompetenz != BenutzerKompetenz.KEINE) && (!user.pruefeKompetenz(kompetenz)) && (user.getId() != user_id))
			throw OperationError.FORBIDDEN.exception();
		return user;
	}

	
	/**
	 * Ermittelt den aktuellen SVWS-Benutzer anhand des HTTP-Requests und überprüft, ob der Benutzer
	 * entweder Admin-Rechte oder die übergebene Kompetenz besitzt. Anschließend wird eine
	 * {@link DBEntityManager} Instanz für den Datenbankzugriff zurückgegeben.
	 * 
	 * @param request   das HTTP-Request-Objekt
	 * @param kompetenz die zu prüfende Kompetenz
	 * 
	 * @return die Datenbankverbindung für den aktuellen SVWS-Benutzer, falls ein Benutzer mit der Kompetenz angemeldet ist
	 * 
	 * @throws WebApplicationException   Ist kein Benutzer angemeldet oder besitzt nicht die erforderliche Kompetenz,
	 *                                   so wird eine WebApplicationException mit dem HTTP Status Code FORBIDDEN (403) generiert
	 */
	public static DBEntityManager getDBConnection(HttpServletRequest request, BenutzerKompetenz kompetenz) throws WebApplicationException {
		return getSVWSUser(request, kompetenz).getEntityManager();
	}


	/**
	 * Ermittelt den aktuellen SVWS-Benutzer anhand des HTTP-Requests und überprüft, ob der Benutzer
	 * entweder Admin-Rechte oder die übergebene Kompetenz besitzt. Erlaubt wird auch der Zugriff von 
	 * dem Benutzer mit der übergebenen Benutzer-ID.  
	 * Anschließend wird eine {@link DBEntityManager} Instanz für den Datenbankzugriff zurückgegeben.
	 * 
	 * @param request     das HTTP-Request-Objekt
	 * @param kompetenz   die zu prüfende Kompetenz
	 * @param user_id     die zu prüfende Benutzer-ID (ist dies die ID des angemeldeten Benutzers?)
	 * 
	 * @return die Datenbankverbindung für den aktuellen SVWS-Benutzer, falls ein Benutzer mit der Kompetenz angemeldet ist
	 * 
	 * @throws WebApplicationException   Ist kein Benutzer angemeldet oder besitzt nicht die erforderliche Kompetenz,
	 *                                   so wird eine WebApplicationException mit dem HTTP Status Code FORBIDDEN (403) generiert
	 */
	public static DBEntityManager getDBConnectionAllowSelf(HttpServletRequest request, BenutzerKompetenz kompetenz, long user_id) throws WebApplicationException {
		return getSVWSUserAllowSelf(request, kompetenz, user_id).getEntityManager();
	}
	
}