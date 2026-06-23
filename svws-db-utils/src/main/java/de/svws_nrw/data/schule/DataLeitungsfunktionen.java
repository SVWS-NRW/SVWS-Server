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
import de.svws_nrw.core.data.schule.Leitungsfunktion;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.util.ValidationUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOLeitungsfunktion;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.Strings;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Leitungsfunktion}
 */
public final class DataLeitungsfunktionen extends DataManagerRevised<Long, DTOLeitungsfunktion, Leitungsfunktion> {

	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden soll
	 */
	public DataLeitungsfunktionen(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation("bezeichnung");
	}

	@Override
	protected void initDTO(final DTOLeitungsfunktion dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.ID = newID;
	}

	@Override
	protected long getLongId(final DTOLeitungsfunktion dto) throws ApiOperationException {
		return dto.ID;
	}

	@Override
	public List<Leitungsfunktion> getAll() {
		final List<DTOLeitungsfunktion> leitungsfunktionen = conn.queryAll(DTOLeitungsfunktion.class);
		final Set<Long> idsLeitungsfunktionen = this.mapToIds(leitungsfunktionen);
		final Set<Long> idsOfReferencedLeitungsfunktionen = this.getIdsOfReferencedLeitungsfunktionen(idsLeitungsfunktionen);

		return leitungsfunktionen.stream()
				.map(this::map)
				.map(leitungsfunktion -> setReferenceFlag(leitungsfunktion, idsOfReferencedLeitungsfunktionen))
				.sorted(Comparator.comparing(lf -> lf.id))
				.toList();
	}

	@Override
	public Leitungsfunktion getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Eine Anfrage zu einer Leitungsfunktion mit der ID null ist unzulässig.");
		}
		final DTOLeitungsfunktion dto = conn.queryByKey(DTOLeitungsfunktion.class, id);
		if (dto == null) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Die Leitungsfunktion mit der ID %d wurde nicht gefunden.".formatted(id));
		}
		return map(dto);
	}

	@Override
	protected Leitungsfunktion map(final DTOLeitungsfunktion dto) {
		final Leitungsfunktion lf = new Leitungsfunktion();
		lf.id = dto.ID;
		lf.bezeichnung = dto.Bezeichnung;
		lf.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		lf.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		return lf;
	}

	@Override
	protected void mapAttribute(final DTOLeitungsfunktion dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> ValidationUtils.validateId(dto.ID, name, value);
			case "bezeichnung" -> updateBezeichnung(dto, name, value);
			case "istSichtbar" -> updateSichtbar(dto, name, value);
			case "sortierung" -> updateSortierung(dto, name, value);
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	@Override
	protected void checkBeforeDeletionWithSimpleOperationResponse(final List<DTOLeitungsfunktion> dtos, final Map<Long, SimpleOperationResponse> responses) {
		final Set<Long> ids = this.mapToIds(dtos);
		final Set<Long> idsReferenced = this.getIdsOfReferencedLeitungsfunktionen(ids);
		dtos.stream()
				.filter(lf -> idsReferenced.contains(lf.ID))
				.forEach(lf -> markResponseAsFailed(responses.get(lf.ID), lf.Bezeichnung));
	}

	private static void markResponseAsFailed(final SimpleOperationResponse response, final String bezeichnung) {
		response.success = false;
		response.log.add(
				"Die Leitungsfunktion mit der Bezeichnung %s ist in der Datenbank referenziert und kann daher nicht gelöscht werden."
						.formatted(bezeichnung)
		);
	}

	private void updateBezeichnung(final DTOLeitungsfunktion dto, final String name, final Object value) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Schulfunktionen.col_Bezeichnung.datenlaenge(), name);
		if (ValidationUtils.isBlankOrUnchanged(dto.Bezeichnung, bezeichnung)) {
			return;
		}
		validateBezeichnung(dto.ID, bezeichnung);
		dto.Bezeichnung = bezeichnung;
	}

	private Set<Long> mapToIds(final List<DTOLeitungsfunktion> leitungsfunktionen) {
		return leitungsfunktionen
				.stream()
				.map(lf -> lf.ID)
				.collect(Collectors.toSet());
	}

	private Set<Long> getIdsOfReferencedLeitungsfunktionen(final Set<Long> ids) {
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

	private Leitungsfunktion setReferenceFlag(final Leitungsfunktion leitungsfunktion, final Set<Long> idsOfReferencedLeistungsfunktionen) {
		leitungsfunktion.referenziertInAnderenTabellen = idsOfReferencedLeistungsfunktionen.contains(leitungsfunktion.id);
		return leitungsfunktion;
	}

	private void validateBezeichnung(final Long id, final String bezeichnung) throws ApiOperationException {
		final boolean bezeichnungAlreadyUsed = this.conn
				.queryAll(DTOLeitungsfunktion.class)
				.stream()
				.anyMatch(lf -> (!id.equals(lf.ID)) && Strings.CI.equals(bezeichnung, lf.Bezeichnung));
		if (bezeichnungAlreadyUsed) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));
		}
	}

	private static void updateSortierung(final DTOLeitungsfunktion dto, final String name, final Object value) throws ApiOperationException {
		dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
	}

	private static void updateSichtbar(final DTOLeitungsfunktion dto, final String name, final Object value) throws ApiOperationException {
		dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
	}
}
