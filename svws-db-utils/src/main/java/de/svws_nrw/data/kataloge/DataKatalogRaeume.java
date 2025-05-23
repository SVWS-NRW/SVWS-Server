package de.svws_nrw.data.kataloge;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ObjLongConsumer;

import de.svws_nrw.core.data.schule.Raum;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogRaum;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link Raum}.
 */
public final class DataKatalogRaeume extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link Raum}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogRaeume(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOKatalogRaum} in einen Core-DTO {@link Raum}.
	 */
	private static final DTOMapper<DTOKatalogRaum, Raum> dtoMapper = (final DTOKatalogRaum r) -> {
		final Raum daten = new Raum();
		daten.id = r.ID;
		daten.kuerzel = r.Kuerzel;
		daten.beschreibung = r.Beschreibung;
		daten.groesse = r.Groesse;
		return daten;
	};


	@Override
	public Response getAll() throws ApiOperationException {
		return this.getList();
	}

	/**
	 * Gibt die Räume zurück.
	 *
	 * @param conn   die Datenbankverbindung
	 *
	 * @return die Liste der Räume
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<Raum> getRaeume(final @NotNull DBEntityManager conn) throws ApiOperationException {
		final List<DTOKatalogRaum> raeume = conn.queryAll(DTOKatalogRaum.class);
		final ArrayList<Raum> daten = new ArrayList<>();
		for (final DTOKatalogRaum r : raeume)
			daten.add(dtoMapper.apply(r));
		return daten;
	}


	@Override
	public Response getList() throws ApiOperationException {
		final List<Raum> daten = getRaeume(conn);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einem Raum mit der ID null ist unzulässig.");
		final DTOKatalogRaum raum = conn.queryByKey(DTOKatalogRaum.class, id);
		if (raum == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Raum mit der ID %d gefunden.".formatted(id));
		final Raum daten = dtoMapper.apply(raum);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static final Map<String, DataBasicMapper<DTOKatalogRaum>> patchMappings = Map.ofEntries(
			Map.entry("id", (conn, dto, value, map) -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id.longValue() != dto.ID))
					throw new ApiOperationException(Status.BAD_REQUEST);
			}),
			Map.entry("kuerzel", (conn, dto, value, map) -> {
				dto.Kuerzel = JSONMapper.convertToString(value, false, false, 20).trim();
				if ("".equals(dto.Kuerzel))
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Das Kürzel darf nicht nur aus Leerzeichen bestehen (diese werden am Anfang und am Ende des Kürzels automatisch entfernt.");
			}),
			Map.entry("beschreibung", (conn, dto, value, map) -> dto.Beschreibung = JSONMapper.convertToString(value, false, true, 1000)),
			Map.entry("groesse", (conn, dto, value, map) -> dto.Groesse = JSONMapper.convertToInteger(value, false)));


	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOKatalogRaum.class, patchMappings);
	}


	private static final Set<String> requiredCreateAttributes = Set.of("kuerzel", "groesse");

	private static final ObjLongConsumer<DTOKatalogRaum> initDTO = (dto, id) -> dto.ID = id;

	/**
	 * Fügt einen Raum mit den übergebenen JSON-Daten der Datenbank hinzu und gibt das zugehörige CoreDTO
	 * zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response add(final InputStream is) throws ApiOperationException {
		return super.addBasic(is, DTOKatalogRaum.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Fügt mehrere Räume mit den übergebenen JSON-Daten der Datenbank hinzu und gibt die zugehörigen CoreDTOs
	 * zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response addMultiple(final InputStream is) throws ApiOperationException {
		return super.addBasicMultiple(is, DTOKatalogRaum.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Löscht einen Raum
	 *
	 * @param id   die ID des Raums
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response delete(final Long id) throws ApiOperationException {
		return super.deleteBasic(id, DTOKatalogRaum.class, dtoMapper);
	}


	/**
	 * Löscht mehrere Räume
	 *
	 * @param ids   die IDs der Räume
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response deleteMultiple(final List<Long> ids) throws ApiOperationException {
		return super.deleteBasicMultiple(ids, DTOKatalogRaum.class, dtoMapper);
	}

}
