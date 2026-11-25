package de.svws_nrw.service.schueler.lernabschnitt;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittBemerkungen;
import de.svws_nrw.core.utils.CollectionUtils;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOFachklassen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.mapper.schueler.leistungsdaten.SchuelerLeistungsdatenMapper;
import de.svws_nrw.mapper.schueler.lernabschnittsdaten.SchuelerLernabschnittBemerkungenMapper;
import de.svws_nrw.mapper.schueler.lernabschnittsdaten.SchuelerLernabschnittsdatenMappingContext;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittBemerkungRepository;
import de.svws_nrw.repo.schule.kataloge.fachklasse.FachklasseRepository;


/**
 * Lädt die Zusatzdaten (Bemerkungen, Fachklassen und ggf. Leistungsdaten) für mehrere Lernabschnitte mit jeweils einer
 * Abfrage und stellt sie als {@link SchuelerLernabschnittsdatenMappingContext} für den Mapper bereit.
 */
public final class SchuelerLernabschnittMappingContextLoader {

	private final SchuelerLernabschnittBemerkungRepository bemerkungRepository;
	private final SchuelerLeistungsdatenRepository leistungsdatenRepository;
	private final SchuelerLeistungsdatenMapper leistungsdatenMapper;
	private final SchuelerLernabschnittBemerkungenMapper bemerkungenMapper;
	private final FachklasseRepository fachklasseRepository;

	/**
	 * Erstellt einen neuen Loader für die Mapping-Kontexte der Lernabschnitte.
	 *
	 * @param bemerkungRepository        das Repository für die Bemerkungen
	 * @param leistungsdatenRepository   das Repository für die Leistungsdaten
	 * @param leistungsdatenMapper       der Mapper für die Leistungsdaten
	 * @param bemerkungenMapper          der Mapper für die Bemerkungen
	 * @param fachklasseRepository       das Repository für die Fachklassen (schulinterner Katalog, BK)
	 */
	public SchuelerLernabschnittMappingContextLoader(
			final SchuelerLernabschnittBemerkungRepository bemerkungRepository,
			final SchuelerLeistungsdatenRepository leistungsdatenRepository,
			final SchuelerLeistungsdatenMapper leistungsdatenMapper,
			final SchuelerLernabschnittBemerkungenMapper bemerkungenMapper,
			final FachklasseRepository fachklasseRepository) {
		this.bemerkungRepository = bemerkungRepository;
		this.leistungsdatenRepository = leistungsdatenRepository;
		this.leistungsdatenMapper = leistungsdatenMapper;
		this.bemerkungenMapper = bemerkungenMapper;
		this.fachklasseRepository = fachklasseRepository;
	}

	/**
	 * Erstellt für jeden Lernabschnitt den Mapping-Kontext mit den bereits gemappten Bemerkungen und Leistungsdaten
	 * sowie der Kennung der zugeordneten Fachklasse.
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

		final var zusatzdaten = loadZusatzdaten(entities, mitLeistungsdaten);

		return entities.stream()
				.collect(Collectors.toMap(
						entity -> entity.ID,
						entity -> createContext(entity, zusatzdaten, schuljahrByIdAbschnitt.get(entity.Schuljahresabschnitts_ID))
				));
	}

	private Zusatzdaten loadZusatzdaten(final List<DTOSchuelerLernabschnittsdaten> entities, final boolean mitLeistungsdaten) {
		final var ids = entities.stream()
				.map(entity -> entity.ID)
				.toList();
		return new Zusatzdaten(
				bemerkungRepository.findMapByLernabschnittID(ids),
				loadLeistungsdaten(ids, mitLeistungsdaten),
				loadFachklassen(entities));
	}

	private Map<Long, DTOFachklassen> loadFachklassen(final List<DTOSchuelerLernabschnittsdaten> entities) {
		final var idsFachklassen = entities.stream()
				.map(entity -> entity.Fachklasse_ID)
				.filter(Objects::nonNull)
				.distinct()
				.toList();
		return fachklasseRepository.findMapByIds(idsFachklassen);
	}

	private Map<Long, List<DTOSchuelerLeistungsdaten>> loadLeistungsdaten(final List<Long> ids, final boolean mitLeistungsdaten) {
		return mitLeistungsdaten ? leistungsdatenRepository.getMapListByLernabschnittsIds(ids) : Collections.emptyMap();
	}

	private SchuelerLernabschnittsdatenMappingContext createContext(final DTOSchuelerLernabschnittsdaten entity,
			final Zusatzdaten zusatzdaten, final int schuljahr) {
		final var bemerkung = zusatzdaten.bemerkungenById().get(entity.ID);
		final var bemerkungen = (bemerkung == null) ? new SchuelerLernabschnittBemerkungen() : bemerkungenMapper.toApi(bemerkung);
		final var leistungen = zusatzdaten.leistungsdatenById().getOrDefault(entity.ID, List.of()).stream()
				.map(dto -> leistungsdatenMapper.toApi(dto, schuljahr))
				.toList();
		return new SchuelerLernabschnittsdatenMappingContext(bemerkungen, leistungen, zusatzdaten.getKennungFachklasse(entity));
	}

	/**
	 * Die gebündelt geladenen Zusatzdaten aller Lernabschnitte eines Aufrufs von {@link #load(List, Map, boolean)}.
	 *
	 * @param bemerkungenById      die Bemerkungen je Lernabschnitt-ID
	 * @param leistungsdatenById   die Leistungsdaten je Lernabschnitt-ID, leer wenn sie nicht geladen wurden
	 * @param fachklassenById      die Fachklassen des schulinternen Katalogs je Fachklassen-ID
	 */
	private record Zusatzdaten(
			Map<Long, DTOSchuelerPSFachBemerkungen> bemerkungenById,
			Map<Long, List<DTOSchuelerLeistungsdaten>> leistungsdatenById,
			Map<Long, DTOFachklassen> fachklassenById) {

		private String getKennungFachklasse(final DTOSchuelerLernabschnittsdaten entity) {
			return Optional.ofNullable(entity.Fachklasse_ID)
					.map(fachklassenById::get)
					.map(fachklasse -> fachklasse.Kennung)
					.orElse(null);
		}

	}

}
