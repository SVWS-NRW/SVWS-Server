package de.svws_nrw.mapper.filter;

import java.util.List;

import de.svws_nrw.core.data.filter.FilterCategoryResponse;
import de.svws_nrw.service.filter.FilterAttributeKey;
import de.svws_nrw.service.filter.FilterCategory;
import de.svws_nrw.service.filter.FilterCategoryKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FilterCategoryMapperTest {

	private final FilterCategoryMapper mapper = FilterCategoryMapper.INSTANCE;

	@Test
	@DisplayName("toApi: Schlüssel, Anzeigename, Reihenfolge und Attribute werden übernommen")
	void toApi_mapsAllFields() {
		final FilterCategory domain = new FilterCategory(
				new FilterCategoryKey("schueler.individualdaten"),
				"Individualdaten",
				3,
				List.of(new FilterAttributeKey("schueler.status"), new FilterAttributeKey("schueler.geschlecht")));

		final FilterCategoryResponse result = mapper.toApi(domain);

		assertThat(result.key).isEqualTo("schueler.individualdaten");
		assertThat(result.label).isEqualTo("Individualdaten");
		assertThat(result.order).isEqualTo(3);
		assertThat(result.attributes).containsExactly("schueler.status", "schueler.geschlecht");
	}

}
