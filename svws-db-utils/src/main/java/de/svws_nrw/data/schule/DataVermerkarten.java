package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.VermerkartEintrag;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOVermerkArt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerVermerke;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link VermerkartEintrag}.
 */
public final class DataVermerkarten extends DataManagerRevised<Long, DTOVermerkArt, VermerkartEintrag> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link VermerkartEintrag}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataVermerkarten(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("bezeichnung");
		setAttributesNotPatchable("id");
	}


	@Override
	public VermerkartEintrag map(final DTOVermerkArt dtoVermerkArt) {
		final VermerkartEintrag daten = new VermerkartEintrag();
		daten.id = dtoVermerkArt.ID;
		daten.bezeichnung = (dtoVermerkArt.Bezeichnung == null) ? "" : dtoVermerkArt.Bezeichnung;
		daten.sortierung = (dtoVermerkArt.Sortierung == null) ? 32000 : dtoVermerkArt.Sortierung;
		daten.istSichtbar = (dtoVermerkArt.Sichtbar == null) || dtoVermerkArt.Sichtbar;
		daten.anzahlVermerke = 0;
		return daten;
	}


	/**
	 * Konvertiert ein DTOVermerkArt-Objekt in ein VermerkartEintrag-Objekt und setzt die Anzahl der Vermerke.
	 *
	 * @param dtoVermerkArt Das DTOVermerkArt-Objekt, das konvertiert werden soll.
	 * @param anzahlVermerke Die Anzahl der Vermerke, die gesetzt werden sollen.
	 *
	 * @return Ein VermerkartEintrag-Objekt, das aus dem DTOVermerkArt-Objekt konvertiert und mit der Anzahl der Vermerke gesetzt wurde.
	 */
	public VermerkartEintrag map(final DTOVermerkArt dtoVermerkArt, final int anzahlVermerke) {
		final VermerkartEintrag vm = map(dtoVermerkArt);
		vm.anzahlVermerke = anzahlVermerke;
		return vm;
	}


	@Override
	public List<VermerkartEintrag> getAll() {
		final List<DTOVermerkArt> vermerkArten = conn.queryAll(DTOVermerkArt.class);

		final Map<Long, Long> anzahlVermerkeById = conn.queryList(DTOSchuelerVermerke.QUERY_ALL.concat(" WHERE e.VermerkArt_ID IS NOT NULL"),
				DTOSchuelerVermerke.class).stream().collect(Collectors.groupingBy(s -> s.VermerkArt_ID, Collectors.counting()));

		return vermerkArten.stream().map(v -> map(v, anzahlVermerkeById.getOrDefault(v.ID, 0L).intValue())).toList();
	}


	@Override
	public VermerkartEintrag getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Vermerkart mit der ID null ist unzulässig.");

		final DTOVermerkArt vermerkArt = conn.queryByKey(DTOVermerkArt.class, id);
		if (vermerkArt == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Vermerkart mit der ID %d wurde nicht gefunden.".formatted(id));

		final int anzahlVermerke = conn.queryList(DTOSchuelerVermerke.QUERY_BY_VERMERKART_ID.replace("SELECT e ", "SELECT COUNT(e) "),
				DTOSchuelerVermerke.class, vermerkArt.ID).size();
		return map(vermerkArt, anzahlVermerke);
	}


	@Override
	protected void initDTO(final DTOVermerkArt dtoVermerkArt, final Long vermerkartId, final Map<String, Object> initAttributes) {
		dtoVermerkArt.ID = vermerkartId;
	}


	@Override
	protected void mapAttribute(final DTOVermerkArt dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, name);
				if (!Objects.equals(dto.ID, id))
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
			}
			case "bezeichnung" -> updateBezeichnung(dto, value, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private void updateBezeichnung(final DTOVermerkArt dto, final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Vermerkart.col_Bezeichnung.datenlaenge(), name);
		if (Objects.equals(dto.Bezeichnung, bezeichnung) || bezeichnung.isBlank())
			return;

		final boolean alreadyUsed = this.conn.queryAll(DTOVermerkArt.class).stream()
				.anyMatch(v -> (v.ID != dto.ID) && v.Bezeichnung.equalsIgnoreCase(bezeichnung));
		if (alreadyUsed)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));

		dto.Bezeichnung = bezeichnung;
	}


	/**
	 * Löscht mehrere Vermerkarten und gibt das Ergebnis der Lösch-Operationen als Liste von {@link SimpleOperationResponse} zurück.
	 *
	 * @param ids   die IDs der zu löschenden Vermerkarten
	 *
	 * @return die Response mit einer Liste von {@link SimpleOperationResponse} zu den angefragten Lösch-Operationen.
	 */
	@Override
	public Response deleteMultipleAsResponse(final List<Long> ids) {
		if (ids.isEmpty()) {
			final SimpleOperationResponse response = new SimpleOperationResponse();
			response.success = false;
			response.log.add("Die Liste der zu löschenden Ids ist leer.");
			return Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(response).build();
		}
		final Map<Long, List<DTOSchuelerVermerke>> vermerkeById =
				this.conn.queryList("SELECT v FROM DTOSchuelerVermerke v WHERE v.VermerkArt_ID IN ?1", DTOSchuelerVermerke.class, ids).stream()
				.filter(v -> Objects.nonNull(v.VermerkArt_ID))
				.collect(Collectors.groupingBy(v -> v.VermerkArt_ID));
		return Response.ok(
				this.conn.queryByKeyList(DTOVermerkArt.class, ids).stream()
						.map(dto -> {
							final SimpleOperationResponse response = checkDeletePreConditions(dto, vermerkeById);
							if (response.log.isEmpty())
								response.success = this.conn.transactionRemove(dto);
							return response;
						}).toList()
		).build();
	}

	/**
	 * Diese Methode prüft, ob alle Vorbedingungen zum Löschen einer Vermerkart erfüllt sind.
	 * Es wird eine {@link SimpleOperationResponse} zurückgegeben.
	 *
	 * @param dto   das DTO der VermerkArt, die gelöscht werden soll
	 * @param vermerkeById Map der DTOSchuelerVermerke nach VermerkArt_ID
	 *
	 * @return Liefert eine Response mit dem Log der Vorbedingungsprüfung zurück.
	 */
	private SimpleOperationResponse checkDeletePreConditions(final @NotNull DTOVermerkArt dto, final Map<Long, List<DTOSchuelerVermerke>> vermerkeById) {
		final SimpleOperationResponse response = new SimpleOperationResponse();
		response.id = dto.ID;
		final List<DTOSchuelerVermerke> vermerke = vermerkeById.getOrDefault(dto.ID, Collections.emptyList());
		if (!vermerke.isEmpty())
			response.log.add("Vermerkart %s (ID: %d) hat noch %d verknüpfte(n) Vermerke.".formatted(dto.Bezeichnung, dto.ID, vermerke.size()));
		return response;
	}

}
