package de.svws_nrw.data.uv;

import java.util.Map;

import de.svws_nrw.core.data.uv.UvRaum;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvRaum;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link UvRaum}.
 */
public final class DataUvRaeume extends DataManagerRevised<Long, DTOUvRaum, UvRaum> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link UvRaum}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataUvRaeume(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("id");
		super.setAttributesRequiredOnCreation("kuerzel", "gueltigVon");
	}

	/**
	 * Gibt die Daten eines {@link UvRaum} zur ID zurück.
	 *
	 * @param id die ID des {@link UvRaum}
	 * @return die Daten des {@link UvRaum} zur ID
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public UvRaum getById(final Long id) throws ApiOperationException {
		final DTOUvRaum dto = getDTO(id);
		return map(dto);
	}

	/**
	 * Ermittelt das entsprechende {@link DTOUvRaum}-Objekt zur angegebenen ID.
	 *
	 * @param id die ID des {@link DTOUvRaum}-Objekts
	 * @return das entsprechende {@link DTOUvRaum}-Objekt
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOUvRaum getDTO(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für den UV-Raum darf nicht null sein.");
		}
		final DTOUvRaum dto = conn.queryByKey(DTOUvRaum.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Raum zur ID " + id + " gefunden.");
		}
		return dto;
	}

	@Override
	protected void initDTO(final DTOUvRaum dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected UvRaum map(final DTOUvRaum dto) throws ApiOperationException {
		final UvRaum daten = new UvRaum();
		daten.id = dto.ID;
		daten.kuerzel = dto.Kuerzel;
		daten.gueltigVon = dto.GueltigVon;
		daten.gueltigBis = dto.GueltigBis;
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOUvRaum dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "kuerzel" -> dto.Kuerzel = JSONMapper.convertToString(value, false, false, Schema.tab_UV_Raeume.col_Kuerzel.datenlaenge(), name);
			case "gueltigAb" -> dto.GueltigVon = JSONMapper.convertToString(value, false, false, null, name);
			case "gueltigBis" -> dto.GueltigBis = JSONMapper.convertToString(value, true, false, null, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}
}
