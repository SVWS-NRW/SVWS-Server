package de.svws_nrw.data.schueler;

import de.svws_nrw.core.data.schueler.SchuelerEinwilligung;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerDatenschutz;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link SchuelerEinwilligung}.
 */
public final class DataSchuelerEinwilligungen extends DataManagerRevised<Long[], DTOSchuelerDatenschutz, SchuelerEinwilligung> {

	private final Long idSchueler;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link SchuelerEinwilligung}.
	 *
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idSchueler   die ID des Schülerdatensatzes auf dem die Datenbankoperationen ausgeführt werden
	 */
	public DataSchuelerEinwilligungen(final DBEntityManager conn, final Long idSchueler) {
		super(conn);
		this.idSchueler = idSchueler;
		setAttributesNotPatchable("idSchueler", "idEinwilligungsart");
		setAttributesRequiredOnCreation("idEinwilligungsart");
	}


	@Override
	public SchuelerEinwilligung map(final DTOSchuelerDatenschutz dtoEinwilligung) {
		final SchuelerEinwilligung daten = new SchuelerEinwilligung();
		daten.idSchueler = dtoEinwilligung.Schueler_ID;
		daten.idEinwilligungsart = dtoEinwilligung.Datenschutz_ID;
		daten.status = dtoEinwilligung.Status;
		daten.abgefragt = dtoEinwilligung.Abgefragt;
		return daten;
	}

	@Override
	public List<SchuelerEinwilligung> getAll() throws ApiOperationException {
		final List<DTOSchuelerDatenschutz> einwilligungen = conn.queryAll(DTOSchuelerDatenschutz.class);
		return einwilligungen.stream().map(this::map).toList();
	}

	/**
	 * Liefert alle Einwilligungen für die angegebenen Schüler.
	 *
	 * @param ids Liste der Schüler IDs, deren Einwilligungen abgerufen werden sollen.
	 *
	 * @return Alle Einwilligungen der angegebenen Schüler.
	 */
	public List<SchuelerEinwilligung> getListBySchuelerIds(final List<Long> ids) {
		if (ids.isEmpty())
			return new ArrayList<>();
		final List<DTOSchuelerDatenschutz> einwilligungen = conn.queryList(DTOSchuelerDatenschutz.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerDatenschutz.class, ids);
		return einwilligungen.stream().map(this::map).toList();
	}

	/**
	 * Liefert eine Response mit einer Liste mit {@link SchuelerEinwilligung} Objekten zu den übergebenen IDs.
	 *
	 * @param ids IDs der Schüler
	 *
	 * @return die Response mit der Liste von {@link SchuelerEinwilligung} Objekten
	 */
	public Response getListBySchuelerIdsAsResponse(final List<Long> ids) {
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(getListBySchuelerIds(ids)).build();
	}

	@Override
	public List<SchuelerEinwilligung> getList() throws ApiOperationException {
		final List<DTOSchuelerDatenschutz> einwilligungen = conn.queryList(
				DTOSchuelerDatenschutz.QUERY_BY_SCHUELER_ID, DTOSchuelerDatenschutz.class, idSchueler);
		return einwilligungen.stream().map(this::map).toList();
	}

	@Override
	protected Long[] getID(final Map<String, Object> attributes) throws ApiOperationException {
		final Long tmpIdSchueler = JSONMapper.convertToLong(attributes.get("idSchueler"), false, "idSchueler");
		final Long tmpIdEinwilligungsart = JSONMapper.convertToLong(attributes.get("idEinwilligungsart"), false, "idEinwilligungsart");
		return new Long[]{ tmpIdSchueler, tmpIdEinwilligungsart };
	}

	@Override
	protected void initDTO(final DTOSchuelerDatenschutz dto, final Long[] idArray, final Map<String, Object> initAttributes) {
		dto.Schueler_ID = this.idSchueler;
		dto.Datenschutz_ID = idArray[1];
		dto.Status = false;
		dto.Abgefragt = false;
	}

	@Override
	public void checkBeforeCreation(final Long[] newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		final Long tmpIdSchueler = JSONMapper.convertToLong(initAttributes.get("idSchueler"), false, "idSchueler");
		final Long tmpIdEinwilligungsart = JSONMapper.convertToLong(initAttributes.get("idEinwilligungsart"), false, "idEinwilligungsart");
		final DTOSchuelerDatenschutz existingEntry = conn.queryByKey(DTOSchuelerDatenschutz.class, tmpIdSchueler, tmpIdEinwilligungsart);
		if (existingEntry != null)
			throw new ApiOperationException(
					Status.BAD_REQUEST,
					"Es existiert bereits eine Einwilligung für die Kombination aus Schüler-ID %d und Einwilligungsart-ID %d.".formatted(tmpIdSchueler, tmpIdEinwilligungsart)
			);
	}

	@Override
	protected void mapAttribute(final DTOSchuelerDatenschutz dto, final String name, final Object value,
			final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "idSchueler" -> {
				final Long tmpId = JSONMapper.convertToLong(value, false, "idSchueler");
				if (!Objects.equals(dto.Schueler_ID, tmpId))
					throw new ApiOperationException(Status.BAD_REQUEST, "IdPatch %d ist ungleich dtoId %d".formatted(tmpId, dto.Schueler_ID));
			}
			case "idEinwilligungsart" -> {
				final Long idEinwilligungsart = JSONMapper.convertToLong(value, false, "idEinwilligungsart");
				if (!Objects.equals(dto.Datenschutz_ID, idEinwilligungsart))
					throw new ApiOperationException(Status.BAD_REQUEST, "IdPatch %d ist ungleich dtoId %d".formatted(idEinwilligungsart, dto.Datenschutz_ID));
			}
			case "status" -> dto.Status = JSONMapper.convertToBoolean(value, false, "status");
			case "abgefragt" -> dto.Abgefragt = JSONMapper.convertToBoolean(value, false, "abgefragt");
			default -> throw new ApiOperationException(Status.BAD_REQUEST,  "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	@Override
	public SchuelerEinwilligung getById(final Long[] id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Einwilligungsart mit der ID null ist unzulässig.");
		final DTOSchuelerDatenschutz einwilligung = conn.queryByKey(DTOSchuelerDatenschutz.class, id[0], id[1]);
		if (einwilligung == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Eine Einwilligung mit SchuelerID %d und der EinwilligungsartID %d wurde nicht gefunden.".formatted(id[0], id[1]));
		return map(einwilligung);
	}

	/**
	 * Bestimmt die IDs der Einwilligungen, welche zu der übergebenen ID der Einwilligungsart gehören.
	 *
	 * @param id     die ID der Einwilligungsart
	 *
	 * @return die List von Einwilligungs IDs, welche der entsprechenden Einwilligungsart zugeordnet sind
	 */
	public List<Long> getIDsByEinwilligungsartId(final Long id) {
		return conn.queryList(DTOSchuelerDatenschutz.QUERY_BY_DATENSCHUTZ_ID, DTOSchuelerDatenschutz.class, id).stream().map(v -> v.Schueler_ID).toList();
	}

	@Override
	public DTOSchuelerDatenschutz getDatabaseDTOByID(final Long[] id) {
		return conn.queryByKey(DTOSchuelerDatenschutz.class, id[0], id[1]);
	}

}
