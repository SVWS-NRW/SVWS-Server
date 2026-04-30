package de.svws_nrw.repo.benutzer;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppenMitglied;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BenutzergruppenMitgliedRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private BenutzergruppenMitgliedRepositoryImpl cut;

	@Test
	@DisplayName("findByBenutzergruppeId | Gibt alle Mitglieder der Gruppe zurueck")
	void findByBenutzergruppeId() {
		final var mitglied1 = buildMitglied(10L, 1L);
		final var mitglied2 = buildMitglied(10L, 2L);
		when(conn.queryList(DTOBenutzergruppenMitglied.QUERY_BY_GRUPPE_ID, DTOBenutzergruppenMitglied.class, 10L))
				.thenReturn(List.of(mitglied1, mitglied2));

		final var result = cut.findByBenutzergruppeId(10L);

		assertThat(result).containsExactlyInAnyOrder(mitglied1, mitglied2);
	}

	@Test
	@DisplayName("findByBenutzerId | Gibt alle Gruppenmitgliedschaften des Benutzers zurueck")
	void findByBenutzerId() {
		final var mitglied1 = buildMitglied(10L, 42L);
		final var mitglied2 = buildMitglied(20L, 42L);
		when(conn.queryList(DTOBenutzergruppenMitglied.QUERY_BY_BENUTZER_ID, DTOBenutzergruppenMitglied.class, 42L))
				.thenReturn(List.of(mitglied1, mitglied2));

		final var result = cut.findByBenutzerId(42L);

		assertThat(result).containsExactlyInAnyOrder(mitglied1, mitglied2);
	}

	@Test
	@DisplayName("hasGroupRights | Success")
	void hasGroupRights() {
		final var idBenutzer = 1L;
		final var idBenutzerGruppe = 2L;
		when(conn.existsBy(DTOBenutzergruppenMitglied.QUERY_PK, DTOBenutzergruppenMitglied.class, idBenutzerGruppe, idBenutzer)).thenReturn(true);

		final var result = cut.hasGroupRights(idBenutzer, idBenutzerGruppe);

		assertThat(result).isTrue();
	}

	private static DTOBenutzergruppenMitglied buildMitglied(final long gruppeId, final long benutzerId) {
		final var mitglied = new DTOBenutzergruppenMitglied(gruppeId, benutzerId);
		mitglied.Gruppe_ID = gruppeId;
		mitglied.Benutzer_ID = benutzerId;
		return mitglied;
	}
}
