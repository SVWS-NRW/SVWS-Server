package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsichten;
import de.svws_nrw.data.DataManager;
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
 * {@link StundenplanPausenaufsichten}.
 */
public final class DataStundenplanPausenaufsichten extends DataManager<Long> {

	private final long idStundenplan;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link StundenplanPausenaufsichten}.
	 *
	 * @param conn          die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idStundenplan die ID des Stundenplans, dessen Pausenaufsichten abgefragt
	 *                      werden
	 */
	public DataStundenplanPausenaufsichten(final DBEntityManager conn, final long idStundenplan) {
		super(conn);
		this.idStundenplan = idStundenplan;
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	private List<StundenplanPausenaufsichten> getAufsichten(final long idStundenplan) {
		final List<StundenplanPausenaufsichten> daten = new ArrayList<>();
		// Bestimme die Pausenzeiten des Stundenplans
		final List<Long> pausenzeiten = conn.queryNamed("DTOStundenplanPausenzeit.stundenplan_id", idStundenplan, DTOStundenplanPausenzeit.class)
				.stream().map(p -> p.ID).toList();
		if (pausenzeiten.isEmpty())
			return daten;
		// Bestimme die Aufsichten in dieser Pausenzeit
		final List<DTOStundenplanPausenaufsichten> dtoAufsichten = conn.queryNamed("DTOStundenplanPausenaufsichten.pausenzeit_id.multiple", pausenzeiten, DTOStundenplanPausenaufsichten.class);
		if (dtoAufsichten.isEmpty())
			return daten;
		// Bestimme die Zuordnung der Aufsichtsbereiche zu den Pausenaufsichten
		final Map<Long, List<DTOStundenplanPausenaufsichtenBereiche>> mapBereiche = conn.queryNamed("DTOStundenplanPausenaufsichtenBereiche.pausenaufsicht_id.multiple", dtoAufsichten.stream().map(a -> a.ID).toList(), DTOStundenplanPausenaufsichtenBereiche.class)
				.stream().collect(Collectors.groupingBy(b -> b.Pausenaufsicht_ID));
		for (final DTOStundenplanPausenaufsichten dtoAufsicht : dtoAufsichten) {
			final StundenplanPausenaufsichten aufsicht = new StundenplanPausenaufsichten();
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
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);
		if (stundenplan == null)
			return OperationError.NOT_FOUND.getResponse("Es wurde kein Stundenplan mit der ID %d gefunden.".formatted(idStundenplan));
		final List<StundenplanPausenaufsichten> daten = getAufsichten(idStundenplan);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long idSchueler, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
