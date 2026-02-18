package de.svws_nrw.data.kataloge;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.kataloge.SchuelerSchwerpunkt;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.util.ValidationUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuelerSchwerpunkt;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Katalog Core-DTO {@link SchuelerSchwerpunkt}.
 */
public final class DataSchuelerSchwerpunkte extends DataManagerRevised<Long, DTOSchuelerSchwerpunkt, SchuelerSchwerpunkt> {

	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden soll
	 */
	public DataSchuelerSchwerpunkte(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	protected void initDTO(final DTOSchuelerSchwerpunkt dto, final Long newId, final Map<String, Object> initAttributes) {
		dto.ID = newId;
	}

	@Override
	public SchuelerSchwerpunkt getById(final Long id) {
		if (id == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die ID des Schwerpunktes darf nicht null sein.");
		}

		final DTOSchuelerSchwerpunkt entity = conn.queryByKey(DTOSchuelerSchwerpunkt.class, id);
		if (entity == null) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde kein Schwerpunkt mit der ID %d gefunden.".formatted(id));
		}

		final SchuelerSchwerpunkt schuelerSchwerpunkt = map(entity);
		return updatedWithReferenceFlag(schuelerSchwerpunkt, getReferencedIds(List.of(entity)));
	}

	@Override
	public List<SchuelerSchwerpunkt> getAll() {
		final List<DTOSchuelerSchwerpunkt> schuelerSchwerpunkt = conn.queryAll(DTOSchuelerSchwerpunkt.class);

		final var referencedIds = getReferencedIds(schuelerSchwerpunkt);
		return schuelerSchwerpunkt
				.stream()
				.map(this::map)
				.map(s -> updatedWithReferenceFlag(s, referencedIds))
				.toList();
	}

	private static SchuelerSchwerpunkt updatedWithReferenceFlag(final SchuelerSchwerpunkt schwerpunkt, final Set<Long> idsOfReferencedFaecher) {
		schwerpunkt.referenziertInAnderenTabellen = idsOfReferencedFaecher.contains(schwerpunkt.id);
		return schwerpunkt;
	}

	@Override
	protected void checkBeforeDeletionWithSimpleOperationResponse(final List<DTOSchuelerSchwerpunkt> dtos, final Map<Long, SimpleOperationResponse> responses) {
		final var referencedIds = getReferencedIds(dtos);

		dtos.stream()
				.filter(d -> referencedIds.contains(d.ID))
				.forEach(invalidDto -> markResponseAsFailed(responses.get(invalidDto.ID), invalidDto.Bezeichnung));
	}

	@Override
	protected long getLongId(final DTOSchuelerSchwerpunkt entity) {
		return entity.ID;
	}

	private static void markResponseAsFailed(final SimpleOperationResponse response, final String name) {
		response.success = false;
		response.log.add(("Schwerpunkt mit dem Namen %s ist in der Datenbank referenziert und kann daher nicht gelöscht werden.").formatted(name));
	}

	@Override
	protected void mapAttribute(final DTOSchuelerSchwerpunkt dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> ValidationUtils.validateId(dto.ID, name, value);
			case "bezeichnung" -> updateBezeichnung(dto, name, value);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			default ->
				throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	@Override
	protected SchuelerSchwerpunkt map(final DTOSchuelerSchwerpunkt entity) {
		final var schwerpunkt = new SchuelerSchwerpunkt();

		schwerpunkt.id = entity.ID;
		schwerpunkt.bezeichnung = entity.Bezeichnung;
		schwerpunkt.sortierung = Objects.requireNonNullElse(entity.Sortierung, 32000);
		schwerpunkt.istSichtbar = Boolean.TRUE.equals(entity.Sichtbar);

		return schwerpunkt;
	}

	private void updateBezeichnung(final DTOSchuelerSchwerpunkt dto, final String name, final Object value) {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Schwerpunkt.col_Bezeichnung.datenlaenge(), name);
		if (ValidationUtils.isBlankOrUnchanged(dto.Bezeichnung, bezeichnung)) {
			return;
		}

		final var isBezeichnungPresent = conn.existsBy(DTOSchuelerSchwerpunkt.QUERY_BY_BEZEICHNUNG, DTOSchuelerSchwerpunkt.class, bezeichnung);

		if (isBezeichnungPresent) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Bezeichnung %s wird bereits verwendet.".formatted(bezeichnung));
		}

		dto.Bezeichnung = bezeichnung;
	}

	private Set<Long> getReferencedIds(final List<DTOSchuelerSchwerpunkt> schwerpunkteToCheck) {
		if ((schwerpunkteToCheck == null) || schwerpunkteToCheck.isEmpty()) {
			return Collections.emptySet();
		}

		final var idsToCheck = schwerpunkteToCheck.stream()
				.map(s -> s.ID)
				.toList();

		final String referencedQuery = "SELECT DISTINCT a.Schwerpunkt_ID FROM DTOSchuelerLernabschnittsdaten a WHERE a.Schwerpunkt_ID IN :referencedIds";

		final List<Long> results = conn.query(referencedQuery, Long.class)
				.setParameter("referencedIds", idsToCheck)
				.getResultList();
		return new HashSet<>(results);
	}
}
