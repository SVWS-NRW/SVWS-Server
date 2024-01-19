package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenDataCollection;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkSkt;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
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

	private List<GostSchuelerklausurTermin> getSchuelerKlausurenZuTermin(final long terminId) {
		if (conn.queryByKey(DTOGostKlausurenTermine.class, terminId) == null)
			throw OperationError.BAD_REQUEST.exception("Klausurtermin nicht gefunden, ID: " + terminId);
		final List<Long> kursKlausurIds = conn.queryNamed("DTOGostKlausurenKursklausuren.termin_id", terminId, DTOGostKlausurenKursklausuren.class).stream().map(k -> k.ID).distinct().toList();

		final List<Long> kkSkIds = new ArrayList<>();
		if (!kursKlausurIds.isEmpty()) {
			final Map<Long, DTOGostKlausurenSchuelerklausuren> mapSchuelerklausuren = conn
				.queryNamed("DTOGostKlausurenSchuelerklausuren.kursklausur_id.multiple", kursKlausurIds, DTOGostKlausurenSchuelerklausuren.class).stream()
				.collect(Collectors.toMap(sk -> sk.ID, sk -> sk));
			kkSkIds.addAll(mapSchuelerklausuren.keySet());
		}

		final String skFilter = kkSkIds.isEmpty() ? "" : " OR (skt.Schuelerklausur_ID IN :skIds AND skt.Folge_Nr = 0)";

		final TypedQuery<DTOGostKlausurenSchuelerklausurenTermine> query = conn.query("SELECT skt FROM DTOGostKlausurenSchuelerklausurenTermine skt WHERE skt.Termin_ID = :tid" + skFilter,
						DTOGostKlausurenSchuelerklausurenTermine.class);

		if (!kkSkIds.isEmpty())
			query.setParameter("skIds", kkSkIds);


		final List<DTOGostKlausurenSchuelerklausurenTermine> skts = query.setParameter("tid", terminId).getResultList();

		return skts.stream().map(DataGostKlausurenSchuelerklausurTermin.dtoMapper::apply).toList();

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
	 */
	public static GostKlausurenCollectionSkSkt getCollectionSkSktNachschreiber(final DBEntityManager conn, final int abiturjahr, final GostHalbjahr halbjahr) {
		final GostKlausurenCollectionSkSkt ergebnis = new GostKlausurenCollectionSkSkt();
		final List<GostKursklausur> kursKlausuren = DataGostKlausurenKursklausur.getKursKlausuren(conn, abiturjahr, halbjahr.id, false);
		if (!kursKlausuren.isEmpty()) {
			final List<DTOGostKlausurenSchuelerklausuren> schuelerKlausurDTOs = conn.query(
					"SELECT DISTINCT sk FROM DTOGostKlausurenSchuelerklausuren sk JOIN DTOGostKlausurenSchuelerklausurenTermine skt ON sk.ID = skt.Schuelerklausur_ID AND sk.Kursklausur_ID IN :kkids WHERE skt.Folge_Nr > 0",
					DTOGostKlausurenSchuelerklausuren.class).setParameter("kkids", kursKlausuren.stream().map(kk -> kk.id).toList()).getResultList();
			ergebnis.schuelerklausuren = schuelerKlausurDTOs.stream().map(DataGostKlausurenSchuelerklausur.dtoMapper::apply).toList();
			ergebnis.schuelerklausurtermine = DataGostKlausurenSchuelerklausurTermin.getSchuelerklausurtermineZuSchuelerklausuren(conn, ergebnis.schuelerklausuren);
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
	 */
	public static GostKlausurenCollectionSkSkt getCollectionSkSkt(final DBEntityManager conn, final int abiturjahr, final GostHalbjahr halbjahr, final boolean ganzesSchuljahr) {
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
	 */
	public static List<GostSchuelerklausur> getSchuelerKlausurenZuKursklausuren(final DBEntityManager conn, final List<GostKursklausur> kursKlausuren) {
		if (kursKlausuren.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenSchuelerklausuren> schuelerKlausurDTOs = conn.queryNamed("DTOGostKlausurenSchuelerklausuren.kursklausur_id.multiple", kursKlausuren.stream().map(kk -> kk.id).toList(), DTOGostKlausurenSchuelerklausuren.class);
		return schuelerKlausurDTOs.stream().map(DataGostKlausurenSchuelerklausur.dtoMapper::apply).toList();
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenSchuelerklausuren} in einen Core-DTO
	 * {@link GostSchuelerklausur}.
	 */
	public static final Function<DTOGostKlausurenSchuelerklausuren, GostSchuelerklausur> dtoMapper = (final DTOGostKlausurenSchuelerklausuren sk) -> {
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
	 * Liefert die zu einer Liste von GostSchuelerklausurterminen gehörigen
	 * GostSchuelerklausur-Objekte zurück.
	 *
	 * @param conn    x
	 * @param termine die Liste der GostSchuelerklausurterminen
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausur-Objekte
	 */
	public static List<GostSchuelerklausur> getSchuelerklausurenZuSchuelerklausurterminen(final DBEntityManager conn, final List<GostSchuelerklausurTermin> termine) {
		if (termine.isEmpty())
			return new ArrayList<>();
		return conn.queryNamed("DTOGostKlausurenSchuelerklausuren.id.multiple", termine.stream().map(sk -> sk.idSchuelerklausur).toList(), DTOGostKlausurenSchuelerklausuren.class).stream()
				.map(DataGostKlausurenSchuelerklausur.dtoMapper::apply).toList();
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
	public static GostKlausurenDataCollection getGostKlausurenCollectionBySchuelerid(final DBEntityManager conn, final long idSchueler, final int abiturjahr, final int halbjahr) {
		final GostKlausurenDataCollection result = new GostKlausurenDataCollection();

		result.schuelerklausuren = conn.query(
				"SELECT sk FROM DTOGostKlausurenSchuelerklausuren sk JOIN DTOGostKlausurenKursklausuren kk ON (sk.Schueler_ID = :sId AND sk.Kursklausur_ID = kk.ID) JOIN DTOGostKlausurenVorgaben v ON (kk.Vorgabe_ID = v.ID AND v.Abi_Jahrgang = :abiturjahr AND v.Halbjahr = :halbjahr)",
				DTOGostKlausurenSchuelerklausuren.class).setParameter("sId", idSchueler).setParameter("abiturjahr", abiturjahr).setParameter("halbjahr", GostHalbjahr.fromIDorException(halbjahr))
				.getResultList().stream().map(DataGostKlausurenSchuelerklausur.dtoMapper::apply).toList();

		if (!result.schuelerklausuren.isEmpty()) {

			result.schuelerklausurtermine = conn.queryNamed("DTOGostKlausurenSchuelerklausurenTermine.schuelerklausur_id.multiple", result.schuelerklausuren.stream().map(sk -> sk.id).toList(),
					DTOGostKlausurenSchuelerklausurenTermine.class).stream().map(DataGostKlausurenSchuelerklausurTermin.dtoMapper::apply).toList();
			result.kursklausuren = conn
					.queryNamed("DTOGostKlausurenKursklausuren.id.multiple", result.schuelerklausuren.stream().map(sk -> sk.idKursklausur).toList(), DTOGostKlausurenKursklausuren.class).stream()
					.map(DataGostKlausurenKursklausur.dtoMapper::apply).toList();
			result.vorgaben = conn.queryNamed("DTOGostKlausurenVorgaben.id.multiple", result.kursklausuren.stream().map(kk -> kk.idVorgabe).toList(), DTOGostKlausurenVorgaben.class).stream()
					.map(DataGostKlausurenVorgabe.dtoMapper::apply).toList();

			final List<Long> terminIds = new ArrayList<>();
			terminIds.addAll(result.schuelerklausurtermine.stream().filter(skt -> skt.idTermin != null).map(skt -> skt.idTermin).toList());
			terminIds.addAll(result.kursklausuren.stream().filter(kk -> kk.idTermin != null).map(kk -> kk.idTermin).toList());
			result.termine = terminIds.isEmpty()
					? new ArrayList<>()
					: conn.queryNamed("DTOGostKlausurenTermine.id.multiple", terminIds, DTOGostKlausurenTermine.class).stream().map(DataGostKlausurenTermin.dtoMapper::apply).toList();
		}

		return result;
	}

}
