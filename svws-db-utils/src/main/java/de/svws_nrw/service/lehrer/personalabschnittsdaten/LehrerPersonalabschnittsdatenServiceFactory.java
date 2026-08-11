package de.svws_nrw.service.lehrer.personalabschnittsdaten;

import de.svws_nrw.mapper.lehrer.LehrerPersonalabschnittsdatenMapper;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schule.SchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.schulen.SchulenRepository;
import de.svws_nrw.repo.schule.kataloge.schulen.SchulenRepositoryFactory;
import de.svws_nrw.service.lehrer.LehrerServiceFactory;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionService;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionServiceFactory;

/**
 * Factory für den {@link LehrerPersonalabschnittsdatenService}.
 */
public final class LehrerPersonalabschnittsdatenServiceFactory {

	private final LehrerRepositoryFactory lehrerRepoFactory;
	private final SchulenRepositoryFactory schulenRepoFactory;
	private final SchuleRepositoryFactory schuleRepositoryFactory;
	private final LehrerServiceFactory lehrerServiceFactory;
	private final LehrerFunktionServiceFactory lehrerFunktionServiceFactory;
	private final LehrerPersonalabschnittsdatenMapper mapper;

	private LehrerPersonalabschnittsdatenServiceFactory(
			final LehrerRepositoryFactory lehrerRepoFactory,
			final SchulenRepositoryFactory schulenRepoFactory,
			final SchuleRepositoryFactory schuleRepositoryFactory,
			final LehrerServiceFactory lehrerServiceFactory,
			final LehrerFunktionServiceFactory lehrerFunktionServiceFactory,
			final LehrerPersonalabschnittsdatenMapper mapper) {
		this.lehrerRepoFactory = lehrerRepoFactory;
		this.schulenRepoFactory = schulenRepoFactory;
		this.schuleRepositoryFactory = schuleRepositoryFactory;
		this.lehrerServiceFactory = lehrerServiceFactory;
		this.lehrerFunktionServiceFactory = lehrerFunktionServiceFactory;
		this.mapper = mapper;
	}

	/**
	 * Erstellt eine neue Instanz der {@code LehrerPersonalabschnittsdatenServiceFactory} mit den angegebenen Abhängigkeiten.
	 *
	 * @param lehrerRepoFactory        die Factory für das {@link LehrerRepository}
	 * @param schulenRepoFactory       die Factory für das {@link SchulenRepository}
	 * @param schuleRepositoryFactory  die Factory für das Schule-Repositories (inkl. Schuljahresabschnitte)
	 * @param lehrerServiceFactory     die Factory für die Lehrer-Services
	 * @param funktionServiceFactory   die Factory für den {@link LehrerFunktionService}
	 * @param mapper                   der Mapper zur Konvertierung zwischen Entity und API-Modell
	 * @return eine neue {@code LehrerPersonalabschnittsdatenServiceFactory}
	 */
	public static LehrerPersonalabschnittsdatenServiceFactory getNewInstance(
			final LehrerRepositoryFactory lehrerRepoFactory,
			final SchulenRepositoryFactory schulenRepoFactory,
			final SchuleRepositoryFactory schuleRepositoryFactory,
			final LehrerServiceFactory lehrerServiceFactory,
			final LehrerFunktionServiceFactory funktionServiceFactory,
			final LehrerPersonalabschnittsdatenMapper mapper) {
		return new LehrerPersonalabschnittsdatenServiceFactory(
				lehrerRepoFactory, schulenRepoFactory,
				schuleRepositoryFactory, lehrerServiceFactory,
				funktionServiceFactory, mapper);
	}

	/**
	 * Erstellt eine neue Instanz der {@code LehrerPersonalabschnittsdatenServiceFactory} mit Standardabhängigkeiten.
	 *
	 * @return eine neue {@code LehrerPersonalabschnittsdatenServiceFactory}
	 */
	public static LehrerPersonalabschnittsdatenServiceFactory getNewInstance() {
		final var lehrerRepoFactory = LehrerRepositoryFactory.getNewInstance();
		final var schuleRepositoryFactory = SchuleRepositoryFactory.getNewInstance();
		return new LehrerPersonalabschnittsdatenServiceFactory(
				lehrerRepoFactory,
				SchulenRepositoryFactory.getNewInstance(),
				schuleRepositoryFactory,
				LehrerServiceFactory.getNewInstance(lehrerRepoFactory, schuleRepositoryFactory),
				LehrerFunktionServiceFactory.getNewInstance(),
				LehrerPersonalabschnittsdatenMapper.INSTANCE
		);
	}

	/**
	 * Erstellt einen neuen {@link LehrerPersonalabschnittsdatenService}.
	 *
	 * @return ein neuer {@code LehrerPersonalabschnittsdatenService}
	 */
	public LehrerPersonalabschnittsdatenService getLehrerPersonalabschnittsdatenService() {
		final var subRepos = new LehrerPersonalabschnittsdatenRepos(
				lehrerRepoFactory.getLehrerPersonalabschnittsdatenRepository(),
				lehrerRepoFactory.getLehrerRepository(),
				schulenRepoFactory.getRepo(),
				schuleRepositoryFactory.getSchuljahresabschnitteRepository()
		);

		final var subServices = new LehrerPersonalabschnittsdatenSubServices(
				lehrerServiceFactory.getLehrerAnrechnungsstundenService(),
				lehrerServiceFactory.getLehrerMehrleistungService(),
				lehrerServiceFactory.getLehrerMinderleistungService(),
				lehrerFunktionServiceFactory.getLehrerFunktionService()
		);

		return new LehrerPersonalabschnittsdatenService(subRepos, subServices, mapper);
	}
}
