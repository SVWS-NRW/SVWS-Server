package de.svws_nrw.data.uv;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.uv.UvSchuelergruppe;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppe;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeConstraintJahrgang;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeConstraintSchuelergruppe;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeSchueler;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link UvSchuelergruppe}.
 */
public final class DataUvSchuelergruppen extends DataManagerRevised<Long, DTOUvSchuelergruppe, UvSchuelergruppe> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO
	 * {@link UvSchuelergruppe}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataUvSchuelergruppen(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("id", "idPlanungsabschnitt");
		super.setAttributesRequiredOnCreation("bezeichnung");
	}

	/**
	 * Gibt die Daten eines UvPlanungsabschnittSchueler zur ID zurück.
	 *
	 * @param id Die ID des Eintrags.
	 * @return die Daten des Eintrags zur ID.
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public UvSchuelergruppe getById(final Long id) throws ApiOperationException {
		final DTOUvSchuelergruppe dto = getDTO(id);
		return map(dto);
	}

	/**
	 * Ermittelt das entsprechende DTOUvPlanungsabschnittSchueler Objekt zur angegebenen ID.
	 *
	 * @param id ID des Objekts.
	 * @return Ein DTOUvPlanungsabschnittSchueler Objekt.
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOUvSchuelergruppe getDTO(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die ID für die UV-Schülergruppe darf nicht null sein.");
		}
		final DTOUvSchuelergruppe dto = conn.queryByKey(DTOUvSchuelergruppe.class, id);
		if (dto == null) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Kein Eintrag zur ID " + id + " gefunden.");
		}
		return dto;
	}

	@Override
	protected void initDTO(final DTOUvSchuelergruppe dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected UvSchuelergruppe map(final DTOUvSchuelergruppe dto) throws ApiOperationException {
		final UvSchuelergruppe daten = new UvSchuelergruppe();
		daten.id = dto.ID;
		daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		daten.bezeichnung = dto.Bezeichnung;
		daten.idsSchueler = conn.queryList(DTOUvSchuelergruppeSchueler.QUERY_BY_SCHUELERGRUPPE_ID,
				DTOUvSchuelergruppeSchueler.class, daten.id)
				.stream()
				.map(s -> s.Schueler_ID)
				.toList();
		daten.idsJahrgaengeErlaubt = conn.queryList(DTOUvSchuelergruppeConstraintJahrgang.QUERY_BY_SCHUELERGRUPPE_ID,
				DTOUvSchuelergruppeConstraintJahrgang.class, daten.id)
				.stream()
				.map(s -> s.Jahrgang_ID)
				.toList();
		daten.idsGruppenErlaubt = conn.queryList(DTOUvSchuelergruppeConstraintSchuelergruppe.QUERY_BY_SCHUELERGRUPPE_ID,
				DTOUvSchuelergruppeConstraintSchuelergruppe.class, daten.id)
				.stream()
				.map(s -> s.Schuelergruppe_Vaild_ID)
				.toList();
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOUvSchuelergruppe dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idPlanungsabschnitt" -> dto.Planungsabschnitt_ID = JSONMapper.convertToLong(value, false, name);
			case "bezeichnung" -> dto.Bezeichnung = JSONMapper.convertToString(value, false, false,
					Schema.tab_UV_Schuelergruppen.col_Bezeichnung.datenlaenge());
			case "idsSchueler" -> updateJoinTable(
					conn,
					"Schuelergruppe_ID",
					dto.ID,
					value,
					name,
					"DTOUvSchuelergruppeSchueler",
					"Schueler_ID",
					DTOUvSchuelergruppeSchueler.class,
					DTOUvSchuelergruppeSchueler.QUERY_BY_SCHUELERGRUPPE_ID,
					s -> s.Schueler_ID,
					id -> new DTOUvSchuelergruppeSchueler(dto.ID, id, dto.Planungsabschnitt_ID)
			);
			case "idsJahrgaengeErlaubt" -> updateJoinTable(
					conn,
					"Schuelergruppe_ID",
					dto.ID,
					value,
					name,
					"DTOUvSchuelergruppeConstraintJahrgang",
					"Jahrgang_ID",
					DTOUvSchuelergruppeConstraintJahrgang.class,
					DTOUvSchuelergruppeConstraintJahrgang.QUERY_BY_SCHUELERGRUPPE_ID,
					s -> s.Jahrgang_ID,
					id -> new DTOUvSchuelergruppeConstraintJahrgang(dto.ID, id, dto.Planungsabschnitt_ID)
			);
			case "idsGruppenErlaubt" -> updateJoinTable(
					conn,
					"Schuelergruppe_ID",
					dto.ID,
					value,
					name,
					"DTOUvSchuelergruppeConstraintSchuelergruppe",
					"Schuelergruppe_Vaild_ID",
					DTOUvSchuelergruppeConstraintSchuelergruppe.class,
					DTOUvSchuelergruppeConstraintSchuelergruppe.QUERY_BY_SCHUELERGRUPPE_ID,
					s -> s.Schuelergruppe_Vaild_ID,
					id -> new DTOUvSchuelergruppeConstraintSchuelergruppe(dto.ID, id, dto.Planungsabschnitt_ID)
			);
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}

	static <T> void updateJoinTable(
			final DBEntityManager conn,
			final String idColumnName,
			final long idColumnValue,
			final Object jsonValue,
			final String jsonName,
			final String tableName,
			final String fkColumnName,
			final Class<T> dtoClass,
			final String sqlQueryById,
			final Function<T, Long> idFkExtractor,
			final Function<Long, T> dtoFactory)
			throws ApiOperationException {
		final List<Long> idsNew = JSONMapper.convertToListOfLong(jsonValue, false, jsonName);
		final List<Long> idsInDatabase = conn.queryList(sqlQueryById, dtoClass, idColumnValue)
				.stream()
				.map(idFkExtractor)
				.toList();
		final Set<Long> oldSet = new HashSet<>(idsInDatabase);
		final Set<Long> newSet = new HashSet<>(idsNew);
		final List<T> dtosToCreate = newSet.stream()
				.filter(id -> !oldSet.contains(id))
				.map(dtoFactory)
				.toList();
		final List<Long> dtosToDelete = oldSet.stream()
				.filter(id -> !newSet.contains(id))
				.toList();
		if (!dtosToDelete.isEmpty()) {
			final String fkColumnIdsCSV = dtosToDelete.stream()
					.map(Object::toString)
					.collect(Collectors.joining(","));
			conn.transactionExecuteDelete(
					"DELETE FROM %s WHERE %s = %d AND %s IN (%s)"
							.formatted(tableName, idColumnName, idColumnValue, fkColumnName, fkColumnIdsCSV)
			);
		}
		if (!dtosToCreate.isEmpty()) {
			conn.transactionPersistAll(dtosToCreate);
		}
	}
}
