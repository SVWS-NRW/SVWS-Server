package de.svws_nrw.service.schild3;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.mapper.Schild3FachklasseDQRNiveauZuordnungMapper;
import de.svws_nrw.repo.schule.EigeneSchuleRepository;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Schild3FachklasseDQRNiveauZuordnungServiceTest {

	@InjectMocks
	private Schild3FachklasseDQRNiveauZuordnungService cut;

	@Spy
	private Schild3FachklasseDQRNiveauZuordnungMapper mapper = Schild3FachklasseDQRNiveauZuordnungMapper.INSTANCE;

	@Mock
	private EigeneSchuleRepository eigeneSchuleRepository;

	@Mock
	private SchuljahresabschnittService schuljahresabschnittService;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	void getAll() {
		final Schuljahresabschnitt schuljahresabschnitt = new Schuljahresabschnitt();
		schuljahresabschnitt.id = 1L;
		schuljahresabschnitt.schuljahr = 2025;

		when(eigeneSchuleRepository.getIdSchuljahresabschnitt()).thenReturn(1L);
		when(schuljahresabschnittService.getById(1L)).thenReturn(schuljahresabschnitt);

		final var result = cut.getAll();

		assertThat(result).isNotEmpty();
	}
}
