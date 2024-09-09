package de.svws_nrw.data.gost.klausurplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostNachschreibterminblockungKonfiguration;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurterminraumstunde;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.core.utils.gost.klausurplanung.KlausurblockungNachschreiberAlgorithmus;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausurenTermine;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausurenTermineRaumstunden;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenTermine;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostKlausurraumstunde}.
 */
public final class DataGostKlausurenSchuelerklausurTermin
		extends DataManagerRevised<Long, DTOGostKlausurenSchuelerklausurenTermine, GostSchuelerklausurTermin> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKlausurraumstunde}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenSchuelerklausurTermin(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("idSchuelerklausur", "folgeNr");
		super.setAttributesRequiredOnCreation("idSchuelerklausur", "folgeNr");
	}

	@Override
	protected void initDTO(final DTOGostKlausurenSchuelerklausurenTermine dto, final Long id) {
		dto.ID = id;
	}

	/**
	 * Gibt die Daten einer Klasse zu deren ID zurück.
	 *
	 * @param id   Die ID der Klasse.
	 *
	 * @return die Daten der KLasse zur ID.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public GostSchuelerklausurTermin getById(final Long id) throws ApiOperationException {
		final DTOGostKlausurenSchuelerklausurenTermine klasseDto = getDTO(id);
		return map(klasseDto);
	}

	/**
	 * Die Methode ermittelt das entsprechende {@link DTOKlassen} Objekt zur angegebenen Klassen ID.
	 *
	 * @param id ID der Klasse
	 *
	 * @return Ein {@link DTOKlassen} Objekt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOGostKlausurenSchuelerklausurenTermine getDTO(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für die Klasse darf nicht null sein.");

		final DTOGostKlausurenSchuelerklausurenTermine klasseDto = conn.queryByKey(DTOGostKlausurenSchuelerklausurenTermine.class, id);
		if (klasseDto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Klasse zur ID " + id + " gefunden.");

		return klasseDto;
	}

	@Override
	protected GostSchuelerklausurTermin map(final DTOGostKlausurenSchuelerklausurenTermine dto) throws ApiOperationException {
		final GostSchuelerklausurTermin daten = new GostSchuelerklausurTermin();
		daten.id = dto.ID;
		daten.idSchuelerklausur = dto.Schuelerklausur_ID;
		daten.folgeNr = dto.Folge_Nr;
		daten.idTermin = dto.Termin_ID;
		daten.startzeit = dto.Startzeit;
		daten.bemerkung = dto.Bemerkungen;
		return daten;

	}

	@Override
	protected void mapAttribute(final DTOGostKlausurenSchuelerklausurenTermine dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idSchuelerklausur" -> dto.Schuelerklausur_ID = JSONMapper.convertToLong(value, false);
			case "idTermin" -> dto.Termin_ID = JSONMapper.convertToLong(value, true);
			case "folgeNr" -> dto.Folge_Nr = JSONMapper.convertToInteger(value, false);
			case "startzeit" -> dto.Startzeit = JSONMapper.convertToIntegerInRange(value, true, 0, 1440);
			case "bemerkung" -> dto.Bemerkungen =
					JSONMapper.convertToString(value, true, false, Schema.tab_Gost_Klausuren_Schuelerklausuren_Termine.col_Bemerkungen.datenlaenge());
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}

	/**
	 * Erstellt einen neuen Gost-Schuelerklausurtermin
	 *
	 * @param id   die ID der Schülerklausur
	 *
	 * @return Eine Response mit dem neuen Gost-Klausurtermin
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response create(final long id) throws ApiOperationException {
		final DTOGostKlausurenSchuelerklausurenTermine lastTermin = conn
				.query("SELECT skt FROM DTOGostKlausurenSchuelerklausurenTermine skt WHERE skt.Schuelerklausur_ID = :skid ORDER BY skt.Folge_Nr DESC",
						DTOGostKlausurenSchuelerklausurenTermine.class)
				.setParameter("skid", id)
				.setMaxResults(1)
				.getSingleResult();
		final List<DTOGostKlausurenSchuelerklausurenTermineRaumstunden> raumstunden = conn.queryList(
				DTOGostKlausurenSchuelerklausurenTermineRaumstunden.QUERY_BY_SCHUELERKLAUSURTERMIN_ID,
				DTOGostKlausurenSchuelerklausurenTermineRaumstunden.class, lastTermin.ID);
		conn.transactionRemoveAll(raumstunden);

		final Map<String, Object> initAttributes = new HashMap<>();
		initAttributes.put("folgeNr", lastTermin.Folge_Nr + 1);
		return addFromMapAsResponse(initAttributes);
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausuren gehörigen
	 * GostSchuelerklausurtermin-Objekte zurück.
	 *
	 * @param klausuren die Liste der GostSchuelerklausuren
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurtermin-Objekte
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostSchuelerklausurTermin> getSchuelerklausurtermineZuSchuelerklausuren(final List<GostSchuelerklausur> klausuren)
			throws ApiOperationException {
		return getSchuelerklausurtermineZuSchuelerklausurids(klausuren.stream().map(sk -> sk.id).toList());
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausuren-IDs gehörigen
	 * GostSchuelerklausurtermin-Objekte zurück.
	 *
	 * @param listSkIds die Liste der GostSchuelerklausuren-IDs
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurtermin-Objekte
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostSchuelerklausurTermin> getSchuelerklausurtermineZuSchuelerklausurids(final List<Long> listSkIds)
			throws ApiOperationException {
		if (listSkIds.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenSchuelerklausurenTermine> terminDTOs = conn.queryList(
				DTOGostKlausurenSchuelerklausurenTermine.QUERY_LIST_BY_SCHUELERKLAUSUR_ID, DTOGostKlausurenSchuelerklausurenTermine.class, listSkIds);
		return mapList(terminDTOs);
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurtermin-IDs gehörigen
	 * GostSchuelerklausurtermin-Objekte zurück.
	 *
	 * @param listSkIds die Liste der GostSchuelerklausurtermin-IDs
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurtermin-Objekte
	 * @throws ApiOperationException
	 */
	public List<GostSchuelerklausurTermin> getSchuelerklausurtermineZuSchuelerklausurterminids(final List<Long> listSkIds)
			throws ApiOperationException {
		if (listSkIds.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenSchuelerklausurenTermine> terminDTOs = conn.queryByKeyList(DTOGostKlausurenSchuelerklausurenTermine.class, listSkIds);
		return mapList(terminDTOs);
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurterminraumstunden gehörigen
	 * GostSchuelerklausurtermin-Objekte zurück.
	 *
	 * @param listSktrs die Liste der GostSchuelerklausurterminraumstunden
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurtermin-Objekte
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostSchuelerklausurTermin> getSchuelerklausurtermineZuSchuelerklausurterminraumstunden(
			final List<GostSchuelerklausurterminraumstunde> listSktrs) throws ApiOperationException {
		if (listSktrs.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenSchuelerklausurenTermine> terminDTOs = conn.queryByKeyList(DTOGostKlausurenSchuelerklausurenTermine.class,
				listSktrs.stream().map(skrs -> skrs.idSchuelerklausurtermin).distinct().toList());
		return mapList(terminDTOs);
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurterminen gehörigen
	 * Datenbank-DTO-Objekte zurück.
	 *
	 * @param conn    x
	 * @param listSkts die Liste der GostSchuelerklausurtermine
	 *
	 * @return die Liste der zugehörigen Datenbank-DTOs
	 */
	public static List<DTOGostKlausurenSchuelerklausurenTermine> getSchuelerklausurterminDTOsZuSchuelerklausurterminen(final DBEntityManager conn,
			final List<GostSchuelerklausurTermin> listSkts) {
		if (listSkts.isEmpty())
			return new ArrayList<>();
		return conn.queryByKeyList(DTOGostKlausurenSchuelerklausurenTermine.class, listSkts.stream().map(skt -> skt.id).toList());
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurterminen gehörigen
	 * Datenbank-DTO-Objekte zurück.
	 *
	 * @param conn    x
	 * @param listIds die Liste der IDs der GostSchuelerklausurtermine
	 *
	 * @return die Liste der zugehörigen Datenbank-DTOs
	 */
	public static List<DTOGostKlausurenSchuelerklausurenTermine> getSchuelerklausurterminDTOsById(final DBEntityManager conn, final List<Long> listIds) {
		if (listIds.isEmpty())
			return new ArrayList<>();
		return conn.queryByKeyList(DTOGostKlausurenSchuelerklausurenTermine.class, listIds);
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurterminen gehörigen
	 * GostSchuelerklausur-Objekte zurück.
	 *
	 * @param terminIds die Liste der GostSchuelerklausurterminen
	 * @param includeAbwesend inkludiert auch GostSchuelerklausurtermine, die als abwesend gemeldet sind
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausur-Objekte
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostSchuelerklausurTermin> getSchuelerklausurtermineZuTerminIds(final List<Long> terminIds,
			final boolean includeAbwesend) throws ApiOperationException {
		if (terminIds.isEmpty())
			return new ArrayList<>();
		final List<GostKursklausur> kursklausuren = DataGostKlausurenKursklausur.getKursklausurenZuTerminids(conn, terminIds);
		final List<GostSchuelerklausur> schuelerklausuren = new DataGostKlausurenSchuelerklausur(conn).getSchuelerKlausurenZuKursklausuren(kursklausuren);
		final List<Long> kkSkIds = schuelerklausuren.stream().map(sk -> sk.id).toList();
		String skFilter = "";
		if (!kkSkIds.isEmpty()) {
			skFilter += " OR (skt.Schuelerklausur_ID IN :skIds AND skt.Folge_Nr = 0";
			if (!includeAbwesend)
				skFilter +=
						" AND NOT EXISTS (SELECT sktInner FROM DTOGostKlausurenSchuelerklausurenTermine sktInner WHERE sktInner.Schuelerklausur_ID = skt.Schuelerklausur_ID AND sktInner.Folge_Nr > 0)";
			skFilter += ")";
		}
		final TypedQuery<DTOGostKlausurenSchuelerklausurenTermine> query =
				conn.query("SELECT skt FROM DTOGostKlausurenSchuelerklausurenTermine skt WHERE skt.Termin_ID IN :tids" + skFilter,
						DTOGostKlausurenSchuelerklausurenTermine.class);
		if (!kkSkIds.isEmpty())
			query.setParameter("skIds", kkSkIds);
		final List<DTOGostKlausurenSchuelerklausurenTermine> skts = query.setParameter("tids", terminIds).getResultList();
		return new DataGostKlausurenSchuelerklausurTermin(conn).mapList(skts);
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurterminen gehörigen
	 * GostSchuelerklausur-Objekte zurück.
	 *
	 * @param terminIds die Liste der GostSchuelerklausurterminen
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausur-Objekte
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostSchuelerklausurTermin> getSchuelerklausurtermineZuTerminIds(final List<Long> terminIds)
			throws ApiOperationException {
		return getSchuelerklausurtermineZuTerminIds(terminIds, false);
	}

	/**
	 * Startet den NachschreibterminblockungAlgorithmus mit den übergebenen
	 * GostKlausurenDataCollection-Daten und persistiert die Blockung in der Datenbank.
	 *
	 * @param config das GostNachschreibterminblockungKonfiguration-Objekt
	 *
	 * @return das GostKlausurenDataCollection mit der persistierten Blockung
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public GostKlausurenCollectionAllData blocken(final GostNachschreibterminblockungKonfiguration config) throws ApiOperationException {
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Schule angelegt.");
		final DTOSchuljahresabschnitte schuljahresabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
		if (schuljahresabschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine gültiger Schuljahresabschnitt vorhanden.");

		final List<GostSchuelerklausurTermin> listSktsManager = new ArrayList<>();
		listSktsManager.addAll(config.schuelerklausurtermine);
<<<<<<< Upstream, based on dev
		listSktsManager
				.addAll(new DataGostKlausurenSchuelerklausur(conn).getSchuelerKlausurenZuTerminIds(config.termine.stream().map(t -> t.id).toList(), true));
=======
		listSktsManager.addAll(getSchuelerklausurtermineZuTerminIds(config.termine.stream().map(t -> t.id).toList(), true));
>>>>>>> 4e0a7c0 Verschieben von Terminen, die Teil einer jahrgangsübergreifenden Planung sind

		final List<GostSchuelerklausur> listSks = new DataGostKlausurenSchuelerklausur(conn).getSchuelerklausurenZuSchuelerklausurterminen(listSktsManager);
		final List<GostKursklausur> listKks = DataGostKlausurenKursklausur.getKursklausurenZuSchuelerklausuren(conn, listSks);

		final GostKlausurplanManager kMan = new GostKlausurplanManager(schuljahresabschnitt.Jahr,
				DataGostKlausurenVorgabe.getKlausurvorgabenZuKursklausuren(conn, listKks), listKks, config.termine, listSks, listSktsManager);

		final KlausurblockungNachschreiberAlgorithmus blockAlgo = new KlausurblockungNachschreiberAlgorithmus();

		final List<Pair<GostSchuelerklausurTermin, Long>> blockung = blockAlgo.berechne(config, kMan);

		final Map<Long, DTOGostKlausurenSchuelerklausurenTermine> mapNachschreiber =
				getSchuelerklausurterminDTOsZuSchuelerklausurterminen(conn, config.schuelerklausurtermine).stream()
						.collect(Collectors.toMap(skt -> skt.ID, skt -> skt));
		final Map<Long, DTOGostKlausurenTermine> mapNeueTermine = new HashMap<>();

		long idNextTermin = conn.transactionGetNextID(DTOGostKlausurenTermine.class);
		for (final Pair<GostSchuelerklausurTermin, Long> zuordnung : blockung) {
			final DTOGostKlausurenSchuelerklausurenTermine dtoSkt = DeveloperNotificationException.ifMapGetIsNull(mapNachschreiber, zuordnung.a.id);
			if (zuordnung.b >= 0) {
				dtoSkt.Termin_ID = zuordnung.b;
			} else {
				final GostKlausurvorgabe v = kMan.vorgabeBySchuelerklausurTermin(zuordnung.a);
				DTOGostKlausurenTermine neuerTermin = mapNeueTermine.get(zuordnung.b);
				if (neuerTermin == null) {
					neuerTermin = new DTOGostKlausurenTermine(idNextTermin++, v.abiJahrgang,
							GostHalbjahr.fromIDorException(v.halbjahr), v.quartal, false, true);
					conn.transactionPersist(neuerTermin);
					conn.transactionFlush();
					mapNeueTermine.put(zuordnung.b, neuerTermin);
				}
				if (neuerTermin.Quartal != v.quartal) // mehrer Klausurquartale im neuen Termin gemischt
					neuerTermin.Quartal = 0;
				dtoSkt.Termin_ID = neuerTermin.ID;
			}

		}
		conn.transactionPersistAll(mapNachschreiber.values());

		final GostKlausurenCollectionAllData blockungsDaten = new GostKlausurenCollectionAllData();
		blockungsDaten.schuelerklausurtermine = mapList(mapNachschreiber.values());
		blockungsDaten.termine = new DataGostKlausurenTermin(conn).mapList(mapNeueTermine.values());
		return blockungsDaten;
	}


}
