package de.svws_nrw.data.kataloge;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;

import de.svws_nrw.core.data.schule.Aufsichtsbereich;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogAufsichtsbereich;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link Aufsichtsbereich}.
 */
public final class DataKatalogAufsichtsbereiche extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link Aufsichtsbereich}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogAufsichtsbereiche(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOKatalogAufsichtsbereich} in einen Core-DTO {@link Aufsichtsbereich}.
	 */
	private static final Function<DTOKatalogAufsichtsbereich, Aufsichtsbereich> dtoMapper = (final DTOKatalogAufsichtsbereich a) -> {
		final Aufsichtsbereich daten = new Aufsichtsbereich();
		daten.id = a.ID;
		daten.kuerzel = a.Kuerzel;
		daten.beschreibung = a.Beschreibung;
		return daten;
	};


	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Gibt die Aufsichtsbereiche der Schule zurück.
	 *
	 * @param conn   die Datenbankverbindung
	 *
	 * @return die Liste der Aufsichtsbereiche
	 */
	public static List<Aufsichtsbereich> getAufsichtsbereiche(final @NotNull DBEntityManager conn) {
		final List<DTOKatalogAufsichtsbereich> aufsichtsbereiche = conn.queryAll(DTOKatalogAufsichtsbereich.class);
		final ArrayList<Aufsichtsbereich> daten = new ArrayList<>();
		for (final DTOKatalogAufsichtsbereich a : aufsichtsbereiche)
			daten.add(dtoMapper.apply(a));
		return daten;
	}

	@Override
	public Response getList() {
		final List<Aufsichtsbereich> daten = getAufsichtsbereiche(conn);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einem Aufsichtsbereich der Schule mit der ID null ist unzulässig.");
		final DTOKatalogAufsichtsbereich aufsichtsbereich = conn.queryByKey(DTOKatalogAufsichtsbereich.class, id);
		if (aufsichtsbereich == null)
			return OperationError.NOT_FOUND.getResponse("Es wurde kein Aufsichtsbereich der Schule mit der ID %d gefunden.".formatted(id));
		final Aufsichtsbereich daten = dtoMapper.apply(aufsichtsbereich);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static final Map<String, DataBasicMapper<DTOKatalogAufsichtsbereich>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID))
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("kuerzel", (conn, dto, value, map) -> dto.Kuerzel = JSONMapper.convertToString(value, false, false, 20)),
		Map.entry("beschreibung", (conn, dto, value, map) -> dto.Beschreibung = JSONMapper.convertToString(value, false, true, 1000))
	);

	@Override
	public Response patch(final Long id, final InputStream is) {
		return super.patchBasic(id, is, DTOKatalogAufsichtsbereich.class, patchMappings);
	}


	private static final Set<String> requiredCreateAttributes = Set.of("kuerzel");

	private static final ObjLongConsumer<DTOKatalogAufsichtsbereich> initDTO = (dto, id) -> dto.ID = id;

	/**
	 * Fügt einen Aufsichtsbereich mit den übergebenen JSON-Daten der Datenbank hinzu und gibt das zugehörige CoreDTO
	 * zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 */
	public Response add(final InputStream is) {
		return super.addBasic(is, DTOKatalogAufsichtsbereich.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Fügt mehrere Aufsichtsbereiche mit den übergebenen JSON-Daten der Datenbank hinzu und gibt die zugehörigen CoreDTOs
	 * zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 */
	public Response addMultiple(final InputStream is) {
		return super.addBasicMultiple(is, DTOKatalogAufsichtsbereich.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Löscht einen Aufsichtsbereich
	 *
	 * @param id   die ID des Aufsichtsbereichs
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(final Long id) {
		return super.deleteBasic(id, DTOKatalogAufsichtsbereich.class, dtoMapper);
	}


	/**
	 * Löscht mehrere Aufsichtsbereiche
	 *
	 * @param ids   die IDs der Aufsichtsbereiche
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response deleteMultiple(final List<Long> ids) {
		return super.deleteBasicMultiple(ids, DTOKatalogAufsichtsbereich.class, dtoMapper);
	}

}
