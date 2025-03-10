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
		setAttributesRequiredOnCreation("schulnummer", "kurzbezeichnung", "name");
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
				final Long id = JSONMapper.convertToLong(value, false, "id");
				if (id != dto.ID)
					throw new ApiOperationException(Status.BAD_REQUEST, "Id %d der PatchMap ist ungleich der id %d vom Dto".formatted(id, dto.ID));
			}
			case "schulnummer" -> mapSchulnummer(dto, value);
			case "kuerzel" -> updateKuerzel(dto, value);
			case "kurzbezeichnung" -> dto.KurzBez = JSONMapper.convertToString(value, false, false, 40, "kurzbezeichnung");
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
			case "strassenname" -> dto.Strassenname = JSONMapper.convertToString(value, true, true, 55, "strassenname");
			case "hausnummer" -> dto.HausNr = JSONMapper.convertToString(value, true, true, 10, "hausnummer");
			case "zusatzHausnummer" -> dto.HausNrZusatz = JSONMapper.convertToString(value, true, true, 30, "hausnummerZusatz");
			case "plz" -> dto.PLZ = JSONMapper.convertToString(value, true, true, 10, "plz");
			case "ort" -> dto.Ort = JSONMapper.convertToString(value, true, true, 50, "ort");
			case "telefon" -> dto.Telefon = JSONMapper.convertToString(value, true, true, 20, "telefon");
			case "fax" -> dto.Fax = JSONMapper.convertToString(value, true, true, 20, "fax");
			case "email" -> dto.Email = JSONMapper.convertToString(value, true, true, 40, "email");
			case "schulleiter" -> dto.Schulleiter = JSONMapper.convertToString(value, true, true, 40, "schulleiter");
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, "sortierung");
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, "istSichtbar");
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private void mapSchulnummer(final DTOSchuleNRW dto, final Object value) throws ApiOperationException {
		final String schulnummer = JSONMapper.convertToString(value, false, false, 6, "schulnummer");
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

	private void updateKuerzel(final DTOSchuleNRW dto, final Object value) throws ApiOperationException {
		final String kuerzel = JSONMapper.convertToString(
				value, true, true, Schema.tab_K_Schule.col_Kuerzel.datenlaenge(), "kuerzel");
		// Kuerzel ist unveraendert
		if ((dto.Kuerzel != null) && dto.Kuerzel.equals(kuerzel))
			return;

		// theoretischer Fall, der nicht eintreten sollte
		final List<DTOSchuleNRW> schulen = conn.queryList(DTOSchuleNRW.QUERY_BY_KUERZEL, DTOSchuleNRW.class, kuerzel);

		if (schulen.size() > 1)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Mehr als eine Schule mit dem gleichen Kuerzel vorhanden");

		// kuerzel bereits vorhanden
		if (!schulen.isEmpty()) {
			final DTOSchuleNRW dtoSchule = schulen.getFirst();
			if ((dtoSchule != null) && (dtoSchule.ID != dto.ID))
				throw new ApiOperationException(Status.BAD_REQUEST, "Das Kuerzel %s ist bereits vorhanden.".formatted(value));
		}

		// kuerzel wird gepatched
		dto.Kuerzel = kuerzel;
	}
}
