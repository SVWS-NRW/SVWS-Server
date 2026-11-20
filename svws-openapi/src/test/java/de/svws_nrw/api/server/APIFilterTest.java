package de.svws_nrw.api.server;

import de.svws_nrw.controller.filter.FilterCatalogController;
import de.svws_nrw.controller.filter.FilterControllerFactory;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("APIFilter")
class APIFilterTest {

	private static final String SCHEMA = "gymabi";

	private final APIFilter cut = new APIFilter();

	@Test
	@DisplayName("getFilterCatalogValues | holt die Factory ohne Domain und delegiert den Katalognamen an den Controller")
	void getFilterCatalogValuesDelegates() {
		final var request = mock(HttpServletRequest.class);
		final var factory = mock(FilterControllerFactory.class);
		final var controller = mock(FilterCatalogController.class);
		final var expected = mock(Response.class);
		when(factory.getFilterCatalogController()).thenReturn(controller);
		when(controller.getByName("Orte")).thenReturn(expected);
		try (MockedStatic<FilterControllerFactory> factoryMock = mockStatic(FilterControllerFactory.class)) {
			factoryMock.when(() -> FilterControllerFactory.withReadAccess(request)).thenReturn(factory);

			final var result = cut.getFilterCatalogValues(SCHEMA, "Orte", request);

			assertThat(result).isSameAs(expected);
			verify(controller).getByName("Orte");
		}
	}

	@Test
	@DisplayName("getFilterCatalogValues | reicht NOT_FOUND des Controllers für einen unbekannten Katalog weiter")
	void getFilterCatalogValuesUnknownCatalog() {
		final var request = mock(HttpServletRequest.class);
		final var factory = mock(FilterControllerFactory.class);
		final var controller = mock(FilterCatalogController.class);
		final var error = new ApiOperationException(Status.NOT_FOUND, "Der Katalog 'Unbekannt' ist unbekannt.");
		when(factory.getFilterCatalogController()).thenReturn(controller);
		when(controller.getByName("Unbekannt")).thenThrow(error);
		try (MockedStatic<FilterControllerFactory> factoryMock = mockStatic(FilterControllerFactory.class)) {
			factoryMock.when(() -> FilterControllerFactory.withReadAccess(request)).thenReturn(factory);

			assertThatThrownBy(() -> cut.getFilterCatalogValues(SCHEMA, "Unbekannt", request)).isSameAs(error);
		}
	}

	@Test
	@DisplayName("getFilterCatalogValues | reicht einen Zugriffsfehler der Factory weiter und fragt keinen Controller an")
	void getFilterCatalogValuesAccessDenied() {
		final var request = mock(HttpServletRequest.class);
		final var error = new IllegalStateException("kein Zugriff");
		try (MockedStatic<FilterControllerFactory> factoryMock = mockStatic(FilterControllerFactory.class)) {
			factoryMock.when(() -> FilterControllerFactory.withReadAccess(request)).thenThrow(error);

			assertThatThrownBy(() -> cut.getFilterCatalogValues(SCHEMA, "Orte", request)).isSameAs(error);
		}
	}
}
