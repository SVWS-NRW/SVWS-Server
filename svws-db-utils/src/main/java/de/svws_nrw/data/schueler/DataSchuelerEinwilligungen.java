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

import java.util.List;
import java.util.Map;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link SchuelerEinwilligung}.
 */
public final class DataSchuelerEinwilligungen extends DataManagerRevised<Long[], DTOSchuelerDatenschutz, SchuelerEinwilligung> {

	private static final String ID_SCHUELER = "idSchueler";
	private static final String ID_EINWILLIGUNGSART = "idEinwilligungsart";

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link SchuelerEinwilligung}.
	 *
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerEinwilligungen(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable(ID_SCHUELER, ID_EINWILLIGUNGSART);
		setAttributesRequiredOnCreation(ID_SCHUELER, ID_EINWILLIGUNGSART);
	}

	@Override
	protected void initDTO(final DTOSchuelerDatenschutz dto, final Long[] id, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.Schueler_ID = id[0];
		dto.Datenschutz_ID = id[1];
	}

	@Override
	public SchuelerEinwilligung map(final DTOSchuelerDatenschutz dto) {
		final SchuelerEinwilligung daten = new SchuelerEinwilligung();
		daten.idSchueler = dto.Schueler_ID;
		daten.idEinwilligungsart = dto.Datenschutz_ID;
		daten.status = Boolean.TRUE.equals(dto.Status);
		daten.abgefragt = Boolean.TRUE.equals(dto.Abgefragt);
		return daten;
	}

	@Override
	public SchuelerEinwilligung getById(final Long[] id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID der Einwilligungsart darf nicht null sein.");
		}
		final DTOSchuelerDatenschutz einwilligung = conn.queryByKey(DTOSchuelerDatenschutz.class, id[0], id[1]);
		if (einwilligung == null) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Eine Einwilligung mit SchuelerID %d und der EinwilligungsartID %d wurde nicht gefunden.".formatted(id[0], id[1]));
		}
		return map(einwilligung);
	}

	/**
	 * Liefert die Liste der Einwilligungen eines Schülers
	 *
	 * @param idSchueler		id des Schülers
	 * @return Response			Liste von Einwilligungen des Schülers
	 */
	public Response getAllByIdSchueler(final Long idSchueler) {
		final List<SchuelerEinwilligung> result = this.conn
				.queryList(DTOSchuelerDatenschutz.QUERY_BY_SCHUELER_ID, DTOSchuelerDatenschutz.class, idSchueler)
				.stream()
				.map(this::map)
				.toList();

		return Response
				.status(Status.OK)
				.type(MediaType.APPLICATION_JSON)
				.entity(result)
				.build();
	}



	@Override
	public void checkBeforeCreation(final Long[] newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		final Long idSchueler = JSONMapper.convertToLong(initAttributes.get(ID_SCHUELER), false, ID_SCHUELER);
		final Long idEinwilligungsart = JSONMapper.convertToLong(initAttributes.get(ID_EINWILLIGUNGSART), false, ID_EINWILLIGUNGSART);
		final DTOSchuelerDatenschutz result = conn.queryByKey(DTOSchuelerDatenschutz.class, idSchueler, idEinwilligungsart);
		if (result != null) {
			throw new ApiOperationException(
					Status.BAD_REQUEST,
					"Es existiert bereits eine Einwilligung für die Kombination aus Schüler-ID %d und Einwilligungsart-ID %d."
							.formatted(idSchueler, idEinwilligungsart)
			);
		}
	}

	@Override
	protected void mapAttribute(final DTOSchuelerDatenschutz dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case ID_SCHUELER -> dto.Schueler_ID = JSONMapper.convertToLong(value, false, ID_SCHUELER);
			case ID_EINWILLIGUNGSART -> dto.Datenschutz_ID = JSONMapper.convertToLong(value, false, ID_EINWILLIGUNGSART);
			case "status" -> dto.Status = JSONMapper.convertToBoolean(value, false, "status");
			case "abgefragt" -> dto.Abgefragt = JSONMapper.convertToBoolean(value, false, "abgefragt");
			default -> throw new ApiOperationException(Status.BAD_REQUEST,  "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	@Override
	public DTOSchuelerDatenschutz getDatabaseDTOByID(final Long[] id) {
		return this.conn.queryByKey(DTOSchuelerDatenschutz.class, id[0], id[1]);
	}

	@Override
	protected Long[] getID(final Map<String, Object> attributes) throws ApiOperationException {
		final Long idSchueler = JSONMapper.convertToLong(attributes.get(ID_SCHUELER), false, ID_SCHUELER);
		final Long idEinwilligungsart = JSONMapper.convertToLong(attributes.get(ID_EINWILLIGUNGSART), false, ID_EINWILLIGUNGSART);
		return new Long[]{ idSchueler, idEinwilligungsart };
	}

}
