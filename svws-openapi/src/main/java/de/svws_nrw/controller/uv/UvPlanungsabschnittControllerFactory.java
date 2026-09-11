package de.svws_nrw.controller.uv;

import de.svws_nrw.controller.uv.klassen.UvKlassenLehrerController;
import de.svws_nrw.controller.uv.klassen.UvKlassenLehrerControllerImpl;
import de.svws_nrw.controller.uv.kurse.UvKursController;
import de.svws_nrw.controller.uv.kurse.UvKursControllerImpl;
import de.svws_nrw.controller.uv.kurse.UvKursImportController;
import de.svws_nrw.controller.uv.kurse.UvKursImportControllerImpl;
import de.svws_nrw.controller.uv.lerngruppen.UvLerngruppenLehrerController;
import de.svws_nrw.controller.uv.lerngruppen.UvLerngruppenLehrerControllerImpl;
import de.svws_nrw.controller.uv.lerngruppen.UvLerngruppenSchieneController;
import de.svws_nrw.controller.uv.lerngruppen.UvLerngruppenSchieneControllerImpl;
import de.svws_nrw.controller.uv.lerngruppen.UvLerngruppeKlassenImportController;
import de.svws_nrw.controller.uv.lerngruppen.UvLerngruppeKlassenImportControllerImpl;
import de.svws_nrw.controller.uv.klassen.UvKlasseController;
import de.svws_nrw.controller.uv.klassen.UvKlasseControllerImpl;
import de.svws_nrw.controller.uv.lerngruppen.UvLerngruppeController;
import de.svws_nrw.controller.uv.lerngruppen.UvLerngruppeControllerImpl;
import de.svws_nrw.controller.uv.planungsabschnitte.UvPlanungsabschnittController;
import de.svws_nrw.controller.uv.planungsabschnitte.UvPlanungsabschnittControllerImpl;
import de.svws_nrw.controller.uv.lehrer.UvPlanungsabschnittLehrerController;
import de.svws_nrw.controller.uv.lehrer.UvPlanungsabschnittLehrerControllerImpl;
import de.svws_nrw.controller.uv.schueler.UvPlanungsabschnittSchuelerImportController;
import de.svws_nrw.controller.uv.schueler.UvPlanungsabschnittSchuelerImportControllerImpl;
import de.svws_nrw.controller.uv.schueler.UvPlanungsabschnittSchuelerController;
import de.svws_nrw.controller.uv.schueler.UvPlanungsabschnittSchuelerControllerImpl;
import de.svws_nrw.controller.uv.zeitraster.UvPlanungsabschnittZeitrasterController;
import de.svws_nrw.controller.uv.zeitraster.UvPlanungsabschnittZeitrasterControllerImpl;
import de.svws_nrw.controller.uv.schueler.UvSchuelergruppeController;
import de.svws_nrw.controller.uv.schueler.UvSchuelergruppeControllerImpl;
import de.svws_nrw.controller.uv.schueler.UvSchuelergruppeSchuelerController;
import de.svws_nrw.controller.uv.schueler.UvSchuelergruppeSchuelerControllerImpl;
import de.svws_nrw.controller.uv.unterrichte.UvUnterrichtController;
import de.svws_nrw.controller.uv.unterrichte.UvUnterrichtControllerImpl;
import de.svws_nrw.controller.uv.unterrichte.UvUnterrichtLerngruppenCreateController;
import de.svws_nrw.controller.uv.unterrichte.UvUnterrichtLerngruppenCreateControllerImpl;
import de.svws_nrw.controller.uv.unterrichte.UvUnterrichtLerngruppenlehrerController;
import de.svws_nrw.controller.uv.unterrichte.UvUnterrichtLerngruppenlehrerControllerImpl;
import de.svws_nrw.controller.uv.unterrichte.UvUnterrichtRaumController;
import de.svws_nrw.controller.uv.unterrichte.UvUnterrichtRaumControllerImpl;
import de.svws_nrw.controller.uv.schienen.UvSchieneController;
import de.svws_nrw.controller.uv.schienen.UvSchieneControllerImpl;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.UvPlanungsabschnittRepositoryFactory;
import de.svws_nrw.service.uv.UvPlanungsabschnittServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Die Default-Implementierung einer Controller-Factory für den Bereich der UV-Planungsabschnitte
 * und zugehöriger Zuordnungen (Planungsabschnitt-Lehrer, Klassen-Lehrer, Lerngruppen-Lehrer,
 * Lerngruppen-Schienen).
 */
public final class UvPlanungsabschnittControllerFactory {

	/** Die Service-Factory für die UV-Planungsabschnitte */
	private final UvPlanungsabschnittServiceFactory serviceFactory;

	/**
	 * Erzeugt eine neue Factory für die übergebene Datenbank-Verbindung.
	 * Der Konstruktor ist private und wird nur von den statischen Zugriffsmethoden
	 * dieser Klasse aufgerufen.
	 */
	private UvPlanungsabschnittControllerFactory() {
		final var planungsabschnittRepositoryFactory = UvPlanungsabschnittRepositoryFactory.getNewInstance();
		this.serviceFactory = UvPlanungsabschnittServiceFactory.getNewInstance(planungsabschnittRepositoryFactory);
	}

	/**
	 * Diese statische Methode dient dem Zugriff auf die Factory in der API-Schicht mit Lese-Berechtigung.
	 *
	 * @param request   der HTTP-Request mit welchem der spezielle Controller erzeugt wird
	 *
	 * @return die Controller-Factory
	 *
	 * @throws ApiOperationException   falls die Berechtigung nicht gegeben ist
	 */
	public static UvPlanungsabschnittControllerFactory withReadAccess(final HttpServletRequest request) throws ApiOperationException {
		DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN);
		return new UvPlanungsabschnittControllerFactory();
	}

	/**
	 * Diese statische Methode dient dem Zugriff auf die Factory in der API-Schicht mit Schreib-Berechtigung.
	 *
	 * @param request   der HTTP-Request mit welchem der spezielle Controller erzeugt wird
	 *
	 * @return die Controller-Factory
	 *
	 * @throws ApiOperationException   falls die Berechtigung nicht gegeben ist
	 */
	public static UvPlanungsabschnittControllerFactory withWriteAccess(final HttpServletRequest request) throws ApiOperationException {
		DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN,
				BenutzerKompetenz.UNTERRICHTSVERTEILUNG_FUNKTIONSBEZOGEN_AENDERN);
		return new UvPlanungsabschnittControllerFactory();
	}

	/**
	 * Erstellt einen Controller für die UV-Grunddaten.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvPlanungsabschnittsdatenBundleController getUvPlanungsabschnittsdatenBundleController() throws ApiOperationException {
		return new UvPlanungsabschnittsdatenBundleControllerImpl(serviceFactory);
	}

	/**
	 * Erstellt einen Controller für die UV-Planungsabschnitte.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvPlanungsabschnittController getUvPlanungsabschnittController() throws ApiOperationException {
		return new UvPlanungsabschnittControllerImpl(serviceFactory);
	}

	/**
	 * Erstellt einen Controller für die UV-Planungsabschnitt-Lehrer-Zuordnungen.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvPlanungsabschnittLehrerController getUvPlanungsabschnittLehrerController() throws ApiOperationException {
		return new UvPlanungsabschnittLehrerControllerImpl(serviceFactory.getUvPlanungsabschnittLehrerService());
	}

	/**
	 * Erstellt einen Controller für die UV-Planungsabschnitt-Schüler-Zuordnungen.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvPlanungsabschnittSchuelerController getUvPlanungsabschnittSchuelerController() throws ApiOperationException {
		return new UvPlanungsabschnittSchuelerControllerImpl(serviceFactory.getUvPlanungsabschnittSchuelerService());
	}

	/**
	 * Erstellt einen Controller für den Schülerimport in UV-Planungsabschnitte.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvPlanungsabschnittSchuelerImportController getUvPlanungsabschnittSchuelerImportController() throws ApiOperationException {
		return new UvPlanungsabschnittSchuelerImportControllerImpl(serviceFactory.getUvPlanungsabschnittSchuelerImportService());
	}

	/**
	 * Erstellt einen Controller für die UV-Klassen.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvKlasseController getUvKlasseController() throws ApiOperationException {
		return new UvKlasseControllerImpl(serviceFactory.getUvKlasseService());
	}

	/**
	 * Erstellt einen Controller für die UV-Lerngruppen.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvLerngruppeController getUvLerngruppeController() throws ApiOperationException {
		return new UvLerngruppeControllerImpl(serviceFactory.getUvLerngruppeService());
	}

	/**
	 * Erstellt einen Controller für das Erstellen von UV-Lerngruppen aus Klassen.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvLerngruppeKlassenImportController getUvLerngruppeKlassenImportController() throws ApiOperationException {
		return new UvLerngruppeKlassenImportControllerImpl(serviceFactory.getUvLerngruppeKlassenImportService());
	}

	/**
	 * Erstellt einen Controller für die UV-Schülergruppen.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvSchuelergruppeController getUvSchuelergruppeController() throws ApiOperationException {
		return new UvSchuelergruppeControllerImpl(serviceFactory.getUvSchuelergruppeService());
	}

	/**
	 * Erstellt einen Controller für die UV-Schülergruppe-Schüler-Zuordnungen.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvSchuelergruppeSchuelerController getUvSchuelergruppeSchuelerController() throws ApiOperationException {
		return new UvSchuelergruppeSchuelerControllerImpl(serviceFactory.getUvSchuelergruppeSchuelerService());
	}

	/**
	 * Erstellt einen Controller für die UV-Unterrichte.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvUnterrichtController getUvUnterrichtController() throws ApiOperationException {
		return new UvUnterrichtControllerImpl(serviceFactory.getUvUnterrichtService());
	}

	/**
	 * Erstellt einen Controller für das Erstellen von UV-Unterrichten aus Lerngruppen.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvUnterrichtLerngruppenCreateController getUvUnterrichtLerngruppenCreateController() throws ApiOperationException {
		return new UvUnterrichtLerngruppenCreateControllerImpl(serviceFactory.getUvUnterrichtLerngruppenCreateService());
	}

	/**
	 * Erstellt einen Controller für die UV-Unterricht-Raum-Zuordnungen.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvUnterrichtRaumController getUvUnterrichtRaumController() throws ApiOperationException {
		return new UvUnterrichtRaumControllerImpl(serviceFactory.getUvUnterrichtRaumService());
	}

	/**
	 * Erstellt einen Controller für die UV-Unterricht-Lerngruppenlehrer-Zuordnungen.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvUnterrichtLerngruppenlehrerController getUvUnterrichtLerngruppenlehrerController() throws ApiOperationException {
		return new UvUnterrichtLerngruppenlehrerControllerImpl(serviceFactory.getUvUnterrichtLerngruppenlehrerService());
	}

	/**
	 * Erstellt einen Controller für die UV-Klassen-Lehrer-Zuordnungen.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvKlassenLehrerController getUvKlassenLehrerController() throws ApiOperationException {
		return new UvKlassenLehrerControllerImpl(serviceFactory.getUvKlassenLehrerService());
	}

	/**
	 * Erstellt einen Controller für die UV-Lerngruppen-Lehrer-Zuordnungen.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvLerngruppenLehrerController getUvLerngruppenLehrerController() throws ApiOperationException {
		return new UvLerngruppenLehrerControllerImpl(serviceFactory.getUvLerngruppenLehrerService());
	}

	/**
	 * Erstellt einen Controller für die UV-Lerngruppen-Schienen-Zuordnungen.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvLerngruppenSchieneController getUvLerngruppenSchieneController() throws ApiOperationException {
		return new UvLerngruppenSchieneControllerImpl(serviceFactory.getUvLerngruppenSchieneService());
	}

	/**
	 * Erstellt einen Controller für die UV-Kurse.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvKursController getUvKursController() throws ApiOperationException {
		return new UvKursControllerImpl(serviceFactory.getUvKursService());
	}

	/**
	 * Erstellt einen Controller für den Import von Gost-Kursblockungen.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvKursImportController getUvKursImportController() throws ApiOperationException {
		return new UvKursImportControllerImpl(serviceFactory.getUvKursImportService());
	}

	/**
	 * Erstellt einen Controller für die UV-Schienen.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvSchieneController getUvSchieneController() throws ApiOperationException {
		return new UvSchieneControllerImpl(serviceFactory.getUvSchieneService());
	}

	/**
	 * Erstellt einen Controller für die UV-Planungsabschnitt-Zeitraster-Zuordnungen.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvPlanungsabschnittZeitrasterController getUvPlanungsabschnittZeitrasterController() throws ApiOperationException {
		return new UvPlanungsabschnittZeitrasterControllerImpl(serviceFactory.getUvPlanungsabschnittZeitrasterService());
	}

}
