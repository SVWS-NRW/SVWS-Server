package de.svws_nrw.controller.lehrer.personaldaten;

import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.service.lehrer.personaldaten.LehrerPersonaldatenBatchPatchRequest;
import de.svws_nrw.service.lehrer.personaldaten.LehrerPersonaldatenPatchRequest;
import de.svws_nrw.service.lehrer.personaldaten.LehrerPersonaldatenService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests für den Controller zu den Personaldaten von Lehrkräften.
 */
@ExtendWith(MockitoExtension.class)
class LehrerPersonaldatenControllerImplTest {

	private static final long ID = 1L;
	private static final long ID_2 = 2L;

	@Mock
	private LehrerPersonaldatenService service;

	private MockedStatic<BeanValidator> beanValidator;

	private LehrerPersonaldatenControllerImpl controller;

	private LehrerPersonaldaten apiModel;

	@BeforeEach
	void setUp() {
		beanValidator = Mockito.mockStatic(BeanValidator.class);
		controller = new LehrerPersonaldatenControllerImpl(service);

		apiModel = new LehrerPersonaldaten();
		apiModel.id = ID;
	}

	@AfterEach
	void tearDown() {
		beanValidator.close();
	}

	// -------------------------------------------------------------------------
	// get
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("get liefert die Personaldaten mit Status OK")
	void get() {
		when(service.getById(ID)).thenReturn(apiModel);

		try (var response = controller.get(ID)) {
			assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
			assertThat(response.getEntity()).isSameAs(apiModel);
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch validiert den Request und liefert die Personaldaten mit Status OK")
	void patch() {
		final var dto = new LehrerPersonaldatenPatchRequest();

		when(service.patch(ID, dto)).thenReturn(apiModel);

		try (var response = controller.patch(ID, dto)) {
			beanValidator.verify(() -> BeanValidator.validate(dto));
			assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
			assertThat(response.getEntity()).isSameAs(apiModel);
		}
	}

	@Test
	@DisplayName("patch ruft den Service nicht auf, wenn die Validierung fehlschlägt")
	void patch_validierungFehlgeschlagen() {
		final var dto = new LehrerPersonaldatenPatchRequest();

		beanValidator.when(() -> BeanValidator.validate(dto)).thenThrow(new IllegalArgumentException("ungültig"));

		assertThatException().isThrownBy(() -> controller.patch(ID, dto))
				.isInstanceOf(IllegalArgumentException.class);

		verifyNoInteractions(service);
	}

	// -------------------------------------------------------------------------
	// patchMultiple
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patchMultiple validiert jeden Request und liefert die Personaldaten mit Status OK")
	void patchMultiple() {
		final var dto1 = new LehrerPersonaldatenBatchPatchRequest();
		dto1.id = ID;
		final var dto2 = new LehrerPersonaldatenBatchPatchRequest();
		dto2.id = ID_2;
		final var dtos = List.of(dto1, dto2);

		final var apiModel2 = new LehrerPersonaldaten();
		apiModel2.id = ID_2;

		when(service.patchMultiple(dtos)).thenReturn(List.of(apiModel, apiModel2));

		try (var response = controller.patchMultiple(dtos)) {
			beanValidator.verify(() -> BeanValidator.validate(dto1));
			beanValidator.verify(() -> BeanValidator.validate(dto2));
			assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
			assertThat(response.getEntity()).isEqualTo(List.of(apiModel, apiModel2));
		}
	}

	@Test
	@DisplayName("patchMultiple ruft den Service nicht auf, wenn die Validierung eines Requests fehlschlägt")
	void patchMultiple_validierungFehlgeschlagen() {
		final var dto = new LehrerPersonaldatenBatchPatchRequest();
		dto.id = ID;

		beanValidator.when(() -> BeanValidator.validate(dto)).thenThrow(new IllegalArgumentException("ungültig"));

		assertThatException().isThrownBy(() -> controller.patchMultiple(List.of(dto)))
				.isInstanceOf(IllegalArgumentException.class);

		verifyNoInteractions(service);
	}

	@Test
	@DisplayName("patchMultiple reicht eine leere Liste an den Service weiter")
	void patchMultiple_leereListe() {
		when(service.patchMultiple(List.of())).thenReturn(List.of());

		try (var response = controller.patchMultiple(List.of())) {
			assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
			assertThat(response.getEntity()).isEqualTo(List.of());
		}

		verify(service).patchMultiple(List.of());
	}
}
