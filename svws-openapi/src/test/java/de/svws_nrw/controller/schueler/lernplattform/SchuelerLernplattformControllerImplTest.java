package de.svws_nrw.controller.schueler.lernplattform;

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

import de.svws_nrw.core.data.schueler.SchuelerLernplattform;
import de.svws_nrw.service.schueler.lernplattform.SchuelerLernplattformPatchRequest;
import de.svws_nrw.service.schueler.lernplattform.SchuelerLernplattformService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;

/**
 * Tests für den {@link SchuelerLernplattformControllerImpl}.
 */
@ExtendWith(MockitoExtension.class)
class SchuelerLernplattformControllerImplTest {

	@Mock
	private SchuelerLernplattformService service;

	private SchuelerLernplattformControllerImpl controller;

	private MockedStatic<BeanValidator> beanValidatorMock;

	@BeforeEach
	void setUp() {
		controller = new SchuelerLernplattformControllerImpl(service);
		beanValidatorMock = mockStatic(BeanValidator.class);
	}

	@AfterEach
	void tearDown() {
		beanValidatorMock.close();
	}

	@Test
	@DisplayName("getListByIdSchueler | Liefert die Zuordnungen mit Status 200")
	void getListByIdSchueler_success() {
		final var zuordnungen = List.of(new SchuelerLernplattform());
		when(service.getListByIdSchueler(1L)).thenReturn(zuordnungen);

		final var response = controller.getListByIdSchueler(1L);

		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getEntity()).isEqualTo(zuordnungen);
		verify(service, times(1)).getListByIdSchueler(1L);
	}

	@Test
	@DisplayName("patch | Validiert den Patch und liefert die aktualisierte Zuordnung mit Status 200")
	void patch_success() {
		final var patch = new SchuelerLernplattformPatchRequest();
		final var patched = new SchuelerLernplattform();
		when(service.patch(1L, 10L, patch)).thenReturn(patched);

		final var response = controller.patch(1L, 10L, patch);

		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getEntity()).isSameAs(patched);
		beanValidatorMock.verify(() -> BeanValidator.validate(patch), times(1));
		verify(service, times(1)).patch(1L, 10L, patch);
	}

	@Test
	@DisplayName("patch | Validierungsfehler verhindert den Aufruf des Services")
	void patch_validationError() {
		final var patch = new SchuelerLernplattformPatchRequest();
		beanValidatorMock.when(() -> BeanValidator.validate(patch)).thenThrow(new ValidationException("Validation failed"));

		assertThatThrownBy(() -> controller.patch(1L, 10L, patch))
				.isInstanceOf(ValidationException.class)
				.hasMessage("Validation failed");
		verify(service, never()).patch(anyLong(), anyLong(), any());
	}

}
