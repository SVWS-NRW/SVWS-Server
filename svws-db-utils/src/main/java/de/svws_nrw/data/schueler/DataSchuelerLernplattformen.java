package de.svws_nrw.data.schueler;

import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.schueler.SchuelerLernplattform;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattform;
import de.svws_nrw.db.dto.current.svws.auth.DTOCredentialsLernplattformen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO {@link SchuelerLernplattform}.
 */
public final class DataSchuelerLernplattformen extends DataManagerRevised<Long[], DTOSchuelerLernplattform, SchuelerLernplattform> {

	private static final String ID_SCHUELER = "idSchueler";
	private static final String ID_LERNPLATTFORM = "idLernplattform";


	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link SchuelerLernplattform}.
	 *
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerLernplattformen(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable(ID_SCHUELER, ID_LERNPLATTFORM);
		setAttributesRequiredOnCreation(ID_SCHUELER, ID_LERNPLATTFORM);
	}

	@Override
	protected void initDTO(final DTOSchuelerLernplattform dto, final Long[] id, final Map<String, Object> initAttributes) {
		dto.SchuelerID = id[0];
		dto.LernplattformID = id[1];
	}

	@Override
	public SchuelerLernplattform map(final DTOSchuelerLernplattform dto) {
		final SchuelerLernplattform daten = new SchuelerLernplattform();
		daten.idSchueler = dto.SchuelerID;
		daten.idLernplattform = dto.LernplattformID;
		daten.einwilligungAbgefragt = Boolean.TRUE.equals(dto.EinwilligungAbgefragt);
		daten.einwilligungNutzung = Boolean.TRUE.equals(dto.EinwilligungNutzung);
		daten.einwilligungAudiokonferenz = Boolean.TRUE.equals(dto.EinwilligungAudiokonferenz);
		daten.einwilligungVideokonferenz = Boolean.TRUE.equals(dto.EinwilligungVideokonferenz);
		this.mapCredentials(dto.CredentialID, daten);
		return daten;
	}


	@Override
	public SchuelerLernplattform getById(final Long[] id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID der Lernplattform darf nicht null sein.");
		}
		final DTOSchuelerLernplattform lernplattform = conn.queryByKey(DTOSchuelerLernplattform.class, id[0], id[1]);
		if (lernplattform == null) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Eine Lernplattform mit SchuelerID %d und der LernplattformID %d wurde nicht gefunden.".formatted(id[0], id[1]));
		}
		return map(lernplattform);
	}

	/**
	 * Liefert die Liste der Lernplattformen eines Schülers
	 *
	 * @param idSchueler        id des Schülers
	 * @return Response			Liste von Lernplattformen des Schülers
	 */
	public Response getAllByIdSchueler(final Long idSchueler) {
		final List<SchuelerLernplattform> result = this.conn
				.queryList(DTOSchuelerLernplattform.QUERY_BY_SCHUELERID, DTOSchuelerLernplattform.class, idSchueler)
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
		final Long idLernplattform = JSONMapper.convertToLong(initAttributes.get(ID_LERNPLATTFORM), false, ID_LERNPLATTFORM);
		final DTOSchuelerLernplattform existingEntry = conn.queryByKey(DTOSchuelerLernplattform.class, idSchueler, idLernplattform);
		if (existingEntry != null) {
			throw new ApiOperationException(
					Status.BAD_REQUEST,
					"Es existiert bereits eine Einwilligung für die Kombination aus Schueler-ID %d und Lernplattform-ID %d.".formatted(idSchueler,
							idLernplattform)
			);
		}
	}

	@Override
	protected void mapAttribute(final DTOSchuelerLernplattform dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case ID_SCHUELER -> dto.SchuelerID = JSONMapper.convertToLong(value, false, ID_SCHUELER);
			case ID_LERNPLATTFORM -> dto.LernplattformID = JSONMapper.convertToLong(value, false, ID_LERNPLATTFORM);
			case "einwilligungAbgefragt" -> dto.EinwilligungAbgefragt = JSONMapper.convertToBoolean(value, false, name);
			case "einwilligungNutzung" -> dto.EinwilligungNutzung = JSONMapper.convertToBoolean(value, false, name);
			case "einwilligungAudiokonferenz" -> dto.EinwilligungAudiokonferenz = JSONMapper.convertToBoolean(value, false, name);
			case "einwilligungVideokonferenz" -> dto.EinwilligungVideokonferenz = JSONMapper.convertToBoolean(value, false, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	@Override
	public DTOSchuelerLernplattform getDatabaseDTOByID(final Long[] id) {
		return conn.queryByKey(DTOSchuelerLernplattform.class, id[0], id[1]);
	}

	@Override
	protected Long[] getID(final Map<String, Object> attributes) throws ApiOperationException {
		final Long idSchueler = JSONMapper.convertToLong(attributes.get(ID_SCHUELER), false, ID_SCHUELER);
		final Long idLernplattform = JSONMapper.convertToLong(attributes.get(ID_LERNPLATTFORM), false, ID_LERNPLATTFORM);
		return new Long[] { idSchueler, idLernplattform };
	}

	private void mapCredentials(final Long id, final SchuelerLernplattform daten) {
		if (id == null) {
			return;
		}
		final DTOCredentialsLernplattformen credentials = conn.queryByKey(DTOCredentialsLernplattformen.class, id);
		if (credentials == null) {
			return;
		}
		daten.benutzername = credentials.Benutzername;
		daten.initialKennwort = credentials.Initialkennwort;
	}
}



