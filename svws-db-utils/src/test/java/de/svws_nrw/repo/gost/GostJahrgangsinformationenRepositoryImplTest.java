package de.svws_nrw.repo.gost;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;

/**
 * Testet das Repository für die aggregierten Informationen zu den Jahrgängen der gymnasialen Oberstufe
 */
@ExtendWith(MockitoExtension.class)
class GostJahrgangsinformationenRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private GostJahrgangsinformationenRepositoryImpl repository;

	@Test
	@DisplayName("Test: Die Ergebniszeilen der Abfrage werden in Jahrgangsinformationen gemappt.")
	void testGetAll() {
		// Je nach Datenbank-Treiber werden Zahlen als Integer, Long oder BigInteger geliefert
		final List<Object[]> zeilen = List.of(
				new Object[] { 1L, "EF", 10L, 3L, 0L },
				new Object[] { 2, "Q1", 11, BigInteger.valueOf(0), BigInteger.valueOf(25) });
		when(conn.<Object[]>queryNative(anyString())).thenReturn(zeilen);

		final List<GostJahrgangsinformation> result = repository.getAll();

		assertThat(result).containsExactly(
				new GostJahrgangsinformation(1, "EF", 10, 3, 0),
				new GostJahrgangsinformation(2, "Q1", 11, 0, 25));
	}

	@Test
	@DisplayName("Test: Ohne Ergebniszeilen wird eine leere Liste geliefert.")
	void testGetAllLeer() {
		when(conn.<Object[]>queryNative(anyString())).thenReturn(List.of());

		assertThat(repository.getAll()).isEmpty();
	}

}
