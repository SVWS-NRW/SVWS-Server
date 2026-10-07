package de.svws_nrw.controller.lehrer.lehramt;

import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.service.lehrer.lehramt.LehrerLehramtCreateRequest;
import de.svws_nrw.service.lehrer.lehramt.LehrerLehramtPatchRequest;
import de.svws_nrw.service.lehrer.lehramt.LehrerLehramtService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LehrerLehramtControllerImplTest {

	@Mock
	private LehrerLehramtService service;

	@Mock
	private MockedStatic<BeanValidator> beanValidator;

	@InjectMocks
	private LehrerLehramtControllerImpl controller;

	private LehrerLehramtEintrag apiModel;

	@BeforeEach
	void setUp() {
		apiModel = new LehrerLehramtEintrag();
		apiModel.id = 1L;
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("create")
	void create() {
		final var dto = new LehrerLehramtCreateRequest();

		when(service.create(dto)).thenReturn(apiModel);

		try (var response = controller.create(dto)) {
			beanValidator.verify(() -> BeanValidator.validate(dto));
			assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
			assertThat(response.getEntity()).isEqualTo(apiModel);
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch")
	void patch() {
		final var dto = new LehrerLehramtPatchRequest();

		when(service.patch(1L, dto)).thenReturn(apiModel);

		try (var response = controller.patch(1L, dto)) {
			beanValidator.verify(() -> BeanValidator.validate(dto));
			assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
			assertThat(response.getEntity()).isEqualTo(apiModel);
		}
	}

	// -------------------------------------------------------------------------
	// delete
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("delete")
	void delete() {
		final var operationResponse = SimpleOperationResponse.ofSuccess(1L);

		when(service.delete(List.of(1L))).thenReturn(List.of(operationResponse));

		try (var response = controller.delete(List.of(1L))) {
			assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
			assertThat(response.getEntity()).isEqualTo(List.of(operationResponse));
		}
	}
}
