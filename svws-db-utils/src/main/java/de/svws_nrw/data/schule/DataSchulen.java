package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;

import de.svws_nrw.core.data.kataloge.SchulEintrag;
import de.svws_nrw.core.data.schule.SchulformKatalogEintrag;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchulEintrag}.
 */
public final class DataSchulen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchulEintrag}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchulen(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOSchuleNRW} in einen Core-DTO {@link SchulEintrag}.
	 */
	private static final Function<DTOSchuleNRW, SchulEintrag> dtoMapper = (final DTOSchuleNRW e) -> {
		final SchulEintrag daten = new SchulEintrag();
		daten.id = e.ID;
		daten.kuerzel = e.Kuerzel;
		daten.kurzbezeichnung = e.KurzBez;
		daten.schulnummer = e.SchulNr;
		daten.name = e.Name;
		daten.schulformID = (e.SchulformNr == null) ? null : Schulform.getByNummer(e.SchulformNr).daten.id;
		daten.strassenname = e.Strassenname;
		daten.hausnummer = e.HausNr;
		daten.hausnummerZusatz = e.HausNrZusatz;
		daten.plz = e.PLZ;
		daten.ort = e.Ort;
		daten.telefon = e.Telefon;
		daten.fax = e.Fax;
		daten.email = e.Email;
		daten.schulleiter = e.Schulleiter;
		daten.sortierung = e.Sortierung;
		daten.istSichtbar = e.Sichtbar;
		daten.istAenderbar = e.Aenderbar;
		return daten;
	};


	@Override
	public Response getAll() {
		return this.getList();
	}


	/**
	 * Gibt die Einträge im Katalog der Schulen zurück.
	 *
	 * @param conn   die Datenbankverbindung
	 *
	 * @return die Liste der Schulen
	 */
	public static List<SchulEintrag> getSchulen(final @NotNull DBEntityManager conn) {
		final List<DTOSchuleNRW> eintraege = conn.queryAll(DTOSchuleNRW.class);
		final ArrayList<SchulEintrag> daten = new ArrayList<>();
		for (final DTOSchuleNRW e : eintraege)
			daten.add(dtoMapper.apply(e));
		return daten;
	}


	/**
	 * Gibt die Einträge im Katalog der Schulen zurück, welche ein Kürzel gesetzt haben.
	 *
	 * @param conn   die Datenbankverbindung
	 *
	 * @return die Liste der Schulen, welche ein Kürzel gesetzt haben
	 */
	public static List<SchulEintrag> getSchulenMitKuerzel(final @NotNull DBEntityManager conn) {
		final List<DTOSchuleNRW> eintraege = conn.queryAll(DTOSchuleNRW.class);
		final ArrayList<SchulEintrag> daten = new ArrayList<>();
		for (final DTOSchuleNRW e : eintraege)
			if ((e.Kuerzel != null) && (!e.Kuerzel.isBlank()))
				daten.add(dtoMapper.apply(e));
		return daten;
	}


	@Override
	public Response getList() {
		final List<SchulEintrag> daten = getSchulen(conn);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einem Eintrag im Katalog der Schulen mit der ID null ist unzulässig.");
		final DTOSchuleNRW schule = conn.queryByKey(DTOSchuleNRW.class, id);
		if (schule == null)
			return OperationError.NOT_FOUND.getResponse("Es wurde kein Eintrag im Katalog der Schulen mit der ID %d gefunden.".formatted(id));
		final SchulEintrag daten = dtoMapper.apply(schule);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static final Map<String, DataBasicMapper<DTOSchuleNRW>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID))
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("kuerzel", (conn, dto, value, map) -> dto.Kuerzel = JSONMapper.convertToString(value, true, false, 10)),
		Map.entry("kurzbezeichnung", (conn, dto, value, map) -> dto.KurzBez = JSONMapper.convertToString(value, true, false, 40)),
		Map.entry("name", (conn, dto, value, map) -> dto.Name = JSONMapper.convertToString(value, false, false, 120)),
		Map.entry("schulformID", (conn, dto, value, map) -> {
			final Long id = JSONMapper.convertToLong(value, true);
			if (id == null) {
				dto.SchulformBez = null;
				dto.SchulformKrz = null;
				dto.SchulformNr = null;
			} else {
				final SchulformKatalogEintrag sf = Schulform.getEintragByID(id);
				if (sf == null)
					throw OperationError.BAD_REQUEST.exception();
				dto.SchulformBez = sf.bezeichnung;
				dto.SchulformKrz = sf.kuerzel;
				dto.SchulformNr = sf.nummer;
			}
		}),
		Map.entry("strassenname", (conn, dto, value, map) -> dto.Strassenname = JSONMapper.convertToString(value, false, true, 55)),
		Map.entry("hausnummer", (conn, dto, value, map) -> dto.HausNr = JSONMapper.convertToString(value, false, true, 10)),
		Map.entry("hausnummerZusatz", (conn, dto, value, map) -> dto.HausNrZusatz = JSONMapper.convertToString(value, false, true, 30)),
		Map.entry("plz", (conn, dto, value, map) -> dto.PLZ = JSONMapper.convertToString(value, false, true, 10)),
		Map.entry("ort", (conn, dto, value, map) -> dto.Ort = JSONMapper.convertToString(value, false, true, 50)),
		Map.entry("telefon", (conn, dto, value, map) -> dto.Telefon = JSONMapper.convertToString(value, false, true, 20)),
		Map.entry("fax", (conn, dto, value, map) -> dto.Fax = JSONMapper.convertToString(value, false, true, 20)),
		Map.entry("email", (conn, dto, value, map) -> dto.Email = JSONMapper.convertToString(value, false, true, 40)),
		Map.entry("schulleiter", (conn, dto, value, map) -> dto.Schulleiter = JSONMapper.convertToString(value, false, true, 40)),
		Map.entry("sortierung", (conn, dto, value, map) -> dto.Sortierung = JSONMapper.convertToInteger(value, false)),
		Map.entry("istSichtbar", (conn, dto, value, map) -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false)),
		Map.entry("istAenderbar", (conn, dto, value, map) -> dto.Aenderbar = JSONMapper.convertToBoolean(value, false))
	);


	@Override
	public Response patch(final Long id, final InputStream is) {
		return super.patchBasic(id, is, DTOSchuleNRW.class, patchMappings);
	}


	private static final Set<String> requiredCreateAttributes = Set.of("kuerzel", "groesse");

	private final ObjLongConsumer<DTOSchuleNRW> initDTO = (dto, id) -> dto.ID = id;

	/**
	 * Fügt ein Schul-Katalog-Eintrag mit den übergebenen JSON-Daten der Datenbank hinzu und gibt das
	 * zugehörige CoreDTO zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code
	 * zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 */
	public Response add(final InputStream is) {
		return super.addBasic(is, DTOSchuleNRW.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Löscht einen Schul-Katalog-Eintrag
	 *
	 * @param id   die ID des Schul-Katalog-Eintrag
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(final Long id) {
		return super.deleteBasic(id, DTOSchuleNRW.class, dtoMapper);
	}

}
