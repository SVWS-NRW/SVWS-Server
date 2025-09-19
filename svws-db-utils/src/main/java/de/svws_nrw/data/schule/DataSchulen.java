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
import de.svws_nrw.db.schema.Schema;
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
		setAttributesNotPatchable("id", "schulnummerStatistik");
		setAttributesRequiredOnCreation("schulnummerStatistik", "kurzbezeichnung", "name");
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
		result.schulnummerStatistik = dtoSchuleNRW.SchulNr_SIM;
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
				final Long id = JSONMapper.convertToLong(value, false, name);
				if (id != dto.ID)
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
			}
			case "schulnummerStatistik" -> mapSchulnummer(dto, value, name);
			case "kuerzel" -> updateKuerzel(dto, value, name);
			case "kurzbezeichnung" -> dto.KurzBez = JSONMapper.convertToString(value, false, false, Schema.tab_K_Schule.col_KurzBez.datenlaenge(), name);
			case "name" -> dto.Name = JSONMapper.convertToString(value, false, false, Schema.tab_K_Schule.col_Name.datenlaenge(), name);
			case "idSchulform" -> mapIdSchulform(dto, name, value);
			case "strassenname" -> dto.Strassenname = JSONMapper.convertToString(value, true, true, Schema.tab_K_Schule.col_Strassenname.datenlaenge(), name);
			case "hausnummer" -> dto.HausNr = JSONMapper.convertToString(value, true, true, Schema.tab_K_Schule.col_HausNr.datenlaenge(), name);
			case "zusatzHausnummer" -> dto.HausNrZusatz = JSONMapper.convertToString(value, true, true, Schema.tab_K_Schule.col_HausNrZusatz.datenlaenge(), name);
			case "plz" -> dto.PLZ = JSONMapper.convertToString(value, true, true, Schema.tab_K_Schule.col_PLZ.datenlaenge(), name);
			case "ort" -> dto.Ort = JSONMapper.convertToString(value, true, true, Schema.tab_K_Schule.col_Ort.datenlaenge(), name);
			case "telefon" -> dto.Telefon = JSONMapper.convertToString(value, true, true, Schema.tab_K_Schule.col_Telefon.datenlaenge(), name);
			case "fax" -> dto.Fax = JSONMapper.convertToString(value, true, true, Schema.tab_K_Schule.col_Fax.datenlaenge(), name);
			case "email" -> dto.Email = JSONMapper.convertToString(value, true, true, Schema.tab_K_Schule.col_Email.datenlaenge(), name);
			case "schulleiter" -> dto.Schulleiter = JSONMapper.convertToString(value, true, true, Schema.tab_K_Schule.col_Schulleiter.datenlaenge(), name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void mapIdSchulform(final DTOSchuleNRW dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, true, name);
		if (id == null) {
			dto.SchulformBez = null;
			dto.SchulformKrz = null;
			dto.SchulformNr = null;
		} else {
			final SchulformKatalogEintrag eintrag = Schulform.data().getEintragByID(id);
			if (eintrag == null)
				throw new ApiOperationException(Status.NOT_FOUND, "SchulformKatalogEintrag mit der id %d nicht gefunden".formatted(id));
			dto.SchulformBez = eintrag.text;
			dto.SchulformKrz = eintrag.kuerzel;
			dto.SchulformNr = eintrag.schluessel;
		}
	}

	private void mapSchulnummer(final DTOSchuleNRW dto, final Object value, final String name) throws ApiOperationException {
		final String schulnummer = JSONMapper.convertToString(value, false, false, Schema.tab_K_Schule.col_SchulNr.datenlaenge(), name);
		if (schulnummer.startsWith("1")) {
			dto.SchulNr = schulnummer;
			dto.SchulNr_SIM = schulnummer;
		} else if (schulnummer.startsWith("9")) {
			dto.SchulNr = String.valueOf(dto.ID + 200000);
			dto.SchulNr_SIM = schulnummer;
		} else
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Schulnummer %s ist ungültig. Gültige Schulnummern starten mit der Ziffer 1 (intern) oder 9 (extern).".formatted(schulnummer));
	}

	private void updateKuerzel(final DTOSchuleNRW dto, final Object value, final String name) throws ApiOperationException {
		final String kuerzel = JSONMapper.convertToString(
				value, true, true, Schema.tab_K_Schule.col_Kuerzel.datenlaenge(), name);
		if ((kuerzel == null) || kuerzel.isEmpty()) {
			dto.Kuerzel = null;
			return;
		}

		final boolean kuerzelAlreadyUsed = this.conn.queryAll(DTOSchuleNRW.class).stream()
				.anyMatch(s -> (s.ID != dto.ID)  && kuerzel.equalsIgnoreCase(s.Kuerzel));
		if (kuerzelAlreadyUsed)
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Kuerzel %s ist bereits vorhanden.".formatted(value));

		dto.Kuerzel = kuerzel;
	}
}
