package de.svws_nrw.data.schueler;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.core.data.schueler.SchuelerLernplattform;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattform;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO {@link SchuelerLernplattform}.
 */
public final class DataSchuelerLernplattformen extends DataManagerRevised<Long[], DTOSchuelerLernplattform, SchuelerLernplattform> {

	private final Long idSchueler;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link SchuelerLernplattform}.
	 *
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idSchueler   die ID des Schülerdatensatzes auf dem die Datenbankoperationen ausgeführt werden
	 */
	public DataSchuelerLernplattformen(final DBEntityManager conn, final Long idSchueler) {
		super(conn);
		this.idSchueler = idSchueler;
		setAttributesNotPatchable("idSchueler", "idLernplattform");
		setAttributesRequiredOnCreation("idLernplattform");
	}


	@Override
	public SchuelerLernplattform map(final DTOSchuelerLernplattform dtoLernplattform) {
		final SchuelerLernplattform daten = new SchuelerLernplattform();
		daten.idSchueler = dtoLernplattform.SchuelerID;
		daten.idLernplattform = dtoLernplattform.LernplattformID;
		daten.einwilligungAbgefragt = dtoLernplattform.EinwilligungAbgefragt;
		daten.einwilligungNutzung = dtoLernplattform.EinwilligungNutzung;
		daten.einwilligungAudiokonferenz = dtoLernplattform.EinwilligungAudiokonferenz;
		daten.einwilligungVideokonferenz = dtoLernplattform.EinwilligungVideokonferenz;
		return daten;
	}

	@Override
	public List<SchuelerLernplattform> getAll() throws ApiOperationException {
		final List<DTOSchuelerLernplattform> lernplattformen = conn.queryAll(DTOSchuelerLernplattform.class);
		return lernplattformen.stream().map(this::map).toList();
	}

	@Override
	public List<SchuelerLernplattform> getList() throws ApiOperationException {
		final List<DTOSchuelerLernplattform> lernplattformen = conn.queryList(
				DTOSchuelerLernplattform.QUERY_BY_SCHUELERID, DTOSchuelerLernplattform.class, idSchueler);
		return lernplattformen.stream().map(this::map).toList();
	}

	@Override
	protected Long[] getID(final Map<String, Object> attributes) throws ApiOperationException {
		final Long idSchueler = JSONMapper.convertToLong(attributes.get("idSchueler"), false, "idSchueler");
		final Long idLernplattform = JSONMapper.convertToLong(attributes.get("idLernplattform"), false, "idLernplattform");
		return new Long[]{idSchueler, idLernplattform};
	}

	@Override
	protected void initDTO(final DTOSchuelerLernplattform dto, final Long[] idArray, final Map<String, Object> initAttributes) {
		dto.SchuelerID = this.idSchueler;
		dto.LernplattformID = idArray[1];
		dto.EinwilligungAbgefragt = false;
		dto.EinwilligungNutzung = false;
		dto.EinwilligungAudiokonferenz = false;
		dto.EinwilligungVideokonferenz = false;
	}

	@Override
	public void checkBeforeCreation(final Long[] newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		final Long idSchueler = JSONMapper.convertToLong(initAttributes.get("idSchueler"), false, "idSchueler");
		final Long idLernplattform = JSONMapper.convertToLong(initAttributes.get("idLernplattform"), false, "idLernplattform");
		final DTOSchuelerLernplattform existingEntry = conn.queryByKey(DTOSchuelerLernplattform.class, idSchueler, idLernplattform);
		if (existingEntry != null)
			throw new ApiOperationException(
					Status.BAD_REQUEST,
					"Es existiert bereits eine Einwilligung für die Kombination aus Schueler-ID %d und Lernplattform-ID %d.".formatted(idSchueler,
							idLernplattform)
			);
	}

	@Override
	protected void mapAttribute(final DTOSchuelerLernplattform dto, final String name, final Object value,
			final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "idSchueler" -> {
				final Long idSchueler = JSONMapper.convertToLong(value, false, "idSchueler");
				if (!Objects.equals(dto.SchuelerID, idSchueler))
					throw new ApiOperationException(Status.BAD_REQUEST, "IdPatch %d ist ungleich dtoId %d".formatted(idSchueler, dto.SchuelerID));
			}
			case "idLernplattform" -> {
				final Long idLernplattform = JSONMapper.convertToLong(value, false, "idLernplattform");
				if (!Objects.equals(dto.LernplattformID, idLernplattform))
					throw new ApiOperationException(Status.BAD_REQUEST, "IdPatch %d ist ungleich dtoId %d".formatted(idLernplattform, dto.LernplattformID));
			}
			case "einwilligungAbgefragt" -> dto.EinwilligungAbgefragt = JSONMapper.convertToBoolean(value, false, "einwilligungAbgefragt");
			case "einwilligungNutzung" -> dto.EinwilligungNutzung = JSONMapper.convertToBoolean(value, false, "einwilligungNutzung");
			case "einwilligungAudiokonferenz" -> dto.EinwilligungAudiokonferenz = JSONMapper.convertToBoolean(value, false, "einwilligungAudiokonferenz");
			case "einwilligungVideokonferenz" -> dto.EinwilligungVideokonferenz = JSONMapper.convertToBoolean(value, false, "einwilligungVideokonferenz");
			default -> throw new ApiOperationException(Status.BAD_REQUEST,  "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	@Override
	public SchuelerLernplattform getById(final Long[] id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Lernplattform mit der ID null ist unzulässig.");
		final DTOSchuelerLernplattform lernplattform = conn.queryByKey(DTOSchuelerLernplattform.class, id[0], id[1]);
		if (lernplattform == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Eine Lernplattform mit SchuelerID %d und der LernplattformID %d wurde nicht gefunden.".formatted(id[0], id[1]));
		return map(lernplattform);
	}

	@Override
	public DTOSchuelerLernplattform getDatabaseDTOByID(final Long[] id) {
		return conn.queryByKey(DTOSchuelerLernplattform.class, id[0], id[1]);
	}
}
