package de.svws_nrw.data.kataloge;

import de.svws_nrw.core.data.schule.Fahrschuelerart;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFahrschuelerart;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Fahrschuelerart}.
 */
public final class DataKatalogFahrschuelerarten extends DataManagerRevised<Long, DTOFahrschuelerart, Fahrschuelerart> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Fahrschuelerart}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogFahrschuelerarten(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation("bezeichnung");
	}

	@Override
	protected void initDTO(final DTOFahrschuelerart dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.ID = newID;
	}

	@Override
	protected long getLongId(final DTOFahrschuelerart dto) {
		return dto.ID;
	}

	@Override
	public Fahrschuelerart getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für die Fahrschülerart darf nicht null sein.");

		final DTOFahrschuelerart dto = conn.queryByKey(DTOFahrschuelerart.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Fahrschülerart mit der ID %d gefunden.".formatted(id));

		return map(dto);
	}

	@Override
	public List<Fahrschuelerart> getAll() {
		final List<DTOFahrschuelerart> fahrschuelerarten = this.conn.queryAll(DTOFahrschuelerart.class);
		return fahrschuelerarten.stream().map(this::map).toList();
	}

	@Override
	protected Fahrschuelerart map(final DTOFahrschuelerart dto) {
		final Fahrschuelerart fahrschuelerart = new Fahrschuelerart();
		fahrschuelerart.id = dto.ID;
		fahrschuelerart.bezeichnung = dto.Bezeichnung;
		fahrschuelerart.istSichtbar = (dto.Sichtbar == null) || dto.Sichtbar;
		fahrschuelerart.istAenderbar = (dto.Aenderbar == null) || dto.Aenderbar;
		fahrschuelerart.sortierung = (dto.Sortierung == null) ? -1 : dto.Sortierung;
		return fahrschuelerart;
	}

	@Override
	protected void mapAttribute(final DTOFahrschuelerart dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, name);
				if (!Objects.equals(dto.ID, id))
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
			}
			case "bezeichnung" -> updateBezeichnung(dto, value, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			case "istAenderbar" -> dto.Aenderbar = JSONMapper.convertToBoolean(value, false, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private void updateBezeichnung(final DTOFahrschuelerart dto, final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_FahrschuelerArt.col_Bezeichnung.datenlaenge(), name);
		if (Objects.equals(dto.Bezeichnung, bezeichnung) || bezeichnung.isBlank())
			return;

		final boolean bezeichnungAlreadyUsed = this.conn.queryAll(DTOFahrschuelerart.class).stream()
				.anyMatch(f -> (f.ID != dto.ID) && f.Bezeichnung.equalsIgnoreCase(bezeichnung));
		if (bezeichnungAlreadyUsed)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));

		dto.Bezeichnung = bezeichnung;
	}

}
