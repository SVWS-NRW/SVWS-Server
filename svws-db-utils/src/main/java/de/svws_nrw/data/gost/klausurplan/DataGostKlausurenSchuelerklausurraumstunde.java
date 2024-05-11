package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkrsKrs;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumRich;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurterminraumstunde;
import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurraumManager;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurvorgabenManager;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKursklausurManager;
import de.svws_nrw.core.utils.stundenplan.StundenplanListUtils;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.stundenplan.DataStundenplan;
import de.svws_nrw.data.stundenplan.DataStundenplanListe;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenRaeume;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenRaumstunden;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausurenTermineRaumstunden;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostSchuelerklausurterminraumstunde}.
 */
public final class DataGostKlausurenSchuelerklausurraumstunde extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostSchuelerklausurterminraumstunde}.
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


	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurterminen die Schülerklausurtermin-Raumstunden
	 *
	 * @param conn    x
	 * @param termine die Liste der GostSchuelerklausurterminen
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurterminraumstunde-Objekte
	 */
	public static List<GostSchuelerklausurterminraumstunde> getSchuelerklausurterminraumstundenZuSchuelerklausurterminen(final DBEntityManager conn, final List<GostSchuelerklausurTermin> termine) {
		return getSchuelerklausurterminraumstundenZuSchuelerklausurterminids(conn, termine.stream().map(skt -> skt.id).toList());
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurtermin-IDs die Schülerklausurtermin-Raumstunden
	 *
	 * @param conn    x
	 * @param idsSchuelerklausurtermine die Liste der GostSchuelerklausurtermin-IDs
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurterminraumstunde-Objekte
	 */
	public static List<GostSchuelerklausurterminraumstunde> getSchuelerklausurterminraumstundenZuSchuelerklausurterminids(final DBEntityManager conn, final List<Long> idsSchuelerklausurtermine) {
		return getSchuelerklausurterminraumstundenDTOsZuSchuelerklausurterminids(conn, idsSchuelerklausurtermine).stream().map(dtoMapper::apply).toList();
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurtermin-IDs die Schülerklausurtermin-Raumstunden-DTOs
	 *
	 * @param conn    x
	 * @param idsSchuelerklausurtermine die Liste der GostSchuelerklausurtermin-IDs
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurterminraumstunde-DTOs
	 */
	public static List<DTOGostKlausurenSchuelerklausurenTermineRaumstunden> getSchuelerklausurterminraumstundenDTOsZuSchuelerklausurterminids(final DBEntityManager conn, final List<Long> idsSchuelerklausurtermine) {
		if (idsSchuelerklausurtermine.isEmpty())
			return new ArrayList<>();
		return conn
				.queryNamed("DTOGostKlausurenSchuelerklausurenTermineRaumstunden.schuelerklausurtermin_id.multiple", idsSchuelerklausurtermine, DTOGostKlausurenSchuelerklausurenTermineRaumstunden.class);
	}

	/**
	 * Liefert die zu einer Liste von Klausurraumstunden die Schülerklausurtermin-Raumstunden
	 *
	 * @param conn    x
	 * @param listKlausurraumstunden die Liste der Klausurraumstunden
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurterminraumstunde-Objekte
	 */
	public static List<GostSchuelerklausurterminraumstunde> getSchuelerklausurterminraumstundenZuKlausurraumstunden(final DBEntityManager conn, final List<GostKlausurraumstunde> listKlausurraumstunden) {
		if (listKlausurraumstunden.isEmpty())
			return new ArrayList<>();

		final List<DTOGostKlausurenSchuelerklausurenTermineRaumstunden> dtos = conn.queryNamed(
				"DTOGostKlausurenSchuelerklausurenTermineRaumstunden.raumstunde_id.multiple", listKlausurraumstunden.stream().map(krs -> krs.id).toList(), DTOGostKlausurenSchuelerklausurenTermineRaumstunden.class);

		return dtos.stream().map(dtoMapper::apply).toList();
	}

	/**
	 * Liefert zu einer Raumid die Schülerklausurtermin-Raumstunden
	 *
	 * @param conn    x
	 * @param idRaum Raumid
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurterminraumstunde-Objekte
	 */
	public static List<GostSchuelerklausurterminraumstunde> getSchuelerklausurterminraumstundenZuRaumid(final DBEntityManager conn, final long idRaum) {
		final List<GostKlausurraumstunde> listKlausurraumstunden = DataGostKlausurenRaumstunde.getKlausurraumstundenZuRaumid(conn, idRaum);
		if (listKlausurraumstunden.isEmpty())
			return new ArrayList<>();
		return getSchuelerklausurterminraumstundenZuKlausurraumstunden(conn, listKlausurraumstunden);

	}

	private static long ermittleRaumidAusSchuelerklausurterminen(final DBEntityManager conn, final List<Long> idsSchuelerklausurtermine) throws ApiOperationException {
		if (idsSchuelerklausurtermine.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<GostSchuelerklausurterminraumstunde> sktrs = getSchuelerklausurterminraumstundenZuSchuelerklausurterminids(conn, idsSchuelerklausurtermine);
		if (sktrs.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "Keine SchuelerklausurenTermineRaumstunden gefunden.");
		final List<GostKlausurraumstunde> listKrs = DataGostKlausurenRaumstunde.getKlausurraumstundenZuSchuelerklausurterminraumstunden(conn, sktrs);
		final List<Long> idsRaeume = listKrs.stream().map(krs -> krs.idRaum).distinct().toList();
		if (idsRaeume.size() != 1)
			throw new ApiOperationException(Status.CONFLICT, "Verschiedene Raumids in Schuelerklausuren gefunden.");
		return idsRaeume.get(0);
	}

	/**
	 * Weist die übergebenen Schülerklausuren dem entsprechenden Klausurraum zu.
	 *
	 * @param conn                 x
	 * @param raumSchuelerZuteilung die IDs der zuzuweisenden Schülerklausuren
	 *
	 * @return die Antwort
	 */
	public static Response loescheRaumZuSchuelerklausuren(final DBEntityManager conn, final List<GostKlausurraumRich> raumSchuelerZuteilung) {
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(loescheRaumZuSchuelerklausurenTransaction(conn, raumSchuelerZuteilung)).build();
	}

	/**
	 * Weist die übergebenen Schülerklausuren dem entsprechenden Klausurraum zu.
	 *
	 * @param conn                 x
	 * @param raumSchuelerZuteilung die IDs der zuzuweisenden Schülerklausuren
	 *
	 * @return die Antwort
	 */
	public static GostKlausurenCollectionSkrsKrs loescheRaumZuSchuelerklausurenTransaction(final DBEntityManager conn, final List<GostKlausurraumRich> raumSchuelerZuteilung) {
		final GostKlausurenCollectionSkrsKrs result = new GostKlausurenCollectionSkrsKrs();
		if (raumSchuelerZuteilung.isEmpty())
			return result;


		raumSchuelerZuteilung.stream().map(r -> r.schuelerklausurterminIDs).forEach(result.idsSchuelerklausurtermine::addAll);

		final List<DTOGostKlausurenSchuelerklausurenTermineRaumstunden> stundenAlt = DataGostKlausurenSchuelerklausurraumstunde.getSchuelerklausurterminraumstundenDTOsZuSchuelerklausurterminids(conn, result.idsSchuelerklausurtermine);
		conn.transactionRemoveAll(stundenAlt);
		conn.transactionFlush();
		result.raumstundenGeloescht = removeRaumStundenInDb(conn);

		return result;
	}

	/**
	 * Weist die übergebenen Schülerklausuren dem entsprechenden Klausurraum zu.
	 *
	 * @param conn                 x
	 * @param raumSchuelerZuteilung die IDs der zuzuweisenden Schülerklausuren
	 * @param idAbschnitt          die ID des Schuljahresabschnitts
	 *
	 * @return die Antwort
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response setzeRaumZuSchuelerklausuren(final DBEntityManager conn, final List<GostKlausurraumRich> raumSchuelerZuteilung, final long idAbschnitt) throws ApiOperationException {
		final GostKlausurenCollectionSkrsKrs result = transactionSetzeRaumZuSchuelerklausuren(conn, raumSchuelerZuteilung, idAbschnitt);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(result).build();
	}

	/**
	 * Weist die übergebenen Schülerklausuren dem entsprechenden Klausurraum zu.
	 *
	 * @param conn                 x
	 * @param raumSchuelerZuteilung die IDs der zuzuweisenden Schülerklausuren
	 * @param idAbschnitt          die ID des Schuljahresabschnitts
	 *
	 * @return die Antwort
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static GostKlausurenCollectionSkrsKrs transactionSetzeRaumZuSchuelerklausuren(final DBEntityManager conn, final List<GostKlausurraumRich> raumSchuelerZuteilung,
			final long idAbschnitt) throws ApiOperationException {
		if (raumSchuelerZuteilung.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);

		final GostKlausurenCollectionSkrsKrs result = new GostKlausurenCollectionSkrsKrs();
		for (GostKlausurraumRich pair : raumSchuelerZuteilung) {

//			if (pair.id == -1) {
//				GostKlausurenCollectionSkrsKrs loeschResult = loescheRaumZuSchuelerklausurenTransaction(conn, pair.b);
//				result.idsSchuelerklausurtermine.addAll(loeschResult.idsSchuelerklausurtermine);
//				result.raumstundenGeloescht.addAll(loeschResult.raumstundenGeloescht);
//				continue;
//			}

			final long idRaum = pair.id != -1 ? pair.id : ermittleRaumidAusSchuelerklausurterminen(conn, pair.schuelerklausurterminIDs);

			// Raum und Termin holen
			final GostKlausurraum raum = DataGostKlausurenRaum.dtoMapper.apply(conn.queryByKey(DTOGostKlausurenRaeume.class, idRaum));
			final GostKlausurtermin termin = DataGostKlausurenTermin.getKlausurterminZuId(conn, raum.idTermin);

			// Neue Schülerklausuren ermitteln
			final List<GostSchuelerklausurTermin> listSchuelerklausurtermineNeu = DataGostKlausurenSchuelerklausurTermin.getSchuelerklausurtermineZuSchuelerklausurterminids(conn, pair.schuelerklausurterminIDs);
			if (listSchuelerklausurtermineNeu.isEmpty())
				throw new ApiOperationException(Status.NOT_FOUND);

			// Schon im Raum existente Schülerklausuren ermitteln
			final List<GostSchuelerklausurterminraumstunde> listSchuelerklausurtermineSchonImRaumRaumstunden = getSchuelerklausurterminraumstundenZuRaumid(conn, idRaum);

			final List<GostSchuelerklausurTermin> listSchuelerklausurtermine = new ArrayList<>(DataGostKlausurenSchuelerklausurTermin.getSchuelerklausurtermineZuSchuelerklausurterminraumstunden(conn, listSchuelerklausurtermineSchonImRaumRaumstunden));

			if (pair.id != -1) // überarbeiten! -1 bedeutet: Zeit der Kursklausur wird neu gesetzt, nicht
									// null bedeutet: Raum wird Schülerklausuren zugewiesen
				listSchuelerklausurtermine.addAll(listSchuelerklausurtermineNeu);


			final List<GostSchuelerklausur> listSchuelerklausuren = DataGostKlausurenSchuelerklausur.getSchuelerklausurenZuSchuelerklausurterminen(conn, listSchuelerklausurtermine);

			final List<GostKursklausur> listKursklausuren = DataGostKlausurenKursklausur.getKursklausurenZuSchuelerklausuren(conn, listSchuelerklausuren);

			final List<GostKlausurraumstunde> listRaumstunden = DataGostKlausurenRaumstunde.getKlausurraumstundenZuRaumid(conn, idRaum);
			final List<GostKlausurtermin> listTermine = DataGostKlausurenTermin.getKlausurtermineZuSchuelerklausurterminen(conn, listSchuelerklausurtermine);

			// Manager erzeugen
			final List<GostKlausurvorgabe> listVorgaben = DataGostKlausurenVorgabe.getKlausurvorgabenZuKursklausuren(conn, listKursklausuren);
			final GostKlausurvorgabenManager vorgabenManager = new GostKlausurvorgabenManager(listVorgaben);
			final GostKursklausurManager kursklausurManager = new GostKursklausurManager(vorgabenManager, listKursklausuren, listTermine, listSchuelerklausuren, listSchuelerklausurtermine);
			final GostKlausurraumManager raumManager = new GostKlausurraumManager(raum, listRaumstunden, listSchuelerklausurtermine.stream().map(skt -> skt.id).toList(), kursklausurManager, termin);
			final StundenplanListeEintrag sle = StundenplanListUtils.get(DataStundenplanListe.getStundenplaene(conn, idAbschnitt), termin.datum);
			final StundenplanManager stundenplanManager = new StundenplanManager(DataStundenplan.getStundenplan(conn, sle.id), new ArrayList<>(), new ArrayList<>(), null);

			// Zeitraster_min und _max ermitteln
			int minStart = 1440;
			int maxEnd = -1;
			for (final GostSchuelerklausurTermin sk : listSchuelerklausurtermine) {
				final GostKursklausur kk = kursklausurManager.kursklausurBySchuelerklausurTermin(sk);
				int skStartzeit = -1;
				if (sk.startzeit != null)
					skStartzeit = sk.startzeit;
				else if (kk.startzeit != null)
					skStartzeit = kk.startzeit;
				else
					skStartzeit = kursklausurManager.terminOrNullBySchuelerklausurTermin(sk).startzeit;
				if (skStartzeit < minStart)
					minStart = skStartzeit;
				final GostKlausurvorgabe v = vorgabenManager.vorgabeGetByIdOrException(kk.idVorgabe);
				final int endzeit = skStartzeit + v.dauer + v.auswahlzeit;
				if (endzeit > maxEnd)
					maxEnd = endzeit;
			}

			final LocalDate klausurdatum = LocalDate.parse(termin.datum);
			final List<StundenplanZeitraster> zeitrasterRaum = stundenplanManager.getZeitrasterByWochentagStartVerstrichen(Wochentag.fromIDorException(klausurdatum.getDayOfWeek().getValue()), minStart,
					maxEnd - minStart);
			if (zeitrasterRaum.isEmpty())
				throw new ApiOperationException(Status.NOT_FOUND, "Zeitraster konnte nicht ermittelt werden");

			result.idsSchuelerklausurtermine.addAll(pair.schuelerklausurterminIDs);
			result.raeume.add(raum);
//			result.idKlausurraum = idRaum;

			result.raumstunden.addAll(createRaumStundenInDb(conn, idRaum, zeitrasterRaum, raumManager));
			conn.transactionFlush();
			result.sktRaumstunden.addAll(createSchuelerklausurraumstundenInDb(conn, listSchuelerklausurtermineNeu, idRaum, termin, kursklausurManager, vorgabenManager, stundenplanManager, raumManager));
			conn.transactionFlush();
			result.raumstundenGeloescht.addAll(removeRaumStundenInDb(conn));
		}

		return result;
	}

	/**
	 * Legt die Raumstunden für den gesamten Raum an (Befüllen der Tabelle
	 * Gost_Klausuren_Raeume_Stunden)
	 *
	 * @param conn           x
	 * @param idRaum         x
	 * @param zeitrasterRaum x
	 * @param raumManager    x
	 * @return Liste
	 */
	private static List<GostKlausurraumstunde> createRaumStundenInDb(final DBEntityManager conn, final long idRaum, final List<StundenplanZeitraster> zeitrasterRaum,
			final GostKlausurraumManager raumManager) {
		// Bestimme die ID der ersten neuen Klausurraumstunde
		long idNextKrs = conn.transactionGetNextID(DTOGostKlausurenRaumstunden.class);
		final List<GostKlausurraumstunde> result = new ArrayList<>();
		for (final StundenplanZeitraster stunde : zeitrasterRaum) {
			if (raumManager.klausurraumstundeGetByRaumidAndZeitrasterid(idRaum, stunde.id) == null) {
				final DTOGostKlausurenRaumstunden dtoStundeNeu = new DTOGostKlausurenRaumstunden(idNextKrs++, idRaum, stunde.id);
				final GostKlausurraumstunde stundeNeu = DataGostKlausurenRaumstunde.dtoMapper.apply(dtoStundeNeu);
				raumManager.raumstundeAdd(stundeNeu);
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
	 * @param conn x
	 * @return Liste x
	 */
	private static List<GostKlausurraumstunde> removeRaumStundenInDb(final DBEntityManager conn) {
		final List<DTOGostKlausurenRaumstunden> stundenAlt = conn.queryList(
				"SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.ID NOT IN (SELECT w.Raumstunde_ID FROM DTOGostKlausurenSchuelerklausurenTermineRaumstunden w)",
				DTOGostKlausurenRaumstunden.class);
		conn.transactionRemoveAll(stundenAlt);
		return stundenAlt.stream().map(DataGostKlausurenRaumstunde.dtoMapper::apply).toList();

	}

	/**
	 * Legt die Schuelerklausurraumstunden für jede Klausur an (Befüllen der Tabelle
	 * Gost_Klausuren_Schuelerklausuren_Raeume_Stunden)
	 *
	 * @param conn                     x
	 * @param listSchuelerklausurenNeu x
	 * @param idRaum                   x
	 * @param termin                   x
	 * @param kursklausurManager       x
	 * @param vorgabenManager          x
	 * @param raumManager              x
	 * @param stundenplanManager       x
	 *
	 * @return List
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private static List<GostSchuelerklausurterminraumstunde> createSchuelerklausurraumstundenInDb(final DBEntityManager conn, final List<GostSchuelerklausurTermin> listSchuelerklausurenNeu, final long idRaum,
			final GostKlausurtermin termin, final GostKursklausurManager kursklausurManager, final GostKlausurvorgabenManager vorgabenManager, final StundenplanManager stundenplanManager,
			final GostKlausurraumManager raumManager) throws ApiOperationException {
		final List<GostSchuelerklausurterminraumstunde> result = new ArrayList<>();
		final LocalDate klausurdatum = LocalDate.parse(termin.datum);
		for (final GostSchuelerklausurTermin sk : listSchuelerklausurenNeu) {
			final GostKursklausur kk = kursklausurManager.kursklausurBySchuelerklausurTermin(sk);
			final GostKlausurvorgabe v = vorgabenManager.vorgabeGetByIdOrException(kk.idVorgabe);
			final int startzeit = sk.startzeit != null ? sk.startzeit : (sk.folgeNr == 0 && kk.startzeit != null ? kk.startzeit : kursklausurManager.terminOrExceptionBySchuelerklausurTermin(sk).startzeit);
			final List<StundenplanZeitraster> zeitrasterSk = stundenplanManager.getZeitrasterByWochentagStartVerstrichen(Wochentag.fromIDorException(klausurdatum.getDayOfWeek().getValue()), startzeit,
					v.dauer);
			if (zeitrasterSk.isEmpty())
				throw new ApiOperationException(Status.NOT_FOUND, "Zeitraster konnte nicht ermittelt werden");
			conn.transactionExecuteDelete("DELETE FROM DTOGostKlausurenSchuelerklausurenTermineRaumstunden v WHERE v.Schuelerklausurtermin_ID = %d".formatted(sk.id));
			for (final StundenplanZeitraster stunde : zeitrasterSk) {
				final DTOGostKlausurenSchuelerklausurenTermineRaumstunden skRaumStundeNeu = new DTOGostKlausurenSchuelerklausurenTermineRaumstunden(sk.id,
						raumManager.klausurraumstundeGetByRaumidAndZeitrasterid(idRaum, stunde.id).id);
				conn.transactionPersist(skRaumStundeNeu);
				result.add(DataGostKlausurenSchuelerklausurraumstunde.dtoMapper.apply(skRaumStundeNeu));
			}
		}
		return result;
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenRaumstunden} in einen Core-DTO
	 * {@link GostKlausurraumstunde}.
	 */
	public static final Function<DTOGostKlausurenSchuelerklausurenTermineRaumstunden, GostSchuelerklausurterminraumstunde> dtoMapper = (final DTOGostKlausurenSchuelerklausurenTermineRaumstunden z) -> {
		final GostSchuelerklausurterminraumstunde daten = new GostSchuelerklausurterminraumstunde();
		daten.idRaumstunde = z.Raumstunde_ID;
		daten.idSchuelerklausurtermin = z.Schuelerklausurtermin_ID;
		return daten;
	};

	/**
	 * Gibt die Liste der Klausurvorgaben einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Liste der Klausurraumstunden
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private GostKlausurenCollectionSkrsKrs getSchuelerklausurraumstunden(final Long idTermin) throws ApiOperationException {
		GostKlausurtermin termin = DataGostKlausurenTermin.getKlausurterminZuId(conn, idTermin);
		List<GostKlausurtermin> termine = DataGostKlausurenTermin.getKlausurterminmengeSelbesDatumZuId(conn, termin);
		List<Long> terminIDs = termine.stream().map(t -> t.id).toList();
		final GostKlausurenCollectionSkrsKrs retCollection = new GostKlausurenCollectionSkrsKrs();
		retCollection.idsSchuelerklausurtermine = DataGostKlausurenSchuelerklausur.getSchuelerKlausurenZuTerminIds(conn, terminIDs).stream().map(skt -> skt.id).toList();
		retCollection.raeume = DataGostKlausurenRaum.getKlausurraeumeZuTerminen(conn, terminIDs);
		if (retCollection.raeume.isEmpty())
			return retCollection;
		retCollection.raumstunden = DataGostKlausurenRaumstunde.getKlausurraumstundenZuRaeumen(conn, retCollection.raeume);
		retCollection.sktRaumstunden = DataGostKlausurenSchuelerklausurraumstunde.getSchuelerklausurterminraumstundenZuKlausurraumstunden(conn, retCollection.raumstunden);
		return retCollection;
	}

	@Override
	public Response get(final Long idTermin) throws ApiOperationException {
		// Schuelerklausurraumstunden zu einem Klausurtermin
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getSchuelerklausurraumstunden(idTermin)).build();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
