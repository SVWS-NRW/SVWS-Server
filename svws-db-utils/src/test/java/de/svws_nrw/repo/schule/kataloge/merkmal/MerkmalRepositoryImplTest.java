package de.svws_nrw.repo.schule.kataloge.merkmal;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOMerkmale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MerkmalRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private MerkmalRepositoryImpl repository;

	@Test
	@DisplayName("kuerzelIsAlreadyUsedCreate | Kürzel existiert")
	void kuerzelIsAlreadyUsedCreate_exists() {
		final var kuerzel = "TEST";
		final var expectedQuery = "SELECT m FROM DTOMerkmale m WHERE LOWER(m.kuerzel) = LOWER(?1)";

		when(conn.existsBy(expectedQuery, DTOMerkmale.class, kuerzel)).thenReturn(true);

		final var result = repository.kuerzelIsAlreadyUsedCreate(kuerzel);

		assertThat(result).isTrue();
		verify(conn, times(1)).existsBy(expectedQuery, DTOMerkmale.class, kuerzel);
	}

	@Test
	@DisplayName("kuerzelIsAlreadyUsedCreate | Kürzel existiert nicht")
	void kuerzelIsAlreadyUsedCreate_notExists() {
		final var kuerzel = "NOTEXIST";
		final var expectedQuery = "SELECT m FROM DTOMerkmale m WHERE LOWER(m.kuerzel) = LOWER(?1)";

		when(conn.existsBy(expectedQuery, DTOMerkmale.class, kuerzel)).thenReturn(false);

		final var result = repository.kuerzelIsAlreadyUsedCreate(kuerzel);

		assertThat(result).isFalse();
		verify(conn, times(1)).existsBy(expectedQuery, DTOMerkmale.class, kuerzel);
	}

	@Test
	@DisplayName("kuerzelIsAlreadyUsedPatch | Kürzel existiert bei anderem Merkmal")
	void kuerzelIsAlreadyUsedPatch_existsInOther() {
		final var kuerzel = "TEST";
		final var id = 1L;
		final var expectedQuery = "SELECT m FROM DTOMerkmale m WHERE LOWER(m.kuerzel) = LOWER(?1) AND m.id != ?2";

		when(conn.existsBy(expectedQuery, DTOMerkmale.class, kuerzel, id)).thenReturn(true);

		final var result = repository.kuerzelIsAlreadyUsedPatch(kuerzel, id);

		assertThat(result).isTrue();
		verify(conn, times(1)).existsBy(expectedQuery, DTOMerkmale.class, kuerzel, id);
	}

	@Test
	@DisplayName("kuerzelIsAlreadyUsedPatch | Kürzel existiert beim gleichen Merkmal")
	void kuerzelIsAlreadyUsedPatch_existsInSame() {
		final var kuerzel = "TEST";
		final var id = 1L;
		final var expectedQuery = "SELECT m FROM DTOMerkmale m WHERE LOWER(m.kuerzel) = LOWER(?1) AND m.id != ?2";

		when(conn.existsBy(expectedQuery, DTOMerkmale.class, kuerzel, id)).thenReturn(false);

		final var result = repository.kuerzelIsAlreadyUsedPatch(kuerzel, id);

		assertThat(result).isFalse();
		verify(conn, times(1)).existsBy(expectedQuery, DTOMerkmale.class, kuerzel, id);
	}

	@Test
	@DisplayName("bezeichnungIsAlreadyUsedCreate | Bezeichnung existiert")
	void bezeichnungIsAlreadyUsedCreate_exists() {
		final var bezeichnung = "Test Bezeichnung";
		final var expectedQuery = "SELECT m FROM DTOMerkmale m WHERE LOWER(m.bezeichnung) = LOWER(?1)";

		when(conn.existsBy(expectedQuery, DTOMerkmale.class, bezeichnung)).thenReturn(true);

		final var result = repository.bezeichnungIsAlreadyUsedCreate(bezeichnung);

		assertThat(result).isTrue();
		verify(conn, times(1)).existsBy(expectedQuery, DTOMerkmale.class, bezeichnung);
	}

	@Test
	@DisplayName("bezeichnungIsAlreadyUsedCreate | Bezeichnung existiert nicht")
	void bezeichnungIsAlreadyUsedCreate_notExists() {
		final var bezeichnung = "Nicht vorhanden";
		final var expectedQuery = "SELECT m FROM DTOMerkmale m WHERE LOWER(m.bezeichnung) = LOWER(?1)";

		when(conn.existsBy(expectedQuery, DTOMerkmale.class, bezeichnung)).thenReturn(false);

		final var result = repository.bezeichnungIsAlreadyUsedCreate(bezeichnung);

		assertThat(result).isFalse();
		verify(conn, times(1)).existsBy(expectedQuery, DTOMerkmale.class, bezeichnung);
	}

	@Test
	@DisplayName("bezeichnungIsAlreadyUsedPatch | Bezeichnung existiert bei anderem Merkmal")
	void bezeichnungIsAlreadyUsedPatch_existsInOther() {
		final var bezeichnung = "Test Bezeichnung";
		final var id = 1L;
		final var expectedQuery = "SELECT m FROM DTOMerkmale m WHERE LOWER(m.bezeichnung) = LOWER(?1) AND m.id != ?2";

		when(conn.existsBy(expectedQuery, DTOMerkmale.class, bezeichnung, id)).thenReturn(true);

		final var result = repository.bezeichnungIsAlreadyUsedPatch(bezeichnung, id);

		assertThat(result).isTrue();
		verify(conn, times(1)).existsBy(expectedQuery, DTOMerkmale.class, bezeichnung, id);
	}

	@Test
	@DisplayName("bezeichnungIsAlreadyUsedPatch | Bezeichnung existiert beim gleichen Merkmal")
	void bezeichnungIsAlreadyUsedPatch_existsInSame() {
		final var bezeichnung = "Test Bezeichnung";
		final var id = 1L;
		final var expectedQuery = "SELECT m FROM DTOMerkmale m WHERE LOWER(m.bezeichnung) = LOWER(?1) AND m.id != ?2";

		when(conn.existsBy(expectedQuery, DTOMerkmale.class, bezeichnung, id)).thenReturn(false);

		final var result = repository.bezeichnungIsAlreadyUsedPatch(bezeichnung, id);

		assertThat(result).isFalse();
		verify(conn, times(1)).existsBy(expectedQuery, DTOMerkmale.class, bezeichnung, id);
	}

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new MerkmalRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(MerkmalRepositoryImpl.class)
				.isInstanceOf(MerkmalRepository.class);
	}
}

