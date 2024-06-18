package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ObjLongConsumer;

import de.svws_nrw.core.data.schule.Schulleitung;
import de.svws_nrw.core.types.lehrer.LehrerLeitungsfunktion;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOSchulleitung;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link Schulleitung}.
 */
public final class DataSchulleitung extends DataManager<Long> {

	/** Die ID des Lehrers */
	private final Long idLehrer;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link Schulleitung}.
	 *
	 * @param conn       die Datenbankverbindung
	 * @param idLehrer   die ID des Lehrers, auf den sich die Anfragen beziehen
	 */
	public DataSchulleitung(final DBEntityManager conn, final Long idLehrer) {
		super(conn);
		this.idLehrer = idLehrer;
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOSchulleitung} in einen Core-DTO {@link Schulleitung}.
	 */
	public static final DTOMapper<DTOSchulleitung, Schulleitung> dtoMapper = (final DTOSchulleitung dto) -> {
		final Schulleitung daten = new Schulleitung();
		daten.id = dto.ID;
		daten.idLehrer = dto.LehrerID;
		daten.idLeitungsfunktion = dto.LeitungsfunktionID;
		daten.bezeichnung = dto.Funktionstext;
		daten.beginn = dto.Von;
		daten.ende = dto.Bis;
		return daten;
	};


	@Override
	public Response getAll() throws ApiOperationException {
		return this.getList();
	}


	/**
	 * Gibt die Liste der Schulleitungsfunktionen für den Lehrer mit der angegebenen ID zurück.
	 * Ist die ID null, so werden alle zurückgegeben.
	 *
	 * @param conn       die Datenbankverbindung
	 * @param idLehrer   die ID des Lehrers oder null
	 *
	 * @return die Liste der Schulleitungsfunktionen
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static List<Schulleitung> getSchulleitungsfunktionen(final @NotNull DBEntityManager conn, final Long idLehrer) throws ApiOperationException {
		// Ermittle die Schulleitungsfunktionen ...
		final List<DTOSchulleitung> funktionen = (idLehrer == null) ? conn.queryAll(DTOSchulleitung.class)
				: conn.queryList(DTOSchulleitung.QUERY_BY_LEHRERID, DTOSchulleitung.class, idLehrer);
		if (funktionen.isEmpty())
			return new ArrayList<>();
		final List<Schulleitung> result = new ArrayList<>();
		for (final DTOSchulleitung funktion : funktionen)
			result.add(dtoMapper.apply(funktion));
		return result;
	}


	/**
	 * Gibt die Schulleitungsfunktion für die übergebene ID zurück.
	 *
	 * @param conn   die Datenbankverbindung
	 * @param id     die ID der Schulleitungsfunktion
	 *
	 * @return die Informationen zu der Schulleitungsfunktion
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static Schulleitung getSchulleitungsfunktion(final @NotNull DBEntityManager conn, final long id) throws ApiOperationException {
		final DTOSchulleitung funktion = conn.queryByKey(DTOSchulleitung.class, id);
		if (funktion == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Eine Schulleitungsfunktion mit der ID %d konnte nicht gefunden werden.".formatted(id));
		return dtoMapper.apply(funktion);
	}


	@Override
	public Response getList() throws ApiOperationException {
		final List<Schulleitung> daten = getSchulleitungsfunktionen(conn, this.idLehrer);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		final Schulleitung daten = getSchulleitungsfunktion(conn, id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	private final Map<String, DataBasicMapper<DTOSchulleitung>> patchMappings = Map.ofEntries(
			Map.entry("id", (conn, dto, value, map) -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id.longValue() != dto.ID))
					throw new ApiOperationException(Status.BAD_REQUEST, "Die ID im Patch (%d) stimmt nicht mit der ID des API-Aufrufs (%d) überein."
							.formatted(patch_id, dto.ID));
			}),
			Map.entry("idLeitungsfunktion", (conn, dto, value, map) -> {
				final long id = JSONMapper.convertToLong(value, false);
				if (id != dto.LeitungsfunktionID) {
					final LehrerLeitungsfunktion funktion = LehrerLeitungsfunktion.getByID(id);
					if (funktion == null)
						throw new ApiOperationException(Status.BAD_REQUEST, "Es gibt keine Leitungsfunktion mit der ID %d.".formatted(id));
					dto.LeitungsfunktionID = id;
					if ((dto.Funktionstext == null) || (dto.Funktionstext.isBlank()))
						dto.Funktionstext = funktion.daten.bezeichnung;
				}
			}),
			Map.entry("bezeichnung",
					(conn, dto, value, map) -> dto.Funktionstext =
							JSONMapper.convertToString(value, false, false, Schema.tab_Schulleitung.col_Funktionstext.datenlaenge())),
			Map.entry("idLehrer", (conn, dto, value, map) -> {
				final long id = JSONMapper.convertToLong(value, false);
				if (id != dto.LehrerID) {
					final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, id);
					if (lehrer == null)
						throw new ApiOperationException(Status.NOT_FOUND, "Es konnte kein Lehrer mit der ID %d gefunden werden.".formatted(id));
					dto.LehrerID = id;
				}
			}),
			Map.entry("beginn", (conn, dto, value, map) -> dto.Von = JSONMapper.convertToString(value, true, false, null)),  // TODO convertToDate im JSONMapper
			Map.entry("ende", (conn, dto, value, map) -> dto.Bis = JSONMapper.convertToString(value, true, false, null))  // TODO convertToDate im JSONMapper
	);



	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOSchulleitung.class, patchMappings);
	}

	private static final Set<String> requiredCreateAttributes = Set.of("idLeitungsfunktion", "idLehrer");

	private final ObjLongConsumer<DTOSchulleitung> initDTO = (dto, id) -> {
		dto.ID = id;
	};

	/**
	 * Fügt eine Schulleitungsfunktion mit den übergebenen JSON-Daten der Datenbank hinzu und gibt das zugehörige CoreDTO
	 * zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response add(final InputStream is) throws ApiOperationException {
		return super.addBasic(is, DTOSchulleitung.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Fügt mehrere Schulleitungsfunktionen mit den übergebenen JSON-Daten der Datenbank hinzu und gibt die
	 * zugehörigen CoreDTOs zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response addMultiple(final InputStream is) throws ApiOperationException {
		return super.addBasicMultiple(is, DTOSchulleitung.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Löscht eine Schulleitungsfunktion
	 *
	 * @param id   die ID der Schulleitungsfunktion
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response delete(final Long id) throws ApiOperationException {
		return super.deleteBasic(id, DTOSchulleitung.class, dtoMapper);
	}


	/**
	 * Löscht mehrere Schulleitungsfunktionen
	 *
	 * @param ids   die IDs der Schulleitungsfunktionen
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response deleteMultiple(final List<Long> ids) throws ApiOperationException {
		return super.deleteBasicMultiple(ids, DTOSchulleitung.class, dtoMapper);
	}

}
