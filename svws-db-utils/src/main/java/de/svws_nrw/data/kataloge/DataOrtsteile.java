package de.svws_nrw.data.kataloge;

import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.util.ValidationUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schule.katalog.ort.OrtService;
import jakarta.ws.rs.core.Response.Status;
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
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link OrtsteilKatalogEintrag}.
 */
public final class DataOrtsteile extends DataManagerRevised<Long, DTOOrtsteil, OrtsteilKatalogEintrag> {

	private static final String ORT_ID = "idOrt";
	private static final String ORTSTEIL = "ortsteil";
	private final OrtService ortService;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link OrtsteilKatalogEintrag}.
	 *
	 * @param conn        die Datenbank-Verbindung für den Datenbankzugriff
	 * @param ortService    DataOrte
	 */
	public DataOrtsteile(final DBEntityManager conn, final OrtService ortService) {
		super(conn);
		this.ortService = ortService;
		setAttributesNotPatchable("id", "referenziertInAnderenTabellen");
		setAttributesRequiredOnCreation(ORT_ID, ORTSTEIL);
	}

	@Override
	protected long getLongId(final DTOOrtsteil dto) {
		return dto.ID;
	}

	@Override
	public OrtsteilKatalogEintrag map(final DTOOrtsteil dto) {
		final OrtsteilKatalogEintrag daten = new OrtsteilKatalogEintrag();
		daten.id = dto.ID;
		daten.ortsteil = dto.Bezeichnung;
		daten.idOrt = dto.Ort_ID;
		daten.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		daten.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		daten.istAenderbar = Boolean.TRUE.equals(dto.Aenderbar);
		return daten;
	}

	@Override
	public OrtsteilKatalogEintrag getById(final Long id) {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID des Ortsteils darf nicht null sein.");
		}

		final DTOOrtsteil ortsteil = conn.queryByKey(DTOOrtsteil.class, id);
		if (ortsteil == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Der Ortsteil mit der ID %d wurde nicht gefunden.".formatted(id));
		}

		return map(ortsteil);
	}

	@Override
	protected void initDTO(final DTOOrtsteil dto, final Long newId, final Map<String, Object> initAttributes) {
		dto.ID = newId;
		dto.Sortierung = 32000;
	}

	@Override
	public List<OrtsteilKatalogEintrag> getAll() {
		final List<DTOOrtsteil> ortsteile = this.conn.queryAll(DTOOrtsteil.class);
		final Set<Long> idsOrtsteile = mapToIds(ortsteile);
		final Set<Long> idsOfReferencedOrtsteile = this.getIdsOfReferencedOrtsteile(idsOrtsteile);
		final Map<Long, OrtKatalogEintrag> orteById = this.ortService
				.getAll()
				.stream()
				.collect(Collectors.toMap(ort -> ort.id, ort -> ort));

		return ortsteile
				.stream()
				.map(this::map)
				.map(ortsteil -> setReferenceFlag(ortsteil, idsOfReferencedOrtsteile))
				.map(ortsteil -> setOrtFields(ortsteil, orteById))
				.sorted(Comparator.comparing(ortsteil -> ortsteil.id))
				.toList();
	}

	private static OrtsteilKatalogEintrag setOrtFields(final OrtsteilKatalogEintrag ortsteil, final Map<Long, OrtKatalogEintrag> orteById) {
		// Zum Anzeigen in der Auswahlliste im Frontend
		final OrtKatalogEintrag ort = orteById.get(ortsteil.idOrt);
		if (ort != null) {
			ortsteil.bezeichnungOrt = ort.ortsname;
			ortsteil.plzOrt = ort.plz;
		}
		return ortsteil;
	}

	@Override
	protected void mapAttribute(final DTOOrtsteil dto, final String name, final Object value, final Map<String, Object> map) {
		switch (name) {
			case "id" -> ValidationUtils.validateId(dto.ID, name, value);
			case ORTSTEIL -> updateBezeichnung(dto, value, map, name);
			case ORT_ID -> updateOrt(dto, value, map, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			case "bezeichnungOrt", "plzOrt" -> {
				// kein mapping -> Die Felder sind Teil des Partials, um die Werte nach Creation und Patch in der Auswahlliste im Frontend anzuzeigen
			}
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private void updateOrt(final DTOOrtsteil dto, final Object value, final Map<String, Object> map, final String name) {
		final Long idOrt = JSONMapper.convertToLong(value, false, name);
		if (Objects.equals(idOrt, dto.Ort_ID)) {
			return;
		}
		validateOrt(dto, idOrt, map);

		dto.Ort_ID = idOrt;
	}


	private void updateBezeichnung(final DTOOrtsteil dto, final Object value, final Map<String, Object> map, final String name) {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Ortsteil.col_Bezeichnung.datenlaenge(), name);
		if (ValidationUtils.isBlankOrUnchanged(dto.Bezeichnung, bezeichnung)) {
			return;
		}
		validateBezeichnung(dto, bezeichnung, map);

		dto.Bezeichnung = bezeichnung;
	}

	private void validateBezeichnung(final DTOOrtsteil dto, final String bezeichnung, final Map<String, Object> map) {
		final Long idOrt = getIdOrtForValidation(dto, map);
		if (isBezeichnungAlreadyUsed(dto, bezeichnung, idOrt)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Bezeichnung des Ortsteils '%s' ist bereits vorhanden.".formatted(bezeichnung));
		}
	}

	private void validateOrt(final DTOOrtsteil dto, final Long idOrt, final Map<String, Object> map) {
		if (this.conn.queryByKey(DTOOrt.class, idOrt) == null) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es wurde kein Ort mit der ID %d gefunden.".formatted(idOrt));
		}
		final String bezeichnung = getBezeichnungForValidation(dto, map);
		if (isBezeichnungAlreadyUsed(dto, bezeichnung, idOrt)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Bezeichnung des Ortsteils '%s' ist bereits vorhanden.".formatted(bezeichnung));
		}
	}

	private Long getIdOrtForValidation(final DTOOrtsteil dto, final Map<String, Object> map) {
		final boolean isPatch = this.conn.queryByKey(DTOOrtsteil.class, dto.ID) != null;
		return isPatch
				? dto.Ort_ID
				: JSONMapper.convertToLong(map.get(ORT_ID), false, ORT_ID);
	}

	private String getBezeichnungForValidation(final DTOOrtsteil dto, final Map<String, Object> map) {
		final boolean isPatch = this.conn.queryByKey(DTOOrtsteil.class, dto.ID) != null;
		return isPatch
				? dto.Bezeichnung
				: JSONMapper.convertToString(map.get(ORTSTEIL), false, false,
						Schema.tab_K_Ortsteil.col_Bezeichnung.datenlaenge(), ORTSTEIL);
	}

	private boolean isBezeichnungAlreadyUsed(final DTOOrtsteil dto, final String bezeichnung, final Long idOrt) {
		return this.conn.queryAll(DTOOrtsteil.class).stream()
				.anyMatch(e -> !Objects.equals(e.ID, dto.ID)
						&& Objects.equals(e.Ort_ID, idOrt)
						&& Strings.CI.equals(bezeichnung, e.Bezeichnung));
	}


	private static Set<Long> mapToIds(final List<DTOOrtsteil> ortsteile) {
		return ortsteile.stream()
				.map(f -> f.ID)
				.collect(Collectors.toSet());
	}

	private Set<Long> getIdsOfReferencedOrtsteile(final Set<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return Collections.emptySet();
		}

		final String lehrer = "SELECT DISTINCT a.Ortsteil_ID FROM DTOLehrer a WHERE a.Ortsteil_ID IN :ids";
		final String schueler = "SELECT DISTINCT b.Ortsteil_ID FROM DTOSchueler b WHERE b.Ortsteil_ID IN :ids";
		final String erzieher = "SELECT DISTINCT c.ErzOrtsteil_ID FROM DTOSchuelerErzieherAdresse c WHERE c.ErzOrtsteil_ID IN :ids";
		final String betriebe = "SELECT DISTINCT d.ortsteil_id FROM DTOBetrieb d WHERE d.ortsteil_id IN :ids";
		final String query = String.join("\nUNION ALL\n", lehrer, schueler, erzieher, betriebe);
		final List<Long> results = this.conn.query(query, Long.class).setParameter("ids", ids).getResultList();
		return new HashSet<>(results);
	}

	private static OrtsteilKatalogEintrag setReferenceFlag(final OrtsteilKatalogEintrag ortsteil, final Set<Long> idsOfReferencedOrtsteile) {
		ortsteil.referenziertInAnderenTabellen = idsOfReferencedOrtsteile.contains(ortsteil.id);
		return ortsteil;
	}

}
