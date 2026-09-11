package de.svws_nrw.service.uv;

import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.uv.UvGrunddatenRepositoryFactory;
import de.svws_nrw.repo.uv.UvPlanungsabschnittRepositoryFactory;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import de.svws_nrw.service.uv.klassen.UvKlasseService;
import de.svws_nrw.service.uv.klassen.UvKlassenLehrerService;
import de.svws_nrw.service.uv.kurse.UvKursImportService;
import de.svws_nrw.service.uv.kurse.UvKursService;
import de.svws_nrw.service.uv.lehrer.UvPlanungsabschnittLehrerService;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppeKlassenImportService;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppeService;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppenLehrerService;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppenSchieneService;
import de.svws_nrw.service.uv.lerngruppen.UvUnterrichtLerngruppenlehrerService;
import de.svws_nrw.service.uv.planungsabschnitte.UvPlanungsabschnittService;
import de.svws_nrw.service.uv.schienen.UvSchieneService;
import de.svws_nrw.service.uv.schueler.UvPlanungsabschnittSchuelerImportService;
import de.svws_nrw.service.uv.schueler.UvPlanungsabschnittSchuelerService;
import de.svws_nrw.service.uv.schueler.UvSchuelergruppeSchuelerService;
import de.svws_nrw.service.uv.schueler.UvSchuelergruppeService;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelFachService;
import de.svws_nrw.service.uv.unterrichte.UvUnterrichtLerngruppenCreateService;
import de.svws_nrw.service.uv.unterrichte.UvUnterrichtRaumService;
import de.svws_nrw.service.uv.unterrichte.UvUnterrichtService;
import de.svws_nrw.service.uv.zeitraster.UvPlanungsabschnittZeitrasterService;

/**
 * Eine Factory zum Erstellen der Services für die UV-Planungsabschnitte und zugehörige Zuordnungen.
 */
public final class UvPlanungsabschnittServiceFactory {

	/** die Factory für die Planungsabschnitt-Repositories */
	private final UvPlanungsabschnittRepositoryFactory planungsabschnittRepositoryFactory;

	/** die Factory für die Schule-Repositories */
	private final EigeneSchuleRepositoryFactory schuleRepositoryFactory;

	/** die Factory für die Schüler-Repositories */
	private final SchuelerRepositoryFactory schuelerRepositoryFactory;

	/** die Factory für die Klassen-Repositories */
	private final KlassenRepositoryFactory klassenRepositoryFactory;

	/** die Factory für die Jahrgang-Repositories */
	private final KatalogRepositoryFactory katalogRepositoryFactory;

	/** die Factory für die UV-Grunddaten-Repositories */
	private final UvGrunddatenRepositoryFactory grunddatenRepositoryFactory;

	/**
	 * Erstellt eine neue Service-Factory.
	 *
	 * @param planungsabschnittRepositoryFactory   die Factory für die Planungsabschnitt-Repositories
	 */
	private UvPlanungsabschnittServiceFactory(final UvPlanungsabschnittRepositoryFactory planungsabschnittRepositoryFactory) {
		this.planungsabschnittRepositoryFactory = planungsabschnittRepositoryFactory;
		this.schuleRepositoryFactory = EigeneSchuleRepositoryFactory.getNewInstance();
		this.schuelerRepositoryFactory = SchuelerRepositoryFactory.getNewInstance();
		this.klassenRepositoryFactory = KlassenRepositoryFactory.getNewInstance();
		this.katalogRepositoryFactory = KatalogRepositoryFactory.getNewInstance();
		this.grunddatenRepositoryFactory = UvGrunddatenRepositoryFactory.getNewInstance();
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory.
	 *
	 * @param planungsabschnittRepositoryFactory   die Factory für die Planungsabschnitt-Repositories
	 *
	 * @return die neue Factory-Instanz
	 */
	public static UvPlanungsabschnittServiceFactory getNewInstance(final UvPlanungsabschnittRepositoryFactory planungsabschnittRepositoryFactory) {
		return new UvPlanungsabschnittServiceFactory(planungsabschnittRepositoryFactory);
	}

	/**
	 * Erzeugt eine Instanz des {@link UvPlanungsabschnittService}.
	 *
	 * @return eine Instanz des {@link UvPlanungsabschnittService}
	 */
	public UvPlanungsabschnittService getUvPlanungsabschnittService() {
		return new UvPlanungsabschnittService(planungsabschnittRepositoryFactory.getUvPlanungsabschnittRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvPlanungsabschnittLehrerService}.
	 *
	 * @return eine Instanz des {@link UvPlanungsabschnittLehrerService}
	 */
	public UvPlanungsabschnittLehrerService getUvPlanungsabschnittLehrerService() {
		return new UvPlanungsabschnittLehrerService(planungsabschnittRepositoryFactory.getUvPlanungsabschnittLehrerRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvPlanungsabschnittSchuelerService}.
	 *
	 * @return eine Instanz des {@link UvPlanungsabschnittSchuelerService}
	 */
	public UvPlanungsabschnittSchuelerService getUvPlanungsabschnittSchuelerService() {
		return new UvPlanungsabschnittSchuelerService(planungsabschnittRepositoryFactory.getUvPlanungsabschnittSchuelerRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvPlanungsabschnittSchuelerImportService}.
	 *
	 * @return eine Instanz des {@link UvPlanungsabschnittSchuelerImportService}
	 */
	public UvPlanungsabschnittSchuelerImportService getUvPlanungsabschnittSchuelerImportService() {
		return new UvPlanungsabschnittSchuelerImportService(
				new SchuljahresabschnittService(schuleRepositoryFactory.getSchuljahresabschnitteRepository()),
				schuelerRepositoryFactory.getSchuelerRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository(),
				klassenRepositoryFactory.getKlassenRepository(),
				katalogRepositoryFactory.getJahrgangRepository(),
				getUvPlanungsabschnittService(),
				getUvPlanungsabschnittSchuelerService(),
				getUvKlasseService(),
				getUvSchuelergruppeService(),
				getUvSchuelergruppeSchuelerService());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvKlasseService}.
	 *
	 * @return eine Instanz des {@link UvKlasseService}
	 */
	public UvKlasseService getUvKlasseService() {
		return new UvKlasseService(planungsabschnittRepositoryFactory.getUvKlasseRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvLerngruppeService}.
	 *
	 * @return eine Instanz des {@link UvLerngruppeService}
	 */
	public UvLerngruppeService getUvLerngruppeService() {
		return new UvLerngruppeService(planungsabschnittRepositoryFactory.getUvLerngruppeRepository(), getUvKursService());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvLerngruppeKlassenImportService}.
	 *
	 * @return eine Instanz des {@link UvLerngruppeKlassenImportService}
	 */
	public UvLerngruppeKlassenImportService getUvLerngruppeKlassenImportService() {
		return new UvLerngruppeKlassenImportService(
				getUvKlasseService(),
				new SchuljahresabschnittService(schuleRepositoryFactory.getSchuljahresabschnitteRepository()),
				new UvStundentafelFachService(grunddatenRepositoryFactory.getUvStundentafelFachRepository()),
				planungsabschnittRepositoryFactory.getUvLerngruppeRepository(),
				getUvLerngruppeService());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvSchuelergruppeSchuelerService}.
	 *
	 * @return eine Instanz des {@link UvSchuelergruppeSchuelerService}
	 */
	public UvSchuelergruppeSchuelerService getUvSchuelergruppeSchuelerService() {
		return new UvSchuelergruppeSchuelerService(planungsabschnittRepositoryFactory.getUvSchuelergruppeSchuelerRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvSchuelergruppeService}.
	 *
	 * @return eine Instanz des {@link UvSchuelergruppeService}
	 */
	public UvSchuelergruppeService getUvSchuelergruppeService() {
		return new UvSchuelergruppeService(
				planungsabschnittRepositoryFactory.getUvSchuelergruppeRepository(),
				planungsabschnittRepositoryFactory.getUvSchuelergruppeConstraintJahrgangRepository(),
				planungsabschnittRepositoryFactory.getUvSchuelergruppeConstraintSchuelergruppeRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvUnterrichtService}.
	 *
	 * @return eine Instanz des {@link UvUnterrichtService}
	 */
	public UvUnterrichtService getUvUnterrichtService() {
		return new UvUnterrichtService(planungsabschnittRepositoryFactory.getUvUnterrichtRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvUnterrichtLerngruppenCreateService}.
	 *
	 * @return eine Instanz des {@link UvUnterrichtLerngruppenCreateService}
	 */
	public UvUnterrichtLerngruppenCreateService getUvUnterrichtLerngruppenCreateService() {
		return new UvUnterrichtLerngruppenCreateService(
				getUvLerngruppeService(),
				planungsabschnittRepositoryFactory.getUvUnterrichtRepository(),
				getUvUnterrichtService());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvUnterrichtRaumService}.
	 *
	 * @return eine Instanz des {@link UvUnterrichtRaumService}
	 */
	public UvUnterrichtRaumService getUvUnterrichtRaumService() {
		return new UvUnterrichtRaumService(planungsabschnittRepositoryFactory.getUvUnterrichtRaumRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvUnterrichtLerngruppenlehrerService}.
	 *
	 * @return eine Instanz des {@link UvUnterrichtLerngruppenlehrerService}
	 */
	public UvUnterrichtLerngruppenlehrerService getUvUnterrichtLerngruppenlehrerService() {
		return new UvUnterrichtLerngruppenlehrerService(planungsabschnittRepositoryFactory.getUvUnterrichtLerngruppenlehrerRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvKlassenLehrerService}.
	 *
	 * @return eine Instanz des {@link UvKlassenLehrerService}
	 */
	public UvKlassenLehrerService getUvKlassenLehrerService() {
		return new UvKlassenLehrerService(planungsabschnittRepositoryFactory.getUvKlassenLehrerRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvLerngruppenLehrerService}.
	 *
	 * @return eine Instanz des {@link UvLerngruppenLehrerService}
	 */
	public UvLerngruppenLehrerService getUvLerngruppenLehrerService() {
		return new UvLerngruppenLehrerService(planungsabschnittRepositoryFactory.getUvLerngruppenLehrerRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvLerngruppenSchieneService}.
	 *
	 * @return eine Instanz des {@link UvLerngruppenSchieneService}
	 */
	public UvLerngruppenSchieneService getUvLerngruppenSchieneService() {
		return new UvLerngruppenSchieneService(planungsabschnittRepositoryFactory.getUvLerngruppenSchieneRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvKursService}.
	 *
	 * @return eine Instanz des {@link UvKursService}
	 */
	public UvKursService getUvKursService() {
		return new UvKursService(planungsabschnittRepositoryFactory.getUvKursRepository(),
				planungsabschnittRepositoryFactory.getUvLerngruppeRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvKursImportService}.
	 *
	 * @return eine Instanz des {@link UvKursImportService}
	 */
	public UvKursImportService getUvKursImportService() {
		return new UvKursImportService(getUvPlanungsabschnittService(), getUvPlanungsabschnittLehrerService(),
				getUvSchieneService(),
				getUvSchuelergruppeService(),
				getUvKursService(),
				getUvLerngruppeService(),
				getUvLerngruppenLehrerService(),
				getUvLerngruppenSchieneService(), getUvSchuelergruppeSchuelerService());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvSchieneService}.
	 *
	 * @return eine Instanz des {@link UvSchieneService}
	 */
	public UvSchieneService getUvSchieneService() {
		return new UvSchieneService(planungsabschnittRepositoryFactory.getUvSchieneRepository(),
				planungsabschnittRepositoryFactory.getUvSchieneConstraintJahrgangRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvPlanungsabschnittZeitrasterService}.
	 *
	 * @return eine Instanz des {@link UvPlanungsabschnittZeitrasterService}
	 */
	public UvPlanungsabschnittZeitrasterService getUvPlanungsabschnittZeitrasterService() {
		return new UvPlanungsabschnittZeitrasterService(
				planungsabschnittRepositoryFactory.getUvPlanungsabschnittZeitrasterRepository(),
				planungsabschnittRepositoryFactory.getUvPlanungsabschnittZeitrasterConstraintJahrgangRepository());
	}

	/**
	 * Erzeugt eine Instanz des {@link UvPlanungsabschnittsdatenBundleService}.
	 *
	 * @return eine Instanz des {@link UvPlanungsabschnittsdatenBundleService}
	 */
	public UvPlanungsabschnittsdatenBundleService getUvPlanungsabschnittsdatenBundleService() {
		return new UvPlanungsabschnittsdatenBundleService(getUvPlanungsabschnittService(), getUvPlanungsabschnittLehrerService(),
				getUvPlanungsabschnittSchuelerService(), getUvPlanungsabschnittZeitrasterService(), getUvSchieneService(), getUvKlasseService(),
				getUvKlassenLehrerService(), getUvKursService(), getUvSchuelergruppeService(), getUvLerngruppenLehrerService(),
				getUvLerngruppenSchieneService(), getUvLerngruppeService(), getUvSchuelergruppeSchuelerService(), getUvUnterrichtService(),
				getUvUnterrichtRaumService(), getUvUnterrichtLerngruppenlehrerService());
	}

}
