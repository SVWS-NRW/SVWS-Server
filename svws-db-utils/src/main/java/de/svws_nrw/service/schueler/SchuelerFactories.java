package de.svws_nrw.service.schueler;

import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.gost.GostRepositoryFactory;
import de.svws_nrw.repo.kurse.KurseRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import de.svws_nrw.service.schule.katalog.KatalogServiceFactory;

/**
 * Bündelt die Factories, die die {@link SchuelerServiceFactory} von außen benötigt.
 *
 * @param benutzerRepositoryFactory       die Factory für Benutzer-Repositories
 * @param schuelerRepositoryFactory       die Factory für Schüler-Repositories
 * @param katalogRepositoryFactory        die Factory für Katalog-Repositories
 * @param katalogServiceFactory           die Factory für Katalog-Services
 * @param eigeneSchuleRepositoryFactory   die Factory für Repositories der eigenen Schule
 * @param eigeneSchuleServiceFactory      die Factory für Services der eigenen Schule
 * @param kurseRepositoryFactory          die Factory für Kurs-Repositories
 * @param gostRepositoryFactory           die Factory für Gost-Repositories
 */
public record SchuelerFactories(
		BenutzerRepositoryFactory benutzerRepositoryFactory,
		SchuelerRepositoryFactory schuelerRepositoryFactory,
		KatalogRepositoryFactory katalogRepositoryFactory,
		KatalogServiceFactory katalogServiceFactory,
		EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory,
		EigeneSchuleServiceFactory eigeneSchuleServiceFactory,
		KurseRepositoryFactory kurseRepositoryFactory,
		GostRepositoryFactory gostRepositoryFactory) {
}
