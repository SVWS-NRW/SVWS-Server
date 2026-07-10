package de.svws_nrw.repo.schule.kataloge.fachklasse;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOFachklassen;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
class FachklasseRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private FachklasseRepositoryImpl repository;

	// -------------------------------------------------------------------------
	// Hilfskonstanten
	// -------------------------------------------------------------------------

	private static final String QUERY_KUERZEL_CREATE =
			"SELECT m FROM DTOFachklassen m WHERE LOWER(m.kuerzel) = LOWER(?1)";
	private static final String QUERY_KUERZEL_PATCH =
			"SELECT m FROM DTOFachklassen m WHERE LOWER(m.kuerzel) = LOWER(?1) AND m.id != ?2";
	private static final String QUERY_BEZEICHNUNG_CREATE =
			"SELECT m FROM DTOFachklassen m WHERE LOWER(m.bezeichnung) = LOWER(?1)";
	private static final String QUERY_BEZEICHNUNG_PATCH =
			"SELECT m FROM DTOFachklassen m WHERE LOWER(m.bezeichnung) = LOWER(?1) AND m.id != ?2";

	// -------------------------------------------------------------------------
	// kuerzelIsAlreadyUsedCreate
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("kuerzelIsAlreadyUsedCreate")
	class KuerzelIsAlreadyUsedCreate {

		@Test
		@DisplayName("Liefert true wenn Kürzel bereits existiert")
		void kuerzelIsAlreadyUsedCreate_exists() {
			final var kuerzel = "BK-IT";
			when(conn.existsBy(QUERY_KUERZEL_CREATE, DTOFachklassen.class, kuerzel)).thenReturn(true);

			final var result = repository.kuerzelIsAlreadyUsedCreate(kuerzel);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_KUERZEL_CREATE, DTOFachklassen.class, kuerzel);
		}

		@Test
		@DisplayName("Liefert false wenn Kürzel nicht existiert")
		void kuerzelIsAlreadyUsedCreate_notExists() {
			final var kuerzel = "NOTEXIST";
			when(conn.existsBy(QUERY_KUERZEL_CREATE, DTOFachklassen.class, kuerzel)).thenReturn(false);

			final var result = repository.kuerzelIsAlreadyUsedCreate(kuerzel);

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(QUERY_KUERZEL_CREATE, DTOFachklassen.class, kuerzel);
		}
	}

	// -------------------------------------------------------------------------
	// kuerzelIsAlreadyUsedPatch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("kuerzelIsAlreadyUsedPatch")
	class KuerzelIsAlreadyUsedPatch {

		@Test
		@DisplayName("Liefert true wenn Kürzel bei einem anderen Eintrag existiert")
		void kuerzelIsAlreadyUsedPatch_existsInOther() {
			final var kuerzel = "BK-IT";
			final var id = 1L;
			when(conn.existsBy(QUERY_KUERZEL_PATCH, DTOFachklassen.class, kuerzel, id)).thenReturn(true);

			final var result = repository.kuerzelIsAlreadyUsedPatch(kuerzel, id);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_KUERZEL_PATCH, DTOFachklassen.class, kuerzel, id);
		}

		@Test
		@DisplayName("Liefert false wenn Kürzel nur beim eigenen Eintrag existiert")
		void kuerzelIsAlreadyUsedPatch_existsOnlyInSelf() {
			final var kuerzel = "BK-IT";
			final var id = 1L;
			when(conn.existsBy(QUERY_KUERZEL_PATCH, DTOFachklassen.class, kuerzel, id)).thenReturn(false);

			final var result = repository.kuerzelIsAlreadyUsedPatch(kuerzel, id);

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(QUERY_KUERZEL_PATCH, DTOFachklassen.class, kuerzel, id);
		}
	}

	// -------------------------------------------------------------------------
	// bezeichnungIsAlreadyUsedCreate
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("bezeichnungIsAlreadyUsedCreate")
	class BezeichnungIsAlreadyUsedCreate {

		@Test
		@DisplayName("Liefert true wenn Bezeichnung bereits existiert")
		void bezeichnungIsAlreadyUsedCreate_exists() {
			final var bezeichnung = "Elektrotechnik";
			when(conn.existsBy(QUERY_BEZEICHNUNG_CREATE, DTOFachklassen.class, bezeichnung)).thenReturn(true);

			final var result = repository.bezeichnungIsAlreadyUsedCreate(bezeichnung);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_BEZEICHNUNG_CREATE, DTOFachklassen.class, bezeichnung);
		}

		@Test
		@DisplayName("Liefert false wenn Bezeichnung nicht existiert")
		void bezeichnungIsAlreadyUsedCreate_notExists() {
			final var bezeichnung = "Unbekannte Klasse";
			when(conn.existsBy(QUERY_BEZEICHNUNG_CREATE, DTOFachklassen.class, bezeichnung)).thenReturn(false);

			final var result = repository.bezeichnungIsAlreadyUsedCreate(bezeichnung);

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(QUERY_BEZEICHNUNG_CREATE, DTOFachklassen.class, bezeichnung);
		}
	}

	// -------------------------------------------------------------------------
	// bezeichnungIsAlreadyUsedPatch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("bezeichnungIsAlreadyUsedPatch")
	class BezeichnungIsAlreadyUsedPatch {

		@Test
		@DisplayName("Liefert true wenn Bezeichnung bei einem anderen Eintrag existiert")
		void bezeichnungIsAlreadyUsedPatch_existsInOther() {
			final var bezeichnung = "Elektrotechnik";
			final var id = 1L;
			when(conn.existsBy(QUERY_BEZEICHNUNG_PATCH, DTOFachklassen.class, bezeichnung, id)).thenReturn(true);

			final var result = repository.bezeichnungIsAlreadyUsedPatch(bezeichnung, id);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_BEZEICHNUNG_PATCH, DTOFachklassen.class, bezeichnung, id);
		}

		@Test
		@DisplayName("Liefert false wenn Bezeichnung nur beim eigenen Eintrag existiert")
		void bezeichnungIsAlreadyUsedPatch_existsOnlyInSelf() {
			final var bezeichnung = "Elektrotechnik";
			final var id = 1L;
			when(conn.existsBy(QUERY_BEZEICHNUNG_PATCH, DTOFachklassen.class, bezeichnung, id)).thenReturn(false);

			final var result = repository.bezeichnungIsAlreadyUsedPatch(bezeichnung, id);

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(QUERY_BEZEICHNUNG_PATCH, DTOFachklassen.class, bezeichnung, id);
		}
	}

	// -------------------------------------------------------------------------
	// Konstruktor
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new FachklasseRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(FachklasseRepositoryImpl.class)
				.isInstanceOf(FachklasseRepository.class);
	}
}
