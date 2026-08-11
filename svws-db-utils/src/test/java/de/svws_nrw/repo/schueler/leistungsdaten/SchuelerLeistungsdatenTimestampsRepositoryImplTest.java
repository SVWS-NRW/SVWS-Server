package de.svws_nrw.repo.schueler.leistungsdaten;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLeistungsdaten;

@ExtendWith(MockitoExtension.class)
class SchuelerLeistungsdatenTimestampsRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuelerLeistungsdatenTimestampsRepositoryImpl repository;

	@Test
	@DisplayName("Test: Erstelle einen neuen Eintrag und prüfe die Zuweisung der neuen ID.")
	void testCreate() {
		final long idAlt = -1L;
		final DTOTimestampsSchuelerLeistungsdaten neu =
				new DTOTimestampsSchuelerLeistungsdaten(idAlt, "tsNotenKrz", "tsNotenKrzQuartal", "tsFehlStd", "tsuFehlStd", "tsLernentw", "tsWarnung");

		final long idNeu = 42L;

		when(conn.transactionGetNextID(DTOTimestampsSchuelerLeistungsdaten.class)).thenReturn(idNeu);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOTimestampsSchuelerLeistungsdaten result = repository.create(neu);

		assertNotNull(result);
		assertEquals(idNeu, result.ID, "Die ID wurde nicht korrekt gesetzt.");
		verify(conn).transactionGetNextID(DTOTimestampsSchuelerLeistungsdaten.class);
		verify(conn).transactionPersist(neu);
	}

	@Test
	@DisplayName("Test: Der Konstruktor setzt die ID-Zugriffe (getId/setId) korrekt auf das ID-Feld.")
	void testConstructorAndIdAccess() {
		// Teste getId indirekt via getMap()
		final DTOTimestampsSchuelerLeistungsdaten k =
				new DTOTimestampsSchuelerLeistungsdaten(999L, "tsNotenKrz", "tsNotenKrzQuartal", "tsFehlStd", "tsuFehlStd", "tsLernentw", "tsWarnung");
		when(conn.queryAll(DTOTimestampsSchuelerLeistungsdaten.class)).thenReturn(List.of(k));
		assertThat(repository.getMap()).containsEntry(999L, k);

		// Teste setId über die create-Methode
		when(conn.transactionGetNextID(DTOTimestampsSchuelerLeistungsdaten.class)).thenReturn(1000L);
		when(conn.transactionPersist(k)).thenReturn(true);
		repository.create(k);
		assertThat(k.ID).isEqualTo(1000L);
	}

}
