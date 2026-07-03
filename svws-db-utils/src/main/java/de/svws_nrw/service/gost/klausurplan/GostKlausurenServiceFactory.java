package de.svws_nrw.service.gost.klausurplan;

import de.svws_nrw.repo.gost.GostRepositoryFactory;
import de.svws_nrw.repo.gost.klausurplan.GostKlausurenRepositoryFactory;
import de.svws_nrw.repo.kataloge.KatalogeRepositoryFactory;

/**
 * Eine Factory zum Erstellen der Services für die GOSt-Klausurplanung.
 */
public final class GostKlausurenServiceFactory {

	private final GostKlausurenRepositoryFactory klausurenRepositoryFactory;
	private final GostRepositoryFactory gostRepositoryFactory;
	private final KatalogeRepositoryFactory katalogeRepositoryFactory;

	private GostKlausurenServiceFactory(final GostKlausurenRepositoryFactory klausurenRepositoryFactory,
			final GostRepositoryFactory gostRepositoryFactory, final KatalogeRepositoryFactory katalogeRepositoryFactory) {
		this.klausurenRepositoryFactory = klausurenRepositoryFactory;
		this.gostRepositoryFactory = gostRepositoryFactory;
		this.katalogeRepositoryFactory = katalogeRepositoryFactory;
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory.
	 *
	 * @param klausurenRepositoryFactory die Repository-Factory für die GOSt-Klausurplanung
	 * @param gostRepositoryFactory die Repository-Factory für die GOSt
	 * @param katalogeRepositoryFactory die Repository-Factory für Kataloge
	 *
	 * @return die neue Factory-Instanz
	 */
	public static GostKlausurenServiceFactory getNewInstance(final GostKlausurenRepositoryFactory klausurenRepositoryFactory,
			final GostRepositoryFactory gostRepositoryFactory, final KatalogeRepositoryFactory katalogeRepositoryFactory) {
		return new GostKlausurenServiceFactory(klausurenRepositoryFactory, gostRepositoryFactory, katalogeRepositoryFactory);
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenVorgabeService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenVorgabeService}
	 */
	public GostKlausurenVorgabeService getGostKlausurenVorgabeService() {
		return new GostKlausurenVorgabeService(klausurenRepositoryFactory.getGostKlausurenVorgabeRepository(),
				gostRepositoryFactory.getGostJahrgangsdatenRepository(), katalogeRepositoryFactory.getFachRepository());
	}

}
