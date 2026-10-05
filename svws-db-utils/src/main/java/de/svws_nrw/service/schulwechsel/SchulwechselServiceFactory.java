package de.svws_nrw.service.schulwechsel;

import de.svws_nrw.mapper.schulwechsel.abgang.SchulwechselAbgangMapper;
import de.svws_nrw.mapper.schulwechsel.dokument.SchulwechselDokumentMapper;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schulwechsel.SchulwechselRepositoryFactory;
import de.svws_nrw.service.schulwechsel.abgang.SchulwechselAbgangService;
import de.svws_nrw.service.schulwechsel.dokument.SchulwechselDokumentService;

/**
 * Factory für {@link SchulwechselAbgangService}-Instanzen.
 */
public final class SchulwechselServiceFactory {

	private final SchulwechselRepositoryFactory repoFactory;
	private final SchuelerRepositoryFactory schuelerRepoFactory;

	private SchulwechselServiceFactory(
			final SchulwechselRepositoryFactory repoFactory,
			final SchuelerRepositoryFactory schuelerRepoFactory) {
		this.repoFactory = repoFactory;
		this.schuelerRepoFactory = schuelerRepoFactory;
	}

	/**
	 * Erstellt eine neue Instanz mit expliziten Abhängigkeiten (für Tests).
	 *
	 * @param schuelerRepoFactory die Repository-Factory für das Schüler-Repository
	 * @param repoFactory 		  die Repository-Factory
	 * @return eine neue {@link SchulwechselServiceFactory}
	 */
	public static SchulwechselServiceFactory getNewInstance(
			final SchulwechselRepositoryFactory repoFactory,
			final SchuelerRepositoryFactory schuelerRepoFactory) {
		return new SchulwechselServiceFactory(repoFactory, schuelerRepoFactory);
	}

	/**
	 * Erstellt eine neue Instanz mit Produktiv-Defaults.
	 *
	 * @return eine neue {@link SchulwechselServiceFactory}
	 */
	public static SchulwechselServiceFactory getNewInstance() {
		return new SchulwechselServiceFactory(SchulwechselRepositoryFactory.getNewInstance(), SchuelerRepositoryFactory.getNewInstance());
	}

	/**
	 * Erstellt einen neuen {@link SchulwechselAbgangService}.
	 *
	 * @return ein neuer Service
	 */
	public SchulwechselAbgangService getSchulwechselAbgangService() {
		return new SchulwechselAbgangService(
				this.repoFactory.getSchulwechselAbgangRepository(),
				schuelerRepoFactory.getSchuelerRepository(),
				SchulwechselAbgangMapper.INSTANCE
		);
	}

	/**
	 * Erstellt einen neuen {@link SchulwechselDokumentService}.
	 *
	 * @return ein neuer Service
	 */
	public SchulwechselDokumentService getSchulwechselDokumentService() {
		return new SchulwechselDokumentService(
				this.repoFactory.getSchulwechselDokumentRepository(),
				SchulwechselDokumentMapper.INSTANCE
		);
	}
}
