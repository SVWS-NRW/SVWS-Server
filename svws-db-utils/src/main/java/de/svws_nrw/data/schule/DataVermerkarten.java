package de.svws_nrw.data.schule;

import de.svws_nrw.core.data.schule.VermerkartEintrag;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOVermerkArt;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ObjLongConsumer;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link VermerkartEintrag}.
 */
public final class DataVermerkarten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link VermerkartEintrag}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataVermerkarten(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOVermerkArt} in einen Core-DTO {@link VermerkartEintrag}.
	 */
	private static final DTOMapper<DTOVermerkArt, VermerkartEintrag> dtoMapper = (final DTOVermerkArt k) -> {
		final VermerkartEintrag daten = new VermerkartEintrag();
		daten.id = k.ID;
		daten.bezeichnung = (k.Bezeichnung == null) ? "" : k.Bezeichnung;
		daten.sortierung = (k.Sortierung == null) ? 32000 : k.Sortierung;
		daten.istSichtbar = (k.Sichtbar == null) || k.Sichtbar;
		return daten;
	};


	@Override
	public Response getAll() throws ApiOperationException {
		final var daten = getVermerkarten(conn);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Gibt die Liste der Vermerkarten zurück.
	 *
	 * @param conn die Datenbankverbindung
	 *
	 * @return die Liste der Vermerkarten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static List<VermerkartEintrag> getVermerkarten(final @NotNull DBEntityManager conn) throws ApiOperationException {
		if (conn == null)
			return new ArrayList<>();
		final List<DTOVermerkArt> katalog = conn.queryAll(DTOVermerkArt.class);
		if (katalog == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Vermerkarten gefunden.");
		final ArrayList<VermerkartEintrag> daten = new ArrayList<>();
		for (final DTOVermerkArt r : katalog)
			daten.add(dtoMapper.apply(r));
		return daten;
	}


	@Override
	public Response getList() throws ApiOperationException {
		return this.getAll();
	}


	/**
	 * Bestimmt die Vermerkart anhand der angegebenen ID.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 * @param id   die ID des Vermerk-Katalog-Eintrags
	 *
	 * @return der Eintrag der Vermerkart
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static VermerkartEintrag getVermerk(final @NotNull DBEntityManager conn, final long id) throws ApiOperationException {
		final DTOVermerkArt vermerk = conn.queryByKey(DTOVermerkArt.class, id);
		if (vermerk == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Vermerkart mit der ID %d wurde nicht gefunden.".formatted(id));
		return dtoMapper.apply(vermerk);
	}


	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Vermerkart mit der ID null ist unzulässig.");
		final VermerkartEintrag daten = getVermerk(conn, id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static final Map<String, DataBasicMapper<DTOVermerkArt>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (Long.compare(patch_id, dto.ID) != 0))
				throw new ApiOperationException(Status.BAD_REQUEST, "Die angegebene ID %d ist null oder stimmt nicht mit der ID %d im DTO überein.".formatted(patch_id, dto.ID));
		}),
		Map.entry("bezeichnung", (conn, dto, value, map) -> dto.Bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Vermerkart.col_Bezeichnung.datenlaenge())),
		Map.entry("sortierung", (conn, dto, value, map) -> dto.Sortierung = JSONMapper.convertToInteger(value, false)),
		Map.entry("istSichtbar", (conn, dto, value, map) -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false))
	);


	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOVermerkArt.class, patchMappings);
	}


	private static final Set<String> requiredCreateAttributes = Set.of("bezeichnung");

	private static final ObjLongConsumer<DTOVermerkArt> initDTO = (dto, id) -> {
		dto.ID = id;
	};


	/**
	 * Erstellt eine neue Vermerkart
	 *
	 * @param is JSON-Objekt mit den Daten
	 *
	 * @return Eine Response mit der neuen Vermerkart
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response add(final InputStream is) throws ApiOperationException {
		return super.addBasic(is, DTOVermerkArt.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Löscht eine Vermerkart aus dem Katalog
	 *
	 * @param id die ID des Vermerkart-Katalog-Eintrags
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response delete(final Long id) throws ApiOperationException {
		return super.deleteBasic(id, DTOVermerkArt.class, dtoMapper);
	}


	/**
	 * Löscht mehrere Vermerkart-Katalog-Einträge
	 *
	 * @param ids die IDs der Vermerkart-Katalog-Einträge
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response deleteMultiple(final List<Long> ids) throws ApiOperationException {
		return super.deleteBasicMultiple(ids, DTOVermerkArt.class, dtoMapper);
	}

}
