package de.svws_nrw.service.lehrer.mehrleistung;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.types.lehrer.LehrerMehrleistungsarten;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerMehrleistung;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.LehrerMehrleistungMapper;
import jakarta.ws.rs.core.Response;

/**
 * Ein Service für den Zugriff auf die Mehrleistungen bei Lehrern
 */
public final class LehrerMehrleistungService {

	private final LehrerMehrleistungServiceKontext kontext;
	private final LehrerMehrleistungMapper mapper;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param	kontext	der Daten-Kontext für diesen Service
	 * @param	mapper	der Mapper zur Konvertierung zwischen Domain- und API-Modellen
	 */
	public LehrerMehrleistungService(final LehrerMehrleistungServiceKontext kontext, final LehrerMehrleistungMapper mapper) {
		this.kontext = kontext;
		this.mapper = mapper;
	}

	private LehrerPersonalabschnittsdatenAnrechnungsstunden toApi(final DTOLehrerMehrleistung dto) {
		final var lehrerabschnittsdaten = kontext.getLehrerAbschnitt(dto.idAbschnittsdaten);
		final var schuljahresabschnitt = kontext.getSchuljahresabschnitt(lehrerabschnittsdaten.Schuljahresabschnitts_ID);

		// Ermittle die Art des Grundes. Ist dieser nicht gültig für das Halbjahr, so wird keine Fehlermeldung ausgegeben, sondern der Grund auf null gesetzt.
		final LehrerMehrleistungsarten art = LehrerMehrleistungsarten.data().getWertByKuerzel(dto.idGrund);
		final Long idGrund = LehrerMehrleistungsarten.data().getIDByWertAndSchuljahr(art, schuljahresabschnitt.Jahr);

		return mapper.toApi(dto, idGrund);
	}

	/**
	 * Ermittelt die Mehrleistung anhand der übergebenen ID.
	 *
	 * @param id   die ID für den Eintrag zu den Mehrleistungen des Lehrers
	 *
	 * @return die Mehrleistung
	 */
	public LehrerPersonalabschnittsdatenAnrechnungsstunden get(final long id) {
		final var list = getList(List.of(id));
		if (list.isEmpty()) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde kein Eintrag mit der ID %d gefunden.".formatted(id));
		}
		return list.getFirst();
	}

	/**
	 * Ermittelt die Mehrleistung-Einträge anhand der übergebenen IDs.
	 *
	 * @param ids   die IDs für die Einträge zu den Mehrleistungen des Lehrers
	 *
	 * @return die Einträge zu den Mehrleistungen
	 */
	public List<LehrerPersonalabschnittsdatenAnrechnungsstunden> getList(final Collection<Long> ids) {
		final List<DTOLehrerMehrleistung> mehrleistungen = kontext.fetch(ids);
		return mehrleistungen.stream().map(this::toApi).toList();
	}

	/**
	 * Bestimmt die Liste der Einträge von Mehrleistungen für die Lernabschnittsdaten mit der übergebenen ID.
	 *
	 * @param idLehrerabschnittsdaten   die ID der Lernabschnittsdaten
	 *
	 * @return die Liste der Einträge von Mehrleistungen
	 */
	public List<LehrerPersonalabschnittsdatenAnrechnungsstunden> getListByLehrerabschnittsdatenId(final long idLehrerabschnittsdaten) {
		final List<DTOLehrerMehrleistung> entities = kontext.fetchByLehrerabschnittsdatenId(idLehrerabschnittsdaten);
		return entities.stream().map(this::toApi).toList();
	}

	/**
	 * Ermittelt die Mehrleistungs-Einträge gruppiert nach den IDs der Lehrerabschnittsdaten.
	 *
	 * @param idsLehrerAbschnittsdaten die IDs der Lehrerabschnittsdaten
	 * @return Map von Lehrerabschnittsdaten-ID auf Liste der zugehörigen Mehrleistungen
	 */
	public Map<Long, List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> getListByIdLehrerAbschnittsdaten(
			final Collection<Long> idsLehrerAbschnittsdaten) {
		return kontext.fetchMapByAbschnittIds(idsLehrerAbschnittsdaten).entrySet().stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						entry -> entry.getValue().stream().map(this::toApi).toList()
				));
	}

	/**
	 * Führt einen Patch für das Core-DTO mit der angegebenen ID aus
	 *
	 * @param patch   der Patch
	 *
	 * @return das gepatchte Core-DTO
	 */
	public LehrerPersonalabschnittsdatenAnrechnungsstunden patch(final LehrerMehrleistungPatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches auf mehrere Core-DTOs aus.
	 *
	 * @param patches   eine Collection mit den Patches.
	 *
	 * @return die Liste mit den gepatchten Core-DTOs
	 */
	public List<LehrerPersonalabschnittsdatenAnrechnungsstunden> patchMultiple(final Collection<LehrerMehrleistungPatchRequest> patches) {
		if (patches.isEmpty()) {
			return new ArrayList<>();
		}

		return transactional(() -> {
			// Bestimme die Entitäten aus der Datenbank
			final List<Long> ids = patches.stream().map(p -> p.id).toList();
			final List<DTOLehrerMehrleistung> entities = kontext.fetch(ids);
			if (entities.size() != patches.size()) {
				throw new ApiOperationException(Response.Status.NOT_FOUND, "Nicht alle angefragten Datensätze konnten gefunden werden.");
			}

			// Führe die Patches aus
			for (final var patch : patches) {
				final var entity = kontext.getMehrleistung(patch.id);
				applyPatch(entity, patch);
			}

			// Persistiere das Ergebnis und gebe die Core-DTOs zurück
			kontext.persist(entities);
			return getList(ids);
		});
	}

	private String applyAnrechnungsgrund(final long idAbschnittsdaten, final long idGrund) {
		final var grund = LehrerMehrleistungsarten.data().getWertByIDOrNull(idGrund);
		if (grund == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Der Anrechnungsgrund mit der ID %d ist nicht vorhanden.".formatted(idGrund));
		}
		final var lehrerabschnitt = kontext.getLehrerAbschnitt(idAbschnittsdaten);
		final var schuljahresabschnitt = kontext.getSchuljahresabschnitt(lehrerabschnitt.Schuljahresabschnitts_ID);
		final var eintrag = grund.daten(schuljahresabschnitt.Jahr);
		if (eintrag == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST,
					"Der Anrechnungsgrund mit der ID %d ist im Schuljahr %d nicht gültig.".formatted(idGrund, schuljahresabschnitt.Jahr));
		}
		return eintrag.kuerzel;
	}

	private void applyPatch(final DTOLehrerMehrleistung daten, final LehrerMehrleistungPatchRequest patch) {
		mapper.patch(patch, daten);
		patch.idGrund.ifPresent(val -> daten.idGrund = applyAnrechnungsgrund(daten.idAbschnittsdaten, val));
	}

	private void applyCreateRequest(final DTOLehrerMehrleistung daten, final LehrerMehrleistungCreateRequest createRequest) {
		daten.idGrund = applyAnrechnungsgrund(daten.idAbschnittsdaten, createRequest.idGrund);
		daten.anzahl = createRequest.anzahl;
	}

	/**
	 * Erstellt ein neues Core-DTO mit einer neuen ID und mithilfe
	 * des Create-Patches.
	 *
	 * @param patch   der Create-Patch
	 *
	 * @return das neue Core-DTO
	 */
	public LehrerPersonalabschnittsdatenAnrechnungsstunden create(final LehrerMehrleistungCreateRequest patch) {
		return createMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Erstellt neue Core-DTOs mit neuen IDs und mithilfe der Create-Patches.
	 *
	 * @param createRequests   die Create-Patches
	 *
	 * @return die neuen Core-DTOs
	 */
	public List<LehrerPersonalabschnittsdatenAnrechnungsstunden> createMultiple(final List<LehrerMehrleistungCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}

		return transactional(() -> {
			// Erstelle die neuen Entitäten als Grundlage für den Patch-Vorgang
			final List<DTOLehrerMehrleistung> entities = kontext.create(createRequests);

			// Führe die Patches aus und validiere die Ergebnisse
			for (int i = 0; i < createRequests.size(); i++) {
				applyCreateRequest(entities.get(i), createRequests.get(i));
			}

			// Persistiere das Ergebnis und gebe die Core-DTOs zurück
			kontext.persist(entities);
			final var ids = entities.stream().map(e -> e.id).toList();
			return getList(ids);
		});
	}

	/**
	 * Löscht das Core-DTO mit der angegebenen ID aus der Datenbank.
	 *
	 * @param id   die ID
	 *
	 * @return das entfernte Core-DTO
	 */
	public SimpleOperationResponse delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere Core-DTOs mit der angegebenen ID aus der Datenbank.
	 *
	 * @param ids   die IDs
	 *
	 * @return die entfernten Core-DTOs
	 */
	public List<SimpleOperationResponse> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final var entities = kontext.fetch(ids);
			kontext.delete(entities);
			return entities.stream()
					.map(e -> createResponseLog(e.id))
					.toList();
		});
	}

	private static SimpleOperationResponse createResponseLog(final long id) {
		final var log = new SimpleOperationResponse();
		log.id = id;
		log.success = true;
		return log;
	}

}
