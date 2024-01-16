package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import de.svws_nrw.core.data.schueler.Sprachbelegung;
import de.svws_nrw.core.types.fach.Sprachreferenzniveau;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.types.jahrgang.Jahrgaenge;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachenfolge;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link Sprachbelegung}.
 */
public final class DataSchuelerSprachbelegung extends DataManager<String> {

	private final long idSchueler;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link Sprachbelegung}.
	 *
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idSchueler   die ID des Schülers
	 */
	public DataSchuelerSprachbelegung(final DBEntityManager conn, final long idSchueler) {
		super(conn);
		this.idSchueler = idSchueler;
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOSchuelerSprachenfolge} in
	 * einen Core-DTO {@link Sprachbelegung}.
	 */
	private static final Function<DTOSchuelerSprachenfolge, Sprachbelegung> dtoMapper = (final DTOSchuelerSprachenfolge dto) -> {
		final Sprachbelegung daten = new Sprachbelegung();
		daten.sprache = dto.Sprache;
		daten.reihenfolge = dto.ReihenfolgeNr;
		daten.belegungVonJahrgang = dto.ASDJahrgangVon;
		daten.belegungVonAbschnitt = dto.AbschnittVon;
		daten.belegungBisJahrgang = dto.ASDJahrgangBis;
		daten.belegungBisAbschnitt = dto.AbschnittBis;
		daten.referenzniveau = (dto.Referenzniveau == null) ? null : dto.Referenzniveau.daten.kuerzel;
		daten.hatKleinesLatinum = (dto.KleinesLatinumErreicht != null) && dto.KleinesLatinumErreicht;
		daten.hatLatinum = (dto.LatinumErreicht != null) && dto.LatinumErreicht;
		daten.hatGraecum = (dto.GraecumErreicht != null) && dto.GraecumErreicht;
		daten.hatHebraicum = (dto.HebraicumErreicht != null) && dto.HebraicumErreicht;
		return daten;
	};

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	private List<DTOSchuelerSprachenfolge> getDTOs() {
		// Überprüfe, ob die Schüler-ID gültig ist.
    	final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, idSchueler);
    	if (schueler == null)
    		throw OperationError.NOT_FOUND.exception("Es wurde kein Schüler mit der ID %d gefunden.".formatted(idSchueler));
    	// Bestimme die Sprachbelegungen des Schülers
		return conn.queryNamed("DTOSchuelerSprachenfolge.schueler_id", idSchueler, DTOSchuelerSprachenfolge.class);
	}

	private List<Sprachbelegung> getSprachbelegungen() {
		return getDTOs().stream().map(dtoMapper).toList();
	}

	@Override
	public Response getList() {
		final List<Sprachbelegung> daten = getSprachbelegungen();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	private DTOSchuelerSprachenfolge getDTO(final @NotNull String kuerzel) {
		if ((kuerzel == null) || (kuerzel.isBlank()))
			throw OperationError.NOT_FOUND.exception("Es wurde kein gültiges Kürzel übergeben.");
		// Überprüfe, ob die Schüler-ID gültig ist.
    	final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, idSchueler);
    	if (schueler == null)
    		throw OperationError.NOT_FOUND.exception("Es wurde kein Schüler mit der ID %d gefunden.".formatted(idSchueler));
    	// Bestimme die zugehörige Sprachbelegung
		final List<DTOSchuelerSprachenfolge> belegungen = conn.queryList("SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.Schueler_ID = ?1 AND e.Sprache = ?2", DTOSchuelerSprachenfolge.class, idSchueler, kuerzel);
		if (belegungen.isEmpty())
			throw OperationError.NOT_FOUND.exception("Keine Sprachbelegung mit dem Kürzel gefunden.");
		if (belegungen.size() > 1)
			throw OperationError.INTERNAL_SERVER_ERROR.exception("Es wurden mehrere Einträge zu dem Schüler mit der ID %d und der Sprache %s gefunden.".formatted(idSchueler, kuerzel));
		return belegungen.get(0);
	}

	private Sprachbelegung getSprachbelegung(final @NotNull String kuerzel) {
		return dtoMapper.apply(getDTO(kuerzel));
	}

	@Override
	public Response get(final @NotNull String kuerzel) {
		final Sprachbelegung daten = getSprachbelegung(kuerzel);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	private static final Map<String, DataBasicMapper<DTOSchuelerSprachenfolge>> patchMappings = Map.ofEntries(
		Map.entry("sprache", (conn, dto, value, map) -> {
			final String patchSprache = JSONMapper.convertToString(value, false, false, 2);
			if ((patchSprache == null) || (patchSprache.isBlank()))
				throw OperationError.BAD_REQUEST.exception();
			if (!patchSprache.equals(dto.Sprache)) {
				if (!ZulaessigesFach.getListFremdsprachenKuerzelAtomar().contains(patchSprache))
					throw OperationError.BAD_REQUEST.exception();
				dto.Sprache = patchSprache;
			}
		}),
		Map.entry("reihenfolge", (conn, dto, value, map) -> dto.ReihenfolgeNr = JSONMapper.convertToIntegerInRange(value, true, 0, 9)),
		Map.entry("belegungVonJahrgang", (conn, dto, value, map) -> {
			final String kuerzel = JSONMapper.convertToString(value, true, false, 10);
			if (kuerzel == null) {
				dto.ASDJahrgangVon = null;
			} else {
				final Jahrgaenge jg = Jahrgaenge.getByKuerzel(kuerzel);
				if (jg == null)
					throw OperationError.BAD_REQUEST.exception("Ungültiges Jahrgangs-Kürzel verwendet.");
				dto.ASDJahrgangVon = jg.daten.kuerzel;
			}
		}),
		Map.entry("belegungVonAbschnitt", (conn, dto, value, map) -> {
			final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
			if (schule == null)
				throw OperationError.INTERNAL_SERVER_ERROR.exception("Die Daten der Schule konnten nicht bestimmt werden.");
			dto.AbschnittVon = JSONMapper.convertToIntegerInRange(value, true, 1, (schule.AnzahlAbschnitte == null ? 2 : schule.AnzahlAbschnitte) + 1);
		}),
		Map.entry("belegungBisJahrgang", (conn, dto, value, map) -> {
			final String kuerzel = JSONMapper.convertToString(value, true, false, 10);
			if (kuerzel == null) {
				dto.ASDJahrgangBis = null;
			} else {
				final Jahrgaenge jg = Jahrgaenge.getByKuerzel(kuerzel);
				if (jg == null)
					throw OperationError.BAD_REQUEST.exception("Ungültiges Jahrgangs-Kürzel verwendet.");
				dto.ASDJahrgangBis = jg.daten.kuerzel;
			}
		}),
		Map.entry("belegungBisAbschnitt", (conn, dto, value, map) -> {
			final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
			if (schule == null)
				throw OperationError.INTERNAL_SERVER_ERROR.exception("Die Daten der Schule konnten nicht bestimmt werden.");
			dto.AbschnittBis = JSONMapper.convertToIntegerInRange(value, true, 1, (schule.AnzahlAbschnitte == null ? 2 : schule.AnzahlAbschnitte) + 1);
		}),
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
		})
	);

	@Override
	public Response patch(final @NotNull String kuerzel, final InputStream is) {
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.isEmpty())
			return OperationError.NOT_FOUND.getResponse("In dem Patch sind keine Daten enthalten.");
		final DTOSchuelerSprachenfolge dto = getDTO(kuerzel);
		if (dto == null)
			throw OperationError.NOT_FOUND.exception();
		applyPatchMappings(conn, dto, map, patchMappings, null);
		conn.transactionPersist(dto);
		conn.transactionFlush();
		return Response.status(Status.OK).build();
	}


	private static final Set<String> requiredCreateAttributes = Set.of("sprache");


	/**
	 * Fügt eine neue Sprachbelegung mit den übergebenen JSON-Daten der Datenbank hinzu und gibt das zugehörige CoreDTO
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
			final long newID = conn.transactionGetNextID(DTOSchuelerSprachenfolge.class);
			final String sprache = JSONMapper.convertToString(map.get("sprache"), false, false, 2);
			if ((sprache == null) || (sprache.isBlank()))
				throw OperationError.BAD_REQUEST.exception();
			if (!ZulaessigesFach.getListFremdsprachenKuerzelAtomar().contains(sprache))
				throw OperationError.BAD_REQUEST.exception();
			final DTOSchuelerSprachenfolge dto = new DTOSchuelerSprachenfolge(newID, idSchueler, sprache);
			applyPatchMappings(conn, dto, map, patchMappings, null);
			// Persistiere das DTO in der Datenbank
			if (!conn.transactionPersist(dto))
				throw OperationError.INTERNAL_SERVER_ERROR.exception();
			conn.transactionFlush();
			final Sprachbelegung daten = dtoMapper.apply(dto);
			return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (final Exception e) {
			if (e instanceof final WebApplicationException webAppException)
				return webAppException.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		}
	}


	/**
	 * Löscht eine Sprachbelegung
	 *
	 * @param kuerzel   das Kürzel der Sprache
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(final @NotNull String kuerzel) {
		// Bestimme das DTO
		final DTOSchuelerSprachenfolge dto = getDTO(kuerzel);
		if (dto == null)
			throw OperationError.NOT_FOUND.exception();
		final Sprachbelegung daten = dtoMapper.apply(dto);
		// Entferne das DTO
		conn.transactionRemove(dto);
		conn.transactionFlush();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

}
