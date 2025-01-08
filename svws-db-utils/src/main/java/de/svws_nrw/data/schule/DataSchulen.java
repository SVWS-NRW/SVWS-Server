package de.svws_nrw.data.schule;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import de.svws_nrw.core.data.kataloge.SchulEintrag;
import de.svws_nrw.asd.data.schule.SchulformKatalogEintrag;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link SchulEintrag}.
 */
public final class DataSchulen extends DataManagerRevised<Long, DTOSchuleNRW, SchulEintrag> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link SchulEintrag}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchulen(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation("schulnummer");
	}

	@Override
	protected void initDTO(final DTOSchuleNRW dto, final Long newId, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.ID = newId;
	}

	@Override
	public List<SchulEintrag> getAll() {
		return getSchulenFiltered(null);
	}

	@Override
	public List<SchulEintrag> getList() {
		return getSchulenFiltered(dto -> (dto.Kuerzel != null) && !dto.Kuerzel.isBlank());
	}

	private List<SchulEintrag> getSchulenFiltered(final Predicate<DTOSchuleNRW> filter) {
		return conn.queryAll(DTOSchuleNRW.class).stream().filter(dto -> (filter == null) || filter.test(dto)).map(this::map).toList();
	}

	@Override
	public SchulEintrag getById(final Long id) throws ApiOperationException {
		final DTOSchuleNRW dtoSchuleNRW = conn.queryByKey(DTOSchuleNRW.class, id);
		if (dtoSchuleNRW == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Eintrag im Katalog der Schulen mit der ID %d gefunden.".formatted(id));

		return map(dtoSchuleNRW);
	}

	@Override
	public SchulEintrag map(final DTOSchuleNRW dtoSchuleNRW) {
		final SchulEintrag result = new SchulEintrag();
		result.id = dtoSchuleNRW.ID;
		result.kuerzel = dtoSchuleNRW.Kuerzel;
		result.kurzbezeichnung = dtoSchuleNRW.KurzBez;
		result.schulnummer = dtoSchuleNRW.SchulNr;
		result.name = (dtoSchuleNRW.Name != null) ? dtoSchuleNRW.Name : "";
		final Schulform schulform = (dtoSchuleNRW.SchulformNr != null) ? Schulform.data().getWertBySchluessel(dtoSchuleNRW.SchulformNr) : null;
		final SchulformKatalogEintrag schulformEintrag = (schulform != null) ? schulform.daten(conn.getUser().schuleGetSchuljahr()) : null;
		result.idSchulform = (schulformEintrag != null) ? schulformEintrag.id : null;
		result.strassenname = dtoSchuleNRW.Strassenname;
		result.hausnummer = dtoSchuleNRW.HausNr;
		result.zusatzHausnummer = dtoSchuleNRW.HausNrZusatz;
		result.plz = dtoSchuleNRW.PLZ;
		result.ort = dtoSchuleNRW.Ort;
		result.telefon = dtoSchuleNRW.Telefon;
		result.fax = dtoSchuleNRW.Fax;
		result.email = dtoSchuleNRW.Email;
		result.schulleiter = dtoSchuleNRW.Schulleiter;
		result.sortierung = (dtoSchuleNRW.Sortierung == null) ? 32000 : dtoSchuleNRW.Sortierung;
		result.istSichtbar = (dtoSchuleNRW.Sichtbar == null) || (dtoSchuleNRW.Sichtbar);
		return result;
	}

	@Override
	protected void mapAttribute(final DTOSchuleNRW dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, "id");
				if (id != dto.ID)
					throw new ApiOperationException(Status.BAD_REQUEST, "Id %d der PatchMap ist ungleich der id %d vom Dto".formatted(id, dto.ID));
			}
			case "schulnummer" -> dto.SchulNr = JSONMapper.convertToString(value, true, false, 6, "schulnummer");
			case "kuerzel" -> dto.Kuerzel = JSONMapper.convertToString(value, true, false, 10, "kuerzel");
			case "kurzbezeichnung" -> dto.KurzBez = JSONMapper.convertToString(value, true, false, 40, "kurzbezeichnung");
			case "name" -> dto.Name = JSONMapper.convertToString(value, false, false, 120, "name");
			case "idSchulform" -> {
				final Long id = JSONMapper.convertToLong(value, true, "idSchulform");
				if (id == null) {
					dto.SchulformBez = null;
					dto.SchulformKrz = null;
					dto.SchulformNr = null;
				} else {
					final SchulformKatalogEintrag eintragByID = Schulform.data().getEintragByID(id);
					if (eintragByID == null)
						throw new ApiOperationException(Status.NOT_FOUND, "SchulformKatalogEintrag mit der id %d nicht gefunden".formatted(id));
					dto.SchulformBez = eintragByID.text;
					dto.SchulformKrz = eintragByID.kuerzel;
					dto.SchulformNr = eintragByID.schluessel;
				}
			}
			case "strassenname" -> dto.Strassenname = JSONMapper.convertToString(value, false, true, 55, "strassenname");
			case "hausnummer" -> dto.HausNr = JSONMapper.convertToString(value, false, true, 10, "hausnummer");
			case "zusatzHausnummer" -> dto.HausNrZusatz = JSONMapper.convertToString(value, false, true, 30, "hausnummerZusatz");
			case "plz" -> dto.PLZ = JSONMapper.convertToString(value, false, true, 10, "plz");
			case "ort" -> dto.Ort = JSONMapper.convertToString(value, false, true, 50, "ort");
			case "telefon" -> dto.Telefon = JSONMapper.convertToString(value, false, true, 20, "telefon");
			case "fax" -> dto.Fax = JSONMapper.convertToString(value, false, true, 20, "fax");
			case "email" -> dto.Email = JSONMapper.convertToString(value, false, true, 40, "email");
			case "schulleiter" -> dto.Schulleiter = JSONMapper.convertToString(value, false, true, 40, "schulleiter");
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, "sortierung");
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, "istSichtbar");
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}
}
