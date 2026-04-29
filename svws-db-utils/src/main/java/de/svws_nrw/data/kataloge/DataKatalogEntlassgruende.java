package de.svws_nrw.data.kataloge;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.kataloge.KatalogEntlassgrund;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.util.ValidationUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOEntlassarten;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link KatalogEntlassgrund}
 */
public final class DataKatalogEntlassgruende extends DataManagerRevised<Long, DTOEntlassarten, KatalogEntlassgrund> {

	private static final String BEZEICHNUNG = "bezeichnung";

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} mit der angegebenen Verbindung
	 *
	 * @param conn    die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogEntlassgruende(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id", BEZEICHNUNG);
		setAttributesRequiredOnCreation(BEZEICHNUNG);
	}

	@Override
	protected void initDTO(final DTOEntlassarten dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.ID = newID;
	}

	@Override
	protected long getLongId(final DTOEntlassarten dto) {
		return dto.ID;
	}


	/**
	 * Gibt die Entlassgrund-Entität zur Id zurück
	 *
	 * @param id Die Id des Entlassgrundes
	 *
	 * @return {@link DTOEntlassarten}-Eintrag
	 * @throws ApiOperationException wenn die Entlassart nicht gefunden wurde.
	 */
	public DTOEntlassarten getEntityById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für den Entlassgrund darf nicht null sein.");
		}

		final DTOEntlassarten dto = conn.queryByKey(DTOEntlassarten.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Entlassgrund mit der ID %d gefunden.".formatted(id));
		}
		return dto;
	}

	@Override
	public KatalogEntlassgrund getById(final Long id) throws ApiOperationException {
		return map(this.getEntityById(id));
	}

	/**
	 * Gibt die Entlassgrund-Entität zur Bezeichnung zurück
	 *
	 * @param bezeichnungEntlassgrund Die Bezeichnung des Entlassgrundes
	 *
	 * @return {@link DTOEntlassarten}-Eintrag
	 * @throws ApiOperationException wenn keine Bezeichnung übergeben wurde oder diese keinem Entlassgrund eindeutig zugeordnet werden kann.
	 */
	public DTOEntlassarten getEntityByBezeichnung(final String bezeichnungEntlassgrund) {
		if (bezeichnungEntlassgrund == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Keine Bezeichnung für den Entlassgrund übergeben");
		}
		final var result = this.conn.queryList(DTOEntlassarten.QUERY_BY_BEZEICHNUNG, DTOEntlassarten.class, bezeichnungEntlassgrund);
		if (result.size() != 1) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Bezeichnung %s kann nicht eindeutig einer Entlassart zugeordnert werden".formatted(bezeichnungEntlassgrund));
		}
		return result.getFirst();
	}

	/**
	 * Gibt alle Entlassgrunde-Entitäten zurück
	 *
	 * @return sortierte Liste aller {@link DTOEntlassarten}-Einträge
	 */
	public List<DTOEntlassarten> getAllEntities() {
		return this.conn.queryAll(DTOEntlassarten.class);
	}

	@Override
	public List<KatalogEntlassgrund> getAll() {
		final List<DTOEntlassarten> entlassgruende = this.getAllEntities();
		final Set<Long> idsOfReferencedEntlassgruende = this.getIdsOfReferencedEntlassgruende(entlassgruende);
		return entlassgruende
				.stream()
				.map(this::map)
				.map(e -> setReferencedFlag(e, idsOfReferencedEntlassgruende))
				.sorted(Comparator.comparing(e -> e.id))
				.toList();
	}

	@Override
	protected KatalogEntlassgrund map(final DTOEntlassarten dto) {
		final KatalogEntlassgrund entlassgrund = new KatalogEntlassgrund();
		entlassgrund.id = dto.ID;
		entlassgrund.bezeichnung = Objects.requireNonNullElse(dto.Bezeichnung, "");
		entlassgrund.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		entlassgrund.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		return entlassgrund;
	}

	@Override
	protected void mapAttribute(final DTOEntlassarten dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> ValidationUtils.validateId(dto.ID, name, value);
			case BEZEICHNUNG -> updateBezeichnung(dto, value, name);
			case "sortierung" -> updateSortierung(dto, value, name);
			case "istSichtbar" -> updateSichtbar(dto, value, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	@Override
	protected void checkBeforeDeletionWithSimpleOperationResponse(final List<DTOEntlassarten> entlassarten, final Map<Long, SimpleOperationResponse> responses) {
		final Set<Long> idsOfReferencedEntlassarten = getIdsOfReferencedEntlassgruende(entlassarten);
		entlassarten.stream()
				.filter(e -> idsOfReferencedEntlassarten.contains(e.ID))
				.forEach(e -> markResponseAsFailed(responses.get(e.ID), e.Bezeichnung));

	}

	private static void markResponseAsFailed(final SimpleOperationResponse response, final String bezeichnung) {
		response.success = false;
		response.log.add(
				"Der Entlassgrund mit der Bezeichnung %s ist in der Datenbank referenziert und kann daher nicht gelöscht werden.".formatted(bezeichnung)
		);
	}

	private void updateBezeichnung(final DTOEntlassarten dto, final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_EntlassGrund.col_Bezeichnung.datenlaenge(), name);
		if (bezeichnung.isBlank()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine leere Bezeichnung ist nicht gestattet");
		}

		if (bezeichnungIsAlreadyUsed(bezeichnung)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(value));
		}

		dto.Bezeichnung = bezeichnung;
	}

	private static void updateSichtbar(final DTOEntlassarten dto, final Object value, final String name) throws ApiOperationException {
		dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
	}

	private static void updateSortierung(final DTOEntlassarten dto, final Object value, final String name) throws ApiOperationException {
		dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
	}

	private Set<Long> getIdsOfReferencedEntlassgruende(final List<DTOEntlassarten> entlassgruende) {
		if ((entlassgruende == null) || entlassgruende.isEmpty()) {
			return Collections.emptySet();
		}

		final Set<String> bezeichnungen = this.mapToBezeichnung(entlassgruende);
		if (bezeichnungen.isEmpty()) {
			return Collections.emptySet();
		}

		final String query1 = "SELECT DISTINCT a.Entlassgrund FROM DTOSchueler a WHERE a.Entlassgrund IN :bezeichnungen";
		final String query2 = "SELECT DISTINCT a.LSEntlassgrund FROM DTOSchueler a WHERE a.LSEntlassgrund IN :bezeichnungen";
		final String query = String.join("\nUNION\n", query1, query2);

		final Set<String> resultSet = new HashSet<>(
				this.conn
						.query(query, String.class)
						.setParameter("bezeichnungen", bezeichnungen)
						.getResultList()
		);

		return entlassgruende.stream()
				.filter(dto -> resultSet.contains(dto.Bezeichnung))
				.map(dto -> dto.ID)
				.collect(Collectors.toSet());
	}

	private KatalogEntlassgrund setReferencedFlag(final KatalogEntlassgrund entlassgrund, final Set<Long> idsOfReferencedEntlassgruende) {
		entlassgrund.referenziertInAnderenTabellen = idsOfReferencedEntlassgruende.contains(entlassgrund.id);
		return entlassgrund;
	}

	private Set<String> mapToBezeichnung(final List<DTOEntlassarten> entlassgruende) {
		return entlassgruende.stream()
				.filter(f -> (f.Bezeichnung != null) && !f.Bezeichnung.isEmpty() && !f.Bezeichnung.isBlank())
				.map(f -> f.Bezeichnung)
				.collect(Collectors.toSet());
	}

	private boolean bezeichnungIsAlreadyUsed(final String bezeichnung) {
		return this.conn
				.queryAll(DTOEntlassarten.class).stream()
				.anyMatch(f -> bezeichnung.equalsIgnoreCase(f.Bezeichnung));
	}

}
