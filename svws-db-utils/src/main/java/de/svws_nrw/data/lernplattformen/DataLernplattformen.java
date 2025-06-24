package de.svws_nrw.data.lernplattformen;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.kurse.ZulaessigeKursartKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Fach;
import de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Jahrgang;
import de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Klasse;
import de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Lehrer;
import de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Lerngruppe;
import de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Schueler;
import de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Export;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLernplattform;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattform;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.svws.auth.DTOCredentialsLernplattformen;
import de.svws_nrw.db.dto.current.svws.auth.DTOLernplattformen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse ist der DataManager für die Lernplattformen und stellt die gesammelten Daten bereit. Das zugehörige Core-DTO ist {@link LernplattformV1Export}.
 */
public class DataLernplattformen {

	/** Die Datenbank-Verbindung zum Aggregieren der Informationen aus der DB und zum Schreiben der Informationen bzw. Teilinformationen */
	private final @NotNull DBEntityManager conn;

	/** Die ID des Schuljahresabschnitts zu dem die Lernplattform Daten ermittelt werden. */
	private final int idSchuljahresabschnitt;

	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn                    die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden soll
	 * @param idSchuljahresabschnitt  die ID des Schuljahresabschnitts
	 */
	public DataLernplattformen(final @NotNull DBEntityManager conn, final int idSchuljahresabschnitt) {
		this.conn = conn;
		this.idSchuljahresabschnitt = idSchuljahresabschnitt;
	}

	/**
	 * Gibt die Lernplattform-Daten als mit JSON-Objekt zurück.
	 *
	 * @param idLernplattform ID der Lernplattform
	 *
	 * @return das GZIP-komprimierte JSON-Objekt.
	 */
	public Response getByIdAsResponse(final long idLernplattform) throws ApiOperationException {
		return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(getById(idLernplattform)).build();
	}

	/**
	 * Gibt die Lernplattform-Daten als mit GZIP komprimiertes JSON-Objekt zurück.
	 *
	 * @param idLernplattform ID der Lernplattform
	 *
	 * @return das GZIP-komprimierte JSON-Objekt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response getByIdAsGzipResponse(final long idLernplattform) throws ApiOperationException {
		return JSONMapper.gzipFileResponseFromObject(getById(idLernplattform), "lernplattform.json.gz");
	}

	private LernplattformV1Export getById(final long idLernplattform) throws ApiOperationException {
		final LernplattformV1Export lernplattformExport = new LernplattformV1Export();
		// Zeitpunkt der API Anfrage
		lernplattformExport.anfrageZeitpunkt = LocalDateTime.now().toString();

		// Lese Daten zum aktuellen Schuljahresabschnitt aus der Datenbank
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(idSchuljahresabschnitt);
		final Map<Long, DTOLehrer> mapLehrer = getLehrerListe();
		final Map<Long, DTOSchueler> mapSchueler = getSchuelerListe(schuljahresabschnitt);
		final Map<Long, DTOCredentialsLernplattformen> mapLernplattformenCredentials = getLernplattformenCredentials(idLernplattform);
		final Map<Long, DTOSchuelerLernplattform> mapSchuelerLernplattformen = getSchuelerLernplattformen(idLernplattform);
		final Map<Long, DTOLehrerLernplattform> mapLehrerLernplattformen = getLehrerLernplattformen(idLernplattform);
		final Map<Long, DTOFach> mapFaecher = getFaecherListe();
		final Map<Long, DTOJahrgang> mapJahrgaenge = getJahrgangsListe();
		final Map<Long, DTOKlassen> mapKlassen = getKlassenListe(schuljahresabschnitt);
		final Map<Long, List<DTOKlassenLeitung>> mapKlassenLeitung = getKlassenleitungen(mapKlassen);
		final Map<Long, DTOKurs> mapKurse = getKurse(schuljahresabschnitt);

		final Map<Long, LernplattformV1Jahrgang> jahrgaengeToExport = new HashMap<>();
		final Map<Long, LernplattformV1Klasse> klassenToExport = new HashMap<>();
		final Map<Long, LernplattformV1Schueler> schuelerToExport = new HashMap<>();
		final Map<Long, LernplattformV1Fach> faecherToExport = new HashMap<>();
		final Map<Long, LernplattformV1Lehrer> lehrerToExport = new HashMap<>();
		final Map<String, LernplattformV1Lerngruppe> lerngruppenToExport = new HashMap<>();

		final AtomicInteger lerngruppenIDZaehler = new AtomicInteger(1);

		// Bestimme alle aktuellen Lernabschnitte des aktuellen Schuljahresabschnittes
		final List<DTOSchuelerLernabschnittsdaten> schuelerLernabschnitte =
				conn.queryList("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schuljahresabschnitts_ID = ?1 AND e.WechselNr = 0",
								DTOSchuelerLernabschnittsdaten.class, schuljahresabschnitt.id).stream()
						.filter(la -> mapSchueler.containsKey(la.Schueler_ID))
						.toList();
		final Set<Long> idsLernabschnitte = schuelerLernabschnitte.stream().map(la -> la.ID).collect(Collectors.toSet());

		// Bestimme die relevanten Schüler Leistungsdaten zu den Lernabschnitten
		final Map<Long, List<DTOSchuelerLeistungsdaten>> mapSchuelerLeistungsdaten = conn.queryList(DTOSchuelerLeistungsdaten.QUERY_LIST_BY_ABSCHNITT_ID,
				DTOSchuelerLeistungsdaten.class, idsLernabschnitte).stream().collect(Collectors.groupingBy(l -> l.Abschnitt_ID));

		// setze Headerdaten im Exportobjekt
		addExportHeaderdaten(lernplattformExport, idLernplattform, schuljahresabschnitt);

		for (final DTOSchuelerLernabschnittsdaten schuelerLernabschnitt : schuelerLernabschnitte) {
			if ((schuelerLernabschnitt.Klassen_ID == null) || (schuelerLernabschnitt.Jahrgang_ID == null))
				continue;

			addSchuelerToExport(schuelerToExport, schuelerLernabschnitt, mapSchueler, mapSchuelerLernplattformen, mapLernplattformenCredentials);
			addSchuelerLeistungsdatenToExport(lerngruppenToExport, schuelerToExport, faecherToExport, schuelerLernabschnitt, mapSchuelerLeistungsdaten,
					mapFaecher, mapSchueler, schuljahresabschnitt, lerngruppenIDZaehler, mapKurse);
			addKlasseToExport(klassenToExport, lehrerToExport, schuelerLernabschnitt, mapKlassen, mapKlassenLeitung, mapLehrer, mapLehrerLernplattformen,
					mapLernplattformenCredentials);
			addJahrgangToExport(jahrgaengeToExport, schuelerLernabschnitt, mapJahrgaenge);
		}

		// Alle Daten dem Exportobjekt hinzufügen
		lernplattformExport.jahrgaenge.addAll(jahrgaengeToExport.values());
		lernplattformExport.klassen.addAll(klassenToExport.values());
		lernplattformExport.lehrer.addAll(lehrerToExport.values());
		lernplattformExport.faecher.addAll(faecherToExport.values());
		lernplattformExport.lerngruppen.addAll(lerngruppenToExport.values());
		lernplattformExport.schueler.addAll(schuelerToExport.values());

		// Alle Listen im resultierenden Exportobjekt nach ID aufsteigend sortieren
		lernplattformExport.jahrgaenge.sort(Comparator.comparingLong(jahrgang -> jahrgang.id));
		lernplattformExport.klassen.sort(Comparator.comparingLong(klasse -> klasse.id));
		lernplattformExport.lehrer.sort(Comparator.comparingLong(lehrer -> lehrer.id));
		lernplattformExport.faecher.sort(Comparator.comparingLong(faecher -> faecher.id));
		lernplattformExport.lerngruppen.sort(Comparator.comparingLong(lerngruppe -> lerngruppe.id));
		lernplattformExport.schueler.sort(Comparator.comparingLong(schueler -> schueler.id));

		// Zeitpunkt der API Antwort
		lernplattformExport.antwortZeitpunkt = LocalDateTime.now().toString();

		return lernplattformExport;
	}

	private Map<Long, DTOLehrerLernplattform> getLehrerLernplattformen(final long idLernplattform) {
		return conn.queryList(DTOLehrerLernplattform.QUERY_LIST_BY_LERNPLATTFORMID, DTOLehrerLernplattform.class, List.of(idLernplattform)).stream()
				.collect(Collectors.toMap(lernplattform -> lernplattform.LehrerID, lernplattform -> lernplattform));
	}

	private Map<Long, DTOCredentialsLernplattformen> getLernplattformenCredentials(final long idLernplattform) {
		return conn.queryList(DTOCredentialsLernplattformen.QUERY_LIST_BY_LERNPLATTFORMID, DTOCredentialsLernplattformen.class, List.of(idLernplattform))
				.stream()
				.collect(Collectors.toMap(credentials -> credentials.ID, credentials -> credentials));
	}

	private Map<Long, DTOSchuelerLernplattform> getSchuelerLernplattformen(final long idLernplattform) {
		return conn.queryList(DTOSchuelerLernplattform.QUERY_LIST_BY_LERNPLATTFORMID, DTOSchuelerLernplattform.class, List.of(idLernplattform)).stream()
				.collect(Collectors.toMap(lernplattform -> lernplattform.SchuelerID, lernplattform -> lernplattform));
	}

	private void addExportHeaderdaten(final LernplattformV1Export lernplattformExport, final long idLernplattform,
			final Schuljahresabschnitt schuljahresabschnitt) throws ApiOperationException {
		final DTOLernplattformen lernplattformDto = getLernplattform(idLernplattform);
		final SchuleStammdaten schuleStammdaten = conn.getUser().schuleGetStammdaten(); // geht das wirklich über den Nutzer? Wie wird der Nutzer erstellt?
		lernplattformExport.schulnummer = schuleStammdaten.schulNr;
		lernplattformExport.schuljahr = schuljahresabschnitt.schuljahr;
		lernplattformExport.idSchuljahresabschnitt = schuljahresabschnitt.id;
		lernplattformExport.mailadresse = schuleStammdaten.email;
		lernplattformExport.schulbezeichnung = schuleStammdaten.bezeichnung1 + schuleStammdaten.bezeichnung2 + schuleStammdaten.bezeichnung3;
		lernplattformExport.idLernplattform = lernplattformDto.ID;
		lernplattformExport.lernplattformBezeichnung = lernplattformDto.Bezeichnung;
	}

	private void addSchuelerLeistungsdatenToExport(final Map<String, LernplattformV1Lerngruppe> lerngruppenToExport,
			final Map<Long, LernplattformV1Schueler> schuelerToExport, final Map<Long, LernplattformV1Fach> faecherToExport,
			final DTOSchuelerLernabschnittsdaten schuelerLernabschnittsdaten, final Map<Long, List<DTOSchuelerLeistungsdaten>> mapSchuelerLeistungsdaten,
			final Map<Long, DTOFach> mapFaecher, final Map<Long, DTOSchueler> mapSchueler, final Schuljahresabschnitt abschnitt,
			final AtomicInteger lerngruppenIDZaehler, final Map<Long, DTOKurs> mapKurse) {
		final DTOSchueler schuelerDto = mapSchueler.get(schuelerLernabschnittsdaten.Schueler_ID);

		final List<DTOSchuelerLeistungsdaten> schuelerLeistungsdatenList =
				mapSchuelerLeistungsdaten.getOrDefault(schuelerLernabschnittsdaten.ID, Collections.emptyList());
		for (final DTOSchuelerLeistungsdaten schuelerLeistungsdaten : schuelerLeistungsdatenList) {
			if (schuelerLeistungsdaten.Fachlehrer_ID == null)
				continue;

			final DTOFach fachDto = mapFaecher.get(schuelerLeistungsdaten.Fach_ID);

			addFachToExport(faecherToExport, fachDto);
			final LernplattformV1Lerngruppe lernplattformLerngruppe = addLerngruppeToExport(lerngruppenToExport, abschnitt, lerngruppenIDZaehler, mapKurse,
					schuelerLeistungsdaten, fachDto, schuelerLernabschnittsdaten.Klassen_ID);

			final LernplattformV1Schueler lernplattformSchueler = schuelerToExport.get(schuelerDto.ID);
			if (lernplattformSchueler != null)
				lernplattformSchueler.idsLerngruppen.add(lernplattformLerngruppe.id);
		}
	}

	private void addFachToExport(final Map<Long, LernplattformV1Fach> faecherToExport, final DTOFach fachDto) {
		// Prüfen, ob Fach bereits im Export enthalten ist
		if (faecherToExport.containsKey(fachDto.ID))
			return;

		final LernplattformV1Fach lernplattformFach = new LernplattformV1Fach();
		lernplattformFach.id = fachDto.ID;
		lernplattformFach.kuerzel = fachDto.StatistikKuerzel;
		lernplattformFach.kuerzelAnzeige = fachDto.Kuerzel;
		lernplattformFach.istFremdsprache = fachDto.IstFremdsprache;

		faecherToExport.put(lernplattformFach.id, lernplattformFach);
	}

	private LernplattformV1Lerngruppe addLerngruppeToExport(final Map<String, LernplattformV1Lerngruppe> lerngruppenToExport,
			final Schuljahresabschnitt abschnitt, final AtomicInteger lerngruppenIDZaehler, final Map<Long, DTOKurs> mapKurse,
			final DTOSchuelerLeistungsdaten schuelerLeistungsdaten, final DTOFach fachDto, final long klassenId) {
		// Hier wird die temporäre LerngruppenID erstellt, mit der in der Klasse Lernplattformdaten gearbeitet wird.
		// Es wird eine eindeutige ID benötigt, die Kurs- und Klassenübergreifend diese Lerngruppe identifiziert.
		final String lerngruppenID = (schuelerLeistungsdaten.Kurs_ID == null)
				? ("Klasse:" + klassenId + "/" + schuelerLeistungsdaten.Fach_ID)
				: ("Kurs:" + schuelerLeistungsdaten.Kurs_ID);

		// Prüfen, ob Lerngruppe bereits im Export enthalten ist
		if (lerngruppenToExport.containsKey(lerngruppenID))
			return lerngruppenToExport.get(lerngruppenID);

		final ZulaessigeKursart kursart = getKursart(schuelerLeistungsdaten);
		final String kursartAllg = getKursartAllg(abschnitt, kursart);

		final LernplattformV1Lerngruppe lernplattformLerngruppe = new LernplattformV1Lerngruppe();
		// Unterscheidung zwischen den beiden Lerngruppen-Typen (Klasse / Kurs)
		if (schuelerLeistungsdaten.Kurs_ID == null) {
			lernplattformLerngruppe.id = lerngruppenIDZaehler.getAndIncrement();
			lernplattformLerngruppe.idIntern = klassenId;
			lernplattformLerngruppe.idFach = schuelerLeistungsdaten.Fach_ID;
			lernplattformLerngruppe.idKursart = null;
			lernplattformLerngruppe.bezeichnung = fachDto.Kuerzel;
			lernplattformLerngruppe.kursartKuerzel = kursartAllg;
			lernplattformLerngruppe.bilingualeSprache = fachDto.Unterrichtssprache;
			lernplattformLerngruppe.wochenstunden = (schuelerLeistungsdaten.Wochenstunden == null) ? null : schuelerLeistungsdaten.Wochenstunden;
		} else {
			final DTOKurs kurs = mapKurse.get(schuelerLeistungsdaten.Kurs_ID);
			lernplattformLerngruppe.id = lerngruppenIDZaehler.getAndIncrement();
			lernplattformLerngruppe.idIntern = schuelerLeistungsdaten.Kurs_ID;
			lernplattformLerngruppe.idFach = schuelerLeistungsdaten.Fach_ID;
			lernplattformLerngruppe.idKursart = (kursart == null) ? null : Integer.parseInt(kursart.daten(abschnitt.schuljahr).nummer);
			lernplattformLerngruppe.bezeichnung = kurs.KurzBez;
			lernplattformLerngruppe.kursartKuerzel = kursartAllg;
			lernplattformLerngruppe.bilingualeSprache = fachDto.Unterrichtssprache;
			lernplattformLerngruppe.wochenstunden = kurs.WochenStd;
		}

		lernplattformLerngruppe.idsLehrer.add(schuelerLeistungsdaten.Fachlehrer_ID);
		lerngruppenToExport.put(lerngruppenID, lernplattformLerngruppe);

		return lernplattformLerngruppe;
	}

	private String getKursartAllg(final Schuljahresabschnitt abschnitt, final ZulaessigeKursart kursart) {
		if (kursart == null)
			return null;

		final ZulaessigeKursartKatalogEintrag kursartEintrag = kursart.daten(abschnitt.schuljahr);
		return ((kursartEintrag.kuerzelAllg == null) || kursartEintrag.kuerzelAllg.isEmpty()) ? kursartEintrag.kuerzel : kursartEintrag.kuerzelAllg;
	}

	private ZulaessigeKursart getKursart(final DTOSchuelerLeistungsdaten schuelerLeistungsdaten) {
		return (schuelerLeistungsdaten.Kurs_ID == null) ? null : ZulaessigeKursart.data().getWertByKuerzel(schuelerLeistungsdaten.Kursart);
	}

	private void addSchuelerToExport(final Map<Long, LernplattformV1Schueler> schuelerToExport,
			final DTOSchuelerLernabschnittsdaten schuelerLernabschnittsdaten, final Map<Long, DTOSchueler> mapSchueler,
			final Map<Long, DTOSchuelerLernplattform> mapSchuelerLernplattformen,
			final Map<Long, DTOCredentialsLernplattformen> mapLernplattformenCredentials) {
		final DTOSchueler schuelerDto = mapSchueler.get(schuelerLernabschnittsdaten.Schueler_ID);
		final DTOSchuelerLernplattform schuelerLernplattformenDto = mapSchuelerLernplattformen.get(schuelerDto.ID);
		final DTOCredentialsLernplattformen schuelerCredentialsDto =
				(schuelerLernplattformenDto != null) ? mapLernplattformenCredentials.get(schuelerLernplattformenDto.CredentialID) : null;

		final LernplattformV1Schueler lernplattformSchueler = new LernplattformV1Schueler();
		lernplattformSchueler.id = schuelerDto.ID;
		lernplattformSchueler.geschlecht = schuelerDto.Geschlecht.kuerzel;
		lernplattformSchueler.vorname = schuelerDto.Vorname;
		lernplattformSchueler.nachname = schuelerDto.Nachname;
		lernplattformSchueler.idJahrgang = schuelerLernabschnittsdaten.Jahrgang_ID;
		lernplattformSchueler.idKlasse = schuelerLernabschnittsdaten.Klassen_ID;

		if (schuelerCredentialsDto != null) {
			lernplattformSchueler.lernplattformlogin.benutzername = schuelerCredentialsDto.Benutzername;
			lernplattformSchueler.lernplattformlogin.initialpasswort = schuelerCredentialsDto.Initialkennwort;
		}

		schuelerToExport.put(lernplattformSchueler.id, lernplattformSchueler);
	}

	private void addJahrgangToExport(final Map<Long, LernplattformV1Jahrgang> jahrgaengeToExport, final DTOSchuelerLernabschnittsdaten lernabschnittsdaten,
			final Map<Long, DTOJahrgang> mapJahrgaenge) {
		// Prüfen, ob Jahrgang bereits im Export enthalten ist
		if (jahrgaengeToExport.containsKey(lernabschnittsdaten.Jahrgang_ID))
			return;

		final DTOJahrgang jahrgang = mapJahrgaenge.get(lernabschnittsdaten.Jahrgang_ID);
		if (jahrgang == null)
			throw new NullPointerException("Kein Jahrgang zu der ID %d gefunden.".formatted(lernabschnittsdaten.Jahrgang_ID));

		final LernplattformV1Jahrgang lernplattformJahrgang = new LernplattformV1Jahrgang();
		lernplattformJahrgang.id = jahrgang.ID;
		lernplattformJahrgang.kuerzelAnzeige = jahrgang.InternKrz;
		lernplattformJahrgang.kuerzel = jahrgang.ASDJahrgang;
		lernplattformJahrgang.bezeichnung = jahrgang.ASDBezeichnung;

		jahrgaengeToExport.put(lernplattformJahrgang.id, lernplattformJahrgang);
	}

	private void addKlasseToExport(final Map<Long, LernplattformV1Klasse> klassenToExport, final Map<Long, LernplattformV1Lehrer> lehrerToExport,
			final DTOSchuelerLernabschnittsdaten lernabschnittsdaten, final Map<Long, DTOKlassen> mapKlassen,
			final Map<Long, List<DTOKlassenLeitung>> mapKlassenLeitung, final Map<Long, DTOLehrer> mapLehrer,
			final Map<Long, DTOLehrerLernplattform> mapLehrerLernplattformen, final Map<Long, DTOCredentialsLernplattformen> mapLernplattformenCredentials) {
		// Prüfen, ob Klasse bereits im Export enthalten ist
		if (klassenToExport.containsKey(lernabschnittsdaten.Klassen_ID))
			return;

		final DTOKlassen dtoKlasse = mapKlassen.get(lernabschnittsdaten.Klassen_ID);
		if (dtoKlasse == null)
			throw new NullPointerException("Keine Klasse zu der ID %d gefunden.".formatted(lernabschnittsdaten.Klassen_ID));

		final LernplattformV1Klasse lernplattformKlasse = new LernplattformV1Klasse();
		lernplattformKlasse.id = dtoKlasse.ID;
		lernplattformKlasse.idJahrgang = dtoKlasse.Jahrgang_ID;
		lernplattformKlasse.kuerzel = dtoKlasse.ASDKlasse;
		lernplattformKlasse.kuerzelAnzeige = dtoKlasse.Klasse;

		addLehrerToKlasseAndExport(mapLehrer, lehrerToExport, mapKlassenLeitung, lernplattformKlasse, mapLehrerLernplattformen, mapLernplattformenCredentials);

		klassenToExport.put(dtoKlasse.ID, lernplattformKlasse);
	}

	private void addLehrerToKlasseAndExport(final Map<Long, DTOLehrer> mapLehrer, final Map<Long, LernplattformV1Lehrer> lehrerToExport,
			final Map<Long, List<DTOKlassenLeitung>> mapKlassenleitungen, final LernplattformV1Klasse lernplattformKlasse, final Map<Long,
					DTOLehrerLernplattform> mapLehrerLernplattformen, final Map<Long, DTOCredentialsLernplattformen> mapLernplattformenCredentials) {
		final List<DTOKlassenLeitung> klassenleitungen = mapKlassenleitungen.getOrDefault(lernplattformKlasse.id, Collections.emptyList());
		for (final DTOKlassenLeitung klassenleitungDto : klassenleitungen) {
			// prüfen, ob es den Lehrer überhaupt gibt
			if (!mapLehrer.containsKey(klassenleitungDto.Lehrer_ID))
				continue;

			final DTOLehrer lehrerDto = mapLehrer.get(klassenleitungDto.Lehrer_ID);
			final LernplattformV1Lehrer lernplattformLehrer = new LernplattformV1Lehrer();
			lernplattformLehrer.id = lehrerDto.ID;
			lernplattformLehrer.vorname = lehrerDto.Vorname;
			lernplattformLehrer.nachname = lehrerDto.Nachname;
			lernplattformLehrer.geschlecht = lehrerDto.Geschlecht.kuerzel;
			lernplattformLehrer.kuerzel = lehrerDto.Kuerzel;
			lernplattformLehrer.emailDienstlich = lehrerDto.eMailDienstlich;

			final DTOLehrerLernplattform dtoLehrerLernplattform = mapLehrerLernplattformen.get(lernplattformLehrer.id);
			final DTOCredentialsLernplattformen lehrerCredentials =
					(dtoLehrerLernplattform != null) ? mapLernplattformenCredentials.get(dtoLehrerLernplattform.CredentialID) : null;
			if (lehrerCredentials != null) {
				lernplattformLehrer.lernplattformlogin.benutzername = lehrerCredentials.Benutzername;
				lernplattformLehrer.lernplattformlogin.initialpasswort = lehrerCredentials.Initialkennwort;
			}

			lehrerToExport.put(lehrerDto.ID, lernplattformLehrer);
			lernplattformKlasse.idsKlassenlehrer.add(klassenleitungDto.Lehrer_ID);
		}
	}

	private DTOLernplattformen getLernplattform(final Long idLernplattform) throws ApiOperationException {
		final DTOLernplattformen dtoLernplattform = conn.queryByKey(DTOLernplattformen.class, idLernplattform);
		if (dtoLernplattform == null) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Lernplattform mit ID %s wurde nicht gefunden.".formatted(idLernplattform));
		}
		return dtoLernplattform;
	}

	private Map<Long, DTOKurs> getKurse(final Schuljahresabschnitt abschnitt) throws ApiOperationException {
		final List<DTOKurs> kurse = conn.queryList(DTOKurs.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKurs.class, abschnitt.id);
		if (kurse == null)
			throw new ApiOperationException(Response.Status.NOT_FOUND);
		return conn.queryAll(DTOKurs.class).stream().collect(Collectors.toMap(k -> k.ID, k -> k));
	}

	private Map<Long, List<DTOKlassenLeitung>> getKlassenleitungen(final Map<Long, DTOKlassen> mapKlassen) {
		if (mapKlassen.isEmpty())
			return new HashMap<>();
		return conn.queryList(DTOKlassenLeitung.QUERY_LIST_BY_KLASSEN_ID, DTOKlassenLeitung.class, mapKlassen.keySet()).stream()
				.collect(Collectors.groupingBy(kl -> kl.Klassen_ID));
	}

	private Map<Long, DTOLehrer> getLehrerListe() throws ApiOperationException {
		final List<DTOLehrer> lehrer = conn.queryAll(DTOLehrer.class);
		if (lehrer.isEmpty())
			throw new ApiOperationException(Response.Status.NOT_FOUND);
		return lehrer.stream().collect(Collectors.toMap(e -> e.ID, e -> e));
	}

	private Map<Long, DTOSchueler> getSchuelerListe(final Schuljahresabschnitt abschnitt)
			throws ApiOperationException {
		final List<DTOSchueler> schueler = conn.queryList(DTOSchueler.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOSchueler.class, abschnitt.id);
		if (schueler.isEmpty())
			throw new ApiOperationException(Response.Status.NOT_FOUND);
		return schueler.stream().filter(s -> (s.idStatus == SchuelerStatus.AKTIV.daten(abschnitt.schuljahr).id)
				|| (s.idStatus == SchuelerStatus.EXTERN.daten(abschnitt.schuljahr).id)).collect(Collectors.toMap(s -> s.ID, s -> s));
	}

	private Map<Long, DTOFach> getFaecherListe() throws ApiOperationException {
		final List<DTOFach> faecher = conn.queryAll(DTOFach.class);
		if (faecher.isEmpty())
			throw new ApiOperationException(Response.Status.NOT_FOUND);
		return faecher.stream().collect(Collectors.toMap(f -> f.ID, f -> f));
	}

	private Map<Long, DTOJahrgang> getJahrgangsListe() throws ApiOperationException {
		final List<DTOJahrgang> jahrgaenge = conn.queryAll(DTOJahrgang.class);
		if (jahrgaenge.isEmpty())
			throw new ApiOperationException(Response.Status.NOT_FOUND);
		return jahrgaenge.stream().collect(Collectors.toMap(j -> j.ID, j -> j));
	}

	private Map<Long, DTOKlassen> getKlassenListe(final Schuljahresabschnitt abschnitt) throws ApiOperationException {
		final List<DTOKlassen> klassen = conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, abschnitt.id);
		if (klassen.isEmpty())
			throw new ApiOperationException(Response.Status.NOT_FOUND);
		return klassen.stream().collect(Collectors.toMap(e -> e.ID, e -> e));
	}

}
