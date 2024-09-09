package de.svws_nrw.data.faecher;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.ObjLongConsumer;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link FachDaten}.
 */
public final class DataFachdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link FachDaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataFachdaten(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOFach} in einen Core-DTO {@link FachDaten}.
	 */
	private final DTOMapper<DTOFach, FachDaten> dtoMapperFach = (final DTOFach f) -> {
		final FachDaten daten = new FachDaten();
		daten.id = f.ID;
		daten.kuerzel = (f.Kuerzel == null) ? "" : f.Kuerzel;
		daten.kuerzelStatistik = f.StatistikKuerzel;
		daten.bezeichnung = (f.Bezeichnung == null) ? "" : f.Bezeichnung;
		daten.istOberstufenFach = f.IstOberstufenFach;
		daten.istPruefungsordnungsRelevant = f.IstPruefungsordnungsRelevant;
		daten.sortierung = f.SortierungAllg;
		daten.istSichtbar = f.Sichtbar;
		daten.aufgabenfeld = f.Aufgabenfeld;
		daten.bilingualeSprache = f.Unterichtssprache;
		daten.istNachpruefungErlaubt = f.IstNachpruefungErlaubt;
		daten.aufZeugnis = f.AufZeugnis;
		daten.bezeichnungZeugnis = (f.BezeichnungZeugnis == null) ? "" : f.BezeichnungZeugnis;
		daten.bezeichnungUeberweisungszeugnis = (f.BezeichnungUeberweisungsZeugnis == null) ? "" : f.BezeichnungUeberweisungsZeugnis;
		daten.maxZeichenInFachbemerkungen = (f.MaxBemZeichen == null) ? Integer.MAX_VALUE : f.MaxBemZeichen;
		daten.istSchriftlichZK = f.IstSchriftlichZK;
		daten.istSchriftlichBA = f.IstSchriftlichBA;
		daten.holeAusAltenLernabschnitten = f.AbgeschlFaecherHolen;
		daten.istFHRFach = ((f.GewichtungFHR != null) && (f.GewichtungFHR != 0));
		return daten;
	};

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
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final DTOFach fach = conn.queryByKey(DTOFach.class, id);
		if (fach == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final FachDaten daten = dtoMapperFach.apply(fach);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Erstellt eine Map, die alle Fächer der DB als Fachdaten-Objekte zur Fach-ID enthält.
	 *
	 * @return Map der Fachdaten zur Fach-ID.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Map<Long, FachDaten> getFaecherdaten() throws ApiOperationException {
		final Map<Long, FachDaten> mapFaecher = new HashMap<>();
		for (final DTOFach f : conn.queryAll(DTOFach.class))
			mapFaecher.put(f.ID, dtoMapperFach.apply(f));
		return mapFaecher;
	}

	/**
	 * Erstellt eine Map, die die GOSt-Daten aller Fach-Einträge der DB als GostFach-Objekte zur Fach-ID enthält.
	 *
	 * @param schuljahr   das Schuljahr, für welches die Daten bestimmt werden sollen
	 *
	 * @return Map der GOSt-Daten aller Fächer der DB zur Fach-ID.
	 */
	public Map<Long, GostFach> getFaecherGostdaten(final int schuljahr) {
		final Map<Long, DTOFach> faecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		return faecher.values().stream().collect(Collectors.toMap(f -> f.ID, f -> DBUtilsFaecherGost.mapFromDTOFach(schuljahr, f, faecher)));
	}


	private static final Map<String, DataBasicMapper<DTOFach>> patchMappings = Map.ofEntries(
			Map.entry("id", (conn, dto, value, map) -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id.longValue() != dto.ID))
					throw new ApiOperationException(Status.BAD_REQUEST);
			}),
			Map.entry("kuerzel", (conn, dto, value, map) -> dto.Kuerzel = JSONMapper.convertToString(value, false, false, 20)),
			Map.entry("kuerzelStatistik", (conn, dto, value, map) -> {
				final String fachKuerzel = JSONMapper.convertToString(value, false, false, 2);
				final Fach f = Fach.data().getWertBySchluessel(fachKuerzel);
				if (f == null)
					throw new ApiOperationException(Status.BAD_REQUEST);
				dto.StatistikKuerzel = fachKuerzel;
			}),
			Map.entry("bezeichnung", (conn, dto, value, map) -> dto.Bezeichnung = JSONMapper.convertToString(value, false, true, 255)),
			Map.entry("istPruefungsordnungsRelevant", (conn, dto, value, map) -> dto.IstPruefungsordnungsRelevant = JSONMapper.convertToBoolean(value, false)),
			Map.entry("istOberstufenFach", (conn, dto, value, map) -> dto.IstOberstufenFach = JSONMapper.convertToBoolean(value, false)),
			Map.entry("sortierung", (conn, dto, value, map) -> dto.SortierungAllg = JSONMapper.convertToIntegerInRange(value, false, 0, Integer.MAX_VALUE)),
			Map.entry("istSichtbar", (conn, dto, value, map) -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false)),
			Map.entry("aufgabenfeld", (conn, dto, value, map) -> dto.Aufgabenfeld = JSONMapper.convertToString(value, true, true, 2)),
			Map.entry("bilingualeSprache", (conn, dto, value, map) -> dto.Unterichtssprache = JSONMapper.convertToString(value, true, true, 1)),
			Map.entry("istNachpruefungErlaubt", (conn, dto, value, map) -> dto.IstNachpruefungErlaubt = JSONMapper.convertToBoolean(value, false)),
			Map.entry("aufZeugnis", (conn, dto, value, map) -> dto.AufZeugnis = JSONMapper.convertToBoolean(value, false)),
			Map.entry("bezeichnungZeugnis", (conn, dto, value, map) -> dto.BezeichnungZeugnis = JSONMapper.convertToString(value, false, true, 255)),
			Map.entry("bezeichnungUeberweisungszeugnis",
					(conn, dto, value, map) -> dto.BezeichnungUeberweisungsZeugnis = JSONMapper.convertToString(value, false, true, 255)),
			Map.entry("maxZeichenInFachbemerkungen", (conn, dto, value, map) -> dto.MaxBemZeichen = JSONMapper.convertToIntegerInRange(value, false, 1, null)),
			Map.entry("istSchriftlichZK", (conn, dto, value, map) -> dto.IstSchriftlichZK = JSONMapper.convertToBoolean(value, false)),
			Map.entry("istSchriftlichBA", (conn, dto, value, map) -> dto.IstSchriftlichBA = JSONMapper.convertToBoolean(value, false)),
			Map.entry("holeAusAltenLernabschnitten", (conn, dto, value, map) -> dto.AbgeschlFaecherHolen = JSONMapper.convertToBoolean(value, false)),
			Map.entry("istFHRFach", (conn, dto, value, map) -> dto.GewichtungFHR = Boolean.TRUE.equals(JSONMapper.convertToBoolean(value, false)) ? 1 : 0));

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOFach.class, patchMappings);
	}

	private static final Set<String> requiredCreateAttributes = Set.of("kuerzel", "kuerzelStatistik");

	/**
	 * Fügt ein Fach mit den übergebenen JSON-Daten der Datenbank hinzu und gibt das zugehörige CoreDTO
	 * zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response add(final InputStream is) throws ApiOperationException {
		// füge den Raum in der Datenbank hinzu und gebe das zugehörige CoreDTO zurück.
		final ObjLongConsumer<DTOFach> initDTO = (dto, id) -> dto.ID = id;
		return super.addBasic(is, DTOFach.class, initDTO, dtoMapperFach, requiredCreateAttributes, patchMappings);
	}

	/**
	 * Löscht ein Fach
	 *
	 * @param id   die ID des Faches
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(final Long id) {
		throw new UnsupportedOperationException("Diese Funktion wird zur Zeit noch nicht unterstützt.");
	}

}
