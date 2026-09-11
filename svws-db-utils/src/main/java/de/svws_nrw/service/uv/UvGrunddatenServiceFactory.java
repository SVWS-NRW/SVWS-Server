package de.svws_nrw.service.uv;

import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.uv.UvGrunddatenRepositoryFactory;
import de.svws_nrw.service.lehrer.unterrichtsfach.LehrerUnterrichtsfachService;
import de.svws_nrw.service.uv.faecher.UvFachService;
import de.svws_nrw.service.uv.lehrer.UvLehrerAnrechnungsstundenImportService;
import de.svws_nrw.service.uv.lehrer.UvLehrerAnrechnungsstundenService;
import de.svws_nrw.service.uv.lehrer.UvLehrerPflichtstundensollImportService;
import de.svws_nrw.service.uv.lehrer.UvLehrerPflichtstundensollService;
import de.svws_nrw.service.uv.lehrer.UvLehrerService;
import de.svws_nrw.service.uv.lehrer.UvLehrerUnterrichtsfachService;
import de.svws_nrw.service.uv.raeume.UvRaumService;
import de.svws_nrw.service.uv.raeume.UvRaumgruppeService;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelFachService;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelImportService;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelService;
import de.svws_nrw.service.uv.zeitraster.UvZeitrasterEintragService;
import de.svws_nrw.service.uv.zeitraster.UvZeitrasterService;

/**
 * Eine Factory zum Erstellen der Lehrer-spezifischen Services
 */
public final class UvGrunddatenServiceFactory {

	/** die Factory für die Lehrer-Repositories */
	private final UvGrunddatenRepositoryFactory grunddatenRepositoryFactory;
	private final LehrerRepositoryFactory lehrerRepositoryFactory;
	private final EigeneSchuleRepositoryFactory schuleRepositoryFactory;
	private final SchuelerRepositoryFactory schuelerRepositoryFactory;
	private final KlassenRepositoryFactory klassenRepositoryFactory;

	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param grunddatenRepositoryFactory   die Factory für Lehrer-Repositories
	 * @param lehrerRepositoryFactory       die Factory für die Schild-Lehrer-Repositories
	 * @param schuleRepositoryFactory       die Factory für die Schul-Repositories
	 * @param schuelerRepositoryFactory     die Factory für die Schüler-Repositories
	 * @param klassenRepositoryFactory      die Factory für die Klassen-Repositories
	 */
	private UvGrunddatenServiceFactory(final UvGrunddatenRepositoryFactory grunddatenRepositoryFactory, final LehrerRepositoryFactory lehrerRepositoryFactory,
			final EigeneSchuleRepositoryFactory schuleRepositoryFactory, final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final KlassenRepositoryFactory klassenRepositoryFactory) {
		this.grunddatenRepositoryFactory = grunddatenRepositoryFactory;
		this.lehrerRepositoryFactory = lehrerRepositoryFactory;
		this.schuleRepositoryFactory = schuleRepositoryFactory;
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
		this.klassenRepositoryFactory = klassenRepositoryFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param grunddatenRepositoryFactory   die Factory für Lehrer-Repositories
	 * @param lehrerRepositoryFactory       die Factory für die Schild-Lehrer-Repositories
	 * @param schuleRepositoryFactory       die Factory für die Schul-Repositories
	 * @param schuelerRepositoryFactory     die Factory für die Schüler-Repositories
	 * @param klassenRepositoryFactory      die Factory für die Klassen-Repositories
	 * @return die neue Factory-Instanz
	 */
	public static UvGrunddatenServiceFactory getNewInstance(final UvGrunddatenRepositoryFactory grunddatenRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final EigeneSchuleRepositoryFactory schuleRepositoryFactory, final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final KlassenRepositoryFactory klassenRepositoryFactory) {
		return new UvGrunddatenServiceFactory(grunddatenRepositoryFactory, lehrerRepositoryFactory, schuleRepositoryFactory, schuelerRepositoryFactory, klassenRepositoryFactory);
	}

	/**
	 * Erzeugt eine neue Service-Factory ohne Zugriff auf Schüler-Repositories.
	 *
	 * @param grunddatenRepositoryFactory die Factory für UV-Grunddaten-Repositories
	 * @param lehrerRepositoryFactory die Factory für Lehrer-Repositories
	 * @param schuleRepositoryFactory die Factory für Schul-Repositories
	 * @return die Service-Factory
	 */
	public static UvGrunddatenServiceFactory getNewInstance(final UvGrunddatenRepositoryFactory grunddatenRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory, final EigeneSchuleRepositoryFactory schuleRepositoryFactory) {
		return getNewInstance(grunddatenRepositoryFactory, lehrerRepositoryFactory, schuleRepositoryFactory, SchuelerRepositoryFactory.getNewInstance(), KlassenRepositoryFactory.getNewInstance());
	}


	/**
	 * Erstellt einen neuen Statistik-Service für den Zugriff auf Lehrämter von Lehrern.
	 *
	 * @return der Service für die Lehrämter von Lehrern
	 */
	public UvLehrerService getUvLehrerService() {
		return new UvLehrerService(grunddatenRepositoryFactory.getUvLehrerRepository(), lehrerRepositoryFactory.getLehrerRepository());
	}

	/**
	 * Creates and returns an instance of {@link UvLehrerPflichtstundensollService}.
	 * This service is used to manage and access data related to the mandatory teaching hours
	 * required for teachers.
	 *
	 * @return an instance of {@link UvLehrerPflichtstundensollService}
	 */
	public UvLehrerPflichtstundensollService getUvLehrerPflichtstundensollService() {
		return new UvLehrerPflichtstundensollService(grunddatenRepositoryFactory.getUvLehrerPflichtstundensollRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvLehrerPflichtstundensollImportService}, der das Importieren
	 * des Pflichtstundensolls aus den Schild-Personalabschnittsdaten in die UV-Pflichtstundensoll-Tabelle kapselt.
	 *
	 * @return eine Instanz des {@link UvLehrerPflichtstundensollImportService}
	 */
	public UvLehrerPflichtstundensollImportService getUvLehrerPflichtstundensollImportService() {
		return new UvLehrerPflichtstundensollImportService(
				grunddatenRepositoryFactory.getUvLehrerPflichtstundensollRepository(),
				grunddatenRepositoryFactory.getUvLehrerRepository(),
				lehrerRepositoryFactory.getLehrerPersonalabschnittsdatenRepository(),
				schuleRepositoryFactory.getSchuljahresabschnitteRepository());
	}

	/**
	 * Creates and returns an instance of {@link UvLehrerAnrechnungsstundenService}.
	 * This service is used to manage and access the data related to the
	 * allocated hours for teachers.
	 *
	 * @return an instance of {@link UvLehrerAnrechnungsstundenService}
	 */
	public UvLehrerAnrechnungsstundenService getUvLehrerAnrechnungsstundenService() {
		return new UvLehrerAnrechnungsstundenService(grunddatenRepositoryFactory.getUvLehrerAnrechnungsstundenRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvLehrerAnrechnungsstundenImportService}, der das Importieren
	 * der Anrechnungsstunden aus den Schild-Personalabschnittsdaten in die UV-Anrechnungsstunden-Tabelle kapselt.
	 *
	 * @return eine Instanz des {@link UvLehrerAnrechnungsstundenImportService}
	 */
	public UvLehrerAnrechnungsstundenImportService getUvLehrerAnrechnungsstundenImportService() {
		return new UvLehrerAnrechnungsstundenImportService(
				grunddatenRepositoryFactory.getUvLehrerAnrechnungsstundenRepository(),
				grunddatenRepositoryFactory.getUvLehrerRepository(),
				lehrerRepositoryFactory.getLehrerPersonalabschnittsdatenRepository(),
				lehrerRepositoryFactory.getLehrerAnrechnungRepository(),
				lehrerRepositoryFactory.getLehrerMehrleistungRepository(),
				lehrerRepositoryFactory.getLehrerMinderleistungRepository(),
				schuleRepositoryFactory.getSchuljahresabschnitteRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvLehrerUnterrichtsfachService}, der den Zugriff auf
	 * die Unterrichtsfächer von UV-Lehrkräften (Tabelle UV_LehrerUnterrichtsfaecher) kapselt.
	 *
	 * @return eine Instanz des {@link UvLehrerUnterrichtsfachService}
	 */
	public UvLehrerUnterrichtsfachService getUvLehrerUnterrichtsfachService() {
		return new UvLehrerUnterrichtsfachService(grunddatenRepositoryFactory.getUvLehrerUnterrichtsfaecherRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvFachService}, der den Zugriff auf die
	 * UV-Fächer (Tabelle UV_Faecher) kapselt.
	 *
	 * @return eine Instanz des {@link UvFachService}
	 */
	public UvFachService getUvFachService() {
		return new UvFachService(grunddatenRepositoryFactory.getUvFachRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvRaumService}.
	 *
	 * @return eine Instanz des {@link UvRaumService}
	 */
	public UvRaumService getUvRaumService() {
		return new UvRaumService(grunddatenRepositoryFactory.getUvRaumRepository(),
				grunddatenRepositoryFactory.getUvRaumgruppeRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvRaumgruppeService}.
	 *
	 * @return eine Instanz des {@link UvRaumgruppeService}
	 */
	public UvRaumgruppeService getUvRaumgruppeService() {
		return new UvRaumgruppeService(grunddatenRepositoryFactory.getUvRaumgruppeRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvStundentafelService}.
	 *
	 * @return eine Instanz des {@link UvStundentafelService}
	 */
	public UvStundentafelService getUvStundentafelService() {
		return new UvStundentafelService(grunddatenRepositoryFactory.getUvStundentafelRepository());
	}

	/**
	 * Erstellt einen Service zum Importieren von Stundentafeln aus Leistungsdaten.
	 *
	 * @return eine Instanz des {@link UvStundentafelImportService}
	 */
	public UvStundentafelImportService getUvStundentafelImportService() {
		return new UvStundentafelImportService(grunddatenRepositoryFactory.getUvStundentafelRepository(),
				grunddatenRepositoryFactory.getUvStundentafelFachRepository(), grunddatenRepositoryFactory.getUvFachRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository(), schuelerRepositoryFactory.getSchuelerLeistungsdatenRepository(),
				schuleRepositoryFactory.getSchuljahresabschnitteRepository(), klassenRepositoryFactory.getKlassenRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvStundentafelFachService}.
	 *
	 * @return eine Instanz des {@link UvStundentafelFachService}
	 */
	public UvStundentafelFachService getUvStundentafelFachService() {
		return new UvStundentafelFachService(grunddatenRepositoryFactory.getUvStundentafelFachRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvZeitrasterService}.
	 *
	 * @return eine Instanz des {@link UvZeitrasterService}
	 */
	public UvZeitrasterService getUvZeitrasterService() {
		return new UvZeitrasterService(grunddatenRepositoryFactory.getUvZeitrasterRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvZeitrasterEintragService}.
	 *
	 * @return eine Instanz des {@link UvZeitrasterEintragService}
	 */
	public UvZeitrasterEintragService getUvZeitrasterEintragService() {
		return new UvZeitrasterEintragService(grunddatenRepositoryFactory.getUvZeitrasterEintragRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvZeitrasterEintragService}.
	 *
	 * @return eine Instanz des {@link UvZeitrasterEintragService}
	 */
	public UvGrunddatenBundleService getUvGrunddatenBundleService() {
		return new UvGrunddatenBundleService(getUvRaumgruppeService(), getUvRaumService(), getUvStundentafelService(), getUvStundentafelFachService(),
				getUvFachService(), getUvLehrerService(), getUvZeitrasterService(), getUvZeitrasterEintragService(), getUvLehrerAnrechnungsstundenService(),
				getUvLehrerPflichtstundensollService(), getUvLehrerUnterrichtsfachService(),
				new LehrerUnterrichtsfachService(lehrerRepositoryFactory.getLehrerUnterrichtsfachRepository()));
	}

}
