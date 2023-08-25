package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkrsKrs;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurraumstunde;
import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.core.utils.klausurplanung.GostKlausurraumManager;
import de.svws_nrw.core.utils.klausurplanung.GostKlausurvorgabenManager;
import de.svws_nrw.core.utils.klausurplanung.GostKursklausurManager;
import de.svws_nrw.core.utils.stundenplan.StundenplanListUtils;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.stundenplan.DataStundenplan;
import de.svws_nrw.data.stundenplan.DataStundenplanListe;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenKursklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenRaeume;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenRaeumeStunden;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausurenRaeumeStunden;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenTermine;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenVorgaben;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostSchuelerklausurraumstunde}.
 */
public final class DataGostKlausurenSchuelerklausurraumstunde extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostSchuelerklausurraumstunde}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenSchuelerklausurraumstunde(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	private static long ermittleRaumidAusSchuelerklausuren(final DBEntityManager conn, final List<Long> idsSchuelerklausuren) {
		final List<Long> idsRaumstunden = conn
				.queryNamed("DTOGostKlausurenSchuelerklausurenRaeumeStunden.schuelerklausur_id.multiple", idsSchuelerklausuren, DTOGostKlausurenSchuelerklausurenRaeumeStunden.class).stream()
				.map(skrs -> skrs.KlausurRaumStunde_ID).distinct().toList();
		final List<Long> idsRaeume = conn.queryNamed("DTOGostKlausurenRaeumeStunden.id.multiple", idsRaumstunden, DTOGostKlausurenRaeumeStunden.class).stream().map(krs -> krs.Klausurraum_ID)
				.distinct().toList();
		if (idsRaeume.size() != 1)
			return -1;
		return idsRaeume.get(0);
	}

	private static List<GostSchuelerklausur> ermittleSchuelerklausurenSchonImRaum(final DBEntityManager conn, final long idRaum) {
		final List<DTOGostKlausurenRaeumeStunden> listKlausurraumstunden = conn.queryNamed("DTOGostKlausurenRaeumeStunden.klausurraum_id", idRaum, DTOGostKlausurenRaeumeStunden.class);
		if (!listKlausurraumstunden.isEmpty()) {
			final List<DTOGostKlausurenSchuelerklausurenRaeumeStunden> listSchuelerklausurenSchonImRaumRaumStunden = conn.queryNamed(
					"DTOGostKlausurenSchuelerklausurenRaeumeStunden.klausurraumstunde_id.multiple", listKlausurraumstunden.stream().map(krs -> krs.ID).toList(),
					DTOGostKlausurenSchuelerklausurenRaeumeStunden.class);
			if (!listSchuelerklausurenSchonImRaumRaumStunden.isEmpty()) {
				return new ArrayList<>(
						conn.queryNamed("DTOGostKlausurenSchuelerklausuren.id.multiple", listSchuelerklausurenSchonImRaumRaumStunden.stream().map(skrs -> skrs.Schuelerklausur_ID).distinct().toList(),
								DTOGostKlausurenSchuelerklausuren.class).stream().map(DataGostKlausurenSchuelerklausur.dtoMapper::apply).toList());
			}
		}
		return new ArrayList<>();
	}

	/**
	 * Weist die übergebenen Schülerklausuren dem entsprechenden Klausurraum zu.
	 *
	 * @param conn
	 * @param _idRaum              die ID des Klausurraums
	 * @param idsSchuelerklausuren die IDs der zuzuweisenden Schülerklausuren
	 * @param idAbschnitt          die ID des Schuljahresabschnitts
	 *
	 * @return die Antwort
	 */
	public static Response setzeRaumZuSchuelerklausuren(final DBEntityManager conn, final Long _idRaum, final List<Long> idsSchuelerklausuren, final long idAbschnitt) {
		try {
			conn.transactionBegin();

			GostKlausurenCollectionSkrsKrs result = transactionSetzeRaumZuSchuelerklausuren(conn, _idRaum, idsSchuelerklausuren, idAbschnitt);

			if (!conn.transactionCommit())
				throw OperationError.CONFLICT.exception("Datenbankfehler beim Persistieren der Gost-Klausurraumstunden");

			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(result).build();
//	} catch (final Exception e) {
//		if (e instanceof final WebApplicationException webApplicationException)
//			return webApplicationException.getResponse();
//		return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		} finally {
			conn.transactionRollback();
		}

	}

	/**
	 * Weist die übergebenen Schülerklausuren dem entsprechenden Klausurraum zu.
	 *
	 * @param conn
	 * @param _idRaum              die ID des Klausurraums
	 * @param idsSchuelerklausuren die IDs der zuzuweisenden Schülerklausuren
	 * @param idAbschnitt          die ID des Schuljahresabschnitts
	 *
	 * @return die Antwort
	 */
	public static GostKlausurenCollectionSkrsKrs transactionSetzeRaumZuSchuelerklausuren(final DBEntityManager conn, final Long _idRaum, final List<Long> idsSchuelerklausuren,
			final long idAbschnitt) {

		final long idRaum = _idRaum != null ? _idRaum : ermittleRaumidAusSchuelerklausuren(conn, idsSchuelerklausuren);
		if (idRaum == -1)
			throw OperationError.CONFLICT.exception(); // TODO Exceptionhandling Klausurraum nicht eindeutig

		// Raum und Termin holen
		final GostKlausurraum raum = DataGostKlausurenRaum.dtoMapper.apply(conn.queryByKey(DTOGostKlausurenRaeume.class, idRaum));
		final DTOGostKlausurenTermine termin = conn.queryByKey(DTOGostKlausurenTermine.class, raum.idTermin);

		// Neue Schülerklausuren ermitteln
		final List<GostSchuelerklausur> listSchuelerklausurenNeu = conn.queryNamed("DTOGostKlausurenSchuelerklausuren.id.multiple", idsSchuelerklausuren, DTOGostKlausurenSchuelerklausuren.class)
				.stream().map(DataGostKlausurenSchuelerklausur.dtoMapper::apply).toList();
		if (listSchuelerklausurenNeu.isEmpty())
			throw OperationError.NOTHING_TO_DO.exception(); // TODO Exceptionhandling

		// Schon im Raum existente Schülerklausuren ermitteln
		final List<GostSchuelerklausur> listSchuelerklausuren = ermittleSchuelerklausurenSchonImRaum(conn, idRaum);

		final List<GostKursklausur> listKursklausuren = conn.queryNamed("DTOGostKlausurenKursklausuren.id.multiple",
				Stream.concat(listSchuelerklausuren.stream().map(sk -> sk.idKursklausur).distinct(), listSchuelerklausurenNeu.stream().map(sk -> sk.idKursklausur).distinct()).distinct().toList(),
				DTOGostKlausurenKursklausuren.class).stream().map(DataGostKlausurenKursklausur.dtoMapper2::apply).toList();

		if (_idRaum != null) // überarbeiten! null bedeutet: Zeit der Kursklausur wird neu gesetzt, nicht null bedeutet: Raum wird Schülerklausuren zugewiesen
			listSchuelerklausuren.addAll(listSchuelerklausurenNeu);

		final List<GostKlausurraumstunde> listRaumstunden = conn.queryNamed("DTOGostKlausurenRaeumeStunden.klausurraum_id", idRaum, DTOGostKlausurenRaeumeStunden.class).stream()
				.map(DataGostKlausurenRaumstunde.dtoMapper::apply).toList();

		// Manager erzeugen
		final List<GostKlausurvorgabe> listVorgaben = conn
				.queryNamed("DTOGostKlausurenVorgaben.id.multiple", listKursklausuren.stream().map(k -> k.idVorgabe).distinct().toList(), DTOGostKlausurenVorgaben.class).stream()
				.map(DataGostKlausurenVorgabe.dtoMapper::apply).toList();
		final GostKlausurvorgabenManager vorgabenManager = new GostKlausurvorgabenManager(listVorgaben, null);
		final GostKlausurraumManager raumManager = new GostKlausurraumManager(raum, listRaumstunden, listSchuelerklausuren);
		final GostKursklausurManager kursklausurManager = new GostKursklausurManager(listKursklausuren);
		final StundenplanListeEintrag sle = StundenplanListUtils.get(DataStundenplanListe.getStundenplaene(conn, idAbschnitt), termin.Datum);
		final StundenplanManager stundenplanManager = new StundenplanManager(DataStundenplan.getStundenplan(conn, sle.id), new ArrayList<>(), new ArrayList<>(), null);

		// Zeitraster_min und _max ermitteln
		int minStart = 1440;
		int maxEnd = -1;
		for (final GostKursklausur kk : listKursklausuren) {
			if (kk.startzeit != null && kk.startzeit < minStart)
				minStart = kk.startzeit;
			final int startzeit = kk.startzeit != null ? kk.startzeit : termin.Startzeit;
			final int endzeit = startzeit + vorgabenManager.gibGostKlausurvorgabe(kk.idVorgabe).dauer;
			if (endzeit > maxEnd)
				maxEnd = endzeit;
		}
		for (final GostSchuelerklausur sk : listSchuelerklausuren) {
			if (sk.startzeit != null) {
				if (sk.startzeit < minStart)
					minStart = sk.startzeit;
				final int endzeit = sk.startzeit + vorgabenManager.gibGostKlausurvorgabe(kursklausurManager.gibKursklausurById(sk.idKursklausur).idVorgabe).dauer;
				if (endzeit > maxEnd)
					maxEnd = endzeit;
			}
		}
		if (minStart == 1440)
			minStart = termin.Startzeit;

		final LocalDate klausurdatum = LocalDate.parse(termin.Datum);
		final List<StundenplanZeitraster> zeitrasterRaum = stundenplanManager.getZeitrasterByWochentagStartVerstrichen(Wochentag.fromIDorException(klausurdatum.getDayOfWeek().getValue()), minStart,
				maxEnd - minStart);
		if (zeitrasterRaum.isEmpty())
			throw OperationError.NOTHING_TO_DO.exception(); // TODO Exceptionhandling, Zeitraster konnte nicht ermittelt werden

		final GostKlausurenCollectionSkrsKrs result = new GostKlausurenCollectionSkrsKrs();
		result.idKlausurraum = idRaum;

		result.raumstunden = createRaumStundenInDb(conn, idRaum, zeitrasterRaum, raumManager);
		conn.transactionFlush();
		result.skRaumstunden = createSchuelerklausurraumstundenInDb(conn, listSchuelerklausurenNeu, idRaum, termin, kursklausurManager, vorgabenManager, stundenplanManager, raumManager);
		conn.transactionFlush();
		result.raumstundenGeloescht = removeRaumStundenInDb(conn);

		return result;
	}

	/**
	 * Legt die Raumstunden für den gesamten Raum an (Befüllen der Tabelle
	 * Gost_Klausuren_Raeume_Stunden)
	 *
	 * @param conn
	 * @param idRaum
	 * @param zeitrasterRaum
	 * @param raumManager
	 * @return Liste
	 */
	private static List<GostKlausurraumstunde> createRaumStundenInDb(final DBEntityManager conn, final long idRaum, final List<StundenplanZeitraster> zeitrasterRaum,
			final GostKlausurraumManager raumManager) {
		// Bestimme die ID der ersten neuen Klausurraumstunde
		final DTOSchemaAutoInkremente lastKrsID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Klausuren_Raeume_Stunden");
		long idKrs = lastKrsID == null ? 1 : lastKrsID.MaxID + 1;

		final List<GostKlausurraumstunde> result = new ArrayList<>();
		for (final StundenplanZeitraster stunde : zeitrasterRaum) {
			if (raumManager.getKlausurraumstundeByRaumZeitraster(idRaum, stunde.id) == null) {
				final DTOGostKlausurenRaeumeStunden dtoStundeNeu = new DTOGostKlausurenRaeumeStunden(idKrs++, idRaum, stunde.id);
				final GostKlausurraumstunde stundeNeu = DataGostKlausurenRaumstunde.dtoMapper.apply(dtoStundeNeu);
				raumManager.addKlausurraumstunde(stundeNeu);
				result.add(stundeNeu);
				conn.transactionPersist(dtoStundeNeu);
			}
		}
		return result;
	}

	/**
	 * Ermittelt und löscht die nicht mehr benötigten Raumstunden (Aufräumen der
	 * Tabelle Gost_Klausuren_Raeume_Stunden)
	 *
	 * @param conn
	 * @return Liste
	 */
	private static List<GostKlausurraumstunde> removeRaumStundenInDb(final DBEntityManager conn) {
		final List<DTOGostKlausurenRaeumeStunden> stundenAlt = conn.queryList(
				"SELECT e FROM DTOGostKlausurenRaeumeStunden e WHERE e.ID NOT IN (SELECT w.KlausurRaumStunde_ID FROM DTOGostKlausurenSchuelerklausurenRaeumeStunden w)",
				DTOGostKlausurenRaeumeStunden.class);
		conn.transactionRemoveAll(stundenAlt);
		return stundenAlt.stream().map(DataGostKlausurenRaumstunde.dtoMapper::apply).toList();

	}

	/**
	 * Legt die Schuelerklausurraumstunden für jede Klausur an (Befüllen der Tabelle
	 * Gost_Klausuren_Schuelerklausuren_Raeume_Stunden)
	 *
	 * @param conn
	 * @param listSchuelerklausurenNeu
	 * @param idRaum
	 * @param termin
	 * @param kursklausurManager
	 * @param vorgabenManager
	 * @param raumManager
	 * @param stundenplanManager
	 * @return List
	 */
	private static List<GostSchuelerklausurraumstunde> createSchuelerklausurraumstundenInDb(final DBEntityManager conn, final List<GostSchuelerklausur> listSchuelerklausurenNeu, final long idRaum,
			final DTOGostKlausurenTermine termin, final GostKursklausurManager kursklausurManager, final GostKlausurvorgabenManager vorgabenManager, final StundenplanManager stundenplanManager,
			final GostKlausurraumManager raumManager) {
		final List<GostSchuelerklausurraumstunde> result = new ArrayList<>();
		final LocalDate klausurdatum = LocalDate.parse(termin.Datum);
		for (final GostSchuelerklausur sk : listSchuelerklausurenNeu) {
			final GostKursklausur kk = kursklausurManager.getKursklausurById(sk.idKursklausur);
			final GostKlausurvorgabe v = vorgabenManager.gibGostKlausurvorgabe(kk.idVorgabe);
			final int startzeit = sk.startzeit != null ? sk.startzeit : (kk.startzeit != null ? kk.startzeit : termin.Startzeit);
			final List<StundenplanZeitraster> zeitrasterSk = stundenplanManager.getZeitrasterByWochentagStartVerstrichen(Wochentag.fromIDorException(klausurdatum.getDayOfWeek().getValue()), startzeit,
					v.dauer);
			if (zeitrasterSk.isEmpty())
				throw OperationError.NOTHING_TO_DO.exception(); // TODO Exceptionhandling, Zeitraster konnte nicht ermittelt werden
			conn.transactionExecuteDelete("DELETE FROM DTOGostKlausurenSchuelerklausurenRaeumeStunden v WHERE v.Schuelerklausur_ID = %d".formatted(sk.idSchuelerklausur));
			for (final StundenplanZeitraster stunde : zeitrasterSk) {
				final DTOGostKlausurenSchuelerklausurenRaeumeStunden skRaumStundeNeu = new DTOGostKlausurenSchuelerklausurenRaeumeStunden(sk.idSchuelerklausur,
						raumManager.getKlausurraumstundeByRaumZeitraster(idRaum, stunde.id).id);
				conn.transactionPersist(skRaumStundeNeu);
				result.add(DataGostKlausurenSchuelerklausurraumstunde.dtoMapper.apply(skRaumStundeNeu));
			}
		}
		return result;
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenRaeumeStunden} in einen Core-DTO
	 * {@link GostKlausurraumstunde}.
	 */
	public static final Function<DTOGostKlausurenSchuelerklausurenRaeumeStunden, GostSchuelerklausurraumstunde> dtoMapper = (final DTOGostKlausurenSchuelerklausurenRaeumeStunden z) -> {
		final GostSchuelerklausurraumstunde daten = new GostSchuelerklausurraumstunde();
		daten.idRaumstunde = z.KlausurRaumStunde_ID;
		daten.idSchuelerklausur = z.Schuelerklausur_ID;
		return daten;
	};

	/**
	 * Gibt die Liste der Klausurvorgaben einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Liste der Klausurraumstunden
	 */
	private GostKlausurenCollectionSkrsKrs getSchuelerklausurraumstunden(final Long idTermin) {
		final GostKlausurenCollectionSkrsKrs retCollection = new GostKlausurenCollectionSkrsKrs();

		final List<GostKlausurraum> listRaeume = new DataGostKlausurenRaum(conn).getKlausurraeume(idTermin);

		if (listRaeume.isEmpty()) {
			// TODO Errorhandling nötig?
			return retCollection;
		}

		final List<DTOGostKlausurenRaeumeStunden> raumStunden = conn.queryNamed("DTOGostKlausurenRaeumeStunden.klausurraum_id.multiple", listRaeume.stream().map(s -> s.id).toList(),
				DTOGostKlausurenRaeumeStunden.class);

		if (!raumStunden.isEmpty()) {
			for (final DTOGostKlausurenRaeumeStunden s : raumStunden)
				retCollection.raumstunden.add(DataGostKlausurenRaumstunde.dtoMapper.apply(s));

			final List<DTOGostKlausurenSchuelerklausurenRaeumeStunden> skrStunden = conn.queryNamed("DTOGostKlausurenSchuelerklausurenRaeumeStunden.klausurraumstunde_id.multiple",
					raumStunden.stream().map(s -> s.ID).toList(), DTOGostKlausurenSchuelerklausurenRaeumeStunden.class);

			for (final DTOGostKlausurenSchuelerklausurenRaeumeStunden s : skrStunden)
				retCollection.skRaumstunden.add(DataGostKlausurenSchuelerklausurraumstunde.dtoMapper.apply(s));
		}

		return retCollection;
	}

	@Override
	public Response get(final Long idTermin) {
		// Schuelerklausurraumstunden zu einem Klausurtermin
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getSchuelerklausurraumstunden(idTermin)).build();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Löscht eine Gost-Klausurraumstunde *
	 *
	 * @param id die ID des zu löschenden Klausurraums
	 *
	 * @return die Response
	 */
	public Response delete(final Long id) {
		// TODO use transaction
		// Bestimme die Raumstunde
		final DTOGostKlausurenRaeumeStunden stunde = conn.queryByKey(DTOGostKlausurenRaeumeStunden.class, id);
		if (stunde == null)
			return OperationError.NOT_FOUND.getResponse();
		// Entferne die Raumstunde
		conn.remove(stunde);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(id).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
