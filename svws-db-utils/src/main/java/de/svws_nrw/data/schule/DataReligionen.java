package de.svws_nrw.data.schule;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.asd.types.schule.Religion;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKonfession;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link ReligionEintrag}.
 */
public final class DataReligionen extends DataManagerRevised<Long, DTOKonfession, ReligionEintrag> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link ReligionEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataReligionen(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation("kuerzel", "bezeichnung");
	}

	@Override
	protected void initDTO(final DTOKonfession dto, final Long newId, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.ID = newId;
	}

	@Override
	protected long getLongId(final DTOKonfession dbDTO) {
		return dbDTO.ID;
	}

	@Override
	public List<ReligionEintrag> getAll() {
		return conn.queryAll(DTOKonfession.class).stream().map(this::map).toList();
	}

	@Override
	public ReligionEintrag getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Eine Anfrage mit der ID null ist unzulässig.");

		final DTOKonfession religion = conn.queryByKey(DTOKonfession.class, id);
		if (religion == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Eintrag im Katalog der Religionen mit der ID %d gefunden.".formatted(id));

		return map(religion);
	}

	@Override
	protected ReligionEintrag map(final DTOKonfession dto) {
		final ReligionEintrag daten = new ReligionEintrag();
		daten.id = dto.ID;
		daten.bezeichnung = (dto.Bezeichnung == null) ? "" : dto.Bezeichnung;
		daten.bezeichnungZeugnis = dto.ZeugnisBezeichnung;
		daten.kuerzel = dto.StatistikKrz;
		daten.sortierung = (dto.Sortierung == null) ? 32000 : dto.Sortierung;
		daten.istSichtbar = (dto.Sichtbar == null) || dto.Sichtbar;
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOKonfession dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, name);
				if (!Objects.equals(dto.ID, id))
					throw new ApiOperationException(Response.Status.BAD_REQUEST,
							"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
			}
			case "kuerzel" -> {
				final String statistikKrz = JSONMapper.convertToString(value, true, true, Schema.tab_K_Religion.col_StatistikKrz.datenlaenge(), name);
				if ((statistikKrz != null) && (Religion.data().getWertByKuerzel(statistikKrz) == null)) {
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Eine Religion mit dem Kürzel " + statistikKrz + " existiert in der amtlichen Schulstatistik nicht.");
				}
				dto.StatistikKrz = statistikKrz;
			}
			case "bezeichnung" -> validateBezeichnung(dto, name, value);
			case "bezeichnungZeugnis" ->
					dto.ZeugnisBezeichnung = JSONMapper.convertToString(value, true, true, Schema.tab_K_Religion.col_ZeugnisBezeichnung.datenlaenge(), name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, true, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, true, name);
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private void validateBezeichnung(final DTOKonfession dto, final String name, final Object value) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Religion.col_Bezeichnung.datenlaenge(), name);
		if (Objects.equals(dto.Bezeichnung, bezeichnung) || bezeichnung.isBlank())
			return;

		final boolean bezeichnungAlreadyUsed = this.conn.queryAll(DTOKonfession.class).stream()
				.anyMatch(e -> (e.ID != dto.ID) && e.Bezeichnung.equalsIgnoreCase(bezeichnung));

		if (bezeichnungAlreadyUsed)
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));

		dto.Bezeichnung =  bezeichnung;
	}
}
