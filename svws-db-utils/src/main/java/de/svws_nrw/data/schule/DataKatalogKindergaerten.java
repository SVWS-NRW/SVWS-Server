package de.svws_nrw.data.schule;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.core.data.schule.Kindergarten;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOKindergarten;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Kindergarten}.
 */
public final class DataKatalogKindergaerten extends DataManagerRevised<Long, DTOKindergarten, Kindergarten> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Kindergarten}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogKindergaerten(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation("bezeichnung");
	}

	@Override
	public Kindergarten map(final DTOKindergarten dto) {
		final Kindergarten kindergarten = new Kindergarten();
		kindergarten.id = dto.ID;
		kindergarten.bezeichnung = (dto.Bezeichnung == null) ? "" : dto.Bezeichnung;
		kindergarten.plz = dto.PLZ;
		kindergarten.ort = dto.Ort;
		kindergarten.strassenname = dto.Strassenname;
		kindergarten.hausNr = dto.HausNr;
		kindergarten.hausNrZusatz = dto.HausNrZusatz;
		kindergarten.tel = dto.Tel;
		kindergarten.email = dto.Email;
		kindergarten.bemerkung = dto.Bemerkung;
		kindergarten.istSichtbar = (dto.Sichtbar == null) || dto.Sichtbar;
		kindergarten.sortierung = (dto.Sortierung == null) ? -1 : dto.Sortierung;
		return kindergarten;
	}

	@Override
	protected long getLongId(final DTOKindergarten dto) {
		return dto.ID;
	}

	@Override
	public List<Kindergarten> getAll() throws ApiOperationException {
		final List<DTOKindergarten> kindergaerten = conn.queryAll(DTOKindergarten.class);
		return kindergaerten.stream().map(this::map).toList();
	}

	@Override
	public Kindergarten getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die ID für den Kindergarten darf nicht null sein.");

		final DTOKindergarten dto = conn.queryByKey(DTOKindergarten.class, id);
		if (dto == null)
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde kein Kindergarten mit der ID %d gefunden.".formatted(id));

		return map(dto);
	}

	@Override
	protected void initDTO(final DTOKindergarten dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected void mapAttribute(final DTOKindergarten dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, true, name);
				if (!Objects.equals(dto.ID, id))
					throw new ApiOperationException(Response.Status.BAD_REQUEST,
							"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
			}
			case "bezeichnung" -> dto.Bezeichnung =
					JSONMapper.convertToString(value, false, false, Schema.tab_K_Kindergarten.col_Bezeichnung.datenlaenge(), name);
			case "plz" -> dto.PLZ = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_PLZ.datenlaenge(), name);
			case "ort" -> dto.Ort = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_Ort.datenlaenge(), name);
			case "strassenname" ->
					dto.Strassenname = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_Strassenname.datenlaenge(), name);
			case "hausNr" -> dto.HausNr = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_HausNr.datenlaenge(), name);
			case "hausNrZusatz" ->
					dto.HausNrZusatz = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_HausNrZusatz.datenlaenge(), name);
			case "tel" -> validateTel(dto, name, value);
			case "email" -> dto.Email = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_Email.datenlaenge(), name);
			case "bemerkung" -> dto.Bemerkung = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_Bemerkung.datenlaenge(), name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void validateTel(final DTOKindergarten dto, final String name, final Object value) throws ApiOperationException {
		final String tel = JSONMapper.convertToString(value, true, true, Schema.tab_K_Kindergarten.col_Tel.datenlaenge(), name);
		if (Objects.equals(tel, dto.Tel) || tel.isBlank())
			return;

		// folgende Formate sind erlaubt: 0151123456, 0151/123456, 0151-123456, +49/176-456456 -> Buchstaben sind nicht erlaubt
		if (!tel.matches("^\\+?\\d+([-/]?\\d+)*$"))
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Telefonnummer %s entspricht nicht dem erlaubten Format.".formatted(tel));

		dto.Tel = tel;
	}

}
