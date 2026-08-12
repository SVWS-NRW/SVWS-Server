package de.svws_nrw.service.schueler.foto;

import de.svws_nrw.mapper.schueler.foto.SchuelerFotoMapper;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;

public final class SchuelerFotoServiceFactory {

	private final SchuelerRepositoryFactory repoFactory;
	private final SchuelerFotoMapper mapper;

	private SchuelerFotoServiceFactory(
			final SchuelerRepositoryFactory repoFactory,
			final SchuelerFotoMapper mapper) {
		this.repoFactory = repoFactory;
		this.mapper = mapper;
	}

	/**
	 * Erstellt eine neue Instanz der {@code SchuelerFotoServiceFactory}.
	 *
	 * @param repoFactory das Repository-Factory für {@link SchuelerFotoService}-Instanzen
	 * @param mapper      der Mapper zur Konvertierung zwischen Entity und Domain
	 * @return eine neue {@code SchuelerFotoServiceFactory}
	 */
	public static SchuelerFotoServiceFactory getNewInstance(
			final SchuelerRepositoryFactory repoFactory,
			final SchuelerFotoMapper mapper) {
		return new SchuelerFotoServiceFactory(repoFactory, mapper);
	}

	/**
	 * Erstellt eine neue Instanz der {@code SchuelerFotoServiceFactory}
	 * mit den Standard-Abhängigkeiten.
	 *
	 * @return eine neue {@code SchuelerFotoServiceFactory}
	 */
	public static SchuelerFotoServiceFactory getNewInstance() {
		return new SchuelerFotoServiceFactory(
				SchuelerRepositoryFactory.getNewInstance(),
				SchuelerFotoMapper.INSTANCE
		);
	}

	/**
	 * Erstellt eine neue Instanz des {@link SchuelerFotoService}.
	 *
	 * @return ein neuer {@code SchuelerFotoService} mit allen erforderlichen Abhängigkeiten
	 */
	public SchuelerFotoService getSchuelerFotoService() {
		return new SchuelerFotoService(
				repoFactory.getSchuelerFotoRepository(),
				mapper
		);
	}
}
