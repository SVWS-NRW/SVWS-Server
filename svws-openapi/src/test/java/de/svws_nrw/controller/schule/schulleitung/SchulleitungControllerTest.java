package de.svws_nrw.controller.schule.schulleitung;

import java.util.List;

import de.svws_nrw.asd.data.schule.Schulleitung;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.service.schule.schulleitung.SchulleitungCreateRequest;
import de.svws_nrw.service.schule.schulleitung.SchulleitungPatchRequest;
import de.svws_nrw.service.schule.schulleitung.SchulleitungService;
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
class SchulleitungControllerTest {

	@Mock
	private SchulleitungService service;

	private SchulleitungController controller;

	private MockedStatic<BeanValidator> beanValidatorMock;

	@BeforeEach
	void setUp() {
		controller = new SchulleitungController(service);
		beanValidatorMock = mockStatic(BeanValidator.class);
	}

	@AfterEach
	void tearDown() {
		beanValidatorMock.close();
	}

	@Test
	@DisplayName("getAll | Erfolg mit mehreren Einträgen")
	void getAll_success() {
		final var schulleitung1 = mock(Schulleitung.class);
		final var schulleitung2 = mock(Schulleitung.class);
		final var schulleitungen = List.of(schulleitung1, schulleitung2);

		when(service.getAll()).thenReturn(schulleitungen);

		assertThat(controller.getAll())
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.hasSize(2)
				.containsExactly(schulleitung1, schulleitung2);

		verify(service, times(1)).getAll();
	}

	@Test
	@DisplayName("getAll | Erfolg mit leerer Liste")
	void getAll_emptyList() {
		when(service.getAll()).thenReturn(List.of());

		assertThat(controller.getAll())
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.isEmpty();

		verify(service, times(1)).getAll();
	}

	@Test
	@DisplayName("getAllByIdLehrer | Erfolg mit mehreren Einträgen")
	void getAllByIdLehrer_success() {
		final var idLehrer = 42L;
		final var schulleitung1 = mock(Schulleitung.class);
		final var schulleitung2 = mock(Schulleitung.class);
		final var schulleitungen = List.of(schulleitung1, schulleitung2);

		when(service.getAllByIdLehrer(idLehrer)).thenReturn(schulleitungen);

		assertThat(controller.getAllByIdLehrer(idLehrer))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.hasSize(2)
				.containsExactly(schulleitung1, schulleitung2);

		verify(service, times(1)).getAllByIdLehrer(idLehrer);
	}

	@Test
	@DisplayName("getAllByIdLehrer | Erfolg mit leerer Liste")
	void getAllByIdLehrer_emptyList() {
		final var idLehrer = 99L;

		when(service.getAllByIdLehrer(idLehrer)).thenReturn(List.of());

		assertThat(controller.getAllByIdLehrer(idLehrer))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.isEmpty();

		verify(service, times(1)).getAllByIdLehrer(idLehrer);
	}

	@Test
	@DisplayName("create | Erfolg")
	void create_success() {
		final var dto = mock(SchulleitungCreateRequest.class);
		final var created = mock(Schulleitung.class);

		beanValidatorMock.when(() -> BeanValidator.validate(dto)).thenAnswer(invocation -> null);
		when(service.create(dto)).thenReturn(created);

		assertThat(controller.create(dto))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.CREATED.getStatusCode())
				.extracting(Response::getEntity)
				.isNotNull()
				.satisfies(entity -> assertThat(entity).isInstanceOf(Schulleitung.class));

		beanValidatorMock.verify(() -> BeanValidator.validate(dto), times(1));
		verify(service, times(1)).create(dto);
	}

	@Test
	@DisplayName("create | Validierungsfehler")
	void create_validationError() {
		final var dto = mock(SchulleitungCreateRequest.class);

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
		final var dto = mock(SchulleitungPatchRequest.class);
		final var patched = mock(Schulleitung.class);

		beanValidatorMock.when(() -> BeanValidator.validate(dto)).thenAnswer(invocation -> null);
		when(service.patch(id, dto)).thenReturn(patched);

		assertThat(controller.patch(id, dto))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isNotNull()
				.satisfies(entity -> assertThat(entity).isInstanceOf(Schulleitung.class));

		beanValidatorMock.verify(() -> BeanValidator.validate(dto), times(1));
		verify(service, times(1)).patch(id, dto);
	}

	@Test
	@DisplayName("patch | Validierungsfehler")
	void patch_validationError() {
		final var id = 1L;
		final var dto = mock(SchulleitungPatchRequest.class);

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
		final var deleteResponses = List.of(mock(SimpleOperationResponse.class));

		when(service.delete(ids)).thenReturn(deleteResponses);

		assertThat(controller.delete(ids))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.isNotNull()
				.hasSize(1);

		verify(service, times(1)).delete(ids);
	}
}
