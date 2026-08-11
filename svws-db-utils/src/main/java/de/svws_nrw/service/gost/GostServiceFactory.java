package de.svws_nrw.service.gost;

import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.gost.GostRepositoryFactory;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.service.benutzer.BenutzerServiceFactory;
import de.svws_nrw.service.crypto.CryptoServiceFactory;
import de.svws_nrw.service.schueler.SchuelerServiceFactory;

/**
 * Eine Factory zum Erstellen der Services für die gymnasiale Oberstufe
 */
public final class GostServiceFactory {

	/** die Factory für die Repositories für die gymnasiale Oberstufe */
	private final GostRepositoryFactory gostRepositoryFactory;

	/** die Factory für die Schüler-Repositories */
	private final SchuelerRepositoryFactory schuelerRepositoryFactory;

	/** die Factory für die Lehrer-Repositories */
	private final LehrerRepositoryFactory lehrerRepositoryFactory;

	/** die Factory für die Benutzer-Repositories */
	private final BenutzerRepositoryFactory benutzerRepositoryFactory;

	/** die Factory für die Katalog-Repositories */
	private final KatalogRepositoryFactory katalogRepositoryFactory;

	/** die Factory für die Schul-Repositories */
	private final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory;

	/** die Factory für die Benutzer-Services */
	private final BenutzerServiceFactory benutzerServiceFactory;

	/** die Factory für die kryptographischen Services */
	private final CryptoServiceFactory cryptoServiceFactory;

	/** die Factory für die Schüler-Services */
	private final SchuelerServiceFactory schuelerServiceFactory;

	/** die Factory für die Repositories für die Klausurplanung für die gymnasiale Oberstufe */
	private final GostKlausurenRepositoryFactory gostKlausurenRepositoryFactory;


	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param gostRepositoryFactory            die Factory für die Repositories für die gymnasiale Oberstufe
	 * @param schuelerRepositoryFactory        die Factory für Schüler-Repositories
	 * @param lehrerRepositoryFactory          die Factory für Lehrer-Repositories
	 * @param benutzerRepositoryFactory        die Factory für Benutzer-Repositories
	 * @param katalogRepositoryFactory        die Factory für die Katalog-Repositories
	 * @param eigeneSchuleRepositoryFactory          die Factory für Schul-Repositories
	 * @param benutzerServiceFactory           die Factory für die Benutzer-Services
	 * @param cryptoServiceFactory             die Factory für die kryptographischen Services
	 * @param schuelerServiceFactory           die Factory für die Schüler-Services
	 * @param gostKlausurenRepositoryFactory   die Factory für die Klausuren in der gymnasialen Oberstufe
	 */
	private GostServiceFactory(final GostRepositoryFactory gostRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final BenutzerRepositoryFactory benutzerRepositoryFactory,
			final KatalogRepositoryFactory katalogRepositoryFactory,
			final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory,
			final BenutzerServiceFactory benutzerServiceFactory,
			final CryptoServiceFactory cryptoServiceFactory,
			final SchuelerServiceFactory schuelerServiceFactory,
			final GostKlausurenRepositoryFactory gostKlausurenRepositoryFactory) {
		this.gostRepositoryFactory = gostRepositoryFactory;
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
		this.lehrerRepositoryFactory = lehrerRepositoryFactory;
		this.benutzerRepositoryFactory = benutzerRepositoryFactory;
		this.katalogRepositoryFactory = katalogRepositoryFactory;
		this.eigeneSchuleRepositoryFactory = eigeneSchuleRepositoryFactory;
		this.benutzerServiceFactory = benutzerServiceFactory;
		this.cryptoServiceFactory = cryptoServiceFactory;
		this.schuelerServiceFactory = schuelerServiceFactory;
		this.gostKlausurenRepositoryFactory = gostKlausurenRepositoryFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param gostRepositoryFactory       die Factory für die Repositories für die gymnasiale Oberstufe
	 * @param schuelerRepositoryFactory   die Factory für Schüler-Repositories
	 * @param lehrerRepositoryFactory     die Factory für Lehrer-Repositories
	 * @param benutzerRepositoryFactory   die Factory für Benutzer-Repositories
	 * @param katalogRepositoryFactory   die Factory für die Katalog-Repositories
	 * @param eigeneSchuleRepositoryFactory     die Factory für Schul-Repositories
	 * @param benutzerServiceFactory      die Factory für die Benutzer-Services
	 * @param cryptoServiceFactory        die Factory für die kryptographischen Services
	 * @param schuelerServiceFactory      die Factory für die Schüler-Services
	 * @param gostKlausurenRepositoryFactory   die Factory für die Klausuren in der gymnasialen Oberstufe
	 *
	 * @return die Factory
	 */
	public static GostServiceFactory getNewInstance(final GostRepositoryFactory gostRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final BenutzerRepositoryFactory benutzerRepositoryFactory,
			final KatalogRepositoryFactory katalogRepositoryFactory,
			final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory,
			final BenutzerServiceFactory benutzerServiceFactory,
			final CryptoServiceFactory cryptoServiceFactory,
			final SchuelerServiceFactory schuelerServiceFactory,
			final GostKlausurenRepositoryFactory gostKlausurenRepositoryFactory) {
		return new GostServiceFactory(gostRepositoryFactory, schuelerRepositoryFactory, lehrerRepositoryFactory, benutzerRepositoryFactory,
				katalogRepositoryFactory, eigeneSchuleRepositoryFactory, benutzerServiceFactory, cryptoServiceFactory, schuelerServiceFactory,
				gostKlausurenRepositoryFactory);
	}


	/**
	 * Erstellt einen neuen Service für die Fächer der gymnasialen Oberstufe
	 *
	 * @return der Service
	 */
	public GostFaecherService getGostFaecherService() {
		return new GostFaecherService(benutzerRepositoryFactory.getBenutzerAllgemeinRepository(),
				katalogRepositoryFactory.getFachRepository(),
				gostRepositoryFactory.getGostJahrgangFaecherRepository());
	}


	/**
	 * Erstellt einen neuen Service für die Beratungslehrer der gymnasialen Oberstufe
	 *
	 * @return der Service
	 */
	public GostBeratungslehrerService getGostBeratungslehrerService() {
		return new GostBeratungslehrerService(lehrerRepositoryFactory.getLehrerRepository(),
				gostRepositoryFactory.getGostJahrgangBeratungslehrerRepository());
	}


	/**
	 * Erstellt einen neuen Service für die Schüler der gymnasialen Oberstufe
	 *
	 * @return der Service
	 */
	public GostSchuelerService getGostSchuelerService() {
		return new GostSchuelerService(benutzerRepositoryFactory.getBenutzerAllgemeinRepository(),
				katalogRepositoryFactory.getJahrgangRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository());
	}


	/**
	 * Erstellt einen neuen Service für GOSt-Belegungen zu Kursen.
	 *
	 * @return der Service
	 */
	public GostKursBelegungService getGostKursBelegungService() {
		return new GostKursBelegungService(schuelerRepositoryFactory.getSchuelerLernabschnittRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				eigeneSchuleRepositoryFactory.getSchuljahresabschnitteRepository());
	}


	/**
	 * Erstellt einen neuen Service für die Aggregation der Abiturdaten aus den Leistungsdaten und den Fachwahlen der gymnasialen Oberstufe
	 *
	 * @return der Service
	 */
	public GostAbiturdatenService getGostAbiturdatenService() {
		return new GostAbiturdatenService(benutzerRepositoryFactory.getBenutzerAllgemeinRepository(),
				katalogRepositoryFactory.getJahrgangRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenRepository(),
				gostRepositoryFactory.getGostSchuelerFachbelegungenRepository(),
				benutzerServiceFactory.getBenutzerKompetenzService(),
				schuelerServiceFactory.getSchuelerSprachdatenService(),
				this.getGostFaecherService(),
				this.getGostSchuelerService());
	}


	/**
	 * Erstellt einen neuen Service für die Fachwahlen der der gymnasialen Oberstufe
	 *
	 * @return der Service
	 */
	public GostFachwahlService getGostFachwahlService() {
		return new GostFachwahlService(benutzerRepositoryFactory.getBenutzerAllgemeinRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenRepository(),
				katalogRepositoryFactory.getJahrgangRepository(),
				katalogRepositoryFactory.getFachRepository(),
				gostRepositoryFactory.getGostSchuelerRepository(),
				gostRepositoryFactory.getGostSchuelerFachbelegungenRepository(),
				gostRepositoryFactory.getGostJahrgangsdatenRepository(),
				gostRepositoryFactory.getGostJahrgangFachbelegungenRepository(),
				gostKlausurenRepositoryFactory.getGostKlausurenVorgabeRepository(),
				this.getGostAbiturdatenService(),
				this.getGostSchuelerService());
	}


	/**
	 * Erstellt einen neuen Service für die aggregierten Fachwahlen eines Abiturjahrgangs der gymnasialen Oberstufe
	 *
	 * @return der Service
	 */
	public GostJahrgangFachwahlService getGostJahrgangFachwahlService() {
		return new GostJahrgangFachwahlService(benutzerRepositoryFactory.getBenutzerAllgemeinRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				katalogRepositoryFactory.getFachRepository(),
				this.getGostAbiturdatenService());
	}


	/**
	 * Erstellt einen neuen Service für die Wahlen zu Gleichwertig Komplexen Lernleistungen in der gymnasialen Oberstufe
	 *
	 * @return der Service
	 */
	public GostSchuelerGKLWahlService getGostSchuelerGKLWahlService() {
		return new GostSchuelerGKLWahlService(benutzerRepositoryFactory.getBenutzerAllgemeinRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				gostRepositoryFactory.getGostSchuelerRepository(),
				gostKlausurenRepositoryFactory.getGostKlausurenVorgabeRepository());
	}


	/**
	 * Erstellt einen neuen Export-Service für die Laufbahnplanung der gymnasialen Oberstufe (Datenformat - Version 1)
	 *
	 * @return der Service
	 */
	public GostLaufbahnplanungExportV1Service getGostLaufbahnplanungExportV1Service() {
		return new GostLaufbahnplanungExportV1Service(benutzerRepositoryFactory.getBenutzerAllgemeinRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				gostRepositoryFactory.getGostJahrgangFachkombinationenRepository(),
				gostRepositoryFactory.getGostJahrgangsdatenRepository(),
				cryptoServiceFactory.getSchuelerCredentialsService(),
				this.getGostAbiturdatenService(),
				this.getGostFaecherService(),
				this.getGostBeratungslehrerService());
	}


	/**
	 * Erstellt einen neuen Import-Service für die Laufbahnplanung der gymnasialen Oberstufe (Datenformat - Version 1)
	 *
	 * @return der Service
	 */
	public GostLaufbahnplanungImportV1Service getGostLaufbahnplanungImportV1Service() {
		return new GostLaufbahnplanungImportV1Service(benutzerRepositoryFactory.getBenutzerAllgemeinRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				gostRepositoryFactory.getGostSchuelerRepository(),
				gostRepositoryFactory.getGostSchuelerFachbelegungenRepository(),
				cryptoServiceFactory.getSchuelerCredentialsService(),
				this.getGostAbiturdatenService(),
				this.getGostFaecherService());
	}

	/**
	 * Erstellt einen neuen Export-Service für die Laufbahnplanung der gymnasialen Oberstufe (Datenformat - Version 1)
	 *
	 * @return der Service
	 */
	public GostLaufbahnplanungExportV2Service getGostLaufbahnplanungExportV2Service() {
		return new GostLaufbahnplanungExportV2Service(benutzerRepositoryFactory.getBenutzerAllgemeinRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				gostRepositoryFactory.getGostSchuelerRepository(),
				gostRepositoryFactory.getGostJahrgangFachkombinationenRepository(),
				gostRepositoryFactory.getGostJahrgangsdatenRepository(),
				gostKlausurenRepositoryFactory.getGostKlausurenVorgabeRepository(),
				cryptoServiceFactory.getSchuelerCredentialsService(),
				this.getGostAbiturdatenService(),
				this.getGostFaecherService(),
				this.getGostBeratungslehrerService());
	}


	/**
	 * Erstellt einen neuen Import-Service für die Laufbahnplanung der gymnasialen Oberstufe (Datenformat - Version 1)
	 *
	 * @return der Service
	 */
	public GostLaufbahnplanungImportV2Service getGostLaufbahnplanungImportV2Service() {
		return new GostLaufbahnplanungImportV2Service(benutzerRepositoryFactory.getBenutzerAllgemeinRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				gostRepositoryFactory.getGostSchuelerRepository(),
				gostRepositoryFactory.getGostSchuelerFachbelegungenRepository(),
				cryptoServiceFactory.getSchuelerCredentialsService(),
				this.getGostAbiturdatenService(),
				this.getGostFaecherService());
	}

}
