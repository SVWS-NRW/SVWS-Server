package de.svws_nrw.controller.statistik;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.kurse.KurseRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.lehrer.LehrerServiceFactory;
import de.svws_nrw.service.schule.SchuleServiceFactory;
import de.svws_nrw.service.statistik.StatistikService;
import de.svws_nrw.service.statistik.StatistikServiceFactory;

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
		final var schuleRepositoryFactory = EigeneSchuleRepositoryFactory.getNewInstance();
		final var lehrerServiceFactory = LehrerServiceFactory.getNewInstance(lehrerRepositoryFactory, schuleRepositoryFactory);
		final var schuleServiceFactory = SchuleServiceFactory.getNewInstance(schuleRepositoryFactory);
		this.serviceFactory = StatistikServiceFactory.getNewInstance(
				BenutzerRepositoryFactory.getNewInstance(),
				KatalogRepositoryFactory.getNewInstance(),
				KlassenRepositoryFactory.getNewInstance(),
				KurseRepositoryFactory.getNewInstance(),
				lehrerRepositoryFactory,
				SchuelerRepositoryFactory.getNewInstance(),
				schuleRepositoryFactory,
				lehrerServiceFactory,
				schuleServiceFactory);
	}


	@Override
	public StatistikController getControllerStatistikGesamt() throws ApiOperationException {
		final StatistikService service = this.serviceFactory.getStatistikService();
		return new StatistikControllerImpl(service);
	}

}
