package de.svws_nrw.service.lehrer;

import de.svws_nrw.mapper.lehrer.fachrichtung.LehrerFachrichtungMapper;
import de.svws_nrw.mapper.lehrer.funktion.LehrerFunktionMapper;
import de.svws_nrw.mapper.lehrer.lehrbefaehigung.LehrerLehrbefaehigungMapper;
import de.svws_nrw.mapper.lehrer.mehrleistung.LehrerMehrleistungMapper;
import de.svws_nrw.asd.types.lehrer.LehrerMinderleistungsarten;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import de.svws_nrw.mapper.lehrer.minderleistung.LehrerMinderleistungMapper;
import de.svws_nrw.mapper.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenMapper;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.lehrer.anrechnung.LehrerAnrechnungsstundeService;
import de.svws_nrw.service.lehrer.anrechnung.LehrerAnrechnungsstundeServiceKontext;
import de.svws_nrw.service.lehrer.anrechnung.LehrerPersonalabschnittsdatenAnrechnungsstundeService;
import de.svws_nrw.service.lehrer.lehrbefaehigung.LehrerLehrbefaehigungService;
import de.svws_nrw.service.lehrer.fachrichtung.LehrerFachrichtungService;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionService;
import de.svws_nrw.service.lehrer.lehramt.LehrerLehramtService;
import de.svws_nrw.service.lehrer.mehrleistung.LehrerMehrleistungService;
import de.svws_nrw.service.lehrer.mehrleistung.LehrerMehrleistungServiceKontext;
import de.svws_nrw.service.lehrer.minderleistung.LehrerMinderleistungService;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenRepos;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenService;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenSubServices;
import de.svws_nrw.service.lehrer.unterrichtsfach.LehrerUnterrichtsfachService;

/**
 * Eine Factory zum Erstellen der Lehrer-spezifischen Services
 */
public final class LehrerServiceFactory {

	private final LehrerRepositoryFactory lehrerRepositoryFactory;
	private final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory;
	private final KatalogRepositoryFactory katalogRepositoryFactory;


	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param lehrerRepositoryFactory   die Factory für Lehrer-Repositories
	 * @param eigeneSchuleRepositoryFactory   die Factory für Schule-Repositories
	 * @param katalogRepositoryFactory   die Factory für Katalog-Repositories
	 */
	private LehrerServiceFactory(
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory,
			final KatalogRepositoryFactory katalogRepositoryFactory
	) {
		this.lehrerRepositoryFactory = lehrerRepositoryFactory;
		this.eigeneSchuleRepositoryFactory = eigeneSchuleRepositoryFactory;
		this.katalogRepositoryFactory = katalogRepositoryFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param lehrerRepositoryFactory   die Factory für Lehrer-Repositories
	 * @param eigeneSchuleRepositoryFactory   die Factory für Schule-Repositories
	 * @param katalogRepositoryFactory   die Factory für Katalog-Repositories
	 *
	 * @return die neue Factory-Instanz
	 */
	public static LehrerServiceFactory getNewInstance(
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory,
			final KatalogRepositoryFactory katalogRepositoryFactory) {
		return new LehrerServiceFactory(lehrerRepositoryFactory, eigeneSchuleRepositoryFactory, katalogRepositoryFactory);
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory für die vorübergehende Verwendung in Data-Klasse
	 *
	 * @return die neue Factory-Instanz
	 */
	public static LehrerServiceFactory getNewInstance() {
		return new LehrerServiceFactory(
				LehrerRepositoryFactory.getNewInstance(),
				EigeneSchuleRepositoryFactory.getNewInstance(),
				KatalogRepositoryFactory.getNewInstance());
	}


	/**
	 * Erstellt einen neuen Statistik-Service für den Zugriff auf Lehrämter von Lehrern.
	 *
	 * @return der Service für die Lehrämter von Lehrern
	 */
	public LehrerLehramtService getLehrerLehramtService() {
		return new LehrerLehramtService(lehrerRepositoryFactory.getLehrerPersonaldatenLehramtRepository(),
				this.getLehrerFachrichtungService(),
				this.getLehrerLehrbefaehigungService());
	}

	/**
	 * Erstellt einen neuen Statistik-Service für den Zugriff auf die Lehrbefähigungen von Lehrern.
	 *
	 * @return der Service für die Lehrbefähigungen von Lehrern
	 */
	public LehrerLehrbefaehigungService getLehrerLehrbefaehigungService() {
		return new LehrerLehrbefaehigungService(
				lehrerRepositoryFactory.getLehrerPersonaldatenLehramtLehrbefaehigungenRepository(),
				lehrerRepositoryFactory.getLehrerPersonaldatenLehramtRepository(),
				LehrerLehrbefaehigungMapper.INSTANCE
		);
	}

	/**
	 * Erstellt einen neuen Statistik-Service für den Zugriff auf die Fachrichtungen von Lehrern.
	 *
	 * @return der Service für die Fachrichtungen von Lehrern.
	 */
	public LehrerFachrichtungService getLehrerFachrichtungService() {
		return new LehrerFachrichtungService(
				lehrerRepositoryFactory.getLehrerPersonaldatenLehramtFachrichtungRepository(),
				lehrerRepositoryFactory.getLehrerPersonaldatenLehramtRepository(),
				LehrerFachrichtungMapper.INSTANCE
		);
	}

	/**
	 * Erstellt einen neuen Statistik-Service für den Zugriff auf die Anrechnungsstunden bei Lehrern.
	 *
	 * @return der Service für die Anrechnungsstunden bei Lehrern
	 */
	public LehrerPersonalabschnittsdatenAnrechnungsstundeService getLehrerPersonalabschnittsdatenAnrechnungsstundenService() {
		return new LehrerPersonalabschnittsdatenAnrechnungsstundeService(eigeneSchuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				lehrerRepositoryFactory.getLehrerMehrleistungRepository(),
				lehrerRepositoryFactory.getLehrerMinderleistungRepository(),
				lehrerRepositoryFactory.getLehrerAnrechnungRepository());
	}

	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Unterrichtsfächer von Lehrern.
	 *
	 * @return der Service für die Unterrichtsfächer von Lehrern
	 */
	public LehrerUnterrichtsfachService getLehrerUnterrichtsfachService() {
		return new LehrerUnterrichtsfachService(lehrerRepositoryFactory.getLehrerUnterrichtsfachRepository());
	}

	/**
	 * Erstellt einen neuen Service für den Zugriff auf Einträge zu den Anrechnungsstunden bei Lehrern.
	 *
	 * @return der Service für die Fachrichtungen von Lehrern.
	 */
	public LehrerAnrechnungsstundeService getLehrerAnrechnungsstundenService() {
		return new LehrerAnrechnungsstundeService(LehrerAnrechnungsstundeServiceKontext.of(
				eigeneSchuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				lehrerRepositoryFactory.getLehrerPersonalabschnittsdatenRepository(),
				lehrerRepositoryFactory.getLehrerAnrechnungRepository()));
	}

	/**
	 * Erstellt einen neuen Service für den Zugriff auf Einträge zu den Mehrleistungen bei Lehrern.
	 *
	 * @return der Service für die Mehrleistungen von Lehrern.
	 */
	public LehrerMehrleistungService getLehrerMehrleistungService() {
		return new LehrerMehrleistungService(LehrerMehrleistungServiceKontext.of(
				eigeneSchuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				lehrerRepositoryFactory.getLehrerPersonalabschnittsdatenRepository(),
				lehrerRepositoryFactory.getLehrerMehrleistungRepository()),
				LehrerMehrleistungMapper.INSTANCE);
	}

	/**
	 * Erstellt einen neuen Service für den Zugriff auf Einträge zu den Minderleistungen bei Lehrern.
	 *
	 * @return der Service für die Minderleistungen von Lehrern.
	 */
	public LehrerMinderleistungService getLehrerMinderleistungService() {
		return new LehrerMinderleistungService(
				lehrerRepositoryFactory.getLehrerMinderleistungRepository(),
				eigeneSchuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				lehrerRepositoryFactory.getLehrerPersonalabschnittsdatenRepository(),
				LehrerMinderleistungMapper.INSTANCE,
				CoreTypeDataManager.getManager(LehrerMinderleistungsarten.class)
		);
	}

	/**
	 * Erstellt einen neuen {@link LehrerFunktionService}.
	 *
	 * @return ein neuer {@code LehrerFunktionService}
	 */
	public LehrerFunktionService getLehrerFunktionService() {
		return new LehrerFunktionService(
				lehrerRepositoryFactory.getLehrerFunktionRepository(),
				lehrerRepositoryFactory.getLehrerPersonalabschnittsdatenRepository(),
				lehrerRepositoryFactory.getLeitungsfunktionRepository(),
				LehrerFunktionMapper.INSTANCE
		);
	}

	/**
	 * Erstellt einen neuen {@link LehrerPersonalabschnittsdatenService}.
	 *
	 * @return ein neuer {@code LehrerPersonalabschnittsdatenService}
	 */
	public LehrerPersonalabschnittsdatenService getLehrerPersonalabschnittsdatenService() {
		final var subRepos = new LehrerPersonalabschnittsdatenRepos(
				lehrerRepositoryFactory.getLehrerPersonalabschnittsdatenRepository(),
				lehrerRepositoryFactory.getLehrerRepository(),
				katalogRepositoryFactory.getSchulenRepository(),
				eigeneSchuleRepositoryFactory.getSchuljahresabschnitteRepository()
		);

		final var subServices = new LehrerPersonalabschnittsdatenSubServices(
				this.getLehrerAnrechnungsstundenService(),
				this.getLehrerMehrleistungService(),
				this.getLehrerMinderleistungService(),
				this.getLehrerFunktionService()
		);

		return new LehrerPersonalabschnittsdatenService(subRepos, subServices, LehrerPersonalabschnittsdatenMapper.INSTANCE);
	}

}
