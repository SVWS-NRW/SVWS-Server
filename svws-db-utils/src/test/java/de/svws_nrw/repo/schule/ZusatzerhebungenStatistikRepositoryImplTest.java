package de.svws_nrw.repo.schule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOStatistikZusatzerhebungen;

@ExtendWith(MockitoExtension.class)
class ZusatzerhebungenStatistikRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private ZusatzerhebungenStatistikRepositoryImpl repository;

	@Test
	@DisplayName("Test: Bestimme die Zusatzerhebungen anhand der ID des Schuljahresabschnitts.")
	void testFindById() {
		final DTOStatistikZusatzerhebungen entity = new DTOStatistikZusatzerhebungen(42L, false);
		when(conn.queryByKey(DTOStatistikZusatzerhebungen.class, 42L)).thenReturn(entity);

		assertThat(repository.findById(42L)).containsSame(entity);
	}

	@Test
	@DisplayName("Test: Beim Anlegen bleibt die ID des Schuljahresabschnitts erhalten und es wird keine neue ID vergeben.")
	void testCreateBehaeltId() {
		final DTOStatistikZusatzerhebungen entity = new DTOStatistikZusatzerhebungen(42L, false);
		when(conn.transactionPersist(entity)).thenReturn(true);

		repository.create(entity);

		assertThat(entity.idSchuljahresabschnitt).isEqualTo(42L);
		verify(conn, never()).transactionGetNextID(DTOStatistikZusatzerhebungen.class);
	}

}
