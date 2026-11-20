package de.svws_nrw.controller.schueler.kaoa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schueler.SchuelerKAoADaten;
import de.svws_nrw.service.schueler.kaoa.SchuelerKAoACreateRequest;
import de.svws_nrw.service.schueler.kaoa.SchuelerKAoAPatchRequest;
import de.svws_nrw.service.schueler.kaoa.SchuelerKAoAService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;

/**
 * Tests für den {@link SchuelerKAoAControllerImpl}.
 */
@ExtendWith(MockitoExtension.class)
class SchuelerKAoAControllerImplTest {

	@Mock
	private SchuelerKAoAService service;

	private SchuelerKAoAControllerImpl controller;

	private MockedStatic<BeanValidator> beanValidatorMock;

	@BeforeEach
	void setUp() {
		controller = new SchuelerKAoAControllerImpl(service);
		beanValidatorMock = mockStatic(BeanValidator.class);
	}

	@AfterEach
	void tearDown() {
		beanValidatorMock.close();
	}

	@Test
	@DisplayName("getList | Liefert die KAoA-Daten mit Status 200")
	void getList_success() {
		final var daten = List.of(new SchuelerKAoADaten());
		when(service.getListByIdSchueler(1L)).thenReturn(daten);

		final var response = controller.getList(1L);

		assertThat(response)
				.returns(Response.Status.OK.getStatusCode(), Response::getStatus)
				.returns(daten, Response::getEntity);
	}

	@Test
	@DisplayName("create | Validiert den Request und liefert die angelegten KAoA-Daten mit Status 201")
	void create_success() {
		final var request = new SchuelerKAoACreateRequest();
		final var created = new SchuelerKAoADaten();
		when(service.create(1L, request)).thenReturn(created);

		final var response = controller.create(1L, request);

		assertThat(response)
				.returns(Response.Status.CREATED.getStatusCode(), Response::getStatus)
				.returns(created, Response::getEntity);
		beanValidatorMock.verify(() -> BeanValidator.validate(request), times(1));
	}

	@Test
	@DisplayName("create | Validierungsfehler verhindert den Aufruf des Services")
	void create_validationError() {
		final var request = new SchuelerKAoACreateRequest();
		beanValidatorMock.when(() -> BeanValidator.validate(request)).thenThrow(new ValidationException("Validation failed"));

		assertThatThrownBy(() -> controller.create(1L, request))
				.isInstanceOf(ValidationException.class)
				.hasMessage("Validation failed");
		verify(service, never()).create(anyLong(), any());
	}

	@Test
	@DisplayName("patch | Validiert den Patch und liefert die aktualisierten KAoA-Daten mit Status 200")
	void patch_success() {
		final var patch = new SchuelerKAoAPatchRequest();
		final var patched = new SchuelerKAoADaten();
		when(service.patch(1L, 5L, patch)).thenReturn(patched);

		final var response = controller.patch(1L, 5L, patch);

		assertThat(response)
				.returns(Response.Status.OK.getStatusCode(), Response::getStatus)
				.returns(patched, Response::getEntity);
		beanValidatorMock.verify(() -> BeanValidator.validate(patch), times(1));
		verify(service, times(1)).patch(1L, 5L, patch);
	}

	@Test
	@DisplayName("patch | Validierungsfehler verhindert den Aufruf des Services")
	void patch_validationError() {
		final var patch = new SchuelerKAoAPatchRequest();
		beanValidatorMock.when(() -> BeanValidator.validate(patch)).thenThrow(new ValidationException("Validation failed"));

		assertThatThrownBy(() -> controller.patch(1L, 5L, patch))
				.isInstanceOf(ValidationException.class)
				.hasMessage("Validation failed");
		verify(service, never()).patch(anyLong(), anyLong(), any());
	}

	@Test
	@DisplayName("delete | Löscht den Eintrag und liefert die SimpleOperationResponse mit Status 200")
	void delete_success() {
		final var deleted = new SimpleOperationResponse();
		when(service.delete(1L, 5L)).thenReturn(deleted);

		final var response = controller.delete(1L, 5L);

		assertThat(response)
				.returns(Response.Status.OK.getStatusCode(), Response::getStatus)
				.returns(deleted, Response::getEntity);
		verify(service, times(1)).delete(1L, 5L);
	}

}
