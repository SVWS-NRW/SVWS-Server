package de.svws_nrw.service.lehrer.stammdaten;

import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.schule.kataloge.ort.OrtRepository;
import de.svws_nrw.repo.schule.kataloge.ortsteil.OrtsteilRepository;
import de.svws_nrw.repo.schule.schulleitung.SchulleitungRepository;

/**
 * Bündelt die Repositories, die der {@link LehrerStammdatenService} benötigt.
 *
 * @param lehrerRepository         das Repository für die Lehrer
 * @param schulleitungRepository   das Repository für die Schulleitungseinträge
 * @param ortRepository            das Repository für den Orte-Katalog
 * @param ortsteilRepository       das Repository für den Ortsteile-Katalog
 */
public record LehrerStammdatenRepositories(
		LehrerRepository lehrerRepository,
		SchulleitungRepository schulleitungRepository,
		OrtRepository ortRepository,
		OrtsteilRepository ortsteilRepository
) { }
