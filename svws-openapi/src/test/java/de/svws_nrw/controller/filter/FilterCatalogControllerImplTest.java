package de.svws_nrw.controller.filter;

import java.util.List;

import de.svws_nrw.core.data.filter.FilterCatalogValue;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.filter.FilterCatalogService;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static jakarta.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FilterCatalogControllerImpl")
class FilterCatalogControllerImplTest {

	@Mock
	private FilterCatalogService service;

	private FilterCatalogControllerImpl cut;

	@BeforeEach
	void setUp() {
		cut = new FilterCatalogControllerImpl(service);
	}

	@Test
	@DisplayName("getByName | gibt 200 mit der Liste der Katalogwerte als JSON zurück")
	void getByNameSuccess() {
		final var values = List.of(new FilterCatalogValue());
		when(service.getByName("Orte")).thenReturn(values);

		final var response = cut.getByName("Orte");

		assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
		assertThat(response.getMediaType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
		assertThat(response.getEntity()).isSameAs(values);
		verify(service).getByName("Orte");
	}

	@Test
	@DisplayName("getByName | reicht die ApiOperationException des Services unverändert weiter")
	void getByNameUnknown() {
		final var error = new ApiOperationException(Status.NOT_FOUND, "unbekannt");
		when(service.getByName("Unbekannt")).thenThrow(error);

		assertThatThrownBy(() -> cut.getByName("Unbekannt")).isSameAs(error);
	}
}
