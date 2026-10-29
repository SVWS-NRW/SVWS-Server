package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.List;

import de.svws_nrw.core.data.schuljahresabschnitt.v1.SchuljahresabschnittV1;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.mapper.schule.SchuljahresabschnittMapper;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.ws.rs.core.Response;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSchuljahresabschnitteV1Test {

	@Mock
	private SchuljahresabschnitteRepository repositoryMock;

	private DataSchuljahresabschnitteV1 cut;

	@BeforeEach
	void setUp() {
		cut = new DataSchuljahresabschnitteV1(new SchuljahresabschnittService(repositoryMock, SchuljahresabschnittMapper.INSTANCE));
	}

	@Test
	void getAllAsResponseWithListOf3SchuljahresabschnittenInDescOrder() {
		final DTOSchuljahresabschnitte abschnitt20261 = new DTOSchuljahresabschnitte(3L, 2026, 1);
		abschnitt20261.VorigerAbschnitt_ID = 2L;

		final DTOSchuljahresabschnitte abschnitt20252 = new DTOSchuljahresabschnitte(2L, 2025, 2);
		abschnitt20252.VorigerAbschnitt_ID = 1L;
		abschnitt20252.FolgeAbschnitt_ID = 3L;

		final DTOSchuljahresabschnitte abschnitt20251 = new DTOSchuljahresabschnitte(1L, 2025, 1);
		abschnitt20251.FolgeAbschnitt_ID = 2L;

		when(repositoryMock.getAll()).thenReturn(List.of(abschnitt20252, abschnitt20261, abschnitt20251));

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
		when(repositoryMock.getAll()).thenReturn(Collections.emptyList());

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
