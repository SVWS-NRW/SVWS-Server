package de.svws_nrw.data.uv;

import java.util.Map;

import de.svws_nrw.core.data.uv.UvUnterricht;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvUnterricht;
import de.svws_nrw.db.dto.current.uv.DTOUvUnterrichtRaum;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Datenmanager für Unterrichtseinheiten im Unterrichtsverteilungsmodul.
 */
public final class DataUvUnterrichte extends DataManagerRevised<Long, DTOUvUnterricht, UvUnterricht> {

	/**
	 * Erstellt einen neuen {@link DataUvUnterrichte}-Manager.
	 *
	 * @param conn   die Datenbankverbindung
	 */
	public DataUvUnterrichte(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("id", "idPlanungsabschnitt");
		super.setAttributesRequiredOnCreation("idPlanungsabschnitt", "idLerngruppe");
	}

	@Override
	public UvUnterricht getById(final Long id) throws ApiOperationException {
		final DTOUvUnterricht dto = getDTO(id);
		return map(dto);
	}

	private DTOUvUnterricht getDTO(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID darf nicht null sein.");
		}
		final DTOUvUnterricht dto = conn.queryByKey(DTOUvUnterricht.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Unterricht mit ID " + id + " gefunden.");
		}
		return dto;
	}

	@Override
	protected void initDTO(final DTOUvUnterricht dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected UvUnterricht map(final DTOUvUnterricht dto) throws ApiOperationException {
		final UvUnterricht daten = new UvUnterricht();
		daten.id = dto.ID;
		daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		daten.idLerngruppe = dto.Lerngruppe_ID;
		daten.idZeitrasterEintrag = dto.ZeitrasterEintrag_ID;
		daten.idsRaeume = conn.queryList(DTOUvUnterrichtRaum.QUERY_BY_UNTERRICHT_ID,
						DTOUvUnterrichtRaum.class, daten.id)
				.stream()
				.map(s -> s.Raum_ID)
				.toList();
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOUvUnterricht dto, final String name, final Object value,
			final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "idPlanungsabschnitt" -> dto.Planungsabschnitt_ID = JSONMapper.convertToLong(value, false, name);
			case "idLerngruppe" -> dto.Lerngruppe_ID = JSONMapper.convertToLong(value, false, name);
			case "idZeitrasterEintrag" -> dto.ZeitrasterEintrag_ID = JSONMapper.convertToLong(value, true, name);
			case "idsRaeume" -> DataUvSchuelergruppen.updateJoinTable(
					conn,
					"Unterricht_ID",
					dto.ID,
					value,
					name,
					"DTOUvUnterrichtRaum",
					"Raum_ID",
					DTOUvUnterrichtRaum.class,
					DTOUvUnterrichtRaum.QUERY_BY_UNTERRICHT_ID,
					s -> s.Raum_ID,
					id -> new DTOUvUnterrichtRaum(dto.ID, id, dto.Planungsabschnitt_ID)
			);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}
}
