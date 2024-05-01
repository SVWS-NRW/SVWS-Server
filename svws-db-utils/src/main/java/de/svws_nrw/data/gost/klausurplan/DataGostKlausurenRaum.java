package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ObjLongConsumer;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenRaeume;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenTermine;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanRaum;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostKlausurraum}.
 */
public final class DataGostKlausurenRaum extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKlausurraum}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenRaum(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenRaeume} in einen Core-DTO
	 * {@link GostKlausurraum}.
	 */
	public static final DTOMapper<DTOGostKlausurenRaeume, GostKlausurraum> dtoMapper = (final DTOGostKlausurenRaeume z) -> {
		final GostKlausurraum daten = new GostKlausurraum();
		daten.id = z.ID;
		daten.idTermin = z.Termin_ID;
		daten.idStundenplanRaum = z.Stundenplan_Raum_ID;
		daten.bemerkung = z.Bemerkungen;
		return daten;
	};

	private static final Set<String> requiredCreateAttributes = Set.of("idTermin");

	private final Map<String, DataBasicMapper<DTOGostKlausurenRaeume>> patchMappings =
		Map.ofEntries(
			Map.entry("idTermin", (conn, dto, value, map) -> {
				dto.Termin_ID = JSONMapper.convertToLong(value, false);
				if (conn.queryByKey(DTOGostKlausurenTermine.class, dto.Termin_ID) == null)
					throw new ApiOperationException(Status.NOT_FOUND, "Klausurtermin mit ID %d existiert nicht.".formatted(dto.Termin_ID));
			}),
			Map.entry("idStundenplanRaum", (conn, dto, value, map) -> {
				dto.Stundenplan_Raum_ID = JSONMapper.convertToLong(value, true);
				if (conn.queryByKey(DTOStundenplanRaum.class, dto.Stundenplan_Raum_ID) == null)
					throw new ApiOperationException(Status.BAD_REQUEST, "Stundenplanraum nicht gefunden, ID: " + dto.Stundenplan_Raum_ID);
			}),
			Map.entry("bemerkung", (conn, dto, value, map) -> dto.Bemerkungen = JSONMapper.convertToString(value, true, true, Schema.tab_Gost_Klausuren_Raeume.col_Bemerkungen.datenlaenge()))
		);

	/**
	 * Gibt die Liste der Klausurvorgaben einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param terminIds die IDs dee Klausurtermine
	 *
	 * @return die Liste der Klausurräume
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostKlausurraum> getKlausurraeumeZuTerminen(final DBEntityManager conn, final List<Long> terminIds) throws ApiOperationException {
		final List<DTOGostKlausurenRaeume> raeume = conn.queryNamed("DTOGostKlausurenRaeume.termin_id.multiple", terminIds, DTOGostKlausurenRaeume.class);
		final List<GostKlausurraum> daten = new ArrayList<>();
		for (final DTOGostKlausurenRaeume r : raeume)
			daten.add(dtoMapper.apply(r));
		return daten;
	}

	@Override
	public Response get(final Long idTermin) throws ApiOperationException {
		// Klausurräume zu einem Klausurtermin
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(getKlausurraeumeZuTerminen(conn, ListUtils.create1(idTermin))).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasicFiltered(id, is, DTOGostKlausurenRaeume.class, patchMappings, requiredCreateAttributes);
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Erstellt einen neue Gost-Klausurraum
	 *
	 * @param is Das JSON-Objekt mit den Daten
	 *
	 * @return Eine Response mit dem neuen Gost-Klausurraum
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response create(final InputStream is) throws ApiOperationException {
		final ObjLongConsumer<DTOGostKlausurenRaeume> initDTO = (dto, id) -> dto.ID = id;
		return super.addBasic(is, DTOGostKlausurenRaeume.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}

	/**
	 * Löscht einen Gost-Klausurraum *
	 *
	 * @param id die ID des zu löschenden Klausurraums
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response delete(final Long id) throws ApiOperationException {
		return super.deleteBasic(id, DTOGostKlausurenRaeume.class, dtoMapper);
	}


}
