package de.svws_nrw.service.statistik;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.repo.schule.EigeneSchuleRepository;
import de.svws_nrw.service.schule.SchuljahresabschnittService;

/**
 * Testet den Service für den Zugriff auf die Schuldaten für die Statistik.
 */
@ExtendWith(MockitoExtension.class)
class SchuleStatistikServiceTest {

	@Mock
	private EigeneSchuleRepository eigeneSchuleRepository;

	@Mock
	private SchuljahresabschnittService schuljahresabschnittService;

	private DTOEigeneSchule createSchule() {
		final var schule = new DTOEigeneSchule(1L, 1L, 3L, false, 1L, false, false, false, 1L, 0L);
		schule.SchulNr = 123456;
		schule.Schuljahresabschnitts_ID = 14L;
		return schule;
	}

	@Test
	@DisplayName("get: Die Statistik-Felder werden aus den Spalten der Tabelle EigeneSchule übernommen")
	void testGetUebernimmtStatistikFelder() {
		final var schule = createSchule();
		schule.idGebundenerGanztag = 4L;
		schule.idFormOffenerGanztag = 2L;
		schule.istOffenerGanztag = true;
		schule.JVAZeigen = true;
		schule.idBilingualerUnterricht = 3L;
		schule.hatRealschuleHauptschulbildungsgang = true;
		schule.hatInternationaleKontakte = true;
		schule.hatKonfessionelleKooperation = true;
		schule.idTalentschule = 2L;
		schule.idReformpaedagogik = 3000L;
		when(eigeneSchuleRepository.getFirst()).thenReturn(schule);
		when(schuljahresabschnittService.getList()).thenReturn(List.of());

		final var daten = new SchuleStatistikService(eigeneSchuleRepository, schuljahresabschnittService).get();

		assertThat(daten.idGebundenerGanztag).isEqualTo(4L);
		assertThat(daten.idFormOffenerGanztag).isEqualTo(2L);
		assertThat(daten.istOffenerGanztag).isTrue();
		assertThat(daten.istJva).isTrue();
		assertThat(daten.idBilingualerUnterricht).isEqualTo(3L);
		assertThat(daten.hatRealschuleHauptschulbildungsgang).isTrue();
		assertThat(daten.hatInternationaleKontakte).isTrue();
		assertThat(daten.hatKonfessionelleKooperation).isTrue();
		assertThat(daten.idTalentschule).isEqualTo(2L);
		assertThat(daten.idReformpaedagogik).isEqualTo(3000L);
	}

	@Test
	@DisplayName("get: Fehlende Boolean-Werte (null) werden als false übernommen")
	void testGetNullBooleanWirdFalse() {
		final var schule = createSchule();
		schule.istOffenerGanztag = null;
		schule.JVAZeigen = null;
		schule.hatRealschuleHauptschulbildungsgang = null;
		schule.hatInternationaleKontakte = null;
		schule.hatKonfessionelleKooperation = null;
		when(eigeneSchuleRepository.getFirst()).thenReturn(schule);
		when(schuljahresabschnittService.getList()).thenReturn(List.of());

		final var daten = new SchuleStatistikService(eigeneSchuleRepository, schuljahresabschnittService).get();

		assertThat(daten.istOffenerGanztag).isFalse();
		assertThat(daten.istJva).isFalse();
		assertThat(daten.hatRealschuleHauptschulbildungsgang).isFalse();
		assertThat(daten.hatInternationaleKontakte).isFalse();
		assertThat(daten.hatKonfessionelleKooperation).isFalse();
	}

	@Test
	@DisplayName("get: Die Standardwerte der neuen Spalten werden unverändert übernommen")
	void testGetUebernimmtStandardwerte() {
		when(eigeneSchuleRepository.getFirst()).thenReturn(createSchule());
		when(schuljahresabschnittService.getList()).thenReturn(List.of());

		final var daten = new SchuleStatistikService(eigeneSchuleRepository, schuljahresabschnittService).get();

		assertThat(daten.idGebundenerGanztag).isEqualTo(1L);
		assertThat(daten.idFormOffenerGanztag).isEqualTo(3L);
		assertThat(daten.idBilingualerUnterricht).isEqualTo(1L);
		assertThat(daten.idTalentschule).isEqualTo(1L);
		assertThat(daten.idReformpaedagogik).isZero();
		assertThat(daten.istOffenerGanztag).isFalse();
		assertThat(daten.istJva).isFalse();
	}

	@Test
	@DisplayName("get: Die neuen Felder verändern die übrigen Statistik-Felder nicht")
	void testGetLaesstUebrigeFelderUnveraendert() {
		final var schule = createSchule();
		schule.JVAZeigen = true;
		when(eigeneSchuleRepository.getFirst()).thenReturn(schule);
		when(schuljahresabschnittService.getList()).thenReturn(List.of());

		final var daten = new SchuleStatistikService(eigeneSchuleRepository, schuljahresabschnittService).get();

		assertThat(daten.schulNr).isEqualTo(123456);
		assertThat(daten.idSchuljahresabschnitt).isEqualTo(14L);
		assertThat(daten.dauerUnterrichtseinheit).isEqualTo(45);
		assertThat(daten.istJva).isTrue();
	}
}
