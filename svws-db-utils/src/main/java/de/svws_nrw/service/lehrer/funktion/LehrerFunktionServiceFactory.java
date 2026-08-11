package de.svws_nrw.service.lehrer.funktion;

import de.svws_nrw.mapper.lehrer.LehrerFunktionMapper;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.lehrer.funktion.LehrerFunktionRepository;

public final class LehrerFunktionServiceFactory {

	private final LehrerRepositoryFactory repoFactory;
	private final LehrerFunktionMapper mapper;

	private LehrerFunktionServiceFactory(
			final LehrerRepositoryFactory repoFactory,
			final LehrerFunktionMapper mapper) {
		this.repoFactory = repoFactory;
		this.mapper = mapper;
	}

	/**
	 * Erstellt eine neue Instanz der {@code LehrerFunktionServiceFactory} mit den angegebenen Abhängigkeiten.
	 *
	 * @param repoFactory                  die Factory für das {@link LehrerFunktionRepository}
	 * @param mapper                       der Mapper zur Konvertierung zwischen Entity und API-Modell
	 * @return eine neue {@code LehrerFunktionServiceFactory}
	 */
	public static LehrerFunktionServiceFactory getNewInstance(
			final LehrerRepositoryFactory repoFactory,
			final LehrerFunktionMapper mapper) {
		return new LehrerFunktionServiceFactory(repoFactory, mapper);
	}

	/**
	 * Erstellt eine neue Instanz der {@code LehrerFunktionServiceFactory} mit Standardabhängigkeiten.
	 *
	 * @return eine neue {@code LehrerFunktionServiceFactory}
	 */
	public static LehrerFunktionServiceFactory getNewInstance() {
		return new LehrerFunktionServiceFactory(
				LehrerRepositoryFactory.getNewInstance(),
				LehrerFunktionMapper.INSTANCE
		);
	}

	/**
	 * Erstellt einen neuen {@link LehrerFunktionService}.
	 *
	 * @return ein neuer {@code LehrerFunktionService}
	 */
	public LehrerFunktionService getLehrerFunktionService() {
		return new LehrerFunktionService(
				repoFactory.getLehrerFunktionRepository(),
				repoFactory.getLehrerPersonalabschnittsdatenRepository(),
				repoFactory.getLeitungsfunktionRepository(),
				mapper
		);
	}
}
