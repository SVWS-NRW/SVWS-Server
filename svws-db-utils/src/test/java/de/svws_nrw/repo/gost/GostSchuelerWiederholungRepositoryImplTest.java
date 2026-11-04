package de.svws_nrw.repo.gost;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerWiederholung;

/**
 * Prüft, dass beim Anlegen einer Wiederholung die Schüler-ID erhalten bleibt, da sie ein Fremdschlüssel auf die Schülertabelle ist.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests für das Repository der Wiederholungen")
class GostSchuelerWiederholungRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@Test
	@DisplayName("create: Die Schüler-ID bleibt erhalten")
	void testCreateBehaeltSchuelerID() {
		when(conn.transactionPersist(any())).thenReturn(true);
		final GostSchuelerWiederholungRepositoryImpl repository = new GostSchuelerWiederholungRepositoryImpl(conn);
		final DTOGostSchuelerWiederholung dto = repository.create(new DTOGostSchuelerWiederholung(1099L, 2029, GostHalbjahr.Q11));
		assertThat(dto.Schueler_ID).isEqualTo(1099L);
	}

}
