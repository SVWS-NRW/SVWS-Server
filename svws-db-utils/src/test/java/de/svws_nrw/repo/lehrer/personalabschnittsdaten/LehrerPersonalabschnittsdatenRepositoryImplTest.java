package de.svws_nrw.repo.lehrer.personalabschnittsdaten;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LehrerPersonalabschnittsdatenRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@Test
	@DisplayName("constructor | Erfolg")
	void constructor_success() {
		final var repository = new LehrerPersonalabschnittsdatenRepositoryImpl(conn);

		assertThat(repository)
				.isNotNull()
				.isInstanceOf(LehrerPersonalabschnittsdatenRepository.class)
				.isInstanceOf(LehrerPersonalabschnittsdatenRepositoryImpl.class);
	}

	@Test
	@DisplayName("findByIdLehrer | delegiert an DBEntityManager.queryList")
	void findByIdLehrer_delegatesToConn() {
		final var repository = new LehrerPersonalabschnittsdatenRepositoryImpl(conn);
		final var expected = List.of(new DTOLehrerAbschnittsdaten(1L, 10L, 20L));

		when(conn.queryList(
				DTOLehrerAbschnittsdaten.QUERY_BY_LEHRER_ID,
				DTOLehrerAbschnittsdaten.class,
				10L
		)).thenReturn(expected);

		final var result = repository.findByIdLehrer(10L);

		assertThat(result).isSameAs(expected);
		verify(conn).queryList(
				DTOLehrerAbschnittsdaten.QUERY_BY_LEHRER_ID,
				DTOLehrerAbschnittsdaten.class,
				10L
		);
	}
}
