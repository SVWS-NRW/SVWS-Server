package de.svws_nrw.service.enm;

import de.svws_nrw.repo.enm.NotenmodulRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.kurse.KurseRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;

/**
 * Eine Factory zum Erstellen von Services für das Notenmodul
 */
public final class NotenmodulServiceFactory {

	private final NotenmodulRepositoryFactory notenmodulRepositoryFactory;
	private final LehrerRepositoryFactory lehrerRepositoryFactory;
	private final SchuelerRepositoryFactory schuelerRepositoryFactory;
	private final KatalogRepositoryFactory katalogRepositoryFactory;
	private final KlassenRepositoryFactory klassenRepositoryFactory;
	private final KurseRepositoryFactory kurseRepositoryFactory;
	private final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory;

	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param notenmodulRepositoryFactory   die Factory für Notenmodul-Repositories
	 * @param lehrerRepositoryFactory       die Factory für Lehrer-Repositories
	 * @param schuelerRepositoryFactory     die Factory für Schüler-Repositories
	 * @param katalogRepositoryFactory     die Factory für Katalog-Repositories
	 * @param klassenRepositoryFactory      die Factory für Klassen-Repositories
	 * @param kurseRepositoryFactory        die Factory für Kurse-Repositories
	 * @param eigeneSchuleRepositoryFactory       die Factory für Schul-Repositories
	 */
	private NotenmodulServiceFactory(final NotenmodulRepositoryFactory notenmodulRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory, final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final KatalogRepositoryFactory katalogRepositoryFactory, final KlassenRepositoryFactory klassenRepositoryFactory,
			final KurseRepositoryFactory kurseRepositoryFactory, final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory) {
		this.notenmodulRepositoryFactory = notenmodulRepositoryFactory;
		this.lehrerRepositoryFactory = lehrerRepositoryFactory;
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
		this.katalogRepositoryFactory = katalogRepositoryFactory;
		this.klassenRepositoryFactory = klassenRepositoryFactory;
		this.kurseRepositoryFactory = kurseRepositoryFactory;
		this.eigeneSchuleRepositoryFactory = eigeneSchuleRepositoryFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param notenmodulRepositoryFactory   die Factory für Notenmodul-Repositories
	 * @param lehrerRepositoryFactory       die Factory für Lehrer-Repositories
	 * @param schuelerRepositoryFactory     die Factory für Schüler-Repositories
	 * @param katalogRepositoryFactory     die Factory für Katalog-Repositories
	 * @param klassenRepositoryFactory      die Factory für Klassen-Repositories
	 * @param kurseRepositoryFactory        die Factory für Kurse-Repositories
	 * @param eigeneSchuleRepositoryFactory       die Factory für Schul-Repositories
	 *
	 * @return die neue Factory-Instanz
	 */
	public static NotenmodulServiceFactory getNewInstance(final NotenmodulRepositoryFactory notenmodulRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory, final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final KatalogRepositoryFactory katalogRepositoryFactory, final KlassenRepositoryFactory klassenRepositoryFactory,
			final KurseRepositoryFactory kurseRepositoryFactory, final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory) {
		return new NotenmodulServiceFactory(notenmodulRepositoryFactory, lehrerRepositoryFactory, schuelerRepositoryFactory,
				katalogRepositoryFactory, klassenRepositoryFactory, kurseRepositoryFactory, eigeneSchuleRepositoryFactory);
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
				this.getEnmV2GetService(),
				this.getEnmV2ImportService(),
				this.getNotenmodulCredentialGeneratorService()
		);
	}

	/**
	 * Erstellt einen Service für die Erstellung von Notenmodul-Credentials (welche bei externen Notenmodulen eingesetzt werden).
	 *
	 * @return der Service
	 */
	public NotenmodulCredentialGeneratorService getNotenmodulCredentialGeneratorService() {
		return new NotenmodulCredentialGeneratorService(notenmodulRepositoryFactory.getNotenmodulCredentialsRepository(),
				lehrerRepositoryFactory.getLehrerRepository());
	}


	/**
	 * Erstellt einen Service für die Verwaltung der Notenmodul-Credentials (welche bei externen Notenmodulen eingesetzt werden).
	 *
	 * @return der Service
	 */
	public NotenmodulCredentialsService getNotenmodulCredentialsService() {
		return new NotenmodulCredentialsService(notenmodulRepositoryFactory.getNotenmodulCredentialsRepository(),
				lehrerRepositoryFactory.getLehrerRepository(),
				this.getEnmV2GetService(),
				this.getNotenmodulCredentialGeneratorService()
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
				schuelerRepositoryFactory.getSchuelerTeilleistungenRepository(),
				schuelerRepositoryFactory.getSchuelerAnkreuzkompetenzenRepository(),
				katalogRepositoryFactory.getAnkreuzkompetenzRepository()
		);
	}


	/**
	 * Erstellt einen neuen Service für das Auslesen der ENM-Daten in der Version 1
	 *
	 * @return der Service
	 */
	public EnmV1GetService getEnmV1GetService() {
		return new EnmV1GetService(EnmV1GetServiceKontext.of(
				eigeneSchuleRepositoryFactory.getSchuleRepository(),
				eigeneSchuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				lehrerRepositoryFactory.getLehrerRepository(),
				katalogRepositoryFactory.getFachRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittTimestampsRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittBemerkungenRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenTimestampsRepository(),
				schuelerRepositoryFactory.getSchuelerTeilleistungenRepository(),
				schuelerRepositoryFactory.getSchuelerTeilleistungenTimestampsRepository(),
				schuelerRepositoryFactory.getSchuelerAnkreuzkompetenzenRepository(),
				schuelerRepositoryFactory.getSchuelerAnkreuzkompetenzenTimestampsRepository(),
				kurseRepositoryFactory.getKurseRepository(),
				klassenRepositoryFactory.getKlassenRepository(),
				klassenRepositoryFactory.getKlassenleitungenRepository(),
				katalogRepositoryFactory.getJahrgangRepository(),
				katalogRepositoryFactory.getFoerderschwerpunktRepository(),
				katalogRepositoryFactory.getAnkreuzkompetenzKonfigurationRepository(),
				katalogRepositoryFactory.getAnkreuzkompetenzRepository(),
				katalogRepositoryFactory.getAnkreuzkompetenzJahrgangRepository(),
				katalogRepositoryFactory.getFloskelRepository(),
				katalogRepositoryFactory.getFloskelgruppeRepository(),
				katalogRepositoryFactory.getFloskelJahrgangRepository(),
				katalogRepositoryFactory.getTeilleistungsartRepository(),
				notenmodulRepositoryFactory.getNotenmodulCredentialsRepository(),
				notenmodulRepositoryFactory.getNotenmodulCredentialsTimestampsRepository()));
	}


	/**
	 * Erstellt einen Service für den Import von ENM-Daten in der Version 1
	 *
	 * @return der Service
	 */
	public EnmV1ImportService getEnmV1ImportService() {
		return new EnmV1ImportService(lehrerRepositoryFactory.getLehrerRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittTimestampsRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittBemerkungenRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenTimestampsRepository(),
				schuelerRepositoryFactory.getSchuelerTeilleistungenRepository(),
				schuelerRepositoryFactory.getSchuelerTeilleistungenTimestampsRepository(),
				schuelerRepositoryFactory.getSchuelerAnkreuzkompetenzenRepository(),
				schuelerRepositoryFactory.getSchuelerAnkreuzkompetenzenTimestampsRepository(),
				notenmodulRepositoryFactory.getNotenmodulCredentialsRepository(),
				notenmodulRepositoryFactory.getNotenmodulCredentialsTimestampsRepository());
	}


	/**
	 * Erstellt einen neuen Service für das Auslesen der ENM-Daten in der Version 2
	 *
	 * @return der Service
	 */
	public EnmV2GetService getEnmV2GetService() {
		return new EnmV2GetService(EnmV2GetServiceKontext.of(
				eigeneSchuleRepositoryFactory.getSchuleRepository(),
				eigeneSchuleRepositoryFactory.getSchulleitungRepository(),
				eigeneSchuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				eigeneSchuleRepositoryFactory.getAbteilungenRepository(),
				eigeneSchuleRepositoryFactory.getAbteilungenKlassenRepository(),
				lehrerRepositoryFactory.getLehrerRepository(),
				katalogRepositoryFactory.getFachRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittTimestampsRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittBemerkungenRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenTimestampsRepository(),
				schuelerRepositoryFactory.getSchuelerTeilleistungenRepository(),
				schuelerRepositoryFactory.getSchuelerTeilleistungenTimestampsRepository(),
				schuelerRepositoryFactory.getSchuelerAnkreuzkompetenzenRepository(),
				schuelerRepositoryFactory.getSchuelerAnkreuzkompetenzenTimestampsRepository(),
				schuelerRepositoryFactory.getSchuelerZP10Repository(),
				schuelerRepositoryFactory.getSchuelerZP10TimestampsRepository(),
				kurseRepositoryFactory.getKurseRepository(),
				klassenRepositoryFactory.getKlassenRepository(),
				klassenRepositoryFactory.getKlassenleitungenRepository(),
				katalogRepositoryFactory.getJahrgangRepository(),
				katalogRepositoryFactory.getFoerderschwerpunktRepository(),
				katalogRepositoryFactory.getAnkreuzkompetenzKonfigurationRepository(),
				katalogRepositoryFactory.getAnkreuzkompetenzRepository(),
				katalogRepositoryFactory.getAnkreuzkompetenzJahrgangRepository(),
				katalogRepositoryFactory.getFloskelRepository(),
				katalogRepositoryFactory.getFloskelgruppeRepository(),
				katalogRepositoryFactory.getFloskelJahrgangRepository(),
				katalogRepositoryFactory.getTeilleistungsartRepository(),
				this.getNotenmodulCredentialGeneratorService(),
				notenmodulRepositoryFactory.getNotenmodulCredentialsTimestampsRepository()));
	}


	/**
	 * Erstellt einen Service für den Import von ENM-Daten in der Version 2
	 *
	 * @return der Service
	 */
	public EnmV2ImportService getEnmV2ImportService() {
		return new EnmV2ImportService(lehrerRepositoryFactory.getLehrerRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittTimestampsRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittBemerkungenRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenTimestampsRepository(),
				schuelerRepositoryFactory.getSchuelerTeilleistungenRepository(),
				schuelerRepositoryFactory.getSchuelerTeilleistungenTimestampsRepository(),
				schuelerRepositoryFactory.getSchuelerAnkreuzkompetenzenRepository(),
				schuelerRepositoryFactory.getSchuelerAnkreuzkompetenzenTimestampsRepository(),
				notenmodulRepositoryFactory.getNotenmodulCredentialsRepository(),
				notenmodulRepositoryFactory.getNotenmodulCredentialsTimestampsRepository());
	}

}
