package de.svws_nrw.data.schueler;

import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

import java.util.Comparator;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link FoerderschwerpunktEintrag}.
 */
public final class DataKatalogSchuelerFoerderschwerpunkte extends DataManagerRevised<Long, DTOFoerderschwerpunkt, FoerderschwerpunktEintrag> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link FoerderschwerpunktEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogSchuelerFoerderschwerpunkte(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("kuerzelStatistik");
		setAttributesNotPatchable("id", "text");
	}

	@Override
	protected void initDTO(final DTOFoerderschwerpunkt dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.ID = newID;
		dto.Sichtbar = true;
		dto.Sortierung = 32000;
	}

	@Override
	protected long getLongId(final DTOFoerderschwerpunkt dto) {
		return dto.ID;
	}

	@Override
	public FoerderschwerpunktEintrag getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID des Förderschwerpunktes darf nicht null sein.");

		final DTOFoerderschwerpunkt dto = this.conn.queryByKey(DTOFoerderschwerpunkt.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Förderschwerpunkt mit der ID %d gefunden.".formatted(id));

		return map(dto);
	}

	@Override
	public List<FoerderschwerpunktEintrag> getAll() {
		final List<DTOFoerderschwerpunkt> foerderschwerpunkte = this.conn.queryAll(DTOFoerderschwerpunkt.class);
		final Set<Long> idsFoerderschwerpunkte = mapToIds(foerderschwerpunkte);
		final Set<Long> idsOfReferencedFoerderschwerpunkte = idsFoerderschwerpunkte.isEmpty()
				? Collections.emptySet()
				: this.getIdsOfReferencedFoerderschwerpunkte(idsFoerderschwerpunkte);

		return foerderschwerpunkte.stream()
				.map(f -> {
					final FoerderschwerpunktEintrag foerderschwerpunkt = this.map(f);
					foerderschwerpunkt.referenziertInAnderenTabellen = idsOfReferencedFoerderschwerpunkte.contains(f.ID);
					return foerderschwerpunkt;
				})
				.sorted(Comparator.comparing(f -> f.id))
				.toList();
	}

	@Override
	protected FoerderschwerpunktEintrag map(final DTOFoerderschwerpunkt dto) {
		final FoerderschwerpunktEintrag foerderschwerpunkt = new FoerderschwerpunktEintrag();
		foerderschwerpunkt.id = dto.ID;
		foerderschwerpunkt.kuerzel = Objects.requireNonNullElse(dto.Bezeichnung, "");
		foerderschwerpunkt.kuerzelStatistik = Objects.requireNonNullElse(dto.StatistikKrz, "");
		foerderschwerpunkt.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		foerderschwerpunkt.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		return foerderschwerpunkt;
	}

	@Override
	protected void mapAttribute(final DTOFoerderschwerpunkt dto, final String name, final Object value, final Map<String, Object> attributes)
			throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case "kuerzel" -> updateBezeichnung(dto, value, name);
			case "kuerzelStatistik" -> updateKuerzelStatistik(dto, value, name);
			case "istSichtbar" -> updateSichtbar(dto, name, value);
			case "sortierung" -> updateSortierung(dto, name, value);
			case "text" -> {
				// kein mapping, da kein korrelierendes Feld in der Entity
			}
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	@Override
	protected void checkBeforeDeletionWithSimpleOperationResponse(final List<DTOFoerderschwerpunkt> foerderschwerpunkte,
			final Map<Long, SimpleOperationResponse> responses) {
		final Set<Long> idsOfReferencedFoerderschwerpunkte = getIdsOfReferencedFoerderschwerpunkte(mapToIds(foerderschwerpunkte));
		foerderschwerpunkte.stream()
				.filter(f -> idsOfReferencedFoerderschwerpunkte.contains(f.ID))
				.forEach(f -> markResponseAsFailed(responses.get(f.ID), f.Bezeichnung));
	}

	private static void validateId(final DTOFoerderschwerpunkt dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if (!Objects.equals(dto.ID, id))
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
	}

	private static void updateSortierung(final DTOFoerderschwerpunkt dto, final String name, final Object value) throws ApiOperationException {
		dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
	}

	private static void updateSichtbar(final DTOFoerderschwerpunkt dto, final String name, final Object value) throws ApiOperationException {
		dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
	}

	private void updateBezeichnung(final DTOFoerderschwerpunkt dto, final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Foerderschwerpunkt.col_Bezeichnung.datenlaenge(), name);
		if (valueIsBlankOrHasNotChanged(dto.Bezeichnung, bezeichnung))
			return;

		if (bezeichnungIsAlreadyUsed(dto.ID, bezeichnung))
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Kürzel %s darf nicht doppelt vergeben werden".formatted(bezeichnung));

		dto.Bezeichnung = bezeichnung;
	}

	private static void updateKuerzelStatistik(final DTOFoerderschwerpunkt dto, final Object value, final String name) throws ApiOperationException {
		final String kuerzel = JSONMapper.convertToString(value, false, false, Schema.tab_K_Foerderschwerpunkt.col_StatistikKrz.datenlaenge(), name);
		if (valueIsBlankOrHasNotChanged(dto.StatistikKrz, kuerzel))
			return;

		if (noMatchingCoreTypeFound(kuerzel))
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Zum angegebenen Kürzel %s wurde kein passender Förderschwerpunkt gefunden.".formatted(kuerzel));

		dto.StatistikKrz = kuerzel;
	}

	private Set<Long> getIdsOfReferencedFoerderschwerpunkte(final Set<Long> ids) {
		if ((ids == null) || ids.isEmpty())
			return new HashSet<>();
		final String query1 = "SELECT DISTINCT f.Foerderschwerpunkt_ID FROM DTOSchuelerLernabschnittsdaten f WHERE f.Foerderschwerpunkt_ID IN :ids";
		final String query2 = "SELECT DISTINCT g.Foerderschwerpunkt2_ID FROM DTOSchuelerLernabschnittsdaten g WHERE g.Foerderschwerpunkt2_ID IN :ids";
		final String query = String.join("\nUNION\n", query1, query2);
		final List<Long> results = this.conn.query(query, Long.class).setParameter("ids", ids).getResultList();
		return new HashSet<>(results);
	}

	private static void markResponseAsFailed(final SimpleOperationResponse response, final String bezeichnung) {
		response.success = false;
		response.log.add(
				("Der Förderschwerpunkt mit dem Kürzel %s ist in der Datenbank referenziert und kann daher nicht gelöscht werden.").formatted(bezeichnung));
	}

	private static Set<Long> mapToIds(final List<DTOFoerderschwerpunkt> foerderschwerpunkte) {
		return foerderschwerpunkte.stream()
				.map(f -> f.ID)
				.collect(Collectors.toSet());
	}

	private static boolean valueIsBlankOrHasNotChanged(final String oldValue, final String newValue) {
		return Objects.equals(oldValue, newValue) || newValue.isBlank();
	}

	private static boolean noMatchingCoreTypeFound(final String kuerzel) {
		return Foerderschwerpunkt.data().getWertBySchluessel(kuerzel) == null;
	}

	private boolean bezeichnungIsAlreadyUsed(final Long id, final String bezeichnung) {
		return this.conn
				.queryAll(DTOFoerderschwerpunkt.class).stream()
				.anyMatch(f -> (f.ID != id) && bezeichnung.equalsIgnoreCase(f.Bezeichnung));
	}
}
