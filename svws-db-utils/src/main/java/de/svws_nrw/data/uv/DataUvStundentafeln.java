package de.svws_nrw.data.uv;

import java.util.Map;

import de.svws_nrw.core.data.uv.UvStundentafel;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausuren;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvStundentafel;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link UvStundentafel}.
 */
public final class DataUvStundentafeln extends DataManagerRevised<Long, DTOUvStundentafel, UvStundentafel> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO
	 * {@link UvStundentafel}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataUvStundentafeln(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("id", "jahrgangId");
		super.setAttributesRequiredOnCreation("jahrgangId", "gueltigVon");
	}

	/**
	 * Gibt die Daten einer {@link UvStundentafel} zu deren ID zurück.
	 *
	 * @param id Die ID der {@link UvStundentafel}.
	 * @return die Daten der {@link UvStundentafel} zur ID.
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public UvStundentafel getById(final Long id) throws ApiOperationException {
		final DTOUvStundentafel dto = getDTO(id);
		return map(dto);
	}

	/**
	 * Die Methode ermittelt das entsprechende {@link DTOUvStundentafel} Objekt zur angegebenen ID.
	 *
	 * @param id ID des {@link DTOUvStundentafel} Objekts.
	 * @return Ein {@link DTOUvStundentafel} Objekt.
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOUvStundentafel getDTO(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für die UV-Stundentafel darf nicht null sein.");
		}
		final DTOUvStundentafel dto = conn.queryByKey(DTOUvStundentafel.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Stundentafel zur ID " + id + " gefunden.");
		}
		return dto;
	}

	@Override
	protected void initDTO(final DTOUvStundentafel dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected UvStundentafel map(final DTOUvStundentafel dto) throws ApiOperationException {
		final UvStundentafel daten = new UvStundentafel();
		daten.id = dto.ID;
		daten.jahrgangId = dto.Jahrgang_ID;
		daten.gueltigVon = dto.GueltigVon;
		daten.gueltigBis = dto.GueltigBis;
		daten.bezeichnung = dto.Bezeichnung;
		daten.beschreibung = dto.Beschreibung;
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOUvStundentafel dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "jahrgangId" -> dto.Jahrgang_ID = JSONMapper.convertToLong(value, false, name);
			case "gueltigAb" -> dto.GueltigVon = JSONMapper.convertToString(value, false, false, null, name);
			case "gueltigBis" -> dto.GueltigBis = JSONMapper.convertToString(value, true, false, null, name);
			case "bezeichnung" -> dto.Bezeichnung = DataGostKlausuren.convertEmptyStringToNull(JSONMapper.convertToString(value, false, false,
					Schema.tab_UV_Stundentafeln.col_Bezeichnung.datenlaenge(), name));
			case "beschreibung" -> dto.Beschreibung = DataGostKlausuren.convertEmptyStringToNull(JSONMapper.convertToString(value, true, true,
					Schema.tab_UV_Stundentafeln.col_Beschreibung.datenlaenge(), name));
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}
}

