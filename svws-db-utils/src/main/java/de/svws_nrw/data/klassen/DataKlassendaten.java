package de.svws_nrw.data.klassen;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogDaten;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.klassen.Klassenart;
import de.svws_nrw.core.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.core.types.schule.BerufskollegOrganisationsformen;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.types.schule.Schulgliederung;
import de.svws_nrw.core.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.schueler.DataSchuelerliste;
import de.svws_nrw.data.schule.DataSchuljahresabschnitte;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.dto.current.schild.schule.DTOTeilstandorte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.json.JsonDaten;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KlassenDaten}.
 */
public final class DataKlassendaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KlassenDaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKlassendaten(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Mapper zum Erstellen des Core-DTOs aus den Datenbank-DTOs zu den Klassen, Klassenleitungen und Schülern.
	 *
	 * @param schulform                  die Schulform
	 * @param mapSchuljahresabschnitte   die Map mit den Schuljahresabschnitten der Schule
	 * @param klasse                     die Klasse
	 * @param klassenLeitungen           die Leitungen der Klasse
	 * @param schueler                   die Schüler der Klasse
	 * @param mapKlassenVorher           die Klassen im vorigen Schuljahresabschnitt, sofern in der DB vorhanden
	 * @param mapKlassenNachher          die Klassen im folgenden Schuljahresabschnitt, sofern in der DB bereits angelegt
	 *
	 * @return Core-DTO mit allen Klasseninformationen
	 */
	public static KlassenDaten mapDTO(final Schulform schulform, final @NotNull Map<@NotNull Long, @NotNull DTOSchuljahresabschnitte> mapSchuljahresabschnitte,
			final DTOKlassen klasse, final List<DTOKlassenLeitung> klassenLeitungen, final List<DTOSchueler> schueler,
			final Map<String, DTOKlassen> mapKlassenVorher, final Map<String, DTOKlassen> mapKlassenNachher) {
		final KlassenDaten daten = new KlassenDaten();
		daten.id = klasse.ID;
		daten.idSchuljahresabschnitt = klasse.Schuljahresabschnitts_ID;
		daten.kuerzel = klasse.Klasse;
		daten.idJahrgang = klasse.Jahrgang_ID;
		daten.parallelitaet = ((klasse.ASDKlasse == null) || (klasse.ASDKlasse.length() < 3))
				? null : klasse.ASDKlasse.substring(2);
		daten.sortierung = klasse.Sortierung;
		daten.istSichtbar = klasse.Sichtbar;
		if (klassenLeitungen != null)
			for (final DTOKlassenLeitung kl : klassenLeitungen)
				daten.klassenLeitungen.add(kl.Lehrer_ID);
		for (final DTOSchueler dto : schueler)
			if (Boolean.FALSE.equals(dto.Geloescht))
				daten.schueler.add(DataSchuelerliste.mapToSchueler.apply(dto));
		daten.teilstandort = (klasse.AdrMerkmal == null) ? "" : klasse.AdrMerkmal;
		daten.beschreibung = (klasse.Bezeichnung == null) ? "" : klasse.Bezeichnung;
		daten.idAllgemeinbildendOrganisationsform = (AllgemeinbildendOrganisationsformen.getByKuerzel(klasse.OrgFormKrz) == null)
				? null : AllgemeinbildendOrganisationsformen.getByKuerzel(klasse.OrgFormKrz).daten.id;
		daten.idBerufsbildendOrganisationsform = (BerufskollegOrganisationsformen.getByKuerzel(klasse.OrgFormKrz) == null)
				? null : BerufskollegOrganisationsformen.getByKuerzel(klasse.OrgFormKrz).daten.id;
		daten.idWeiterbildungOrganisationsform = (WeiterbildungskollegOrganisationsformen.getByKuerzel(klasse.OrgFormKrz) == null)
				? null : WeiterbildungskollegOrganisationsformen.getByKuerzel(klasse.OrgFormKrz).daten.id;
		daten.pruefungsordnung = klasse.PruefOrdnung;
		Schulgliederung gliederung = Schulgliederung.getBySchulformAndKuerzel(schulform, klasse.ASDSchulformNr);
		if (gliederung == null)
			gliederung = Schulgliederung.getDefault(schulform);
		daten.idSchulgliederung = (gliederung == null) ? -1 : gliederung.daten.id;
		final Klassenart klassenart = Klassenart.getByKuerzel(klasse.Klassenart);
		daten.idKlassenart = ((klassenart != null) && (klassenart.hasSchulform(schulform))) ? klassenart.daten.id : Klassenart.UNDEFINIERT.daten.id;
		daten.noteneingabeGesperrt = (klasse.NotenGesperrt != null) && klasse.NotenGesperrt;
		daten.verwendungAnkreuzkompetenzen = (klasse.Ankreuzzeugnisse != null) && klasse.Ankreuzzeugnisse;
		daten.kuerzelVorgaengerklasse = klasse.VKlasse;
		daten.kuerzelFolgeklasse = klasse.FKlasse;
		// Bestimme die IDs der Vorgänger- und der Nachfolge-Klassen dieser Klasse, sofern möglich und berücksichtige dabei den Semesterbetrieb i, Weiterbildungskolleg
		final @NotNull DTOSchuljahresabschnitte schuljahresabschnitt = mapSchuljahresabschnitte.get(klasse.Schuljahresabschnitts_ID);
		if ((!mapKlassenVorher.isEmpty()) && (daten.kuerzelVorgaengerklasse != null)) {
			DTOKlassen klasseVorher = null;
			if ((schulform != Schulform.WB) && (schuljahresabschnitt.Abschnitt == 2))
				klasseVorher = mapKlassenVorher.get(daten.kuerzel);
			else
				klasseVorher = mapKlassenVorher.get(daten.kuerzelVorgaengerklasse);
			daten.idVorgaengerklasse = (klasseVorher == null) ? null : klasseVorher.ID;
		} else
			daten.idVorgaengerklasse = null;
		if ((!mapKlassenNachher.isEmpty()) && (daten.kuerzelFolgeklasse != null)) {
			DTOKlassen klasseNachher = null;
			if ((schulform != Schulform.WB) && (schuljahresabschnitt.Abschnitt == 1))
				klasseNachher = mapKlassenNachher.get(daten.kuerzel);
			else
				klasseNachher = mapKlassenNachher.get(daten.kuerzelFolgeklasse);
			daten.idVorgaengerklasse = (klasseNachher == null) ? null : klasseNachher.ID;
		} else
			daten.idFolgeklasse = null;
		daten.idFachklasse = klasse.Fachklasse_ID;
		daten.beginnSommersemester = (klasse.SommerSem != null) && klasse.SommerSem;
		return daten;
	}

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		final KlassenDaten daten = getFromID(id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private KlassenDaten getFromIDInternal(final Long id, final List<DTOSchueler> dtoSchueler) throws ApiOperationException {
		final DTOKlassen klasse = conn.queryByKey(DTOKlassen.class, id);
		if (klasse == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Klasse zur ID " + id + " gefunden.");
		return getFromDTOInternal(klasse, dtoSchueler);
	}


	private KlassenDaten getFromDTOInternal(final DTOKlassen klasse, final List<DTOSchueler> dtoSchueler) throws ApiOperationException {
		// Bestimme die Informationen zur Schule und zu den Schuljahresabschnitten
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Konnte die Informationen zur Schule nicht einlesen");
		final @NotNull Map<@NotNull Long, @NotNull DTOSchuljahresabschnitte> mapSchuljahresabschnitte = DataSchuljahresabschnitte.getDTOMap(conn);
		final DTOSchuljahresabschnitte schuljahresabschnitt = mapSchuljahresabschnitte.get(klasse.Schuljahresabschnitts_ID);
		if (schuljahresabschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Der Schuljahresabschnitt mit der ID %d für die Klasse mit der ID %d wurde nicht gefunden."
					.formatted(klasse.Schuljahresabschnitts_ID, klasse.ID));
		final List<DTOKlassenLeitung> klassenLeitungen = conn.queryList(DTOKlassenLeitung.QUERY_BY_KLASSEN_ID, DTOKlassenLeitung.class, klasse.ID);
		// Bestimme alle Klassen-DTOs der klassen aus dem vorigen und nachfolgenden Schuljahresabschnitt
		final Map<String, DTOKlassen> klassenVorher = (schuljahresabschnitt.VorigerAbschnitt_ID == null)
				? new HashMap<>()
				: conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, schuljahresabschnitt.VorigerAbschnitt_ID)
						.stream().collect(Collectors.toMap(k -> k.Klasse, k -> k));
		// Bestimme alle Klassen-DTOs der klassen aus dem vorigen und nachfolgenden Schuljahresabschnitt
		final Map<String, DTOKlassen> klassenNachher = (schuljahresabschnitt.FolgeAbschnitt_ID == null)
				? new HashMap<>()
				: conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, schuljahresabschnitt.FolgeAbschnitt_ID)
						.stream().collect(Collectors.toMap(k -> k.Klasse, k -> k));
		// Erstelle das Core-DTO-Objekt für die Klasse
		return mapDTO(schule.Schulform, mapSchuljahresabschnitte, klasse, klassenLeitungen, dtoSchueler, klassenVorher, klassenNachher);
	}


	/**
	 * Gibt die Daten einer Klasse zu deren ID zurück.
	 *
	 * @param id   Die ID der Klasse.
	 *
	 * @return die Daten der Klasse zur ID.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public KlassenDaten getFromID(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine ID für die Klasse übergeben.");
		// Bestimme die Schüler der Klasse
		final List<Long> schuelerIDs = getSchuelerIDsByKlassenID(id);
		final List<DTOSchueler> dtoSchueler = schuelerIDs.isEmpty() ? new ArrayList<>()
				: conn.queryByKeyList(DTOSchueler.class, schuelerIDs);
		// Erstelle das Core-DTO-Objekt für die Klasse
		return getFromIDInternal(id, dtoSchueler);
	}


	/**
	 * Bestimmt die IDs der Schüler, welche zu der übergebenen ID der Klasse gehören.
	 *
	 * @param id   die ID der Klasse
	 *
	 * @return die List von Schüler IDs, welche der Klasse zugeordnet sind
	 */
	public List<Long> getSchuelerIDsByKlassenID(final Long id) {
		return conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_KLASSEN_ID, DTOSchuelerLernabschnittsdaten.class, id)
				.stream().filter(sla -> sla.WechselNr == 0).map(sla -> sla.Schueler_ID).toList();
	}


	/**
	 * Gibt die Daten einer Klasse zu deren ID ohne Schülerliste zurück.
	 *
	 * @param id   Die ID der Klasse.
	 *
	 * @return die Daten der KLasse zur ID ohne Schülerliste.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public KlassenDaten getFromIDOhneSchueler(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine ID für die Klasse übergeben.");
		// Erstelle das Core-DTO-Objekt für die Klasse
		return getFromIDInternal(id, Collections.emptyList());
	}


	/**
	 * Gibt eine Liste von Klassen zu einer SchuljahresabschnittsID zurück.
	 *
	 * @param id   Die ID des Schuljahresabschnitts.
	 *
	 * @return die Daten der Klasse zum Schuljahresabschnitt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<KlassenDaten> getFromSchuljahresabschnittsIDOhneSchueler(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine ID für den Schuljahresabschnitt übergeben.");
		final List<DTOKlassen> klassen = conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, id);
		if (klassen == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Klasse zur SchuljahresabschnittsID " + id + " gefunden.");
		final List<KlassenDaten> daten = new ArrayList<>();
		for (final DTOKlassen k : klassen)
			daten.add(getFromIDOhneSchueler(k.ID));
		return daten;
	}


	private static final Map<String, DataBasicMapper<DTOKlassen>> patchMappings = Map.ofEntries(
			Map.entry("id", (conn, dto, value, map) -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id.longValue() != dto.ID))
					throw new ApiOperationException(Status.BAD_REQUEST, "Die ID im Patch stimmt nicht mit der ID des Datenbank-Objektes überein");
			}),
			Map.entry("idSchuljahresabschnitt", (conn, dto, value, map) -> {
				throw new ApiOperationException(Status.BAD_REQUEST, "Die ID des Schuljahresabschnittes darf nicht nachträglich angepasst werden.");
			}),
			Map.entry("kuerzel", (conn, dto, value, map) -> {
				dto.Klasse = JSONMapper.convertToString(value, false, false, 6);
			}),
			Map.entry("idJahrgang", (conn, dto, value, map) -> {
				final Long idJahrgang = JSONMapper.convertToLong(value, true);
				if (idJahrgang == null) {
					// Jahrgangs-übergreifende Klasse -> JU
					dto.Jahrgang_ID = null;
					dto.ASDKlasse = "JU" + (((dto.ASDKlasse != null) && (dto.ASDKlasse.length() > 2)) ? dto.ASDKlasse.charAt(2) : "");
				} else {
					final DTOJahrgang jg = conn.queryByKey(DTOJahrgang.class, idJahrgang);
					if (jg == null)
						throw new ApiOperationException(Status.NOT_FOUND, "Der Jahrgang mit der ID %d konnte nicht gefunden werden.".formatted(idJahrgang));
					dto.Jahrgang_ID = jg.ID;
					String asdKlassenjahrgang = jg.ASDJahrgang;
					if ("E1".equals(jg.ASDJahrgang))
						asdKlassenjahrgang = "1E";
					else if ("E2".equals(jg.ASDJahrgang))
						asdKlassenjahrgang = "2E";
					dto.ASDKlasse = asdKlassenjahrgang + (((dto.ASDKlasse != null) && (dto.ASDKlasse.length() > 2)) ? dto.ASDKlasse.charAt(2) : "");
				}
			}),
			Map.entry("parallelitaet", (conn, dto, value, map) -> {
				final String parallelitaet = JSONMapper.convertToString(value, true, false, 1);
				if (parallelitaet == null) {
					dto.ASDKlasse = dto.ASDKlasse.substring(0, 2);
				} else {
					final char p = parallelitaet.charAt(0);
					if ((p < 'A') || (p > 'Z'))
						throw new ApiOperationException(Status.BAD_REQUEST,
								"Die Parallelität muss durch einen Buchstaben A-Z in Großschreibung angegeben werden.");
					dto.ASDKlasse = dto.ASDKlasse.substring(0, 2) + p;
				}
			}),
			Map.entry("sortierung", (conn, dto, value, map) -> dto.Sortierung = JSONMapper.convertToIntegerInRange(value, false, 0, Integer.MAX_VALUE)),
			Map.entry("istSichtbar", (conn, dto, value, map) -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false)),
			Map.entry("teilstandort", (conn, dto, value, map) -> {
				final String t = JSONMapper.convertToString(value, false, false, 1);
				final DTOTeilstandorte teilstandort = conn.queryByKey(DTOTeilstandorte.class, t);
				if (teilstandort == null)
					throw new ApiOperationException(Status.NOT_FOUND, "Der Teilstandort %s wurde nicht gefunden.".formatted(t));
				dto.AdrMerkmal = t;
			}),
			Map.entry("beschreibung", (conn, dto, value, map) -> dto.Bezeichnung = JSONMapper.convertToString(value, true, true, 151)),
			Map.entry("idVorgaengerklasse", (conn, dto, value, map) -> {
				final Long idVorgaengerklasse = JSONMapper.convertToLong(value, true);
				if (idVorgaengerklasse == null) {
					dto.VKlasse = null;
				} else {
					final DTOKlassen vk = conn.queryByKey(DTOKlassen.class, idVorgaengerklasse);
					if (vk == null)
						throw new ApiOperationException(Status.NOT_FOUND,
								"Die Vorgängerklasse mit der ID %d wurde nicht gefunden.".formatted(idVorgaengerklasse));
					final DTOSchuljahresabschnitte a = conn.queryByKey(DTOSchuljahresabschnitte.class, dto.Schuljahresabschnitts_ID);
					if (a == null)
						throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
								"Die ID des Schuljahresabschnitts %d der Klasse mit der ID %d ist ungültig.".formatted(dto.Schuljahresabschnitts_ID, dto.ID));
					if (vk.Schuljahresabschnitts_ID != a.VorigerAbschnitt_ID)
						throw new ApiOperationException(Status.BAD_REQUEST,
								"Die ID für die Vorgängerklasse gehört nicht zu einer Klasse aus dem vorigen Schuljahresabschnitt.");
					dto.VKlasse = vk.Klasse;
				}
			}),
			Map.entry("kuerzelVorgaengerklasse", (conn, dto, value, map) -> {
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Das Kürzel für die Vorgängerklasse kann nur indirekt über die ID für die Vorgängerklasse angepasst werden.");
			}),
			Map.entry("idFolgeklasse", (conn, dto, value, map) -> {
				final Long idFolgeklasse = JSONMapper.convertToLong(value, true);
				if (idFolgeklasse == null) {
					dto.FKlasse = null;
				} else {
					final DTOKlassen fk = conn.queryByKey(DTOKlassen.class, idFolgeklasse);
					if (fk == null)
						throw new ApiOperationException(Status.NOT_FOUND, "Die Folgeklasse mit der ID %d wurde nicht gefunden.".formatted(idFolgeklasse));
					final DTOSchuljahresabschnitte a = conn.queryByKey(DTOSchuljahresabschnitte.class, dto.Schuljahresabschnitts_ID);
					if (a == null)
						throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
								"Die ID des Schuljahresabschnitts %d der Klasse mit der ID %d ist ungültig.".formatted(dto.Schuljahresabschnitts_ID, dto.ID));
					if (fk.Schuljahresabschnitts_ID != a.FolgeAbschnitt_ID)
						throw new ApiOperationException(Status.BAD_REQUEST,
								"Die ID für die Folgeklasse gehört nicht zu einer Klasse aus dem nachfolgenden Schuljahresabschnitt.");
					dto.FKlasse = fk.Klasse;
				}
			}),
			Map.entry("kuerzelFolgeklasse", (conn, dto, value, map) -> {
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Das Kürzel für die Folgeklasse kann nur indirekt über die ID für die Folgeklasse angepasst werden.");
			}),
			Map.entry("idAllgemeinbildendOrganisationsform", (conn, dto, value, map) -> {
				final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
				if (!schule.Schulform.daten.istAllgemeinbildend)
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Der Wert kann nicht gesetzt werden, da die Schule keine allgemeinbildende Schulform hat.");
				final Long idOrgform = JSONMapper.convertToLong(value, true);
				AllgemeinbildendOrganisationsformen orgform = AllgemeinbildendOrganisationsformen.getByID(idOrgform);
				if (idOrgform == null)
					orgform = AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET;
				if (orgform == null)
					throw new ApiOperationException(Status.BAD_REQUEST, "Die ID %d für die allgemeinene Organisationform ist ungültig");
				dto.OrgFormKrz = orgform.daten.kuerzel;
			}),
			Map.entry("idBerufsbildendOrganisationsform", (conn, dto, value, map) -> {
				final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
				if (!schule.Schulform.daten.istBerufsbildend)
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Der Wert kann nicht gesetzt werden, da die Schule keine berufsbildende Schulform hat.");
				final Long idOrgform = JSONMapper.convertToLong(value, true);
				if (idOrgform == null) {
					dto.OrgFormKrz = AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET.daten.kuerzel;
				} else {
					final BerufskollegOrganisationsformen orgform = BerufskollegOrganisationsformen.getByID(idOrgform);
					if (orgform == null)
						throw new ApiOperationException(Status.BAD_REQUEST, "Die ID %d für die allgemeinene Organisationform ist ungültig");
					dto.OrgFormKrz = orgform.daten.kuerzel;
				}
			}),
			Map.entry("idWeiterbildungOrganisationsform", (conn, dto, value, map) -> {
				final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
				if (!schule.Schulform.daten.istWeiterbildung)
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Der Wert kann nicht gesetzt werden, da die Schule keine Schulform für die Weiterbildung hat.");
				final Long idOrgform = JSONMapper.convertToLong(value, true);
				if (idOrgform == null) {
					dto.OrgFormKrz = AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET.daten.kuerzel;
				} else {
					final WeiterbildungskollegOrganisationsformen orgform = WeiterbildungskollegOrganisationsformen.getByID(idOrgform);
					if (orgform == null)
						throw new ApiOperationException(Status.BAD_REQUEST, "Die ID %d für die Organisationform am Weiterbildungskolleg ist ungültig");
					dto.OrgFormKrz = orgform.daten.kuerzel;
				}
			}),
			Map.entry("pruefungsordnung", (conn, dto, value, map) -> {
				throw new ApiOperationException(Status.BAD_REQUEST, "Das Setzen der Prüfungsordnung wird hier zur Zeit nicht unterstützt.");
			}),
			Map.entry("idSchulgliederung", (conn, dto, value, map) -> {
				final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
				if (schule == null)
					throw new ApiOperationException(Status.NOT_FOUND, "Die Informatione zur Schule konnten nicht gefunde werden.");
				final Long idSchulgliederung = JSONMapper.convertToLong(value, true);
				if (((idSchulgliederung == null) || (idSchulgliederung == -1)) && (Schulgliederung.getDefault(schule.Schulform) == null)) {
					dto.ASDSchulformNr = null;
					return;
				}
				final Schulgliederung sgl = Schulgliederung.getByID(idSchulgliederung);
				if (!sgl.hasSchulform(schule.Schulform))
					throw new ApiOperationException(Status.BAD_REQUEST, "Die Schulgliederung wird von der angegeben Schulform nicht unterstützt.");
				dto.ASDSchulformNr = sgl.daten.kuerzel;
			}),
			Map.entry("idKlassenart", (conn, dto, value, map) -> {
				final Long idKlassenart = JSONMapper.convertToLong(value, true);
				final Klassenart k = Klassenart.getByID(idKlassenart);
				if (k == null)
					throw new ApiOperationException(Status.BAD_REQUEST, "Die Klassenart für die ID %d konnte nicht gefunden werden.".formatted(idKlassenart));
				dto.Klassenart = k.daten.kuerzel;
			}),
			Map.entry("noteneingabeGesperrt", (conn, dto, value, map) -> dto.NotenGesperrt = JSONMapper.convertToBoolean(value, false)),
			Map.entry("verwendungAnkreuzkompetenzen", (conn, dto, value, map) -> dto.Ankreuzzeugnisse = JSONMapper.convertToBoolean(value, false)),
			Map.entry("idFachklasse", (conn, dto, value, map) -> {
				final Long idFachklasse = JSONMapper.convertToLong(value, true);
				if (idFachklasse == null) {
					dto.Fachklasse_ID = null;
				} else {
					final BerufskollegFachklassenKatalogDaten fachklasse = JsonDaten.fachklassenManager.getDaten(idFachklasse);
					if (fachklasse == null)
						throw new ApiOperationException(Status.BAD_REQUEST, "Keine Fachklasse die ID %d gefunden.".formatted(idFachklasse));
					dto.Fachklasse_ID = fachklasse.id;
				}
			}),
			Map.entry("beginnSommersemester", (conn, dto, value, map) -> {
				dto.SommerSem = JSONMapper.convertToBoolean(value, false);
			}));


	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOKlassen.class, patchMappings);
	}


	private static final Set<String> requiredCreateAttributes = Set.of("id", "idSchuljahresabschnitt", "kuerzel", "idJahrgang");


	/**
	 * Fügt eine Klasse mit den übergebenen JSON-Daten der Datenbank hinzu und gibt das zugehörige CoreDTO
	 * zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response add(final InputStream is) throws ApiOperationException {
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Konnte die Informationen zur Schule nicht einlesen");
		final DTOTeilstandorte teilstandort = conn.querySingle(DTOTeilstandorte.class);
		if (teilstandort == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es ist kein Teilstandort definiert, es muss aber mindestens einer festgelegt sein.");
		final long newID = conn.transactionGetNextID(DTOKlassen.class);
		final Map<String, Object> map = JSONMapper.toMap(is);
		// Prüfe, ob alle relevanten Attribute im JSON-Inputstream vorhanden sind
		for (final String attr : requiredCreateAttributes)
			if (!map.containsKey(attr))
				throw new ApiOperationException(Status.BAD_REQUEST, "Das Attribut %s fehlt in der Anfrage".formatted(attr));
		// Erstelle ein neues DTO für die DB, setze Default-Werte und wende Initialisierung und das Mapping der Attribute an
		final DTOKlassen dto = new DTOKlassen(newID, schule.Schuljahresabschnitts_ID, "");
		dto.Sichtbar = true;
		dto.Sortierung = 32000;
		dto.AdrMerkmal = teilstandort.AdrMerkmal;
		dto.OrgFormKrz = AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET.daten.kuerzel;
		dto.ASDSchulformNr = Schulgliederung.getDefault(schule.Schulform).daten.kuerzel;
		dto.Klassenart = Klassenart.getDefault(schule.Schulform).daten.kuerzel;
		applyPatchMappings(conn, dto, map, patchMappings, Collections.emptySet(), null);
		// Persistiere das DTO in der Datenbank
		if (!conn.transactionPersist(dto))
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
		conn.transactionFlush();
		final KlassenDaten daten = getFromDTOInternal(dto, Collections.emptyList());
		return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Löscht eine Klasse
	 *
	 * @param id   die ID der Klasse
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(final Long id) {
		throw new UnsupportedOperationException("Das Löschen von Klassen ist zur Zeit noch nicht implementiert.");
	}


	/**
	 * Löscht mehrere Klassen und gibt das Ergebnis der Lösch-Operationen als Liste von {@link SimpleOperationResponse} zurück.
	 *
	 * @param ids   die IDs der zu löschenden Klassen
	 *
	 * @return die Response mit einer Liste von {@link SimpleOperationResponse} zu den angefragten Lösch-Operationen.
	 */
	public Response deleteMultiple(final List<Long> ids) {
		// Bestimme die Datenbank-DTOs der Klassen
		final List<DTOKlassen> klassen = this.conn.queryByKeyList(DTOKlassen.class, ids).stream().toList();

		// Prüfe ob das Löschen der Klassen erlaubt ist
		final Map<Long, SimpleOperationResponse> mapResponses = klassen.stream()
				.collect(Collectors.toMap(r -> r.ID, this::checkDeletePreConditions));

		// Lösche die Klassen und gib den Erfolg in der Response zurück
		for (final DTOKlassen klasse : klassen) {
			final SimpleOperationResponse operationResponse = mapResponses.get(klasse.ID);
			if (operationResponse == null)
				throw new DeveloperNotificationException("Das SimpleOperationResponse Objekt zu der ID %d existiert nicht.".formatted(klasse.ID));

			if (operationResponse.log.isEmpty())
				operationResponse.success = this.conn.transactionRemove(klasse);
		}

		return Response.ok().entity(mapResponses.values()).build();
	}

	/**
	 * Diese Methode prüft, ob alle Vorbedingungen zum Löschen einer Klasse erfüllt sind.
	 * Es wird eine {@link SimpleOperationResponse} zurückgegeben.
	 *
	 * @param dtoKlasse   das DTO der Klasse, die gelöscht werden soll
	 *
	 * @return Liefert eine Response mit dem Log der Vorbedingungsprüfung zurück.
	 */
	private SimpleOperationResponse checkDeletePreConditions(@NotNull final DTOKlassen dtoKlasse) {
		final SimpleOperationResponse operationResponse = new SimpleOperationResponse();
		operationResponse.id = dtoKlasse.ID;

		// Die Klasse darf keine Schüler beinhalten. Dies kann an zugeordneten Lernabschnittsdaten geprüft werden
		final List<Long> schuelerIds = getSchuelerIDsByKlassenID(dtoKlasse.ID);
		if (!schuelerIds.isEmpty())
			operationResponse.log.add("Klasse %s (ID: %d) hat noch %d verknüpfte(n) Schüler.".formatted(dtoKlasse.Klasse, dtoKlasse.ID, schuelerIds.size()));

		return operationResponse;
	}

}
