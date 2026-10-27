package de.svws_nrw.controller.schule.katalog.lernplattform;

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
import de.svws_nrw.core.data.schule.Lernplattform;
import de.svws_nrw.service.schule.katalog.lernplattform.LernplattformCreateRequest;
import de.svws_nrw.service.schule.katalog.lernplattform.LernplattformPatchRequest;
import de.svws_nrw.service.schule.katalog.lernplattform.LernplattformService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;

/**
 * Tests für den {@link LernplattformControllerImpl}.
 */
@ExtendWith(MockitoExtension.class)
class LernplattformControllerImplTest {

	@Mock
	private LernplattformService lernplattformService;

	private LernplattformControllerImpl controller;

	private MockedStatic<BeanValidator> beanValidatorMock;

	@BeforeEach
	void setUp() {
		controller = new LernplattformControllerImpl(lernplattformService);
		beanValidatorMock = mockStatic(BeanValidator.class);
	}

	@AfterEach
	void tearDown() {
		beanValidatorMock.close();
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		final var lernplattform = mock(Lernplattform.class);
		when(lernplattformService.getAll()).thenReturn(List.of(lernplattform));

		assertThat(controller.getAll())
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.hasSize(1);
	}

	@Test
	@DisplayName("create | Erfolg")
	void create_success() {
		final var dto = mock(LernplattformCreateRequest.class);
		final var created = mock(Lernplattform.class);
		when(lernplattformService.create(dto)).thenReturn(created);

		assertThat(controller.create(dto))
				.hasFieldOrPropertyWithValue("status", Response.Status.CREATED.getStatusCode())
				.extracting(Response::getEntity)
				.isInstanceOf(Lernplattform.class);

		beanValidatorMock.verify(() -> BeanValidator.validate(dto), times(1));
	}

	@Test
	@DisplayName("create | Validierungsfehler")
	void create_validationError() {
		final var dto = mock(LernplattformCreateRequest.class);
		beanValidatorMock.when(() -> BeanValidator.validate(dto)).thenThrow(new ValidationException("Validation failed"));

		assertThatThrownBy(() -> controller.create(dto)).isInstanceOf(ValidationException.class).hasMessage("Validation failed");

		verify(lernplattformService, never()).create(any());
	}

	@Test
	@DisplayName("patch | Erfolg")
	void patch_success() {
		final var dto = mock(LernplattformPatchRequest.class);
		final var patched = mock(Lernplattform.class);
		when(lernplattformService.patch(1L, dto)).thenReturn(patched);

		assertThat(controller.patch(1L, dto))
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isInstanceOf(Lernplattform.class);

		beanValidatorMock.verify(() -> BeanValidator.validate(dto), times(1));
	}

	@Test
	@DisplayName("patch | Validierungsfehler")
	void patch_validationError() {
		final var dto = mock(LernplattformPatchRequest.class);
		beanValidatorMock.when(() -> BeanValidator.validate(dto)).thenThrow(new ValidationException("Invalid patch data"));

		assertThatThrownBy(() -> controller.patch(1L, dto)).isInstanceOf(ValidationException.class).hasMessage("Invalid patch data");

		verify(lernplattformService, never()).patch(anyLong(), any());
	}

	@Test
	@DisplayName("deleteMultiple | Erfolg")
	void deleteMultiple_success() {
		final var ids = List.of(1L, 2L);
		final var response = mock(SimpleOperationResponse.class);
		when(lernplattformService.deleteMultiple(ids)).thenReturn(List.of(response));

		assertThat(controller.deleteMultiple(ids))
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.hasSize(1);

		verify(lernplattformService, times(1)).deleteMultiple(ids);
	}

}
