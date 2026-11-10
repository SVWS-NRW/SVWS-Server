package de.svws_nrw.controller.gost;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.core.data.gost.GostZulassungSimulation;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.benutzer.BenutzerKompetenzService;
import de.svws_nrw.service.gost.GostAbiturdatenService;
import de.svws_nrw.service.gost.GostFachwahlService;
import de.svws_nrw.service.gost.GostJahrgangFachwahlService;
import de.svws_nrw.service.gost.GostLaufbahnplanungExportV2Service;
import de.svws_nrw.service.gost.GostLaufbahnplanungImportV1Service;
import de.svws_nrw.service.gost.GostLaufbahnplanungImportV2Service;
import de.svws_nrw.service.gost.GostSchuelerGKLWahlService;
import de.svws_nrw.service.gost.GostSchuelerService;
import de.svws_nrw.service.gost.GostZulassungSimulationService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft, dass Lesen und Speichern der Simulation der Abiturzulassung die Kompetenz für die Jahrgänge des Schülers voraussetzen.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests für die Kompetenzprüfung bei der Simulation der Abiturzulassung")
class GostLaufbahnplanungControllerImplZulassungSimulationTest {

	private static final long ID_SCHUELER = 1L;
	private static final List<Integer> JAHRGAENGE = List.of(2028, 2029);

	@Mock
	private BenutzerKompetenzService benutzerKompetenzService;

	@Mock
	private GostAbiturdatenService gostAbiturdatenService;

	@Mock
	private GostFachwahlService gostFachwahlService;

	@Mock
	private GostSchuelerService gostSchuelerService;

	@Mock
	private GostSchuelerGKLWahlService gostSchuelerGKLWahlService;

	@Mock
	private GostZulassungSimulationService gostZulassungSimulationService;

	@Mock
	private GostJahrgangFachwahlService gostJahrgangFachwahlService;

	@Mock
	private GostLaufbahnplanungImportV1Service gostLaufbahnplanungImportV1Service;

	@Mock
	private GostLaufbahnplanungExportV2Service gostLaufbahnplanungExportV2Service;

	@Mock
	private GostLaufbahnplanungImportV2Service gostLaufbahnplanungImportV2Service;

	private GostLaufbahnplanungControllerImpl controller;

	@BeforeEach
	void setUp() {
		controller = new GostLaufbahnplanungControllerImpl(benutzerKompetenzService, gostAbiturdatenService, gostFachwahlService, gostSchuelerService,
				gostSchuelerGKLWahlService, gostZulassungSimulationService, gostJahrgangFachwahlService, gostLaufbahnplanungImportV1Service,
				gostLaufbahnplanungExportV2Service, gostLaufbahnplanungImportV2Service);
		when(gostSchuelerService.getAbiturjahrgaenge(ID_SCHUELER)).thenReturn(JAHRGAENGE);
	}

	private void verbieteZugriff() {
		doThrow(new ApiOperationException(Status.FORBIDDEN, "kein Zugriff")).when(benutzerKompetenzService).pruefeKompetenzLaufbahnplanung(JAHRGAENGE);
	}

	@Test
	@DisplayName("Lesen: Mit Kompetenz wird der Stand geliefert")
	void testGet() {
		final GostZulassungSimulation simulation = new GostZulassungSimulation();
		when(gostZulassungSimulationService.get(ID_SCHUELER)).thenReturn(simulation);

		final Response response = controller.getZulassungSimulation(ID_SCHUELER);

		assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
		assertThat(response.getEntity()).isSameAs(simulation);
		verify(benutzerKompetenzService).pruefeKompetenzLaufbahnplanung(JAHRGAENGE);
	}

	@Test
	@DisplayName("Lesen: Ohne Kompetenz für die Jahrgänge des Schülers führt der Zugriff zu 403")
	void testGetVerboten() {
		verbieteZugriff();

		assertThatThrownBy(() -> controller.getZulassungSimulation(ID_SCHUELER)).isInstanceOfSatisfying(ApiOperationException.class,
				e -> assertThat(e.getStatus()).isEqualTo(Status.FORBIDDEN));
		verify(gostZulassungSimulationService, never()).get(anyLong());
	}

	@Test
	@DisplayName("Speichern: Mit Kompetenz wird der Stand geschrieben")
	void testPut() {
		final GostZulassungSimulation simulation = new GostZulassungSimulation();

		final Response response = controller.putZulassungSimulation(ID_SCHUELER, simulation);

		assertThat(response.getStatus()).isEqualTo(Status.NO_CONTENT.getStatusCode());
		verify(gostZulassungSimulationService).put(ID_SCHUELER, simulation);
		verify(benutzerKompetenzService).pruefeKompetenzLaufbahnplanung(JAHRGAENGE);
	}

	@Test
	@DisplayName("Speichern: Ohne Kompetenz für die Jahrgänge des Schülers führt der Zugriff zu 403 und es wird nichts geschrieben")
	void testPutVerboten() {
		verbieteZugriff();

		assertThatThrownBy(() -> controller.putZulassungSimulation(ID_SCHUELER, new GostZulassungSimulation())).isInstanceOfSatisfying(ApiOperationException.class,
				e -> assertThat(e.getStatus()).isEqualTo(Status.FORBIDDEN));
		verify(gostZulassungSimulationService, never()).put(anyLong(), any());
	}

}
