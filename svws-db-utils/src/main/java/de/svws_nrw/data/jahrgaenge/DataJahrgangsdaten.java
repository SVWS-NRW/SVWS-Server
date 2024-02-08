package de.svws_nrw.data.jahrgaenge;

import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.types.jahrgang.Jahrgaenge;
import de.svws_nrw.core.types.schule.Schulgliederung;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link JahrgangsDaten}.
 */
public final class DataJahrgangsdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link JahrgangsDaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataJahrgangsdaten(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOJahrgang} in einen Core-DTO {@link JahrgangsDaten}.
	 */
	private final Function<DTOJahrgang, JahrgangsDaten> dtoMapper = (final DTOJahrgang jahrgang) -> {
		final JahrgangsDaten daten = new JahrgangsDaten();
		daten.id = jahrgang.ID;
		daten.kuerzel = jahrgang.InternKrz;
		daten.kuerzelStatistik = jahrgang.ASDJahrgang;
		daten.bezeichnung = jahrgang.ASDBezeichnung;
		daten.kuerzelSchulgliederung = jahrgang.Gliederung.daten.kuerzel;
		daten.idFolgejahrgang = jahrgang.Folgejahrgang_ID;
		daten.anzahlRestabschnitte = jahrgang.AnzahlRestabschnitte;
		daten.sortierung = jahrgang.Sortierung;
		daten.istSichtbar = jahrgang.Sichtbar;
		daten.gueltigVon = jahrgang.GueltigVon;
		daten.gueltigBis = jahrgang.GueltigBis;
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
		final JahrgangsDaten daten = getFromID(id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Gibt die Jahrgangsdaten zur ID eines Jahrgangs zurück.
	 *
 	 * @param id	Die ID des Jahrgangs.
	 * @return		Die Jahrgangsdaten zur ID.
	 */
	public JahrgangsDaten getFromID(final Long id) {
		if (id == null)
			throw OperationError.NOT_FOUND.exception("Keine ID für den Jahrgang übergeben.");
		final DTOJahrgang jahrgang = conn.queryByKey(DTOJahrgang.class, id);
		if (jahrgang == null)
			throw OperationError.NOT_FOUND.exception("Kein Jahrgang zur ID " + id + " gefunden.");

		return dtoMapper.apply(jahrgang);
	}

	/**
	 * Gibt die Jahrgangsdaten der Schule zurück.
	 *
	 * @return		Die Jahrgangsdaten der Schule.
	 */
	public List<JahrgangsDaten> getJahrgaenge() {
		final List<DTOJahrgang> jahrgang = conn.queryAll(DTOJahrgang.class);

		return jahrgang.stream().map(dtoMapper).toList();
	}

	private static final Map<String, DataBasicMapper<DTOJahrgang>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID))
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("kuerzel", (conn, dto, value, map) -> dto.InternKrz = JSONMapper.convertToString(value, true, true, 20)),
		Map.entry("kuerzelStatistik", (conn, dto, value, map) -> {
			final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
			final String strJahrgang = JSONMapper.convertToString(value, true, false, 2);
			final Jahrgaenge jahrgang = (strJahrgang == null) ? null : Jahrgaenge.getByKuerzel(strJahrgang);
			if ((jahrgang == null) && (strJahrgang != null))
				throw OperationError.CONFLICT.exception();
			dto.ASDJahrgang = (jahrgang == null) ? null : jahrgang.daten.kuerzel;
			dto.ASDBezeichnung = (jahrgang == null) ? null : jahrgang.getBezeichnung(schule.Schulform);
		}),
		Map.entry("bezeichnung", (conn, dto, value, map) -> dto.ASDBezeichnung = JSONMapper.convertToString(value, true, true, 100)),
		Map.entry("sortierung", (conn, dto, value, map) -> dto.Sortierung = JSONMapper.convertToInteger(value, true)),
		Map.entry("kuerzelSchulgliederung", (conn, dto, value, map) -> {
			final String str = JSONMapper.convertToString(value, true, false, null);
			dto.Gliederung = Schulgliederung.getByKuerzel(str);
		}),
		Map.entry("idFolgejahrgang", (conn, dto, value, map) -> {
			final Long idFolgejahrgang = JSONMapper.convertToLong(value, true);
			if (idFolgejahrgang != null) {
				conn.transactionFlush();
				final DTOJahrgang folgeJahrgang = conn.queryByKey(DTOJahrgang.class, idFolgejahrgang);
				if (folgeJahrgang == null)
					throw OperationError.CONFLICT.exception();
				conn.transactionFlush();
			}
			dto.Folgejahrgang_ID = idFolgejahrgang;
		}),
		Map.entry("anzahlRestabschnitte", (conn, dto, value, map) -> dto.AnzahlRestabschnitte = JSONMapper.convertToIntegerInRange(value, true, 0, 40)),
		Map.entry("istSichtbar", (conn, dto, value, map) -> dto.Sichtbar = JSONMapper.convertToBoolean(value, true)),
		Map.entry("gueltigVon", (conn, dto, value, map) -> dto.Sortierung = JSONMapper.convertToIntegerInRange(value, true, 1900, 3000)),
		Map.entry("gueltigBis", (conn, dto, value, map) -> dto.Sortierung = JSONMapper.convertToIntegerInRange(value, true, 1900, 3000))
	);


	@Override
	public Response patch(final Long id, final InputStream is) {
		return super.patchBasic(id, is, DTOJahrgang.class, patchMappings);
	}


	private static final Set<String> requiredCreateAttributes = Set.of("kuerzel", "kuerzelStatistik");

	private final ObjLongConsumer<DTOJahrgang> initDTO = (dto, id) -> {
		dto.ID = id;
	};


	/**
	 * Erstellt einen neuen Jahrgang
	 *
	 * @param  is	JSON-Objekt mit den Daten
	 *
	 * @return Eine Response mit dem neuen Jahrgang
	 */
	public Response add(final InputStream is) {
		return super.addBasic(is, DTOJahrgang.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Prüft, ob der Jahrgang sicher, d.h. ohne Datenverluste an anderer Stelle
	 * gelöscht werden kann.
	 *
	 * @param id   die ID des Jahrgangs
	 *
	 * @return true, falls der Jahrgang sicher gelöscht werden kann und ansonsten false
	 */
	private boolean isDeletable(final Long id) {
		if (id == null)
			return false;
		// TODO Prüfe, ob der Jahrgang sicher (true) gelöscht werden kann. Existiert auch nur eine Referenz, so muss dieser erhalten bleiben (false)
		return false;
	}

	/**
	 * Löscht einen Jahrgang
	 *
	 * @param id   die ID des Jahrgangs
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(final Long id) {
		if (!isDeletable(id))
			throw OperationError.CONFLICT.exception("Der Jahrgang kann nicht sicher gelöscht werden.");
		return super.deleteBasic(id, DTOJahrgang.class, dtoMapper);
	}


	/**
	 * Löscht mehrere Jahrgänge
	 *
	 * @param ids   die IDs der Jahrgänge
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response deleteMultiple(final List<Long> ids) {
		for (final Long id : ids)
			if (!isDeletable(id))
				throw OperationError.CONFLICT.exception("Der Jahrgang kann nicht sicher gelöscht werden.");
		return super.deleteBasicMultiple(ids, DTOJahrgang.class, dtoMapper);
	}

}
