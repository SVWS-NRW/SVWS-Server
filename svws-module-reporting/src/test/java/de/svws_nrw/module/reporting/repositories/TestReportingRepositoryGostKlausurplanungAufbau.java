package de.svws_nrw.module.reporting.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenAlleKlausurdaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenHalbjahresdaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausuren.GostKursklausur;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausurtermin;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.filterung.ReportingFilterService;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungSchuelerklausur;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.service.gost.klausuren.GostKlausurenAllDataService;
import de.svws_nrw.service.gost.klausuren.GostKlausurenServiceFactory;
import de.svws_nrw.service.gost.klausuren.GostKlausurenServiceFactoryBuilder;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft, dass das Repository der GOSt-Klausurplanung seinen Manager beim ersten Zugriff aufbaut und dass es keinen Zugriff auf ein nur teilweise aufgebautes
 * Repository gibt. Ein gescheiterter Aufbau wird gemerkt und an jedem Getter unverändert gemeldet; ein gelungener Aufbau läuft kein zweites Mal.
 */
@DisplayName("Der Aufbau des Klausurplanungs-Repositorys beim ersten Zugriff")
class TestReportingRepositoryGostKlausurplanungAufbau {

	/** Das Abiturjahr der einzigen ausgewählten Stufe. */
	private static final int ABITURJAHR = 2026;

	/** Die kombinierte ID der einzigen ausgewählten Stufe aus Abiturjahr und GOSt-Halbjahr. */
	private static final long ID_STUFE = (ABITURJAHR * 10L) + GostHalbjahr.Q12.id;

	/** Die ID des Klausurtermins der Fixture. */
	private static final long ID_KLAUSURTERMIN = 41L;

	/** Die ID der Klausurvorgabe der Fixture. */
	private static final long ID_VORGABE = 51L;

	/** Die ID der Kursklausur der Fixture. */
	private static final long ID_KURSKLAUSUR = 61L;

	/** Die ID der Schülerklausur der Fixture. */
	private static final long ID_SCHUELERKLAUSUR = 71L;

	/** Die ID des Schülerklausurtermins der Fixture. */
	private static final long ID_SCHUELERKLAUSURTERMIN = 81L;

	/** Die ID des Kurses, zu dem die Kursklausur gehört. */
	private static final long ID_KURS = 91L;

	/** Die ID des Faches der Kursklausur. */
	private static final long ID_FACH = 1L;

	/** Die ID des Schülers, der die Schülerklausur schreibt. */
	private static final long ID_SCHUELER = 100L;

	/** Die ID der zweiten Schülerklausur derselben Kursklausur. */
	private static final long ID_SCHUELERKLAUSUR_MIT_FEHLER = 72L;

	/** Die ID des Schülerklausurtermins der zweiten Schülerklausur. */
	private static final long ID_SCHUELERKLAUSURTERMIN_MIT_FEHLER = 82L;

	/** Die ID des Schülers, dessen Verarbeitung den Aufbau scheitern lässt. */
	private static final long ID_SCHUELER_MIT_FEHLER = 101L;

	/** Der gemockte Context, über den das Repository seine Umgebung erreicht. */
	private ReportingContext reportingContext;

	/** Der gemockte Dienst, der die Klausurdaten zur ausgewählten Stufe liefert. */
	private GostKlausurenAllDataService allDataService;

	/** Das gemockte zentrale Schüler-Repository, aus dem der Aufbau die geteilten Schüler-Objekte bezieht. */
	private ReportingRepositorySchueler repositorySchueler;

	/** Die Klausurliste des ReportingSchueler, an die das Repository seine Schülerklausuren hängt. */
	private List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausurenAmSchueler;


	/** Die Schülerdaten der Klausurdaten stammen aus den Core-Types und verlangen deren Initialisierung. */
	@BeforeAll
	static void setUpCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(new Logger());
		final ReportingParameterTypisiert reportingParameter = mock(ReportingParameterTypisiert.class);
		when(reportingParameter.idsHauptdaten()).thenReturn(List.of(ID_STUFE));
		when(reportingContext.reportingParameter()).thenReturn(reportingParameter);
		when(reportingContext.filterService()).thenReturn(mock(ReportingFilterService.class));
		when(reportingContext.repositorySchule()).thenReturn(mock(ReportingRepositorySchule.class, RETURNS_DEEP_STUBS));

		final ReportingRepositoryGost repositoryGost = mock(ReportingRepositoryGost.class);
		when(repositoryGost.abiturjahrgaenge()).thenReturn(List.of(ABITURJAHR));
		when(reportingContext.repositoryGost()).thenReturn(repositoryGost);

		schuelerklausurenAmSchueler = new ArrayList<>();
		final ReportingSchueler schueler = mock(ReportingSchueler.class);
		when(schueler.id()).thenReturn(ID_SCHUELER);
		when(schueler.gostKlausurplanungSchuelerklausuren()).thenReturn(schuelerklausurenAmSchueler);
		repositorySchueler = mock(ReportingRepositorySchueler.class);
		when(repositorySchueler.schueler(ID_SCHUELER)).thenReturn(schueler);
		when(repositorySchueler.schueler(any())).thenReturn(new ArrayList<>(List.of(schueler)));
		when(reportingContext.repositorySchueler()).thenReturn(repositorySchueler);

		final ReportingKurs kurs = mock(ReportingKurs.class);
		when(kurs.id()).thenReturn(ID_KURS);
		final ReportingRepositoryLerngruppen repositoryLerngruppen = mock(ReportingRepositoryLerngruppen.class);
		when(repositoryLerngruppen.kurs(ID_KURS)).thenReturn(kurs);
		when(reportingContext.repositoryLerngruppen()).thenReturn(repositoryLerngruppen);

		allDataService = mock(GostKlausurenAllDataService.class);
	}


	/**
	 * Hängt den gemockten Klausurdaten-Dienst in die Service-Factory ein. Der Ladeweg des Repositorys läuft über den statischen Builder, nicht über die
	 * Datenbankverbindung des Contexts.
	 *
	 * @return Der statische Mock des Builders; er ist vom Aufrufer zu schließen.
	 */
	private MockedStatic<GostKlausurenServiceFactoryBuilder> serviceFactoryMitAllDataService() {
		final MockedStatic<GostKlausurenServiceFactoryBuilder> builder = mockStatic(GostKlausurenServiceFactoryBuilder.class);
		final GostKlausurenServiceFactory serviceFactory = mock(GostKlausurenServiceFactory.class);
		when(serviceFactory.getGostKlausurenAllDataService()).thenReturn(allDataService);
		builder.when(GostKlausurenServiceFactoryBuilder::getGostKlausurenServiceFactory).thenReturn(serviceFactory);
		return builder;
	}

	/**
	 * Baut die Klausurdaten der ausgewählten Stufe auf: ein Klausurtermin, eine Kursklausur auf diesem Termin und eine Schülerklausur dazu. Ein leerer Plan
	 * genügt nicht, denn seine leeren Listen sind von einem übersprungenen Aufbau nicht zu unterscheiden.
	 *
	 * @return Die Klausurdaten für den gemockten Dienst.
	 */
	private static GostKlausurenAlleKlausurdaten klausurdatenMitEinerKlausur() {
		final GostKlausurenHalbjahresdaten halbjahresdaten = new GostKlausurenHalbjahresdaten(ABITURJAHR, GostHalbjahr.Q12.id);
		halbjahresdaten.idSchuljahresabschnitt = 1L;

		final KursDaten kurs = new KursDaten();
		kurs.id = ID_KURS;
		kurs.idSchuljahresabschnitt = 1L;
		kurs.kuerzel = "D-GK1";
		kurs.idFach = ID_FACH;
		kurs.kursartAllg = "GK";
		halbjahresdaten.kurse.add(kurs);

		final SchuelerListeEintrag schueler = new SchuelerListeEintrag();
		schueler.id = ID_SCHUELER;
		schueler.nachname = "Muster";
		schueler.vorname = "Max";
		halbjahresdaten.schueler = new ArrayList<>(List.of(schueler));

		final GostKlausurvorgabe vorgabe = new GostKlausurvorgabe();
		vorgabe.id = ID_VORGABE;
		vorgabe.abiturjahrgang = ABITURJAHR;
		vorgabe.halbjahr = GostHalbjahr.Q12.id;
		vorgabe.quartal = 1;
		vorgabe.idFach = ID_FACH;
		vorgabe.kursart = "GK";
		vorgabe.dauer = 90;
		halbjahresdaten.klausurdaten.vorgaben.add(vorgabe);

		final GostKlausurtermin termin = new GostKlausurtermin();
		termin.id = ID_KLAUSURTERMIN;
		termin.idSchuljahresabschnitt = 1L;
		termin.abiturjahrgang = ABITURJAHR;
		termin.halbjahr = GostHalbjahr.Q12.id;
		termin.quartal = 1;
		termin.datum = "2026-02-10";
		termin.bezeichnung = "Klausurtag";
		termin.istHaupttermin = true;
		halbjahresdaten.klausurdaten.termine.add(termin);

		final GostKursklausur kursklausur = new GostKursklausur();
		kursklausur.id = ID_KURSKLAUSUR;
		kursklausur.idVorgabe = ID_VORGABE;
		kursklausur.idKurs = ID_KURS;
		kursklausur.idTermin = ID_KLAUSURTERMIN;
		halbjahresdaten.klausurdaten.kursklausuren.add(kursklausur);

		final GostSchuelerklausur schuelerklausur = new GostSchuelerklausur();
		schuelerklausur.id = ID_SCHUELERKLAUSUR;
		schuelerklausur.idKursklausur = ID_KURSKLAUSUR;
		schuelerklausur.idSchueler = ID_SCHUELER;
		halbjahresdaten.klausurdaten.schuelerklausuren.add(schuelerklausur);

		// FolgeNr 0 ohne eigene Termin-ID bezeichnet den Termin der Kursklausur.
		final GostSchuelerklausurtermin schuelerklausurtermin = new GostSchuelerklausurtermin();
		schuelerklausurtermin.id = ID_SCHUELERKLAUSURTERMIN;
		schuelerklausurtermin.idSchuelerklausur = ID_SCHUELERKLAUSUR;
		schuelerklausurtermin.folgeNr = 0;
		halbjahresdaten.klausurdaten.schuelerklausurtermine.add(schuelerklausurtermin);

		final GostKlausurenAlleKlausurdaten alleKlausurdaten = new GostKlausurenAlleKlausurdaten();
		alleKlausurdaten.halbjahresdaten.add(halbjahresdaten);
		return alleKlausurdaten;
	}

	/**
	 * Ergänzt die Klausurdaten um einen zweiten Schüler mit einer Schülerklausur derselben Kursklausur. Beide Schülerklausuren teilen sich die Klausurvorgabe,
	 * daher ordnet der Klausurplan-Manager sie nach ihrer ID. Die Schülerklausur des ersten Schülers wird deshalb zuerst verteilt.
	 *
	 * @return Die Klausurdaten für den gemockten Dienst.
	 */
	private static GostKlausurenAlleKlausurdaten klausurdatenMitZweiKlausuren() {
		final GostKlausurenAlleKlausurdaten alleKlausurdaten = klausurdatenMitEinerKlausur();
		final GostKlausurenHalbjahresdaten halbjahresdaten = alleKlausurdaten.halbjahresdaten.get(0);

		final SchuelerListeEintrag schueler = new SchuelerListeEintrag();
		schueler.id = ID_SCHUELER_MIT_FEHLER;
		schueler.nachname = "Muster";
		schueler.vorname = "Erika";
		halbjahresdaten.schueler.add(schueler);

		final GostSchuelerklausur schuelerklausur = new GostSchuelerklausur();
		schuelerklausur.id = ID_SCHUELERKLAUSUR_MIT_FEHLER;
		schuelerklausur.idKursklausur = ID_KURSKLAUSUR;
		schuelerklausur.idSchueler = ID_SCHUELER_MIT_FEHLER;
		halbjahresdaten.klausurdaten.schuelerklausuren.add(schuelerklausur);

		final GostSchuelerklausurtermin schuelerklausurtermin = new GostSchuelerklausurtermin();
		schuelerklausurtermin.id = ID_SCHUELERKLAUSURTERMIN_MIT_FEHLER;
		schuelerklausurtermin.idSchuelerklausur = ID_SCHUELERKLAUSUR_MIT_FEHLER;
		schuelerklausurtermin.folgeNr = 0;
		halbjahresdaten.klausurdaten.schuelerklausurtermine.add(schuelerklausurtermin);

		return alleKlausurdaten;
	}


	@Test
	void testDieReihenfolgeDerGetterAendertDieDatenNicht() throws ApiOperationException {
		when(allDataService.getAllData(any())).thenReturn(klausurdatenMitEinerKlausur());

		try (MockedStatic<GostKlausurenServiceFactoryBuilder> serviceFactoryBuilder = serviceFactoryMitAllDataService()) {
			final ReportingRepositoryGostKlausurplanung ueberKursklausuren = new ReportingRepositoryGostKlausurplanung(reportingContext);
			assertEquals(List.of(ID_KURSKLAUSUR), ueberKursklausuren.kursklausuren().stream().map(k -> k.id()).toList());
			assertEquals(List.of(ID_KLAUSURTERMIN), ueberKursklausuren.klausurtermine().stream().map(t -> t.id()).toList());

			final ReportingRepositoryGostKlausurplanung ueberTermine = new ReportingRepositoryGostKlausurplanung(reportingContext);
			assertEquals(List.of(ID_KLAUSURTERMIN), ueberTermine.klausurtermine().stream().map(t -> t.id()).toList());
			assertEquals(List.of(ID_KURSKLAUSUR), ueberTermine.kursklausuren().stream().map(k -> k.id()).toList());
		}
	}

	@Test
	void testDerAufbauLaeuftBeiMehrfachemZugriffNurEinmal() throws ApiOperationException {
		when(allDataService.getAllData(any())).thenReturn(klausurdatenMitEinerKlausur());

		try (MockedStatic<GostKlausurenServiceFactoryBuilder> serviceFactoryBuilder = serviceFactoryMitAllDataService()) {
			final ReportingRepositoryGostKlausurplanung repository = new ReportingRepositoryGostKlausurplanung(reportingContext);
			repository.klausurtermine();
			repository.kursklausuren();
			repository.schuelerklausuren();

			verify(allDataService, times(1)).getAllData(any());
		}
	}

	@Test
	void testEinAufbaufehlerTraegtAnJedemGetterDenselbenStatus() throws ApiOperationException {
		final ApiOperationException ursache = new ApiOperationException(Status.NOT_FOUND, "Die Klausurdaten sind nicht vorhanden.");
		when(allDataService.getAllData(any())).thenThrow(ursache);

		try (MockedStatic<GostKlausurenServiceFactoryBuilder> serviceFactoryBuilder = serviceFactoryMitAllDataService()) {
			final ReportingRepositoryGostKlausurplanung repository = new ReportingRepositoryGostKlausurplanung(reportingContext);

			final ApiOperationException ueberListe = assertThrows(ApiOperationException.class, repository::klausurtermine);
			final ApiOperationException ueberEinzelzugriff = assertThrows(ApiOperationException.class, () -> repository.klausurtermin(ID_KLAUSURTERMIN));
			// Die Zählmethode speist das Feld angefordert des Hinweis-Headers. Ohne Aufbau meldete sie still 0 und die Ausgabe erschiene vollständig.
			final ApiOperationException ueberZaehlmethode = assertThrows(ApiOperationException.class, repository::anzahlKlausurtermineVorhanden);

			assertEquals(Status.NOT_FOUND, ueberListe.getStatus());
			assertEquals(Status.NOT_FOUND, ueberEinzelzugriff.getStatus());
			assertEquals(Status.NOT_FOUND, ueberZaehlmethode.getStatus());
			assertSame(ursache, ueberZaehlmethode, "Die Meldung der Datenschicht bleibt unverändert erhalten.");
		}
	}

	@Test
	void testEinZweiterZugriffNachEinemAufbaufehlerLaedtNichtErneut() throws ApiOperationException {
		// Ein zweiter Lauf hängt die Schülerklausuren ein weiteres Mal an die geteilten Schüler-Objekte.
		when(allDataService.getAllData(any())).thenThrow(new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Klausurdaten sind fehlerhaft."));

		try (MockedStatic<GostKlausurenServiceFactoryBuilder> serviceFactoryBuilder = serviceFactoryMitAllDataService()) {
			final ReportingRepositoryGostKlausurplanung repository = new ReportingRepositoryGostKlausurplanung(reportingContext);

			final ApiOperationException erster = assertThrows(ApiOperationException.class, repository::klausurtermine);
			final ApiOperationException zweiter = assertThrows(ApiOperationException.class, repository::kursklausuren);

			assertSame(erster, zweiter, "Der gemerkte Fehler wird unverändert erneut gemeldet.");
			verify(allDataService, times(1)).getAllData(any());
		}
	}

	@Test
	void testEinUngepruefterFehlerBeendetDenAufbauEbenfallsEndgueltig() throws ApiOperationException {
		// Die Core-Manager melden ihre Datenfehler als DeveloperNotificationException. Auch sie beendet den Aufbau endgültig.
		when(allDataService.getAllData(any())).thenThrow(new DeveloperNotificationException("Fehlerinjektion"));

		try (MockedStatic<GostKlausurenServiceFactoryBuilder> serviceFactoryBuilder = serviceFactoryMitAllDataService()) {
			final ReportingRepositoryGostKlausurplanung repository = new ReportingRepositoryGostKlausurplanung(reportingContext);

			final ApiOperationException erster = assertThrows(ApiOperationException.class, repository::klausurtermine);
			final ApiOperationException zweiter = assertThrows(ApiOperationException.class, repository::schuelerklausuren);

			assertEquals(Status.INTERNAL_SERVER_ERROR, erster.getStatus());
			assertSame(erster, zweiter, "Der gemerkte Fehler wird unverändert erneut gemeldet.");
			verify(allDataService, times(1)).getAllData(any());
		}
	}

	@Test
	void testEinFehlerAusserhalbDerFehlermerkungLaesstKeineHalbenObjekteZurueck() throws ApiOperationException {
		// Das Feld trägt den Manager erst nach dem vollständigen Aufbau, das Kennzeichen hält den Versuch fest. Ein Fehler außerhalb des Catch hinterlässt
		// deshalb weder halbe Bestände noch einen zweiten Aufbau.
		when(allDataService.getAllData(any())).thenThrow(new StackOverflowError("Fehlerinjektion"));

		try (MockedStatic<GostKlausurenServiceFactoryBuilder> serviceFactoryBuilder = serviceFactoryMitAllDataService()) {
			final ReportingRepositoryGostKlausurplanung repository = new ReportingRepositoryGostKlausurplanung(reportingContext);

			assertThrows(StackOverflowError.class, repository::klausurtermine, "Der Catch der Fehlermerkung fängt den Error nicht.");

			final ApiOperationException zweiter = assertThrows(ApiOperationException.class, repository::kursklausuren,
					"Der nächste Getter meldet den Abbruch, statt halbe Objekte auszugeben oder erneut aufzubauen.");
			assertEquals(Status.INTERNAL_SERVER_ERROR, zweiter.getStatus());
			final String meldung = String.valueOf(zweiter.getBody());
			assertTrue(meldung.contains("ist abgebrochen."), "Die Meldung stammt aus dem Kennzeichen des begonnenen Aufbaus.");
			assertFalse(meldung.contains("konnten nicht aufgebaut werden."), "Die Meldung stammt nicht aus dem leeren Fehlerfeld.");
		}
	}

	@Test
	void testEinZweiterZugriffHaengtDieSchuelerklausurenNichtErneutAn() throws ApiOperationException {
		// Der Aufbau hängt die Schülerklausuren an die Liste des geteilten ReportingSchueler an, statt sie zu ersetzen. Ein zweiter Lauf zeigte jede
		// Schülerklausur doppelt.
		when(allDataService.getAllData(any())).thenReturn(klausurdatenMitEinerKlausur());

		try (MockedStatic<GostKlausurenServiceFactoryBuilder> serviceFactoryBuilder = serviceFactoryMitAllDataService()) {
			final ReportingRepositoryGostKlausurplanung repository = new ReportingRepositoryGostKlausurplanung(reportingContext);

			assertEquals(List.of(ID_SCHUELERKLAUSURTERMIN), repository.schuelerklausuren().stream().map(s -> s.idSchuelerklausurtermin()).toList());
			assertEquals(1, schuelerklausurenAmSchueler.size(), "Der Aufbau hängt die Schülerklausur an das Schüler-Objekt.");

			repository.klausurtermine();
			assertEquals(1, schuelerklausurenAmSchueler.size(), "Der zweite Zugriff hängt keine weitere Schülerklausur an das geteilte Schüler-Objekt.");
			verify(allDataService, times(1)).getAllData(any());
		}
	}

	@Test
	void testEinAbbruchNachDemErstenAnhaengenVervielfachtDieSchuelerklausurenNicht() throws ApiOperationException {
		// Der zweite Schüler lässt das Verteilen der Schülerklausuren scheitern, nachdem der erste Schüler seine Schülerklausur erhalten hat.
		when(allDataService.getAllData(any())).thenReturn(klausurdatenMitZweiKlausuren());
		final ReportingSchueler schuelerMitFehler = mock(ReportingSchueler.class);
		when(schuelerMitFehler.id()).thenReturn(ID_SCHUELER_MIT_FEHLER);
		when(schuelerMitFehler.gostKlausurplanungSchuelerklausuren()).thenThrow(new DeveloperNotificationException("Fehlerinjektion"));
		when(repositorySchueler.schueler(ID_SCHUELER_MIT_FEHLER)).thenReturn(schuelerMitFehler);

		try (MockedStatic<GostKlausurenServiceFactoryBuilder> serviceFactoryBuilder = serviceFactoryMitAllDataService()) {
			final ReportingRepositoryGostKlausurplanung repository = new ReportingRepositoryGostKlausurplanung(reportingContext);

			final ApiOperationException erster = assertThrows(ApiOperationException.class, repository::schuelerklausuren);
			assertEquals(1, schuelerklausurenAmSchueler.size(), "Der Abbruch fällt hinter das Anhängen der ersten Schülerklausur.");

			final ApiOperationException zweiter = assertThrows(ApiOperationException.class, repository::klausurtermine);
			assertSame(erster, zweiter, "Der gemerkte Fehler wird unverändert erneut gemeldet.");
			assertEquals(1, schuelerklausurenAmSchueler.size(), "Der zweite Zugriff hängt keine weitere Schülerklausur an das geteilte Schüler-Objekt.");
			verify(allDataService, times(1)).getAllData(any());
		}
	}

}
