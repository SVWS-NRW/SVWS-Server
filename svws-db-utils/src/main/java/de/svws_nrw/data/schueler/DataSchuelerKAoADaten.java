package de.svws_nrw.data.schueler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerKAoADaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerKAoADaten}.
 */
public final class DataSchuelerKAoADaten extends DataManagerRevised<Long, DTOSchuelerKAoADaten, SchuelerKAoADaten> {

	/** Die ID des Schülers, für welchen die KAoA-Daten geprüft werden */
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
		final DTOSchueler dtoSchueler = this.conn.queryByKey(DTOSchueler.class, idSchueler);
		if (dtoSchueler == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine SchuelerDaten mit der ID %d gefunden".formatted(idSchueler));

		this.idSchueler = idSchueler;
		setAttributesNotPatchable(Schema.tab_SchuelerKAoADaten.col_ID.javaAttributName());
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

	private void patchCoreDto(final SchuelerKAoADaten schuelerKAoADaten, final Map<String, Object> patchAttributes) throws ApiOperationException {
		try {
			final Class<?> clazz = schuelerKAoADaten.getClass();
			for (final Map.Entry<String, Object> entry : patchAttributes.entrySet()) {
				final String fieldName = entry.getKey();
				final Object fieldValue = entry.getValue();
				final Field field = clazz.getDeclaredField(fieldName);
				if (fieldValue == null) {
					field.set(schuelerKAoADaten, null);
				} else if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
					field.set(schuelerKAoADaten, ((Number) fieldValue).longValue());
				} else {
					field.set(schuelerKAoADaten, fieldValue);
				}
			}
		} catch (final NoSuchFieldException | IllegalAccessException e) {
			throw new ApiOperationException(Status.BAD_REQUEST, e.getCause(), "Fehler beim patchen des CoreDto: %s".formatted(e.getMessage()));
		}
	}

	@Override
	public SchuelerKAoADaten getById(final Long id) throws ApiOperationException {
		final DTOSchuelerKAoADaten dtoSchuelerKAoADaten = conn.queryByKey(DTOSchuelerKAoADaten.class, id);
		if (dtoSchuelerKAoADaten == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine SchuelerKAoADaten mit der ID %d gefunden".formatted(id));

		return map(dtoSchuelerKAoADaten);
	}

	@Override
	public SchuelerKAoADaten map(final DTOSchuelerKAoADaten schuelerKAoADaten) throws ApiOperationException {
		final SchuelerKAoADaten result = new SchuelerKAoADaten();
		result.id = schuelerKAoADaten.id;
		result.idKategorie = schuelerKAoADaten.idKategorie;
		result.idMerkmal = schuelerKAoADaten.idMerkmal;
		result.idZusatzmerkmal = schuelerKAoADaten.idZusatzmerkmal;
		result.idAnschlussoption = schuelerKAoADaten.idAnschlussoption;
		result.idEbene4 = schuelerKAoADaten.idEbene4;
		result.idBerufsfeld = schuelerKAoADaten.idBerufsfeld;
		result.idJahrgang = getJahrgaengeKatalogEintrag(schuelerKAoADaten).id;
		result.idSchuljahresabschnitt = getLernabschnittsdaten(schuelerKAoADaten.idLernabschnitt).Schuljahresabschnitts_ID;
		result.bemerkung = schuelerKAoADaten.bemerkung;
		return result;
	}

	/**
	 * Diese Methode liefert das Schuljahr der gegebenen SchülerKAoADaten.
	 *
	 * @param schuelerKAoADaten   schuelerKAoADaten
	 *
	 * @return schuljahr
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private int getSchuljahrByKAoADaten(final DTOSchuelerKAoADaten schuelerKAoADaten) throws ApiOperationException {
		final DTOSchuelerLernabschnittsdaten lernabschnittsdaten =
				this.conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, schuelerKAoADaten.idLernabschnitt);
		if (lernabschnittsdaten == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Keine Lernabschnittsdaten mit der ID %d vorhanden.".formatted(schuelerKAoADaten.idLernabschnitt));

		final Schuljahresabschnitt schuljahresabschnitt =
				conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(lernabschnittsdaten.Schuljahresabschnitts_ID);
		return schuljahresabschnitt.schuljahr;
	}

	@NotNull JahrgaengeKatalogEintrag getJahrgaengeKatalogEintrag(final DTOSchuelerKAoADaten schuelerKAoADaten) throws ApiOperationException {
		final int schuljahr = getSchuljahrByKAoADaten(schuelerKAoADaten);
		final Jahrgaenge jahrgang = Jahrgaenge.data().getWertBySchluessel(schuelerKAoADaten.jahrgang);
		if (jahrgang == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keinen Jahrgang mit dem Schlüssel %s gefunden.".formatted(schuelerKAoADaten.jahrgang));

		final JahrgaengeKatalogEintrag jahrgaengeKatalogEintrag = jahrgang.daten(schuljahr);
		if (jahrgaengeKatalogEintrag == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Keinen Jahrgangskatalogeintrag im Schuljahr %d mit dem Schlüssel %s gefunden.".formatted(schuljahr, schuelerKAoADaten.jahrgang));
		return jahrgaengeKatalogEintrag;
	}

	@Override
	protected void mapAttribute(final DTOSchuelerKAoADaten dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idJahrgang" -> {
				final long idJahrgang = JSONMapper.convertToLong(value, false, "Jahrgang");
				final JahrgaengeKatalogEintrag eintrag = Jahrgaenge.data().getEintragByID(idJahrgang);
				if (eintrag == null)
					throw new ApiOperationException(Status.NOT_FOUND, "Der Jahrgang mit der ID %d wurde nicht gefunden".formatted(idJahrgang));
				dto.jahrgang = eintrag.schluessel;
			}
			case "idSchuljahresabschnitt" -> dto.idLernabschnitt = getIdLernabschnitt(JSONMapper.convertToLong(value, false, "Schuljahresabschnitt"));
			case "idKategorie" -> dto.idKategorie = JSONMapper.convertToLong(value, false, "Kategorie");
			case "idMerkmal" -> dto.idMerkmal = JSONMapper.convertToLong(value, false, "Merkmal");
			case "idZusatzmerkmal" -> dto.idZusatzmerkmal = JSONMapper.convertToLong(value, false, "Zusatzmerkmal");
			case "idEbene4" -> dto.idEbene4 = JSONMapper.convertToLong(value, true, "Ebene4");
			case "idAnschlussoption" -> dto.idAnschlussoption = JSONMapper.convertToLong(value, true, "Anschlussoption");
			case "idBerufsfeld" -> dto.idBerufsfeld = JSONMapper.convertToLong(value, true, "Berufsfeld");
			case "bemerkung" -> dto.bemerkung = JSONMapper.convertToString(value, true, true, null, "Bemerkung");
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	@Override
	public List<SchuelerKAoADaten> getAll() throws ApiOperationException {
		final List<DTOSchuelerLernabschnittsdaten> schuelerLernabschnittsdaten =
				conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_SCHUELER_ID, DTOSchuelerLernabschnittsdaten.class, this.idSchueler);
		final List<Long> schuelerLernabschnittsdatenIds = schuelerLernabschnittsdaten.stream().map(s -> s.ID).toList();
		final List<DTOSchuelerKAoADaten> dtoSchuelerKAoADaten =
				conn.queryList(DTOSchuelerKAoADaten.QUERY_LIST_BY_IDLERNABSCHNITT, DTOSchuelerKAoADaten.class, schuelerLernabschnittsdatenIds);
		final List<SchuelerKAoADaten> result = new ArrayList<>();
		for (final DTOSchuelerKAoADaten dto : dtoSchuelerKAoADaten)
			result.add(this.map(dto));
		return result;
	}

	/**
	 * Validiert die Korrektheit der Attribute.
	 *
	 * @param schuelerKAoADaten   SchuelerKAoADaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private void validateAttributes(final SchuelerKAoADaten schuelerKAoADaten) throws ApiOperationException {
		validateOptionalAttributes(schuelerKAoADaten);
		validateLernabschnittsdaten(getIdLernabschnitt(schuelerKAoADaten.idSchuljahresabschnitt));
		final KAOAKategorie kategorie = validateKategorie(schuelerKAoADaten.idKategorie);
		final int schuljahr = getSchuljahr(schuelerKAoADaten.idSchuljahresabschnitt);
		validateJahrgang(schuelerKAoADaten.idJahrgang, kategorie, schuljahr);
		final KAOAMerkmal merkmal = validateMerkmal(schuelerKAoADaten.idMerkmal, kategorie, schuljahr);
		final KAOAZusatzmerkmal zusatzmerkmal = validateZusatzmerkmal(schuelerKAoADaten.idZusatzmerkmal, merkmal, schuljahr);
		final KAOAZusatzmerkmalKatalogEintrag eintrag = validateEintragZusatzmerkmal(zusatzmerkmal, schuljahr);
		final KAOAZusatzmerkmaleOptionsarten optionsart = validateKAoAZusatzmerkmalOptionsarten(eintrag);
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

	private int getSchuljahr(final long schuljahresabschnittsId) throws ApiOperationException {
		try {
			return conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(schuljahresabschnittsId).schuljahr;
		} catch (final DeveloperNotificationException e) {
			throw new ApiOperationException(Status.NOT_FOUND, e.getCause(), "Kein Schuljahresabschnitt zur ID %d gefunden.".formatted(schuljahresabschnittsId));
		}
	}

	private static void validateOptionalAttributes(final SchuelerKAoADaten schuelerKAoADaten) throws ApiOperationException {
		int nonEmptyOptionalAttributeCount = 0;
		if (schuelerKAoADaten.idAnschlussoption != null)
			nonEmptyOptionalAttributeCount++;
		if (schuelerKAoADaten.idEbene4 != null)
			nonEmptyOptionalAttributeCount++;
		if (schuelerKAoADaten.idBerufsfeld != null)
			nonEmptyOptionalAttributeCount++;
		if ((schuelerKAoADaten.bemerkung != null) && !schuelerKAoADaten.bemerkung.isBlank())
			nonEmptyOptionalAttributeCount++;
		if (nonEmptyOptionalAttributeCount > 1) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Anzahl vorhandener optionaler Attribute ist größer 1");
		}
	}

	private long getIdLernabschnitt(final long idSchuljahresabschnitt) throws ApiOperationException {
		final List<DTOSchuelerLernabschnittsdaten> schuelerLernabschnittsdaten =
				conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOSchuelerLernabschnittsdaten.class, idSchuljahresabschnitt);
		return schuelerLernabschnittsdaten.stream().filter(e -> e.Schueler_ID == idSchueler).filter(e -> e.WechselNr == 0).findFirst().map(e -> e.ID)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Keine Lernabschnittsdaten zur IdSchuljahresabschnitt %d gefunden".formatted(idSchuljahresabschnitt)));
	}

	/**
	 * Validiert, ob die Lernabschnittsdaten zum ausgewählten Schüler passen.
	 *
	 * @param idLernabschnitt   idLernabschnitt
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private void validateLernabschnittsdaten(final long idLernabschnitt) throws ApiOperationException {
		final DTOSchuelerLernabschnittsdaten lernabschnittsdaten = getLernabschnittsdaten(idLernabschnitt);
		if (lernabschnittsdaten.Schueler_ID != idSchueler)
			throw new ApiOperationException(Status.BAD_REQUEST, "Lernabschnittsdaten mit der ID %d passen nicht zum Schueler mit der ID %d"
					.formatted(idLernabschnitt, idSchueler));
	}

	private DTOSchuelerLernabschnittsdaten getLernabschnittsdaten(final long idLernabschnitt) throws ApiOperationException {
		final DTOSchuelerLernabschnittsdaten lernabschnittsdaten = this.conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, idLernabschnitt);
		if (lernabschnittsdaten == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Lernabschnittsdaten mit der ID %d vorhanden.".formatted(idLernabschnitt));
		return lernabschnittsdaten;
	}

	/**
	 * Validiert, ob eine gültige Kategorie ausgewählt wurde.
	 *
	 * @param idKategorie   idKategorie
	 *
	 * @return KAoAKategorie
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private static KAOAKategorie validateKategorie(final long idKategorie) throws ApiOperationException {
		try {
			return KAOAKategorie.data().getWertByID(idKategorie);
		} catch (final CoreTypeException e) {
			throw new ApiOperationException(Status.NOT_FOUND, e.getCause(), "Keine KAoAKategorie mit der ID %d vorhanden.".formatted(idKategorie));
		}
	}

	/**
	 * Validiert den Jahrgang (abhängig von der ausgewählten Kategorie).
	 *
	 * @param idJahrgang   idJahrgang
	 * @param kategorie    KAOAKategorie
	 * @param schuljahr    Schuljahr, für das die Daten validiert werden
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private static void validateJahrgang(final long idJahrgang, final KAOAKategorie kategorie, final int schuljahr) throws ApiOperationException {
		try {
			final JahrgaengeKatalogEintrag eintrag = Jahrgaenge.data().getEintragByID(idJahrgang);
			if (eintrag == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Kein Jahrgangseintrag mit der ID %d vorhanden.".formatted(idJahrgang));
			final Jahrgaenge jahrgang = Jahrgaenge.data().getWertByID(idJahrgang);
			if (!kategorie.hatJahrgang(schuljahr, jahrgang))
				throw new ApiOperationException(Status.BAD_REQUEST, "Der Jahrgang mit der ID %d ist nicht in der Kategorie %s für das Schuljahr %d enthalten."
						.formatted(idJahrgang, kategorie.name(), schuljahr));
		} catch (final CoreTypeException e) {
			throw new ApiOperationException(Status.NOT_FOUND, e.getCause(), "Keine Jahrgang mit der ID %d vorhanden.".formatted(idJahrgang));
		}
	}

	/**
	 * Validiert das Merkmal (abhängig von der ausgewählten Kategorie).
	 *
	 * @param idMerkmal    idMerkmal
	 * @param kategorie    KAOAKategorie
	 * @param schuljahr    Schuljahr, für das die Daten validiert werden
	 *
	 * @return KAOAMerkmal
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private static KAOAMerkmal validateMerkmal(final long idMerkmal, final KAOAKategorie kategorie, final int schuljahr) throws ApiOperationException {
		try {
			final KAOAMerkmal merkmal = KAOAMerkmal.data().getWertByID(idMerkmal);
			final KAOAMerkmalKatalogEintrag eintrag = merkmal.daten(schuljahr);
			if (eintrag == null)
				throw new ApiOperationException(Status.BAD_REQUEST, "Das Merkmal ist im Schuljahr %d nicht gültig.".formatted(schuljahr));
			if (!eintrag.kategorie.equals(kategorie.name()))
				throw new ApiOperationException(Status.BAD_REQUEST, "Das KAoAMerkmal mit der ID %d passt nicht zur kategorie mit der ID %d."
						.formatted(idMerkmal, kategorie.daten(schuljahr).id));
			return merkmal;
		} catch (final CoreTypeException e) {
			throw new ApiOperationException(Status.NOT_FOUND, e.getCause(), "Kein KAoAMerkmal mit der ID %d vorhanden.".formatted(idMerkmal));
		}
	}

	/**
	 * Validiert das Zusatzmerkmal (abhängig vom ausgewählten Merkmal).
	 *
	 * @param idZusatzmerkmal   idZusatzmerkmal
	 * @param merkmal           KAOAMerkmal
	 * @param schuljahr         Schuljahr, für das die Daten validiert werden
	 *
	 * @return Zusatzmerkmal
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private static KAOAZusatzmerkmal validateZusatzmerkmal(final long idZusatzmerkmal, final KAOAMerkmal merkmal, final int schuljahr)
			throws ApiOperationException {
		try {
			final KAOAZusatzmerkmal zusatzmerkmal = KAOAZusatzmerkmal.data().getWertByID(idZusatzmerkmal);
			final KAOAZusatzmerkmalKatalogEintrag eintrag = validateEintragZusatzmerkmal(zusatzmerkmal, schuljahr);
			if (!eintrag.merkmal.equals(merkmal.name()))
				throw new ApiOperationException(Status.BAD_REQUEST, "Das KAoAZusatzmerkmal mit der ID %d passt nicht zum KAoAMerkmal mit der ID %d."
						.formatted(idZusatzmerkmal, merkmal.daten(schuljahr).id));
			return zusatzmerkmal;
		} catch (final CoreTypeException e) {
			throw new ApiOperationException(Status.NOT_FOUND, e.getCause(), "Kein KAoAZusatzmerkmal mit der ID %d vorhanden.".formatted(idZusatzmerkmal));
		}
	}

	/**
	 * Validiert die Ebene4 (abhängig vom ausgewählten Zusatzmerkmal).
	 *
	 * @param idEbene4        idEbene4
	 * @param zusatzmerkmal   KAOAZusatzmerkmal
	 * @param schuljahr       Schuljahr, für das die Daten validiert werden
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private static void validateEbene4(final Long idEbene4, final KAOAZusatzmerkmal zusatzmerkmal, final int schuljahr) throws ApiOperationException {
		try {
			if (idEbene4 == null)
				throw new ApiOperationException(Status.BAD_REQUEST, "idEbene4 darf nicht null sein.");
			final KAOAEbene4 ebene4 = KAOAEbene4.data().getWertByID(idEbene4);
			if (!ebene4.daten(schuljahr).zusatzmerkmal.equals(zusatzmerkmal.name()))
				throw new ApiOperationException(Status.BAD_REQUEST, "Die Ebene4 mit der ID %d passt nicht zum KAoAZusatzmerkmal mit der ID %d."
						.formatted(idEbene4, zusatzmerkmal.daten(schuljahr).id));
		} catch (final CoreTypeException e) {
			throw new ApiOperationException(Status.NOT_FOUND, e.getCause(), "Keine Ebene4 mit der ID %d vorhanden.".formatted(idEbene4));
		}
	}

	/**
	 * Validiert die Anschlussoption (abhängig vom ausgewählten Zusatzmerkmal).
	 *
	 * @param idAnschlussoption   idAnschlussoption
	 * @param zusatzmerkmal       KAOAZusatzmerkmal
	 * @param schuljahr           Schuljahr, für das die Daten validiert werden
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private static void validateAnschlussoption(final Long idAnschlussoption, final KAOAZusatzmerkmal zusatzmerkmal, final int schuljahr)
			throws ApiOperationException {
		try {
			if (idAnschlussoption == null)
				throw new ApiOperationException(Status.BAD_REQUEST, "idAnschlussoption darf nicht null sein.");
			final KAOAAnschlussoptionen anschlussoptionen = KAOAAnschlussoptionen.data().getWertByID(idAnschlussoption);
			if (!anschlussoptionen.daten(schuljahr).anzeigeZusatzmerkmal.contains(zusatzmerkmal.name()))
				throw new ApiOperationException(Status.BAD_REQUEST, "Die Anschlussoption mit der ID %d passt nicht zum KAoAZusatzmerkmal mit der ID %d."
						.formatted(idAnschlussoption, zusatzmerkmal.daten(schuljahr).id));
		} catch (final CoreTypeException e) {
			throw new ApiOperationException(Status.NOT_FOUND, e.getCause(), "Keine Anschlussoption mit der ID %d vorhanden.".formatted(idAnschlussoption));
		}
	}

	/**
	 * Validiert das Berufsfeld (abhängig vom ausgewählten Zusatzmerkmal).
	 *
	 * @param idBerufsfeld    idBerufsfeld
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private static void validateBerufsfeld(final Long idBerufsfeld) throws ApiOperationException {
		try {
			if (idBerufsfeld == null)
				throw new ApiOperationException(Status.BAD_REQUEST, "idBerufsfeld darf nicht null sein.");
			KAOABerufsfeld.data().getWertByID(idBerufsfeld);
		} catch (final CoreTypeException e) {
			throw new ApiOperationException(Status.NOT_FOUND, e.getCause(), "Kein Berufsfeld mit der ID %d vorhanden.".formatted(idBerufsfeld));
		}
	}

	/**
	 * Validiert die optionale Bemerkung, insofern vorhanden - es sind maximal 255 Zeichen erlaubt.
	 *
	 * @param bemerkung       bemerkung
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private static void validateBemerkung(final String bemerkung) throws ApiOperationException {
		if ((bemerkung != null) && (bemerkung.length() > 255))
			throw new ApiOperationException(Status.BAD_REQUEST, "Bemerkung darf nicht mehr als 255 Zeichen beinhalten.");
	}

	private static KAOAZusatzmerkmaleOptionsarten validateKAoAZusatzmerkmalOptionsarten(final KAOAZusatzmerkmalKatalogEintrag eintragZusatzmerkmal)
			throws ApiOperationException {
		try {
			return KAOAZusatzmerkmaleOptionsarten.data().getWertByBezeichner(eintragZusatzmerkmal.optionsart);
		} catch (final CoreTypeException e) {
			throw new ApiOperationException(Status.NOT_FOUND, e.getCause(), "Kein Optionsart für den Bezeichner %s vorhanden."
					.formatted(eintragZusatzmerkmal.optionsart));
		}
	}

	private static KAOAZusatzmerkmalKatalogEintrag validateEintragZusatzmerkmal(final KAOAZusatzmerkmal zusatzmerkmal, final int schuljahr)
			throws ApiOperationException {
		final var eintragZusatzmerkmal = zusatzmerkmal.daten(schuljahr);
		if (eintragZusatzmerkmal == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Zusatzmerkmal ist im Schuljahr %d nicht gültig.".formatted(schuljahr));
		return eintragZusatzmerkmal;
	}

}
