package de.svws_nrw.service.lehrer.anrechnung;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.types.lehrer.LehrerAnrechnungsgrund;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAnrechnungsstunde;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;


/**
 * Ein Service für den Zugriff auf die Anrechnungsstunden bei Lehrern
 */
public final class LehrerAnrechnungsstundeService {

	private final LehrerAnrechnungsstundeServiceKontext kontext;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param kontext   der Daten-Kontext für diesen Service
	 */
	public LehrerAnrechnungsstundeService(final LehrerAnrechnungsstundeServiceKontext kontext) {
		this.kontext = kontext;
	}


	private LehrerPersonalabschnittsdatenAnrechnungsstunden toApi(final DTOLehrerAnrechnungsstunde dto) {
		final var lehrerabschnittsdaten = kontext.getAbschnitt(dto.Abschnitt_ID);
		final var schuljahresabschnitt = kontext.getSchuljahresabschnitt(lehrerabschnittsdaten.Schuljahresabschnitts_ID);

		final var daten = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
		daten.id = dto.ID;
		daten.idAbschnittsdaten = dto.Abschnitt_ID;

		// Ermittle die Art des Grundes. Ist dieser nicht gültig für das Halbjahr, so wird keine Fehlermeldung ausgegeben, sondern der Grund auf null gesetzt.
		final var art = LehrerAnrechnungsgrund.data().getWertByKuerzel(dto.AnrechnungsgrundKrz);
		daten.idGrund = LehrerAnrechnungsgrund.data().getIDByWertAndSchuljahr(art, schuljahresabschnitt.Jahr);

		daten.anzahl = Objects.requireNonNullElse(dto.AnrechnungStd, 0.0);
		return daten;
	}


	/**
	 * Ermittelt die Anrechnungsstunden anhand der übergebenen ID.
	 *
	 * @param id   die ID für den Eintrag zu den Anrechnungsstunden des Lehrers
	 *
	 * @return die Anrechnungsstunden
	 */
	public LehrerPersonalabschnittsdatenAnrechnungsstunden get(final long id) {
		final var list = getList(List.of(id));
		if (list.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Eintrag mit der ID %d gefunden.".formatted(id));
		}
		return list.getFirst();
	}


	/**
	 * Ermittelt die Anrechnungsstunden-Einträge anhand der übergebenen IDs.
	 *
	 * @param ids   die IDs für die Einträge zu den Anrechnungsstunden des Lehrers
	 *
	 * @return die Einträge zu den Anrechnungsstunden
	 */
	public List<LehrerPersonalabschnittsdatenAnrechnungsstunden> getList(final Collection<Long> ids) {
		final List<DTOLehrerAnrechnungsstunde> anrechnungen = kontext.fetch(ids);
		return anrechnungen.stream().map(this::toApi).toList();
	}

	/**
	 * Ermittelt die Anrechnungsstunden-Einträge gruppiert nach den IDs der Lehrerabschnittsdaten.
	 *
	 * @param idsLehrerAbschnittsdaten die IDs der Lehrerabschnittsdaten
	 * @return Map von Lehrerabschnittsdaten-ID auf Liste der zugehörigen Anrechnungsstunden
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
	 * Bestimmt die Liste der Einträge von Anrechungsstunden für die Lernabschnittsdaten mit der übergebenen ID.
	 *
	 * @param idLehrerabschnittsdaten   die ID der Lernabschnittsdaten
	 *
	 * @return die Liste der Einträge von Anrechungsstunden
	 */
	public List<LehrerPersonalabschnittsdatenAnrechnungsstunden> getListByLehrerabschnittsdatenId(final long idLehrerabschnittsdaten) {
		final List<DTOLehrerAnrechnungsstunde> dtos = kontext.fetchByLehrerabschnittsdatenId(idLehrerabschnittsdaten);
		return dtos.stream().map(this::toApi).toList();
	}


	/**
	 * Führt einen Patch für das Core-DTO aus. Der Patch enthält die ID auf welche er sich bezieht
	 *
	 * @param patch   der Patch
	 *
	 * @return das gepatchte Core-DTO
	 */
	public LehrerPersonalabschnittsdatenAnrechnungsstunden patch(final LehrerAnrechnungsstundePatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}


	/**
	 * Führt mehrere Patches auf mehrere Core-DTOs aus. Die Patches enthalten die IDs auf welche sie sich beziehen
	 *
	 * @param patches   die Patches
	 *
	 * @return die Liste mit den gepatchten Core-DTOs
	 */
	public List<LehrerPersonalabschnittsdatenAnrechnungsstunden> patchMultiple(final Collection<LehrerAnrechnungsstundePatchRequest> patches) {
		if (patches.isEmpty()) {
			return new ArrayList<>();
		}

		return transactional(() -> {
			// Bestimme die Entitäten aus der Datenbank
			final List<Long> ids = patches.stream().map(p -> p.id).toList();
			final List<DTOLehrerAnrechnungsstunde> entities = kontext.fetch(ids);
			if (entities.size() != patches.size()) {
				throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle angefragten Datensätze konnten gefunden werden.");
			}

			// Führe die Patches aus
			for (final var patch : patches) {
				final var entity = kontext.getAnrechnungsstunden(patch.id);
				applyPatch(entity, patch);
			}

			// Persistiere das Ergebnis und gebe die Core-DTOs zurück
			kontext.persist(entities);
			return getList(ids);
		});
	}


	private String applyAnrechnungsgrund(final long idAbschnittsdaten, final long idGrund) {
		final var grund = LehrerAnrechnungsgrund.data().getWertByIDOrNull(idGrund);
		if (grund == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Anrechnungsgrund mit der ID %d ist nicht vorhanden.".formatted(idGrund));
		}
		final var lehrerabschnitt = kontext.getAbschnitt(idAbschnittsdaten);
		final var schuljahresabschnitt = kontext.getSchuljahresabschnitt(lehrerabschnitt.Schuljahresabschnitts_ID);

		final var eintrag = grund.daten(schuljahresabschnitt.Jahr);
		if (eintrag == null) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der Anrechnungsgrund mit der ID %d ist im Schuljahr %d nicht gültig.".formatted(idGrund, schuljahresabschnitt.Jahr));
		}
		return eintrag.kuerzel;
	}

	private void applyPatch(final DTOLehrerAnrechnungsstunde daten, final LehrerAnrechnungsstundePatchRequest patch) {
		patch.idGrund.ifPresent(val -> daten.AnrechnungsgrundKrz = applyAnrechnungsgrund(daten.Abschnitt_ID, val));
		patch.anzahl.ifPresent(val -> daten.AnrechnungStd = val);
	}

	private void applyCreateRequest(final DTOLehrerAnrechnungsstunde daten, final LehrerAnrechnungsstundeCreateRequest createRequest) {
		daten.AnrechnungsgrundKrz = applyAnrechnungsgrund(daten.Abschnitt_ID, createRequest.idGrund);
		daten.AnrechnungStd = createRequest.anzahl;
	}


	/**
	 * Erstellt eine neues Core-DTO mit einer neuen ID und mithilfe
	 * des Create-Patches.
	 *
	 * @param patch   der Create-Patch
	 *
	 * @return das neue Core-DTO
	 */
	public LehrerPersonalabschnittsdatenAnrechnungsstunden create(final LehrerAnrechnungsstundeCreateRequest patch) {
		return createMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Erstellt neue Core-DTOs mit neuen IDs und mithilfe der Create-Patches.
	 *
	 * @param createRequests   die Create-Patches
	 *
	 * @return die neuen Core-DTOs
	 */
	public List<LehrerPersonalabschnittsdatenAnrechnungsstunden> createMultiple(final Collection<LehrerAnrechnungsstundeCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}

		return transactional(() -> {
			// Erstelle die neuen Entitäten als Grundlage für den Patch-Vorgang
			final List<DTOLehrerAnrechnungsstunde> entities = kontext.create(createRequests);

			// Führe die Patches aus und validiere die Ergebnisse
			int i = 0;
			for (final LehrerAnrechnungsstundeCreateRequest createRequest : createRequests) {
				applyCreateRequest(entities.get(i), createRequest);
				i++;
			}

			// Persistiere das Ergebnis und gebe die Core-DTOs zurück
			kontext.persist(entities);
			final var ids = entities.stream().map(e -> e.ID).toList();
			return getList(ids);
		});
	}


	/**
	 * Löscht das Core-DTO mit der angebenen ID aus der Datenbank.
	 *
	 * @param id   die ID
	 *
	 * @return das entfernte Core-DTO
	 */
	public LehrerPersonalabschnittsdatenAnrechnungsstunden delete(final long id) {
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> result = deleteMultiple(List.of(id));
		return result.getFirst();
	}


	/**
	 * Löscht mehrere Core-DTO mit der angebenen ID aus der Datenbank.
	 *
	 * @param ids   die IDs
	 *
	 * @return die entfernten Core-DTOs
	 */
	public List<LehrerPersonalabschnittsdatenAnrechnungsstunden> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final var entities = kontext.fetch(ids);
			final var result = entities.stream().map(this::toApi).toList();
			kontext.delete(entities);
			return result;
		});
	}

}
