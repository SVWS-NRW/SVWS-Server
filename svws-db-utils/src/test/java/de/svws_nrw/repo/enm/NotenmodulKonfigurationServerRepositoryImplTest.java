package de.svws_nrw.repo.enm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulKonfigurationServer;

/**
 * Testklasse für das NotenmodulKonfigurationServerRepositoryImpl.
 */
@ExtendWith(MockitoExtension.class)
class NotenmodulKonfigurationServerRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private NotenmodulKonfigurationServerRepositoryImpl repository;


	@Test
	@DisplayName("Test: getById() nutzt das Attribut Schlüssel für die Abfrage")
	void testGetById() {
		final String schluessel = "notenmodul.einstellungen.allgemein";
		final DTONotenmodulKonfigurationServer dto = new DTONotenmodulKonfigurationServer(schluessel, "{ farbe: \"rot\" }");

		when(conn.queryByKey(DTONotenmodulKonfigurationServer.class, schluessel)).thenReturn(dto);

		final DTONotenmodulKonfigurationServer result = repository.getById(schluessel);

		assertThat(result).isNotNull().isEqualTo(dto);
		verify(conn).queryByKey(DTONotenmodulKonfigurationServer.class, schluessel);
	}

	@Test
	@DisplayName("Test: mapIdToParameter() konvertiert den Schlüssel in ein Object-Arra.")
	void testMapIdToParameter() {
		final String schluessel = "notenmodul.einstellungen.allgemein";
		repository.findById(schluessel);
		verify(conn).queryByKey(DTONotenmodulKonfigurationServer.class, schluessel);
	}

}
