package de.svws_nrw.service.uv;

import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.uv.UvGrunddatenRepositoryFactory;
import de.svws_nrw.repo.uv.UvPlanungsabschnittRepositoryFactory;

/**
 * Eine Factory zum Erstellen von Services für versionierte UV-Exporte.
 */
public final class UvExportServiceFactory {

	private final UvGrunddatenServiceFactory uvGrunddatenServiceFactory;
	private final UvPlanungsabschnittServiceFactory uvPlanungsabschnittServiceFactory;

	/**
	 * Erstellt eine neue Service-Factory.
	 */
	private UvExportServiceFactory() {
		final var uvGrunddatenRepositoryFactory = UvGrunddatenRepositoryFactory.getNewInstance();
		final var lehrerRepositoryFactory = LehrerRepositoryFactory.getNewInstance();
		final var schuleRepositoryFactory = EigeneSchuleRepositoryFactory.getNewInstance();
		final var uvPlanungsabschnittRepositoryFactory = UvPlanungsabschnittRepositoryFactory.getNewInstance();
		this.uvGrunddatenServiceFactory = UvGrunddatenServiceFactory.getNewInstance(
				uvGrunddatenRepositoryFactory, lehrerRepositoryFactory, schuleRepositoryFactory);
		this.uvPlanungsabschnittServiceFactory = UvPlanungsabschnittServiceFactory.getNewInstance(uvPlanungsabschnittRepositoryFactory);
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory.
	 *
	 * @return die neue Factory-Instanz
	 */
	public static UvExportServiceFactory getNewInstance() {
		return new UvExportServiceFactory();
	}

	/**
	 * Erzeugt eine Instanz des {@link UvExportService}.
	 *
	 * @return eine Instanz des {@link UvExportService}
	 */
	public UvExportService getUvExportService() {
		return new UvExportService(
				uvGrunddatenServiceFactory.getUvGrunddatenBundleService(),
				uvPlanungsabschnittServiceFactory.getUvPlanungsabschnittService(),
				uvPlanungsabschnittServiceFactory.getUvPlanungsabschnittsdatenBundleService());
	}

}
