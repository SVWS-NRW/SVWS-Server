package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.List;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.schuljahresabschnitt.v1.SchuljahresabschnittV1;
import jakarta.ws.rs.core.Response;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSchuljahresabschnitteV1Test {

	@InjectMocks
	private DataSchuljahresabschnitteV1 cut;

	@Mock
	private DataSchuljahresabschnitte dataSchuljahresabschnitteMock;

	@Test
	void getAllAsResponseWithListOf3SchuljahresabschnittenInDescOrder() {
		final Schuljahresabschnitt abschnitt20261 = new Schuljahresabschnitt();
		abschnitt20261.id = 3L;
		abschnitt20261.schuljahr = 2026;
		abschnitt20261.abschnitt = 1;
		abschnitt20261.idVorigerAbschnitt = 2L;

		final Schuljahresabschnitt abschnitt20252 = new Schuljahresabschnitt();
		abschnitt20252.id = 2L;
		abschnitt20252.schuljahr = 2025;
		abschnitt20252.abschnitt = 2;
		abschnitt20252.idVorigerAbschnitt = 1L;
		abschnitt20252.idFolgeAbschnitt = 3L;

		final Schuljahresabschnitt abschnitt20251 = new Schuljahresabschnitt();
		abschnitt20251.id = 1L;
		abschnitt20251.schuljahr = 2025;
		abschnitt20251.abschnitt = 1;
		abschnitt20251.idFolgeAbschnitt = 2L;

		when(dataSchuljahresabschnitteMock.getAbschnitte()).thenReturn(List.of(abschnitt20251, abschnitt20252, abschnitt20261));

		final Response result = cut.getAllAsResponse();

		assertThat(result)
				.isNotNull()
				.extracting(Response::getStatus)
				.isEqualTo(200);

		assertThat(result)
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.list(SchuljahresabschnittV1.class))
				.hasSize(3)
				.extracting("id", "schuljahr", "abschnitt", "idVorigerAbschnitt", "idFolgeAbschnitt")
				.containsExactly(Tuple.tuple(3L, 2026, 1, 2L, null),
						Tuple.tuple(2L, 2025, 2, 1L, 3L),
						Tuple.tuple(1L, 2025, 1, null, 2L));
	}

	@Test
	void getAllAsResponseWithEmptyListOfSchuljahreabschnitten() {
		when(dataSchuljahresabschnitteMock.getAbschnitte()).thenReturn(Collections.emptyList());

		final Response result = cut.getAllAsResponse();

		assertThat(result)
				.isNotNull()
				.extracting(Response::getStatus)
				.isEqualTo(200);

		assertThat(result)
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.list(SchuljahresabschnittV1.class))
				.isEmpty();
	}

}
