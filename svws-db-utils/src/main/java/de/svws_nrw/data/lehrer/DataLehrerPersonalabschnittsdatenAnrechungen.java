package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import de.svws_nrw.core.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.core.types.lehrer.LehrerAnrechnungsgrund;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAnrechnungsstunde;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}.
 */
public final class DataLehrerPersonalabschnittsdatenAnrechungen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLehrerPersonalabschnittsdatenAnrechungen(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOLehrerAnrechnungsstunde} in einen Core-DTO {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}.
	 */
	private static final Function<DTOLehrerAnrechnungsstunde, LehrerPersonalabschnittsdatenAnrechnungsstunden> dtoMapper =
			(final DTOLehrerAnrechnungsstunde dto) -> {
				final LehrerPersonalabschnittsdatenAnrechnungsstunden daten = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
				daten.id = dto.ID;
				daten.idAbschnittsdaten = dto.Abschnitt_ID;
				daten.idGrund = LehrerAnrechnungsgrund.getByKuerzel(dto.AnrechnungsgrundKrz).daten.id;
				daten.anzahl = (dto.AnrechnungStd == null) ? 0.0 : dto.AnrechnungStd;
				return daten;
			};

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Ermittelt die Anrechungen für die Lehrerabschnittsdaten mit der angegebenen ID und und gibt diese zurück.
	 *
	 * @param conn                      die Datenbankverbindung
	 * @param idLehrerabschnittsdaten   die ID der Lehrerabschnittsdaten
	 *
	 * @return die Liste mit den Anrechnungen
	 */
	public static List<LehrerPersonalabschnittsdatenAnrechnungsstunden> getByLehrerabschnittsdatenId(final DBEntityManager conn,
			final Long idLehrerabschnittsdaten) {
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> result = new ArrayList<>();
		// Bestimme die Anrechungen für die Lehrerabschnittsdaten
		final List<DTOLehrerAnrechnungsstunde> dtos = conn.queryList(DTOLehrerAnrechnungsstunde.QUERY_BY_ABSCHNITT_ID,
				DTOLehrerAnrechnungsstunde.class, idLehrerabschnittsdaten);
		if (dtos == null)
			return result;
		// Konvertiere sie und füge sie zur Liste hinzu
		for (final DTOLehrerAnrechnungsstunde dto : dtos)
			result.add(dtoMapper.apply(dto));
		return result;
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final DTOLehrerAnrechnungsstunde dto = conn.queryByKey(DTOLehrerAnrechnungsstunde.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final LehrerPersonalabschnittsdatenAnrechnungsstunden daten = dtoMapper.apply(dto);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	private final Map<String, DataBasicMapper<DTOLehrerAnrechnungsstunde>> patchMappings = Map.ofEntries(
			Map.entry("id", (conn, anrechnung, value, map) -> {
				final long patch_id = JSONMapper.convertToLong(value, false);
				if ((patch_id != anrechnung.ID))
					throw new ApiOperationException(Status.BAD_REQUEST);
			}),
			Map.entry("idAbschnittsdaten", (conn, anrechnung, value, map) -> {
				final long patch_id = JSONMapper.convertToLong(value, false);
				if (patch_id != anrechnung.Abschnitt_ID)
					throw new ApiOperationException(Status.BAD_REQUEST);
			}),
			Map.entry("idGrund", (conn, anrechnung, value, map) -> {
				final long idGrund = JSONMapper.convertToLong(value, false);
				final LehrerAnrechnungsgrund grund = LehrerAnrechnungsgrund.getByID(idGrund);
				if (grund == null)
					throw new ApiOperationException(Status.NOT_FOUND);
				anrechnung.AnrechnungsgrundKrz = grund.daten.kuerzel;
			}),
			Map.entry("anzahl", (conn, anrechnung, value, map) -> {
				anrechnung.AnrechnungStd = JSONMapper.convertToDouble(value, false);
			}));

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOLehrerAnrechnungsstunde.class, patchMappings);
	}

}
