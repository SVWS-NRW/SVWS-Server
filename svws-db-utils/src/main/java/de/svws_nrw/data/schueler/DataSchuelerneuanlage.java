package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.data.schueler.SchuelerStatusKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogEinwilligungsart;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerDatenschutz;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerFoto;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattform;
import de.svws_nrw.db.dto.current.svws.auth.DTOLernplattformen;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;

public final class DataSchuelerneuanlage extends DataManagerRevised<Long, DTOSchueler, SchuelerStammdaten> {

	private final Long idSchuljahresabschnitt;
	private final DataSchuelerLernabschnittsdaten dataSchuelerLernabschnittsdaten;
	private static final String KEY_SCHULJAHRESABSCHNITT = "schuljahresabschnitt";

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link SchuelerStammdaten}.
	 *
	 * @param conn                              die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idSchuljahresabschnitt            die ID des Schuljahresabschnitts
	 * @param dataSchuelerLernabschnittsdaten   die Lernabschnittsdaten eines Schülers
	 */
	public DataSchuelerneuanlage(final DBEntityManager conn, final Long idSchuljahresabschnitt,
			final DataSchuelerLernabschnittsdaten dataSchuelerLernabschnittsdaten) {
		super(conn);
		this.idSchuljahresabschnitt = idSchuljahresabschnitt;
		this.dataSchuelerLernabschnittsdaten = dataSchuelerLernabschnittsdaten;
		setAttributesRequiredOnCreation("nachname", "vorname", "geschlecht", "geburtsdatum");
		setAttributesNotPatchable("id");
	}

	@Override
	public SchuelerStammdaten getById(final Long id) throws ApiOperationException {
		final DTOSchueler dto = getDTO(id);
		final SchuelerStammdaten schuelerStammdaten = map(dto);
		final DTOSchuelerFoto dtoSchuelerFoto = conn.queryByKey(DTOSchuelerFoto.class, id);
		schuelerStammdaten.foto = Optional.ofNullable(dtoSchuelerFoto)
				.map(sf -> sf.FotoBase64)
				.orElse(null);
		return schuelerStammdaten;
	}

	/**
	 * Die Methode ermittelt das entsprechende {@link DTOSchueler} Objekt zur übergebenen ID.
	 *
	 * @param id   die ID des Schülers
	 *
	 * @return das zugehörige {@link DTOSchueler} Objekt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public DTOSchueler getDTO(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die ID für den Schüler darf nicht null sein.");

		final DTOSchueler dto = conn.queryByKey(DTOSchueler.class, id);
		if (dto == null)
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Kein Schüler zur ID %d gefunden.".formatted(id));

		return dto;
	}

	@Override
	protected void initDTO(final DTOSchueler dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
		dto.GU_ID = String.format("{%s}", UUID.randomUUID());
		dto.Schuljahresabschnitts_ID = idSchuljahresabschnitt;
		dto.Nachname = "";
		dto.Vorname = "";
		dto.AlleVornamen = "";
		dto.Geschlecht = Geschlecht.X;
		dto.Geburtsdatum = "";
		dto.KonfDruck = false;
		dto.Migrationshintergrund = false;
		dto.idStatus = null;
		dto.Duplikat = false;
		dto.AnmeldeDatum = null;
		dto.Aufnahmedatum = null;
		dto.Volljaehrig = false;
		dto.KeineAuskunft = false;
		dto.SchulpflichtErf = false;
		dto.BerufsschulpflErf = false;
		dto.MasernImpfnachweis = false;
		dto.Bafoeg = false;
		dto.MeisterBafoeg = false;
		dto.BeginnBildungsgang = null;
	}

	@Override
	protected SchuelerStammdaten map(final DTOSchueler dto) throws ApiOperationException {
		final SchuelerStammdaten daten = new SchuelerStammdaten();
		daten.id = dto.ID;
		daten.nachname = Objects.requireNonNullElse(dto.Nachname, "");
		daten.vorname = Objects.requireNonNullElse(dto.Vorname, "");
		daten.alleVornamen = Objects.requireNonNullElse(dto.AlleVornamen, "");
		daten.geschlecht = Optional.ofNullable(dto.Geschlecht).map(g -> g.id).orElse(-1);
		daten.geburtsdatum = dto.Geburtsdatum;
		daten.status = Optional.ofNullable(dto.idStatus).orElse(-1);
		daten.anmeldedatum = dto.AnmeldeDatum;
		daten.aufnahmedatum = dto.Aufnahmedatum;
		daten.beginnBildungsgang = dto.BeginnBildungsgang; // Schulform BK und SB
		daten.dauerBildungsgang = dto.DauerBildungsgang; // Schulform BK und SB
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOSchueler dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> validateID(dto, value, name);
			case "nachname" -> updateNachname(dto, value, name);
			case "vorname" -> updateVorname(dto, value, name);
			case "alleVornamen" -> updateAlleVornamen(dto, value, name);
			case "geschlecht" -> updateGeschlecht(dto, value, name);
			case "geburtsdatum" -> updateGeburtsdatum(dto, value, name);
			case "status" -> updateStatus(dto, value, name);
			case "anmeldedatum" -> updateAnmeldedatum(dto, value, name);
			case "aufnahmedatum" -> updateAufnahmedatum(dto, value, name);
			case "beginnBildungsgang" -> updateBeginnBildungsgang(dto, value, name);
			case "dauerBildungsgang" -> updateDauerBildungsgang(dto, value, name);
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST, "Das Patchen des Attributes %s ist nicht implementiert.".formatted(name));
		}
	}

	/**
	 * Erstellt einen Schüler aus dem InputStream und liefert das erstellte Core-DTO zurück.
	 * Für den Schüler wird ein Lernabschnitt angelegt. Außerdem werden dem Schüler alle Einwilligungen und Lernplattformen hinzugefügt.
	 *
	 * @param is InputStream mit den JSON-Daten
	 * @return das erstellte SchuelerStammdaten Core-DTO
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response addNewSchuelerWithLernabschnitt(final InputStream is) throws ApiOperationException {
		final Map<String, Object> initAttributes = JSONMapper.toMap(is);

		final Map<String, Object> lernAbschnittAttributes = extractAttributes(initAttributes);

		final SchuelerStammdaten createdSchueler = this.add(initAttributes);

		persistEinwilligungenToNewSchueler(createdSchueler.id);

		persistLernplattformenToNewSchueler(createdSchueler.id);

		createLernabschnittForNewSchueler(createdSchueler.id, lernAbschnittAttributes);

		return Response.status(Response.Status.CREATED).entity(createdSchueler).build();
	}

	private Map<String, Object> extractAttributes(final Map<String, Object> initAttributes) {
		final Map<String, Object> extractedAttributes = new HashMap<>();
		final String[] keysToExtract = { KEY_SCHULJAHRESABSCHNITT, "jahrgangID", "klassenID" };

		for (final String k : keysToExtract)
			if (initAttributes.containsKey(k)) {
				final Object value = initAttributes.remove(k);
				extractedAttributes.put(k, value);
			}

		return extractedAttributes;
	}

	private void persistEinwilligungenToNewSchueler(final long idSchueler) {
		final List<DTOKatalogEinwilligungsart> katalogEinwilligungsarten = conn.queryAll(DTOKatalogEinwilligungsart.class);
		final List<DTOSchuelerDatenschutz> einwilligungen = katalogEinwilligungsarten.stream()
				.filter(e -> e.personTyp == PersonTyp.SCHUELER)
				.map(e -> new DTOSchuelerDatenschutz(idSchueler, e.ID, false, false))
				.toList();

		if (!einwilligungen.isEmpty()) {
			conn.transactionPersistAll(einwilligungen);
			conn.transactionFlush();
		}
	}

	private void persistLernplattformenToNewSchueler(final long idSchueler) {
		final List<DTOLernplattformen> katalogLernplattformen = conn.queryAll(DTOLernplattformen.class);
		final List<DTOSchuelerLernplattform> lernplattformen = katalogLernplattformen.stream()
				.map(e -> new DTOSchuelerLernplattform(idSchueler, e.ID, false, false, false, false))
				.toList();

		if (!lernplattformen.isEmpty()) {
			conn.transactionPersistAll(lernplattformen);
			conn.transactionFlush();
		}
	}

	private void createLernabschnittForNewSchueler(final long idSchueler, final Map<String, Object> lernabschnittAttributes) throws ApiOperationException {
		if (lernabschnittAttributes.isEmpty())
			return;

		Long tmpIdSchuljahresabschnitt = null;

		if (lernabschnittAttributes.containsKey(KEY_SCHULJAHRESABSCHNITT)) {
			final Object value = lernabschnittAttributes.remove(KEY_SCHULJAHRESABSCHNITT);
			tmpIdSchuljahresabschnitt = JSONMapper.convertToLong(value, false, KEY_SCHULJAHRESABSCHNITT);
		}
		lernabschnittAttributes.put("schuelerID", idSchueler);

		if (tmpIdSchuljahresabschnitt != null)
			lernabschnittAttributes.put(KEY_SCHULJAHRESABSCHNITT, tmpIdSchuljahresabschnitt);

		this.dataSchuelerLernabschnittsdaten.add(lernabschnittAttributes);
	}

	private static void validateID(final DTOSchueler dto, final Object value, final String name) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if ((id == null) || (id != dto.ID))
			throw new ApiOperationException(Response.Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
	}

	private static void updateNachname(final DTOSchueler dto, final Object value, final String name) throws ApiOperationException {
		dto.Nachname = JSONMapper.convertToString(value, false, false, Schema.tab_Schueler.col_Name.datenlaenge(), name);
	}

	private static void updateVorname(final DTOSchueler dto, final Object value, final String name) throws ApiOperationException {
		dto.Vorname = JSONMapper.convertToString(value, false, false, Schema.tab_Schueler.col_Vorname.datenlaenge(), name);
	}

	private static void updateAlleVornamen(final DTOSchueler dto, final Object value, final String name) throws ApiOperationException {
		dto.AlleVornamen = JSONMapper.convertToString(value, false, true, Schema.tab_Schueler.col_Zusatz.datenlaenge(), name);
	}

	private static void updateGeschlecht(final DTOSchueler dto, final Object value, final String name) throws ApiOperationException {
		final Integer idGeschlecht = JSONMapper.convertToInteger(value, false, name);
		final Geschlecht geschlecht = Geschlecht.fromValue(idGeschlecht);
		if (geschlecht == null)
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Geschlecht darf nicht null sein.");

		dto.Geschlecht = geschlecht;
	}

	private static void updateGeburtsdatum(final DTOSchueler dto, final Object value, final String name) throws ApiOperationException {
		dto.Geburtsdatum = JSONMapper.convertToString(value, false, false, null, name);
	}

	private void updateStatus(final DTOSchueler dto, final Object value, final String name) throws ApiOperationException {
		final int idStatus = JSONMapper.convertToInteger(value, false, name);
		final SchuelerStatus schuelerStatus = SchuelerStatus.data().getWertByID(idStatus);

		final long schuljahresabschnittsID = Objects.requireNonNullElse(dto.Schuljahresabschnitts_ID, idSchuljahresabschnitt);
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(schuljahresabschnittsID);
		final SchuelerStatusKatalogEintrag schuelerStatusEintrag = schuelerStatus.daten(abschnitt.schuljahr);
		if (schuelerStatusEintrag == null)
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Kein Schülerstatuseintrag gefunden.");

		dto.idStatus = idStatus;
	}

	private static void updateAnmeldedatum(final DTOSchueler dto, final Object value, final String name) throws ApiOperationException {
		dto.AnmeldeDatum = JSONMapper.convertToString(value, true, false, null, name);
	}

	private static void updateAufnahmedatum(final DTOSchueler dto, final Object value, final String name) throws ApiOperationException {
		dto.Aufnahmedatum = JSONMapper.convertToString(value, true, false, null, name);
	}

	private static void updateBeginnBildungsgang(final DTOSchueler dto, final Object value, final String name) throws ApiOperationException {
		dto.BeginnBildungsgang = JSONMapper.convertToString(value, true, false, Schema.tab_Schueler.col_BeginnBildungsgang.datenlaenge(), name);
	}

	private static void updateDauerBildungsgang(final DTOSchueler dto, final Object value, final String name) throws ApiOperationException {
		dto.DauerBildungsgang = JSONMapper.convertToInteger(value, true, name);
	}
}
