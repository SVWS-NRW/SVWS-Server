package de.svws_nrw.repo.gost.klausurplan;

import de.svws_nrw.repo.RepositoryFactory;

/**
 * Eine Factory zum Erstellen von Repositories für die GOSt-Klausurplanung.
 */
public final class GostKlausurenRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz.
	 *
	 * @return die neue Factory
	 */
	public static GostKlausurenRepositoryFactory getNewInstance() {
		return new GostKlausurenRepositoryFactory();
	}

	/**
	 * Liefert eine Instanz des {@link GostKlausurenVorgabeRepository}.
	 *
	 * @return eine Instanz des {@link GostKlausurenVorgabeRepository}
	 */
	public GostKlausurenVorgabeRepository getGostKlausurenVorgabeRepository() {
		return getOrCreate(GostKlausurenVorgabeRepository.class, () -> new GostKlausurenVorgabeRepositoryImpl(conn));
	}

}
