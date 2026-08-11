package de.svws_nrw.repo.schueler.lernabschnitt;

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
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLernabschnittsdaten;

@ExtendWith(MockitoExtension.class)
class SchuelerLernabschnittTimestampRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuelerLernabschnittTimestampRepositoryImpl repository;

	@Test
	@DisplayName("Test: Erstelle einen neuen Eintrag und prüfe die Zuweisung der neuen ID.")
	void testCreate() {
		final long idAlt = -1L;
		final DTOTimestampsSchuelerLernabschnittsdaten neu =
				new DTOTimestampsSchuelerLernabschnittsdaten(idAlt, "tsSumFehlStd", "tsSumFehlStdU", "tsZeugnisBem", "tsASV", "tsAUE", "tsLELS", "tsESF",
						"tsBemerkungFSP", "tsBemerkungVersetzung");

		final long idNeu = 42L;

		when(conn.transactionGetNextID(DTOTimestampsSchuelerLernabschnittsdaten.class)).thenReturn(idNeu);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOTimestampsSchuelerLernabschnittsdaten result = repository.create(neu);

		assertNotNull(result);
		assertEquals(idNeu, result.ID, "Die ID wurde nicht korrekt gesetzt.");
		verify(conn).transactionGetNextID(DTOTimestampsSchuelerLernabschnittsdaten.class);
		verify(conn).transactionPersist(neu);
	}

	@Test
	@DisplayName("Test: Der Konstruktor setzt die ID-Zugriffe (getId/setId) korrekt auf das ID-Feld.")
	void testConstructorAndIdAccess() {
		// Teste getId indirekt via getMap()
		final DTOTimestampsSchuelerLernabschnittsdaten k =
				new DTOTimestampsSchuelerLernabschnittsdaten(999L, "tsSumFehlStd", "tsSumFehlStdU", "tsZeugnisBem", "tsASV", "tsAUE", "tsLELS", "tsESF",
						"tsBemerkungFSP", "tsBemerkungVersetzung");
		when(conn.queryAll(DTOTimestampsSchuelerLernabschnittsdaten.class)).thenReturn(List.of(k));
		assertThat(repository.getMap()).containsEntry(999L, k);

		// Teste setId über die create-Methode
		when(conn.transactionGetNextID(DTOTimestampsSchuelerLernabschnittsdaten.class)).thenReturn(1000L);
		when(conn.transactionPersist(k)).thenReturn(true);
		repository.create(k);
		assertThat(k.ID).isEqualTo(1000L);
	}

}
