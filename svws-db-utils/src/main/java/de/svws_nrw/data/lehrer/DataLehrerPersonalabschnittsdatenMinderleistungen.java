package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import de.svws_nrw.core.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.core.types.lehrer.LehrerMinderleistungArt;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerEntlastungsstunde;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}.
 */
public final class DataLehrerPersonalabschnittsdatenMinderleistungen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLehrerPersonalabschnittsdatenMinderleistungen(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOLehrerEntlastungsstunde} in einen Core-DTO {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}.
	 */
	private static final Function<DTOLehrerEntlastungsstunde, LehrerPersonalabschnittsdatenAnrechnungsstunden> dtoMapper = (final DTOLehrerEntlastungsstunde dto) -> {
		final LehrerPersonalabschnittsdatenAnrechnungsstunden daten = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
		daten.id = dto.ID;
		daten.idAbschnittsdaten = dto.Abschnitt_ID;
		daten.idGrund = LehrerMinderleistungArt.getByKuerzel(dto.EntlastungsgrundKrz).daten.id;
		daten.anzahl = dto.EntlastungStd == null ? 0.0 : dto.EntlastungStd;
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
	 * Ermittelt die Entlastungen für die Lehrerabschnittsdaten mit der angegebenen ID und und gibt diese zurück.
	 *
	 * @param conn                      die Datenbankverbindung
	 * @param idLehrerabschnittsdaten   die ID der Lehrerabschnittsdaten
	 *
	 * @return die Liste mit den Entlastungen
	 */
	public static List<LehrerPersonalabschnittsdatenAnrechnungsstunden> getByLehrerabschnittsdatenId(final DBEntityManager conn, final Long idLehrerabschnittsdaten) {
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> result = new ArrayList<>();
    	// Bestimme die Anrechungen für die Lehrerabschnittsdaten
		final List<DTOLehrerEntlastungsstunde> dtos = conn.queryNamed("DTOLehrerEntlastungsstunde.abschnitt_id", idLehrerabschnittsdaten, DTOLehrerEntlastungsstunde.class);
    	if (dtos == null)
    		return result;
    	// Konvertiere sie und füge sie zur Liste hinzu
    	for (final DTOLehrerEntlastungsstunde dto : dtos)
    		result.add(dtoMapper.apply(dto));
    	return result;
	}

	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
		final DTOLehrerEntlastungsstunde dto = conn.queryByKey(DTOLehrerEntlastungsstunde.class, id);
    	if (dto == null)
    		return OperationError.NOT_FOUND.getResponse();
    	final LehrerPersonalabschnittsdatenAnrechnungsstunden daten = dtoMapper.apply(dto);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	private final Map<String, DataBasicMapper<DTOLehrerEntlastungsstunde>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, anrechnung, value, map) -> {
			final long patch_id = JSONMapper.convertToLong(value, false);
			if ((patch_id != anrechnung.ID))
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("idAbschnittsdaten", (conn, anrechnung, value, map) -> {
			final long patch_id = JSONMapper.convertToLong(value, false);
			if (patch_id != anrechnung.Abschnitt_ID)
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("idGrund", (conn, anrechnung, value, map) -> {
			final long idGrund = JSONMapper.convertToLong(value, false);
			final LehrerMinderleistungArt grund = LehrerMinderleistungArt.getByID(idGrund);
			if (grund == null)
				throw OperationError.NOT_FOUND.exception();
			anrechnung.EntlastungsgrundKrz = grund.daten.kuerzel;
		}),
		Map.entry("anzahl", (conn, anrechnung, value, map) -> {
			anrechnung.EntlastungStd = JSONMapper.convertToDouble(value, false);
		})
	);

	@Override
	public Response patch(final Long id, final InputStream is) {
		return super.patchBasic(id, is, DTOLehrerEntlastungsstunde.class, patchMappings);
	}

}
