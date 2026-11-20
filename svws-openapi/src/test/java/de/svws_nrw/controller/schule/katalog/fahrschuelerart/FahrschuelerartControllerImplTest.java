package de.svws_nrw.controller.schule.katalog.fahrschuelerart;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Fahrschuelerart;
import de.svws_nrw.service.schule.katalog.fahrschuelerart.FahrschuelerartCreateRequest;
import de.svws_nrw.service.schule.katalog.fahrschuelerart.FahrschuelerartPatchRequest;
import de.svws_nrw.service.schule.katalog.fahrschuelerart.FahrschuelerartService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FahrschuelerartControllerImplTest {

	@Mock
	private FahrschuelerartService service;

	private FahrschuelerartControllerImpl controller;

	private MockedStatic<BeanValidator> beanValidatorMock;

	@BeforeEach
	void setUp() {
		controller = new FahrschuelerartControllerImpl(service);
		beanValidatorMock = mockStatic(BeanValidator.class);
	}

	@AfterEach
	void tearDown() {
		beanValidatorMock.close();
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		when(service.getAll()).thenReturn(List.of(mock(Fahrschuelerart.class)));

		assertThat(controller.getAll())
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.isNotNull()
				.hasSize(1)
				.first()
				.satisfies(eintrag -> assertThat(eintrag).isInstanceOf(Fahrschuelerart.class));
	}

	@Test
	@DisplayName("create | Erfolg")
	void create_success() {
		final var request = mock(FahrschuelerartCreateRequest.class);
		final var created = mock(Fahrschuelerart.class);

		beanValidatorMock.when(() -> BeanValidator.validate(request)).thenAnswer(invocation -> null);
		when(service.create(request)).thenReturn(created);

		assertThat(controller.create(request))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.CREATED.getStatusCode())
				.extracting(Response::getEntity)
				.isNotNull()
				.satisfies(entity -> assertThat(entity).isInstanceOf(Fahrschuelerart.class));

		beanValidatorMock.verify(() -> BeanValidator.validate(request), times(1));
		verify(service, times(1)).create(request);
	}

	@Test
	@DisplayName("create | Validierungsfehler")
	void create_validationError() {
		final var request = mock(FahrschuelerartCreateRequest.class);

		beanValidatorMock.when(() -> BeanValidator.validate(request))
				.thenThrow(new ValidationException("Validation failed"));

		assertThatThrownBy(() -> controller.create(request))
				.isInstanceOf(ValidationException.class)
				.hasMessage("Validation failed");

		verify(service, never()).create(any());
	}

	@Test
	@DisplayName("patch | Erfolg")
	void patch_success() {
		final var id = 1L;
		final var request = mock(FahrschuelerartPatchRequest.class);
		final var patched = mock(Fahrschuelerart.class);

		beanValidatorMock.when(() -> BeanValidator.validate(request)).thenAnswer(invocation -> null);
		when(service.patch(id, request)).thenReturn(patched);

		assertThat(controller.patch(id, request))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isNotNull()
				.satisfies(entity -> assertThat(entity).isInstanceOf(Fahrschuelerart.class));

		beanValidatorMock.verify(() -> BeanValidator.validate(request), times(1));
		verify(service, times(1)).patch(id, request);
	}

	@Test
	@DisplayName("patch | Validierungsfehler")
	void patch_validationError() {
		final var request = mock(FahrschuelerartPatchRequest.class);

		beanValidatorMock.when(() -> BeanValidator.validate(request))
				.thenThrow(new ValidationException("Invalid patch data"));

		assertThatThrownBy(() -> controller.patch(1L, request))
				.isInstanceOf(ValidationException.class)
				.hasMessage("Invalid patch data");

		verify(service, never()).patch(anyLong(), any());
	}

	@Test
	@DisplayName("deleteMultiple | Erfolg mit mehreren IDs")
	void deleteMultiple_success() {
		final var ids = List.of(1L, 2L, 3L);
		final var responses = List.of(mock(SimpleOperationResponse.class));

		when(service.deleteMultiple(ids)).thenReturn(responses);

		assertThat(controller.deleteMultiple(ids))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.isNotNull()
				.hasSize(1);

		verify(service, times(1)).deleteMultiple(ids);
	}
}
