package de.svws_nrw.data.uv;

import java.util.Map;

import de.svws_nrw.core.data.uv.UvLehrerPflichtstundensoll;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvLehrerPflichtstundensoll;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link UvLehrerPflichtstundensoll}.
 */
public final class DataUvLehrerPflichtstundensoll extends DataManagerRevised<Long, DTOUvLehrerPflichtstundensoll, UvLehrerPflichtstundensoll> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link UvLehrerPflichtstundensoll}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataUvLehrerPflichtstundensoll(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("id", "idLehrer");
		super.setAttributesRequiredOnCreation("idLehrer", "pflichtstdSoll", "gueltigVon");
	}

	/**
	 * Gibt den {@link UvLehrerPflichtstundensoll}-Eintrag anhand der ID zurück.
	 *
	 * @param id die ID des Pflichtstundensoll-Eintrags
	 * @return der {@link UvLehrerPflichtstundensoll}-Eintrag
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public UvLehrerPflichtstundensoll getById(final Long id) throws ApiOperationException {
		final DTOUvLehrerPflichtstundensoll dto = getDTO(id);
		return map(dto);
	}

	/**
	 * Gibt das {@link DTOUvLehrerPflichtstundensoll}-Objekt zur angegebenen ID zurück.
	 *
	 * @param id die ID des DTOs
	 * @return das DTO
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOUvLehrerPflichtstundensoll getDTO(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die ID für das Lehrer-Pflichtstundensoll darf nicht null sein.");
		}
		final DTOUvLehrerPflichtstundensoll dto = conn.queryByKey(DTOUvLehrerPflichtstundensoll.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Kein Lehrer-Pflichtstundensoll mit der ID " + id + " gefunden.");
		}
		return dto;
	}

	@Override
	protected void initDTO(final DTOUvLehrerPflichtstundensoll dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected UvLehrerPflichtstundensoll map(final DTOUvLehrerPflichtstundensoll dto) throws ApiOperationException {
		final UvLehrerPflichtstundensoll daten = new UvLehrerPflichtstundensoll();
		daten.id = dto.ID;
		daten.idLehrer = dto.Lehrer_ID;
		daten.pflichtstdSoll = dto.PflichtstdSoll;
		daten.gueltigVon = dto.GueltigVon;
		daten.gueltigBis = dto.GueltigBis;
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOUvLehrerPflichtstundensoll dto, final String name, final Object value,
			final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "idLehrer" -> dto.Lehrer_ID = JSONMapper.convertToLong(value, false, name);
			case "pflichtstdSoll" -> dto.PflichtstdSoll = JSONMapper.convertToDouble(value, false, name);
			case "gueltigAb" -> dto.GueltigVon = JSONMapper.convertToString(value, false, false, null, name);
			case "gueltigBis" -> dto.GueltigBis = JSONMapper.convertToString(value, true, false, null, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}
}
