package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.klassen.KlassenartKatalogEintrag;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittNachpruefung;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittNachpruefungsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.asd.data.schule.FachklasseKatalogEintrag;
import de.svws_nrw.asd.data.schule.OrganisationsformKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchulgliederungKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.fach.BilingualeSprache;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.types.schueler.Versetzungsvermerk;
import de.svws_nrw.asd.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.asd.types.schule.BerufskollegOrganisationsformen;
import de.svws_nrw.asd.types.schule.Fachklasse;
import de.svws_nrw.asd.types.schule.SchulabschlussAllgemeinbildend;
import de.svws_nrw.asd.types.schule.SchulabschlussBerufsbildend;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link SchuelerLernabschnittsdaten}.
 */
public final class DataSchuelerLernabschnittsdaten extends DataManagerRevised<Long, DTOSchuelerLernabschnittsdaten, SchuelerLernabschnittsdaten> {

	private static final String KLASSEN_ID = "klassenID";
	private static final String JAHRGANG_ID = "jahrgangID";
	private static final String DATUM_ANFANG = "datumAnfang";
	private static final String DATUM_ENDE = "datumEnde";
	private static final String DATUM_KONFERENZ = "datumKonferenz";
	private static final String DATUM_ZEUGNIS = "datumZeugnis";

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link SchuelerLernabschnittsdaten}.
	 *
	 * @param conn                   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerLernabschnittsdaten(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("schuelerID", "schuljahresabschnitt");
		setAttributesDelayedOnCreation(KLASSEN_ID, JAHRGANG_ID);
	}

	@Override
	protected void initDTO(final DTOSchuelerLernabschnittsdaten dto, final Long newId, final Map<String, Object> initAttributes) {
		dto.ID = newId;
		dto.Schwerbehinderung = false;
		dto.AOSF = false;
		dto.Autist = false;
		dto.ZieldifferentesLernen = false;
		dto.SemesterWertung = true;
		dto.Wiederholung = false;
		dto.FachPraktAnteilAusr = true;
		dto.WechselNr = 0;
	}

	/**
	 * Liefert die Lernabschnittsdaten für einen bestimmten Schüler in einem bestimmten Schuljahresabschnitt
	 *
	 * @param idSchueler                idSchueler
	 * @param idSchulejahresabschnitt    idSchulejahresabschnitt
	 *
	 * @return Lernabschnittsdaten für einen bestimmten Schüler in einem bestimmten Schuljahresabschnitt als Response
	 */
	public Response getAbschnittsdatenByIdSchuelerAndIdJahresabschnitt(final long idSchueler, final long idSchulejahresabschnitt) {
		final List<SchuelerLernabschnittsdaten> payload = this.conn
				.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_SCHUELER_ID, DTOSchuelerLernabschnittsdaten.class, idSchueler)
				.stream()
				.filter(l -> l.Schuljahresabschnitts_ID == idSchulejahresabschnitt)
				.map(this::map)
				.sorted(Comparator.comparing(l -> l.id))
				.toList();

		return Response
				.status(Status.OK)
				.type(MediaType.APPLICATION_JSON)
				.entity(payload)
				.build();
	}

	@Override
	public SchuelerLernabschnittsdaten map(final DTOSchuelerLernabschnittsdaten dto) {
		// Mappe zunächst die Daten des DTO.
		final SchuelerLernabschnittsdaten daten = mapInternalDTOSchuelerLernabschnittsdaten(dto);

		// Ermittle die Fachbemerkungen des Lernabschnitts und ergänze sie im Daten-Objekt.
		final List<DTOSchuelerPSFachBemerkungen> bemerkungen = this.conn.queryList(
				DTOSchuelerPSFachBemerkungen.QUERY_BY_ABSCHNITT_ID, DTOSchuelerPSFachBemerkungen.class, dto.ID);
		if (bemerkungen == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Datensatz mit Bemerkungen zur Abschnitt-ID " + dto.ID + " gefunden.");
		}
		if (bemerkungen.size() > 1) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Mehr als einen Datensatz mit Bemerkungen zur Abschnitt-ID " + dto.ID + " gefunden.");
		}
		if (!bemerkungen.isEmpty()) {
			final DTOSchuelerPSFachBemerkungen b = bemerkungen.getFirst();
			daten.bemerkungen.zeugnisASV = b.ASV;
			daten.bemerkungen.zeugnisLELS = b.LELS;
			daten.bemerkungen.zeugnisAUE = b.AUE;
			daten.bemerkungen.uebergangESF = b.ESF;
			daten.bemerkungen.foerderschwerpunkt = b.BemerkungFSP;
			daten.bemerkungen.versetzungsentscheidung = b.BemerkungVersetzung;
		}

		// Ermittle die Leistungsdaten des Lernabschnitts und ergänze sie im daten-Objekt.
		if (!(new DataSchuelerLeistungsdaten(this.conn).getByLernabschnitt(dto.ID, daten.leistungsdaten))) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Keine Leistungsdaten zur Abschnitt-ID " + dto.ID + " gefunden.");
		}

		return daten;
	}

	/**
	 * Überführt die Daten des DTO eines Schülerlernabschnitts in ein entsprechendes Core-Data-Objekt, aber ohne Leistungsdaten.
	 *
	 * @param dto das DTO mit den SchuelerLernabschnittsdaten.
	 *
	 * @return    ein Core-Data-Objekt mit den SchuelerLernabschnittsdaten ohne Leistungsdaten.
	 */
	public SchuelerLernabschnittsdaten mapOhneLeistungsdaten(final DTOSchuelerLernabschnittsdaten dto) {
		final SchuelerLernabschnittsdaten daten = mapInternalDTOSchuelerLernabschnittsdaten(dto);

		// Ermittle die Fachbemerkungen des Lernabschnitts und ergänze sie im Daten-Objekt.
		final List<DTOSchuelerPSFachBemerkungen> bemerkungen = this.conn.queryList(
				DTOSchuelerPSFachBemerkungen.QUERY_BY_ABSCHNITT_ID, DTOSchuelerPSFachBemerkungen.class, dto.ID);
		if (bemerkungen == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Datensatz mit Bemerkungen zur Abschnitt-ID " + dto.ID + " gefunden.");
		}
		if (bemerkungen.size() > 1) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Mehr als einen Datensatz mit Bemerkungen zur Abschnitt-ID " + dto.ID + " gefunden.");
		}
		if (!bemerkungen.isEmpty()) {
			final DTOSchuelerPSFachBemerkungen b = bemerkungen.getFirst();
			daten.bemerkungen.zeugnisASV = b.ASV;
			daten.bemerkungen.zeugnisLELS = b.LELS;
			daten.bemerkungen.zeugnisAUE = b.AUE;
			daten.bemerkungen.uebergangESF = b.ESF;
			daten.bemerkungen.foerderschwerpunkt = b.BemerkungFSP;
			daten.bemerkungen.versetzungsentscheidung = b.BemerkungVersetzung;
		}

		daten.leistungsdaten = new ArrayList<>();

		return daten;
	}


	@Override
	public void checkBeforeCreation(final Long newID, final Map<String, Object> initAttributes) {
		// Prüfe ggf., ob der Benutzer die Rechte in Abhängigkeit der Klasse hat, um die Lernabschnittsdaten in dem Lernabschnitt zu erstellen
		final Long idKlasse = JSONMapper.convertToLong(initAttributes.get(KLASSEN_ID), true);
		checkFunktionsbezogeneKompetenzAufKlasse((idKlasse == null) ? null : List.of(idKlasse));
	}


	@Override
	public void checkBeforePatch(final DTOSchuelerLernabschnittsdaten dto, final Map<String, Object> patchAttributes) {
		// Prüfe ggf., ob der Benutzer die Rechte in Abhängigkeit der Klasse hat, um die Lernabschnittsdaten in dem Lernabschnitt zu verändern
		if (patchAttributes.get(KLASSEN_ID) != null) {
			final Long idKlasse = JSONMapper.convertToLong(patchAttributes.get(KLASSEN_ID), true);
			checkFunktionsbezogeneKompetenzAufKlasse(List.of(idKlasse));
		}
		checkFunktionsbezogeneKompetenzAufKlasse((dto.Klassen_ID == null) ? null : List.of(dto.Klassen_ID));
	}


	@Override
	public void checkBeforeDeletion(final List<DTOSchuelerLernabschnittsdaten> dtos) {
		final boolean hasKlassenIdNull = dtos.stream().anyMatch(l -> (l.Klassen_ID == null));
		// Prüfe ggf., ob der Benutzer die Rechte in Abhängigkeit der Klasse hat, um die Lernabschnittsdaten zu löschen
		checkFunktionsbezogeneKompetenzAufKlasse(hasKlassenIdNull ? null : dtos.stream().map(l -> l.Klassen_ID).toList());
	}


	@Override
	public SchuelerLernabschnittsdaten getById(final Long id) {
		// Prüfe, ob der Lernabschnitt mit der ID existiert
		if (id == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es ist keine Abschnitt-ID angegeben worden.");
		}
		final DTOSchuelerLernabschnittsdaten dto = this.conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Lernabschnittsdaten zur Abschnitt-ID " + id + " gefunden.");
		}
		return map(dto);
	}


	/**
	 * Bestimmt die Lernabschnittsdaten zur Wechsel-Nr. 0 (aktiver Abschnitt im Schuljahresabschnitt)
	 * anhand der übergebenen Schüler-ID und dem angegebenen Schuljahresabschnitt
	 *
	 * @param schueler_id            die Schüler-ID
	 * @param schuljahresabschnitt   der Schuljahresabschnitt
	 *
	 * @return die Lernabschnittsdaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response get(final Long schueler_id, final long schuljahresabschnitt) {
		final SchuelerLernabschnittsdaten daten = getFromSchuelerIDUndSchuljahresabschnittID(schueler_id, schuljahresabschnitt);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Bestimmt die Lernabschnittsdaten anhand der übergebenen Schüler-ID und dem angegebenen Schuljahresabschnitt.
	 *
	 * @param schueler_id            die Schüler-ID
	 * @param schuljahresabschnitt_id   der Schuljahresabschnitt
	 *
	 * @return die Lernabschnittsdaten zu den übergebenen IDs.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public SchuelerLernabschnittsdaten getFromSchuelerIDUndSchuljahresabschnittID(final Long schueler_id, final long schuljahresabschnitt_id)
			throws ApiOperationException {
		// Prüfe, ob der Schüler mit der ID existiert
		if (schueler_id == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es ist keine Schueler-ID angegeben worden.");
		}
		final DTOSchueler schueler = this.conn.queryByKey(DTOSchueler.class, schueler_id);
		if (schueler == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Schüler mit der ID " + schueler_id + " gefunden.");
		}

		// Bestimme den aktuellen Lernabschnitt
		final String jpql = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = ?1 and e.Schuljahresabschnitts_ID = ?2 and e.WechselNr = 0";
		final List<DTOSchuelerLernabschnittsdaten> lernabschnittsdaten = this.conn.queryList(jpql, DTOSchuelerLernabschnittsdaten.class, schueler_id,
				schuljahresabschnitt_id);
		if ((lernabschnittsdaten == null) || lernabschnittsdaten.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Lernabschnitt zum Schüler mit der ID " + schueler_id + " und der Schuljahresabschnitt-ID "
					+ schuljahresabschnitt_id + " gefunden.");
		}
		if (lernabschnittsdaten.size() > 1) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Mehr als einen aktuellen Lernabschnitt zum Schüler mit der ID " + schueler_id
					+ " und der Schuljahresabschnitt-ID " + schuljahresabschnitt_id + " gefunden.");
		}

		return getById(lernabschnittsdaten.getFirst().ID);
	}


	/**
	 * Erstellt eine Liste von Lernabschnittsdaten anhand der übergebenen Schüler-IDs gemäß der übergebenen Parameter.
	 *
	 * @param idsSchueler                 die Liste mit Schüler-IDs
	 * @param mitLeistungsdaten legt fest, ob die erzeugten Objekte bereits die Fachbemerkungen und Leistungsdaten des Lernabschnitts enthalten.
	 * @param validiereSchueler            legt fest, ob die übergebenen Schüler-IDs validiert werden sollen. Diese Option sollte, stets true sein, sofern
	 *                                     nicht vorher an der Stelle eine Validierung der IDs vorab stattgefunden hat.
	 *
	 * @return die Lernabschnittsdaten zu den übergebenen IDs.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<SchuelerLernabschnittsdaten> getListFromSchuelerIDs(final List<Long> idsSchueler, final boolean mitLeistungsdaten,
			final boolean validiereSchueler)
			throws ApiOperationException {
		// Prüfe, ob die Liste der Schüler-IDs existiert
		if (idsSchueler == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es sind keine Schueler-ID angegeben worden.");
		}
		if (idsSchueler.isEmpty()) {
			return new ArrayList<>();
		}

		if (validiereSchueler) {
			final Map<Long, DTOSchueler> mapSchueler = this.conn.queryByKeyList(DTOSchueler.class, idsSchueler)
					.stream().collect(Collectors.toMap(s -> s.ID, s -> s));
			for (final Long schuelerID : idsSchueler) {
				if (mapSchueler.get(schuelerID) == null) {
					throw new ApiOperationException(Status.NOT_FOUND, "Ein Schüler mit der ID %d existiert nicht.".formatted(schuelerID));
				}
			}
		}

		// Hole alle Lernabschnitte der übergebenen Schüler-IDs und filtere sie auf den Schuljahresabschnitt und die Wechsel-Nr.
		final List<DTOSchuelerLernabschnittsdaten> dtoLernabschnitte = this.conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_LIST_BY_SCHUELER_ID,
						DTOSchuelerLernabschnittsdaten.class, idsSchueler).stream()
				.sorted(Comparator
						.comparing((final DTOSchuelerLernabschnittsdaten a) -> a.Schueler_ID)
						.thenComparing((final DTOSchuelerLernabschnittsdaten a) -> a.Schuljahresabschnitts_ID)
						.thenComparing((final DTOSchuelerLernabschnittsdaten a) -> a.WechselNr))
				.toList();

		final List<SchuelerLernabschnittsdaten> daten = new ArrayList<>();
		for (final DTOSchuelerLernabschnittsdaten a : dtoLernabschnitte) {
			daten.add(mitLeistungsdaten ? map(a) : mapOhneLeistungsdaten(a));
		}
		return daten;
	}


	/**
	 * Für einen Patch für die angegebenen Bemerkungsfelder aus.
	 *
	 * @param id   die ID des Lernabschnitts
	 * @param is   ein Input-Stream mit den JSON-Daten des Patches
	 *
	 * @return die HTTP-Response für den Patch
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response patchBemerkungen(final Long id, final InputStream is) {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Ein Patch mit der ID null ist nicht möglich.");
		}
		final Map<String, Object> attributesToPatch = JSONMapper.toMap(is);
		if (attributesToPatch.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "In dem Patch sind keine Daten enthalten.");
		}
		final DTOSchuelerLernabschnittsdaten dto = this.conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND);
		}
		checkBeforePatch(dto, attributesToPatch);
		final List<DTOSchuelerPSFachBemerkungen> dtoListFachBem = this.conn.queryList(
				DTOSchuelerPSFachBemerkungen.QUERY_BY_ABSCHNITT_ID, DTOSchuelerPSFachBemerkungen.class, id);
		final DTOSchuelerPSFachBemerkungen dtoFachBem = (dtoListFachBem.isEmpty())
				? new DTOSchuelerPSFachBemerkungen(this.conn.transactionGetNextID(DTOSchuelerPSFachBemerkungen.class), id)
				: dtoListFachBem.getFirst();
		boolean patchedDTOLernabschitt = false;
		boolean patchedDTOFachBem = false;
		for (final Entry<String, Object> entry : attributesToPatch.entrySet()) {
			final String key = entry.getKey();
			final Object value = entry.getValue();
			switch (key) {
				case "zeugnisAllgemein" -> {
					dto.ZeugnisBem = JSONMapper.convertToString(value, true, true, null);
					patchedDTOLernabschitt = true;
				}
				case "zeugnisASV" -> {
					dtoFachBem.ASV = JSONMapper.convertToString(value, true, true, null);
					patchedDTOFachBem = true;
				}
				case "zeugnisLELS" -> {
					dtoFachBem.LELS = JSONMapper.convertToString(value, true, true, null);
					patchedDTOFachBem = true;
				}
				case "zeugnisAUE" -> {
					dtoFachBem.AUE = JSONMapper.convertToString(value, true, true, null);
					patchedDTOFachBem = true;
				}
				case "uebergangESF" -> {
					dtoFachBem.ESF = JSONMapper.convertToString(value, true, true, null);
					patchedDTOFachBem = true;
				}
				case "foerderschwerpunkt" -> {
					dtoFachBem.BemerkungFSP = JSONMapper.convertToString(value, true, true, null);
					patchedDTOFachBem = true;
				}
				case "versetzungsentscheidung" -> {
					dtoFachBem.BemerkungVersetzung = JSONMapper.convertToString(value, true, true, null);
					patchedDTOFachBem = true;
				}
				default -> {
					/**/
				}
			}
		}
		if (patchedDTOLernabschitt) {
			this.conn.transactionPersist(dto);
			this.conn.transactionFlush();
		}
		if (patchedDTOFachBem) {
			this.conn.transactionPersist(dtoFachBem);
			this.conn.transactionFlush();
		}
		return Response.status(Status.OK).build();
	}


	/**
	 * Hilfsmethode zum Überführen der reinen Daten des DTO eines Schülerlernabschnitts in ein entsprechendes Core-Data-Objekt.
	 *
	 * @param dto das DTO mit den SchuelerLernabschnittsdaten.
	 *
	 * @return    ein Core-Data-Objekt mit den reinen SchuelerLernabschnittsdaten.
	 */
	private SchuelerLernabschnittsdaten mapInternalDTOSchuelerLernabschnittsdaten(final DTOSchuelerLernabschnittsdaten dto) {
		final Schulform schulform = this.conn.getUser().schuleGetSchulform();
		final Schuljahresabschnitt abschnitt = this.conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dto.Schuljahresabschnitts_ID);
		final SchuelerLernabschnittsdaten daten = new SchuelerLernabschnittsdaten();
		daten.id = dto.ID;
		daten.schuelerID = dto.Schueler_ID;
		daten.schuljahresabschnitt = dto.Schuljahresabschnitts_ID;
		daten.wechselNr = dto.WechselNr;
		daten.datumAnfang = dto.DatumVon;
		daten.datumEnde = dto.DatumBis;
		daten.datumKonferenz = dto.Konferenzdatum;
		daten.datumZeugnis = dto.ZeugnisDatum;
		daten.istGewertet = (dto.SemesterWertung == null) || dto.SemesterWertung;
		daten.istWiederholung = (dto.Wiederholung != null) && dto.Wiederholung;
		daten.pruefungsOrdnung = dto.PruefOrdnung;
		daten.tutorID = dto.Tutor_ID;
		daten.klassenID = dto.Klassen_ID;
		daten.folgeklassenID = dto.Folgeklasse_ID;
		// TODO Validierung der Schulgliederung überprüfen...
		daten.idSchulgliederung = getIdSchulgliederung(dto, schulform, abschnitt);
		daten.jahrgangID = dto.Jahrgang_ID;
		daten.idEpJahre = getIdEpJahre(dto);
		daten.fachklasseID = dto.Fachklasse_ID;
		daten.schwerpunktID = dto.Schwerpunkt_ID;
		//TODO Nachfragen, wegen Schulformen und doppelten hin und her wandeln, hier und in mapAttribute
		daten.idOrganisationsform = getIdOrganisationsform(dto, schulform, abschnitt);
		daten.idKlassenart = getIdKlassenart(dto, abschnitt);
		daten.fehlstundenGesamt = (dto.SumFehlStd == null) ? 0 : dto.SumFehlStd;
		daten.fehlstundenUnentschuldigt = (dto.SumFehlStdU == null) ? 0 : dto.SumFehlStdU;
		daten.fehlstundenGrenzwert = dto.FehlstundenGrenzwert;
		daten.hatSchwerbehinderungsNachweis = (dto.Schwerbehinderung != null) && dto.Schwerbehinderung;
		daten.hatAOSF = (dto.AOSF != null) && dto.AOSF;
		daten.hatAutismus = (dto.Autist != null) && dto.Autist;
		daten.hatZieldifferentenUnterricht = (dto.ZieldifferentesLernen != null) && dto.ZieldifferentesLernen;
		daten.foerderschwerpunkt1ID = dto.Foerderschwerpunkt_ID;
		daten.foerderschwerpunkt2ID = dto.Foerderschwerpunkt2_ID;
		daten.sonderpaedagogeID = dto.Sonderpaedagoge_ID;
		daten.bilingualerZweig = dto.BilingualerZweig;
		daten.istFachpraktischerAnteilAusreichend = dto.FachPraktAnteilAusr;
		daten.idVersetzungsvermerk = (Versetzungsvermerk.data().getWertByKuerzel(dto.VersetzungKrz) == null) ? -1L : Versetzungsvermerk.data().getWertByKuerzel(dto.VersetzungKrz).id(abschnitt.schuljahr);
		daten.noteDurchschnitt = dto.DSNote;
		final Note noteLernbereichGSbzwAL = (dto.Gesamtnote_GS == null) ? null : Note.fromNoteSekI(dto.Gesamtnote_GS);
		daten.noteLernbereichGSbzwAL = (noteLernbereichGSbzwAL == null) ? null : dto.Gesamtnote_GS;
		final Note noteLernbereichNW = (dto.Gesamtnote_NW == null) ? null : Note.fromNoteSekI(dto.Gesamtnote_NW);
		daten.noteLernbereichNW = (noteLernbereichNW == null) ? null : dto.Gesamtnote_NW;
		daten.abschlussart = dto.AbschlussArt;
		daten.istAbschlussPrognose = (dto.AbschlIstPrognose != null) && dto.AbschlIstPrognose;
		daten.abschluss = dto.Abschluss;
		daten.abschlussBerufsbildend = dto.Abschluss_B;
		daten.textErgebnisPruefungsalgorithmus = dto.PruefAlgoErgebnis;
		daten.zeugnisart = dto.Zeugnisart;
		if (dto.MoeglNPFaecher != null) {
			final String[] moeglicheNPFaecher = dto.MoeglNPFaecher.split(",");
			if ((moeglicheNPFaecher.length > 0) && (!moeglicheNPFaecher[0].trim().isBlank())) {
				daten.nachpruefungen = new SchuelerLernabschnittNachpruefungsdaten();
				Collections.addAll(daten.nachpruefungen.moegliche, moeglicheNPFaecher);
				if (dto.NPV_Fach_ID != null) {
					final SchuelerLernabschnittNachpruefung np = new SchuelerLernabschnittNachpruefung();
					np.grund = "V";
					np.fachID = dto.NPV_Fach_ID;
					np.datum = dto.NPV_Datum;
					np.note = dto.NPV_NoteKrz;
					daten.nachpruefungen.pruefungen.add(np);
				}
				if (dto.NPAA_Fach_ID != null) {
					final SchuelerLernabschnittNachpruefung np = new SchuelerLernabschnittNachpruefung();
					np.grund = "A";
					np.fachID = dto.NPAA_Fach_ID;
					np.datum = dto.NPAA_Datum;
					np.note = dto.NPAA_NoteKrz;
					daten.nachpruefungen.pruefungen.add(np);
				}
				if (dto.NPBQ_Fach_ID != null) {
					final SchuelerLernabschnittNachpruefung np = new SchuelerLernabschnittNachpruefung();
					np.grund = "B";
					np.fachID = dto.NPBQ_Fach_ID;
					np.datum = dto.NPBQ_Datum;
					np.note = dto.NPBQ_NoteKrz;
					daten.nachpruefungen.pruefungen.add(np);
				}
			}
		}
		daten.bemerkungen.zeugnisAllgemein = dto.ZeugnisBem;
		return daten;
	}

	private static Long getIdEpJahre(final DTOSchuelerLernabschnittsdaten dto) {
		return Optional.ofNullable(dto.EPJahre)
				.map(Integer::longValue)
				.orElse(null);
	}

	private static Long getIdKlassenart(final DTOSchuelerLernabschnittsdaten dto, final Schuljahresabschnitt abschnitt) {
		final Klassenart klassenart = Klassenart.data().getWertByKuerzel(dto.Klassenart);
		if (klassenart == null) {
			return null;
		}

		return klassenart.daten(abschnitt.schuljahr).id;
	}

	private Long getIdSchulgliederung(final DTOSchuelerLernabschnittsdaten dto, final Schulform schulform, final Schuljahresabschnitt abschnitt) {
		if (dto.Schulgliederung == null) {
			return Optional.ofNullable(Schulgliederung.getDefault(schulform))
					.map(e -> e.daten(abschnitt.schuljahr).id)
					.orElse(null);
		}

		final Schulgliederung schulgliederung = Schulgliederung.data().getWertByKuerzel(dto.Schulgliederung);
		if (schulgliederung == null) {
			return Optional.ofNullable(Schulgliederung.getDefault(schulform))
					.map(e -> e.daten(abschnitt.schuljahr).id)
					.orElse(null);
		}

		final SchulgliederungKatalogEintrag schulgliederungEintrag = schulgliederung.daten(abschnitt.schuljahr);
		if (schulgliederungEintrag == null) {
			return Optional.ofNullable(Schulgliederung.getDefault(schulform))
					.map(e -> e.daten(abschnitt.schuljahr).id)
					.orElse(null);
		}

		return schulgliederungEintrag.id;
	}

	private Long getIdOrganisationsform(final DTOSchuelerLernabschnittsdaten dto, final Schulform schulform, final Schuljahresabschnitt abschnitt) {
		if (Schulform.WB == schulform) {
			return Optional.ofNullable(WeiterbildungskollegOrganisationsformen.data().getWertByKuerzel(dto.OrgFormKrz))
					.map(e -> e.daten(abschnitt.schuljahr).id)
					.orElse(null);
		} else if ((Schulform.BK == schulform) || (Schulform.SB == schulform)) {
			return Optional.ofNullable(BerufskollegOrganisationsformen.data().getWertByKuerzel(dto.OrgFormKrz))
					.map(e -> e.daten(abschnitt.schuljahr).id)
					.orElse(null);
		} else {
			return Optional.ofNullable(AllgemeinbildendOrganisationsformen.data().getWertByKuerzel(dto.OrgFormKrz))
					.map(e -> e.daten(abschnitt.schuljahr).id)
					.orElse(null);
		}
	}


	@Override
	protected void mapAttribute(final DTOSchuelerLernabschnittsdaten dto, final String name, final Object value, final Map<String, Object> map) {
		switch (name) {
			case "id" -> updateID(dto, value);
			case "schuelerID" -> updateSchuelerID(dto, value);
			case "schuljahresabschnitt" -> updateSchuljahresabschnitt(dto, value);
			case "wechselNr" -> dto.WechselNr = JSONMapper.convertToIntegerInRange(value, true, 0, 1000);
			case DATUM_ANFANG -> updateDatumVon(dto, value);
			case DATUM_ENDE -> updateDatumBis(dto, value);
			case DATUM_KONFERENZ -> updateDatumKonferenz(dto, value);
			case DATUM_ZEUGNIS -> updateDatumZeugnis(dto, value);
			case "istGewertet" -> dto.SemesterWertung = JSONMapper.convertToBoolean(value, false);
			case "istWiederholung" -> dto.Wiederholung = JSONMapper.convertToBoolean(value, false);
			// TODO Prüfungsordnung anhand des Schild3-Katalogs validieren
			case "pruefungsOrdnung" -> dto.PruefOrdnung = JSONMapper.convertToString(value, true, false, null);
			case "tutorID" -> updateTutorID(dto, value);
			case KLASSEN_ID -> updateIdKlasse(value, dto, map);
			case JAHRGANG_ID -> updateIdJahrgang(value, dto, map);
			case "folgeklassenID" -> updateFolgeklassenID(dto, value);
			case "idSchulgliederung" -> updateSchulgliederung(dto, value);
			case "idEpJahre" -> dto.EPJahre = JSONMapper.convertToIntegerInRange(value, true, 1, 4);
			case "fachklasseID" -> updateFachklasseID(dto, value);
			// TODO Validierung des Schwerpunktes
			case "schwerpunktID" -> dto.Schwerpunkt_ID = JSONMapper.convertToLong(value, true);
			case "idOrganisationsform" -> updateOrganisationsform(dto, value);
			case "idKlassenart" -> updateKlassenart(dto, value);
			case "fehlstundenGesamt" -> dto.SumFehlStd = JSONMapper.convertToIntegerInRange(value, true, 0, 100000);
			case "fehlstundenUnentschuldigt" -> dto.SumFehlStdU = JSONMapper.convertToIntegerInRange(value, true, 0, 100000);
			case "fehlstundenGrenzwert" -> dto.FehlstundenGrenzwert = JSONMapper.convertToIntegerInRange(value, true, 0, 100000);
			case "hatSchwerbehinderungsNachweis" -> dto.Schwerbehinderung = JSONMapper.convertToBoolean(value, false);
			case "hatAOSF" -> dto.AOSF = JSONMapper.convertToBoolean(value, false);
			case "hatAutismus" -> dto.Autist = JSONMapper.convertToBoolean(value, false);
			case "hatZieldifferentenUnterricht" -> dto.ZieldifferentesLernen = JSONMapper.convertToBoolean(value, false);
			case "foerderschwerpunkt1ID" -> updateFoerderschwerpunkt1ID(dto, value);
			case "foerderschwerpunkt2ID" -> updateFoerderschwerpunkt2ID(dto, value);
			case "sonderpaedagogeID" -> updateSonderpaedagogeID(dto, value);
			case "bilingualerZweig" -> updateBilingualerZweig(dto, value);
			case "istFachpraktischerAnteilAusreichend" -> dto.FachPraktAnteilAusr = JSONMapper.convertToBoolean(value, true);
			// TODO Prüfung des Versetzungsvermerks
			case "versetzungsvermerk" -> dto.VersetzungKrz = JSONMapper.convertToString(value, true, false, null);
			// TODO Prüfung der Durchschnittsnote
			case "noteDurchschnitt" -> dto.DSNote = JSONMapper.convertToString(value, true, false, null);
			case "noteLernbereichGSbzwAL" -> updateNoteLernbereichGSbzwAL(dto, value);
			case "noteLernbereichNW" -> updateNoteLernbereichNW(dto, value);
			// TODO Prüfung der Abschlussart
			case "abschlussart" -> dto.AbschlussArt = JSONMapper.convertToInteger(value, true);
			case "istAbschlussPrognose" -> dto.AbschlIstPrognose = JSONMapper.convertToBoolean(value, true);
			case "abschluss" -> updateAbschluss(dto, value);
			case "abschlussBerufsbildend" -> updateAbschlussBerufsbildend(dto, value);
			case "textErgebnisPruefungsalgorithmus" -> dto.PruefAlgoErgebnis = JSONMapper.convertToString(value, true, false, null);
			case "zeugnisart" -> dto.Zeugnisart = JSONMapper.convertToString(value, true, false, 5);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten ein unbekanntes Attribut.");
		}
	}

	private static void updateDatumVon(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final String datumVon = JSONMapper.convertToString(value, true, false, null);
		validateDatumsformat(datumVon, DATUM_ANFANG);

		dto.DatumVon = datumVon;
	}

	private static void updateDatumBis(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final String datumBis = JSONMapper.convertToString(value, true, false, null);
		validateDatumsformat(datumBis, DATUM_ENDE);

		dto.DatumBis = datumBis;
	}

	private static void updateDatumKonferenz(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final String datumKonferenz = JSONMapper.convertToString(value, true, false, null);
		validateDatumsformat(datumKonferenz, DATUM_KONFERENZ);

		dto.Konferenzdatum = datumKonferenz;
	}

	private static void updateDatumZeugnis(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final String datumZeugnis = JSONMapper.convertToString(value, true, false, null);
		validateDatumsformat(datumZeugnis, DATUM_ZEUGNIS);

		dto.ZeugnisDatum = datumZeugnis;
	}

	private static void validateDatumsformat(final String value, final String attributeName) {
		if (!DateUtils.isValidDate(value)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Datumsformat für das Attribut %s ist nicht zulässig.".formatted(attributeName));
		}
	}

	private static void updateAbschlussBerufsbildend(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final String str = JSONMapper.convertToString(value, true, false, null);
		if ((str != null) && (SchulabschlussBerufsbildend.data().getWertByKuerzel(str) == null)) {
			throw new ApiOperationException(Status.CONFLICT);
		}
		dto.Abschluss_B = str;
	}

	private static void updateAbschluss(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final String str = JSONMapper.convertToString(value, true, false, null);
		if ((str != null) && (SchulabschlussAllgemeinbildend.data().getWertByKuerzel(str) == null)) {
			throw new ApiOperationException(Status.CONFLICT);
		}
		dto.Abschluss = str;
	}

	private static void updateNoteLernbereichNW(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final Integer noteSekI = JSONMapper.convertToIntegerInRange(value, true, 1, 6);
		if ((noteSekI != null) && (Note.fromNoteSekI(noteSekI) == null)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Notenwert für die Lernbereichsnote ist ungültig");
		}
		dto.Gesamtnote_NW = noteSekI;
	}

	private static void updateNoteLernbereichGSbzwAL(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final Integer noteSekI = JSONMapper.convertToIntegerInRange(value, true, 1, 6);
		if ((noteSekI != null) && (Note.fromNoteSekI(noteSekI) == null)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Notenwert für die Lernbereichsnote ist ungültig");
		}
		dto.Gesamtnote_GS = noteSekI;
	}

	private static void updateBilingualerZweig(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final String spracheKuerzel = JSONMapper.convertToString(value, true, false, null);
		if (spracheKuerzel == null) {
			dto.BilingualerZweig = null;
			return;
		}

		final BilingualeSprache bilingualeSprache = BilingualeSprache.data().getWertByKuerzel(spracheKuerzel);
		if (bilingualeSprache == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es existiert keine BilingualeSprache mit dem Kürzel %s.".formatted(spracheKuerzel));
		}

		dto.BilingualerZweig = spracheKuerzel;
	}

	private void updateSonderpaedagogeID(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final Long idLehrer = JSONMapper.convertToLong(value, true);
		if (idLehrer == null) {
			dto.Sonderpaedagoge_ID = null;
			return;
		}

		final DTOLehrer lehrer = this.conn.queryByKey(DTOLehrer.class, idLehrer);
		if (lehrer == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es existiert kein Lehrer mit der ID %d.".formatted(idLehrer));
		}

		dto.Sonderpaedagoge_ID = idLehrer;
	}

	private void updateFoerderschwerpunkt1ID(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final Long idFoerderschwerpunkt = JSONMapper.convertToLong(value, true);
		if (idFoerderschwerpunkt == null) {
			dto.Foerderschwerpunkt_ID = null;
			return;
		}

		final DTOFoerderschwerpunkt foerderschwerpunkt = this.conn.queryByKey(DTOFoerderschwerpunkt.class, idFoerderschwerpunkt);
		if (foerderschwerpunkt == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es existiert kein Förderschwerpunkt mit der ID %d.".formatted(idFoerderschwerpunkt));
		}

		dto.Foerderschwerpunkt_ID = idFoerderschwerpunkt;
	}

	private void updateFoerderschwerpunkt2ID(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final Long idFoerderschwerpunkt = JSONMapper.convertToLong(value, true);
		if (idFoerderschwerpunkt == null) {
			dto.Foerderschwerpunkt_ID = null;
			return;
		}

		final DTOFoerderschwerpunkt foerderschwerpunkt = this.conn.queryByKey(DTOFoerderschwerpunkt.class, idFoerderschwerpunkt);
		if (foerderschwerpunkt == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es existiert kein Förderschwerpunkt mit der ID %d.".formatted(idFoerderschwerpunkt));
		}

		dto.Foerderschwerpunkt2_ID = idFoerderschwerpunkt;
	}

	private void updateKlassenart(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final Long idKlassenart = JSONMapper.convertToLong(value, true, "idKlassenart");
		if (idKlassenart == null) {
			dto.Klassenart = null;
			return;
		}

		final Klassenart klassenart = Klassenart.data().getWertByIDOrNull(idKlassenart);
		if (klassenart == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es existiert kein Klassenart CoreType Wert für die ID %d.".formatted(idKlassenart));
		}

		final int schuljahr = this.conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dto.Schuljahresabschnitts_ID).schuljahr;
		final KlassenartKatalogEintrag klassenartEintrag = klassenart.daten(schuljahr);
		if (klassenartEintrag == null) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es existiert keine gültige Klassenart mit der ID %d im Schuljahr %d.".formatted(idKlassenart, schuljahr));
		}

		dto.Klassenart = klassenartEintrag.kuerzel;
	}

	private void updateOrganisationsform(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final Long idOrganisationsform = JSONMapper.convertToLong(value, true, "idOrganisationsform");
		if (idOrganisationsform == null) {
			dto.OrgFormKrz = null;
			return;
		}

		final Schulform schulform = this.conn.getUser().schuleGetSchulform();

		final String organisationsformKuerzel;
		if ((schulform == Schulform.WB)) {
			organisationsformKuerzel = getOrganisationsformKuerzel(dto, WeiterbildungskollegOrganisationsformen.data(), idOrganisationsform);
		} else if ((schulform == Schulform.BK) || (schulform == Schulform.SB)) {
			organisationsformKuerzel = getOrganisationsformKuerzel(dto, BerufskollegOrganisationsformen.data(), idOrganisationsform);
		} else {
			organisationsformKuerzel = getOrganisationsformKuerzel(dto, AllgemeinbildendOrganisationsformen.data(), idOrganisationsform);
		}

		if (organisationsformKuerzel == null) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es existiert keine Weiterbildungskolleg, Berufskolleg oder allgemeinbildende Organisationsform CoreType Wert für die ID %d."
							.formatted(idOrganisationsform));
		}

		dto.OrgFormKrz = organisationsformKuerzel;
	}

	private String getOrganisationsformKuerzel(final DTOSchuelerLernabschnittsdaten dto,
			final CoreTypeDataManager<OrganisationsformKatalogEintrag, ?> orgformCoreTypeManager, final Long idOrganisationsform) {
		final CoreType<OrganisationsformKatalogEintrag, ?> organisationsform = orgformCoreTypeManager.getWertByIDOrNull(idOrganisationsform);
		if (organisationsform == null) {
			return null;
		}

		final int schuljahr = this.conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dto.Schuljahresabschnitts_ID).schuljahr;
		final OrganisationsformKatalogEintrag organisationsformEintrag = organisationsform.daten(schuljahr);
		if (organisationsformEintrag == null) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es existiert keine gültige Organisationsform mit der ID %d im Schuljahr %d."
							.formatted(idOrganisationsform, schuljahr));
		}

		return organisationsformEintrag.kuerzel;
	}

	private static void updateFachklasseID(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final Long idFachklasse = JSONMapper.convertToLong(value, true);
		if (idFachklasse == null) {
			dto.Fachklasse_ID = null;
			return;
		}

		final FachklasseKatalogEintrag fachklasse = Fachklasse.data().getEintragByID(idFachklasse);
		if (fachklasse == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es existiert keine Fachklasse mit der ID %d".formatted(idFachklasse));
		}

		dto.Fachklasse_ID = idFachklasse;
	}

	private void updateSchulgliederung(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final Long idSchulgliederung = JSONMapper.convertToLong(value, true, "idSchulgliederung");
		if (idSchulgliederung == null) {
			dto.Schulgliederung = null;
			return;
		}

		final Schulgliederung schulgliederung = Schulgliederung.data().getWertByIDOrNull(idSchulgliederung);
		if (schulgliederung == null) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es existiert kein Schulgliederung CoreType Wert für die ID %d.".formatted(idSchulgliederung));
		}

		final int schuljahr = this.conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dto.Schuljahresabschnitts_ID).schuljahr;
		final SchulgliederungKatalogEintrag schulgliederungEintrag = schulgliederung.daten(schuljahr);
		if (schulgliederungEintrag == null) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es existiert keine gültige Schulgliederung mit der ID %d im Schuljahr %d.".formatted(idSchulgliederung, schuljahr));
		}

		dto.Schulgliederung = schulgliederungEintrag.kuerzel;
	}

	private void updateFolgeklassenID(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final Long idKlasse = JSONMapper.convertToLong(value, true);
		if (idKlasse == null) {
			dto.Folgeklasse_ID = null;
			return;
		}

		final DTOKlassen klasse = this.conn.queryByKey(DTOKlassen.class, idKlasse);
		if (klasse == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es existiert keine Klasse mit der ID %d.".formatted(idKlasse));
		}

		dto.Folgeklasse_ID = idKlasse;
	}

	private void updateTutorID(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final Long idLehrer = JSONMapper.convertToLong(value, true);
		if (idLehrer == null) {
			dto.Tutor_ID = null;
			return;
		}

		final DTOLehrer lehrer = this.conn.queryByKey(DTOLehrer.class, idLehrer);
		if (lehrer == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es existiert kein Lehrer mit der ID %d.".formatted(idLehrer));
		}

		dto.Tutor_ID = idLehrer;
	}

	private static void updateID(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final Long id = JSONMapper.convertToLong(value, true);
		if ((id == null) || (id != dto.ID)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID des Lernabschnitts darf nicht verändert werden.");
		}
	}

	private void updateSchuljahresabschnitt(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final long idSchuljahresabschnitt = JSONMapper.convertToLong(value, false);

		final DTOSchuljahresabschnitte schuljahresabschnitt = this.conn.queryByKey(DTOSchuljahresabschnitte.class, idSchuljahresabschnitt);
		if (schuljahresabschnitt == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es existiert kein Schuljahresabschnitt mit der ID %d.".formatted(idSchuljahresabschnitt));
		}

		dto.Schuljahresabschnitts_ID = idSchuljahresabschnitt;
	}

	private void updateSchuelerID(final DTOSchuelerLernabschnittsdaten dto, final Object value) {
		final long idSchueler = JSONMapper.convertToLong(value, false);

		final DTOSchueler schueler = this.conn.queryByKey(DTOSchueler.class, idSchueler);
		if (schueler == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es existiert kein Schüler mit der ID %d.".formatted(idSchueler));
		}

		dto.Schueler_ID = idSchueler;
	}

	private void updateIdKlasse(final Object value, final DTOSchuelerLernabschnittsdaten dto, final Map<String, Object> patchAttributes) {
		final Long idKlasse = JSONMapper.convertToLong(value, true, KLASSEN_ID);
		if (idKlasse == null) {
			dto.Klassen_ID = null;
			return;
		}

		final DTOKlassen klasse = this.conn.queryByKey(DTOKlassen.class, idKlasse);
		if (klasse == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es existiert keine Klasse mit der ID %d.".formatted(idKlasse));
		}

		if (klasse.Schuljahresabschnitts_ID != dto.Schuljahresabschnitts_ID) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Klasse gehört nicht zum angegebenen Schuljahresabschnitt.");
		}

		final Long idJahrgang;
		final Object jahrgangPatch = patchAttributes.get(JAHRGANG_ID);
		if (jahrgangPatch != null) {
			idJahrgang = JSONMapper.convertToLong(jahrgangPatch, true, JAHRGANG_ID);
		} else {
			idJahrgang = dto.Jahrgang_ID;
		}

		final DTOJahrgang jahrgang = getJahrgangById(idJahrgang);
		pruefeKompatibilitaetKlasseUndJahrgang(klasse, jahrgang);

		checkFunktionsbezogeneKompetenzAufKlasse(List.of(idKlasse));

		dto.Klassen_ID = idKlasse;
	}

	private DTOJahrgang getJahrgangById(final Long idJahrgang) {
		final DTOJahrgang jahrgang = this.conn.queryByKey(DTOJahrgang.class, idJahrgang);
		if (jahrgang == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es existiert kein Jahrgang mit der ID %d.".formatted(idJahrgang));
		}
		return jahrgang;
	}

	private void updateIdJahrgang(final Object value, final DTOSchuelerLernabschnittsdaten dto, final Map<String, Object> patchAttributes) {
		final Long idJahrgang = JSONMapper.convertToLong(value, true, JAHRGANG_ID);
		if (idJahrgang == null) {
			dto.Jahrgang_ID = null;
			dto.ASDJahrgang = null;
			return;
		}

		final DTOJahrgang jahrgang = getJahrgangById(idJahrgang);

		final Long idKlasse;
		final Object klassePatch = patchAttributes.get(KLASSEN_ID);
		if (klassePatch != null) {
			idKlasse = JSONMapper.convertToLong(klassePatch, true, KLASSEN_ID);
		} else {
			idKlasse = dto.Klassen_ID;
		}

		if (idKlasse != null) {
			final DTOKlassen klasse = this.conn.queryByKey(DTOKlassen.class, idKlasse);
			if (klasse == null) {
				throw new ApiOperationException(Status.NOT_FOUND, "Es konnte keine zugehörige Klasse mit der ID %d gefunden werden.".formatted(dto.Klassen_ID));
			}

			pruefeKompatibilitaetKlasseUndJahrgang(klasse, jahrgang);
		}

		dto.Jahrgang_ID = idJahrgang;
		dto.ASDJahrgang = jahrgang.ASDJahrgang;
	}

	private void pruefeKompatibilitaetKlasseUndJahrgang(final DTOKlassen klasse, final DTOJahrgang jahrgang) {
		if ((klasse == null) || (klasse.Jahrgang_ID == null) || (jahrgang == null)) {
			return;
		}

		// Bei jahrgangsübergreifenden Klassen sind alle Jahrgänge zulässig
		if (istKlasseJahrgangsuebergreifend(klasse)) {
			return;
		}

		final DTOJahrgang klasseJahrgang = getJahrgangById(klasse.Jahrgang_ID);

		if (!Objects.equals(klasseJahrgang.ASDJahrgang, jahrgang.ASDJahrgang)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Der ausgewählte Jahrgang ist für die Klasse nicht zulässig.");
		}
	}

	private static boolean istKlasseJahrgangsuebergreifend(final DTOKlassen klasse) {
		return klasse.Jahrgang_ID == null;
	}

	private void checkFunktionsbezogeneKompetenzAufKlasse(final List<Long> idsKlassen) {
		if (hatBenutzerNurFunktionsbezogeneKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN,
				Set.of(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN))) {
			if (idsKlassen == null) {
				throw new ApiOperationException(Status.FORBIDDEN,
						"Der Benutzer kann keine funktionsbezogene Kompetenz nutzen, um auf Daten zuzugreifen, die keiner Klasse zugeordnet sind.");
			}
			for (final Long idKlasse : idsKlassen) {
				checkBenutzerFunktionsbezogeneKompetenzKlasse(idKlasse);
			}
		}
	}

	// TODO Patch für Nachprüfungen als getrennte Patch-Methode - SchuelerLernabschnittNachpruefungsdaten

}
