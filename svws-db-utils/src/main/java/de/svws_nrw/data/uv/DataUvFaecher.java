package de.svws_nrw.data.uv;

import java.util.Map;

import de.svws_nrw.core.data.uv.UvFach;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvFach;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link UvFach}.
 */
public final class DataUvFaecher extends DataManagerRevised<Long, DTOUvFach, UvFach> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO
	 * {@link UvFach}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataUvFaecher(final DBEntityManager conn) {
		super(conn);
		super.setAttributesRequiredOnCreation("idFach", "gueltigVon");
		super.setAttributesNotPatchable("idFach");
	}

	/**
	 * Gibt die Daten eines UV-Fachs zur ID zurück.
	 *
	 * @param id Die ID des Eintrags.
	 * @return die Daten des Eintrags zur ID.
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public UvFach getById(final Long id) throws ApiOperationException {
		final DTOUvFach dto = getDTO(id);
		return map(dto);
	}

	/**
	 * Ermittelt das entsprechende DTOUvFach-Objekt zur angegebenen ID.
	 *
	 * @param id ID des Objekts.
	 * @return Ein DTOUvFach-Objekt.
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOUvFach getDTO(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die ID für das UV-Fach darf nicht null sein.");
		}
		final DTOUvFach dto = conn.queryByKey(DTOUvFach.class, id);
		if (dto == null) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Kein UV-Fach mit der ID " + id + " gefunden.");
		}
		return dto;
	}

	@Override
	protected void initDTO(final DTOUvFach dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected UvFach map(final DTOUvFach dto) throws ApiOperationException {
		final UvFach daten = new UvFach();
		daten.id = dto.ID;
		daten.idFach = dto.Fach_ID;
		daten.gueltigVon = dto.GueltigVon;
		daten.gueltigBis = dto.GueltigBis;
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOUvFach dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idFach" -> dto.Fach_ID = JSONMapper.convertToLong(value, false, name);
			case "gueltigAb" -> dto.GueltigVon = JSONMapper.convertToString(value, false, false, null, name);
			case "gueltigBis" -> dto.GueltigBis = JSONMapper.convertToString(value, true, false, null, name);
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST,
					"Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}
}
