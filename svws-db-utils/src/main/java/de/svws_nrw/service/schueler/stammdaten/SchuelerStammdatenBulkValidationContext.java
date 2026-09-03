package de.svws_nrw.service.schueler.stammdaten;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.repo.schule.kataloge.fahrschuelerart.FahrschuelerartRepository;
import de.svws_nrw.repo.schule.kataloge.haltestelle.HaltestelleRepository;
import de.svws_nrw.repo.schule.kataloge.ort.OrtRepository;
import de.svws_nrw.repo.schule.kataloge.ortsteil.OrtsteilRepository;
import de.svws_nrw.repo.schule.kataloge.religion.ReligionRepository;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Enthält bulk-geladene Validierungsdaten für {@link SchuelerStammdatenService#patchMultiple}.
 *
 * @param existingOrtIds          Menge der in der DB vorhandenen Ort-IDs
 * @param ortsteilById            Map von Ortsteil-ID auf {@link DTOOrtsteil}
 * @param existingReligionIds     Menge der in der DB vorhandenen Religions-IDs
 * @param existingFahrschuelerartIds Menge der in der DB vorhandenen Fahrschülerart-IDs
 * @param existingHaltestelleIds  Menge der in der DB vorhandenen Haltestellen-IDs
 */
record SchuelerStammdatenBulkValidationContext(
		Set<Long> existingOrtIds,
		Map<Long, DTOOrtsteil> ortsteilById,
		Set<Long> existingReligionIds,
		Set<Long> existingFahrschuelerartIds,
		Set<Long> existingHaltestelleIds
) {

	/**
	 * Erstellt einen {@link SchuelerStammdatenBulkValidationContext} aus den übergebenen Patch-Requests.
	 *
	 * @param dtos                       die Patch-Requests
	 * @param ortRepository              {@link OrtRepository}
	 * @param ortsteilRepository         {@link OrtsteilRepository}
	 * @param religionRepository         {@link ReligionRepository}
	 * @param fahrschuelerartRepository  {@link FahrschuelerartRepository}
	 * @param haltestelleRepository      {@link HaltestelleRepository}
	 *
	 * @return den befüllten {@link SchuelerStammdatenBulkValidationContext}
	 */
	static SchuelerStammdatenBulkValidationContext load(
			final List<? extends SchuelerStammdatenPatchRequest> dtos,
			final OrtRepository ortRepository,
			final OrtsteilRepository ortsteilRepository,
			final ReligionRepository religionRepository,
			final FahrschuelerartRepository fahrschuelerartRepository,
			final HaltestelleRepository haltestelleRepository) {

		final var ortIds = collectPresent(dtos, dto -> dto.wohnortID);
		final var ortsteilIds = collectPresent(dtos, dto -> dto.ortsteilID);
		final var religionIds = collectPresent(dtos, dto -> dto.religionID);
		final var fahrschuelerartIds = collectPresent(dtos, dto -> dto.fahrschuelerArtID);
		final var haltestelleIds = collectPresent(dtos, dto -> dto.haltestelleID);

		final Map<Long, DTOOrtsteil> ortsteilById = ortsteilRepository.findListByIds(ortsteilIds)
				.stream()
				.collect(Collectors.toMap(o -> o.id, o -> o));

		return new SchuelerStammdatenBulkValidationContext(
				ortRepository.existsByIds(ortIds),
				ortsteilById,
				religionRepository.existsByIds(religionIds),
				fahrschuelerartRepository.existsByIds(fahrschuelerartIds),
				haltestelleRepository.existsByIds(haltestelleIds)
		);
	}

	private static Set<Long> collectPresent(
			final List<? extends SchuelerStammdatenPatchRequest> dtos,
			final Function<SchuelerStammdatenPatchRequest, JsonNullable<Long>> extractor) {
		return dtos.stream()
				.map(extractor)
				.filter(JsonNullable::isPresent)
				.map(JsonNullable::get)
				.filter(Objects::nonNull)
				.collect(Collectors.toCollection(HashSet::new));
	}

}
