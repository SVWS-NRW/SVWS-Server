package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import de.svws_nrw.core.data.schueler.Sprachpruefung;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.fach.Sprachpruefungniveau;
import de.svws_nrw.core.types.fach.Sprachreferenzniveau;
import de.svws_nrw.core.types.jahrgang.Jahrgaenge;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachpruefungen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link Sprachpruefung}.
 */
public final class DataSchuelerSprachpruefung extends DataManager<String> {

	private final long idSchueler;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link Sprachpruefung}.
	 *
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idSchueler   die ID des Schülers
	 */
	public DataSchuelerSprachpruefung(final DBEntityManager conn, final long idSchueler) {
		super(conn);
		this.idSchueler = idSchueler;
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOSchuelerSprachpruefungen} in
	 * einen Core-DTO {@link Sprachpruefung}.
	 */
	private static final Function<DTOSchuelerSprachpruefungen, Sprachpruefung> dtoMapper = (final DTOSchuelerSprachpruefungen dto) -> {
		final Sprachpruefung daten = new Sprachpruefung();
		daten.sprache = dto.Sprache;
		daten.jahrgang = dto.ASDJahrgang;
		daten.anspruchsniveauId = (dto.Anspruchsniveau == null) ? null : dto.Anspruchsniveau.daten.id;
		daten.pruefungsdatum = dto.Pruefungsdatum;
		daten.istHSUPruefung = (dto.IstHSUPruefung != null) && dto.IstHSUPruefung;
		daten.istFeststellungspruefung = (dto.IstFeststellungspruefung != null) && dto.IstFeststellungspruefung;
		daten.kannErstePflichtfremdspracheErsetzen = (dto.KannErstePflichtfremdspracheErsetzen != null) && dto.KannErstePflichtfremdspracheErsetzen;
		daten.kannZweitePflichtfremdspracheErsetzen = (dto.KannZweitePflichtfremdspracheErsetzen != null) && dto.KannZweitePflichtfremdspracheErsetzen;
		daten.kannWahlpflichtfremdspracheErsetzen = (dto.KannWahlpflichtfremdspracheErsetzen != null) && dto.KannWahlpflichtfremdspracheErsetzen;
		daten.kannBelegungAlsFortgefuehrteSpracheErlauben =
				(dto.KannBelegungAlsFortgefuehrteSpracheErlauben != null) && dto.KannBelegungAlsFortgefuehrteSpracheErlauben;
		daten.referenzniveau = (dto.Referenzniveau == null) ? null : dto.Referenzniveau.daten.kuerzel;
		daten.note = (dto.NotePruefung == null) ? null : dto.NotePruefung.getNoteSekI();
		return daten;
	};

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	private List<DTOSchuelerSprachpruefungen> getDTOs() throws ApiOperationException {
		// Überprüfe, ob die Schüler-ID gültig ist.
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (schueler == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Schüler mit der ID %d gefunden.".formatted(idSchueler));
		// Bestimme die Sprachprüfungen des Schülers
		return conn.queryList(DTOSchuelerSprachpruefungen.QUERY_BY_SCHUELER_ID, DTOSchuelerSprachpruefungen.class, idSchueler);
	}

	private List<Sprachpruefung> getSprachpruefungen() throws ApiOperationException {
		return getDTOs().stream().map(dtoMapper).toList();
	}

	@Override
	public Response getList() throws ApiOperationException {
		final List<Sprachpruefung> daten = getSprachpruefungen();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	private DTOSchuelerSprachpruefungen getDTO(final @NotNull String kuerzel) throws ApiOperationException {
		if ((kuerzel == null) || (kuerzel.isBlank()))
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein gültiges Kürzel übergeben.");
		// Überprüfe, ob die Schüler-ID gültig ist.
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (schueler == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Schüler mit der ID %d gefunden.".formatted(idSchueler));
		// Bestimme die zugehörige Sprachprüfung
		final List<DTOSchuelerSprachpruefungen> belegungen = conn.queryList(
				"SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.Schueler_ID = ?1 AND e.Sprache = ?2", DTOSchuelerSprachpruefungen.class, idSchueler,
				kuerzel);
		if (belegungen.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Sprachprüfung mit dem Kürzel gefunden.");
		if (belegungen.size() > 1)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Es wurden mehrere Einträge zu dem Schüler mit der ID %d und der Sprache %s gefunden."
					.formatted(idSchueler, kuerzel));
		return belegungen.get(0);
	}

	private Sprachpruefung getSprachpruefung(final @NotNull String kuerzel) throws ApiOperationException {
		return dtoMapper.apply(getDTO(kuerzel));
	}

	@Override
	public Response get(final @NotNull String kuerzel) throws ApiOperationException {
		final Sprachpruefung daten = getSprachpruefung(kuerzel);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	private static final Map<String, DataBasicMapper<DTOSchuelerSprachpruefungen>> patchMappings = Map.ofEntries(
			Map.entry("sprache", (conn, dto, value, map) -> {
				final String patchSprache = JSONMapper.convertToString(value, false, false, 2);
				if ((patchSprache == null) || (patchSprache.isBlank()) || (!patchSprache.equals(dto.Sprache)))
					throw new ApiOperationException(Status.BAD_REQUEST);
			}),
			Map.entry("jahrgang", (conn, dto, value, map) -> {
				final String kuerzel = JSONMapper.convertToString(value, true, false, 10);
				if (kuerzel == null) {
					dto.ASDJahrgang = null;
				} else {
					final Jahrgaenge jg = Jahrgaenge.getByKuerzel(kuerzel);
					if (jg == null)
						throw new ApiOperationException(Status.BAD_REQUEST, "Ungültiges Jahrgangs-Kürzel verwendet.");
					dto.ASDJahrgang = jg.daten.kuerzel;
				}
			}),
			Map.entry("anspruchsniveauId", (conn, dto, value, map) -> {
				final Integer id = JSONMapper.convertToInteger(value, true);
				if (id == null) {
					dto.Anspruchsniveau = null;
				} else {
					final Sprachpruefungniveau niveau = Sprachpruefungniveau.getByID(id);
					if (niveau == null)
						throw new ApiOperationException(Status.BAD_REQUEST, "Ungültiges Sprachprüfungsniveau-Kürzel verwendet.");
					dto.Anspruchsniveau = niveau;
				}
			}),
			Map.entry("pruefungsdatum", (conn, dto, value, map) -> dto.Pruefungsdatum = JSONMapper.convertToString(value, true, false, null)),
			Map.entry("istHSUPruefung", (conn, dto, value, map) -> dto.IstHSUPruefung = JSONMapper.convertToBoolean(value, false)),
			Map.entry("istFeststellungspruefung", (conn, dto, value, map) -> dto.IstFeststellungspruefung = JSONMapper.convertToBoolean(value, false)),
			Map.entry("kannErstePflichtfremdspracheErsetzen",
					(conn, dto, value, map) -> dto.KannErstePflichtfremdspracheErsetzen = JSONMapper.convertToBoolean(value, false)),
			Map.entry("kannZweitePflichtfremdspracheErsetzen",
					(conn, dto, value, map) -> dto.KannZweitePflichtfremdspracheErsetzen = JSONMapper.convertToBoolean(value, false)),
			Map.entry("kannWahlpflichtfremdspracheErsetzen",
					(conn, dto, value, map) -> dto.KannWahlpflichtfremdspracheErsetzen = JSONMapper.convertToBoolean(value, false)),
			Map.entry("kannBelegungAlsFortgefuehrteSpracheErlauben",
					(conn, dto, value, map) -> dto.KannBelegungAlsFortgefuehrteSpracheErlauben = JSONMapper.convertToBoolean(value, false)),
			Map.entry("referenzniveau", (conn, dto, value, map) -> {
				final String kuerzel = JSONMapper.convertToString(value, true, false, 10);
				if (kuerzel == null) {
					dto.Referenzniveau = null;
				} else {
					final Sprachreferenzniveau niveau = Sprachreferenzniveau.getByKuerzel(kuerzel);
					if (niveau == null)
						throw new ApiOperationException(Status.BAD_REQUEST, "Ungültiges Sprachreferenzniveau-Kürzel verwendet.");
					dto.Referenzniveau = niveau;
				}
			}),
			Map.entry("note", (conn, dto, value, map) -> {
				final Integer note = JSONMapper.convertToIntegerInRange(value, true, 1, 6);
				if (note == null) {
					dto.NotePruefung = null;
				} else {
					final Note notePruefung = Note.fromNoteSekI(note);
					if (notePruefung == null)
						throw new ApiOperationException(Status.BAD_REQUEST, "Ungültige Note angegeben.");
					dto.NotePruefung = notePruefung;
				}
			}));

	@Override
	public Response patch(final @NotNull String kuerzel, final InputStream is) throws ApiOperationException {
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "In dem Patch sind keine Daten enthalten.");
		final DTOSchuelerSprachpruefungen dto = getDTO(kuerzel);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		applyPatchMappings(conn, dto, map, patchMappings, Collections.emptySet(), null);
		conn.transactionPersist(dto);
		conn.transactionFlush();
		return Response.status(Status.OK).build();
	}


	private static final Set<String> requiredCreateAttributes = Set.of("sprache");


	/**
	 * Fügt eine neue Sprachprüfung mit den übergebenen JSON-Daten der Datenbank hinzu und gibt das zugehörige Core-DTO
	 * zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response add(final InputStream is) throws ApiOperationException {
		// Prüfe, ob ein Schüler mit der idSchueler existiert und lade diesen
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (schueler == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Ein Schüler mit der ID %d ist nicht vorhanden.".formatted(idSchueler));
		// Prüfe, ob alle relevanten Attribute im JSON-Inputstream vorhanden sind
		final Map<String, Object> map = JSONMapper.toMap(is);
		for (final String attr : requiredCreateAttributes)
			if (!map.containsKey(attr))
				throw new ApiOperationException(Status.BAD_REQUEST, "Das Attribut %s fehlt in der Anfrage".formatted(attr));
		try {
			// Bestimme die nächste verfügbare ID
			final long newID = conn.transactionGetNextID(DTOSchuelerSprachpruefungen.class);
			final String sprache = JSONMapper.convertToString(map.get("sprache"), false, false, 2);
			if ((sprache == null) || (sprache.isBlank()))
				throw new ApiOperationException(Status.BAD_REQUEST);
			final DTOSchuelerSprachpruefungen dto = new DTOSchuelerSprachpruefungen(newID, idSchueler, sprache);
			applyPatchMappings(conn, dto, map, patchMappings, Collections.emptySet(), null);
			// Persistiere das DTO in der Datenbank
			if (!conn.transactionPersist(dto))
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
			conn.transactionFlush();
			final Sprachpruefung daten = dtoMapper.apply(dto);
			return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (final Exception e) {
			if (e instanceof final ApiOperationException aoe)
				throw aoe;
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
		}
	}


	/**
	 * Löscht eine Sprachprüfung
	 *
	 * @param kuerzel   das Kürzel der Sprache
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response delete(final @NotNull String kuerzel) throws ApiOperationException {
		// Bestimme das DTO
		final DTOSchuelerSprachpruefungen dto = getDTO(kuerzel);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final Sprachpruefung daten = dtoMapper.apply(dto);
		// Entferne das DTO
		conn.transactionRemove(dto);
		conn.transactionFlush();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

}
