package de.svws_nrw.mapper.filter;

import de.svws_nrw.service.filter.AttributeTarget;
import de.svws_nrw.service.filter.CatalogFilterAttribute;
import de.svws_nrw.service.filter.CoreTypeFilterAttribute;
import de.svws_nrw.service.filter.EnumFilterAttribute;
import de.svws_nrw.service.filter.FilterAttribute;
import de.svws_nrw.service.filter.FilterAttributeKey;
import de.svws_nrw.service.filter.FilterCategoryKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FilterAttributeMapperTest {

	private static final FilterAttributeKey KEY = new FilterAttributeKey("schueler.testattribut");
	private static final FilterCategoryKey KEY_CATEGORY = new FilterCategoryKey("schueler.individualdaten");
	private static final AttributeTarget TARGET = AttributeTarget.of("Entity", "property");

	private final FilterAttributeMapper mapper = FilterAttributeMapper.INSTANCE;

	@Test
	@DisplayName("toApi: Enum-Attribut übernimmt Schlüssel, Anzeigename, Kategorie sowie Typ und Quelle")
	void toApi_mapsEnumAttribute() {
		final FilterAttribute domain = new EnumFilterAttribute(KEY, "Geschlecht", "Geschlecht", KEY_CATEGORY, TARGET);

		final var result = mapper.toApi(domain);

		assertThat(result)
				.extracting(r -> r.key, r -> r.label, r -> r.keyCategory, r -> r.type, r -> r.source)
				.containsExactly("schueler.testattribut", "Geschlecht", "schueler.individualdaten", "enum", "Geschlecht");
	}

	@Test
	@DisplayName("toApi: CoreType-Attribut übernimmt Typ coretype und den Namen des CoreTypes als Quelle")
	void toApi_mapsCoreTypeAttribute() {
		final FilterAttribute domain = new CoreTypeFilterAttribute(KEY, "Status", "SchuelerStatus", KEY_CATEGORY, TARGET);

		final var result = mapper.toApi(domain);

		assertThat(result)
				.extracting(r -> r.type, r -> r.source)
				.containsExactly("coretype", "SchuelerStatus");
	}

	@Test
	@DisplayName("toApi: Katalog-Attribut übernimmt Typ catalog und den Namen des Katalogs als Quelle")
	void toApi_mapsCatalogAttribute() {
		final FilterAttribute domain = new CatalogFilterAttribute(KEY, "Ort", "Orte", KEY_CATEGORY, TARGET);

		final var result = mapper.toApi(domain);

		assertThat(result)
				.extracting(r -> r.type, r -> r.source)
				.containsExactly("catalog", "Orte");
	}

	@Test
	@DisplayName("toApi: gibt null zurück, wenn das Attribut null ist")
	void toApi_nullReturnsNull() {
		assertThat(mapper.toApi(null)).isNull();
	}
}
