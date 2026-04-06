package de.svws_nrw.data.uv;

import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausuren;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnitt;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittLehrer;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link UvPlanungsabschnitt}.
 */
public final class DataUvPlanungsabschnitte extends DataManagerRevised<Long, DTOUvPlanungsabschnitt, UvPlanungsabschnitt> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO
	 * {@link UvPlanungsabschnitt}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataUvPlanungsabschnitte(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("id", "schuljahr");
		super.setAttributesRequiredOnCreation("schuljahr", "gueltigVon");
	}

	/**
	 * Gibt die Daten eines {@link UvPlanungsabschnitt}s zu deren ID zurück.
	 *
	 * @param id   Die ID des {@link UvPlanungsabschnitt}s.
	 *
	 * @return die Daten des {@link UvPlanungsabschnitt}s zur ID.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public UvPlanungsabschnitt getById(final Long id) throws ApiOperationException {
		final DTOUvPlanungsabschnitt klasseDto = getDTO(id);
		return map(klasseDto);
	}

	/**
	 * Die Methode ermittelt das entsprechende {@link DTOUvPlanungsabschnitt} Objekt zur angegebenen ID.
	 *
	 * @param id ID des {@link DTOUvPlanungsabschnitt} Objekts.
	 *
	 * @return Ein {@link DTOUvPlanungsabschnitt} Objekt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOUvPlanungsabschnitt getDTO(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für den UV-Planungsabschnitt darf nicht null sein.");
		}

		final DTOUvPlanungsabschnitt dto = conn.queryByKey(DTOUvPlanungsabschnitt.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Planungsabschnitt zur ID " + id + " gefunden.");
		}

		return dto;
	}

	@Override
	protected void initDTO(final DTOUvPlanungsabschnitt dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected UvPlanungsabschnitt map(final DTOUvPlanungsabschnitt dto) throws ApiOperationException {
		final UvPlanungsabschnitt daten = new UvPlanungsabschnitt();
		daten.id = dto.ID;
		daten.schuljahr = dto.Schuljahr;
		daten.aktiv = dto.Aktiv;
		daten.gueltigVon = dto.GueltigVon;
		daten.gueltigBis = dto.GueltigBis;
		daten.beschreibung = dto.Beschreibung;
		daten.idsLehrer = conn.queryList(DTOUvPlanungsabschnittLehrer.QUERY_BY_PLANUNGSABSCHNITT_ID,
				DTOUvPlanungsabschnittLehrer.class, daten.id)
				.stream()
				.map(s -> s.Lehrer_ID)
				.toList();
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOUvPlanungsabschnitt dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "schuljahr" -> dto.Schuljahr = JSONMapper.convertToInteger(value, false, name);
			case "aktiv" -> dto.Aktiv = JSONMapper.convertToBoolean(value, false, name);
			case "gueltigAb" -> dto.GueltigVon = JSONMapper.convertToString(value, false, false, null, name);
			case "gueltigBis" -> dto.GueltigBis = JSONMapper.convertToString(value, true, false, null, name);
			case "beschreibung" -> dto.Beschreibung =
					DataGostKlausuren.convertEmptyStringToNull(
							JSONMapper.convertToString(value, true, true, Schema.tab_UV_Planungsabschnitte.col_Beschreibung.datenlaenge(), name));
			case "idsLehrer" -> DataUvSchuelergruppen.updateJoinTable(
					conn,
					"Planungsabschnitt_ID",
					dto.ID,
					value,
					name,
					"DTOUvPlanungsabschnittLehrer",
					"Lehrer_ID",
					DTOUvPlanungsabschnittLehrer.class,
					DTOUvPlanungsabschnittLehrer.QUERY_BY_PLANUNGSABSCHNITT_ID,
					s -> s.Lehrer_ID,
					id -> new DTOUvPlanungsabschnittLehrer(dto.ID, id)
			);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));

		}
	}

	/**
	 * Methode prüft vor dem Persistieren eines Datenbank-DTOs, ob alle Vorbedingungen zum Patch erfüllt sind.
	 * Standardmäßig hat diese Methode keine Implementierung.
	 * Wenn eine Prüfung durchgeführt werden soll, muss diese Methode überschrieben werden.
	 *
	 * @param dto              das Stundenplan-DTO
	 * @param patchedAttributes    die Map mit dem Mapping der Attributnamen auf die Werte der Attribute im Patch
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	@Override
	public void checkBeforePersist(final DTOUvPlanungsabschnitt dto, final Map<String, Object> patchedAttributes) throws ApiOperationException {
		if (dto.Aktiv
				&& (patchedAttributes.containsKey("gueltigAb") || patchedAttributes.containsKey("gueltigBis") || patchedAttributes.containsKey("aktiv"))) {
			final List<UvPlanungsabschnitt> plaene = getUvPlanungsabschnitteBySchuljahr(conn, dto.Schuljahr);
			for (final UvPlanungsabschnitt abschnitt : plaene) {
				if ((abschnitt.id == dto.ID) || !abschnitt.aktiv) {
					continue;
				}
				if (DateUtils.berechneGemeinsameTage(abschnitt.gueltigVon, abschnitt.gueltigBis, dto.GueltigVon, dto.GueltigBis).length > 0) {
					throw new ApiOperationException(Status.CONFLICT,
							"Der Gültigkeit des UV-Planungsabschnitts steht in Konflikt zum Planungsabschnitt mit der ID %d.".formatted(abschnitt.id));
				}
			}
		}
	}

	/**
	 * Gibt die Liste der Planungsabschnitte der UV für ein oder alle Schuljahre zurück.
	 *
	 * @param conn                     die Datenbankverbindung
	 * @param schuljahr   das Schuljahr oder null für alle
	 *
	 * @return die Liste der Stundenpläne
	 */
	public static List<UvPlanungsabschnitt> getUvPlanungsabschnitteBySchuljahr(final DBEntityManager conn, final Integer schuljahr)
			throws ApiOperationException {
		final List<DTOUvPlanungsabschnitt> plaene = (schuljahr == null)
				? conn.queryAll(DTOUvPlanungsabschnitt.class)
				: conn.queryList(DTOUvPlanungsabschnitt.QUERY_BY_SCHULJAHR, DTOUvPlanungsabschnitt.class, schuljahr);
		return (new DataUvPlanungsabschnitte(conn)).mapList(plaene);
	}

}
