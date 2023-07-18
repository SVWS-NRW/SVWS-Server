package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsicht;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanPausenaufsichten;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanPausenaufsichtenBereiche;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanPausenzeit;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link StundenplanPausenaufsicht}.
 */
public final class DataStundenplanPausenaufsichten extends DataManager<Long> {

	private final Long idStundenplan;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link StundenplanPausenaufsicht}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idStundenplan   die ID des Stundenplans, dessen Pausenaufsichten abgefragt werden
	 */
	public DataStundenplanPausenaufsichten(final DBEntityManager conn, final Long idStundenplan) {
		super(conn);
		this.idStundenplan = idStundenplan;
	}


	@Override
	public Response getAll() {
		return this.getList();
	}


	/**
	 * Holt alle StundenplanPausenaufsichten eines gegebenen Stundenplans aus
	 * der Datenbank.
	 *
	 * @param idStundenplan die ID des Stundenplans
	 *
	 * @return eine Liste aller Pausenaufsichten des Stundenplans
	 */
	List<StundenplanPausenaufsicht> getAufsichten(final long idStundenplan) {
		final List<StundenplanPausenaufsicht> daten = new ArrayList<>();
		// Bestimme die Pausenzeiten des Stundenplans
		final List<Long> pausenzeiten = conn.queryNamed("DTOStundenplanPausenzeit.stundenplan_id", idStundenplan, DTOStundenplanPausenzeit.class)
				.stream().map(p -> p.ID).toList();
		if (pausenzeiten.isEmpty())
			return daten;
		// Bestimme die Aufsichten in dieser Pausenzeit
		final List<DTOStundenplanPausenaufsichten> dtoAufsichten = conn.queryNamed("DTOStundenplanPausenaufsichten.pausenzeit_id.multiple",
				pausenzeiten, DTOStundenplanPausenaufsichten.class);
		if (dtoAufsichten.isEmpty())
			return daten;
		// Bestimme die Zuordnung der Aufsichtsbereiche zu den Pausenaufsichten
		final Map<Long, List<DTOStundenplanPausenaufsichtenBereiche>> mapBereiche = conn.queryNamed("DTOStundenplanPausenaufsichtenBereiche.pausenaufsicht_id.multiple",
				dtoAufsichten.stream().map(a -> a.ID).toList(), DTOStundenplanPausenaufsichtenBereiche.class)
				.stream().collect(Collectors.groupingBy(b -> b.Pausenaufsicht_ID));
		for (final DTOStundenplanPausenaufsichten dtoAufsicht : dtoAufsichten) {
			final StundenplanPausenaufsicht aufsicht = new StundenplanPausenaufsicht();
			aufsicht.id = dtoAufsicht.ID;
			aufsicht.idPausenzeit = dtoAufsicht.Pausenzeit_ID;
			aufsicht.idLehrer = dtoAufsicht.Lehrer_ID;
			aufsicht.wochentyp = dtoAufsicht.Wochentyp;
			if (mapBereiche.containsKey(aufsicht.id))
				aufsicht.bereiche.addAll(mapBereiche.get(aufsicht.id).stream().map(b -> b.Aufsichtsbereich_ID).toList());
			daten.add(aufsicht);
		}
		return daten;
	}


	@Override
	public Response getList() {
		if (idStundenplan == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einem Stundenplan mit der ID null ist unzulässig.");
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);
		if (stundenplan == null)
			return OperationError.NOT_FOUND.getResponse("Es wurde kein Stundenplan mit der ID %d gefunden.".formatted(idStundenplan));
		final List<StundenplanPausenaufsicht> daten = getAufsichten(idStundenplan);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einer Pausenaufsicht mit der ID null ist unzulässig.");
		final DTOStundenplanPausenaufsichten dtoAufsicht = conn.queryByKey(DTOStundenplanPausenaufsichten.class, id);
		if (dtoAufsicht == null)
			return OperationError.NOT_FOUND.getResponse("Es wurde keine Pausenaufsicht mit der ID %d gefunden.".formatted(id));

		final List<Long> bereiche = conn.queryNamed("DTOStundenplanPausenaufsichtenBereiche.pausenaufsicht_id",
				dtoAufsicht.ID, DTOStundenplanPausenaufsichtenBereiche.class).stream().map(b -> b.Aufsichtsbereich_ID).toList();
		final StundenplanPausenaufsicht daten = new StundenplanPausenaufsicht();
		daten.id = dtoAufsicht.ID;
		daten.idPausenzeit = dtoAufsicht.Pausenzeit_ID;
		daten.idLehrer = dtoAufsicht.Lehrer_ID;
		daten.wochentyp = dtoAufsicht.Wochentyp;
		daten.bereiche.addAll(bereiche);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static final Map<String, DataBasicMapper<DTOStundenplanPausenaufsichten>> patchMappings = Map.ofEntries(
		Map.entry("id", (dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID))
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("idPausenzeit", (dto, value, map) -> { throw OperationError.BAD_REQUEST.exception(); }), // TODO Implementierung und Validierung der Pausenzeit (existiert sie für den Stundenplan?)
		Map.entry("idLehrer", (dto, value, map) -> { throw OperationError.BAD_REQUEST.exception(); }), // TODO Implementierung und Validierung des Lehrers (existiert er?)
		Map.entry("wochentyp", (dto, value, map) -> dto.Wochentyp = JSONMapper.convertToInteger(value, false)));

	@Override
	public Response patch(final Long id, final InputStream is) {
		return super.patchBasic(id, is, DTOStundenplanPausenaufsichten.class, patchMappings);
	}

}
