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
import de.svws_nrw.core.data.schule.Betriebsart;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOBetriebsart;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

import static jakarta.ws.rs.core.Response.Status;

/** Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das CoreDTO {@link Betriebsart} */
public final class DataBetriebsarten extends DataManagerRevised<Long, DTOBetriebsart, Betriebsart> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Betriebsart}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataBetriebsarten(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("bezeichnung");
		setAttributesNotPatchable("id", "referenziertInAnderenTabellen");
	}

	@Override
	protected long getLongId(final DTOBetriebsart dto) {
		return dto.ID;
	}

	@Override
	public Betriebsart map(final DTOBetriebsart dto) {
		final Betriebsart daten = new Betriebsart();
		daten.id = dto.ID;
		daten.bezeichnung = dto.Bezeichnung;
		daten.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		daten.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		return daten;
	}

	@Override
	public Betriebsart getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID der Betriebsart darf nicht null sein.");
		}
		final DTOBetriebsart betriebsart = conn.queryByKey(DTOBetriebsart.class, id);
		if (betriebsart == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Die Betriebsart mit der ID %d wurde nicht gefunden.".formatted(id));
		}
		return map(betriebsart);
	}

	@Override
	protected void initDTO(final DTOBetriebsart dto, final Long newId, final Map<String, Object> initAttributes) {
		dto.ID = newId;
		dto.Sortierung = 32000;
	}

	@Override
	public List<Betriebsart> getAll() {
		final List<DTOBetriebsart> betriebsarten = this.conn.queryAll(DTOBetriebsart.class);
		final Set<Long> idsBetriebsarten = this.mapToIds(betriebsarten);
		final Set<Long> idsOfReferencedBetriebsarten = this.getIdsOfReferencedBetriebsarten(idsBetriebsarten);

		return betriebsarten
				.stream()
				.map(this::map)
				.map(v -> setReferenceFlag(v, idsOfReferencedBetriebsarten))
				.sorted(Comparator.comparing(v -> v.id))
				.toList();
	}

	@Override
	protected void mapAttribute(final DTOBetriebsart dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case "bezeichnung" -> updateBezeichnung(dto, value, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	@Override
	protected void checkBeforeDeletionWithSimpleOperationResponse(final List<DTOBetriebsart> betriebsarten,
			final Map<Long, SimpleOperationResponse> responses) {
		final Set<Long> idsOfReferencedBetriebsarten = getIdsOfReferencedBetriebsarten(mapToIds(betriebsarten));
		betriebsarten.stream()
				.filter(f -> idsOfReferencedBetriebsarten.contains(f.ID))
				.forEach(f -> markResponseAsFailed(responses.get(f.ID), f.Bezeichnung));
	}

	private static void validateId(final DTOBetriebsart dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if (!Objects.equals(dto.ID, id)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
		}
	}

	private void updateBezeichnung(final DTOBetriebsart dto, final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Adressart.col_Bezeichnung.datenlaenge(), name);
		if (StringUtils.isBlank(bezeichnung) || Strings.CS.equals(dto.Bezeichnung, bezeichnung)) {
			return;
		}
		validateBezeichnung(dto.ID, bezeichnung);
		dto.Bezeichnung = bezeichnung;
	}

	private void validateBezeichnung(final Long id, final String bezeichnung) throws ApiOperationException {
		final boolean isAlreadyUsed = this.conn
				.queryAll(DTOBetriebsart.class).stream()
				.anyMatch(e -> (e.ID != id) && Strings.CI.equals(bezeichnung, e.Bezeichnung));
		if (isAlreadyUsed) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));
		}
	}

	private Set<Long> mapToIds(final List<DTOBetriebsart> betriebsarten) {
		return betriebsarten.stream()
				.map(f -> f.ID)
				.collect(Collectors.toSet());
	}

	private Set<Long> getIdsOfReferencedBetriebsarten(final Set<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return Collections.emptySet();
		}
		final String query = "SELECT DISTINCT a.adressArt FROM DTOBetrieb a WHERE a.adressArt IN :ids";
		final List<Long> results = this.conn.query(query, Long.class).setParameter("ids", ids).getResultList();
		return new HashSet<>(results);
	}

	private Betriebsart setReferenceFlag(final Betriebsart betriebsart, final Set<Long> idsOfReferencedBetriebsarten) {
		betriebsart.referenziertInAnderenTabellen = idsOfReferencedBetriebsarten.contains(betriebsart.id);
		return betriebsart;
	}

	private static void markResponseAsFailed(final SimpleOperationResponse response, final String name) {
		response.success = false;
		response.log.add(
				("Die Betriebsart mit dem Name %s ist in der Datenbank referenziert und kann daher nicht gelöscht werden.").formatted(name));
	}

}
