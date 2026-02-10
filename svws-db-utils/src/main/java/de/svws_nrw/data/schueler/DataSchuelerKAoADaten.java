package de.svws_nrw.data.schueler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.data.jahrgang.JahrgaengeKatalogEintrag;
import de.svws_nrw.asd.data.kaoa.KAOAMerkmalKatalogEintrag;
import de.svws_nrw.asd.data.kaoa.KAOAZusatzmerkmalKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.kaoa.KAOAAnschlussoptionen;
import de.svws_nrw.asd.types.kaoa.KAOABerufsfeld;
import de.svws_nrw.asd.types.kaoa.KAOAEbene4;
import de.svws_nrw.asd.types.kaoa.KAOAKategorie;
import de.svws_nrw.asd.types.kaoa.KAOAMerkmal;
import de.svws_nrw.asd.types.kaoa.KAOAZusatzmerkmal;
import de.svws_nrw.asd.types.kaoa.KAOAZusatzmerkmaleOptionsarten;
import de.svws_nrw.core.data.schueler.SchuelerKAoADaten;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerKAoADaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;
import org.apache.commons.lang3.StringUtils;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link SchuelerKAoADaten}.
 */
public final class DataSchuelerKAoADaten extends DataManagerRevised<Long, DTOSchuelerKAoADaten, SchuelerKAoADaten> {

	private final Long idSchueler;

	/**
	 * Erstellt einen neuen DataSchuelerKAoADaten mit der angegebenen Verbindung.
	 *
	 * @param conn         DBEntityManager
	 * @param idSchueler   schuelerId
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public DataSchuelerKAoADaten(final DBEntityManager conn, final Long idSchueler) throws ApiOperationException {
		super(conn);
		setAttributesNotPatchable("id");
		validateIdSchueler(idSchueler);
		this.idSchueler = idSchueler;
	}

	@Override
	protected void initDTO(final DTOSchuelerKAoADaten dto, final Long newId, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.id = newId;
	}

	@Override
	public void checkBeforeCreation(final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		final SchuelerKAoADaten schuelerKAoADaten = new SchuelerKAoADaten();
		schuelerKAoADaten.id = newID;
		patchCoreDto(schuelerKAoADaten, initAttributes);
		validateAttributes(schuelerKAoADaten);
	}

	@Override
	public void checkBeforePatch(final DTOSchuelerKAoADaten dto, final Map<String, Object> patchAttributes) throws ApiOperationException {
		final SchuelerKAoADaten schuelerKAoADaten = map(dto);
		patchCoreDto(schuelerKAoADaten, patchAttributes);
		validateAttributes(schuelerKAoADaten);
	}

	@Override
	public SchuelerKAoADaten getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID darf nicht null sein.");
		}
		final DTOSchuelerKAoADaten dto = conn.queryByKey(DTOSchuelerKAoADaten.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine KAoADaten mit der ID %d gefunden".formatted(id));
		}
		return map(dto);
	}

	@Override
	public SchuelerKAoADaten map(final DTOSchuelerKAoADaten schuelerKAoADaten) throws ApiOperationException {
		final SchuelerKAoADaten result = new SchuelerKAoADaten();
		result.id = schuelerKAoADaten.id;
		result.idKategorie = schuelerKAoADaten.idKategorie;
		result.idMerkmal = Objects.requireNonNullElse(schuelerKAoADaten.idMerkmal, -1L);
		result.idZusatzmerkmal = Objects.requireNonNullElse(schuelerKAoADaten.idZusatzmerkmal, -1L);
		result.idAnschlussoption = schuelerKAoADaten.idAnschlussoption;
		result.idEbene4 = schuelerKAoADaten.idEbene4;
		result.idBerufsfeld = schuelerKAoADaten.idBerufsfeld;
		result.idJahrgang = mapIdJahrgang(schuelerKAoADaten);
		result.idSchuljahresabschnitt = getLernabschnittsdaten(schuelerKAoADaten.idLernabschnitt).Schuljahresabschnitts_ID;
		result.bemerkung = schuelerKAoADaten.bemerkung;
		return result;
	}

	@Override
	public List<SchuelerKAoADaten> getAll() throws ApiOperationException {
		final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = this.conn
				.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_SCHUELER_ID, DTOSchuelerLernabschnittsdaten.class, this.idSchueler);
		final List<Long> idsLernabschnitte = lernabschnitte
				.stream()
				.map(s -> s.ID)
				.toList();
		final List<DTOSchuelerKAoADaten> kaoaDaten = this.conn
				.queryList(DTOSchuelerKAoADaten.QUERY_LIST_BY_IDLERNABSCHNITT, DTOSchuelerKAoADaten.class, idsLernabschnitte);
		final List<SchuelerKAoADaten> result = new ArrayList<>();
		for (final DTOSchuelerKAoADaten dto : kaoaDaten) {
			result.add(this.map(dto));
		}
		return result;
	}

	private void patchCoreDto(final SchuelerKAoADaten schuelerKAoADaten, final Map<String, Object> patchAttributes) throws ApiOperationException {
		for (final Map.Entry<String, Object> entry : patchAttributes.entrySet()) {
			final String name = entry.getKey();
			final Object value = entry.getValue();

			switch (name) {
				case "idJahrgang" -> schuelerKAoADaten.idJahrgang = JSONMapper.convertToLong(value, false, name);
				case "idSchuljahresabschnitt" -> schuelerKAoADaten.idSchuljahresabschnitt = JSONMapper.convertToLong(value, false, name);
				case "idKategorie" -> schuelerKAoADaten.idKategorie = JSONMapper.convertToLong(value, false, name);
				case "idMerkmal" -> schuelerKAoADaten.idMerkmal = JSONMapper.convertToLong(value, false, name);
				case "idZusatzmerkmal" -> schuelerKAoADaten.idZusatzmerkmal = JSONMapper.convertToLong(value, false, name);
				case "idEbene4" -> schuelerKAoADaten.idEbene4 = JSONMapper.convertToLong(value, true, name);
				case "idAnschlussoption" -> schuelerKAoADaten.idAnschlussoption = JSONMapper.convertToLong(value, true, name);
				case "idBerufsfeld" -> schuelerKAoADaten.idBerufsfeld = JSONMapper.convertToLong(value, true, name);
				case "bemerkung" -> schuelerKAoADaten.bemerkung = (String) value;
				default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
			}
		}
	}

	@Override
	protected void mapAttribute(final DTOSchuelerKAoADaten dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idJahrgang" -> updateIdJahrgang(dto, value, name);
			case "idSchuljahresabschnitt" -> dto.idLernabschnitt = getIdLernabschnitt(JSONMapper.convertToLong(value, false, name));
			case "idKategorie" -> dto.idKategorie = JSONMapper.convertToLong(value, false, name);
			case "idMerkmal" -> dto.idMerkmal = JSONMapper.convertToLong(value, false, name);
			case "idZusatzmerkmal" -> dto.idZusatzmerkmal = JSONMapper.convertToLong(value, false, name);
			case "idEbene4" -> dto.idEbene4 = JSONMapper.convertToLong(value, true, name);
			case "idAnschlussoption" -> dto.idAnschlussoption = JSONMapper.convertToLong(value, true, name);
			case "idBerufsfeld" -> dto.idBerufsfeld = JSONMapper.convertToLong(value, true, name);
			case "bemerkung" -> dto.bemerkung = JSONMapper.convertToString(value, true, true, null, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void updateIdJahrgang(final DTOSchuelerKAoADaten dto, final Object value, final String name) throws ApiOperationException {
		final long idJahrgang = JSONMapper.convertToLong(value, false, name);
		final JahrgaengeKatalogEintrag eintrag = Jahrgaenge.data().getEintragByID(idJahrgang);
		if (eintrag == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Der Jahrgang mit der ID %d wurde nicht gefunden".formatted(idJahrgang));
		}
		dto.jahrgang = eintrag.schluessel;
	}

	private int getSchuljahr(final long schuljahresabschnittsId) throws ApiOperationException {
		try {
			return conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(schuljahresabschnittsId).schuljahr;
		} catch (final DeveloperNotificationException e) {
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Schuljahresabschnitt zur ID %d gefunden.".formatted(schuljahresabschnittsId));
		}
	}

	private long getIdLernabschnitt(final long idSchuljahresabschnitt) throws ApiOperationException {
		final List<DTOSchuelerLernabschnittsdaten> lernabschnitte =
				conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOSchuelerLernabschnittsdaten.class, idSchuljahresabschnitt);
		return lernabschnitte
				.stream()
				.filter(e -> e.Schueler_ID == idSchueler)
				.filter(e -> e.WechselNr == 0)
				.findFirst()
				.map(e -> e.ID)
				.orElseThrow(() -> new ApiOperationException(
						Status.NOT_FOUND, "Keine Lernabschnittsdaten zur IdSchuljahresabschnitt %d gefunden".formatted(idSchuljahresabschnitt)));
	}

	private int getSchuljahr(final DTOSchuelerKAoADaten schuelerKAoADaten) throws ApiOperationException {
		final DTOSchuelerLernabschnittsdaten lernabschnitte = this.conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, schuelerKAoADaten.idLernabschnitt);
		if (lernabschnitte == null) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Keine Lernabschnittsdaten mit der ID %d vorhanden.".formatted(schuelerKAoADaten.idLernabschnitt));
		}
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(lernabschnitte.Schuljahresabschnitts_ID);
		return schuljahresabschnitt.schuljahr;
	}

	private long mapIdJahrgang(final DTOSchuelerKAoADaten schuelerKAoADaten) throws ApiOperationException {
		if (schuelerKAoADaten.jahrgang == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Jahrgang ist null.");
		}
		final int schuljahr = getSchuljahr(schuelerKAoADaten);
		final Jahrgaenge jahrgang = Jahrgaenge.data().getWertBySchluessel(schuelerKAoADaten.jahrgang);
		if (jahrgang == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keinen Jahrgang mit dem Schlüssel %s gefunden.".formatted(schuelerKAoADaten.jahrgang));
		}
		final JahrgaengeKatalogEintrag jahrgaengeEintrag = jahrgang.daten(schuljahr);
		if (jahrgaengeEintrag == null) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Keinen Eintrag im Schuljahr %d mit dem Schlüssel %s gefunden.".formatted(schuljahr, schuelerKAoADaten.jahrgang));
		}
		return jahrgaengeEintrag.id;
	}

	// --- validate ---

	private void validateAttributes(final SchuelerKAoADaten schuelerKAoADaten) throws ApiOperationException {
		validateOptionalAttributes(schuelerKAoADaten);
		validateLernabschnittsdaten(getIdLernabschnitt(schuelerKAoADaten.idSchuljahresabschnitt));

		final KAOAKategorie kategorie = validateKategorie(schuelerKAoADaten.idKategorie);
		final int schuljahr = getSchuljahr(schuelerKAoADaten.idSchuljahresabschnitt);
		validateJahrgang(schuelerKAoADaten.idJahrgang, kategorie, schuljahr);

		final KAOAMerkmal merkmal = validateMerkmal(schuelerKAoADaten.idMerkmal, kategorie, schuljahr);
		final KAOAZusatzmerkmal zusatzmerkmal = validateZusatzmerkmal(schuelerKAoADaten.idZusatzmerkmal, merkmal, schuljahr);
		final KAOAZusatzmerkmalKatalogEintrag zusatzmerkmalEintrag = validateEintragZusatzmerkmal(zusatzmerkmal, schuljahr);
		final KAOAZusatzmerkmaleOptionsarten optionsart = validateKAoAZusatzmerkmalOptionsarten(zusatzmerkmalEintrag);
		switch (optionsart) {
			case SBO_EBENE_4 -> validateEbene4(schuelerKAoADaten.idEbene4, zusatzmerkmal, schuljahr);
			case ANSCHLUSSOPTION -> validateAnschlussoption(schuelerKAoADaten.idAnschlussoption, zusatzmerkmal, schuljahr);
			case BERUFSFELD -> validateBerufsfeld(schuelerKAoADaten.idBerufsfeld);
			case FREITEXT, FREITEXT_BERUF -> validateBemerkung(schuelerKAoADaten.bemerkung);
			default -> {
				/* keine Validierung notwendig */
			}
		}
	}

	private static void validateOptionalAttributes(final SchuelerKAoADaten schuelerKAoADaten) throws ApiOperationException {
		int nonEmptyOptionalAttributeCount = 0;
		if (schuelerKAoADaten.idAnschlussoption != null) {
			nonEmptyOptionalAttributeCount++;
		}
		if (schuelerKAoADaten.idEbene4 != null) {
			nonEmptyOptionalAttributeCount++;
		}
		if (schuelerKAoADaten.idBerufsfeld != null) {
			nonEmptyOptionalAttributeCount++;
		}
		if (StringUtils.isNotBlank(schuelerKAoADaten.bemerkung)) {
			nonEmptyOptionalAttributeCount++;
		}
		if (nonEmptyOptionalAttributeCount > 1) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Es darf nur eines der folgenden Felder gefüllt sein: idAnschlussoption, idEbene4, "
					+ "idBerufsfeld, bemerkung");
		}
	}

	private void validateLernabschnittsdaten(final long idLernabschnitt) throws ApiOperationException {
		final DTOSchuelerLernabschnittsdaten lernabschnitt = getLernabschnittsdaten(idLernabschnitt);
		if (lernabschnitt.Schueler_ID != idSchueler) {
			throw new ApiOperationException(
					Status.BAD_REQUEST, "Lernabschnittsdaten mit der ID %d passen nicht zum Schueler mit der ID %d".formatted(idLernabschnitt, idSchueler));
		}
	}

	private DTOSchuelerLernabschnittsdaten getLernabschnittsdaten(final long idLernabschnitt) throws ApiOperationException {
		final DTOSchuelerLernabschnittsdaten lernabschnitt = this.conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, idLernabschnitt);
		if (lernabschnitt == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Lernabschnittsdaten mit der ID %d vorhanden.".formatted(idLernabschnitt));
		}
		return lernabschnitt;
	}

	private static KAOAKategorie validateKategorie(final long idKategorie) throws ApiOperationException {
		try {
			return KAOAKategorie.data().getWertByID(idKategorie);
		} catch (final CoreTypeException e) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine KAoAKategorie mit der ID %d vorhanden.".formatted(idKategorie));
		}
	}

	private static void validateJahrgang(final long idJahrgang, final KAOAKategorie kategorie, final int schuljahr) throws ApiOperationException {
		try {
			final Jahrgaenge jahrgang = Jahrgaenge.data().getWertByID(idJahrgang);
			if (!kategorie.hatJahrgang(schuljahr, jahrgang)) {
				throw new ApiOperationException(
						Status.BAD_REQUEST,
						"Der Jahrgang mit der ID %d für die Kategorie %s im Schuljahr %d enthalten.".formatted(idJahrgang, kategorie.name(), schuljahr)
				);
			}
		} catch (final CoreTypeException e) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Jahrgang mit der ID %d vorhanden.".formatted(idJahrgang));
		}
	}

	private static KAOAMerkmal validateMerkmal(final long idMerkmal, final KAOAKategorie kategorie, final int schuljahr) throws ApiOperationException {
		try {
			final KAOAMerkmal merkmal = KAOAMerkmal.data().getWertByID(idMerkmal);
			final KAOAMerkmalKatalogEintrag merkmalEintrag = merkmal.daten(schuljahr);
			if (merkmalEintrag == null) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Das Merkmal ist im Schuljahr %d nicht gültig.".formatted(schuljahr));
			}
			if (!merkmalEintrag.kategorie.equals(kategorie.name())) {
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Das KAoAMerkmal mit der ID %d passt nicht zur kategorie mit der ID %d.".formatted(idMerkmal, kategorie.daten(schuljahr).id));
			}
			return merkmal;
		} catch (final CoreTypeException e) {
			throw new ApiOperationException(Status.NOT_FOUND, "Kein KAoAMerkmal mit der ID %d vorhanden.".formatted(idMerkmal));
		}
	}

	private static KAOAZusatzmerkmal validateZusatzmerkmal(final long idZusatzmerkmal, final KAOAMerkmal merkmal, final int schuljahr)
			throws ApiOperationException {
		try {
			final KAOAZusatzmerkmal zusatzmerkmal = KAOAZusatzmerkmal.data().getWertByID(idZusatzmerkmal);
			final KAOAZusatzmerkmalKatalogEintrag zusatzmerkmalEintrag = validateEintragZusatzmerkmal(zusatzmerkmal, schuljahr);
			if (!zusatzmerkmalEintrag.merkmal.equals(merkmal.name())) {
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Das KAoAZusatzmerkmal mit der ID %d passt nicht zum KAoAMerkmal mit der ID %d.".formatted(idZusatzmerkmal,
								merkmal.daten(schuljahr).id));
			}
			return zusatzmerkmal;
		} catch (final CoreTypeException e) {
			throw new ApiOperationException(Status.NOT_FOUND, "Kein KAoAZusatzmerkmal mit der ID %d vorhanden.".formatted(idZusatzmerkmal));
		}
	}

	private static void validateEbene4(final Long idEbene4, final KAOAZusatzmerkmal zusatzmerkmal, final int schuljahr) throws ApiOperationException {
		try {
			if (idEbene4 == null) {
				throw new ApiOperationException(Status.BAD_REQUEST, "idEbene4 darf nicht null sein.");
			}
			final KAOAEbene4 ebene4 = KAOAEbene4.data().getWertByID(idEbene4);
			if (!ebene4.daten(schuljahr).zusatzmerkmal.equals(zusatzmerkmal.name())) {
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Die Ebene4 mit der ID %d passt nicht zum KAoAZusatzmerkmal mit der ID %d.".formatted(idEbene4, zusatzmerkmal.daten(schuljahr).id));
			}
		} catch (final CoreTypeException e) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Ebene4 mit der ID %d vorhanden.".formatted(idEbene4));
		}
	}

	private static void validateAnschlussoption(final Long idAnschlussoption, final KAOAZusatzmerkmal zusatzmerkmal, final int schuljahr)
			throws ApiOperationException {
		try {
			if (idAnschlussoption == null) {
				throw new ApiOperationException(Status.BAD_REQUEST, "idAnschlussoption darf nicht null sein.");
			}
			final KAOAAnschlussoptionen anschlussoption = KAOAAnschlussoptionen.data().getWertByID(idAnschlussoption);
			if (!anschlussoption.daten(schuljahr).anzeigeZusatzmerkmal.contains(zusatzmerkmal.name())) {
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Die Anschlussoption %d passt nicht zum KAoAZusatzmerkmal  %d.".formatted(idAnschlussoption, zusatzmerkmal.daten(schuljahr).id));
			}
		} catch (final CoreTypeException e) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Anschlussoption mit der ID %d vorhanden.".formatted(idAnschlussoption));
		}
	}

	private static void validateBerufsfeld(final Long idBerufsfeld) throws ApiOperationException {
		try {
			if (idBerufsfeld == null) {
				throw new ApiOperationException(Status.BAD_REQUEST, "idBerufsfeld darf nicht null sein.");
			}
			KAOABerufsfeld.data().getWertByID(idBerufsfeld);
		} catch (final CoreTypeException e) {
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Berufsfeld mit der ID %d vorhanden.".formatted(idBerufsfeld));
		}
	}

	private static void validateBemerkung(final String bemerkung) throws ApiOperationException {
		if ((bemerkung != null) && (bemerkung.length() > 255)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Bemerkung darf nicht mehr als 255 Zeichen beinhalten.");
		}
	}

	private static KAOAZusatzmerkmaleOptionsarten validateKAoAZusatzmerkmalOptionsarten(final KAOAZusatzmerkmalKatalogEintrag eintragZusatzmerkmal)
			throws ApiOperationException {
		try {
			return KAOAZusatzmerkmaleOptionsarten.data().getWertByBezeichner(eintragZusatzmerkmal.optionsart);
		} catch (final CoreTypeException e) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Keine Optionsart für den Bezeichner %s vorhanden.".formatted(eintragZusatzmerkmal.optionsart));
		}
	}

	private static KAOAZusatzmerkmalKatalogEintrag validateEintragZusatzmerkmal(final KAOAZusatzmerkmal zusatzmerkmal, final int schuljahr)
			throws ApiOperationException {
		final var eintragZusatzmerkmal = zusatzmerkmal.daten(schuljahr);
		if (eintragZusatzmerkmal == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Zusatzmerkmal ist im Schuljahr %d nicht gültig.".formatted(schuljahr));
		}
		return eintragZusatzmerkmal;
	}

	private void validateIdSchueler(final Long idSchueler) throws ApiOperationException {
		final DTOSchueler dtoSchueler = this.conn.queryByKey(DTOSchueler.class, idSchueler);
		if (dtoSchueler == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Schueler mit der ID %d gefunden.".formatted(idSchueler));
		}
	}

}
