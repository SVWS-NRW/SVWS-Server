package de.svws_nrw.service.lehrer.funktion;

import de.svws_nrw.mapper.lehrer.LehrerFunktionMapper;
import de.svws_nrw.repo.lehrer.LehrerAbschnittsdatenRepository;
import de.svws_nrw.repo.lehrer.LehrerAbschnittsdatenRepositoryFactory;
import de.svws_nrw.repo.lehrer.funktion.LehrerFunktionRepository;
import de.svws_nrw.repo.lehrer.funktion.LehrerFunktionRepositoryFactory;
import de.svws_nrw.repo.schule.leitungsfunktion.LehrerLeitungsfunktionRepository;
import de.svws_nrw.repo.schule.leitungsfunktion.LehrerLeitungsfunktionRepositoryFactory;

public final class LehrerFunktionServiceFactory {

	private final LehrerFunktionRepositoryFactory repoFactory;
	private final LehrerAbschnittsdatenRepositoryFactory abschnittsdatenRepoFactory;
	private final LehrerLeitungsfunktionRepositoryFactory leitungsfunktionRepoFactory;
	private final LehrerFunktionMapper mapper;

	private LehrerFunktionServiceFactory(
			final LehrerFunktionRepositoryFactory repoFactory,
			final LehrerAbschnittsdatenRepositoryFactory abschnittsdatenRepoFactory,
			final LehrerLeitungsfunktionRepositoryFactory leitungsfunktionRepoFactory,
			final LehrerFunktionMapper mapper) {
		this.repoFactory = repoFactory;
		this.abschnittsdatenRepoFactory = abschnittsdatenRepoFactory;
		this.leitungsfunktionRepoFactory = leitungsfunktionRepoFactory;
		this.mapper = mapper;
	}

	/**
	 * Erstellt eine neue Instanz der {@code LehrerFunktionServiceFactory} mit den angegebenen Abhängigkeiten.
	 *
	 * @param repoFactory                  die Factory für das {@link LehrerFunktionRepository}
	 * @param abschnittsdatenRepoFactory   die Factory für das {@link LehrerAbschnittsdatenRepository}
	 * @param leitungsfunktionRepoFactory  die Factory für das {@link LehrerLeitungsfunktionRepository}
	 * @param mapper                       der Mapper zur Konvertierung zwischen Entity und API-Modell
	 * @return eine neue {@code LehrerFunktionServiceFactory}
	 */
	public static LehrerFunktionServiceFactory getNewInstance(
			final LehrerFunktionRepositoryFactory repoFactory,
			final LehrerAbschnittsdatenRepositoryFactory abschnittsdatenRepoFactory,
			final LehrerLeitungsfunktionRepositoryFactory leitungsfunktionRepoFactory,
			final LehrerFunktionMapper mapper) {
		return new LehrerFunktionServiceFactory(repoFactory, abschnittsdatenRepoFactory, leitungsfunktionRepoFactory, mapper);
	}

	/**
	 * Erstellt eine neue Instanz der {@code LehrerFunktionServiceFactory} mit Standardabhängigkeiten.
	 *
	 * @return eine neue {@code LehrerFunktionServiceFactory}
	 */
	public static LehrerFunktionServiceFactory getNewInstance() {
		return new LehrerFunktionServiceFactory(
				LehrerFunktionRepositoryFactory.getNewInstance(),
				LehrerAbschnittsdatenRepositoryFactory.getNewInstance(),
				LehrerLeitungsfunktionRepositoryFactory.getNewInstance(),
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
				repoFactory.getRepository(),
				abschnittsdatenRepoFactory.getRepository(),
				leitungsfunktionRepoFactory.getLeitungsfunktionRepository(),
				mapper
		);
	}
}
