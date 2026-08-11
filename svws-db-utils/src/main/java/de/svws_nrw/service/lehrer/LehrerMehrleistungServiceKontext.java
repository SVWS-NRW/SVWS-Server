package de.svws_nrw.service.lehrer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerMehrleistung;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.lehrer.mehrleistung.LehrerMehrleistungRepository;
import de.svws_nrw.repo.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse stellt einen Daten-Kontext für den Service {@link LehrerMehrleistungService} bereit.
 */
public final class LehrerMehrleistungServiceKontext {
	private final SchuljahresabschnitteRepository schuljahresabschnitteRepository;
	private final LehrerPersonalabschnittsdatenRepository lehrerAbschnittsdatenRepository;
	private final LehrerMehrleistungRepository lehrerMehrleistungRepository;

	private Map<Long, DTOLehrerMehrleistung> mapMehrleistungen = new HashMap<>();
	private Map<Long, DTOLehrerAbschnittsdaten> mapLehrerAbschnittsdaten = new HashMap<>();
	private Map<Long, DTOSchuljahresabschnitte> mapSchuljahresabschnitte = new HashMap<>();

	private LehrerMehrleistungServiceKontext(
			final SchuljahresabschnitteRepository schuljahresabschnitteRepository,
			final LehrerPersonalabschnittsdatenRepository lehrerAbschnittsdatenRepository,
			final LehrerMehrleistungRepository lehrerMehrleistungRepository) {
		this.schuljahresabschnitteRepository = schuljahresabschnitteRepository;
		this.lehrerAbschnittsdatenRepository = lehrerAbschnittsdatenRepository;
		this.lehrerMehrleistungRepository = lehrerMehrleistungRepository;
	}

	/**
	 * Erstellt einen neuen Service-Kontext.
	 *
	 * @param schuljahresabschnitteRepository   das Repository für die Schuljahresabschnitte
	 * @param lehrerAbschnittsdatenRepository   das Repository für die Abschnittsdaten
	 * @param lehrerMehrleistungRepository        das Repository für die Mehrleistungen
	 *
	 * @return der neue Service-Kontext.
	 */
	public static LehrerMehrleistungServiceKontext of(
			final SchuljahresabschnitteRepository schuljahresabschnitteRepository,
			final LehrerPersonalabschnittsdatenRepository lehrerAbschnittsdatenRepository,
			final LehrerMehrleistungRepository lehrerMehrleistungRepository) {
		return new LehrerMehrleistungServiceKontext(schuljahresabschnitteRepository, lehrerAbschnittsdatenRepository, lehrerMehrleistungRepository);
	}

	/**
	 * Persistiert die Daten aus dem Cache der Mehrleistungen.
	 *
	 * @param entities die zu persistierenden Entitäten
	 */
	public void persist(final List<DTOLehrerMehrleistung> entities) {
		lehrerMehrleistungRepository.update(entities);
		lehrerMehrleistungRepository.flush();
	}

	/**
	 * Löscht alle Entitäten, die im Cache sind, aus der Datenbank.
	 *
	 * @param entities die zu löschenden Entitäten
	 */
	public void delete(final List<DTOLehrerMehrleistung> entities) {
		lehrerMehrleistungRepository.delete(entities);
	}

	/**
	 * Erstellt eine Liste mit neuen Entitäten passend in der Reihenfolge zu der Reihenfolge in der Liste der Create-Requests.
	 * Führt außerdem ein preFetch aus, um die zugehörigen Daten aus der Datenbank für das Mapping über diesen Kontext bereitzustellen.
	 *
	 * @param patches   die Create-Patches
	 *
	 * @return die Liste mit den neuen Entitäten
	 */
	public List<DTOLehrerMehrleistung> create(final Collection<LehrerMehrleistungCreateRequest> patches) {
		long nextId = lehrerMehrleistungRepository.getNextID();
		final List<DTOLehrerMehrleistung> result = new ArrayList<>();
		for (final LehrerMehrleistungCreateRequest patch : patches) {
			final var neu = new DTOLehrerMehrleistung(nextId++, patch.idAbschnittsdaten, "");
			result.add(neu);
		}
		preFetch(result);
		// Schutz davor, dass der EntityManager nach dem Laden der Kontext-Daten in einem inkonsistenten Zustand ist.
		lehrerMehrleistungRepository.flush();
		return result;
	}

	private void preFetch(final List<DTOLehrerMehrleistung> listMehrleistungen) {
		final var idsAbschnitte = listMehrleistungen.stream().map(a -> a.idAbschnittsdaten).collect(Collectors.toSet());
		final var listAbschnitte = lehrerAbschnittsdatenRepository.findListByIds(idsAbschnitte);
		if (listAbschnitte.size() != idsAbschnitte.size()) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR,
					"Es wurden nicht alle Lehrer-Abschnittsdaten zu den IDs gefunden (%d von %d).".formatted(listAbschnitte.size(), idsAbschnitte.size()));
		}
		mapLehrerAbschnittsdaten = listAbschnitte.stream().collect(Collectors.toMap(e -> e.ID, e -> e));

		// ... und mit Schuljahresabschnitten
		mapSchuljahresabschnitte = schuljahresabschnitteRepository.getAll().stream().collect(Collectors.toMap(e -> e.ID, e -> e));
	}

	/**
	 * Lädt alle für das Mapping benötigten Daten anhand der übergebenen Abschnitt-IDs in den Cache.
	 *
	 * @param idsAbschnitte die IDs der Lehrerabschnittsdaten
	 */
	public void preFetchByAbschnittIds(final Collection<Long> idsAbschnitte) {
		final var listAbschnitte = lehrerAbschnittsdatenRepository.findListByIds(idsAbschnitte);
		mapLehrerAbschnittsdaten = listAbschnitte.stream().collect(Collectors.toMap(e -> e.ID, e -> e));
		mapSchuljahresabschnitte = schuljahresabschnitteRepository.getAll().stream()
				.collect(Collectors.toMap(e -> e.ID, e -> e));
	}

	/**
	 * Lädt alle Mehrleistungen gruppiert nach Abschnittsdaten-ID.
	 *
	 * @param idsAbschnitte die IDs der Lehrerabschnittsdaten
	 * @return Map von Abschnittsdaten-ID auf Liste der zugehörigen DTOs
	 */
	public Map<Long, List<DTOLehrerMehrleistung>> fetchMapByAbschnittIds(final Collection<Long> idsAbschnitte) {
		final var result = lehrerMehrleistungRepository.getListByIdLehrerAbschnittsdaten(idsAbschnitte);
		preFetchByAbschnittIds(idsAbschnitte);
		return result;
	}

	/**
	 * Führt eine Anfrage auf das Repository der Lehrer-Mehrleistungen mit den übergebenen IDs aus.
	 *
	 * @param ids   die IDs
	 *
	 * @return die gesuchten Entitäten, sofern entweder im Cache oder aus der Datenbank geladen
	 */
	public List<DTOLehrerMehrleistung> fetch(final Collection<Long> ids) {
		// Lade die Mehrleistungen ...
		final var result = lehrerMehrleistungRepository.findListByIds(ids);
		if (result.size() != ids.size()) {
			throw new ApiOperationException(Response.Status.NOT_FOUND,
					"Es wurden nicht alle Mehrleistungen zu den IDs gefunden (%d von %d).".formatted(result.size(), ids.size()));
		}
		mapMehrleistungen = result.stream().collect(Collectors.toMap(e -> e.id, e -> e));

		// ... und mache ein pre-Fetch für die Daten, die für ein Mapping benötigt werden.
		preFetch(result);
		return result;
	}

	/**
	 * Führt eine Anfrage auf das Repository der Lehrer-Mehrleistungen mit der übergebenen ID von Lehrer-Abschnittsdaten aus.
	 *
	 * @param idLehrerabschnittsdaten   die ID der Lehrer-Abschnittsdaten
	 *
	 * @return die gesuchten Entitäten, sofern entweder im Cache oder aus der Datenbank geladen
	 */
	public List<DTOLehrerMehrleistung> fetchByLehrerabschnittsdatenId(final long idLehrerabschnittsdaten) {
		// Lade die Mehrleistungen ...
		final List<DTOLehrerMehrleistung> result =
				lehrerMehrleistungRepository.getMapByIdsLehrerAbschnittsdaten(List.of(idLehrerabschnittsdaten)).getOrDefault(idLehrerabschnittsdaten, List.of());
		mapMehrleistungen = result.stream().collect(Collectors.toMap(e -> e.id, e -> e));

		// ... und mache ein pre-Fetch für die Daten, die für ein Mapping benötigt werden.
		preFetch(result);
		return result;
	}

	/**
	 * Gibt die Mehrleistung für die übergebene ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return die Mehrleistung
	 */
	public DTOLehrerMehrleistung getMehrleistung(final long id) {
		return mapMehrleistungen.get(id);
	}

	/**
	 * Gibt die Lehrerabschnittsdaten für die übergebene ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return die Lehrerabschnittsdaten
	 */
	public DTOLehrerAbschnittsdaten getLehrerAbschnitt(final long id) {
		return mapLehrerAbschnittsdaten.get(id);
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
