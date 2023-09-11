package de.svws_nrw.data.schueler;

import java.io.InputStream;
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
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;
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
		daten.anspruchsniveauId = dto.Anspruchsniveau.daten.id;
		daten.pruefungsdatum = dto.Pruefungsdatum;
		daten.ersetzteSprache = dto.ErsetzteSprache;
		daten.istHSUPruefung = (dto.IstHSUPruefung != null) && dto.IstHSUPruefung;
		daten.istFeststellungspruefung = (dto.IstFeststellungspruefung != null) && dto.IstFeststellungspruefung;
		daten.kannErstePflichtfremdspracheErsetzen = (dto.KannErstePflichtfremdspracheErsetzen != null) && dto.KannErstePflichtfremdspracheErsetzen;
		daten.kannZweitePflichtfremdspracheErsetzen = (dto.KannZweitePflichtfremdspracheErsetzen != null) && dto.KannZweitePflichtfremdspracheErsetzen;
		daten.kannWahlpflichtfremdspracheErsetzen = (dto.KannWahlpflichtfremdspracheErsetzen != null) && dto.KannWahlpflichtfremdspracheErsetzen;
		daten.kannBelegungAlsFortgefuehrteSpracheErlauben = (dto.KannBelegungAlsFortgefuehrteSpracheErlauben != null) && dto.KannBelegungAlsFortgefuehrteSpracheErlauben;
		daten.referenzniveau = dto.Referenzniveau.daten.kuerzel;
		daten.note = (dto.NotePruefung == null) ? null : dto.NotePruefung.getNoteSekI();
		return daten;
	};

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	private List<DTOSchuelerSprachpruefungen> getDTOs() {
		// Überprüfe, ob die Schüler-ID gültig ist.
    	final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, idSchueler);
    	if (schueler == null)
    		throw OperationError.NOT_FOUND.exception("Es wurde kein Schüler mit der ID %d gefunden.".formatted(idSchueler));
    	// Bestimme die Sprachprüfungen des Schülers
		return conn.queryNamed("DTOSchuelerSprachpruefungen.schueler_id", idSchueler, DTOSchuelerSprachpruefungen.class);
	}

	private List<Sprachpruefung> getSprachpruefungen() {
		return getDTOs().stream().map(dtoMapper).toList();
	}

	@Override
	public Response getList() {
		final List<Sprachpruefung> daten = getSprachpruefungen();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	private DTOSchuelerSprachpruefungen getDTO(final @NotNull String kuerzel) {
		if ((kuerzel == null) || (kuerzel.isBlank()))
			throw OperationError.NOT_FOUND.exception("Es wurde kein gültiges Kürzel übergeben.");
		// Überprüfe, ob die Schüler-ID gültig ist.
    	final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, idSchueler);
    	if (schueler == null)
    		throw OperationError.NOT_FOUND.exception("Es wurde kein Schüler mit der ID %d gefunden.".formatted(idSchueler));
    	// Bestimme die zugehörige Sprachprüfung
		final List<DTOSchuelerSprachpruefungen> belegungen = conn.queryList("SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.Schueler_ID = ?1 AND e.Sprache = ?2", DTOSchuelerSprachpruefungen.class, idSchueler, kuerzel);
		if (belegungen.isEmpty())
			throw OperationError.NOT_FOUND.exception("Keine Sprachprüfung mit dem Kürzel gefunden.");
		if (belegungen.size() > 1)
			throw OperationError.INTERNAL_SERVER_ERROR.exception("Es wurden mehrere Einträge zu dem Schüler mit der ID %d und der Sprache %s gefunden.".formatted(idSchueler, kuerzel));
		return belegungen.get(0);
	}

	private Sprachpruefung getSprachpruefung(final @NotNull String kuerzel) {
		return dtoMapper.apply(getDTO(kuerzel));
	}

	@Override
	public Response get(final @NotNull String kuerzel) {
		final Sprachpruefung daten = getSprachpruefung(kuerzel);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	private static final Map<String, DataBasicMapper<DTOSchuelerSprachpruefungen>> patchMappings = Map.ofEntries(
		Map.entry("sprache", (conn, dto, value, map) -> {
			final String patchSprache = JSONMapper.convertToString(value, false, false, 2);
			if ((patchSprache == null) || (patchSprache.isBlank()) || (!patchSprache.equals(dto.Sprache)))
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("jahrgang", (conn, dto, value, map) -> {
			final String kuerzel = JSONMapper.convertToString(value, true, false, 10);
			if (kuerzel == null) {
				dto.ASDJahrgang = null;
			} else {
				final Jahrgaenge jg = Jahrgaenge.getByKuerzel(kuerzel);
				if (jg == null)
					throw OperationError.BAD_REQUEST.exception("Ungültiges Jahrgangs-Kürzel verwendet.");
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
					throw OperationError.BAD_REQUEST.exception("Ungültiges Sprachprüfungsniveau-Kürzel verwendet.");
				dto.Anspruchsniveau = niveau;
			}
		}),
		Map.entry("pruefungsdatum", (conn, dto, value, map) -> dto.Pruefungsdatum = JSONMapper.convertToString(value, true, false, null)),
		Map.entry("ersetzteSprache", (conn, dto, value, map) -> dto.ErsetzteSprache = JSONMapper.convertToString(value, true, false, 2)),
		Map.entry("istHSUPruefung", (conn, dto, value, map) -> dto.IstHSUPruefung = JSONMapper.convertToBoolean(value, false)),
		Map.entry("istFeststellungspruefung", (conn, dto, value, map) -> dto.IstFeststellungspruefung = JSONMapper.convertToBoolean(value, false)),
		Map.entry("kannErstePflichtfremdspracheErsetzen", (conn, dto, value, map) -> dto.KannErstePflichtfremdspracheErsetzen = JSONMapper.convertToBoolean(value, false)),
		Map.entry("kannZweitePflichtfremdspracheErsetzen", (conn, dto, value, map) -> dto.KannZweitePflichtfremdspracheErsetzen = JSONMapper.convertToBoolean(value, false)),
		Map.entry("kannWahlpflichtfremdspracheErsetzen", (conn, dto, value, map) -> dto.KannWahlpflichtfremdspracheErsetzen = JSONMapper.convertToBoolean(value, false)),
		Map.entry("kannBelegungAlsFortgefuehrteSpracheErlauben", (conn, dto, value, map) -> dto.KannBelegungAlsFortgefuehrteSpracheErlauben = JSONMapper.convertToBoolean(value, false)),
		Map.entry("referenzniveau", (conn, dto, value, map) -> {
			final String kuerzel = JSONMapper.convertToString(value, true, false, 10);
			if (kuerzel == null) {
				dto.Referenzniveau = null;
			} else {
				final Sprachreferenzniveau niveau = Sprachreferenzniveau.getByKuerzel(kuerzel);
				if (niveau == null)
					throw OperationError.BAD_REQUEST.exception("Ungültiges Sprachreferenzniveau-Kürzel verwendet.");
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
					throw OperationError.BAD_REQUEST.exception("Ungültige Note angegeben.");
				dto.NotePruefung = notePruefung;
			}
		})
	);

	@Override
	public Response patch(final @NotNull String kuerzel, final InputStream is) {
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.isEmpty())
			return OperationError.NOT_FOUND.getResponse("In dem Patch sind keine Daten enthalten.");
		final DTOSchuelerSprachpruefungen dto = getDTO(kuerzel);
		if (dto == null)
			throw OperationError.NOT_FOUND.exception();
		applyPatchMappings(conn, dto, map, patchMappings, null);
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
	 */
	public Response add(final InputStream is) {
		// Prüfe, ob ein Schüler mit der idSchueler existiert und lade diesen
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (schueler == null)
			throw OperationError.NOT_FOUND.exception("Ein Schüler mit der ID %d ist nicht vorhanden.".formatted(idSchueler));
		// Prüfe, ob alle relevanten Attribute im JSON-Inputstream vorhanden sind
		final Map<String, Object> map = JSONMapper.toMap(is);
		for (final String attr : requiredCreateAttributes)
			if (!map.containsKey(attr))
				return OperationError.BAD_REQUEST.getResponse("Das Attribut %s fehlt in der Anfrage".formatted(attr));
		try {
			// Bestimme die nächste verfügbare ID
			final long newID = conn.transactionGetNextID(DTOSchuelerSprachpruefungen.class);
			final String sprache = JSONMapper.convertToString(map.get("sprache"), false, false, 2);
			if ((sprache == null) || (sprache.isBlank()))
				throw OperationError.BAD_REQUEST.exception();
			final DTOSchuelerSprachpruefungen dto = new DTOSchuelerSprachpruefungen(newID, idSchueler, sprache);
			applyPatchMappings(conn, dto, map, patchMappings, null);
			// Persistiere das DTO in der Datenbank
			if (!conn.transactionPersist(dto))
				throw OperationError.INTERNAL_SERVER_ERROR.exception();
			conn.transactionFlush();
			final Sprachpruefung daten = dtoMapper.apply(dto);
			return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (final Exception e) {
			if (e instanceof final WebApplicationException webAppException)
				return webAppException.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		}
	}


	/**
	 * Löscht eine Sprachprüfung
	 *
	 * @param kuerzel   das Kürzel der Sprache
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(final @NotNull String kuerzel) {
		// Bestimme das DTO
		final DTOSchuelerSprachpruefungen dto = getDTO(kuerzel);
		if (dto == null)
			throw OperationError.NOT_FOUND.exception();
		final Sprachpruefung daten = dtoMapper.apply(dto);
		// Entferne das DTO
		conn.transactionRemove(dto);
		conn.transactionFlush();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

}
