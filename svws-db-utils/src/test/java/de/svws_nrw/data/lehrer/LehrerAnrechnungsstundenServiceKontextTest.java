package de.svws_nrw.data.lehrer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAnrechnungsstunde;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.repo.lehrer.LehrerAbschnittsdatenRepository;
import de.svws_nrw.repo.lehrer.LehrerAnrechnungRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests für LehrerAnrechnungsstundenServiceKontext")
class LehrerAnrechnungsstundenServiceKontextTest {

	@Mock
	private SchuljahresabschnitteRepository repoSchuljahr;
	@Mock
	private LehrerAbschnittsdatenRepository repoAbschnitt;
	@Mock
	private LehrerAnrechnungRepository repoAnrechnung;

	@InjectMocks
	private LehrerAnrechnungsstundenServiceKontext kontext;


	private void mockSetup(final long idAnrechnungsstunde, final long idAbschnitt, final long idSchuljahresabschnitt) {
		final var anrechnungsstunde = new DTOLehrerAnrechnungsstunde(idAnrechnungsstunde, idAbschnitt);
		final var abschnitt = new DTOLehrerAbschnittsdaten(idAbschnitt, 42L, idSchuljahresabschnitt);
		final var schuljahresabschnitt = new DTOSchuljahresabschnitte(idSchuljahresabschnitt, 2024, 1);

		// Gibt jeweils die Daten zurück
		lenient().when(repoAnrechnung.findListByIds(List.of(idAnrechnungsstunde))).thenReturn(List.of(anrechnungsstunde));
		lenient().when(repoAbschnitt.findListByIds(Set.of(idAbschnitt))).thenReturn(List.of(abschnitt));
		lenient().when(repoSchuljahr.getAll()).thenReturn(List.of(schuljahresabschnitt));
	}


	@Test
	@DisplayName("of: Erstellt eine neue Instanz des Kontextes mit den Repositories")
	void of_createsInstance() {
		final long idAnrechnung = 1L;
		final long idAbschnitt = 100L;
		final long idSchuljahr = 200L;
		mockSetup(idAnrechnung, idAbschnitt, idSchuljahr);

		final var newKontext = LehrerAnrechnungsstundenServiceKontext.of(repoSchuljahr, repoAbschnitt, repoAnrechnung);

		assertThat(newKontext).isNotNull();

		final var result = newKontext.fetch(List.of(idAnrechnung));

		assertThat(result).hasSize(1);
		verify(repoAnrechnung).findListByIds(List.of(idAnrechnung));
		verify(repoAbschnitt).findListByIds(Set.of(idAbschnitt));
		verify(repoSchuljahr).getAll();
	}


	@Test
	@DisplayName("fetch: Lädt Anrechnungsstunden und kaskadiert korrekt zu Abschnitten und Schuljahren")
	void fetch() {
		mockSetup(1L, 100L, 200L);
		final var result = kontext.fetch(List.of(1L));

		assertThat(result).hasSize(1);
		assertThat(result.get(0).ID).isEqualTo(1L);
		final var abschnitt = kontext.getAbschnitt(100L);
		assertThat(abschnitt).isNotNull();
		assertThat(abschnitt.Schuljahresabschnitts_ID).isEqualTo(200L);
		assertThat(kontext.getSchuljahresabschnitt(200L)).isNotNull();

		verify(repoAnrechnung).findListByIds(anyList());
		verify(repoAbschnitt).findListByIds(anySet());
		verify(repoSchuljahr).getAll();
	}

	@Test
	@DisplayName("create: Erzeugt neue DTOs mit den IDs und fügt diese zum Cache hinzu")
	void create() {
		mockSetup(500L, 100L, 200L);
		when(repoAnrechnung.getNextID()).thenReturn(500L);

		final var patch = new LehrerAnrechnungsstundenCreateRequest();
		patch.idAbschnittsdaten = 100L;
		patch.idGrund = 310000L;
		patch.anzahl = 2.0;

		final var list = kontext.create(List.of(patch));

		assertThat(list).hasSize(1);
		assertThat(list.iterator().next().ID).isEqualTo(500L);

		verify(repoAbschnitt).findListByIds(anySet());
		verify(repoSchuljahr).getAll();
		verify(repoAnrechnung).flush();
	}


	@Test
	@DisplayName("fetchByLehrerabschnittsdatenId: Lädt über Abschnitts-ID und befüllt den Cache manuell")
	void fetchByLehrerabschnittsdatenId() {
		final long idAnrechnungsstunde = 1L;
		final long idAbschnitt = 100L;
		final long idSchuljahresabschnitt = 200L;
		mockSetup(idAnrechnungsstunde, idAbschnitt, idSchuljahresabschnitt);

		final var anrechnungsstunde = new DTOLehrerAnrechnungsstunde(idAnrechnungsstunde, idAbschnitt);
		when(repoAnrechnung.getMapByAbschnitt(List.of(idAbschnitt)))
				.thenReturn(Map.of(idAbschnitt, List.of(anrechnungsstunde)));

		final var result = kontext.fetchByLehrerabschnittsdatenId(idAbschnitt);

		assertThat(result).hasSize(1);
		assertThat(result.get(0).ID).isEqualTo(idAnrechnungsstunde);
		assertThat(kontext.getAnrechnungsstunden(idAnrechnungsstunde)).isNotNull();
	}

	@Test
	@DisplayName("persist: Delegiert den gesamten Cache-Inhalt an das Repository-Update")
	void persistEntities() {
		final long id = 1L;
		mockSetup(id, 100L, 200L);
		final var loaded = kontext.fetch(List.of(id));

		kontext.persist(loaded);

		verify(repoAnrechnung).update(loaded);
		verify(repoAnrechnung).flush();
	}

	@Test
	@DisplayName("delete: Löscht alle Entitäten, die sich aktuell im Cache befinden")
	void deleteEntities() {
		final long id = 1L;
		mockSetup(id, 100L, 200L);

		final var loaded = kontext.fetch(List.of(id));

		kontext.delete(loaded);

		verify(repoAnrechnung).delete(loaded);
	}

}
