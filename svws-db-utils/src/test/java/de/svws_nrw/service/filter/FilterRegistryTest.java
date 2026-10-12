package de.svws_nrw.service.filter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.types.filter.FilterDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("FilterRegistry")
class FilterRegistryTest {

	private static final FilterCategoryKey KEY_CATEGORY = new FilterCategoryKey("schueler.test");
	private static final FilterAttributeKey KEY_ATTRIBUTE = new FilterAttributeKey("schueler.testattribut");

	private static FilterCategory category(final FilterCategoryKey key) {
		return new FilterCategory(key, "Test", 1, List.of(KEY_ATTRIBUTE));
	}

	private static FilterAttribute attribute() {
		return new EnumFilterAttribute(KEY_ATTRIBUTE, "Testattribut", "TestEnum", KEY_CATEGORY, AttributeTarget.of("Entity", "property"));
	}

	@Nested
	@DisplayName("Konstruktor")
	class Constructor {

		@Test
		@DisplayName("wirft NullPointerException, wenn die Kategorien-Map null ist")
		void categoriesNull() {
			final Map<FilterCategoryKey, List<FilterAttribute>> attributes = Map.of();

			assertThatThrownBy(() -> new FilterRegistry(null, attributes))
					.isInstanceOf(NullPointerException.class)
					.hasMessageContaining("Filterkategorien je Domäne");
		}

		@Test
		@DisplayName("wirft NullPointerException, wenn die Attribute-Map null ist")
		void attributesNull() {
			final Map<FilterDomain, List<FilterCategory>> categories = Map.of();

			assertThatThrownBy(() -> new FilterRegistry(categories, null))
					.isInstanceOf(NullPointerException.class)
					.hasMessageContaining("Filterattribute je Kategorie");
		}

		@Test
		@DisplayName("wirft NullPointerException, wenn die Kategorienliste einer Domäne null ist")
		void categoryListNull() {
			final Map<FilterDomain, List<FilterCategory>> categories = new EnumMap<>(FilterDomain.class);
			categories.put(FilterDomain.SCHUELER, null);

			assertThatThrownBy(() -> new FilterRegistry(categories, Map.of()))
					.isInstanceOf(NullPointerException.class)
					.hasMessageContaining(FilterDomain.SCHUELER.toString());
		}

		@Test
		@DisplayName("wirft NullPointerException, wenn der Kategorie-Schlüssel null ist")
		void attributeKeyNull() {
			final Map<FilterCategoryKey, List<FilterAttribute>> attributes = new HashMap<>();
			attributes.put(null, List.of());

			assertThatThrownBy(() -> new FilterRegistry(Map.of(), attributes))
					.isInstanceOf(NullPointerException.class)
					.hasMessageContaining("Schlüssel der Filterkategorie");
		}

		@Test
		@DisplayName("wirft NullPointerException, wenn die Attributliste einer Kategorie null ist")
		void attributeListNull() {
			final Map<FilterCategoryKey, List<FilterAttribute>> attributes = new HashMap<>();
			attributes.put(KEY_CATEGORY, null);

			assertThatThrownBy(() -> new FilterRegistry(Map.of(), attributes))
					.isInstanceOf(NullPointerException.class)
					.hasMessageContaining(KEY_CATEGORY.toString());
		}

		@Test
		@DisplayName("legt defensive Kopien an: spätere Änderungen an der übergebenen Konfiguration wirken sich nicht aus")
		void copiesConfiguration() {
			final List<FilterCategory> categoryList = new ArrayList<>(List.of(category(KEY_CATEGORY)));
			final Map<FilterDomain, List<FilterCategory>> categories = new EnumMap<>(FilterDomain.class);
			categories.put(FilterDomain.SCHUELER, categoryList);
			final FilterRegistry cut = new FilterRegistry(categories, Map.of(KEY_CATEGORY, List.of(attribute())));

			categoryList.clear();
			categories.clear();

			assertThat(cut.getCategoriesByDomain(FilterDomain.SCHUELER)).hasSize(1);
		}
	}

	@Nested
	@DisplayName("getCategoriesByDomain")
	class GetCategoriesByDomain {

		@Test
		@DisplayName("liefert die konfigurierten Kategorien der Domäne")
		void returnsConfiguredCategories() {
			final FilterCategory category = category(KEY_CATEGORY);
			final FilterRegistry cut = new FilterRegistry(Map.of(FilterDomain.SCHUELER, List.of(category)), Map.of());

			assertThat(cut.getCategoriesByDomain(FilterDomain.SCHUELER)).containsExactly(category);
		}

		@Test
		@DisplayName("liefert eine leere Liste, wenn für die Domäne nichts konfiguriert ist")
		void unknownDomainReturnsEmptyList() {
			final FilterRegistry cut = new FilterRegistry(Map.of(), Map.of());

			assertThat(cut.getCategoriesByDomain(FilterDomain.SCHUELER)).isEmpty();
		}

		@Test
		@DisplayName("liefert eine leere Liste, wenn die Domäne null ist")
		void nullDomainReturnsEmptyList() {
			final FilterRegistry cut = new FilterRegistry(Map.of(FilterDomain.SCHUELER, List.of(category(KEY_CATEGORY))), Map.of());

			assertThat(cut.getCategoriesByDomain(null)).isEmpty();
		}

		@Test
		@DisplayName("liefert eine unveränderliche Liste")
		void returnedListIsImmutable() {
			final FilterRegistry cut = new FilterRegistry(Map.of(FilterDomain.SCHUELER, List.of(category(KEY_CATEGORY))), Map.of());
			final List<FilterCategory> result = cut.getCategoriesByDomain(FilterDomain.SCHUELER);
			final FilterCategory other = category(new FilterCategoryKey("schueler.andere"));

			assertThatThrownBy(() -> result.add(other)).isInstanceOf(UnsupportedOperationException.class);
		}
	}

	@Nested
	@DisplayName("defaults")
	class Defaults {

		@Test
		@DisplayName("liefert immer dieselbe Instanz")
		void isSingleton() {
			final FilterRegistry first = FilterRegistry.defaults();
			assertThat(first).isSameAs(FilterRegistry.defaults());
		}

		@Test
		@DisplayName("enthält für SCHUELER die Kategorie Individualdaten mit Status, Geschlecht und Wohnort")
		void containsSchuelerIndividualdaten() {
			final List<FilterCategory> result = FilterRegistry.defaults().getCategoriesByDomain(FilterDomain.SCHUELER);

			assertThat(result).hasSize(1);
			final FilterCategory category = result.getFirst();
			assertThat(category.key()).isEqualTo(FilterCategoryKeys.SCHUELER_INDIVIDUALDATEN);
			assertThat(category.label()).isEqualTo("Individualdaten");
			assertThat(category.order()).isEqualTo(1);
			assertThat(category.attributes()).containsExactly(
					FilterAttributeKeys.SCHUELER_STATUS,
					FilterAttributeKeys.SCHUELER_GESCHLECHT,
					FilterAttributeKeys.SCHUELER_WOHNORT);
		}
	}
}
