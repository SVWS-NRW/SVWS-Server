package de.svws_nrw.service.enm;

import de.svws_nrw.repo.enm.NotenmodulRepositoryFactory;
import de.svws_nrw.repo.kataloge.KatalogeRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;

/**
 * Eine Factory zum Erstellen von Services für das Notenmodul
 */
public final class NotenmodulServiceFactory {

	private final NotenmodulRepositoryFactory notenmodulRepositoryFactory;
	private final EnmV2ServiceFactory enmV2ServiceFactory;
	private final LehrerRepositoryFactory lehrerRepositoryFactory;
	private final SchuelerRepositoryFactory schuelerRepositoryFactory;
	private final KatalogeRepositoryFactory katalogeRepositoryFactory;

	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param notenmodulRepositoryFactory   die Factory für Notenmodul-Repositories
	 * @param enmV2ServiceFactory           die Service-Factory für Services zu ENM-Daten in der Version 2
	 * @param lehrerRepositoryFactory       die Factory für Lehrer-Repositories
	 * @param schuelerRepositoryFactory     die Factory für Schüler-Repositories
	 * @param katalogeRepositoryFactory     die Factory für Katalog-Repositories
	 */
	private NotenmodulServiceFactory(final NotenmodulRepositoryFactory notenmodulRepositoryFactory, final EnmV2ServiceFactory enmV2ServiceFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory, final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final KatalogeRepositoryFactory katalogeRepositoryFactory) {
		this.notenmodulRepositoryFactory = notenmodulRepositoryFactory;
		this.enmV2ServiceFactory = enmV2ServiceFactory;
		this.lehrerRepositoryFactory = lehrerRepositoryFactory;
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
		this.katalogeRepositoryFactory = katalogeRepositoryFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param notenmodulRepositoryFactory   die Factory für Notenmodul-Repositories
	 * @param enmV2ServiceFactory           die Service-Factory für Services zu ENM-Daten in der Version 2
	 * @param lehrerRepositoryFactory       die Factory für Lehrer-Repositories
	 * @param schuelerRepositoryFactory     die Factory für Schüler-Repositories
	 * @param katalogeRepositoryFactory     die Factory für Katalog-Repositories
	 *
	 * @return die neue Factory-Instanz
	 */
	public static NotenmodulServiceFactory getNewInstance(final NotenmodulRepositoryFactory notenmodulRepositoryFactory,
			final EnmV2ServiceFactory enmV2ServiceFactory, final LehrerRepositoryFactory lehrerRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory, final KatalogeRepositoryFactory katalogeRepositoryFactory) {
		return new NotenmodulServiceFactory(notenmodulRepositoryFactory, enmV2ServiceFactory, lehrerRepositoryFactory, schuelerRepositoryFactory,
				katalogeRepositoryFactory);
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Notenmodul-Verbindungen
	 *
	 * @return der Service
	 */
	public NotenmodulVerbindungenService getNotenmodulVerbindungenService() {
		return new NotenmodulVerbindungenService(notenmodulRepositoryFactory.getNotenmodulVerbindungenRepository());
	}


	/**
	 * Erstellt einen Service für die Synchronisation mit einem externen Notenmodul-Server
	 * über eine vorkonfigurierte Notenmodul-Verbindung.
	 *
	 * @return der Service
	 */
	public NotenmodulSynchronisationService getNotenmodulSynchronisationService() {
		return new NotenmodulSynchronisationService(notenmodulRepositoryFactory.getNotenmodulVerbindungenRepository(),
				enmV2ServiceFactory.getEnmV2GetService(),
				enmV2ServiceFactory.getEnmV2ImportService(),
				this.getNotenmodulCredentialsService()
		);
	}

	/**
	 * Erstellt einen Service für die Verwaltung der Notenmodul-Credentials (welche bei externen Notenmodulen eingesetzt werden).
	 *
	 * @return der Service
	 */
	public NotenmodulCredentialsService getNotenmodulCredentialsService() {
		return new NotenmodulCredentialsService(notenmodulRepositoryFactory.getNotenmodulCredentialsRepository(),
				lehrerRepositoryFactory.getLehrerRepository(),
				enmV2ServiceFactory.getEnmV2GetService()
		);
	}

	/**
	 * Erstellt einen Service für die Zugriffe auf das lokale Notenmodul.
	 *
	 * @return der Service
	 */
	public NotenmodulLocalService getNotenmodulLocalService() {
		return new NotenmodulLocalService(notenmodulRepositoryFactory.getNotenmodulKonfigurationClientRepository(),
				notenmodulRepositoryFactory.getNotenmodulKonfigurationServerRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittBemerkungenRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenRepository(),
				schuelerRepositoryFactory.getSchuelerTeilleistungenRepository(),
				schuelerRepositoryFactory.getSchuelerAnkreuzkompetenzenRepository(),
				katalogeRepositoryFactory.getAnkreuzkompetenzenRepository()
		);
	}

}
