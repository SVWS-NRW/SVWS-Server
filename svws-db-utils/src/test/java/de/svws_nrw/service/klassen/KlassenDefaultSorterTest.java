package de.svws_nrw.service.klassen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import jakarta.ws.rs.core.Response;

/**
 * Test für die Klasse {@link KlassenDefaultSorter}
 */
@ExtendWith(MockitoExtension.class)
class KlassenDefaultSorterTest {

	private static final long ID_ABSCHNITT = 11L;
	private static final long ID_JAHRGANG_5 = 5L;
	private static final long ID_JAHRGANG_6 = 6L;
	private static final long ID_JAHRGANG_OHNE_SORTIERUNG = 7L;

	@Mock
	private KlassenRepository klassenRepository;

	@Mock
	private JahrgangRepository jahrgangRepository;

	private KlassenDefaultSorter sorter;

	@BeforeEach
	void setUp() {
		sorter = new KlassenDefaultSorter(klassenRepository, jahrgangRepository);
	}

	private static DTOJahrgang createJahrgang(final long id, final Integer sortierung) {
		final var jahrgang = new DTOJahrgang(id);
		jahrgang.Sortierung = sortierung;

		return jahrgang;
	}

	private static DTOKlassen createKlasse(final long id, final Long idJahrgang, final String asdKlasse) {
		final var klasse = new DTOKlassen(id, ID_ABSCHNITT, "k" + id, false, false);
		klasse.Jahrgang_ID = idJahrgang;
		klasse.ASDKlasse = asdKlasse;

		return klasse;
	}

	private void stubJahrgaenge() {
		when(jahrgangRepository.getMap()).thenReturn(Map.of(
				ID_JAHRGANG_5, createJahrgang(ID_JAHRGANG_5, 1),
				ID_JAHRGANG_6, createJahrgang(ID_JAHRGANG_6, 2),
				ID_JAHRGANG_OHNE_SORTIERUNG, createJahrgang(ID_JAHRGANG_OHNE_SORTIERUNG, null)));
	}

	@Test
	@DisplayName("Die Klassen werden nach Jahrgang und Parallelität sortiert, Klassen ohne Jahrgangssortierung stehen am Ende")
	void sortiertNachJahrgangUndParallelitaet() {
		stubJahrgaenge();
		final var k6b = createKlasse(1L, ID_JAHRGANG_6, "06B");
		final var k5aa = createKlasse(2L, ID_JAHRGANG_5, "05AA");
		final var k5b = createKlasse(3L, ID_JAHRGANG_5, "05b");
		final var ju = createKlasse(4L, null, "JU");
		final var k5a = createKlasse(5L, ID_JAHRGANG_5, "05A");
		final var ohneSortierung = createKlasse(6L, ID_JAHRGANG_OHNE_SORTIERUNG, "07A");
		final var ohneAsdKuerzel = createKlasse(7L, ID_JAHRGANG_5, null);
		when(klassenRepository.getListBySchuljahresabschnitt(ID_ABSCHNITT))
				.thenReturn(List.of(k6b, k5aa, k5b, ju, k5a, ohneSortierung, ohneAsdKuerzel));

		sorter.apply(ID_ABSCHNITT);

		assertThat(List.of(ohneAsdKuerzel, k5a, k5b, k5aa, k6b, ju, ohneSortierung))
				.extracting(k -> k.Sortierung)
				.containsExactly(1, 2, 3, 4, 5, 6, 7);
		verify(klassenRepository).update(ArgumentMatchers.<List<DTOKlassen>>any());
	}

	@Test
	@DisplayName("Klassen mit einem Jahrgang, der nicht im Katalog steht, stehen am Ende")
	void unbekannterJahrgangStehtAmEnde() {
		stubJahrgaenge();
		final var unbekannt = createKlasse(1L, 99L, "99A");
		final var k5a = createKlasse(2L, ID_JAHRGANG_5, "05A");
		when(klassenRepository.getListBySchuljahresabschnitt(ID_ABSCHNITT)).thenReturn(List.of(unbekannt, k5a));

		sorter.apply(ID_ABSCHNITT);

		assertThat(List.of(k5a, unbekannt))
				.extracting(k -> k.Sortierung)
				.containsExactly(1, 2);
	}

	@Test
	@DisplayName("Ohne Jahrgänge wird mit 404 abgelehnt")
	void keineJahrgaenge() {
		when(jahrgangRepository.getMap()).thenReturn(Map.of());

		assertThatThrownBy(() -> sorter.apply(ID_ABSCHNITT))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Response.Status.NOT_FOUND));

		verify(klassenRepository, never()).update(ArgumentMatchers.<List<DTOKlassen>>any());
	}

	@Test
	@DisplayName("Ohne Klassen im Schuljahresabschnitt wird nichts geändert")
	void keineKlassen() {
		stubJahrgaenge();
		when(klassenRepository.getListBySchuljahresabschnitt(ID_ABSCHNITT)).thenReturn(List.of());

		sorter.apply(ID_ABSCHNITT);

		verify(klassenRepository, never()).update(ArgumentMatchers.<List<DTOKlassen>>any());
	}

}
