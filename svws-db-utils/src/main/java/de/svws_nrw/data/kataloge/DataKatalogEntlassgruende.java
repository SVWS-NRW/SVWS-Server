package de.svws_nrw.data.kataloge;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import de.svws_nrw.core.data.kataloge.KatalogEntlassgrund;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOEntlassarten;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link KatalogEntlassgrund}
 */
public final class DataKatalogEntlassgruende extends DataManagerRevised<Long, DTOEntlassarten, KatalogEntlassgrund> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} mit der angegebenen Verbindung
	 *
	 * @param conn    die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogEntlassgruende(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation("bezeichnung");
	}

	@Override
	protected void initDTO(final DTOEntlassarten dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.ID = newID;
	}

	@Override
	protected long getLongId(final DTOEntlassarten dto) {
		return dto.ID;
	}

	@Override
	public KatalogEntlassgrund getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für den Entlassgrund darf nicht null sein.");

		final DTOEntlassarten dto = conn.queryByKey(DTOEntlassarten.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Entlassgrund mit der ID %d gefunden.".formatted(id));

		return map(dto);
	}

	@Override
	public List<KatalogEntlassgrund> getAll() {
		final List<DTOEntlassarten> entlassgruende = this.conn.queryAll(DTOEntlassarten.class);
		return entlassgruende.stream().map(this::map).toList();
	}

	@Override
	protected KatalogEntlassgrund map(final DTOEntlassarten dto) {
		final KatalogEntlassgrund entlassgrund = new KatalogEntlassgrund();
		entlassgrund.id = dto.ID;
		entlassgrund.bezeichnung = dto.Bezeichnung;
		entlassgrund.sortierung = Optional.ofNullable(dto.Sortierung).orElse(-1);
		entlassgrund.istSichtbar = Optional.ofNullable(dto.Sichtbar).orElse(true);
		entlassgrund.istAenderbar = Optional.ofNullable(dto.Aenderbar).orElse(false);
		return entlassgrund;
	}

	@Override
	protected void mapAttribute(final DTOEntlassarten dto, final String name, final Object value, final Map<String, Object> map)
		throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, name);
				if (!Objects.equals(dto.ID, id))
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
			}
			case "bezeichnung" -> updateBezeichnung(dto, value, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, true, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, true, name);
			case "istAenderbar" -> dto.Aenderbar = JSONMapper.convertToBoolean(value, true, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private void updateBezeichnung(final DTOEntlassarten dto, final Object value, final String name) throws ApiOperationException {
		final String bezeichnung =
				JSONMapper.convertToString(value, false, false, Schema.tab_K_EntlassGrund.col_Bezeichnung.datenlaenge(), name);
		if (Objects.equals(dto.Bezeichnung, bezeichnung) || bezeichnung.isBlank())
			return;

		final boolean bezeichnungAlreadyUsed = this.conn.queryAll(DTOEntlassarten.class).stream()
				.anyMatch(e -> (e.ID != dto.ID) && e.Bezeichnung.equalsIgnoreCase(bezeichnung));
		if (bezeichnungAlreadyUsed)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(value));

		dto.Bezeichnung = bezeichnung;
	}
}
