package de.svws_nrw.service.schueler.lernabschnitt;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittBemerkungen;
import de.svws_nrw.core.utils.CollectionUtils;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.mapper.schueler.leistungsdaten.SchuelerLeistungsdatenMapper;
import de.svws_nrw.mapper.schueler.lernabschnittsdaten.SchuelerLernabschnittBemerkungenMapper;
import de.svws_nrw.mapper.schueler.lernabschnittsdaten.SchuelerLernabschnittsdatenMappingContext;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittBemerkungRepository;


/**
 * Lädt die Zusatzdaten (Bemerkungen und ggf. Leistungsdaten) für mehrere Lernabschnitte mit jeweils einer Abfrage
 * und stellt sie als {@link SchuelerLernabschnittsdatenMappingContext} für den Mapper bereit.
 */
public final class SchuelerLernabschnittMappingContextLoader {

	private final SchuelerLernabschnittBemerkungRepository bemerkungRepository;
	private final SchuelerLeistungsdatenRepository leistungsdatenRepository;
	private final SchuelerLeistungsdatenMapper leistungsdatenMapper;
	private final SchuelerLernabschnittBemerkungenMapper bemerkungenMapper;

	/**
	 * Erstellt einen neuen Loader für die Mapping-Kontexte der Lernabschnitte.
	 *
	 * @param bemerkungRepository        das Repository für die Bemerkungen
	 * @param leistungsdatenRepository   das Repository für die Leistungsdaten
	 * @param leistungsdatenMapper       der Mapper für die Leistungsdaten
	 * @param bemerkungenMapper          der Mapper für die Bemerkungen
	 */
	public SchuelerLernabschnittMappingContextLoader(
			final SchuelerLernabschnittBemerkungRepository bemerkungRepository,
			final SchuelerLeistungsdatenRepository leistungsdatenRepository,
			final SchuelerLeistungsdatenMapper leistungsdatenMapper,
			final SchuelerLernabschnittBemerkungenMapper bemerkungenMapper) {
		this.bemerkungRepository = bemerkungRepository;
		this.leistungsdatenRepository = leistungsdatenRepository;
		this.leistungsdatenMapper = leistungsdatenMapper;
		this.bemerkungenMapper = bemerkungenMapper;
	}

	/**
	 * Erstellt für jeden Lernabschnitt den Mapping-Kontext mit den bereits gemappten Bemerkungen und Leistungsdaten.
	 *
	 * @param entities                 die Entities der Lernabschnitte
	 * @param schuljahrByIdAbschnitt   die Schuljahre je Schuljahresabschnitt-ID, die alle Abschnitte der Entities enthalten
	 * @param mitLeistungsdaten        gibt an, ob die Leistungsdaten geladen werden sollen
	 *
	 * @return die Map von der Lernabschnitt-ID auf den Mapping-Kontext, leer bei leerer Entity-Liste
	 */
	public Map<Long, SchuelerLernabschnittsdatenMappingContext> load(final List<DTOSchuelerLernabschnittsdaten> entities,
			final Map<Long, Integer> schuljahrByIdAbschnitt, final boolean mitLeistungsdaten) {
		if (CollectionUtils.isEmpty(entities)) {
			return Collections.emptyMap();
		}

		final var ids = entities.stream()
				.map(entity -> entity.ID)
				.toList();
		final var bemerkungenById = bemerkungRepository.findMapByLernabschnittID(ids);
		final var leistungsdatenById = loadLeistungsdaten(ids, mitLeistungsdaten);

		return entities.stream()
				.collect(Collectors.toMap(
						entity -> entity.ID,
						entity -> createContext(
								bemerkungenById.get(entity.ID),
								leistungsdatenById.getOrDefault(entity.ID, List.of()),
								schuljahrByIdAbschnitt.get(entity.Schuljahresabschnitts_ID))
				));
	}

	private Map<Long, List<DTOSchuelerLeistungsdaten>> loadLeistungsdaten(final List<Long> ids, final boolean mitLeistungsdaten) {
		return mitLeistungsdaten ? leistungsdatenRepository.getMapListByLernabschnittsIds(ids) : Collections.emptyMap();
	}

	private SchuelerLernabschnittsdatenMappingContext createContext(final DTOSchuelerPSFachBemerkungen bemerkung,
			final List<DTOSchuelerLeistungsdaten> leistungsdaten, final int schuljahr) {
		final var bemerkungen = (bemerkung == null) ? new SchuelerLernabschnittBemerkungen() : bemerkungenMapper.toApi(bemerkung);
		final var leistungen = leistungsdaten.stream()
				.map(dto -> leistungsdatenMapper.toApi(dto, schuljahr))
				.toList();
		return new SchuelerLernabschnittsdatenMappingContext(bemerkungen, leistungen);
	}

}
