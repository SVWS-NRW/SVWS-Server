package de.svws_nrw.data.kataloge;

import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
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
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link OrtsteilKatalogEintrag}.
 */
public final class DataOrtsteile extends DataManagerRevised<Long, DTOOrtsteil, OrtsteilKatalogEintrag> {

	private final DataOrte dataOrte;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link OrtsteilKatalogEintrag}.
	 *
	 * @param conn   		die Datenbank-Verbindung für den Datenbankzugriff
	 * @param dataOrte   	DataOrte
	 */
	public DataOrtsteile(final DBEntityManager conn, final DataOrte dataOrte) {
		super(conn);
		this.dataOrte = dataOrte;
		setAttributesNotPatchable("id", "referenziertInAnderenTabellen");
		setAttributesRequiredOnCreation("ort_id", "ortsteil");
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
		daten.ort_id = dto.Ort_ID;
		daten.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		daten.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		daten.istAenderbar = Boolean.TRUE.equals(dto.Aenderbar);
		return daten;
	}

	@Override
	public OrtsteilKatalogEintrag getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID des Ortsteils darf nicht null sein.");

		final DTOOrtsteil ortsteil = conn.queryByKey(DTOOrtsteil.class, id);
		if (ortsteil == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Der Ortsteil mit der ID %d wurde nicht gefunden.".formatted(id));

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
		final Map<Long, OrtKatalogEintrag> orteById = this.dataOrte
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
		final OrtKatalogEintrag ort = orteById.get(ortsteil.ort_id);
		if (ort != null) {
			ortsteil.bezeichnungOrt = ort.ortsname;
			ortsteil.plzOrt = ort.plz;
		}
		return ortsteil;
	}

	@Override
	protected void mapAttribute(final DTOOrtsteil dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case "ortsteil" -> updateBezeichnung(dto, value, name);
			case "ort_id" -> updateOrt(dto, value, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			case "bezeichnungOrt", "plzOrt" -> {
				// kein mapping -> Die Felder sind Teil des Partials, um die Werte nach Creation und Patch in der Auswahlliste im Frontend anzuzeigen
			}
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private void updateOrt(final DTOOrtsteil dto, final Object value, final String name) throws ApiOperationException {
		final Long idOrt = JSONMapper.convertToLong(value, false, name);
		if (Objects.equals(idOrt, dto.Ort_ID))
			return;
		final DTOOrt ort = this.conn.queryByKey(DTOOrt.class, idOrt);
		if (ort == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Es wurde kein Ort mit der ID %d gefunden.".formatted(idOrt));

		dto.Ort_ID = idOrt;
	}

	private void updateBezeichnung(final DTOOrtsteil dto, final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Ortsteil.col_Bezeichnung.datenlaenge(), name);
		if (valueIsBlankOrHasNotChanged(dto.Bezeichnung, bezeichnung))
			return;

		validateBezeichnung(dto.ID, bezeichnung);

		dto.Bezeichnung = bezeichnung;
	}

	private static boolean valueIsBlankOrHasNotChanged(final String oldValue, final String newValue) {
		return StringUtils.isBlank(newValue) || Strings.CS.equals(oldValue, newValue);
	}

	private void validateBezeichnung(final Long id, final String bezeichnung) throws ApiOperationException {
		final boolean isAlreadyUsed = this.conn
				.queryAll(DTOOrtsteil.class).stream()
				.anyMatch(e -> (e.ID != id) && Strings.CI.equals(bezeichnung, e.Bezeichnung));
		if (isAlreadyUsed)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung des Ortsteil %s ist bereits vorhanden.".formatted(bezeichnung));
	}


	private static void validateId(final DTOOrtsteil dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if (!Objects.equals(dto.ID, id))
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
	}


	private static Set<Long> mapToIds(final List<DTOOrtsteil> ortsteile) {
		return ortsteile.stream()
				.map(f -> f.ID)
				.collect(Collectors.toSet());
	}

	private Set<Long> getIdsOfReferencedOrtsteile(final Set<Long> ids) {
		if ((ids == null) || ids.isEmpty())
			return Collections.emptySet();

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
