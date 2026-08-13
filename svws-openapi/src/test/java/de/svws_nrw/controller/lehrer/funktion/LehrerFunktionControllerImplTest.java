package de.svws_nrw.controller.lehrer.funktion;

import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerFunktion;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionBatchPatchRequest;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionCreateRequest;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LehrerFunktionControllerImplTest {

	@Mock
	private LehrerFunktionService service;

	@Mock
	private MockedStatic<BeanValidator> beanValidator;

	@InjectMocks
	private LehrerFunktionControllerImpl controller;

	private LehrerFunktion apiModel;

	@BeforeEach
	void setUp() {
		apiModel = new LehrerFunktion();
		apiModel.id = 1L;
		apiModel.idAbschnittsdaten = 10L;
		apiModel.idFunktion = 20L;
	}

	// -------------------------------------------------------------------------
	// get
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("get")
	void get() {
		when(service.get(1L)).thenReturn(apiModel);

		final var response = controller.get(1L);

		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getEntity()).isEqualTo(apiModel);
	}

	// -------------------------------------------------------------------------
	// getAll
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getAll")
	void getAll() {
		when(service.getAll()).thenReturn(List.of(apiModel));

		try (var response = controller.getAll()) {
			assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
			assertThat(response.getEntity()).isEqualTo(List.of(apiModel));
		}
	}

	// -------------------------------------------------------------------------
	// getListByIdAbschnitt
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getListByIdAbschnitt")
	void getListByIdAbschnitt() {
		when(service.getListByIdAbschnitt(10L)).thenReturn(List.of(apiModel));

		final var response = controller.getListByIdAbschnitt(10L);

		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getEntity()).isEqualTo(List.of(apiModel));
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("create")
	void create() {
		final var dto = new LehrerFunktionCreateRequest();
		dto.idAbschnittsdaten = 10L;
		dto.idFunktion = 20L;

		when(service.create(dto)).thenReturn(apiModel);

		try (var response = controller.create(dto)) {
			beanValidator.verify(() -> BeanValidator.validate(dto));
			assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
			assertThat(response.getEntity()).isEqualTo(apiModel);
		}
	}

	// -------------------------------------------------------------------------
	// createMultiple
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("createMultiple")
	void createMultiple() {
		final var dto = new LehrerFunktionCreateRequest();
		dto.idAbschnittsdaten = 10L;
		dto.idFunktion = 20L;

		when(service.createMultiple(List.of(dto))).thenReturn(List.of(apiModel));

		try (var response = controller.createMultiple(List.of(dto))) {
			beanValidator.verify(() -> BeanValidator.validate(dto));
			assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
			assertThat(response.getEntity()).isEqualTo(List.of(apiModel));
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch")
	void patch() {
		final var dto = new LehrerFunktionBatchPatchRequest();
		dto.id = 1L;
		dto.idFunktion = JsonNullable.of(99L);

		when(service.patch(1L, dto)).thenReturn(apiModel);

		try (var response = controller.patch(1L, dto)) {
			beanValidator.verify(() -> BeanValidator.validate(dto));
			assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
			assertThat(response.getEntity()).isEqualTo(apiModel);
		}
	}

	// -------------------------------------------------------------------------
	// patchMultiple
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patchMultiple")
	void patchMultiple() {
		final var dto = new LehrerFunktionBatchPatchRequest();
		dto.id = 1L;
		dto.idFunktion = JsonNullable.of(99L);

		when(service.patchMultiple(List.of(dto))).thenReturn(List.of(apiModel));

		try (var response = controller.patchMultiple(List.of(dto))) {
			beanValidator.verify(() -> BeanValidator.validate(dto));
			assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
			assertThat(response.getEntity()).isEqualTo(List.of(apiModel));
		}
	}

	// -------------------------------------------------------------------------
	// delete
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("delete")
	void delete() {
		final var operationResponse = SimpleOperationResponse.ofSuccess(1L);
		when(service.delete(1L)).thenReturn(operationResponse);

		try (var response = controller.delete(1L)) {
			assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
			assertThat(response.getEntity()).isEqualTo(operationResponse);
		}
	}

	// -------------------------------------------------------------------------
	// deleteMultiple
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("deleteMultiple")
	void deleteMultiple() {
		final var operationResponse = SimpleOperationResponse.ofSuccess(1L);
		when(service.deleteMultiple(List.of(1L))).thenReturn(List.of(operationResponse));

		try (var response = controller.deleteMultiple(List.of(1L))) {
			assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
			assertThat(response.getEntity()).isEqualTo(List.of(operationResponse));
		}
	}
}
