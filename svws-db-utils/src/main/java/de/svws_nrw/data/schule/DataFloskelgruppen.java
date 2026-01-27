package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.asd.types.schule.Floskelgruppenart;
import de.svws_nrw.core.data.schule.Floskelgruppe;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.katalog.DTOFloskelgruppen;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import org.apache.commons.lang3.Strings;

import static jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Floskelgruppe}.
 */
public final class DataFloskelgruppen extends DataManagerRevised<Long, DTOFloskelgruppen, Floskelgruppe> {

	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden soll
	 */
	public DataFloskelgruppen(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation("kuerzel", "bezeichnung");
	}

	@Override
	protected void initDTO(final DTOFloskelgruppen dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.ID = newID;
	}

	@Override
	protected long getLongId(final DTOFloskelgruppen dto) throws ApiOperationException {
		return dto.ID;
	}

	@Override
	public List<Floskelgruppe> getAll() {
		final List<DTOFloskelgruppen> floskelgruppen = conn.queryAll(DTOFloskelgruppen.class);
		final Set<Long> idsFloskelgruppen = this.mapToIds(floskelgruppen);
		final Set<Long> idsOfReferencedFloskelgruppen = this.getIdsOfReferencedFloskelgruppen(idsFloskelgruppen);

		return floskelgruppen.stream()
				.map(this::map)
				.map(floskelgruppe -> setReferenceFlag(floskelgruppe, idsOfReferencedFloskelgruppen))
				.sorted(Comparator.comparing(floskelgruppe -> floskelgruppe.id))
				.toList();
	}

	@Override
	public Floskelgruppe getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Floskelgruppe mit der ID null ist unzulässig.");
		}
		final DTOFloskelgruppen dto = conn.queryByKey(DTOFloskelgruppen.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Die Floskelgruppe mit der ID %d wurde nicht gefunden.".formatted(id));
		}
		return map(dto);
	}

	@Override
	protected Floskelgruppe map(final DTOFloskelgruppen dto) {
		final Floskelgruppe floskelgruppe = new Floskelgruppe();
		floskelgruppe.id = dto.ID;
		floskelgruppe.kuerzel = Objects.requireNonNullElse(dto.Kuerzel, "");
		floskelgruppe.bezeichnung = Objects.requireNonNullElse(dto.Bezeichnung, "");
		floskelgruppe.idFloskelgruppenart = dto.Hauptgruppe_ID;
		return floskelgruppe;
	}

	@Override
	protected void mapAttribute(final DTOFloskelgruppen dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case "kuerzel" -> updateKuerzel(dto, name, value);
			case "bezeichnung" -> updateBezeichnung(dto, name, value);
			case "idFloskelgruppenart" -> updateFloskelgruppenart(dto, name, value);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void validateId(final DTOFloskelgruppen dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if (!Objects.equals(dto.ID, id)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
		}
	}

	private void updateBezeichnung(final DTOFloskelgruppen dto, final String name, final Object value) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_Floskelgruppen.col_Bezeichnung.datenlaenge(), name);
		if (valueIsBlankOrHasNotChanged(dto.Bezeichnung, bezeichnung)) {
			return;
		}

		validateBezeichnung(dto.ID, bezeichnung);
		dto.Bezeichnung = bezeichnung;
	}

	private void updateKuerzel(final DTOFloskelgruppen dto, final String name, final Object value) throws ApiOperationException {
		final String kuerzel = JSONMapper.convertToString(value, false, false, Schema.tab_Katalog_Floskeln_Gruppen.col_Kuerzel.datenlaenge(), name);
		if (valueIsBlankOrHasNotChanged(dto.Kuerzel, kuerzel)) {
			return;
		}

		validateKuerzel(dto.ID, kuerzel);
		dto.Kuerzel = kuerzel;
	}

	private static void updateFloskelgruppenart(final DTOFloskelgruppen dto, final String name, final Object value) throws ApiOperationException {
		final Long idFloskelgruppenart = JSONMapper.convertToLong(value, true, name);
		if (idFloskelgruppenart == null) {
			dto.Hauptgruppe_ID = null;
			return;
		}
		if (Objects.equals(idFloskelgruppenart, dto.Hauptgruppe_ID)) {
			return;
		}
		if (matchingFloskelgruppenartNotFound(idFloskelgruppenart)) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Floskelgruppenart zur ID %d gefunden.".formatted(idFloskelgruppenart));
		}

		dto.Hauptgruppe_ID = idFloskelgruppenart;
	}

	private static boolean matchingFloskelgruppenartNotFound(final long id) {
		return Floskelgruppenart.data().getEintragByID(id) == null;
	}

	private Set<Long> mapToIds(final List<DTOFloskelgruppen> floskelgruppen) {
		return floskelgruppen
				.stream()
				.map(floskelgruppe -> floskelgruppe.ID)
				.collect(Collectors.toSet());
	}

	private Set<Long> getIdsOfReferencedFloskelgruppen(final Set<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return Collections.emptySet();
		}

		final String query = "SELECT DISTINCT f.Gruppe_ID FROM DTOFloskeln f WHERE f.Gruppe_ID IN :ids";
		final List<Long> results = this.conn
				.query(query, Long.class)
				.setParameter("ids", ids)
				.getResultList();
		return new HashSet<>(results);
	}

	private Floskelgruppe setReferenceFlag(final Floskelgruppe floskelgruppe, final Set<Long> idsOfReferencedFloskelgruppen) {
		floskelgruppe.referenziertInAnderenTabellen = idsOfReferencedFloskelgruppen.contains(floskelgruppe.id);
		return floskelgruppe;
	}

	private static boolean valueIsBlankOrHasNotChanged(final String oldValue, final String newValue) {
		return Objects.equals(oldValue, newValue) || ((newValue != null) && newValue.isBlank());
	}

	private void validateKuerzel(final Long id, final String kuerzel) throws ApiOperationException {
		final boolean kuerzelAlreadyUsed = this.conn
				.queryAll(DTOFloskelgruppen.class)
				.stream()
				.anyMatch(f -> (f.ID != id) && Strings.CI.equals(kuerzel, f.Kuerzel));

		if (kuerzelAlreadyUsed) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Kürzel %s ist bereits vorhanden.".formatted(kuerzel));
		}
	}

	private void validateBezeichnung(final Long id, final String bezeichnung) throws ApiOperationException {
		final boolean bezeichnungAlreadyUsed = this.conn
				.queryAll(DTOFloskelgruppen.class)
				.stream()
				.anyMatch(f -> (f.ID != id) && Strings.CI.equals(bezeichnung, f.Bezeichnung));

		if (bezeichnungAlreadyUsed) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));
		}
	}

}
