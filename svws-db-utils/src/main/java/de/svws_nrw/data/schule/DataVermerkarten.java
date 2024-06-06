package de.svws_nrw.data.schule;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.VermerkartEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.schueler.DataSchuelerVermerke;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOVermerkArt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerVermerke;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ObjLongConsumer;
import java.util.stream.Collectors;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link VermerkartEintrag}.
 */
public final class DataVermerkarten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link VermerkartEintrag}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataVermerkarten(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOVermerkArt} in einen Core-DTO {@link VermerkartEintrag}.
	 * Dieser Mapper wird explizit nur verwendet wenn eine neue Vermerkart hinzugefügt werden soll. Für diese Vermerkart
	 * ist per definitionem der Anzahl der Vermerke 0.
	 */
	private static final DTOMapper<DTOVermerkArt, VermerkartEintrag> dtoMapperAdd = (final DTOVermerkArt k) -> {
		final VermerkartEintrag daten = new VermerkartEintrag();
		daten.id = k.ID;
		daten.bezeichnung = (k.Bezeichnung == null) ? "" : k.Bezeichnung;
		daten.sortierung = (k.Sortierung == null) ? 32000 : k.Sortierung;
		daten.istSichtbar = (k.Sichtbar == null) || k.Sichtbar;
		daten.anzahlVermerke = 0;
		return daten;
	};


	/**
	 * Mapper zum Erstellen des Core-DTOs aus den Datenbank-DTOs zu den Vermerkarten.
	 *
	 * @param dtoVermerkArt    Der DTO für die Vermerke arten
	 * @param anzahlVermerke   anzahl der existieren Vermerke für diese Vermerkart
	 *
	 * @return Core-DTO mit allen Vermerkartinformationen
	 */
	private static VermerkartEintrag mapDTO(final DTOVermerkArt dtoVermerkArt, final int anzahlVermerke) throws ApiOperationException {
		// Wir können den standard dtoMapper nutzen, müssen nur die automatisch auf 0 gesetze Anzahl der Vermerke berücksichtigen
		final VermerkartEintrag daten = dtoMapperAdd.apply(dtoVermerkArt);
		daten.anzahlVermerke = anzahlVermerke;
		return daten;
	};


	@Override
	public Response getAll() throws ApiOperationException {
		final var daten = getVermerkarten(conn);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Gibt die Liste der Vermerkarten zurück.
	 *
	 * @param conn die Datenbankverbindung
	 *
	 * @return die Liste der Vermerkarten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static List<VermerkartEintrag> getVermerkarten(final @NotNull DBEntityManager conn) throws ApiOperationException {
		if (conn == null)
			return new ArrayList<>();
		// Lese den Katalog der Vermerkarten ein
		final List<DTOVermerkArt> katalog = conn.queryAll(DTOVermerkArt.class);
		if (katalog == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Vermerkarten gefunden.");
		// Bestimme die zugehörigen Anzahlen zu den Vermerkarten
		final Map<Long, Long> mapAnzahlSchuelerVermerkeByVermerkart = conn.queryAll(DTOSchuelerVermerke.class)
				.stream().collect(Collectors.groupingBy(s -> s.VermerkArt_ID, Collectors.counting()));
		// Erstelle die Liste der Core-DTOs für die Schüler-Vermerke
		final ArrayList<VermerkartEintrag> daten = new ArrayList<>();
		for (final DTOVermerkArt r : katalog) {
			final int anzahlVermerke = mapAnzahlSchuelerVermerkeByVermerkart.computeIfAbsent(r.ID, k -> 0L).intValue();
			daten.add(mapDTO(r, anzahlVermerke));
		}
		return daten;
	}


	@Override
	public Response getList() throws ApiOperationException {
		return this.getAll();
	}


	/**
	 * Bestimmt die Vermerkart anhand der angegebenen ID.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 * @param id   die ID des Vermerk-Katalog-Eintrags
	 *
	 * @return der Eintrag der Vermerkart
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static VermerkartEintrag getVermerk(final @NotNull DBEntityManager conn, final long id) throws ApiOperationException {
		final DTOVermerkArt vermerkArt = conn.queryByKey(DTOVermerkArt.class, id);
		if (vermerkArt == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Vermerkart mit der ID %d wurde nicht gefunden.".formatted(id));
		final int anzahlVermerke = conn.queryList(DTOSchuelerVermerke.QUERY_BY_VERMERKART_ID.replace("SELECT e ", "SELECT COUNT(e) "),
				DTOSchuelerVermerke.class, vermerkArt.ID).size();
		return mapDTO(vermerkArt, anzahlVermerke);
	}


	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Vermerkart mit der ID null ist unzulässig.");
		final VermerkartEintrag daten = getVermerk(conn, id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static final Map<String, DataBasicMapper<DTOVermerkArt>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (Long.compare(patch_id, dto.ID) != 0))
				throw new ApiOperationException(Status.BAD_REQUEST, "Die angegebene ID %d ist null oder stimmt nicht mit der ID %d im DTO überein.".formatted(patch_id, dto.ID));
		}),
		Map.entry("bezeichnung", (conn, dto, value, map) -> {
			final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Vermerkart.col_Bezeichnung.datenlaenge());
			final List<DTOVermerkArt> arten = conn.queryList(DTOVermerkArt.QUERY_BY_BEZEICHNUNG, DTOVermerkArt.class, bezeichnung);
			if (!arten.isEmpty())
				throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung '%s' wird bereits für eine andere Vermerkart genutzt.".formatted(bezeichnung));
			dto.Bezeichnung = bezeichnung;
		}),
		Map.entry("sortierung", (conn, dto, value, map) -> dto.Sortierung = JSONMapper.convertToInteger(value, false)),
		Map.entry("istSichtbar", (conn, dto, value, map) -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false))
	);


	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOVermerkArt.class, patchMappings);
	}


	private static final Set<String> requiredCreateAttributes = Set.of("bezeichnung");


	private static final ObjLongConsumer<DTOVermerkArt> initDTO = (dto, id) -> {
		dto.ID = id;
	};


	/**
	 * Erstellt eine neue Vermerkart
	 *
	 * @param is JSON-Objekt mit den Daten
	 *
	 * @return Eine Response mit der neuen Vermerkart
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response add(final InputStream is) throws ApiOperationException {
		return super.addBasic(is, DTOVermerkArt.class, initDTO, dtoMapperAdd, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Löscht eine Vermerkart aus dem Katalog
	 *
	 * @param id die ID des Vermerkart-Katalog-Eintrags
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response delete(final Long id) throws ApiOperationException {
		throw new UnsupportedOperationException("Das Löschen von einzelnen Vermerkarten ist zur Zeit noch nicht implementiert. Bitte nutze delete multiple.");
	}


	/**
	 * Löscht mehrere Vermerkarten und gibt das Ergebnis der Lösch-Operationen als Liste von {@link SimpleOperationResponse} zurück.
	 *
	 * @param ids   die IDs der zu löschenden Vermerkarten
	 *
	 * @return die Response mit einer Liste von {@link SimpleOperationResponse} zu den angefragten Lösch-Operationen.
	 */
	public Response deleteMultiple(final List<Long> ids) {
		// Bestimme die Datenbank-DTOs der VermerkArten
		final List<DTOVermerkArt> vermerkArten = this.conn.queryByKeyList(DTOVermerkArt.class, ids).stream().toList();

		// Prüfe, ob das Löschen der Vermerkarten erlaubt ist
		final Map<Long, SimpleOperationResponse> mapResponses = vermerkArten.stream()
			.collect(Collectors.toMap(r -> r.ID, this::checkDeletePreConditions));

		// Lösche die Vermerkarten und gib den Erfolg in der Response zurück
		for (final DTOVermerkArt dtoVermerkArt : vermerkArten) {
			final SimpleOperationResponse operationResponse = mapResponses.get(dtoVermerkArt.ID);
			if (operationResponse == null)
				throw new DeveloperNotificationException("Das SimpleOperationResponse Objekt zu der ID %d existiert nicht.".formatted(dtoVermerkArt.ID));

			if (operationResponse.log.isEmpty())
				operationResponse.success = this.conn.transactionRemove(dtoVermerkArt);
		}

		return Response.ok().entity(mapResponses.values()).build();
	}


	/**
	 * Diese Methode prüft, ob alle Vorbedingungen zum Löschen einer Vermerkart erfüllt sind.
	 * Es wird eine {@link SimpleOperationResponse} zurückgegeben.
	 *
	 * @param dtoVermerkArt   das DTO der VermerkArt, die gelöscht werden soll
	 *
	 * @return Liefert eine Response mit dem Log der Vorbedingungsprüfung zurück.
	 */
	private SimpleOperationResponse checkDeletePreConditions(@NotNull final DTOVermerkArt dtoVermerkArt) {
		final SimpleOperationResponse operationResponse = new SimpleOperationResponse();
		operationResponse.id = dtoVermerkArt.ID;

		// Kein Schüler darf Vermerke dieser Vermerkart haben
		final List<Long> vermerkIds = DataSchuelerVermerke.getIDsByVermerkart(conn, dtoVermerkArt.ID);
		if (!vermerkIds.isEmpty())
			operationResponse.log.add("Vermerkart %s (ID: %d) hat noch %d verknüpfte(n) Vermerke.".formatted(dtoVermerkArt.Bezeichnung, dtoVermerkArt.ID, vermerkIds.size()));

		return operationResponse;
	}

}
