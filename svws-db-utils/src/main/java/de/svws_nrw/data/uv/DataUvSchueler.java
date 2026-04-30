package de.svws_nrw.data.uv;

import java.util.Map;

import de.svws_nrw.core.data.uv.LongPair;
import de.svws_nrw.core.data.uv.UvSchueler;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link UvSchueler}.
 */
public final class DataUvSchueler extends DataManagerRevised<LongPair, DTOUvPlanungsabschnittSchueler, UvSchueler> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO
	 * {@link UvSchueler}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataUvSchueler(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("idPlanungsabschnitt", "idSchueler");
		super.setAttributesRequiredOnCreation("idPlanungsabschnitt", "idSchueler");
	}

	/**
	 * Gibt die Daten eines UvPlanungsabschnittSchueler zur ID zurück.
	 *
	 * @param id Die ID des Eintrags.
	 * @return die Daten des Eintrags zur ID.
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public UvSchueler getById(final LongPair id) throws ApiOperationException {
		final DTOUvPlanungsabschnittSchueler dto = getDTO(id);
		return map(dto);
	}

	@Override
	public DTOUvPlanungsabschnittSchueler getDatabaseDTOByID(final LongPair id) {
		return conn.queryByKey(DTOUvPlanungsabschnittSchueler.class, id.a, id.b);
	}

	@Override
	protected LongPair getID(final Map<String, Object> attributes) throws ApiOperationException {
		final Long idPlanungsabschnitt = JSONMapper.convertToLong(attributes.get("idPlanungsabschnitt"), false, "idPlanungsabschnitt");
		final Long idSchueler = JSONMapper.convertToLong(attributes.get("idSchueler"), false, "idSchueler");
		return new LongPair(idSchueler, idPlanungsabschnitt);
	}

	@Override
	protected LongPair getNextID(final LongPair lastID, final Map<String, Object> initAttributes) throws ApiOperationException {
		return getID(initAttributes);
	}

	/**
	 * Ermittelt das entsprechende DTOUvPlanungsabschnittSchueler Objekt zur angegebenen ID.
	 *
	 * @param id ID des Objekts.
	 * @return Ein DTOUvPlanungsabschnittSchueler Objekt.
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOUvPlanungsabschnittSchueler getDTO(final LongPair id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die ID für den UV-Schüler darf nicht null sein.");
		}
		final DTOUvPlanungsabschnittSchueler dto = conn.queryByKey(DTOUvPlanungsabschnittSchueler.class, id.a, id.b);
		if (dto == null) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Kein Eintrag zur ID " + id + " gefunden.");
		}
		return dto;
	}

	@Override
	protected void initDTO(final DTOUvPlanungsabschnittSchueler dto, final LongPair id, final Map<String, Object> initAttributes)
			throws ApiOperationException {
		dto.Planungsabschnitt_ID = JSONMapper.convertToLong(initAttributes.get("idPlanungsabschnitt"), false, "idPlanungsabschnitt");
		dto.Schueler_ID = JSONMapper.convertToLong(initAttributes.get("idSchueler"), false, "idSchueler");
	}

	@Override
	protected UvSchueler map(final DTOUvPlanungsabschnittSchueler dto) throws de.svws_nrw.db.utils.ApiOperationException {
		final UvSchueler daten = new UvSchueler();
		daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		daten.idSchueler = dto.Schueler_ID;
		daten.idJahrgang = dto.Jahrgang_ID;
		daten.idKlasse = dto.Klasse_ID;
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOUvPlanungsabschnittSchueler dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idPlanungsabschnitt" -> dto.Planungsabschnitt_ID = JSONMapper.convertToLong(value, false, name);
			case "idSchueler" -> dto.Schueler_ID = JSONMapper.convertToLong(value, false, name);
			case "idJahrgang" -> dto.Jahrgang_ID = JSONMapper.convertToLong(value, true, name);
			case "idKlasse" -> dto.Klasse_ID = JSONMapper.convertToLong(value, true, name);
			default -> throw new ApiOperationException(jakarta.ws.rs.core.Response.Status.BAD_REQUEST,
					"Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}
}
