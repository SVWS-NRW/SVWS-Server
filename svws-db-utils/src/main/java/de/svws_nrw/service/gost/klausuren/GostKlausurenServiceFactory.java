package de.svws_nrw.service.gost.klausuren;

import de.svws_nrw.repo.kurse.KurseRepositoryFactory;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.gost.GostServiceFactory;

/**
 * Eine Factory zum Erstellen der Services für die GOSt-Klausurplanung.
 */
public final class GostKlausurenServiceFactory {

	private final GostKlausurenRepositoryFactory klausurenRepositoryFactory;
	private final GostServiceFactory gostServiceFactory;
	private final KurseRepositoryFactory kurseRepositoryFactory;
	private final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory;

	private GostKlausurenServiceFactory(final GostKlausurenRepositoryFactory klausurenRepositoryFactory,
			final GostServiceFactory gostServiceFactory) {
		this.klausurenRepositoryFactory = klausurenRepositoryFactory;
		this.gostServiceFactory = gostServiceFactory;
		this.kurseRepositoryFactory = KurseRepositoryFactory.getNewInstance();
		this.eigeneSchuleRepositoryFactory = EigeneSchuleRepositoryFactory.getNewInstance();
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory.
	 *
	 * @param klausurenRepositoryFactory die Repository-Factory für die GOSt-Klausurplanung
	 * @param gostServiceFactory die Service-Factory für die GOSt
	 *
	 * @return die neue Factory-Instanz
	 */
	public static GostKlausurenServiceFactory getNewInstance(final GostKlausurenRepositoryFactory klausurenRepositoryFactory,
			final GostServiceFactory gostServiceFactory) {
		return new GostKlausurenServiceFactory(klausurenRepositoryFactory, gostServiceFactory);
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenVorgabeService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenVorgabeService}
	 */
	public GostKlausurenVorgabeService getGostKlausurenVorgabeService() {
		return new GostKlausurenVorgabeService(klausurenRepositoryFactory.getGostKlausurenVorgabeRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenVorgabeVorlagenService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenVorgabeVorlagenService}
	 */
	public GostKlausurenVorgabeVorlagenService getGostKlausurenVorgabeVorlagenService() {
		return new GostKlausurenVorgabeVorlagenService(klausurenRepositoryFactory.getGostKlausurenVorgabeRepository(),
				gostServiceFactory.getGostFaecherService());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenTerminService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenTerminService}
	 */
	public GostKlausurenTerminService getGostKlausurenTerminService() {
		return new GostKlausurenTerminService(klausurenRepositoryFactory.getGostKlausurenTerminRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenKursklausurService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenKursklausurService}
	 */
	public GostKlausurenKursklausurService getGostKlausurenKursklausurService() {
		return new GostKlausurenKursklausurService(klausurenRepositoryFactory.getGostKlausurenKursklausurRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenSchuelerklausurService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenSchuelerklausurService}
	 */
	public GostKlausurenSchuelerklausurService getGostKlausurenSchuelerklausurService() {
		return new GostKlausurenSchuelerklausurService(klausurenRepositoryFactory.getGostKlausurenSchuelerklausurRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenSchuelerklausurterminService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenSchuelerklausurterminService}
	 */
	public GostKlausurenSchuelerklausurterminService getGostKlausurenSchuelerklausurterminService() {
		return new GostKlausurenSchuelerklausurterminService(klausurenRepositoryFactory.getGostKlausurenSchuelerklausurterminRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenRaumService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenRaumService}
	 */
	public GostKlausurenRaumService getGostKlausurenRaumService() {
		return new GostKlausurenRaumService(klausurenRepositoryFactory.getGostKlausurenRaumRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenRaumstundeService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenRaumstundeService}
	 */
	public GostKlausurenRaumstundeService getGostKlausurenRaumstundeService() {
		return new GostKlausurenRaumstundeService(klausurenRepositoryFactory.getGostKlausurenRaumstundeRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenSchuelerklausurterminraumstundeService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenSchuelerklausurterminraumstundeService}
	 */
	public GostKlausurenSchuelerklausurterminraumstundeService getGostKlausurenSchuelerklausurterminraumstundeService() {
		return new GostKlausurenSchuelerklausurterminraumstundeService(
				klausurenRepositoryFactory.getGostKlausurenSchuelerklausurterminraumstundeRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenKlausurdatenService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenKlausurdatenService}
	 */
	public GostKlausurenKlausurdatenService getGostKlausurenKlausurdatenService() {
		return new GostKlausurenKlausurdatenService(
				getGostKlausurenVorgabeService(),
				getGostKlausurenKursklausurService(),
				getGostKlausurenSchuelerklausurService(),
				getGostKlausurenSchuelerklausurterminService(),
				getGostKlausurenTerminService());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenAllDataService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenAllDataService}
	 */
	public GostKlausurenAllDataService getGostKlausurenAllDataService() {
		return new GostKlausurenAllDataService(klausurenRepositoryFactory.getGostKlausurenAllDataRepository(), getGostKlausurenKlausurdatenService(),
				getGostKlausurenRaumzuweisungService());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenKlausurdatenIssuesService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenKlausurdatenIssuesService}
	 */
	public GostKlausurenKlausurdatenIssuesService getGostKlausurenKlausurdatenIssuesService() {
		return new GostKlausurenKlausurdatenIssuesService(getGostKlausurenKlausurdatenService(),
				eigeneSchuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				kurseRepositoryFactory.getKurseRepository(),
				gostServiceFactory.getGostKursBelegungService());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenKursklausurCreationService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenKursklausurCreationService}
	 */
	public GostKlausurenKursklausurCreationService getGostKlausurenKursklausurCreationService() {
		return new GostKlausurenKursklausurCreationService(getGostKlausurenVorgabeService(),
				getGostKlausurenKursklausurService(),
				klausurenRepositoryFactory.getGostKlausurenKursklausurRepository(),
				klausurenRepositoryFactory.getGostKlausurenSchuelerklausurRepository(),
				klausurenRepositoryFactory.getGostKlausurenSchuelerklausurterminRepository(),
				eigeneSchuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				kurseRepositoryFactory.getKurseRepository(),
				gostServiceFactory.getGostKursBelegungService());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenSchuelerklausurCreationService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenSchuelerklausurCreationService}
	 */
	public GostKlausurenSchuelerklausurCreationService getGostKlausurenSchuelerklausurCreationService() {
		return new GostKlausurenSchuelerklausurCreationService(klausurenRepositoryFactory.getGostKlausurenSchuelerklausurRepository(),
				klausurenRepositoryFactory.getGostKlausurenSchuelerklausurterminRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenSchuelerklausurterminCreationService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenSchuelerklausurterminCreationService}
	 */
	public GostKlausurenSchuelerklausurterminCreationService getGostKlausurenSchuelerklausurterminCreationService() {
		return new GostKlausurenSchuelerklausurterminCreationService(getGostKlausurenSchuelerklausurterminService(), getGostKlausurenRaumzuweisungService());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenKursklausurPatchService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenKursklausurPatchService}
	 */
	public GostKlausurenKursklausurPatchService getGostKlausurenKursklausurPatchService() {
		return new GostKlausurenKursklausurPatchService(getGostKlausurenKursklausurService(),
				getGostKlausurenTerminService(),
				getGostKlausurenVorgabeService(),
				getGostKlausurenSchuelerklausurService(),
				getGostKlausurenSchuelerklausurterminService(),
				getGostKlausurenRaumzuweisungService());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenTerminPatchService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenTerminPatchService}
	 */
	public GostKlausurenTerminPatchService getGostKlausurenTerminPatchService() {
		return new GostKlausurenTerminPatchService(getGostKlausurenTerminService(),
				getGostKlausurenSchuelerklausurterminService(),
				getGostKlausurenRaumzuweisungService());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenSchuelerklausurterminPatchService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenSchuelerklausurterminPatchService}
	 */
	public GostKlausurenSchuelerklausurterminPatchService getGostKlausurenSchuelerklausurterminPatchService() {
		return new GostKlausurenSchuelerklausurterminPatchService(getGostKlausurenSchuelerklausurterminService(), getGostKlausurenRaumzuweisungService());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenSchuelerKlausurdatenService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenSchuelerKlausurdatenService}
	 */
	public GostKlausurenSchuelerKlausurdatenService getGostKlausurenSchuelerKlausurdatenService() {
		return new GostKlausurenSchuelerKlausurdatenService(getGostKlausurenSchuelerklausurService(),
				getGostKlausurenSchuelerklausurterminService(),
				getGostKlausurenKursklausurService(),
				getGostKlausurenVorgabeService(),
				getGostKlausurenTerminService());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenRaumzuweisungService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenRaumzuweisungService}
	 */
	public GostKlausurenRaumzuweisungService getGostKlausurenRaumzuweisungService() {
		return new GostKlausurenRaumzuweisungService(klausurenRepositoryFactory.getGostKlausurenStundenplanDataRepository(),
				klausurenRepositoryFactory.getGostKlausurenRaumstundeRepository(),
				klausurenRepositoryFactory.getGostKlausurenSchuelerklausurterminraumstundeRepository(),
				getGostKlausurenSchuelerklausurterminraumstundeService(),
				getGostKlausurenRaumstundeService(),
				getGostKlausurenTerminService(),
				getGostKlausurenRaumService(),
				getGostKlausurenSchuelerklausurterminService(),
				getGostKlausurenSchuelerklausurService(),
				getGostKlausurenKursklausurService(),
				getGostKlausurenVorgabeService());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenKursklausurBlockungService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenKursklausurBlockungService}
	 */
	public GostKlausurenKursklausurBlockungService getGostKlausurenKursklausurBlockungService() {
		return new GostKlausurenKursklausurBlockungService(getGostKlausurenVorgabeService(),
				klausurenRepositoryFactory.getGostKlausurenTerminRepository(),
				klausurenRepositoryFactory.getGostKlausurenKursklausurRepository(),
				getGostKlausurenSchuelerklausurService(),
				eigeneSchuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				kurseRepositoryFactory.getKurseRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link GostKlausurenNachschreibterminBlockungService}.
	 *
	 * @return eine Instanz des {@link GostKlausurenNachschreibterminBlockungService}
	 */
	public GostKlausurenNachschreibterminBlockungService getGostKlausurenNachschreibterminBlockungService() {
		return new GostKlausurenNachschreibterminBlockungService(getGostKlausurenVorgabeService(),
				klausurenRepositoryFactory.getGostKlausurenTerminRepository(),
				getGostKlausurenKursklausurService(),
				getGostKlausurenSchuelerklausurService(),
				getGostKlausurenSchuelerklausurterminService(),
				klausurenRepositoryFactory.getGostKlausurenSchuelerklausurterminRepository(),
				eigeneSchuleRepositoryFactory.getSchuljahresabschnitteRepository());
	}

}
