package de.svws_nrw.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.svws_nrw.api.client.APISVWSClient;
import de.svws_nrw.api.debug.APIDebug;
import de.svws_nrw.api.schema.APISchemaRoot;
import de.svws_nrw.api.server.APIAdressbuch;
import de.svws_nrw.api.server.APIAlgoGesamtschuleAbschluss;
import de.svws_nrw.api.server.APIAlgoGostAbschluss;
import de.svws_nrw.api.server.APIBenutzer;
import de.svws_nrw.api.server.APIBetrieb;
import de.svws_nrw.api.server.APIClientConfig;
import de.svws_nrw.api.server.APIConfig;
import de.svws_nrw.api.server.APIDatabase;
import de.svws_nrw.api.server.APIENM;
import de.svws_nrw.api.server.APIErzieher;
import de.svws_nrw.api.server.APIFaecher;
import de.svws_nrw.api.server.APIGesamtschule;
import de.svws_nrw.api.server.APIGost;
import de.svws_nrw.api.server.APIGostDatenaustausch;
import de.svws_nrw.api.server.APIGostKlausuren;
import de.svws_nrw.api.server.APIGostKursplanung;
import de.svws_nrw.api.server.APIJahrgaenge;
import de.svws_nrw.api.server.APIKAOA;
import de.svws_nrw.api.server.APIKalender;
import de.svws_nrw.api.server.APIKataloge;
import de.svws_nrw.api.server.APIKlassen;
import de.svws_nrw.api.server.APIKurse;
import de.svws_nrw.api.server.APILehrer;
import de.svws_nrw.api.server.APISchema;
import de.svws_nrw.api.server.APISchild;
import de.svws_nrw.api.server.APISchueler;
import de.svws_nrw.api.server.APISchule;
import de.svws_nrw.api.server.APIStundenplan;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.OperationError;
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
import jakarta.ws.rs.core.Response;


/**
 * Diese Klasse dient als Grundlage für einen OpenAPI-Server und dient der Initialisierung
 * der OpenAPI-Schnittstelle auf Basis der zugeordneten OpenAPI-Klassen. Zunächst können
 * OpenAPI-Klassen über die Methode addAPI hinzugefügt werden, bevor das
 * Servlet später mit dem zugehörigen Init-Parameter (jakarta.ws.rs.Application)
 * und dieser Klasse initialisiert wird.
 */
@ApplicationPath("/")
public final class OpenAPIApplication extends Application {

	/// Enthält alle Klassen, die für die OpenAPI eingebunden werden
    private final Set<Class<?>> classes = new HashSet<>();


    /**
     * Erstellt die OpenAPI-Definition für den server und levered diese zurück.
     *
     * @return die OpenAPI-Spezifikation ohne die Definition der Schnittstellen-Klassen.
     */
    public static OpenAPI getBasicOpenAPI() {
    	final String adminEmail = "admin@localhost";   // TODO Servlet-Config
    	final String baseUrl = "https://localhost";   // TODO automatisch bestimmen

        final OpenAPI oas = new OpenAPI();
        final Info info = new Info()
                .title("SVWSOpenAPI")
                .version("1.0.0")
                .description("Ein Server für die Bereitstellung der Open-API eines SVWS-Servers und Anwendungen zum Zugriff auf die Daten dieses Servers "
                		+ "auf [" + baseUrl + "](" + baseUrl + ").")
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
	public OpenAPIApplication(@Context final ServletConfig servletConfig) {
		super();

		final OpenAPI oas = getBasicOpenAPI();

        final SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                .openAPI(oas)
                .prettyPrint(true)
				.resourcePackages(Stream.of("de.svws_nrw.openapi").collect(Collectors.toSet()));

		try {
            new JaxrsOpenApiContextBuilder()
                    .servletConfig(servletConfig)
                    .application(this)
                    .openApiConfiguration(oasConfig)
                    .buildContext(true);
		} catch (final OpenApiConfigurationException e) {
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
		this.classes.add(APIGostDatenaustausch.class);
		this.classes.add(APIGost.class);
		this.classes.add(APIGostKursplanung.class);
		this.classes.add(APILehrer.class);
		this.classes.add(APIErzieher.class);
		this.classes.add(APIBetrieb.class);
		this.classes.add(APIKataloge.class);
		this.classes.add(APISchema.class);
		this.classes.add(APIDatabase.class);
		this.classes.add(APIStundenplan.class);
        this.classes.add(APISchild.class);
        this.classes.add(APIKAOA.class);
		this.classes.add(APIENM.class);
		this.classes.add(APIAdressbuch.class);
		this.classes.add(APIKalender.class);
		this.classes.add(APIGostKlausuren.class);
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
	 * @param request   das HTTP-Request-Objekt
	 * @param mode      der benötigte Server-Mode für den API-Zugriff
	 *
	 * @return der aktuelle SVWS-Benutzer
	 */
	private static Benutzer getSVWSUser(final HttpServletRequest request, final ServerMode mode) {
		if (!mode.checkServerMode(SVWSKonfiguration.get().getServerMode()))
			throw OperationError.SERVICE_UNAVAILABLE.exception("Der Dienst ist noch nicht verfügbar, da er sich zur Zeit noch in der Entwicklung befindet (Stand: %s).".formatted(mode.name()));
		if (request.getUserPrincipal() instanceof final OpenAPIPrincipal openAPIPrincipal) {
			final Benutzer user = openAPIPrincipal.getUser();
			if (user == null)
				return null;
			final DBConfig config = user.connectionManager.getConfig();
			if ((config == null) || (config.getDBSchema() == null))
				return user;
			if (SVWSKonfiguration.get().isDeactivatedSchema(config.getDBSchema()))
				throw OperationError.SERVICE_UNAVAILABLE.exception("Datenbank-Schema ist zur Zeit deaktviert, da es fehlerhaft ist. Bitte wenden Sie sich an Ihren System-Administrator.");
			if (SVWSKonfiguration.get().isLockedSchema(config.getDBSchema()))
				throw OperationError.SERVICE_UNAVAILABLE.exception("Datenbank-Schema ist zur Zeit aufgrund von internen Operationen gesperrt. Der Zugriff kann später nochmals versucht werden.");
			return user;
		}
		return null;
	}

	/**
	 * Ermittelt den aktuellen SVWS-Benutzer anhand des HTTP-Requests und überprüft, ob der Benutzer
	 * entweder Admin-Rechte oder eine der übergebenen Kompetenzen besitzt.
	 *
	 * @param request       das HTTP-Request-Objekt
	 * @param mode          der benötigte Server-Mode für den API-Zugriff
	 * @param kompetenzen   die zu prüfenden Kompetenzen
	 *
	 * @return der aktuelle SVWS-Benutzer, falls ein Benutzer mit der Kompetenz angemeldet ist
	 *
	 * @throws WebApplicationException   Ist kein Benutzer angemeldet oder besitzt nicht die erforderliche Kompetenz,
	 *                                   so wird eine WebApplicationException mit dem HTTP Status Code FORBIDDEN (403) generiert
	 */
	public static Benutzer getSVWSUser(final HttpServletRequest request, final ServerMode mode, final BenutzerKompetenz... kompetenzen) throws WebApplicationException {
		final Benutzer user = getSVWSUser(request, mode);
		final Set<BenutzerKompetenz> setKompetenzen = new HashSet<>(Arrays.asList(kompetenzen));
		if ((user == null) || (!setKompetenzen.contains(BenutzerKompetenz.KEINE)) && (!user.pruefeKompetenz(setKompetenzen)))
			throw OperationError.FORBIDDEN.exception();
		return user;
	}

	/**
	 * Ermittelt den aktuellen SVWS-Benutzer anhand des HTTP-Requests und überprüft, ob der Benutzer
	 * entweder Admin-Rechte oder einer der übergebenen Kompetenzen besitzt. Erlaubt wird auch der Zugriff von
	 * dem Benutzer mit der übergebenen Benutzer-ID.
	 *
	 * @param request       das HTTP-Request-Objekt
	 * @param mode          der benötigte Server-Mode für den API-Zugriff
	 * @param user_id       die zu prüfende Benutzer-ID (ist dies die ID des angemeldeten Benutzers?)
	 * @param kompetenzen   die zu prüfenden Kompetenzen
	 *
	 * @return der aktuelle SVWS-Benutzer, falls ein Benutzer mit der Kompetenz angemeldet ist
	 *
	 * @throws WebApplicationException   Ist kein Benutzer angemeldet oder besitzt nicht die erforderliche Kompetenz,
	 *                                   so wird eine WebApplicationException mit dem HTTP Status Code FORBIDDEN (403) generiert
	 */
	public static Benutzer getSVWSUserAllowSelf(final HttpServletRequest request, final ServerMode mode, final long user_id, final BenutzerKompetenz... kompetenzen) throws WebApplicationException {
		final Benutzer user = getSVWSUser(request, mode);
		final Set<BenutzerKompetenz> setKompetenzen = new HashSet<>(Arrays.asList(kompetenzen));
		if ((user == null) || (!setKompetenzen.contains(BenutzerKompetenz.KEINE)) && (!user.pruefeKompetenz(setKompetenzen)) && (user.getId() != user_id))
			throw OperationError.FORBIDDEN.exception();
		return user;
	}


	/**
	 * Ermittelt den aktuellen SVWS-Benutzer anhand des HTTP-Requests und überprüft, ob der Benutzer
	 * entweder Admin-Rechte oder eine der übergebenen Kompetenzen besitzt. Anschließend wird eine
	 * {@link DBEntityManager} Instanz für den Datenbankzugriff zurückgegeben.
	 *
	 * @param request       das HTTP-Request-Objekt
	 * @param mode          der benötigte Server-Mode für den API-Zugriff
	 * @param kompetenzen   die zu prüfenden Kompetenzen
	 *
	 * @return die Datenbankverbindung für den aktuellen SVWS-Benutzer, falls ein Benutzer mit der Kompetenz angemeldet ist
	 *
	 * @throws WebApplicationException   Ist kein Benutzer angemeldet oder besitzt nicht die erforderliche Kompetenz,
	 *                                   so wird eine WebApplicationException mit dem HTTP Status Code FORBIDDEN (403) generiert
	 */
	public static DBEntityManager getDBConnection(final HttpServletRequest request, final ServerMode mode, final BenutzerKompetenz... kompetenzen) throws WebApplicationException {
		return getSVWSUser(request, mode, kompetenzen).getEntityManager();
	}


	/**
	 * Ermittelt den aktuellen SVWS-Benutzer anhand des HTTP-Requests und überprüft, ob der Benutzer
	 * entweder Admin-Rechte oder eine der übergebenen Kompetenzen besitzt. Erlaubt wird auch der Zugriff von
	 * dem Benutzer mit der übergebenen Benutzer-ID.
	 * Anschließend wird eine {@link DBEntityManager} Instanz für den Datenbankzugriff zurückgegeben.
	 *
	 * @param request       das HTTP-Request-Objekt
	 * @param mode          der benötigte Server-Mode für den API-Zugriff
	 * @param user_id       die zu prüfende Benutzer-ID (ist dies die ID des angemeldeten Benutzers?)
	 * @param kompetenzen   die zu prüfenden Kompetenzen
	 *
	 * @return die Datenbankverbindung für den aktuellen SVWS-Benutzer, falls ein Benutzer mit der Kompetenz angemeldet ist
	 *
	 * @throws WebApplicationException   Ist kein Benutzer angemeldet oder besitzt nicht die erforderliche Kompetenz,
	 *                                   so wird eine WebApplicationException mit dem HTTP Status Code FORBIDDEN (403) generiert
	 */
	public static DBEntityManager getDBConnectionAllowSelf(final HttpServletRequest request, final ServerMode mode, final long user_id, final BenutzerKompetenz... kompetenzen) throws WebApplicationException {
		return getSVWSUserAllowSelf(request, mode, user_id, kompetenzen).getEntityManager();
	}

	/**
	 * Führt die übergebene Aufgabe auf der Datenbank aus und gibt bei Erfolg die Response der Aufgabe zurück.
	 * Hierfür wird der aktuelle SVWS-Benutzer anhand des HTTP-Requests ermittelt und überprüft, ob der
	 * Benutzer entweder Admin-Rechte oder eine der übergebenen Kompetenzen besitzt. Die
	 * dabei erstellte {@link DBEntityManager}-Instanz wird dabei für den Datenbankzugriff genutzt.
	 *
	 * Wichtig: Eine Transaktion für die Aufgabe wird erzeugt und von dieser Methode gehandhabt!
	 *
	 * @param task          die auszuführende Aufgabe
	 * @param request       das HTTP-Request-Objekt
	 * @param mode          der benötigte Server-Mode für den API-Zugriff
	 * @param kompetenzen   die zu prüfenden Kompetenzen
	 *
	 * @return die Response zu der Aufgabe
	 */
	public static Response runWithTransaction(final Function<DBEntityManager, Response> task, final HttpServletRequest request,
			final ServerMode mode, final BenutzerKompetenz... kompetenzen) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, mode, kompetenzen)) {
			try {
				conn.transactionBegin();
				final Response response = task.apply(conn);
				conn.transactionCommitOrThrow();
				return response;
			} catch (final Exception e) {
				if (e instanceof final WebApplicationException webAppException)
					return webAppException.getResponse();
				return OperationError.INTERNAL_SERVER_ERROR.exception(e).getResponse();
			} finally {
				// Perform a rollback if necessary
				conn.transactionRollbackOrThrow();
			}
		}
	}


	/**
	 * Führt die übergebene Aufgabe auf der Datenbank aus und gibt bei Erfolg die Response der Aufgabe zurück.
	 * Hierfür wird der aktuelle SVWS-Benutzer anhand des HTTP-Requests ermittelt und überprüft, ob der
	 * Benutzer entweder Admin-Rechte oder eine der übergebenen Kompetenzen besitzt.
	 * Bei dieser Methode wird auch der Zugriff von dem Benutzer mit der übergebenen Benutzer-ID erlaubt.
	 * Die dabei erstellte {@link DBEntityManager}-Instanz wird dabei für den Datenbankzugriff genutzt.
	 *
	 * Wichtig: Eine Transaktion für die Aufgabe wird erzeugt und von dieser Methode gehandhabt!
	 *
	 * @param task          die auszuführende Aufgabe
	 * @param request       das HTTP-Request-Objekt
	 * @param mode          der benötigte Server-Mode für den API-Zugriff
	 * @param user_id       die zu prüfende Benutzer-ID (ist dies die ID des angemeldeten Benutzers?)
	 * @param kompetenzen   die zu prüfenden Kompetenzen
	 *
	 * @return die Response zu der Aufgabe
	 */
	public static Response runWithTransactionAllowSelf(final Function<DBEntityManager, Response> task, final HttpServletRequest request,
			final ServerMode mode, final long user_id, final BenutzerKompetenz... kompetenzen) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnectionAllowSelf(request, mode, user_id, kompetenzen)) {
			try {
				conn.transactionBegin();
				final Response response = task.apply(conn);
				conn.transactionCommitOrThrow();
				return response;
			} catch (final Exception e) {
				if (e instanceof final WebApplicationException webAppException)
					return webAppException.getResponse();
				return OperationError.INTERNAL_SERVER_ERROR.getResponse();
			} finally {
				// Perform a rollback if necessary
				conn.transactionRollbackOrThrow();
			}
		}
	}

}
