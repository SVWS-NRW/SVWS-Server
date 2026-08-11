package de.svws_nrw.service.lehrer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAnrechnungsstunde;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.lehrer.anrechnung.LehrerAnrechnungRepository;
import de.svws_nrw.repo.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse stellt einen Daten-Kontext für den Service {@link LehrerAnrechnungsstundenService} bereit.
 */
public final class LehrerAnrechnungsstundenServiceKontext {

	private final SchuljahresabschnitteRepository schuljahresabschnitteRepository;
	private final LehrerPersonalabschnittsdatenRepository lehrerAbschnittsdatenRepository;
	private final LehrerAnrechnungRepository lehrerAnrechnungRepository;

	private Map<Long, DTOLehrerAnrechnungsstunde> mapAnrechnungsstunden = new HashMap<>();
	private Map<Long, DTOLehrerAbschnittsdaten> mapAbschnittsdaten = new HashMap<>();
	private Map<Long, DTOSchuljahresabschnitte> mapSchuljahresabschnitte = new HashMap<>();

	private LehrerAnrechnungsstundenServiceKontext(
			final SchuljahresabschnitteRepository schuljahresabschnitteRepository,
			final LehrerPersonalabschnittsdatenRepository lehrerAbschnittsdatenRepository,
			final LehrerAnrechnungRepository lehrerAnrechnungRepository) {
		this.schuljahresabschnitteRepository = schuljahresabschnitteRepository;
		this.lehrerAbschnittsdatenRepository = lehrerAbschnittsdatenRepository;
		this.lehrerAnrechnungRepository = lehrerAnrechnungRepository;
	}

	/**
	 * Erstellt einen neuen Service-Kontext.
	 *
	 * @param schuljahresabschnitteRepository   das Repository für die Schuljahresabschnitte
	 * @param lehrerAbschnittsdatenRepository   das Repository für die Abschnittsdaten
	 * @param lehrerAnrechnungRepository        das Repository für die Anrechnungsstunden
	 *
	 * @return der neue Service-Kontext.
	 */
	public static LehrerAnrechnungsstundenServiceKontext of(final SchuljahresabschnitteRepository schuljahresabschnitteRepository,
			final LehrerPersonalabschnittsdatenRepository lehrerAbschnittsdatenRepository,
			final LehrerAnrechnungRepository lehrerAnrechnungRepository) {
		return new LehrerAnrechnungsstundenServiceKontext(schuljahresabschnitteRepository, lehrerAbschnittsdatenRepository, lehrerAnrechnungRepository);
	}


	/**
	 * Persistiert die Daten aus dem Cache der Anrechnungsstunden
	 *
	 * @param entities   die zu persistierenden Entitäten
	 */
	public void persist(final List<DTOLehrerAnrechnungsstunde> entities) {
		lehrerAnrechnungRepository.update(entities);
		lehrerAnrechnungRepository.flush();
	}

	/**
	 * Löscht alle Entitäten, die im Cache sind aus der Datenbank
	 *
	 * @param entities   die zu löschenden Entitäten
	 */
	public void delete(final List<DTOLehrerAnrechnungsstunde> entities) {
		lehrerAnrechnungRepository.delete(entities);
	}

	/**
	 * Erstellt eine List mit neuen Entitäten passend in der Reihenfolge zu der Reihenfolge in der Liste der Create-Requests.
	 * Führt außerdem ein preFetch aus, um die zugehörigen Daten aus der Datenbnak für das Mapping über diesen Kontext bereitzustellen.
	 *
	 * @param patches   die Create-Patches
	 *
	 * @return die Liste mit den neuen Entitäten
	 */
	public List<DTOLehrerAnrechnungsstunde> create(final Collection<LehrerAnrechnungsstundenCreateRequest> patches) {
		long nextId = lehrerAnrechnungRepository.getNextID();
		final List<DTOLehrerAnrechnungsstunde> result = new ArrayList<>();
		for (final LehrerAnrechnungsstundenCreateRequest patch : patches) {
			final var neu = new DTOLehrerAnrechnungsstunde(nextId++, patch.idAbschnittsdaten);
			result.add(neu);
		}
		this.preFetch(result);
		lehrerAnrechnungRepository.flush();
		return result;
	}


	private void preFetch(final List<DTOLehrerAnrechnungsstunde> listAnrechnungsstunden) {
		final var idsAbschnitte = listAnrechnungsstunden.stream().map(a -> a.Abschnitt_ID).collect(Collectors.toSet());
		final var listAbschnitte = lehrerAbschnittsdatenRepository.findListByIds(idsAbschnitte);
		if (listAbschnitte.size() != idsAbschnitte.size()) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Es wurden nicht alle Lehrer-Abschhnittdaten zu den IDs gefunden (%d von %d).".formatted(listAbschnitte.size(), idsAbschnitte.size()));
		}
		mapAbschnittsdaten = listAbschnitte.stream().collect(Collectors.toMap(e -> e.ID, e -> e));

		// ... und mit Schuljahresabschnitten.
		mapSchuljahresabschnitte = schuljahresabschnitteRepository.getAll().stream().collect(Collectors.toMap(e -> e.ID, e -> e));
	}

	/**
	 * Lädt alle für das Mapping benötigten Daten anhand der übergebenen Abschnitt-IDs in den Cache.
	 *
	 * @param idsAbschnitte die IDs der Lehrerabschnittsdaten
	 */
	public void preFetchByAbschnittIds(final Collection<Long> idsAbschnitte) {
		final var listAbschnitte = lehrerAbschnittsdatenRepository.findListByIds(idsAbschnitte);
		mapAbschnittsdaten = listAbschnitte.stream().collect(Collectors.toMap(e -> e.ID, e -> e));
		mapSchuljahresabschnitte = schuljahresabschnitteRepository.getAll().stream()
				.collect(Collectors.toMap(e -> e.ID, e -> e));
	}

	/**
	 * Lädt alle Anrechnungsstunden gruppiert nach Abschnittsdaten-ID.
	 *
	 * @param idsAbschnitte die IDs der Lehrerabschnittsdaten
	 * @return Map von Abschnittsdaten-ID auf Liste der zugehörigen DTOs
	 */
	public Map<Long, List<DTOLehrerAnrechnungsstunde>> fetchMapByAbschnittIds(final Collection<Long> idsAbschnitte) {
		final var result = lehrerAnrechnungRepository.getListByIdLehrerAbschnittsdaten(idsAbschnitte);
		preFetchByAbschnittIds(idsAbschnitte);
		return result;
	}


	/**
	 * Führt eine Anfrage auf das Repository der Lehrer-Anrechnungsstunden mit den übergebenen IDs aus.
	 *
	 * @param ids   die IDs
	 *
	 * @return die gesuchten Entitäten, sofern entweder im Cache oder aus der Datenbank geladen
	 */
	public List<DTOLehrerAnrechnungsstunde> fetch(final Collection<Long> ids) {
		// Lade die Anrechnungsstunden ...
		final var result = lehrerAnrechnungRepository.findListByIds(ids);
		if (result.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle Anrechnungsstunden zu den IDs gefunden (%d von %d).".formatted(result.size(), ids.size()));
		}
		mapAnrechnungsstunden = result.stream().collect(Collectors.toMap(e -> e.ID, e -> e));

		// ... und mache ein pre-Fetch für die Daten, die für ein Mapping benötigt werden.
		preFetch(result);
		return result;
	}



	/**
	 * Führt eine Anfrage auf das Repository der Lehrer-Anrechnungsstunden mit der übergebenen ID von Lehrer-Abschnittsdaten aus.
	 *
	 * @param idLehrerabschnittsdaten   die ID der Lehrer-Abschnittsdaten
	 *
	 * @return die gesuchten Entitäten, sofern entweder im Cache odr aus der Datenbank geladen
	 */
	public List<DTOLehrerAnrechnungsstunde> fetchByLehrerabschnittsdatenId(final long idLehrerabschnittsdaten) {
		// Lade die Anrechnungsstunden ...
		final List<DTOLehrerAnrechnungsstunde> result =
				lehrerAnrechnungRepository.getMapByAbschnitt(List.of(idLehrerabschnittsdaten)).getOrDefault(idLehrerabschnittsdaten, List.of());
		mapAnrechnungsstunden = result.stream().collect(Collectors.toMap(e -> e.ID, e -> e));

		// ... und mache ein pre-Fetch für die Daten, die für ein Mapping benötigt werden.
		preFetch(result);
		return result;
	}


	/**
	 * Gibt die Anrechnungsstunden für die übergebene ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return die Anrechnungsstunden
	 */
	public DTOLehrerAnrechnungsstunde getAnrechnungsstunden(final long id) {
		return mapAnrechnungsstunden.get(id);
	}


	/**
	 * Gibt die Lehrerabschnittsdaten für die übergebene ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return die Lehrerabschnittdaten
	 */
	public DTOLehrerAbschnittsdaten getAbschnitt(final long id) {
		return mapAbschnittsdaten.get(id);
	}


	/**
	 * Gibt den Schuljahresabschnitt für die übergebene ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return der Schuljahresabschnitt
	 */
	public DTOSchuljahresabschnitte getSchuljahresabschnitt(final long id) {
		return mapSchuljahresabschnitte.get(id);
	}

}
