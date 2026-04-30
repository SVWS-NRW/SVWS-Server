package de.svws_nrw.repo.wiedervorlage;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schule.DTOWiedervorlage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static de.svws_nrw.repo.wiedervorlage.WiedervorlageRepositoryImpl.QUERY_ALL_BY_BENUTZER_ID;
import static de.svws_nrw.repo.wiedervorlage.WiedervorlageRepositoryImpl.QUERY_ALL_BY_IDS_AND_BENUTZER_ID;
import static de.svws_nrw.repo.wiedervorlage.WiedervorlageRepositoryImpl.QUERY_BY_ID_AND_BENUTZER_ID;
import static de.svws_nrw.repo.wiedervorlage.WiedervorlageRepositoryImpl.QUERY_COUNT_FAELLIG_FOR_BENUTZER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WiedervorlageRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private WiedervorlageRepositoryImpl cut;


	@Test
	@DisplayName("findByBenutzerId | Delegiert Query an DBEntityManager und gibt Ergebnis zurueck")
	void findAllByBenutzerId() {
		final long idBenutzer = 42L;
		final var dto = buildWiedervorlage();
		when(conn.queryList(QUERY_ALL_BY_BENUTZER_ID, DTOWiedervorlage.class, idBenutzer))
				.thenReturn(List.of(dto));

		final var result = cut.findAllByBenutzerId(idBenutzer);

		assertNotNull(result);
	}

	@Test
	@DisplayName("findByIdAndBenutzerId | Delegiert Query an DBEntityManager und gibt Ergebnis zurueck")
	void findByIdAndBenutzerId() {
		final long id = 1L;
		final long idBenutzer = 42L;
		final var dto = new DTOWiedervorlage(id, "Bemerkung", false);
		when(conn.queryList(QUERY_BY_ID_AND_BENUTZER_ID, DTOWiedervorlage.class, id, idBenutzer))
				.thenReturn(List.of(dto));

		final var result = cut.findByIdAndBenutzerId(id, idBenutzer);

		assertNotNull(result);
	}

	@Test
	@DisplayName("getAnzahlOffeneWiedervorlagen | Delegiert Query an DBEntityManager und gibt Ergebnis zurueck")
	void getAnzahlOffeneWiedervorlagen() {
		final long idBenutzer = 42L;
		when(conn.queryList(QUERY_COUNT_FAELLIG_FOR_BENUTZER, Long.class, idBenutzer))
				.thenReturn(Collections.singletonList(2L));

		final var result = cut.getAnzahlOffeneWiedervorlagen(idBenutzer);

		assertThat(result).isEqualTo(2L);
	}

	@Test
	@DisplayName("findAllByIdsAndBenutzerId | Delegiert Delete-Query an DBEntityManager und gibt Ergebnis zurück")
	void findAllByIdsAndBenutzerId() {
		final Set<Long> ids = Set.of(1L, 2L);
		final long idBenutzer = 42L;
		when(conn.queryList(QUERY_ALL_BY_IDS_AND_BENUTZER_ID, DTOWiedervorlage.class, ids, idBenutzer))
				.thenReturn(List.of(buildWiedervorlage()));

		final var result = cut.findAllByIdsAndBenutzerId(ids, idBenutzer);

		assertThat(result).isNotEmpty();
	}

	@Test
	@DisplayName("deleteByIds | Delegiert Delete-Query an DBEntityManager")
	void deleteByIds() {
		final var ids = Set.of(1L, 2L, 3L);

		cut.deleteByIds(ids);

		verify(conn).executeDelete(anyString(), eq(ids));
	}

	private static DTOWiedervorlage buildWiedervorlage() {
		return new DTOWiedervorlage(1L, "Bemerkung", false);
	}
}
