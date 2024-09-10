package de.svws_nrw.data.gost.klausurplan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionRaumData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkrsKrsData;
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
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.core.utils.stundenplan.StundenplanListUtils;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.stundenplan.DataStundenplan;
import de.svws_nrw.data.stundenplan.DataStundenplanListe;
import de.svws_nrw.data.stundenplan.DataStundenplanPausenaufsichten;
import de.svws_nrw.data.stundenplan.DataStundenplanUnterricht;
import de.svws_nrw.data.stundenplan.DataStundenplanUnterrichtsverteilung;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenRaumstunden;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausurenTermineRaumstunden;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link GostSchuelerklausurterminraumstunde}.
 */
public final class DataGostKlausurenSchuelerklausurraumstunde extends DataManagerRevised<Long, DTOGostKlausurenSchuelerklausurenTermineRaumstunden, GostSchuelerklausurterminraumstunde> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO
	 * {@link GostSchuelerklausurterminraumstunde}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenSchuelerklausurraumstunde(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Gibt die Daten einer {@link GostSchuelerklausurterminraumstunde} zu deren ID zurück.
	 *
	 * @param id   Die ID der {@link GostSchuelerklausurterminraumstunde}.
	 *
	 * @return die Daten der {@link GostSchuelerklausurterminraumstunde} zur ID.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public GostSchuelerklausurterminraumstunde getById(final Long id) throws ApiOperationException {
		final DTOGostKlausurenSchuelerklausurenTermineRaumstunden klasseDto = getDTO(id);
		return map(klasseDto);
	}

	/**
	 * Die Methode ermittelt das entsprechende {@link DTOGostKlausurenSchuelerklausurenTermineRaumstunden} Objekt zur angegebenen ID.
	 *
	 * @param id ID der {@link DTOGostKlausurenSchuelerklausurenTermineRaumstunden}
	 *
	 * @return Ein {@link DTOGostKlausurenSchuelerklausurenTermineRaumstunden} Objekt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOGostKlausurenSchuelerklausurenTermineRaumstunden getDTO(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für die GostSchuelerklausurterminraumstunde darf nicht null sein.");

		final DTOGostKlausurenSchuelerklausurenTermineRaumstunden klasseDto = conn.queryByKey(DTOGostKlausurenSchuelerklausurenTermineRaumstunden.class, id);
		if (klasseDto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine GostSchuelerklausurterminraumstunde zur ID " + id + " gefunden.");

		return klasseDto;
	}

	@Override
	protected GostSchuelerklausurterminraumstunde map(final DTOGostKlausurenSchuelerklausurenTermineRaumstunden dto) throws ApiOperationException {
		final GostSchuelerklausurterminraumstunde daten = new GostSchuelerklausurterminraumstunde();
		daten.idRaumstunde = dto.Raumstunde_ID;
		daten.idSchuelerklausurtermin = dto.Schuelerklausurtermin_ID;
		return daten;
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurterminen die Schülerklausurtermin-Raumstunden
	 *
	 * @param termine die Liste der GostSchuelerklausurterminen
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurterminraumstunde-Objekte
	 * @throws ApiOperationException
	 */
	public List<GostSchuelerklausurterminraumstunde> getSchuelerklausurterminraumstundenZuSchuelerklausurterminen(final List<GostSchuelerklausurTermin> termine) throws ApiOperationException {
		return getSchuelerklausurterminraumstundenZuSchuelerklausurterminids(termine.stream().map(skt -> skt.id).toList());
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurtermin-IDs die Schülerklausurtermin-Raumstunden
	 *
	 * @param idsSchuelerklausurtermine die Liste der GostSchuelerklausurtermin-IDs
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurterminraumstunde-Objekte
	 * @throws ApiOperationException
	 */
	public List<GostSchuelerklausurterminraumstunde> getSchuelerklausurterminraumstundenZuSchuelerklausurterminids(final List<Long> idsSchuelerklausurtermine) throws ApiOperationException {
		return mapList(getSchuelerklausurterminraumstundenDTOsZuSchuelerklausurterminids(idsSchuelerklausurtermine));
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurtermin-IDs die Schülerklausurtermin-Raumstunden-DTOs
	 *
	 * @param idsSchuelerklausurtermine die Liste der GostSchuelerklausurtermin-IDs
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurterminraumstunde-DTOs
	 */
	public List<DTOGostKlausurenSchuelerklausurenTermineRaumstunden> getSchuelerklausurterminraumstundenDTOsZuSchuelerklausurterminids(final List<Long> idsSchuelerklausurtermine) {
		if (idsSchuelerklausurtermine.isEmpty())
			return new ArrayList<>();
		return conn.queryList(DTOGostKlausurenSchuelerklausurenTermineRaumstunden.QUERY_LIST_BY_SCHUELERKLAUSURTERMIN_ID,
				DTOGostKlausurenSchuelerklausurenTermineRaumstunden.class, idsSchuelerklausurtermine);
	}

	/**
	 * Liefert die zu einer Liste von Klausurraumstunden die Schülerklausurtermin-Raumstunden
	 *
	 * @param listKlausurraumstunden die Liste der Klausurraumstunden
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurterminraumstunde-Objekte
	 * @throws ApiOperationException
	 */
	public List<GostSchuelerklausurterminraumstunde> getSchuelerklausurterminraumstundenZuKlausurraumstunden(final List<GostKlausurraumstunde> listKlausurraumstunden) throws ApiOperationException {
		if (listKlausurraumstunden.isEmpty())
			return new ArrayList<>();

		final List<DTOGostKlausurenSchuelerklausurenTermineRaumstunden> dtos = conn.queryList(
				DTOGostKlausurenSchuelerklausurenTermineRaumstunden.QUERY_LIST_BY_RAUMSTUNDE_ID, DTOGostKlausurenSchuelerklausurenTermineRaumstunden.class,
				listKlausurraumstunden.stream().map(krs -> krs.id).toList());

		return mapList(dtos);
	}

	/**
	 * Liefert zu einer Raumid die Schülerklausurtermin-Raumstunden
	 *
	 * @param idRaum Raumid
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurterminraumstunde-Objekte
	 * @throws ApiOperationException
	 */
	public List<GostSchuelerklausurterminraumstunde> getSchuelerklausurterminraumstundenZuRaumid(final long idRaum) throws ApiOperationException {
		final List<GostKlausurraumstunde> listKlausurraumstunden = new DataGostKlausurenRaumstunde(conn).getKlausurraumstundenZuRaumid(idRaum);
		if (listKlausurraumstunden.isEmpty())
			return new ArrayList<>();
		return getSchuelerklausurterminraumstundenZuKlausurraumstunden(listKlausurraumstunden);

	}

	private long ermittleRaumidAusSchuelerklausurterminen(final List<Long> idsSchuelerklausurtermine)
			throws ApiOperationException {
		if (idsSchuelerklausurtermine.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<GostSchuelerklausurterminraumstunde> sktrs = getSchuelerklausurterminraumstundenZuSchuelerklausurterminids(idsSchuelerklausurtermine);
		if (sktrs.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "Keine SchuelerklausurenTermineRaumstunden gefunden.");
		final List<GostKlausurraumstunde> listKrs = new DataGostKlausurenRaumstunde(conn).getKlausurraumstundenZuSchuelerklausurterminraumstunden(sktrs);
		final List<Long> idsRaeume = listKrs.stream().map(krs -> krs.idRaum).distinct().toList();
		if (idsRaeume.size() != 1)
			throw new ApiOperationException(Status.CONFLICT, "Verschiedene Raumids in Schuelerklausuren gefunden.");
		return idsRaeume.get(0);
	}

	/**
	 * Weist die übergebenen Schülerklausuren dem entsprechenden Klausurraum zu.
	 *
	 * @param raumSchuelerZuteilung die IDs der zuzuweisenden Schülerklausuren
	 *
	 * @return die Antwort
	 * @throws ApiOperationException
	 */
	public Response loescheRaumZuSchuelerklausuren(final List<GostKlausurraumRich> raumSchuelerZuteilung) throws ApiOperationException {
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(loescheRaumZuSchuelerklausurenTransaction(raumSchuelerZuteilung))
				.build();
	}

	/**
	 * Weist die übergebenen Schülerklausuren dem entsprechenden Klausurraum zu.
	 *
	 * @param raumSchuelerZuteilung die IDs der zuzuweisenden Schülerklausuren
	 *
	 * @return die Antwort
	 * @throws ApiOperationException
	 */
	public GostKlausurenCollectionSkrsKrsData loescheRaumZuSchuelerklausurenTransaction(final List<GostKlausurraumRich> raumSchuelerZuteilung) throws ApiOperationException {
		final GostKlausurenCollectionSkrsKrsData result = new GostKlausurenCollectionSkrsKrsData();
		if (raumSchuelerZuteilung.isEmpty())
			return result;


		raumSchuelerZuteilung.stream().map(r -> r.schuelerklausurterminIDs).forEach(result.idsSchuelerklausurtermine::addAll);

		final List<DTOGostKlausurenSchuelerklausurenTermineRaumstunden> stundenAlt = getSchuelerklausurterminraumstundenDTOsZuSchuelerklausurterminids(result.idsSchuelerklausurtermine);
		conn.transactionRemoveAll(stundenAlt);
		conn.transactionFlush();
		result.raumstundenGeloescht = new DataGostKlausurenRaumstunde(conn).removeRaumStundenInDb();

		return result;
	}

	/**
	 * Weist die übergebenen Schülerklausuren dem entsprechenden Klausurraum zu.
	 *
	 * @param raumSchuelerZuteilung die IDs der zuzuweisenden Schülerklausuren
	 * @param abijahr              das Abiturjahr
	 * @param halbjahr             das Halbjahr
	 *
	 * @return die Antwort
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response setzeRaumZuSchuelerklausuren(final List<GostKlausurraumRich> raumSchuelerZuteilung,
			final int abijahr, final GostHalbjahr halbjahr) throws ApiOperationException {
		final Schuljahresabschnitt schuljahresabschnitt = DataGostKlausuren.getSchuljahresabschnittFromAbijahrUndHalbjahr(conn, abijahr, halbjahr);
		final GostKlausurenCollectionSkrsKrsData result = transactionSetzeRaumZuSchuelerklausuren(raumSchuelerZuteilung, schuljahresabschnitt);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(result).build();
	}

	/**
	 * Weist die übergebenen Schülerklausuren dem entsprechenden Klausurraum zu.
	 *
	 * @param raumSchuelerZuteilung die IDs der zuzuweisenden Schülerklausuren
	 * @param schuljahresabschnitt          der Schuljahresabschnitt
	 *
	 * @return die Antwort
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public GostKlausurenCollectionSkrsKrsData transactionSetzeRaumZuSchuelerklausuren(final List<GostKlausurraumRich> raumSchuelerZuteilung, final Schuljahresabschnitt schuljahresabschnitt) throws ApiOperationException {
		if (raumSchuelerZuteilung.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);

		final GostKlausurenCollectionSkrsKrsData result = new GostKlausurenCollectionSkrsKrsData();
		for (final GostKlausurraumRich pair : raumSchuelerZuteilung) {

			if (pair.schuelerklausurterminIDs.isEmpty())
				continue;
			final long idRaum = (pair.id != -1) ? pair.id : ermittleRaumidAusSchuelerklausurterminen(pair.schuelerklausurterminIDs);

			// Raum und Termin holen
			final GostKlausurraum raum = new DataGostKlausurenRaum(conn).getById(idRaum);
			final GostKlausurtermin termin = new DataGostKlausurenTermin(conn).getById(raum.idTermin);

			// Neue Schülerklausuren ermitteln
			final List<GostSchuelerklausurTermin> listSchuelerklausurtermineNeu =
					new DataGostKlausurenSchuelerklausurTermin(conn).getSchuelerklausurtermineZuSchuelerklausurterminids(pair.schuelerklausurterminIDs);

			// Schon im Raum existente Schülerklausuren ermitteln
			final List<GostSchuelerklausurterminraumstunde> listSchuelerklausurtermineSchonImRaumRaumstunden =
					getSchuelerklausurterminraumstundenZuRaumid(idRaum);

			final List<GostSchuelerklausurTermin> listSchuelerklausurtermine = new ArrayList<>(new DataGostKlausurenSchuelerklausurTermin(conn)
					.getSchuelerklausurtermineZuSchuelerklausurterminraumstunden(listSchuelerklausurtermineSchonImRaumRaumstunden));

			if (pair.id != -1) // überarbeiten! -1 bedeutet: Zeit der Kursklausur wird neu gesetzt, nicht
								// null bedeutet: Raum wird Schülerklausuren zugewiesen
				listSchuelerklausurtermine.addAll(listSchuelerklausurtermineNeu);


			final List<GostSchuelerklausur> listSchuelerklausuren =
					new DataGostKlausurenSchuelerklausur(conn).getSchuelerklausurenZuSchuelerklausurterminen(listSchuelerklausurtermine);

			final List<GostKursklausur> listKursklausuren = new DataGostKlausurenKursklausur(conn).getKursklausurenZuSchuelerklausuren(listSchuelerklausuren);

			final List<GostKlausurraumstunde> listRaumstunden = new DataGostKlausurenRaumstunde(conn).getKlausurraumstundenZuRaumid(idRaum);
			final List<GostKlausurtermin> listTermine = new DataGostKlausurenTermin(conn).getKlausurtermineZuSchuelerklausurterminen(listSchuelerklausurtermine);

			// Manager erzeugen
			final List<GostKlausurvorgabe> listVorgaben = new DataGostKlausurenVorgabe(conn).getKlausurvorgabenZuKursklausuren(listKursklausuren);
			final GostKlausurplanManager manager = new GostKlausurplanManager(schuljahresabschnitt.schuljahr, listVorgaben, listKursklausuren, listTermine,
					listSchuelerklausuren, listSchuelerklausurtermine);
			manager.raumAdd(raum);
			manager.raumstundeAddAll(listRaumstunden);
			final StundenplanListeEintrag sle = StundenplanListUtils.get(DataStundenplanListe.getStundenplaene(conn, schuljahresabschnitt.id), termin.datum);

			final StundenplanManager stundenplanManager =
					new StundenplanManager(DataStundenplan.getStundenplan(conn, sle.id), DataStundenplanUnterricht.getUnterrichte(conn, sle.id),
							DataStundenplanPausenaufsichten.getAufsichten(conn, sle.id),
							DataStundenplanUnterrichtsverteilung.getUnterrichtsverteilung(conn, sle.id));
			manager.setStundenplanManager(stundenplanManager);

			// Zeitraster_min und _max ermitteln
			// TODO: durch Manager ersetzen
			int minStart = 1440;
			int maxEnd = -1;
			for (final GostSchuelerklausurTermin sk : listSchuelerklausurtermine) {
				final GostKursklausur kk = manager.kursklausurBySchuelerklausurTermin(sk);
				int skStartzeit = -1;
				if (sk.startzeit != null)
					skStartzeit = sk.startzeit;
				else if (kk.startzeit != null)
					skStartzeit = kk.startzeit;
				else
					skStartzeit = manager.terminOrNullBySchuelerklausurTermin(sk).startzeit;
				if (skStartzeit < minStart)
					minStart = skStartzeit;
				final GostKlausurvorgabe v = manager.vorgabeGetByIdOrException(kk.idVorgabe);
				final int endzeit = skStartzeit + v.dauer + v.auswahlzeit;
				if (endzeit > maxEnd)
					maxEnd = endzeit;
			}

			final LocalDate klausurdatum = LocalDate.parse(termin.datum);
			final List<StundenplanZeitraster> zeitrasterRaum =
					stundenplanManager.getZeitrasterByWochentagStartVerstrichen(Wochentag.fromIDorException(klausurdatum.getDayOfWeek().getValue()), minStart,
							maxEnd - minStart);
			if (zeitrasterRaum.isEmpty())
				throw new ApiOperationException(Status.NOT_FOUND, "Zeitraster konnte nicht ermittelt werden");

			result.idsSchuelerklausurtermine.addAll(pair.schuelerklausurterminIDs);
//			result.raumdata.raeume.add(raum);

			result.raumdata.raumstunden.addAll(createRaumStundenInDb(raum, zeitrasterRaum, manager));
			conn.transactionFlush();
			result.raumdata.sktRaumstunden.addAll(createSchuelerklausurraumstundenInDb(listSchuelerklausurtermineNeu, raum, termin, manager));
			conn.transactionFlush();
			result.raumstundenGeloescht.addAll(new DataGostKlausurenRaumstunde(conn).removeRaumStundenInDb());
		}

		return result;
	}

	/**
	 * Legt die Raumstunden für den gesamten Raum an (Befüllen der Tabelle
	 * Gost_Klausuren_Raeume_Stunden)
	 *
	 * @param raum         x
	 * @param zeitrasterRaum x
	 * @param manager    x
	 * @return Liste
	 * @throws ApiOperationException
	 */
	private List<GostKlausurraumstunde> createRaumStundenInDb(final GostKlausurraum raum,
			final List<StundenplanZeitraster> zeitrasterRaum, final GostKlausurplanManager manager) throws ApiOperationException {
		// Bestimme die ID der ersten neuen Klausurraumstunde
		long idNextKrs = conn.transactionGetNextID(DTOGostKlausurenRaumstunden.class);
		final List<GostKlausurraumstunde> result = new ArrayList<>();
		for (final StundenplanZeitraster stunde : zeitrasterRaum) {
			if (manager.raumstundeGetByRaumAndZeitraster(raum, stunde) == null) {
				final DTOGostKlausurenRaumstunden dtoStundeNeu = new DTOGostKlausurenRaumstunden(idNextKrs++, raum.id, stunde.id);
				final GostKlausurraumstunde stundeNeu = new DataGostKlausurenRaumstunde(conn).map(dtoStundeNeu);
				manager.raumstundeAdd(stundeNeu);
				result.add(stundeNeu);
				conn.transactionPersist(dtoStundeNeu);
			}
		}
		return result;
	}

	/**
	 * Legt die Schuelerklausurraumstunden für jede Klausur an (Befüllen der Tabelle
	 * Gost_Klausuren_Schuelerklausuren_Raeume_Stunden)
	 *
	 * @param listSchuelerklausurenNeu x
	 * @param raum                   x
	 * @param termin                   x
	 * @param manager       x
	 *
	 * @return List
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private List<GostSchuelerklausurterminraumstunde> createSchuelerklausurraumstundenInDb(final List<GostSchuelerklausurTermin> listSchuelerklausurenNeu, final GostKlausurraum raum, final GostKlausurtermin termin,
			final GostKlausurplanManager manager) throws ApiOperationException {
		final List<GostSchuelerklausurterminraumstunde> result = new ArrayList<>();
		final LocalDate klausurdatum = LocalDate.parse(termin.datum);
		for (final GostSchuelerklausurTermin sk : listSchuelerklausurenNeu) {
			final GostKursklausur kk = manager.kursklausurBySchuelerklausurTermin(sk);
			final GostKlausurvorgabe v = manager.vorgabeGetByIdOrException(kk.idVorgabe);
			final int startzeit = (sk.startzeit != null) ? sk.startzeit
					: (((sk.folgeNr == 0) && (kk.startzeit != null)) ? kk.startzeit
							: manager.terminOrExceptionBySchuelerklausurTermin(sk).startzeit);
			final List<StundenplanZeitraster> zeitrasterSk =
					manager.getStundenplanManager().getZeitrasterByWochentagStartVerstrichen(
							Wochentag.fromIDorException(klausurdatum.getDayOfWeek().getValue()),
							startzeit, v.dauer);
			if (zeitrasterSk.isEmpty())
				throw new ApiOperationException(Status.NOT_FOUND, "Zeitraster konnte nicht ermittelt werden");
			conn.transactionExecuteDelete(
					"DELETE FROM DTOGostKlausurenSchuelerklausurenTermineRaumstunden v WHERE v.Schuelerklausurtermin_ID = %d".formatted(sk.id));
			for (final StundenplanZeitraster stunde : zeitrasterSk) {
				final DTOGostKlausurenSchuelerklausurenTermineRaumstunden skRaumStundeNeu = new DTOGostKlausurenSchuelerklausurenTermineRaumstunden(sk.id,
						manager.raumstundeGetByRaumAndZeitraster(raum, stunde).id);
				conn.transactionPersist(skRaumStundeNeu);
				result.add(map(skRaumStundeNeu));
			}
		}
		return result;
	}

//	/**
//	 * Gibt die Liste der Klausurvorgaben einer Jahrgangsstufe im übergebenen
//	 * Gost-Halbjahr zurück.
//	 *
//	 * @param idTermin die ID des Klausurtermins
//	 *
//	 * @return die Liste der Klausurraumstunden
//	 *
//	 * @throws ApiOperationException   im Fehlerfall
//	 */
//	private GostKlausurenCollectionRaumData getSchuelerklausurraumstundenByIdTermin(final Long idTermin) throws ApiOperationException {
//		return getSchuelerklausurraumstundenByTerminids(ListUtils.create1(idTermin));
//	}

	/**
	 * Gibt die Liste der Klausurvorgaben einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param pTerminIDs die ID des Klausurtermins
	 *
	 * @return die Liste der Klausurraumstunden
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public GostKlausurenCollectionRaumData getSchuelerklausurraumstundenByTerminids(final List<Long> pTerminIDs) throws ApiOperationException {
		final List<GostKlausurtermin> termin = new DataGostKlausurenTermin(conn).getKlausurtermineZuIds(pTerminIDs);
		final List<GostKlausurtermin> termine = new DataGostKlausurenTermin(conn).getKlausurterminmengeSelbesDatumZuTerminMenge(termin);
		final List<Long> terminIDs = termine.stream().map(t -> t.id).toList();
		final GostKlausurenCollectionRaumData retCollection = new GostKlausurenCollectionRaumData();
		retCollection.idsKlausurtermine = terminIDs;
		retCollection.raeume = new DataGostKlausurenRaum(conn).getKlausurraeumeZuTerminen(terminIDs);
		if (retCollection.raeume.isEmpty())
			return retCollection;
		retCollection.raumstunden = new DataGostKlausurenRaumstunde(conn).getKlausurraumstundenZuRaeumen(retCollection.raeume);
		retCollection.sktRaumstunden =
				getSchuelerklausurterminraumstundenZuKlausurraumstunden(retCollection.raumstunden);
		return retCollection;
	}

	/**
	 * Gibt die Liste der Klausurvorgaben einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param idSkts die ID des Klausurtermins
	 * @param includeSelbesDatum wenn true, werden Termine anderer Jahrgangsstufen am selben Datum eingeschlossen
	 *
	 * @return die Liste der Klausurraumstunden
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public GostKlausurenCollectionRaumData getSchuelerklausurraumstundenBySchuelerklausurterminids(final List<Long> idSkts, final boolean includeSelbesDatum)
			throws ApiOperationException {
		final GostKlausurenCollectionRaumData retCollection = new GostKlausurenCollectionRaumData();
		if (idSkts.isEmpty())
			return retCollection;

		final List<GostSchuelerklausurTermin> skts =
				new DataGostKlausurenSchuelerklausurTermin(conn).getSchuelerklausurtermineZuSchuelerklausurterminids(idSkts);
		if (includeSelbesDatum) {
			final List<GostKlausurtermin> termine = new DataGostKlausurenTermin(conn).getKlausurtermineZuIds(skts.stream().map(s -> s.idTermin).toList());
			final List<GostKlausurtermin> termineSelbesDatum = new DataGostKlausurenTermin(conn).getKlausurterminmengeSelbesDatumZuTerminMenge(termine);
			retCollection.idsKlausurtermine = termineSelbesDatum.stream().map(t -> t.id).toList();
		} else {
			retCollection.idsKlausurtermine = skts.stream().map(s -> s.idTermin).toList();
		}
		retCollection.raeume = new DataGostKlausurenRaum(conn).getKlausurraeumeZuTerminen(retCollection.idsKlausurtermine);
		if (retCollection.raeume.isEmpty())
			return retCollection;
		retCollection.raumstunden = new DataGostKlausurenRaumstunde(conn).getKlausurraumstundenZuRaeumen(retCollection.raeume);
		retCollection.sktRaumstunden = getSchuelerklausurterminraumstundenZuKlausurraumstunden(retCollection.raumstunden);
		return retCollection;
	}

}
