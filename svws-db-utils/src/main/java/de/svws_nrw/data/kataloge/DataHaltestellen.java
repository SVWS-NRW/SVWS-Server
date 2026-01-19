package de.svws_nrw.data.kataloge;

import de.svws_nrw.core.data.schule.Haltestelle;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOHaltestellen;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Haltestelle}.
 */
public final class DataHaltestellen extends DataManagerRevised<Long, DTOHaltestellen, Haltestelle> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Haltestelle}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataHaltestellen(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation("bezeichnung");
	}

	@Override
	protected void initDTO(final DTOHaltestellen dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected long getLongId(final DTOHaltestellen dto) {
		return dto.ID;
	}

	@Override
	public Haltestelle getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für die Haltestelle darf nicht null sein.");
		}
		final DTOHaltestellen dto = conn.queryByKey(DTOHaltestellen.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Haltestelle mit der ID %d gefunden.".formatted(id));
		}
		return map(dto);
	}

	@Override
	public List<Haltestelle> getAll() {
		final List<DTOHaltestellen> haltestellen = this.conn.queryAll(DTOHaltestellen.class);
		final Set<Long> idsOfReferencedHaltestellen = this.getIdsOfReferencedHaltestellen(mapToIds(haltestellen));

		return haltestellen
				.stream()
				.map(this::map)
				.map(f -> setReferenceFlag(f, idsOfReferencedHaltestellen))
				.sorted(Comparator.comparing(f -> f.id))
				.toList();
	}

	@Override
	protected Haltestelle map(final DTOHaltestellen dto) {
		final Haltestelle haltestelle = new Haltestelle();
		haltestelle.id = dto.ID;
		haltestelle.bezeichnung = dto.Bezeichnung;
		haltestelle.entfernungSchule = dto.EntfernungSchule;
		haltestelle.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		haltestelle.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		return haltestelle;
	}

	@Override
	protected void mapAttribute(final DTOHaltestellen dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case "bezeichnung" -> validateBezeichnung(dto, value, name);
			case "entfernungSchule" -> dto.EntfernungSchule = JSONMapper.convertToDouble(value, true, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void validateId(final DTOHaltestellen dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if (!Objects.equals(dto.ID, id)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
		}
	}

	private void validateBezeichnung(final DTOHaltestellen dto, final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Haltestelle.col_Bezeichnung.datenlaenge(), name);
		if (StringUtils.isBlank(bezeichnung) || Strings.CS.equals(dto.Bezeichnung, bezeichnung)) {
			return;
		}
		validateBezeichnungIsUnique(bezeichnung);
		dto.Bezeichnung = bezeichnung;
	}

	private void validateBezeichnungIsUnique(final String bezeichnung) throws ApiOperationException {
		final boolean bezeichnungAlreadyUsed = this.conn
				.queryAll(DTOHaltestellen.class)
				.stream()
				.anyMatch(h -> Strings.CI.equals(bezeichnung, h.Bezeichnung));
		if (bezeichnungAlreadyUsed) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));
		}
	}

	private Set<Long> getIdsOfReferencedHaltestellen(final Set<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return Collections.emptySet();
		}
		final String query = "SELECT DISTINCT a.Haltestelle_ID FROM DTOSchueler a WHERE a.Haltestelle_ID IN :ids";
		final List<Long> results = this.conn
				.query(query, Long.class)
				.setParameter("ids", ids)
				.getResultList();
		return new HashSet<>(results);
	}

	private static Set<Long> mapToIds(final List<DTOHaltestellen> haltestellen) {
		return haltestellen
				.stream()
				.map(f -> f.ID)
				.collect(Collectors.toSet());
	}

	private static Haltestelle setReferenceFlag(final Haltestelle haltestelle, final Set<Long> idsOfReferencedHaltestellen) {
		haltestelle.referenziertInAnderenTabellen = idsOfReferencedHaltestellen.contains(haltestelle.id);
		return haltestelle;
	}

}
