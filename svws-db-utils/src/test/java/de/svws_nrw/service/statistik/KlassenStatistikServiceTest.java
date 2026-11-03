package de.svws_nrw.service.statistik;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.klassen.KlassenleitungenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schule.EigeneSchuleRepository;

/**
 * Testet den Service für den Zugriff auf die Klassendaten für die Statistik.
 */
@ExtendWith(MockitoExtension.class)
class KlassenStatistikServiceTest {

	private static final long ID_ABSCHNITT = 14L;

	@Mock
	private EigeneSchuleRepository eigeneSchuleRepository;

	@Mock
	private KlassenRepository klassenRepository;

	@Mock
	private KlassenleitungenRepository klassenleitungenRepository;

	@Mock
	private SchuelerLernabschnittRepository lernabschnitteRepository;

	private KlassenStatistikService createService(final List<DTOKlassen> klassen) {
		when(eigeneSchuleRepository.getIdSchuljahresabschnitt()).thenReturn(ID_ABSCHNITT);
		when(klassenRepository.getListBySchuljahresabschnitt(ID_ABSCHNITT)).thenReturn(klassen);
		when(klassenleitungenRepository.getMapKlassenleitungen(klassen.stream().map(k -> k.ID).toList())).thenReturn(Map.of());
		when(lernabschnitteRepository.getMapKlassenSchueler(klassen.stream().map(k -> k.ID).toList())).thenReturn(Map.of());
		return new KlassenStatistikService(eigeneSchuleRepository, klassenRepository, klassenleitungenRepository, lernabschnitteRepository);
	}

	@Test
	@DisplayName("getList: istJva und istReformpaedagogik werden aus der Klasse übernommen")
	void testGetListUebernimmtIstJvaUndIstReformpaedagogik() {
		final var mitFlags = new DTOKlassen(1L, ID_ABSCHNITT, "5a", true, true);
		mitFlags.Sortierung = 1;
		final var ohneFlags = new DTOKlassen(2L, ID_ABSCHNITT, "5b", false, false);
		ohneFlags.Sortierung = 2;
		final var service = createService(List.of(mitFlags, ohneFlags));

		final var result = service.getList();

		assertThat(result).hasSize(2);
		assertThat(result.get(0).istJva).isTrue();
		assertThat(result.get(0).istReformpaedagogik).isTrue();
		assertThat(result.get(1).istJva).isFalse();
		assertThat(result.get(1).istReformpaedagogik).isFalse();
	}

	@Test
	@DisplayName("getList: Fehlende Werte (null) werden als false übernommen")
	void testGetListNullWirdFalse() {
		final var klasse = new DTOKlassen(1L, ID_ABSCHNITT, "5a", false, false);
		klasse.Sortierung = 1;
		klasse.istJva = null;
		klasse.istReformpaedagogik = null;
		final var service = createService(List.of(klasse));

		final var result = service.getList();

		assertThat(result.getFirst().istJva).isFalse();
		assertThat(result.getFirst().istReformpaedagogik).isFalse();
	}

}
