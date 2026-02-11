package de.svws_nrw.data.enm;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest.BodyPublishers;
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
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.enm.ENMConfigResponse;
import de.svws_nrw.core.data.enm.ENMServerConfig;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.oauth2.OAuth2Token;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulVerbindungen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Die Klasse dient der HTTP-Kommunikation zu einem Web-Notenmodul-Server.
 *
 * Hierzu zählt auch die OAuth2-basierte Kommunikation mit dem Server.
 * Informationen zu dem Token und den TLS-Informationen vom Server werden ggf.
 * automatisch in der SVWS-DB gespeichert.
 */
public final class HttpENMServerConnection {

	/** Die Datenbank-Verbindung zur SVWS-DB, wo die Verbindungsinformationen hinterlegt sind */
	private final DBEntityManager conn;

	/** Der zu verwendende Logger */
	private final Logger logger;

	/** Das DTO mit den OAuth2-Informationen */
	private final DTONotenmodulVerbindungen dto;



	/**
	 * Erzeugt eine neuen Verbindung zu einem Notenmodul-Server und erneuert ggf. das aktuelle Token.
	 *
	 * @param conn            die Datenbankverbindung zur SVWS-DB, um die aktuell gespeicherten Verindungsdaten abzufragen
	 * @param logger          ein Logger für das Loggen der Kommunikation
	 * @param id              die ID der Verbindung zu dem Notenmodul-Server
	 * @param updateToken     gibt an, ob das Token überprüft und ggf. erneuert werden soll
	 * @param forceNewToken   gibt an, ob das Token bei einer Prüfung immer erneuert werden soll
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private HttpENMServerConnection(final DBEntityManager conn, final Logger logger, final long id, final boolean updateToken,
			final boolean forceNewToken) throws ApiOperationException {
		this.conn = conn;
		this.logger = logger;

		// Lese die Verbindungsdaten aus der Datenbank ein.
		logger.logLn("Lese die Verbindung mit der ID %d aus der Datenbank...".formatted(id));
		this.dto = conn.queryByKey(DTONotenmodulVerbindungen.class, id);
		if (this.dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurden keine Verbindung mit der ID %d gefunden.".formatted(id));
		if ((dto.url == null) || dto.url.isBlank())
			throw new ApiOperationException(Status.NOT_FOUND, "Bei der Verbindung wurde keine Server-URL angegeben.");
		if ((dto.clientID == null) || dto.clientID.isBlank())
			throw new ApiOperationException(Status.NOT_FOUND, "Bei der Verbindung wurde keine Client-ID für die Authentifizierung angegeben.");
		if (updateToken) {
			if ((dto.clientSecret == null) || dto.clientSecret.isBlank())
				throw new ApiOperationException(Status.NOT_FOUND, "Bei der Verbindung wurde kein Client-Secret für die Authentifizierung angegeben.");
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
	 * Gibt wieder, ob ein Token vorhanden ist, welches nicht abgelaufen ist
	 *
	 * @return true, wenn ein nicht abgelaufenes Token vorhanden ist, und ansonsten false
	 */
	private boolean isTokenValid() {
		if (dto.token == null)
			return false;
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
	private void requestToken(final String basicAuthString, final Logger logger) throws ApiOperationException {
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
		if (statusCode == 401)
			throw new ApiOperationException(Status.BAD_GATEWAY, "Verbindung zu dem Server ergab 401 (Unauthorized)."
					+ " Die Client-ID und das Client-Secret sollten überprüft werden.");
		if (statusCode == 500)
			throw new ApiOperationException(Status.UNAUTHORIZED, "Verbindung zu dem Server ergab 500 (Internal Server Error)."
					+ " Die Client-ID und das Client-Secret sollten überprüft werden.");
		if ((statusCode != 200) && (statusCode != 201))
			throw new ApiOperationException(Status.BAD_GATEWAY, "Verbindung zu dem Server mit dem OAuth2-Status-Code %d fehlgeschlagen.".formatted(statusCode));
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
			conn.transactionPersist(this.dto);
			conn.transactionFlush();
			logger.logLn("Das Token wurde erfolgreich empfangen.");
		} catch (@SuppressWarnings("unused") final JsonProcessingException e) {
			throw new ApiOperationException(Status.BAD_GATEWAY, "Fehler in der Antwort des Servers:\n" + stringResponse);
		}
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
	private <T> HttpResponse<T> send(final HttpRequest request, final BodyHandler<T> handler) throws ApiOperationException {
		HttpClient.Builder builder = HttpClient.newBuilder().version(Version.HTTP_1_1).connectTimeout(Duration.ofSeconds(20));
		if ((dto.serverTLSCertIsKnown == null) || (!dto.serverTLSCertIsKnown)) {
			try {
				if (dto.serverTLSCert == null)
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "In der Datenbank ist keine TLS-Zertifikatskette des TLS-Servers zur Nutzung hinterlegt.");
				final List<X509Certificate> certList = TLSUtils.decodeCertListJson(dto.serverTLSCert);
				if (certList.isEmpty())
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "In der Datenbank ist kein TLS-Zertifikat zur Nutzung hinterlegt.");
				if (!Boolean.TRUE.equals(dto.serverTLSCertIsTrusted))
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der in der Datenbank zur Nutzung hinterlegten TLS-Zertifikatskette des TLS-Servers wird nicht vertraut.");
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
	private <T> HttpResponse<T> postMultipart(final String path, final String filename, final byte[] bytes, final BodyHandler<T> handler)
			throws ApiOperationException {
		logger.logLn("Bereite die HTTP-Anfrage vor...");
		final URI uri = URI.create(dto.url + path);
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
	private <T> HttpResponse<T> postEmpty(final String path, final BodyHandler<T> handler) throws ApiOperationException {
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
	private <T> HttpResponse<T> put(final String path, final BodyHandler<T> handler, final String daten)
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
	private <T> HttpResponse<T> get(final String path, final BodyHandler<T> handler) throws ApiOperationException {
		logger.logLn("Bereite die HTTP-Anfrage vor...");
		final URI uri = URI.create(dto.url + path);
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2)).GET()
				.header("Accept", "*/*")
				.header("Authorization", "Bearer " + dto.token).build();
		logger.logLn("Sende die HTTP-Anfrage...");
		return send(request, handler);
	}


	/**
	 * Lädt die ENM-Daten über den gegebenen OAuthClient vom ENM-Server und mit dem gegebenen DataManager in die
	 * Datenbank
	 *
	 * @param conn     die Datenbank-Verbindung
	 * @param client   der OAuthClient
	 * @param logger   der Logger
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private static void downloadENMDaten(final DBEntityManager conn, final HttpENMServerConnection client, final Logger logger) throws ApiOperationException {
		logger.logLn("Sende die Anfrage zum Herunderladen der ENM-Daten von dem ENM-Server...");
		final HttpResponse<byte[]> httpResponse = client.get("/api/secure/export", BodyHandlers.ofByteArray());
		if (httpResponse.statusCode() != Status.OK.getStatusCode())
			throw new ApiOperationException(Status.BAD_GATEWAY, httpResponse.body());
		logger.logLn("Schreibe die neuen Daten aus ENM-Daten anhand der Zeitstempel in die Datenbank des SVWS-Servers...");
		DataENMDaten.importDatenGZip(conn, httpResponse.body());
	}


	/**
	 * Lädt die ENM-Daten beim ENM-Server hoch
	 *
	 * @param conn     die Datenbank-Verbindung
	 * @param client   der OAuth-Client zur Verbindung mit dem ENM
	 * @param logger   der Logger
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private static void uploadENMDaten(final DBEntityManager conn, final HttpENMServerConnection client, final Logger logger) throws ApiOperationException {
		logger.logLn("Bestimme die ENM-Daten aus der Datenbank des SVWS-Servers...");
		final byte[] daten = DataENMDaten.getAllGZIPBytes(conn);
		logger.logLn("Sende die ENM-Daten an den ENM-Server...");
		logger.modifyIndent(2);
		final HttpResponse<String> response = client.postMultipart("/api/secure/import", "json.gz", daten, BodyHandlers.ofString());
		logger.modifyIndent(-2);
		if (response.statusCode() != Status.OK.getStatusCode())
			throw new ApiOperationException(Status.BAD_GATEWAY, response.body());
		logger.logLn("ENM-Daten erfolgreich an den ENM-Server übertragen.");
	}


	/**
	 * Synchronisiert die Daten des Externen Notenmoduls (ENM) mit dem ENM-Server und lädt
	 * dabei diese als ZIP beim ENM hoch und anschließend wieder von diesem herunter und speichert
	 * diese in der Datenbank.
	 *
	 * @param conn           die Datenbank-Verbindung
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response synchronize(final DBEntityManager conn, final long idVerbindung) throws ApiOperationException {
		// Erstelle zunächst einen Logger für die Operation
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		// Führe den Upload und dann den Download aus und gib den Erfolg der Operation als SimpleOperationResponse mit einem Log zurück
		final SimpleOperationResponse sor = new SimpleOperationResponse();
		Status status = Status.OK;
		try {
			logger.logLn("Führe eine Synchronisation der Daten durch...");
			logger.modifyIndent(2);
			final HttpENMServerConnection client = new HttpENMServerConnection(conn, logger, idVerbindung, true, false);
			uploadENMDaten(conn, client, logger);
			downloadENMDaten(conn, client, logger);
			logger.logLn("Die Synchronisation wurde erfolgreich abgeschlossen.");
			sor.success = true;
			logger.setIndent(0);
		} catch (final Exception e) {
			status = Status.INTERNAL_SERVER_ERROR;
			if (e instanceof final ApiOperationException aoe) {
				status = aoe.getStatus();
			}
			logger.log("Fehler: " + e.getLocalizedMessage());
			sor.success = false;
			logger.setIndent(0);
		}
		// Gib den Erfolg der Operation als SimpleOperationResponse mit einem Log zurück
		sor.log = log.getStrings();
		return Response.status(status).type(MediaType.APPLICATION_JSON).entity(sor).build();
	}


	/**
	 * Lädt die ENM-Daten aus der Datenbank zu dem ENM-Server hoch.
	 *
	 * @param conn           die Datenbank-Verbindung
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response upload(final DBEntityManager conn, final long idVerbindung) throws ApiOperationException {
		// Erstelle zunächst einen Logger für die Operation
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		// Führe den Upload aus und gib den Erfolg der Operation als SimpleOperationResponse mit einem Log zurück
		final SimpleOperationResponse sor = new SimpleOperationResponse();
		Status status = Status.OK;
		try {
			logger.logLn("Führe einen Upload der Daten durch...");
			logger.modifyIndent(2);
			final HttpENMServerConnection client = new HttpENMServerConnection(conn, logger, idVerbindung, true, false);
			uploadENMDaten(conn, client, logger);
			logger.logLn("Der Upload wurde erfolgreich abgeschlossen.");
			sor.success = true;
			logger.setIndent(0);
		} catch (final Exception e) {
			status = Status.INTERNAL_SERVER_ERROR;
			if (e instanceof final ApiOperationException aoe) {
				status = aoe.getStatus();
			}
			logger.log("Fehler: " + e.getLocalizedMessage());
			sor.success = false;
			logger.setIndent(0);
		}
		// Gib den Erfolg der Operation als SimpleOperationResponse mit einem Log zurück
		sor.log = log.getStrings();
		return Response.status(status).type(MediaType.APPLICATION_JSON).entity(sor).build();
	}


	/**
	 * Importiert die ENM-Daten von dem ENM-Server und schreibt diese in die Datenbank.
	 *
	 * @param conn           die Datenbank-Verbindung
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response download(final DBEntityManager conn, final long idVerbindung) throws ApiOperationException {
		// Erstelle zunächst einen Logger für die Operation
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		// Führe den Download aus und gib den Erfolg der Operation als SimpleOperationResponse mit einem Log zurück
		final SimpleOperationResponse sor = new SimpleOperationResponse();
		Status status = Status.OK;
		try {
			logger.logLn("Führe einen Download der Daten durch...");
			logger.modifyIndent(2);
			final HttpENMServerConnection client = new HttpENMServerConnection(conn, logger, idVerbindung, true, false);
			downloadENMDaten(conn, client, logger);
			logger.logLn("Der Download wurde erfolgreich abgeschlossen.");
			sor.success = true;
			logger.setIndent(0);
		} catch (final Exception e) {
			status = Status.INTERNAL_SERVER_ERROR;
			if (e instanceof final ApiOperationException aoe) {
				status = aoe.getStatus();
			}
			logger.log("Fehler: " + e.getLocalizedMessage());
			sor.success = false;
			logger.setIndent(0);
		}
		// Gib den Erfolg der Operation als SimpleOperationResponse mit einem Log zurück
		sor.log = log.getStrings();
		return Response.status(status).type(MediaType.APPLICATION_JSON).entity(sor).build();
	}


	/**
	 * Entfernt die ENM-Daten von dem ENM-Server. Dabei werden auch die Benutzerdaten auf dem Server entfernt.
	 *
	 * @param conn           die Datenbank-Verbindung
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response truncate(final DBEntityManager conn, final long idVerbindung) throws ApiOperationException {
		// Erstelle zunächst einen Logger für die Operation
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		// Führe den Truncate aus und gib den Erfolg der Operation als SimpleOperationResponse mit einem Log zurück
		final SimpleOperationResponse sor = new SimpleOperationResponse();
		Status status = Status.OK;
		try {
			logger.logLn("Führe ein Truncate auf dem Server durch...");
			logger.modifyIndent(2);
			HttpENMServerConnection client = new HttpENMServerConnection(conn, logger, idVerbindung, true, false);
			HttpResponse<String> response = client.postEmpty("/api/secure/truncate", BodyHandlers.ofString());
			if (response.statusCode() == Status.UNAUTHORIZED.getStatusCode()) {
				logger.logLn("Das Token wurde vom Server abgelehnt. Erstelle eine Verbindung mit einem neuen Token.");
				client = new HttpENMServerConnection(conn, logger, idVerbindung, true, true);
				response = client.postEmpty("/api/secure/truncate", BodyHandlers.ofString());
			}
			if (response.statusCode() != Status.OK.getStatusCode())
				throw new ApiOperationException(Status.BAD_GATEWAY, response.body());
			logger.logLn("Die Truncate-Operation wurde erfolgreich abgeschlossen.");
			sor.success = true;
			logger.setIndent(0);
		} catch (final Exception e) {
			status = Status.INTERNAL_SERVER_ERROR;
			if (e instanceof final ApiOperationException aoe) {
				status = aoe.getStatus();
			}
			logger.log("Fehler: " + e.getLocalizedMessage());
			sor.success = false;
			logger.setIndent(0);
		}
		// Gib den Erfolg der Operation als SimpleOperationResponse mit einem Log zurück
		sor.log = log.getStrings();
		return Response.status(status).type(MediaType.APPLICATION_JSON).entity(sor).build();
	}

	/**
	 * Entfernt die ENM-Daten von dem ENM-Server.
	 *
	 * @param conn           die Datenbank-Verbindung
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response reset(final DBEntityManager conn, final long idVerbindung) throws ApiOperationException {
		// Erstelle zunächst einen Logger für die Operation
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		// Führe den Reset aus und gib den Erfolg der Operation als SimpleOperationResponse mit einem Log zurück
		final SimpleOperationResponse sor = new SimpleOperationResponse();
		Status status = Status.OK;
		try {
			logger.logLn("Führe ein Reset auf dem Server durch...");
			logger.modifyIndent(2);
			HttpENMServerConnection client = new HttpENMServerConnection(conn, logger, idVerbindung, true, false);
			HttpResponse<String> response = client.postEmpty("/api/secure/reset", BodyHandlers.ofString());
			if (response.statusCode() == Status.UNAUTHORIZED.getStatusCode()) {
				logger.logLn("Das Token wurde vom Server abgelehnt. Erstelle eine Verbindung mit einem neuen Token.");
				client = new HttpENMServerConnection(conn, logger, idVerbindung, true, true);
				response = client.postEmpty("/api/secure/reset", BodyHandlers.ofString());
			}
			if (response.statusCode() != Status.OK.getStatusCode())
				throw new ApiOperationException(Status.BAD_GATEWAY, response.body());
			logger.logLn("Die Reset-Operation wurde erfolgreich abgeschlossen.");
			sor.success = true;
			logger.setIndent(0);
		} catch (final Exception e) {
			status = Status.INTERNAL_SERVER_ERROR;
			if (e instanceof final ApiOperationException aoe) {
				status = aoe.getStatus();
			}
			logger.log("Fehler: " + e.getLocalizedMessage());
			sor.success = false;
			logger.setIndent(0);
		}
		// Gib den Erfolg der Operation als SimpleOperationResponse mit einem Log zurück
		sor.log = log.getStrings();
		return Response.status(status).type(MediaType.APPLICATION_JSON).entity(sor).build();
	}

	/**
	 * Prüft, ob der ENM-Server mit den hinterlegten Verbindungsdaten erreichbar ist.
	 *
	 * @param conn           die Datenbank-Verbindung
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response check(final DBEntityManager conn, final long idVerbindung) throws ApiOperationException {
		// Erstelle zunächst einen Logger für die Operation
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		// Führe den Login aus und gib den Erfolg der Operation als SimpleOperationResponse mit einem Log zurück
		final SimpleOperationResponse sor = new SimpleOperationResponse();
		Status status = Status.OK;
		try {
			logger.logLn("Prüft, ob der Endpunkt für einen Verbindungstest erreichbar ist...");
			logger.modifyIndent(2);
			HttpENMServerConnection client = new HttpENMServerConnection(conn, logger, idVerbindung, true, false);
			HttpResponse<String> response = client.get("/api/secure/check", BodyHandlers.ofString());
			if (response.statusCode() == Status.UNAUTHORIZED.getStatusCode()) {
				logger.logLn("Das Token wurde vom Server abgelehnt. Erstelle eine Verbindung mit einem neuen Token.");
				client = new HttpENMServerConnection(conn, logger, idVerbindung, true, true);
				response = client.get("/api/secure/check", BodyHandlers.ofString());
			}
			if (response.statusCode() != Status.OK.getStatusCode())
				throw new ApiOperationException(Status.BAD_GATEWAY, response.body());
			logger.logLn("Der Verbindungstest wurde erfolgreich durchgeführt.");
			sor.success = true;
			logger.setIndent(0);
		} catch (final Exception e) {
			status = Status.INTERNAL_SERVER_ERROR;
			if (e instanceof final ApiOperationException aoe) {
				status = aoe.getStatus();
			}
			logger.log("Fehler: " + e.getLocalizedMessage());
			sor.success = false;
			logger.setIndent(0);
		}
		// Gib den Erfolg der Operation als SimpleOperationResponse mit einem Log zurück
		sor.log = log.getStrings();
		return Response.status(status).type(MediaType.APPLICATION_JSON).entity(sor).build();
	}

	/**
	 * Holt die auf dem ENM-Server hintelegten Konfigurationselemente
	 *
	 * @param conn           die Datenbank-Verbindung
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response getENMServerConfig(final DBEntityManager conn, final long idVerbindung) throws ApiOperationException {
		// Erstelle zunächst einen Logger für die Operation
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		// Führe den Login aus und gib den Erfolg der Operation als ENMConfigResponse mit einem Log zurück
		final ENMConfigResponse res = new ENMConfigResponse();
		Status status = Status.OK;
		try {
			logger.logLn("Frage Serverkonfiguration an...");
			logger.modifyIndent(2);
			final HttpENMServerConnection client = new HttpENMServerConnection(conn, logger, idVerbindung, true, false);
			final HttpResponse<String> response = client.get("/api/secure/serverconfig", BodyHandlers.ofString());
			if (response.statusCode() != Status.OK.getStatusCode())
				throw new ApiOperationException(Status.BAD_GATEWAY, response.body());
			logger.logLn("Die Serverkonfiguration wurde erfolgreich abgefragt.");
			if (response.body() == null)
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Keine Daten vom Server erhalten.");
			res.config = JSONMapper.toObject(response.body().getBytes(), ENMServerConfig.class);
			res.success = true;
		} catch (final Exception e) {
			status = Status.INTERNAL_SERVER_ERROR;
			if (e instanceof final ApiOperationException aoe) {
				status = aoe.getStatus();
			}
			logger.log("Fehler: " + e.getLocalizedMessage());
			res.success = false;
		}
		logger.setIndent(0);
		// Gib den Erfolg der Operation als ENMConfigResponse mit einem Log zurück
		res.log = log.getStrings();
		return Response.status(status).type(MediaType.APPLICATION_JSON).entity(res).build();
	}


	/**
	 * Schreibt ein Konfigurationselement in die Serverkonfiguration oder in die Globale
	 * Client-Konfiguration des Servers.
	 *
	 * @param conn           die Datenbank-Verbindung
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 * @param is             der Input-Stream mit den Konfigurationsdaten
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response setENMServerConfigElement(final DBEntityManager conn, final long idVerbindung, final InputStream is) throws ApiOperationException {
		// Erstelle zunächst einen Logger für die Operation
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		// Führe den Login aus und gib den Erfolg der Operation als ENMConfigResponse mit einem Log zurück
		final SimpleOperationResponse res = new SimpleOperationResponse();
		Status status = Status.OK;
		try {
			logger.logLn("Schicke das Konfigurationselement an den Server...");
			logger.modifyIndent(2);
			final String element = JSONMapper.toJsonString(is);
			final HttpENMServerConnection client = new HttpENMServerConnection(conn, logger, idVerbindung, true, false);
			final HttpResponse<String> response = client.put("/api/secure/serverconfig", BodyHandlers.ofString(), element);
			if (response.statusCode() != Status.OK.getStatusCode())
				throw new ApiOperationException(Status.BAD_GATEWAY, response.body());
			logger.logLn("Das Konfigurationselement wurde erfolgreich gesetzt.");
			res.success = true;
		} catch (final Exception e) {
			status = Status.INTERNAL_SERVER_ERROR;
			if (e instanceof final ApiOperationException aoe) {
				status = aoe.getStatus();
			}
			logger.log("Fehler: " + e.getLocalizedMessage());
			res.success = false;
		}
		logger.setIndent(0);
		// Gib den Erfolg der Operation als SimpleOperationResponse mit einem Log zurück
		res.log = log.getStrings();
		return Response.status(status).type(MediaType.APPLICATION_JSON).entity(res).build();
	}



	/**
	 * Prüft das TLS-Zertifikat des Servers mithilfe eines TLS-Handshakes. Weicht dieses von den bisherigen Informationen ab,
	 * so wird das neue Zertifikat in das Datenbank-DTO geschrieben und persistiert.
	 *
	 * @return true, falls das Zertifikat erfolgreich validiert wurde. Ansonsten false.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private boolean checkCertificate() throws ApiOperationException {
		try {
			final List<X509Certificate> chain = new ArrayList<>();
			boolean isTrusted;
			isTrusted = TLSUtils.queryServerCertificates(dto.url, chain);
			// Fehlerbehandlung: Hat der Server ein Zertifikat zurückgegeben?
			if (chain.isEmpty())
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Kein gültiges Server-Zertifikat erhalten.");
			final List<X509Certificate> dtoChain = (dto.serverTLSCert == null) ? null : TLSUtils.decodeCertListJson(dto.serverTLSCert);
			// Prüfe, ob das Zertifikat in der DB gespeichert ist, dann ist relevant, ob diesem vertraut wird
			if ((dtoChain != null) && (!dtoChain.isEmpty()) && (chain.getFirst().equals(dtoChain.getFirst())))
				return dto.serverTLSCertIsTrusted;
			// Im anderen Fall - kein Zertifikat in der Datenbank hinterlegt ist oder es hat sich geändert,
			// dann muss dieses einfach nur in der DB eingetragen werden.
			dto.serverTLSCert = TLSUtils.encodeCertListJson(chain);
			dto.serverTLSCertIsKnown = isTrusted;
			dto.serverTLSCertIsTrusted = isTrusted;
			conn.transactionPersist(dto);
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
	private <T> HttpResponse<T> getUnauthorized(final String path, final BodyHandler<T> handler) throws ApiOperationException {
		logger.logLn("Bereite die HTTP-Anfrage vor...");
		final URI uri = URI.create(dto.url + path);
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2)).GET()
				.header("Accept", "*/*").build();
		logger.logLn("Sende die HTTP-Anfrage...");
		return send(request, handler);
	}


	/**
	 * Prüft, ob der ENM-Server bereits initialisiert ist und gleichzeitig, ob das TLS bekannt ist.
	 *
	 * @param conn           die Datenbank-Verbindung
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response setup(final DBEntityManager conn, final long idVerbindung) throws ApiOperationException {
		// Erstelle zunächst einen Logger für die Operation
		final Logger logger = new Logger();
		try {
			final HttpENMServerConnection client = new HttpENMServerConnection(conn, logger, idVerbindung, false, false);
			final boolean isTrusted = client.checkCertificate();
			if (!isTrusted)
				return Response.status(Status.CONFLICT).entity("Dem Zertifikat wird aktuell nicht vertraut.").build();
			final HttpResponse<String> response = client.getUnauthorized("/api/setup", BodyHandlers.ofString());
			if ((response.statusCode() != Status.NO_CONTENT.getStatusCode()) && (response.statusCode() != Status.CONFLICT.getStatusCode()))
				throw new ApiOperationException(Status.BAD_GATEWAY, response.body());
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(response.statusCode() == Status.NO_CONTENT.getStatusCode()).build();
		} catch (final Exception e) {
			if (e instanceof final ApiOperationException aoe)
				throw aoe;
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Unerwarteter Fehler aufgetreten: " + e.getMessage());
		}
	}

}
