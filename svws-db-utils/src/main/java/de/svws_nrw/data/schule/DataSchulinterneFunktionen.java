package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.SchulinterneFunktion;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.util.ValidationUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchulinterneFunktion;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.Strings;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link SchulinterneFunktion}
 */
public final class DataSchulinterneFunktionen extends DataManagerRevised<Long, DTOSchulinterneFunktion, SchulinterneFunktion> {

	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden soll
	 */
	public DataSchulinterneFunktionen(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation("bezeichnung");
	}

	@Override
	protected void initDTO(final DTOSchulinterneFunktion dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.id = newID;
	}

	@Override
	protected long getLongId(final DTOSchulinterneFunktion dto) throws ApiOperationException {
		return dto.id;
	}

	@Override
	public List<SchulinterneFunktion> getAll() {
		final List<DTOSchulinterneFunktion> schulinterneFunktionen = conn.queryAll(DTOSchulinterneFunktion.class);
		final Set<Long> idsSchulinterneFunktionen = this.mapToIds(schulinterneFunktionen);
		final Set<Long> idsOfReferencedSchulinterneFunktionen = this.getIdsOfReferencedSchulinterneFunktionen(idsSchulinterneFunktionen);

		return schulinterneFunktionen.stream()
				.map(this::map)
				.map(schulinterneFunktion -> setReferenceFlag(schulinterneFunktion, idsOfReferencedSchulinterneFunktionen))
				.sorted(Comparator.comparing(sf -> sf.id))
				.toList();
	}

	@Override
	public SchulinterneFunktion getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Eine Anfrage zu einer schulinternen Funktion mit der ID null ist unzulässig.");
		}
		final DTOSchulinterneFunktion dto = conn.queryByKey(DTOSchulinterneFunktion.class, id);
		if (dto == null) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Die schulinterne Funktion mit der ID %d wurde nicht gefunden.".formatted(id));
		}
		return map(dto);
	}

	@Override
	protected SchulinterneFunktion map(final DTOSchulinterneFunktion dto) {
		final SchulinterneFunktion sf = new SchulinterneFunktion();
		sf.id = dto.id;
		sf.bezeichnung = dto.bezeichnung;
		sf.istSichtbar = Boolean.TRUE.equals(dto.istSichtbar);
		sf.sortierung = Objects.requireNonNullElse(dto.sortierung, 32000);
		return sf;
	}

	@Override
	protected void mapAttribute(final DTOSchulinterneFunktion dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> ValidationUtils.validateId(dto.id, name, value);
			case "bezeichnung" -> updateBezeichnung(dto, name, value);
			case "istSichtbar" -> updateSichtbar(dto, name, value);
			case "sortierung" -> updateSortierung(dto, name, value);
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	@Override
	protected void checkBeforeDeletionWithSimpleOperationResponse(final List<DTOSchulinterneFunktion> dtos, final Map<Long, SimpleOperationResponse> responses) {
		final Set<Long> ids = this.mapToIds(dtos);
		final Set<Long> idsReferenced = this.getIdsOfReferencedSchulinterneFunktionen(ids);
		dtos.stream()
				.filter(sf -> idsReferenced.contains(sf.id))
				.forEach(sf -> markResponseAsFailed(responses.get(sf.id), sf.bezeichnung));
	}

	private static void markResponseAsFailed(final SimpleOperationResponse response, final String bezeichnung) {
		response.success = false;
		response.log.add(
				"Die schulinterne Funktion mit der Bezeichnung %s ist in der Datenbank referenziert und kann daher nicht gelöscht werden."
						.formatted(bezeichnung)
		);
	}

	private void updateBezeichnung(final DTOSchulinterneFunktion dto, final String name, final Object value) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Schulfunktionen.col_Bezeichnung.datenlaenge(), name);
		if (ValidationUtils.isBlankOrUnchanged(dto.bezeichnung, bezeichnung)) {
			return;
		}
		validateBezeichnung(dto.id, bezeichnung);
		dto.bezeichnung = bezeichnung;
	}

	private Set<Long> mapToIds(final List<DTOSchulinterneFunktion> schulinterneFunktionen) {
		return schulinterneFunktionen
				.stream()
				.map(sf -> sf.id)
				.collect(Collectors.toSet());
	}

	private Set<Long> getIdsOfReferencedSchulinterneFunktionen(final Set<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return Collections.emptySet();
		}
		final String query = "SELECT DISTINCT lf.idFunktion FROM DTOLehrerFunktion lf WHERE lf.idFunktion IN :ids";
		final List<Long> results = this.conn
				.query(query, Long.class)
				.setParameter("ids", ids)
				.getResultList();
		return new HashSet<>(results);
	}

	private SchulinterneFunktion setReferenceFlag(final SchulinterneFunktion schulinterneFunktion, final Set<Long> idsOfReferencedSchulinterneFunktionen) {
		schulinterneFunktion.referenziertInAnderenTabellen = idsOfReferencedSchulinterneFunktionen.contains(schulinterneFunktion.id);
		return schulinterneFunktion;
	}

	private void validateBezeichnung(final Long id, final String bezeichnung) throws ApiOperationException {
		final boolean bezeichnungAlreadyUsed = this.conn
				.queryAll(DTOSchulinterneFunktion.class)
				.stream()
				.anyMatch(sf -> (!id.equals(sf.id)) && Strings.CI.equals(bezeichnung, sf.bezeichnung));
		if (bezeichnungAlreadyUsed) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));
		}
	}

	private static void updateSortierung(final DTOSchulinterneFunktion dto, final String name, final Object value) throws ApiOperationException {
		dto.sortierung = JSONMapper.convertToInteger(value, false, name);
	}

	private static void updateSichtbar(final DTOSchulinterneFunktion dto, final String name, final Object value) throws ApiOperationException {
		dto.istSichtbar = JSONMapper.convertToBoolean(value, false, name);
	}
}
