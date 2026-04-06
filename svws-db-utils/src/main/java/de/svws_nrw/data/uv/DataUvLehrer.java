package de.svws_nrw.data.uv;

import java.util.Map;

import de.svws_nrw.core.data.uv.UvLehrer;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvLehrer;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link UvLehrer}.
 */
public final class DataUvLehrer extends DataManagerRevised<Long, DTOUvLehrer, UvLehrer> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link UvLehrer}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataUvLehrer(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("id");
	}

	/**
	 * Gibt die Daten eines {@link UvLehrer} zur ID zurück.
	 *
	 * @param id die ID des {@link UvLehrer}
	 * @return die Daten des {@link UvLehrer} zur ID
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public UvLehrer getById(final Long id) throws ApiOperationException {
		final DTOUvLehrer dto = getDTO(id);
		return map(dto);
	}

	/**
	 * Ermittelt das entsprechende {@link DTOUvLehrer}-Objekt zur angegebenen ID.
	 *
	 * @param id die ID des {@link DTOUvLehrer}-Objekts
	 * @return das entsprechende {@link DTOUvLehrer}-Objekt
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOUvLehrer getDTO(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für den UV-Lehrer darf nicht null sein.");
		}
		final DTOUvLehrer dto = conn.queryByKey(DTOUvLehrer.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Lehrer zur ID " + id + " gefunden.");
		}
		return dto;
	}

	@Override
	protected void initDTO(final DTOUvLehrer dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected UvLehrer map(final DTOUvLehrer dto) throws ApiOperationException {
		final UvLehrer daten = new UvLehrer();
		daten.id = dto.ID;
		daten.idKLehrer = dto.K_Lehrer_ID;
		daten.kuerzel = dto.Kuerzel;
		daten.nachname = dto.Nachname;
		daten.vorname = dto.Vorname;
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOUvLehrer dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idKLehrer" -> dto.K_Lehrer_ID = JSONMapper.convertToLong(value, true, name);
			case "kuerzel" -> dto.Kuerzel = JSONMapper.convertToString(value, false, false, Schema.tab_UV_Lehrer.col_Kuerzel.datenlaenge(), name);
			case "nachname" -> dto.Nachname = JSONMapper.convertToString(value, false, false, Schema.tab_UV_Lehrer.col_Nachname.datenlaenge(), name);
			case "vorname" -> dto.Vorname = JSONMapper.convertToString(value, true, false, Schema.tab_UV_Lehrer.col_Vorname.datenlaenge(), name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}
}
