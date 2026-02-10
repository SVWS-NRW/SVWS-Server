package de.svws_nrw.data.statistik;

import de.svws_nrw.data.lehrer.LehrerServiceFactory;
import de.svws_nrw.data.schule.SchuleServiceFactory;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.kataloge.KatalogeRepositoryFactory;
import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.SchuleRepositoryFactory;

/**
 * Die Default-Implementierung einer Controller-Factory für den Bereich der Statistik
 */
public final class StatistikControllerFactoryImpl implements StatistikControllerFactory {

	/** Die Service-Factory für die Statistik */
	private final StatistikServiceFactory serviceFactory;


	/**
	 * Erzeugt eine neue Factory für die übergebene Datenbank-Verbindung.
	 * Der Konstruktor ist package private und sollte nur von einer Default-Methode
	 * im Interface aufgerufen werden.
	 */
	StatistikControllerFactoryImpl() {
		final var lehrerRepositoryFactory = LehrerRepositoryFactory.getNewInstance();
		final var schuleRepositoryFactory = SchuleRepositoryFactory.getNewInstance();
		final var lehrerServiceFactory = LehrerServiceFactory.getNewInstance(lehrerRepositoryFactory, schuleRepositoryFactory);
		final var schuleServiceFactory = SchuleServiceFactory.getNewInstance(schuleRepositoryFactory);
		this.serviceFactory = StatistikServiceFactory.getNewInstance(
				KatalogeRepositoryFactory.getNewInstance(),
				KlassenRepositoryFactory.getNewInstance(),
				lehrerRepositoryFactory,
				SchuelerRepositoryFactory.getNewInstance(),
				schuleRepositoryFactory,
				lehrerServiceFactory,
				schuleServiceFactory);
	}


	@Override
	public StatistikController getControllerStatistikGesamt() throws ApiOperationException {
		final StatistikService service = serviceFactory.getStatistikService();
		return new StatistikControllerImpl(service);
	}

}
