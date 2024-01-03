package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkKkKv;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenKursklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausurenTermine;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenTermine;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenVorgaben;
import de.svws_nrw.db.utils.OperationError;
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
	 * Bereitet eine Liste von Schülerklausur-DTOs zu Core-DTOs auf
	 *
	 * @param conn       x
	 * @param schuelerklausuren die Liste der Schülerklausur-DTOs
	 *
	 * @return die Liste der GostSchuelerklausur-Objekte
	 */
	public static List<GostSchuelerklausur> preprocessSchuelerklausuren(final DBEntityManager conn, final List<DTOGostKlausurenSchuelerklausuren> schuelerklausuren) {
		if (schuelerklausuren.isEmpty()) {
			return new ArrayList<>();
		}
		final Map<Long, List<DTOGostKlausurenSchuelerklausurenTermine>> mapSchuelerklausuren = conn
				.queryNamed("DTOGostKlausurenSchuelerklausurenTermine.schuelerklausur_id.multiple", schuelerklausuren.stream().map(sk -> sk.ID).toList(), DTOGostKlausurenSchuelerklausurenTermine.class).stream()
				.collect(Collectors.groupingBy(skt -> skt.Schuelerklausur_ID));
		if (mapSchuelerklausuren.isEmpty()) {
			return new ArrayList<>();
		}

		final List<GostSchuelerklausur> daten = new ArrayList<>();
		schuelerklausuren.stream().forEach(sk -> {
			final List<DTOGostKlausurenSchuelerklausurenTermine> skts = mapSchuelerklausuren.get(sk.ID);
			if (skts != null && !skts.isEmpty()) {
				final GostSchuelerklausur gsk = dtoMapperToSchuelerklausur.apply(sk, skts);
				daten.add(gsk);
			}
		});
		return daten;
	}

	/**
	 * Bereitet eine Liste von Schülerklausurtermin-DTOs zu Core-DTOs auf
	 *
	 * @param conn       x
	 * @param schuelerklausurenTermine die Liste der Schülerklausur-DTOs
	 *
	 * @return die Liste der GostSchuelerklausurTermin-Objekte
	 */
	public static List<GostSchuelerklausurTermin> preprocessSchuelerklausurenTermine(final DBEntityManager conn, final List<DTOGostKlausurenSchuelerklausurenTermine> schuelerklausurenTermine) {
		if (schuelerklausurenTermine.isEmpty()) {
			return new ArrayList<>();
		}
		final Map<Long, DTOGostKlausurenSchuelerklausuren> mapSchuelerklausuren = conn
				.queryNamed("DTOGostKlausurenSchuelerklausuren.id.multiple", schuelerklausurenTermine.stream().map(skt -> skt.Schuelerklausur_ID).toList(), DTOGostKlausurenSchuelerklausuren.class).stream()
				.collect(Collectors.toMap(sk -> sk.ID, sk -> sk));
		if (mapSchuelerklausuren.isEmpty()) {
			return new ArrayList<>();
		}

		final List<GostSchuelerklausurTermin> daten = new ArrayList<>();
		for (DTOGostKlausurenSchuelerklausurenTermine skt : schuelerklausurenTermine) {
			daten.add(dtoMapperToSchuelerklausurTermin.apply(mapSchuelerklausuren.get(skt.Schuelerklausur_ID), skt));
		}

		return daten;
	}

	private List<GostSchuelerklausurTermin> getSchuelerKlausurenZuTermin(final long terminId) {
		if (conn.queryByKey(DTOGostKlausurenTermine.class, terminId) == null)
			throw OperationError.BAD_REQUEST.exception("Klausurtermin nicht gefunden, ID: " + terminId);
		final List<Long> kursKlausurIds = conn.queryNamed("DTOGostKlausurenKursklausuren.termin_id", terminId, DTOGostKlausurenKursklausuren.class).stream().map(k -> k.ID).distinct().toList();

		final Map<Long, DTOGostKlausurenSchuelerklausuren> mapSchuelerklausuren = conn.queryNamed("DTOGostKlausurenSchuelerklausuren.kursklausur_id.multiple", kursKlausurIds,
				DTOGostKlausurenSchuelerklausuren.class).stream().collect(Collectors.toMap(sk -> sk.ID, sk -> sk));

		final List<DTOGostKlausurenSchuelerklausurenTermine> skts = conn.query("SELECT skt FROM DTOGostKlausurenSchuelerklausurenTermine skt WHERE (skt.Schuelerklausur_ID IN :skIds AND skt.Termin_ID IS NULL) OR skt.Termin_ID = :tid",
				DTOGostKlausurenSchuelerklausurenTermine.class)
				.setParameter("skIds", mapSchuelerklausuren.keySet())
				.setParameter("tid", terminId)
				.getResultList();

		final List<GostSchuelerklausurTermin> daten = new ArrayList<>();
		for (DTOGostKlausurenSchuelerklausurenTermine skt : skts) {
			daten.add(dtoMapperToSchuelerklausurTermin.apply(mapSchuelerklausuren.get(skt.Schuelerklausur_ID), skt));
		}

		return daten;
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenSchuelerklausuren} in einen Core-DTO
	 * {@link GostSchuelerklausur}.
	 */
	public static final Function3<DTOGostKlausurenSchuelerklausuren, List<DTOGostKlausurenSchuelerklausurenTermine>, GostSchuelerklausur> dtoMapperToSchuelerklausur = (final DTOGostKlausurenSchuelerklausuren sk, final List<DTOGostKlausurenSchuelerklausurenTermine> skts) -> {
		final GostSchuelerklausur daten = new GostSchuelerklausur();
		daten.idKursklausur = sk.Kursklausur_ID;
		daten.idSchueler = sk.Schueler_ID;
		daten.id = sk.ID;
		daten.schuelerklausurTermine = skts.stream().map(DataGostKlausurenSchuelerklausurTermin.dtoMapper::apply).toList();
		return daten;
	};

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenSchuelerklausuren} in einen Core-DTO
	 * {@link GostSchuelerklausur}.
	 */
	public static final Function3<DTOGostKlausurenSchuelerklausuren, DTOGostKlausurenSchuelerklausurenTermine, GostSchuelerklausurTermin> dtoMapperToSchuelerklausurTermin = (final DTOGostKlausurenSchuelerklausuren sk, final DTOGostKlausurenSchuelerklausurenTermine skt) -> {
		final GostSchuelerklausurTermin daten = new GostSchuelerklausurTermin();
		daten.idKursklausur = sk.Kursklausur_ID;
		daten.idSchueler = sk.Schueler_ID;
		daten.idSchuelerklausur = sk.ID;
		daten.idTermin = skt.Termin_ID;
		daten.startzeit = skt.Startzeit;
		return daten;
	};

	private static final Set<String> forbiddenPatchAttributes = Set.of("idSchuelerklausur", "idKursklausur", "idSchueler");

	private final Map<String, DataBasicMapper<DTOGostKlausurenSchuelerklausuren>> patchMappings = Map.ofEntries(
			Map.entry("idSchuelerklausur", (conn, dto, value, map) -> dto.Schueler_ID = JSONMapper.convertToLong(value, false)),
			Map.entry("idKursklausur", (conn, dto, value, map) -> dto.Kursklausur_ID = JSONMapper.convertToLong(value, false)),
			Map.entry("idSchueler", (conn, dto, value, map) -> dto.Schueler_ID = JSONMapper.convertToLong(value, false)));

	@Override
	public Response patch(final Long id, final InputStream is) {
		return super.patchBasicFiltered(id, is, DTOGostKlausurenSchuelerklausuren.class, patchMappings, forbiddenPatchAttributes);
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response get(final Long id) {
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getSchuelerKlausurenZuTermin(id)).build();
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
	 */
	public static GostKlausurenCollectionSkKkKv getGostKlausurenCollectionBySchuelerid(final DBEntityManager conn, final long idSchueler, final int abiturjahr, final int halbjahr) {
		final GostKlausurenCollectionSkKkKv result = new GostKlausurenCollectionSkKkKv();

		result.schuelerklausuren = preprocessSchuelerklausuren(conn, conn.query(
				"SELECT sk FROM DTOGostKlausurenSchuelerklausuren sk JOIN DTOGostKlausurenKursklausuren kk ON (sk.Schueler_ID = :sId AND sk.Kursklausur_ID = kk.ID) JOIN DTOGostKlausurenVorgaben v ON (kk.Vorgabe_ID = v.ID AND v.Abi_Jahrgang = :abiturjahr AND v.Halbjahr = :halbjahr)",
				DTOGostKlausurenSchuelerklausuren.class).setParameter("sId", idSchueler).setParameter("abiturjahr", abiturjahr).setParameter("halbjahr", GostHalbjahr.fromIDorException(halbjahr))
				.getResultList());

		List<Long> terminIds = new ArrayList<>();
		for (GostSchuelerklausur sk : result.schuelerklausuren) {
			terminIds.addAll(sk.schuelerklausurTermine.stream().filter(skt -> skt.idTermin != -1).map(skt -> skt.idTermin).toList());
		}

		if (!result.schuelerklausuren.isEmpty()) {
			result.kursklausuren = conn
					.queryNamed("DTOGostKlausurenKursklausuren.id.multiple", result.schuelerklausuren.stream().map(sk -> sk.idKursklausur).toList(), DTOGostKlausurenKursklausuren.class).stream()
					.map(DataGostKlausurenKursklausur.dtoMapper2::apply).toList();
			terminIds.addAll(result.kursklausuren.stream().filter(kk -> kk.idTermin != null).map(kk -> kk.idTermin).toList());
			result.vorgaben = conn.queryNamed("DTOGostKlausurenVorgaben.id.multiple", result.kursklausuren.stream().map(kk -> kk.idVorgabe).toList(), DTOGostKlausurenVorgaben.class).stream()
					.map(DataGostKlausurenVorgabe.dtoMapper::apply).toList();
			result.termine = conn.queryNamed("DTOGostKlausurenTermine.id.multiple", terminIds, DTOGostKlausurenTermine.class).stream()
					.map(DataGostKlausurenTermin.dtoMapper::apply).toList();
		}

		return result;
	}

	/**
	 * Functional Interface
	 *
	 * @param <One>   One
	 * @param <Two>   Two
	 * @param <Three> Three
	 */
	@FunctionalInterface
	interface Function3<One, Two, Three> {
		/**
		 * Apply Method
		 *
		 * @param one   one
		 * @param two   two
		 *
		 * @return ret
		 */
		Three apply(One one, Two two);
	}

}
