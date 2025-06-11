package de.svws_nrw.data.gost.klausurplan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.core.utils.ListUtils;
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
public final class DataGostKlausurenSchuelerklausurraumstunde
		extends DataManagerRevised<Long, DTOGostKlausurenSchuelerklausurenTermineRaumstunden, GostSchuelerklausurterminraumstunde> {

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
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<GostSchuelerklausurterminraumstunde> getSchuelerklausurterminraumstundenZuSchuelerklausurterminen(final List<GostSchuelerklausurTermin> termine)
			throws ApiOperationException {
		return getSchuelerklausurterminraumstundenZuSchuelerklausurterminids(termine.stream().map(skt -> skt.id).toList());
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurtermin-IDs die Schülerklausurtermin-Raumstunden
	 *
	 * @param idsSchuelerklausurtermine die Liste der GostSchuelerklausurtermin-IDs
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurterminraumstunde-Objekte
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<GostSchuelerklausurterminraumstunde> getSchuelerklausurterminraumstundenZuSchuelerklausurterminids(final List<Long> idsSchuelerklausurtermine)
			throws ApiOperationException {
		return mapList(getSchuelerklausurterminraumstundenDTOsZuSchuelerklausurterminids(idsSchuelerklausurtermine));
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurtermin-IDs die Schülerklausurtermin-Raumstunden-DTOs
	 *
	 * @param idsSchuelerklausurtermine die Liste der GostSchuelerklausurtermin-IDs
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurterminraumstunde-DTOs
	 */
	public List<DTOGostKlausurenSchuelerklausurenTermineRaumstunden> getSchuelerklausurterminraumstundenDTOsZuSchuelerklausurterminids(
			final List<Long> idsSchuelerklausurtermine) {
		if (idsSchuelerklausurtermine.isEmpty())
			return new ArrayList<>();
		return conn.queryList(DTOGostKlausurenSchuelerklausurenTermineRaumstunden.QUERY_LIST_BY_SCHUELERKLAUSURTERMIN_ID,
				DTOGostKlausurenSchuelerklausurenTermineRaumstunden.class, idsSchuelerklausurtermine);
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurterminen die Schülerklausurtermin-Raumstunden-DTOs
	 *
	 * @param schuelerklausurtermine die Liste der GostSchuelerklausurtermine
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurterminraumstunde-DTOs
	 */
	public List<DTOGostKlausurenSchuelerklausurenTermineRaumstunden> getSchuelerklausurterminraumstundenDTOsZuSchuelerklausurterminen(
			final List<GostSchuelerklausurTermin> schuelerklausurtermine) {
		return getSchuelerklausurterminraumstundenDTOsZuSchuelerklausurterminids(schuelerklausurtermine.stream().map(skt -> skt.id).toList());
	}

	/**
	 * Liefert die zu einer Liste von Klausurraumstunden die Schülerklausurtermin-Raumstunden
	 *
	 * @param listKlausurraumstunden die Liste der Klausurraumstunden
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurterminraumstunde-Objekte
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<GostSchuelerklausurterminraumstunde> getSchuelerklausurterminraumstundenZuKlausurraumstunden(
			final Collection<GostKlausurraumstunde> listKlausurraumstunden) throws ApiOperationException {
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
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<GostSchuelerklausurterminraumstunde> getSchuelerklausurterminraumstundenZuRaumid(final long idRaum) throws ApiOperationException {
		final List<GostKlausurraumstunde> listKlausurraumstunden = new DataGostKlausurenRaumstunde(conn).getKlausurraumstundenZuRaumid(idRaum);
		if (listKlausurraumstunden.isEmpty())
			return new ArrayList<>();
		return getSchuelerklausurterminraumstundenZuKlausurraumstunden(listKlausurraumstunden);

	}

	/**
	 * Löscht zu den übergebenen IDs der {@link GostSchuelerklausurTermin}e die Raumzuweisung
	 *
	 * @param sktIds die IDs der {@link GostSchuelerklausurTermin}e zu denen die Raumzuweisung gelöscht werden soll
	 *
	 * @return das {@link GostKlausurenCollectionSkrsKrsData}-Objekt mit den geänderten Raumdaten	 */
	public Response loescheRaumZuSchuelerklausuren(final List<Long> sktIds) throws ApiOperationException {
		return Response.status(Status.OK)
				.type(MediaType.APPLICATION_JSON)
				.entity(loescheRaumZuSchuelerklausurIds(sktIds))
				.build();
	}

	/**
	 * Löscht zu den übergebenen {@link GostSchuelerklausurTermin}en die Raumzuweisung
	 *
	 * @param termine die {@link GostSchuelerklausurTermin}e zu denen die Raumzuweisung gelöscht werden soll
	 *
	 * @return das {@link GostKlausurenCollectionSkrsKrsData}-Objekt mit den geänderten Raumdaten
	 */
	public GostKlausurenCollectionSkrsKrsData loescheRaumZuSchuelerklausurenTransaction(final List<GostSchuelerklausurTermin> termine)
			throws ApiOperationException {
		return loescheRaumZuSchuelerklausurIds(termine.stream().map(t -> t.id).toList());
	}

	/**
	 * Löscht zu den übergebenen IDs der {@link GostSchuelerklausurTermin}e die Raumzuweisung
	 *
	 * @param sktIds die IDs der {@link GostSchuelerklausurTermin}e zu denen die Raumzuweisung gelöscht werden soll
	 *
	 * @return das {@link GostKlausurenCollectionSkrsKrsData}-Objekt mit den geänderten Raumdaten
	 */
	public GostKlausurenCollectionSkrsKrsData loescheRaumZuSchuelerklausurIds(final List<Long> sktIds)
			throws ApiOperationException {
		final GostKlausurenCollectionSkrsKrsData result = new GostKlausurenCollectionSkrsKrsData();
		if (sktIds.isEmpty())
			return result;

		final List<DTOGostKlausurenSchuelerklausurenTermineRaumstunden> stundenAlt = getSchuelerklausurterminraumstundenDTOsZuSchuelerklausurterminids(sktIds);
		conn.transactionRemoveAll(stundenAlt);
		conn.transactionFlush();
		result.raumstundenGeloescht = new DataGostKlausurenRaumstunde(conn).removeRaumStundenInDb();

		return result;
	}

	/**
	 * Weist die übergebenen Schülerklausuren dem entsprechenden Klausurraum zu.
	 *
	 * @param raumSchuelerZuteilung die IDs der zuzuweisenden Schülerklausuren
	 *
	 * @return die Antwort
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response setzeRaumZuSchuelerklausuren(final List<GostKlausurraumRich> raumSchuelerZuteilung) throws ApiOperationException {
		final GostKlausurenCollectionSkrsKrsData result = setzeRaumZuSchuelerklausurterminen(raumSchuelerZuteilung);
		return Response.status(Status.OK)
				.type(MediaType.APPLICATION_JSON)
				.entity(result)
				.build();
	}

	private GostKlausurplanManager createKlausurManagerMitStundenplan(final List<GostKlausurtermin> termine,
			final List<GostSchuelerklausurTermin> schuelerklausurtermine, final Collection<GostKlausurraum> raeume) throws ApiOperationException {

		final Set<GostKlausurtermin> manTermine = new HashSet<>();
		final Set<GostKlausurraum> manRaeume = new HashSet<>();
		final Set<GostSchuelerklausurTermin> manSchuelerklausurtermine = new HashSet<>();
		final Set<GostKlausurraumstunde> manRaumstunden = new HashSet<>();
		final Set<GostSchuelerklausurterminraumstunde> manSchuelerklausurterminraumstunden = new HashSet<>();

		if (termine != null) {
			manTermine.addAll(termine);
			manRaeume.addAll(new DataGostKlausurenRaum(conn).getKlausurraeumeZuTerminen(termine));
			manSchuelerklausurtermine.addAll(new DataGostKlausurenSchuelerklausurTermin(conn).getSchuelerklausurtermineZuTerminen(termine));
		}

		if (schuelerklausurtermine != null) {
			manSchuelerklausurtermine.addAll(schuelerklausurtermine);
			manRaeume.addAll(new DataGostKlausurenRaum(conn).getKlausurraeumeZuTerminen(new DataGostKlausurenTermin(conn).getKlausurtermineZuSchuelerklausurterminen(schuelerklausurtermine)));
			manSchuelerklausurterminraumstunden.addAll(
					new DataGostKlausurenSchuelerklausurraumstunde(conn).getSchuelerklausurterminraumstundenZuSchuelerklausurterminen(schuelerklausurtermine));
			manRaumstunden.addAll(new DataGostKlausurenRaumstunde(conn).getKlausurraumstundenZuSchuelerklausurterminraumstunden(manSchuelerklausurterminraumstunden));
		}

		if (raeume != null)
			manRaeume.addAll(raeume);

		manRaeume.addAll(new DataGostKlausurenRaum(conn).getKlausurraeumeZuRaumstunden(manRaumstunden));
		manRaumstunden.addAll(new DataGostKlausurenRaumstunde(conn).getKlausurraumstundenZuRaeumen(manRaeume));
		manSchuelerklausurterminraumstunden
				.addAll(new DataGostKlausurenSchuelerklausurraumstunde(conn).getSchuelerklausurterminraumstundenZuKlausurraumstunden(manRaumstunden));
		manSchuelerklausurtermine
				.addAll(new DataGostKlausurenSchuelerklausurTermin(conn).getSchuelerklausurtermineZuSchuelerklausurterminraumstunden(manSchuelerklausurterminraumstunden));

		final GostKlausurenCollectionRaumData raumData = new GostKlausurenCollectionRaumData();
		raumData.raeume.addAll(manRaeume);
		raumData.sktRaumstunden.addAll(manSchuelerklausurterminraumstunden);
		raumData.raumstunden.addAll(manRaumstunden);

		manTermine.addAll(new DataGostKlausurenTermin(conn).getKlausurtermineZuSchuelerklausurterminen(manSchuelerklausurtermine));
		final List<GostSchuelerklausur> schuelerklausuren =
				new DataGostKlausurenSchuelerklausur(conn).getSchuelerklausurenZuSchuelerklausurterminen(manSchuelerklausurtermine);
		final List<GostKursklausur> kursklausuren =
				new DataGostKlausurenKursklausur(conn).getKursklausurenZuSchuelerklausuren(schuelerklausuren);
		final List<GostKlausurvorgabe> vorgaben =
				new DataGostKlausurenVorgabe(conn).getKlausurvorgabenZuKursklausuren(kursklausuren);

		final GostKlausurplanManager manager = new GostKlausurplanManager(vorgaben, kursklausuren, manTermine, schuelerklausuren, manSchuelerklausurtermine);
		manager.addRaumData(raumData);

		long idSja = -1;
		List<StundenplanListeEintrag> listSle = new ArrayList<>();
		for (final GostKlausurtermin termin : manager.terminMitDatumGetMenge()) {
			if (manager.stundenplanManagerGetByTerminOrNull(termin) == null) {
				if (idSja != termin.idSchuljahresabschnitt) {
					idSja = termin.idSchuljahresabschnitt;
					listSle = DataStundenplanListe.getStundenplaeneAktiv(conn, idSja);
				}
				final StundenplanListeEintrag sle = StundenplanListUtils.get(listSle, termin.datum);
				final StundenplanManager stundenplanManager = new StundenplanManager(
						new DataStundenplan(conn).getById(sle.id),
						DataStundenplanUnterricht.getUnterrichte(conn, sle.id),
						DataStundenplanPausenaufsichten.getAufsichten(conn, sle.id),
						DataStundenplanUnterrichtsverteilung.getUnterrichtsverteilung(conn, sle.id)
				);
				manager.stundenplanManagerAdd(stundenplanManager);
			}
		}
		return manager;
	}

	private GostKlausurenCollectionSkrsKrsData recreateRaumstunden(final GostKlausurraumRich raum, final GostKlausurplanManager manager)
			throws ApiOperationException {

		final GostKlausurenCollectionSkrsKrsData result = new GostKlausurenCollectionSkrsKrsData();

		final List<GostSchuelerklausurTermin> skts = manager.schuelerklausurterminGetMengeByRaumid(raum.klausurraum.id);
		if (!raum.schuelerklausurterminIDs.isEmpty())
			skts.addAll(new DataGostKlausurenSchuelerklausurTermin(conn).getSchuelerklausurtermineZuSchuelerklausurterminids(raum.schuelerklausurterminIDs));
		final GostKlausurtermin termin = manager.terminGetByIdOrException(raum.klausurraum.idTermin);

		int minStart = manager.minKlausurstartzeitBySchuelerklausurterminMenge(skts, true);
		int maxEnd = manager.maxKlausurendzeitBySchuelerklausurterminMenge(skts, true);

		final LocalDate klausurdatum = LocalDate.parse(termin.datum);

		final StundenplanManager stundenplanManager = manager.stundenplanManagerGetByTerminOrException(termin);

		final List<StundenplanZeitraster> zeitrasterRaum =
				stundenplanManager.getZeitrasterByWochentagStartVerstrichen(Wochentag.fromIDorException(klausurdatum.getDayOfWeek().getValue()), minStart,
						maxEnd - minStart);
		if (zeitrasterRaum.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "Zeitraster konnte nicht ermittelt werden");

		result.raumdata.raumstunden.addAll(createRaumStundenInDb(raum.klausurraum, zeitrasterRaum, manager));
		conn.transactionFlush();
		result.addAll(createSchuelerklausurraumstundenInDb(skts, raum.klausurraum, termin, manager));
		conn.transactionFlush();
		result.raumstundenGeloescht.addAll(new DataGostKlausurenRaumstunde(conn).removeRaumStundenInDb());

		return result;

	}

	/**
	 * Aktualisiert die RaumStunden und SchülerklausurterminRaumstunden zu allen Räumen eines Klausurtermins
	 * @param termin der zu aktualisierende Klausurtermin
	 * @return das {@link GostKlausurenCollectionSkrsKrsData}-Objekt mit den geänderten Raumdaten
	 */
	public GostKlausurenCollectionSkrsKrsData updateRaeumeZuKlausurtermin(final GostKlausurtermin termin) throws ApiOperationException {

		final List<GostKlausurraum> raeume = new DataGostKlausurenRaum(conn).getKlausurraeumeZuTerminen(ListUtils.create1(termin));
		final GostKlausurplanManager manager = createKlausurManagerMitStundenplan(ListUtils.create1(termin), null, null);
		final GostKlausurenCollectionSkrsKrsData result = new GostKlausurenCollectionSkrsKrsData();

		for (final GostKlausurraum raum : raeume) {
			if (manager.schuelerklausurterminGetMengeByRaum(raum).isEmpty())
				continue;
			result.addAll(recreateRaumstunden(new GostKlausurraumRich(raum, null), manager));
		}

		return result;
	}

	/**
	 * Weist die übergebenen Schülerklausuren dem entsprechenden Klausurraum zu.
	 *
	 * @param zuteilungen die IDs der zuzuweisenden Schülerklausuren
	 *
	 * @return die Antwort
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public GostKlausurenCollectionSkrsKrsData setzeRaumZuSchuelerklausurterminen(final List<GostKlausurraumRich> zuteilungen)
			throws ApiOperationException {
		if (zuteilungen.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);

		final GostKlausurenCollectionSkrsKrsData result = new GostKlausurenCollectionSkrsKrsData();

		for (final GostKlausurraumRich raum : zuteilungen) {
			if (raum.schuelerklausurterminIDs == null || raum.schuelerklausurterminIDs.isEmpty())
				continue;

			final List<GostSchuelerklausurTermin> neu =
					new DataGostKlausurenSchuelerklausurTermin(conn).getSchuelerklausurtermineZuSchuelerklausurterminids(raum.schuelerklausurterminIDs);
			final List<GostSchuelerklausurterminraumstunde> schonImRaum =
					getSchuelerklausurterminraumstundenZuRaumid(raum.klausurraum.id);
			final List<GostSchuelerklausurTermin> gesamt = new ArrayList<>(
					new DataGostKlausurenSchuelerklausurTermin(conn).getSchuelerklausurtermineZuSchuelerklausurterminraumstunden(schonImRaum)
			);
			gesamt.addAll(neu);
			final GostKlausurplanManager manager = createKlausurManagerMitStundenplan(null, gesamt, ListUtils.create1(raum.klausurraum));
			final GostKlausurenCollectionSkrsKrsData partial = recreateRaumstunden(raum, manager);
			result.addAll(partial);
		}

		return result;
	}


	/**
	 * Weist die übergebenen Schülerklausuren dem entsprechenden Klausurraum zu.
	 *
	 * @param skts die Schülerklausurtermine
	 *
	 * @return die Antwort
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public GostKlausurenCollectionSkrsKrsData updateRaeumeZuSchuelerklausurterminen(final List<GostSchuelerklausurTermin> skts)
			throws ApiOperationException {
		if (skts.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);

		final GostKlausurenCollectionSkrsKrsData result = new GostKlausurenCollectionSkrsKrsData();

		final GostKlausurplanManager manager = createKlausurManagerMitStundenplan(null, skts, null);

		final Set<GostKlausurraum> raeume = skts.stream().map(manager::raumGetBySchuelerklausurtermin).collect(Collectors.toSet());

		for (final GostKlausurraum raum : raeume)
			result.addAll(recreateRaumstunden(new GostKlausurraumRich(raum, null), manager));

		return result;
	}

	/**
	 * Legt die {@link GostKlausurraumstunde}n im {@link GostKlausurraum} für die übergebenen {@link StundenplanZeitraster} an.
	 *
	 * @param raum der {@link GostKlausurraum}, für den die Raumstunden angelegt werden sollen
	 * @param zeitrasterRaum die Liste von {@link StundenplanZeitraster}n, für die Raumstunden angelegt werden sollen
	 * @param manager der {@link GostKlausurplanManager}
	 *
	 * @return die Liste von in der Datenbank neu angelegten {@link GostKlausurraumstunde}n
	 */
	private List<GostKlausurraumstunde> createRaumStundenInDb(final GostKlausurraum raum,
			final List<StundenplanZeitraster> zeitrasterRaum, final GostKlausurplanManager manager) throws ApiOperationException {
		final List<GostKlausurraumstunde> result = new ArrayList<>();
		long idNextKrs = conn.transactionGetNextID(DTOGostKlausurenRaumstunden.class);
		for (final StundenplanZeitraster stunde : zeitrasterRaum) {
			if (manager.raumstundeGetByRaumAndZeitrasterOrNull(raum, stunde) == null)
				result.add(createRaumStundeInDb(raum, stunde, manager, idNextKrs++));
		}
		return result;
	}

	private GostKlausurraumstunde createRaumStundeInDb(final GostKlausurraum raum,
			final StundenplanZeitraster zeitraster, final GostKlausurplanManager manager, final long idNext) throws ApiOperationException {
		// Bestimme die ID der ersten neuen Klausurraumstunde
		if (manager.raumstundeGetByRaumAndZeitrasterOrNull(raum, zeitraster) != null)
			throw new DeveloperNotificationException("Raumstunde für Raum %d und Zeitraster %d existiert bereits.".formatted(raum.id, zeitraster.id));
		final DTOGostKlausurenRaumstunden dtoStundeNeu = new DTOGostKlausurenRaumstunden(idNext, raum.id);
		dtoStundeNeu.Zeitraster_ID = zeitraster.id;
		final GostKlausurraumstunde ergebnis = new DataGostKlausurenRaumstunde(conn).map(dtoStundeNeu);
		manager.raumstundeAdd(ergebnis);
		conn.transactionPersist(dtoStundeNeu);
		return ergebnis;
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
	private GostKlausurenCollectionSkrsKrsData createSchuelerklausurraumstundenInDb(final List<GostSchuelerklausurTermin> listSchuelerklausurenNeu,
			final GostKlausurraum raum, final GostKlausurtermin termin,
			final GostKlausurplanManager manager) throws ApiOperationException {
		final GostKlausurenCollectionSkrsKrsData ergebnis = new GostKlausurenCollectionSkrsKrsData();
		final LocalDate klausurdatum = LocalDate.parse(termin.datum);
		for (final GostSchuelerklausurTermin sk : listSchuelerklausurenNeu) {
			final int startzeit = manager.startzeitBySchuelerklausurterminOrException(sk);
			final List<StundenplanZeitraster> zeitrasterSk =
					manager.stundenplanManagerGetByTerminOrException(termin).getZeitrasterByWochentagStartVerstrichen(
							Wochentag.fromIDorException(klausurdatum.getDayOfWeek().getValue()),
							startzeit, manager.vorgabeBySchuelerklausurTermin(sk).dauer);
			if (zeitrasterSk.isEmpty())
				throw new ApiOperationException(Status.NOT_FOUND, "Zeitraster konnte nicht ermittelt werden");
			conn.transactionExecuteDelete(
					"DELETE FROM DTOGostKlausurenSchuelerklausurenTermineRaumstunden v WHERE v.Schuelerklausurtermin_ID = %d".formatted(sk.id));
			final List<GostSchuelerklausurterminraumstunde> vorhanden = manager.schuelerklausurraumstundeGetMengeByIdSchuelerklausurtermin(sk.id);
			for (final StundenplanZeitraster stunde : zeitrasterSk) {
				GostKlausurraumstunde raumstundeVorhanden = manager.raumstundeGetByRaumAndZeitrasterOrNull(raum, stunde);
				if (raumstundeVorhanden == null) {
					raumstundeVorhanden = createRaumStundeInDb(raum, stunde, manager, conn.transactionGetNextID(DTOGostKlausurenRaumstunden.class));
					ergebnis.raumdata.raumstunden.add(raumstundeVorhanden);
				}
				final DTOGostKlausurenSchuelerklausurenTermineRaumstunden skRaumStundeNeu = new DTOGostKlausurenSchuelerklausurenTermineRaumstunden(sk.id,
						raumstundeVorhanden.id);
				conn.transactionPersist(skRaumStundeNeu);
				final GostSchuelerklausurterminraumstunde aktuell = map(skRaumStundeNeu);
				if (!(vorhanden.contains(aktuell)))
					ergebnis.raumdata.sktRaumstunden.add(aktuell);
				else if (vorhanden.contains(aktuell))
					vorhanden.remove(aktuell);
			}
			ergebnis.schuelerklausurterminraumstundenGeloescht.addAll(vorhanden);
		}
		return ergebnis;
	}

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
	public GostKlausurenCollectionRaumData getRaumDataByTerminids(final List<Long> pTerminIDs) throws ApiOperationException {
		final List<GostKlausurtermin> termin = new DataGostKlausurenTermin(conn).getKlausurtermineZuIds(pTerminIDs);
		final List<GostKlausurtermin> termine = new DataGostKlausurenTermin(conn).getKlausurterminmengeSelbesDatumZuTerminMenge(termin);
		final List<Long> terminIDs = termine.stream().map(t -> t.id).toList();
		final GostKlausurenCollectionRaumData retCollection = new GostKlausurenCollectionRaumData();
		retCollection.idsKlausurtermine = terminIDs;
		retCollection.raeume = new DataGostKlausurenRaum(conn).getKlausurraeumeZuTerminIDs(terminIDs);
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
		retCollection.raeume = new DataGostKlausurenRaum(conn).getKlausurraeumeZuTerminIDs(retCollection.idsKlausurtermine);
		if (retCollection.raeume.isEmpty())
			return retCollection;
		retCollection.raumstunden = new DataGostKlausurenRaumstunde(conn).getKlausurraumstundenZuRaeumen(retCollection.raeume);
		retCollection.sktRaumstunden = getSchuelerklausurterminraumstundenZuKlausurraumstunden(retCollection.raumstunden);
		return retCollection;
	}

}
