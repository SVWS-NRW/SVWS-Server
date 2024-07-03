package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkSkt;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenDataCollection;
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
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausurenTermine;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostSchuelerklausur}.
 */
public final class DataGostKlausurenSchuelerklausur extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostSchuelerklausur}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenSchuelerklausur(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Gibt die Liste der Schülerklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück, die eine Nachschreibklausur beinhalten.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das Gost-Halbjahr
	 *
	 * @return die Liste der Schülerklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static GostKlausurenCollectionSkSkt getCollectionSkSktNachschreiber(final DBEntityManager conn, final int abiturjahr, final GostHalbjahr halbjahr)
			throws ApiOperationException {
		final GostKlausurenCollectionSkSkt ergebnis = new GostKlausurenCollectionSkSkt();
		final List<GostKursklausur> kursKlausuren = DataGostKlausurenKursklausur.getKursKlausuren(conn, abiturjahr, halbjahr.id, false);
		if (!kursKlausuren.isEmpty()) {
			final List<DTOGostKlausurenSchuelerklausuren> schuelerKlausurDTOs = conn.query(
					"SELECT DISTINCT sk FROM DTOGostKlausurenSchuelerklausuren sk JOIN DTOGostKlausurenSchuelerklausurenTermine skt ON sk.ID = skt.Schuelerklausur_ID AND sk.Kursklausur_ID IN :kkids WHERE skt.Folge_Nr > 0",
					DTOGostKlausurenSchuelerklausuren.class).setParameter("kkids", kursKlausuren.stream().map(kk -> kk.id).toList()).getResultList();
			ergebnis.schuelerklausuren = DTOMapper.mapList(schuelerKlausurDTOs, DataGostKlausurenSchuelerklausur.dtoMapper);
			ergebnis.schuelerklausurtermine =
					DataGostKlausurenSchuelerklausurTermin.getSchuelerklausurtermineZuSchuelerklausuren(conn, ergebnis.schuelerklausuren);
		}
		return ergebnis;
	}


	/**
	 * Gibt die Liste der Schülerklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück, die eine Nachschreibklausur beinhalten.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das Gost-Halbjahr
	 * @param ganzesSchuljahr true, um Klausuren für das gesamte Schuljahr zu erhalten, false nur für das übergeben Halbjahr
	 *
	 * @return die Liste der Schülerklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static GostKlausurenCollectionSkSkt getCollectionSkSkt(final DBEntityManager conn, final int abiturjahr, final GostHalbjahr halbjahr,
			final boolean ganzesSchuljahr) throws ApiOperationException {
		final GostKlausurenCollectionSkSkt ergebnis = new GostKlausurenCollectionSkSkt();
		final List<GostKursklausur> kursKlausuren = DataGostKlausurenKursklausur.getKursKlausuren(conn, abiturjahr, halbjahr.id, ganzesSchuljahr);
		ergebnis.schuelerklausuren = getSchuelerKlausurenZuKursklausuren(conn, kursKlausuren);
		ergebnis.schuelerklausurtermine = DataGostKlausurenSchuelerklausurTermin.getSchuelerklausurtermineZuSchuelerklausuren(conn, ergebnis.schuelerklausuren);
		return ergebnis;
	}

	/**
	 * Gibt die Liste der Schülerklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück, die eine Nachschreibklausur beinhalten.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param kursKlausuren die Liste der Kursklausuren, für die die Schülerklausuren gesucht werden
	 *
	 * @return die Liste der Schülerklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostSchuelerklausur> getSchuelerKlausurenZuKursklausuren(final DBEntityManager conn, final List<GostKursklausur> kursKlausuren)
			throws ApiOperationException {
		if (kursKlausuren.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenSchuelerklausuren> schuelerKlausurDTOs = conn.queryList(DTOGostKlausurenSchuelerklausuren.QUERY_LIST_BY_KURSKLAUSUR_ID,
				DTOGostKlausurenSchuelerklausuren.class, kursKlausuren.stream().map(kk -> kk.id).toList());
		return DTOMapper.mapList(schuelerKlausurDTOs, DataGostKlausurenSchuelerklausur.dtoMapper);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenSchuelerklausuren} in einen Core-DTO
	 * {@link GostSchuelerklausur}.
	 */
	public static final DTOMapper<DTOGostKlausurenSchuelerklausuren, GostSchuelerklausur> dtoMapper = (final DTOGostKlausurenSchuelerklausuren sk) -> {
		final GostSchuelerklausur daten = new GostSchuelerklausur();
		daten.idKursklausur = sk.Kursklausur_ID;
		daten.idSchueler = sk.Schueler_ID;
		daten.id = sk.ID;
		daten.bemerkung = sk.Bemerkungen;
		return daten;
	};

	private static final Set<String> forbiddenPatchAttributes = Set.of("idSchuelerklausur", "idKursklausur", "idSchueler");

	private final Map<String, DataBasicMapper<DTOGostKlausurenSchuelerklausuren>> patchMappings = Map.ofEntries(
			Map.entry("idSchuelerklausur", (conn, dto, value, map) -> dto.Schueler_ID = JSONMapper.convertToLong(value, false)),
			Map.entry("idKursklausur", (conn, dto, value, map) -> dto.Kursklausur_ID = JSONMapper.convertToLong(value, false)),
			Map.entry("idSchueler", (conn, dto, value, map) -> dto.Schueler_ID = JSONMapper.convertToLong(value, false)),
			Map.entry("bemerkung", (conn, dto, value, map) -> dto.Bemerkungen =
					JSONMapper.convertToString(value, true, true, Schema.tab_Gost_Klausuren_Schuelerklausuren.col_Bemerkungen.datenlaenge())));

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasicFiltered(id, is, DTOGostKlausurenSchuelerklausuren.class, patchMappings, forbiddenPatchAttributes);
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(getSchuelerKlausurenZuTerminIds(conn, ListUtils.create1(id))).build();
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurterminen gehörigen
	 * GostSchuelerklausur-Objekte zurück.
	 *
	 * @param conn    x
	 * @param termine die Liste der GostSchuelerklausurterminen
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausur-Objekte
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostSchuelerklausur> getSchuelerklausurenZuSchuelerklausurterminen(final DBEntityManager conn,
			final List<GostSchuelerklausurTermin> termine) throws ApiOperationException {
		if (termine.isEmpty())
			return new ArrayList<>();
		final List<GostSchuelerklausur> sks = DTOMapper.mapList(conn.queryByKeyList(DTOGostKlausurenSchuelerklausuren.class,
				termine.stream().map(sk -> sk.idSchuelerklausur).toList()), DataGostKlausurenSchuelerklausur.dtoMapper);
		if (sks.isEmpty())
			throw new ApiOperationException(Status.CONFLICT, "Schülerklausuren zu Schülerklausurterminen nicht gefunden.");
		return sks;
	}

	/**
	 * Weist die übergebenen Schülerklausuren dem entsprechenden Klausurraum zu.
	 *
	 * @param conn       x
	 * @param idSchueler die IDs der zuzuweisenden Schülerklausuren
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das Gost-Halbjahr
	 *
	 * @return die Antwort
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static GostKlausurenDataCollection getGostKlausurenCollectionBySchuelerid(final DBEntityManager conn, final long idSchueler, final int abiturjahr,
			final int halbjahr) throws ApiOperationException {
		final GostKlausurenDataCollection result = new GostKlausurenDataCollection();

		result.schuelerklausuren = DTOMapper.mapList(
				conn.query(
						"SELECT sk FROM DTOGostKlausurenSchuelerklausuren sk JOIN DTOGostKlausurenKursklausuren kk ON (sk.Schueler_ID = :sId AND sk.Kursklausur_ID = kk.ID) JOIN DTOGostKlausurenVorgaben v ON (kk.Vorgabe_ID = v.ID AND v.Abi_Jahrgang = :abiturjahr AND v.Halbjahr = :halbjahr)",
						DTOGostKlausurenSchuelerklausuren.class)
						.setParameter("sId", idSchueler).setParameter("abiturjahr", abiturjahr)
						.setParameter("halbjahr", GostHalbjahr.fromIDorException(halbjahr))
						.getResultList(),
				DataGostKlausurenSchuelerklausur.dtoMapper);

		if (!result.schuelerklausuren.isEmpty()) {
			result.schuelerklausurtermine = DataGostKlausurenSchuelerklausurTermin.getSchuelerklausurtermineZuSchuelerklausuren(conn, result.schuelerklausuren);
			result.kursklausuren = DataGostKlausurenKursklausur.getKursklausurenZuSchuelerklausuren(conn, result.schuelerklausuren);
			result.vorgaben = DataGostKlausurenVorgabe.getKlausurvorgabenZuKursklausuren(conn, result.kursklausuren);
			final List<Long> terminIds = new ArrayList<>();
			terminIds.addAll(result.schuelerklausurtermine.stream().filter(skt -> skt.idTermin != null).map(skt -> skt.idTermin).toList());
			terminIds.addAll(result.kursklausuren.stream().filter(kk -> kk.idTermin != null).map(kk -> kk.idTermin).toList());
			result.termine = DataGostKlausurenTermin.getKlausurtermineZuIds(conn, terminIds);
		}

		return result;
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurterminen gehörigen
	 * GostSchuelerklausur-Objekte zurück.
	 *
	 * @param conn    x
	 * @param terminIds die Liste der GostSchuelerklausurterminen
	 * @param includeAbwesend inkludiert auch GostSchuelerklausurtermine, die als abwesend gemeldet sind
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausur-Objekte
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostSchuelerklausurTermin> getSchuelerKlausurenZuTerminIds(final DBEntityManager conn, final List<Long> terminIds, final boolean includeAbwesend)
			throws ApiOperationException {
		if (terminIds.isEmpty())
			return new ArrayList<>();
		final List<GostKursklausur> kursklausuren = DataGostKlausurenKursklausur.getKursklausurenZuTerminids(conn, terminIds);
		final List<GostSchuelerklausur> schuelerklausuren = DataGostKlausurenSchuelerklausur.getSchuelerKlausurenZuKursklausuren(conn, kursklausuren);
		final List<Long> kkSkIds = schuelerklausuren.stream().map(sk -> sk.id).toList();
		String skFilter = "";
		if (!kkSkIds.isEmpty()) {
		    skFilter += " OR (skt.Schuelerklausur_ID IN :skIds AND skt.Folge_Nr = 0";
		    if (!includeAbwesend)
			skFilter += "AND NOT EXISTS (SELECT sktInner FROM DTOGostKlausurenSchuelerklausurenTermine sktInner WHERE sktInner.Schuelerklausur_ID = skt.Schuelerklausur_ID AND sktInner.Folge_Nr > 0)";
		    skFilter += ")";
		}
		final TypedQuery<DTOGostKlausurenSchuelerklausurenTermine> query =
				conn.query("SELECT skt FROM DTOGostKlausurenSchuelerklausurenTermine skt WHERE skt.Termin_ID IN :tids" + skFilter,
						DTOGostKlausurenSchuelerklausurenTermine.class);
		if (!kkSkIds.isEmpty())
			query.setParameter("skIds", kkSkIds);
		final List<DTOGostKlausurenSchuelerklausurenTermine> skts = query.setParameter("tids", terminIds).getResultList();
		return DTOMapper.mapList(skts, DataGostKlausurenSchuelerklausurTermin.dtoMapper);
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurterminen gehörigen
	 * GostSchuelerklausur-Objekte zurück.
	 *
	 * @param conn    x
	 * @param terminIds die Liste der GostSchuelerklausurterminen
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausur-Objekte
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostSchuelerklausurTermin> getSchuelerKlausurenZuTerminIds(final DBEntityManager conn, final List<Long> terminIds)
			throws ApiOperationException {
		return getSchuelerKlausurenZuTerminIds(conn, terminIds, false);
	}

}
