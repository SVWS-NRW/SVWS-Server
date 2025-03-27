package de.svws_nrw.data.schule;

import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.types.schule.Religion;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKonfession;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link ReligionEintrag}.
 */
public final class DataReligionen extends DataManagerRevised<Long, DTOKonfession, ReligionEintrag> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link ReligionEintrag}.
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
	public List<ReligionEintrag> getAll() {
		return conn.queryAll(DTOKonfession.class).stream().map(this::map).toList();
	}

	@Override
	public ReligionEintrag getById(final Long id) throws ApiOperationException {
		final DTOKonfession religion = conn.queryByKey(DTOKonfession.class, id);
		if (religion == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Eintrag im Katalog der Religionen mit der ID %d gefunden.".formatted(id));

		return map(religion);
	}

	@Override
	protected ReligionEintrag map(final DTOKonfession dto) {
		final ReligionEintrag daten = new ReligionEintrag();
		daten.id = dto.ID;
		daten.bezeichnung = dto.Bezeichnung;
		daten.bezeichnungZeugnis = dto.ZeugnisBezeichnung;
		daten.kuerzel = dto.StatistikKrz;
		daten.sortierung = (dto.Sortierung == null) ? 32000 : dto.Sortierung;
		daten.istSichtbar = (dto.Sichtbar == null) || dto.Sichtbar;
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOKonfession dto, final String name, final Object patchValue, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "kuerzel" -> {
				final String statistikKrz = JSONMapper.convertToString(patchValue, true, true, Schema.tab_K_Religion.col_StatistikKrz.datenlaenge());
				if ((statistikKrz != null) && (Religion.data().getWertByKuerzel(statistikKrz) == null)) {
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Eine Religion mit dem Kürzel " + statistikKrz + " existiert in der amtlichen Schulstatistik nicht.");
				}
				dto.StatistikKrz = statistikKrz;
			}
			case "bezeichnung" -> {
				final String bezeichnung = JSONMapper.convertToString(patchValue, false, true, Schema.tab_K_Religion.col_Bezeichnung.datenlaenge());
				if (bezeichnungExists(bezeichnung)) {
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Eine Religion mit der Bezeichnung " + bezeichnung + " existiert bereits. Die Bezeichnung muss eindeutig sein.");
				}
				dto.Bezeichnung = bezeichnung;
			}
			case "bezeichnungZeugnis" ->
					dto.ZeugnisBezeichnung = JSONMapper.convertToString(patchValue, true, true, Schema.tab_K_Religion.col_ZeugnisBezeichnung.datenlaenge());
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(patchValue, true);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(patchValue, true);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Für das Attribut %s existiert kein Patch Mapping.".formatted(name));
		}
	}

	@Override
	protected long getLongId(final DTOKonfession dbDTO) {
		return dbDTO.ID;
	}

	/**
	 * Methode prüft, ob bereits ein DTOKonfession Eintrag mit der gegebenen Bezeichnung in der DB existiert.
	 *
	 * @param bezeichnung zu prüfende Bezeichnung
	 * @return <code>true</code>, wenn es bereits ein DTOKonfession mit der Bezeichnung in der DB existiert, ansonsten <code>false</code>
	 */
	boolean bezeichnungExists(final String bezeichnung) {
		return !conn.query(DTOKonfession.QUERY_BY_BEZEICHNUNG, DTOKonfession.class)
				.setParameter(1, bezeichnung)
				.getResultList()
				.isEmpty();
	}
}
