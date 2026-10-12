package de.svws_nrw.controller.filter;

import java.util.List;

import de.svws_nrw.core.data.filter.FilterCategoryResponse;
import de.svws_nrw.core.types.filter.FilterDomain;
import de.svws_nrw.service.filter.FilterCategoryService;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static jakarta.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FilterCategoryControllerImpl")
class FilterCategoryControllerImplTest {

	@Mock
	private FilterCategoryService service;

	private FilterCategoryControllerImpl cut;

	@BeforeEach
	void setUp() {
		cut = new FilterCategoryControllerImpl(service);
	}

	@Test
	@DisplayName("getByDomain | gibt 200 mit der Liste der Kategorien als JSON zurück")
	void getByDomainSuccess() {
		final List<FilterCategoryResponse> categories = List.of(new FilterCategoryResponse());
		when(service.getByDomain(FilterDomain.SCHUELER)).thenReturn(categories);

		final Response response = cut.getByDomain(FilterDomain.SCHUELER);

		assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
		assertThat(response.getMediaType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
		assertThat(response.getEntity()).isSameAs(categories);
		verify(service).getByDomain(FilterDomain.SCHUELER);
	}

	@Test
	@DisplayName("getByDomain | gibt 200 mit leerer Liste zurück, wenn der Service keine Kategorien liefert")
	void getByDomainEmpty() {
		when(service.getByDomain(FilterDomain.SCHUELER)).thenReturn(List.of());

		final Response response = cut.getByDomain(FilterDomain.SCHUELER);

		assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
		assertThat(response.getEntity()).isEqualTo(List.of());
	}
}
