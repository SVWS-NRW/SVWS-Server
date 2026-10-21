package de.svws_nrw.repo.schule.kataloge.lernplattform;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.auth.DTOCredentialsLernplattformen;

/**
 * Tests für die Klasse {@link LernplattformCredentialRepositoryImpl}.
 */
@ExtendWith(MockitoExtension.class)
class LernplattformCredentialRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private LernplattformCredentialRepositoryImpl repository;

	@Test
	@DisplayName("Test: Prüfe, ob findMapByIds die Zugangsdaten ihrer ID zuordnet.")
	void testFindMapByIds() {
		final DTOCredentialsLernplattformen credentials1 = new DTOCredentialsLernplattformen(5L, 10L, "benutzer1");
		final DTOCredentialsLernplattformen credentials2 = new DTOCredentialsLernplattformen(6L, 10L, "benutzer2");
		final Set<Long> ids = Set.of(5L, 6L);
		when(conn.queryByKeyList(DTOCredentialsLernplattformen.class, ids)).thenReturn(List.of(credentials1, credentials2));

		assertThat(repository.findMapByIds(ids))
				.hasSize(2)
				.containsEntry(5L, credentials1)
				.containsEntry(6L, credentials2);
	}

	@Test
	@DisplayName("Test: Prüfe, ob findMapByIds bei null oder leerer Eingabe eine leere Map liefert.")
	void testFindMapByIdsEmpty() {
		assertThat(repository.findMapByIds(null)).isEmpty();
		assertThat(repository.findMapByIds(Set.of())).isEmpty();
	}

	@Test
	@DisplayName("Test: Prüfe, ob create die nächste freie ID als ID der Zugangsdaten setzt.")
	void testCreate() {
		final DTOCredentialsLernplattformen neu = new DTOCredentialsLernplattformen(0L, 10L, "benutzer");
		when(conn.transactionGetNextID(DTOCredentialsLernplattformen.class)).thenReturn(3L);
		when(conn.transactionPersist(neu)).thenReturn(true);

		assertThat(repository.create(neu).ID).isEqualTo(3L);
	}

}
