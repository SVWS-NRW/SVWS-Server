package de.svws_nrw.data.faecher;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;

import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.utils.OperationError;
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
	private final Function<DTOFach, FachDaten> dtoMapperFach = (final DTOFach f) -> {
		final FachDaten daten = new FachDaten();
		daten.id = f.ID;
		daten.kuerzel = (f.Kuerzel == null) ? "" : f.Kuerzel;
		daten.kuerzelStatistik = f.StatistikFach.daten.kuerzelASD;
		daten.bezeichnung = (f.Bezeichnung == null) ? "" : f.Bezeichnung;
		daten.istOberstufenFach = f.IstOberstufenFach;
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
	public Response get(final Long id) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
    	final DTOFach fach = conn.queryByKey(DTOFach.class, id);
    	if (fach == null)
    		return OperationError.NOT_FOUND.getResponse();
		final FachDaten daten = dtoMapperFach.apply(fach);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	private static final Map<String, DataBasicMapper<DTOFach>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID))
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("kuerzel", (conn, dto, value, map) -> dto.Kuerzel = JSONMapper.convertToString(value, false, false, 20)),
		Map.entry("kuerzelStatistik", (conn, dto, value, map) -> {
			final ZulaessigesFach f = ZulaessigesFach.getByKuerzelASD(JSONMapper.convertToString(value, false, false, 2));
			if (f == null)
				throw OperationError.BAD_REQUEST.exception();
			dto.StatistikFach = f;
		}),
		Map.entry("bezeichnung", (conn, dto, value, map) -> dto.Bezeichnung = JSONMapper.convertToString(value, false, true, 255)),
		Map.entry("istOberstufenFach", (conn, dto, value, map) -> dto.IstOberstufenFach = JSONMapper.convertToBoolean(value, false)),
		Map.entry("sortierung", (conn, dto, value, map) -> dto.SortierungAllg = JSONMapper.convertToIntegerInRange(value, false, 0, Integer.MAX_VALUE)),
		Map.entry("istSichtbar", (conn, dto, value, map) -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false)),
		Map.entry("aufgabenfeld", (conn, dto, value, map) -> dto.Aufgabenfeld = JSONMapper.convertToString(value, true, true, 2)),
		Map.entry("bilingualeSprache", (conn, dto, value, map) -> dto.Unterichtssprache = JSONMapper.convertToString(value, true, true, 1)),
		Map.entry("istNachpruefungErlaubt", (conn, dto, value, map) -> dto.IstNachpruefungErlaubt = JSONMapper.convertToBoolean(value, false)),
		Map.entry("aufZeugnis", (conn, dto, value, map) -> dto.AufZeugnis = JSONMapper.convertToBoolean(value, false)),
		Map.entry("bezeichnungZeugnis", (conn, dto, value, map) -> dto.BezeichnungZeugnis = JSONMapper.convertToString(value, false, true, 255)),
		Map.entry("bezeichnungUeberweisungszeugnis", (conn, dto, value, map) -> dto.BezeichnungUeberweisungsZeugnis = JSONMapper.convertToString(value, false, true, 255)),
		Map.entry("maxZeichenInFachbemerkungen", (conn, dto, value, map) -> dto.MaxBemZeichen = JSONMapper.convertToIntegerInRange(value, false, 0, Integer.MAX_VALUE)),
		Map.entry("istSchriftlichZK", (conn, dto, value, map) -> dto.IstSchriftlichZK = JSONMapper.convertToBoolean(value, false)),
		Map.entry("istSchriftlichBA", (conn, dto, value, map) -> dto.IstSchriftlichBA = JSONMapper.convertToBoolean(value, false)),
		Map.entry("holeAusAltenLernabschnitten", (conn, dto, value, map) -> dto.AbgeschlFaecherHolen = JSONMapper.convertToBoolean(value, false)),
		Map.entry("istFHRFach", (conn, dto, value, map) -> dto.GewichtungFHR = Boolean.TRUE.equals(JSONMapper.convertToBoolean(value, false)) ? 1 : 0)
	);

	@Override
	public Response patch(final Long id, final InputStream is) {
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
	 */
	public Response add(final InputStream is) {
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
