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
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import de.svws_nrw.base.crypto.KeyStoreUtils;
import de.svws_nrw.base.crypto.TLSUtils;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.oauth2.OAuth2ServerTyp;
import de.svws_nrw.db.DBEntityManager;
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

	/** allg. Pfad auf OAuth2-Servern zum Tokenaustausch */
	private static final String OAUTH2_PATH = "/oauth/token";

	/** Dir Datenbank-Verbindung für den Zugriff auf die OAuth2-Verbindungsdaten */
	private final DBEntityManager conn;

	/** Der zu verwendende Logger */
	private final Logger logger;

	/** Das DTO mit den OAuth2-Informationen */
	private final DTOSchuleOAuthSecrets dto;


	/**
	 * Erzeugt einen neuen OAuth2-Client und erneuert ggf. das aktuelle Token.
	 *
	 * @param conn         die Datenbankverbindung zur SVWS-DB, um die aktuell gespeicherten OAuth2-Verindungsdaten abzufragen
	 * @param logger       ein Logger für das Loggen der Kommunikation über diesen Client
	 * @param serverTyp    der Server-Typ des zu kontaktierenden OAuth2-Servers
	 * @param updateToken  gibt an, ob das Token überprüft und ggf. erneuert werden soll
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public OAuth2Client(final DBEntityManager conn, final Logger logger, final OAuth2ServerTyp serverTyp, final boolean updateToken)
			throws ApiOperationException {
		this.conn = conn;
		this.logger = logger;
		// Lese die Verbindungsdaten aus der Datenbank ein.
		logger.logLn("Lese die OAuth2-Client-Secrets für den Server-Typ %s aus der Datenbank...".formatted(serverTyp.name()));
		this.dto = new DataOauthClientSecrets(conn).getDto(serverTyp);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden keine OAuth2-Client-Secrets den Server-Typ %s in der Datenbank gefunden.".formatted(serverTyp.name()));
		if ((dto.AuthServer == null) || dto.AuthServer.isBlank())
			throw new ApiOperationException(Status.NOT_FOUND, "Bei den OAuth2-Daten aus der Datenbank fehlt die Server-URL.");
		if ((dto.ClientID == null) || dto.ClientID.isBlank())
			throw new ApiOperationException(Status.NOT_FOUND, "Bei den OAuth2-Daten aus der Datenbank ist keine Client-ID angegeben.");
		if (!dto.ClientID.equals("" + serverTyp.getId()))
			throw new ApiOperationException(Status.NOT_FOUND, "Bei den OAuth2-Daten aus der Datenbank ist keine passende Client-ID angegeben.");
		if (updateToken) {
			if ((dto.ClientSecret == null) || dto.ClientSecret.isBlank())
				throw new ApiOperationException(Status.NOT_FOUND, "Die OAuth2-Daten aus der Datenbank enthalten kein Client-Secret.");
			logger.logLn("Generiere den HTTP-Header für Basic-Auth bestehen aus der Client-ID als User und dem Client-Secret als Kennwort...");
			final String basicAuth = Base64.getEncoder().encodeToString((dto.ClientID + ":" + dto.ClientSecret).getBytes());
			logger.logLn("Prüfe, ob ein bestehendes Token wiederverwendet werden kann...");
			if (isTokenValid()) {
				logger.logLn("Das Token ist noch gültig und wird erneut verwendet.");
			} else {
				logger.logLn("Es exitiert kein gültiges Token und ein neues Token muss angefordert werden...");
				requestToken(basicAuth, logger);
			}
		}
	}


	/**
	 * Gibt wieder, ob ein Token vorhanden ist, welches nicht abgelaufen ist
	 *
	 * @return true, wenn ein nicht abgelaufenes Token vorhanden ist, und ansonsten false
	 */
	private boolean isTokenValid() {
		if (dto.Token == null)
			return false;
		// Berechne die Zeit in Millisekunden, wann das Token abläuft
		final long tsExpiration = ((dto.TokenExpiresIn * 1000) + dto.TokenTimestamp);
		// Bestimme die aktuelle Zeit zum Vergleich, addiere aber einen Wert darauf, um das Token ggf. früher zu erneuern
		// (dies umgeht Probleme der unterschiedlichen Zeitstempel von Server und Client und der Netzwerkverzögerung bei der Übertragung des Tokens)
		final long tsNow = System.currentTimeMillis() + 60000;
		// Das Token ist noch gültig, wenn der aktuelle Zeitstempel kleiner ist als der Zeitstempel für das Ablaufen des Tokens
		return (tsNow < tsExpiration);
	}


	/**
	 * Erzeugt auf Basis eines Basic-Auth Strings ein Token und hinterlegt es an diesem Client
	 *
	 * @param basicAuthString   String für die BasicAuth, Base64 encoded "clientId:password"
	 * @param logger            der zu verwendende Logger
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private void requestToken(final String basicAuthString, final Logger logger) throws ApiOperationException {
		logger.logLn("Erstelle den HTTP-Header für die Token-Anfrage...");
		// Bereite des HTTP-Request vor...
		final String client_secret_b64 = basicAuthString;
		final URI uri = URI.create(dto.AuthServer + OAUTH2_PATH);
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2))
				.POST(BodyPublishers.ofString("grant_type=client_credentials")).header("Content-Type", "application/x-www-form-urlencoded")
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
		try {
			// Wandle dafür die Anwort in ein Java-DTO um ...
			final ObjectMapper mapper = new ObjectMapper();
			mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
			final OAuth2Token token = mapper.readValue(stringResponse, OAuth2Token.class);
			// ... und speichere die Informationen zum Token in der Datenbank
			this.dto.TokenTimestamp = System.currentTimeMillis();
			this.dto.TokenType = token.tokenType;
			this.dto.Token = token.accessToken;
			this.dto.TokenExpiresIn = token.expiresIn;
			this.dto.TokenScope = token.scope;
			conn.transactionPersist(this.dto);
			conn.transactionFlush();
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
	private <T> HttpResponse<T> send(final HttpRequest request, final BodyHandler<T> handler) throws ApiOperationException {
		HttpClient.Builder builder = HttpClient.newBuilder().version(Version.HTTP_1_1).connectTimeout(Duration.ofSeconds(20));
		if ((dto.TLSCertIsKnown == null) || (!dto.TLSCertIsKnown)) {
			try {
				if (dto.TLSCert == null)
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "In der Datenbank ist keine TLS-Zertifikatskette des TLS-Servers zur Nutzung hinterlegt.");
				final List<X509Certificate> certList = TLSUtils.decodeCertListJson(dto.TLSCert);
				if (certList.isEmpty())
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "In der Datenbank ist kein TLS-Zertifikat zur Nutzung hinterlegt.");
				if (!dto.TLSCertIsTrusted)
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der in der Datenbank zur Nutzung hinterlegten TLS-Zertifikatskette des TLS-Servers wird nicht vertraut.");
				final KeyStore keystore = KeyStoreUtils.newKeystore();
				KeyStoreUtils.addCertificate(keystore, dto.AuthServer, certList.getFirst());
				SSLContext sslContext = TLSUtils.getTLSContextFromKeystore(keystore);
				builder = builder.sslContext(sslContext);
			} catch (final ApiOperationException e) {
				throw e;
			} catch (GeneralSecurityException e) {
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, e.getMessage());
			}
		}
		try {
			try (HttpClient client = builder.build()) {
				return client.send(request, handler);
			}
		} catch (IOException | InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new ApiOperationException(Status.BAD_GATEWAY, e, "Fehler beim Senden der Informationen: " + e.getLocalizedMessage());
		}
	}


	/**
	 * Sendet Daten an eine URL mit dem Content-Type multipart/form-data.
	 *
	 * @param <T>        der generische Typ der {@link HttpResponse} und des entsprechenden {@link BodyHandler}
	 * @param path       der Pfad als Teil der URL an den der Request gesendet wird
	 * @param filename   der Dateiname, der verwendet wird
	 * @param bytes      die Bytes, die innerhalb des Files gesendet werden
	 * @param handler    der BodyHandler für die Response
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public <T> HttpResponse<T> postMultipart(final String path, final String filename, final byte[] bytes, final BodyHandler<T> handler)
			throws ApiOperationException {
		logger.logLn("Bereite die HTTP-Anfrage vor...");
		final URI uri = URI.create(dto.AuthServer + path);
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
				.header("Authorization", "Bearer " + dto.Token).header("file", "file").build();
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
	 * @param keyValuePairs   Schlüssel-Wert-Paare für die Form-Parameter, gerader Index = Schlüssel, ungerader Index = Wert
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public <T> HttpResponse<T> postFormUrlEncoded(final String path, final BodyHandler<T> handler, final String... keyValuePairs) throws ApiOperationException {
		logger.logLn("Bereite die HTTP-Anfrage vor...");
		final URI uri = URI.create(dto.AuthServer + path);
		if ((keyValuePairs.length % 2) != 0)
			throw new IllegalArgumentException("Invalid nameValuePairs");
		String input = "";
		for (int i = 0; i < keyValuePairs.length; i += 2)
			input = keyValuePairs[i] + "=" + keyValuePairs[i + 1] + "\n";
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2))
				.POST(BodyPublishers.ofString(input)).header("Content-Type", "application/x-www-form-urlencoded")
				.header("Accept", "application/json")
				.header("Authorization", "Bearer " + dto.Token).build();
		logger.logLn("Sende die HTTP-Anfrage an den OAuth2-Server...");
		return send(request, handler);
	}


	/**
	 * Führt ein POST-Request gegen den gegebenen Pfad aus
	 *
	 * @param <T>       generischer Typ des {@link HttpResponse} und {@link BodyHandler}
	 * @param path      der Pfad als Teil der URL für diesen OauthClient, an den das POST geschickt wird
	 * @param handler   der BodyHandler für den Response-Body
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public <T> HttpResponse<T> postEmpty(final String path, final BodyHandler<T> handler) throws ApiOperationException {
		logger.logLn("Bereite die HTTP-Anfrage vor...");
		final URI uri = URI.create(dto.AuthServer + path);
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2)).GET()
				.POST(BodyPublishers.ofString("")).header("Content-Type", "application/x-www-form-urlencoded")
				.header("Accept", "application/json")
				.header("Authorization", "Bearer " + dto.Token).build();
		logger.logLn("Sende die HTTP-Anfrage an den OAuth2-Server...");
		return send(request, handler);
	}


	/**
	 * Sendet Daten als PUT mit Content-Type application/json.
	 *
	 * @param <T>             generischer Typ des {@link HttpResponse} und {@link BodyHandler}
	 * @param path            der Pfad als Teil der URL für diesen OauthClient, an den die Daten geschickt werden
	 * @param handler         der BodyHandler für den Response-Body
	 * @param daten           die Daten
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public <T> HttpResponse<T> put(final String path, final BodyHandler<T> handler, final String daten)
			throws ApiOperationException {
		logger.logLn("Bereite die HTTP-Anfrage vor...");
		final URI uri = URI.create(dto.AuthServer + path);
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2))
				.PUT(BodyPublishers.ofString(daten)).header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.header("Authorization", "Bearer " + dto.Token).build();
		logger.logLn("Sende die HTTP-Anfrage an den OAuth2-Server...");
		return send(request, handler);
	}


	/**
	 * Führt ein GET-Request gegen den gegebenen Pfad aus
	 *
	 * @param <T>       generischer Typ des {@link HttpResponse} und {@link BodyHandler}
	 * @param path      der Pfad als Teil der URL für diesen OauthClient, an den das GET geschickt wird
	 * @param handler   der BodyHandler für den Response-Body
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public <T> HttpResponse<T> get(final String path, final BodyHandler<T> handler) throws ApiOperationException {
		logger.logLn("Bereite die HTTP-Anfrage vor...");
		final URI uri = URI.create(dto.AuthServer + path);
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2)).GET()
				.header("Accept", "application/json")
				.header("Authorization", "Bearer " + dto.Token).build();
		logger.logLn("Sende die HTTP-Anfrage an den OAuth2-Server...");
		return send(request, handler);
	}


	/**
	 * Führt ein GET-Request gegen den gegebenen Pfad ohne Authorization-Header aus
	 *
	 * @param <T>       generischer Typ des {@link HttpResponse} und {@link BodyHandler}
	 * @param path      der Pfad als Teil der URL für diesen OauthClient, an den das GET geschickt wird
	 * @param handler   der BodyHandler für den Response-Body
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public <T> HttpResponse<T> getUnauthorized(final String path, final BodyHandler<T> handler) throws ApiOperationException {
		logger.logLn("Bereite die HTTP-Anfrage vor...");
		final URI uri = URI.create(dto.AuthServer + path);
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2)).GET()
				.header("Accept", "application/json").build();
		logger.logLn("Sende die HTTP-Anfrage an den OAuth2-Server...");
		return send(request, handler);
	}


	/**
	 * Prüft das TLS-Zertifikat des Servers mithilfe eines TLS-Handshakes. Weicht dieses von den bisherigen Informationen ab,
	 * so wird das neue Zertifikat in das Datenbank-DTO geschrieben und persistiert.
	 *
	 * @return true, falls das Zertifikat erfolgreich validiert wurde. Ansonsten false.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public boolean checkCertificate() throws ApiOperationException {
		try {
			final List<X509Certificate> chain = new ArrayList<>();
			boolean isTrusted;
			isTrusted = TLSUtils.queryServerCertificates(dto.AuthServer, chain);
			// Fehlerbehandlung: Hat der Server ein Zertifikat zurückgegeben?
			if (chain.isEmpty())
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Kein gültiges Server-Zertifikat erhalten.");
			final List<X509Certificate> dtoChain = (dto.TLSCert == null) ? null : TLSUtils.decodeCertListJson(dto.TLSCert);
			// Prüfe, ob das Zertifikat in der DB gespeichert ist, dann ist relevant, ob diesem vertraut wird
			if ((dtoChain != null) && (!dtoChain.isEmpty()) && (chain.getFirst().equals(dtoChain.getFirst())))
				return dto.TLSCertIsTrusted;
			// Im anderen Fall - kein Zertifikat in der Datenbank hinterlegt ist oder es hat sich geändert,
			// dann muss dieses einfach nur in der DB eingetragen werden.
			dto.TLSCert = TLSUtils.encodeCertListJson(chain);
			dto.TLSCertIsKnown = isTrusted;
			dto.TLSCertIsTrusted = isTrusted;
			conn.transactionPersist(dto);
			return isTrusted;
		} catch (final SSLException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim TLS-Handshake.");
		} catch (final CertificateException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Auslesen der Server-Zertifikate aus dem TLS-Handshake.");
		}
	}


}
