package de.svws_nrw.controller.schule.katalog.einwilligungsart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Einwilligungsart;
import de.svws_nrw.service.schule.katalog.einwilligungsart.EinwilligungsartCreateRequest;
import de.svws_nrw.service.schule.katalog.einwilligungsart.EinwilligungsartPatchRequest;
import de.svws_nrw.service.schule.katalog.einwilligungsart.EinwilligungsartService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;

/**
 * Tests für den {@link EinwilligungsartControllerImpl}.
 */
@ExtendWith(MockitoExtension.class)
class EinwilligungsartControllerImplTest {

	@Mock
	private EinwilligungsartService einwilligungsartService;

	private EinwilligungsartControllerImpl controller;

	private MockedStatic<BeanValidator> beanValidatorMock;

	@BeforeEach
	void setUp() {
		controller = new EinwilligungsartControllerImpl(einwilligungsartService);
		beanValidatorMock = mockStatic(BeanValidator.class);
	}

	@AfterEach
	void tearDown() {
		beanValidatorMock.close();
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		final var einwilligungsart = mock(Einwilligungsart.class);
		when(einwilligungsartService.getAll()).thenReturn(List.of(einwilligungsart));

		assertThat(controller.getAll())
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.hasSize(1);
	}

	@Test
	@DisplayName("create | Erfolg")
	void create_success() {
		final var dto = mock(EinwilligungsartCreateRequest.class);
		final var created = mock(Einwilligungsart.class);
		when(einwilligungsartService.create(dto)).thenReturn(created);

		assertThat(controller.create(dto))
				.hasFieldOrPropertyWithValue("status", Response.Status.CREATED.getStatusCode())
				.extracting(Response::getEntity)
				.isInstanceOf(Einwilligungsart.class);

		beanValidatorMock.verify(() -> BeanValidator.validate(dto), times(1));
	}

	@Test
	@DisplayName("create | Validierungsfehler")
	void create_validationError() {
		final var dto = mock(EinwilligungsartCreateRequest.class);
		beanValidatorMock.when(() -> BeanValidator.validate(dto)).thenThrow(new ValidationException("Validation failed"));

		assertThatThrownBy(() -> controller.create(dto)).isInstanceOf(ValidationException.class).hasMessage("Validation failed");

		verify(einwilligungsartService, never()).create(any());
	}

	@Test
	@DisplayName("patch | Erfolg")
	void patch_success() {
		final var dto = mock(EinwilligungsartPatchRequest.class);
		final var patched = mock(Einwilligungsart.class);
		when(einwilligungsartService.patch(1L, dto)).thenReturn(patched);

		assertThat(controller.patch(1L, dto))
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isInstanceOf(Einwilligungsart.class);

		beanValidatorMock.verify(() -> BeanValidator.validate(dto), times(1));
	}

	@Test
	@DisplayName("patch | Validierungsfehler")
	void patch_validationError() {
		final var dto = mock(EinwilligungsartPatchRequest.class);
		beanValidatorMock.when(() -> BeanValidator.validate(dto)).thenThrow(new ValidationException("Invalid patch data"));

		assertThatThrownBy(() -> controller.patch(1L, dto)).isInstanceOf(ValidationException.class).hasMessage("Invalid patch data");

		verify(einwilligungsartService, never()).patch(anyLong(), any());
	}

	@Test
	@DisplayName("deleteMultiple | Erfolg")
	void deleteMultiple_success() {
		final var ids = List.of(1L, 2L);
		final var response = mock(SimpleOperationResponse.class);
		when(einwilligungsartService.deleteMultiple(ids)).thenReturn(List.of(response));

		assertThat(controller.deleteMultiple(ids))
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.hasSize(1);

		verify(einwilligungsartService, times(1)).deleteMultiple(ids);
	}

}
