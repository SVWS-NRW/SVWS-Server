package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.core.data.stundenplan.StundenplanAufsichtsbereich;
import de.svws_nrw.core.data.stundenplan.StundenplanJahrgang;
import de.svws_nrw.core.data.stundenplan.StundenplanKalenderwochenzuordnung;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenzeit;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanSchiene;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link Stundenplan}.
 */
public final class DataStundenplan extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link Stundenplan}.
	 *
	 * @param conn   die Datenbank-Verbindung
	 */
	public DataStundenplan(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Gibt den Stundenplan zur angegebenen ID zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param id   die ID des Stundenplans
	 *
	 * @return das Stundenplan-Objekt
	 */
	public static Stundenplan getStundenplan(final DBEntityManager conn, final long id) {
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, id);
		if (stundenplan == null)
			return null;
		final List<StundenplanZeitraster> zeitraster = DataStundenplanZeitraster.getZeitraster(conn, id);
		final List<StundenplanRaum> raeume = DataStundenplanRaeume.getRaeume(conn, id);
		final List<StundenplanSchiene> schienen = DataStundenplanSchienen.getSchienen(conn, id);
		final List<StundenplanJahrgang> jahrgaenge = DataStundenplanJahrgaenge.getJahrgaenge(conn, id);
		final List<StundenplanPausenzeit> pausenzeiten = DataStundenplanPausenzeiten.getPausenzeiten(conn, id);
		final List<StundenplanAufsichtsbereich> aufsichtsbereiche = DataStundenplanAufsichtsbereiche.getAufsichtsbereiche(conn, id);
		final List<StundenplanKalenderwochenzuordnung> kalenderwochenzuordnung = DataStundenplanKalenderwochenzuordnung.getKalenderwochenzuordnungen(conn, id);
		// Erstelle das Core-DTO-Objekt für die Response
		final Stundenplan daten = new Stundenplan();
		daten.id = stundenplan.ID;
		daten.idSchuljahresabschnitt = stundenplan.Schuljahresabschnitts_ID;
		daten.gueltigAb = stundenplan.Beginn;
		daten.gueltigBis = stundenplan.Ende;
		daten.bezeichnungStundenplan = stundenplan.Beschreibung;
		daten.wochenTypModell = stundenplan.WochentypModell;
		daten.zeitraster.addAll(zeitraster);
		daten.raeume.addAll(raeume);
		daten.schienen.addAll(schienen);
		daten.jahrgaenge.addAll(jahrgaenge);
		daten.pausenzeiten.addAll(pausenzeiten);
		daten.aufsichtsbereiche.addAll(aufsichtsbereiche);
		daten.kalenderwochenZuordnung.addAll(kalenderwochenzuordnung);
		return daten;
	}

	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einem Stundenplan mit der ID null ist unzulässig.");
		final Stundenplan stundenplan = DataStundenplan.getStundenplan(conn, id);
		if (stundenplan == null)
			return OperationError.NOT_FOUND.getResponse("Es wurde kein Stundenplan mit der ID %d gefunden.".formatted(id));
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(stundenplan).build();
	}


	private static final Map<String, DataBasicMapper<DTOStundenplan>> patchMappings = Map.ofEntries(
		Map.entry("id", (dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID))
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("idSchuljahresabschnitt", (dto, value, map) -> { throw OperationError.BAD_REQUEST.exception(); }),
		Map.entry("gueltigAb", (dto, value, map) -> dto.Beginn = JSONMapper.convertToString(value, false, false, null)),
		Map.entry("gueltigBis", (dto, value, map) -> dto.Ende = JSONMapper.convertToString(value, false, false, null)),
		Map.entry("bezeichnungStundenplan", (dto, value, map) -> dto.Beschreibung = JSONMapper.convertToString(value, false, false, 1000)),
		Map.entry("wochenTypModell", (dto, value, map) -> dto.WochentypModell = JSONMapper.convertToInteger(value, false))
	);

	@Override
	public Response patch(final Long id, final InputStream is) {
		return super.patchBasic(id, is, DTOStundenplan.class, patchMappings);
	}

}
