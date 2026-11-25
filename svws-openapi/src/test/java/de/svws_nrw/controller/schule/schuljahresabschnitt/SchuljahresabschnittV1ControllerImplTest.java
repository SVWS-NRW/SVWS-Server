package de.svws_nrw.controller.schule.schuljahresabschnitt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.schuljahresabschnitt.v1.SchuljahresabschnittV1;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.SchuljahresabschnittV1Mapper;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.ws.rs.core.Response;

/**
 * Tests für den {@link SchuljahresabschnittV1ControllerImpl}.
 */
@ExtendWith(MockitoExtension.class)
class SchuljahresabschnittV1ControllerImplTest {

	@Mock
	private SchuljahresabschnittService service;

	@Mock
	private SchuljahresabschnittV1Mapper mapper;

	private SchuljahresabschnittV1ControllerImpl controller;

	@BeforeEach
	void setUp() {
		controller = new SchuljahresabschnittV1ControllerImpl(service, mapper);
	}

	@Test
	@DisplayName("getList | Erfolg: mappt die Abschnitte des Services und liefert 200")
	void getList_success() {
		final var abschnitte = List.of(new Schuljahresabschnitt());
		final var abschnitteV1 = List.of(new SchuljahresabschnittV1());
		when(service.getList()).thenReturn(abschnitte);
		when(mapper.toApiListNewestFirst(abschnitte)).thenReturn(abschnitteV1);

		final var response = controller.getList();

		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getEntity()).isEqualTo(abschnitteV1);
		verify(service, times(1)).getList();
	}

	@Test
	@DisplayName("getList | Fehler im Service wird weitergereicht")
	void getList_serviceError() {
		when(service.getList()).thenThrow(new ApiOperationException(Response.Status.BAD_REQUEST, "Fehler"));

		assertThatThrownBy(() -> controller.getList())
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
				.hasMessage("Fehler");
	}

}
