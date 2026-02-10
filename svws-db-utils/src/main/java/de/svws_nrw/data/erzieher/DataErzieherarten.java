package de.svws_nrw.data.erzieher;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.erzieher.Erzieherart;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOErzieherart;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Erzieherart}.
 */
public final class DataErzieherarten extends DataManagerRevised<Long, DTOErzieherart, Erzieherart> {

	private static final String BEZEICHNUNG = "bezeichnung";

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Erzieherart}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataErzieherarten(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation(BEZEICHNUNG);
		setAttributesNotPatchable("id", BEZEICHNUNG);
	}

	@Override
	protected void initDTO(final DTOErzieherart dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected long getLongId(final DTOErzieherart dto) {
		return dto.ID;
	}

	@Override
	public Erzieherart map(final DTOErzieherart dto) {
		final Erzieherart daten = new Erzieherart();
		daten.id = dto.ID;
		daten.bezeichnung = dto.Bezeichnung;
		daten.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		daten.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		return daten;
	}

	@Override
	public Erzieherart getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die ID für die Erzieherart darf nicht null sein.");
		}
		final DTOErzieherart dto = conn.queryByKey(DTOErzieherart.class, id);
		if (dto == null) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde keine Erzieherart mit der ID %d gefunden.".formatted(id));
		}
		return map(dto);
	}

	@Override
	public List<Erzieherart> getAll() {
		final List<DTOErzieherart> erzieherarten = this.conn.queryAll(DTOErzieherart.class);
		final Set<Long> idsOfReferencedErzieherarten = this.getIdsOfReferencedErzieherarten(mapToIds(erzieherarten));

		return erzieherarten
				.stream()
				.map(this::map)
				.map(f -> setReferenceFlag(f, idsOfReferencedErzieherarten))
				.sorted(Comparator.comparing(f -> f.id))
				.toList();
	}

	@Override
	protected void mapAttribute(final DTOErzieherart dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case BEZEICHNUNG -> validateBezeichnung(dto, value, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void validateId(final DTOErzieherart dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if (!Objects.equals(dto.ID, id))
			throw new ApiOperationException(Response.Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
	}

	private void validateBezeichnung(final DTOErzieherart dto, final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_ErzieherArt.col_Bezeichnung.datenlaenge(), name);
		if (StringUtils.isBlank(bezeichnung)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Attribut bezeichnung: Ein leerer String ist nicht erlaubt.");
		}
		validateBezeichnungIsUnique(bezeichnung);
		dto.Bezeichnung = bezeichnung;
	}

	private void validateBezeichnungIsUnique(final String bezeichnung) throws ApiOperationException {
		final boolean bezeichnungAlreadyUsed = this.conn
				.queryAll(DTOErzieherart.class)
				.stream()
				.anyMatch(e -> Strings.CI.equals(bezeichnung, e.Bezeichnung));
		if (bezeichnungAlreadyUsed) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));
		}
	}

	@Override
	protected void checkBeforeDeletionWithSimpleOperationResponse(final List<DTOErzieherart> erzieherarten, final Map<Long, SimpleOperationResponse> responses) {
		erzieherarten
				.stream()
				.filter(e -> Boolean.FALSE.equals(e.Aenderbar))
				.forEach(e -> markResponseAsFailed(responses.get(e.ID), e.ID));
	}

	private static void markResponseAsFailed(final SimpleOperationResponse response, final Long id) {
		response.success = false;
		response.log.add(("Die Erzieherart mit der id %d darf aufgrund der Volljährigkeitsberechnung in SchildZentral nicht gelöscht werden.").formatted(id));
	}

	private Set<Long> getIdsOfReferencedErzieherarten(final Set<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return Collections.emptySet();
		}

		final String query = "SELECT DISTINCT a.ErzieherArt_ID FROM DTOSchuelerErzieherAdresse a WHERE a.ErzieherArt_ID IN :ids";
		final List<Long> results = this.conn
				.query(query, Long.class)
				.setParameter("ids", ids)
				.getResultList();
		return new HashSet<>(results);
	}

	private static Set<Long> mapToIds(final List<DTOErzieherart> erzieherarten) {
		return erzieherarten
				.stream()
				.map(f -> f.ID)
				.collect(Collectors.toSet());
	}

	private static Erzieherart setReferenceFlag(final Erzieherart erzieherart, final Set<Long> idsOfReferencedErzieherarten) {
		erzieherart.referenziertInAnderenTabellen = idsOfReferencedErzieherarten.contains(erzieherart.id);
		return erzieherart;
	}

}
