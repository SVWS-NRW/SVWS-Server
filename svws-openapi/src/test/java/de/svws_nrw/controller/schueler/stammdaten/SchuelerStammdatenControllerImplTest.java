package de.svws_nrw.controller.schueler.stammdaten;

import java.util.List;

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.service.schueler.stammdaten.SchuelerImportData;
import de.svws_nrw.service.schueler.stammdaten.SchuelerStammdatenPatchRequest;
import de.svws_nrw.service.schueler.stammdaten.SchuelerStammdatenService;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class SchuelerStammdatenControllerImplTest {

	@Mock
	private SchuelerStammdatenService schuelerStammdatenService;

	private SchuelerStammdatenControllerImpl schuelerStammdatenControllerImpl;

	private MockedStatic<BeanValidator> beanValidatorMock;

	@BeforeEach
	void setUp() {
		schuelerStammdatenControllerImpl =
				new SchuelerStammdatenControllerImpl(schuelerStammdatenService);

		beanValidatorMock = mockStatic(BeanValidator.class);
	}

	@AfterEach
	void tearDown() {
		beanValidatorMock.close();
	}

	@Test
	@DisplayName("get | Erfolg")
	void get_success() {
		final var id = 1L;
		final var stammdaten = mock(SchuelerStammdaten.class);

		when(schuelerStammdatenService.get(id)).thenReturn(stammdaten);

		assertThat(schuelerStammdatenControllerImpl.get(id))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isNotNull()
				.isEqualTo(stammdaten);

		verify(schuelerStammdatenService, times(1)).get(id);
	}

	@Test
	@DisplayName("getList | Erfolg mit mehreren IDs")
	void getList_success() {
		final var ids = List.of(1L, 2L, 3L);
		final var stammdaten = List.of(
				mock(SchuelerStammdaten.class),
				mock(SchuelerStammdaten.class)
		);

		when(schuelerStammdatenService.getList(ids)).thenReturn(stammdaten);

		assertThat(schuelerStammdatenControllerImpl.getList(ids))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.isNotNull()
				.hasSize(2);

		verify(schuelerStammdatenService, times(1)).getList(ids);
	}

	@Test
	@DisplayName("getList | Erfolg mit leerer Liste")
	void getList_success_emptyList() {
		final var ids = List.<Long>of();

		when(schuelerStammdatenService.getList(ids)).thenReturn(List.of());

		assertThat(schuelerStammdatenControllerImpl.getList(ids))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.isNotNull()
				.isEmpty();

		verify(schuelerStammdatenService, times(1)).getList(ids);
	}

	@Test
	@DisplayName("create | Erfolg")
	void create_success() {
		final var dto = mock(SchuelerImportData.class);
		final var created = mock(SchuelerStammdaten.class);

		beanValidatorMock
				.when(() -> BeanValidator.validate(dto))
				.thenAnswer(invocation -> null);

		when(schuelerStammdatenService.create(dto)).thenReturn(created);

		assertThat(schuelerStammdatenControllerImpl.create(dto))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.CREATED.getStatusCode())
				.extracting(Response::getEntity)
				.isNotNull()
				.isEqualTo(created);

		beanValidatorMock.verify(
				() -> BeanValidator.validate(dto),
				times(1)
		);
		verify(schuelerStammdatenService, times(1)).create(dto);
	}

	@Test
	@DisplayName("create | Validierungsfehler")
	void create_validationError() {
		final var dto = mock(SchuelerImportData.class);

		beanValidatorMock
				.when(() -> BeanValidator.validate(dto))
				.thenThrow(new ValidationException("Validation failed"));

		assertThatThrownBy(() -> schuelerStammdatenControllerImpl.create(dto))
				.isInstanceOf(ValidationException.class)
				.hasMessage("Validation failed");

		verify(schuelerStammdatenService, never()).create(any());
	}

	@Test
	@DisplayName("delete | Erfolg mit mehreren IDs")
	void delete_success() {
		final var ids = List.of(1L, 2L, 3L);
		final var deleteResponses = List.of(
				mock(SimpleOperationResponse.class),
				mock(SimpleOperationResponse.class)
		);

		when(schuelerStammdatenService.delete(ids)).thenReturn(deleteResponses);

		assertThat(schuelerStammdatenControllerImpl.delete(ids))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.isNotNull()
				.hasSize(2);

		verify(schuelerStammdatenService, times(1)).delete(ids);
	}

	@Test
	@DisplayName("patch | Erfolg")
	void patch_success() {
		final var id = 1L;
		final var dto = mock(SchuelerStammdatenPatchRequest.class);
		final var patched = mock(SchuelerStammdaten.class);

		beanValidatorMock
				.when(() -> BeanValidator.validate(dto))
				.thenAnswer(invocation -> null);

		when(schuelerStammdatenService.patch(id, dto)).thenReturn(patched);

		assertThat(schuelerStammdatenControllerImpl.patch(id, dto))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isNotNull()
				.isEqualTo(patched);

		beanValidatorMock.verify(
				() -> BeanValidator.validate(dto),
				times(1)
		);
		verify(schuelerStammdatenService, times(1)).patch(id, dto);
	}

	@Test
	@DisplayName("patch | Validierungsfehler")
	void patch_validationError() {
		final var id = 1L;
		final var dto = mock(SchuelerStammdatenPatchRequest.class);

		beanValidatorMock
				.when(() -> BeanValidator.validate(dto))
				.thenThrow(new ValidationException("Invalid patch data"));

		assertThatThrownBy(() -> schuelerStammdatenControllerImpl.patch(id, dto))
				.isInstanceOf(ValidationException.class)
				.hasMessage("Invalid patch data");

		verify(schuelerStammdatenService, never()).patch(anyLong(), any());
	}

}
