package de.svws_nrw.data.datenaustausch;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import de.svws_nrw.asd.data.RGBFarbe;
import de.svws_nrw.asd.data.fach.FachgruppeKatalogEintrag;
import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.asd.types.fach.Fachgruppe;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.base.LogUtils;
import de.svws_nrw.base.untis.UntisGPU001;
import de.svws_nrw.base.untis.UntisGPU002;
import de.svws_nrw.base.untis.UntisGPU003;
import de.svws_nrw.base.untis.UntisGPU004;
import de.svws_nrw.base.untis.UntisGPU005;
import de.svws_nrw.base.untis.UntisGPU006;
import de.svws_nrw.base.untis.UntisGPU010;
import de.svws_nrw.base.untis.UntisGPU015;
import de.svws_nrw.base.untis.UntisGPU017;
import de.svws_nrw.base.untis.UntisGPU019;
import de.svws_nrw.base.untis.UntisSchuelerBezeichner;
import de.svws_nrw.core.adt.LongArrayKey;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.LongAndStringLists;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.fach.FaecherListeEintrag;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKurs;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisSchiene;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionHjData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.core.data.stundenplan.StundenplanListeEintragMinimal;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanSchiene;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.core.utils.Map2DUtils;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.SimpleBinaryMultipartBody;
import de.svws_nrw.data.faecher.DataFaecherliste;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausuren;
import de.svws_nrw.data.jahrgaenge.DataJahrgangsdaten;
import de.svws_nrw.data.kataloge.DataKatalogRaeume;
import de.svws_nrw.data.kataloge.DataKatalogZeitraster;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.kurse.DataKurse;
import de.svws_nrw.data.lehrer.DataLehrerliste;
import de.svws_nrw.data.schule.DataSchuljahresabschnitte;
import de.svws_nrw.data.stundenplan.DataStundenplan;
import de.svws_nrw.data.stundenplan.DataStundenplanListe;
import de.svws_nrw.data.stundenplan.DataStundenplanPausenaufsichten;
import de.svws_nrw.data.stundenplan.DataStundenplanRaeume;
import de.svws_nrw.data.stundenplan.DataStundenplanSchienen;
import de.svws_nrw.data.stundenplan.DataStundenplanUnterricht;
import de.svws_nrw.data.stundenplan.DataStundenplanUnterrichtsverteilung;
import de.svws_nrw.data.stundenplan.DataStundenplanZeitraster;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogRaum;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterricht;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtKlasse;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtLehrer;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtRaum;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtSchiene;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse stellt Methoden für den Import und Export von Untis-Daten
 * zur Vefügung.
 */
public final class DataUntis {

	private DataUntis() {
		throw new IllegalStateException("Instantiation of " + DataUntis.class.getName() + " not allowed");
	}

	private static void _importGPU001(final Logger logger, final DBEntityManager conn, final long idSchuljahresabschnitt,
			final String beginn, final String beschreibung, final List<UntisGPU001> unterrichte, final int wochentyp, final boolean ignoreMissing)
			throws ApiOperationException {
		// Prüfe die ID des Schuljahreabschnitts
		logger.logLn("-> Prüfe, ob der Schuljahresabschnitt existiert... ");
		final Schuljahresabschnitt schuljahresabschnitt;
		try {
			schuljahresabschnitt = new DataSchuljahresabschnitte(conn).getByID(idSchuljahresabschnitt);
		} catch (final ApiOperationException e) {
			logger.logLn(2, "[Fehler] - Die ID des Schuljahresabschnitt %d ist ungültig.".formatted(idSchuljahresabschnitt));
			throw e;
		}
		// Bestimme die Lehrer
		final Map<String, LehrerListeEintrag> mapLehrerByKuerzel = new DataLehrerliste(conn).getLehrerListe(false).stream()
				.collect(Collectors.toMap(l -> l.kuerzel, l -> l));
		// Bestimme die Fächer
		final Map<String, FaecherListeEintrag> mapFaecherByKuerzel = new DataFaecherliste(conn).getFaecherListe(false).stream().collect(Collectors.toMap(f -> f.kuerzel, f -> f));
		// Bestimme die Klassen des Schuljahresabschnitts
		final List<DTOKlassen> klassen = conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, schuljahresabschnitt.id);
		final Map<String, DTOKlassen> mapKlassenByKuerzel = klassen.stream().collect(Collectors.toMap(k -> k.Klasse, k -> k));
		// Bestimme die Kurse des Schuljahresabschnitts
		final List<KursDaten> kurse = DataKurse.getKursListenFuerAbschnitt(conn, idSchuljahresabschnitt, false);
		final HashMap2D<String, Long, KursDaten> mapKurseByKuerzelUndJahrgang = new HashMap2D<>();
		for (final KursDaten kurs : kurse)
			for (final long idJahrgang : kurs.idJahrgaenge)
				mapKurseByKuerzelUndJahrgang.put(kurs.kuerzel, idJahrgang, kurs);
		// Prüfe den Beginn des Stundenplan - ist dieser evtl. nach dem Schuljahr des angegebenen Schuljahresabschnitts?
		final int schuljahr = DateUtils.getSchuljahrFromDateISO8601(beginn);
		if (schuljahr > schuljahresabschnitt.schuljahr) {
			logger.logLn(2, "[Fehler] - Das angegebene Startdatum %s liegt nach dem Schuljahr %d des angegebenen Schuljahresabschnitts"
					.formatted(beginn, schuljahresabschnitt.schuljahr) + " und ist damit unzulässig.");
			throw new ApiOperationException(Status.CONFLICT, "Das angegebene Startdatum %s liegt nach dem Schuljahr %d des angegebenen Schuljahresabschnitts"
					.formatted(beginn, schuljahresabschnitt.schuljahr) + " und ist damit unzulässig.");
		}
		if (schuljahr < schuljahresabschnitt.schuljahr) {
			logger.logLn(2, "[Fehler] - Das angegebene Startdatum %s liegt vor dem Schuljahr %d des angegebenen Schuljahresabschnitts und ist damit unzulässig."
					.formatted(beginn, schuljahresabschnitt.schuljahr));
			throw new ApiOperationException(Status.CONFLICT, "Das angegebene Startdatum %s liegt vor dem Schuljahr %d des angegebenen Schuljahresabschnitts"
					.formatted(beginn, schuljahresabschnitt.schuljahr) + " und ist damit unzulässig.");
		}
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
		DataStundenplanSchienen.updateSchienenFromKursliste(conn, idStundenplan, kurse);
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
		final Map<LongArrayKey, Long> mapUnterrichte = new HashMap<>();
		int maxWochentyp = 0;
		for (final UntisGPU001 u : unterrichte) {
			logger.logLn("-> Importiere Unterricht: " + u.toString());
			// Bestimme den Zeitraster-Eintrag des neuen Stundenplans
			final StundenplanZeitraster zeitraster = DataStundenplanZeitraster.getOrCreateZeitrasterEintrag(conn, idStundenplan, u.wochentag, u.stunde);
			// Bestimme die Klasse des Unterrichtes
			final DTOKlassen klasse = mapKlassenByKuerzel.get(u.klasseKuerzel);
			if (klasse == null) {
				logger.logLn(2, "[Fehler] - Die Klasse mit dem Kürzel %s konnte nicht in der Datenbank gefunden werden.".formatted(u.klasseKuerzel));
				if (ignoreMissing) {
					logger.logLn(2, "Der Unterrichts-Eintrag wird ignoriert.");
					continue;
				}
				throw new ApiOperationException(Status.NOT_FOUND, "Die Klasse mit dem Kürzel %s konnte nicht in der Datenbank gefunden werden."
						.formatted(u.klasseKuerzel));
			}
			// Bestimme den Fachlehrer
			final LehrerListeEintrag lehrer = mapLehrerByKuerzel.get(u.lehrerKuerzel);
			if ((u.lehrerKuerzel != null) && (lehrer == null)) {
				logger.logLn(2, "[Fehler] - Der Lehrer mit dem Kürzel %s konnte nicht in der Datenbank gefunden werden.".formatted(u.lehrerKuerzel));
				if (ignoreMissing) {
					logger.logLn(2, "Der Unterrichts-Eintrag wird ignoriert.");
					continue;
				}
				throw new ApiOperationException(Status.NOT_FOUND,
						"Der Lehrer mit dem Kürzel %s konnte nicht in der Datenbank gefunden werden.".formatted(u.lehrerKuerzel));
			}
			// Prüfe, ob es sich um Kursunterricht handelt
			final KursDaten kurs = mapKurseByKuerzelUndJahrgang.getOrNull(u.fachKuerzel, klasse.Jahrgang_ID);
			if (kurs == null) {
				// Bestimme das Fach
				final FaecherListeEintrag fach = mapFaecherByKuerzel.get(u.fachKuerzel);
				if (fach == null) {
					logger.logLn(2, "[Fehler] - Das Fach bzw. der Kurs mit dem Kürzel %s konnte nicht in der Datenbank gefunden werden."
							.formatted(u.fachKuerzel));
					if (ignoreMissing) {
						logger.logLn(2, "Der Unterrichts-Eintrag wird ignoriert.");
						continue;
					}
					throw new ApiOperationException(Status.NOT_FOUND, "Das Fach bzw. der Kurs mit dem Kürzel %s konnte nicht in der Datenbank gefunden werden."
							.formatted(u.fachKuerzel));
				}
				// Prüfe, ob der Kursunterricht schon mit einem früheren Datensatz bearbeitet wurde
				int wt = 0;
				if ((u.wochentyp != null) && !u.wochentyp.isBlank()) {
					try {
						wt = Integer.valueOf(u.wochentyp);
						if ((wt < 0) || (wt > 4))
							wt = 0;
					} catch (@SuppressWarnings("unused") final NumberFormatException nfe) {
						wt = 0;
					}
				}
				final long[] tmpKey = { klasse.ID, fach.id, u.idUnterricht, u.wochentag, u.stunde, wt };
				final LongArrayKey key = new LongArrayKey(tmpKey);
				if (mapUnterrichte.containsKey(key)) {
					// Prüfe, ob der Lehrer bereits dem Unterricht zugeordnet ist, wenn nicht, dann füge diesen als weiteren Lehrer hinzu
					final long uid = mapUnterrichte.get(key);
					final List<DTOStundenplanUnterrichtLehrer> listLehrer =
							conn.queryList(DTOStundenplanUnterrichtLehrer.QUERY_BY_UNTERRICHT_ID + " AND e.Lehrer_ID = ?2",
									DTOStundenplanUnterrichtLehrer.class, uid, lehrer.id);
					if (listLehrer.isEmpty()) {
						logger.logLn(2, "Ergänze Lehrer %s zu bestehendem Klassen-Unterricht.".formatted(lehrer.kuerzel));
						conn.transactionPersist(new DTOStundenplanUnterrichtLehrer(next_lid++, uid, lehrer.id));
						conn.transactionFlush();
						continue;
					}
					// Wenn der Lehrer bereits vorhanden ist, dann gibt verwerfe den Eintrag
					logger.logLn(2,
							"Unterricht mit der ID %d wurde für das Fach '%s' der Klasse '%s' mit der ID %d bereits für den Wochentag %d und der Stunde %d mit Wochentyp %d mit dem Lehrer mit der ID %d hinzugefügt."
									.formatted(u.idUnterricht, fach.kuerzel, klasse.Klasse, klasse.ID, u.wochentag, u.stunde, wt, lehrer.id) + " Überspringe diesen Eintrag...");
					continue;
				}

				// Erstelle den Klassen-Unterricht ...
				final long uid = next_uid++;
				maxWochentyp = (maxWochentyp < wt) ? wt : maxWochentyp;
				final DTOStundenplanUnterricht dtoUnterricht = new DTOStundenplanUnterricht(uid, zeitraster.id, wt, fach.id);
				dtoUnterricht.Kurs_ID = null;
				conn.transactionPersist(dtoUnterricht);
				mapUnterrichte.put(key, uid);
				conn.transactionFlush();
				// ... Lehrer ...
				if (lehrer != null)
					conn.transactionPersist(new DTOStundenplanUnterrichtLehrer(next_lid++, uid, lehrer.id));
				// ... Klasse ...
				conn.transactionPersist(new DTOStundenplanUnterrichtKlasse(next_kid++, uid, klasse.ID));
				// ... Raum
				if (u.raumKuerzel != null)
					conn.transactionPersist(new DTOStundenplanUnterrichtRaum(next_rid++, uid,
							DataStundenplanRaeume.getOrCreateRaum(conn, idStundenplan, u.raumKuerzel).id));
			} else {
				// Prüfe, ob der Kursunterricht schon mit einem früheren Datensatz bearbeitet wurde
				int wt = 0;
				if ((u.wochentyp != null) && !u.wochentyp.isBlank()) {
					try {
						wt = Integer.valueOf(u.wochentyp);
						if ((wt < 0) || (wt > 4))
							wt = 0;
					} catch (@SuppressWarnings("unused") final NumberFormatException nfe) {
						wt = 0;
					}
				}
				final long[] tmpKey = { kurs.id, u.idUnterricht, u.wochentag, u.stunde, wt };
				final LongArrayKey key = new LongArrayKey(tmpKey);
				if (mapUnterrichte.containsKey(key)) {
					// Prüfe, ob der Lehrer bereits dem Unterricht zugeordnet ist, wenn nicht, dann füge diesen als weiteren Lehrer hinzu
					final long uid = mapUnterrichte.get(key);
					final List<DTOStundenplanUnterrichtLehrer> listLehrer =
							conn.queryList(DTOStundenplanUnterrichtLehrer.QUERY_BY_UNTERRICHT_ID + " AND e.Lehrer_ID = ?2",
									DTOStundenplanUnterrichtLehrer.class, uid, lehrer.id);
					if (listLehrer.isEmpty()) {
						logger.logLn(2, "Ergänze Lehrer %s zu bestehendem Kurs-Unterricht.".formatted(lehrer.kuerzel));
						conn.transactionPersist(new DTOStundenplanUnterrichtLehrer(next_lid++, uid, lehrer.id));
						conn.transactionFlush();
						continue;
					}
					// Wenn der Lehrer bereits vorhanden ist, dann gibt verwerfe den Eintrag
					logger.logLn(2,
							"Unterricht mit der ID %d wurde für den Kurs '%s' mit der ID %d bereits für den Wochentag %d und der Stunde %d mit Wochentyp %d mit dem Lehrer mit der ID %d hinzugefügt."
									.formatted(u.idUnterricht, kurs.kuerzel, kurs.id, u.wochentag, u.stunde, wt, lehrer.id) + " Überspringe diesen Eintrag...");
					continue;
				}
				// Erstelle den Kurs-Unterricht ...
				final long uid = next_uid++;
				maxWochentyp = (maxWochentyp < wt) ? wt : maxWochentyp;
				final DTOStundenplanUnterricht dtoUnterricht = new DTOStundenplanUnterricht(uid, zeitraster.id, wt, kurs.idFach);
				dtoUnterricht.Kurs_ID = kurs.id;
				conn.transactionPersist(dtoUnterricht);
				mapUnterrichte.put(key, uid);
				conn.transactionFlush();
				// ... Lehrer ...
				if (lehrer != null)
					conn.transactionPersist(new DTOStundenplanUnterrichtLehrer(next_lid++, uid, lehrer.id));
				// ... Schiene ...
				for (final long idJahrgang : kurs.idJahrgaenge) {
					for (final int schiene : kurs.schienen) {
						final StundenplanSchiene s = mapSchienen.getOrNull(idJahrgang, schiene);
						if (s == null)
							throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
									"Interner Fehler beim Anlegen der Schienen für den Kursunterricht des Stundenplans.");
						conn.transactionPersist(new DTOStundenplanUnterrichtSchiene(next_sid++, uid, s.id));
					}
				}
				// ... Raum
				if (u.raumKuerzel != null)
					conn.transactionPersist(new DTOStundenplanUnterrichtRaum(next_rid++, uid,
							DataStundenplanRaeume.getOrCreateRaum(conn, idStundenplan, u.raumKuerzel).id));
			}
			conn.transactionFlush();
		}
		// Passe ggf. das Wochentyp-Modell an, falls Unterrichte mit einem Wochentyp eingelesen wurden.
		if (maxWochentyp != 0) {
			logger.logLn("-> Setze das Wochentyp-Modell auf %d.".formatted(maxWochentyp));
			dtoStundenplan.WochentypModell = maxWochentyp;
			conn.transactionPersist(dtoStundenplan);
			conn.transactionFlush();
			// TODO Fasse alle erzeugten Unterrichte als WT 0 zusammen, die bis auf den Wochentyp identisch sind und alle Wochentypen abdecken.
		}
	}


	/**
	 * Importiert die in dem Multipart übergebene Datei.
	 *
	 * @param conn        die Datenbank-Verbindung
	 * @param multipart   der Multipart-Body mmit der Datei
	 * @param ignoreMissing   wenn true, dann werden fehlende Klassen und Kurse ignoriert
	 *                        und protokolliert, es wird aber kein Fehler erzeugt.
	 *
	 * @return die HTTP-Response mit dem Log
	 */
	public static Response importGPU001(final DBEntityManager conn, final UntisGPU001MultipartBody multipart, final boolean ignoreMissing) {
		final Logger logger = new Logger();
		logger.copyConsumer(Logger.global());
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		final SimpleOperationResponse daten = new SimpleOperationResponse();
		try {
			// Erstelle aus dem Multipart den String mit dem Inhalt der Textdatei
			logger.logLn("Lese die Datensätze aus der Text-Datei ein.");
			final List<UntisGPU001> unterrichte = UntisGPU001.readCSV(multipart.data);
			final StundenplanListeEintragMinimal entry = new ObjectMapper().readValue(multipart.entry, StundenplanListeEintragMinimal.class);
			logger.logLn("Importiere den Stundenplan:");
			_importGPU001(logger, conn, entry.idSchuljahresabschnitt, entry.gueltigAb, entry.bezeichnung, unterrichte, 0, ignoreMissing);
			logger.logLn("Import beendet");
		} catch (@SuppressWarnings("unused") final IOException e) {
			logger.logLn(2, "Fehler beim Einlesen der Datensätze.");
			daten.success = false;
			daten.log = log.getStrings();
			return Response.status(Status.CONFLICT).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (final ApiOperationException e) {
			logger.logLn(2, "[FEHLER] beim Import");
			daten.success = false;
			daten.log = log.getStrings();
			return Response.status(e.getStatus()).type(MediaType.APPLICATION_JSON).entity(daten).build();
		}
		daten.success = true;
		daten.log = log.getStrings();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Importiert die in dem Multipart übergebene Datei.
	 *
	 * @param conn        die Datenbank-Verbindung
	 * @param multipart   der Multipart-Body mmit der Datei
	 *
	 * @return die HTTP-Response mit dem Log
	 */
	public static Response importGPU005(final DBEntityManager conn, final SimpleBinaryMultipartBody multipart) {
		final Logger logger = new Logger();
		logger.copyConsumer(Logger.global());
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);

		boolean success = true;
		Status statusCode = Status.OK;
		try {
			logger.logLn("Importiere die Räume aus der Untis-Datei GPU005.txt:");
			importUntisRaeume(conn, logger, multipart.data);
			logger.logLn("  Import beendet");
		} catch (final Exception e) {
			success = false;
			if (e instanceof final ApiOperationException aoe) {
				statusCode = aoe.getStatus();
			} else {
				logger.logLn("  [FEHLER] Unerwarteter Fehler: " + e.getLocalizedMessage());
				statusCode = Status.INTERNAL_SERVER_ERROR;
			}
		}
		final SimpleOperationResponse daten = new SimpleOperationResponse();
		daten.success = success;
		daten.log = log.getStrings();
		return Response.status(statusCode).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Importiert Räume aus der Untis-Datei GPU005.txt in das Datenbank-Schema, welches durch die übergebene
	 * Verbindung festgelegt ist.
	 *
	 * @param conn     die Datenbank-Verbindung.
	 * @param logger   der Logger für Rückmeldungen zum Import-Prozess
	 * @param csv      die CSV-Datei mit den Räumen (GPU005.txt)
	 *
	 * @return true im Erfolgsfall und false im Fehlerfall
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static boolean importUntisRaeume(final DBEntityManager conn, final Logger logger, final byte[] csv) throws ApiOperationException {
		// Lese zunächst die Informationen zur Schule aus der SVWS-Datenbank und überprüfe die Schulform
		logger.logLn("-> Lese Informationen zu der Schule ein...");
		logger.modifyIndent(2);
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null) {
			logger.logLn("[Fehler] - Konnte die Informationen zur Schule nicht aus der Datenbank lesen");
			throw new ApiOperationException(Status.NOT_FOUND, "Konnte die Informationen zur Schule nicht aus der Datenbank lesen.");
		}
		logger.logLn("[OK]");
		logger.modifyIndent(-2);
		try {
			logger.logLn("-> Lese die Räume aus der CSV-Datei ein...");
			logger.modifyIndent(2);
			final List<UntisGPU005> raeume = UntisGPU005.readCSV(csv);
			logger.logLn("[OK]");
			logger.modifyIndent(-2);

			conn.transactionBegin();

			logger.logLn("-> Lese die bereits im Katalog vorhandenen Räume ein...");
			logger.modifyIndent(2);
			final Map<String, DTOKatalogRaum> raeumeVorhanden = conn.queryAll(DTOKatalogRaum.class).stream().collect(Collectors.toMap(r -> r.Kuerzel, r -> r));
			logger.logLn("[OK]");
			logger.modifyIndent(-2);

			logger.logLn("-> Schreibe die Räume...");
			logger.modifyIndent(2);
			long id = conn.transactionGetNextID(DTOKatalogRaum.class);
			for (final UntisGPU005 raum : raeume) {
				if (raum.kuerzel == null) {
					logger.logLn("[Fehler] - Konnte die Informationen zur Schule nicht aus der Datenbank lesen");
					logger.modifyIndent(-2);
					throw new ApiOperationException(Status.BAD_REQUEST, "Jeder Raum muss ein gültiges Kürzel haben.");
				}
				if (raeumeVorhanden.containsKey(raum.kuerzel)) {
					logger.logLn("Raum '%s' wird nicht übernommen, da er bereits vorhanden ist.".formatted(raum.kuerzel));
					continue;
				}
				final DTOKatalogRaum dto = new DTOKatalogRaum(id++, raum.kuerzel, (raum.bezeichnung == null) ? raum.kuerzel : raum.bezeichnung,
						(raum.groesse == null) ? 40 : raum.groesse);
				conn.transactionPersist(dto);
				logger.logLn("Raum '%s' hinzugefügt.".formatted(raum.kuerzel));
			}

			if (!conn.transactionCommit()) {
				logger.logLn("[Fehler] Unerwarteter Fehler beim Schreiben in die Datenbank.");
				logger.modifyIndent(-2);
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
			}
			logger.logLn("[OK]");
			logger.modifyIndent(-2);
			return true;
		} catch (final UnrecognizedPropertyException upe) {
			LogUtils.logStacktrace(logger, upe);
			logger.logLn("[Fehler] Konnte die Datei GPU005.txt nicht einlesen. Prüfen sie ggf. auch die Zeichenkodierung der Datei."
					+ " Diese muss UTF-8 (ohne BOM) sein.");
			logger.modifyIndent(-2);
			return false;
		} catch (@SuppressWarnings("unused") final Exception exception) {
			logger.logLn("[Fehler]");
			logger.modifyIndent(-2);
			return false;
		} finally {
			conn.transactionRollback();
		}
	}


	/**
	 * Gibt das Untis-Kürzel für das angegebene Geschlecht zurück oder null.
	 *
	 * @param geschlecht   das Geschlecht
	 *
	 * @return das Untis-Kürzel
	 */
	private static String getUntisGeschlecht(final Geschlecht geschlecht) {
		return switch (geschlecht) {
			case Geschlecht.M -> "2";
			case Geschlecht.W -> "1";
			default -> null;
		};
	}


	/**
	 * Gibt das im iso-Format angegebene Datum im Format für Untis zurück.
	 *
	 * @param isoDate   das Datum im ISO-Format
	 *
	 * @return das Datum im Untis-Format
	 */
	private static String getUntisDate(final String isoDate) {
		final LocalDate date = ((isoDate == null) || "".equals(isoDate)) ? null : LocalDate.parse(isoDate);
		return (date == null) ? null : "%04d%02d%02d".formatted(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
	}


	/**
	 * Bestimmt die für den Untis-Export verwendete Schulstufe anhand des übergebenen Jahrgangs.
	 *
	 * @param jg   der Jahrgang
	 *
	 * @return die Schulstufe
	 */
	private static String getSchulstufeByASDJahrgang(final Jahrgaenge jg) {
		if (jg == null)
			return null;
		return switch (jg) {
			case JAHRGANG_00, VORKURS_SEMESTER_1, VORKURS_SEMESTER_2 -> "00";
			case JAHRGANG_01, SEMESTER_01 -> "01";
			case JAHRGANG_02, SEMESTER_02 -> "02";
			case JAHRGANG_03, SEMESTER_03 -> "03";
			case JAHRGANG_04, SEMESTER_04 -> "04";
			case JAHRGANG_05, SEMESTER_05 -> "05";
			case JAHRGANG_06, SEMESTER_06 -> "06";
			case JAHRGANG_07 -> "07";
			case JAHRGANG_08 -> "08";
			case JAHRGANG_09 -> "09";
			case JAHRGANG_10 -> "10";
			case JAHRGANG_11, EF -> "11";
			case JAHRGANG_12, Q1 -> "12";
			case JAHRGANG_13, Q2 -> "13";
			default -> null;
		};
	}

	/**
	 * Erstellt für die übergebenen GPU002-Daten eine 2D-Map mit den Schlüsseln der Klassen- und Fach-Kürzeln auf die
	 * einzelnen Unterrichts-Einträge der GPU002
	 *
	 * @param gpu002List   die Liste der GPU002-Einträge
	 *
	 * @return die Map
	 */
	private static HashMap2D<String, String, List<UntisGPU002>> getMapUntisGPU002ByKlasseAndFach(final List<UntisGPU002> gpu002List) {
		final HashMap2D<String, String, List<UntisGPU002>> result = new HashMap2D<>();
		for (final UntisGPU002 entry : gpu002List)
			Map2DUtils.addToList(result, entry.klasseKuerzel, entry.fachKuerzel, entry);
		return result;
	}



	/**
	 * Bestimmt anhand der übergebenen Klassenliste, der übergebenen Jahrgangsinformationen, Klassenlehrer und Vorjahresklassen
	 * zu den Klassen die Liste der zugehörigen Klasseneinträge für die GPU003.TXT-Datei von Untis.
	 *
	 * @param klassen             die Klassen
	 * @param mapJahrgang         die Jahrgänge zu den Klassen
	 * @param mapKlassenlehrer    die Klassenlehrer zu den Klassen
	 *
	 * @return die Liste der GPU003-Einträge
	 */
	private static List<UntisGPU003> getListGPU003(final @NotNull List<DTOKlassen> klassen, final @NotNull Map<Long, DTOJahrgang> mapJahrgang,
			final @NotNull Map<Long, List<DTOLehrer>> mapKlassenlehrer) {
		final List<UntisGPU003> result = new ArrayList<>();
		for (final DTOKlassen klasse : klassen) {
			final UntisGPU003 dto = new UntisGPU003();
			dto.kuerzel = klasse.Klasse;
			dto.bezeichnung = klasse.Bezeichnung;
			dto.raumKuerzel = null; // Der Klassenraum für die Klasse
			dto.kennzeichen = null; // Das Untis-Kennzeichen der Klasse
			final DTOJahrgang dtoJg = mapJahrgang.get(klasse.ID);
			final Jahrgaenge jg = (dtoJg == null) ? null : Jahrgaenge.data().getWertBySchluessel(dtoJg.ASDJahrgang);
			dto.schulstufe = getSchulstufeByASDJahrgang(jg);
			dto.kuerzelVorjahr = klasse.VKlasse;
			final List<DTOLehrer> klLehrer = mapKlassenlehrer.get(klasse.ID);
			dto.kuerzelKlassenlehrer = ((klLehrer == null) || (klLehrer.isEmpty())) ? null
					: klLehrer.stream().map(l -> l.Kuerzel).collect(Collectors.joining(","));
			dto.kuerzelHauptklasse = ((dtoJg != null) && (jg != null) && jg.istGymOb()) ? dtoJg.ASDJahrgang : klasse.Klasse;
			result.add(dto);
		}
		return result;
	}


	/**
	 * Erstellt die GPU003-CSV-Daten für die übergebene Klassenliste und die übergebenen Zuordnungen.
	 *
	 * @param logger             der Logger
	 * @param klassen            die Klassen
	 * @param mapJahrgang        die Jahrgänge zu den Klassen
	 * @param mapKlassenlehrer   die Klassenlehrer zu den Klassen
	 *
	 * @return die CSV-Daten als UTF8-String
	 *
	 * @throws ApiOperationException   falls ein Fehler beim Erstellen der CSV-Daten entsteht
	 */
	private static @NotNull String getGPU003(final @NotNull Logger logger, final @NotNull List<DTOKlassen> klassen,
			final @NotNull Map<Long, DTOJahrgang> mapJahrgang, final @NotNull Map<Long, List<DTOLehrer>> mapKlassenlehrer) throws ApiOperationException {
		logger.logLn("-> Erstelle Liste der Klassen für GPU003.txt");
		logger.modifyIndent(2);
		try {
			final List<UntisGPU003> gpu003 = getListGPU003(klassen, mapJahrgang, mapKlassenlehrer);
			final String csvSchueler = UntisGPU003.writeCSV(gpu003);
			logger.logLn("OK");
			logger.modifyIndent(-2);
			return csvSchueler;
		} catch (final Exception e) {
			logger.logLn("Fehler beim Erstellen der Datei GPU003.txt.");
			logger.modifyIndent(-2);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen der Datei GPU003.txt.");
		}
	}


	/**
	 * Bestimmt die Export-Daten für die Klassenliste für den übergebenen Schuljahresabschnitt
	 *
	 * @param conn                     die aktuelle Datenbankverbindung
	 * @param logger                   der Logger zum Protokollieren des Exportes
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 *
	 * @return die GPU003.txt-Datei als String im UTF8-Format
	 *
	 * @throws ApiOperationException   wenn ein Fehler beim Erstellen des Exportes auftritt
	 */
	private static @NotNull String getGPU003bySchuljahresabschnitt(final @NotNull DBEntityManager conn, final @NotNull Logger logger,
			final long idSchuljahresabschnitt) throws ApiOperationException {
		logger.log("Ermittle GPU003-Daten für die Klassen...");
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetAbschnittById(idSchuljahresabschnitt);
		if (schuljahresabschnitt == null) {
			logger.logLn("-> FEHLER: Kein Schuljahresabschnitt mit der ID %d gefunden.".formatted(idSchuljahresabschnitt));
			throw new ApiOperationException(Status.NOT_FOUND,
					"In der Datenbank gibt es keinen Schuljahresabschnitt mit der ID %d.".formatted(idSchuljahresabschnitt));
		}
		logger.logLn("-> für den Schuljahresabschnitt %d.%d".formatted(schuljahresabschnitt.schuljahr, schuljahresabschnitt.abschnitt));
		final List<DTOKlassen> klassen = conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, schuljahresabschnitt.id);
		if (klassen.isEmpty()) {
			logger.logLn("-> keine Klassen für den Export gefunden");
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Klassen im Schuljahresabschnitt %d.%d für den Export gefunden."
					.formatted(schuljahresabschnitt.schuljahr, schuljahresabschnitt.abschnitt));
		}
		final List<Long> idsKlassen = klassen.stream().map(kl -> kl.ID).toList();
		logger.logLn("-> bestimme die zugehörigen Jahrgänge...");
		final Map<Long, DTOJahrgang> mapJahrgaengeByKlassenId = DataJahrgangsdaten.getDTOMapByKlassen(conn, klassen);
		logger.logLn("-> bestimme die zugehörigen Klassenlehrer...");
		final Map<Long, List<DTOLehrer>> mapKlassenlehrerByKlassenId = DataKlassendaten.getDTOMapKlassenlehrerByKlassenID(conn, idsKlassen);
		return getGPU003(logger, klassen, mapJahrgaengeByKlassenId, mapKlassenlehrerByKlassenId);
	}


	/**
	 * Erzeugt den Export der Klassenliste des angegebenen Schuljahresabschnittes für Untis (Datei GPU003.txt)
	 * und gibt diese als Response zurück.
	 *
	 * @param conn                     die Datenbank-Verbindung
	 * @param logger                   der Logger
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @return eine Response mit der CSV-Datei
	 *
	 * @throws ApiOperationException   wenn ein Fehler beim Erstellen des Exportes auftritt
	 */
	public static Response exportGPU003(final DBEntityManager conn, final Logger logger, final long idSchuljahresabschnitt) throws ApiOperationException {
		final @NotNull String daten = getGPU003bySchuljahresabschnitt(conn, logger, idSchuljahresabschnitt);
		return Response.ok(daten).header("Content-Disposition", "attachment; filename=\"GPU003.txt\"").build();
	}


	/**
	 * Bestimmt anhand der übergebenen Lehrerliste und der übergebenen Abschnittsinformationen zu den Lehrkräften die
	 * Liste der zugehörigen Klasseneinträge für die GPU004.TXT-Datei von Untis.
	 *
	 * @param lehrer          die Lehrkräfte
	 * @param mapAbschnitte   die Abschnittsdaten der einzelnen Lehrkräfte
	 *
	 * @return die Liste der GPU004-Einträge
	 */
	private static List<UntisGPU004> getListGPU004(final @NotNull List<DTOLehrer> lehrer, final @NotNull Map<Long, DTOLehrerAbschnittsdaten> mapAbschnitte) {
		final List<UntisGPU004> result = new ArrayList<>();
		for (final DTOLehrer l : lehrer) {
			final UntisGPU004 dto = new UntisGPU004();
			dto.kuerzel = l.Kuerzel;
			dto.nachname = l.Nachname;
			dto.vorname = l.Vorname;
			dto.titel = l.Titel;
			dto.geburtsdatum = getUntisDate(l.Geburtsdatum);
			dto.geschlecht = getUntisGeschlecht(l.Geschlecht);
			dto.eMail = l.eMailDienstlich;
			dto.telefon = l.telefon;
			dto.telefonMobil = l.telefonMobil;
			dto.persNr = l.personalNrLBV;
			dto.stundensatz = l.verguetungsSchluessel;
			dto.datumZugang = getUntisDate(l.DatumZugang);
			dto.datumAbgang = getUntisDate(l.DatumAbgang);
			final DTOLehrerAbschnittsdaten a = mapAbschnitte.get(l.ID);
			if (a != null) {
				final LehrerRechtsverhaeltnis rv = LehrerRechtsverhaeltnis.data().getWertBySchluessel(a.Rechtsverhaeltnis);
				if (rv != null) {
					dto.statistik1 = a.Rechtsverhaeltnis;
					final LehrerBeschaeftigungsart ba = LehrerBeschaeftigungsart.data().getWertBySchluessel(a.Beschaeftigungsart);
					if ((ba == LehrerBeschaeftigungsart.T) || (ba == LehrerBeschaeftigungsart.WT) || (ba == LehrerBeschaeftigungsart.TA))
						dto.statistik1 += ",T";
				}
				dto.stammschule = a.StammschulNr;
				dto.wochenSoll = a.PflichtstdSoll;
			}
			result.add(dto);
		}
		return result;
	}


	/**
	 * Erstellt die GPU004-CSV-Daten für die übergebene Lehrerliste und die übergebenen Zuordnungen.
	 *
	 * @param logger          der Logger
	 * @param lehrer          die Lehrkräfte
	 * @param mapAbschnitte   die Abschnittsdaten der einzelnen Lehrkräfte
	 *
	 * @return die CSV-Daten als UTF8-String
	 *
	 * @throws ApiOperationException   falls ein Fehler beim Erstellen der CSV-Daten entsteht
	 */
	private static @NotNull String getGPU004(final @NotNull Logger logger, final @NotNull List<DTOLehrer> lehrer,
			final @NotNull Map<Long, DTOLehrerAbschnittsdaten> mapAbschnitte) throws ApiOperationException {
		logger.logLn("-> Erstelle Liste der Klassen für GPU004.txt");
		logger.modifyIndent(2);
		try {
			final List<UntisGPU004> gpu004 = getListGPU004(lehrer, mapAbschnitte);
			final String csvSchueler = UntisGPU004.writeCSV(gpu004);
			logger.logLn("OK");
			logger.modifyIndent(-2);
			return csvSchueler;
		} catch (final Exception e) {
			logger.logLn("Fehler beim Erstellen der Datei GPU004.txt.");
			logger.modifyIndent(-2);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen der Datei GPU004.txt.");
		}
	}


	/**
	 * Bestimmt die Export-Daten für die Lehrerliste für den übergebenen Schuljahresabschnitt
	 *
	 * @param conn                     die aktuelle Datenbankverbindung
	 * @param logger                   der Logger zum Protokollieren des Exportes
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 *
	 * @return die GPU004.txt-Datei als String im UTF8-Format
	 *
	 * @throws ApiOperationException   wenn ein Fehler beim Erstellen des Exportes auftritt
	 */
	private static @NotNull String getGPU004bySchuljahresabschnitt(final @NotNull DBEntityManager conn, final @NotNull Logger logger,
			final long idSchuljahresabschnitt) throws ApiOperationException {
		logger.log("Ermittle GPU004-Daten für die Lehrer...");
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetAbschnittById(idSchuljahresabschnitt);
		if (schuljahresabschnitt == null) {
			logger.logLn("-> FEHLER: Kein Schuljahresabschnitt mit der ID %d gefunden.".formatted(idSchuljahresabschnitt));
			throw new ApiOperationException(Status.NOT_FOUND,
					"In der Datenbank gibt es keinen Schuljahresabschnitt mit der ID %d.".formatted(idSchuljahresabschnitt));
		}
		logger.logLn("-> für den Schuljahresabschnitt %d.%d".formatted(schuljahresabschnitt.schuljahr, schuljahresabschnitt.abschnitt));
		final List<DTOLehrer> lehrer = conn.queryList(DTOLehrer.QUERY_BY_SICHTBAR, DTOLehrer.class, true);
		if (lehrer.isEmpty()) {
			logger.logLn("-> keine Lehrer für den Export gefunden");
			throw new ApiOperationException(Status.NOT_FOUND, "Keine sichtbaren Lehrer für den Export gefunden.");
		}
		final List<Long> idsLehrer = lehrer.stream().map(kl -> kl.ID).toList();
		logger.logLn("-> bestimme die zugehörigen Abschnittsdaten für die Lehrer...");
		final Map<Long, DTOLehrerAbschnittsdaten> mapAbschnitteByLehrerId =
				new DataLehrerliste(conn).getDTOAbschnittsdatenByID(idsLehrer, idSchuljahresabschnitt);
		return getGPU004(logger, lehrer, mapAbschnitteByLehrerId);
	}


	/**
	 * Erzeugt den Export der Lehrerliste des angegebenen Schuljahresabschnittes für Untis (Datei GPU004.txt)
	 * und gibt diese als Response zurück.
	 *
	 * @param conn                     die Datenbank-Verbindung
	 * @param logger                   der Logger
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @return eine Response mit der CSV-Datei
	 *
	 * @throws ApiOperationException   wenn ein Fehler beim Erstellen des Exportes auftritt
	 */
	public static Response exportGPU004(final DBEntityManager conn, final Logger logger, final long idSchuljahresabschnitt) throws ApiOperationException {
		final @NotNull String daten = getGPU004bySchuljahresabschnitt(conn, logger, idSchuljahresabschnitt);
		return Response.ok(daten).header("Content-Disposition", "attachment; filename=\"GPU004.txt\"").build();
	}


	/**
	 * Bestimmt anhand der übergebenen Fächerliste und der übergebenen Kursliste die Liste der
	 * zugehörigen Fächereinträge für die GPU006.TXT-Datei von Untis. Kurse werden hier auch als
	 * Fächer modelliert.
	 *
	 * @param faecher         die Fächer
	 * @param kurse           die Jahrgänge zu den Klassen
	 * @param mapFachgruppe   eine Map mit den Zuordnungen der Fächer zu den Informationen der Fachgruppen
	 *
	 * @return die Liste der GPU006-Einträge
	 */
	private static List<UntisGPU006> getListGPU006(final @NotNull List<DTOFach> faecher, final @NotNull List<DTOKurs> kurse,
			final @NotNull Map<String, FachgruppeKatalogEintrag> mapFachgruppe) {
		final List<UntisGPU006> result = new ArrayList<>();
		for (final DTOFach fach : faecher) {
			final FachgruppeKatalogEintrag fg = mapFachgruppe.get(fach.StatistikKuerzel);
			final UntisGPU006 dto = new UntisGPU006();
			dto.kuerzel = fach.Kuerzel;
			dto.bezeichnung = fach.Bezeichnung;
			dto.kennzeichen = null; // evtl. Hauptfächer mit H kennzeichnen
			dto.fachgruppe = (fg == null) ? null : fg.text;
			dto.farbeHintergrund = null; // kann ggf. anhand der Farbe der Fachgruppe gesetzt werden
			dto.faktor = 1.0;
			dto.beschreibung = fach.Bezeichnung;
			result.add(dto);
		}
		final Map<Long, DTOFach> mapFaecher = faecher.stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		for (final DTOKurs kurs : kurse) {
			final DTOFach fach = mapFaecher.get(kurs.Fach_ID);
			final FachgruppeKatalogEintrag fg = (fach == null) ? null : mapFachgruppe.get(fach.StatistikKuerzel);
			final UntisGPU006 dto = new UntisGPU006();
			dto.kuerzel = kurs.KurzBez;
			dto.bezeichnung = (fach == null) ? null : fach.Bezeichnung;
			dto.kennzeichen = null; // evtl. Hauptfächer mit H kennzeichnen
			dto.fachgruppe = (fg == null) ? null : fg.text;
			dto.farbeHintergrund = null; // kann ggf. anhand der Farbe der Fachgruppe gesetzt werden
			dto.faktor = 1.0;
			dto.beschreibung = (fach == null) ? null : fach.Bezeichnung;
			result.add(dto);
		}
		return result;
	}


	/**
	 * Erstellt die GPU006-CSV-Daten für die übergebene Lehrerliste und die übergebenen Zuordnungen.
	 *
	 * @param logger          der Logger
	 * @param faecher         die Fächer
	 * @param kurse           die Jahrgänge zu den Klassen
	 * @param mapFachgruppe   eine Map mit den Zuordnungen der Fächer zu den Informationen der Fachgruppen
	 *
	 * @return die CSV-Daten als UTF8-String
	 *
	 * @throws ApiOperationException   falls ein Fehler beim Erstellen der CSV-Daten entsteht
	 */
	private static @NotNull String getGPU006(final @NotNull Logger logger, final @NotNull List<DTOFach> faecher, final @NotNull List<DTOKurs> kurse,
			final @NotNull Map<String, FachgruppeKatalogEintrag> mapFachgruppe) throws ApiOperationException {
		logger.logLn("-> Erstelle Liste der Klassen für GPU006.txt");
		logger.modifyIndent(2);
		try {
			final List<UntisGPU006> gpu006 = getListGPU006(faecher, kurse, mapFachgruppe);
			final String csvSchueler = UntisGPU006.writeCSV(gpu006);
			logger.logLn("OK");
			logger.modifyIndent(-2);
			return csvSchueler;
		} catch (final Exception e) {
			logger.logLn("Fehler beim Erstellen der Datei GPU006.txt.");
			logger.modifyIndent(-2);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen der Datei GPU006.txt.");
		}
	}


	/**
	 * Bestimmt die Export-Daten für die Fächer- bzw. Kursliste für den übergebenen Schuljahresabschnitt
	 *
	 * @param conn                     die aktuelle Datenbankverbindung
	 * @param logger                   der Logger zum Protokollieren des Exportes
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 *
	 * @return die GPU006.txt-Datei als String im UTF8-Format
	 *
	 * @throws ApiOperationException   wenn ein Fehler beim Erstellen des Exportes auftritt
	 */
	private static @NotNull String getGPU006bySchuljahresabschnitt(final @NotNull DBEntityManager conn, final @NotNull Logger logger,
			final long idSchuljahresabschnitt) throws ApiOperationException {
		logger.log("Ermittle GPU006-Daten für die Fächer bzw. die Kurse...");
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetAbschnittById(idSchuljahresabschnitt);
		if (schuljahresabschnitt == null) {
			logger.logLn("-> FEHLER: Kein Schuljahresabschnitt mit der ID %d gefunden.".formatted(idSchuljahresabschnitt));
			throw new ApiOperationException(Status.NOT_FOUND,
					"In der Datenbank gibt es keinen Schuljahresabschnitt mit der ID %d.".formatted(idSchuljahresabschnitt));
		}
		logger.logLn("-> für den Schuljahresabschnitt %d.%d".formatted(schuljahresabschnitt.schuljahr, schuljahresabschnitt.abschnitt));
		final List<DTOFach> faecher = conn.queryAll(DTOFach.class);
		if (faecher.isEmpty()) {
			logger.logLn("-> keine Fächer für den Export gefunden");
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Fächer für den Export gefunden.");
		}
		final @NotNull Map<String, FachgruppeKatalogEintrag> mapFachgruppeByStatistikKuerzel = new HashMap<>();
		for (final @NotNull DTOFach fach : faecher) {
			if (fach.StatistikKuerzel == null)
				continue;
			final Fach f = Fach.data().getWertBySchluessel(fach.StatistikKuerzel);
			if (f == null)
				continue;
			final Fachgruppe fg = f.getFachgruppe(schuljahresabschnitt.schuljahr);
			if (fg == null)
				continue;
			final FachgruppeKatalogEintrag fgke = fg.daten(schuljahresabschnitt.schuljahr);
			if (fgke != null)
				mapFachgruppeByStatistikKuerzel.put(fach.StatistikKuerzel, fgke);
		}
		final List<DTOKurs> kurse = conn.queryList(DTOKurs.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKurs.class, idSchuljahresabschnitt);
		if (kurse.isEmpty())
			logger.logLn("-> keine Kurse in dem Schuljahresabschnitt %d.%d gefunden. Es werden nur die Fächer exportiert."
					.formatted(schuljahresabschnitt.schuljahr, schuljahresabschnitt.abschnitt));
		return getGPU006(logger, faecher, kurse, mapFachgruppeByStatistikKuerzel);
	}


	/**
	 * Erzeugt den Export der Fächer- bzw- Kursliste des angegebenen Schuljahresabschnittes für Untis (Datei GPU006.txt)
	 * und gibt diese als Response zurück.
	 *
	 * @param conn                     die Datenbank-Verbindung
	 * @param logger                   der Logger
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @return eine Response mit der CSV-Datei
	 *
	 * @throws ApiOperationException   wenn ein Fehler beim Erstellen des Exportes auftritt
	 */
	public static Response exportGPU006(final DBEntityManager conn, final Logger logger, final long idSchuljahresabschnitt) throws ApiOperationException {
		final @NotNull String daten = getGPU006bySchuljahresabschnitt(conn, logger, idSchuljahresabschnitt);
		return Response.ok(daten).header("Content-Disposition", "attachment; filename=\"GPU006.txt\"").build();
	}


	/**
	 * Bestimmt anhand der übergebenen Schülerliste und der übergebenen Klasseninformationen zu den Schüler die
	 * Liste der zugehörigen Schülereinträge für die GPU010.TXT-Datei von Untis.
	 *
	 * @param schueler    die Liste der schüler
	 * @param mapKlasse   die Klassen der Schüler anhand der jeweiligen Schüler-ID
	 * @param idVariante  die Variante für den Schüler-Bezeichner (1, 2 oder 3 - siehe {@link UntisSchuelerBezeichner})
	 *
	 * @return die Liste der GPU010-Einträge
	 */
	private static List<UntisGPU010> getListGPU010(final @NotNull List<DTOSchueler> schueler, final @NotNull Map<Long, DTOKlassen> mapKlasse,
			final int idVariante) {
		final List<UntisGPU010> result = new ArrayList<>();
		for (final DTOSchueler dtoSchueler : schueler) {
			final UntisGPU010 dto = new UntisGPU010();
			dto.geburtsdatum = getUntisDate(dtoSchueler.Geburtsdatum);
			dto.name = UntisSchuelerBezeichner.getBezeichner(idVariante, dtoSchueler.ID, dtoSchueler.Nachname, dtoSchueler.Vorname, dtoSchueler.Geburtsdatum);
			dto.langname = ((dtoSchueler.Nachname == null) || ("".equals(dtoSchueler.Nachname.trim()))) ? "???" : dtoSchueler.Nachname.trim();
			dto.vorname = ((dtoSchueler.Vorname == null) || "".equals(dtoSchueler.Vorname.trim())) ? "???" : dtoSchueler.Vorname.trim();
			dto.schuelernummer = "" + dtoSchueler.ID;
			final DTOKlassen kl = mapKlasse.get(dtoSchueler.ID);
			dto.klasse = (kl == null) ? null : kl.Klasse;
			dto.geschlecht = getUntisGeschlecht(dtoSchueler.Geschlecht);
			dto.emailAdresse = dtoSchueler.Email;
			dto.fremdschluessel = "" + dtoSchueler.ID;
			result.add(dto);
		}
		return result;
	}


	/**
	 * Erstellt die GPU010-CSV-Daten für die übergebene Schüler-Liste und die übergebene Klassenzuordnung.
	 *
	 * @param logger      der Logger
	 * @param schueler    die Schüler-Liste
	 * @param mapKlasse   die Klassenzuordnungen der Schüler
	 * @param idVariante  die Variante für den Schüler-Bezeichner (1, 2 oder 3 - siehe {@link UntisSchuelerBezeichner})
	 *
	 * @return die CSV-Daten als UTF8-String
	 *
	 * @throws ApiOperationException   falls ein Fehler beim Erstellen der CSV-Daten entsteht
	 */
	private static @NotNull String getGPU010(final @NotNull Logger logger, final @NotNull List<DTOSchueler> schueler,
			final @NotNull Map<Long, DTOKlassen> mapKlasse, final int idVariante)
			throws ApiOperationException {
		logger.logLn("-> Erstelle Liste der Schüler für GPU010.txt");
		logger.modifyIndent(2);
		try {
			final List<UntisGPU010> gpu010 = getListGPU010(schueler, mapKlasse, idVariante);
			final String csvSchueler = UntisGPU010.writeCSV(gpu010);
			logger.logLn("OK");
			logger.modifyIndent(-2);
			return csvSchueler;
		} catch (final Exception e) {
			logger.logLn("Fehler beim Erstellen der Datei GPU010.txt.");
			logger.modifyIndent(-2);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen der Datei GPU010.txt.");
		}
	}


	/**
	 * Bestimmt die Export-Daten für die Schülerliste für den übergebenen Schuljahresabschnitt
	 *
	 * @param conn                     die aktuelle Datenbankverbindung
	 * @param logger                   der Logger zum Protokollieren des Exportes
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param idVariante               die Variante für den Schüler-Bezeichner (1, 2 oder 3 - siehe {@link UntisSchuelerBezeichner})
	 *
	 * @return die GPU010.txt-Datei als String im UTF8-Format
	 *
	 * @throws ApiOperationException   wenn ein Fehler beim Erstellen des Exportes auftritt
	 */
	private static @NotNull String getGPU010bySchuljahresabschnitt(final @NotNull DBEntityManager conn, final @NotNull Logger logger,
			final long idSchuljahresabschnitt, final int idVariante) throws ApiOperationException {
		logger.log("Ermittle GPU010-Daten für die Schüler...");
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetAbschnittById(idSchuljahresabschnitt);
		if (schuljahresabschnitt == null) {
			logger.logLn("-> FEHLER: Kein Schuljahresabschnitt mit der ID %d gefunden.".formatted(idSchuljahresabschnitt));
			throw new ApiOperationException(Status.NOT_FOUND,
					"In der Datenbank gibt es keinen Schuljahresabschnitt mit der ID %d.".formatted(idSchuljahresabschnitt));
		}
		logger.logLn("-> für den Schuljahresabschnitt %d.%d".formatted(schuljahresabschnitt.schuljahr, schuljahresabschnitt.abschnitt));
		final List<Long> statusIDs = List.of(
				SchuelerStatus.NEUAUFNAHME.daten(schuljahresabschnitt.schuljahr).id,
				SchuelerStatus.WARTELISTE.daten(schuljahresabschnitt.schuljahr).id,
				SchuelerStatus.AKTIV.daten(schuljahresabschnitt.schuljahr).id,
				SchuelerStatus.EXTERN.daten(schuljahresabschnitt.schuljahr).id);
		final List<DTOSchueler> schueler = conn.queryList("SELECT s FROM DTOSchueler s WHERE s.ID IS NOT NULL AND s.Schuljahresabschnitts_ID = ?1 AND "
				+ "(s.Geloescht = null OR s.Geloescht = false) AND s.idStatus IN ?2", DTOSchueler.class, idSchuljahresabschnitt, statusIDs);
		if (schueler.isEmpty()) {
			logger.logLn("-> keine Schüler für den Export gefunden");
			throw new ApiOperationException(Status.NOT_FOUND,
					"Keine Schüler im Schuljahresabschnitt %d.%d für den Export gefunden.".formatted(schuljahresabschnitt.schuljahr,
							schuljahresabschnitt.abschnitt));
		}
		final List<Long> idsSchueler = schueler.stream().map(s -> s.ID).toList();
		logger.logLn("-> bestimme die zugehörigen Klassen...");
		final Map<Long, DTOKlassen> mapKlassenBySchuelerId = DataKlassendaten.getDTOMapAktuellBySchuelerID(conn, idsSchueler);
		return getGPU010(logger, schueler, mapKlassenBySchuelerId, idVariante);
	}

	/**
	 * Erzeugt den Export der Schülerliste des angegebenen Schuljahresabschnittes für Untis (Datei GPU010.txt)
	 * und gibt diese als Response zurück. Dabei werden nur Schüler mit dem Status AKTIV, EXTERN, NEUAUFNAHME
	 * und WARTELISTE berücksichtigt.
	 *
	 * @param conn                     die Datenbank-Verbindung
	 * @param logger                   der Logger
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 * @param idVariante               die Variante für den Schüler-Bezeichner (1, 2 oder 3 - siehe {@link UntisSchuelerBezeichner})
	 *
	 * @return eine Response mit der CSV-Datei
	 *
	 * @throws ApiOperationException   wenn ein Fehler beim Erstellen des Exportes auftritt
	 */
	public static Response exportGPU010(final DBEntityManager conn, final Logger logger, final long idSchuljahresabschnitt, final int idVariante)
			throws ApiOperationException {
		final @NotNull String daten = getGPU010bySchuljahresabschnitt(conn, logger, idSchuljahresabschnitt, idVariante);
		return Response.ok(daten).header("Content-Disposition", "attachment; filename=\"GPU010.txt\"").build();
	}


	/**
	 * Bestimmt anhand der übergebenen Daten die Kurs-Fachwahlen für die GPU015.TXT-Datei von Untis.
	 *
	 * @param kurse               die Kurse
	 * @param faecher             die Fächer
	 * @param mapSchueler         die Map mit den Schülerdaten
	 * @param mapLernabschnitte   die Map mit den Lernabschnitten der Schüler
	 * @param mapLeistungsdaten   die Map mit den Leistungsdaten der Schüler
	 * @param mapKlassen          die Klassen
	 * @param mapUnterrichte      die Zuordnung der Unterrichtsnummern zu den Kursen
	 * @param idVariante          die Variante für den Schüler-Bezeichner (1, 2 oder 3 - siehe {@link UntisSchuelerBezeichner})
	 *
	 * @return die Liste der GPU015-Einträge
	 */
	private static List<UntisGPU015> getListGPU015(final @NotNull List<KursDaten> kurse, final @NotNull List<DTOFach> faecher,
			final @NotNull Map<Long, DTOSchueler> mapSchueler, final Map<Long, DTOSchuelerLernabschnittsdaten> mapLernabschnitte,
			final HashMap2D<Long, Long, DTOSchuelerLeistungsdaten> mapLeistungsdaten, final Map<Long, DTOKlassen> mapKlassen,
			final HashMap2D<String, String, List<UntisGPU002>> mapUnterrichte, final int idVariante) {
		final List<UntisGPU015> result = new ArrayList<>();
		final Map<Long, DTOFach> mapFaecher = faecher.stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		for (final KursDaten kurs : kurse) {
			if (kurs.schueler.isEmpty())
				continue;
			final DTOFach fach = mapFaecher.get(kurs.idFach);
			if (fach == null)
				continue;
			for (final Schueler schueler : kurs.schueler) {
				final DTOSchuelerLernabschnittsdaten la = mapLernabschnitte.get(schueler.id);
				if (la == null)
					continue;
				final DTOSchuelerLeistungsdaten ld = mapLeistungsdaten.getOrNull(la.ID, kurs.id);
				if (ld == null)
					continue;
				final ZulaessigeKursart kursart = ZulaessigeKursart.data().getWertBySchluessel(ld.Kursart);
				if (kursart == null)
					continue;
				final DTOKlassen kl = mapKlassen.get(la.Klassen_ID);
				if (kl == null)
					continue;
				final List<UntisGPU002> unterrichte = mapUnterrichte.getOrNull(kl.Klasse, kurs.kuerzel);
				if ((unterrichte == null) || (unterrichte.isEmpty()))
					continue;
				final String schuelerName = UntisSchuelerBezeichner.getBezeichner(idVariante, schueler.id, schueler.nachname,
						schueler.vorname, (idVariante == 1) ? "" : mapSchueler.get(schueler.id).Geburtsdatum);
				for (final UntisGPU002 unterricht : unterrichte) {
					final UntisGPU015 dto = new UntisGPU015();
					dto.name = schuelerName;
					dto.idUnterricht = unterricht.idUnterricht;
					dto.fach = kurs.kuerzel;
					dto.klasse = kl.Klasse;
					dto.statistikKennzeichen = switch (kursart) {
						case LK1 -> "1";
						case LK2 -> "2";
						case AB3 -> "3";
						case AB4 -> "4";
						case GKS, WPII -> "S";
						case PJK -> "P";
						case VTF -> "V";
						case ZK -> "Z";
						case GKM -> "M";
						default -> "";
					};
					dto.idsUnterrichteAlternativkurse = "";
					dto.kuerzelAlternativkurse = "";
					dto.prioAlternativkurse = "";
					result.add(dto);
				}
			}
		}
		return result;
	}


	/**
	 * Erstellt die GPU015-CSV-Daten für die übergebenen Daten.
	 *
	 * @param logger              der Logger
	 * @param kurse               die Kurse
	 * @param faecher             die Fächer
	 * @param mapSchueler         die Map mit den Schülerdaten
	 * @param mapLernabschnitte   die Map mit den Lernabschnitten der Schüler
	 * @param mapLeistungsdaten   die Map mit den Leistungsdaten der Schüler
	 * @param mapKlassen          die Klassen
	 * @param mapUnterrichte      die Zuordnung der Unterrichtsnummern zu den Kursen
	 * @param idVariante          die Variante für den Schüler-Bezeichner (1, 2 oder 3 - siehe {@link UntisSchuelerBezeichner})
	 *
	 * @return die CSV-Daten als UTF8-String
	 *
	 * @throws ApiOperationException   falls ein Fehler beim Erstellen der CSV-Daten entsteht
	 */
	private static @NotNull String getGPU015(final @NotNull Logger logger, final @NotNull List<KursDaten> kurse, final @NotNull List<DTOFach> faecher,
			final @NotNull Map<Long, DTOSchueler> mapSchueler, final Map<Long, DTOSchuelerLernabschnittsdaten> mapLernabschnitte,
			final HashMap2D<Long, Long, DTOSchuelerLeistungsdaten> mapLeistungsdaten, final Map<Long, DTOKlassen> mapKlassen,
			final HashMap2D<String, String, List<UntisGPU002>> mapUnterrichte, final int idVariante)
			throws ApiOperationException {
		logger.logLn("-> Erstelle Liste der Kurs-Fachwahlen für GPU015.txt");
		logger.modifyIndent(2);
		try {
			final List<UntisGPU015> gpu015 =
					getListGPU015(kurse, faecher, mapSchueler, mapLernabschnitte, mapLeistungsdaten, mapKlassen, mapUnterrichte, idVariante);
			final String csvSchueler = UntisGPU015.writeCSV(gpu015);
			logger.logLn("OK");
			logger.modifyIndent(-2);
			return csvSchueler;
		} catch (final Exception e) {
			logger.logLn("Fehler beim Erstellen der Datei GPU015.txt.");
			logger.modifyIndent(-2);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen der Datei GPU015.txt.");
		}
	}


	/**
	 * Bestimmt die Export-Daten für die Kurs-Fachwahlen für den übergebenen Schuljahresabschnitt
	 *
	 * @param conn                     die aktuelle Datenbankverbindung
	 * @param logger                   der Logger zum Protokollieren des Exportes
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param gpu002                   die GPU002-Datei als String
	 * @param idVariante               die Variante für den Schüler-Bezeichner (1, 2 oder 3 - siehe {@link UntisSchuelerBezeichner})
	 *
	 * @return die GPU015.txt-Datei als String im UTF8-Format
	 *
	 * @throws ApiOperationException   wenn ein Fehler beim Erstellen des Exportes auftritt
	 */
	private static @NotNull String getGPU015bySchuljahresabschnitt(final @NotNull DBEntityManager conn, final @NotNull Logger logger,
			final long idSchuljahresabschnitt, final String gpu002, final int idVariante) throws ApiOperationException {
		logger.log("Ermittle GPU015-Daten für die Fachwahlen...");
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetAbschnittById(idSchuljahresabschnitt);
		if (schuljahresabschnitt == null) {
			logger.logLn("-> FEHLER: Kein Schuljahresabschnitt mit der ID %d gefunden.".formatted(idSchuljahresabschnitt));
			throw new ApiOperationException(Status.NOT_FOUND,
					"In der Datenbank gibt es keinen Schuljahresabschnitt mit der ID %d.".formatted(idSchuljahresabschnitt));
		}
		// Bestimme zunächst die Kurse für den Schuljahresabschnitt und hole dabei auch die Schülerzuordnungen
		final String strSchuljahresabschnitt = "Schuljahresabschnitt %d.%d".formatted(schuljahresabschnitt.schuljahr, schuljahresabschnitt.abschnitt);
		logger.logLn("-> für den " + strSchuljahresabschnitt);
		final List<KursDaten> kurse = DataKurse.getKursListenFuerAbschnitt(conn, idSchuljahresabschnitt, true);
		if (kurse.isEmpty()) {
			final String error = "Keine Kurse in dem %s gefunden.".formatted(strSchuljahresabschnitt);
			logger.logLn("-> " + error);
			throw new ApiOperationException(Status.NOT_FOUND, error);
		}
		// Hole die Datenbank-DTOs zu den Kurs-Schülern, sofern die benötigt werden
		Map<Long, DTOSchueler> mapSchueler = new HashMap<>();
		final List<Long> idsSchueler = kurse.stream().flatMap(k -> k.schueler.stream()).map(s -> s.id).distinct().toList();
		if (idsSchueler.isEmpty()) {
			final String error = "Keine Schüler in den Kursen in dem %s gefunden.".formatted(strSchuljahresabschnitt);
			logger.logLn("-> " + error);
			throw new ApiOperationException(Status.NOT_FOUND, error);
		}
		final UntisSchuelerBezeichner variante = UntisSchuelerBezeichner.getByID(idVariante);
		if (variante != UntisSchuelerBezeichner.SCHUELER_IDS)
			mapSchueler = conn.queryByKeyList(DTOSchueler.class, idsSchueler).stream().collect(Collectors.toMap(s -> s.ID, s -> s));
		// Bestimme die aktuellen Schüler-Lernabschnittsdaten der Kurs-Schüler
		final Map<Long, DTOSchuelerLernabschnittsdaten> mapLernabschnitte =
				conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_LIST_BY_SCHUELER_ID + " AND e.Schuljahresabschnitts_ID = ?2 AND e.WechselNr = 0",
						DTOSchuelerLernabschnittsdaten.class, idsSchueler, schuljahresabschnitt.id)
						.stream().collect(Collectors.toMap(la -> la.Schueler_ID, la -> la));
		if (mapLernabschnitte.isEmpty()) {
			final String error = "Keine Lernabschnitt zu den Schülern in den Kursen in dem %s gefunden.".formatted(strSchuljahresabschnitt);
			logger.logLn("-> " + error);
			throw new ApiOperationException(Status.NOT_FOUND, error);
		}
		// Bestimme die Leistungsdaten zu den aktuellen Schüler-Lernabschnittsdaten
		final List<Long> idsLernabschnitte = mapLernabschnitte.values().stream().map(la -> la.ID).toList();
		final List<DTOSchuelerLeistungsdaten> leistungsdaten =
				conn.queryList(DTOSchuelerLeistungsdaten.QUERY_LIST_BY_ABSCHNITT_ID, DTOSchuelerLeistungsdaten.class, idsLernabschnitte);
		if (leistungsdaten.isEmpty()) {
			final String error = "Keine Leistungsdaten zu den Schülern in den Kursen in dem %s gefunden.".formatted(strSchuljahresabschnitt);
			logger.logLn("-> " + error);
			throw new ApiOperationException(Status.NOT_FOUND, error);
		}
		final HashMap2D<Long, Long, DTOSchuelerLeistungsdaten> mapLeistungsdaten = new HashMap2D<>();
		for (final DTOSchuelerLeistungsdaten ld : leistungsdaten) {
			if (ld.Kurs_ID == null)
				continue;
			mapLeistungsdaten.put(ld.Abschnitt_ID, ld.Kurs_ID, ld);
		}
		// Bestimme die Klassen
		final Map<Long, DTOKlassen> mapKlassen = conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, idSchuljahresabschnitt)
				.stream().collect(Collectors.toMap(kl -> kl.ID, kl -> kl));
		if (mapKlassen.isEmpty()) {
			final String error = "Keine Klassen in dem Schuljahresabschnitt %s gefunden.".formatted(strSchuljahresabschnitt);
			logger.logLn("-> " + error);
			throw new ApiOperationException(Status.NOT_FOUND, error);
		}
		// Bestimme die Unterrichtsfächer
		final List<DTOFach> faecher = conn.queryAll(DTOFach.class);
		if (faecher.isEmpty()) {
			final String error = "Keine Fächer in dem %s gefunden.".formatted(strSchuljahresabschnitt);
			logger.logLn("-> " + error);
			throw new ApiOperationException(Status.NOT_FOUND, error);
		}
		// Lese die GPU002 ein
		HashMap2D<String, String, List<UntisGPU002>> unterrichte = new HashMap2D<>();
		logger.logLn("-> analysieren der GPU002-Daten...");
		try {
			unterrichte = getMapUntisGPU002ByKlasseAndFach(UntisGPU002.readCSV(gpu002.getBytes(StandardCharsets.UTF_8)));
		} catch (final IOException e) {
			logger.logLn("-> Fehler: " + e.getMessage());
			return "Fehler: " + e.getMessage();
		}
		// Bestimme die GPU015 mithilfe der Daten...
		return getGPU015(logger, kurse, faecher, mapSchueler, mapLernabschnitte, mapLeistungsdaten, mapKlassen, unterrichte, idVariante);
	}


	/**
	 * Erzeugt den Export der Kurs-Fachwahlen des angegebenen Schuljahresabschnittes für Untis (Datei GPU015.txt)
	 * und gibt diese als Response zurück.
	 *
	 * @param conn                     die Datenbank-Verbindung
	 * @param logger                   der Logger
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 * @param gpu002                   die GPU002-Datei als String
	 * @param idVariante               die Variante für den Schüler-Bezeichner (1, 2 oder 3 - siehe {@link UntisSchuelerBezeichner})
	 *
	 * @return eine Response mit der CSV-Datei
	 *
	 * @throws ApiOperationException   wenn ein Fehler beim Erstellen des Exportes auftritt
	 */
	public static Response exportGPU015(final DBEntityManager conn, final Logger logger, final long idSchuljahresabschnitt, final InputStream gpu002,
			final int idVariante)
			throws ApiOperationException {
		final @NotNull String daten = getGPU015bySchuljahresabschnitt(conn, logger, idSchuljahresabschnitt, JSONMapper.toString(gpu002), idVariante);
		return Response.ok(daten).header("Content-Disposition", "attachment; filename=\"GPU015.txt\"").build();
	}


	/**
	 * Bestimmt anhand des übergebenen Managers die
	 * Liste der zugehörigen Klausureinträge für die GPU017.TXT-Datei von Untis.
	 *
	 * @param logger        der Logger
	 * @param manager       der Klausurplan-Manager
	 * @param unterrichte   die Zuordnung der Unterrichtsnummern zu den Kursen
	 * @param idVariante    die Variante für den Schüler-Bezeichner (1, 2 oder 3 - siehe {@link UntisSchuelerBezeichner})
	 *
	 * @return die Liste der GPU017-Einträge
	 */
	private static List<UntisGPU017> getListGPU017(final @NotNull Logger logger, final @NotNull GostKlausurplanManager manager,
			final HashMap2D<String, String, List<UntisGPU002>> unterrichte, final int idVariante) {
		final List<UntisGPU017> result = new ArrayList<>();
		// Füge zunächst alle Klausuren mit Raumzuordnung hinzu
		for (final GostKlausurraum raum : manager.raumGetMengeAsList()) {
			final List<GostKlausurraumstunde> stunden = manager.raumstundeGetMengeByRaum(raum);
			if (stunden.isEmpty()) {
				logger.logLn("-> INFO: Keine Klausurraumstunden zu Raum %d gefunden. Überspringe.".formatted(raum.id));
			} else {
				final UntisGPU017 klausur = new UntisGPU017();
				klausur.id = raum.id;

				final GostKlausurtermin termin = manager.terminGetByIdOrException(raum.idTermin);
				klausur.datum = getUntisDate(termin.datum);
				klausur.name = "%s_K%d_%s".formatted(GostHalbjahr.fromIDorException(termin.halbjahr).jahrgang, termin.quartal,
						manager.kursklausurGetMengeByRaum(raum, true).stream().map(manager::kursKurzbezeichnungByKursklausur).collect(Collectors.joining("_")));

				final StundenplanManager stundenplan = manager.stundenplanManagerGetByTerminOrNull(termin);
				if (stundenplan == null) {
					logger.logLn("-> INFO: Kein Stundenplan zu Termin %d gefunden.".formatted(termin.id));
				} else {
					klausur.vonStunde = stundenplan.zeitrasterGetByIdOrException(stunden.getFirst().idZeitraster).unterrichtstunde;
					klausur.bisStunde = stundenplan.zeitrasterGetByIdOrException(stunden.getLast().idZeitraster).unterrichtstunde;
				}

				final StundenplanRaum stundenplanraum = manager.stundenplanraumGetByKlausurraumOrNull(raum);
				if (stundenplanraum != null)
					klausur.raeume = stunden.stream().map(s -> stundenplanraum.kuerzel).collect(Collectors.joining(" - "));
				processKlausurenAndSchueler(klausur, manager, unterrichte, idVariante, manager.kursklausurGetMengeByRaum(raum, true),
						manager.schuelerklausurGetMengeByRaum(raum));
				result.add(klausur);
			}
		}
		// Füge dann alle Termine hinzu, die noch keine Raumzuordnung haben
		for (final GostKlausurtermin termin : manager.terminGetMengeAsList()) {
			if (!manager.raumGetMengeByTermin(termin).isEmpty())
				continue;
			final UntisGPU017 klausur = new UntisGPU017();
			klausur.id = termin.id;
			klausur.datum = getUntisDate(termin.datum);

			final List<GostKursklausur> klausuren = manager.kursklausurGetMengeByTermin(termin);
			klausur.name = (termin.bezeichnung != null) ? termin.bezeichnung : "%s_K%d_%s".formatted(
					GostHalbjahr.fromIDorException(termin.halbjahr).jahrgang, termin.quartal,
					klausuren.stream().map(manager::kursKurzbezeichnungByKursklausur).collect(Collectors.joining("_")));
			processKlausurenAndSchueler(klausur, manager, unterrichte, idVariante, klausuren, manager.schuelerklausurGetMengeByTermin(termin));
			result.add(klausur);
		}
		return result;
	}

	private static void processKlausurenAndSchueler(final UntisGPU017 klausur, final GostKlausurplanManager manager,
			final HashMap2D<String, String, List<UntisGPU002>> unterrichte, final int idVarianteSchuelerBezeichner,
			final Collection<GostKursklausur> klausuren, final List<GostSchuelerklausur> schuelerKlausuren) {
		klausur.unterrichte = klausuren.stream()
				.flatMap(k -> Optional.ofNullable(unterrichte
						.getOrNull(GostHalbjahr.fromIDorException(manager.vorgabeByKursklausur(k).halbjahr).jahrgang,
								manager.kursKurzbezeichnungByKursklausur(k)))
						.map(uKlausuren -> uKlausuren.stream().filter(uKlausur -> {
							if (klausur.datum == null)
								return true;
							final long gueltigAb = Long.parseLong(uKlausur.datumVon);
							final long gueltigBis = Long.parseLong(uKlausur.datumBis);
							final long klausurDatum = Long.parseLong(klausur.datum);
							return (gueltigAb <= klausurDatum) && (klausurDatum <= gueltigBis);
						}))
						.orElseGet(Stream::empty))
				.map(u -> u.idUnterricht).map(Object::toString).collect(Collectors.joining("~"));
		klausur.kurse = klausuren.stream().map(manager::kursKurzbezeichnungByKursklausur).collect(Collectors.joining("~"));
		klausur.text = klausur.kurse;
		klausur.schueler = schuelerKlausuren.stream().map(sk -> manager.getSchuelerMap().get(sk.idSchueler))
				.map(s -> UntisSchuelerBezeichner.getBezeichner(idVarianteSchuelerBezeichner, s.id, s.nachname, s.vorname, s.geburtsdatum))
				.collect(Collectors.joining("~"));
	}


	/**
	 * Erstellt die GPU017-CSV-Daten für den übergebenen Klausurplan-Manager.
	 *
	 * @param logger       der Logger
	 * @param manager      der Klausurplan-Manager
	 * @param unterrichte  die Zuordnung der Unterrichtsnummern zu den Kursen
	 * @param idVariante   die Variante für den Schüler-Bezeichner (1, 2 oder 3 - siehe {@link UntisSchuelerBezeichner})
	 *
	 * @return die CSV-Daten als UTF8-String
	 *
	 * @throws ApiOperationException   falls ein Fehler beim Erstellen der CSV-Daten entsteht
	 */
	private static @NotNull String getGPU017(final @NotNull Logger logger, final @NotNull GostKlausurplanManager manager,
			final HashMap2D<String, String, List<UntisGPU002>> unterrichte, final int idVariante)
			throws ApiOperationException {
		logger.logLn("-> Erstelle Liste der Klausurdaten für GPU017.txt");
		logger.modifyIndent(2);
		try {
			final List<UntisGPU017> gpu017 = getListGPU017(logger, manager, unterrichte, idVariante);
			final String csvKlausuren = UntisGPU017.writeCSV(gpu017);
			logger.logLn("OK");
			logger.modifyIndent(-2);
			return csvKlausuren;
		} catch (final Exception e) {
			logger.logLn("Fehler beim Erstellen der Datei GPU017.txt.");
			logger.modifyIndent(-2);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen der Datei GPU017.txt.");
		}
	}

	/**
	 * Bestimmt die Export-Daten für die Klausurdaten für den übergebenen Schuljahresabschnitt
	 *
	 * @param conn                     die aktuelle Datenbankverbindung
	 * @param logger                   der Logger zum Protokollieren des Exportes
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param gpu002                   die GPU002-Datei als String
	 * @param idVariante               die Variante für den Schüler-Bezeichner (1, 2 oder 3 - siehe {@link UntisSchuelerBezeichner})
	 *
	 * @return die GPU017.txt-Datei als String im UTF8-Format
	 *
	 * @throws ApiOperationException   wenn ein Fehler beim Erstellen des Exportes auftritt
	 */
	private static @NotNull String getGPU017bySchuljahresabschnitt(final @NotNull DBEntityManager conn, final @NotNull Logger logger,
			final long idSchuljahresabschnitt, final String gpu002, final int idVariante) throws ApiOperationException {
		logger.log("Ermittle GPU017-Daten...");
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetAbschnittById(idSchuljahresabschnitt);
		if (schuljahresabschnitt == null) {
			logger.logLn("-> FEHLER: Kein Schuljahresabschnitt mit der ID %d gefunden.".formatted(idSchuljahresabschnitt));
			throw new ApiOperationException(Status.NOT_FOUND,
					"In der Datenbank gibt es keinen Schuljahresabschnitt mit der ID %d.".formatted(idSchuljahresabschnitt));
		}
		logger.logLn("-> für den Schuljahresabschnitt %d.%d".formatted(schuljahresabschnitt.schuljahr, schuljahresabschnitt.abschnitt));

		final List<GostKlausurenCollectionHjData> selection = new ArrayList<>();
		selection.add(new GostKlausurenCollectionHjData(schuljahresabschnitt.schuljahr + 3, schuljahresabschnitt.abschnitt - 1));
		selection.add(new GostKlausurenCollectionHjData(schuljahresabschnitt.schuljahr + 2, schuljahresabschnitt.abschnitt + 1));
		selection.add(new GostKlausurenCollectionHjData(schuljahresabschnitt.schuljahr + 1, schuljahresabschnitt.abschnitt + 3));

		selection.forEach(hjData -> {
			hjData.schueler = new ArrayList<>();
			hjData.faecher = new ArrayList<>();
		});

		logger.logLn("-> bestimme die Klausurplan-Daten...");
		final GostKlausurenCollectionAllData data = DataGostKlausuren.getAllData(conn, selection);
		final GostKlausurplanManager manager = new GostKlausurplanManager(data);

		logger.logLn("-> laden der Stundenpläne für den Schuljahresabschnitt...");
		final List<StundenplanListeEintrag> sleList = DataStundenplanListe.getStundenplaene(conn, schuljahresabschnitt.id);
		for (final StundenplanListeEintrag sle : sleList) {
			final StundenplanManager stundenplanManager =
					new StundenplanManager(new DataStundenplan(conn).getById(sle.id), DataStundenplanUnterricht.getUnterrichte(conn, sle.id),
							DataStundenplanPausenaufsichten.getAufsichten(conn, sle.id),
							DataStundenplanUnterrichtsverteilung.getUnterrichtsverteilung(conn, sle.id));
			manager.stundenplanManagerAdd(stundenplanManager);
		}

		HashMap2D<String, String, List<UntisGPU002>> unterrichte = new HashMap2D<>();
		logger.logLn("-> analysieren der GPU002-Daten...");
		try {
			unterrichte = getMapUntisGPU002ByKlasseAndFach(UntisGPU002.readCSV(gpu002.getBytes(StandardCharsets.UTF_8)));
		} catch (final IOException e) {
			logger.logLn("-> Fehler: " + e.getMessage());
			return "Fehler: " + e.getMessage();
		}

		return getGPU017(logger, manager, unterrichte, idVariante);
	}

	/**
	 * Erzeugt den Export die Klausurdaten des angegebenen Schuljahresabschnittes für Untis (Datei GPU017.txt)
	 * und gibt diese als Response zurück.
	 *
	 * @param conn                     die Datenbank-Verbindung
	 * @param logger                   der Logger
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 * @param gpu002                   die GPU002-Datei als String
	 * @param idVariante               die Variante für den Schüler-Bezeichner (1, 2 oder 3 - siehe {@link UntisSchuelerBezeichner})
	 *
	 * @return eine Response mit der CSV-Datei
	 *
	 * @throws ApiOperationException   wenn ein Fehler beim Erstellen des Exportes auftritt
	 */
	public static Response exportGPU017(final DBEntityManager conn, final Logger logger, final long idSchuljahresabschnitt, final InputStream gpu002,
			final int idVariante) throws ApiOperationException {
		final @NotNull String daten = getGPU017bySchuljahresabschnitt(conn, logger, idSchuljahresabschnitt, JSONMapper.toString(gpu002), idVariante);
		return Response.ok(daten).header("Content-Disposition", "attachment; filename=\"GPU017.txt\"").build();
	}


	/**
	 * Bestimmt anhand der übergebenen Fächerliste und der übergebenen Kursliste die Schienenzuordnungs-Liste der
	 * Kurse für die GPU019.TXT-Datei von Untis.
	 *
	 * @param kurse            die Kurse
	 * @param faecher          die Fächer
	 * @param klassen          die Klassen
	 * @param mapUnterrichte   die Zuordnung der Unterrichtsnummern zu den Kursen
	 *
	 * @return die Liste der GPU019-Einträge
	 */
	private static List<UntisGPU019> getListGPU019(final @NotNull List<KursDaten> kurse, final @NotNull List<DTOFach> faecher,
			final @NotNull List<DTOKlassen> klassen, final HashMap2D<String, String, List<UntisGPU002>> mapUnterrichte) {
		final List<UntisGPU019> result = new ArrayList<>();
		final Map<Long, DTOFach> mapFaecher = faecher.stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		final Map<Long, List<DTOKlassen>> mapKlassenByJahrgang = klassen.stream().collect(Collectors.groupingBy(k -> k.Jahrgang_ID));
		for (final KursDaten kurs : kurse) {
			if (kurs.schienen.isEmpty())
				continue;
			final DTOFach fach = mapFaecher.get(kurs.idFach);
			if (fach == null)
				continue;
			if (kurs.idJahrgaenge.isEmpty()) {
				// Fall 1: Schulweiter Kurs
				for (final long unterricht : mapUnterrichte.getEntrySet().stream().flatMap(m -> m.getValue().entrySet().stream())
						.flatMap(e -> e.getValue().stream()).map(u -> u.idUnterricht).toList()) {
					for (final int schienenNr : kurs.schienen) {
						final UntisGPU019 dto = new UntisGPU019();
						dto.name = "Schiene " + schienenNr;
						dto.art = "2";
						dto.anzahlWochenstunden = kurs.wochenstunden;
						dto.idUnterricht = unterricht;
						dto.fach = kurs.kuerzel;
						dto.klassen = "";
						result.add(dto);
					}
				}
			} else {
				// Fall 2: Ein Kurs, welcher einem oder mehreren Jahrgängen zugeordnet ist
				final List<String> klassenKuerzel =
						kurs.idJahrgaenge.stream().flatMap(idJahrgang -> mapKlassenByJahrgang.get(idJahrgang).stream().map(k -> k.Klasse)).toList();
				final String strKlassen = klassenKuerzel.stream().collect(Collectors.joining("~"));
				final String strSchienenBezKlassen = klassenKuerzel.stream().collect(Collectors.joining(",", " (", ")"));
				final List<Long> unterrichte =
						klassenKuerzel.stream().flatMap(k -> Optional.ofNullable(mapUnterrichte.getOrNull(k, kurs.kuerzel)).orElse(new ArrayList<>()).stream())
								.map(u -> u.idUnterricht).distinct().toList();
				for (final long unterricht : unterrichte) {
					for (final int schienenNr : kurs.schienen) {
						final UntisGPU019 dto = new UntisGPU019();
						dto.name = "Schiene " + schienenNr + strSchienenBezKlassen;
						dto.art = "2";
						dto.anzahlWochenstunden = kurs.wochenstunden;
						dto.idUnterricht = unterricht;
						dto.fach = kurs.kuerzel;
						dto.klassen = strKlassen;
						result.add(dto);
					}
				}
			}
		}
		return result;
	}


	/**
	 * Erstellt die GPU019-CSV-Daten für die übergebenen Daten.
	 *
	 * @param logger           der Logger
	 * @param kurse            die Kurse
	 * @param faecher          die Fächer
	 * @param klassen          die Klassen
	 * @param mapUnterrichte   die Zuordnung der Unterrichtsnummern zu den Kursen
	 *
	 * @return die CSV-Daten als UTF8-String
	 *
	 * @throws ApiOperationException   falls ein Fehler beim Erstellen der CSV-Daten entsteht
	 */
	private static @NotNull String getGPU019(final @NotNull Logger logger, final @NotNull List<KursDaten> kurse, final @NotNull List<DTOFach> faecher,
			final @NotNull List<DTOKlassen> klassen, final HashMap2D<String, String, List<UntisGPU002>> mapUnterrichte) throws ApiOperationException {
		logger.logLn("-> Erstelle Liste der Schienenzuordnungen für GPU019.txt");
		logger.modifyIndent(2);
		try {
			final List<UntisGPU019> gpu019 = getListGPU019(kurse, faecher, klassen, mapUnterrichte);
			final String csvSchueler = UntisGPU019.writeCSV(gpu019);
			logger.logLn("OK");
			logger.modifyIndent(-2);
			return csvSchueler;
		} catch (final Exception e) {
			logger.logLn("Fehler beim Erstellen der Datei GPU019.txt.");
			logger.modifyIndent(-2);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen der Datei GPU019.txt.");
		}
	}


	/**
	 * Bestimmt die Export-Daten für die Schienenzuordnungen für den übergebenen Schuljahresabschnitt
	 *
	 * @param conn                     die aktuelle Datenbankverbindung
	 * @param logger                   der Logger zum Protokollieren des Exportes
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param gpu002                   die GPU002-Datei als String
	 *
	 * @return die GPU019.txt-Datei als String im UTF8-Format
	 *
	 * @throws ApiOperationException   wenn ein Fehler beim Erstellen des Exportes auftritt
	 */
	private static @NotNull String getGPU019bySchuljahresabschnitt(final @NotNull DBEntityManager conn, final @NotNull Logger logger,
			final long idSchuljahresabschnitt, final String gpu002) throws ApiOperationException {
		logger.log("Ermittle GPU019-Daten für die Schienenzuordnungen...");
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetAbschnittById(idSchuljahresabschnitt);
		if (schuljahresabschnitt == null) {
			logger.logLn("-> FEHLER: Kein Schuljahresabschnitt mit der ID %d gefunden.".formatted(idSchuljahresabschnitt));
			throw new ApiOperationException(Status.NOT_FOUND,
					"In der Datenbank gibt es keinen Schuljahresabschnitt mit der ID %d.".formatted(idSchuljahresabschnitt));
		}
		final String strSchuljahresabschnitt = "Schuljahresabschnitt %d.%d".formatted(schuljahresabschnitt.schuljahr, schuljahresabschnitt.abschnitt);
		logger.logLn("-> für den " + strSchuljahresabschnitt);
		final List<KursDaten> kurse = DataKurse.getKursListenFuerAbschnitt(conn, idSchuljahresabschnitt, false);
		if (kurse.isEmpty()) {
			final String error = "Keine Kurse in dem %s gefunden.".formatted(strSchuljahresabschnitt);
			logger.logLn("-> " + error);
			throw new ApiOperationException(Status.NOT_FOUND, error);
		}
		final List<DTOFach> faecher = conn.queryAll(DTOFach.class);
		if (faecher.isEmpty()) {
			final String error = "Keine Klassen in dem %s gefunden.".formatted(strSchuljahresabschnitt);
			logger.logLn("-> " + error);
			throw new ApiOperationException(Status.NOT_FOUND, error);
		}
		final List<DTOKlassen> klassen = conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, idSchuljahresabschnitt);
		if (klassen.isEmpty()) {
			final String error = "Keine Klassen in dem Schuljahresabschnitt %s gefunden.".formatted(strSchuljahresabschnitt);
			logger.logLn("-> " + error);
			throw new ApiOperationException(Status.NOT_FOUND, error);
		}

		HashMap2D<String, String, List<UntisGPU002>> unterrichte = new HashMap2D<>();
		logger.logLn("-> analysieren der GPU002-Daten...");
		try {
			unterrichte = getMapUntisGPU002ByKlasseAndFach(UntisGPU002.readCSV(gpu002.getBytes(StandardCharsets.UTF_8)));
		} catch (final IOException e) {
			logger.logLn("-> Fehler: " + e.getMessage());
			return "Fehler: " + e.getMessage();
		}

		return getGPU019(logger, kurse, faecher, klassen, unterrichte);
	}


	/**
	 * Erzeugt den Export der Schienenzuordnungen des angegebenen Schuljahresabschnittes für Untis (Datei GPU019.txt)
	 * und gibt diese als Response zurück.
	 *
	 * @param conn                     die Datenbank-Verbindung
	 * @param logger                   der Logger
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 * @param gpu002                   die GPU002-Datei als String
	 *
	 * @return eine Response mit der CSV-Datei
	 *
	 * @throws ApiOperationException   wenn ein Fehler beim Erstellen des Exportes auftritt
	 */
	public static Response exportGPU019(final DBEntityManager conn, final Logger logger, final long idSchuljahresabschnitt, final InputStream gpu002)
			throws ApiOperationException {
		final @NotNull String daten = getGPU019bySchuljahresabschnitt(conn, logger, idSchuljahresabschnitt, JSONMapper.toString(gpu002));
		return Response.ok(daten).header("Content-Disposition", "attachment; filename=\"GPU019.txt\"").build();
	}


	private static long getGPU002ForBlockung(final int schuljahr, final long idUnterrichtStart, final GostBlockungsergebnisManager ergebnisManager,
			final List<UntisGPU002> neueUnterrichte, final HashMap2D<String, String, List<UntisGPU002>> mapUnterrichte,
			final @NotNull Map<@NotNull Long, @NotNull Long> mapKursZuUnterricht) {
		final GostBlockungsdatenManager datenManager = ergebnisManager.getParent();
		final @NotNull List<@NotNull GostBlockungKurs> kurse = datenManager.kursGetListeSortiertNachKursartFachNummer();
		long idUnterricht = idUnterrichtStart;
		for (final @NotNull GostBlockungKurs kurs : kurse) {
			final UntisGPU002 u = new UntisGPU002();
			u.idUnterricht = idUnterricht++;
			mapKursZuUnterricht.put(kurs.id, u.idUnterricht);
			u.wochenstunden = kurs.wochenstunden;
			u.wochenstundenKlasse = kurs.wochenstunden;
			u.wochenstundenLehrer = kurs.wochenstunden;
			u.klasseKuerzel = datenManager.getHalbjahr().jahrgang;
			u.lehrerKuerzel = kurs.lehrer.isEmpty() ? null : kurs.lehrer.get(0).kuerzel;
			u.fachKuerzel = datenManager.kursGetName(kurs.id);
			u.studentenZahl = ergebnisManager.getOfKursAnzahlSchueler(kurs.id);
			u.wochenTyp = "WA";
			u.jahreswert = 0.0048;
			final RGBFarbe fachFarbe = Fach.getBySchluesselOrDefault(ergebnisManager.getFach(kurs.fach_id).kuerzel).getFarbe(schuljahr);
			u.farbeHintergrund = "" + ((fachFarbe.red * 65536) + (fachFarbe.green * 256) + fachFarbe.blue);
			u.kennzeichen = "n";
			u.doppelStdMin = 1;
			u.doppelStdMax = 1;
			u.studentenMaennlich = 0;
			u.studentenWeiblich = 0;
			for (final Schueler s : ergebnisManager.getOfKursSchuelermenge(kurs.id)) {
				switch (Geschlecht.fromValue(s.geschlecht)) {
					case Geschlecht.M -> u.studentenMaennlich++;
					case Geschlecht.W -> u.studentenWeiblich++;
					default -> {
						/* nicht zu tun */
					}
				}
			}
			u.eigenwert = "100000";
			u.eigenwertHunderttausendstel = "1";
			neueUnterrichte.add(u);
		}
		return idUnterricht;
	}


	private static void getGPU015ForBlockung(final List<DTOSchueler> dtosSchueler, final int idVariante, final GostBlockungsergebnisManager ergebnisManager,
			final List<UntisGPU015> gpu015, final @NotNull Map<@NotNull Long, @NotNull Long> mapKursZuUnterricht) {
		final GostBlockungsdatenManager datenManager = ergebnisManager.getParent();
		for (final DTOSchueler dtoSchueler : dtosSchueler) {
			if (datenManager.schuelerGetListeOfFachwahlen(dtoSchueler.ID).isEmpty())
				continue;
			final String schuelerName = UntisSchuelerBezeichner.getBezeichner(idVariante, dtoSchueler.ID, dtoSchueler.Nachname,
					dtoSchueler.Vorname, dtoSchueler.Geburtsdatum);

			for (final GostBlockungsergebnisKurs k : ergebnisManager.getOfSchuelerKursmenge(dtoSchueler.ID)) {
				final UntisGPU015 dto = new UntisGPU015();
				dto.name = schuelerName;
				dto.idUnterricht = mapKursZuUnterricht.get(k.id);
				dto.fach = datenManager.kursGetName(k.id);
				dto.klasse = datenManager.getHalbjahr().jahrgang;
				final GostFachwahl fw = datenManager.schuelerGetOfFachFachwahl(dtoSchueler.ID, k.fachID);
				final GostKursart kursart = GostKursart.fromIDorNull(fw.kursartID);
				dto.statistikKennzeichen = switch (kursart) {
					case LK -> "" + fw.abiturfach;
					case GK -> (fw.abiturfach != null) ? ("" + fw.abiturfach) : (fw.istSchriftlich ? "S" : "M");
					case PJK -> "P";
					case VTF -> "V";
					case ZK -> "Z";
					default -> "";
				};
				dto.idsUnterrichteAlternativkurse = "";
				dto.kuerzelAlternativkurse = "";
				dto.prioAlternativkurse = "";
				String tilde = "";
				for (final GostBlockungKurs ak : datenManager.kursGetListeByFachUndKursart(k.fachID, k.kursart)) {
					final Long idUnterrichtAlternativ = mapKursZuUnterricht.get(ak.id);
					dto.idsUnterrichteAlternativkurse += tilde + idUnterrichtAlternativ;
					dto.kuerzelAlternativkurse += tilde + datenManager.kursGetName(ak.id);
					dto.prioAlternativkurse += tilde + "1";
					if ("".equals(tilde))
						tilde = "~";
				}
				gpu015.add(dto);
			}
		}
	}

	private static void getGPU019ForBlockung(final GostBlockungsergebnisManager ergebnisManager, final List<UntisGPU019> gpu019,
			final @NotNull Map<@NotNull Long, @NotNull Long> mapKursZuUnterricht) {
		final GostBlockungsdatenManager datenManager = ergebnisManager.getParent();
		final @NotNull List<@NotNull GostBlockungKurs> kurse = datenManager.kursGetListeSortiertNachKursartFachNummer();
		for (final @NotNull GostBlockungKurs kurs : kurse) {
			final UntisGPU019 dto = new UntisGPU019();
			// TODO evtl. Multi-Schienen-Kurse unterstützen
			final Set<GostBlockungsergebnisSchiene> schienen = ergebnisManager.getOfKursSchienenmenge(kurs.id);
			final GostBlockungsergebnisSchiene schiene = schienen.iterator().next();
			dto.name = datenManager.schieneGet(schiene.id).bezeichnung;
			dto.art = "2";
			dto.anzahlWochenstunden = kurs.wochenstunden;
			dto.idUnterricht = mapKursZuUnterricht.get(kurs.id);
			dto.fach = datenManager.kursGetName(kurs.id);
			dto.klassen = datenManager.getHalbjahr().jahrgang;
			gpu019.add(dto);
		}
	}

	/**
	 * Erzeugt den Export eines Blockungsergebnisses für Untis, indem die Dateien GPU002.txt, GPU015.txt und GPU019.txt
	 * erzeugt werden und in einem Liste von Strings in dieser Reihenfolge in der Response zurückgegeben werden.
	 *
	 * @param conn                     die Datenbank-Verbindung
	 * @param logger                   der Logger
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 * @param params                   die Abiturjahre und die GPU002 als Parameter
	 * @param idVariante               die Variante für den Schüler-Bezeichner (1, 2 oder 3 - siehe {@link UntisSchuelerBezeichner})
	 *
	 * @return eine Response mit den drei Strings in einer Liste
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response exportUntisBlockungsergebnisse(final DBEntityManager conn, final Logger logger, final long idSchuljahresabschnitt,
			final LongAndStringLists params, final int idVariante) throws ApiOperationException {
		// Prüfe den Schuljahresabschnitt auf Gültigkeit
		logger.log("Prüfe, ob der Schuljahresabschnitt mit der ID %d in der Datenbank existiert... ".formatted(idSchuljahresabschnitt));
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetAbschnittById(idSchuljahresabschnitt);
		if (schuljahresabschnitt == null) {
			logger.logLn(0, "[Fehler]");
			throw new ApiOperationException(Status.NOT_FOUND, "Es existiert kein Schuljahresabschnitt mit der ID %d".formatted(idSchuljahresabschnitt));
		}
		logger.logLn("[OK]");
		// Lese die Unterrichte aus der übergebenen GPU002 ein und erzeuge eine Map für den schnellen Zugriff auf diese Unterrichte
		logger.logLn("Lese die Unterrichte aus der übergebenen GPU002 ein ... ");
		if (params.strings.size() != 1) {
			logger.logLn(0, "[Fehler]");
			throw new ApiOperationException(Status.BAD_REQUEST, "Es muss genau ein String mit der GPU002 übergeben werden.");
		}
		final List<UntisGPU002> inputUnterrichte;
		try {
			inputUnterrichte = UntisGPU002.readCSV(params.strings.getFirst().getBytes(StandardCharsets.UTF_8));
		} catch (final IOException e) {
			throw new ApiOperationException(Status.BAD_REQUEST, e, "Die übergebene GPU002 konnte nicht gelesen werden. Überprüfen Sie das Dateiformat.");
		}
		final HashMap2D<String, String, List<UntisGPU002>> mapUnterrichte = getMapUntisGPU002ByKlasseAndFach(inputUnterrichte);
		long idUnterrichtStart = inputUnterrichte.stream().map(u -> u.idUnterricht).max(Long::compare).orElse(1L);
		// Iteriere durch die übergebenen Blockungsergebnisse und erzeuge die jeweiligen Datensätze für die GPUs
		final List<UntisGPU002> neueUnterrichte = new ArrayList<>();
		final List<UntisGPU015> gpu015 = new ArrayList<>();
		final List<UntisGPU019> gpu019 = new ArrayList<>();
		logger.logLn("Lade die Blockungsergebnisse aus der Datenbank und erzeuge die Datensätze für die GPU-Dateien... ");
		for (final Long idBlockungsergebnis : params.numbers) {
			if (idBlockungsergebnis == null) {
				logger.logLn("[Fehler] Null ist für IDs von Blockungsergebnissen unzulässig.");
				throw new ApiOperationException(Status.BAD_REQUEST, "Null-Werte sind für IDs von Bloockungsergebnissen nicht zulässig.");
			}

			// Lade die Blockungsergebnisse aus der Datenbank
			logger.log("Lade die Blockungsergebnisse mit der ID %d... ".formatted(idBlockungsergebnis));
			final GostBlockungsergebnisManager ergebnisManager;
			try {
				ergebnisManager = DataGostBlockungsergebnisse.getErgebnismanagerFromID(conn, idBlockungsergebnis);
			} catch (final Exception e) {
				logger.logLn(0, "[Fehler]");
				if (e instanceof final ApiOperationException aoe) {
					logger.logLn(aoe.getMessage());
					logger.modifyIndent(-2);
					throw aoe;
				}
				logger.modifyIndent(-2);
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Unerwarteter Fehler beim Einlesen der Blockung");
			}
			logger.logLn("[OK]");

			// Lade die Schülerinformationen aus der Datenbank
			logger.log("Lade die Informationen zu den Schülern der Blockung aus der Datenbank... ");
			final List<Schueler> schueler = ergebnisManager.getParent().schuelerGetListe();
			final List<Long> idsSchueler = schueler.stream().map(s -> s.id).toList();
			if (idsSchueler.isEmpty()) {
				logger.logLn("Keine Schüler in der Blockung vorhanden. Der Export wird abgebrochen.");
				logger.modifyIndent(-2);
				throw new ApiOperationException(Status.NOT_FOUND, "Keine Schüler in der Blockung vorhanden.");
			}
			final List<DTOSchueler> dtosSchueler = conn.queryByKeyList(DTOSchueler.class, idsSchueler);
			if (dtosSchueler.size() != idsSchueler.size()) {
				logger.logLn("Es konnten nicht alle Schüler der Blockung in der Datenbank gefunden werden. Der Export wird abgebrochen.");
				logger.modifyIndent(-2);
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Es konnten nicht alle Schüler der Blockung in der Datenbank gefunden werden.");
			}
			logger.modifyIndent(-2);

			// Prüfe, ob die Unterrichte bereits in der übergebenen GPU002 vorhanden sind und lege ggf. neue Unterrichte an.
			logger.logLn("-> Erstelle Liste der Unterrichte für GPU002.txt");
			logger.modifyIndent(2);
			final @NotNull Map<@NotNull Long, @NotNull Long> mapKursZuUnterricht = new HashMap<>();
			try {
				idUnterrichtStart = getGPU002ForBlockung(schuljahresabschnitt.schuljahr, idUnterrichtStart, ergebnisManager, neueUnterrichte, mapUnterrichte,
						mapKursZuUnterricht);
			} catch (final Exception e) {
				logger.logLn("Fehler beim Erstellen der Datei GPU002.txt.");
				logger.modifyIndent(-2);
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen der Datei GPU002.txt.");
			}
			logger.logLn("OK");
			logger.modifyIndent(-2);

			logger.logLn("-> Erstelle Liste der Kurswahlen für GPU015.txt");
			logger.modifyIndent(2);
			try {
				getGPU015ForBlockung(dtosSchueler, idVariante, ergebnisManager, gpu015, mapKursZuUnterricht);
			} catch (final Exception e) {
				logger.logLn("Fehler beim Erstellen der Datei GPU015.txt.");
				logger.modifyIndent(-2);
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen der Datei GPU015.txt.");
			}
			logger.logLn("OK");
			logger.modifyIndent(-2);

			logger.logLn("-> Erstelle Liste der Schienen für GPU019.txt für die Kurs-Schienen-Zuordnung");
			logger.modifyIndent(2);
			try {
				getGPU019ForBlockung(ergebnisManager, gpu019, mapKursZuUnterricht);
			} catch (final Exception e) {
				logger.logLn("Fehler beim Erstellen der Datei GPU019.txt.");
				logger.modifyIndent(-2);
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen der Datei GPU019.txt.");
			}
			logger.logLn("OK");
			logger.modifyIndent(-2);
		}
		try {
			final List<String> daten = List.of(UntisGPU002.writeCSV(neueUnterrichte), UntisGPU015.writeCSV(gpu015), UntisGPU019.writeCSV(gpu019));
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (final IOException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Unerwarteter Fehler beim Serialisieren der GPU-Dateien.");
		}
	}

}
