package de.svws_nrw.data.datenaustausch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.svws_nrw.base.untis.UntisGPU001;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.fach.FaecherListeEintrag;
import de.svws_nrw.core.data.kurse.KursListeEintrag;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.stundenplan.StundenplanListeEintragMinimal;
import de.svws_nrw.core.data.stundenplan.StundenplanSchiene;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.data.faecher.DataFaecherliste;
import de.svws_nrw.data.kataloge.DataKatalogRaeume;
import de.svws_nrw.data.kataloge.DataKatalogZeitraster;
import de.svws_nrw.data.kurse.DataKursliste;
import de.svws_nrw.data.lehrer.DataLehrerliste;
import de.svws_nrw.data.schule.DataSchuljahresabschnitte;
import de.svws_nrw.data.stundenplan.DataStundenplanRaeume;
import de.svws_nrw.data.stundenplan.DataStundenplanSchienen;
import de.svws_nrw.data.stundenplan.DataStundenplanZeitraster;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterricht;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtKlasse;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtLehrer;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtRaum;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtSchiene;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse stellt Methoden für den Import und Export von Kurs 42 - Blockungen
 * zur Vefügung.
 */
public final class DataUntis {

	private DataUntis() {
		throw new IllegalStateException("Instantiation of " + DataUntis.class.getName() + " not allowed");
	}

	private static void _importGPU001(final Logger logger, final DBEntityManager conn, final long idSchuljahresabschnitt,
			final String beginn, final String beschreibung, final List<UntisGPU001> unterrichte, final int wochentyp) {
		// Prüfe die ID des Schuljahreabschnitts
		logger.logLn("-> Prüfe, ob der Schuljahresabschnitt existiert... ");
		final Schuljahresabschnitt schuljahresabschnitt = DataSchuljahresabschnitte.getByID(conn, idSchuljahresabschnitt);
		if (schuljahresabschnitt == null) {
			logger.logLn(2, "[Fehler] - Die ID des Schuljahresabschnitt %d ist ungültig.".formatted(idSchuljahresabschnitt));
			throw OperationError.NOT_FOUND.exception("Die ID des Schuljahresabschnitt %d ist ungültig.".formatted(idSchuljahresabschnitt));
		}
		// Bestimme die Lehrer
		final Map<String, LehrerListeEintrag> mapLehrerByKuerzel = DataLehrerliste.getLehrerListe(conn).stream().collect(Collectors.toMap(l -> l.kuerzel, l -> l));
		// Bestimme die Fächer
		final Map<String, FaecherListeEintrag> mapFaecherByKuerzel = DataFaecherliste.getFaecherListe(conn).stream().collect(Collectors.toMap(f -> f.kuerzel, f -> f));
		// Bestimme die Klassen des Schuljahresabschnitts
		final List<DTOKlassen> klassen = conn.queryNamed("DTOKlassen.schuljahresabschnitts_id", schuljahresabschnitt.id, DTOKlassen.class);
		final Map<String, DTOKlassen> mapKlassenByKuerzel = klassen.stream().collect(Collectors.toMap(k -> k.Klasse, k -> k));
		// Bestimme die Kurse des Schuljahresabschnitts
		final List<KursListeEintrag> kurse = DataKursliste.getKursListenFuerAbschnitt(conn, idSchuljahresabschnitt, false);
		final HashMap2D<String, Long, KursListeEintrag> mapKurseByKuerzelUndJahrgang = new HashMap2D<>();
		for (final KursListeEintrag kurs : kurse)
			for (final long idJahrgang : kurs.idJahrgaenge)
				mapKurseByKuerzelUndJahrgang.put(kurs.kuerzel, idJahrgang, kurs);
		// Erstelle den neuen Stundenplan
		final long idStundenplan = conn.transactionGetNextID(DTOStundenplan.class);
		final DTOStundenplan dtoStundenplan = new DTOStundenplan(idStundenplan, idSchuljahresabschnitt, beginn, beschreibung, wochentyp);
		dtoStundenplan.Ende = "%04d-%02d-%02d".formatted(schuljahresabschnitt.schuljahr + 1, 7, 31);
		conn.transactionPersist(dtoStundenplan);
		conn.transactionFlush();
		// Übertrage den Katalog der Räume
		DataStundenplanRaeume.addRaeume(conn, dtoStundenplan, DataKatalogRaeume.getRaeume(conn));
		// Übertrage den Katalog mit dem Zeitraster
		DataStundenplanZeitraster.addZeitraster(conn, dtoStundenplan, DataKatalogZeitraster.getZeitraster(conn));
		// Erzeuge die Einträge für die Kurs-Schienen
		DataStundenplanSchienen.addSchienenFromKursliste(conn, idStundenplan, kurse);
        final List<StundenplanSchiene> schienen = DataStundenplanSchienen.getSchienen(conn, idStundenplan);
        final HashMap2D<Long, Integer, StundenplanSchiene> mapSchienen = new HashMap2D<>();
        for (final StundenplanSchiene schiene : schienen)
        	mapSchienen.put(schiene.idJahrgang, schiene.nummer, schiene);
		// Erzeuge die nötigen Einträge für den Stundenplan
        long next_uid = conn.transactionGetNextID(DTOStundenplanUnterricht.class);
        long next_lid = conn.transactionGetNextID(DTOStundenplanUnterrichtLehrer.class);
        long next_kid = conn.transactionGetNextID(DTOStundenplanUnterrichtKlasse.class);
        long next_rid = conn.transactionGetNextID(DTOStundenplanUnterrichtRaum.class);
        long next_sid = conn.transactionGetNextID(DTOStundenplanUnterrichtSchiene.class);
        for (final UntisGPU001 u : unterrichte) {
        	logger.logLn("-> Importiere Unterricht: " + u.toString());
			// Bestimme den Zeitraster-Eintrag des neuen Stundenplans
			final StundenplanZeitraster zeitraster = DataStundenplanZeitraster.getOrCreateZeitrasterEintrag(conn, idStundenplan, u.wochentag, u.stunde);
			// Bestimme die Klasse des Unterrichtes
			final DTOKlassen klasse = mapKlassenByKuerzel.get(u.klasseKuerzel);
			if (klasse == null) {
				logger.logLn(2, "[Fehler] - Die Klasse mit dem Kürzel %s konnte nicht in der Datenbank gefunden werden.".formatted(u.klasseKuerzel));
				throw OperationError.NOT_FOUND.exception("Die Klasse mit dem Kürzel %s konnte nicht in der Datenbank gefunden werden.".formatted(u.klasseKuerzel));
			}
			// Bestimme den Fachlehrer
			final LehrerListeEintrag lehrer = mapLehrerByKuerzel.get(u.lehrerKuerzel);
			if (lehrer == null) {
				logger.logLn(2, "[Fehler] - Der Lehrer mit dem Kürzel %s konnte nicht in der Datenbank gefunden werden.".formatted(u.lehrerKuerzel));
				throw OperationError.NOT_FOUND.exception("Der Lehrer mit dem Kürzel %s konnte nicht in der Datenbank gefunden werden.".formatted(u.lehrerKuerzel));
			}
			// Prüfe, ob es sich um Kursunterricht handelt
			final KursListeEintrag kurs = mapKurseByKuerzelUndJahrgang.getOrNull(u.fachKuerzel, klasse.Jahrgang_ID);
			if (kurs == null) {
				// Bestimme das Fach
				final FaecherListeEintrag fach = mapFaecherByKuerzel.get(u.fachKuerzel);
				if (fach == null) {
					logger.logLn(2, "[Fehler] - Das Fach mit dem Kürzel %s konnte nicht in der Datenbank gefunden werden.".formatted(u.fachKuerzel));
					throw OperationError.NOT_FOUND.exception("Das Fach mit dem Kürzel %s konnte nicht in der Datenbank gefunden werden.".formatted(u.fachKuerzel));
				}
				// Erstelle den Klassen-Unterricht ...
				final long uid = next_uid++;
				final DTOStundenplanUnterricht dtoUnterricht = new DTOStundenplanUnterricht(uid, zeitraster.id, 0, fach.id);
				dtoUnterricht.Kurs_ID = null;
				conn.transactionPersist(dtoUnterricht);
				conn.transactionFlush();
				// ... Lehrer ...
				conn.transactionPersist(new DTOStundenplanUnterrichtLehrer(next_lid++, uid, lehrer.id));
				// ... Klasse ...
				conn.transactionPersist(new DTOStundenplanUnterrichtKlasse(next_kid++, uid, klasse.ID));
				// ... Raum
				if (u.raumKuerzel != null)
					conn.transactionPersist(new DTOStundenplanUnterrichtRaum(next_rid++, uid, DataStundenplanRaeume.getOrCreateRaum(conn, idStundenplan, u.raumKuerzel).id));
			} else {
				// Erstelle den Kurs-Unterricht ...
				final long uid = next_uid++;
				final DTOStundenplanUnterricht dtoUnterricht = new DTOStundenplanUnterricht(uid, zeitraster.id, 0, kurs.idFach);
				dtoUnterricht.Kurs_ID = kurs.id;
				conn.transactionPersist(dtoUnterricht);
				conn.transactionFlush();
				// ... Lehrer ...
				conn.transactionPersist(new DTOStundenplanUnterrichtLehrer(next_lid++, uid, lehrer.id));
				// ... Schiene ...
				for (final long idJahrgang : kurs.idJahrgaenge) {
					for (final int schiene : kurs.schienen) {
						final StundenplanSchiene s = mapSchienen.getOrNull(idJahrgang, schiene);
						if (s == null)
							throw OperationError.INTERNAL_SERVER_ERROR.exception("Interner Fehler beim Anlegen der Schienen für den Kursunterricht des Stundenplans.");
						conn.transactionPersist(new DTOStundenplanUnterrichtSchiene(next_sid++, uid, s.id));
					}
				}
				// ... Raum
				if (u.raumKuerzel != null)
					conn.transactionPersist(new DTOStundenplanUnterrichtRaum(next_rid++, uid, DataStundenplanRaeume.getOrCreateRaum(conn, idStundenplan, u.raumKuerzel).id));
			}
			conn.transactionFlush();
		}
	}


    /**
     * Importiert die in dem Multipart übergebene Datei.
     *
     * @param conn        die Datenbank-Verbindung
     * @param multipart   der Multipart-Body mmit der Datei
     *
     * @return die HTTP-Response mit dem Log
     */
    public static Response importGPU001(final DBEntityManager conn, final UntisGPU001MultipartBody multipart) {
    	final Logger logger = new Logger();
    	final LogConsumerList log = new LogConsumerList();
		final SimpleOperationResponse daten = new SimpleOperationResponse();
    	logger.addConsumer(log);
    	logger.addConsumer(new LogConsumerConsole());
    	try {
        	// Erstelle aus dem Multipart den String mit dem Inhalt der Textdatei
            logger.logLn("Lese die Datensätze aus der Text-Datei ein.");
        	final String strGPU001 = new String(multipart.data, StandardCharsets.UTF_8);
        	final List<UntisGPU001> unterrichte = UntisGPU001.readCSV(strGPU001);
            final StundenplanListeEintragMinimal entry = new ObjectMapper().readValue(multipart.entry, StundenplanListeEintragMinimal.class);
	    	logger.logLn("Importiere den Stundenplan:");
	    	_importGPU001(logger, conn, entry.idSchuljahresabschnitt, entry.gueltigAb, entry.bezeichnung, unterrichte, 0);
			logger.logLn("  Import beendet");
		} catch (@SuppressWarnings("unused") final IOException e) {
			logger.logLn(2, "Fehler beim Einlesen der Datensätze.");
			daten.success = false;
			daten.log = log.getStrings();
			return Response.status(Status.CONFLICT).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (final WebApplicationException e) {
			logger.logLn("  [FEHLER] beim Import");
			daten.success = false;
			daten.log = log.getStrings();
			try (Response r = e.getResponse()) {
				final int status = r.getStatus();
				return Response.status(status).type(MediaType.APPLICATION_JSON).entity(daten).build();
			}
		}
		daten.success = true;
		daten.log = log.getStrings();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
    }

}
