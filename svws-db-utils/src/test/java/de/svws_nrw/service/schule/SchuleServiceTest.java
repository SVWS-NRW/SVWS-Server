package de.svws_nrw.service.schule;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.repo.schule.SchuleRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SchuleService")
class SchuleServiceTest {

	@Mock
	private SchuleRepository schuleRepository;

	@Mock
	private SchuljahresabschnitteRepository schulejahresabschnitteRepository;

	@InjectMocks
	private SchuleService service;

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private static DTOSchuljahresabschnitte schuljahresabschnitt(final long id, final int jahr, final int abschnitt) {
		return new DTOSchuljahresabschnitte(id, jahr, abschnitt);
	}

	// -------------------------------------------------------------------------
	// getSchulnummer()
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getSchulnummer()")
	class GetSchulnummer {

		@Test
		@DisplayName("gibt die Schulnummer aus dem Repository zurück")
		void getSchulnummer_returnsSchulnummerFromRepository() {
			when(schuleRepository.getSchulnummer()).thenReturn(123456);

			assertThat(service.getSchulnummer()).isEqualTo(123456);
			verify(schuleRepository).getSchulnummer();
		}

		@Test
		@DisplayName("delegiert den Aufruf korrekt an das Repository")
		void getSchulnummer_delegatesToRepository() {
			when(schuleRepository.getSchulnummer()).thenReturn(654321);

			service.getSchulnummer();

			verify(schuleRepository).getSchulnummer();
		}
	}

	// -------------------------------------------------------------------------
	// getSchuljahr()
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getSchuljahr()")
	class GetSchuljahr {

		@Test
		@DisplayName("gibt das Schuljahr aus dem Schuljahresabschnitt zurück")
		void getSchuljahr_returnsJahrFromSchuljahresabschnitt() {
			final long abschnittId = 1L;
			final var abschnitt = schuljahresabschnitt(abschnittId, 2024, 1);

			when(schuleRepository.getIdSchuljahresabschnitt()).thenReturn(abschnittId);
			when(schulejahresabschnitteRepository.getById(abschnittId)).thenReturn(abschnitt);

			assertThat(service.getSchuljahr()).isEqualTo(2024);
		}

		@Test
		@DisplayName("verwendet die ID aus schuleRepository um den Abschnitt nachzuschlagen")
		void getSchuljahr_usesIdFromSchuleRepositoryToLookUpAbschnitt() {
			final long abschnittId = 42L;
			final var abschnitt = schuljahresabschnitt(abschnittId, 2025, 2);

			when(schuleRepository.getIdSchuljahresabschnitt()).thenReturn(abschnittId);
			when(schulejahresabschnitteRepository.getById(abschnittId)).thenReturn(abschnitt);

			service.getSchuljahr();

			verify(schuleRepository).getIdSchuljahresabschnitt();
			verify(schulejahresabschnitteRepository).getById(abschnittId);
		}

		@Test
		@DisplayName("gibt das korrekte Jahr zurück wenn Abschnitt 2 ist (zweites Halbjahr)")
		void getSchuljahr_secondAbschnitt_returnsCorrectJahr() {
			final long abschnittId = 7L;
			final var abschnitt = schuljahresabschnitt(abschnittId, 2023, 2);

			when(schuleRepository.getIdSchuljahresabschnitt()).thenReturn(abschnittId);
			when(schulejahresabschnitteRepository.getById(abschnittId)).thenReturn(abschnitt);

			assertThat(service.getSchuljahr()).isEqualTo(2023);
		}
	}

	// -------------------------------------------------------------------------
	// getSchulform()
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getSchulform()")
	class GetSchulform {

		@Test
		@DisplayName("gibt die Schulform aus dem Repository zurück")
		void getSchulform_returnsSchulformFromRepository() {
			when(schuleRepository.getSchulform()).thenReturn(Schulform.GY);

			assertThat(service.getSchulform()).isEqualTo(Schulform.GY);
			verify(schuleRepository).getSchulform();
		}

		@Test
		@DisplayName("gibt auch andere Schulformen korrekt zurück")
		void getSchulform_returnsOtherSchulformen() {
			when(schuleRepository.getSchulform()).thenReturn(Schulform.GE);

			assertThat(service.getSchulform()).isEqualTo(Schulform.GE);
		}

		@Test
		@DisplayName("delegiert den Aufruf korrekt an das Repository")
		void getSchulform_delegatesToRepository() {
			when(schuleRepository.getSchulform()).thenReturn(Schulform.GY);

			service.getSchulform();

			verify(schuleRepository).getSchulform();
		}
	}
}
