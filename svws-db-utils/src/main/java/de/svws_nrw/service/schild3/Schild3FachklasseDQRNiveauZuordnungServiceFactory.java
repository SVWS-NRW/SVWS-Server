package de.svws_nrw.service.schild3;

import de.svws_nrw.mapper.Schild3FachklasseDQRNiveauZuordnungMapper;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.schule.SchuleServiceFactory;

/**
 * Factory für {@link Schild3FachklasseDQRNiveauZuordnungService}
 */
public final class Schild3FachklasseDQRNiveauZuordnungServiceFactory {

	private final Schild3FachklasseDQRNiveauZuordnungMapper mapper;
	private final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory;
	private final SchuleServiceFactory schuleServiceFactory;

	private Schild3FachklasseDQRNiveauZuordnungServiceFactory(final Schild3FachklasseDQRNiveauZuordnungMapper mapper,
			final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory,
			final SchuleServiceFactory schuleServiceFactory) {
		this.mapper = mapper;
		this.eigeneSchuleRepositoryFactory = eigeneSchuleRepositoryFactory;
		this.schuleServiceFactory = schuleServiceFactory;
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param mapper {@link Schild3FachklasseDQRNiveauZuordnungMapper}
	 * @param eigeneSchuleRepositoryFactory {@link EigeneSchuleRepositoryFactory}
	 * @param schuleServiceFactory {@link SchuleServiceFactory}
	 *
	 * @return {@link Schild3FachklasseDQRNiveauZuordnungServiceFactory} - neu erzeugte Factory
	 */
	public static Schild3FachklasseDQRNiveauZuordnungServiceFactory getNewInstance(final Schild3FachklasseDQRNiveauZuordnungMapper mapper,
			final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory,
			final SchuleServiceFactory schuleServiceFactory) {
		return new Schild3FachklasseDQRNiveauZuordnungServiceFactory(mapper, eigeneSchuleRepositoryFactory, schuleServiceFactory);
	}

	/**
	 * Erstellt einen neuen TeilleistungsartenService für den Zugriff auf die Katalogdaten.
	 *
	 * @return {@link Schild3FachklasseDQRNiveauZuordnungService} - neu erzeugter Service
	 */
	public Schild3FachklasseDQRNiveauZuordnungService getSchild3FachklasseDQRNiveauZuordnungService() {
		return new Schild3FachklasseDQRNiveauZuordnungService(mapper,
				this.eigeneSchuleRepositoryFactory.getSchuleRepository(),
				this.schuleServiceFactory.getSchuljahresabschnittService());
	}
}
