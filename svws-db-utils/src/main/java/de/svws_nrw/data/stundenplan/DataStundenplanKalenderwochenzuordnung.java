package de.svws_nrw.data.stundenplan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.stundenplan.StundenplanKalenderwochenzuordnung;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanKalenderwochenZuordnung;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Der Daten-Manager für die Kalenderwochenzuordnung eines Stundenplans:
 * {@link DTOStundenplanKalenderwochenZuordnung} und {@link StundenplanKalenderwochenzuordnung}.
 */
public final class DataStundenplanKalenderwochenzuordnung
		extends DataManagerRevised<Long, DTOStundenplanKalenderwochenZuordnung, StundenplanKalenderwochenzuordnung> {

	private final Long stundenplanID;

	/**
	 * Erstellt einen neuen Manager.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Kalenderwochen-Zuordnungen abgefragt werden
	 *
	 * @throws ApiOperationException   falls die Stundenplan-ID ungültig ist
	 */
	public DataStundenplanKalenderwochenzuordnung(final DBEntityManager conn, final Long stundenplanID) throws ApiOperationException {
		super(conn);
		setAttributesRequiredOnCreation("jahr", "kw", "wochentyp");
		this.stundenplanID = stundenplanID;
		// Prüfe ggf. ob der Stundenplan existiert
		if (stundenplanID != null)
			DataStundenplan.getDTOStundenplan(conn, stundenplanID);
	}

	@Override
	protected Long getID(final Map<String, Object> attributes) {
		return (Long) attributes.get("id");
	}


	@Override
	protected void initDTO(final DTOStundenplanKalenderwochenZuordnung dto, final Long newId, final Map<String, Object> initAttributes)
			throws ApiOperationException {
		dto.ID = newId;
		dto.Stundenplan_ID = this.stundenplanID;
	}


	@Override
	public StundenplanKalenderwochenzuordnung map(final DTOStundenplanKalenderwochenZuordnung dto) {
		final StundenplanKalenderwochenzuordnung daten = new StundenplanKalenderwochenzuordnung();
		daten.id = dto.ID;
		daten.jahr = dto.Jahr;
		daten.kw = dto.KW;
		daten.wochentyp = dto.Wochentyp;
		return daten;
	}


	@Override
	protected void mapAttribute(final DTOStundenplanKalenderwochenZuordnung dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id.longValue() != dto.ID))
					throw new ApiOperationException(Status.BAD_REQUEST);
			}
			case "jahr" -> {
				dto.Jahr = JSONMapper.convertToInteger(value, false);
				if (DateUtils.gibIstJahrUngueltig(dto.Jahr))
					throw new ApiOperationException(Status.BAD_REQUEST);
			}
			case "kw" -> dto.KW = JSONMapper.convertToInteger(value, false);
			case "wochentyp" -> dto.Wochentyp = JSONMapper.convertToInteger(value, false);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten ein unbekanntes Attribut.");
		}
	}


	/**
	 * Gibt die Pausenzeiten des Stundenplans zurück.
	 *
	 * @return die Liste der Pausenzeiten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	@Override
	public List<StundenplanKalenderwochenzuordnung> getList() throws ApiOperationException {
		final List<DTOStundenplanKalenderwochenZuordnung> zuordnungen = conn.queryList(DTOStundenplanKalenderwochenZuordnung.QUERY_BY_STUNDENPLAN_ID,
				DTOStundenplanKalenderwochenZuordnung.class, stundenplanID);
		final ArrayList<StundenplanKalenderwochenzuordnung> daten = new ArrayList<>();
		for (final DTOStundenplanKalenderwochenZuordnung z : zuordnungen)
			daten.add(map(z));
		return daten;
	}


	@Override
	public StundenplanKalenderwochenzuordnung getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Kalenderwochen-Zuordnung mit der ID null ist unzulässig.");
		final DTOStundenplanKalenderwochenZuordnung zuordung = conn.queryByKey(DTOStundenplanKalenderwochenZuordnung.class, id);
		if (zuordung == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Kalenderwochen-Zuordnung mit der ID %d gefunden.".formatted(id));
		return map(zuordung);
	}


	/**
	 * Prüft vor dem Löschen von Pausenzeiten, ob diese alle zu dem Stundenplan gehören.
	 *
	 * @param dtos    die zu löschenden DTOs
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	@Override
	public void checkBeforeDeletion(final List<DTOStundenplanKalenderwochenZuordnung> dtos) throws ApiOperationException {
		for (final DTOStundenplanKalenderwochenZuordnung dto : dtos)
			if (dto.Stundenplan_ID != this.stundenplanID)
				throw new ApiOperationException(Status.BAD_REQUEST, "Mindestens eine Kalenderwochenzuordnung gehört nicht zu dem angegebenen Stundenplan.");
	}

}
