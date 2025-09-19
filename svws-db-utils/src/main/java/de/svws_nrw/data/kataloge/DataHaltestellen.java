package de.svws_nrw.data.kataloge;

import de.svws_nrw.core.data.schule.Haltestelle;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOHaltestellen;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Haltestelle}.
 */
public final class DataHaltestellen extends DataManagerRevised<Long, DTOHaltestellen, Haltestelle> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Haltestelle}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataHaltestellen(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation("bezeichnung");
	}

	@Override
	protected void initDTO(final DTOHaltestellen dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.ID = newID;
	}

	@Override
	protected long getLongId(final DTOHaltestellen dto) {
		return dto.ID;
	}

	@Override
	public Haltestelle getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für die Haltestelle darf nicht null sein.");

		final DTOHaltestellen dto = conn.queryByKey(DTOHaltestellen.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Haltestelle mit der ID %d gefunden.".formatted(id));

		return map(dto);
	}

	@Override
	public List<Haltestelle> getAll() {
		final List<DTOHaltestellen> haltestellen = this.conn.queryAll(DTOHaltestellen.class);
		return haltestellen.stream().map(this::map).toList();
	}

	@Override
	protected Haltestelle map(final DTOHaltestellen dto) {
		final Haltestelle haltestelle = new Haltestelle();
		haltestelle.id = dto.ID;
		haltestelle.bezeichnung = dto.Bezeichnung;
		haltestelle.entfernungSchule = dto.EntfernungSchule;
		haltestelle.istSichtbar = (dto.Sichtbar == null) || dto.Sichtbar;
		haltestelle.istAenderbar = (dto.Aenderbar == null) || dto.Aenderbar;
		haltestelle.sortierung = (dto.Sortierung == null) ? -1 : dto.Sortierung;
		return haltestelle;
	}

	@Override
	protected void mapAttribute(final DTOHaltestellen dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, name);
				if (!Objects.equals(dto.ID, id))
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
			}
			case "bezeichnung" -> updateBezeichnung(dto, value, name);
			case "entfernungSchule" -> dto.EntfernungSchule = JSONMapper.convertToDouble(value, true, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			case "istAenderbar" -> dto.Aenderbar = JSONMapper.convertToBoolean(value, false, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private void updateBezeichnung(final DTOHaltestellen dto, final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Haltestelle.col_Bezeichnung.datenlaenge(), name);
		if (Objects.equals(dto.Bezeichnung, bezeichnung) || bezeichnung.isBlank())
			return;

		final boolean bezeichnungAlreadyUsed = this.conn.queryAll(DTOHaltestellen.class).stream()
				.anyMatch(h -> (h.ID != dto.ID) && h.Bezeichnung.equalsIgnoreCase(bezeichnung));
		if (bezeichnungAlreadyUsed)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));

		dto.Bezeichnung = bezeichnung;
	}
}
