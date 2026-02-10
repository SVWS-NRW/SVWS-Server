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
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

import static de.svws_nrw.db.schema.Schema.tab_K_Ort;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link OrtKatalogEintrag}.
 */
public final class DataOrte extends DataManagerRevised<Long, DTOOrt, OrtKatalogEintrag> {

	private static final String PLZ = "plz";
	private static final String ORTSNAME = "ortsname";

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link OrtKatalogEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataOrte(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation(ORTSNAME, PLZ);
	}

	@Override
	public void checkBeforeCreation(final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		validateOrtsnameisUniqueForThisPlzOnCreation(newID, initAttributes);
	}

	@Override
	protected void initDTO(final DTOOrt dto, final Long newId, final Map<String, Object> initAttributes) {
		dto.ID = newId;
	}

	@Override
	protected long getLongId(final DTOOrt dto) {
		return dto.ID;
	}

	@Override
	public OrtKatalogEintrag getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die ID des Orts darf nicht null sein.");
		}

		final DTOOrt dto = this.conn.queryByKey(DTOOrt.class, id);
		if (dto == null) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde kein Ort mit der ID %d gefunden.".formatted(id));
		}

		return map(dto);
	}

	@Override
	public List<OrtKatalogEintrag> getAll() {
		final List<DTOOrt> orte = conn.queryAll(DTOOrt.class);
		final Set<Long> idsOfReferencedOrte = this.getIdsOfReferencedOrte(mapToIds(orte));

		return orte
				.stream()
				.map(this::map)
				.map(o -> setReferenceFlag(o, idsOfReferencedOrte))
				.sorted(Comparator.comparing(o -> o.id))
				.toList();
	}

	@Override
	protected OrtKatalogEintrag map(final DTOOrt dto) {
		final OrtKatalogEintrag daten = new OrtKatalogEintrag();
		daten.id = dto.ID;
		daten.plz = dto.PLZ;
		daten.ortsname = dto.Bezeichnung;
		daten.kreis = dto.Kreis;
		daten.kuerzelBundesland = dto.Land;
		daten.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		daten.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		daten.istAenderbar = Boolean.TRUE.equals(dto.Aenderbar);
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOOrt dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case PLZ -> dto.PLZ = JSONMapper.convertToString(value, false, false, tab_K_Ort.col_PLZ.datenlaenge(), name);
			case ORTSNAME -> updateOrtsname(dto, name, value, map);
			case "kreis" -> dto.Kreis = JSONMapper.convertToString(value, true, true, tab_K_Ort.col_Kreis.datenlaenge(), name);
			case "kuerzelBundesland" -> dto.Land = JSONMapper.convertToString(value, true, true, tab_K_Ort.col_Land.datenlaenge(), name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, true, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			case "istAenderbar" -> dto.Aenderbar = JSONMapper.convertToBoolean(value, true, name);
			default ->
					throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	@Override
	protected void checkBeforeDeletionWithSimpleOperationResponse(final List<DTOOrt> orte, final Map<Long, SimpleOperationResponse> responses) {
		final Set<Long> idsOfReferencedOrte = getIdsOfReferencedOrte(mapToIds(orte));
		orte.stream()
				.filter(o -> idsOfReferencedOrte.contains(o.ID))
				.forEach(o -> markResponseAsFailed(responses.get(o.ID), o.Bezeichnung));
	}

	private void validateOrtsnameisUniqueForThisPlzOnCreation(final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		final String plz = JSONMapper.convertToString(initAttributes.get(PLZ), false, false, tab_K_Ort.col_PLZ.datenlaenge(), PLZ);
		final String ortsname =
				JSONMapper.convertToString(initAttributes.get(ORTSNAME), false, false, Schema.tab_K_Datenschutz.col_Bezeichnung.datenlaenge(), ORTSNAME);
		validateNameIsUniqueForPlz(newID, plz, ortsname);
	}

	private static void markResponseAsFailed(final SimpleOperationResponse response, final String name) {
		response.success = false;
		response.log.add(("Der Ort mit dem Name %s ist in der Datenbank referenziert und kann daher nicht gelöscht werden.").formatted(name));
	}

	private void updateOrtsname(final DTOOrt dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		final String newOrtsname = JSONMapper.convertToString(value, false, false, tab_K_Ort.col_Bezeichnung.datenlaenge(), name);
		if (valueIsBlankOrHasNotChanged(dto.Bezeichnung, newOrtsname)) {
			return;
		}

		final String plz = getPlz(dto, map);
		validateNameIsUniqueForPlz(dto.ID, plz, newOrtsname);

		dto.Bezeichnung = newOrtsname;
	}

	private static String getPlz(final DTOOrt dto, final Map<String, Object> map) throws ApiOperationException {
		// patch
		if (dto.PLZ != null) {
			return dto.PLZ;
		}
		//create
		return JSONMapper.convertToString(map.get(PLZ), false, false, tab_K_Ort.col_PLZ.datenlaenge(), PLZ);
	}

	private void validateNameIsUniqueForPlz(final Long id, final String plz, final String ortsname) throws ApiOperationException {
		final boolean isAlreadyUsed = this.conn.queryAll(DTOOrt.class).stream()
				.anyMatch(dto -> (dto.ID != id) && Strings.CI.equals(plz, dto.PLZ) && Strings.CI.equals(ortsname, dto.Bezeichnung));
		if (isAlreadyUsed) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Der Ortsname %s ist bereits vorhanden.".formatted(ortsname));
		}
	}

	private static boolean valueIsBlankOrHasNotChanged(final String oldValue, final String newValue) {
		return StringUtils.isBlank(newValue) || Strings.CS.equals(oldValue, newValue);
	}

	private static void validateId(final DTOOrt dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if (!Objects.equals(dto.ID, id)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
		}
	}

	private static Set<Long> mapToIds(final List<DTOOrt> orte) {
		return orte.stream()
				.map(o -> o.ID)
				.collect(Collectors.toSet());
	}

	private Set<Long> getIdsOfReferencedOrte(final Set<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return Collections.emptySet();
		}

		final String lehrer = "SELECT DISTINCT a.Ort_ID FROM DTOLehrer a WHERE a.Ort_ID IN :ids";
		final String schueler = "SELECT DISTINCT b.Ort_ID FROM DTOSchueler b WHERE b.Ort_ID IN :ids";
		final String erzieher = "SELECT DISTINCT c.ErzOrt_ID FROM DTOSchuelerErzieherAdresse c WHERE c.ErzOrt_ID IN :ids";
		final String betriebe = "SELECT DISTINCT d.ort_id FROM DTOBetrieb d WHERE d.ort_id IN :ids";
		final String ortsteile = "SELECT DISTINCT e.Ort_ID FROM DTOOrtsteil e WHERE e.Ort_ID IN :ids";
		final String query = String.join("\nUNION ALL\n", lehrer, schueler, erzieher, betriebe, ortsteile);
		final List<Long> results = this.conn.query(query, Long.class).setParameter("ids", ids).getResultList();
		return new HashSet<>(results);
	}

	private OrtKatalogEintrag setReferenceFlag(final OrtKatalogEintrag ort, final Set<Long> idsOfReferencedOrte) {
		ort.referenziertInAnderenTabellen = idsOfReferencedOrte.contains(ort.id);
		return ort;
	}

}
