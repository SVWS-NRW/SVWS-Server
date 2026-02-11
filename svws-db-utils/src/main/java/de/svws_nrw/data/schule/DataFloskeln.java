package de.svws_nrw.data.schule;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schule.Floskel;
import de.svws_nrw.core.data.schule.FloskelJahrgangZuordnung;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.katalog.DTOFloskelgruppen;
import de.svws_nrw.db.dto.current.katalog.DTOFloskeln;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Floskel}.
 */
public final class DataFloskeln extends DataManagerRevised<Long, DTOFloskeln, Floskel> {

	private final DataFloskelJahrgangZuordnung dataZuordnung;

	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn 				die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden soll
	 * @param dataZuordnung 	DataFloskelJahrgangZuordnung
	 */
	public DataFloskeln(final DBEntityManager conn, final DataFloskelJahrgangZuordnung dataZuordnung) {
		super(conn);
		this.dataZuordnung = dataZuordnung;
		setAttributesRequiredOnCreation("kuerzel");
		setAttributesNotPatchable("id");
		setAttributesDelayedOnCreation("idsJahrgaenge");
	}

	@Override
	protected void initDTO(final DTOFloskeln dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.ID = newID;
	}

	@Override
	protected long getLongId(final DTOFloskeln dto) throws ApiOperationException {
		return dto.ID;
	}

	@Override
	public Floskel getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die ID der Floskel darf nicht null sein.");

		final DTOFloskeln dto = this.conn.queryByKey(DTOFloskeln.class, id);
		if (dto == null)
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde keine Floskel mit der ID %d gefunden.".formatted(id));

		return map(dto);
	}

	@Override
	public List<Floskel> getAll() {
		return this.conn
				.queryAll(DTOFloskeln.class).stream()
				.map(this::map)
				.toList();
	}

	@Override
	protected Floskel map(final DTOFloskeln dto) {
		final Floskel floskel = new Floskel();
		floskel.id = dto.ID;
		floskel.kuerzel = dto.Kuerzel;
		floskel.text = dto.Text;
		floskel.idFloskelgruppe = dto.Gruppe_ID;
		floskel.idFach = dto.Fach_ID;
		floskel.niveau = dto.Niveau;
		floskel.idsJahrgaenge = getIdsJahrgaenge(dto.ID);
		floskel.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		floskel.sortierung = (dto.Sortierung == null) ? 32000 : dto.Sortierung;
		return floskel;
	}

	@Override
	protected void mapAttribute(final DTOFloskeln dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "kuerzel" -> updateKuerzel(dto, value, name);
			case "text" -> updateText(dto, name, value);
			case "idFloskelgruppe" -> updateIdFloskelgruppe(dto, name, value);
			case "idFach" -> updateFach(dto, name, value);
			case "niveau" -> updateNiveau(dto, name, value);
			case "idsJahrgaenge" -> updateIdsJahrgaenge(dto, name, value);
			case "istSichtbar" -> updateSichtbar(dto, name, value);
			case "sortierung" -> updateSortierung(dto, name, value);
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private List<Long> getIdsJahrgaenge(final long idFloskel) {
		return this.dataZuordnung
				.getListByIdFloskel(idFloskel).stream()
				.map(fj -> fj.idJahrgang)
				.toList();
	}

	private void updateKuerzel(final DTOFloskeln dto, final Object value, final String name) throws ApiOperationException {
		final String kuerzel = JSONMapper.convertToString(value, false, false, Schema.tab_Katalog_Floskeln.col_Kuerzel.datenlaenge(), name);
		if (Objects.equals(dto.Kuerzel, kuerzel) || kuerzel.isBlank())
			return;

		final boolean alreadyUsed = this.conn
				.queryAll(DTOFloskeln.class).stream()
				.anyMatch(f -> (f.ID != dto.ID) && kuerzel.equalsIgnoreCase(f.Kuerzel));

		if (alreadyUsed)
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Das Kürzel %s wird bereits verwendet.".formatted(kuerzel));

		dto.Kuerzel = kuerzel;
	}

	private static void updateText(final DTOFloskeln dto, final String name, final Object value) throws ApiOperationException {
		dto.Text = JSONMapper.convertToString(value, false, false, null, name);
	}

	private void updateIdFloskelgruppe(final DTOFloskeln dto, final String name, final Object value) throws ApiOperationException {
		final Long idFloskelgruppe = JSONMapper.convertToLong(value, false, name);
		if (Objects.equals(idFloskelgruppe, dto.Gruppe_ID))
			return;

		if (matchingFloskelgruppeNotFound(idFloskelgruppe))
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Keine Floskelgruppe mit der id %d vorhanden.".formatted(idFloskelgruppe));

		dto.Gruppe_ID = idFloskelgruppe;
	}

	private boolean matchingFloskelgruppeNotFound(final Long id) {
		return (this.conn.queryByKey(DTOFloskelgruppen.class, id) == null);
	}

	private void updateFach(final DTOFloskeln dto, final String name, final Object value) throws ApiOperationException {
		final Long idFach = JSONMapper.convertToLong(value, true, name);
		if (idFach == null) {
			dto.Fach_ID = null;
			return;
		}

		if (this.conn.queryByKey(DTOFach.class, idFach) == null)
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Kein Fach mit der ID %d gefunden.".formatted(idFach));

		dto.Fach_ID = idFach;
	}

	private void updateIdsJahrgaenge(final DTOFloskeln dto, final String name, final Object value) throws ApiOperationException {
		final List<Long> ids = JSONMapper.convertToListOfLong(value, false, name);
		final Set<Long> newIdsJahrgaenge = new HashSet<>(ids);
		if (ids.size() != newIdsJahrgaenge.size())
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Liste der neuen JahrgangIDs darf keine Duplikate enthalten.");

		final Map<Long, FloskelJahrgangZuordnung> zuordnungenById = this.dataZuordnung
				.getListByIdFloskel(dto.ID).stream()
				.collect(Collectors.toMap(
						z -> z.idJahrgang,
						Function.identity()
				));

		final Set<Long> oldIdsJahrgaenge = Set.copyOf(zuordnungenById.keySet());
		if (newIdsJahrgaenge.equals(oldIdsJahrgaenge))
			return;

		deleteObsoleteZuordnungen(oldIdsJahrgaenge, newIdsJahrgaenge, zuordnungenById);
		addNewZuordnungen(oldIdsJahrgaenge, newIdsJahrgaenge, dto.ID);
	}

	private void deleteObsoleteZuordnungen(final Set<Long> oldIdsJahrgaenge, final Set<Long> newIdsJahrgaenge, final Map<Long, FloskelJahrgangZuordnung> zuordnungenByIdJahrgang)
			throws ApiOperationException {
		for (final Long oldIdJahrgang: oldIdsJahrgaenge) {
			if (!newIdsJahrgaenge.contains(oldIdJahrgang)) {
				this.dataZuordnung.delete(zuordnungenByIdJahrgang.get(oldIdJahrgang).id);
			}
		}
	}

	private void addNewZuordnungen(final Set<Long> oldIds, final Set<Long> newIds, final Long idFloskel) throws ApiOperationException {
		for (final Long newId: newIds) {
			if (!oldIds.contains(newId)) {
				this.dataZuordnung.add(Map.of("idFloskel", idFloskel, "idJahrgang", newId));
			}
		}
	}

	private static void updateSortierung(final DTOFloskeln dto, final String name, final Object value) throws ApiOperationException {
		dto.Sortierung = JSONMapper.convertToIntegerInRange(value, true, 0, null, name);
	}

	private static void updateSichtbar(final DTOFloskeln dto, final String name, final Object value) throws ApiOperationException {
		dto.Sichtbar = JSONMapper.convertToBoolean(value, true, name);
	}

	private static void updateNiveau(final DTOFloskeln dto, final String name, final Object value) throws ApiOperationException {
		dto.Niveau = JSONMapper.convertToInteger(value, true, name);
	}

}
