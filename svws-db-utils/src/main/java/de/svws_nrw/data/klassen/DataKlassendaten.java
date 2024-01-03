package de.svws_nrw.data.klassen;

import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogDaten;
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
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.dto.current.schild.schule.DTOTeilstandorte;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.json.JsonDaten;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	 * @param schulform          die Schulform
	 * @param klasse			 Klasse
	 * @param klassenLeitungen	 Leitungen der Klasse
	 * @param schueler			 Schüler der Klasse
	 * @param vorgaengerklasse   die Vorgängerklasse, sofern in der DB vorhanden
	 * @param folgeklasse        die Folgeklasse, sofern in der DB bereits angelegt
	 *
	 * @return Core-DTO mit allen Klasseninformationen
	 */
	private static KlassenDaten dtoMapper(final Schulform schulform, final DTOKlassen klasse, final List<DTOKlassenLeitung> klassenLeitungen,
			final List<DTOSchueler> schueler, final DTOKlassen vorgaengerklasse, final DTOKlassen folgeklasse) {
		final KlassenDaten daten = new KlassenDaten();
		daten.id = klasse.ID;
		daten.idSchuljahresabschnitt = klasse.Schuljahresabschnitts_ID;
		daten.kuerzel = klasse.Klasse;
		daten.idJahrgang = klasse.Jahrgang_ID;
		daten.parallelitaet = klasse.ASDKlasse.length() < 3 ? null : klasse.ASDKlasse.substring(2, 3);
		daten.sortierung = klasse.Sortierung;
		daten.istSichtbar = klasse.Sichtbar;
		if (klassenLeitungen != null)
			for (final DTOKlassenLeitung kl : klassenLeitungen)
				daten.klassenLeitungen.add(kl.Lehrer_ID);
		for (final DTOSchueler dto : schueler)
			daten.schueler.add(DataSchuelerliste.mapToSchueler.apply(dto));
		daten.teilstandort = klasse.AdrMerkmal == null ? "" : klasse.AdrMerkmal;
		daten.beschreibung = klasse.Bezeichnung == null ? "" : klasse.Bezeichnung;
		daten.idAllgemeinbildendOrganisationsform = AllgemeinbildendOrganisationsformen.getByKuerzel(klasse.OrgFormKrz) == null
				? null : AllgemeinbildendOrganisationsformen.getByKuerzel(klasse.OrgFormKrz).daten.id;
		daten.idBerufsbildendOrganisationsform = BerufskollegOrganisationsformen.getByKuerzel(klasse.OrgFormKrz) == null
				? null : BerufskollegOrganisationsformen.getByKuerzel(klasse.OrgFormKrz).daten.id;
		daten.idWeiterbildungOrganisationsform = WeiterbildungskollegOrganisationsformen.getByKuerzel(klasse.OrgFormKrz) == null
				? null : WeiterbildungskollegOrganisationsformen.getByKuerzel(klasse.OrgFormKrz).daten.id;
		daten.pruefungsordnung = klasse.PruefOrdnung;
		final Schulgliederung gliederung = Schulgliederung.getBySchulformAndKuerzel(schulform, klasse.ASDSchulformNr);
		if (gliederung == null)
			throw OperationError.INTERNAL_SERVER_ERROR.exception("Kann für die Schulform %s keine gültige Schulgliederung ermitteln.".formatted(schulform.toString()));
		daten.idSchulgliederung = gliederung.daten.id;
		final Klassenart klassenart = Klassenart.getByKuerzel(klasse.Klassenart);
		daten.idKlassenart = ((klassenart != null) && (klassenart.hasSchulform(schulform))) ? klassenart.daten.id : Klassenart.UNDEFINIERT.daten.id;
		daten.noteneingabeGesperrt = klasse.NotenGesperrt != null && klasse.NotenGesperrt;
		daten.verwendungAnkreuzkompetenzen = klasse.Ankreuzzeugnisse != null && klasse.Ankreuzzeugnisse;
		daten.idVorgaengerklasse = (vorgaengerklasse == null) ? null : vorgaengerklasse.ID;
		daten.kuerzelVorgaengerklasse = (vorgaengerklasse == null) ? klasse.VKlasse : vorgaengerklasse.Klasse;
		daten.idFolgeklasse = (folgeklasse == null) ? null : folgeklasse.ID;
		daten.kuerzelFolgeklasse = (folgeklasse == null) ? klasse.FKlasse : folgeklasse.Klasse;
		daten.idFachklasse = klasse.Fachklasse_ID;
		daten.beginnSommersemester = klasse.SommerSem != null && klasse.SommerSem;
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
	public Response get(final Long id) {
		final KlassenDaten daten = getFromID(id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private KlassenDaten getFromIDInternal(final Long id, final List<DTOSchueler> dtoSchueler) throws WebApplicationException {
		final DTOKlassen klasse = conn.queryByKey(DTOKlassen.class, id);
		if (klasse == null)
			throw OperationError.NOT_FOUND.exception("Keine Klasse zur ID " + id + " gefunden.");
		return getFromDTOInternal(klasse, dtoSchueler);
	}


	private KlassenDaten getFromDTOInternal(final DTOKlassen klasse, final List<DTOSchueler> dtoSchueler) throws WebApplicationException {
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw OperationError.NOT_FOUND.exception("Konnte die Informationen zur Schule nicht einlesen");
		final List<DTOKlassenLeitung> klassenLeitungen = conn.queryNamed("DTOKlassenLeitung.klassen_id", klasse.ID, DTOKlassenLeitung.class);
		// Bestimme den Schuljahresabschnitt der Klasse
		final DTOSchuljahresabschnitte schuljahresabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, klasse.Schuljahresabschnitts_ID);
		if (schuljahresabschnitt == null)
			throw OperationError.NOT_FOUND.exception("Der Schuljahresabschnitt mit der ID %d für die Klasse mit der ID %d wurde nicht gefunden.".formatted(klasse.Schuljahresabschnitts_ID, klasse.ID));
		// Bestimme die Vorgänger-Klasse, falls eine angegeben ist
		DTOKlassen vorgaengerklasse = null;
		if ((klasse.VKlasse != null) && (schuljahresabschnitt.VorigerAbschnitt_ID != null)) {
			final List<DTOKlassen> listKlassen = conn.queryList("SELECT e FROM DTOKlassen e WHERE e.Schuljahresabschnitts_ID = ?1 AND e.Klasse = ?2", DTOKlassen.class, schuljahresabschnitt.VorigerAbschnitt_ID, klasse.VKlasse);
			if (listKlassen.size() == 1)
				vorgaengerklasse = listKlassen.get(0);
		}
		// Bestimme die Folge-Klasse, falls eine angegeben ist
		DTOKlassen folgeklasse = null;
		if ((klasse.FKlasse != null) && (schuljahresabschnitt.FolgeAbschnitt_ID != null)) {
			final List<DTOKlassen> listKlassen = conn.queryList("SELECT e FROM DTOKlassen e WHERE e.Schuljahresabschnitts_ID = ?1 AND e.Klasse = ?2", DTOKlassen.class, schuljahresabschnitt.FolgeAbschnitt_ID, klasse.FKlasse);
			if (listKlassen.size() == 1)
				folgeklasse = listKlassen.get(0);
		}
		// Erstelle das Core-DTO-Objekt für die Klasse
		return dtoMapper(schule.Schulform, klasse, klassenLeitungen, dtoSchueler, vorgaengerklasse, folgeklasse);
	}


	/**
	 * Gibt die Daten einer Klasse zu deren ID zurück.
	 *
	 * @param id	Die ID der Klasse.
	 * @return		Die Daten der KLasse zur ID.
	 *
	 * @throws WebApplicationException im Fehlerfall
	 */
	public KlassenDaten getFromID(final Long id) throws WebApplicationException {
		if (id == null)
			throw OperationError.NOT_FOUND.exception("Keine ID für die Klasse übergeben.");
		// Bestimme die Schüler der Klasse
		final List<Long> schuelerIDs = conn.queryNamed("DTOSchuelerLernabschnittsdaten.klassen_id", id, DTOSchuelerLernabschnittsdaten.class)
			.stream().filter(sla -> sla.WechselNr == 0).map(sla -> sla.Schueler_ID).toList();
		final List<DTOSchueler> dtoSchueler = schuelerIDs == null || schuelerIDs.isEmpty() ? new ArrayList<>()
			: conn.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class);
		// Erstelle das Core-DTO-Objekt für die Klasse
		return getFromIDInternal(id, dtoSchueler);
	}


	/**
	 * Gibt die Daten einer Klasse zu deren ID ohne Schülerliste zurück.
	 *
	 * @param id	Die ID der Klasse.
	 * @return		Die Daten der KLasse zur ID ohne Schülerliste.
	 *
	 * @throws WebApplicationException im Fehlerfall
	 */
	public KlassenDaten getFromIDOhneSchueler(final Long id) throws WebApplicationException {
		if (id == null)
			throw OperationError.NOT_FOUND.exception("Keine ID für die Klasse übergeben.");
		// Erstelle das Core-DTO-Objekt für die Klasse
		return getFromIDInternal(id, Collections.emptyList());
	}

	private static final Map<String, DataBasicMapper<DTOKlassen>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID))
				throw OperationError.BAD_REQUEST.exception("Die ID im Patch stimmt nicht mit der ID des Datenbank-Objektes überein");
		}),
		Map.entry("idSchuljahresabschnitt", (conn, dto, value, map) -> {
			throw OperationError.BAD_REQUEST.exception("Die ID des Schuljahresabschnittes darf nicht nachträglich angepasst werden.");
		}),
		Map.entry("kuerzel", (conn, dto, value, map) -> {
			dto.Klasse = JSONMapper.convertToString(value, false, false, 6);
		}),
		Map.entry("idJahrgang", (conn, dto, value, map) -> {
			final long idJahrgang = JSONMapper.convertToLong(value, false);
			final DTOJahrgang jg = conn.queryByKey(DTOJahrgang.class, idJahrgang);
			if (jg == null)
				throw OperationError.NOT_FOUND.exception("Der Jahrgang mit der ID %d konnte nicht gefunden werden.".formatted(idJahrgang));
			dto.Jahrgang_ID = jg.ID;
			dto.ASDKlasse = jg.ASDJahrgang + (((dto.ASDKlasse != null) && (dto.ASDKlasse.length() > 2)) ? dto.ASDKlasse.charAt(2) : "");
		}),
		Map.entry("parallelitaet", (conn, dto, value, map) -> {
			final String parallelitaet = JSONMapper.convertToString(value, true, false, 1);
			if (parallelitaet == null) {
				dto.ASDKlasse = dto.ASDKlasse.substring(0, 2);
			} else {
				final char p = parallelitaet.charAt(0);
				if ((p < 'A') || (p > 'Z'))
					throw OperationError.BAD_REQUEST.exception("Die Parallelität muss durch einen Buchstaben A-Z in Großschreibung angegeben werden.");
				dto.ASDKlasse = dto.ASDKlasse.substring(0, 2) + p;
			}
		}),
		Map.entry("sortierung", (conn, dto, value, map) -> dto.Sortierung = JSONMapper.convertToIntegerInRange(value, false, 0, Integer.MAX_VALUE)),
		Map.entry("istSichtbar", (conn, dto, value, map) -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false)),
		Map.entry("teilstandort", (conn, dto, value, map) -> {
			final String t = JSONMapper.convertToString(value, false, false, 1);
			final DTOTeilstandorte teilstandort = conn.queryByKey(DTOTeilstandorte.class, t);
			if (teilstandort == null)
				throw OperationError.NOT_FOUND.exception("Der Teilstandort %s wurde nicht gefunden.".formatted(t));
			dto.AdrMerkmal = t;
		}),
		Map.entry("beschreibung", (conn, dto, value, map) -> dto.Bezeichnung = JSONMapper.convertToString(value, true, true, 151)),
		Map.entry("idVorgaengerklasse", (conn, dto, value, map) -> {
			final long idVorgaengerklasse = JSONMapper.convertToLong(value, false);
			final DTOKlassen vk = conn.queryByKey(DTOKlassen.class, idVorgaengerklasse);
			if (vk == null)
				throw OperationError.NOT_FOUND.exception("Die Vorgängerklasse mit der ID %d wurde nicht gefunden.".formatted(idVorgaengerklasse));
			final DTOSchuljahresabschnitte a = conn.queryByKey(DTOSchuljahresabschnitte.class, dto.Schuljahresabschnitts_ID);
			if (a == null)
				throw OperationError.INTERNAL_SERVER_ERROR.exception("Die ID des Schuljahresabschnitts %d der Klasse mit der ID %d ist ungültig.".formatted(dto.Schuljahresabschnitts_ID, dto.ID));
			if (vk.Schuljahresabschnitts_ID != a.VorigerAbschnitt_ID)
				throw OperationError.BAD_REQUEST.exception("Die ID für die Vorgängerklasse gehört nicht zu einer Klasse aus dem vorigen Schuljahresabschnitt.");
			dto.VKlasse = vk.Klasse;
		}),
		Map.entry("kuerzelVorgaengerklasse", (conn, dto, value, map) -> {
			throw OperationError.BAD_REQUEST.exception("Das Kürzel für die Vorgängerklasse kann nur indirekt über die ID für die Vorgängerklasse angepasst werden.");
		}),
		Map.entry("idFolgeklasse", (conn, dto, value, map) -> {
			final long idFolgeklasse = JSONMapper.convertToLong(value, false);
			final DTOKlassen fk = conn.queryByKey(DTOKlassen.class, idFolgeklasse);
			if (fk == null)
				throw OperationError.NOT_FOUND.exception("Die Folgeklasse mit der ID %d wurde nicht gefunden.".formatted(idFolgeklasse));
			final DTOSchuljahresabschnitte a = conn.queryByKey(DTOSchuljahresabschnitte.class, dto.Schuljahresabschnitts_ID);
			if (a == null)
				throw OperationError.INTERNAL_SERVER_ERROR.exception("Die ID des Schuljahresabschnitts %d der Klasse mit der ID %d ist ungültig.".formatted(dto.Schuljahresabschnitts_ID, dto.ID));
			if (fk.Schuljahresabschnitts_ID != a.FolgeAbschnitt_ID)
				throw OperationError.BAD_REQUEST.exception("Die ID für die Folgeklasse gehört nicht zu einer Klasse aus dem nachfolgenden Schuljahresabschnitt.");
			dto.FKlasse = fk.Klasse;
		}),
		Map.entry("kuerzelFolgeklasse", (conn, dto, value, map) -> {
			throw OperationError.BAD_REQUEST.exception("Das Kürzel für die Folgeklasse kann nur indirekt über die ID für die Folgeklasse angepasst werden.");
		}),
		Map.entry("idAllgemeinbildendOrganisationsform", (conn, dto, value, map) -> {
			final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
			if (!schule.Schulform.daten.istAllgemeinbildend)
				throw OperationError.BAD_REQUEST.exception("Der Wert kann nicht gesetzt werden, da die Schule keine allgemeinbildende Schulform hat.");
			final Long idOrgform = JSONMapper.convertToLong(value, true);
			AllgemeinbildendOrganisationsformen orgform = AllgemeinbildendOrganisationsformen.getByID(idOrgform);
			if (idOrgform == null)
				orgform = AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET;
			if (orgform == null)
				throw OperationError.BAD_REQUEST.exception("Die ID %d für die allgemeinene Organisationform ist ungültig");
			dto.OrgFormKrz = orgform.daten.kuerzel;
		}),
		Map.entry("idBerufsbildendOrganisationsform", (conn, dto, value, map) -> {
			final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
			if (!schule.Schulform.daten.istBerufsbildend)
				throw OperationError.BAD_REQUEST.exception("Der Wert kann nicht gesetzt werden, da die Schule keine berufsbildende Schulform hat.");
			final Long idOrgform = JSONMapper.convertToLong(value, true);
			if (idOrgform == null) {
				dto.OrgFormKrz = AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET.daten.kuerzel;
			} else {
				final BerufskollegOrganisationsformen orgform = BerufskollegOrganisationsformen.getByID(idOrgform);
				if (orgform == null)
					throw OperationError.BAD_REQUEST.exception("Die ID %d für die allgemeinene Organisationform ist ungültig");
				dto.OrgFormKrz = orgform.daten.kuerzel;
			}
		}),
		Map.entry("idWeiterbildungOrganisationsform", (conn, dto, value, map) -> {
			final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
			if (!schule.Schulform.daten.istWeiterbildung)
				throw OperationError.BAD_REQUEST.exception("Der Wert kann nicht gesetzt werden, da die Schule keine Schulform für die Weiterbildung hat.");
			final Long idOrgform = JSONMapper.convertToLong(value, true);
			if (idOrgform == null) {
				dto.OrgFormKrz = AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET.daten.kuerzel;
			} else {
				final WeiterbildungskollegOrganisationsformen orgform = WeiterbildungskollegOrganisationsformen.getByID(idOrgform);
				if (orgform == null)
					throw OperationError.BAD_REQUEST.exception("Die ID %d für die Organisationform am Weiterbildungskolleg ist ungültig");
				dto.OrgFormKrz = orgform.daten.kuerzel;
			}
		}),
		Map.entry("pruefungsordnung", (conn, dto, value, map) -> {
			throw OperationError.BAD_REQUEST.exception("Das Setzen der Prüfungsordnung wird hier zur Zeit nicht unterstützt.");
		}),
		Map.entry("idSchulgliederung", (conn, dto, value, map) -> {
			final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
			if (schule == null)
				throw OperationError.NOT_FOUND.exception("Die Informatione zur Schule konnten nicht gefunde werden.");
			final Long idSchulgliederung = JSONMapper.convertToLong(value, true);
			final Schulgliederung sgl = Schulgliederung.getByID(idSchulgliederung);
			if (!sgl.hasSchulform(schule.Schulform))
				throw OperationError.BAD_REQUEST.exception("Die Schulgliederung wird von der angegeben Schulform nicht unterstützt.");
			dto.ASDSchulformNr = sgl.daten.kuerzel;
		}),
		Map.entry("idKlassenart", (conn, dto, value, map) -> {
			final Long idKlassenart = JSONMapper.convertToLong(value, true);
			final Klassenart k = Klassenart.getByID(idKlassenart);
			if (k == null)
				throw OperationError.BAD_REQUEST.exception("Die Klassenart für die ID %d konnte nicht gefunden werden.".formatted(idKlassenart));
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
					throw OperationError.BAD_REQUEST.exception("Keine Fachklasse die ID %d gefunden.".formatted(idFachklasse));
				dto.Fachklasse_ID = fachklasse.id;
			}
		}),
		Map.entry("beginnSommersemester", (conn, dto, value, map) -> {
			dto.SommerSem = JSONMapper.convertToBoolean(value, false);
		})
	);


	@Override
	public Response patch(final Long id, final InputStream is) {
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
	 */
	public Response add(final InputStream is) {
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw OperationError.NOT_FOUND.exception("Konnte die Informationen zur Schule nicht einlesen");
		final DTOTeilstandorte teilstandort = conn.querySingle(DTOTeilstandorte.class);
		if (teilstandort == null)
			throw OperationError.NOT_FOUND.exception("Es ist kein Teilstandort definiert, es muss aber mindestens einer festgelegt sein.");
		final long newID = conn.transactionGetNextID(DTOKlassen.class);
		final Map<String, Object> map = JSONMapper.toMap(is);
		// Prüfe, ob alle relevanten Attribute im JSON-Inputstream vorhanden sind
		for (final String attr : requiredCreateAttributes)
			if (!map.containsKey(attr))
				throw OperationError.BAD_REQUEST.exception("Das Attribut %s fehlt in der Anfrage".formatted(attr));
		// Erstelle ein neues DTO für die DB, setze Default-Werte und wende Initialisierung und das Mapping der Attribute an
		final DTOKlassen dto = new DTOKlassen(newID, schule.Schuljahresabschnitts_ID, "");
		dto.Sichtbar = true;
		dto.Sortierung = 32000;
		dto.AdrMerkmal = teilstandort.AdrMerkmal;
		dto.OrgFormKrz = AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET.daten.kuerzel;
		dto.ASDSchulformNr = Schulgliederung.getDefault(schule.Schulform).daten.kuerzel;
		dto.Klassenart = Klassenart.getDefault(schule.Schulform).daten.kuerzel;
		applyPatchMappings(conn, dto, map, patchMappings, null);
		// Persistiere das DTO in der Datenbank
		if (!conn.transactionPersist(dto))
			throw OperationError.INTERNAL_SERVER_ERROR.exception();
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

}
