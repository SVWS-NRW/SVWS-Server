package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausurenTermine;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostKlausurraumstunde}.
 */
public final class DataGostKlausurenSchuelerklausurTermin extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKlausurraumstunde}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenSchuelerklausurTermin(final DBEntityManager conn) {
		super(conn);
	}

	private static final Set<String> patchForbiddenAttributes = Set.of("idSchuelerklausur");

	private final Map<String, DataBasicMapper<DTOGostKlausurenSchuelerklausurenTermine>> patchMappings =
			Map.ofEntries(
				Map.entry("id", (conn, dto, value, map) -> {
					final Long patch_id = JSONMapper.convertToLong(value, false);
					if ((patch_id == null) || (patch_id.longValue() != dto.ID))
						throw OperationError.BAD_REQUEST.exception();
				}),
				Map.entry("idSchuelerklausur", (conn, dto, value, map) -> dto.Schuelerklausur_ID = JSONMapper.convertToLong(value, false)),
				Map.entry("idTermin", (conn, dto, value, map) -> dto.Termin_ID = JSONMapper.convertToLong(value, true)),
				Map.entry("startzeit", (conn, dto, value, map) -> dto.Startzeit = JSONMapper.convertToIntegerInRange(value, true, 0, 1440)),
				Map.entry("bemerkung", (conn, dto, value, map) -> dto.Bemerkungen = JSONMapper.convertToString(value, true, false, Schema.tab_Gost_Klausuren_Schuelerklausuren_Termine.col_Bemerkungen.datenlaenge()))
			);

	/**
		 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
		 * {@link DTOGostKlausurenSchuelerklausuren} in einen Core-DTO
		 * {@link GostSchuelerklausur}.
		 */
		public static final Function<DTOGostKlausurenSchuelerklausurenTermine, GostSchuelerklausurTermin> dtoMapper = (final DTOGostKlausurenSchuelerklausurenTermine skt) -> {
			final GostSchuelerklausurTermin daten = new GostSchuelerklausurTermin();
			daten.id = skt.ID;
			daten.idSchuelerklausur = skt.Schuelerklausur_ID;
			daten.folgeNr = skt.Folge_Nr;
			daten.idTermin = skt.Termin_ID;
			daten.startzeit = skt.Startzeit;
			daten.bemerkung = skt.Bemerkungen;
			return daten;
		};

	@Override
	public Response getAll() {
		return this.getList();
	}

	@Override
	public Response get(final Long idTermin) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		return super.patchBasicFiltered(id, is, DTOGostKlausurenSchuelerklausurenTermine.class, patchMappings, patchForbiddenAttributes);
	}

	/**
	 * Erstellt einen neuen Gost-Schuelerklausurtermin
	 *
	 * @param id   die ID der Schülerklausur
	 *
	 * @return Eine Response mit dem neuen Gost-Klausurtermin
	 */
	public Response create(final long id) {
		final DTOGostKlausurenSchuelerklausurenTermine lastTermin = conn.query("SELECT skt FROM DTOGostKlausurenSchuelerklausurenTermine skt WHERE skt.Schuelerklausur_ID = :skid ORDER BY skt.Folge_Nr DESC", DTOGostKlausurenSchuelerklausurenTermine.class)
				.setParameter("skid", id)
				.setMaxResults(1)
				.getSingleResult();
		final DTOGostKlausurenSchuelerklausurenTermine newTermin = new DTOGostKlausurenSchuelerklausurenTermine(conn.transactionGetNextID(DTOGostKlausurenSchuelerklausurenTermine.class), id, lastTermin.Folge_Nr + 1);
		conn.transactionPersist(newTermin);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(dtoMapper.apply(newTermin)).build();
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausuren gehörigen
	 * GostSchuelerklausurtermin-Objekte zurück.
	 *
	 * @param conn    x
	 * @param klausuren die Liste der GostSchuelerklausuren
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurtermin-Objekte
	 */
	public static List<GostSchuelerklausurTermin> getSchuelerklausurtermineZuSchuelerklausuren(final DBEntityManager conn, final List<GostSchuelerklausur> klausuren) {
		if (klausuren.isEmpty())
			return new ArrayList<>();
		return conn.queryNamed("DTOGostKlausurenSchuelerklausurenTermine.schuelerklausur_id.multiple", klausuren.stream().map(sk -> sk.id).toList(), DTOGostKlausurenSchuelerklausurenTermine.class).stream()
				.map(dtoMapper::apply).toList();
	}

	/**
	 * Löscht den angegebenen Gost-Schuelerklausurtermin
	 *
	 * @param idSkt   die ID des zu löschenden Schülerklausurtermins
	 *
	 * @return die Response
	 */
	public Response delete(final long idSkt) {
		return super.deleteBasic(idSkt, DTOGostKlausurenSchuelerklausurenTermine.class, dtoMapper);
	}


}
