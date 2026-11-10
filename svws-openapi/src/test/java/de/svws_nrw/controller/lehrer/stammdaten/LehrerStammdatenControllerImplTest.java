package de.svws_nrw.controller.lehrer.stammdaten;

import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.service.lehrer.stammdaten.LehrerStammdatenCreateRequest;
import de.svws_nrw.service.lehrer.stammdaten.LehrerStammdatenPatchRequest;
import de.svws_nrw.service.lehrer.stammdaten.LehrerStammdatenService;
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
import static org.mockito.Mockito.when;

/**
 * Tests für den Controller zu den Stammdaten von Lehrkräften.
 */
@ExtendWith(MockitoExtension.class)
class LehrerStammdatenControllerImplTest {

	private static final long ID = 1L;

	@Mock
	private LehrerStammdatenService service;

	private MockedStatic<BeanValidator> beanValidator;

	private LehrerStammdatenControllerImpl controller;

	private LehrerStammdaten apiModel;

	@BeforeEach
	void setUp() {
		beanValidator = Mockito.mockStatic(BeanValidator.class);
		controller = new LehrerStammdatenControllerImpl(service);

		apiModel = new LehrerStammdaten();
		apiModel.id = ID;
	}

	@AfterEach
	void tearDown() {
		beanValidator.close();
	}

	@Test
	@DisplayName("get liefert die Stammdaten mit Status OK")
	void get() {
		when(service.getById(ID)).thenReturn(apiModel);

		try (var response = controller.get(ID)) {
			assertThat(response)
					.returns(Response.Status.OK.getStatusCode(), Response::getStatus)
					.extracting(Response::getEntity)
					.isSameAs(apiModel);
		}
	}

	@Test
	@DisplayName("getList liefert die Stammdaten zu den IDs mit Status OK")
	void getList() {
		final var ids = List.of(ID);
		when(service.getList(ids)).thenReturn(List.of(apiModel));

		try (var response = controller.getList(ids)) {
			assertThat(response)
					.returns(Response.Status.OK.getStatusCode(), Response::getStatus)
					.extracting(Response::getEntity)
					.isEqualTo(List.of(apiModel));
		}
	}

	@Test
	@DisplayName("create validiert den Request und liefert die Stammdaten mit Status CREATED")
	void create() {
		final var request = new LehrerStammdatenCreateRequest();
		when(service.create(request)).thenReturn(apiModel);

		try (var response = controller.create(request)) {
			beanValidator.verify(() -> BeanValidator.validate(request));
			assertThat(response)
					.returns(Response.Status.CREATED.getStatusCode(), Response::getStatus)
					.extracting(Response::getEntity)
					.isSameAs(apiModel);
		}
	}

	@Test
	@DisplayName("patch validiert den Request und liefert die Stammdaten mit Status OK")
	void patch() {
		final var request = new LehrerStammdatenPatchRequest();
		when(service.patch(ID, request)).thenReturn(apiModel);

		try (var response = controller.patch(ID, request)) {
			beanValidator.verify(() -> BeanValidator.validate(request));
			assertThat(response)
					.returns(Response.Status.OK.getStatusCode(), Response::getStatus)
					.extracting(Response::getEntity)
					.isSameAs(apiModel);
		}
	}

}
