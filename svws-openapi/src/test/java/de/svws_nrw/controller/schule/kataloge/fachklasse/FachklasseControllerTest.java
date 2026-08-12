package de.svws_nrw.controller.schule.kataloge.fachklasse;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.FachklasseEintrag;
import de.svws_nrw.service.schule.katalog.fachklasse.FachklasseEintragCreateRequest;
import de.svws_nrw.service.schule.katalog.fachklasse.FachklasseEintragPatchRequest;
import de.svws_nrw.service.schule.katalog.fachklasse.FachklasseService;
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
class FachklasseControllerTest {

	@Mock
	private FachklasseService fachklasseService;

	private FachklasseController fachklasseController;

	private MockedStatic<BeanValidator> beanValidatorMock;

	@BeforeEach
	void setUp() {
		fachklasseController = new FachklasseController(fachklasseService);
		beanValidatorMock = mockStatic(BeanValidator.class);
	}

	@AfterEach
	void tearDown() {
		beanValidatorMock.close();
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		when(fachklasseService.getAll()).thenReturn(List.of(mock(FachklasseEintrag.class)));

		assertThat(fachklasseController.getAll())
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.isNotNull()
				.hasSize(1);
	}

	@Test
	@DisplayName("create | Erfolg")
	void create_success() {
		final var dto = mock(FachklasseEintragCreateRequest.class);
		final var created = new FachklasseEintrag();

		beanValidatorMock.when(() -> BeanValidator.validate(dto)).thenAnswer(invocation -> null);
		when(fachklasseService.create(dto)).thenReturn(created);

		assertThat(fachklasseController.create(dto))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.CREATED.getStatusCode())
				.extracting(Response::getEntity)
				.isNotNull();

		beanValidatorMock.verify(() -> BeanValidator.validate(dto), times(1));
		verify(fachklasseService, times(1)).create(dto);
	}

	@Test
	@DisplayName("create | Validierungsfehler")
	void create_validationError() {
		final var dto = mock(FachklasseEintragCreateRequest.class);

		beanValidatorMock.when(() -> BeanValidator.validate(dto))
				.thenThrow(new ValidationException("Validation failed"));

		assertThatThrownBy(() -> fachklasseController.create(dto))
				.isInstanceOf(ValidationException.class)
				.hasMessage("Validation failed");

		verify(fachklasseService, never()).create(any());
	}

	@Test
	@DisplayName("delete | Erfolg mit mehreren IDs")
	void delete_success() {
		final var ids = List.of(1L, 2L, 3L);
		final var deleteResponses = List.of(mock(SimpleOperationResponse.class));

		when(fachklasseService.delete(ids)).thenReturn(deleteResponses);

		assertThat(fachklasseController.delete(ids))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.isNotNull()
				.hasSize(1);

		verify(fachklasseService, times(1)).delete(ids);
	}

	@Test
	@DisplayName("patch | Erfolg")
	void patch_success() {
		final var id = 1L;
		final var dto = mock(FachklasseEintragPatchRequest.class);
		final var patched = mock(FachklasseEintrag.class);

		beanValidatorMock.when(() -> BeanValidator.validate(dto)).thenAnswer(invocation -> null);
		when(fachklasseService.patch(id, dto)).thenReturn(patched);

		assertThat(fachklasseController.patch(id, dto))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isNotNull();

		beanValidatorMock.verify(() -> BeanValidator.validate(dto), times(1));
		verify(fachklasseService, times(1)).patch(id, dto);
	}

	@Test
	@DisplayName("patch | Validierungsfehler")
	void patch_validationError() {
		final var id = 1L;
		final var dto = mock(FachklasseEintragPatchRequest.class);

		beanValidatorMock.when(() -> BeanValidator.validate(dto))
				.thenThrow(new ValidationException("Invalid patch data"));

		assertThatThrownBy(() -> fachklasseController.patch(id, dto))
				.isInstanceOf(ValidationException.class)
				.hasMessage("Invalid patch data");

		verify(fachklasseService, never()).patch(anyLong(), any());
	}
}
