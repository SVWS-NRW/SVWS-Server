package de.svws_nrw.service.filter;

import de.svws_nrw.core.types.filter.FilterAttributeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("FilterAttribute-Implementierungen")
class FilterAttributeTest {

	private static final FilterAttributeKey KEY = new FilterAttributeKey("schueler.testattribut");
	private static final FilterCategoryKey KEY_CATEGORY = new FilterCategoryKey("schueler.test");
	private static final AttributeTarget TARGET = AttributeTarget.of("Entity", "property");

	@Test
	@DisplayName("EnumFilterAttribute | liefert den Typ ENUM und den Namen des Enums als Quelle")
	void enumFilterAttribute() {
		final var cut = new EnumFilterAttribute(KEY, "Test", "TestEnum", KEY_CATEGORY, TARGET);

		assertThat(cut)
				.returns(FilterAttributeType.ENUM, FilterAttribute::getType)
				.returns("TestEnum", FilterAttribute::getSource)
				.returns("TestEnum", EnumFilterAttribute::getEnumName);
	}

	@Test
	@DisplayName("CoreTypeFilterAttribute | liefert den Typ CORETYPE und den Namen des CoreTypes als Quelle")
	void coreTypeFilterAttribute() {
		final var cut = new CoreTypeFilterAttribute(KEY, "Test", "TestCoreType", KEY_CATEGORY, TARGET);

		assertThat(cut)
				.returns(FilterAttributeType.CORETYPE, FilterAttribute::getType)
				.returns("TestCoreType", FilterAttribute::getSource)
				.returns("TestCoreType", CoreTypeFilterAttribute::getCoreTypeName);
	}

	@Test
	@DisplayName("CatalogFilterAttribute | liefert den Typ CATALOG und den Namen des Katalogs als Quelle")
	void catalogFilterAttribute() {
		final var cut = new CatalogFilterAttribute(KEY, "Test", FilterCatalog.ORTE, KEY_CATEGORY, TARGET);

		assertThat(cut)
				.returns(FilterAttributeType.CATALOG, FilterAttribute::getType)
				.returns("Orte", FilterAttribute::getSource)
				.returns(FilterCatalog.ORTE, CatalogFilterAttribute::getCatalog);
	}

	@Test
	@DisplayName("CatalogFilterAttribute | wirft NullPointerException, wenn der Katalog null ist")
	void catalogFilterAttributeWithoutCatalog() {
		assertThatThrownBy(() -> new CatalogFilterAttribute(KEY, "Test", null, KEY_CATEGORY, TARGET))
				.isInstanceOf(NullPointerException.class)
				.hasMessageContaining("Katalog");
	}
}
