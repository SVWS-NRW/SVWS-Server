package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkKkKv;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenKursklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausuren;
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

	private List<GostSchuelerklausur> getSchuelerKlausuren(final long terminId) {
		if (conn.queryByKey(DTOGostKlausurenTermine.class, terminId) == null)
			throw OperationError.BAD_REQUEST.exception("Klausurtermin nicht gefunden, ID: " + terminId);
		final List<Long> kursKlausurIds = conn.queryNamed("DTOGostKlausurenKursklausuren.termin_id", terminId, DTOGostKlausurenKursklausuren.class).stream().map(k -> k.ID).distinct().toList();
		final List<DTOGostKlausurenSchuelerklausuren> listSchuelerklausuren = conn.queryNamed("DTOGostKlausurenSchuelerklausuren.kursklausur_id.multiple", kursKlausurIds,
				DTOGostKlausurenSchuelerklausuren.class);
		// Schuelerklausuren entfernen, die an anderem Termin stattfinden sollen.
		listSchuelerklausuren.removeAll(listSchuelerklausuren.stream().filter(sk -> sk.Termin_ID != null && sk.Termin_ID != terminId).toList());
		// Schuelerklausuren ohne zugehörige Kursklausur hinzufügen (z.B. Nachschreiber)
		listSchuelerklausuren.addAll(conn.queryNamed("DTOGostKlausurenSchuelerklausuren.termin_id", terminId, DTOGostKlausurenSchuelerklausuren.class));
		return listSchuelerklausuren.stream().map(dtoMapper::apply).toList();
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenSchuelerklausuren} in einen Core-DTO
	 * {@link GostSchuelerklausur}.
	 */
	public static final Function<DTOGostKlausurenSchuelerklausuren, GostSchuelerklausur> dtoMapper = (final DTOGostKlausurenSchuelerklausuren z) -> {
		final GostSchuelerklausur daten = new GostSchuelerklausur();
		daten.idKursklausur = z.Kursklausur_ID;
		daten.idSchueler = z.Schueler_ID;
		daten.idSchuelerklausur = z.ID;
		daten.idTermin = z.Termin_ID;
		daten.startzeit = z.Startzeit;
		return daten;
	};

	private static final Set<String> forbiddenPatchAttributes = Set.of("idSchuelerklausur", "idKursklausur", "idSchueler");

	private final Map<String, DataBasicMapper<DTOGostKlausurenSchuelerklausuren>> patchMappings = Map.ofEntries(
			Map.entry("idSchuelerklausur", (conn, dto, value, map) -> dto.Schueler_ID = JSONMapper.convertToLong(value, false)),
			Map.entry("idKursklausur", (conn, dto, value, map) -> dto.Kursklausur_ID = JSONMapper.convertToLong(value, false)),
			Map.entry("idSchueler", (conn, dto, value, map) -> dto.Schueler_ID = JSONMapper.convertToLong(value, false)), Map.entry("idTermin", (conn, dto, value, map) -> {
				dto.Termin_ID = JSONMapper.convertToLong(value, true);
				if (conn.queryByKey(DTOGostKlausurenTermine.class, dto.Termin_ID) == null)
					throw OperationError.NOT_FOUND.exception("Klausurtermin nicht gefunden, ID: " + dto.Termin_ID);
			}), Map.entry("startzeit", (conn, dto, value, map) -> dto.Startzeit = JSONMapper.convertToIntegerInRange(value, true, 0, 1440)));

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
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getSchuelerKlausuren(id)).build();
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

		result.schuelerklausuren = conn.query(
				"SELECT sk FROM DTOGostKlausurenSchuelerklausuren sk JOIN DTOGostKlausurenKursklausuren kk ON (sk.Schueler_ID = :sId AND sk.Kursklausur_ID = kk.ID) JOIN DTOGostKlausurenVorgaben v ON (kk.Vorgabe_ID = v.ID AND v.Abi_Jahrgang = :abiturjahr AND v.Halbjahr = :halbjahr)",
				DTOGostKlausurenSchuelerklausuren.class).setParameter("sId", idSchueler).setParameter("abiturjahr", abiturjahr).setParameter("halbjahr", GostHalbjahr.fromIDorException(halbjahr))
				.getResultList().stream().map(dtoMapper::apply).toList();

		if (!result.schuelerklausuren.isEmpty()) {
			result.kursklausuren = conn
					.queryNamed("DTOGostKlausurenKursklausuren.id.multiple", result.schuelerklausuren.stream().map(sk -> sk.idKursklausur).toList(), DTOGostKlausurenKursklausuren.class).stream()
					.map(DataGostKlausurenKursklausur.dtoMapper2::apply).toList();
			result.vorgaben = conn.queryNamed("DTOGostKlausurenVorgaben.id.multiple", result.kursklausuren.stream().map(kk -> kk.idVorgabe).toList(), DTOGostKlausurenVorgaben.class).stream()
					.map(DataGostKlausurenVorgabe.dtoMapper::apply).toList();
		}

		return result;
	}

}
