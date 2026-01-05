package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schule.Kindergarten;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOKindergarten;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.Strings;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Kindergarten}.
 */
public final class DataKindergaerten extends DataManagerRevised<Long, DTOKindergarten, Kindergarten> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Kindergarten}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKindergaerten(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id", "referenziertInAnderenTabellen");
		setAttributesRequiredOnCreation("bezeichnung");
	}

	@Override
	protected void initDTO(final DTOKindergarten dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected long getLongId(final DTOKindergarten dto) {
		return dto.ID;
	}

	@Override
	public Kindergarten getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die ID für den Kindergarten darf nicht null sein.");
		}
		final DTOKindergarten dto = conn.queryByKey(DTOKindergarten.class, id);
		if (dto == null) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde kein Kindergarten mit der ID %d gefunden.".formatted(id));
		}
		return map(dto);
	}

	@Override
	public Kindergarten map(final DTOKindergarten dto) {
		final Kindergarten kindergarten = new Kindergarten();
		kindergarten.id = dto.ID;
		kindergarten.bezeichnung = dto.Bezeichnung;
		kindergarten.plz = dto.PLZ;
		kindergarten.ort = dto.Ort;
		kindergarten.strassenname = dto.Strassenname;
		kindergarten.hausNr = dto.HausNr;
		kindergarten.hausNrZusatz = dto.HausNrZusatz;
		kindergarten.tel = dto.Tel;
		kindergarten.email = dto.Email;
		kindergarten.bemerkung = dto.Bemerkung;
		kindergarten.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		kindergarten.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		return kindergarten;
	}

	@Override
	public List<Kindergarten> getAll() {
		final List<DTOKindergarten> kindergaerten = conn.queryAll(DTOKindergarten.class);
		final Set<Long> idsOfReferencedKindergaerten = this.getIdsOfReferencedKindergaerten(mapToIds(kindergaerten));

		return kindergaerten
				.stream()
				.map(this::map)
				.map(k -> setReferenceFlag(k, idsOfReferencedKindergaerten))
				.sorted(Comparator.comparing(k -> k.id))
				.toList();
	}

	@Override
	protected void mapAttribute(final DTOKindergarten dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case "bezeichnung" -> updateBezeichnung(dto, name, value);
			case "bemerkung" -> dto.Bemerkung = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_Bemerkung.datenlaenge(), name);
			case "tel" -> dto.Tel = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_Tel.datenlaenge(), name);
			case "email" -> dto.Email = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_Email.datenlaenge(), name);
			case "strassenname" ->
					dto.Strassenname = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_Strassenname.datenlaenge(), name);
			case "hausNr" -> dto.HausNr = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_HausNr.datenlaenge(), name);
			case "hausNrZusatz" ->
					dto.HausNrZusatz = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_HausNrZusatz.datenlaenge(), name);
			case "plz" -> dto.PLZ = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_PLZ.datenlaenge(), name);
			case "ort" -> dto.Ort = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_Ort.datenlaenge(), name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void validateId(final DTOKindergarten dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, true, name);
		if (!Objects.equals(dto.ID, id)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
		}
	}

	private void updateBezeichnung(final DTOKindergarten dto, final String name, final Object value) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Kindergarten.col_Bezeichnung.datenlaenge(), name);
		if (valueIsBlankOrHasNotChanged(dto.Bezeichnung, bezeichnung)) {
			return;
		}
		validateBezeichnungIsUnique(dto.ID, bezeichnung);

		dto.Bezeichnung = bezeichnung;
	}

	private void validateBezeichnungIsUnique(final Long id, final String bezeichnung) throws ApiOperationException {
		final boolean bezeichnungAlreadyUsed = this.conn
				.queryAll(DTOKindergarten.class)
				.stream()
				.anyMatch(e -> (e.ID != id) && Strings.CI.equals(bezeichnung, e.Bezeichnung));

		if (bezeichnungAlreadyUsed) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));
		}
	}

	private static boolean valueIsBlankOrHasNotChanged(final String oldValue, final String newValue) {
		return Objects.equals(oldValue, newValue) || ((newValue != null) && newValue.isBlank());
	}

	private static Set<Long> mapToIds(final List<DTOKindergarten> kindergaerten) {
		return kindergaerten
				.stream()
				.map(k -> k.ID)
				.collect(Collectors.toSet());
	}

	private Set<Long> getIdsOfReferencedKindergaerten(final Set<Long> ids) {
		if ((ids == null) || (ids.isEmpty())) {
			return Collections.emptySet();
		}
		final String query = "SELECT DISTINCT s.Kindergarten_ID FROM DTOSchueler s WHERE s.Kindergarten_ID IN :ids";
		final List<Long> results = this.conn
				.query(query, Long.class)
				.setParameter("ids", ids)
				.getResultList();

		return new HashSet<>(results);
	}

	private static Kindergarten setReferenceFlag(final Kindergarten kindergarten, final Set<Long> ids) {
		kindergarten.referenziertInAnderenTabellen = ids.contains(kindergarten.id);
		return kindergarten;
	}

}
