package de.svws_nrw.controller.gost;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import de.svws_nrw.base.compression.CompressionException;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostJahrgangFachwahlen;
import de.svws_nrw.core.data.gost.GostJahrgangFachwahlenHalbjahr;
import de.svws_nrw.core.data.gost.GostSchuelerFachwahl;
import de.svws_nrw.core.data.gost.GostSchuelerGKLWahl;
import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1;
import de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2;
import de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2Schueler;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.Responses;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.benutzer.BenutzerKompetenzService;
import de.svws_nrw.service.gost.GostAbiturdatenService;
import de.svws_nrw.service.gost.GostFachwahlService;
import de.svws_nrw.service.gost.GostJahrgangFachwahlService;
import de.svws_nrw.service.gost.GostLaufbahnplanungExportV2Service;
import de.svws_nrw.service.gost.GostLaufbahnplanungImportV1Service;
import de.svws_nrw.service.gost.GostLaufbahnplanungImportV2Service;
import de.svws_nrw.service.gost.GostSchuelerGKLWahlService;
import de.svws_nrw.base.compression.Zip;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe im Bereich Laufbahnplanung der Gymnasialen Oberstufe gebündelt
 */
public final class GostLaufbahnplanungControllerImpl implements GostLaufbahnplanungController {

	/** Der Service für den Zugriff auf die Benutzer-Kompetenzen des aktuellen Benutzers. */
	private final BenutzerKompetenzService benutzerKompetenzService;

	/** Der Service für den Zugriff auf die Abiturdaten aus der aktuellen Schülerlaufbahn und der Laufbahnplanung heraus. */
	private final GostAbiturdatenService gostAbiturdatenService;

	/** Der Service für den Zugriff auf die Fachwahlen der Laufbahnplanung. */
	private final GostFachwahlService gostFachwahlService;

	/** Der Service für den Zugriff auf die Wahlen zu den Gleichwertig Komplexen Lernleistungen */
	private final GostSchuelerGKLWahlService gostSchuelerGKLWahlService;

	/** Der Service für den Zugriff auf die aggregierten Fachwahlen aus den Laufbahnplanungen eines Abiturjahrgangs. */
	private final GostJahrgangFachwahlService gostJahrgangFachwahlService;

	/** Der Service für den Import mit dem Export-Format in Version 1 für die Laufbahnplanung. */
	private final GostLaufbahnplanungImportV1Service gostLaufbahnplanungImportV1Service;

	/** Der Service für den Export mit dem Export-Format in Version 2 für die Laufbahnplanung. */
	private final GostLaufbahnplanungExportV2Service gostLaufbahnplanungExportV2Service;

	/** Der Service für den Import mit dem Export-Format in Version 2 für die Laufbahnplanung. */
	private final GostLaufbahnplanungImportV2Service gostLaufbahnplanungImportV2Service;


	/**
	 * Erstellt für  die Datenbank-Verbindung eine neue Controller-Instanz.
	 *
	 * @param benutzerKompetenzService             der Service für den Zugriff auf die Benutzer-Kompetenzen des aktuellen Benutzers
	 * @param gostAbiturdatenService               der Service für den Zugriff auf die Abiturdaten aus der aktuellen Schülerlaufbahn und der Laufbahnplanung heraus
	 * @param gostFachwahlService                  der Service für den Zugriff auf die Fachwahlen der Laufbahnplanung
	 * @param gostSchuelerGKLWahlService           der Service für den Zugriff auf die Wahlen zu den Gleichwertig Komplexen Lernleistungen
	 * @param gostJahrgangFachwahlService          der Service für den Zugriff auf die aggregierten Fachwahlen aus den Laufbahnplanungen eines Abiturjahrgangs
	 * @param gostLaufbahnplanungImportV1Service   der Service für den Import mit dem Export-Format in Version 1 für die Laufbahnplanung
	 * @param gostLaufbahnplanungExportV2Service   der Service für den Export mit dem Export-Format in Version 2 für die Laufbahnplanung
	 * @param gostLaufbahnplanungImportV2Service   der Service für den Import mit dem Export-Format in Version 2 für die Laufbahnplanung
	 */
	public GostLaufbahnplanungControllerImpl(final BenutzerKompetenzService benutzerKompetenzService,
			final GostAbiturdatenService gostAbiturdatenService,
			final GostFachwahlService gostFachwahlService,
			final GostSchuelerGKLWahlService gostSchuelerGKLWahlService,
			final GostJahrgangFachwahlService gostJahrgangFachwahlService,
			final GostLaufbahnplanungImportV1Service gostLaufbahnplanungImportV1Service,
			final GostLaufbahnplanungExportV2Service gostLaufbahnplanungExportV2Service,
			final GostLaufbahnplanungImportV2Service gostLaufbahnplanungImportV2Service) {
		this.benutzerKompetenzService = benutzerKompetenzService;
		this.gostAbiturdatenService = gostAbiturdatenService;
		this.gostFachwahlService = gostFachwahlService;
		this.gostSchuelerGKLWahlService = gostSchuelerGKLWahlService;
		this.gostJahrgangFachwahlService = gostJahrgangFachwahlService;
		this.gostLaufbahnplanungImportV1Service = gostLaufbahnplanungImportV1Service;
		this.gostLaufbahnplanungExportV2Service = gostLaufbahnplanungExportV2Service;
		this.gostLaufbahnplanungImportV2Service = gostLaufbahnplanungImportV2Service;
	}


	/**
	 * Gibt die Abiturdaten für den angegebenen Schüler zurück.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Response mit den Abiturdaten
	 */
	@Override
	public Response getBySchuelerID(final long idSchueler) {
		final Abiturdaten daten = gostAbiturdatenService.get(idSchueler);
		benutzerKompetenzService.pruefeKompetenzLaufbahnplanung(daten.abiturjahr);
		return Responses.ok(daten);
	}


	@Override
	public Response getListByAbiturjahrgang(final int abiturjahrgang) {
		final List<Abiturdaten> daten = gostAbiturdatenService.getListByAbiturjahrgang(abiturjahrgang);
		return Responses.ok(daten);
	}


	/**
	 * Gibt die Fachwahlen des angegebenen Schülers für das angebene Fach zurück.
	 *
	 * @param idSchueler   die ID des Schülers
	 * @param idFach       die ID des Faches
	 *
	 * @return die Fachwahlen
	 */
	@Override
	public Response getFachwahl(final long idSchueler, final long idFach) {
		final GostSchuelerFachwahl daten = gostFachwahlService.get(idSchueler, idFach);
		return Responses.ok(daten);
	}



	/**
	 * Führt einen Patch auf die Fachwahlen des angegebenen Schülers für das angebene Fach durch.
	 *
	 * @param idSchueler   die ID des Schülers
	 * @param idFach       die ID des Faches
	 * @param is           der Patch
	 *
	 * @return die Response
	 */
	@Override
	public Response patchFachwahl(final Long idSchueler, final Long idFach, final InputStream is) {
		gostFachwahlService.patch(idSchueler, idFach, is);
		return Responses.ok(null);
	}


	/**
	 * Gibt die Wahlen des angegebenen Schülers zu den Gleichwertig Komplexen Lernleistunen zurück.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Wahlen des Schülers zu den Gleichwertig Komplexen Lernleistunen
	 */
	@Override
	public Response getGKLWahl(final long idSchueler) {
		final GostSchuelerGKLWahl daten = gostSchuelerGKLWahlService.get(idSchueler);
		return Responses.ok(daten);
	}

	/**
	 * Setzt die Wahlen eines Schülers zu den Gleichwertig Komplexen Lernleistunen (GKL).
	 *
	 * @param wahl         die Wahl des Schülers zu den GKLs
	 *
	 * @return die Response
	 */
	@Override
	public Response putGKLWahl(final GostSchuelerGKLWahl wahl) {
		gostSchuelerGKLWahlService.put(wahl);
		return Responses.noContent();
	}


	/**
	 * Setzt die Fachwahlen für den angegebenen Schüler zurück.
	 * Liegen bereits bewertete Halbjahre vor, so werden die zukünftigen Fachwahlen entfernt.
	 * Ansonsten wir die Vorlage für die Fachwahlen des Abiturjahrgangs übernommen.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Response
	 */
	@Override
	public Response reset(final long idSchueler) {
		gostFachwahlService.reset(idSchueler);
		return Responses.noContent();
	}

	/**
	 * Setzt die Fachwahlen bei allen (!) Schülern des angegebenen Abiturjahrgangs zurück.
	 *
	 * @param abijahr   der Abiturjahrgang
	 *
	 * @return die Response
	 */
	@Override
	public Response resetAbiturjahrgang(final Integer abijahr) {
		gostFachwahlService.resetAbiturjahrgang(abijahr);
		return Responses.noContent();
	}


	@Override
	public Response deleteFachwahlen(final List<Long> idsSchueler) {
		gostFachwahlService.delete(idsSchueler);
		return Responses.noContent();
	}


	/**
	 * Erstellt eine Export-Datei mit den Laufbahnplanungsdaten des angegebenen Schülers zur Bearbeitung in einem externen Tool.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Response mit der GZip-Komprimierten Laufbahnplanungs-Datei
	 */
	@Override
	public Response exportGZip(final long idSchueler) {
		final GostLaufbahnplanungExportV2 daten = gostLaufbahnplanungExportV2Service.get(idSchueler);
		final GostLaufbahnplanungExportV2Schueler schueler = daten.schueler.getFirst();
		final String filename =
				"Laufbahnplanung_%d_%s_%s_%s_%d.lp".formatted(daten.abiturjahr, daten.jahrgang, schueler.nachname, schueler.vorname, schueler.id);
		return JSONMapper.gzipFileResponseFromObject(daten, filename);
	}


	/**
	 * Erstellt Export-Dateien mit den Laufbahnplanungsdaten der angegebenen Schüler zur Bearbeitung in einem externen Tool.
	 * Die Dateien werden in einer ZIP-Datei gebündelt.
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die Response mit der ZIP-Datei mit den GZip-Komprimierten Laufbahnplanungs-Dateien
	 */
	@Override
	public Response exportGZip(final Collection<Long> idsSchueler) {
		// Bestimme die einzelnen Export-Dateien
		final List<GostLaufbahnplanungExportV2> gostLaufbahnplanungen = gostLaufbahnplanungExportV2Service.getList(idsSchueler);

		final Map<String, byte[]> fileNameToBytes = new HashMap<>();
		for (final GostLaufbahnplanungExportV2 gostLaufbahnplanung : gostLaufbahnplanungen) {
			final GostLaufbahnplanungExportV2Schueler schueler = gostLaufbahnplanung.schueler.getFirst();

			final String fileName = "Laufbahnplanung_%d_%s_%s_%s_%d.lp".formatted(gostLaufbahnplanung.abiturjahr, gostLaufbahnplanung.jahrgang, schueler.nachname, schueler.vorname, schueler.id);

			try {
				final var fileData = JSONMapper.gzipByteArrayFromObject(gostLaufbahnplanung);

				fileNameToBytes.put(fileName, fileData);
			} catch (final CompressionException e) {
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehler beim GZIP-Komprimieren der Daten.");
			}
		}

		final var zipBytes = Zip.createArchive(fileNameToBytes);

		return Response.ok(zipBytes)
				.header("Content-Disposition", "attachment; filename=\"Laufbahnplanungen.zip\"")
				.header("Content-Length", zipBytes.length)
				.build();
	}


	/**
	 * Erstellt den Export mit den Laufbahnplanungsdaten des angegebenen Schülers zur Bearbeitung in einem externen Tool.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Response mit den Laufbahnplanungsdaten
	 */
	@Override
	public Response exportJSON(final long idSchueler) {
		final GostLaufbahnplanungExportV2 daten = gostLaufbahnplanungExportV2Service.get(idSchueler);
		return Responses.ok(daten);
	}


	/**
	 * Importiert die Daten des Schülers mit der angegebenen ID aus den übergebenen
	 * Laufbahnplanungsdaten.
	 *
	 * @param multipart   die Laufbahnplanungsdaten als GZIP-Komprimierte JSONs
	 *
	 * @return die HTTP-Response mit dem Log
	 */
	@Override
	public Response importGostLaufbahnplanungGZip(final MultipartFormDataInput multipart) {
		try {
			final List<GostLaufbahnplanungExportV1> listImporteV1 = new ArrayList<>();
			final List<GostLaufbahnplanungExportV2> listImporteV2 = new ArrayList<>();
			final List<InputPart> l = multipart.getFormDataMap().get("data");
			for (final InputPart file : l) {
				try (InputStream input = file.getBody()) {
					final byte[] daten = input.readAllBytes();
					CompressionException error = null;
					try {
						final GostLaufbahnplanungExportV2 laufbahnplanungsdaten = JSONMapper.toObjectGZip(daten, GostLaufbahnplanungExportV2.class);
						listImporteV2.add(laufbahnplanungsdaten);
					} catch (final CompressionException e) {
						error = e;
					}
					if (error != null) {
						try {
							final GostLaufbahnplanungExportV1 laufbahnplanungsdaten = JSONMapper.toObjectGZip(daten, GostLaufbahnplanungExportV1.class);
							listImporteV1.add(laufbahnplanungsdaten);
						} catch (@SuppressWarnings("unused") final Exception e) {
							// Erhalte den Fehler vom ersten Versuch, da Version 2 der Standard ist
						}
					}
					if (error != null) {
						throw new CompressionException("Fehler beim Deserialisieren der Laufbahnplanungsdaten - Datei ist fehlerhaft.");
					}
				}
			}
			return importGostLaufbahnplanungInternal(logger -> {
				if (!listImporteV2.isEmpty()) {
					gostLaufbahnplanungImportV2Service.doImport(listImporteV2, logger);
				}
				if (!listImporteV1.isEmpty()) {
					gostLaufbahnplanungImportV1Service.doImport(listImporteV1, logger);
				}
			});
		} catch (final IOException | CompressionException e) {
			final Logger logger = new Logger();
			logger.copyConsumer(Logger.global());
			final LogConsumerList log = new LogConsumerList();
			logger.addConsumer(log);
			if (e instanceof IOException) {
				logger.log("Eine lp-Datei konnte nicht eingelesen werden: " + e.getMessage());
			} else {
				logger.log("Fehler beim Öffnen der Datei.");
				logger.log("Fehlernachricht: " + e.getMessage());
			}
			logger.logLn("Import konnte nicht gestartet werden.");
			final SimpleOperationResponse daten = new SimpleOperationResponse();
			daten.success = false;
			daten.log = log.getStrings();
			return Response.status(daten.success ? Status.OK : Status.CONFLICT).type(MediaType.APPLICATION_JSON).entity(daten).build();
		}
	}

	@Override
	public Response importGostLaufbahnplanungGZip(final byte[] data) {
		final List<GostLaufbahnplanungExportV1> importV1 = new ArrayList<>();
		final List<GostLaufbahnplanungExportV2> importV2 = new ArrayList<>();
		CompressionException error = null;
		try {
			importV2.add(JSONMapper.toObjectGZip(data, GostLaufbahnplanungExportV2.class));
		} catch (final CompressionException e) {
			error = e;
		}
		if (importV2.isEmpty()) {
			try {
				importV1.add(JSONMapper.toObjectGZip(data, GostLaufbahnplanungExportV1.class));
			} catch (@SuppressWarnings("unused") final CompressionException e) {
				// Erhalte den Fehler vom ersten Versuch, da Version 2 der Standard ist
			}
		}
		try {
			if (!importV2.isEmpty()) {
				return importGostLaufbahnplanungInternal(logger -> gostLaufbahnplanungImportV2Service.doImport(importV2.getFirst(), logger));
			}
			if (!importV1.isEmpty()) {
				return importGostLaufbahnplanungInternal(logger -> gostLaufbahnplanungImportV1Service.doImport(importV1.getFirst(), logger));
			}
			throw (error == null) ? new CompressionException("Unerwarteter Fehler beim Dekodieren der Laufbahnplanungsdaten.") : error;
		} catch (final CompressionException e) {
			final Logger logger = new Logger();
			logger.copyConsumer(Logger.global());
			final LogConsumerList log = new LogConsumerList();
			logger.addConsumer(log);
			logger.log("Fehler beim Öffnen der Datei.");
			logger.log("Fehlernachricht: " + e.getMessage());
			logger.logLn("Import konnte nicht gestartet werden.");
			final SimpleOperationResponse daten = new SimpleOperationResponse();
			daten.success = false;
			daten.log = log.getStrings();
			return Response.status(Status.CONFLICT).type(MediaType.APPLICATION_JSON).entity(daten).build();
		}
	}


	@Override
	public Response importGostLaufbahnplanungV1(final GostLaufbahnplanungExportV1 laufbahnplanungsdaten) {
		return importGostLaufbahnplanungInternal(logger -> gostLaufbahnplanungImportV1Service.doImport(laufbahnplanungsdaten, logger));
	}


	@Override
	public Response importGostLaufbahnplanungV2(final GostLaufbahnplanungExportV2 laufbahnplanungsdaten) {
		return importGostLaufbahnplanungInternal(logger -> gostLaufbahnplanungImportV2Service.doImport(laufbahnplanungsdaten, logger));
	}


	private static Response importGostLaufbahnplanungInternal(final Consumer<Logger> importer) {
		// Erstelle den Logger
		final Logger logger = new Logger();
		logger.copyConsumer(Logger.global());
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		final SimpleOperationResponse daten = new SimpleOperationResponse();
		daten.success = true;

		// Importiere die Daten...
		try {
			importer.accept(logger);
			logger.logLn("Import erfolgreich.");
			daten.log = log.getStrings();
			return Responses.ok(daten);
		} catch (final RuntimeException e) {
			if (!((e instanceof final ApiOperationException aoe) && ("Fehler wurde bereits protokolliert.".equals(aoe.getMessage())))) {
				logger.logLn("Unbekannter Fehler aufgetreten. Weitere Informationen werden im Server-Log ausgegeben.");
				Logger.global().logLn("Unerwarteter Fehler beim Import von Laufbahnplanungsdaten aufgetreten: " + e.getMessage());
			}
			logger.logLn("Import fehlgeschlagen.");
			daten.success = false;
			daten.log = log.getStrings();
			return Response.status(Status.CONFLICT).type(MediaType.APPLICATION_JSON).entity(daten).build();
		}
	}


	@Override
	public Response getJahrgangsFachwahlen(final int abiturjahrgang) {
		if (abiturjahrgang < 0) {
			throw new ApiOperationException(Status.NOT_FOUND, "Fachwahlen sind für den Vorlagen-Abiturjahrgang nicht verfügbar.");
		}
		final GostJahrgangFachwahlen daten = gostJahrgangFachwahlService.getSchuelerFachwahlen(abiturjahrgang);
		return Responses.ok(daten);
	}


	@Override
	public Response getJahrgangsFachwahlenForHalbjahr(final int abiturjahrgang, final int idHalbjahr) {
		if (abiturjahrgang < 0) {
			throw new ApiOperationException(Status.NOT_FOUND, "Fachwahlen sind für den Vorlagen-Abiturjahrgang nicht verfügbar.");
		}
		final GostJahrgangFachwahlenHalbjahr daten = gostJahrgangFachwahlService.getSchuelerFachwahlenHalbjahr(abiturjahrgang, GostHalbjahr.fromID(idHalbjahr));
		if (daten.fachwahlen.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND);
		}
		return Responses.ok(daten);
	}


	@Override
	public Response getJahrgangFachwahlStatistik(final int abiturjahrgang) {
		if (abiturjahrgang < 0) {
			throw new ApiOperationException(Status.NOT_FOUND, "Fachwahlen sind für den Vorlagen-Abiturjahrgang nicht verfügbar.");
		}
		final List<GostStatistikFachwahl> daten = gostJahrgangFachwahlService.getFachwahlStatistik(abiturjahrgang);
		if (daten == null) {
			throw new ApiOperationException(Status.NOT_FOUND);
		}
		return Responses.ok(daten);
	}

}
