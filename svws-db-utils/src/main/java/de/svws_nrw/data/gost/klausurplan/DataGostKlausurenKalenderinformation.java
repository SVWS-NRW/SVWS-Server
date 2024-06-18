package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ObjLongConsumer;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenKalenderinformation;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenKalenderinformationen;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostKlausurenKalenderinformation}.
 */
public final class DataGostKlausurenKalenderinformation extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKlausurenKalenderinformation}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenKalenderinformation(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() throws ApiOperationException {
		return this.getList();
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenKalenderinformationen} in einen Core-DTO
	 * {@link GostKlausurenKalenderinformation}.
	 */
	private final DTOMapper<DTOGostKlausurenKalenderinformationen, GostKlausurenKalenderinformation> dtoMapper =
			(final DTOGostKlausurenKalenderinformationen z) -> {
				final GostKlausurenKalenderinformation daten = new GostKlausurenKalenderinformation();
				daten.id = z.ID;
				daten.bemerkung = z.Bemerkungen;
				daten.bezeichnung = z.Bezeichnung;
				daten.startdatum = z.Startdatum;
				daten.startzeit = z.Startzeit;
				daten.enddatum = z.Enddatum;
				daten.endzeit = z.Endzeit;
				daten.istSperrtermin = z.IstSperrtermin;
				return daten;
			};

	/**
	 * Gibt die Liste der Kursklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private List<GostKlausurenKalenderinformation> getKalenderinformationen() throws ApiOperationException {
		final List<DTOGostKlausurenKalenderinformationen> kalInfos = conn.queryAll(DTOGostKlausurenKalenderinformationen.class);
		final List<GostKlausurenKalenderinformation> daten = new ArrayList<>();
		for (final DTOGostKlausurenKalenderinformationen ki : kalInfos)
			daten.add(dtoMapper.apply(ki));
		return daten;
	}

	@Override
	public Response get(final Long id) {
		throw new UnsupportedOperationException();
	}

	private static final Set<String> requiredCreateAttributes = Set.of("startdatum");

	private final Map<String, DataBasicMapper<DTOGostKlausurenKalenderinformationen>> patchMappings =
			Map.ofEntries(
					Map.entry("bemerkung", (conn, dto, value, map) -> dto.Bemerkungen = JSONMapper.convertToString(value, true, false,
							Schema.tab_Gost_Klausuren_Kalenderinformationen.col_Bemerkungen.datenlaenge())),
					Map.entry("bezeichnung", (conn, dto, value, map) -> dto.Bezeichnung = JSONMapper.convertToString(value, true, false,
							Schema.tab_Gost_Klausuren_Kalenderinformationen.col_Bezeichnung.datenlaenge())),
					Map.entry("startdatum", (conn, dto, value, map) -> dto.Startdatum = JSONMapper.convertToString(value, false, false, null)),
					Map.entry("startzeit", (conn, dto, value, map) -> dto.Startzeit = JSONMapper.convertToIntegerInRange(value, true, 0, 1440)),
					Map.entry("enddatum", (conn, dto, value, map) -> dto.Enddatum = JSONMapper.convertToString(value, true, false, null)),
					Map.entry("endzeit", (conn, dto, value, map) -> dto.Endzeit = JSONMapper.convertToIntegerInRange(value, true, 0, 1440)),
					Map.entry("istSperrtermin", (conn, dto, value, map) -> dto.IstSperrtermin = JSONMapper.convertToBoolean(value, false)));

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOGostKlausurenKalenderinformationen.class, patchMappings);
	}

	@Override
	public Response getList() throws ApiOperationException {
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getKalenderinformationen()).build();
	}

	/**
	 * Erstellt eine neue Gost-KlausurenKalenderinformation
	 *
	 * @param is das Objekt
	 *
	 * @return Eine Response mit der neuen Gost-KlausurenKalenderinformation
	 * @throws ApiOperationException
	 */
	public Response create(final InputStream is) throws ApiOperationException {
		final ObjLongConsumer<DTOGostKlausurenKalenderinformationen> initDTO = (dto, id) -> dto.ID = id;
		return super.addBasic(is, DTOGostKlausurenKalenderinformationen.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}

	/**
	 * Löscht eine Gost-KlausurenKalenderinformation *
	 *
	 * @param id die ID der zu löschenden KlausurenKalenderinformation
	 *
	 * @return die Response
	 * @throws ApiOperationException
	 */
	public Response delete(final Long id) throws ApiOperationException {
		return super.deleteBasic(id, DTOGostKlausurenKalenderinformationen.class, dtoMapper);
	}

}
