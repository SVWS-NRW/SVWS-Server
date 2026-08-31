package de.svws_nrw.repo.schule.kataloge.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchuleRepositoryImplTest {

	private static final String QUERY_BY_SCHULNR =
			"SELECT e FROM DTOSchuleNRW e WHERE e.SchulNr = ?1";

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuleRepositoryImpl repository;

	// -------------------------------------------------------------------------
	// Konstruktor
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new SchuleRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(SchuleRepositoryImpl.class)
				.isInstanceOf(SchuleRepository.class);
	}

	// -------------------------------------------------------------------------
	// existsBySchulnummer
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("existsBySchulnummer")
	class ExistsBySchulnummer {

		@Test
		@DisplayName("Gibt true zurück wenn Schulnummer vorhanden")
		void existsBySchulnummer_found() {
			when(conn.existsBy(QUERY_BY_SCHULNR, DTOSchuleNRW.class, "123456")).thenReturn(true);

			final var result = repository.existsBySchulnummer("123456");

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_BY_SCHULNR, DTOSchuleNRW.class, "123456");
		}

		@Test
		@DisplayName("Gibt false zurück wenn Schulnummer nicht vorhanden")
		void existsBySchulnummer_notFound() {
			when(conn.existsBy(QUERY_BY_SCHULNR, DTOSchuleNRW.class, "999999")).thenReturn(false);

			final var result = repository.existsBySchulnummer("999999");

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(QUERY_BY_SCHULNR, DTOSchuleNRW.class, "999999");
		}
	}

	// -------------------------------------------------------------------------
	// getMapBySchulnummer
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getMapBySchulnummer")
	class GetMapBySchulnummer {

		@Test
		@DisplayName("Gibt leere Map zurück wenn keine Schulen vorhanden")
		void getMapBySchulnummer_empty() {
			when(conn.queryAll(DTOSchuleNRW.class)).thenReturn(List.of());

			final var result = repository.getSchulenBySchulnummer();

			assertThat(result).isEmpty();
			verify(conn, times(1)).queryAll(DTOSchuleNRW.class);
		}

		@Test
		@DisplayName("Gibt Map mit allen Schulen zurück, Schlüssel ist SchulNr")
		void getMapBySchulnummer_multipleEntries() {
			final var dto1 = new DTOSchuleNRW(1L, "123456");
			final var dto2 = new DTOSchuleNRW(2L, "654321");
			when(conn.queryAll(DTOSchuleNRW.class)).thenReturn(List.of(dto1, dto2));

			final var result = repository.getSchulenBySchulnummer();

			assertThat(result)
					.hasSize(2)
					.containsEntry("123456", dto1)
					.containsEntry("654321", dto2);
			verify(conn, times(1)).queryAll(DTOSchuleNRW.class);
		}

		@Test
		@DisplayName("Gibt Map mit einer Schule zurück")
		void getMapBySchulnummer_singleEntry() {
			final var dto = new DTOSchuleNRW(1L, "123456");
			when(conn.queryAll(DTOSchuleNRW.class)).thenReturn(List.of(dto));

			final var result = repository.getSchulenBySchulnummer();

			assertThat(result)
					.hasSize(1)
					.containsEntry("123456", dto);
			verify(conn, times(1)).queryAll(DTOSchuleNRW.class);
		}
	}
}
