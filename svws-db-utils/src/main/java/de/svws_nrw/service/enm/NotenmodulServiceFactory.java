package de.svws_nrw.service.enm;

import de.svws_nrw.repo.enm.NotenmodulRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;

/**
 * Eine Factory zum Erstellen von Services für das Notenmodul
 */
public final class NotenmodulServiceFactory {

	private final NotenmodulRepositoryFactory notenmodulRepositoryFactory;
	private final EnmV1ServiceFactory enmV1ServiceFactory;
	private final LehrerRepositoryFactory lehrerRepositoryFactory;
	private final SchuelerRepositoryFactory schuelerRepositoryFactory;

	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param notenmodulRepositoryFactory   die Factory für Notenmodul-Repositories
	 * @param enmV1ServiceFactory           die Service-Factory für Services zu ENM-Daten in der Version 1
	 * @param lehrerRepositoryFactory       die Factory für Lehrer-Repositories
	 * @param schuelerRepositoryFactory     die Factory für Schüler-Repositories
	 */
	private NotenmodulServiceFactory(final NotenmodulRepositoryFactory notenmodulRepositoryFactory, final EnmV1ServiceFactory enmV1ServiceFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory, final SchuelerRepositoryFactory schuelerRepositoryFactory) {
		this.notenmodulRepositoryFactory = notenmodulRepositoryFactory;
		this.enmV1ServiceFactory = enmV1ServiceFactory;
		this.lehrerRepositoryFactory = lehrerRepositoryFactory;
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param notenmodulRepositoryFactory   die Factory für Notenmodul-Repositories
	 * @param enmV1ServiceFactory           die Service-Factory für Services zu ENM-Daten in der Version 1
	 * @param lehrerRepositoryFactory       die Factory für Lehrer-Repositories
	 * @param schuelerRepositoryFactory     die Factory für Schüler-Repositories
	 *
	 * @return die neue Factory-Instanz
	 */
	public static NotenmodulServiceFactory getNewInstance(final NotenmodulRepositoryFactory notenmodulRepositoryFactory,
			final EnmV1ServiceFactory enmV1ServiceFactory, final LehrerRepositoryFactory lehrerRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory) {
		return new NotenmodulServiceFactory(notenmodulRepositoryFactory, enmV1ServiceFactory, lehrerRepositoryFactory, schuelerRepositoryFactory);
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
				enmV1ServiceFactory.getEnmV1GetService(),
				enmV1ServiceFactory.getEnmV1ImportService()
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
				enmV1ServiceFactory.getEnmV1GetService()
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
				schuelerRepositoryFactory.getSchuelerTeilleistungenRepository()
		);
	}

}
