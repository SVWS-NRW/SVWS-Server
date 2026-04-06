package de.svws_nrw.data.uv;

import java.util.Map;

import de.svws_nrw.core.data.uv.UvLerngruppenLehrer;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppenLehrer;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link UvLerngruppenLehrer}.
 */
public final class DataUvLerngruppenLehrer extends DataManagerRevised<Long, DTOUvLerngruppenLehrer, UvLerngruppenLehrer> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link UvLerngruppenLehrer}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataUvLerngruppenLehrer(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("id", "idPlanungsabschnitt", "idLerngruppe");
		super.setAttributesRequiredOnCreation("idPlanungsabschnitt", "idLerngruppe", "idLehrer", "reihenfolge", "wochenstunden", "wochenstundenAngerechnet");
	}

	/**
	 * Gibt die Daten einer {@link UvLerngruppenLehrer}-Zuordnung anhand der ID zurück.
	 *
	 * @param id die ID der Zuordnung
	 * @return die Daten der {@link UvLerngruppenLehrer}-Zuordnung zur ID
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public UvLerngruppenLehrer getById(final Long id) throws ApiOperationException {
		final DTOUvLerngruppenLehrer dto = getDTO(id);
		return map(dto);
	}

	/**
	 * Gibt das {@link DTOUvLerngruppenLehrer}-Objekt zur angegebenen ID zurück.
	 *
	 * @param id die ID des DTOs
	 * @return das DTO
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOUvLerngruppenLehrer getDTO(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die ID für die Lerngruppen-Lehrer-Zuordnung darf nicht null sein.");
		}
		final DTOUvLerngruppenLehrer dto = conn.queryByKey(DTOUvLerngruppenLehrer.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Keine Lerngruppen-Lehrer-Zuordnung mit der ID " + id + " gefunden.");
		}
		return dto;
	}

	@Override
	protected void initDTO(final DTOUvLerngruppenLehrer dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected UvLerngruppenLehrer map(final DTOUvLerngruppenLehrer dto) throws ApiOperationException {
		final UvLerngruppenLehrer daten = new UvLerngruppenLehrer();
		daten.id = dto.ID;
		daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		daten.idLerngruppe = dto.Lerngruppe_ID;
		daten.idLehrer = dto.Lehrer_ID;
		daten.reihenfolge = dto.Reihenfolge;
		daten.wochenstunden = dto.Wochenstunden;
		daten.wochenstundenAngerechnet = dto.WochenstundenAngerechnet;
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOUvLerngruppenLehrer dto, final String name, final Object value,
			final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "idPlanungsabschnitt" -> dto.Planungsabschnitt_ID = JSONMapper.convertToLong(value, false, name);
			case "idLerngruppe" -> dto.Lerngruppe_ID = JSONMapper.convertToLong(value, false, name);
			case "idLehrer" -> dto.Lehrer_ID = JSONMapper.convertToLong(value, false, name);
			case "reihenfolge" -> dto.Reihenfolge = JSONMapper.convertToInteger(value, false, name);
			case "wochenstunden" -> dto.Wochenstunden = JSONMapper.convertToDouble(value, false, name);
			case "wochenstundenAngerechnet" -> dto.WochenstundenAngerechnet = JSONMapper.convertToDouble(value, false, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}
}
