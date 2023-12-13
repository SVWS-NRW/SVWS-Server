package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;

import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanZeitraster;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanZeitraster}.
 */
public final class DataStundenplanZeitraster extends DataManager<Long> {

	private Long stundenplanID = null;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanZeitraster}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Zeitraster abgefragt wird
	 */
	public DataStundenplanZeitraster(final DBEntityManager conn, final Long stundenplanID) {
		super(conn);
		this.stundenplanID = stundenplanID;
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOStundenplanZeitraster} in einen Core-DTO {@link StundenplanZeitraster}.
	 */
	private static final Function<DTOStundenplanZeitraster, StundenplanZeitraster> dtoMapper = (final DTOStundenplanZeitraster z) -> {
		final StundenplanZeitraster daten = new StundenplanZeitraster();
		daten.id = z.ID;
		daten.wochentag = z.Tag;
		daten.unterrichtstunde = z.Stunde;
		daten.stundenbeginn = z.Beginn;
		daten.stundenende = z.Ende;
		return daten;
	};


	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Gibt das Zeitraster des Stundenplans zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return das Zeitraster
	 */
	public static List<StundenplanZeitraster> getZeitraster(final @NotNull DBEntityManager conn, final long idStundenplan) {
		final List<DTOStundenplanZeitraster> zeitraster = conn.queryNamed("DTOStundenplanZeitraster.stundenplan_id", idStundenplan, DTOStundenplanZeitraster.class);
		final ArrayList<StundenplanZeitraster> daten = new ArrayList<>();
		for (final DTOStundenplanZeitraster z : zeitraster)
			daten.add(dtoMapper.apply(z));
		return daten;
	}


	/**
	 * Ermittelt zu dem Tupel (wochentag, unterrichtsstunde) des Stundenplans mit der ID idStundenplan den zugehörigen
	 * Zeitrastereintrag in der Datenbank. Existiert ein solcher noch nicht, so wird ein neue Eintrag mit leeren Zeiten erzeugt.
	 *
	 * @param conn                die Datenbankverbindung
	 * @param idStundenplan       die ID des Stundenplans
	 * @param wochentag           der Wochentag
	 * @param unterrichtsstunde   die Unterrichtsstunde
	 *
	 * @return der Zeitrastereintrag
	 */
	public static StundenplanZeitraster getOrCreateZeitrasterEintrag(final @NotNull DBEntityManager conn, final long idStundenplan, final int wochentag, final int unterrichtsstunde) {
		final List<DTOStundenplanZeitraster> eintraege = conn.queryList("SELECT e FROM DTOStundenplanZeitraster e WHERE e.Stundenplan_ID = ?1 AND e.Tag = ?2 AND e.Stunde = ?3", DTOStundenplanZeitraster.class, idStundenplan, wochentag, unterrichtsstunde);
		final DTOStundenplanZeitraster eintrag;
		if (eintraege.isEmpty()) {
			final long id = conn.transactionGetNextID(DTOStundenplanZeitraster.class);
			eintrag = new DTOStundenplanZeitraster(id, idStundenplan, wochentag, unterrichtsstunde, 430 + unterrichtsstunde * 50, 475 + unterrichtsstunde * 50);
			conn.transactionPersist(eintrag);
			conn.transactionFlush();
		} else if (eintraege.size() == 1) {
			eintrag = eintraege.get(0);
		} else
			throw OperationError.INTERNAL_SERVER_ERROR.exception("Mehrfach-Einträge für die Kombination Wochentag %d und Stunde %d im Stundenplan mit der ID %d.".formatted(wochentag, unterrichtsstunde, idStundenplan));
		return dtoMapper.apply(eintrag);
	}


	@Override
	public Response getList() {
		final List<StundenplanZeitraster> daten = getZeitraster(conn, this.stundenplanID);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einem Zeitrastereintrag eines Stundenplans mit der ID null ist unzulässig.");
		final DTOStundenplanZeitraster eintrag = conn.queryByKey(DTOStundenplanZeitraster.class, id);
		if (eintrag == null)
			return OperationError.NOT_FOUND.getResponse("Es wurde kein Zeitrastereintrag eines Stundenplans mit der ID %d gefunden.".formatted(id));
		final StundenplanZeitraster daten = dtoMapper.apply(eintrag);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static final Map<String, DataBasicMapper<DTOStundenplanZeitraster>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID))
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("wochentag", (conn, dto, value, map) -> dto.Tag = JSONMapper.convertToIntegerInRange(value, false, 1, 8)),
		Map.entry("unterrichtstunde", (conn, dto, value, map) -> dto.Stunde = JSONMapper.convertToIntegerInRange(value, false, 0, 30)),
		Map.entry("stundenbeginn", (conn, dto, value, map) -> dto.Beginn = JSONMapper.convertToIntegerInRange(value, true, 0, 1440)),
		Map.entry("stundenende", (conn, dto, value, map) -> dto.Ende = JSONMapper.convertToIntegerInRange(value, true, 0, 1440))
	);

	@Override
	public Response patch(final Long id, final InputStream is) {
		return super.patchBasic(id, is, DTOStundenplanZeitraster.class, patchMappings);
	}

	/**
	 * Führt Patches für mehrere DTOs aus. Die Patches müssen als Liste übergeben werden.
	 *
	 * @param is   der Input-Stream mit der Liste der Patches
	 *
	 * @return eine NO_CONTENT-Response im Erfolgsfall
	 */
	public Response patchMultiple(final InputStream is) {
		return super.patchBasicMultiple("id", is, DTOStundenplanZeitraster.class, patchMappings);
	}


	private static final Set<String> requiredCreateAttributes = Set.of("wochentag", "unterrichtstunde", "stundenbeginn", "stundenende");

	private final ObjLongConsumer<DTOStundenplanZeitraster> initDTO = (dto, id) -> {
		dto.ID = id;
		dto.Stundenplan_ID = this.stundenplanID;
	};

	/**
	 * Fügt einen Zeitrastereintrag mit den übergebenen JSON-Daten der Datenbank hinzu und gibt das zugehörige CoreDTO
	 * zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 */
	public Response add(final InputStream is) {
		DataStundenplan.getDTOStundenplan(conn, stundenplanID);   // Prüfe, on der Stundenplan existiert
		return super.addBasic(is, DTOStundenplanZeitraster.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Fügt mehrere Zeitrastereinträge  mit den übergebenen JSON-Daten der Datenbank hinzu und gibt die zugehörigen CoreDTOs
	 * zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 */
	public Response addMultiple(final InputStream is) {
		DataStundenplan.getDTOStundenplan(conn, stundenplanID);   // Prüfe, on der Stundenplan existiert
		return super.addBasicMultiple(is, DTOStundenplanZeitraster.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Kopiert die Zeitrastereinträge des allgemeinen Katalogs zu den Zeitrastereinträgen des angegebenen Stundenplans hinzu.
	 * Dabei wird die angegebene Datenbankverbingung genutzt, welche eine offene Transaktion haben muss.
	 *
	 * @param conn             die Datenbankverbindung
	 * @param dtoStundenplan   das DTO des Stundenplans
	 * @param eintraege        die hinzuzufügenden Zeitraster-Einträge
	 */
	public static void addZeitraster(final @NotNull DBEntityManager conn, final DTOStundenplan dtoStundenplan, final List<StundenplanZeitraster> eintraege) {
		long id = conn.transactionGetNextID(DTOStundenplanZeitraster.class);
		for (final StundenplanZeitraster eintrag : eintraege)
			conn.transactionPersist(new DTOStundenplanZeitraster(id++, dtoStundenplan.ID, eintrag.wochentag, eintrag.unterrichtstunde, eintrag.stundenbeginn, eintrag.stundenende));
		conn.transactionFlush();
	}


	/**
	 * Löscht einen Zeitrastereintrag
	 *
	 * @param id   die ID des Zeitrastereintrags
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(final Long id) {
		return super.deleteBasic(id, DTOStundenplanZeitraster.class, dtoMapper);
	}


	/**
	 * Löscht mehrere Zeitrastereinträge
	 *
	 * @param ids   die IDs der Zeitrastereinträge
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response deleteMultiple(final List<Long> ids) {
		if (ids.isEmpty())
			throw OperationError.NOT_FOUND.exception("Es wurden keine IDs übergeben.");
		final List<DTOStundenplanZeitraster> dtos = conn.queryNamed("DTOStundenplanZeitraster.primaryKeyQuery.multiple", ids, DTOStundenplanZeitraster.class);
		for (final DTOStundenplanZeitraster dto : dtos)
			if (dto.Stundenplan_ID != this.stundenplanID)
				throw OperationError.BAD_REQUEST.exception("Der Zeitraster-Eintrag gehört nicht zu dem angegebenen Stundenplan.");
		return super.deleteBasicMultiple(ids, DTOStundenplanZeitraster.class, dtoMapper);
	}

}
