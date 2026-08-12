package de.svws_nrw.controller.schule.merkmale;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Merkmal;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalCreateRequest;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalPatchRequest;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalService;
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
class MerkmalControllerTest {

	@Mock
	private MerkmalService merkmalService;

	private MerkmalController merkmalController;

	private MockedStatic<BeanValidator> beanValidatorMock;

	@BeforeEach
	void setUp() {
		merkmalController = new MerkmalController(merkmalService);
		beanValidatorMock = mockStatic(BeanValidator.class);
	}

	@AfterEach
	void tearDown() {
		beanValidatorMock.close();
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		when(merkmalService.getAll()).thenReturn(List.of(mock(Merkmal.class)));

		assertThat(merkmalController.getAll())
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.isNotNull()
				.hasSize(1)
				.first()
				.satisfies(merkmal -> assertThat(merkmal).isInstanceOf(Merkmal.class));
	}

	@Test
	@DisplayName("create | Erfolg")
	void create_success() {
		final var dto = mock(MerkmalCreateRequest.class);
		final var createdMerkmal = mock(Merkmal.class);

		beanValidatorMock.when(() -> BeanValidator.validate(dto)).thenAnswer(invocation -> null);
		when(merkmalService.create(dto)).thenReturn(createdMerkmal);

		assertThat(merkmalController.create(dto))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.CREATED.getStatusCode())
				.extracting(Response::getEntity)
				.isNotNull()
				.satisfies(entity -> assertThat(entity).isInstanceOf(Merkmal.class));

		beanValidatorMock.verify(() -> BeanValidator.validate(dto), times(1));
		verify(merkmalService, times(1)).create(dto);
	}

	@Test
	@DisplayName("create | Validierungsfehler")
	void create_validationError() {
		final var dto = mock(MerkmalCreateRequest.class);

		beanValidatorMock.when(() -> BeanValidator.validate(dto))
				.thenThrow(new ValidationException("Validation failed"));

		assertThatThrownBy(() -> merkmalController.create(dto))
				.isInstanceOf(ValidationException.class)
				.hasMessage("Validation failed");

		verify(merkmalService, never()).create(any());
	}

	@Test
	@DisplayName("delete | Erfolg mit mehreren IDs")
	void delete_success() {
		final var ids = List.of(1L, 2L, 3L);
		final var deleteResponses = List.of(mock(SimpleOperationResponse.class));

		when(merkmalService.delete(ids)).thenReturn(deleteResponses);

		assertThat(merkmalController.delete(ids))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.isNotNull()
				.hasSize(1);

		verify(merkmalService, times(1)).delete(ids);
	}

	@Test
	@DisplayName("patch | Erfolg")
	void patch_success() {
		final var id = 1L;
		final var dto = mock(MerkmalPatchRequest.class);
		final var patchedMerkmal = mock(Merkmal.class);

		beanValidatorMock.when(() -> BeanValidator.validate(dto)).thenAnswer(invocation -> null);
		when(merkmalService.patch(id, dto)).thenReturn(patchedMerkmal);

		assertThat(merkmalController.patch(id, dto))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isNotNull()
				.satisfies(entity -> assertThat(entity).isInstanceOf(Merkmal.class));

		beanValidatorMock.verify(() -> BeanValidator.validate(dto), times(1));
		verify(merkmalService, times(1)).patch(id, dto);
	}

	@Test
	@DisplayName("patch | Validierungsfehler")
	void patch_validationError() {
		final var id = 1L;
		final var dto = mock(MerkmalPatchRequest.class);

		beanValidatorMock.when(() -> BeanValidator.validate(dto))
				.thenThrow(new ValidationException("Invalid patch data"));

		assertThatThrownBy(() -> merkmalController.patch(id, dto))
				.isInstanceOf(ValidationException.class)
				.hasMessage("Invalid patch data");

		verify(merkmalService, never()).patch(anyLong(), any());
	}
}
