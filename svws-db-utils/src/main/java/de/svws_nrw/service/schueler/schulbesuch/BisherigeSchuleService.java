package de.svws_nrw.service.schueler.schulbesuch;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.data.kataloge.DataKatalogEntlassgruende;
import de.svws_nrw.data.schule.DataSchulen;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.db.dto.current.schild.schueler.DTOEntlassarten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerAbgaenge;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schueler.schulbesuch.BisherigeSchuleMapper;
import de.svws_nrw.repo.schueler.schulbesuch.SchuelerBisherigeSchuleRepository;
import jakarta.ws.rs.core.Response;

public final class BisherigeSchuleService {

	private final SchuelerBisherigeSchuleRepository repository;
	private final BisherigeSchuleMapper mapper;

	private final DataSchulen dataSchulen;
	private final DataKatalogEntlassgruende dataKatalogEntlassgruende;

	/**
	 * Erstellt einen neuen {@code BisherigeSchulenService}.
	 * <p>
	 * Schulen und Entlassarten werden beim Erzeugen einmalig aus den übergebenen
	 * Katalog-Datenquellen in Maps überführt, um spätere Lookups effizient zu halten.
	 * </p>
	 *
	 * @param repository                  das Repository für {@link DTOSchuelerAbgaenge}-Einträge
	 * @param mapper                      der Mapper zur Konvertierung zwischen Entity und API-Modell
	 * @param dataSchulen                 Datenquelle für den Schulkatalog (Lookup via Schulnummer)
	 * @param dataKatalogEntlassgruende   Datenquelle für den Entlassgrundkatalog (Lookup via Bezeichnung)
	 */
	public BisherigeSchuleService(
			final SchuelerBisherigeSchuleRepository repository,
			final BisherigeSchuleMapper mapper,
			final DataSchulen dataSchulen,
			final DataKatalogEntlassgruende dataKatalogEntlassgruende) {
		this.mapper = mapper;
		this.repository = repository;
		this.dataSchulen = dataSchulen;
		this.dataKatalogEntlassgruende = dataKatalogEntlassgruende;
	}

	/**
	 * Gibt alle bisherigen Schulen eines Schülers als API-Modelle zurück.
	 *
	 * @param idSchueler die ID des Schülers
	 * @return Liste der bisherigen Schulen, leer wenn keine vorhanden
	 */
	public List<SchuelerSchulbesuchSchule> getAllByIdSchueler(final Long idSchueler) {
		final var schulenBySchulnummer = this.dataSchulen.getAllEntities()
				.stream()
				.collect(Collectors.toMap(s -> s.SchulNr, s -> s));
		final var entlassartenByBezeichnung = this.dataKatalogEntlassgruende.getAllEntities()
				.stream()
				.collect(Collectors.toMap(e -> e.Bezeichnung, e -> e));
		return this.repository
				.getAllByIdSchueler(idSchueler)
				.stream()
				.map(s -> this.map(s, schulenBySchulnummer, entlassartenByBezeichnung))
				.toList();
	}

	private SchuelerSchulbesuchSchule map(
			final DTOSchuelerAbgaenge entity,
			final Map<String, DTOSchuleNRW> schulenBySchulnummer,
			final Map<String, DTOEntlassarten> entlassartenByBezeichnung) {
		final var idEntlassgrund = Optional.ofNullable(entity.bezeichnungEntlassgrund)
				.map(entlassartenByBezeichnung::get)
				.map(e -> e.ID)
				.orElse(null);
		final var idSchule = Optional.ofNullable(entity.schulnummer)
				.map(schulenBySchulnummer::get)
				.map(s -> s.ID)
				.orElse(null);
		return this.mapper.toApi(entity, idEntlassgrund, idSchule);
	}

	/**
	 * Erstellt ein neuen Eintrag einer bisherigen Schule.
	 * Validiert die Eingabedaten und erstellt den Eintrag in einer Transaktion.
	 *
	 * @param dto die Daten für den neuen Eintrag
	 * @return der erstellte Eintrag
	 *
	 * @throws ApiOperationException wenn die Schule oder Entlassart nicht gefunden wurde,
	 *                               das Enddatum vor dem Startdatum liegt
	 */
	public SchuelerSchulbesuchSchule create(final BisherigeSchuleCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var bisherigeSchule = this.validateCreate(dto);
			final var created = this.repository.create(bisherigeSchule);
			return this.mapper.toApi(created, dto.idEntlassgrund, dto.idSchule);
		});
	}

	/**
	 * Aktualisiert einen bestehenden Eintrag einer bisherigen Schule teilweise (PATCH).
	 * Nur die im Request angegebenen Felder werden aktualisiert.
	 * Die Operation wird in einer Transaktion ausgeführt.
	 * <p>
	 * Schule und Entlassart werden anhand ihrer IDs validiert und aufgelöst,
	 * bevor der Mapper die übrigen Felder auf die Entity anwendet.
	 * </p>
	 *
	 * @param id  die ID des zu aktualisierenden Eintrags
	 * @param dto die zu aktualisierenden Felder
	 * @return der aktualisierte Eintrag als API-Modell
	 * @throws ApiOperationException wenn die Schule oder Entlassart nicht gefunden wurde,
	 *                               das Enddatum vor dem Startdatum liegt
	 *                               oder der Eintrag nicht gefunden wurde
	 */
	public SchuelerSchulbesuchSchule patch(final long id, final BisherigeSchulePatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var entity = repository.getById(id);
			validateAndResolvePatch(entity, dto);
			mapper.patch(dto, entity);
			return this.mapper.toApi(
					entity,
					dto.idEntlassgrund.orElseGet(() -> this.dataKatalogEntlassgruende.getEntityByBezeichnung(entity.bezeichnungEntlassgrund).ID),
					dto.idSchule.orElseGet(()  -> this.dataSchulen.getEntityBySchulnummer(entity.schulnummer).ID)
					);
		});
	}

	private void validateAndResolvePatch(final DTOSchuelerAbgaenge entity, final BisherigeSchulePatchRequest dto) {
		dto.idSchule.ifPresent(idSchule ->
				entity.schulnummer = this.validateAndResolveSchulnummer(idSchule));

		dto.idEntlassgrund.ifPresent(idEntlassgrund ->
				entity.bezeichnungEntlassgrund = this.validateAndResolveEntlassgrund(idEntlassgrund));

		final var effectiveDatumVon = dto.datumVon.orElse(entity.datumVon);
		final var effectiveDatumBis = dto.datumBis.orElse(entity.datumBis);
		this.validateDatumBis(effectiveDatumBis, effectiveDatumVon);
	}

	/**
	 * Löscht mehrere Einträge bisheriger Schulen anhand ihrer IDs.
	 * Die Operation wird in einer Transaktion ausgeführt.
	 *
	 * @param idsToDelete die Liste der IDs der zu löschenden Einträge bisheriger Schulen
	 * @return eine Liste von Antworten mit dem Status jeder Löschoperation, sortiert nach ID
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() -> {
			final var entitiesToDelete = repository.findListByIds(idsToDelete);

			return repository.delete(entitiesToDelete)
					.stream()
					.map(entry -> createResponseLog(entry.id))
					.sorted(Comparator.comparingLong(response -> response.id))
					.toList();
		});
	}

	private DTOSchuelerAbgaenge validateCreate(final BisherigeSchuleCreateRequest dto) {
		final var bezeichnungEntlassgrund = this.validateAndResolveEntlassgrund(dto.idEntlassgrund);
		final var schulnummer = this.validateAndResolveSchulnummer(dto.idSchule);
		this.validateDatumBis(dto.datumBis, dto.datumVon);
		return this.mapper.toDomain(dto, bezeichnungEntlassgrund, schulnummer);
	}

	private String validateAndResolveEntlassgrund(final Long idEntlassgrund) {
		if (idEntlassgrund == null) {
			return null;
		}
		return this.dataKatalogEntlassgruende.getEntityById(idEntlassgrund).Bezeichnung;
	}

	private String validateAndResolveSchulnummer(final Long idSchule) {
		if (idSchule == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "idSchule ist ein Pflichtfeld.");
		}
		return this.dataSchulen.getEntityById(idSchule).SchulNr;
	}

	private void validateDatumBis(final String datumBis, final String datumVon) {
		if ((datumVon != null) && (datumBis != null)) {
			final var von = LocalDate.parse(datumVon);
			final var bis = LocalDate.parse(datumBis);
			if (bis.isBefore(von)) {
				throw new ApiOperationException(Response.Status.BAD_REQUEST, "Das Enddatum %s darf nicht vor dem Startdatum %s liegen".formatted(bis, von));
			}
		}
	}

	private static SimpleOperationResponse createResponseLog(final long id) {
		final var log = new SimpleOperationResponse();
		log.id = id;
		log.success = true;
		return log;
	}

}
