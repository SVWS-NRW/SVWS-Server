package de.svws_nrw.controller.filter;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.filter.FilterDomain;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("FilterControllerFactory")
class FilterControllerFactoryTest {

	@Test
	@DisplayName("withReadAccessByDomain | SCHUELER: DBBenutzerUtils wird mit SCHUELER_INDIVIDUALDATEN_ANSEHEN aufgerufen")
	void withReadAccessByDomainSchueler() {
		final HttpServletRequest request = mock(HttpServletRequest.class);
		try (MockedStatic<DBBenutzerUtils> dbUtilsMock = mockStatic(DBBenutzerUtils.class)) {
			final FilterControllerFactory factory = FilterControllerFactory.withReadAccessByDomain(request, FilterDomain.SCHUELER);

			dbUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(eq(request), any(ServerMode.class),
					eq(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)), times(1));
			assertThat(factory).isNotNull();
		}
	}

	@Test
	@DisplayName("withReadAccessByDomain | Berechtigungsfehler von DBBenutzerUtils wird nicht verschluckt und es entsteht keine Factory")
	void withReadAccessByDomainPropagatesAccessError() {
		final HttpServletRequest request = mock(HttpServletRequest.class);
		final IllegalStateException error = new IllegalStateException("kein Zugriff");
		try (MockedStatic<DBBenutzerUtils> dbUtilsMock = mockStatic(DBBenutzerUtils.class)) {
			dbUtilsMock.when(() -> DBBenutzerUtils.getDBConnection(eq(request), any(ServerMode.class), any(BenutzerKompetenz[].class)))
					.thenThrow(error);

			assertThatThrownBy(() -> FilterControllerFactory.withReadAccessByDomain(request, FilterDomain.SCHUELER)).isSameAs(error);
		}
	}

	@Test
	@DisplayName("getFilterCategoryController | liefert bei jedem Aufruf einen neuen FilterCategoryControllerImpl")
	void getFilterCategoryController() {
		final HttpServletRequest request = mock(HttpServletRequest.class);
		try (MockedStatic<DBBenutzerUtils> dbUtilsMock = mockStatic(DBBenutzerUtils.class)) {
			final FilterControllerFactory factory = FilterControllerFactory.withReadAccessByDomain(request, FilterDomain.SCHUELER);

			final FilterCategoryController first = factory.getFilterCategoryController();
			final FilterCategoryController second = factory.getFilterCategoryController();

			assertThat(first).isInstanceOf(FilterCategoryControllerImpl.class);
			assertThat(second).isInstanceOf(FilterCategoryControllerImpl.class).isNotSameAs(first);
			dbUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(eq(request), any(ServerMode.class), any(BenutzerKompetenz[].class)), times(1));
		}
	}
}
