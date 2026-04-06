package de.svws_nrw.data.uv;

import java.util.Map;

import de.svws_nrw.core.data.uv.UvStundentafelFach;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvStundentafelFach;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link UvStundentafelFach}.
 */
public final class DataUvStundentafelnFaecher extends DataManagerRevised<Long, DTOUvStundentafelFach, UvStundentafelFach> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO
	 * {@link UvStundentafelFach}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataUvStundentafelnFaecher(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("id", "idStundentafel");
		super.setAttributesRequiredOnCreation("idStundentafel", "abschnitt", "idFach", "wochenstunden");
	}

	/**
	 * Gibt die Daten eines {@link UvStundentafelFach}-Eintrags zur ID zurück.
	 *
	 * @param id Die ID des {@link UvStundentafelFach}.
	 * @return die Daten des {@link UvStundentafelFach} zur ID.
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public UvStundentafelFach getById(final Long id) throws ApiOperationException {
		final DTOUvStundentafelFach dto = getDTO(id);
		return map(dto);
	}

	/**
	 * Ermittelt das entsprechende {@link DTOUvStundentafelFach} Objekt zur angegebenen ID.
	 *
	 * @param id ID des {@link DTOUvStundentafelFach} Objekts.
	 * @return Ein {@link DTOUvStundentafelFach} Objekt.
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOUvStundentafelFach getDTO(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für das UV-Stundentafel-Fach darf nicht null sein.");
		}
		final DTOUvStundentafelFach dto = conn.queryByKey(DTOUvStundentafelFach.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Kein UV-Stundentafel-Fach zur ID " + id + " gefunden.");
		}
		return dto;
	}

	@Override
	protected void initDTO(final DTOUvStundentafelFach dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected UvStundentafelFach map(final DTOUvStundentafelFach dto) throws ApiOperationException {
		final UvStundentafelFach daten = new UvStundentafelFach();
		daten.idStundentafel = dto.Stundentafel_ID;
		daten.abschnitt = dto.Abschnitt;
		daten.idFach = dto.Fach_ID;
		daten.wochenstunden = dto.Wochenstunden;
		daten.davonErgaenzungsstunden = dto.DavonErgaenzungsstunden;
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOUvStundentafelFach dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idStundentafel" -> dto.Stundentafel_ID = JSONMapper.convertToLong(value, false, name);
			case "abschnitt" ->	dto.Abschnitt = JSONMapper.convertToInteger(value, false, name);
			case "idFach" -> dto.Fach_ID = JSONMapper.convertToLong(value, false, name);
			case "wochenstunden" ->	dto.Wochenstunden = JSONMapper.convertToDouble(value, false, name);
			case "davonErgaenzungsstunden" -> dto.DavonErgaenzungsstunden = JSONMapper.convertToDouble(value, false, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}
}
