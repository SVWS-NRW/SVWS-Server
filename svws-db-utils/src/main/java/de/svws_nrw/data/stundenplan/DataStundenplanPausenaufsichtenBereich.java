package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ObjLongConsumer;

import de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsichtBereich;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsichtBereichUpdate;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanAufsichtsbereich;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanPausenaufsichten;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanPausenaufsichtenBereiche;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanPausenzeit;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link StundenplanPausenaufsichtBereich}.
 */
public final class DataStundenplanPausenaufsichtenBereich extends DataManager<Long> {

	private final Long idStundenplan;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link StundenplanPausenaufsichtBereich}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idStundenplan   die ID des Stundenplans, dessen Pausenaufsichten abgefragt werden
	 */
	public DataStundenplanPausenaufsichtenBereich(final DBEntityManager conn, final Long idStundenplan) {
		super(conn);
		this.idStundenplan = idStundenplan;
	}


	@Override
	public Response getAll() throws ApiOperationException {
		return this.getList();
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOStundenplanPausenaufsichtenBereiche} in
	 * einen Core-DTO {@link StundenplanPausenaufsichtBereich}.
	 */
	public static final DTOMapper<DTOStundenplanPausenaufsichtenBereiche, StundenplanPausenaufsichtBereich> dtoMapper = (final DTOStundenplanPausenaufsichtenBereiche dto) -> {
		final StundenplanPausenaufsichtBereich daten = new StundenplanPausenaufsichtBereich();
		daten.id = dto.ID;
		daten.idAufsichtsbereich = dto.Aufsichtsbereich_ID;
		daten.idPausenaufsicht = dto.Pausenaufsicht_ID;
		daten.wochentyp = dto.Wochentyp;
		return daten;
	};


	@Override
	public Response getList() throws ApiOperationException {
		throw new UnsupportedOperationException();
	}


	private StundenplanPausenaufsichtBereich getZuordnung(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Zuordnung mit der ID null ist unzulässig.");
		final DTOStundenplanPausenaufsichtenBereiche dto = conn.queryByKey(DTOStundenplanPausenaufsichtenBereiche.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Zuordnung mit der ID %d gefunden.".formatted(id));
		return dtoMapper.apply(dto);
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		final StundenplanPausenaufsichtBereich daten = getZuordnung(id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static final Map<String, DataBasicMapper<DTOStundenplanPausenaufsichtenBereiche>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID))
				throw new ApiOperationException(Status.BAD_REQUEST);
		}),
		Map.entry("idPausenaufsicht", (conn, dto, value, map) -> {
			final DTOStundenplanPausenaufsichten paufsicht = conn.queryByKey(DTOStundenplanPausenaufsichten.class, value);
			if (paufsicht == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Pausenaufsicht mit der ID %d nicht gefunden.".formatted((Long) value));
			dto.Pausenaufsicht_ID = paufsicht.ID;
		}),
		Map.entry("idAufsichtsbereich", (conn, dto, value, map) -> {
			final DTOStundenplanAufsichtsbereich pbereich = conn.queryByKey(DTOStundenplanAufsichtsbereich.class, value);
			if (pbereich == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Aufsichtsbereich mit der ID %d nicht gefunden.".formatted((Long) value));
			dto.Aufsichtsbereich_ID = pbereich.ID;
		}),
		Map.entry("wochentyp", (conn, dto, value, map) -> dto.Wochentyp = JSONMapper.convertToInteger(value, false))
	);

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOStundenplanPausenaufsichtenBereiche.class, patchMappings);
	}


	private static final Set<String> requiredCreateAttributes = Set.of("idPausenaufsicht", "idAufsichtsbereich", "wochentyp");

	private final ObjLongConsumer<DTOStundenplanPausenaufsichtenBereiche> initDTO = (dto, id) -> dto.ID = id;

	/**
	 * Fügt eine Pausenaufsicht mit den übergebenen JSON-Daten der Datenbank hinzu und gibt das zugehörige Core-DTO
	 * zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response add(final InputStream is) throws ApiOperationException {
		DataStundenplan.getDTOStundenplan(conn, idStundenplan);   // Prüfe, on der Stundenplan existiert
		return super.addBasic(is, DTOStundenplanPausenaufsichtenBereiche.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Fügt die Pausenaufsichten mit den übergebenen JSON-Daten der Datenbank hinzu und gibt die zugehörigen Core-DTOs
	 * zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response addMultiple(final InputStream is) throws ApiOperationException {
		DataStundenplan.getDTOStundenplan(conn, idStundenplan);   // Prüfe, on der Stundenplan existiert
		return super.addBasicMultiple(is, DTOStundenplanPausenaufsichtenBereiche.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}

	/**
	 * Löscht eine Pausenaufsicht
	 *
	 * @param id   die ID der Pausenaufsicht
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response delete(final Long id) throws ApiOperationException {
		return super.deleteBasic(id, DTOStundenplanPausenaufsichtenBereiche.class, dtoMapper);
	}


	/**
	 * Löscht mehrere Pausenaufsichten
	 *
	 * @param ids   die IDs der Pausenaufsichten
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response deleteMultiple(final List<Long> ids) throws ApiOperationException {
		final List<DTOStundenplanPausenaufsichtenBereiche> dtos = conn.queryByKeyList(DTOStundenplanPausenaufsichtenBereiche.class, ids);
		if (dtos.size() != ids.size())
			throw new ApiOperationException(Status.BAD_REQUEST, "Nicht alle Zuordnungen konnten anhand der übergebenen IDs in der DB gefunden werden.");
		// TODO Prüfe, ob die Bereiche auch alle zu dem Stundenplan gehören
		return super.deleteBasicMultiple(ids, DTOStundenplanPausenaufsichtenBereiche.class, dtoMapper);
	}


	/**
	 * Entfernt alle zum Entfernen angegebenen Zuordnungen von Pausenaufsichten zu Aufsichtsbereichen
	 * und fügt anschließend alle zum Hinzufügen angegebenen Zuordnungen hinzu.
	 *
	 * @param update       die zu entfernenden Zuordnungen und die hinzuzufügenden Zuordnungen
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Update-Operation angibt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
    public Response update(final @NotNull StundenplanPausenaufsichtBereichUpdate update) throws ApiOperationException {
		if (update.listEntfernen.isEmpty() && update.listHinzuzufuegen.isEmpty())
			return Response.status(Status.NO_CONTENT).build();
		// Bestimme den zugehörigen Stundenplan
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);
		if (stundenplan == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Der Stundenplan mit der ID %d wurde nicht gefunden.".formatted(idStundenplan));
		// Entferne die Zuordnungen, sofern sie zum aktuellen Stundenplan gehören
		if (!update.listEntfernen.isEmpty()) {
			// Überprüfe die zu entfernenden Zuordnungen
			final @NotNull List<@NotNull Long> listEntfernenIDs = update.listEntfernen.stream().map(r -> r.id).toList();
			final List<DTOStundenplanPausenaufsichtenBereiche> zuordnungen = conn.queryByKeyList(DTOStundenplanPausenaufsichtenBereiche.class, listEntfernenIDs);
			if (zuordnungen.size() != update.listEntfernen.size())
				throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle Zuordnungen zum Löschen wurden in der Datenbank gefunden.");
			final List<Long> idsPausenaufsichten = zuordnungen.stream().map(z -> z.Pausenaufsicht_ID).toList();
			final List<DTOStundenplanPausenaufsichten> pausenaufsichten = conn.queryByKeyList(DTOStundenplanPausenaufsichten.class, idsPausenaufsichten);
			final List<Long> idsPausenzeiten = pausenaufsichten.stream().map(a -> a.Pausenzeit_ID).toList();
			final List<DTOStundenplanPausenzeit> pausenzeiten = conn.queryByKeyList(DTOStundenplanPausenzeit.class, idsPausenzeiten);
			final List<Long> idsStundenplan = pausenzeiten.stream().map(z -> z.Stundenplan_ID).toList();
			if ((idsStundenplan.size() != 1) || (idsStundenplan.get(0) != idStundenplan.longValue()))
				throw new ApiOperationException(Status.NOT_FOUND, "Alle Zuordnungen der zu löschenden Zuordnungen müssen zu dem Stundenplan mit der ID %d gehören. Dies ist nicht der Fall.".formatted(idStundenplan));
			// Entferne die Zuordnungen
			conn.transactionRemoveAll(zuordnungen);
			conn.transactionFlush();
		}
		// Füge die Zuordnungen hinzu
		if (!update.listHinzuzufuegen.isEmpty()) {
			// Überprüfe die hinzuzufügenden Zuordnungen
			final List<Long> idsPausenaufsichten = update.listHinzuzufuegen.stream().map(z -> z.idPausenaufsicht).toList();
			final List<DTOStundenplanPausenaufsichten> pausenaufsichten = conn.queryByKeyList(DTOStundenplanPausenaufsichten.class, idsPausenaufsichten);
			if (pausenaufsichten.size() != idsPausenaufsichten.size())
				throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle Pausenaufsichten der hinzuzüfügenden Zuordnungen wurden in der Datenbank gefunden.");
			final List<Long> idsPausenzeiten = pausenaufsichten.stream().map(a -> a.Pausenzeit_ID).toList();
			final List<DTOStundenplanPausenzeit> pausenzeiten = conn.queryByKeyList(DTOStundenplanPausenzeit.class, idsPausenzeiten);
			List<Long> idsStundenplan = pausenzeiten.stream().map(z -> z.Stundenplan_ID).toList();
			if ((idsStundenplan.size() != 1) || (idsStundenplan.get(0) != idStundenplan.longValue()))
				throw new ApiOperationException(Status.NOT_FOUND, "Alle Pausenzeiten der hinzuzufügenden Zuordnungen müssen zu dem Stundenplan mit der ID %d gehören. Dies ist nicht der Fall.".formatted(idStundenplan));
			final List<Long> idsAufsichtsbereiche = update.listHinzuzufuegen.stream().map(z -> z.idAufsichtsbereich).toList();
			final List<DTOStundenplanAufsichtsbereich> aufsichtsbereiche = conn.queryByKeyList(DTOStundenplanAufsichtsbereich.class, idsAufsichtsbereiche);
			if (aufsichtsbereiche.size() != idsAufsichtsbereiche.size())
				throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle Aufsichtsbereiche der hinzuzüfügenden Zuordnungen wurden in der Datenbank gefunden.");
			idsStundenplan = aufsichtsbereiche.stream().map(a -> a.Stundenplan_ID).toList();
			if ((idsStundenplan.size() != 1) || (idsStundenplan.get(0) != idStundenplan.longValue()))
				throw new ApiOperationException(Status.NOT_FOUND, "Alle Aufsichtsbereiche der hinzuzufügenden Zuordnungen müssen zu dem Stundenplan mit der ID %d gehören. Dies ist nicht der Fall.".formatted(idStundenplan));
			// ... und prüfe den Wochentyp
			for (final StundenplanPausenaufsichtBereich bereich : update.listHinzuzufuegen)
				if ((bereich.wochentyp < 0) || (bereich.wochentyp > stundenplan.WochentypModell))
					throw new ApiOperationException(Status.NOT_FOUND, "Der Wochentyp %d der Zuordnung liegt nicht nicht im Bereich des Wochentyp-Modells %d des Stundenplans.".formatted(bereich.wochentyp, stundenplan.WochentypModell));
			// Füge die Zuordnungen hinzu
			long id = conn.transactionGetNextID(DTOStundenplanPausenaufsichtenBereiche.class);
			for (final StundenplanPausenaufsichtBereich bereich : update.listHinzuzufuegen) {
				bereich.id = id++;
				conn.transactionPersist(new DTOStundenplanPausenaufsichtenBereiche(bereich.id, bereich.idPausenaufsicht, bereich.idAufsichtsbereich, bereich.wochentyp));
			}
		}
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(update.listHinzuzufuegen).build();
    }

}
