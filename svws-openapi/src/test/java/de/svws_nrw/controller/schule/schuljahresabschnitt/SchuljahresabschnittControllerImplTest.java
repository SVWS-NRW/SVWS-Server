package de.svws_nrw.controller.schule.schuljahresabschnitt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.ws.rs.core.Response;

/**
 * Tests für den {@link SchuljahresabschnittControllerImpl}.
 */
@ExtendWith(MockitoExtension.class)
class SchuljahresabschnittControllerImplTest {

	@Mock
	private SchuljahresabschnittService service;

	private SchuljahresabschnittControllerImpl controller;

	@BeforeEach
	void setUp() {
		controller = new SchuljahresabschnittControllerImpl(service);
	}

	@Test
	@DisplayName("createVorErstem | Erfolg: delegiert an den Service und liefert 201")
	void createVorErstem_success() {
		final var created = new Schuljahresabschnitt();
		created.id = 100L;
		when(service.createVorErstem()).thenReturn(created);

		final var response = controller.createVorErstem();

		assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
		assertThat(response.getEntity()).isEqualTo(created);
		verify(service, times(1)).createVorErstem();
	}

	@Test
	@DisplayName("createVorErstem | Fehler im Service wird weitergereicht")
	void createVorErstem_serviceError() {
		when(service.createVorErstem()).thenThrow(new ApiOperationException(Response.Status.BAD_REQUEST, "Fehler"));

		assertThatThrownBy(() -> controller.createVorErstem())
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
				.hasMessage("Fehler");
	}

	@Test
	@DisplayName("createNachLetztem | Erfolg: delegiert an den Service und liefert 201")
	void createNachLetztem_success() {
		final var created = new Schuljahresabschnitt();
		created.id = 101L;
		when(service.createNachLetztem()).thenReturn(created);

		final var response = controller.createNachLetztem();

		assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
		assertThat(response.getEntity()).isEqualTo(created);
		verify(service, times(1)).createNachLetztem();
	}

	@Test
	@DisplayName("createNachLetztem | Fehler im Service wird weitergereicht")
	void createNachLetztem_serviceError() {
		when(service.createNachLetztem()).thenThrow(new ApiOperationException(Response.Status.BAD_REQUEST, "Fehler"));

		assertThatThrownBy(() -> controller.createNachLetztem())
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
				.hasMessage("Fehler");
	}

}
