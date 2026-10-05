package de.svws_nrw.controller.schulwechsel.abgang;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.SchulwechselAbgang;
import de.svws_nrw.service.schulwechsel.abgang.SchulwechselAbgangCreateRequest;
import de.svws_nrw.service.schulwechsel.abgang.SchulwechselAbgangPatchRequest;
import de.svws_nrw.service.schulwechsel.abgang.SchulwechselAbgangService;
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
class SchulwechselAbgangControllerTest {

	@Mock
	private SchulwechselAbgangService service;

	private SchulwechselAbgangControllerImpl controller;

	private MockedStatic<BeanValidator> beanValidatorMock;

	@BeforeEach
	void setUp() {
		controller = new SchulwechselAbgangControllerImpl(service);
		beanValidatorMock = mockStatic(BeanValidator.class);
	}

	@AfterEach
	void tearDown() {
		beanValidatorMock.close();
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll_success() {
		final var dokumente = List.of(mock(SchulwechselAbgang.class), mock(SchulwechselAbgang.class));

		when(service.getAll()).thenReturn(dokumente);

		assertThat(controller.getAll())
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.hasSize(2);

		verify(service, times(1)).getAll();
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getById_success() {
		final var id = 1L;
		final var dokument = mock(SchulwechselAbgang.class);

		when(service.getById(id)).thenReturn(dokument);

		assertThat(controller.getById(id))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isInstanceOf(SchulwechselAbgang.class);

		verify(service, times(1)).getById(id);
	}

	@Test
	@DisplayName("create | Erfolg")
	void create_success() {
		final var dto = mock(SchulwechselAbgangCreateRequest.class);
		final var created = mock(SchulwechselAbgang.class);

		beanValidatorMock.when(() -> BeanValidator.validate(dto)).thenAnswer(invocation -> null);
		when(service.create(dto)).thenReturn(created);

		assertThat(controller.create(dto))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.CREATED.getStatusCode())
				.extracting(Response::getEntity)
				.isInstanceOf(SchulwechselAbgang.class);

		beanValidatorMock.verify(() -> BeanValidator.validate(dto), times(1));
		verify(service, times(1)).create(dto);
	}

	@Test
	@DisplayName("create | Validierungsfehler")
	void create_validationError() {
		final var dto = mock(SchulwechselAbgangCreateRequest.class);

		beanValidatorMock.when(() -> BeanValidator.validate(dto))
				.thenThrow(new ValidationException("Validation failed"));

		assertThatThrownBy(() -> controller.create(dto))
				.isInstanceOf(ValidationException.class)
				.hasMessage("Validation failed");

		verify(service, never()).create(any());
	}

	@Test
	@DisplayName("patch | Erfolg")
	void patch_success() {
		final var id = 1L;
		final var dto = mock(SchulwechselAbgangPatchRequest.class);
		final var patched = mock(SchulwechselAbgang.class);

		beanValidatorMock.when(() -> BeanValidator.validate(dto)).thenAnswer(invocation -> null);
		when(service.patch(id, dto)).thenReturn(patched);

		assertThat(controller.patch(id, dto))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isInstanceOf(SchulwechselAbgang.class);

		beanValidatorMock.verify(() -> BeanValidator.validate(dto), times(1));
		verify(service, times(1)).patch(id, dto);
	}

	@Test
	@DisplayName("patch | Validierungsfehler")
	void patch_validationError() {
		final var id = 1L;
		final var dto = mock(SchulwechselAbgangPatchRequest.class);

		beanValidatorMock.when(() -> BeanValidator.validate(dto))
				.thenThrow(new ValidationException("Invalid patch data"));

		assertThatThrownBy(() -> controller.patch(id, dto))
				.isInstanceOf(ValidationException.class)
				.hasMessage("Invalid patch data");

		verify(service, never()).patch(anyLong(), any());
	}

	@Test
	@DisplayName("delete | Erfolg mit mehreren IDs")
	void delete_success() {
		final var ids = List.of(1L, 2L, 3L);
		final var responses = List.of(mock(SimpleOperationResponse.class), mock(SimpleOperationResponse.class), mock(SimpleOperationResponse.class));

		when(service.delete(ids)).thenReturn(responses);

		assertThat(controller.delete(ids))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.hasSize(3);

		verify(service, times(1)).delete(ids);
	}

}
