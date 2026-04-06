package de.svws_nrw.data.uv;

import java.util.Map;

import de.svws_nrw.core.data.uv.UvZeitraster;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvZeitraster;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link UvZeitraster}.
 */
public final class DataUvZeitraster extends DataManagerRevised<Long, DTOUvZeitraster, UvZeitraster> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link UvZeitraster}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataUvZeitraster(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("id");
		super.setAttributesRequiredOnCreation("gueltigVon", "bezeichnung");
	}

	/**
	 * Gibt die Daten eines {@link UvZeitraster}-Eintrags anhand der ID zurück.
	 *
	 * @param id   die ID des {@link UvZeitraster}-Eintrags
	 * @return die Daten des {@link UvZeitraster}-Eintrags
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public UvZeitraster getById(final Long id) throws ApiOperationException {
		final DTOUvZeitraster dto = getDTO(id);
		return map(dto);
	}

	/**
	 * Gibt das {@link DTOUvZeitraster}-Objekt zur angegebenen ID zurück.
	 *
	 * @param id   die ID des DTOs
	 * @return das DTO
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOUvZeitraster getDTO(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für das Zeitraster darf nicht null sein.");
		}
		final DTOUvZeitraster dto = conn.queryByKey(DTOUvZeitraster.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Zeitraster mit der ID " + id + " gefunden.");
		}
		return dto;
	}

	@Override
	protected void initDTO(final DTOUvZeitraster dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected UvZeitraster map(final DTOUvZeitraster dto) throws ApiOperationException {
		final UvZeitraster daten = new UvZeitraster();
		daten.id = dto.ID;
		daten.gueltigVon = dto.GueltigVon;
		daten.gueltigBis = dto.GueltigBis;
		daten.bezeichnung = dto.Bezeichnung;
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOUvZeitraster dto, final String name, final Object value,
			final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "gueltigVon" -> dto.GueltigVon = JSONMapper.convertToString(value, false, false, null, name);
			case "gueltigBis" -> dto.GueltigBis = JSONMapper.convertToString(value, true, false, null, name);
			case "bezeichnung" -> dto.Bezeichnung = JSONMapper.convertToString(value, false, true, Schema.tab_UV_Zeitraster.col_Bezeichnung.datenlaenge(), name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}
}
