package de.svws_nrw.service.enm;

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
import de.svws_nrw.data.oauth2.OAuth2Token;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulVerbindungen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.enm.NotenmodulVerbindungenRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Die Klasse dient der HTTP-Kommunikation zu einem Web-Notenmodul-Server.
 *
 * Hierzu zählt auch die OAuth2-basierte Kommunikation mit dem Server.
 * Informationen zu dem Token und den TLS-Informationen vom Server werden ggf.
 * automatisch in der SVWS-DB gespeichert.
 */
final class HttpENMServerConnection {

	/** Das Repository für den Zugriff auf die Notenmodul-Verbindungen */
	private final NotenmodulVerbindungenRepository repository;

	/** Der zu verwendende Logger */
	private final Logger logger;

	/** Das DTO mit den OAuth2-Informationen */
	private final DTONotenmodulVerbindungen dto;


	/**
	 * Erzeugt eine neuen Verbindung zu einem Notenmodul-Server und erneuert ggf. das aktuelle Token.
	 *
	 * @param repository      das Respository für den Zugriff auf die Notenmodul-Verbindungen
	 * @param logger          ein Logger für das Loggen der Kommunikation
	 * @param id              die ID der Verbindung zu dem Notenmodul-Server
	 * @param updateToken     gibt an, ob das Token überprüft und ggf. erneuert werden soll
	 * @param forceNewToken   gibt an, ob das Token bei einer Prüfung immer erneuert werden soll
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	HttpENMServerConnection(final NotenmodulVerbindungenRepository repository, final Logger logger, final long id, final boolean updateToken,
			final boolean forceNewToken) throws ApiOperationException {
		this.repository = repository;
		this.logger = logger;

		// Lese die Verbindungsdaten aus der Datenbank ein.
		logger.logLn("Lese die Verbindung mit der ID %d aus der Datenbank...".formatted(id));
		this.dto = repository.findById(id)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Es wurden keine Verbindung mit der ID %d gefunden.".formatted(id)));
		if ((dto.url == null) || dto.url.isBlank()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Bei der Verbindung wurde keine Server-URL angegeben.");
		}
		if ((dto.clientID == null) || dto.clientID.isBlank()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Bei der Verbindung wurde keine Client-ID für die Authentifizierung angegeben.");
		}
		dto.url = removeTrailingSlash(dto.url);
		if (updateToken) {
			if ((dto.clientSecret == null) || dto.clientSecret.isBlank()) {
				throw new ApiOperationException(Status.NOT_FOUND, "Bei der Verbindung wurde kein Client-Secret für die Authentifizierung angegeben.");
			}
			logger.logLn("Generiere den HTTP-Header für Basic-Auth bestehen aus der Client-ID als User und dem Client-Secret als Kennwort...");
			final String basicAuth = Base64.getEncoder().encodeToString((dto.clientID + ":" + dto.clientSecret).getBytes());
			if (forceNewToken) {
				logger.logLn("Ignoriere ein ggf. existierendes Token und fordere ein neues Token an...");
				requestToken(basicAuth, logger);
			} else {
				logger.logLn("Prüfe, ob ein bestehendes Token wiederverwendet werden kann...");
				if (isTokenValid()) {
					logger.logLn("Das Token ist noch gültig und wird erneut verwendet.");
				} else {
					logger.logLn("Es existiert kein gültiges Token und ein neues Token muss angefordert werden...");
					requestToken(basicAuth, logger);
				}
			}
		}
	}


	/**
	 * Entfernt von der übergebenen URL ein slash am Ende, sofern eines vorhanden ist.
	 *
	 * @param url   die URL
	 *
	 * @return die URL ohne trailing slash
	 */
	private static String removeTrailingSlash(final String url) {
		return (url != null && url.endsWith("/"))
				? url.substring(0, url.length() - 1)
				: url;
	}


	/**
	 * Gibt wieder, ob ein Token vorhanden ist, welches nicht abgelaufen ist
	 *
	 * @return true, wenn ein nicht abgelaufenes Token vorhanden ist, und ansonsten false
	 */
	boolean isTokenValid() {
		if (dto.token == null) {
			return false;
		}
		// Berechne die Zeit in Millisekunden, wann das Token abläuft
		final long tsExpiration = ((dto.tokenExpiresIn * 1000) + dto.tokenTimestamp);
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
	void requestToken(final String basicAuthString, final Logger logger) throws ApiOperationException {
		logger.logLn("Erstelle den HTTP-Header für die Token-Anfrage...");
		// Bereite des HTTP-Request vor...
		final String client_secret_b64 = basicAuthString;
		final URI uri = URI.create(dto.url + "/oauth/token");
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2))
				.POST(BodyPublishers.ofString("grant_type=client_credentials")).header("Content-Type", "application/x-www-form-urlencoded")
				.setHeader("Authorization", "Basic " + client_secret_b64).build();
		// ... sende den Request und warte auf die Antwort ...
		logger.logLn("Sende die HTTP-Anfrage für eines neues Token...");
		final HttpResponse<String> response = send(request, BodyHandlers.ofString());
		// ... prüfe, den Response-Code ...
		final int statusCode = response.statusCode();
		if (statusCode == 401) {
			throw new ApiOperationException(Status.BAD_GATEWAY, "Verbindung zu dem Server ergab 401 (Unauthorized)."
					+ " Die Client-ID und das Client-Secret sollten überprüft werden.");
		}
		if (statusCode == 500) {
			throw new ApiOperationException(Status.UNAUTHORIZED, "Verbindung zu dem Server ergab 500 (Internal Server Error)."
					+ " Die Client-ID und das Client-Secret sollten überprüft werden.");
		}
		if ((statusCode != 200) && (statusCode != 201)) {
			throw new ApiOperationException(Status.BAD_GATEWAY, "Verbindung zu dem Server mit dem OAuth2-Status-Code %d fehlgeschlagen.".formatted(statusCode));
		}
		// ... und validiere im Erfolgsfall die HTTP-Response
		final String stringResponse = response.body();
		try {
			// Wandle dafür die Anwort in ein Java-DTO um ...
			final ObjectMapper mapper = new ObjectMapper();
			mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
			final OAuth2Token token = mapper.readValue(stringResponse, OAuth2Token.class);
			// ... und speichere die Informationen zum Token in der Datenbank
			this.dto.tokenTimestamp = System.currentTimeMillis();
			this.dto.token = token.accessToken;
			this.dto.tokenExpiresIn = token.expiresIn;
			repository.update(this.dto);
			repository.flush();
			logger.logLn("Das Token wurde erfolgreich empfangen.");
		} catch (@SuppressWarnings("unused") final JsonProcessingException e) {
			throw new ApiOperationException(Status.BAD_GATEWAY, "Fehler in der Antwort des Servers:\n" + stringResponse);
		}
	}


	/**
	 * Entfernt das aktuelle Token für die Verbindung.
	 */
	public void removeToken() {
		logger.logLn("Entferne das Verbindungstoken...");
		this.dto.tokenTimestamp = null;
		this.dto.token = null;
		this.dto.tokenExpiresIn = null;
		repository.update(this.dto);
		repository.flush();
		logger.logLn("Das Token wurde entfernt.");
	}


	/**
	 * Methode zum Versenden eines HTTP-Requests mithilfe der Methode {@link #send(HttpRequest, BodyHandler)}.
	 *
	 * @param <T> generischer Typ der Response
	 * @param request   der zu sendende Request
	 * @param handler   der BodyHandler
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	<T> HttpResponse<T> send(final HttpRequest request, final BodyHandler<T> handler) throws ApiOperationException {
		HttpClient.Builder builder = HttpClient.newBuilder().version(Version.HTTP_1_1).connectTimeout(Duration.ofSeconds(20));
		if ((dto.serverTLSCertIsKnown == null) || (!dto.serverTLSCertIsKnown)) {
			try {
				if (dto.serverTLSCert == null) {
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
							"In der Datenbank ist keine TLS-Zertifikatskette des TLS-Servers zur Nutzung hinterlegt.");
				}
				final List<X509Certificate> certList = TLSUtils.decodeCertListJson(dto.serverTLSCert);
				if (certList.isEmpty()) {
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "In der Datenbank ist kein TLS-Zertifikat zur Nutzung hinterlegt.");
				}
				if (!Boolean.TRUE.equals(dto.serverTLSCertIsTrusted)) {
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
							"Der in der Datenbank zur Nutzung hinterlegten TLS-Zertifikatskette des TLS-Servers wird nicht vertraut.");
				}
				final KeyStore keystore = KeyStoreUtils.newKeystore();
				KeyStoreUtils.addCertificate(keystore, dto.url, certList.getFirst());
				final SSLContext sslContext = TLSUtils.getTLSContextFromKeystore(keystore);
				builder = builder.sslContext(sslContext);
			} catch (final ApiOperationException e) {
				throw e;
			} catch (final GeneralSecurityException e) {
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
	<T> HttpResponse<T> postMultipart(final String path, final String filename, final byte[] bytes, final BodyHandler<T> handler)
			throws ApiOperationException {
		logger.logLn("Bereite die HTTP-Anfrage vor...");
		final URI uri = URI.create(dto.url + path);
		final String actualBoundary = UUID.randomUUID().toString();
		final String boundary = "--" + actualBoundary;
		final byte[] contentDisposition = (boundary + "\r\nContent-Disposition: form-data; name=\"file\"; filename=\"" + filename + "\"\r\n\r\n").getBytes();
		final byte[] boundaryBytes = ("\r\n" + boundary + "--\r\n").getBytes();
		final byte[] c = new byte[contentDisposition.length + bytes.length + boundaryBytes.length];
		System.arraycopy(contentDisposition, 0, c, 0, contentDisposition.length);
		System.arraycopy(bytes, 0, c, contentDisposition.length, bytes.length);
		System.arraycopy(boundaryBytes, 0, c, contentDisposition.length + bytes.length, boundaryBytes.length);
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2))
				.POST(BodyPublishers.ofByteArray(c))
				.header("Content-Type", "multipart/form-data; boundary=" + actualBoundary)
				.header("Accept", "*/*")
				.header("Authorization", "Bearer " + dto.token).header("file", "file").build();
		logger.logLn("Sende die HTTP-Anfrage...");
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
	<T> HttpResponse<T> postEmpty(final String path, final BodyHandler<T> handler) throws ApiOperationException {
		logger.logLn("Bereite die HTTP-Anfrage vor...");
		final URI uri = URI.create(dto.url + path);
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2)).GET()
				.POST(BodyPublishers.ofString("")).header("Content-Type", "application/x-www-form-urlencoded")
				.header("Accept", "*/*")
				.header("Authorization", "Bearer " + dto.token).build();
		logger.logLn("Sende die HTTP-Anfrage...");
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
	<T> HttpResponse<T> put(final String path, final BodyHandler<T> handler, final String daten)
			throws ApiOperationException {
		logger.logLn("Bereite die HTTP-Anfrage vor...");
		final URI uri = URI.create(dto.url + path);
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2))
				.PUT(BodyPublishers.ofString(daten)).header("Content-Type", "application/json")
				.header("Accept", "*/*")
				.header("Authorization", "Bearer " + dto.token).build();
		logger.logLn("Sende die HTTP-Anfrage...");
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
	<T> HttpResponse<T> get(final String path, final BodyHandler<T> handler) throws ApiOperationException {
		logger.logLn("Bereite die HTTP-Anfrage vor...");
		final URI uri = URI.create(dto.url + path);
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2)).GET()
				.header("Accept", "*/*")
				.header("Authorization", "Bearer " + dto.token).build();
		logger.logLn("Sende die HTTP-Anfrage...");
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
	boolean checkCertificate() throws ApiOperationException {
		try {
			final List<X509Certificate> chain = new ArrayList<>();
			boolean isTrusted;
			isTrusted = TLSUtils.queryServerCertificates(dto.url, chain);
			// Fehlerbehandlung: Hat der Server ein Zertifikat zurückgegeben?
			if (chain.isEmpty()) {
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Kein gültiges Server-Zertifikat erhalten.");
			}
			final List<X509Certificate> dtoChain = (dto.serverTLSCert == null) ? null : TLSUtils.decodeCertListJson(dto.serverTLSCert);
			// Prüfe, ob das Zertifikat in der DB gespeichert ist, dann ist relevant, ob diesem vertraut wird
			if ((dtoChain != null) && (!dtoChain.isEmpty()) && (chain.getFirst().equals(dtoChain.getFirst()))) {
				return dto.serverTLSCertIsTrusted;
			}
			// Im anderen Fall - kein Zertifikat in der Datenbank hinterlegt ist oder es hat sich geändert,
			// dann muss dieses einfach nur in der DB eingetragen werden.
			dto.serverTLSCert = TLSUtils.encodeCertListJson(chain);
			dto.serverTLSCertIsKnown = isTrusted;
			dto.serverTLSCertIsTrusted = isTrusted;
			repository.update(dto);
			return isTrusted;
		} catch (final SSLException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim TLS-Handshake.");
		} catch (final CertificateException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Auslesen der Server-Zertifikate aus dem TLS-Handshake.");
		}
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
	<T> HttpResponse<T> getUnauthorized(final String path, final BodyHandler<T> handler) throws ApiOperationException {
		logger.logLn("Bereite die HTTP-Anfrage vor...");
		final URI uri = URI.create(dto.url + path);
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2)).GET()
				.header("Accept", "*/*").build();
		logger.logLn("Sende die HTTP-Anfrage...");
		return send(request, handler);
	}

}
