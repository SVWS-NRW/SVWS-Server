package de.svws_nrw.data.schule;

import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.schule.Kindergarten;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOKindergarten;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO {@link Kindergarten}.
 */
public final class DataKatalogKindergaerten extends DataManagerRevised<Long, DTOKindergarten, Kindergarten> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link Kindergarten}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogKindergaerten(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("bezeichnung");
	}

	@Override
	public Kindergarten map(final DTOKindergarten dto) {
		final Kindergarten daten = new Kindergarten();
		daten.id = dto.ID;
		daten.bezeichnung = (dto.Bezeichnung == null) ? "" : dto.Bezeichnung;
		daten.plz = (dto.PLZ == null) ? "" : dto.PLZ;
		daten.ort = (dto.Ort == null) ? "" : dto.Ort;
		daten.strassenname = (dto.Strassenname == null) ? "" : dto.Strassenname;
		daten.hausNr = (dto.HausNr == null) ? "" : dto.HausNr;
		daten.hausNrZusatz = (dto.HausNrZusatz == null) ? "" : dto.HausNrZusatz;
		daten.tel = (dto.Tel == null) ? "" : dto.Tel;
		daten.email = (dto.Email == null) ? "" : dto.Email;
		daten.bemerkung = (dto.Bemerkung == null) ? "" : dto.Bemerkung;
		daten.istSichtbar = (dto.Sichtbar == null) || dto.Sichtbar;
		daten.sortierung = dto.Sortierung;
		return daten;
	}

	@Override
	public List<Kindergarten> getAll() throws ApiOperationException {
		final List<DTOKindergarten> mapKindergarten = conn.queryAll(DTOKindergarten.class);
		return mapKindergarten.stream().map(this::map).toList();
	}

	@Override
	public Kindergarten getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Eine Anfrage zu einen Kindergarten mit der ID null ist unzulässig.");
		final DTOKindergarten kindergarten = conn.queryByKey(DTOKindergarten.class, id);
		if (kindergarten == null)
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Der Kindergarten mit der ID %d wurde nicht gefunden.".formatted(id));
		return map(kindergarten);
	}

	@Override
	protected void initDTO(final DTOKindergarten dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
		dto.Bezeichnung = "";
		dto.Sichtbar = true;
		dto.Sortierung = 32000;
	}

	@Override
	protected void mapAttribute(final DTOKindergarten dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long patch_id = JSONMapper.convertToLong(value, true, name);
				if ((patch_id == null) || (patch_id != dto.ID))
					throw new ApiOperationException(Response.Status.BAD_REQUEST,
							"Die angegebene ID %d ist null oder stimmt nicht mit der ID %d im DTO überein.".formatted(patch_id, dto.ID));
			}
			case "bezeichnung" ->
					dto.Bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Kindergarten.col_Bezeichnung.datenlaenge(), name);
			case "plz" -> dto.PLZ = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_PLZ.datenlaenge(), name);
			case "ort" -> dto.Ort = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_Ort.datenlaenge(), name);
			case "strassenname" ->
					dto.Strassenname = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_Strassenname.datenlaenge(), name);
			case "hausNr" -> dto.HausNr = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_HausNr.datenlaenge(), name);
			case "hausNrZusatz" ->
					dto.HausNrZusatz = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_HausNrZusatz.datenlaenge(), name);
			case "tel" -> dto.Tel = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_Tel.datenlaenge(), name);
			case "email" -> dto.Email = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_Email.datenlaenge(), name);
			case "bemerkung" -> dto.Bemerkung = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_Bemerkung.datenlaenge(), name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Daten des Patches enthalten ein unbekanntes Attribut.");
		}
	}
}
