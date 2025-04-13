package de.svws_nrw.data.lehrer;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.core.data.lehrer.LehrerLernplattform;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLernplattform;
import de.svws_nrw.db.dto.current.svws.auth.DTOCredentialsLernplattformen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO {@link LehrerLernplattform}.
 */
public final class DataLehrerLernplattformen extends DataManagerRevised<Long[], DTOLehrerLernplattform, LehrerLernplattform> {

	private final Long idLehrer;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link LehrerLernplattform}.
	 *
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idLehrer     die ID des Lehrerdatensatzes auf dem die Datenbankoperationen ausgeführt werden
	 */
	public DataLehrerLernplattformen(final DBEntityManager conn, final Long idLehrer) {
		super(conn);
		this.idLehrer = idLehrer;
		setAttributesNotPatchable("idLehrer", "idLernplattform");
		setAttributesRequiredOnCreation("idLernplattform");
	}


	@Override
	public LehrerLernplattform map(final DTOLehrerLernplattform dtoLernplattform) {
		final LehrerLernplattform daten = new LehrerLernplattform();
		daten.idLehrer = dtoLernplattform.LehrerID;
		daten.idLernplattform = dtoLernplattform.LernplattformID;
		daten.einwilligungAbgefragt = dtoLernplattform.EinwilligungAbgefragt;
		daten.einwilligungNutzung = dtoLernplattform.EinwilligungNutzung;
		daten.einwilligungAudiokonferenz = dtoLernplattform.EinwilligungAudiokonferenz;
		daten.einwilligungVideokonferenz = dtoLernplattform.EinwilligungVideokonferenz;
		return daten;
	}

	/**
	 * Konvertiert ein DTOLernplattformen-Objekt in ein Lernplattform-Objekt und setzt den Benutzernamen und das Initialkennwort.
	 *
	 * @param dtoLernplattform Das DTOLernplattformen-Objekt, das konvertiert werden soll.
	 * @param benutzername     Der Benutzername zu der Lernplattform, das gesetzt werden sollen.
	 * @param initialkennwort  Das Initialkennwort zu der Lernplattform, das gesetzt werden sollen.
	 *
	 * @return Ein Lernplattform-Objekt, das aus dem DTOLernplattformen-Objekt konvertiert und mit den der Benutzername und das Initialkennwort gesetzt wurde.
	 */
	public LehrerLernplattform map(final DTOLehrerLernplattform dtoLernplattform, final String benutzername, final String initialkennwort) {
		final LehrerLernplattform daten = map(dtoLernplattform);
		daten.benutzername = benutzername;
		daten.initialKennwort = initialkennwort;
		return daten;
	}

	@Override
	public List<LehrerLernplattform> getAll() throws ApiOperationException {
		final List<DTOLehrerLernplattform> lernplattformen = conn.queryAll(DTOLehrerLernplattform.class);
		return lernplattformen.stream().map(this::map).toList();
	}

	@Override
	public List<LehrerLernplattform> getList() {
		final List<DTOLehrerLernplattform> dtos = conn.queryList(DTOLehrerLernplattform.QUERY_BY_LEHRERID, DTOLehrerLernplattform.class, idLehrer);
		return dtos.stream().map(dto -> {
			String benutzername = null;
			String initialKennwort = null;
			if (dto.CredentialID != null) {
				final DTOCredentialsLernplattformen credentials = conn.queryByKey(DTOCredentialsLernplattformen.class, dto.CredentialID);
				if (credentials != null) {
					benutzername = credentials.Benutzername;
					initialKennwort = credentials.Initialkennwort;
				}
			}
			return map(dto, benutzername, initialKennwort);
		}).toList();
	}

	@Override
	protected Long[] getID(final Map<String, Object> attributes) throws ApiOperationException {
		final Long idLehrer = JSONMapper.convertToLong(attributes.get("idLehrer"), false, "idLehrer");
		final Long idLernplattform = JSONMapper.convertToLong(attributes.get("idLernplattform"), false, "idLernplattform");
		return new Long[]{idLehrer, idLernplattform};
	}

	@Override
	protected void initDTO(final DTOLehrerLernplattform dto, final Long[] idArray, final Map<String, Object> initAttributes) {
		dto.LehrerID = this.idLehrer;
		dto.LernplattformID = idArray[1];
		dto.EinwilligungAbgefragt = false;
		dto.EinwilligungNutzung = false;
		dto.EinwilligungAudiokonferenz = false;
		dto.EinwilligungVideokonferenz = false;
	}

	@Override
	public void checkBeforeCreation(final Long[] newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		final Long idLehrer = JSONMapper.convertToLong(initAttributes.get("idLehrer"), false, "idLehrer");
		final Long idLernplattform = JSONMapper.convertToLong(initAttributes.get("idLernplattform"), false, "idLernplattform");
		final DTOLehrerLernplattform existingEntry = conn.queryByKey(DTOLehrerLernplattform.class, idLehrer, idLernplattform);
		if (existingEntry != null)
			throw new ApiOperationException(
					Status.BAD_REQUEST,
					"Es existiert bereits eine Einwilligung für die Kombination aus Lehrer-ID %d und Lernplattform-ID %d.".formatted(idLehrer, idLernplattform));
	}

	@Override
	protected void mapAttribute(final DTOLehrerLernplattform dto, final String name, final Object value,
			final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "idLehrer" -> {
				final Long idLehrer = JSONMapper.convertToLong(value, false, "idLehrer");
				if (!Objects.equals(dto.LehrerID, idLehrer))
					throw new ApiOperationException(Status.BAD_REQUEST, "IdPatch %d ist ungleich dtoId %d".formatted(idLehrer, dto.LehrerID));
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
	public LehrerLernplattform getById(final Long[] id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Lernplattform mit der ID null ist unzulässig.");
		final DTOLehrerLernplattform lernplattform = conn.queryByKey(DTOLehrerLernplattform.class, id[0], id[1]);
		if (lernplattform == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Eine Lernplattform mit LehrerID %d und der LernplattformID %d wurde nicht gefunden.".formatted(id[0], id[1]));
		return map(lernplattform);
	}

	@Override
	public DTOLehrerLernplattform getDatabaseDTOByID(final Long[] id) {
		return conn.queryByKey(DTOLehrerLernplattform.class, id[0], id[1]);
	}

}
