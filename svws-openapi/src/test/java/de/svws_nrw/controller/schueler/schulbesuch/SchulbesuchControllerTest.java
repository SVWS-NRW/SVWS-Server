package de.svws_nrw.controller.schueler.schulbesuch;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerSchulbesuchPatchRequest;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerSchulbesuchService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchulbesuchControllerTest {

	@Mock
	private SchuelerSchulbesuchService service;

	@InjectMocks
	private SchulbesuchController controller;

	@Mock
	private MockedStatic<BeanValidator> beanValidatorMock;

	@AfterEach
	void tearDown() {
		beanValidatorMock.close();
	}

	// -------------------------------------------------------------------------
	// getByIdSchueler
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getByIdSchueler | Erfolg")
	void getByIdSchueler_success() {
		final var idSchueler = 1L;
		final var result = mock(SchuelerSchulbesuchsdaten.class);

		when(service.getById(idSchueler)).thenReturn(result);

		assertThat(controller.getByIdSchueler(idSchueler))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isNotNull()
				.isInstanceOf(SchuelerSchulbesuchsdaten.class);

		verify(service, times(1)).getById(idSchueler);
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch | Erfolg")
	void patch_success() {
		final var id = 1L;
		final var dto = mock(SchuelerSchulbesuchPatchRequest.class);
		final var patched = mock(SchuelerSchulbesuchsdaten.class);

		beanValidatorMock.when(() -> BeanValidator.validate(dto)).thenAnswer(invocation -> null);
		when(service.patch(id, dto)).thenReturn(patched);

		assertThat(controller.patch(id, dto))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isNotNull()
				.isInstanceOf(SchuelerSchulbesuchsdaten.class);

		beanValidatorMock.verify(() -> BeanValidator.validate(dto), times(1));
		verify(service, times(1)).patch(id, dto);
	}

	@Test
	@DisplayName("patch | Validierungsfehler")
	void patch_validationError() {
		final var id = 1L;
		final var dto = mock(SchuelerSchulbesuchPatchRequest.class);

		beanValidatorMock.when(() -> BeanValidator.validate(dto))
				.thenThrow(new ValidationException("Validation failed"));

		assertThatThrownBy(() -> controller.patch(id, dto))
				.isInstanceOf(ValidationException.class)
				.hasMessage("Validation failed");

		verify(service, never()).patch(anyLong(), any());
	}

}
