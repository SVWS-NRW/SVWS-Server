package de.svws_nrw.data.betriebe;

import de.svws_nrw.core.data.betrieb.Beschaeftigungsart;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOBeschaeftigungsart;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Beschaeftigungsart}.
 */
public final class DataBeschaeftigungsarten extends DataManagerRevised<Long, DTOBeschaeftigungsart, Beschaeftigungsart> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Beschaeftigungsart}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataBeschaeftigungsarten(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation("bezeichnung");
	}

	@Override
	protected void initDTO(final DTOBeschaeftigungsart dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.ID = newID;
	}

	@Override
	protected long getLongId(final DTOBeschaeftigungsart dto) {
		return dto.ID;
	}

	@Override
	public Beschaeftigungsart getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für die Beschäftigungsart darf nicht null sein.");

		final DTOBeschaeftigungsart dto = conn.queryByKey(DTOBeschaeftigungsart.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Beschäftigungsart mit der ID %d gefunden.".formatted(id));

		return map(dto);
	}

	@Override
	public List<Beschaeftigungsart> getAll() {
		final List<DTOBeschaeftigungsart> result = this.conn.queryAll(DTOBeschaeftigungsart.class);
		return result.stream().map(this::map).toList();
	}

	@Override
	protected Beschaeftigungsart map(final DTOBeschaeftigungsart dto) {
		final Beschaeftigungsart beschaeftigungsart = new Beschaeftigungsart();
		beschaeftigungsart.id = dto.ID;
		beschaeftigungsart.bezeichnung = dto.Bezeichnung;
		beschaeftigungsart.sortierung = (dto.Sortierung == null) ? 32000 : dto.Sortierung;
		beschaeftigungsart.istSichtbar = (dto.Sichtbar == null) || dto.Sichtbar;
		beschaeftigungsart.istAenderbar = (dto.Aenderbar == null) || dto.Aenderbar;
		return beschaeftigungsart;
	}

	@Override
	protected void mapAttribute(final DTOBeschaeftigungsart dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, name);
				if (!Objects.equals(dto.ID, id))
					throw new ApiOperationException(Status.BAD_REQUEST, "IdPatch %d ist ungleich dtoId %d".formatted(id, dto.ID));
			}
			case "bezeichnung" -> dto.Bezeichnung = JSONMapper.convertToString(
					value, false, false, Schema.tab_K_BeschaeftigungsArt.col_Bezeichnung.datenlaenge(), name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, true, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, true, name);
			case "istAenderbar" -> dto.Aenderbar = JSONMapper.convertToBoolean(value, true, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}
}
