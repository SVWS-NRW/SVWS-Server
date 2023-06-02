package de.svws_nrw.data.kataloge;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;

import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOKatalogZeitraster;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanZeitraster}.
 */
public final class DataKatalogZeitraster extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanZeitraster}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogZeitraster(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOKatalogZeitraster} in einen Core-DTO {@link StundenplanZeitraster}.
	 */
	private static final Function<DTOKatalogZeitraster, StundenplanZeitraster> dtoMapper = (final DTOKatalogZeitraster z) -> {
		final StundenplanZeitraster daten = new StundenplanZeitraster();
		daten.id = z.ID;
		daten.wochentag = z.Tag;
		daten.unterrichtstunde = z.Stunde;
		daten.stundenbeginn = z.Beginn;
		daten.stundenende = z.Ende;
		return daten;
	};


	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Gibt das Zeitraster zurück.
	 *
	 * @param conn   die Datenbankverbindung
	 *
	 * @return das Zeitraster
	 */
	public static List<StundenplanZeitraster> getZeitraster(final @NotNull DBEntityManager conn) {
		final List<DTOKatalogZeitraster> zeitraster = conn.queryAll(DTOKatalogZeitraster.class);
		final ArrayList<StundenplanZeitraster> daten = new ArrayList<>();
		for (final DTOKatalogZeitraster z : zeitraster)
			daten.add(dtoMapper.apply(z));
		return daten;
	}

	@Override
	public Response getList() {
		final List<StundenplanZeitraster> daten = getZeitraster(conn);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einem Zeitrastereintrag mit der ID null ist unzulässig.");
		final DTOKatalogZeitraster eintrag = conn.queryByKey(DTOKatalogZeitraster.class, id);
		if (eintrag == null)
			return OperationError.NOT_FOUND.getResponse("Es wurde keine Zeitrastereintrag mit der ID %d gefunden.".formatted(id));
		final StundenplanZeitraster daten = dtoMapper.apply(eintrag);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static final Map<String, DataBasicMapper<DTOKatalogZeitraster>> patchMappings = Map.ofEntries(
		Map.entry("id", (dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID))
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("wochentag", (dto, value, map) -> dto.Tag = JSONMapper.convertToIntegerInRange(value, false, 1, 8)),
		Map.entry("unterrichtstunde", (dto, value, map) -> dto.Stunde = JSONMapper.convertToIntegerInRange(value, false, 0, 30)),
		Map.entry("stundenbeginn", (dto, value, map) -> dto.Beginn = JSONMapper.convertToIntegerInRange(value, true, 0, 1440)),
		Map.entry("stundenende", (dto, value, map) -> dto.Ende = JSONMapper.convertToIntegerInRange(value, true, 0, 1440))
	);

	@Override
	public Response patch(final Long id, final InputStream is) {
		return super.patchBasic(id, is, DTOKatalogZeitraster.class, patchMappings);
	}


	private static final Set<String> requiredCreateAttributes = Set.of("wochentag", "unterrichtstunde", "stundenbeginn", "stundenende");

	/**
	 * Fügt einen Zeitrastereintrag mit den übergebenen JSON-Daten der Datenbank hinzu und gibt das zugehörige CoreDTO
	 * zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 */
	public Response add(final InputStream is) {
		// füge den Zeitrastereintrag in der Datenbank hinzu und gebe das zugehörige CoreDTO zurück.
		final ObjLongConsumer<DTOKatalogZeitraster> initDTO = (dto, id) -> dto.ID = id;
		return super.addBasic(is, DTOKatalogZeitraster.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Löscht einen Zeitrastereintrag
	 *
	 * @param id   die ID des Zeitrastereintrags
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(final Long id) {
		return super.deleteBasic(id, DTOKatalogZeitraster.class, dtoMapper);
	}

}
