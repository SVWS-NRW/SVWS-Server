package de.svws_nrw.data.uv;

import java.util.Map;

import de.svws_nrw.core.data.uv.UvLehrerAnrechnungsstunden;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvLehrerAnrechnungsstunden;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link UvLehrerAnrechnungsstunden}.
 */
public final class DataUvLehrerAnrechnungsstunden extends DataManagerRevised<Long, DTOUvLehrerAnrechnungsstunden, UvLehrerAnrechnungsstunden> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO
	 * {@link UvLehrerAnrechnungsstunden}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataUvLehrerAnrechnungsstunden(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("id", "idLehrer");
		super.setAttributesRequiredOnCreation("idLehrer", "anrechnungsgrundKrz", "anzahlStunden", "gueltigVon", "gueltigBis");
	}

	/**
	 * Gibt die Daten eines {@link UvLehrerAnrechnungsstunden}-Eintrags zu dessen ID zurück.
	 *
	 * @param id Die ID des {@link UvLehrerAnrechnungsstunden}-Eintrags.
	 * @return die Daten des {@link UvLehrerAnrechnungsstunden}-Eintrags zur ID.
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public UvLehrerAnrechnungsstunden getById(final Long id) throws ApiOperationException {
		final DTOUvLehrerAnrechnungsstunden dto = getDTO(id);
		return map(dto);
	}

	/**
	 * Die Methode ermittelt das entsprechende {@link DTOUvLehrerAnrechnungsstunden}-Objekt zur angegebenen ID.
	 *
	 * @param id ID des {@link DTOUvLehrerAnrechnungsstunden}-Objekts.
	 * @return Ein {@link DTOUvLehrerAnrechnungsstunden}-Objekt.
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOUvLehrerAnrechnungsstunden getDTO(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für die Lehrer-Anrechnungsstunde darf nicht null sein.");
		}
		final DTOUvLehrerAnrechnungsstunden dto = conn.queryByKey(DTOUvLehrerAnrechnungsstunden.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Lehrer-Anrechnungsstunde zur ID " + id + " gefunden.");
		}
		return dto;
	}

	@Override
	protected void initDTO(final DTOUvLehrerAnrechnungsstunden dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected UvLehrerAnrechnungsstunden map(final DTOUvLehrerAnrechnungsstunden dto) throws ApiOperationException {
		final UvLehrerAnrechnungsstunden daten = new UvLehrerAnrechnungsstunden();
		daten.id = dto.ID;
		daten.idLehrer = dto.Lehrer_ID;
		daten.anrechnungsgrundKrz = dto.AnrechnungsgrundKrz;
		daten.anzahlStunden = dto.AnzahlStunden;
		daten.gueltigVon = dto.GueltigVon;
		daten.gueltigBis = dto.GueltigBis;
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOUvLehrerAnrechnungsstunden dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idLehrer" -> dto.Lehrer_ID = JSONMapper.convertToLong(value, false, name);
			case "anrechnungsgrundKrz" -> dto.AnrechnungsgrundKrz = JSONMapper.convertToString(value, false, false,
					Schema.tab_UV_LehrerAnrechnungsstunden.col_AnrechnungsgrundKrz.datenlaenge(), name);
			case "anzahlStunden" -> dto.AnzahlStunden = JSONMapper.convertToDouble(value, false, name);
			case "gueltigAb" -> dto.GueltigVon = JSONMapper.convertToString(value, false, false, null, name);
			case "gueltigBis" -> dto.GueltigBis = JSONMapper.convertToString(value, true, false, null, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}
}
