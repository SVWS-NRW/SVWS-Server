package de.svws_nrw.service.schule.katalog;

import java.util.function.Supplier;

import de.svws_nrw.mapper.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzJahrgangMapper;
import de.svws_nrw.mapper.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzKonfigurationMapper;
import de.svws_nrw.mapper.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzMapper;
import de.svws_nrw.mapper.schule.katalog.fachklasse.FachklasseMapper;
import de.svws_nrw.mapper.schule.katalog.jahrgang.JahrgangMapper;
import de.svws_nrw.mapper.schule.katalog.lernplattform.LernplattformMapper;
import de.svws_nrw.mapper.schule.katalog.merkmal.MerkmalMapper;
import de.svws_nrw.mapper.schule.katalog.ort.OrtMapper;
import de.svws_nrw.mapper.schule.katalog.ortsteil.OrtsteilMapper;
import de.svws_nrw.mapper.schule.katalog.religion.ReligionMapper;
import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.lehrer.LehrerServiceFactory;
import de.svws_nrw.service.lehrer.lernplattform.LehrerLernplattformService;
import de.svws_nrw.service.schueler.SchuelerServiceFactory;
import de.svws_nrw.service.schueler.lernplattform.SchuelerLernplattformService;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzJahrgangService;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzKonfigurationService;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzService;
import de.svws_nrw.service.schule.katalog.fachklasse.FachklasseService;
import de.svws_nrw.service.schule.katalog.jahrgang.JahrgangService;
import de.svws_nrw.service.schule.katalog.lernplattform.LernplattformService;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalService;
import de.svws_nrw.service.schule.katalog.ort.OrtService;
import de.svws_nrw.service.schule.katalog.ortsteil.OrtsteilService;
import de.svws_nrw.service.schule.katalog.religion.ReligionService;
import de.svws_nrw.service.schule.katalog.teilleistungsart.TeilleistungsartService;

public final class KatalogServiceFactory {

	private final KatalogRepositoryFactory katalogRepositoryFactory;
	private final EigeneSchuleServiceFactory eigeneSchuleServiceFactory;
	private final Supplier<SchuelerLernplattformService> schuelerLernplattformServiceSupplier;
	private final Supplier<LehrerLernplattformService> lehrerLernplattformServiceSupplier;

	private KatalogServiceFactory(
			final KatalogRepositoryFactory katalogRepositoryFactory,
			final EigeneSchuleServiceFactory eigeneSchuleServiceFactory,
			final Supplier<SchuelerLernplattformService> schuelerLernplattformServiceSupplier,
			final Supplier<LehrerLernplattformService> lehrerLernplattformServiceSupplier) {
		this.katalogRepositoryFactory = katalogRepositoryFactory;
		this.eigeneSchuleServiceFactory = eigeneSchuleServiceFactory;
		this.schuelerLernplattformServiceSupplier = schuelerLernplattformServiceSupplier;
		this.lehrerLernplattformServiceSupplier = lehrerLernplattformServiceSupplier;
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param repositoryFactory   die Factory für Katalog-Repositories
	 * @param eigeneSchuleRepositoryFactory   die Factory für EigeneSchule-Repositories
	 *
	 * @return die neue Factory-Instanz
	 */
	public static KatalogServiceFactory getNewInstance(
			final KatalogRepositoryFactory repositoryFactory,
			final EigeneSchuleServiceFactory eigeneSchuleRepositoryFactory) {
		return new KatalogServiceFactory(repositoryFactory, eigeneSchuleRepositoryFactory,
				() -> SchuelerServiceFactory.getNewInstance(
						BenutzerRepositoryFactory.getNewInstance(),
						SchuelerRepositoryFactory.getNewInstance(),
						repositoryFactory,
						getNewInstance(repositoryFactory, eigeneSchuleRepositoryFactory),
						EigeneSchuleRepositoryFactory.getNewInstance(),
						eigeneSchuleRepositoryFactory).getSchuelerLernplattformService(),
				() -> LehrerServiceFactory.getNewInstance(
						LehrerRepositoryFactory.getNewInstance(),
						EigeneSchuleRepositoryFactory.getNewInstance(),
						repositoryFactory).getLehrerLernplattformService());
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory mit explizit übergebenen Quellen für die Services
	 * der Zuordnungen von Schülern und Lehrern zu Lernplattformen. Diese werden erst beim Erstellen des
	 * {@link LernplattformService} aufgelöst, da die zugehörigen Service-Factories selbst von dieser Factory abhängen.
	 *
	 * @param repositoryFactory   die Factory für Katalog-Repositories
	 * @param eigeneSchuleServiceFactory   die Factory für EigeneSchule-Services
	 * @param schuelerLernplattformServiceSupplier   die Quelle für den {@link SchuelerLernplattformService}
	 * @param lehrerLernplattformServiceSupplier   die Quelle für den {@link LehrerLernplattformService}
	 *
	 * @return die neue Factory-Instanz
	 */
	public static KatalogServiceFactory getNewInstance(
			final KatalogRepositoryFactory repositoryFactory,
			final EigeneSchuleServiceFactory eigeneSchuleServiceFactory,
			final Supplier<SchuelerLernplattformService> schuelerLernplattformServiceSupplier,
			final Supplier<LehrerLernplattformService> lehrerLernplattformServiceSupplier) {
		return new KatalogServiceFactory(repositoryFactory, eigeneSchuleServiceFactory,
				schuelerLernplattformServiceSupplier, lehrerLernplattformServiceSupplier);
	}

	/**
	 * Erstellt einen neuen TeilleistungsartenService für den Zugriff auf die Katalogdaten.
	 *
	 * @return {@link TeilleistungsartService} - neu erzeugter Service
	 */
	public TeilleistungsartService getTeilLeistungsartenService() {
		return new TeilleistungsartService(katalogRepositoryFactory.getTeilleistungsartRepository());
	}

	/**
	 * Erstellt eine neue Instanz des MerkmalService.
	 *
	 * @return eine neue Instanz des MerkmalService
	 */
	public MerkmalService getMerkmalService() {
		return new MerkmalService(katalogRepositoryFactory.getMerkmalRepository(), MerkmalMapper.INSTANCE);
	}

	/**
	 * @return eine neue Instanz des FachklasseService
	 */
	public FachklasseService getFachklasseService() {
		return new FachklasseService(
				katalogRepositoryFactory.getFachklasseRepository(),
				FachklasseMapper.INSTANCE,
				eigeneSchuleServiceFactory.getSchuleService());
	}

	/**
	 * Erstellt eine neue Instanz des OrtService.
	 *
	 * @return eine neue Instanz des OrtService.
	 */
	public OrtService getOrtService() {
		return new OrtService(
				katalogRepositoryFactory.getOrtRepository(),
				OrtMapper.INSTANCE,
				eigeneSchuleServiceFactory.getSchuleService());
	}

	/**
	 * Erstellt eine neue Instanz des OrtsteilServices.
	 *
	 * @return eine neue Instanz des OrtsteilServices.
	 */
	public OrtsteilService getOrtsteilService() {
		return new OrtsteilService(
				katalogRepositoryFactory.getOrtsteilRepository(),
				katalogRepositoryFactory.getOrtRepository(),
				OrtsteilMapper.INSTANCE);
	}

	/**
	 * Erstellt eine neue Instanz des ReligionService.
	 *
	 * @return eine neue Instanz des ReligionService.
	 */
	public ReligionService getReligionService() {
		return new ReligionService(
				katalogRepositoryFactory.getReligionRepository(),
				ReligionMapper.INSTANCE,
				eigeneSchuleServiceFactory.getSchuleService()
		);
	}

	/**
	 * Erstellt eine neue Instanz des LernplattformService.
	 *
	 * @return eine neue Instanz des LernplattformService.
	 */
	public LernplattformService getLernplattformService() {
		return new LernplattformService(
				katalogRepositoryFactory.getLernplattformRepository(),
				LernplattformMapper.INSTANCE,
				schuelerLernplattformServiceSupplier.get(),
				lehrerLernplattformServiceSupplier.get()
		);
	}

	/**
	 * Erstellt eine neue Instanz des JahrgangService.
	 *
	 * @return eine neue Instanz des JahrgangService.
	 */
	public JahrgangService getJahrgangService() {
		return new JahrgangService(
				katalogRepositoryFactory.getJahrgangRepository(),
				JahrgangMapper.INSTANCE,
				eigeneSchuleServiceFactory.getSchuleService()
		);
	}

	/**
	 * Erstellt eine neue Instanz des AnkreuzkompetenzService.
	 *
	 * @return eine neue Instanz des AnkreuzkompetenzService.
	 */
	public AnkreuzkompetenzService getAnkreuzkompetenzService() {
		return new AnkreuzkompetenzService(
				katalogRepositoryFactory.getAnkreuzkompetenzRepository(),
				katalogRepositoryFactory.getFachRepository(),
				getAnkreuzkompetenzJahrgangService(),
				eigeneSchuleServiceFactory.getSchuleService(),
				AnkreuzkompetenzMapper.INSTANCE
		);
	}

	/**
	 * Erstellt eine neue Instanz des AnkreuzkompetenzJahrgangService.
	 *
	 * @return eine neue Instanz des AnkreuzkompetenzJahrgangService.
	 */
	public AnkreuzkompetenzJahrgangService getAnkreuzkompetenzJahrgangService() {
		return new AnkreuzkompetenzJahrgangService(
				katalogRepositoryFactory.getAnkreuzkompetenzJahrgangRepository(),
				katalogRepositoryFactory.getAnkreuzkompetenzRepository(),
				katalogRepositoryFactory.getJahrgangRepository(),
				AnkreuzkompetenzJahrgangMapper.INSTANCE
		);
	}


	/**
	 * Erstellt eine neue Instanz des AnkreuzkompetenzKonfigurationService.
	 *
	 * @return eine neue Instanz des AnkreuzkompetenzKonfigurationService.
	 */
	public AnkreuzkompetenzKonfigurationService getAnkreuzkompetenzKonfigurationService() {
		return new AnkreuzkompetenzKonfigurationService(
				katalogRepositoryFactory.getAnkreuzkompetenzKonfigurationRepository(),
				AnkreuzkompetenzKonfigurationMapper.INSTANCE
		);
	}

}
