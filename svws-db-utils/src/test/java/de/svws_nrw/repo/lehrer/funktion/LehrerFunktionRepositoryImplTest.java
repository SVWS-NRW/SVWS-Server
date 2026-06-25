package de.svws_nrw.repo.lehrer.funktion;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerFunktion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LehrerFunktionRepositoryImplTest {

	private static final String QUERY = "SELECT e FROM DTOLehrerFunktion e WHERE e.idAbschnittsdaten = ?1 AND e.idFunktion = ?2";
	private static final String QUERY_WITH_ID_EXCLUDE = "SELECT e FROM DTOLehrerFunktion e WHERE e.idAbschnittsdaten = ?1 AND e.idFunktion = ?2 AND e.id != ?3";

	@Mock
	private DBEntityManager conn;

	@Test
	@DisplayName("constructor | Erfolg")
	void constructor_success() {
		final var repository = new LehrerFunktionRepositoryImpl(conn);

		assertThat(repository)
				.isNotNull()
				.isInstanceOf(LehrerFunktionRepository.class)
				.isInstanceOf(LehrerFunktionRepositoryImpl.class);
	}

	@Test
	@DisplayName("findAllByIdAbschnitt | Erfolg")
	void findAllByIdAbschnitt_success() {
		final var repository = new LehrerFunktionRepositoryImpl(conn);
		final long idAbschnitt = 123L;

		final var expected = List.of(
				new DTOLehrerFunktion(1L, idAbschnitt, 10L),
				new DTOLehrerFunktion(2L, idAbschnitt, 11L)
		);

		when(conn.queryList(DTOLehrerFunktion.QUERY_BY_IDABSCHNITTSDATEN, DTOLehrerFunktion.class, idAbschnitt))
				.thenReturn(expected);

		final var result = repository.findAllByIdAbschnitt(idAbschnitt);

		assertThat(result).isSameAs(expected);
		verify(conn).queryList(DTOLehrerFunktion.QUERY_BY_IDABSCHNITTSDATEN, DTOLehrerFunktion.class, idAbschnitt);
	}

	@Test
	@DisplayName("existsByIdAbschnittAndIdFunktion | true wenn Treffer vorhanden")
	void existsByIdAbschnittAndIdFunktion_true() {
		final var repository = new LehrerFunktionRepositoryImpl(conn);
		final long idAbschnitt = 12L;
		final long idFunktion = 34L;

		when(conn.existsBy(
				QUERY,
				DTOLehrerFunktion.class, idAbschnitt, idFunktion
		)).thenReturn(true);

		assertThat(repository.existsByIdAbschnittAndIdFunktion(idAbschnitt, idFunktion)).isTrue();

		verify(conn).existsBy(
				QUERY,
				DTOLehrerFunktion.class, idAbschnitt, idFunktion
		);
	}

	@Test
	@DisplayName("existsByIdAbschnittAndIdFunktion | false wenn keine Treffer vorhanden")
	void existsByIdAbschnittAndIdFunktion_false() {
		final var repository = new LehrerFunktionRepositoryImpl(conn);
		final long idAbschnitt = 12L;
		final long idFunktion = 34L;

		when(conn.existsBy(
				QUERY,
				DTOLehrerFunktion.class, idAbschnitt, idFunktion
		)).thenReturn(false);

		assertThat(repository.existsByIdAbschnittAndIdFunktion(idAbschnitt, idFunktion)).isFalse();

		verify(conn).existsBy(
				QUERY,
				DTOLehrerFunktion.class, idAbschnitt, idFunktion
		);
	}

	@Test
	@DisplayName("existsByIdAbschnittAndIdFunktionExcludingId | true wenn Treffer vorhanden")
	void existsByIdAbschnittAndIdFunktionExcludingId_true() {
		final var repository = new LehrerFunktionRepositoryImpl(conn);
		final long idAbschnitt = 12L;
		final long idFunktion = 34L;
		final long excludeId = 56L;

		when(conn.existsBy(
				QUERY_WITH_ID_EXCLUDE,
				DTOLehrerFunktion.class, idAbschnitt, idFunktion, excludeId
		)).thenReturn(true);

		assertThat(repository.existsByIdAbschnittAndIdFunktionExcludingId(idAbschnitt, idFunktion, excludeId)).isTrue();

		verify(conn).existsBy(
				QUERY_WITH_ID_EXCLUDE,
				DTOLehrerFunktion.class, idAbschnitt, idFunktion, excludeId
		);
	}

	@Test
	@DisplayName("existsByIdAbschnittAndIdFunktionExcludingId | false wenn keine Treffer vorhanden")
	void existsByIdAbschnittAndIdFunktionExcludingId_false() {
		final var repository = new LehrerFunktionRepositoryImpl(conn);
		final long idAbschnitt = 12L;
		final long idFunktion = 34L;
		final long excludeId = 56L;

		assertThat(repository.existsByIdAbschnittAndIdFunktionExcludingId(idAbschnitt, idFunktion, excludeId)).isFalse();

		verify(conn).existsBy(
				QUERY_WITH_ID_EXCLUDE,
				DTOLehrerFunktion.class, idAbschnitt, idFunktion, excludeId
		);
	}
}
