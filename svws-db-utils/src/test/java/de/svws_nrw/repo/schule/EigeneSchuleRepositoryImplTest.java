package de.svws_nrw.repo.schule;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.repo.RepositoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EigeneSchuleRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private EigeneSchuleRepositoryImpl repository;

	@BeforeEach
	void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("Test: Erstelle einen neuen Eintrag und prüfe die Zuweisung der neuen ID.")
	void testCreateSchule() {
		final DTOEigeneSchule neu = new DTOEigeneSchule(-1L);

		final long neueId = 42L;

		when(conn.transactionGetNextID(DTOEigeneSchule.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOEigeneSchule result = repository.create(neu);

		assertNotNull(result);
		assertEquals(neueId, result.ID, "Die ID wurde nicht korrekt gesetzt.");
		verify(conn).transactionGetNextID(DTOEigeneSchule.class);
		verify(conn).transactionPersist(neu);
	}

	@Test
	@DisplayName("Test: Bestimme einen Eintrag anhand der ID.")
	void testGetById() {
		final Long id = 42L;
		final DTOEigeneSchule entity = new DTOEigeneSchule(id);

		when(conn.queryByKey(DTOEigeneSchule.class, id)).thenReturn(entity);

		final DTOEigeneSchule result = repository.getById(id);

		assertNotNull(result);
		verify(conn).queryByKey(DTOEigeneSchule.class, id);
	}


	@Test
	@DisplayName("Test: Bestimme den aktuellen Schuljahresabschnitt über den Schuleintrag.")
	void testGetIdSchuljahresabschnitt() {
		// Szenario: Die Schule befindet sich in dem Schuljahresabschnitt mit der ID 42
		final long idSchuljahresabschnitt = 42L;
		final DTOEigeneSchule schule = new DTOEigeneSchule(1L);
		schule.Schuljahresabschnitts_ID = idSchuljahresabschnitt;

		when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(schule);
		final long result = repository.getIdSchuljahresabschnitt();

		assertEquals(idSchuljahresabschnitt, result, "Die ID des Schuljahresabschnitts wurde nicht korrekt ausgelesen.");
		verify(conn).querySingle(DTOEigeneSchule.class);
	}

	@Nested
	@DisplayName("getSchulnummer()")
	class GetSchulnummer {

		@Test
		@DisplayName("gibt die Schulnummer aus dem DTO zurück")
		void getSchulnummer_returnsSchulnummer() {
			when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(dtoWithSchulnummer(123456));

			assertThat(repository.getSchulnummer()).isEqualTo(123456);
		}

		@Test
		@DisplayName("wirft RepositoryException wenn SchulNr null ist")
		void getSchulnummer_nullSchulNr_throwsRepositoryException() {
			final var dto = dtoWithSchulnummer(0);
			dto.SchulNr = null;
			when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(dto);

			assertThatThrownBy(() -> repository.getSchulnummer())
					.isInstanceOf(RepositoryException.class)
					.hasMessageContaining("SchulNr");
		}

		@Test
		@DisplayName("wirft RepositoryException wenn kein DTO vorhanden ist")
		void getSchulnummer_noDto_throwsRepositoryException() {
			when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(null);

			assertThatThrownBy(() -> repository.getSchulnummer())
					.isInstanceOf(RepositoryException.class);
		}

		private static DTOEigeneSchule dtoWithSchulnummer(final int schulNr) {
			final var dto = new DTOEigeneSchule(1L);
			dto.SchulNr = schulNr;
			dto.SchulformKuerzel = "GY";
			dto.Schuljahresabschnitts_ID = 1L;
			return dto;
		}
	}


	@Nested
	@DisplayName("getSchulform()")
	class GetSchulform {

		@Test
		@DisplayName("gibt die Schulform anhand des Kürzels zurück")
		void getSchulform_validKuerzel_returnsSchulform() {
			when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(dtoWithSchulform("GY"));

			final var result = repository.getSchulform();

			assertThat(result)
					.isNotNull()
					.isEqualTo(Schulform.data().getWertByKuerzel("GY"));
		}

		@Test
		@DisplayName("wirft RepositoryException wenn SchulformKuerzel null ist")
		void getSchulform_nullKuerzel_throwsRepositoryException() {
			final var dto = dtoWithSchulform(null);
			when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(dto);

			assertThatThrownBy(() -> repository.getSchulform())
					.isInstanceOf(RepositoryException.class)
					.hasMessageContaining("Schulform");
		}

		@Test
		@DisplayName("wirft RepositoryException wenn SchulformKuerzel kein gültiges Kürzel ist")
		void getSchulform_invalidKuerzel_throwsRepositoryException() {
			when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(dtoWithSchulform("UNGUELTIG"));

			assertThatThrownBy(() -> repository.getSchulform())
					.isInstanceOf(RepositoryException.class)
					.hasMessageContaining("gültige Schulform");
		}

		@Test
		@DisplayName("wirft RepositoryException wenn kein DTO vorhanden ist")
		void getSchulform_noDto_throwsRepositoryException() {
			when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(null);

			assertThatThrownBy(() -> repository.getSchulform())
					.isInstanceOf(RepositoryException.class);
		}

		private static DTOEigeneSchule dtoWithSchulform(final String schulformKuerzel) {
			final var dto = new DTOEigeneSchule(1L);
			dto.SchulNr = 123456;
			dto.SchulformKuerzel = schulformKuerzel;
			dto.Schuljahresabschnitts_ID = 1L;
			return dto;
		}
	}

}
