package de.svws_nrw.service.filter;

import java.util.List;

import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.mapper.schule.katalog.ort.OrtMapper;
import de.svws_nrw.repo.schule.kataloge.ort.OrtRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrtFilterCatalogSource")
class OrtFilterCatalogSourceTest {

	@Mock
	private OrtRepository repository;

	private final OrtMapper mapper = OrtMapper.INSTANCE;

	private OrtFilterCatalogSource cut;

	@BeforeEach
	void setUp() {
		cut = new OrtFilterCatalogSource(repository, mapper);
	}

	@Test
	@DisplayName("Konstruktor | wirft NullPointerException, wenn Repository oder Mapper null sind")
	void constructorNull() {
		assertThatThrownBy(() -> new OrtFilterCatalogSource(null, mapper))
				.isInstanceOf(NullPointerException.class)
				.hasMessageContaining("OrtRepository");
		assertThatThrownBy(() -> new OrtFilterCatalogSource(repository, null))
				.isInstanceOf(NullPointerException.class)
				.hasMessageContaining("OrtMapper");
	}

	@Test
	@DisplayName("getCatalog | liefert den Katalog der Orte")
	void getCatalog() {
		assertThat(cut.getCatalog()).isSameAs(FilterCatalog.ORTE);
	}

	@Test
	@DisplayName("loadValues | liefert alle Orte als Werte, sortiert nach Bezeichnung")
	void loadValuesSorted() {
		when(repository.getAll()).thenReturn(List.of(
				new DTOOrt(2L, "53840", "Troisdorf"),
				new DTOOrt(3L, "44649", "Herne"),
				new DTOOrt(1L, "44649", "Ärgershausen")));

		final var result = cut.loadValues();

		assertThat(result)
				.extracting(value -> value.id, value -> value.bezeichnung)
				.containsExactly(
						tuple(1L, "44649 Ärgershausen"),
						tuple(3L, "44649 Herne"),
						tuple(2L, "53840 Troisdorf"));
	}

	@Test
	@DisplayName("loadValues | liefert eine leere Liste, wenn es keine Orte gibt")
	void loadValuesEmpty() {
		when(repository.getAll()).thenReturn(List.of());

		assertThat(cut.loadValues()).isEmpty();
	}

	@Test
	@DisplayName("loadValues | liefert auch nicht sichtbare Orte, weil sie in vorhandenen Daten vorkommen können")
	void loadValuesIncludesInvisible() {
		final var unsichtbar = new DTOOrt(5L, "12345", "Altstadt");
		unsichtbar.istSichtbar = false;
		final var sichtbar = new DTOOrt(6L, "12345", "Neustadt");
		sichtbar.istSichtbar = true;
		when(repository.getAll()).thenReturn(List.of(sichtbar, unsichtbar));

		final var result = cut.loadValues();

		assertThat(result)
				.extracting(value -> value.id)
				.containsExactly(5L, 6L);
	}

	@Test
	@DisplayName("loadValues | lädt die Orte bei jedem Aufruf genau einmal aus dem Repository")
	void loadValuesAccessesRepositoryOnce() {
		when(repository.getAll()).thenReturn(List.of());

		cut.loadValues();

		verify(repository, times(1)).getAll();
		verifyNoMoreInteractions(repository);
	}

	@Test
	@DisplayName("loadValues | reicht eine Exception des Repositories weiter")
	void loadValuesPropagatesRepositoryError() {
		final var error = new IllegalStateException("kein Zugriff");
		when(repository.getAll()).thenThrow(error);

		assertThatThrownBy(() -> cut.loadValues()).isSameAs(error);
	}
}
