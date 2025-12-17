package de.svws_nrw.data.kataloge;

import de.svws_nrw.core.data.schule.Fahrschuelerart;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFahrschuelerart;
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
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Fahrschuelerart}.
 */
public final class DataFahrschuelerarten extends DataManagerRevised<Long, DTOFahrschuelerart, Fahrschuelerart> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Fahrschuelerart}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataFahrschuelerarten(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation("bezeichnung");
	}

	@Override
	protected void initDTO(final DTOFahrschuelerart dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.ID = newID;
	}

	@Override
	protected long getLongId(final DTOFahrschuelerart dto) {
		return dto.ID;
	}

	@Override
	public Fahrschuelerart getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für die Fahrschülerart darf nicht null sein.");
		}
		final DTOFahrschuelerart dto = conn.queryByKey(DTOFahrschuelerart.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Fahrschülerart mit der ID %d gefunden.".formatted(id));
		}
		return map(dto);
	}

	@Override
	public List<Fahrschuelerart> getAll() {
		final List<DTOFahrschuelerart> fahrschuelerarten = this.conn.queryAll(DTOFahrschuelerart.class);
		final Set<Long> idsOfReferencedFahrschuelerarten = this.getIdsOfReferencedFahrschuelerarten(mapToIds(fahrschuelerarten));

		return fahrschuelerarten
				.stream()
				.map(this::map)
				.map(f -> setReferenceFlag(f, idsOfReferencedFahrschuelerarten))
				.sorted(Comparator.comparing(f -> f.id))
				.toList();
	}

	@Override
	protected Fahrschuelerart map(final DTOFahrschuelerart dto) {
		final Fahrschuelerart fahrschuelerart = new Fahrschuelerart();
		fahrschuelerart.id = dto.ID;
		fahrschuelerart.bezeichnung = dto.Bezeichnung;
		fahrschuelerart.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		fahrschuelerart.istAenderbar = Boolean.TRUE.equals(dto.Aenderbar);
		fahrschuelerart.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		return fahrschuelerart;
	}

	@Override
	protected void mapAttribute(final DTOFahrschuelerart dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case "bezeichnung" -> updateBezeichnung(dto, value, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			case "istAenderbar" -> dto.Aenderbar = JSONMapper.convertToBoolean(value, false, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void validateId(final DTOFahrschuelerart dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if (!Objects.equals(dto.ID, id)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
		}
	}

	private void updateBezeichnung(final DTOFahrschuelerart dto, final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_FahrschuelerArt.col_Bezeichnung.datenlaenge(), name);
		if (StringUtils.isBlank(bezeichnung) || Strings.CS.equals(dto.Bezeichnung, bezeichnung)) {
			return;
		}
		final boolean bezeichnungAlreadyUsed = this.conn
				.queryAll(DTOFahrschuelerart.class)
				.stream()
				.anyMatch(f -> (f.ID != dto.ID) && Strings.CI.equals(bezeichnung, f.Bezeichnung));
		if (bezeichnungAlreadyUsed) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));
		}
		dto.Bezeichnung = bezeichnung;
	}

	private static Set<Long> mapToIds(final List<DTOFahrschuelerart> fahrschuelerarten) {
		return fahrschuelerarten
				.stream()
				.map(f -> f.ID)
				.collect(Collectors.toSet());
	}

	private Set<Long> getIdsOfReferencedFahrschuelerarten(final Set<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return Collections.emptySet();
		}

		final String query = "SELECT DISTINCT a.Fahrschueler_ID FROM DTOSchueler a WHERE a.Fahrschueler_ID IN :ids";
		final List<Long> results = this.conn
				.query(query, Long.class)
				.setParameter("ids", ids)
				.getResultList();
		return new HashSet<>(results);
	}

	private static Fahrschuelerart setReferenceFlag(final Fahrschuelerart fahrschuelerart, final Set<Long> idsOfReferencedFahrschuelerarten) {
		fahrschuelerart.referenziertInAnderenTabellen = idsOfReferencedFahrschuelerarten.contains(fahrschuelerart.id);
		return fahrschuelerart;
	}

}
