package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ObjLongConsumer;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenTermine;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostKlausurtermin}.
 */
public final class DataGostKlausurenTermin extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKlausurtermin}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenTermin(final DBEntityManager conn) {
		super(conn);
	}

	private static final Set<String> requiredCreateAttributes = Set.of("abijahr", "halbjahr", "quartal");
	private static final Set<String> patchForbiddenAttributes = Set.of("abijahr", "halbjahr", "istHaupttermin");

	private final Map<String, DataBasicMapper<DTOGostKlausurenTermine>> patchMappings =
			Map.ofEntries(
					Map.entry("id", (conn, dto, value, map) -> {
						final Long patch_id = JSONMapper.convertToLong(value, false);
						if ((patch_id == null) || (patch_id.longValue() != dto.ID))
							throw new ApiOperationException(Status.BAD_REQUEST);
					}),
					Map.entry("abijahr", (conn, dto, value, map) -> dto.Abi_Jahrgang = JSONMapper.convertToInteger(value, false)),
					Map.entry("halbjahr", (conn, dto, value, map) -> dto.Halbjahr =
							DataGostKlausurenVorgabe.checkHalbjahr(JSONMapper.convertToInteger(value, false))),
					Map.entry("quartal", (conn, dto, value, map) -> dto.Quartal =
							DataGostKlausurenVorgabe.checkQuartal(JSONMapper.convertToInteger(value, false))),
					Map.entry("bemerkung", (conn, dto, value, map) -> dto.Bemerkungen =
							JSONMapper.convertToString(value, true, false, Schema.tab_Gost_Klausuren_Termine.col_Bemerkungen.datenlaenge())),
					Map.entry("bezeichnung", (conn, dto, value, map) -> dto.Bezeichnung =
							JSONMapper.convertToString(value, true, false, Schema.tab_Gost_Klausuren_Termine.col_Bezeichnung.datenlaenge())),
					Map.entry("datum", (conn, dto, value, map) -> dto.Datum = JSONMapper.convertToString(value, true, false, null)),
					Map.entry("startzeit", (conn, dto, value, map) -> dto.Startzeit = JSONMapper.convertToIntegerInRange(value, true, 0, 1440)),
					Map.entry("istHaupttermin", (conn, dto, value, map) -> dto.IstHaupttermin = JSONMapper.convertToBoolean(value, false)),
					Map.entry("nachschreiberZugelassen", (conn, dto, value, map) -> {
						final boolean newValue = JSONMapper.convertToBoolean(value, false);
						if ((dto.NachschreiberZugelassen != null) && dto.NachschreiberZugelassen.booleanValue() && !newValue
								&& !DataGostKlausurenSchuelerklausurTermin.getSchuelerklausurtermineZuTerminids(conn, ListUtils.create1(dto.ID)).isEmpty())
							throw new ApiOperationException(Status.FORBIDDEN, "Klausurtermin enthält Nachschreibklausuren");
						dto.NachschreiberZugelassen = newValue;
					}));

	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenTermine} in einen Core-DTO {@link GostKlausurtermin}.
	 */
	public static final DTOMapper<DTOGostKlausurenTermine, GostKlausurtermin> dtoMapper = (final DTOGostKlausurenTermine z) -> {
		final GostKlausurtermin daten = new GostKlausurtermin();
		daten.id = z.ID;
		daten.abijahr = z.Abi_Jahrgang;
		daten.datum = z.Datum;
		daten.halbjahr = z.Halbjahr.id;
		daten.quartal = z.Quartal;
		daten.startzeit = z.Startzeit;
		daten.bezeichnung = z.Bezeichnung;
		daten.bemerkung = z.Bemerkungen;
		daten.nachschreiberZugelassen = z.NachschreiberZugelassen;
		daten.istHaupttermin = z.IstHaupttermin;
		return daten;
	};

	/**
	 * Gibt die Liste der Klausurtermine zu den übergebenen Kursklausuren zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param kursKlausuren die Liste der Kursklausuren, zu denen die Klausurtermine gesucht werden.
	 *
	 * @return die Liste der Klausurtermine
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostKlausurtermin> getKlausurtermineZuKursklausuren(final DBEntityManager conn, final List<GostKursklausur> kursKlausuren)
			throws ApiOperationException {
		if (kursKlausuren.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenTermine> terminDTOs = conn.queryByKeyList(DTOGostKlausurenTermine.class,
				kursKlausuren.stream().map(kk -> kk.idTermin).toList());
		return DTOMapper.mapList(terminDTOs, dtoMapper);
	}

	/**
	 * Gibt die Liste der Klausurtermine zu den übergebenen Schülerklausurterminen zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param schuelerklausurTermine die Liste der Schülerklausurterminen, zu denen die Klausurtermine gesucht werden.
	 *
	 * @return die Liste der Klausurtermine
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostKlausurtermin> getKlausurtermineZuSchuelerklausurterminen(final DBEntityManager conn,
			final List<GostSchuelerklausurTermin> schuelerklausurTermine) throws ApiOperationException {
		final Set<GostKlausurtermin> ergebnis = new HashSet<>();
		if (schuelerklausurTermine.isEmpty())
			return new ArrayList<>(ergebnis);
		final List<GostSchuelerklausurTermin> schuelerklausurTermineNullTermin = schuelerklausurTermine.stream().filter(skt -> skt.folgeNr == 0).toList();
		if (schuelerklausurTermineNullTermin.size() > 0) {
			final List<GostSchuelerklausur> schuelerklausurNullTermin =
					DataGostKlausurenSchuelerklausur.getSchuelerklausurenZuSchuelerklausurterminen(conn, schuelerklausurTermineNullTermin);
			final List<GostKursklausur> kursklausurNullTermin =
					DataGostKlausurenKursklausur.getKursklausurenZuSchuelerklausuren(conn, schuelerklausurNullTermin);
			ergebnis.addAll(DataGostKlausurenTermin.getKlausurtermineZuKursklausuren(conn, kursklausurNullTermin));
		}

		// TODO Termine von Kursklausuren ergänzen

		final List<DTOGostKlausurenTermine> terminDTOs = conn.queryByKeyList(DTOGostKlausurenTermine.class,
				schuelerklausurTermine.stream().map(skt -> skt.idTermin).toList());
		ergebnis.addAll(DTOMapper.mapList(terminDTOs, dtoMapper));
		return new ArrayList<>(ergebnis);
	}

	/**
	 * Gibt die Liste der Kursklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abiturjahr 	das Abiturjahr
	 * @param halbjahr das Gost-Halbjahr
	 * @param ganzesSchuljahr true, um Termine für das gesamte Schuljahr zu erhalten, false nur für das übergeben Halbjahr
	 * @param plusTerminIds
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostKlausurtermin> getKlausurtermine(final DBEntityManager conn, final int abiturjahr, final int halbjahr, final boolean ganzesSchuljahr, final List<Long> plusTerminIds)
			throws ApiOperationException {
		final GostHalbjahr ghj = (halbjahr <= 0) ? null : DataGostKlausurenVorgabe.checkHalbjahr(halbjahr);

		final String plusHJ = (ghj == null ? "" : " AND t.Halbjahr IN :hj");
		final String plusTermine = ((plusTerminIds == null || plusTerminIds.isEmpty()) ? "" : " OR t.ID IN :plusIds");
		final TypedQuery<DTOGostKlausurenTermine> query = conn.query("SELECT t FROM DTOGostKlausurenTermine t WHERE t.Abi_Jahrgang = :jgid" + plusHJ + plusTermine, DTOGostKlausurenTermine.class)
				.setParameter("jgid", abiturjahr);
		if (ghj != null)
			query.setParameter("hj", Arrays.asList(ganzesSchuljahr ? ghj.getSchuljahr() : new GostHalbjahr[] { ghj }));
		if (plusTermine.length() > 0)
			query.setParameter("plusIds", plusTerminIds);
		final List<DTOGostKlausurenTermine> terminDTOs = query.getResultList();
		return DTOMapper.mapList(terminDTOs, dtoMapper);
	}

	/**
	 * Gibt die Liste der Kursklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param termin Termin
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostKlausurtermin> getKlausurterminmengeSelbesDatumZuId(final DBEntityManager conn, final GostKlausurtermin termin)
			throws ApiOperationException {
		if (termin.datum == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Klausurtermin hat kein Datum gesetzt, ID: " + termin.id);

		final List<GostKlausurtermin> termine =
				DTOMapper.mapList(conn.queryList(DTOGostKlausurenTermine.QUERY_BY_DATUM, DTOGostKlausurenTermine.class, termin.datum), dtoMapper);
//		termine.remove(termin);

		return termine;
	}

	/**
	 * Gibt die Liste der Kursklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param termine Termin-Liste
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostKlausurtermin> getKlausurterminmengeSelbesDatumZuTerminMenge(final DBEntityManager conn, final List<GostKlausurtermin> termine)
			throws ApiOperationException {
		if (termine.isEmpty())
			return new ArrayList<>();
//		if (termin.datum == null)
//			throw new ApiOperationException(Status.BAD_REQUEST, "Klausurtermin hat kein Datum gesetzt, ID: " + termin.id);

		final List<GostKlausurtermin> termineErgebnis =
				DTOMapper.mapList(conn.queryList(DTOGostKlausurenTermine.QUERY_LIST_BY_DATUM, DTOGostKlausurenTermine.class, termine.stream().map(t -> t.datum).toList()), dtoMapper);
//		termine.remove(termin);

		return termineErgebnis;
	}

	/**
	 * Gibt den Klausurtermin zur übergebenen ID zurück oder eine Exception, falls er nicht in der DB vorhanden ist.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return der Klausurtermin
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static GostKlausurtermin getKlausurterminZuId(final DBEntityManager conn, final long idTermin) throws ApiOperationException {
		final DTOGostKlausurenTermine termin = conn.queryByKey(DTOGostKlausurenTermine.class, idTermin);
		if (termin == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Klausurtermin nicht gefunden, ID: " + idTermin);
		return dtoMapper.apply(termin);
	}

	/**
	 * Gibt die Klausurtermine zur übergebenen ID-Liste zurück oder eine Exception, falls er nicht in der DB vorhanden ist.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param listIds	 die Liste von IDs der Klausurtermine
	 *
	 * @return der Klausurtermin
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostKlausurtermin> getKlausurtermineZuIds(final DBEntityManager conn, final List<Long> listIds) throws ApiOperationException {
		final List<DTOGostKlausurenTermine> terminDTOs = getKlausurterminDTOsZuIds(conn, listIds);
		return DTOMapper.mapList(terminDTOs, dtoMapper);
	}

	/**
	 * Gibt die Klausurtermin-DTOs zur übergebenen ID-Liste zurück oder eine Exception, falls er nicht in der DB vorhanden ist.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param listIds	 die Liste von IDs der Klausurtermine
	 *
	 * @return der Klausurtermin
	 */
	public static List<DTOGostKlausurenTermine> getKlausurterminDTOsZuIds(final DBEntityManager conn, final List<Long> listIds) {
		if (listIds.isEmpty())
			return new ArrayList<>();
		return conn.queryByKeyList(DTOGostKlausurenTermine.class, listIds);
	}


	@Override
	public Response get(final Long halbjahr) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasicFiltered(id, is, DTOGostKlausurenTermine.class, patchMappings, patchForbiddenAttributes);
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Erstellt einen neuen Gost-Klausurtermin *
	 *
	 * @param is   Das JSON-Objekt mit den Daten
	 *
	 * @return Eine Response mit dem neuen Gost-Klausurtermin
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response create(final InputStream is) throws ApiOperationException {
		final ObjLongConsumer<DTOGostKlausurenTermine> initDTO = (dto, id) -> dto.ID = id;
		return super.addBasic(is, DTOGostKlausurenTermine.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Löscht eine Gost-Klausurvorgabe *
	 *
	 * @param id die ID der zu löschenden Klausurvorgabe
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response delete(final Long id) throws ApiOperationException {
		return super.deleteBasic(id, DTOGostKlausurenTermine.class, dtoMapper);
	}

	/**
	 * Löscht einen Gost-Klausurtermin *
	 *
	 * @param ids die IDs der zu löschenden Klausurtermine
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response delete(final List<Long> ids) throws ApiOperationException {
		return super.deleteBasicMultiple(ids, DTOGostKlausurenTermine.class, dtoMapper);
	}

}
