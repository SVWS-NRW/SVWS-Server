package de.svws_nrw.controller.schild3;

import java.util.Collections;
import java.util.List;

import de.svws_nrw.core.data.schild3.Schild3FachklasseDQRNiveauZuordnung;
import de.svws_nrw.service.schild3.Schild3FachklasseDQRNiveauZuordnungService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Schild3FachklasseDQRNiveauZuordnungControllerTest {

	@InjectMocks
	private Schild3FachklasseSchild3FachklasseDQRNiveauZuordnungZuordnungImpl cut;

	@Mock
	private Schild3FachklasseDQRNiveauZuordnungService schild3FachklasseDqrNiveauZuordnungService;

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		final var payload = List.of(new Schild3FachklasseDQRNiveauZuordnung(), new Schild3FachklasseDQRNiveauZuordnung());
		when(schild3FachklasseDqrNiveauZuordnungService.getAll()).thenReturn(payload);

		final var result = cut.getAll();

		assertThat(result).isNotNull()
				.extracting(Response::getStatus, Response::getEntity)
				.containsExactly(Response.Status.OK.getStatusCode(), payload);
	}

	@Test
	@DisplayName("getAll | leere Liste | Erfolg")
	void getAllWithEmptyList() {
		when(schild3FachklasseDqrNiveauZuordnungService.getAll()).thenReturn(Collections.emptyList());

		final var result = cut.getAll();

		assertThat(result).isNotNull()
				.extracting(Response::getStatus, Response::getEntity)
				.containsExactly(Response.Status.OK.getStatusCode(), Collections.emptyList());
	}

}
