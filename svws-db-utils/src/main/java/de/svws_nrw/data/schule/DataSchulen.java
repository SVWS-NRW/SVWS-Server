package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.SimpleOperationResponse;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link SchulEintrag}.
 */
public final class DataSchulen extends DataManagerRevised<Long, DTOSchuleNRW, SchulEintrag> {

	private static final String SCHULNUMMER_STATISTIK = "schulnummerStatistik";

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link SchulEintrag}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchulen(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id", SCHULNUMMER_STATISTIK);
		setAttributesRequiredOnCreation(SCHULNUMMER_STATISTIK, "kurzbezeichnung", "name");
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
		final List<DTOSchuleNRW> schulen = this.conn.queryAll(DTOSchuleNRW.class);
		final Set<Long> idsOfReferencedSchulen = this.getIdsOfReferencedSchulen(schulen);
		return schulen
				.stream()
				.filter(dto -> (filter == null) || filter.test(dto))
				.map(this::map)
				.map(s -> setReferencedFlag(s, idsOfReferencedSchulen))
				.sorted(Comparator.comparing(s -> s.id))
				.toList();
	}

	@Override
	public SchulEintrag getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Keine ID für die Schule übergeben.");
		}
		final DTOSchuleNRW dtoSchuleNRW = conn.queryByKey(DTOSchuleNRW.class, id);
		if (dtoSchuleNRW == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Eintrag im Katalog der Schulen mit der ID %d gefunden.".formatted(id));
		}
		return map(dtoSchuleNRW);
	}

	@Override
	protected long getLongId(final DTOSchuleNRW sto) {
		return sto.ID;
	}

	@Override
	public SchulEintrag map(final DTOSchuleNRW dto) {
		final SchulEintrag schule = new SchulEintrag();
		schule.id = dto.ID;
		schule.kuerzel = dto.Kuerzel;
		schule.kurzbezeichnung = dto.KurzBez;
		schule.schulnummerStatistik = dto.SchulNr_SIM;
		schule.name = Objects.requireNonNullElse(dto.Name, "");
		schule.idSchulform = mapIdSchulform(dto.SchulformNr);
		schule.strassenname = dto.Strassenname;
		schule.hausnummer = dto.HausNr;
		schule.zusatzHausnummer = dto.HausNrZusatz;
		schule.plz = dto.PLZ;
		schule.ort = dto.Ort;
		schule.telefon = dto.Telefon;
		schule.fax = dto.Fax;
		schule.email = dto.Email;
		schule.schulleiter = dto.Schulleiter;
		schule.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		schule.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		return schule;
	}

	@Override
	protected void mapAttribute(final DTOSchuleNRW dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case SCHULNUMMER_STATISTIK -> mapSchulnummer(dto, value, name);
			case "kuerzel" -> updateKuerzel(dto, value, name);
			case "kurzbezeichnung" -> updateKurzbezeichnung(dto, name, value);
			case "name" -> updateName(dto, name, value);
			case "idSchulform" -> updateIdSchulform(dto, name, value);
			case "strassenname" -> updateStrassenname(dto, name, value);
			case "hausnummer" -> updateHausnummer(dto, name, value);
			case "zusatzHausnummer" -> updateZusatzHausnummer(dto, name, value);
			case "plz" -> updatePlz(dto, name, value);
			case "ort" -> updateOrt(dto, name, value);
			case "telefon" -> updateTelefon(dto, name, value);
			case "fax" -> updateFax(dto, name, value);
			case "email" -> updateEmail(dto, name, value);
			case "schulleiter" -> updateSchulleiter(dto, name, value);
			case "sortierung" -> updateSortierung(dto, name, value);
			case "istSichtbar" -> updateSichtbar(dto, name, value);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	@Override
	protected void checkBeforeDeletionWithSimpleOperationResponse(final List<DTOSchuleNRW> schulen, final Map<Long, SimpleOperationResponse> mapResponses) {
		final Set<Long> referencedIds = getIdsOfReferencedSchulen(schulen);
		schulen.stream()
				.filter(s -> referencedIds.contains(s.ID))
				.forEach(s -> markResponseAsFailed(mapResponses.get(s.ID), s.KurzBez));
	}


	private Long mapIdSchulform(final String schulformNr) {
		if (schulformNr == null) {
			return null;
		}
		final Schulform schulform = Schulform.data().getWertBySchluessel(schulformNr);
		if (schulform == null) {
			return null;
		}
		final int schuljahr = conn.getUser().schuleGetSchuljahr();
		final SchulformKatalogEintrag eintrag = schulform.daten(schuljahr);
		return (eintrag != null) ? eintrag.id : null;
	}

	private static void validateId(final DTOSchuleNRW dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if (id != dto.ID) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
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
		} else {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Schulnummer %s ist ungültig. Gültige Schulnummern starten mit der Ziffer 1 (intern) oder 9 (extern).".formatted(schulnummer));
		}
	}

	private void updateKuerzel(final DTOSchuleNRW dto, final Object value, final String name) throws ApiOperationException {
		final String kuerzel = JSONMapper.convertToString(
				value, true, true, Schema.tab_K_Schule.col_Kuerzel.datenlaenge(), name);
		if ((kuerzel == null) || kuerzel.isEmpty()) {
			dto.Kuerzel = null;
			return;
		}
		if (StringUtils.isBlank(kuerzel) || Strings.CS.equals(dto.Kuerzel, kuerzel)) {
			return;
		}
		validateKuerzelIsUnique(kuerzel);
		dto.Kuerzel = kuerzel;
	}

	private void validateKuerzelIsUnique(final String bezeichnung) throws ApiOperationException {
		final boolean kuerzelAlreadyUsed = this.conn
				.queryAll(DTOSchuleNRW.class)
				.stream()
				.anyMatch(h -> Strings.CI.equals(bezeichnung, h.Kuerzel));
		if (kuerzelAlreadyUsed) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Kuerzel %s ist bereits vorhanden.".formatted(bezeichnung));
		}
	}

	private static void updateKurzbezeichnung(final DTOSchuleNRW dto, final String name, final Object value) throws ApiOperationException {
		dto.KurzBez = JSONMapper.convertToString(value, false, false, Schema.tab_K_Schule.col_KurzBez.datenlaenge(), name);
	}

	private static void updateName(final DTOSchuleNRW dto, final String name, final Object value) throws ApiOperationException {
		dto.Name = JSONMapper.convertToString(value, false, false, Schema.tab_K_Schule.col_Name.datenlaenge(), name);
	}

	private static void updateIdSchulform(final DTOSchuleNRW dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, true, name);
		if (id == null) {
			dto.SchulformBez = null;
			dto.SchulformKrz = null;
			dto.SchulformNr = null;
		} else {
			final SchulformKatalogEintrag eintrag = Schulform.data().getEintragByID(id);
			if (eintrag == null) {
				throw new ApiOperationException(Status.NOT_FOUND, "SchulformKatalogEintrag mit der id %d nicht gefunden".formatted(id));
			}
			dto.SchulformBez = eintrag.text;
			dto.SchulformKrz = eintrag.kuerzel;
			dto.SchulformNr = eintrag.schluessel;
		}
	}

	private static void updateStrassenname(final DTOSchuleNRW dto, final String name, final Object value) throws ApiOperationException {
		dto.Strassenname = JSONMapper.convertToString(value, true, true, Schema.tab_K_Schule.col_Strassenname.datenlaenge(), name);
	}

	private static void updateHausnummer(final DTOSchuleNRW dto, final String name, final Object value) throws ApiOperationException {
		dto.HausNr = JSONMapper.convertToString(value, true, true, Schema.tab_K_Schule.col_HausNr.datenlaenge(), name);
	}

	private static void updateZusatzHausnummer(final DTOSchuleNRW dto, final String name, final Object value) throws ApiOperationException {
		dto.HausNrZusatz = JSONMapper.convertToString(value, true, true, Schema.tab_K_Schule.col_HausNrZusatz.datenlaenge(), name);
	}

	private static void updatePlz(final DTOSchuleNRW dto, final String name, final Object value) throws ApiOperationException {
		dto.PLZ = JSONMapper.convertToString(value, true, true, Schema.tab_K_Schule.col_PLZ.datenlaenge(), name);
	}

	private static void updateOrt(final DTOSchuleNRW dto, final String name, final Object value) throws ApiOperationException {
		dto.Ort = JSONMapper.convertToString(value, true, true, Schema.tab_K_Schule.col_Ort.datenlaenge(), name);
	}

	private static void updateTelefon(final DTOSchuleNRW dto, final String name, final Object value) throws ApiOperationException {
		dto.Telefon = JSONMapper.convertToString(value, true, true, Schema.tab_K_Schule.col_Telefon.datenlaenge(), name);
	}

	private static void updateFax(final DTOSchuleNRW dto, final String name, final Object value) throws ApiOperationException {
		dto.Fax = JSONMapper.convertToString(value, true, true, Schema.tab_K_Schule.col_Fax.datenlaenge(), name);
	}

	private static void updateEmail(final DTOSchuleNRW dto, final String name, final Object value) throws ApiOperationException {
		dto.Email = JSONMapper.convertToString(value, true, true, Schema.tab_K_Schule.col_Email.datenlaenge(), name);
	}

	private static void updateSchulleiter(final DTOSchuleNRW dto, final String name, final Object value) throws ApiOperationException {
		dto.Schulleiter = JSONMapper.convertToString(value, true, true, Schema.tab_K_Schule.col_Schulleiter.datenlaenge(), name);
	}

	private static void updateSortierung(final DTOSchuleNRW dto, final String name, final Object value) throws ApiOperationException {
		dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
	}

	private static void updateSichtbar(final DTOSchuleNRW dto, final String name, final Object value) throws ApiOperationException {
		dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
	}

	private static void markResponseAsFailed(final SimpleOperationResponse response, final String kurzbezeichnung) {
		response.success = false;
		response.log
				.add("Die Schule mit der Kurzbezeichnung %s ist in der Datenbank referenziert und kann daher nicht gelöscht werden".formatted(kurzbezeichnung));
	}

	private Set<Long> getIdsOfReferencedSchulen(final List<DTOSchuleNRW> schulen) {
		if ((schulen == null) || schulen.isEmpty()) {
			return Collections.emptySet();
		}

		final Map<String, Long> schulNrByIdSchule = getSchulNrByIdSchule(schulen);

		if (schulNrByIdSchule.isEmpty()) {
			return Collections.emptySet();
		}

		final Set<String> schulnummern = schulNrByIdSchule.keySet();

		final String queryLetzteSchule = "SELECT DISTINCT a.LSSchulNr FROM DTOSchueler a WHERE a.LSSchulNr IN :schulnummern";
		final String queryAufnehmendeSchule = "SELECT DISTINCT b.SchulwechselNr FROM DTOSchueler b WHERE b.SchulwechselNr IN :schulnummern";

		final String query = String.join("\nUNION ALL\n", queryLetzteSchule, queryAufnehmendeSchule);
		return conn.query(query, String.class)
				.setParameter("schulnummern", schulnummern)
				.getResultList()
				.stream()
				.map(schulNrByIdSchule::get)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}

	private static Map<String, Long> getSchulNrByIdSchule(final List<DTOSchuleNRW> schulen) {
		return schulen
				.stream()
				.filter(Objects::nonNull)
				.collect(Collectors.toMap(
						s -> s.SchulNr,
						s -> s.ID));
	}

	private SchulEintrag setReferencedFlag(final SchulEintrag schuleintrag, final Set<Long> idsOfReferencedSchulen) {
		schuleintrag.referenziertInAnderenTabellen = idsOfReferencedSchulen.contains(schuleintrag.id);
		return schuleintrag;
	}

}
