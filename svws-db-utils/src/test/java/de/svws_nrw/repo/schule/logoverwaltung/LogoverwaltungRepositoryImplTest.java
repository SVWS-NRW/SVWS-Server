package de.svws_nrw.repo.schule.logoverwaltung;

import java.util.Optional;

import de.svws_nrw.core.types.reporting.ReportingBildDefinition;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOLogo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogoverwaltungRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private LogoverwaltungRepositoryImpl repository;

	private static final String VALID_BASE64_PNG =
			"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==";

	// -------------------------------------------------------------------------
	// Konstruktor
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new LogoverwaltungRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(LogoverwaltungRepositoryImpl.class)
				.isInstanceOf(LogoverwaltungRepository.class);
	}

	// -------------------------------------------------------------------------
	// existsByKennung
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("existsByKennung")
	class ExistsByKennung {

		private static final String EXPECTED_QUERY =
				"SELECT e FROM DTOLogo e WHERE e.kennung = ?1";

		@Test
		@DisplayName("Gibt true zurück, wenn ein Logo mit der Kennung existiert")
		void existsByKennung_found() {
			final ReportingBildDefinition kennung = ReportingBildDefinition.SCHULLOGO_QUADRATISCH;

			when(conn.existsBy(EXPECTED_QUERY, DTOLogo.class, kennung)).thenReturn(true);

			final var result = repository.existsByKennung(kennung);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(EXPECTED_QUERY, DTOLogo.class, kennung);
		}

		@Test
		@DisplayName("Gibt false zurück, wenn kein Logo mit der Kennung existiert")
		void existsByKennung_notFound() {
			final ReportingBildDefinition kennung = ReportingBildDefinition.SCHULLOGO_QUADRATISCH;

			when(conn.existsBy(EXPECTED_QUERY, DTOLogo.class, kennung)).thenReturn(false);

			final var result = repository.existsByKennung(kennung);

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(EXPECTED_QUERY, DTOLogo.class, kennung);
		}
	}

	// -------------------------------------------------------------------------
	// findByKennung
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("findByKennung")
	class FindByKennung {

		@Test
		@DisplayName("Gibt befülltes Optional zurück, wenn Logo mit Kennung gefunden")
		void findByKennung_found() {
			final ReportingBildDefinition kennung = ReportingBildDefinition.SCHULLOGO_QUADRATISCH;
			final DTOLogo dto = new DTOLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD, VALID_BASE64_PNG, "2024-01-01");

			when(conn.queryFirst(DTOLogo.QUERY_BY_KENNUNG, DTOLogo.class, kennung))
					.thenReturn(Optional.of(dto));

			final var result = repository.findByKennung(kennung);

			assertThat(result).isPresent().contains(dto);
			verify(conn, times(1)).queryFirst(DTOLogo.QUERY_BY_KENNUNG, DTOLogo.class, kennung);
		}

		@Test
		@DisplayName("Gibt leeres Optional zurück, wenn kein Logo mit Kennung gefunden")
		void findByKennung_notFound() {
			final ReportingBildDefinition kennung = ReportingBildDefinition.SCHULLOGO_QUADRATISCH;

			when(conn.queryFirst(DTOLogo.QUERY_BY_KENNUNG, DTOLogo.class, kennung))
					.thenReturn(Optional.empty());

			final var result = repository.findByKennung(kennung);

			assertThat(result).isEmpty();
			verify(conn, times(1)).queryFirst(DTOLogo.QUERY_BY_KENNUNG, DTOLogo.class, kennung);
		}
	}
}
