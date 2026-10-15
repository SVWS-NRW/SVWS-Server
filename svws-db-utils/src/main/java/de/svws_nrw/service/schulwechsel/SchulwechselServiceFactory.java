package de.svws_nrw.service.schulwechsel;

import de.svws_nrw.mapper.schulwechsel.abgang.SchulwechselAbgangMapper;
import de.svws_nrw.mapper.schulwechsel.dokument.SchulwechselDokumentMapper;
import de.svws_nrw.mapper.schulwechsel.erfolgreiche_bewerbung.SchulwechselErfolgreicheBewerbungMapper;
import de.svws_nrw.oauth.OAuthHttpClientFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schulwechsel.SchulwechselRepositoryFactory;
import de.svws_nrw.service.schulwechsel.abgang.SchulwechselAbgangService;
import de.svws_nrw.service.schulwechsel.client.SchulbewerbungClientFactory;
import de.svws_nrw.service.schulwechsel.dokument.SchulwechselDokumentService;
import de.svws_nrw.service.schulwechsel.erfolgreiche_bewerbung.SchulwechselErfolgreicheBewerbungService;

/**
 * Factory für {@link SchulwechselAbgangService}-Instanzen.
 */
public final class SchulwechselServiceFactory {

	private final SchulwechselRepositoryFactory repoFactory;
	private final SchuelerRepositoryFactory schuelerRepoFactory;
	private final SchulbewerbungClientFactory clientFactory;

	private SchulwechselServiceFactory(
			final SchulwechselRepositoryFactory repoFactory,
			final SchuelerRepositoryFactory schuelerRepoFactory,
			final SchulbewerbungClientFactory clientFactory) {
		this.repoFactory = repoFactory;
		this.schuelerRepoFactory = schuelerRepoFactory;
		this.clientFactory = clientFactory;
	}

	/**
	 * Erstellt eine neue Instanz mit expliziten Abhängigkeiten (für Tests).
	 *
	 * @param schuelerRepoFactory die Repository-Factory für das Schüler-Repository
	 * @param repoFactory 		  die Repository-Factory
	 * @param clientFactory		  die Client-Factory
	 * @return eine neue {@link SchulwechselServiceFactory}
	 */
	public static SchulwechselServiceFactory getNewInstance(
			final SchulwechselRepositoryFactory repoFactory,
			final SchuelerRepositoryFactory schuelerRepoFactory,
			final SchulbewerbungClientFactory clientFactory) {
		return new SchulwechselServiceFactory(repoFactory, schuelerRepoFactory, clientFactory);
	}

	/**
	 * Erstellt eine neue Instanz mit Produktiv-Defaults.
	 *
	 * @return eine neue {@link SchulwechselServiceFactory}
	 */
	public static SchulwechselServiceFactory getNewInstance() {
		return new SchulwechselServiceFactory(SchulwechselRepositoryFactory.getNewInstance(), SchuelerRepositoryFactory.getNewInstance(),
				SchulbewerbungClientFactory.getNewInstance(OAuthHttpClientFactory.getNewInstance()));
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

	/**
	 * Erstellt einen neuen {@link SchulwechselErfolgreicheBewerbungService}.
	 *
	 * @return ein neuer Service
	 */
	public SchulwechselErfolgreicheBewerbungService getSchulwechselErfolgreicheBewerbungService() {
		return new SchulwechselErfolgreicheBewerbungService(
				this.repoFactory.getSchulwechselErfolgreicheBewerbungRepository(),
				SchulwechselErfolgreicheBewerbungMapper.INSTANCE,
				this.getSchulwechselDokumentService(),
				this.clientFactory.getErfolgreicheBewerbungenClient()
		);
	}
}
