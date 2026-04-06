package de.svws_nrw.data.uv;

import java.util.Map;

import de.svws_nrw.core.data.uv.UvZeitrasterEintrag;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvZeitrasterEintrag;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link UvZeitrasterEintrag}.
 */
public final class DataUvZeitrasterEintraege extends DataManagerRevised<Long, DTOUvZeitrasterEintrag, UvZeitrasterEintrag> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link UvZeitrasterEintrag}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataUvZeitrasterEintraege(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("id", "idZeitraster");
		super.setAttributesRequiredOnCreation("idZeitraster", "tag", "stunde", "beginn", "ende");
	}

	/**
	 * Gibt die Daten eines {@link UvZeitrasterEintrag}-Eintrags anhand der ID zurück.
	 *
	 * @param id   die ID des {@link UvZeitrasterEintrag}-Eintrags
	 * @return die Daten des {@link UvZeitrasterEintrag}-Eintrags
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public UvZeitrasterEintrag getById(final Long id) throws ApiOperationException {
		final DTOUvZeitrasterEintrag dto = getDTO(id);
		return map(dto);
	}

	/**
	 * Gibt das {@link DTOUvZeitrasterEintrag}-Objekt zur angegebenen ID zurück.
	 *
	 * @param id   die ID des DTOs
	 * @return das DTO
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOUvZeitrasterEintrag getDTO(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für den Zeitrastereintrag darf nicht null sein.");
		}
		final DTOUvZeitrasterEintrag dto = conn.queryByKey(DTOUvZeitrasterEintrag.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Zeitrastereintrag mit der ID " + id + " gefunden.");
		}
		return dto;
	}

	@Override
	protected void initDTO(final DTOUvZeitrasterEintrag dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected UvZeitrasterEintrag map(final DTOUvZeitrasterEintrag dto) throws ApiOperationException {
		final UvZeitrasterEintrag daten = new UvZeitrasterEintrag();
		daten.id = dto.ID;
		daten.idZeitraster = dto.Zeitraster_ID;
		daten.tag = dto.Tag;
		daten.stunde = dto.Stunde;
		daten.beginn = (dto.Beginn == null) ? 0 : dto.Beginn;
		daten.ende = (dto.Ende == null) ? 0 : dto.Ende;
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOUvZeitrasterEintrag dto, final String name, final Object value,
			final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "idZeitraster" -> dto.Zeitraster_ID = JSONMapper.convertToLong(value, false, name);
			case "tag" -> dto.Tag = JSONMapper.convertToInteger(value, false, name);
			case "stunde" -> dto.Stunde = JSONMapper.convertToInteger(value, false, name);
			case "beginn" -> dto.Beginn = JSONMapper.convertToInteger(value, false, name);
			case "ende" -> dto.Ende = JSONMapper.convertToInteger(value, false, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}
}
