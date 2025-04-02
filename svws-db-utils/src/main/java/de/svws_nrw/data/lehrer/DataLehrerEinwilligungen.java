package de.svws_nrw.data.lehrer;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.core.data.schule.Einwilligungsart;
import de.svws_nrw.core.data.lehrer.LehrerEinwilligung;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerDatenschutz;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Einwilligungsart}.
 */
public final class DataLehrerEinwilligungen extends DataManagerRevised<Long[], DTOLehrerDatenschutz, LehrerEinwilligung> {

	private final Long idLehrer;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link LehrerEinwilligung}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idLehrer   die ID des Lehrerdatensatzes auf dem die Datenbankoperationen ausgeführt werden
	 */
	public DataLehrerEinwilligungen(final DBEntityManager conn, final Long idLehrer) {
		super(conn);
		this.idLehrer = idLehrer;
		setAttributesNotPatchable("idLehrer", "idEinwilligungsart");
		setAttributesRequiredOnCreation("idEinwilligungsart");
	}

	@Override
	public LehrerEinwilligung map(final DTOLehrerDatenschutz dtoEinwilligung) {
		final LehrerEinwilligung daten = new LehrerEinwilligung();
		daten.idLehrer = dtoEinwilligung.LehrerID;
		daten.idEinwilligungsart = dtoEinwilligung.DatenschutzID;
		daten.istZugestimmt = dtoEinwilligung.Status;
		daten.istAbgefragt = dtoEinwilligung.Abgefragt;
		return daten;
	}

	@Override
	public List<LehrerEinwilligung> getAll() throws ApiOperationException {
		final List<DTOLehrerDatenschutz> einwilligungen = conn.queryAll(DTOLehrerDatenschutz.class);
		return einwilligungen.stream().map(this::map).toList();
	}

	@Override
	public List<LehrerEinwilligung> getList() throws ApiOperationException {
		final List<DTOLehrerDatenschutz> einwilligungen = conn.queryList(
				DTOLehrerDatenschutz.QUERY_BY_LEHRERID, DTOLehrerDatenschutz.class, idLehrer);
		return einwilligungen.stream().map(this::map).toList();
	}

	@Override
	protected Long[] getID(final Map<String, Object> attributes) throws ApiOperationException {
		final Long idLehrer = JSONMapper.convertToLong(attributes.get("idLehrer"), false, "idLehrer");
		final Long idEinwilligungsart = JSONMapper.convertToLong(attributes.get("idEinwilligungsart"), false, "idEinwilligungsart");
		return new Long[]{idLehrer, idEinwilligungsart};
	}

	@Override
	protected void initDTO(final DTOLehrerDatenschutz dto, final Long[] ids, final Map<String, Object> initAttributes) {
		dto.LehrerID = this.idLehrer;
		dto.DatenschutzID = ids[1];
		dto.Status = false;
		dto.Abgefragt = false;
	}

	@Override
	public void checkBeforeCreation(final Long[] newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		final Long idLehrer = JSONMapper.convertToLong(initAttributes.get("idLehrer"), false, "idLehrer");
		final Long idEinwilligungsart = JSONMapper.convertToLong(initAttributes.get("idEinwilligungsart"), false, "idEinwilligungsart");
		final DTOLehrerDatenschutz existingEntry = conn.queryByKey(DTOLehrerDatenschutz.class, idLehrer, idEinwilligungsart);
		if (existingEntry != null)
			throw new ApiOperationException(
					Status.NOT_FOUND,
					"Es existiert bereits eine Einwilligung für die Kombination aus Lehrer-ID %d und Einwilligungsart-ID %d.".formatted(idLehrer,
							idEinwilligungsart)
			);
	}

	@Override
	protected void mapAttribute(final DTOLehrerDatenschutz dto, final String name, final Object value,
			final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "idLehrer" -> {
				final Long idLehrer = JSONMapper.convertToLong(value, false, "idLehrer");
				if (!Objects.equals(dto.LehrerID, idLehrer))
					throw new ApiOperationException(Status.BAD_REQUEST, "IdPatch %d ist ungleich dtoId %d".formatted(idLehrer, dto.LehrerID));
			}
			case "idEinwilligungsart" -> {
				final Long idEinwilligungsart = JSONMapper.convertToLong(value, false, "idEinwilligungsart");
				if (!Objects.equals(dto.DatenschutzID, idEinwilligungsart))
					throw new ApiOperationException(Status.BAD_REQUEST, "IdPatch %d ist ungleich dtoId %d".formatted(idEinwilligungsart, dto.DatenschutzID));
			}
			case "istZugestimmt" -> dto.Status = JSONMapper.convertToBoolean(value, false, "istZugestimmt");
			case "istAbgefragt" -> dto.Abgefragt = JSONMapper.convertToBoolean(value, false, "istAbgefragt");
			default -> throw new ApiOperationException(Status.BAD_REQUEST,  "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	@Override
	public LehrerEinwilligung getById(final Long[] id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Einwilligungsart mit der ID null ist unzulässig.");
		final DTOLehrerDatenschutz einwilligung = conn.queryByKey(DTOLehrerDatenschutz.class, id[0], id[1]);
		if (einwilligung == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Eine Einwilligung mit LehrerID %d und der EinwilligungsartID %d wurde nicht gefunden.".formatted(id[0], id[1]));
		return map(einwilligung);
	}

	/**
	 * Bestimmt die IDs der Einwilligungen, welche zu der übergebenen ID der Einwilligungsart gehören.
	 *
	 * @param id     die ID der Einwilligungsart
	 *
	 * @return       die List von Einwilligung-IDs, welche der entsprechenden Einwilligungsart zugeordnet sind
	 */
	public List<Long> getIDsByEinwilligungsartId(final Long id) {
		return conn.queryList(DTOLehrerDatenschutz.QUERY_BY_DATENSCHUTZID, DTOLehrerDatenschutz.class, id).stream().map(v -> v.LehrerID).toList();
	}

	@Override
	public DTOLehrerDatenschutz getDatabaseDTOByID(final Long[] id) {
		return conn.queryByKey(DTOLehrerDatenschutz.class, id[0], id[1]);
	}
}
