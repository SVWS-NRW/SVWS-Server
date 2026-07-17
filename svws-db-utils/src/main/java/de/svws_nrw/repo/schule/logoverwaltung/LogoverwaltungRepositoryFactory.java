package de.svws_nrw.repo.schule.logoverwaltung;

import de.svws_nrw.repo.RepositoryFactory;

public final class LogoverwaltungRepositoryFactory extends RepositoryFactory {

	private LogoverwaltungRepositoryFactory() {
	}

	/**
	 * Erstellt eine neue Instanz des LogoverwaltungRepositoryFactory
	 *
	 * @return die neue Instanz des LogoverwaltungRepositoryFactory
	 */
	public static LogoverwaltungRepositoryFactory getNewInstance() {
		return new LogoverwaltungRepositoryFactory();
	}

	/**
	 * Erstellt ein neues Repository für die Logoverwaltung.
	 *
	 * @return das Repository-Objekt
	 */
	public LogoverwaltungRepository getRepository() {
		return this.getOrCreate(LogoverwaltungRepository.class, () -> new LogoverwaltungRepositoryImpl(this.conn));
	}
}
