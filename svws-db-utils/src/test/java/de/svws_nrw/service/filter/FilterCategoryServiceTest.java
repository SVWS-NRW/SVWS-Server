package de.svws_nrw.service.filter;

import java.util.List;

import de.svws_nrw.core.data.filter.FilterCategoryResponse;
import de.svws_nrw.core.types.filter.FilterDomain;
import de.svws_nrw.mapper.filter.FilterCategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FilterCategoryService")
class FilterCategoryServiceTest {

	@Mock
	private FilterRegistry filterRegistry;

	private final FilterCategoryMapper mapper = FilterCategoryMapper.INSTANCE;

	private FilterCategoryService cut;

	@BeforeEach
	void setUp() {
		cut = new FilterCategoryService(filterRegistry, mapper);
	}

	@Nested
	@DisplayName("Konstruktor")
	class Constructor {

		@Test
		@DisplayName("wirft NullPointerException, wenn die FilterRegistry null ist")
		void registryNull() {
			assertThatThrownBy(() -> new FilterCategoryService(null, mapper))
					.isInstanceOf(NullPointerException.class)
					.hasMessageContaining("FilterRegistry");
		}

		@Test
		@DisplayName("wirft NullPointerException, wenn der Mapper null ist")
		void mapperNull() {
			assertThatThrownBy(() -> new FilterCategoryService(filterRegistry, null))
					.isInstanceOf(NullPointerException.class)
					.hasMessageContaining("FilterCategoryMapper");
		}
	}

	@Nested
	@DisplayName("getByDomain")
	class GetByDomain {

		@Test
		@DisplayName("mappt alle Kategorien der Registry vollständig und in der Reihenfolge der Registry")
		void mapsAllCategoriesInOrder() {
			final FilterCategory first = new FilterCategory(new FilterCategoryKey("schueler.erste"), "Erste", 1,
					List.of(new FilterAttributeKey("schueler.status"), new FilterAttributeKey("schueler.geschlecht")));
			final FilterCategory second = new FilterCategory(new FilterCategoryKey("schueler.zweite"), "Zweite", 2, List.of());
			when(filterRegistry.getCategoriesByDomain(FilterDomain.SCHUELER)).thenReturn(List.of(first, second));

			final List<FilterCategoryResponse> result = cut.getByDomain(FilterDomain.SCHUELER);

			assertThat(result)
					.extracting(response -> response.key, response -> response.label, response -> response.order, response -> response.attributes)
					.containsExactly(
							tuple("schueler.erste", "Erste", 1, List.of("schueler.status", "schueler.geschlecht")),
							tuple("schueler.zweite", "Zweite", 2, List.of()));
		}

		@Test
		@DisplayName("liefert eine leere Liste, wenn die Domäne keine Kategorien hat")
		void emptyRegistryResult() {
			when(filterRegistry.getCategoriesByDomain(FilterDomain.SCHUELER)).thenReturn(List.of());

			assertThat(cut.getByDomain(FilterDomain.SCHUELER)).isEmpty();
		}

		@Test
		@DisplayName("fragt die Registry mit der übergebenen Domäne ab")
		void delegatesDomainToRegistry() {
			when(filterRegistry.getCategoriesByDomain(FilterDomain.SCHUELER)).thenReturn(List.of());

			cut.getByDomain(FilterDomain.SCHUELER);

			verify(filterRegistry).getCategoriesByDomain(FilterDomain.SCHUELER);
		}

		@Test
		@DisplayName("liefert mit der Default-Registry die Kategorie Individualdaten")
		void integrationWithDefaults() {
			final FilterCategoryService service = new FilterCategoryService(FilterRegistry.defaults(), mapper);

			final List<FilterCategoryResponse> result = service.getByDomain(FilterDomain.SCHUELER);

			assertThat(result)
					.extracting(response -> response.key, response -> response.label, response -> response.order, response -> response.attributes)
					.containsExactly(
							tuple("schueler.individualdaten", "Individualdaten", 1, List.of("schueler.status", "schueler.geschlecht", "schueler.wohnort")));
		}
	}
}
