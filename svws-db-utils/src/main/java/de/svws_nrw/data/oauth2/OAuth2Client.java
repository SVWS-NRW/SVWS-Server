package de.svws_nrw.data.oauth2;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.dto.current.svws.auth.DTOSchuleOAuthSecrets;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse dient dem Verbinden mit einem OAuth2 Server mit ggf. vorhandenen Token. Das Token wird,
 * sofern nicht im Cache vorhanden oder ungültig über das Client-Secret und Client-Id erzeugt.
 *
 * Der Client stellt darüber hinaus HTTP-Methoden zum Verbinden mit OAuth2-Servern bereit.
 */
public final class OAuth2Client {

	/** Konstante, um Token früher als nötig zu invalidieren (zum Vermeiden von Varianz zwischen OAuth2-Server generiertem
	 * expires_in im Vergleich zum lokal erzeugtem {@link #authenticateTimestamp}) */
	private static final int TOKEN_EXPIRING_MODIFIER = 60000;

	/** Konstante für den POST_CONTENT beim Erzeugen eines Tokens */
	private static final String POST_CONTENT = "grant_type=client_credentials";

	/** Konstante Map als Cache der bereits erzeugten OAuthclients anhand ihrer URL */
	private static final Map<String, OAuth2Client> OAUTH2_CLIENT_CACHE_BY_URL = new ConcurrentHashMap<>();

	/** allg. Pfad auf OAuth2-Servern zum Tokenaustausch */
	private static final String OAUTH2_PATH = "/oauth/token";

	/** Die URL auf die dieser Client verweisen soll */
	private final String url;

	/** das Oauth Token Objekt aus der Response beim Tokenaustausch */
	private OAuth2Token token;

	/** der lokale Timestamp (vgl. {@link System#currentTimeMillis()}) zum Zeitpunkt des Tokenaustausch */
	private long authenticateTimestamp;

	/** der zu nutzende Pfad für den Tokenaustausch */
	private final String oauth2Path;


	/**
	 * Privater Konstruktor, vgl. {@link OAuth2Client#getClient(String, Supplier)} zum Instantiieren
	 * des Clients
	 *
	 * @param url          die URL mit der dieser Client sich verbinden soll
	 * @param oauth2Path   der Pfad zum Tokenaustausch
	 */
	private OAuth2Client(final String url, final String oauth2Path) {
		this.url = url;
		this.oauth2Path = oauth2Path;
	}


	/**
	 * Hilfsmethode prüft, ob für die gegebene URL bereits ein Client im mit validem Token vorhanden ist und gibt diesen
	 * zurück. Falls kein Client vorhanden ist, wird ein neuer erzeugt. Falls das Token ausgelaufen ist, wird ein neuer
	 * Tokenaustausch ausgeführt.
	 *
	 * @param url          die URL des OAuth2-Servers
	 * @param oauth2Path   der Pfad zum Tokenaustausch
	 * @param basicAuth    der BasicAuth String zur Authentifizierung
	 * @param logger       der Logger
	 *
	 * @return der erstellte OAuth2Client mit validem Token
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private static OAuth2Client getClient(final String url, final String oauth2Path, final String basicAuth, final Logger logger) throws ApiOperationException {
		logger.logLn("Prüfe, ob ein bestehendes Token wiederverwendet werden kann...");
		final OAuth2Client client = OAUTH2_CLIENT_CACHE_BY_URL.computeIfAbsent(url, s -> new OAuth2Client(url, oauth2Path));
		if (client.isTokenValid()) {
			logger.logLn("Das Token ist noch gültig und wird erneut verwendet.");
		} else {
			logger.logLn("Es exitiert kein gültiges Token und ein neues Token muss angefordert werden...");
			client.requestToken(basicAuth, logger);
		}
		return client;
	}


	/**
	 * Öffentliche Methode, um einen {@link OAuth2Client} für die gegebene URL zu erhalten. vgl.
	 * {@link #getClient(String, String, String, Logger)} mit {@link #OAUTH2_PATH}
	 *
	 * @param url         die URL zu der der OAuth2Client verbunden werden soll
	 * @param basicAuth   für die Basisauthentifizierung
	 * @param logger      der Logger
	 *
	 * @return den OAuth2Client
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static OAuth2Client getClient(final String url, final String basicAuth, final Logger logger) throws ApiOperationException {
		return getClient(url, OAUTH2_PATH, basicAuth, logger);
	}


	/**
	 * Öffentliche Methode um einen OAuth2-Client anhand des {@link DTOSchuleOAuthSecrets} zu erhalten
	 *
	 * @param dto      das DTO mit den OAuth2-Secrets der Schule
	 * @param logger   der Logger
	 *
	 * @return den OAuth2-Client
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static OAuth2Client getClient(final DTOSchuleOAuthSecrets dto, final Logger logger) throws ApiOperationException {
		if ((dto == null) || (dto.AuthServer == null) || dto.AuthServer.isBlank() || (dto.ClientID == null)
				|| dto.ClientID.isBlank() || (dto.ClientSecret == null) || dto.ClientSecret.isBlank())
			throw new ApiOperationException(Status.NOT_FOUND, "Die OAuth2-Daten aus der Datenbank sind unvollständig.");
		logger.logLn("Generiere den HTTP-Header für Basic-Auth bestehen aus der Client-ID als User und dem Client-Secret als Kennwort...");
		final String basicAuth = Base64.getEncoder().encodeToString((dto.ClientID + ":" + dto.ClientSecret).getBytes());
		return getClient(dto.AuthServer, basicAuth, logger);
	}


	/**
	 * Leert den Client-Cache für das übergenene Secret und damit auch das zugehörige Token
	 *
	 * @param dto   das Secret, zu welchem die Daten aus dem Cache entfernt werden sollen
	 */
	public static void clearClientCache(final DTOSchuleOAuthSecrets dto) {
		if ((dto == null) || (dto.AuthServer == null) || dto.AuthServer.isBlank())
			return;
		OAUTH2_CLIENT_CACHE_BY_URL.remove(dto.AuthServer);
	}


	/**
	 * Gibt wieder, ob ein Token vorhanden ist, welches nicht abgelaufen ist
	 *
	 * @return true, wenn ein nicht abgelaufenes Token vorhanden ist, und ansonsten false
	 */
	private boolean isTokenValid() {
		if (this.token == null)
			return false;
		return ((this.token.expiresIn * 1000) + authenticateTimestamp) > (System.currentTimeMillis() - TOKEN_EXPIRING_MODIFIER);
	}


	/**
	 * Erzeugt auf Basis eines Basic-Auth Strings ein Token und hinterlegt es an diesem Client
	 *
	 * @param basicAuthString   String für die BasicAuth, Base64 encoded clientId:password
	 * @param logger            der zu verwendende Logger
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private void requestToken(final String basicAuthString, final Logger logger) throws ApiOperationException {
		logger.logLn("Erstelle den HTTP-Header für die Token-Anfrage...");
		// Bereite des HTTP-Request vor...
		final String client_secret_b64 = basicAuthString;
		final URI uri = URI.create(url + oauth2Path);
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2))
				.POST(BodyPublishers.ofString(POST_CONTENT)).header("Content-Type", "application/x-www-form-urlencoded")
				.setHeader("Authorization", "Basic " + client_secret_b64).build();
		// ... sende den Request und warte auf die Antwort ...
		logger.logLn("Sende die HTTP-Anfrage für eines neues Token...");
		final HttpResponse<String> response = send(request, BodyHandlers.ofString());
		// ... prüfe, den Response-Code ...
		final int statusCode = response.statusCode();
		if (statusCode == 401)
			throw new ApiOperationException(Status.BAD_GATEWAY, "Verbindung zu dem OAuth2-Server ergab 401 (Unauthorized)."
					+ " Die Client Credentials sollten überprüft werden.");
		if (statusCode == 500)
			throw new ApiOperationException(Status.UNAUTHORIZED, "Verbindung zu dem OAuth2-Server ergab 500 (Internal Server Error)."
					+ " Die Client Credentials sollten überprüft werden. Hier liegt ein interner Fehler im OAuth2-Server vor.");
		if ((statusCode != 200) && (statusCode != 201))
			throw new ApiOperationException(Status.BAD_GATEWAY, "Verbindung zu dem OAuth2-Server mit dem Status-Code %d fehlgeschlagen.".formatted(statusCode));
		// ... und validiere im Erfolgsfall die HTTP-Response
		final String stringResponse = response.body();
		this.authenticateTimestamp = System.currentTimeMillis();
		try {
			this.token = getTokenfromJson(stringResponse);
			logger.logLn("Das Token wurde erfolgreich empfangen.");
		} catch (@SuppressWarnings("unused") final JsonProcessingException e) {
			throw new ApiOperationException(Status.BAD_GATEWAY, "Antwort des OAuthServers inkorrekt:\n" + stringResponse);
		}
	}


	/**
	 * Methode zum Versenden eines HTTP-Requests mithilfe der Methode
	 * {@link #send(HttpRequest, BodyHandler)}.
	 *
	 * @param <T> generischer Typ der Response
	 * @param request   der zu sendende Request
	 * @param handler   der BodyHandler
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private static <T> HttpResponse<T> send(final HttpRequest request, final BodyHandler<T> handler) throws ApiOperationException {
		try (HttpClient client = HttpClient.newBuilder().version(Version.HTTP_1_1).connectTimeout(Duration.ofSeconds(20)).build()) {
			return client.send(request, handler);
		} catch (IOException | InterruptedException e) {
			throw new ApiOperationException(Status.BAD_GATEWAY, e, "Fehler beim Senden der Informationen: " + e.getLocalizedMessage());
		}
	}


	/**
	 * Getter für das {@link OAuth2Token}
	 *
	 * @return das {@link OAuth2Token}
	 */
	public OAuth2Token getToken() {
		return this.token;
	}


	/**
	 * Sendet Daten an eine URL mit dem Content-Type multipart/form-data.
	 *
	 * @param <T>        der generische Typ der {@link HttpResponse} und des entsprechenden {@link BodyHandler}
	 * @param path       der Pfad als Teil der URL an den der Request gesendet wird
	 * @param filename   der Dateiname, der verwendet wird
	 * @param bytes      die Bytes, die innerhalb des Files gesendet werden
	 * @param handler    der BodyHandler für die Response
	 * @param logger     der Logger
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public <T> HttpResponse<T> postMultipart(final String path, final String filename, final byte[] bytes, final BodyHandler<T> handler, final Logger logger)
			throws ApiOperationException {
		logger.logLn("Bereite die HTTP-Anfrage vor...");
		final URI uri = URI.create(url + path);
		final String actualBoundary = UUID.randomUUID().toString() + "--";
		final String boundary = "--" + actualBoundary;
		final byte[] boundaryBytes = ("\r\n" + boundary).getBytes();
		final byte[] contentDisposition = (boundary + "\r\nContent-Disposition: form-data; name=\"file\"; filename=\"" + filename + "\"\r\n\r\n").getBytes();
		final byte[] c = new byte[contentDisposition.length + bytes.length + boundaryBytes.length];
		System.arraycopy(contentDisposition, 0, c, 0, contentDisposition.length);
		System.arraycopy(bytes, 0, c, contentDisposition.length, bytes.length);
		System.arraycopy(boundaryBytes, 0, c, contentDisposition.length + bytes.length, boundaryBytes.length);
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2))
				.POST(BodyPublishers.ofByteArray(c))
				.header("Content-Type", "multipart/form-data;boundary=" + actualBoundary)
				.header("Accept", "application/json")
				.header("Authorization", "Bearer " + token.accessToken).header("file", "file").build();
		logger.logLn("Sende die HTTP-Anfrage an den OAuth2-Server...");
		return send(request, handler);
	}


	/**
	 * Sendet Daten mit Content-Type application/x-www-form-urlencoded. Dabei werden die übergebenen Strings als
	 * Schlüssel-Wert-Paare betrachtet
	 *
	 * @param <T>             generischer Typ des {@link HttpResponse} und {@link BodyHandler}
	 * @param path            der Pfad als Teil der URL für diesen OauthClient, an den die Daten geschickt werden
	 * @param handler         der BodyHandler für den Response-Body
	 * @param logger          der Logger
	 * @param keyValuePairs   Schlüssel-Wert-Paare für die Form-Parameter, gerader Index = Schlüssel, ungerader Index = Wert
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public <T> HttpResponse<T> postFormUrlEncoded(final String path, final BodyHandler<T> handler, final Logger logger, final String... keyValuePairs)
			throws ApiOperationException {
		logger.logLn("Bereite die HTTP-Anfrage vor...");
		final URI uri = URI.create(url + path);
		if ((keyValuePairs.length % 2) != 0)
			throw new IllegalArgumentException("Invalid nameValuePairs");
		String input = "";
		for (int i = 0; i < keyValuePairs.length; i += 2)
			input = keyValuePairs[i] + "=" + keyValuePairs[i + 1] + "\n";
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2))
				.POST(BodyPublishers.ofString(input)).header("Content-Type", "application/x-www-form-urlencoded")
				.header("Accept", "application/json")
				.header("Authorization", "Bearer " + token.accessToken).build();
		logger.logLn("Sende die HTTP-Anfrage an den OAuth2-Server...");
		return send(request, handler);
	}


	/**
	 * Führt ein POST-Request gegen den gegebenen Pfad aus
	 *
	 * @param <T>       generischer Typ des {@link HttpResponse} und {@link BodyHandler}
	 * @param path      der Pfad als Teil der URL für diesen OauthClient, an den das POST geschickt wird
	 * @param handler   der BodyHandler für den Response-Body
	 * @param logger    der Logger
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public <T> HttpResponse<T> postEmpty(final String path, final BodyHandler<T> handler, final Logger logger) throws ApiOperationException {
		logger.logLn("Bereite die HTTP-Anfrage vor...");
		final URI uri = URI.create(url + path);
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2)).GET()
				.POST(BodyPublishers.ofString("")).header("Content-Type", "application/x-www-form-urlencoded")
				.header("Accept", "application/json")
				.header("Authorization", "Bearer " + token.accessToken).build();
		logger.logLn("Sende die HTTP-Anfrage an den OAuth2-Server...");
		return send(request, handler);
	}


	/**
	 * Sendet Daten als PUT mit Content-Type application/json.
	 *
	 * @param <T>             generischer Typ des {@link HttpResponse} und {@link BodyHandler}
	 * @param path            der Pfad als Teil der URL für diesen OauthClient, an den die Daten geschickt werden
	 * @param handler         der BodyHandler für den Response-Body
	 * @param logger          der Logger
	 * @param daten           die Daten
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public <T> HttpResponse<T> put(final String path, final BodyHandler<T> handler, final Logger logger, final String daten)
			throws ApiOperationException {
		logger.logLn("Bereite die HTTP-Anfrage vor...");
		final URI uri = URI.create(url + path);
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2))
				.PUT(BodyPublishers.ofString(daten)).header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.header("Authorization", "Bearer " + token.accessToken).build();
		logger.logLn("Sende die HTTP-Anfrage an den OAuth2-Server...");
		return send(request, handler);
	}


	/**
	 * Führt ein GET-Request gegen den gegebenen Pfad aus
	 *
	 * @param <T>       generischer Typ des {@link HttpResponse} und {@link BodyHandler}
	 * @param path      der Pfad als Teil der URL für diesen OauthClient, an den das GET geschickt wird
	 * @param handler   der BodyHandler für den Response-Body
	 * @param logger    der Logger
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public <T> HttpResponse<T> get(final String path, final BodyHandler<T> handler, final Logger logger) throws ApiOperationException {
		logger.logLn("Bereite die HTTP-Anfrage vor...");
		final URI uri = URI.create(url + path);
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2)).GET()
				.header("Accept", "application/json")
				.header("Authorization", "Bearer " + token.accessToken).build();
		logger.logLn("Sende die HTTP-Anfrage an den OAuth2-Server...");
		return send(request, handler);
	}


	/**
	 * Wandelt einen JSON-String in ein {@link OAuth2Token} um.
	 *
	 * @param json   der JSON-String
	 *
	 * @return das OAuth2-Token
	 *
	 * @throws JsonProcessingException   wenn die Umwandlung fehlschlägt
	 */
	public static OAuth2Token getTokenfromJson(final String json) throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
		return mapper.readValue(json, OAuth2Token.class);
	}

}
