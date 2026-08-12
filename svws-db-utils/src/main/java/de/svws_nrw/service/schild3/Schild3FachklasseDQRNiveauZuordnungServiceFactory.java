package de.svws_nrw.service.schild3;

import de.svws_nrw.mapper.Schild3FachklasseDQRNiveauZuordnungMapper;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;

/**
 * Factory für {@link Schild3FachklasseDQRNiveauZuordnungService}
 */
public final class Schild3FachklasseDQRNiveauZuordnungServiceFactory {

	private final Schild3FachklasseDQRNiveauZuordnungMapper mapper;
	private final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory;
	private final EigeneSchuleServiceFactory eigeneSchuleServiceFactory;

	private Schild3FachklasseDQRNiveauZuordnungServiceFactory(final Schild3FachklasseDQRNiveauZuordnungMapper mapper,
			final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory,
			final EigeneSchuleServiceFactory eigeneSchuleServiceFactory) {
		this.mapper = mapper;
		this.eigeneSchuleRepositoryFactory = eigeneSchuleRepositoryFactory;
		this.eigeneSchuleServiceFactory = eigeneSchuleServiceFactory;
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param mapper {@link Schild3FachklasseDQRNiveauZuordnungMapper}
	 * @param eigeneSchuleRepositoryFactory {@link EigeneSchuleRepositoryFactory}
	 * @param eigeneSchuleServiceFactory {@link EigeneSchuleServiceFactory}
	 *
	 * @return {@link Schild3FachklasseDQRNiveauZuordnungServiceFactory} - neu erzeugte Factory
	 */
	public static Schild3FachklasseDQRNiveauZuordnungServiceFactory getNewInstance(final Schild3FachklasseDQRNiveauZuordnungMapper mapper,
			final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory,
			final EigeneSchuleServiceFactory eigeneSchuleServiceFactory) {
		return new Schild3FachklasseDQRNiveauZuordnungServiceFactory(mapper, eigeneSchuleRepositoryFactory, eigeneSchuleServiceFactory);
	}

	/**
	 * Erstellt einen neuen TeilleistungsartenService für den Zugriff auf die Katalogdaten.
	 *
	 * @return {@link Schild3FachklasseDQRNiveauZuordnungService} - neu erzeugter Service
	 */
	public Schild3FachklasseDQRNiveauZuordnungService getSchild3FachklasseDQRNiveauZuordnungService() {
		return new Schild3FachklasseDQRNiveauZuordnungService(mapper,
				this.eigeneSchuleRepositoryFactory.getSchuleRepository(),
				this.eigeneSchuleServiceFactory.getSchuljahresabschnittService());
	}
}
