package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import de.svws_nrw.core.data.lehrer.LehrerPersonalabschnittsdatenLehrerfunktion;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchulfunktion;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerFunktion;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerPersonalabschnittsdatenLehrerfunktion}.
 */
public final class DataLehrerPersonalabschnittsdatenLehrerfunktionen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerPersonalabschnittsdatenLehrerfunktion}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLehrerPersonalabschnittsdatenLehrerfunktionen(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOLehrerFunktion} in einen Core-DTO {@link LehrerPersonalabschnittsdatenLehrerfunktion}.
	 */
	private static final Function<DTOLehrerFunktion, LehrerPersonalabschnittsdatenLehrerfunktion> dtoMapper = (final DTOLehrerFunktion dto) -> {
		final LehrerPersonalabschnittsdatenLehrerfunktion daten = new LehrerPersonalabschnittsdatenLehrerfunktion();
		daten.id = dto.ID;
		daten.idAbschnittsdaten = dto.Abschnitt_ID;
		daten.idFunktion = dto.Funktion_ID;
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
	 * Ermittelt die Lehrerfunktionen für die Lehrerabschnittsdaten mit der angegebenen ID und und gibt diese zurück.
	 *
	 * @param conn                      die Datenbankverbindung
	 * @param idLehrerabschnittsdaten   die ID der Lehrerabschnittsdaten
	 *
	 * @return die Liste mit den Lehrerfunktionen
	 */
	public static List<LehrerPersonalabschnittsdatenLehrerfunktion> getByLehrerabschnittsdatenId(final DBEntityManager conn, final Long idLehrerabschnittsdaten) {
		final List<LehrerPersonalabschnittsdatenLehrerfunktion> result = new ArrayList<>();
    	// Bestimme die Lehrerfunktionen für die Lehrerabschnittsdaten
		final List<DTOLehrerFunktion> dtos = conn.queryNamed("DTOLehrerFunktion.abschnitt_id", idLehrerabschnittsdaten, DTOLehrerFunktion.class);
    	if (dtos == null)
    		return result;
    	// Konvertiere sie und füge sie zur Liste hinzu
    	for (final DTOLehrerFunktion dto : dtos)
    		result.add(dtoMapper.apply(dto));
    	return result;
	}

	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
		final DTOLehrerFunktion dto = conn.queryByKey(DTOLehrerFunktion.class, id);
    	if (dto == null)
    		return OperationError.NOT_FOUND.getResponse();
    	final LehrerPersonalabschnittsdatenLehrerfunktion daten = dtoMapper.apply(dto);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	private final Map<String, DataBasicMapper<DTOLehrerFunktion>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, funktion, value, map) -> {
			final long patch_id = JSONMapper.convertToLong(value, false);
			if ((patch_id != funktion.ID))
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("idAbschnittsdaten", (conn, funktion, value, map) -> {
			final long patch_id = JSONMapper.convertToLong(value, false);
			if (patch_id != funktion.Abschnitt_ID)
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("idFunktion", (conn, funktion, value, map) -> {
			final long idFunktion = JSONMapper.convertToLong(value, false);
			final DTOSchulfunktion f = conn.queryByKey(DTOSchulfunktion.class, idFunktion);
			if (f == null)
				throw OperationError.NOT_FOUND.exception();
			funktion.Funktion_ID = idFunktion;
		})
	);

	@Override
	public Response patch(final Long id, final InputStream is) {
		return super.patchBasic(id, is, DTOLehrerFunktion.class, patchMappings);
	}

}
