package de.svws_nrw.data.betriebe;

import de.svws_nrw.core.data.betrieb.Beschaeftigungsart;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOBeschaeftigungsart;
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
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Beschaeftigungsart}.
 */
public final class DataBeschaeftigungsarten extends DataManagerRevised<Long, DTOBeschaeftigungsart, Beschaeftigungsart> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Beschaeftigungsart}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataBeschaeftigungsarten(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id", "referenziertInAnderenTabellen");
		setAttributesRequiredOnCreation("bezeichnung");
	}

	@Override
	protected void initDTO(final DTOBeschaeftigungsart dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.ID = newID;
	}

	@Override
	protected long getLongId(final DTOBeschaeftigungsart dto) {
		return dto.ID;
	}

	@Override
	public Beschaeftigungsart getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für die Beschäftigungsart darf nicht null sein.");
		}
		final DTOBeschaeftigungsart dto = conn.queryByKey(DTOBeschaeftigungsart.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Beschäftigungsart mit der ID %d gefunden.".formatted(id));
		}
		return map(dto);
	}

	@Override
	public List<Beschaeftigungsart> getAll() {
		final List<DTOBeschaeftigungsart> beschaeftigungsarten = this.conn.queryAll(DTOBeschaeftigungsart.class);
		final Set<Long> idsOfReferencedBeschaeftigungsarten = this.getIdsOfReferencedBeschaeftigungsarten(mapToIds(beschaeftigungsarten));

		return beschaeftigungsarten
				.stream()
				.map(this::map)
				.map(f -> setReferenceFlag(f, idsOfReferencedBeschaeftigungsarten))
				.sorted(Comparator.comparing(f -> f.id))
				.toList();
	}

	@Override
	protected Beschaeftigungsart map(final DTOBeschaeftigungsart dto) {
		final Beschaeftigungsart beschaeftigungsart = new Beschaeftigungsart();
		beschaeftigungsart.id = dto.ID;
		beschaeftigungsart.bezeichnung = dto.Bezeichnung;
		beschaeftigungsart.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		beschaeftigungsart.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		return beschaeftigungsart;
	}

	@Override
	protected void mapAttribute(final DTOBeschaeftigungsart dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case "bezeichnung" -> validateBezeichnung(dto, value, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, true, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, true, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void validateId(final DTOBeschaeftigungsart dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if (!Objects.equals(dto.ID, id)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
		}
	}

	private void validateBezeichnung(final DTOBeschaeftigungsart dto, final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(
				value, false, false, Schema.tab_K_BeschaeftigungsArt.col_Bezeichnung.datenlaenge(), name);
		if (StringUtils.isBlank(bezeichnung) || Strings.CS.equals(dto.Bezeichnung, bezeichnung)) {
			return;
		}
		validateBezeichnungIsUnique(bezeichnung);
		dto.Bezeichnung = bezeichnung;
	}

	private void validateBezeichnungIsUnique(final String bezeichnung) throws ApiOperationException {
		final boolean bezeichnungAlreadyUsed = this.conn
				.queryAll(DTOBeschaeftigungsart.class)
				.stream()
				.anyMatch(h -> Strings.CI.equals(bezeichnung, h.Bezeichnung));
		if (bezeichnungAlreadyUsed) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));
		}
	}

	private Set<Long> getIdsOfReferencedBeschaeftigungsarten(final Set<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return Collections.emptySet();
		}
		final String query = "SELECT DISTINCT a.Vertragsart_ID FROM DTOSchuelerAllgemeineAdresse a WHERE a.Vertragsart_ID IN :ids";
		final List<Long> results = this.conn
				.query(query, Long.class)
				.setParameter("ids", ids)
				.getResultList();
		return new HashSet<>(results);
	}

	private static Set<Long> mapToIds(final List<DTOBeschaeftigungsart> beschaeftigungsarten) {
		return beschaeftigungsarten
				.stream()
				.map(f -> f.ID)
				.collect(Collectors.toSet());
	}

	private static Beschaeftigungsart setReferenceFlag(final Beschaeftigungsart beschaeftigungsart, final Set<Long> idsOfReferencedBeschaeftigungsarten) {
		beschaeftigungsart.referenziertInAnderenTabellen = idsOfReferencedBeschaeftigungsarten.contains(beschaeftigungsart.id);
		return beschaeftigungsart;
	}
}
