package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schule.VermerkartEintrag;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOVermerkArt;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link VermerkartEintrag}.
 */
public final class DataVermerkarten extends DataManagerRevised<Long, DTOVermerkArt, VermerkartEintrag> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link VermerkartEintrag}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataVermerkarten(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("bezeichnung");
		setAttributesNotPatchable("id");
	}

	@Override
	protected long getLongId(final DTOVermerkArt dto) {
		return dto.ID;
	}

	@Override
	public VermerkartEintrag map(final DTOVermerkArt dtoVermerkArt) {
		final VermerkartEintrag daten = new VermerkartEintrag();
		daten.id = dtoVermerkArt.ID;
		daten.bezeichnung = dtoVermerkArt.Bezeichnung;
		daten.sortierung = Objects.requireNonNullElse(dtoVermerkArt.Sortierung, 32000);
		daten.istSichtbar = Boolean.TRUE.equals(dtoVermerkArt.Sichtbar);
		return daten;
	}

	@Override
	public List<VermerkartEintrag> getAll() {
		final List<DTOVermerkArt> vermerkarten = conn.queryAll(DTOVermerkArt.class);
		final Set<Long> idsVermerkarten = this.mapToIds(vermerkarten);
		final Set<Long> idsOfReferencedVermerkarten = this.getIdsOfReferencedVermerkarten(idsVermerkarten);

		return vermerkarten
				.stream()
				.map(this::map)
				.map(v -> setReferenceFlag(v, idsOfReferencedVermerkarten))
				.sorted(Comparator.comparing(v -> v.id))
				.toList();
	}

	@Override
	public VermerkartEintrag getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID der Vermerkart darf nicht null sein.");

		final DTOVermerkArt vermerkArt = conn.queryByKey(DTOVermerkArt.class, id);
		if (vermerkArt == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Vermerkart mit der ID %d wurde nicht gefunden.".formatted(id));

		return map(vermerkArt);
	}

	@Override
	protected void initDTO(final DTOVermerkArt dtoVermerkArt, final Long vermerkartId, final Map<String, Object> initAttributes) {
		dtoVermerkArt.ID = vermerkartId;
		dtoVermerkArt.Sortierung = 32000;
	}


	@Override
	protected void mapAttribute(final DTOVermerkArt dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case "bezeichnung" -> updateBezeichnung(dto, value, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void validateId(final DTOVermerkArt dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if (!Objects.equals(dto.ID, id))
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
	}

	private void updateBezeichnung(final DTOVermerkArt dto, final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Vermerkart.col_Bezeichnung.datenlaenge(), name);
		if (valueIsBlankOrHasNotChanged(dto.Bezeichnung, bezeichnung))
			return;

		validateBezeichnung(dto.ID, bezeichnung);

		dto.Bezeichnung = bezeichnung;
	}

	private Set<Long> mapToIds(final List<DTOVermerkArt> vermerkarten) {
		return vermerkarten.stream()
				.map(f -> f.ID)
				.collect(Collectors.toSet());
	}

	private VermerkartEintrag setReferenceFlag(final VermerkartEintrag vermerkart, final Set<Long> idsOfReferencedVermerkarten) {
		vermerkart.referenziertInAnderenTabellen = idsOfReferencedVermerkarten.contains(vermerkart.id);
		return vermerkart;
	}

	private Set<Long> getIdsOfReferencedVermerkarten(final Set<Long> ids) {
		if ((ids == null) || ids.isEmpty())
			return Collections.emptySet();

		final String query = "SELECT DISTINCT a.VermerkArt_ID FROM DTOSchuelerVermerke a WHERE a.VermerkArt_ID IN :ids";
		final List<Long> results = this.conn.query(query, Long.class).setParameter("ids", ids).getResultList();
		return new HashSet<>(results);
	}

	private static boolean valueIsBlankOrHasNotChanged(final String oldValue, final String newValue) {
		return Objects.equals(oldValue, newValue) || ((newValue != null) && newValue.isBlank());
	}

	private void validateBezeichnung(final Long id, final String bezeichnung) throws ApiOperationException {
		final boolean isAlreadyUsed = this.conn
				.queryAll(DTOVermerkArt.class).stream()
				.anyMatch(e -> (e.ID != id) && bezeichnung.equalsIgnoreCase(e.Bezeichnung));
		if (isAlreadyUsed)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));
	}

}
