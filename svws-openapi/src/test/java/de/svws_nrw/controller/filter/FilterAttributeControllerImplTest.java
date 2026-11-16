package de.svws_nrw.controller.filter;

import java.util.List;

import de.svws_nrw.core.data.filter.FilterAttributeResponse;
import de.svws_nrw.core.types.filter.FilterDomain;
import de.svws_nrw.service.filter.FilterAttributeService;
import jakarta.ws.rs.core.MediaType;
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
@DisplayName("FilterAttributeControllerImpl")
class FilterAttributeControllerImplTest {

	@Mock
	private FilterAttributeService service;

	private FilterAttributeControllerImpl cut;

	@BeforeEach
	void setUp() {
		cut = new FilterAttributeControllerImpl(service);
	}

	@Test
	@DisplayName("getByDomain | gibt 200 mit der Liste der Attribute als JSON zurück")
	void getByDomainSuccess() {
		final var attributes = List.of(new FilterAttributeResponse());
		when(service.getByDomain(FilterDomain.SCHUELER)).thenReturn(attributes);

		final var response = cut.getByDomain(FilterDomain.SCHUELER);

		assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
		assertThat(response.getMediaType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
		assertThat(response.getEntity()).isSameAs(attributes);
		verify(service).getByDomain(FilterDomain.SCHUELER);
	}

	@Test
	@DisplayName("getByDomain | gibt 200 mit leerer Liste zurück, wenn der Service keine Attribute liefert")
	void getByDomainEmpty() {
		when(service.getByDomain(FilterDomain.SCHUELER)).thenReturn(List.of());

		final var response = cut.getByDomain(FilterDomain.SCHUELER);

		assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
		assertThat(response.getEntity()).isEqualTo(List.of());
	}
}
