package de.svws_nrw.repo.gost.klausuren;

import de.svws_nrw.repo.RepositoryFactory;

/**
 * Eine Factory zum Erstellen von Repositories für die GOSt-Klausurplanung.
 */
public final class GostKlausurenRepositoryFactory extends RepositoryFactory {

	private GostKlausurenRepositoryFactory() {
		super();
	}

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

	/**
	 * Liefert eine Instanz des {@link GostKlausurenTerminRepository}.
	 *
	 * @return eine Instanz des {@link GostKlausurenTerminRepository}
	 */
	public GostKlausurenTerminRepository getGostKlausurenTerminRepository() {
		return getOrCreate(GostKlausurenTerminRepository.class, () -> new GostKlausurenTerminRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link GostKlausurenKursklausurRepository}.
	 *
	 * @return eine Instanz des {@link GostKlausurenKursklausurRepository}
	 */
	public GostKlausurenKursklausurRepository getGostKlausurenKursklausurRepository() {
		return getOrCreate(GostKlausurenKursklausurRepository.class, () -> new GostKlausurenKursklausurRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link GostKlausurenSchuelerklausurRepository}.
	 *
	 * @return eine Instanz des {@link GostKlausurenSchuelerklausurRepository}
	 */
	public GostKlausurenSchuelerklausurRepository getGostKlausurenSchuelerklausurRepository() {
		return getOrCreate(GostKlausurenSchuelerklausurRepository.class, () -> new GostKlausurenSchuelerklausurRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link GostKlausurenSchuelerklausurterminRepository}.
	 *
	 * @return eine Instanz des {@link GostKlausurenSchuelerklausurterminRepository}
	 */
	public GostKlausurenSchuelerklausurterminRepository getGostKlausurenSchuelerklausurterminRepository() {
		return getOrCreate(GostKlausurenSchuelerklausurterminRepository.class, () -> new GostKlausurenSchuelerklausurterminRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link GostKlausurenRaumRepository}.
	 *
	 * @return eine Instanz des {@link GostKlausurenRaumRepository}
	 */
	public GostKlausurenRaumRepository getGostKlausurenRaumRepository() {
		return getOrCreate(GostKlausurenRaumRepository.class, () -> new GostKlausurenRaumRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link GostKlausurenRaumstundeRepository}.
	 *
	 * @return eine Instanz des {@link GostKlausurenRaumstundeRepository}
	 */
	public GostKlausurenRaumstundeRepository getGostKlausurenRaumstundeRepository() {
		return getOrCreate(GostKlausurenRaumstundeRepository.class, () -> new GostKlausurenRaumstundeRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link GostKlausurenSchuelerklausurterminraumstundeRepository}.
	 *
	 * @return eine Instanz des {@link GostKlausurenSchuelerklausurterminraumstundeRepository}
	 */
	public GostKlausurenSchuelerklausurterminraumstundeRepository getGostKlausurenSchuelerklausurterminraumstundeRepository() {
		return getOrCreate(GostKlausurenSchuelerklausurterminraumstundeRepository.class,
				() -> new GostKlausurenSchuelerklausurterminraumstundeRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link GostKlausurenStundenplanHookRepository}.
	 *
	 * @return eine Instanz des {@link GostKlausurenStundenplanHookRepository}
	 */
	public GostKlausurenStundenplanHookRepository getGostKlausurenStundenplanHookRepository() {
		return getOrCreate(GostKlausurenStundenplanHookRepository.class, () -> new GostKlausurenStundenplanHookRepository(conn));
	}

	/**
	 * Liefert eine Instanz des {@link GostKlausurenAllDataRepository}.
	 *
	 * @return eine Instanz des {@link GostKlausurenAllDataRepository}
	 */
	public GostKlausurenAllDataRepository getGostKlausurenAllDataRepository() {
		return getOrCreate(GostKlausurenAllDataRepository.class, () -> new GostKlausurenAllDataRepository(conn));
	}

	/**
	 * Liefert eine Instanz des {@link GostKlausurenStundenplanDataRepository}.
	 *
	 * @return eine Instanz des {@link GostKlausurenStundenplanDataRepository}
	 */
	public GostKlausurenStundenplanDataRepository getGostKlausurenStundenplanDataRepository() {
		return getOrCreate(GostKlausurenStundenplanDataRepository.class, () -> new GostKlausurenStundenplanDataRepository(conn));
	}

}
