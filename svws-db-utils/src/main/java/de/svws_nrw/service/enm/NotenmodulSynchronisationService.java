package de.svws_nrw.service.enm;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.io.InputStream;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.function.Consumer;

import de.svws_nrw.base.compression.CompressionException;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.enm.ENMConfigResponse;
import de.svws_nrw.core.data.enm.ENMServerConfig;
import de.svws_nrw.core.data.enm.v2.ENMv2Daten;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.enm.NotenmodulVerbindungenRepository;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

/**
 * Service für die Synchronisation von ENM-Daten mit externen Servern.
 */
public final class NotenmodulSynchronisationService {

	private final NotenmodulVerbindungenRepository repository;
	private final EnmV2GetService enmGetService;
	private final EnmV2ImportService enmImportService;

	/**
	 * Erstellt einen neuen Service für die Notenmodul-Verbindung zu einem externen Notenmodul-Server
	 *
	 * @param repository         das Repository für den Zugriff auf die Notenmodul-Verbindungen
	 * @param enmGetService      der Service zum Einlesen der ENM-Daten aus der SVWS-Datenbank
	 * @param enmImportService   der Service für den Import der ENM-Daten
	 */
	public NotenmodulSynchronisationService(final NotenmodulVerbindungenRepository repository, final EnmV2GetService enmGetService,
			final EnmV2ImportService enmImportService) {
		this.repository = repository;
		this.enmGetService = enmGetService;
		this.enmImportService = enmImportService;
	}


	/**
	 * Führt die übergebene Operation mit einem Log aus und gibt das Ergebnis der Operation in
	 * einer {@link SimpleOperationResponse} zurück.
	 *
	 * @param opName     die Bezeichnung der Operation für das Logging
	 * @param runnable   die Operation
	 *
	 * @return die {@link SimpleOperationResponse} mit dem Ergebnis
	 */
	private static SimpleOperationResponse executeWithLog(final String opName, final Consumer<Logger> runnable) {
		// Erstelle zunächst einen Logger für die Operation
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);

		// Erstelle eine SimpleOperationsresponse und fülle diese mit dem Ergebnis der Operation
		final var sor = new SimpleOperationResponse();
		Status status = Status.OK;
		try {
			logger.logLn("Starte " + opName + "...");
			logger.modifyIndent(2);
			runnable.accept(logger);
			logger.logLn(opName + " erfolgreich abgeschlossen.");
			sor.success = true;
			logger.setIndent(0);
			sor.log = log.getStrings();
			return sor;
		} catch (final Exception e) {
			status = Status.INTERNAL_SERVER_ERROR;
			if (e instanceof final ApiOperationException aoe) {
				status = aoe.getStatus();
			}
			logger.logLn("Fehler bei " + opName + ": " + e.getMessage());
			sor.success = false;
			logger.setIndent(0);
			sor.log = log.getStrings();
			throw new ApiOperationException(status, e, sor, MediaType.APPLICATION_JSON);
		}
	}


	/**
	 * Lädt die ENM-Daten über den gegebenen OAuthClient vom ENM-Server und mit dem gegebenen DataManager in die
	 * Datenbank
	 *
	 * @param client   der OAuthClient
	 * @param logger   der Logger
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private void downloadENMDaten(final HttpENMServerConnection client, final Logger logger) throws ApiOperationException {
		logger.logLn("Sende die Anfrage zum Herunderladen der ENM-Daten von dem ENM-Server...");
		final HttpResponse<byte[]> httpResponse = client.get("/api/secure/export", BodyHandlers.ofByteArray());
		if (httpResponse.statusCode() != Status.OK.getStatusCode()) {
			throw new ApiOperationException(Status.BAD_GATEWAY, httpResponse.body());
		}
		logger.logLn("Schreibe die neuen Daten aus ENM-Daten anhand der Zeitstempel in die Datenbank des SVWS-Servers...");

		try {
			this.enmImportService.applyLatest(JSONMapper.toObjectGZip(httpResponse.body(), ENMv2Daten.class));
		} catch (final CompressionException e) {
			throw new ApiOperationException(Status.BAD_REQUEST, e, "Die ENM-Daten konnten nicht mit GZip entpackt werden.");
		}
	}


	/**
	 * Lädt die ENM-Daten beim ENM-Server hoch
	 *
	 * @param client   der OAuth-Client zur Verbindung mit dem ENM
	 * @param logger   der Logger
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private void uploadENMDaten(final HttpENMServerConnection client, final Logger logger) throws ApiOperationException {
		try {
			logger.logLn("Bestimme die ENM-Daten aus der Datenbank des SVWS-Servers...");
			final ENMv2Daten enmDaten = enmGetService.get(null); // null für schulenweiten Export
			final byte[] daten = JSONMapper.gzipByteArrayFromObject(enmDaten);

			logger.logLn("Sende die ENM-Daten an den ENM-Server...");
			logger.modifyIndent(2);
			final HttpResponse<String> response = client.postMultipart("/api/secure/import", "json.gz", daten, BodyHandlers.ofString());
			logger.modifyIndent(-2);
			if (response.statusCode() != Status.OK.getStatusCode()) {
				throw new ApiOperationException(Status.BAD_GATEWAY, response.body());
			}

			logger.logLn("ENM-Daten erfolgreich an den ENM-Server übertragen.");
		} catch (final CompressionException ce) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, ce, "Fehler beim Komprimieren der ENM-Daten.");
		}
	}


	/**
	 * Synchronisiert die Daten des Externen Notenmoduls (ENM) mit dem ENM-Server und lädt
	 * dabei diese als ZIP beim ENM hoch und anschließend wieder von diesem herunter und speichert
	 * diese in der Datenbank.
	 *
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die {@link SimpleOperationResponse} der Operation
	 */
	public SimpleOperationResponse synchronize(final long idVerbindung) {
		return executeWithLog("Synchronisation", logger -> {
			final HttpENMServerConnection client = new HttpENMServerConnection(repository, logger, idVerbindung, true, false);
			uploadENMDaten(client, logger);
			downloadENMDaten(client, logger);
		});
	}


	/**
	 * Lädt die ENM-Daten aus der Datenbank zu dem ENM-Server hoch.
	 *
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die {@link SimpleOperationResponse} der Operation
	 */
	public SimpleOperationResponse upload(final long idVerbindung) {
		return executeWithLog("Upload", logger -> {
			final HttpENMServerConnection client = new HttpENMServerConnection(repository, logger, idVerbindung, true, false);
			uploadENMDaten(client, logger);
		});
	}


	/**
	 * Importiert die ENM-Daten von dem ENM-Server und schreibt diese in die Datenbank.
	 *
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die {@link SimpleOperationResponse} der Operation
	 */
	public SimpleOperationResponse download(final long idVerbindung) {
		return executeWithLog("Download", logger -> {
			final HttpENMServerConnection client = new HttpENMServerConnection(repository, logger, idVerbindung, true, false);
			downloadENMDaten(client, logger);
		});
	}


	/**
	 * Entfernt die ENM-Daten von dem ENM-Server. Dabei werden auch die Benutzerdaten auf dem Server entfernt.
	 *
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die {@link SimpleOperationResponse} der Operation
	 */
	public SimpleOperationResponse truncate(final long idVerbindung) {
		return transactional(() -> executeWithLog("Truncate", logger -> {
			HttpENMServerConnection client = new HttpENMServerConnection(repository, logger, idVerbindung, true, false);
			HttpResponse<String> response = client.postEmpty("/api/secure/truncate", BodyHandlers.ofString());
			if (response.statusCode() == Status.UNAUTHORIZED.getStatusCode()) {
				logger.logLn("Das Token wurde vom Server abgelehnt. Erstelle eine Verbindung mit einem neuen Token.");
				client = new HttpENMServerConnection(repository, logger, idVerbindung, true, true);
				response = client.postEmpty("/api/secure/truncate", BodyHandlers.ofString());
			}
			if (response.statusCode() != Status.OK.getStatusCode()) {
				throw new ApiOperationException(Status.BAD_GATEWAY, response.body());
			}
		}));
	}

	/**
	 * Entfernt die ENM-Daten von dem ENM-Server.
	 *
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die {@link SimpleOperationResponse} der Operation
	 */
	public SimpleOperationResponse reset(final long idVerbindung) {
		return transactional(() -> executeWithLog("Reset", logger -> {
			HttpENMServerConnection client = new HttpENMServerConnection(repository, logger, idVerbindung, true, false);
			HttpResponse<String> response = client.postEmpty("/api/secure/reset", BodyHandlers.ofString());
			if (response.statusCode() == Status.UNAUTHORIZED.getStatusCode()) {
				logger.logLn("Das Token wurde vom Server abgelehnt. Erstelle eine Verbindung mit einem neuen Token.");
				client = new HttpENMServerConnection(repository, logger, idVerbindung, true, true);
				response = client.postEmpty("/api/secure/reset", BodyHandlers.ofString());
			}
			if (response.statusCode() != Status.OK.getStatusCode()) {
				throw new ApiOperationException(Status.BAD_GATEWAY, response.body());
			}
		}));
	}

	/**
	 * Prüft, ob der ENM-Server mit den hinterlegten Verbindungsdaten erreichbar ist.
	 *
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die {@link SimpleOperationResponse} der Operation
	 */
	public SimpleOperationResponse check(final long idVerbindung) {
		return transactional(() -> executeWithLog("Verbindungstest", logger -> {
			HttpENMServerConnection client = new HttpENMServerConnection(repository, logger, idVerbindung, true, false);
			HttpResponse<String> response = client.get("/api/secure/check", BodyHandlers.ofString());
			if (response.statusCode() == Status.UNAUTHORIZED.getStatusCode()) {
				logger.logLn("Das Token wurde vom Server abgelehnt. Erstelle eine Verbindung mit einem neuen Token.");
				client = new HttpENMServerConnection(repository, logger, idVerbindung, true, true);
				response = client.get("/api/secure/check", BodyHandlers.ofString());
			}
			if (response.statusCode() != Status.OK.getStatusCode()) {
				throw new ApiOperationException(Status.BAD_GATEWAY, response.body());
			}
		}));
	}

	/**
	 * Holt die auf dem ENM-Server hintelegten Konfigurationselemente
	 *
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die {@link ENMConfigResponse} der Operation
	 */
	public ENMConfigResponse getENMServerConfig(final long idVerbindung) {
		return transactional(() -> {
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
				final HttpENMServerConnection client = new HttpENMServerConnection(repository, logger, idVerbindung, true, false);
				final HttpResponse<String> response = client.get("/api/secure/serverconfig", BodyHandlers.ofString());
				if (response.statusCode() != Status.OK.getStatusCode()) {
					throw new ApiOperationException(Status.BAD_GATEWAY, response.body());
				}
				logger.logLn("Die Serverkonfiguration wurde erfolgreich abgefragt.");
				if (response.body() == null) {
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Keine Daten vom Server erhalten.");
				}
				res.config = JSONMapper.toObject(response.body().getBytes(), ENMServerConfig.class);
				res.success = true;
				logger.setIndent(0);
				res.log = log.getStrings();
				return res;
			} catch (final Exception e) {
				status = Status.INTERNAL_SERVER_ERROR;
				if (e instanceof final ApiOperationException aoe) {
					status = aoe.getStatus();
				}
				logger.log("Fehler(" + status.getReasonPhrase() + "): " + e.getLocalizedMessage());
				res.success = false;
				logger.setIndent(0);
				res.log = log.getStrings();
				throw new ApiOperationException(status, e, res, MediaType.APPLICATION_JSON);
			}
		});
	}


	/**
	 * Schreibt ein Konfigurationselement in die Serverkonfiguration oder in die Globale
	 * Client-Konfiguration des Servers.
	 *
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 * @param is             der Input-Stream mit den Konfigurationsdaten
	 *
	 * @return die {@link SimpleOperationResponse} der Operation
	 */
	public SimpleOperationResponse setENMServerConfigElement(final long idVerbindung, final InputStream is) {
		return transactional(() -> executeWithLog("Setzen eines Konfigurationselements", logger -> {
			final String element = JSONMapper.toJsonString(is);
			final HttpENMServerConnection client = new HttpENMServerConnection(repository, logger, idVerbindung, true, false);
			final HttpResponse<String> response = client.put("/api/secure/serverconfig", BodyHandlers.ofString(), element);
			if (response.statusCode() != Status.OK.getStatusCode()) {
				throw new ApiOperationException(Status.BAD_GATEWAY, response.body());
			}
		}));
	}


	/**
	 * Prüft, ob der ENM-Server bereits initialisiert ist und gleichzeitig, ob das TLS bekannt ist.
	 *
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return das Ergebnis der Prüfung
	 */
	public Boolean setup(final long idVerbindung) {
		return transactional(() -> {
			final Logger logger = new Logger();
			try {
				final HttpENMServerConnection client = new HttpENMServerConnection(repository, logger, idVerbindung, false, false);
				final boolean isTrusted = client.checkCertificate();
				if (!isTrusted) {
					return false;
				}
				final HttpResponse<String> response = client.getUnauthorized("/api/setup", BodyHandlers.ofString());
				if ((response.statusCode() != Status.NO_CONTENT.getStatusCode()) && (response.statusCode() != Status.CONFLICT.getStatusCode())) {
					throw new ApiOperationException(Status.BAD_GATEWAY, response.body());
				}
				return response.statusCode() == Status.NO_CONTENT.getStatusCode();
			} catch (final Exception e) {
				if (e instanceof final ApiOperationException aoe) {
					throw aoe;
				}
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Unerwarteter Fehler aufgetreten: " + e.getMessage());
			}
		});
	}

}
