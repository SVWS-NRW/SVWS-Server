package de.svws_nrw.data.lehrer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtBefaehigung;
import de.svws_nrw.repo.lehrer.LehrerPersonaldatenLehramtLehrbefaehigungenRepository;

/**
 * Tests für den Service zu Lehrbefähigungen.
 */
@ExtendWith(MockitoExtension.class)
class LehrerLehrbefaehigungServiceTest {

	@Mock
	private LehrerPersonaldatenLehramtLehrbefaehigungenRepository repo;

	@InjectMocks
	private LehrerLehrbefaehigungService service;

	@Test
	@DisplayName("Prüfe, ob getMapByLehramt bei einer leeren Map aus dem Repository auch eine leere Map zurückgibt.")
	void testGetMapByLehramtEmpty() {
		when(repo.getMapByLehramt(anyCollection())).thenReturn(Map.of());
		final var result = service.getMapByLehramt(List.of());
		assertTrue(result.isEmpty());
	}

	@Test
	@DisplayName("Prüfe, ob getMapByLehramt eine Map mit DTOs korrekt in eine Map der Core-Types umwandelt.")
	void testGetMapByLehramt() {
		// Erstelle eine Map, welche von dem Repository zurückgegeben wird
		final long id = 42L;
		final long idLehramt = 47L;
		final long idLehrbefaehigung = 88L;
		final long idAnerkennung = 33L;
		final var dto = new DTOLehrerPersonaldatenLehramtBefaehigung(id, idLehramt, idLehrbefaehigung);
		dto.LehrbefaehigungAnerkennung_Katalog_ID = idAnerkennung;
		final Map<Long, List<DTOLehrerPersonaldatenLehramtBefaehigung>> repoMap = Map.of(idLehramt, List.of(dto));
		when(repo.getMapByLehramt(anyCollection())).thenReturn(repoMap);

		final var result = service.getMapByLehramt(List.of(idLehramt));

		assertThat(result).containsKey(idLehramt);
		assertThat(result.get(idLehramt)).hasSize(1).first().satisfies(core -> {
			assertThat(core.id).isEqualTo(id);
			assertThat(core.idLehrbefaehigung).isEqualTo(idLehrbefaehigung);
			assertThat(core.idAnerkennungsgrund).isEqualTo(idAnerkennung);
		});
	}

}
