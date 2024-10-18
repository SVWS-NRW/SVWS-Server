package de.svws_nrw.data.schule;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.VermerkartEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.schueler.DataSchuelerVermerke;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOVermerkArt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerVermerke;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link VermerkartEintrag}.
 */
public final class DataVermerkarten extends DataManagerRevised<Long, DTOVermerkArt, VermerkartEintrag> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link VermerkartEintrag}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataVermerkarten(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("bezeichnung");
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
	public List<VermerkartEintrag> getAll() throws ApiOperationException {
		// Lese den Katalog der Vermerkarten ein
		final List<DTOVermerkArt> vermerkartenKatalog = conn.queryAll(DTOVermerkArt.class);
		if (vermerkartenKatalog == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Vermerkarten gefunden.");

		// Bestimme die zugehörigen Anzahlen zu den Vermerkarten
		final Map<Long, Long> mapAnzahlVermerkeByVermerkart = conn.queryList(DTOSchuelerVermerke.QUERY_ALL.concat(" WHERE e.VermerkArt_ID IS NOT NULL"),
				DTOSchuelerVermerke.class).stream().collect(Collectors.groupingBy(s -> s.VermerkArt_ID, Collectors.counting()));

		// Erstelle die Liste der Core-DTOs für die Schüler-Vermerke
		return vermerkartenKatalog.stream().map(vk -> map(vk, mapAnzahlVermerkeByVermerkart.computeIfAbsent(vk.ID, k -> 0L).intValue())).toList();
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
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id != dto.ID))
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die angegebene ID %d ist null oder stimmt nicht mit der ID %d im DTO überein.".formatted(patch_id, dto.ID));
			}
			case "bezeichnung" -> {
				final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Vermerkart.col_Bezeichnung.datenlaenge());
				final List<DTOVermerkArt> arten = conn.queryList(DTOVermerkArt.QUERY_BY_BEZEICHNUNG, DTOVermerkArt.class, bezeichnung);
				if (!arten.isEmpty())
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die Bezeichnung '%s' wird bereits für eine andere Vermerkart genutzt.".formatted(bezeichnung));
				dto.Bezeichnung = bezeichnung;
			}
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false);
			default -> throw new ApiOperationException(Status.BAD_REQUEST,
					"Das Attribut %s wird beim Patchen nicht unterstützt".formatted(name));
		}
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
	private SimpleOperationResponse checkDeletePreConditions(final @NotNull DTOVermerkArt dtoVermerkArt) {
		final SimpleOperationResponse operationResponse = new SimpleOperationResponse();
		operationResponse.id = dtoVermerkArt.ID;

		// Kein Schüler darf Vermerke dieser Vermerkart haben
		final List<Long> vermerkIds = new DataSchuelerVermerke(conn).getIDsByVermerkartId(dtoVermerkArt.ID);
		if (!vermerkIds.isEmpty())
			operationResponse.log.add(
					"Vermerkart %s (ID: %d) hat noch %d verknüpfte(n) Vermerke.".formatted(dtoVermerkArt.Bezeichnung, dtoVermerkArt.ID, vermerkIds.size()));

		return operationResponse;
	}

}
