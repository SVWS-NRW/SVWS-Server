package de.svws_nrw.controller.schueler.create;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schueler.create.SchuelerCreateRequest;
import de.svws_nrw.service.schueler.create.SchuelerCreateService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;

/**
 * Tests für den {@link SchuelerCreateControllerImpl}.
 */
@ExtendWith(MockitoExtension.class)
class SchuelerCreateControllerImplTest {

	@Mock
	private SchuelerCreateService service;

	private SchuelerCreateControllerImpl controller;

	private MockedStatic<BeanValidator> beanValidatorMock;

	@BeforeEach
	void setUp() {
		controller = new SchuelerCreateControllerImpl(service);
		beanValidatorMock = mockStatic(BeanValidator.class);
	}

	@AfterEach
	void tearDown() {
		beanValidatorMock.close();
	}

	@Test
	@DisplayName("create | Erfolg: validiert, delegiert an den Service und liefert 201")
	void create_success() {
		final var dto = new SchuelerCreateRequest();
		final var created = new SchuelerStammdaten();
		created.id = 100L;
		when(service.create(dto)).thenReturn(created);

		final var response = controller.create(dto);

		assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
		assertThat(response.getEntity()).isEqualTo(created);
		beanValidatorMock.verify(() -> BeanValidator.validate(dto), times(1));
		verify(service, times(1)).create(dto);
	}

	@Test
	@DisplayName("create | Validierungsfehler: der Service wird nicht aufgerufen")
	void create_validationError() {
		final var dto = new SchuelerCreateRequest();
		beanValidatorMock.when(() -> BeanValidator.validate(dto)).thenThrow(new ValidationException("Validation failed"));

		assertThatThrownBy(() -> controller.create(dto))
				.isInstanceOf(ValidationException.class)
				.hasMessage("Validation failed");

		verify(service, never()).create(any());
	}

	@Test
	@DisplayName("create | Fehler im Service wird weitergereicht")
	void create_serviceError() {
		final var dto = new SchuelerCreateRequest();
		when(service.create(dto)).thenThrow(new ApiOperationException(Response.Status.BAD_REQUEST, "Kein Status zur ID 9999 gefunden"));

		assertThatThrownBy(() -> controller.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
				.hasMessage("Kein Status zur ID 9999 gefunden");
	}

}
