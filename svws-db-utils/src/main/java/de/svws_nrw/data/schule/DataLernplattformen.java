package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schule.Lernplattform;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLernplattform;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattform;
import de.svws_nrw.db.dto.current.svws.auth.DTOLernplattformen;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Lernplattform}.
 */
public final class DataLernplattformen extends DataManagerRevised<Long, DTOLernplattformen, Lernplattform> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Lernplattform}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLernplattformen(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("bezeichnung");
		setAttributesNotPatchable("id");
	}

	@Override
	protected void initDTO(final DTOLernplattformen dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected long getLongId(final DTOLernplattformen lernplattform) {
		return lernplattform.ID;
	}

	@Override
	public Lernplattform getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID der Lernplattform darf nicht null sein.");

		final DTOLernplattformen lernplattform = conn.queryByKey(DTOLernplattformen.class, id);
		if (lernplattform == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Lernplattform mit der ID %d wurde nicht gefunden.".formatted(id));

		return map(lernplattform);
	}

	@Override
	public Lernplattform map(final DTOLernplattformen dto) {
		final Lernplattform daten = new Lernplattform();
		daten.id = dto.ID;
		daten.bezeichnung = Objects.requireNonNullElse(dto.Bezeichnung, "");
		return daten;
	}

	@Override
	public List<Lernplattform> getAll() {
		final List<DTOLernplattformen> lernplattformen = this.conn.queryAll(DTOLernplattformen.class);
		final Set<Long> idsLernplattformen = this.mapToIds(lernplattformen);
		final Set<Long> idsOfReferencedLernplattformen = this.getIdsOfReferencedLernplattformen(idsLernplattformen);

		return lernplattformen
				.stream()
				.map(this::map)
				.map(l -> setReferencedFlag(l, idsOfReferencedLernplattformen))
				.sorted(Comparator.comparing(l -> l.id))
				.toList();
	}

	@Override
	protected Lernplattform addBasic(final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		final Lernplattform lernplattform = super.addBasic(newID, initAttributes);
		persistLernplattformen("SELECT e.ID FROM DTOSchueler e", id -> new DTOSchuelerLernplattform(id, lernplattform.id, false, false, false, false));
		persistLernplattformen("SELECT e.ID FROM DTOLehrer e", id -> new DTOLehrerLernplattform(id, lernplattform.id, false, false, false, false));
		return lernplattform;
	}

	private <T> void persistLernplattformen(final String query, final Function<Long, T> dtoMapper) {
		final List<T> lernplattformen = this.conn.queryList(query, Long.class)
				.stream()
				.map(dtoMapper)
				.toList();

		this.conn.transactionPersistAll(lernplattformen);
		this.conn.transactionFlush();
	}

	@Override
	protected void mapAttribute(final DTOLernplattformen dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case "bezeichnung" -> validateBezeichnung(dto, value, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void validateId(final DTOLernplattformen dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if (!Objects.equals(dto.ID, id))
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
	}

	private Lernplattform setReferencedFlag(final Lernplattform lernplattform, final Set<Long> idsOfReferencedLernplattformen) {
		lernplattform.referenziertInAnderenTabellen = idsOfReferencedLernplattformen.contains(lernplattform.id);
		return lernplattform;
	}

	private Set<Long> getIdsOfReferencedLernplattformen(final Set<Long> ids) {
		if ((ids == null) || ids.isEmpty())
			return Collections.emptySet();

		final String query1 = "SELECT DISTINCT a.LernplattformID FROM DTOSchuelerLernplattform a WHERE a.LernplattformID in :ids";
		final String query2 = "SELECT DISTINCT b.LernplattformID FROM DTOLehrerLernplattform b WHERE b.LernplattformID in :ids";
		final String query = String.join("\nUNION\n", query1, query2);
		final List<Long> results = this.conn.query(query, Long.class).setParameter("ids", ids).getResultList();
		return new HashSet<>(results);
	}

	private Set<Long> mapToIds(final List<DTOLernplattformen> lernplattformen) {
		return lernplattformen
				.stream()
				.map(l -> l.ID)
				.collect(Collectors.toSet());
	}

	private void validateBezeichnung(final DTOLernplattformen dto, final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(
				value, false, false, Schema.tab_Lernplattformen.col_Bezeichnung.datenlaenge(), name);
		if (valueIsBlankOrHasNotChanged(dto.Bezeichnung, bezeichnung))
			return;

		if (bezeichnungIsAlreadyUsed(dto.ID, bezeichnung))
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));

		dto.Bezeichnung = bezeichnung;
	}

	private boolean bezeichnungIsAlreadyUsed(final Long id, final String bezeichnung) {
		return this.conn.queryAll(DTOLernplattformen.class)
				.stream()
				.anyMatch(l -> (l.ID != id) && bezeichnung.equalsIgnoreCase(l.Bezeichnung));
	}

	private static boolean valueIsBlankOrHasNotChanged(final String oldValue, final String newValue) {
		return Objects.equals(oldValue, newValue) || ((newValue != null) && newValue.isBlank());
	}

}
